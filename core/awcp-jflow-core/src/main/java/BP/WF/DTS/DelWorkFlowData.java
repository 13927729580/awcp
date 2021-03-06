package BP.WF.DTS;

import BP.DA.DBAccess;
import BP.DTS.DataIOEn;
import BP.DTS.DoType;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Work;
import BP.WF.Template.CCLists;

public class DelWorkFlowData extends DataIOEn
{
	public DelWorkFlowData()
	{
		this.HisDoType = DoType.UnName;
		this.Title = "<font color=red><b>清除流程数据</b></font>";
		//this.HisRunTimeType = RunTimeType.UnName;
		//this.FromDBUrl = DBUrlType.AppCenterDSN;
		//this.ToDBUrl = DBUrlType.AppCenterDSN;
	}
	@Override
	public void Do() throws Exception
	{
		if ( ! BP.Web.WebUser.getNo().equals("admin"))
		{
			throw new RuntimeException("非法用户。");
		}

	  //  DBAccess.RunSQL("DELETE FROM WF_CHOfFlow");
		DBAccess.RunSQL("DELETE FROM WF_Bill");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkerlist");
		DBAccess.RunSQL("DELETE FROM WF_GenerWorkFlow");
	  //  DBAccess.RunSQL("DELETE FROM WF_WORKLIST");
		DBAccess.RunSQL("DELETE FROM WF_ReturnWork");
		DBAccess.RunSQL("DELETE FROM WF_GECheckStand");
		DBAccess.RunSQL("DELETE FROM WF_GECheckMul");
	//    DBAccess.RunSQL("DELETE FROM WF_ForwardWork");
		DBAccess.RunSQL("DELETE FROM WF_SelectAccper");

		// 删除.
		CCLists ens = new CCLists();
		ens.ClearTable();

		Nodes nds = new Nodes();
		nds.RetrieveAll();

		String msg = "";
		for (Node nd : nds.ToJavaList())
		{

			Work wk = null;
			try
			{
				wk = nd.getHisWork();
				DBAccess.RunSQL("DELETE FROM " + wk.getEnMap().getPhysicsTable());
			}
			catch (RuntimeException ex)
			{
				wk.CheckPhysicsTable();
				msg += "@" + ex.getMessage();
			}
		}

		if (!msg.equals(""))
		{
			throw new RuntimeException(msg);
		}
	}
}