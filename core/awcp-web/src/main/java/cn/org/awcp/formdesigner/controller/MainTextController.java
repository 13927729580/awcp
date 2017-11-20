package cn.org.awcp.formdesigner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import cn.org.awcp.base.BaseController;
import cn.org.awcp.core.utils.Tools;
import cn.org.awcp.core.utils.constants.SessionContants;
import cn.org.awcp.core.utils.mongodb.MongoDBUtils;
import cn.org.awcp.formdesigner.core.domain.Attachment;
import cn.org.awcp.unit.vo.PunSystemVO;
import cn.org.awcp.unit.vo.PunUserBaseInfoVO;

@Controller
@RequestMapping("/fd/mainText")
public class MainTextController extends BaseController {

	@ResponseBody
	@RequestMapping(value = "/getUser")
	public PunUserBaseInfoVO getUser() {
		Object obj3 = Tools.getObjectFromSession(SessionContants.CURRENT_USER);
		PunUserBaseInfoVO user = (PunUserBaseInfoVO) obj3;
		return user;
	}

	@ResponseBody
	@RequestMapping(value = "/save")
	public Attachment save(String id, String busId) {
		// 获取临时文件的ID
		Attachment file = Attachment.get(Attachment.class, id);
		if (file != null) {
			Attachment attr = new Attachment();
			// 插入正式数据
			attr.setId(busId);
			attr.setContentType("正文编辑");
			attr.setFileName("正文编辑");
			attr.setSize(file.getSize());
			attr.setStorageId(file.getStorageId());
			attr.setSystemId(file.getSystemId());
			attr.setUserId(file.getUserId());
			attr.setUserName(file.getUserName());
			attr.save();
			file.remove();
			// 修改mangodb数据

			Object obj2 = Tools.getObjectFromSession(SessionContants.CURRENT_SYSTEM);
			if (obj2 instanceof PunSystemVO) {
				PunSystemVO system = (PunSystemVO) obj2;
				Object obj3 = Tools.getObjectFromSession(SessionContants.CURRENT_USER);
				PunUserBaseInfoVO user = (PunUserBaseInfoVO) obj3;

				MongoClient client = MongoDBUtils.getMongoClient();
				DB db = client.getDB("myFiles");
				GridFS myFS = new GridFS(db);
				DBObject query = new BasicDBObject("id", id);
				GridFSDBFile filedb = myFS.findOne(query);

				GridFSInputFile input = myFS.createFile(filedb.getInputStream());
				input.setContentType(file.getContentType());
				input.setFilename("正文");
				input.put("id", busId);
				input.put("user", user.getUserId());
				input.put("sysId", system.getSysId());
				input.save();
				myFS.remove(query);

			}

			return attr;
		}

		return null;
	}

}
