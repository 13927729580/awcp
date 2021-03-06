package BP.Sys;

import BP.DA.*;
import BP.En.*;

/** 
 表单报表设置数据存储表
 
*/
public class FrmReportField extends EntityMyPK
{

		
	/** 
	 表单编号
	 
	*/
	public final String getFK_MapData()
	{
		return this.GetValStrByKey(FrmReportFieldAttr.FK_MapData);
	}
	public final void setFK_MapData(String value)
	{
		this.SetValByKey(FrmReportFieldAttr.FK_MapData, value);
	}
	/** 
	 字段名
	 
	*/
	public final String getKeyOfEn()
	{
		return this.GetValStrByKey(FrmReportFieldAttr.KeyOfEn);
	}
	public final void setKeyOfEn(String value)
	{
		this.SetValByKey(FrmReportFieldAttr.KeyOfEn, value);
	}
	/** 
	 显示中文名
	 
	*/
	public final String getName()
	{
		return this.GetValStrByKey(FrmReportFieldAttr.Name);
	}
	public final void setName(String value)
	{
		this.SetValByKey(FrmReportFieldAttr.Name, value);
	}
	/** 
	 列宽
	 
	*/
	public final String getUIWidth()
	{
		return this.GetValStrByKey(FrmReportFieldAttr.UIWidth);
	}
	public final void setUIWidth(String value)
	{
		this.SetValByKey(FrmReportFieldAttr.UIWidth, value);
	}
	/** 
	 是否显示
	 
	*/
	public final boolean getUIVisible()
	{
		return this.GetValBooleanByKey(FrmReportFieldAttr.UIVisible);
	}
	public final void setUIVisible(boolean value)
	{
		this.SetValByKey(FrmReportFieldAttr.UIVisible, value);
	}
	/** 
	 显示顺序
	 
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(FrmReportFieldAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(FrmReportFieldAttr.Idx, value);
	}

		///#endregion


		
	/** 
	 表单报表
	 
	*/
	public FrmReportField()
	{
	}
	/** 
	 表单报表
	 
	 @param mypk
	*/
	public FrmReportField(String mypk)
	{
		this.setMyPK(mypk);
		this.Retrieve();
	}

		///#endregion

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
		Map map = new Map("Sys_FrmRePortField", "表单报表");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.AddMyPK();
		map.AddTBString(FrmReportFieldAttr.FK_MapData, null, "表单编号", true, false, 1, 100, 80);
		map.AddTBString(FrmReportFieldAttr.KeyOfEn, null, "字段名", true, false, 1, 100, 100);
		map.AddTBString(FrmReportFieldAttr.Name, null, "显示中文名", true, false, 1, 200, 200);
		map.AddTBString(FrmReportFieldAttr.UIWidth, "0", "宽度", true, false, 1, 100, 100);
		map.AddBoolean(FrmReportFieldAttr.UIVisible, true, "是否显示", true, true);
		map.AddTBInt(FrmReportFieldAttr.Idx, 0, "显示顺序", true, false);
		this.set_enMap(map);
		return this.get_enMap();
	}

	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setMyPK(this.getFK_MapData() + "_" + this.getKeyOfEn());
		return super.beforeUpdateInsertAction();
	}
}