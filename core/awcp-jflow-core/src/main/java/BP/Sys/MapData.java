package BP.Sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BP.DA.*;
import BP.En.*;
import BP.Tools.StringHelper;
import BP.WF.RefObject;

/** 
 映射基础
 
*/
public class MapData extends EntityNoName
{

	/** 是否关键字查询
	 
	*/
	public static final String RptIsSearchKey = "RptIsSearchKey";
	/** 
	 时间段查询方式
	 
	*/
	public static final String RptDTSearchWay = "RptDTSearchWay";
	/** 
	 时间字段
	 
	*/
	public static final String RptDTSearchKey = "RptDTSearchKey";
	/** 
	 查询外键枚举字段
	 
	*/
	public static final String RptSearchKeys = "RptSearchKeys";
	
	public static final String FlowCtrls = "FlowCtrls";
	/** 
	 升级逻辑.
	 
	*/
	public final void Upgrade()
	{
		String sql = "";

			///#region 升级ccform控件.
		if (BP.DA.DBAccess.IsExitsObject("Sys_FrmLine") == true)
		{
			//重命名.
			BP.Sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLine", "Sys_FrmLineBak", sql);

			//检查是否升级.
			sql = "SELECT * FROM Sys_FrmLineBak ORDER BY FK_MapData ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				BP.Sys.FrmEle ele = new FrmEle();
				ele.Copy(dr);
				ele.setEleType(BP.Sys.FrmEle.Line);
				//ele.BorderColor = dr["BorderColor"].ToString();
				//ele.BorderWidth = int.Parse(dr["BorderWidth"].ToString());
				if (ele.getIsExits() == true)
				{
					ele.setMyPK(BP.DA.DBAccess.GenerGUID());
				}
				ele.Insert();
			}
		}
		if (BP.DA.DBAccess.IsExitsObject("Sys_FrmLab") == true)
		{
			//重命名.
			BP.Sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLab", "Sys_FrmLabBak", sql);

			//检查是否升级.
			sql = "SELECT * FROM Sys_FrmLabBak ORDER BY FK_MapData ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				BP.Sys.FrmEle ele = new FrmEle();
				ele.Copy(dr);
				ele.setEleType(BP.Sys.FrmEle.Label);

				ele.setEleName(dr.get(FrmLabAttr.Text).toString());

				//ele.FontColor = dr[FrmLabAttr.FontColor].ToString();
				//ele.FontName = dr[FrmLabAttr.FontName].ToString();
				//ele.FontSize = int.Parse(dr[FrmLabAttr.FontSize].ToString());
				//ele.BorderWidth = int.Parse(dr["BorderWidth"].ToString());

				if (ele.getIsExits() == true)
				{
					ele.setMyPK(BP.DA.DBAccess.GenerGUID());
				}
				ele.Insert();
			}
		}
		if (BP.DA.DBAccess.IsExitsObject("Sys_FrmBtn") == true)
		{
			//重命名.
			BP.Sys.SFDBSrc dbsrc = new SFDBSrc("local");
			dbsrc.Rename("Table", "Sys_FrmLab", "Sys_FrmLabBak", sql);

			//检查是否升级.
			sql = "SELECT * FROM Sys_FrmLabBak ORDER BY FK_MapData ";
			DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
			for (DataRow dr : dt.Rows)
			{
				BP.Sys.FrmEle ele = new FrmEle();
				ele.Copy(dr);
				ele.setEleType(BP.Sys.FrmEle.Line);
				//ele.BorderColor = dr["BorderColor"].ToString();
				//ele.BorderWidth = int.Parse(dr["BorderWidth"].ToString());
				if (ele.getIsExits() == true)
				{
					ele.setMyPK(BP.DA.DBAccess.GenerGUID());
				}
				ele.Insert();
			}
		}

			///#endregion 升级ccform控件.
	}

	/** 
	 表单设计器设计工具
	 
	*/
	public final String getDesignerTool()
	{
		return this.GetValStringByKey(MapDataAttr.DesignerTool, "Silverlight");
	}
	public final void setDesignerTool(String value)
	{
		this.SetValByKey(MapDataAttr.DesignerTool, value);
	}


		///#region weboffice文档属性(参数属性)
	/** 
	 是否启用锁定行
	 
	*/
	public final boolean getIsRowLock()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsRowLock, false);
	}
	public final void setIsRowLock(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsRowLock, value);
	}

	/** 
	 是否启用打印
	 
	*/
	public final boolean getIsWoEnablePrint()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnablePrint);
	}
	public final void setIsWoEnablePrint(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnablePrint, value);
	}
	/** 
	 是否启用只读
	 
	*/
	public final boolean getIsWoEnableReadonly()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableReadonly);
	}
	public final void setIsWoEnableReadonly(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableReadonly, value);
	}
	/** 
	 是否启用修订
	 
	*/
	public final boolean getIsWoEnableRevise()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableRevise);
	}
	public final void setIsWoEnableRevise(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableRevise, value);
	}
	/** 
	 是否启用保存
	 
	*/
	public final boolean getIsWoEnableSave()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSave);
	}
	public final void setIsWoEnableSave(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSave, value);
	}
	/** 
	 是否查看用户留痕
	 
	*/
	public final boolean getIsWoEnableViewKeepMark()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableViewKeepMark);
	}
	public final void setIsWoEnableViewKeepMark(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableViewKeepMark, value);
	}
	/** 
	 是否启用weboffice
	 
	*/
	public final boolean getIsWoEnableWF()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableWF);
	}
	public final void setIsWoEnableWF(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableWF, value);
	}

	/** 
	 是否启用套红
	 
	*/
	public final boolean getIsWoEnableOver()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableOver);
	}
	public final void setIsWoEnableOver(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableOver, value);
	}

	/** 
	 是否启用签章
	 
	*/
	public final boolean getIsWoEnableSeal()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableSeal);
	}
	public final void setIsWoEnableSeal(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableSeal, value);
	}

	/** 
	 是否启用公文模板
	 
	*/
	public final boolean getIsWoEnableTemplete()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableTemplete);
	}
	public final void setIsWoEnableTemplete(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableTemplete, value);
	}

	/** 
	 是否记录节点信息
	 
	*/
	public final boolean getIsWoEnableCheck()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableCheck);
	}
	public final void setIsWoEnableCheck(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableCheck, value);
	}

	/** 
	 是否插入流程图
	 
	*/
	public final boolean getIsWoEnableInsertFlow()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFlow);
	}
	public final void setIsWoEnableInsertFlow(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFlow, value);
	}

	/** 
	 是否插入风险点
	 
	*/
	public final boolean getIsWoEnableInsertFengXian()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableInsertFengXian);
	}
	public final void setIsWoEnableInsertFengXian(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableInsertFengXian, value);
	}

	/** 
	 是否启用留痕模式
	 
	*/
	public final boolean getIsWoEnableMarks()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableMarks);
	}
	public final void setIsWoEnableMarks(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableMarks, value);
	}
	/** 
	 是否插入风险点
	 
	*/
	public final boolean getIsWoEnableDown()
	{
		return this.GetParaBoolen(FrmAttachmentAttr.IsWoEnableDown);
	}
	public final void setIsWoEnableDown(boolean value)
	{
		this.SetPara(FrmAttachmentAttr.IsWoEnableDown, value);
	}

		///#endregion weboffice文档属性


		///#region 自动计算属性.
	public final float getMaxLeft()
	{
		return this.GetParaFloat(MapDataAttr.MaxLeft);
	}
	public final void setMaxLeft(float value)
	{
		this.SetPara(MapDataAttr.MaxLeft, value);
	}
	public final float getMaxRight()
	{
		return this.GetParaFloat(MapDataAttr.MaxRight);
	}
	public final void setMaxRight(float value)
	{
		this.SetPara(MapDataAttr.MaxRight, value);
	}
	public final float getMaxTop()
	{
		return this.GetParaFloat(MapDataAttr.MaxTop);
	}
	public final void setMaxTop(float value)
	{
		this.SetPara(MapDataAttr.MaxTop, value);
	}
	public final float getMaxEnd()
	{
		return this.GetParaFloat(MapDataAttr.MaxEnd);
	}
	public final void setMaxEnd(float value)
	{
		this.SetPara(MapDataAttr.MaxEnd, value);
	}

		///#endregion 自动计算属性.


		///#region 报表属性(参数方式存储).
	/** 
	 是否关键字查询
	 
	*/
	public final boolean getRptIsSearchKey()
	{
		return this.GetParaBoolen(MapDataAttr.RptIsSearchKey, true);
	}
	public final void setRptIsSearchKey(boolean value)
	{
		this.SetPara(MapDataAttr.RptIsSearchKey, value);
	}
	/** 
	 时间段查询方式
	 
	*/
	public final DTSearchWay getRptDTSearchWay()
	{
		return DTSearchWay.forValue(this.GetParaInt(MapDataAttr.RptDTSearchWay));
	}
	public final void setRptDTSearchWay(DTSearchWay value)
	{
		this.SetPara(MapDataAttr.RptDTSearchWay, value.getValue());
	}
	/** 
	 时间字段
	 
	*/
	public final String getRptDTSearchKey()
	{
		return this.GetParaString(MapDataAttr.RptDTSearchKey);
	}
	public final void setRptDTSearchKey(String value)
	{
		this.SetPara(MapDataAttr.RptDTSearchKey, value);
	}
	/** 
	 查询外键枚举字段
	 
	*/
	public final String getRptSearchKeys()
	{
		return this.GetParaString(MapDataAttr.RptSearchKeys, "*");
	}
	public final void setRptSearchKeys(String value)
	{
		this.SetPara(MapDataAttr.RptSearchKeys, value);
	}

		///#endregion 报表属性(参数方式存储).


		///#region 外键属性
	/** 
	版本号.
	 
	*/
	public final String getVer()
	{
		return this.GetValStringByKey(MapDataAttr.Ver);
	}
	public final void setVer(String value)
	{
		this.SetValByKey(MapDataAttr.Ver, value);
	}
	/** 
	 顺序号
	 
	*/
	public final int getIdx()
	{
		return this.GetValIntByKey(MapDataAttr.Idx);
	}
	public final void setIdx(int value)
	{
		this.SetValByKey(MapDataAttr.Idx, value);
	}
	/** 
	 扩展控件
	 
	*/
	public final FrmEles getHisFrmEles()
	{
		Object tempVar = this.GetRefObject("FrmEles");
		FrmEles obj = (FrmEles)((tempVar instanceof FrmEles) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEles(this.getNo());
			this.SetRefObject("FrmEles", obj);
		}
		return obj;
	}
	/** 
	 框架
	 
	*/
	public final MapFrames getMapFrames()
	{
		Object tempVar = this.GetRefObject("MapFrames");
		MapFrames obj = (MapFrames)((tempVar instanceof MapFrames) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapFrames(this.getNo());
			this.SetRefObject("MapFrames", obj);
		}
		return obj;
	}
	/** 
	 分组字段
	 
	*/
	public final GroupFields getGroupFields()
	{
		Object tempVar = this.GetRefObject("GroupFields");
		GroupFields obj = (GroupFields)((tempVar instanceof GroupFields) ? tempVar : null);
		if (obj == null)
		{
			obj = new GroupFields(this.getNo());
			this.SetRefObject("GroupFields", obj);
		}
		return obj;
	}
	/** 
	 逻辑扩展
	 
	*/
	public final MapExts getMapExts()
	{
		Object tempVar = this.GetRefObject("MapExts");
		MapExts obj = (MapExts)((tempVar instanceof MapExts) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapExts(this.getNo());
			this.SetRefObject("MapExts", obj);
		}
		return obj;
	}
	/** 
	 事件
	 
	*/
	public final FrmEvents getFrmEvents()
	{
		Object tempVar = this.GetRefObject("FrmEvents");
		FrmEvents obj = (FrmEvents)((tempVar instanceof FrmEvents) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEvents(this.getNo());
			this.SetRefObject("FrmEvents", obj);
		}
		return obj;
	}
	/** 
	 一对多
	 
	*/
	public final MapM2Ms getMapM2Ms()
	{
		Object tempVar = this.GetRefObject("MapM2Ms");
		MapM2Ms obj = (MapM2Ms)((tempVar instanceof MapM2Ms) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapM2Ms(this.getNo());
			this.SetRefObject("MapM2Ms", obj);
		}
		return obj;
	}
	/** 
	 从表
	 
	*/
	public final MapDtls getMapDtls()
	{
		Object tempVar = this.GetRefObject("MapDtls");
		MapDtls obj = (MapDtls)((tempVar instanceof MapDtls) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapDtls(this.getNo());
			this.SetRefObject("MapDtls", obj);
		}
		return obj;
	}
	/** 
	 报表
	 
	*/
	public final FrmRpts getFrmRpts()
	{
		Object tempVar = this.GetRefObject("FrmRpts");
		FrmRpts obj = (FrmRpts)((tempVar instanceof FrmRpts) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmRpts(this.getNo());
			this.SetRefObject("FrmRpts", obj);
		}
		return obj;
	}
	/** 
	 超连接
	 
	*/
	public final FrmLinks getFrmLinks()
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmLinks obj = (FrmLinks)((tempVar instanceof FrmLinks) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLinks(this.getNo());
			this.SetRefObject("FrmLinks", obj);
		}
		return obj;
	}
	/** 
	 按钮
	 
	*/
	public final FrmBtns getFrmBtns()
	{
		Object tempVar = this.GetRefObject("FrmLinks");
		FrmBtns obj = (FrmBtns)((tempVar instanceof FrmBtns) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmBtns(this.getNo());
			this.SetRefObject("FrmBtns", obj);
		}
		return obj;
	}
	/** 
	 元素
	 
	*/
	public final FrmEles getFrmEles()
	{
		Object tempVar = this.GetRefObject("FrmEles");
		FrmEles obj = (FrmEles)((tempVar instanceof FrmEles) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmEles(this.getNo());
			this.SetRefObject("FrmEles", obj);
		}
		return obj;
	}
	/** 
	 线
	 
	*/
	public final FrmLines getFrmLines()
	{
		Object tempVar = this.GetRefObject("FrmLines");
		FrmLines obj = (FrmLines)((tempVar instanceof FrmLines) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLines(this.getNo());
			this.SetRefObject("FrmLines", obj);
		}
		return obj;
	}
	/** 
	 标签
	 
	*/
	public final FrmLabs getFrmLabs()
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmLabs obj = (FrmLabs)((tempVar instanceof FrmLabs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmLabs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 图片
	 
	*/
	public final FrmImgs getFrmImgs()
	{
		Object tempVar = this.GetRefObject("FrmLabs");
		FrmImgs obj = (FrmImgs)((tempVar instanceof FrmImgs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmImgs(this.getNo());
			this.SetRefObject("FrmLabs", obj);
		}
		return obj;
	}
	/** 
	 附件
	 
	*/
	public final FrmAttachments getFrmAttachments()
	{
		Object tempVar = this.GetRefObject("FrmAttachments");
		FrmAttachments obj = (FrmAttachments)((tempVar instanceof FrmAttachments) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmAttachments(this.getNo());
			this.SetRefObject("FrmAttachments", obj);
		}
		return obj;
	}
	/** 
	 图片附件
	 
	*/
	public final FrmImgAths getFrmImgAths()
	{
		Object tempVar = this.GetRefObject("FrmImgAths");
		FrmImgAths obj = (FrmImgAths)((tempVar instanceof FrmImgAths) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmImgAths(this.getNo());
			this.SetRefObject("FrmImgAths", obj);
		}
		return obj;
	}
	/** 
	 单选按钮
	 
	*/
	public final FrmRBs getFrmRBs()
	{
		Object tempVar = this.GetRefObject("FrmRBs");
		FrmRBs obj = (FrmRBs)((tempVar instanceof FrmRBs) ? tempVar : null);
		if (obj == null)
		{
			obj = new FrmRBs(this.getNo());
			this.SetRefObject("FrmRBs", obj);
		}
		return obj;
	}
	/** 
	 属性
	 
	*/
	public final MapAttrs getMapAttrs()
	{
		Object tempVar = this.GetRefObject("MapAttrs");
		MapAttrs obj = (MapAttrs)((tempVar instanceof MapAttrs) ? tempVar : null);
		if (obj == null)
		{
			obj = new MapAttrs(this.getNo());
			this.SetRefObject("MapAttrs", obj);
		}
		return obj;
	}

		///#endregion

	public static boolean getIsEditDtlModel()
	{
		String s = BP.Web.WebUser.GetSessionByKey("IsEditDtlModel", "0");
		if (s.equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public static void setIsEditDtlModel(boolean value)
	{
		BP.Web.WebUser.SetSessionByKey("IsEditDtlModel", "1");
	}


		///#region 表单结构数据json
	/** 
	 表单图数据
	 * @throws IOException 
	 
	*/
	public final String getFormJson() throws IOException
	{
		String str= this.GetBigTextFromDB("FormJson");
		if (str == null)
		{
			return "";
		}
		return str;
	}
	public final void setFormJson(String value)
	{
		try {
			this.SaveBigTxtToDB("FormJson", value);
		} catch (Exception e) {
			Log.DebugWriteError("MapData setFormJson "+e.getMessage());
		}
	}

		///#endregion


		
	/** 
	 物理表
	 
	*/
	public final String getPTable()
	{
		String s = this.GetValStrByKey(MapDataAttr.PTable);
		if (s.equals("") || s == null)
		{
			return this.getNo();
		}
		return s;
	}
	public final void setPTable(String value)
	{
		this.SetValByKey(MapDataAttr.PTable, value);
	}
	
	
	 public final int getPTableModel()
	 {
		 return this.GetValIntByKey(MapDataAttr.PTableModel);
	 }
	 public final void setPTableModel(int value)
	 {
		 this.SetValByKey(MapDataAttr.PTableModel, value);
	 }
	/** 
	 URL
	 
	*/
	public final String getUrl()
	{
		return this.GetValStrByKey(MapDataAttr.Url);
	}
	public final void setUrl(String value)
	{
		this.SetValByKey(MapDataAttr.Url, value);
	}
	public final DBUrlType getHisDBUrl()
	{
		return DBUrlType.AppCenterDSN;
			// return (DBUrlType)this.GetValIntByKey(MapDataAttr.DBURL);
	}
	public final int getHisFrmTypeInt()
	{
		return this.GetValIntByKey(MapDataAttr.FrmType);
	}
	public final void setHisFrmTypeInt(int value)
	{
		this.SetValByKey(MapDataAttr.FrmType, value);
	}
	public final FrmType getHisFrmType()
	{
		return FrmType.forValue(this.GetValIntByKey(MapDataAttr.FrmType));
	}
	public final void setHisFrmType(FrmType value)
	{
		this.SetValByKey(MapDataAttr.FrmType, value.getValue());
	}
	
	public final AppType getHisAppType()
	{
		return AppType.forValue(this.GetValIntByKey(MapDataAttr.AppType));
	}
	
	public final void setHisAppType(AppType value)
	{
		this.SetValByKey(MapDataAttr.AppType, value.getValue());
	}
	/** 
	 备注
	*/
	public final String getNote()
	{
		return this.GetValStrByKey(MapDataAttr.Note);
	}
	public final void setNote(String value)
	{
		this.SetValByKey(MapDataAttr.Note, value);
	}
	/** 
	 是否有CA.
	 
	*/
	public final boolean getIsHaveCA()
	{
		return this.GetParaBoolen("IsHaveCA", false);

	}
	public final void setIsHaveCA(boolean value)
	{
		this.SetPara("IsHaveCA", value);
	}
	/** 
	 类别，可以为空.
	 
	*/
	public final String getFK_FrmSort()
	{
		return this.GetValStrByKey(MapDataAttr.FK_FrmSort);
	}
	public final void setFK_FrmSort(String value)
	{
		this.SetValByKey(MapDataAttr.FK_FrmSort, value);
	}
	/** 
	 数据源
	 
	*/
	public final String getDBSrc()
	{
		return this.GetValStrByKey(MapDataAttr.DBSrc);
	}
	public final void setDBSrc(String value)
	{
		this.SetValByKey(MapDataAttr.DBSrc, value);
	}

	/** 
	 类别，可以为空.
	 
	*/
	public final String getFK_FormTree()
	{
		return this.GetValStrByKey(MapDataAttr.FK_FormTree);
	}
	public final void setFK_FormTree(String value)
	{
		this.SetValByKey(MapDataAttr.FK_FormTree, value);
	}
	/** 
	 从表集合.
	 
	*/
	public final String getDtls()
	{
		return this.GetValStrByKey(MapDataAttr.Dtls);
	}
	public final void setDtls(String value)
	{
		this.SetValByKey(MapDataAttr.Dtls, value);
	}
	/** 
	 主键
	 
	*/
	public final String getEnPK()
	{
		String s = this.GetValStrByKey(MapDataAttr.EnPK);
		if (StringHelper.isNullOrEmpty(s))
		{
			return "OID";
		}
		return s;
	}
	public final void setEnPK(String value)
	{
		this.SetValByKey(MapDataAttr.EnPK, value);
	}
	public Entities _HisEns = null;
	public final Entities getHisEns()
	{
		if (_HisEns == null)
		{
			_HisEns = BP.En.ClassFactory.GetEns(this.getNo());
		}
		return _HisEns;
	}
	public final Entity getHisEn()
	{
		return this.getHisEns().getGetNewEntity();
	}
	public final float getFrmW()
	{
		return this.GetValFloatByKey(MapDataAttr.FrmW);
	}
	public final void setFrmW(float value)
	{
		this.SetValByKey(MapDataAttr.FrmW, value);
	}
	///// <summary>
	///// 表单控制方案
	///// </summary>
	//public string Slns
	//{
	//    get
	//    {
	//        return this.GetValStringByKey(MapDataAttr.Slns);
	//    }
	//    set
	//    {
	//        this.SetValByKey(MapDataAttr.Slns, value);
	//    }
	//}
	public final float getFrmH()
	{
		return this.GetValFloatByKey(MapDataAttr.FrmH);
	}
	public final void setFrmH(float value)
	{
		this.SetValByKey(MapDataAttr.FrmH, value);
	}
	public final int getTableCol()
	{
		int i = this.GetValIntByKey(MapDataAttr.TableCol);
		if (i == 0 || i == 1)
		{
			return 4;
		}
		return i;
	}

	/** 
	 应用类型.  0独立表单.1节点表单
	 
	*/
	public final String getAppType()
	{
		return this.GetValStrByKey(MapDataAttr.AppType);
	}
	public final void setAppType(String value)
	{
		this.SetValByKey(MapDataAttr.AppType, value);
	}
	/** 
	 表单body属性.
	 
	*/
	public final String getBodyAttr()
	{
		String str = this.GetValStrByKey(MapDataAttr.BodyAttr);
		str = str.replace("~", "'");
		return str;
	}
	public final void setBodyAttr(String value)
	{
		this.SetValByKey(MapDataAttr.BodyAttr, value);
	}

	/** 
	 枚举值
	 
*/
	public final SysEnums getSysEnums()
	{
		Object tempVar = this.GetRefObject("SysEnums");
		SysEnums obj = (SysEnums)((tempVar instanceof SysEnums) ? tempVar : null);
		if (obj == null)
		{
			obj = new SysEnums();
			obj.RetrieveInSQL(SysEnumAttr.EnumKey, "SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "'");
			//obj.RetrieveInSQL(SysEnumAttr.EnumKey, "select UIBindKey from ( SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData='" + this.getNo() + "' ) as tab1");
			this.SetRefObject("SysEnums", obj);

		}
		return obj;
	}
	
	public final Map GenerHisMap()
	{
		MapAttrs mapAttrs = this.getMapAttrs();
		if (mapAttrs.size() == 0)
		{
			this.RepairMap();
			mapAttrs = this.getMapAttrs();
		}

		Map map = new Map(this.getPTable(), this.getName());
		map.setEnDBUrl(new DBUrl(this.getHisDBUrl()));
		map.Java_SetEnType(EnType.App);
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);

		Attrs attrs = new Attrs();
		for (MapAttr mapAttr : MapAttrs.convertMapAttrs(mapAttrs))
		{
			map.AddAttr(mapAttr.getHisAttr());
		}

		// 产生从表。
		MapDtls dtls = this.getMapDtls(); // new MapDtls(this.No);
		for (MapDtl dtl :  MapDtls.convertMapDtls(dtls))
		{
			GEDtls dtls1 = new GEDtls(dtl.getNo());
			map.AddDtl(dtls1, "RefPK");
		}


			///#region 查询条件.
		map.IsShowSearchKey = this.getRptIsSearchKey(); //是否启用关键字查询.
		// 按日期查询.
		map.DTSearchWay = this.getRptDTSearchWay(); //日期查询方式.
		map.DTSearchKey = this.getRptDTSearchKey(); //日期字段.

		//加入外键查询字段.
		String[] keys = this.getRptSearchKeys().split("[*]", -1);
		for (String key : keys)
		{
			if (StringHelper.isNullOrEmpty(key))
			{
				continue;
			}

			map.AddSearchAttr(key);
		}

			///#endregion 查询条件.

		return map;
	}
	private GEEntity _HisEn = null;
	public final GEEntity getHisGEEn()
	{
		if (this._HisEn == null)
		{
			_HisEn = new GEEntity(this.getNo());
		}
		return _HisEn;
	}
	public final String getTableWidth()
	{
		// switch (this.TableCol)
		// {
		// case 2:
		// return
		// labCol = 25;
		// ctrlCol = 75;
		// break;
		// case 4:
		// labCol = 20;
		// ctrlCol = 30;
		// break;
		// case 6:
		// labCol = 15;
		// ctrlCol = 30;
		// break;
		// case 8:
		// labCol = 10;
		// ctrlCol = 15;
		// break;
		// default:
		// break;
		// }
		
		int i = this.GetValIntByKey(MapDataAttr.TableWidth);
		if (i <= 50)
		{
			return "100%";
		}
		return i + "px";
	}
	/** 
	 生成实体
	 
	 @param ds
	 @return 
	*/
	public final GEEntity GenerGEEntityByDataSet(DataSet ds)
	{
		// New 它的实例.
		GEEntity en = this.getHisGEEn();

		// 它的table.
		DataTable dt = ds.Tables.get(Integer.parseInt(this.getNo()));

		//装载数据.
		en.getRow().LoadDataTable(dt, dt.Rows.get(0));

		// dtls.
		MapDtls dtls = this.getMapDtls();
		for (Object item : dtls)
		{
			DataTable dtDtls = ds.Tables.get(Integer.parseInt(((EntityNo) item).getNo()));
			GEDtls dtlsEn = new GEDtls(((EntityNo) item).getNo());
			for (DataRow dr : dtDtls.Rows)
			{
				// 产生它的Entity data.
				GEDtl dtl = (GEDtl)dtlsEn.getGetNewEntity();
				dtl.getRow().LoadDataTable(dtDtls, dr);

				//加入这个集合.
				dtlsEn.AddEntity(dtl);
			}

			//加入到他的集合里.
			en.getDtls().add(dtDtls);
		}
		return en;
	}
	/** 
	 生成map.
	 
	 @param no
	 @return 
	*/
	public static Map GenerHisMap(String no)
	{
		if (SystemConfig.getIsDebug())
		{
			MapData md = new MapData();
			md.setNo(no);
			md.Retrieve();
			return md.GenerHisMap();
		}
		else
		{
			Map map = BP.DA.Cash.GetMap(no);
			if (map == null)
			{
				MapData md = new MapData();
				md.setNo(no);
				md.Retrieve();
				map = md.GenerHisMap();
				BP.DA.Cash.SetMap(no, map);
			}
			return map;
		}
	}
	/** 
	 映射基础
	 
	*/
	public MapData()
	{
	}
	/** 
	 映射基础
	 
	 @param no 映射编号
	*/
	public MapData(String no)
	{
		 
		super(no);
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

		Map map = new Map("Sys_MapData", "表单注册表");
		map.Java_SetDepositaryOfEntity(Depositary.None);
		map.Java_SetDepositaryOfMap(Depositary.Application);
		map.Java_SetEnType(EnType.Sys);
		map.Java_SetCodeStruct("4");


			///#region 基础信息.
		map.AddTBStringPK(MapDataAttr.No, null, "编号", true, false, 1, 200, 100);
		map.AddTBString(MapDataAttr.Name, null, "描述", true, false, 0, 500, 20);
		
		 map.AddTBString(MapDataAttr.FormEventEntity, null, "事件实体", true, true, 0, 100, 20, true);

         map.AddTBString(MapDataAttr.EnPK, null, "实体主键", true, false, 0, 200, 20);
         map.AddTBString(MapDataAttr.PTable, null, "物理表", true, false, 0, 500, 20);

         //@周朋 表存储格式0=自定义表,1=指定表,可以修改字段2=执行表不可以修改字段.
         map.AddTBInt(MapDataAttr.PTableModel, 0, "表存储模式", true, true);
         
		map.AddTBString(MapDataAttr.Url, null, "连接(对嵌入式表单有效)", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.Dtls, null, "从表", true, false, 0, 500, 20);

			//格式为: @1=方案名称1@2=方案名称2@3=方案名称3
			//map.AddTBString(MapDataAttr.Slns, null, "表单控制解决方案", true, false, 0, 500, 20);

		map.AddTBInt(MapDataAttr.FrmW, 900, "FrmW", true, true);
		map.AddTBInt(MapDataAttr.FrmH, 1200, "FrmH", true, true);

		map.AddTBInt(MapDataAttr.TableCol, 4, "傻瓜表单显示的列", true, true);
		map.AddTBInt(MapDataAttr.TableWidth, 800, "表格宽度", true, true);

			//Tag
		map.AddTBString(MapDataAttr.Tag, null, "Tag", true, false, 0, 500, 20);

			// 可以为空这个字段。
		map.AddTBString(MapDataAttr.FK_FrmSort, null, "表单类别", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.FK_FormTree, null, "表单树类别", true, false, 0, 500, 20);


			// enumFrmType  @自由表单，@傻瓜表单，@嵌入式表单.  
		map.AddDDLSysEnum(MapDataAttr.FrmType, BP.Sys.FrmType.FreeFrm.getValue(), "表单类型", true, false, MapDataAttr.FrmType);

			// 应用类型.  0独立表单.1节点表单
		map.AddTBInt(MapDataAttr.AppType, 0, "应用类型", true, false);
		map.AddTBString(MapDataAttr.DBSrc, "local", "数据源", true, false, 0, 100, 20);
		map.AddTBString(MapDataAttr.BodyAttr, null, "表单Body属性", true, false, 0, 100, 20);

			///#endregion 基础信息.


			///#region 设计者信息.
		map.AddTBString(MapDataAttr.Note, null, "备注", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.Designer, null, "设计者", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerUnit, null, "单位", true, false, 0, 500, 20);
		map.AddTBString(MapDataAttr.DesignerContact, null, "联系方式", true, false, 0, 500, 20);

		map.AddTBInt(MapDataAttr.Idx, 100, "顺序号", true, true);
		map.AddTBString(MapDataAttr.GUID, null, "GUID", true, false, 0, 128, 20);
		map.AddTBString(MapDataAttr.Ver, null, "版本号", true, false, 0, 30, 20);
		map.AddTBString(MapDataAttr.DesignerTool, null, "表单设计器", true, true, 0, 30, 20);


		 //流程控件.
        map.AddTBString(MapDataAttr.FlowCtrls, null, "流程控件", true, true, 0, 200, 20);
        
			//增加参数字段.
		map.AddTBAtParas(4000);

			///#endregion

		this.set_enMap(map);
		return this.get_enMap();
	}

	/** 
	 上移
	 
	*/
	public final void DoUp()
	{
		this.DoOrderUp(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
	}
	/** 
	 下移
	 
	*/
	public final void DoOrderDown()
	{
		this.DoOrderDown(MapDataAttr.FK_FormTree, this.getFK_FormTree(), MapDataAttr.Idx);
	}

		///#endregion
	 //检查表单
	public final void CheckPTableSaveModel(String filed) throws Exception
	{
		if (this.getPTableModel() == 2)
		{
			//如果是存储格式
			if (DBAccess.IsExitsTableCol(this.getPTable(), filed) == false)
			{
				throw new RuntimeException("@表单的表存储模式不允许您创建不存在的字段(" + filed + ")，不允许修改表结构.");
			}
		}
	}

	/** 
	 获得PTableModel=2模式下的表单，没有被使用的字段集合.
	 
	 @param frmID
	 @return 
	*/
	public static DataTable GetFieldsOfPTableMode2(String frmID)
	{
		
		String pTable="";
		  MapDtl dtl = new MapDtl();
          dtl.setNo(frmID);
          if (dtl.RetrieveFromDBSources() == 1)
          {
              pTable = dtl.getPTable();
          }
          else
          {
              MapData md = new MapData();
              md.setNo( frmID);
              md.RetrieveFromDBSources();
              pTable = md.getPTable();
          }

          
		//MapData md = new MapData(frmID);

		//获得原始数据.
		DataTable dt = BP.DA.DBAccess.GetTableSchema( pTable);

		//创建样本表结构.
		DataTable mydt = BP.DA.DBAccess.GetTableSchema(pTable);
		mydt.Rows.clear();

		//获得现有的列..
		MapAttrs attrs = new MapAttrs(frmID);

		String flowFiels = ",GUID,PRI,PrjNo,PrjName,PEmp,AtPara,FlowNote,WFSta,PNodeID,FK_FlowSort,FK_Flow,OID,FID,Title,WFState,CDT,FlowStarter,FlowStartRDT,FK_Dept,FK_NY,FlowDaySpan,FlowEmps,FlowEnder,FlowEnderRDT,FlowEndNode,MyNum,PWorkID,PFlowNo,BillNo,ProjNo,";

		//排除已经存在的列. 把所有的列都输出给前台，让前台根据类型分拣.
		for (DataRow dr : dt.Rows)
		{
			String key = (String) dr.get("FName");
			if (attrs.Contains(MapAttrAttr.KeyOfEn, key) == true)
			{
				continue;
			}

			if (flowFiels.contains("," + key + ",") == true)
			{
				continue;
			}

			DataRow mydr = mydt.NewRow();
			mydr.setValue("FName", dr.get("FName")); 
			mydr.setValue("FType", dr.get("FType"));
			mydr.setValue("FLen", dr.get("FLen"));
			mydr.setValue("FDesc", dr.get("FDesc"));
			mydt.Rows.add(mydr);
		}
		return mydt;
	}

	/** 
	 导入
	 
	 @param ds
	 @return 
	*/
	public static MapData ImpMapData(DataSet ds)
	{
		try
		{
			return ImpMapData(ds, true);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	/** 
	 导入数据
	 
	 @param ds
	 @param isSetReadony
	 @return 
	*/
	public static MapData ImpMapData(DataSet ds, boolean isSetReadony)
			throws Exception
	{
		String errMsg = "";
		errMsg = dsCheck(ds);
		DataTable dt = ds.hashTables.get("Sys_MapData");
		String fk_mapData = dt.Rows.get(0).getValue("No").toString();
		MapData md = new MapData();
		md.setNo(fk_mapData);
		if (md.getIsExits())
		{
			throw new RuntimeException("已经存在(" + fk_mapData + ")的数据。");
		}
		
		return ImpMapData(fk_mapData, ds, isSetReadony);
	}
	
	 /** 
	 设置表单为只读属性
	 
	 @param fk_mapdata 表单ID
*/
	public static void SetFrmIsReadonly(String fk_mapdata)
	{
		//把主表字段设置为只读.
		MapAttrs attrs = new MapAttrs(fk_mapdata);
		for (MapAttr attr : attrs.ToJavaList())
		{
			if (attr.getDefValReal().contains("@"))
			{
				attr.setUIIsEnable(false);
				attr.setDefValReal(""); //清空默认值.
				attr.SetValByKey("ExtDefVal", ""); //设置默认值.
				attr.Update();
				continue;
			}
//
//			if (attr.getUIIsEnable() == false)
//			{
//				attr.setUIIsEnable(true);
//				attr.Update();
//				continue;
//			}
		}

		//把从表字段设置为只读.
		MapDtls dtls = new MapDtls(fk_mapdata);
		for (MapDtl dtl : dtls.ToJavaList())
		{
			dtl.setIsInsert(true);
			dtl.setIsUpdate(true);
			dtl.setIsDelete(true);
			dtl.Update();

			attrs = new MapAttrs(dtl.getNo());
			for (MapAttr attr : attrs.ToJavaList())
			{
				if (attr.getDefValReal().contains("@"))
				{
					attr.setUIIsEnable(false);
					attr.setDefValReal(""); //清空默认值.
					attr.SetValByKey("ExtDefVal", ""); //设置默认值.
					attr.Update();
				}

//				if (attr.getUIIsEnable() == false)
//				{
//					attr.setUIIsEnable(true);
//					attr.Update();
//					continue;
//				}
			}
		}

		//把附件设置为只读.
		FrmAttachments aths = new FrmAttachments(fk_mapdata);
		for (FrmAttachment item : aths.ToJavaList())
		{
			item.setIsUpload(false);
			item.setHisDeleteWay(AthDeleteWay.DelSelf);

			//如果是从开始节点表单导入的,就默认为, 按照workid继承的模式.
			if (fk_mapdata.indexOf("ND") == 0)
			{
				item.setHisCtrlWay(AthCtrlWay.WorkID);
				item.setDataRefNoOfObj("AttachM1");
			}
			item.Update();
		}
	}
	
	
	
	public static String dsCheck(DataSet ds)
	{
		//对Sys_MapAttr、Sys_MapData是否存在增加检测 by fanleiwei 2016.4.20
		String errMsg = "";
		String msgAttr = "";
		String msgData = "";
		for (DataTable dt :ds.Tables){
			if (dt.getTableName().contains("WF_Flow"))
			{
				errMsg += "@此模板文件为流程模板。";
				break;
			}
			if (dt.getTableName().contains("Sys_MapAttr")) 
			{
				msgAttr = "1";
			} 
			else if (dt.getTableName().contains("Sys_MapData")) 
			{
				msgData = "2";
			}
		};
		if (msgAttr.equals("1") && msgData.equals("2"))
		{
			errMsg ="";
		}else
		{
			throw new RuntimeException(errMsg);
		}
		return errMsg;
	}
	/** 
	 导入表单
	 
	 @param fk_mapdata 表单ID
	 @param ds 表单数据
	 @param isSetReadonly 是否设置只读？
	 @return 
	*/
	public static MapData ImpMapData(String fk_mapdata, DataSet ds, boolean isSetReadonly)
	{

			///#region 检查导入的数据是否完整.
		String errMsg = "";
		//if (ds.Tables[0].TableName != "Sys_MapData")
		//    errMsg += "@非表单模板。";
		List<DataTable> listD = ds.getTables();
		ArrayList<String> arr = new ArrayList<String>();
		for(DataTable dt:listD){
			arr.add(dt.TableName);
		}
		if (arr.contains("WF_Flow") == true)
		{
			errMsg += "@此模板文件为流程模板。";
		}

		if (arr.contains("Sys_MapAttr") == false)
		{
			errMsg += "@缺少表:Sys_MapAttr";
		}

		if (arr.contains("Sys_MapData") == false)
		{
			errMsg += "@缺少表:Sys_MapData";
		}

		int num = arr.indexOf("Sys_MapAttr");
		DataTable dtCheck = ds.Tables.get(num);
		
		boolean isHave = false;
		for (DataRow dr : dtCheck.Rows)
		{
			if (dr.getValue("KeyOfEn").toString().toUpperCase().equals("OID") || dr.getValue("KEYOFEN").toString().toUpperCase().equals("OID"))
			{
				isHave = true;
				break;
			}
		}

		if (isHave == false)
		{
			errMsg += "@表单模版缺少列:OID";
		}

		if (!errMsg.equals(""))
		{
			throw new RuntimeException("@以下错误不可导入，可能的原因是非表单模板文件:" + errMsg);
		}

			///#endregion

		// 定义在最后执行的sql.
		String endDoSQL = "";

		//检查是否存在OID字段.
		MapData mdOld = new MapData();
		mdOld.setNo(fk_mapdata);
		mdOld.RetrieveFromDBSources();
		mdOld.Delete();

		// 求出dataset的map.
		String oldMapID = "";
		DataTable dtMap = ds.Tables.get(arr.indexOf("Sys_MapData"));
		if (dtMap.Rows.size() == 1)
		{
			oldMapID = dtMap.Rows.get(0).getValue("No").toString();
		}
		else
		{
			// 求旧的表单ID.
			for (DataRow dr : dtMap.Rows)
			{
				oldMapID = dr.getValue("No").toString();
			}

			if (StringHelper.isNullOrEmpty(oldMapID) == true)
			{
				oldMapID = dtMap.Rows.get(0).getValue("No").toString();
				//oldMapID = fk_mapdata.substring(2, fk_mapdata.length());
			}
			//  throw new Exception("@没有找到 oldMapID ");
		}

		String timeKey = new java.util.Date().toString();

		// string timeKey = fk_mapdata;


			///#region 表单元素
		for (DataTable dt : ds.Tables)
		{
			int idx = 0;

//			switch (dt.TableName)
//ORIGINAL LINE: case "Sys_MapDtl":
			if (dt.TableName.equals("Sys_MapDtl"))
			{
					for (DataRow dr : dt.Rows)
					{
						MapDtl dtl = new MapDtl();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							dtl.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
//						if (isSetReadonly)
//						{
//							dtl.setIsInsert(false);
//							dtl.setIsUpdate(false);
//							dtl.setIsDelete(false);
//						}

						dtl.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_MapData":
			else if (dt.TableName.equals("Sys_MapData"))
			{
					for (DataRow dr : dt.Rows)
					{
						MapData md = new MapData();
						for (DataColumn dc : dt.Columns)
						{
							String val = dr.getValue(dc.ColumnName) == null ? ""
									: String.valueOf(dr.getValue(dc.ColumnName));
							if (!StringHelper.isNullOrEmpty(val))
							{
								md.SetValByKey(dc.ColumnName, val.toString()
										.replace(oldMapID, fk_mapdata));
							}
							// md.SetValByKey(dc.ColumnName, val);
						}
						//如果物理表为空，则使用编号为物理数据表
						if (StringHelper.isNullOrEmpty(md.getPTable().trim()) == true)
						{
							md.setPTable(md.getNo());
						}

						//表单类别编号不为空，则用原表单类别编号
						if (StringHelper.isNullOrEmpty(mdOld.getFK_FormTree()) == false)
						{
							md.setFK_FormTree(mdOld.getFK_FormTree());
						}

						//表单类别编号不为空，则用原表单类别编号
						if (StringHelper.isNullOrEmpty(mdOld.getFK_FrmSort()) == false)
						{
							md.setFK_FrmSort(mdOld.getFK_FrmSort());
						}

						if (StringHelper.isNullOrEmpty(mdOld.getPTable()) == false)
						{
							md.setPTable(mdOld.getPTable());
						}
						if (StringHelper.isNullOrEmpty(mdOld.getName()) == false)
						{
							md.setName(mdOld.getName());
						}

						md.setHisFrmType(mdOld.getHisFrmType());
						//表单应用类型保持不变
						md.setAppType(mdOld.getAppType());

						md.DirectInsert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmBtn":
			else if (dt.TableName.equals("Sys_FrmBtn"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmBtn en = new FrmBtn();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
//						if (isSetReadonly == true)
//						{
//							en.setIsEnable(false);
//						}


						en.setMyPK(DBAccess.GenerGUID());
						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmLine":
			else if (dt.TableName.equals("Sys_FrmLine"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLine en = new FrmLine();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						en.setMyPK("LE_" + idx + "_" + fk_mapdata);
						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmLab":
			else if (dt.TableName.equals("Sys_FrmLab"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLab en = new FrmLab();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}


							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						//  en.FK_MapData = fk_mapdata; 删除此行解决从表lab的问题。
						en.setMyPK("LB_" + idx + "_" + fk_mapdata);
						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmLink":
			else if (dt.TableName.equals("Sys_FrmLink"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmLink en = new FrmLink();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						en.setMyPK("LK_" + idx + "_" + fk_mapdata);
						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmEle":
			else if (dt.TableName.equals("Sys_FrmEle"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmEle en = new FrmEle();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
//						if (isSetReadonly == true)
//						{
//							en.setIsEnable(false);
//						}

						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmImg":
			else if (dt.TableName.equals("Sys_FrmImg"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImg en = new FrmImg();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}



							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						en.setMyPK("Img_" + idx + "_" + fk_mapdata);
						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmImgAth":
			else if (dt.TableName.equals("Sys_FrmImgAth"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmImgAth en = new FrmImgAth();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

						if (StringHelper.isNullOrEmpty(en.getCtrlID()))
						{
							en.setCtrlID("ath" + idx);
						}

						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_FrmRB":
			else if (dt.TableName.equals("Sys_FrmRB"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmRB en = new FrmRB();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}


						try
						{
							en.Save();
						}
						catch (java.lang.Exception e)
						{
						}
					}
			}
//ORIGINAL LINE: case "Sys_FrmAttachment":
			else if (dt.TableName.equals("Sys_FrmAttachment"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						FrmAttachment en = new FrmAttachment();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						en.setMyPK("Ath_" + idx + "_" + fk_mapdata);
//						if (isSetReadonly == true)
//						{
//							// en.IsDeleteInt = 0;
//							en.setHisDeleteWay(AthDeleteWay.None);
//							en.setIsUpload(false);
//						}

						try
						{
							en.Insert();
						}
						catch (java.lang.Exception e2)
						{
						}
					}
			}
//ORIGINAL LINE: case "Sys_MapM2M":
			else if (dt.TableName.equals("Sys_MapM2M"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapM2M en = new MapM2M();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}

							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						//   en.NoOfObj = "M2M_" + idx + "_" + fk_mapdata;
//						if (isSetReadonly == true)
//						{
//							en.setIsDelete(false);
//							en.setIsInsert(false);
//						}
						try
						{
							en.Insert();
						}
						catch (java.lang.Exception e3)
						{
							en.Update();
						}
					}
			}
//ORIGINAL LINE: case "Sys_MapFrame":
			else if (dt.TableName.equals("Sys_MapFrame"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapFrame en = new MapFrame();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}


							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}
						en.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_MapExt":
			else if (dt.TableName.equals("Sys_MapExt"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						MapExt en = new MapExt();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							if (StringHelper.isNullOrEmpty(val.toString()) == true)
							{
								continue;
							}
							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

						//执行保存，并统一生成PK的规则.
						en.InitPK();
						en.Save();
					}
			}
//ORIGINAL LINE: case "Sys_MapAttr":
			else if (dt.TableName.equals("Sys_MapAttr"))
			{
					for (DataRow dr : dt.Rows)
					{
						MapAttr en = new MapAttr();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
						}

//						if (isSetReadonly == true)
//						{
//							if (en.getDefValReal() != null && (en.getDefValReal().contains("@WebUser.") || en.getDefValReal().contains("@RDT")))
//							{
//								en.setDefValReal("");
//							}
//
//							switch (en.getUIContralType())
//							{
//								case DDL:
//									en.setUIIsEnable(false);
//									break;
//								case TB:
//									en.setUIIsEnable(false);
//									break;
//								case RadioBtn:
//									en.setUIIsEnable(false);
//									break;
//								case CheckBok:
//									en.setUIIsEnable(false);
//									break;
//								default:
//									break;
//							}
//						}
						en.setMyPK(en.getFK_MapData() + "_" + en.getKeyOfEn());

						//直接插入.
						try
						{
							en.DirectInsert();
						}
						catch (java.lang.Exception e4)
						{
						}
					}
			}
//ORIGINAL LINE: case "Sys_GroupField":
			else if (dt.TableName.equals("Sys_GroupField"))
			{
					for (DataRow dr : dt.Rows)
					{
						idx++;
						GroupField en = new GroupField();
						for (DataColumn dc : dt.Columns)
						{
							Object val = (Object)((dr.get(dc.ColumnName) instanceof Object) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							try
							{
								en.SetValByKey(dc.ColumnName, val.toString().replace(oldMapID, fk_mapdata));
							}
							catch (java.lang.Exception e5)
							{
								throw new RuntimeException("val:" + val.toString() + "oldMapID:" + oldMapID + "fk_mapdata:" + fk_mapdata);
							}
						}
						int beforeID = (int) en.getOID();
						en.setOID(0);
						en.Insert();
						endDoSQL += "@UPDATE Sys_MapAttr SET GroupID=" + en.getOID() + " WHERE FK_MapData='" + fk_mapdata + "' AND GroupID=" + beforeID;
					}
			}
//ORIGINAL LINE: case "Sys_Enum":
			else if (dt.TableName.equals("Sys_Enum"))
			{
					for (DataRow dr : dt.Rows)
					{
						SysEnum se = new SysEnum();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.get(dc.ColumnName) instanceof String) ? dr.get(dc.ColumnName) : null);
							se.SetValByKey(dc.ColumnName, val);
						}
						se.setMyPK(se.getEnumKey() + "_" + se.getLang() + "_" + se.getIntKey());
						if (se.getIsExits())
						{
							continue;
						}
						se.Insert();
					}
			}
//ORIGINAL LINE: case "Sys_EnumMain":
			else if (dt.TableName.equals("Sys_EnumMain"))
			{
					for (DataRow dr : dt.Rows)
					{
						SysEnumMain sem = new SysEnumMain();
						for (DataColumn dc : dt.Columns)
						{
							String val = (String)((dr.get(dc.ColumnName) instanceof String) ? dr.get(dc.ColumnName) : null);
							if (val == null)
							{
								continue;
							}
							sem.SetValByKey(dc.ColumnName, val);
						}
						if (sem.getIsExits())
						{
							continue;
						}
						sem.Insert();
					}
			}
//ORIGINAL LINE: case "WF_Node":
			else if (dt.TableName.equals("WF_Node"))
			{
					if (dt.Rows.size() > 0)
					{
						endDoSQL += "@UPDATE WF_Node SET FWCSta=2" + ",FWC_X=" + dt.Rows.get(0).getValue("FWC_X") + ",FWC_Y=" + dt.Rows.get(0).getValue("FWC_Y") + ",FWC_H=" + dt.Rows.get(0).getValue("FWC_H") + ",FWC_W=" + dt.Rows.get(0).getValue("FWC_W") + ",FWCType=" + dt.Rows.get(0).getValue("FWCTYPE") + " WHERE NodeID=" + fk_mapdata.replace("ND", "");
					}
			}
			else
			{
			}
		}

			///#endregion

		//执行最后结束的sql.
		DBAccess.RunSQLs(endDoSQL);

		MapData mdNew = new MapData(fk_mapdata);
		mdNew.RepairMap();
		mdNew.Update();
		return mdNew;
	}
	/** 
	 修复map.
	 
	*/
	public final void RepairMap()
	{
		GroupFields gfs = new GroupFields(this.getNo());
		if (gfs.size() == 0)
		{
			GroupField gf = new GroupField();
			gf.setEnName(this.getNo());
			gf.setLab(this.getName());
			gf.Insert();

			String sqls = "";
			sqls += "@UPDATE Sys_MapDtl SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			//sqls += "@UPDATE Sys_MapFrame SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			sqls += "@UPDATE Sys_MapM2M SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gf.getOID() + " WHERE FK_MapData='" + this.getNo() + "'";
			DBAccess.RunSQLs(sqls);
		}
		else
		{
			 if (SystemConfig.getAppCenterDBType() != DBType.Oracle)
             {
				GroupField gfFirst = (GroupField)((gfs.get(0) instanceof GroupField) ? gfs.get(0) : null);
				String sqls = "";
				//sqls += "@UPDATE Sys_MapDtl SET GroupID=" + gfFirst.OID + "        WHERE  No   IN (SELECT X.No FROM (SELECT No FROM Sys_MapDtl WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.No + "')) AS X ) AND FK_MapData='" + this.No + "'";
				sqls += "@UPDATE Sys_MapAttr SET GroupID=" + gfFirst.getOID() + "       WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_MapAttr       WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.getNo() + "') or GroupID is null) X) AND FK_MapData='" + this.getNo() + "' ";
				//sqls += "@UPDATE Sys_MapFrame SET GroupID=" + gfFirst.getOID() + "      WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_MapFrame      WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.getNo() + "')) X) AND FK_MapData='" + this.getNo() + "' ";
				//   sqls += "@UPDATE Sys_MapM2M SET GroupID=" + gfFirst.OID + "        WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_MapM2M        WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.No + "')) AS X) AND FK_MapData='" + this.No + "' ";
				sqls += "@UPDATE Sys_FrmAttachment SET GroupID=" + gfFirst.getOID() + " WHERE  MyPK IN (SELECT X.MyPK FROM (SELECT MyPK FROM Sys_FrmAttachment WHERE GroupID NOT IN (SELECT OID FROM Sys_GroupField WHERE EnName='" + this.getNo() + "')) X) AND FK_MapData='" + this.getNo() + "' ";
             
				///#warning 这些sql 对于Oracle 有问题，但是不影响使用.
				try
				{
					DBAccess.RunSQLs(sqls);
				}
				catch (java.lang.Exception e)
				{
				}
             }
		}
		BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		if (this.getEnPK().equals("OID"))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, "OID", MapAttrAttr.FK_MapData, this.getNo()) == false)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn("OID");
				attr.setName("OID");
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setDefVal("0");
				attr.setHisEditType(BP.En.EditType.Readonly);
				attr.Insert();
			}
		}
		if (this.getEnPK().equals("No") || this.getEnPK().equals("MyPK"))
		{
			if (attr.IsExit(MapAttrAttr.KeyOfEn, this.getEnPK(), MapAttrAttr.FK_MapData, this.getNo()) == false)
			{
				attr.setFK_MapData(this.getNo());
				attr.setKeyOfEn(this.getEnPK());
				attr.setName(this.getEnPK());
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setDefVal("0");
				attr.setHisEditType(BP.En.EditType.Readonly);
				attr.Insert();
			}
		}

		if (attr.IsExit(MapAttrAttr.KeyOfEn, "RDT", MapAttrAttr.FK_MapData, this.getNo()) == false)
		{
			attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setHisEditType(BP.En.EditType.UnDel);
			attr.setKeyOfEn("RDT");
			attr.setName("更新时间");

			attr.setMyDataType(BP.DA.DataType.AppDateTime);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("@RDT");
			attr.setTag("1");
			attr.Insert();
		}

		//检查特殊UIBindkey丢失的问题.
		MapAttrs attrs = new MapAttrs();
		attrs.Retrieve(MapAttrAttr.FK_MapData, this.getNo());

		for (Object item : attrs)
		{
			if (((MapAttr) item).getLGType() == FieldTypeS.Enum || ((MapAttr) item).getLGType() == FieldTypeS.FK)
			{
				if (StringHelper.isNullOrEmpty(((MapAttr) item).getUIBindKey()) == true)
				{
					((MapAttr) item).setLGType(FieldTypeS.Normal);
					((MapAttr) item).setUIContralType(UIContralType.TB);
					((Entity) item).Update();
				}
			}
		}
	}
	@Override
	protected boolean beforeInsert()
	{
		this.setPTable(PubClass.DealToFieldOrTableNames(this.getPTable()));
		return super.beforeInsert();
	}
	/** 
	 设置Para 参数.
	 * @throws Exception 
	 
	*/
	/**
	 * 设置Para 参数.
	 */
	public final void ResetMaxMinXY()
	{
		float i1 = 0;
		try
		{
			i1 = DBAccess.RunSQLReturnValFloat(
					"SELECT MIN(X1) FROM Sys_FrmLine WHERE FK_MapData='"
							+ this.getNo() + "'", 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (i1 == 0)
		{
			try
			{
				i1 = DBAccess.RunSQLReturnValFloat(
						"SELECT MIN(X) FROM Sys_FrmImg WHERE FK_MapData='"
								+ this.getNo() + "'", 0);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		float i2 = 0;
		try
		{
			i2 = DBAccess.RunSQLReturnValFloat(
					"SELECT MIN(X)  FROM Sys_FrmLab  WHERE FK_MapData='"
							+ this.getNo() + "'", 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (i1 > i2)
		{
			this.setMaxLeft(i2);
		} else
		{
			this.setMaxLeft(i1);
		}
		
		// 求最右边.
		try
		{
			i1 = DBAccess.RunSQLReturnValFloat(
					"SELECT Max(X2) FROM Sys_FrmLine WHERE FK_MapData='"
							+ this.getNo() + "'", 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (i1 == 0)
		{
			// 没有线的情况，按照图片来计算。
			try
			{
				i1 = DBAccess.RunSQLReturnValFloat(
						"SELECT Max(X+W) FROM Sys_FrmImg WHERE FK_MapData='"
								+ this.getNo() + "'", 0);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		this.setMaxRight(i1);
		
		// 求最top.
		try
		{
			i1 = DBAccess.RunSQLReturnValFloat(
					"SELECT MIN(Y1) FROM Sys_FrmLine WHERE FK_MapData='"
							+ this.getNo() + "'", 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			i2 = DBAccess.RunSQLReturnValFloat(
					"SELECT MIN(Y)  FROM Sys_FrmLab  WHERE FK_MapData='"
							+ this.getNo() + "'", 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (i1 > i2)
		{
			this.setMaxTop(i2);
		} else
		{
			this.setMaxTop(i1);
		}
		
		try
		{
			i1 = DBAccess.RunSQLReturnValFloat(
					"SELECT Max(Y1) FROM Sys_FrmLine WHERE FK_MapData='"
							+ this.getNo() + "'", 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (i1 == 0)
		{
			try
			{
				i1 = DBAccess.RunSQLReturnValFloat(
						"SELECT Max(Y+H) FROM Sys_FrmImg WHERE FK_MapData='"
								+ this.getNo() + "'", 0);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// 小周鹏添加2014/10/23-----------------------START
		try
		{
			i2 = DBAccess.RunSQLReturnValFloat(
					"SELECT Max(Y)  FROM Sys_FrmLab  WHERE FK_MapData='"
							+ this.getNo() + "'", 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (i2 == 0)
		{
			try
			{
				i2 = DBAccess.RunSQLReturnValFloat(
						"SELECT Max(Y+H) FROM Sys_FrmImg WHERE FK_MapData='"
								+ this.getNo() + "'", 0);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if (i1 > i2)
		{
			this.setMaxEnd(i1);
		} else
		{
			this.setMaxEnd(i2);
		}
		
		this.DirectUpdate();

		// 清理缓存，否则获取不到MaxLeft值，导致第一次打开表单便宜
		this.getRow().remove("_atobj_");
	}

	/**
	 * 求位移.
	 * 
	 * @param md
	 * @param scrWidth
	 * @return
	 */
	public static float GenerSpanWeiYi(MapData md, float scrWidth)
	{
		if (scrWidth == 0)
		{
			scrWidth = 900;
		}
		
		float left = md.getMaxLeft();
		if (left == 0)
		{
			md.ResetMaxMinXY();
			md.RetrieveFromDBSources();
			md.Retrieve();
			
			left = md.getMaxLeft();
		}
		
		float right = md.getMaxRight();
		float withFrm = right - left;
		if (withFrm >= scrWidth)
		{
			return -left;
		}
		float space = (scrWidth - withFrm) / 2; // �հ׵ĵط�.
		
		return -(left - space);
	}
	/** 
	 求屏幕宽度
	 
	 @param md
	 @param scrWidth
	 @return 
	 * @throws Exception 
	*/
	public static float GenerSpanWidth(MapData md, float scrWidth) throws Exception
	{
		if (scrWidth == 0)
		{
			scrWidth = 900;
		}
		float left = md.getMaxLeft();
		if (left == 0)
		{
			md.ResetMaxMinXY();
			left = md.getMaxLeft();
		}

		float right = md.getMaxRight();
		float withFrm = right - left;
		if (withFrm >= scrWidth)
		{
			return withFrm;
		}
		return scrWidth;
	}
	/** 
	 求屏幕高度
	 
	 @param md
	 @param scrWidth
	 @return 
	*/
	public static float GenerSpanHeight(MapData md, float scrHeight)
	{
		if (scrHeight == 0)
		{
			scrHeight = 1200;
		}

		float end = md.getMaxEnd();
		if (end > scrHeight)
		{
			return end + 10;
		}
		else
		{
			return scrHeight;
		}
	}
	@Override
	protected boolean beforeUpdateInsertAction()
	{
		this.setPTable(PubClass.DealToFieldOrTableNames(this.getPTable()));
		getMapAttrs().Retrieve(MapAttrAttr.FK_MapData, getPTable());

		//更新版本号.
		this.setVer(DataType.getCurrentDataTimess());


			///#region  检查是否有ca认证设置.
		boolean isHaveCA = false;
		for (Object item : this.getMapAttrs())
		{
			if (((MapAttr) item).getSignType() == SignType.CA)
			{
				isHaveCA = true;
				break;
			}
		}
		this.setIsHaveCA(isHaveCA);
		if (getIsHaveCA() == true)
		{
			//就增加隐藏字段.
			//MapAttr attr = new BP.Sys.MapAttr();
			// attr.MyPK = this.No + "_SealData";
			// attr.FK_MapData = this.No;
			// attr.HisEditType = BP.En.EditType.UnDel;
			//attr.KeyOfEn = "SealData";
			// attr.Name = "SealData";
			// attr.MyDataType = BP.DA.DataType.AppString;
			// attr.UIContralType = UIContralType.TB;
			//  attr.LGType = FieldTypeS.Normal;
			// attr.UIVisible = false;
			// attr.UIIsEnable = false;
			// attr.MaxLen = 4000;
			// attr.MinLen = 0;
			// attr.Save();
		}

			///#endregion  检查是否有ca认证设置.

		return super.beforeUpdateInsertAction();
	}
	/** 
	 更新版本
	*/
	public final void UpdateVer()
	{
		String sql = "UPDATE Sys_MapData SET VER='" + BP.DA.DataType.getCurrentDataTimess() + "' WHERE No='" + this.getNo() + "'";
		BP.DA.DBAccess.RunSQL(sql);
	}
	@Override
	protected boolean beforeDelete()
	{
		String sql = "";
		sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='" + this.getNo() + "'";
		DataTable Sys_MapDtl = DBAccess.RunSQLReturnTable(sql);
		//String ids = "'" + this.getNo() + "'";
		String whereFK_MapData = "FK_MapData= '" + this.getNo() + "' ";
		String whereEnsName = "EnName= '" + this.getNo() + "' ";
		String whereNo = "No='" + this.getNo() + "' ";
		
		for (DataRow dr : Sys_MapDtl.Rows)
		{
			//ids += ",'" + dr.get("No") + "'";
			whereFK_MapData += " OR FK_MapData='" + dr.get("No") + "' ";
			whereEnsName += " OR EnName='" + dr.get("No") + "' ";
			whereNo += " OR No='" + dr.get("No") + "' ";
		}

		//String where = " FK_MapData IN (" + ids + ")";


			///#region 删除相关的数据。
		 sql = "DELETE FROM Sys_MapDtl WHERE FK_MapData='" + this.getNo() + "'";
			sql += "@DELETE FROM Sys_FrmLine WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmEle WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmEvent WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmBtn WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmLab WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmLink WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmImg WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmImgAth WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmRB WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_FrmAttachment WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_MapM2M WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_MapFrame WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_MapExt WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_MapAttr WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_GroupField WHERE " + whereEnsName;
			sql += "@DELETE FROM Sys_MapData WHERE " + whereNo;
			sql += "@DELETE FROM Sys_MapM2M WHERE " + whereFK_MapData;
			sql += "@DELETE FROM Sys_M2M WHERE " + whereFK_MapData;
			sql += "@DELETE FROM WF_FrmNode WHERE FK_Frm='" + this.getNo() + "'";
			sql += "@DELETE FROM Sys_FrmSln WHERE " + whereFK_MapData;
		DBAccess.RunSQLs(sql);

			///#endregion 删除相关的数据。


			///#region 删除物理表。
		//如果存在物理表.
		if (DBAccess.IsExitsObject(this.getPTable()) && this.getPTable().indexOf("ND")==0 )
		{
			//如果其他表单引用了该表，就不能删除它.
			sql = "SELECT COUNT(No) AS NUM  FROM Sys_MapData WHERE PTable='" + this.getPTable() + "' OR ( PTable='' AND No='" + this.getPTable() + "')";
			if (DBAccess.RunSQLReturnValInt(sql, 0) > 1)
			{
				//说明有多个表单在引用.
			}
			else
			{
				//edit by zhoupeng 误删已经有数据的表.
				// if (DBAccess.RunSQLReturnValInt("SELECT COUNT(*) FROM " + this.getPTable() + " WHERE 1=1 ") == 0){
				DBAccess.RunSQL("DROP TABLE " + this.getPTable());}
			//}
		}

		MapDtls dtls = new MapDtls(this.getNo());
		for (Object dtl : dtls)
		{
			((Entity) dtl).Delete();
		}
		return super.beforeDelete();
	}

	/** 
	 @param mes
	 @param en
	 @return 
	*/
	public final GEEntity GenerHisEn(MapExts mes, GEEntity en)
	{
		return en;
	}
	
	public static DataSet GenerHisDataSet(String FK_MapData)
	{
		
		// Sys_MapDtl.
		String sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='" + FK_MapData + "'";
		
		DataTable dtMapDtl = DBAccess.RunSQLReturnTable(sql);
		dtMapDtl.TableName = "Sys_MapDtl";
		
		String ids = "'" + FK_MapData + "'";
		for (DataRow dr : dtMapDtl.Rows)
		{
			ids += ",'" + dr.getValue("No") + "'";
		}
		
		List<String> listNames = new ArrayList<String>();
		// Sys_GroupField.
		listNames.add("Sys_GroupField");
		sql = "SELECT * FROM Sys_GroupField WHERE  EnName IN (" + ids + ")";
		String sqls = sql;
		
		// Sys_Enum
		listNames.add("Sys_Enum");
		sql = "@SELECT * FROM Sys_Enum WHERE EnumKey IN ( SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData IN (" + ids + ") )";
		sqls += sql;
		
		// 审核组件
		String nodeIDstr = FK_MapData.replace("ND", "");
		if (DataType.IsNumStr(nodeIDstr))
		{
			// 审核组件状态:0 禁用;1 启用;2 只读;
			listNames.add("WF_Node");
			sql = "@SELECT NodeID,FWC_X,FWC_Y,FWC_W,FWC_H,FWCSTA,FWCTYPE,SFSTA,SF_X,SF_Y,SF_H,SF_W FROM WF_Node WHERE NodeID="
					+ nodeIDstr + " AND ( FWCSta >0  OR SFSta >0 )";
			sqls += sql;
		}
		
		String where = " FK_MapData IN (" + ids + ")";
		
		// Sys_MapData.
		listNames.add("Sys_MapData");
		sql = "@SELECT No,Name,FrmW,FrmH FROM Sys_MapData WHERE No='" + FK_MapData + "'";
		sqls += sql;
		
		// Sys_MapAttr.
		listNames.add("Sys_MapAttr");
		sql = "@SELECT * FROM Sys_MapAttr WHERE " + where
				+ " AND KeyOfEn NOT IN('WFState') ORDER BY FK_MapData,IDX ";
		sqls += sql;
		
		// Sys_MapM2M.
		listNames.add("Sys_MapM2M");
		sql = "@SELECT MyPK,FK_MapData,NoOfObj,M2MTYPE,X,Y,W,H FROM Sys_MapM2M WHERE " + where;
		sqls += sql;
		
		// Sys_MapExt.
		listNames.add("Sys_MapExt");
		sql = "@SELECT * FROM Sys_MapExt WHERE " + where;
		sqls += sql;
		
		// line.
		listNames.add("Sys_FrmLine");
		sql = "@SELECT MyPK,FK_MapData,X,X1,X2,Y,Y1,Y2,BorderColor,BorderWidth FROM Sys_FrmLine WHERE " + where;
		sqls += sql;
		
		// ele.
		listNames.add("Sys_FrmEle");
		sql = "@SELECT FK_MapData,MyPK,EleType,EleID,EleName,X,Y,W,H FROM Sys_FrmEle WHERE "
				+ where;
		sqls += sql;
		
		// link.
		listNames.add("Sys_FrmLink");
		sql = "@SELECT FK_MapData,MyPK,Text,URL,Target,FontSize,FontColor,X,Y FROM Sys_FrmLink WHERE "
				+ where;
		sqls += sql;
		
		// btn.
		listNames.add("Sys_FrmBtn");
		sql = "@SELECT FK_MapData,MyPK,Text,BtnType,EventType,EventContext,MsgErr,MsgOK,X,Y FROM Sys_FrmBtn WHERE "
				+ where;
		sqls += sql;
		
		// Sys_FrmImg.
		listNames.add("Sys_FrmImg");
		sql = "@SELECT * FROM Sys_FrmImg WHERE " + where;
		sqls += sql;
		
		// Sys_FrmLab.
		listNames.add("Sys_FrmLab");
		sql = "@SELECT MyPK,FK_MapData,Text,X,Y,FontColor,FontName,FontSize,FontStyle,FontWeight,IsBold,IsItalic FROM Sys_FrmLab WHERE "
				+ where;
		sqls += sql;
		
		// Sys_FrmRB.
		listNames.add("Sys_FrmRB");
		sql = "@SELECT * FROM Sys_FrmRB WHERE " + where;
		sqls += sql;
		
		// Sys_FrmAttachment.
		listNames.add("Sys_FrmAttachment");
		sql = "@SELECT MyPK,FK_MapData,UploadType,X,Y,W,H,NoOfObj,Name,Exts,SaveTo,IsUpload,IsDelete,IsDownload ,AtPara"
				+ " FROM Sys_FrmAttachment WHERE " + where + " AND FK_Node=0";
		sqls += sql;
		
		// Sys_FrmImgAth.
		listNames.add("Sys_FrmImgAth");
		sql = "@SELECT * FROM Sys_FrmImgAth WHERE " + where;
		sqls += sql;
		
		// DataSet ds = DBAccess.RunSQLReturnDataSet_UL(sqls);
		// if (ds != null && ds.Tables.size() == listNames.size())
		// for (int i = 0; i < listNames.size(); i++)
		// {
		// ds.Tables.get(i).TableName = listNames.get(i);
		// }
		
		// string[] strs = sqls.Split(';');
		// DataSet ds = new DataSet();
		// for (int i = 0; i < strs.Length; i++)
		// {
		// sql = strs[i];
		// if (string.IsNullOrEmpty(sql))
		// continue;
		
		// DataTable dt = RunSQLReturnTable(sql);
		// string tableName = "DT" + i;
		// try
		// {
		// tableName = listNames[i];
		// //int indexStart = sql.IndexOf("From ",
		// StringComparison.OrdinalIgnoreCase) + 5;
		// //int indexEnd = sql.IndexOf(" WHERE",
		// StringComparison.OrdinalIgnoreCase) - indexStart;
		// //tableName = sql.Substring(indexStart, indexEnd);
		// }
		// catch (Exception) { }
		// dt.TableName = tableName;
		// ds.Tables.Add(dt);
		// }
		
		String[] strs = sqls.split("@");
		DataSet ds = new DataSet();
		
		if (strs != null && strs.length == listNames.size())
		{
			for (int i = 0; i < listNames.size(); i++)
			{
				String s = strs[i];
				if (StringHelper.isNullOrEmpty(s))
					continue;
				DataTable dt = BP.DA.DBAccess.RunSQLReturnTable(s);
				if(dt!=null){
					dt.TableName = listNames.get(i);
					ds.Tables.add(dt);
				}
			}
		}
		
		for (DataTable item : ds.Tables)
		{
			if (item.TableName.equals("Sys_MapAttr") && item.Rows.size() == 0)
			{
				MapAttr attr = new MapAttr();
				attr.setFK_MapData(FK_MapData);
				attr.setKeyOfEn("OID");
				attr.setName("OID");
				attr.setMyDataType(BP.DA.DataType.AppInt);
				attr.setUIContralType(UIContralType.TB);
				attr.setLGType(FieldTypeS.Normal);
				attr.setUIVisible(false);
				attr.setUIIsEnable(false);
				attr.setDefVal("0");
				attr.setHisEditType(EditType.Readonly);
				attr.Insert();
			}
		}
		
		ds.Tables.add(dtMapDtl);
		return ds;
	}
	
	public final DataSet GenerHisDataSet()
	{
		DataSet ds = new DataSet();
		String sql = "";
		
		// Sys_MapDtl.
		sql = "SELECT * FROM Sys_MapDtl WHERE FK_MapData ='" + this.getNo()
				+ "'";
		DataTable Sys_MapDtl = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_MapDtl.TableName = "Sys_MapDtl";
		// ds.Tables.add(Sys_MapDtl);
		// ds.hashTables.put(Sys_MapDtl.TableName, Sys_MapDtl);
		String ids = "'" + this.getNo() + "'";
		for (DataRow dr : Sys_MapDtl.Rows)
		{
			ids += ",'" + dr.getValue("No") + "'";
		}
		String where = " FK_MapData IN (" + ids + ")";
		
		// Sys_GroupField.
		sql = "SELECT * FROM Sys_GroupField WHERE  EnName IN (" + ids + ")";
		DataTable Sys_GroupField = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_GroupField.TableName = "Sys_GroupField";
		ds.Tables.add(Sys_GroupField);
		ds.hashTables.put(Sys_GroupField.TableName, Sys_GroupField);
		
		// Sys_Enum
		sql = "SELECT * FROM Sys_Enum WHERE EnumKey IN ( SELECT UIBindKey FROM Sys_MapAttr WHERE FK_MapData IN ("
				+ ids + ") )";
		DataTable Sys_Enum = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_Enum.TableName = "Sys_Enum";
		ds.Tables.add(Sys_Enum);
		ds.hashTables.put("Sys_Enum", Sys_Enum);
		
		String nodeIDstr = this.getNo().replace("ND", "");
		if (DataType.IsNumStr(nodeIDstr))
		{
			// sql =
			// "SELECT NodeID,FWCSta,FWC_X,FWC_Y,FWC_H,FWC_W,FWCType,* FROM WF_Node WHERE NodeID="
			// + nodeIDstr;
			sql = "SELECT * FROM WF_Node WHERE NodeID=" + nodeIDstr;
			DataTable WF_Node = DBAccess.RunSQLReturnTable_UL(sql);
			if (WF_Node.Rows.size() == 1)
			{
				if (WF_Node.Rows.get(0).get("FWCSta") == null
						|| !WF_Node.Rows.get(0).get("FWCSta").toString()
								.equals("0"))
				{
					WF_Node.TableName = "WF_Node";
					ds.Tables.add(WF_Node);
					ds.hashTables.put(WF_Node.TableName, WF_Node);
				}
			}
		}
		
		// Sys_MapData.
		// sql = "SELECT * FROM Sys_MapData WHERE No IN (" + ids + ")";
		sql = "SELECT * FROM Sys_MapData WHERE No='" + this.getNo() + "'";
		DataTable Sys_MapData = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_MapData.TableName = "Sys_MapData";
		ds.Tables.add(Sys_MapData);
		ds.hashTables.put(Sys_MapData.TableName, Sys_MapData);
		
		// Sys_MapAttr.
		sql = "SELECT * FROM Sys_MapAttr WHERE " + where
				+ " AND KeyOfEn NOT IN('WFState') ORDER BY FK_MapData,IDX ";
		DataTable Sys_MapAttr = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_MapAttr.TableName = "Sys_MapAttr";
		if (Sys_MapAttr.Rows.size() == 0)
		{
			BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
			attr.setFK_MapData(this.getNo());
			attr.setKeyOfEn("OID");
			attr.setName("OID");
			attr.setMyDataType(BP.DA.DataType.AppInt);
			attr.setUIContralType(UIContralType.TB);
			attr.setLGType(FieldTypeS.Normal);
			attr.setUIVisible(false);
			attr.setUIIsEnable(false);
			attr.setDefVal("0");
			attr.setHisEditType(BP.En.EditType.Readonly);
			attr.Insert();
		}
		ds.Tables.add(Sys_MapAttr);
		ds.hashTables.put(Sys_MapAttr.TableName, Sys_MapAttr);
		
		// Sys_MapM2M.
		sql = "SELECT * FROM Sys_MapM2M WHERE " + where;
		DataTable Sys_MapM2M = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_MapM2M.TableName = "Sys_MapM2M";
		ds.Tables.add(Sys_MapM2M);
		ds.hashTables.put(Sys_MapM2M.TableName, Sys_MapM2M);
		
		// Sys_MapExt.
		sql = "SELECT * FROM Sys_MapExt WHERE " + where;
		DataTable Sys_MapExt = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_MapExt.TableName = "Sys_MapExt";
		ds.Tables.add(Sys_MapExt);
		ds.hashTables.put(Sys_MapExt.TableName, Sys_MapExt);
		
		// line.
		sql = "SELECT * FROM Sys_FrmLine WHERE " + where;
		DataTable dtLine = DBAccess.RunSQLReturnTable_UL(sql);
		dtLine.TableName = "Sys_FrmLine";
		ds.Tables.add(dtLine);
		ds.hashTables.put(dtLine.TableName, dtLine);
		
		// ele.
		sql = "SELECT * FROM Sys_FrmEle WHERE " + where;
		DataTable dtFrmEle = DBAccess.RunSQLReturnTable_UL(sql);
		dtFrmEle.TableName = "Sys_FrmEle";
		ds.Tables.add(dtFrmEle);
		ds.hashTables.put(dtFrmEle.TableName, dtFrmEle);
		
		// link.
		sql = "SELECT * FROM Sys_FrmLink WHERE " + where;
		DataTable dtLink = DBAccess.RunSQLReturnTable_UL(sql);
		dtLink.TableName = "Sys_FrmLink";
		ds.Tables.add(dtLink);
		ds.hashTables.put(dtLink.TableName, dtLink);
		
		// btn.
		sql = "SELECT * FROM Sys_FrmBtn WHERE " + where;
		DataTable dtBtn = DBAccess.RunSQLReturnTable_UL(sql);
		dtBtn.TableName = "Sys_FrmBtn";
		ds.Tables.add(dtBtn);
		ds.hashTables.put(dtBtn.TableName, dtBtn);
		
		// Sys_FrmImg.
		sql = "SELECT * FROM Sys_FrmImg WHERE " + where;
		DataTable dtFrmImg = DBAccess.RunSQLReturnTable_UL(sql);
		dtFrmImg.TableName = "Sys_FrmImg";
		ds.Tables.add(dtFrmImg);
		ds.hashTables.put(dtFrmImg.TableName, dtFrmImg);
		
		// Sys_FrmLab.
		sql = "SELECT * FROM Sys_FrmLab WHERE " + where;
		DataTable Sys_FrmLab = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_FrmLab.TableName = "Sys_FrmLab";
		ds.Tables.add(Sys_FrmLab);
		ds.hashTables.put(Sys_FrmLab.TableName, Sys_FrmLab);
		
		// Sys_FrmRB.
		sql = "SELECT * FROM Sys_FrmRB WHERE " + where;
		DataTable Sys_FrmRB = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_FrmRB.TableName = "Sys_FrmRB";
		ds.Tables.add(Sys_FrmRB);
		ds.hashTables.put(Sys_FrmRB.TableName, Sys_FrmRB);
		
		// Sys_FrmAttachment.
		sql = "SELECT * FROM Sys_FrmAttachment WHERE " + where
				+ " AND FK_Node=0";
		DataTable Sys_FrmAttachment = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_FrmAttachment.TableName = "Sys_FrmAttachment";
		ds.Tables.add(Sys_FrmAttachment);
		ds.hashTables.put(Sys_FrmAttachment.TableName, Sys_FrmAttachment);
		
		// Sys_FrmImgAth.
		sql = "SELECT * FROM Sys_FrmImgAth WHERE " + where;
		DataTable Sys_FrmImgAth = DBAccess.RunSQLReturnTable_UL(sql);
		Sys_FrmImgAth.TableName = "Sys_FrmImgAth";
		ds.Tables.add(Sys_FrmImgAth);
		ds.hashTables.put(Sys_FrmImgAth.TableName, Sys_FrmImgAth);
		
		ds.Tables.add(Sys_MapDtl);
		ds.hashTables.put(Sys_MapDtl.TableName, Sys_MapDtl);
		
		// // Sys_MapAttr.
		// sql = "SELECT * FROM Sys_MapAttr WHERE " + where +
		// " AND KeyOfEn NOT IN('WFState') ORDER BY FK_MapData,IDX ";
		// DataTable Sys_MapAttr = DBAccess.RunSQLReturnTable_UL(sql);
		// Sys_MapAttr.TableName = "Sys_MapAttr";
		// if (Sys_MapAttr.Rows.size() == 0)
		// {
		// BP.Sys.MapAttr attr = new BP.Sys.MapAttr();
		// attr.setFK_MapData(this.getNo());
		// attr.setKeyOfEn("OID");
		// attr.setName("OID");
		// attr.setMyDataType(BP.DA.DataType.AppInt);
		// attr.setUIContralType(UIContralType.TB);
		// attr.setLGType(FieldTypeS.Normal);
		// attr.setUIVisible(false);
		// attr.setUIIsEnable(false);
		// attr.setDefVal("0");
		// attr.setHisEditType(BP.En.EditType.Readonly);
		// attr.Insert();
		// }
		// ds.Tables.add(Sys_MapAttr);
		// ds.hashTables.put(Sys_MapAttr.TableName, Sys_MapAttr);
		
		return ds;
	}
	/** 
	 生成自动的ｊｓ程序。
	 @param pk
	 @param attrs
	 @param attr
	 @param tbPer
	 @return 
	*/
	public static String GenerAutoFull(String pk, MapAttrs attrs, MapExt me, String tbPer)
	{
		String left = "\n document.forms[0]." + tbPer + "_TB" + me.getAttrOfOper() + "_" + pk + ".value = ";
		String right = me.getDoc();
		for (Object mattr : attrs)
		{
			right = right.replace("@" + ((MapAttr) mattr).getKeyOfEn(), " parseFloat( document.forms[0]." + tbPer + "_TB_" + ((MapAttr) mattr).getKeyOfEn() + "_" + pk + ".value) ");
		}
		return " alert( document.forms[0]." + tbPer + "_TB" + me.getAttrOfOper() + "_" + pk + ".value ) ; \t\n " + left + right;
	}
	/** 
	 获得Excel文件流
	 @param oid
	 @return 
	*/
	public final boolean ExcelGenerFile(int oid, RefObject<byte[]> bytes)
	{
		byte[] by = BP.DA.DBAccess.GetByteFromDB(this.getPTable(), this.getEnPK(), (new Integer(oid)).toString(), "DBFile");
		if (by != null)
		{
			bytes.argvalue = by;
			return true;
		}
		else //说明当前excel文件没有生成.
		{
			String tempExcel = BP.Sys.SystemConfig.getPathOfDataUser() + "FrmOfficeTemplate/" + this.getNo() + ".xlsx";
			File file = new File(tempExcel);
			if (file.exists() == true)
			{
				bytes.argvalue = BP.DA.DataType.ConvertFileToByte(tempExcel);
				return false;
			}
			else //模板文件也不存在时
			{
				throw new RuntimeException("@没有找到模版文件." + tempExcel + " 请确认表单配置.");
			}
		}
	}
	/** 
	 * 保存excel文件
	 * @param oid
	 * @param bty
	 * @throws Exception 
	*/
	public final void ExcelSaveFile(int oid, byte[] bty) throws Exception
	{
		BP.DA.DBAccess.SaveFileToDB(bty.toString(), this.getPTable(), this.getEnPK(), (new Integer(oid)).toString(), "DBFile");
	}
	//#endregion 与Excel相关的操作 .
	
	/** 
	 * 表单类型名称
	 */
	public final String getHisFrmTypeText()
	{
		SysEnum se = new SysEnum("FrmType", this.getHisFrmTypeInt());
		return se.getLab();
	}
	
	/** 
	 * 类别名称
	 */
	public final String getFK_FormTreeText()
	{
		return DBAccess.RunSQLReturnStringIsNull("SELECT Name FROM Sys_FormTree WHERE No='" + this.getNo() + "'", "数据无");
	}
}