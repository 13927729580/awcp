package cn.org.awcp.common.file;

import cn.org.awcp.core.utils.Tools;
import cn.org.awcp.formdesigner.core.domain.Store;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.service.FileService;
import cn.org.awcp.venson.service.impl.FileServiceImpl.AttachmentVO;
import cn.org.awcp.venson.util.DocumentToHtml;
import cn.org.awcp.venson.util.ImageUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@SuppressWarnings("restriction")
@Controller(value = "myFileController")
@RequestMapping("/common/file")
public class FileController {
    /**
     * 日志对象
     */
    private static final Log logger = LogFactory.getLog(FileController.class);

    /**
     * 上传文件最大值
     */
    private static final int UPLOAD_MAX_SIZE = 52428800;
    @Resource(name = "IFileService")
    private FileService fileService;

    @ResponseBody
    @RequestMapping(value = "/upload")
    public ReturnResult upload(HttpServletRequest request) throws IOException {
        ReturnResult result = ReturnResult.get();
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            String uploadType = request.getParameter("uploadType");
            int type = StringUtils.isNumeric(uploadType) ? Integer.parseInt(uploadType) : FileService.DEFAULT;
            String isIndex = request.getParameter("isIndex");
            String filePath = null;
            // 判断是否是文件夹存储方式
            if (type == FileService.FOLDER) {
                // 获取文件夹
                String uploadFolder = request.getParameter("uploadFolder");
                filePath = getUploadPath(uploadFolder);
            }
            List<Serializable> list = new ArrayList<>();
            while (iter.hasNext()) {
                // 取得上传文件
                List<MultipartFile> files = multiRequest.getFiles(iter.next());
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        if (file != null) {
                            // 取得当前上传文件的文件名称
                            String myFileName = file.getOriginalFilename();
                            // 如果名称不为"",说明该文件存在，否则说明该文件不存在
                            if (myFileName.trim() != "") {
                                String fileName = file.getOriginalFilename();
                                if (type == FileService.FOLDER) {
                                    fileName = filePath + fileName;
                                }
                                Serializable id = fileService.save(file.getInputStream(), file.getContentType(),
                                        fileName, type, "1".equals(isIndex));
                                if (id != null) {
                                    list.add(id);
                                }
                            }
                        }
                    }
                }
            }
            if (list.size() > 0) {
                return result.setStatus(StatusCode.SUCCESS).setData(StringUtils.join(list, ";"));
            } else {
                return result.setStatus(StatusCode.FAIL.setMessage("上传失败"));
            }
        } else {
            return result.setStatus(StatusCode.FAIL.setMessage("未找到上传文件，上传失败"));
        }
    }

    /**
     * 下载文件
     *
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download")
    public String download(HttpServletResponse response, String fileId) throws IOException {
        boolean isSuccess = false;
        AttachmentVO att = fileService.get(fileId);
        if (att != null) {
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition",
                    "attachment; filename=" + ControllerHelper.processFileName(att.getFileName()));
            isSuccess = fileService.download(att, response.getOutputStream());
        }
        if (!isSuccess) {
            return "redirect:" + fileLose();
        }
        return null;
    }

    @RequestMapping(value = "/showPicture")
    public String showPicture(HttpServletResponse response, @RequestParam(value = "id") String id,
                              @RequestParam(value = "width", required = false, defaultValue = "0") int width,
                              @RequestParam(value = "height", required = false, defaultValue = "0") int height) throws IOException {
        // 设置缓存
        response.addHeader("Cache-Control", "max-age=86400");
        response.addHeader("Expires", new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000).toString());
        AttachmentVO attachmentVO=fileService.get(id);
        if (attachmentVO == null) {
            return "redirect:" + fileLose();
        }
        InputStream input = fileService.getInputStream(attachmentVO);
        if (input == null) {
            return "redirect:" + fileLose();
        }
        response.addHeader("Content-type", attachmentVO.getContentType());
        if (width > 0 || height > 0) {
            BufferedImage bi;
            //如果宽高都有值则不进行等比
            if(width==0||height==0){
                 bi = ImageUtils.scale(input,width,height,true);
            }else{
                bi = ImageUtils.scale(input,width,height,false);
            }
            if (bi != null) {
                ImageIO.write(bi, ImageUtils.PNG, response.getOutputStream());
            } else {
                return "redirect:" + fileLose();
            }
        } else {
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("ETag", attachmentVO.getId());
            response.addHeader("Content-Length", attachmentVO.getSize()+"");
            fileService.copy(input, response.getOutputStream());
        }

        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/scale/{id}", method = RequestMethod.GET)
    public ReturnResult scale(@PathVariable(value = "id") String id,
                              @RequestParam(value = "width", required = false, defaultValue = "0") int width,
                              @RequestParam(value = "height", required = false, defaultValue = "0") int height,
                              @RequestParam(value = "isRatio", required = false, defaultValue = "true") boolean isRatio) throws IOException {
        ReturnResult result = ReturnResult.get();
        AttachmentVO att = fileService.get(id);
        if (att == null) {
            return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片去火星了"));
        }
        InputStream input = fileService.getInputStream(att);
        if (input == null) {
            return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片去火星了"));
        }
        if (!isRatio) {
            if (width == 0 || height == 0) {
                return result.setStatus(StatusCode.FAIL.setMessage("非等比缩放时宽度和高度不能为空"));
            }
        }
        if (width > 0 || height > 0) {
            BufferedImage bi = ImageUtils.scale(input, width, height, isRatio);
            if (bi != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bi, att.getFileName().substring(att.getFileName().lastIndexOf(".") + 1), os);
                String fileId;
                if (att.getType() == FileService.FOLDER) {
                    fileId = fileService.save(os.toByteArray(), att.getContentType(), att.getStorageId(), att.getType());
                } else {
                    fileId = fileService.save(os.toByteArray(), att.getContentType(), att.getFileName(), att.getType());
                }
                if (fileId != null) {
                    return result.setStatus(StatusCode.SUCCESS).setData(fileId);
                } else {
                    return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片保存失败啦"));
                }
            } else {
                return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片保存失败啦"));
            }
        } else {
            return result.setStatus(StatusCode.FAIL.setMessage("宽度和高度必须要有一个值"));
        }

    }

    @ResponseBody
    @RequestMapping(value = "/crop/{id}", method = RequestMethod.GET)
    public ReturnResult crop(@PathVariable(value = "id") String id,@RequestParam(value = "width") Integer width,
                             @RequestParam(value = "height") Integer height,@RequestParam(value = "x") Integer x,
                             @RequestParam(value = "y") Integer y) throws IOException {
        ReturnResult result = ReturnResult.get();
        AttachmentVO att = fileService.get(id);
        if (att == null) {
            return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片去火星了"));
        }
        InputStream input = fileService.getInputStream(att);
        if (input == null) {
            return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片去火星了"));
        }
        String suffix=att.getFileName().substring(att.getFileName().lastIndexOf(".") + 1);
            BufferedImage bi = ImageUtils.crop(input, x,y, width,height,suffix);
            if (bi != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bi, suffix, os);
                String fileId;
                if (att.getType() == FileService.FOLDER) {
                    fileId = fileService.save(os.toByteArray(), att.getContentType(), att.getStorageId(), att.getType());
                } else {
                    fileId = fileService.save(os.toByteArray(), att.getContentType(), att.getFileName(), att.getType());
                }
                if (fileId != null) {
                    return result.setStatus(StatusCode.SUCCESS).setData(fileId);
                } else {
                    return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片保存失败啦"));
                }
            } else {
                return result.setStatus(StatusCode.FAIL.setMessage("啊哦，图片保存失败啦"));
            }

    }

    /**
     * 将base64位编码转为图片，保存，返回图片路径
     */
    @ResponseBody
    @RequestMapping(value = "/generateImageByBase64Str")
    public String generateImageByBase64Str(String imgStr) {
        // 对字节数组字符串进行Base64解码并生成图片,图像数据为空
        if (imgStr == null) {
            return "";
        }
        String[] imgStrs = imgStr.split(",");
        BASE64Decoder decoder = new BASE64Decoder();
        logger.debug(imgStr);
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStrs[imgStrs.length - 1]);
            for (int i = 0; i < bytes.length; ++i) {
                // 调整异常数据
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            Serializable id = fileService.save(new ByteArrayInputStream(bytes), "img.jpg", "img/jpg",
                    FileService.MONGODB);
            // 返回访问图片的url到富文本框中，通过getImage方法访问图片
            return ControllerHelper.getBasePath() + "common/file/showPicture.do?id=" + id;
        } catch (Exception e) {
            return fileLose();
        }
    }

    /**
     * 批量下载文件
     * <p>
     * 先将文件写到本地，然后封装到ZIP中下载
     *
     * @param response
     * @param fileIds
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/batchDownload")
    public String batchDownload(HttpServletResponse response,
                                @RequestParam(value = "fileName", required = false, defaultValue = "download") String fileName,
                                String[] fileIds) throws IOException {
        boolean isSuccess;
        if (fileIds == null || fileIds.length == 0) {
            isSuccess = false;
        } else {
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + ControllerHelper.processFileName(fileName) + ".zip");
            isSuccess = fileService.batchDownload(fileIds, response.getOutputStream());
        }
        if (!isSuccess) {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html;charset=UTF-8");
            out.print("文件不存在");
            out.close();
        }
        return null;
    }

    /**
     * 根据ID取得文件信息
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get")
    public ReturnResult get(String id) {
        ReturnResult result = ReturnResult.get();
        AttachmentVO vo = fileService.get(id);
        InputStream in = fileService.getInputStream(vo);
        if (in != null) {
            result.setStatus(StatusCode.SUCCESS).setData(vo);
            IOUtils.closeQuietly(in);
        } else {
            result.setStatus(StatusCode.SUCCESS).setData(-1);
        }
        return result;
    }

    /**
     * @param ids 需要删除的文件id
     * @return {result,msg} result = 1 sucess result = 2 部分删除成功 result = 3 删除失败
     */
    @ResponseBody
    @RequestMapping(value = "/delete")
    public ReturnResult delete(String[] ids) {
        ReturnResult result = ReturnResult.get();
        if (ids == null || ids.length == 0) {
            return result.setStatus(StatusCode.SUCCESS).setData(-1);
        }
        if (fileService.delete(true, ids)) {
            result.setStatus(StatusCode.SUCCESS).setData(0);
        } else {
            result.setStatus(StatusCode.SUCCESS).setData(-1);
        }
        return result;
    }

    /**
     * @param ids 需要删除的文件id
     * @return {result,msg} result = 1 sucess result = 2 部分删除成功 result = 3 删除失败
     */
    @ResponseBody
    @RequestMapping(value = "/remove")
    public ReturnResult remove(String[] ids) {
        ReturnResult result = ReturnResult.get();
        if (ids == null || ids.length == 0) {
            return result.setStatus(StatusCode.SUCCESS).setData(-1);
        }
        result.setStatus(StatusCode.SUCCESS).setData(0);
//        if (fileService.delete(false, ids)) {
//            result.setStatus(StatusCode.SUCCESS).setData(0);
//        } else {
//            result.setStatus(StatusCode.SUCCESS).setData(-1);
//        }
        return result;
    }

    @RequestMapping("/preview")
    public String preview(HttpServletResponse response, String fileId) throws Exception {
        AttachmentVO att = fileService.get(fileId);
        InputStream is = fileService.getInputStream(att);
        String msg;
        if (is != null) {
            String fileName = att.getFileName();
            // 读取文件信息，判断格式，如果是文本，直接显示，如果是word，先进行转化，然后返回html地址，
            String fileType = Tools.getTypePart(fileName);
            String ext = fileType.toLowerCase();
            if (StringUtils.isBlank(fileType)) {
                msg = "文件格式不支持预览，请先下载后再打开。";
            } else {
                if ("txt".equals(ext) || "doc".equals(ext) || "docx".equals(ext) || "xls".equals(ext)
                        || "xlsx".equals(ext)) {
                    String rootPath = ControllerHelper.UPLOAD_PREVIEW_PATH;
                    String htmlPath = ControllerHelper.getUploadPath(ControllerHelper.UPLOAD_PREVIEW_PATH);
                    String htmlName = fileId + ".html";
                    String position = htmlPath + htmlName;
                    if (!new File(position).exists()) {
                        DocumentToHtml.getInstance().toHtml(is, fileName, htmlPath, fileId + ".html");
                    }
                    return "redirect:" + rootPath + "/" + htmlName;
                } else if ("jpg".equals(ext) || "png".equals(ext) || "bmp".equals(ext) || "gif".equals(ext)) {
                    // 图片,重定向到 showPicture.do?id=
                    return "redirect:showPicture.do?id=" + fileId;
                } else if ("pdf".equals(ext)) {
                    // 直接在线预览pdf
                    response.setContentType("application/pdf;charset=UTF-8");
                    response.setHeader("Content-disposition", "inline; filename=" + fileName);
                    ServletOutputStream out = response.getOutputStream();
                    fileService.copy(is, out);
                    out.flush();
                    out.close();
                    return null;
                } else if ("html".equals(ext)) {
                    // 直接在线预览pdf
                    response.setContentType("text/html;charset=UTF-8");
                    response.setHeader("Content-disposition", "inline; filename=" + fileName);
                    ServletOutputStream out = response.getOutputStream();
                    fileService.copy(is, out);
                    out.flush();
                    out.close();
                    return null;
                } else {
                    msg = "文件格式不支持预览，请先下载后再打开。";
                }
            }
        } else {
            msg = "文件不存在";
        }
        response.setContentType("text/html;charset=UTF-8");
        return msg;
    }

    /**
     * kindeditor文件上传后台方法
     *
     * @param request
     * @param response
     * @return
     * @throws FileUploadException
     */
    @ResponseBody
    @RequestMapping(value = "/uploadImg")
    public String uploadImege(HttpServletRequest request, HttpServletResponse response) {
        // uuid
        Serializable uuid;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        // 定义允许上传的文件扩展名
        HashMap<String, String> extMap = new HashMap<>(4);
        extMap.put("image", "gif,jpg,jpeg,png,bmp");
        extMap.put("flash", "swf,flv");
        extMap.put("media", "swf,flv,mp3,mp4,m4v,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

        response.setContentType("text/html; charset=UTF-8");

        JSONObject obj = new JSONObject();
        obj.put("error", 0);
        String dirName = request.getParameter("dir");
        if (dirName == null) {
            dirName = "image";
        }
        Iterator<String> it = multipartRequest.getFileNames();
        if (it == null || !it.hasNext()) {
            throw new PlatformException("上传文件为空");
        }
        MultipartFile imgFile = multipartRequest.getFile(multipartRequest.getFileNames().next());
        String fileName = imgFile.getOriginalFilename();
        String uploadType = request.getParameter("uploadType");
        int type = StringUtils.isNumeric(uploadType) ? Integer.parseInt(uploadType) : FileService.DEFAULT;
        // 判断是否是文件夹存储方式
        if (type == FileService.FOLDER) {
            // 获取文件夹
            String uploadFolder = request.getParameter("uploadFolder");
            String filePath = getUploadPath(uploadFolder);
            fileName = filePath + fileName;
        }
        checkFile(request, extMap, obj, dirName, imgFile, fileName);
        if (obj.getInteger("error") == 1) {
            return obj.toJSONString();
        }
        try {
            uuid = fileService.save(imgFile.getInputStream(), imgFile.getContentType(), fileName, type);
        } catch (IOException e) {
            logger.info("ERROR", e);
            obj.put("error", 1);
            obj.put("message", "upload fail.");
            return obj.toJSONString();
        }
        // 返回访问图片的url到富文本框中，通过getImage方法访问图片
        obj.put("url", ControllerHelper.getBasePath() + "common/file/showPicture.do?id=" + uuid);
        return obj.toJSONString();

    }

    private String fileLose() {
        return ControllerHelper.getBasePath() + "images/img_lose.png";
    }

    private void checkFile(HttpServletRequest request, HashMap<String, String> extMap, JSONObject obj, String dirName,
                           MultipartFile imgFile, String fileName) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            obj.put("error", 1);
            obj.put("message", "请选择文件！");
            return;
        }
        if (!extMap.containsKey(dirName)) {
            obj.put("error", 1);
            obj.put("message", "目录名不正确！");
            return;
        }
        // 获取原图片文件名字及文件扩展名
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 上传格式检查
        if (!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)) {
            obj.put("error", 1);
            obj.put("message", "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
            return;
        }
        if (imgFile.getSize() > UPLOAD_MAX_SIZE) {
            obj.put("error", 1);
            obj.put("message", "上传文件过大！");
            return;
        }
    }

    private String getUploadPath(String uploadFolder) {
        String filePath;
        Store store = Store.get(Store.class, uploadFolder);
        String uploadPath = null;
        if (store != null && store.getContent() != null) {
            JSONObject content = JSON.parseObject(store.getContent());
            uploadPath = content.getString("filePath");
        }
        if (StringUtils.isBlank(uploadPath)) {
            uploadPath = ControllerHelper.getUploadPath();
        }
        uploadPath = uploadPath.replaceAll("\\\\", FileService.separator);
        if (!uploadPath.endsWith(FileService.separator)) {
            filePath = uploadPath + FileService.separator;
        } else {
            filePath = uploadPath;
        }
        return filePath;
    }
}
