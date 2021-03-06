package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 GroupField
 
*/
public class GroupField extends EntityOID
{
	@Override
	public UAC getHisUAC()
	{
		UAC uac = new UAC();
		if (BP.Web.WebUser.getNo().equals("admin"))
		{
				// 
			uac.IsDelete = true;
			uac.IsInsert = false;
			uac.IsUpdate = true;
			return uac;
		}
		uac.Readonly();
		uac.IsView = false;
		return uac;
	}


		
	public boolean IsUse = false;
	public final String getEnName()
	{
		return this.GetValStrByKey(GroupFieldAttr.EnName);
	}
	public final void setEnName(String value)
	{
		this.SetValByKey(GroupFieldAttr.EnName, value);
	}
	/** 
	 标签
	 
	*/
	public final String getLab()
	{
		return this.GetValStrByKey(GroupFieldAttr.Lab);
	}
	public final void setLab(String value)
	{
		this.SetValByKey(GroupFieldAttr.Lab, value);
	}
	/** 
	 顺序号
	 
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(GroupFieldAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(GroupFieldAttr.Idx, value);
	}
	/** 
	 控件类型
	 
	*/
	public final String getCtrlType()
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlType);
	}
	public final void setCtrlType(String value)
	{
		this.SetValByKey(GroupFieldAttr.CtrlType, value);
	}
	/** 
	 控件ID
	 
	*/
	public final String getCtrlID()
	{
		return this.GetValStrByKey(GroupFieldAttr.CtrlID);
	}
	public final void setCtrlID(String value)
	{
		this.SetValByKey(GroupFieldAttr.CtrlID, value);
	}

		///#endregion


		
	/** 
	 GroupField
	 
	*/
	public GroupField()
	{
	}
	public GroupField(int oid)
	{
		super(oid);
	}
	/** 
	 EnMap
	 
	*/
	@Override
	public Map getEnMap()
	{
		if (this.get_enMap() != null)
		{
			return this.get_enMap();
		}
		Map map = new Map("Sys_GroupField", "傻瓜表单分组");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);

		map.AddTBIntPKOID();
		map.AddTBString(GroupFieldAttr.Lab, null, "标签", true, false, 0, 500, 20, true);
		map.AddTBString(GroupFieldAttr.EnName, null, "类", false, false, 0, 200, 20);
		map.AddTBInt(GroupFieldAttr.Idx, 99, "顺序号", false, false);


		map.AddTBString(FrmBtnAttr.GUID, null, "GUID", false, false, 0, 128, 20);
		map.AddTBString(GroupFieldAttr.CtrlType, null, "控件类型", false, false, 0, 50, 20);
		map.AddTBString(GroupFieldAttr.CtrlID, null, "控件ID", false, false, 0, 500, 20);
		map.AddTBAtParas(3000);

		RefMethod rm = new RefMethod();
			//rm.Title = "增加字段";
			//rm.Icon = "../WF/Img/Btn/New.gif";
			//rm.ClassMethodName = this.ToString() + ".DoAddField";
			//rm.RefMethodType = RefMethodType.LinkeWinOpen;
			//map.AddRefMethod(rm);

		rm = new RefMethod();
		rm.Title = "删除隶属分组的字段";
		rm.Icon = "../WF/Img/Btn/Delete.gif";
		rm.Warning = "您确定要删除该分组下的所有字段吗？";
		rm.ClassMethodName = this.toString() + ".DoDelAllField";
		rm.refMethodType = RefMethodType.Func;
		map.AddRefMethod(rm);


		this.set_enMap(map);
		return this.get_enMap();
	}

		///#endregion

	/** 
	 删除所有隶属该分组的字段.
	 
	 @return 
	*/
	public final String DoDelAllField()
	{
		String sql = "DELETE FROM Sys_MapAttr WHERE FK_MapData='" + this.getEnName() + "' AND GroupID=" + this.getOID()+" AND KeyOfEn NOT IN ('OID','RDT','REC','RefPK','FID')";
		int i= BP.DA.DBAccess.RunSQL(sql);
		return "删除字段{"+i+"}个，被删除成功, 执行的SQL:"+sql;
	}
	/** 
	 增加字段
	 
	 @return 
	*/
	public final String DoAddField()
	{
		return SystemConfig.getCCFlowWebPath() + "WF/Admin/FoolFormDesigner/FieldTypeList.htm?DoType=AddF&FK_MapData11=" + this.getEnName() + "&GroupField=" + this.getOID(); // DataType.getCurrentDataTime();
	}

	@Override
	protected boolean beforeUpdate()
	{
	  //  this.Update("Lab", this.Lab);
		String sql = "UPDATE Sys_GroupField SET LAB='"+this.getLab()+"' WHERE OID="+this.getOID();
		BP.DA.DBAccess.RunSQL(sql);
		return super.beforeUpdate(); //edited by liuxc,2017-2-9,修复GroupField不能更新的问题
	}



	public final void DoDown()
	{
		this.DoOrderDown(GroupFieldAttr.EnName, this.getEnName(), GroupFieldAttr.Idx);
		return;
	}
	public final void DoUp()
	{
		this.DoOrderUp(GroupFieldAttr.EnName, this.getEnName(), GroupFieldAttr.Idx);
		return;
	}
	@Override
	protected boolean beforeInsert()
	{
		//if (this.IsExit(GroupFieldAttr.EnName, this.EnName, GroupFieldAttr.Lab, this.Lab) == true)
		//    throw new Exception("@已经在("+this.EnName+")里存在("+this.Lab+")的分组了。");
		try
		{
			String sql = "SELECT MAX(IDX) FROM Sys_GroupField WHERE EnName='" + this.getEnName() + "'";
			this.setIdx(DBAccess.RunSQLReturnValInt(sql, 0) + 1);
		}
		catch (java.lang.Exception e)
		{
			this.setIdx(1);
		}
		return super.beforeInsert();
	}
}