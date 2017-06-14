package org.jflow.framework.common.model;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import BP.Port.Emp;
import BP.Port.WebUser;
import BP.Sys.PubClass;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.DoWhatList;

public class PortModel extends BaseModel {
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(PortModel.class);

	public PortModel(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public final String getDoWhat() {
		return getParameter("DoWhat");
	}

	public final String getUserNo() {
		return getParameter("UserNo");
	}

	public final String getSID() {
		return getParameter("SID");
	}

	public final void loadPage() {
		if (!StringHelper.isNullOrEmpty(getUserNo()) && !StringHelper.isNullOrEmpty(getSID())) {
			try {
				String uNo = "";
				if (this.getUserNo().equals(SystemConfig.getAppSettings().get("Admin").toString())) {
					uNo = "zhoupeng";
				} else {
					uNo = this.getUserNo();
				}

				String sql = "SELECT SID FROM Port_Emp WHERE No='" + uNo + "'";
				String sid = BP.DA.DBAccess.RunSQLReturnString(sql);
				if (!this.getSID().equals(sid)) {
					ToErrorPage("非法的访问，请与管理员联系。sid=" + sid);
					// this.Response.Write("非法的访问，请与管理员联系。sid=" + sid);
					return;
				} else {
					Emp emL = new Emp(this.getUserNo());
					WebUser.setToken(get_request().getSession().getId());
					WebUser.SignInOfGenerLang(emL, SystemConfig.getSysLanguage());
				}
			} catch (RuntimeException ex) {
				ToErrorPage("@有可能您没有配置好ccflow的安全验证机制:" + ex.getMessage());
				return;
				// throw new RuntimeException("@有可能您没有配置好ccflow的安全验证机制:" +
				// ex.getMessage());
			}
		}

		if (StringHelper.isNullOrEmpty(getUserNo())) {
			ToErrorPage("用户名不能为空！");
			return;
		}

		Emp em = new Emp(this.getUserNo());
		WebUser.setToken(get_request().getSession().getId());
		WebUser.SignInOfGenerLang(em, SystemConfig.getSysLanguage());

		String paras = "";
		//
		Enumeration enu = get_request().getParameterNames();
		while (enu.hasMoreElements()) {
			// 判断是否有内容，hasNext()
			String key = (String) enu.nextElement();
			String val = getParameter(key);
			if (val.indexOf('@') != -1) {
				winCloseWithMsg("您没有能参数: [ " + key + " ," + val + " ] 给值 ，URL 将不能被执行。");
				// throw new RuntimeException("您没有能参数: [ " + key + " ," + val +
				// " ] 给值 ，URL 将不能被执行。");
				return;
			}

			// switch (str)
			if (key.equals(DoWhatList.DoNode) || key.equals(DoWhatList.Emps) || key.equals(DoWhatList.EmpWorks)
					|| key.equals(DoWhatList.FlowSearch) || key.equals(DoWhatList.Login)
					|| key.equals(DoWhatList.MyFlow) || key.equals(DoWhatList.MyWork) || key.equals(DoWhatList.Start)
					|| key.equals(DoWhatList.Start5) || key.equals(DoWhatList.JiSu) || key.equals(DoWhatList.StartSmall)
					|| key.equals(DoWhatList.FlowFX) || key.equals(DoWhatList.DealWork)
					|| key.equals(DoWhatList.DealWorkInSmall) || key.equals("FK_Flow") || key.equals("WorkID")
					|| key.equals("FK_Node") || key.equals("SID")) {
				// case DoWhatList.CallMyFlow:
			} else {
				paras += "&" + key + "=" + val;
			}
		}

		// if (this.IsPostBack == false)
		// {
		if (this.IsCanLogin() == false) {
			ToMsgPage("安全验证错误<br>系统无法执行您的请求，可能是您的登陆时间太长，请重新登陆。<br>如果您要取消安全验证请修改web.config 中IsDebug 中的值设置成1。");
			return;
		}

		BP.Port.Emp emp = new BP.Port.Emp(this.getUserNo());
		WebUser.SignInOfGener(emp); // 开始执行登陆。

		String nodeID = String.valueOf(Integer.parseInt(this.getFK_Flow() + "01"));

		if (this.getDoWhat().equals(DoWhatList.OneWork)) // 工作处理器调用.
		{
			if (this.getFK_Flow() == null || this.getWorkID() == 0) {
				ToErrorPage("@参数 FK_Flow 或者 WorkID 为Null 。");
				return;
				// throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为Null 。");
			}
			sendRedirect(
					"/WF/WFRpt.jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras);
		} else if (this.getDoWhat().equals(DoWhatList.JiSu)) // 极速模式的方式发起工作
		{
			sendWork();
			// if (this.getFK_Flow() == null)
			// {
			// sendRedirect("./App/Simple/Default.jsp");
			// }
			// else
			// {
			// sendRedirect("./App/Simple/Default.jsp?FK_Flow=" +
			// this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
			// }

		} else if (this.getDoWhat().equals(DoWhatList.Start5)) // 发起工作
		{
			sendWork();
			// if (this.getFK_Flow() == null)
			// {
			//// sendRedirect("./App/Classic/Default.jsp");
			// sendRedirect("./Default.jsp");
			// }
			// else
			// {
			// //sendRedirect("./Default.jsp?FK_Flow=" + this.getFK_Flow() +
			// paras + "&FK_Node=" + nodeID);
			// String url = "./MyFlow.jsp?FK_Flow=" + this.getFK_Flow() +
			// "&FK_Node=" + nodeID + "&DoWhat=" + getDoWhat();
			// try {
			// PubClass.WinOpen(get_response(), url, 800, 600);
			// } catch (IOException e) {
			// logger.info("ERROR", e);
			// }
			// }
		}
		// else if (this.getDoWhat().equals(DoWhatList.StartLigerUI))
		// {
		// if (this.getFK_Flow() == null)
		// {
		// sendRedirect("../AppDemoLigerUI/Default.jsp");
		// }
		// else
		// {
		// sendRedirect("../AppDemoLigerUI/Default.jsp?FK_Flow=" +
		// this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
		// }
		// }
		// ORIGINAL LINE: case DoWhatList.Amaze:
		// else if (this.getDoWhat().equals(DoWhatList.Amaze))
		// {
		// if (this.getFK_Flow() == null)
		// {
		// sendRedirect("./App/Amaz/Default.jsp");
		// }
		// else
		// {
		// sendRedirect("./App/Amaz/Default.jsp?FK_Flow=" + this.getFK_Flow() +
		// paras + "&FK_Node=" + nodeID);
		// }
		// }
		else if (this.getDoWhat().equals(DoWhatList.Start)) // 发起工作
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("Start.jsp");
			} else {
				sendRedirect("MyFlow.jsp?FK_Flow=" + this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
			}
		} else if (this.getDoWhat().equals(DoWhatList.StartSmall)) // 发起工作 小窗口
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("Start.jsp?FK_Flow=" + this.getFK_Flow() + paras);
			} else {
				sendRedirect("MyFlow.jsp?FK_Flow=" + this.getFK_Flow() + paras);
			}
		} else if (this.getDoWhat().equals(DoWhatList.StartSmallSingle)) // 发起工作单独小窗口
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("Start.jsp?FK_Flow=" + this.getFK_Flow() + paras + "&IsSingle=1&FK_Node=" + nodeID);
			} else {
				sendRedirect("MyFlowSmallSingle.jsp?FK_Flow=" + this.getFK_Flow() + paras + "&FK_Node=" + nodeID);
			}
		} else if (this.getDoWhat().equals(DoWhatList.Runing)) // 在途中工作
		{
			sendRedirect("Runing.jsp?FK_Flow=" + this.getFK_Flow());
		} else if (this.getDoWhat().equals(DoWhatList.Tools)) // 工具栏目。
		{
			sendRedirect("Tools.jsp");
		} else if (this.getDoWhat().equals(DoWhatList.ToolsSmall)) // 小工具栏目。
		{
			sendRedirect("Tools.jsp?RefNo=" + getRefNo());
		} else if (this.getDoWhat().equals(DoWhatList.EmpWorks)) // 我的工作小窗口
		{
			if (this.getFK_Flow() == null || this.getFK_Flow().equals("")) {
				sendRedirect("EmpWorks.jsp");
			} else {
				sendRedirect("EmpWorks.jsp?FK_Flow=" + this.getFK_Flow());
			}
		} else if (this.getDoWhat().equals(DoWhatList.Login)) {
			if (this.getFK_Flow() == null) {
				sendRedirect("EmpWorks.jsp");
			} else {
				sendRedirect("EmpWorks.jsp?FK_Flow=" + this.getFK_Flow());
			}
		} else if (this.getDoWhat().equals(DoWhatList.Emps)) // 通讯录。
		{
			sendRedirect("Emps.jsp");
		} else if (this.getDoWhat().equals(DoWhatList.FlowSearch)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("FlowSearch.jsp");
			} else {
				sendRedirect("/Rpt/Search.jsp?Endse=s&FK_Flow=001&EnsName=ND" + Integer.parseInt(this.getFK_Flow())
						+ "Rpt" + paras);
			}
		} else if (this.getDoWhat().equals(DoWhatList.FlowSearchSmall)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("FlowSearch.jsp");
			} else {
				sendRedirect("./Comm/Search.jsp?EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras);
			}
		} else if (this.getDoWhat().equals(DoWhatList.FlowSearchSmallSingle)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				sendRedirect("FlowSearchSmallSingle.jsp");
			} else {
				sendRedirect("./Comm/Search.jsp?EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras);
			}
		} else if (this.getDoWhat().equals(DoWhatList.FlowFX)) // 流程查询。
		{
			if (this.getFK_Flow() == null) {
				throw new RuntimeException("@没有参数流程编号。");
			}

			sendRedirect("./Comm/Group.jsp?EnsName=ND" + Integer.parseInt(this.getFK_Flow()) + "Rpt" + paras);
		} else if (this.getDoWhat().equals(DoWhatList.DealWork)) {
			if (this.getFK_Flow() == null || this.getWorkID() == 0) {
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为Null 。");
			}
			sendRedirect("MyFlow.jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras);
		} else if (this.getDoWhat().equals(DoWhatList.DealWorkInSmall)) {
			if (this.getFK_Flow() == null || this.getWorkID() == 0) {
				throw new RuntimeException("@参数 FK_Flow 或者 WorkID 为Null 。");
			}

			sendRedirect("MyFlow.jsp?FK_Flow=" + this.getFK_Flow() + "&WorkID=" + this.getWorkID() + "&o2=1" + paras);
		} else {
			ToErrorPage("没有约定的标记:DoWhat=" + this.getDoWhat());
		}
	}

	/**
	 * 验证登陆用户是否合法
	 * 
	 * @return
	 */
	public final boolean IsCanLogin() {
		if (null == SystemConfig.getAppSettings().get("IsAuth")) {
			return true;
		}
		if (SystemConfig.getAppSettings().get("IsAuth").equals("1")) {
			if (!this.getSID().equals(this.GetKey())) {
				if (SystemConfig.getIsDebug()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public final String GetKey() {
		return BP.DA.DBAccess.RunSQLReturnString("SELECT SID From Port_Emp WHERE no='" + this.getUserNo() + "'");
	}

	/**
	 * 发起工作
	 */
	private void sendWork() {
		if (!getFK_Flow().equals("")) {
			String nodeID = String.valueOf(Integer.parseInt(this.getFK_Flow() + "01"));
			String url = "./MyFlow.jsp?FK_Flow=" + this.getFK_Flow() + "&FK_Node=" + nodeID + "&DoWhat=" + getDoWhat();
			try {
				PubClass.WinOpen(get_response(), url, 800, 600);
			} catch (IOException e) {
				logger.info("ERROR", e);
			}
		}
	}

}
