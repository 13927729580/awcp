package cn.org.awcp.venson.service.impl;

import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.extend.formdesigner.DocumentUtils;
import cn.org.awcp.formdesigner.core.domain.Attachment;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.util.MD5Util;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.ContentStream;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Date;

import static cn.org.awcp.solr.service.SolrService.IContentStream;

@Service("IFileService")
@Transactional(rollbackFor = {Exception.class})
public class FileServiceImpl implements FileService {
    /**
     * 日志对象
     */
    private static final Log logger = LogFactory.getLog(FileServiceImpl.class);

    private static final String MONGO_DB_NAME="myFiles";


    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private HttpSolrClient httpSolrClient;


    @Override
    public String save(byte[] input, String fileType, String fileName, int type) {
        return save(input, fileType, fileName, type, false);
    }

    @Override
    public String save(byte[] input, String fileType, String fileName, int type, boolean isIndex) {
        if (input == null) {
            return null;
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            String uuid = MD5Util.getFileMD5String(input);
            AttachmentVO vo = this.get(uuid);
            Attachment att;
            // 判断附件在数据库中是否已经存在
            if (vo != null) {
                // 判断文件流是否存在
                InputStream inputStream = this.getInputStream(vo);
                // 如果存在则直接返回
                if (inputStream != null) {
                    return uuid;
                } else {
                    // 不存在则移除数据库数据
                    att = new Attachment();
                    att.setId(vo.getId());
                    att.remove();
                }
            } else {
                att = new Attachment();
            }
            in = new BufferedInputStream(new ByteArrayInputStream(input));
            // 文件信息保存到附件表
            PunUserBaseInfoVO user = ControllerHelper.getUser();
            att.setId(uuid);
            att.setSize((long) in.available());
            att.setUserId(user.getUserId());
            att.setUserName(user.getName());
            att.setContentType(fileType);
            Date date = new Date();
            att.setCreateTime(date);
            att.setUpdateTime(date);
            att.setSystemId(ControllerHelper.getSystemId());
            switch (type) {
                case FileService.MONGODB:
                    DB db = mongoClient.getDB(MONGO_DB_NAME);
                    GridFS myFS = new GridFS(db);
                    GridFSInputFile gf = myFS.createFile(in);
                    gf.setContentType(fileType);
                    gf.setFilename(fileName);
                    gf.put("id", uuid);
                    gf.put("user", user.getUserId());
                    gf.save();

                    att.setFileName(fileName);
                    att.setStorageId(uuid);
                    att.setType(FileService.MONGODB);
                    break;
                case FileService.FOLDER:
                    // 将分隔符统一替换
                    fileName = fileName.replaceAll("\\\\", FileService.separator);
                    // 获取文件名称
                    String name = fileName.substring(fileName.lastIndexOf(FileService.separator) + 1);
                    String suffix = name.substring(name.lastIndexOf("."));
                    att.setType(FileService.FOLDER);
                    att.setFileName(name);
                    String path = fileName.split(name)[0];
                    att.setStorageId(path + uuid + suffix);
                    // 判断目录是否存在
                    File file = new File(path);
                    boolean flag = true;
                    if (!file.exists()) {
                        flag = file.mkdirs();
                    }
                    if (flag) {
                        out = new BufferedOutputStream(new FileOutputStream(att.getStorageId()));
                        IOUtils.copy(in, out);
                    } else {
                        return null;
                    }
                    break;
                case FileService.FTP:
                    // unrealized
                    break;
                default:
                    break;
            }
            //是否索引到solr服务器
            if(isIndex){
                indexFileToSolr(in,vo);
            }
            att.save();
            return uuid;
        } catch (Exception e) {
            logger.info("ERROR", e);
            return null;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    @Override
    public String save(InputStream input, String fileType, String fileName, int type, boolean isIndex) {
        if (input == null) {
            return null;
        }
        try {
            return save(IOUtils.toByteArray(input), fileType, fileName, type, isIndex);
        } catch (IOException e) {
            logger.info("ERROR", e);
            return null;
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Override
    public AttachmentVO get(String id) {
        Attachment att = Attachment.get(Attachment.class, id);
        if (att != null) {
            AttachmentVO vo = new AttachmentVO();
            BeanUtils.copyProperties(Attachment.get(Attachment.class, id), vo, false);
            return vo;
        } else {
            return null;
        }
    }

    public static class AttachmentVO {
        private String id;
        private String storageId;

        private String fileName;

        private Long userId;

        private String userName;

        private String contentType;

        private Long systemId;

        private Long size;

        private Date createTime;
        private Date updateTime;

        private Integer type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStorageId() {
            return storageId;
        }

        public void setStorageId(String storageId) {
            this.storageId = storageId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public Long getSystemId() {
            return systemId;
        }

        public void setSystemId(Long systemId) {
            this.systemId = systemId;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

    @Override
    public boolean delete(boolean isCheck, String... id) {
        long userId = ControllerHelper.getUserId();
        boolean isSuccess = true;
        for (String i : id) {
            AttachmentVO att = get(i);
            if (att == null) {
                continue;
            }
            if (isCheck) {
                if (att.getUserId() != userId) {
                    isSuccess = false;
                    continue;
                }
            }
            switch (att.getType()) {
                case FileService.MONGODB:
                    DB db = mongoClient.getDB(MONGO_DB_NAME);
                    GridFS myFS = new GridFS(db);
                    DBObject query = new BasicDBObject("id", att.getStorageId());
                    myFS.remove(query);
                    break;

                case FileService.FOLDER:
                    isSuccess = new File(att.getStorageId()).delete();
                    break;
                default:
                    break;
            }
            Attachment atte = new Attachment();
            atte.setId(att.getId());
            atte.remove();
        }
        return isSuccess;
    }

    @Override
    public boolean delete(String... id) {
        return this.delete(true, id);
    }

    @Override
    public InputStream getInputStream(AttachmentVO att) {
        if (att == null) {
            return null;
        }
        InputStream in = null;
        switch (att.getType()) {
            case FileService.MONGODB:
                InputStream GFSInput=null;
                try {
                    DB db = mongoClient.getDB(MONGO_DB_NAME);
                    GridFS myFS = new GridFS(db);
                    DBObject query = new BasicDBObject("id", att.getStorageId());
                    GridFSDBFile gif = myFS.findOne(query);
                    if (gif != null) {
                        GFSInput=gif.getInputStream();
                        in=new ByteArrayInputStream(IOUtils.toByteArray(GFSInput));
                    }
                } catch (Exception e) {
                    logger.debug("ERROR", e);
                }finally {
                    IOUtils.closeQuietly(GFSInput);
                }

                break;

            case FileService.FOLDER:
                try {
                    File file = new File(att.getStorageId());
                    if (file.exists()) {
                        in = new FileInputStream(file);
                    }
                } catch (FileNotFoundException e) {
                    logger.info("ERROR", e);
                }
                break;
            default:
                break;
        }

        return in;
    }

    @Override
    public InputStream getInputStream(String id) {
        return this.getInputStream(get(id));
    }

    @Override
    public boolean download(AttachmentVO att, OutputStream out) {
        return copy(getInputStream(att), out);
    }

    @Override
    public boolean download(String id, OutputStream out) {
        return copy(getInputStream(id), out);
    }

    @Override
    public boolean copy(InputStream in, OutputStream out) {
        if (in != null && out != null) {
            try {
                IOUtils.copy(in, out);
                return true;
            } catch (IOException e) {
                logger.info("ERROR", e);
                return false;
            } finally {
                IOUtils.closeQuietly(in);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean batchDownload(String[] ids, OutputStream out) {
        ZipOutputStream zipOut = null;
        try {
            zipOut = new ZipOutputStream(out);
            for (String id : ids) {
                AttachmentVO att = get(id);
                InputStream inputStream = getInputStream(att);
                if (inputStream != null) {
                    // 设置压缩文件内的字符编码，不然会变成乱码
                    zipOut.setEncoding("GBK");
                    zipOut.putNextEntry(new ZipEntry(att.getFileName()));
                    // 读入需要下载的文件的内容，打包到zip文件
                    IOUtils.copy(inputStream, zipOut);
                    // 关闭输入流
                    IOUtils.closeQuietly(inputStream);
                }
            }
            return true;
        } catch (IOException e) {
            logger.info("ERROR", e);
            return false;
        } finally {
            if (zipOut != null) {
                // 关闭流
                try {
                    zipOut.flush();
                    zipOut.closeEntry();
                } catch (IOException e) {
                    logger.info("ERROE", e);
                }
                IOUtils.closeQuietly(zipOut);
            }
        }
    }

    @Override
    public String save(InputStream input, String fileType, String fileName, int type) {
        return this.save(input, fileType, fileName, type, false);
    }

    @Override
    public String save(InputStream input, String fileType, String fileName) {
        return this.save(input, fileType, fileName, FileService.DEFAULT, false);
    }


    @Override
    public void indexFileToSolr(InputStream input, AttachmentVO vo) {
        try {
            //得出KB
            ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
            ContentStream contentStream = new IContentStream(vo, input);
            up.addContentStream(contentStream);
            up.setParam("literal.id", vo.getId());
            up.setParam("literal.filename", vo.getFileName());
            //文件大小
            up.setParam("literal.filesize", vo.getSize() / 1024 + " KB");
            //每个索引条件的id
            // 文件索引时间
            up.setParam("literal.pathuploaddate", DocumentUtils.getIntance().today());
            // 文件类型，doc,pdf
            up.setParam("literal.pathftype", vo.getContentType());
            // 文件内容
            up.setParam("fmap.content", "attr_content");
            up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
            httpSolrClient.request(up);
            httpSolrClient.commit();
        } catch (SolrServerException | IOException e) {
            logger.debug("ERROR", e);
        }
    }

}