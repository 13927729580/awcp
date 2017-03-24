package org.jflow.framework.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import BP.DA.*;
import BP.Port.WebUser;
import BP.Sys.*;
import BP.Tools.StringHelper;

@Controller
@RequestMapping("/WF/Comm")
public class HelperOfTBEUIController {
	private HttpServletRequest _request = null;
	private HttpServletResponse _response = null;

	public String getUTF8ToString(String param) {
		try {
			return java.net.URLDecoder.decode(_request.getParameter(param),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public int getgetTaps() {
		try {
			return Integer.parseInt(_request.getParameter("getTaps"));
		} catch (java.lang.Exception e) {
			return 100;
		}
	}

	@RequestMapping(value = "/HelperOfTBEUI", method = RequestMethod.GET)
	public void executeHelper(HttpServletRequest request,
			HttpServletResponse response) {
		_request = request;
		_response = response;
		treeResult.setLength(0);
		treesb.setLength(0);
		String method = "";
		// 返回值
		String s_responsetext = "";
		if (StringHelper.isNullOrEmpty(request.getParameter("method"))) {
			return;
		}

		method = request.getParameter("method").toString();

		if (method.equals("getMyData")) {
			s_responsetext = getMyData();
		} else if (method.equals("insertSameNodeMet")) {
			s_responsetext = insertSameNodeMet();
		} else if (method.equals("insertSonNodeMet")) {
			s_responsetext = insertSonNodeMet();
		} else if (method.equals("editNodeMet")) {
			s_responsetext = editNodeMet();
		} else if (method.equals("delNodeMet")) {
			s_responsetext = delNodeMet();
		}
		if (StringHelper.isNullOrEmpty(s_responsetext)) {
			s_responsetext = "";
		}
		// 组装ajax字符串格式,返回调用客户端
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getOutputStream().write(s_responsetext.replace("][", "],[").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String insertSameNodeMet() {
		String setFk_Emp = null;
		if (getgetTaps() == 1) // 我的词汇FK_Emp为当前登录人账号
		{
			setFk_Emp = WebUser.getNo();
		} else {
			setFk_Emp = "";
		}
		String selectId = getUTF8ToString("selectId");
		String setText = getUTF8ToString("setText");
		String sql = String.format(
				"select ParentNo from Sys_DefVal where No='%s'", selectId);
		String isParentNo = DBAccess.RunSQLReturnTable(sql)
				.getValue(0, "ParentNo").toString();
		if (Integer.parseInt(isParentNo) == 0) // 说明当前选中节点是父节点(根节点)
		{
			DefVal Dv = new DefVal();
			Dv.setFK_Emp(setFk_Emp);
			Dv.setVal(setText);
			Dv.setParentNo("0");
			Dv.setIsParent("1"); // 父节点增加同级节点---赋值为"1"
			Dv.Insert();
		} else {
			DefVal Dv = new DefVal();
			Dv.setFK_Emp(setFk_Emp);
			Dv.setVal(setText);
			Dv.setParentNo(isParentNo);
			Dv.setIsParent("0"); // 子节点增加同级节点---赋值为"0"
			Dv.Insert();
		}

		return "";
	}

	/**
	 * 所有节点都可以增加--------(旧)只有父节点为"0"的节点才可以添加子节点
	 * 
	 * @return
	 */
	private String insertSonNodeMet() {
		String setFk_Emp = null;
		if (getgetTaps() == 1) // 我的词汇FK_Emp为当前登录人账号
		{
			setFk_Emp = WebUser.getNo();
		} else {
			setFk_Emp = "";
		}
		String selectId = getUTF8ToString("selectId");
		String setText = getUTF8ToString("setText");
		String sql = String.format(
				"select ParentNo from Sys_DefVal where No='%s'", selectId);
		String isParentNo = DBAccess.RunSQLReturnTable(sql)
				.getValue(0, "ParentNo").toString();
		// if (isParentNo == 0)//说明当前选中节点是父节点(根节点)
		// {
		DefVal Dv = new DefVal();
		Dv.setFK_Emp(setFk_Emp);
		Dv.setVal(setText);
		Dv.setParentNo(selectId);
		Dv.setIsParent("0"); // 子节点增加子节点---赋值为"0"
		Dv.Insert();

		DefVal DvuUpFatNode = new DefVal(); // 所选节点增加子节点后---当前所选节点的IsParent属性赋值为"1"
		DvuUpFatNode.Retrieve(DefValAttr.No, selectId);
		DvuUpFatNode.setIsParent("1");
		DvuUpFatNode.Update();
		// }

		return "";
	}

	/**
	 * 编辑节点---所有节点都可编辑
	 * 
	 * @return
	 */
	private String editNodeMet() {
		String selectId = getUTF8ToString("selectId");
		String setText = getUTF8ToString("setText");

		DefVal Dv = new DefVal();
		Dv.Retrieve(DefValAttr.No, selectId);
		Dv.setVal(setText);
		Dv.Update();

		return "";
	}

	/**
	 * 删除节点--如果父节点还有子节点--禁止删除此父节点
	 * 
	 * @return
	 */
	private String delNodeMet() {
		String selectId = getUTF8ToString("selectId");
		//String setText = getUTF8ToString("setText");
		String sql = String.format(
				"select ParentNo from Sys_DefVal where No='%s'", selectId);
		String isParentNo = DBAccess.RunSQLReturnTable(sql)
				.getValue(0, "ParentNo").toString();
		if (Integer.parseInt(isParentNo) == 0) // 父节点
		{
			String canNotDelSql = "select No from Sys_DefVal where ParentNo='0'";
			if (DBAccess.RunSQLReturnCOUNT(canNotDelSql) == 1) // 此处限制最后一个父节点不可以删除，避免空数据报错的bug
			{
				return "";
			}
			DefVal Dv = new DefVal();
			Dv.Delete(DefValAttr.No, selectId);
		} else // 子节点删除不做限制
		{
			String upParent = String.format(
					"select ParentNo from Sys_DefVal where No='%s'", selectId);
			String getParentNo = DBAccess.RunSQLReturnTable(upParent)
					.getValue(0, "ParentNo").toString();
			String isFirstParent = String.format(
					"select ParentNo from Sys_DefVal where No='%s'",
					getParentNo);

			DefVal Dv = new DefVal();
			Dv.Delete(DefValAttr.No, selectId);

			String hasSonOrNot = String.format(
					"select No from Sys_DefVal where ParentNo='%s'",
					getParentNo);
			if (DBAccess.RunSQLReturnCOUNT(hasSonOrNot) == 0
					&& Integer.parseInt(DBAccess
							.RunSQLReturnTable(isFirstParent)
							.getValue(0, "ParentNo").toString()) != 0) {
				DefVal DvUp = new DefVal();
				DvUp.Retrieve(DefValAttr.No, getParentNo);
				DvUp.setIsParent("0");
				DvUp.Update();
			}
		}
		return "";
	}

	private String getMyData() {

		DefVal dv = new DefVal();
		dv.CheckPhysicsTable();
		// 初始化数据库---如果为空,添加默认数据
		String MyhasNoDataSql = String.format(
				"SELECT * FROM Sys_DefVal where FK_Emp='%s' AND ParentNo=0",
				WebUser.getNo());
		String PubhasNoDataSql = "SELECT * FROM Sys_DefVal where FK_Emp='' AND ParentNo=0";

		if (DBAccess.RunSQLReturnCOUNT(MyhasNoDataSql) == 0) // 我的词汇
		{
			DefVal MyDv = new DefVal();
			MyDv.setVal("我的词汇");
			MyDv.setFK_Emp(WebUser.getNo());
			MyDv.setParentNo("0");
			MyDv.setIsParent("1");
			MyDv.Insert();
		}

		if (DBAccess.RunSQLReturnCOUNT(PubhasNoDataSql) == 0) // 全局词汇
		{
			DefVal PubDv = new DefVal();
			PubDv.setVal("全局词汇");
			PubDv.setFK_Emp("");
			PubDv.setParentNo("0");
			PubDv.setIsParent("1");
			PubDv.Insert();
		}

		String getMyDataSql = null;
		String setFk_Emp = null;
		if (getgetTaps() == 1) // 我的词汇FK_Emp为当前登录人账号
		{
			setFk_Emp = WebUser.getNo();
			getMyDataSql = String
					.format("select NO,Val,ParentNo,IsParent from Sys_DefVal where FK_Emp='%s' and 1=1 ",
							setFk_Emp);
		} else {
			setFk_Emp = "";
			getMyDataSql = String
					.format("select NO,Val,ParentNo,IsParent from Sys_DefVal where FK_Emp='%s' and 1=1 ",
							setFk_Emp);
		}
		DataTable dt_dept = DBAccess.RunSQLReturnTable(getMyDataSql+ " ORDER BY No");
		String s_responsetext = "";
		String s_checkded = "";

		s_responsetext = GetTreeJsonByTable(getMyDataSql, dt_dept, "NO", "Val",
				"ParentNo", "0", "IsParent", s_checkded);

		return s_responsetext;
	}
	
	private String boolToValue(boolean bool)
	{
		if(bool)
			return "1";
		else
			return "0";
	}

	/**
	 * 根据DataTable生成Json树结构
	 */
	private StringBuilder treeResult = new StringBuilder();
	private StringBuilder treesb = new StringBuilder();

	public final String GetTreeJsonByTable(String sql,DataTable tabel, String idCol, String txtCol, String rela, Object pId, String IsParent, String CheckedString)
	{
		String treeJson = "";
		treeResult.append(treesb.toString());
		DataTable tmpTable = null;
		// rows = null;
		treesb.setLength(0);
		if (tabel.Rows.size() > 0)
		{
			treesb.append("[");
			String filer = "";
			if (pId.toString().equals(""))
			{
				filer = String.format("%s is null", rela);
			}
			else
			{
				filer = String.format("%s='%s'", rela, pId);
			}
			//DataRow[] rows = tabel.Select(filer);
			tmpTable = DBAccess.RunSQLReturnTable(sql+" and "+filer+ " ORDER BY No");
			DataRowCollection rows = tmpTable.Rows;
			if (rows.size() > 0)
			{
				for (DataRow row : rows)
				{
					String deptNo = row.getValue(idCol).toString();

					if (treeResult.length() == 0)
					{
						 treesb.append("{\"id\":\"" + row.getValue(idCol)
	                                + "\",\"text\":\"" + row.getValue(txtCol)
	                                  + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}"
	                                   + ",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ",")) + ",\"state\":\"open\"");	
					}
					else {
						String fi = String.format("%s='%s'", rela, row.getValue(idCol));
						DataTable tmpTable2 = DBAccess.RunSQLReturnTable(sql+" and "+fi+ " ORDER BY No");
						DataRowCollection rows2 = tmpTable2.Rows;
						if (rows2.size() > 0)
						{
								//+ "\",\"IsParent\":\"" + row.getValue(IsParent]
							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}" + ",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ",")) + ",\"state\":\"open\"");
							//+ "\",\"checked\":" + CheckedString.Contains("," + row.getValue(idCol] + ",").ToString().ToLower() + ",\"state\":\"open\"");
						}
						else
						{
								//+ "\",\"IsParent\":\"" +row.getValue(IsParent]
							treesb.append("{\"id\":\"" + row.getValue(idCol) + "\",\"text\":\"" + row.getValue(txtCol) + "\",\"attributes\":{\"IsParent\":\"" + row.getValue(IsParent) + "\"}" + ",\"checked\":" + boolToValue(CheckedString.contains("," + row.getValue(idCol) + ",")));
							//+ "\",\"checked\":" + CheckedString.Contains("," + row.getValue(idCol] + ",").ToString().ToLower());
						}
					}

					String fi = String.format("%s='%s'", rela, row.getValue(idCol));
					DataTable tmpTable3 = DBAccess.RunSQLReturnTable(sql+" and "+fi+ " ORDER BY No");
					DataRowCollection rows3 = tmpTable3.Rows;
					if (rows3.size() > 0)
					{
						treesb.append(",\"children\":");
						GetTreeJsonByTable(sql,tabel, idCol, txtCol, rela, row.getValue(idCol), IsParent, CheckedString);
						treeResult.append(treesb.toString());
						treesb.setLength(0);
					}
					treeResult.append(treesb.toString());
					treesb.setLength(0);
					treesb.append("},");
				}
				treesb = treesb.deleteCharAt(treesb.length() - 1);
			}
			treesb.append("]");
			treeResult.append(treesb.toString());
			treeJson = treeResult.toString();
			treesb.setLength(0);
		}
		return treeJson;
	}
}