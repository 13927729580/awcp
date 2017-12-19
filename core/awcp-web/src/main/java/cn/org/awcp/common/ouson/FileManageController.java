package cn.org.awcp.common.ouson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.formdesigner.core.domain.Attachment;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;

/**
 * 文档管理
 * 
 * @author Administrator
 *
 */
@RequestMapping(value = "ouson")
@Controller
public class FileManageController {

	@Resource
	private SzcloudJdbcTemplate jdbcTemplate;

	/**
	 * 文件夹树
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllDirectory")
	public List<Map<String, Object>> getAllDirectory(String isAdmin) {
		String sql = "select id,pId,name,url,target,ifnull(open,true) as open,roles from oa_os_directory order by seq";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if ("Y".equals(isAdmin)) {
			return list;
		}
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) DocumentUtils.getIntance().getUser();
		sql = "select GROUP_CONCAT(DISTINCT ROLE_ID) from p_un_user_role where USER_ID=" + user.getUserId();
		String userRoles = jdbcTemplate.queryForObject(sql, String.class);
		List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
		while (true) {
			if (list.size() == 0) {
				break;
			}
			Map<String, Object> map = list.get(0);
			String needRoles = (String) map.get("roles");
			boolean hasRole = checkHasRole(userRoles, needRoles);
			if (hasRole) {
				temp.add(map);
			}
			list.remove(0);
		}
		return temp;
	}

	private boolean checkHasRole(String userRoles, String needRoles) {
		if ("0".equals(needRoles)) { // 所有用户都有权限
			return true;
		}
		for (String role : userRoles.split(",")) {
			if (needRoles.contains(role)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteDirectory")
	public Map<String, Object> deleteDirectory(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("msg", "1");
		String sql = "";
		// String sql = "select CONCAT(pIds,ID,',') from oa_os_directory where
		// ID='" + id + "'";
		// String pIds = jdbcTemplate.queryForObject(sql, String.class);
		// sql = "select GROUP_CONCAT(CONCAT('''',ID,'''')) from oa_os_directory
		// where pIds like ('" + pIds + "%') or ID=?";
		// String ids = jdbcTemplate.queryForObject(sql, String.class,id);
		try {
			jdbcTemplate.beginTranstaion();

			String selectsql = "select name,pId from oa_os_directory where id =?";
			Map<String, Object> dir = jdbcTemplate.queryForMap(selectsql, id);

			sql = "select dirpath from oa_os_directory where id =?";
			String dirpath = jdbcTemplate.queryForObject(sql, String.class, id);
			sql = "delete from oa_os_files where directoryID in (select id from oa_os_directory where pIds like ('%"
					+ id + "%'))";// 文档1
			jdbcTemplate.update(sql);
			sql = "delete from oa_os_files where directoryID ='" + id + "'";// 文档2
			jdbcTemplate.update(sql);
			sql = "delete from oa_os_directory where pIds like ('%" + id + "%')";// 文档1;//目录2
			jdbcTemplate.update(sql);
			sql = "delete from oa_os_directory where ID = '" + id + "'";// 目录1
			jdbcTemplate.update(sql);
			// 新增日志表
			String Logsql = "INSERT INTO oa_os_log (`ID`, `creator`, `creatDate`, `describes`, `fk_ID`) VALUES "
					+ "(UUID(), '" + DocumentUtils.getIntance().getUser().getUserId() + "', NOW(), " + "'删除目录——"
					+ dir.get("name") + "', '" + dir.get("pId") + "')";
			jdbcTemplate.update(Logsql);
			deleteDir(new File(dirpath));

			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jdbcTemplate.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			result.put("msg", "System error.");
		}
		return result;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		} else {
			// 目录此时为空，可以删除
			Attachment att = Attachment.getByStorage(dir.getAbsolutePath());
			if (att != null) {
				att.remove();
			}
		}
		return dir.delete();
	}

	/**
	 * 移动文件夹位置
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/directoryMove")
	public Map<String, Object> directoryMove(String id, String type) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("msg", "1");
		String sql = "select seq,pId from oa_os_directory where ID=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, id);
		int seq = (Integer) map.get("seq");
		String pId = (String) map.get("pId");
		sql = "select max(seq) from oa_os_directory where pId=?";
		int maxSeq = jdbcTemplate.queryForObject(sql, Integer.class, pId);
		try {
			jdbcTemplate.beginTranstaion();
			if ("up".equals(type)) {
				if (seq == 1) {
					result.put("msg", "This dirctory can't move up.");
				} else {
					sql = "update oa_os_directory set seq=? where seq=? and pId=?";
					jdbcTemplate.update(sql, seq, seq - 1, pId);
					sql = "update oa_os_directory set seq=? where ID=?";
					jdbcTemplate.update(sql, seq - 1, id);
				}
			} else {
				if (seq == maxSeq) {
					result.put("msg", "This dirctory can't move down.");
				} else {
					sql = "update oa_os_directory set seq=? where seq=? and pId=?";
					jdbcTemplate.update(sql, seq, seq + 1, pId);
					sql = "update oa_os_directory set seq=? where ID=?";
					jdbcTemplate.update(sql, seq + 1, id);
				}
			}
			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jdbcTemplate.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/directoryMoveSelect")
	public Map<String, Object> directoryMoveSelect(String oldID, String newID) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("msg", "1");
		// String sql = "select seq,pId from oa_os_directory where ID=?";
		// Map<String,Object> map = jdbcTemplate.queryForMap(sql,oldID);
		// String pId = (String) map.get("pId");
		// String selectSql="select * from oa_os_directory where pId=?";
		// List<Map<String,Object>>
		// list=jdbcTemplate.queryForList(selectSql,oldID);
		// String parentIds="'";
		// for(Map<String,Object> mu: list){
		// parentIds+=mu.get("ID")+"',";
		// }
		// parentIds=parentIds.substring(0,parentIds.length()-0);
		try {
			jdbcTemplate.beginTranstaion();
			String updateSql = "update oa_os_directory set pId='" + newID + "' where ID ='" + oldID + "'";
			jdbcTemplate.execute(updateSql);
			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jdbcTemplate.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/directoryTemplateMoveSelect")
	public Map<String, Object> directoryTemplateMoveSelect(String oldID, String newID) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("msg", "1");
		try {
			jdbcTemplate.beginTranstaion();
			String updateSql = "update oa_os_directorytemplate set pId='" + newID + "' where ID ='" + oldID + "'";
			jdbcTemplate.execute(updateSql);
			jdbcTemplate.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				jdbcTemplate.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 保存文件
	 * 
	 * @param fileIds
	 * @param directoryID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/saveFiles")
	public Map<String, Object> saveFiles(String fileIds, String directoryID) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("msg", "1");
		StringBuffer sb = new StringBuffer("");
		for (String fileId : fileIds.split(";")) {
			sb.append("'").append(fileId).append("',");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		String sql = "select ID,FILE_NAME,USER_ID,size from p_fm_attachment where ID in (" + sb.toString() + ")";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		sql = "insert into oa_os_files (ID,fileID,directoryID,fileName,fileSize,fileType,"
				+ "createPerson,roles,uploadDate,description)values (?,?,?,?,?,?,?,?,CURRENT_DATE,?)";
		try {
			jdbcTemplate.beginTranstaion();
			for (Map<String, Object> map : list) {
				String fileName = (String) map.get("FILE_NAME");
				if (fileName.lastIndexOf("/") != -1) {
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				}
				changeHistoryFileName(fileName, directoryID);
				String fileType = "";
				if (fileName.lastIndexOf(".") != -1) {
					fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
				}
				long size = (Long) map.get("size");
				String fileSize = String.format("%.2f", size / 1024.0) + "K";
				jdbcTemplate.update(sql, UUID.randomUUID().toString(), map.get("ID"), directoryID, fileName, fileSize,
						fileType, map.get("USER_ID"), null, null);
				// 新增日志表
				String Logsql = "INSERT INTO oa_os_log (`ID`, `creator`, `creatDate`, `describes`, `fk_ID`) "
						+ "VALUES (UUID(), '" + DocumentUtils.getIntance().getUser().getUserId() + "', NOW(), "
						+ "'上传文件名为" + fileName + "', '" + directoryID + "')";
				jdbcTemplate.update(Logsql);
			}
			jdbcTemplate.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				jdbcTemplate.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			result.put("msg", "System error.");
		}
		return result;
	}

	private void changeHistoryFileName(String fileName, String directoryID) {
		int index = fileName.lastIndexOf(".");
		String sql = "update oa_os_files set fileName=concat(?,'_',date_format(uploadDate,'%d/%m/%Y'),?) "
				+ "where fileName=? and directoryID=?";
		jdbcTemplate.update(sql, fileName.substring(0, index), fileName.substring(index), fileName, directoryID);
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/downloadFiles")
	public void downloadFiles(String[] ids, HttpServletRequest request, HttpServletResponse response) {
		if (ids.length == 1) {
			downloadFile(ids[0], request, response);
		} else {
			try {
				batchDownloadFile(ids, request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 新增日志表
		for (int i = 0; i < ids.length; i++) {
			String fileNameSql = "select fileName,directoryID from oa_os_files where ID=?";
			Map<String, Object> file = jdbcTemplate.queryForMap(fileNameSql, ids[i]);
			String Logsql = "INSERT INTO oa_os_log (`ID`, `creator`, `creatDate`, `describes`, `fk_ID`) VALUES "
					+ "(UUID(), '" + DocumentUtils.getIntance().getUser().getUserId() + "', NOW(), " + "'下载文件名为"
					+ file.get("fileName") + "', '" + file.get("directoryID") + "')";
			jdbcTemplate.update(Logsql);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/NewDownloadFiles")
	public void NewDownloadFiles(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("_selects");
		String[] ids = id.split(",");
		if (ids.length == 1) {
			downloadFile(ids[0], request, response);
		} else {
			batchDownloadFile(ids, request, response);
		}
	}

	// 下载文件
	private void downloadFile(String id, HttpServletRequest request, HttpServletResponse response) {
		String sql = "select fileID from oa_os_files where ID=?";
		String fileId = jdbcTemplate.queryForObject(sql, String.class, id);
		Attachment attachment = Attachment.get(Attachment.class,fileId);
		if (attachment != null) {
			InputStream is = attachment.getInputStreamByFolder();
			if (is != null) {
				String fileName = attachment.getFileName();
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				downloadFile(is, fileName, request, response);
			}
		}
	}

	// 下载文件
	private void downloadFile(InputStream is, String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition",
					"attachment; filename=" + encodeChineseDownloadFileName(request, fileName));
			OutputStream out = response.getOutputStream(); // 读取文件流
			int i = 0;
			byte[] buffer = new byte[4096];
			while ((i = is.read(buffer)) != -1) { // 写文件流
				out.write(buffer, 0, i);
			}
			out.flush();
			out.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 是文件名为中文时不乱码
	private String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName)
			throws UnsupportedEncodingException {
		String filename = null;
		String agent = request.getHeader("USER-AGENT");
		if (null != agent) {
			if (-1 != agent.indexOf("Firefox")) {// Firefox
				filename = "=?UTF-8?B?"
						+ (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
						+ "?=";
			} else if (-1 != agent.indexOf("Chrome")) {// Chrome
				filename = new String(pFileName.getBytes(), "ISO8859-1");
			} else {// IE7+
				filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
				filename = StringUtils.replace(filename, "+", "%20");// 替换空格
			}
		} else {
			filename = pFileName;
		}
		return filename;
	}

	// 批量下载文件
	private void batchDownloadFile(String[] ids, HttpServletRequest request, HttpServletResponse response) {
		ZipOutputStream out = null;
		try {
			String zipFileName = "download.zip"; // 生成的zip文件名为.zip
			response.setContentType("text/html;charset=UTF-8");
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + zipFileName);
			byte[] buffer = new byte[1024];
			out = new ZipOutputStream(response.getOutputStream());
			for (String id : ids) {
				String sql = "select fileID from oa_os_files where ID=?";
				String fileId = jdbcTemplate.queryForObject(sql, String.class, id);
				Attachment attachment = Attachment.get(Attachment.class,fileId);
				if (attachment != null) {
					InputStream is = attachment.getInputStreamByFolder();
					if (is != null) {
						String fileName = attachment.getFileName();
						fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
						out.setEncoding("GBK"); // 设置压缩文件内的字符编码，不然会变成乱码
						out.putNextEntry(new ZipEntry(fileName));
						int len;
						// 读入需要下载的文件的内容，打包到zip文件
						while ((len = is.read(buffer)) > 0) {
							out.write(buffer, 0, len);
						}
						is.close();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {

				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 删除文件
	 * 
	 * @param fileIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteFiles")
	public Map<String, Object> deleteFiles(String ids) {
		Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		for (String id : ids.split(",")) {
			sb.append("'").append(id).append("',");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) DocumentUtils.getIntance().getUser();
		String sql = "select count(*) from oa_os_files where createPerson<>? and ID in (" + sb.toString() + ")";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, user.getUserId());
		if (count > 0) {
			result.put("msg", "Cannot delete the files that not created by you");
		} else {
			// 新增日志表
			String[] idfile = ids.split(",");
			for (int i = 0; i < idfile.length; i++) {
				String fileNameSql = "select fileName,directoryID from oa_os_files where ID=?";
				Map<String, Object> file = jdbcTemplate.queryForMap(fileNameSql, idfile[i]);
				String Logsql = "INSERT INTO oa_os_log (`ID`, `creator`, `creatDate`, `describes`, `fk_ID`) VALUES "
						+ "(UUID(), '" + DocumentUtils.getIntance().getUser().getUserId() + "', NOW(), " + "'删除文件名为"
						+ file.get("fileName") + "', '" + file.get("directoryID") + "')";
				jdbcTemplate.update(Logsql);
			}
			for (String id : ids.split(",")) {
				deleteFile(id);
			}
			sql = "delete from oa_os_files where ID in (" + sb.toString() + ")";
			jdbcTemplate.update(sql);
			result.put("msg", "1");
		}
		return result;
	}

	private void deleteFile(String id) {
		try {
			String sql = "select fileID from oa_os_files where ID='" + id + "'";
			String fileId = jdbcTemplate.queryForObject(sql, String.class);
			Attachment att = Attachment.get(Attachment.class,fileId);
			if (att != null) {
				new File(att.getStorageId()).delete();
				att.remove();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
