package BP.WF.DTS;

import BP.DA.DBAccess;
import BP.En.Method;
import BP.Sys.GroupField;
import BP.Sys.GroupFields;

/** 
 删除空白的字段分组
 
*/
public class DeleteBlankGroupField extends Method
{
	/** 
	 删除空白的字段分组
	 
	*/
	public DeleteBlankGroupField()
	{
		this.Title = "删除空白的字段分组";
		this.Help = "";
		this.Icon = "<img src='/WF/Img/Btn/Delete.gif'  border=0 />";
	}
	@Override
	public void Init()
	{

	}
	@Override
	public boolean getIsCanDo()
	{
		return true;
	}
	@Override
	public Object Do()
	{
		GroupFields gfs = new GroupFields();
		gfs.RetrieveAll();

		int delNum = 0;
		for (GroupField item : gfs.ToJavaList())
		{
			int num = 0;
			num += DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapAttr WHERE GroupID=" + item.getOID() + " and FK_MapData='" + item.getEnName() + "'");
			//num += DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_FrmAttachment WHERE GroupID=" + item.OID + " and FK_MapData='" + item.EnName + "'");
			//num += DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapDtl WHERE GroupID=" + item.OID + " and FK_MapData='" + item.EnName + "'");
			//num += DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapFrame WHERE GroupID=" + item.OID + " and FK_MapData='" + item.EnName + "'");
			//num += DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM Sys_MapM2M WHERE GroupID=" + item.OID+" and FK_MapData='"+item.EnName+"'");
			if (num == 0)
			{
				delNum++;
				item.Delete();
			}
		}
		return "执行成功,删除数量:" + delNum;
	}
}