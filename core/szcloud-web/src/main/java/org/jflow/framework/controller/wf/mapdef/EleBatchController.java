package org.jflow.framework.controller.wf.mapdef;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.common.model.EleBatchModel;
import org.jflow.framework.common.model.TempObject;
import org.jflow.framework.controller.wf.workopt.BaseController;
import org.jflow.framework.system.ui.core.BaseWebControl;
import org.jflow.framework.system.ui.core.CheckBox;
import org.jflow.framework.system.ui.core.HtmlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.DA.DBAccess;
import BP.DA.DataRow;
import BP.DA.DataTable;
import BP.Sys.Frm.MapAttr;
import BP.Sys.Frm.MapData;
import BP.Sys.Frm.MapDatas;

@Controller
@RequestMapping("/WF/MapDef")
public class EleBatchController extends BaseController{
	@RequestMapping(value = "/btn_Click4", method = RequestMethod.POST)
	public void btn_Click(TempObject object, HttpServletRequest request,
			HttpServletResponse response) {
		MapDatas mds = this.getGetMDs();
		HashMap<String,BaseWebControl> controls = HtmlUtils.httpParser(object.getFormHtml(), true);
		MapAttr mattrOld = new MapAttr(object.getFK_MapData(), object.getKeyOfEn());
		MapAttr mattr = new MapAttr(object.getFK_MapData(), object.getKeyOfEn());
		for (MapData md : MapDatas.convertMapDatas(mds))
		{
			CheckBox cb = (CheckBox) controls.get("CB_" + md.getNo());
			if (cb==null)
			{
				continue;
			}

			if (cb.getChecked() == false)
			{
				continue;
			}

			if (this.getDoType().equals("Copy"))
			{
				//执行批量Copy
				mattr.setFK_MapData(md.getNo());
				mattr.Insert();
				mattr.setIDX(mattrOld.getIDX());
			}

			if (this.getDoType().equals("Update"))
			{
				//执行批量Update
				MapAttr mattrUpdate = new MapAttr(md.getNo(), object.getKeyOfEn());
				int gID = mattrUpdate.getGroupID();
				mattrUpdate.Copy(mattrOld);
				mattrUpdate.setFK_MapData(md.getNo());
				mattrUpdate.setGroupID(gID);
				mattrUpdate.Update();
			}

			if (this.getDoType().equals("Delete"))
			{
				//执行批量 Delete 
				MapAttr mattrDelete = new MapAttr(md.getNo(), object.getKeyOfEn());
				mattrDelete.Delete();
			}

		}
		// 转向.
//		this.Response.Redirect(this.Request.RawUrl, true);
		try {
			response.sendRedirect("EleBatch.jsp?EleType="+object.getEleType()+"&FK_MapData="+object.getFK_MapData()+"&KeyOfEn="+object.getKeyOfEn()+"&DoType="+object.getDoType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public MapDatas getGetMDs() {
		String sql = "SELECT NodeID FROM WF_Node WHERE FK_Flow='"
				+ this.getFK_Flow() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		String nds = "";
		for (DataRow dr : dt.Rows) {
			nds += ",'ND" + dr.getValue(0).toString() + "'";
		}
		sql = "SELECT No FROM Sys_MapData WHERE No IN (" + nds.substring(1)
				+ ")";
		dt = DBAccess.RunSQLReturnTable(sql);
		MapDatas mds = new MapDatas();
		mds.RetrieveInSQL(sql);

		return mds;
	}

}
