package BP.WF.HttpHandler;

import BP.DA.*;
import BP.Sys.*;
import BP.Tools.Json;
import BP.Web.*;
import BP.Port.*;
import BP.En.*;
import BP.WF.*;
import BP.WF.Template.*;
import BP.WF.HttpHandler.Base.*;

import org.apache.http.protocol.HttpContext;

/**
 * 页面功能实体
 */
public class WF_Admin_Sln extends WebContralBase {
	public WF_Admin_Sln() {

	}

	/**
	 * 页面功能实体
	 * 
	 * @param mycontext
	 */
	public WF_Admin_Sln(HttpContext mycontext) {
		this.context = mycontext;
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 绑定流程表单
	/**
	 * 获取流程所有节点
	 * 
	 * @return
	 */
	public final String BindForm_GenderFlowNode() {
		Node nd = new Node(this.getFK_Node());

		// 规范做法.
		Nodes nds = new Nodes(nd.getFK_Flow());
		return nds.ToJson();

	}

	/**
	 * 获取所有节点，复制表单
	 * 
	 * @return
	 */
	public final String BindForm_GetFlowNodeDropList() {
		Nodes nodes = new Nodes();
		nodes.Retrieve(BP.WF.Template.NodeAttr.FK_Flow, this.getFK_Flow(),
				BP.WF.Template.NodeAttr.Step);

		if (nodes.size() == 0) {
			return "";
		}

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("<select id = \"copynodesdll\"  multiple = \"multiple\" style = \"border - style:None; width: 100%; Height: 100%; \">");

		for (Node node : nodes.Tolist()) {
			sBuilder.append("<option "
					+ (this.getFK_Node() == node.getNodeID() ? "disabled = \"disabled\""
							: "") + " value = \"" + node.getNodeID() + "\" >"
					+ "[" + node.getNodeID() + "]" + node.getName()
					+ "</ option >");
		}

		sBuilder.append("</select>");

		return sBuilder.toString();
	}

	/**
	 * 复制表单到节点
	 * 
	 * @return
	 */
	public final String BindFrmsDtl_DoCopyFrmToNodes() {
		String nodeStr = this.GetRequestVal("NodeStr"); // 节点string,
		String frmStr = this.GetRequestVal("frmStr"); // 表单string,

		String[] nodeList = nodeStr.split("[,]", -1);
		String[] frmList = frmStr.split("[,]", -1);

		for (String node : nodeList) {
			if (DataType.IsNullOrEmpty(node)) {
				continue;
			}

			int nodeid = Integer.parseInt(node);

			DBAccess.RunSQL("DELETE FROM WF_FrmNode WHERE FK_Node=" + nodeid);

			for (String frm : frmList) {
				if (DataType.IsNullOrEmpty(frm)) {
					continue;
				}

				FrmNode fn = new FrmNode();
				if (!fn.IsExit("mypk",
						frm + "_" + nodeid + "_" + this.getFK_Flow())) {
					fn.setFK_Frm(frm);
					fn.setFK_Node(nodeid);
					fn.setFK_Flow(this.getFK_Flow());

					fn.Insert();
				}
			}
		}

		return "操作成功！";
	}

	/**
	 * 保存流程表单
	 * 
	 * @return
	 */
	public final String BindFrmsDtl_Save() {
		try {
			String formNos = this.GetRequestVal("formNos");

			FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
			// 删除已经删除的。
			for (FrmNode fn : fns.Tolist()) {
				if (formNos.contains("," + fn.getFK_Frm() + ",") == false) {
					fn.Delete();
					continue;
				}
			}

			// 增加集合中没有的。
			String[] strs = formNos.split("[,]", -1);
			for (String s : strs) {
				if (DotNetToJavaStringHelper.isNullOrEmpty(s)) {
					continue;
				}
				if (fns.Contains(FrmNodeAttr.FK_Frm, s)) {
					continue;
				}

				FrmNode fn = new FrmNode();
				fn.setFK_Frm(s);
				fn.setFK_Flow(this.getFK_Flow());
				fn.setFK_Node(this.getFK_Node());

				fn.setMyPK(fn.getFK_Frm() + "_" + fn.getFK_Node() + "_"
						+ fn.getFK_Flow());
				fn.Save();
			}
			return "true";
		} catch (RuntimeException ex) {
			return "err:保存失败。";
		}
	}

	/**
	 * 获取表单库所有表单
	 * 
	 * @return
	 */
	public final String BindForm_GenerForms() {
		// 形成树
		FlowFormTrees appendFormTrees = new FlowFormTrees();
		// 节点绑定表单
		FrmNodes frmNodes = new FrmNodes(this.getFK_Flow(), this.getFK_Node());
		// 所有表单类别
		SysFormTrees formTrees = new SysFormTrees();
		formTrees.RetrieveAll(SysFormTreeAttr.Idx);

		// 根节点
		BP.WF.Template.FlowFormTree root = new BP.WF.Template.FlowFormTree();
		root.setNo("00");
		root.setParentNo("0");
		root.setName("表单库");
		root.setNodeType("root");
		appendFormTrees.AddEntity(root);

		for (SysFormTree formTree : formTrees.Tolist()) {
			// 已经添加排除
			if (appendFormTrees.Contains("No", formTree.getNo()) == true) {
				continue;
			}
			// 根节点排除
			if (formTree.getParentNo().equals("0")) {
				root.setNo(formTree.getNo());
				continue;
			}
			// 文件夹
			BP.WF.Template.FlowFormTree nodeFolder = new BP.WF.Template.FlowFormTree();
			nodeFolder.setNo(formTree.getNo());
			nodeFolder.setParentNo(formTree.getParentNo());
			nodeFolder.setName(formTree.getName());
			nodeFolder.setNodeType("folder");
			if (formTree.getParentNo().equals("0")) {
				nodeFolder.setParentNo(root.getNo());
			}
			appendFormTrees.AddEntity(nodeFolder);

			// 表单
			MapDatas mapS = new MapDatas();
			mapS.RetrieveByAttr(MapDataAttr.FK_FormTree, formTree.getNo());
			if (mapS != null && mapS.size() > 0) {
				for (MapData map : mapS.Tolist()) {
					BP.WF.Template.FlowFormTree formFolder = new BP.WF.Template.FlowFormTree();
					formFolder.setNo(map.getNo());
					formFolder.setParentNo(map.getFK_FormTree());
					formFolder.setName(map.getName() + "[" + map.getNo() + "]");
					formFolder.setNodeType("form");
					appendFormTrees.AddEntity(formFolder);
				}
			}
		}

		String strCheckedNos = "";
		// 设置选中
		for (FrmNode frmNode : frmNodes.Tolist()) {
			strCheckedNos += "," + frmNode.getFK_Frm() + ",";
		}
		// 重置
		appendMenus = new StringBuilder();
		// 生成数据
		TansEntitiesToGenerTree(appendFormTrees, root.getNo(), strCheckedNos);
		return appendMenus.toString();
	}

	/**
	 * 将实体转为树形
	 * 
	 * @param ens
	 * @param rootNo
	 * @param checkIds
	 */
	private StringBuilder appendMenus = new StringBuilder();
	private StringBuilder appendMenuSb = new StringBuilder();

	public final void TansEntitiesToGenerTree(Entities ens, String rootNo,
			String checkIds) {
		Object tempVar = ens.GetEntityByKey(rootNo);
		EntityMultiTree root = (EntityMultiTree) ((tempVar instanceof EntityMultiTree) ? tempVar
				: null);
		if (root == null) {
			throw new RuntimeException("@没有找到rootNo=" + rootNo + "的entity.");
		}
		appendMenus.append("[{");
		appendMenus.append("\"id\":\"" + rootNo + "\"");
		appendMenus.append(",\"text\":\"" + root.getName() + "\"");
		appendMenus.append(",\"state\":\"open\"");

		// attributes
		BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((root instanceof BP.WF.Template.FlowFormTree) ? root
				: null);
		if (formTree != null) {
			String url = formTree.getUrl() == null ? "" : formTree.getUrl();
			url = url.replace("/", "|");
			appendMenus.append(",\"attributes\":{\"NodeType\":\""
					+ formTree.getNodeType() + "\",\"IsEdit\":\""
					+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
		}
		// 增加它的子级.
		appendMenus.append(",\"children\":");
		AddChildren(root, ens, checkIds);
		appendMenus.append(appendMenuSb);
		appendMenus.append("}]");
	}

	public final void AddChildren(EntityMultiTree parentEn, Entities ens,
			String checkIds) {
		appendMenus.append(appendMenuSb);
		appendMenuSb = new StringBuilder();
		// appendMenuSb.clear();

		appendMenuSb.append("[");
		for (Entity en : ens) {
			EntityMultiTree item = (EntityMultiTree) en;
			if (!item.getParentNo().equals(parentEn.getNo())) {
				continue;
			}

			if (checkIds.contains("," + item.getNo() + ",")) {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":true");
			} else {
				appendMenuSb.append("{\"id\":\"" + item.getNo()
						+ "\",\"text\":\"" + item.getName()
						+ "\",\"checked\":false");
			}

			// attributes
			BP.WF.Template.FlowFormTree formTree = (BP.WF.Template.FlowFormTree) ((item instanceof BP.WF.Template.FlowFormTree) ? item
					: null);
			if (formTree != null) {
				String url = formTree.getUrl() == null ? "" : formTree.getUrl();
				String ico = "icon-tree_folder";
				String treeState = "closed";
				url = url.replace("/", "|");
				appendMenuSb.append(",\"attributes\":{\"NodeType\":\""
						+ formTree.getNodeType() + "\",\"IsEdit\":\""
						+ formTree.getIsEdit() + "\",\"Url\":\"" + url + "\"}");
				// 图标
				if (formTree.getNodeType().equals("form")) {
					ico = "icon-sheet";
				}
				appendMenuSb.append(",\"state\":\"" + treeState + "\"");
				appendMenuSb.append(",iconCls:\"");
				appendMenuSb.append(ico);
				appendMenuSb.append("\"");
			}
			// 增加它的子级.
			appendMenuSb.append(",\"children\":");
			AddChildren(item, ens, checkIds);
			appendMenuSb.append("},");
		}
		if (appendMenuSb.length() > 1) {
			appendMenuSb = appendMenuSb.deleteCharAt(appendMenuSb.length() - 1);
		}
		appendMenuSb.append("]");
		appendMenus.append(appendMenuSb);
		appendMenuSb = new StringBuilder();
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#region 表单方案.
	/**
	 * 表单方案
	 * 
	 * @return
	 */
	public final String BindFrms_Init() {

		FrmNodes fns = new FrmNodes(this.getFK_Flow(), this.getFK_Node());

		boolean isHaveNDFrm = false;
		for (FrmNode fn : fns.Tolist()) {
			if (fn.getFK_Frm().equals("ND" + this.getFK_Node())) {
				isHaveNDFrm = true;
				break;
			}
		}

		if (isHaveNDFrm == false) {
			FrmNode fn = new FrmNode();
			fn.setFK_Flow(this.getFK_Flow());
			fn.setFK_Frm("ND" + this.getFK_Node());
			fn.setFK_Node(this.getFK_Node());

			fn.setFrmEnableRole(FrmEnableRole.Disable); // 就是默认不启用.
			fn.setFrmSln(0);
			// fn.IsEdit = true;
			fn.setIsEnableLoadData(true);

			fn.setFK_Frm("ND" + this.getFK_Node());
 
 			fn.Insert();
 
			fns.AddEntity(fn);
		}
		// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		// /#endregion 如果没有ndFrm 就增加上.

		// 组合这个实体才有外键信息.
		FrmNodeExts fnes = new FrmNodeExts();
		for (FrmNode fn : fns.Tolist()) {
			MapData md = new MapData();
			md.setNo(fn.getFK_Frm());
			if (md.getIsExits() == false) {
				fn.Delete(); // 说明该表单不存在了，就需要把这个删除掉.
				continue;
			}

			FrmNodeExt myen = new FrmNodeExt(fn.getMyPK());
			fnes.AddEntity(myen);
		}

		// 把json数据返回过去.
		return fnes.ToJson();
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	public final String BindFrms_Delete() {
		FrmNodeExt myen = new FrmNodeExt(this.getMyPK());
		myen.Delete();
		return "删除成功.";
	}

	public final String BindFrms_DoOrder() {
		FrmNode myen = new FrmNode(this.getMyPK());

		if (this.GetRequestVal("OrderType").equals("Up")) {
			myen.DoUp();
		} else {
			myen.DoDown();
		}

		return "执行成功...";
	}
	 private class FieldsAttrs
		{
			public int idx;
			public String KeyOfEn;
			public String Name;
			public String LGTypeT;
			public boolean UIVisible;
			public boolean UIIsEnable;
			public boolean IsSigan;
			public String DefVal;
			public boolean IsNotNull;
			public String RegularExp;
			public boolean IsWriteToFlowTable;
			/** 
			  add new attr 是否写入流程注册表
			 
			*/
			public String IsWriteToGenerWorkFlow;
		}
	
	// /#endregion 表单方案.

	
	// /#region 字段权限.
	public final String Fields_Init()
	{
		// 查询出来解决方案.
		FrmFields fss = new FrmFields(this.getFK_MapData(), this.getFK_Node());

		// 处理好.
		MapAttrs attrs = new MapAttrs();
		//增加排序
		QueryObject obj = new QueryObject(attrs);
		obj.AddWhere(MapAttrAttr.FK_MapData, this.getFK_MapData());
		obj.addOrderBy(MapAttrAttr.Y, MapAttrAttr.X);
		obj.DoQuery();

		java.util.ArrayList<FieldsAttrs> fieldsAttrsList = new java.util.ArrayList<FieldsAttrs>();
		int idx = 0;
		for (MapAttr attr : attrs.ToJavaList())
		{
			if(attr.getKeyOfEn() == BP.WF.WorkAttr.RDT)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.FID)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.OID)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.Rec)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.MyNum)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.MD5)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.Emps)
				continue;
			else if(attr.getKeyOfEn() == BP.WF.WorkAttr.CDT)
				continue;

			fieldsAttrsList.add(new FieldsAttrs());
			fieldsAttrsList.get(idx).idx = idx;
			fieldsAttrsList.get(idx).KeyOfEn = attr.getKeyOfEn();
			fieldsAttrsList.get(idx).Name=attr.getName();
			fieldsAttrsList.get(idx).LGTypeT = attr.getLGTypeT();

			Object tempVar = fss.GetEntityByKey(FrmFieldAttr.KeyOfEn, attr.getKeyOfEn());
			FrmField sln = (FrmField)((tempVar instanceof FrmField) ? tempVar : null);
			if (sln == null)
			{
				fieldsAttrsList.get(idx).UIVisible = false;
				fieldsAttrsList.get(idx).UIIsEnable = false;
				fieldsAttrsList.get(idx).IsSigan = false;
				fieldsAttrsList.get(idx).DefVal = "";
				fieldsAttrsList.get(idx).IsNotNull = false;
				fieldsAttrsList.get(idx).IsSigan = false;
				fieldsAttrsList.get(idx).RegularExp = "";
				fieldsAttrsList.get(idx).IsWriteToFlowTable = false;
				//fieldsAttrsList[idx].IsWriteToGenerWorkFlow = sln.IsWriteToGenerWorkFlow;

				//this.Pub2.AddTD("<a href=\"javascript:EditSln('" + this.FK_MapData + "','" + this.SlnString + "','" + attr.KeyOfEn + "')\" >Edit</a>");
			}
			else
			{

				fieldsAttrsList.get(idx).UIVisible = sln.getUIVisible();
				fieldsAttrsList.get(idx).UIIsEnable = sln.getUIIsEnable();
				fieldsAttrsList.get(idx).IsSigan = sln.getIsSigan();
				fieldsAttrsList.get(idx).DefVal = sln.getDefVal();
				fieldsAttrsList.get(idx).IsNotNull = sln.getIsNotNull();
				fieldsAttrsList.get(idx).IsSigan = sln.getIsSigan();
				fieldsAttrsList.get(idx).RegularExp = sln.getRegularExp();
				fieldsAttrsList.get(idx).IsWriteToFlowTable = sln.getIsWriteToFlowTable();
				//fieldsAttrsList[idx].IsWriteToGenerWorkFlow = sln.IsWriteToGenerWorkFlow;

				//this.Pub2.AddTD("<a href=\"javascript:DelSln('" + this.FK_MapData + "','" + this.FK_Flow + "','" + this.FK_Node + "','" + this.FK_Node + "','" + attr.KeyOfEn + "')\" ><img src='../Img/Btn/Delete.gif' border=0/>Delete</a>");
			}

			idx++;
			//this.Pub2.AddTREnd();
		}
		//this.Pub2.AddTableEnd();

		//Button btn = new Button();
		//btn.ID = "Btn_Save";
		//btn.Click += new EventHandler(btn_Field_Click);
		//btn.Text = "保存";
		//this.Pub2.Add(btn); //保存.

		//if (fss.Count != 0)
		//{
		//    btn = new Button();
		//    btn.ID = "Btn_Del";
		//    btn.Click += new EventHandler(btn_Field_Click);
		//    btn.Text = " Delete All ";
		//    btn.Attributes["onclick"] = "return confirm('Are you sure?');";
		//    this.Pub2.Add(btn); //删除定义..
		//}

		//if (dtNodes.Rows.Count >= 1)
		//{
		//    //btn = new Button();
		//    //btn.ID = "Btn_Copy";
		//    ////btn.Click += new EventHandler(btn_Field_Click);
		//    //btn.Text = " Copy From Node ";
		//    //btn.Attributes["onclick"] = "";
		//    this.Pub2.Add("<input type=button value='从其他节点上赋值权限' onclick=\"javascript:CopyIt('" + this.FK_MapData + "','" + this.FK_Flow + "','" + this.FK_Node + "')\">"); //删除定义..
		//}


		String fsj = Json.ToJson(fieldsAttrsList);
		return Json.ToJson(fieldsAttrsList);
	}

	public final String Fields_Save() {
		return "";
	}

	
	// /#endregion 字段权限.

	
	
	  ///#region 附件权限.
			public class AthsAttrs
			{
				public int idx;
				public String NoOfObj;
				public String Name;
				public String UploadTypeT;
				public String PrimitiveAttrTag;
				public String EditTag;
				public String DelTag;
			}
			public final String Aths_Init()
			{
				BP.Sys.FrmAttachments fas = new BP.Sys.FrmAttachments();
				fas.Retrieve(FrmAttachmentAttr.FK_MapData, this.getFK_MapData());

				java.util.ArrayList<AthsAttrs> athsAttrsList = new java.util.ArrayList<AthsAttrs>();
				int idx = 0;
				for (BP.Sys.FrmAttachment item : fas.ToJavaList())
				{
					if (item.getFK_Node() != 0)
					{
						continue;
					}

					athsAttrsList.add(new AthsAttrs ());
					athsAttrsList.get(idx).idx = idx + 1;
					athsAttrsList.get(idx).NoOfObj = item.getNoOfObj();
					athsAttrsList.get(idx).Name=item.getName();
					athsAttrsList.get(idx).UploadTypeT = item.getUploadTypeT();
					athsAttrsList.get(idx).PrimitiveAttrTag = "<a href=\"javascript:EditFJYuanShi('" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">原始属性</a>";
					athsAttrsList.get(idx).EditTag = "<a href=\"javascript:EditFJ('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">编辑</a>";

					FrmAttachment en = new FrmAttachment();
					en.setMyPK(this.getFK_MapData() + "_" + item.getNoOfObj() + "_" + this.getFK_Node());
					if (en.RetrieveFromDBSources() == 0)
					{
						athsAttrsList.get(idx).DelTag = "";
					}
					else
					{
						athsAttrsList.get(idx).DelTag = "<a href=\"javascript:DeleteFJ('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNoOfObj() + "')\">删除</a>";
					}

					idx++;
				}

				return Json.ToJson(athsAttrsList);
			}
			
			
	public final String Aths_Save() {
		return "";
	}

	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion 附件权限.
	
	  ///#region 从表权限.
			public class DtlsAttrs
			{
				public int idx;
				public String No;
				public String Name;
				/** 
				 原始属性标签
				 
				*/
				public String PrimitiveAttrTag;
				public String EditTag;
				public String DelTag;
			}
	
	// /#region 从表权限.
	public final String Dtls_Init() {
		 BP.Sys.MapDtls dtls = new BP.Sys.MapDtls();
			dtls.Retrieve(MapDtlAttr.FK_MapData, this.getFK_MapData());
			java.util.ArrayList<DtlsAttrs> dtlsAttrsList = new java.util.ArrayList<DtlsAttrs>();
			int idx = 0;

			for (BP.Sys.MapDtl item : dtls.ToJavaList())
			{
				if (item.getFK_Node() != 0)
				{
					continue;
				}

				dtlsAttrsList.add(new DtlsAttrs ());
				dtlsAttrsList.get(idx).idx = idx + 1;
				dtlsAttrsList.get(idx).No = item.getNo();
				dtlsAttrsList.get(idx).Name=item.getName();
				dtlsAttrsList.get(idx).PrimitiveAttrTag = "<a href=\"javascript:EditDtlYuanShi('" + this.getFK_MapData() + "','" + item.getNo() + "')\">原始属性</a>";
				dtlsAttrsList.get(idx).EditTag = "<a href=\"javascript:EditDtl('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNo() + "')\">编辑</a>";

				MapDtl en = new MapDtl();
				en.setNo(item.getNo() + "_" + this.getFK_Node());
				if (en.RetrieveFromDBSources() == 0)
				{
					dtlsAttrsList.get(idx).DelTag = "";
				}
				else
				{
					dtlsAttrsList.get(idx).DelTag = "<a href=\"javascript:DeleteDtl('" + this.getFK_Node() + "','" + this.getFK_MapData() + "','" + item.getNo() + "')\">删除</a>";
				}

				idx++;
			}

			return Json.ToJson(dtlsAttrsList);
	}

	public final String Dtls_Save() {
		return "";
	}
	// C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	// /#endregion 从表权限.

}