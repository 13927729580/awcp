package org.szcloud.framework.workflow.core.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EFormLineType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 表单表格对象：是各连线对象的一个闭路表现。
 * 
 * @author Jackie.Wang
 * 
 */
public class CFormTable extends CBase {
	/**
	 * 初始化
	 */
	public CFormTable() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CFormTable(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的表单定定义
	 */
	public CForm Form = null;

	/**
	 * 所包含的表单连线集合
	 */
	public List<CFormLine> FormLines = new ArrayList<CFormLine>();

	/**
	 * 根据标识获取表单连线
	 * 
	 * @param al_Id
	 *            标识
	 * @return
	 */
	public CFormLine getFormLineById(int al_Id) {
		for (CFormLine lo_Line : FormLines) {
			if (lo_Line.LineID == al_Id)
				return lo_Line;
		}
		return null;
	}

	/**
	 * 表单表格位置
	 */
	private CRectangle mo_Rectangle;

	/**
	 * 表单表格位置
	 * 
	 * @return
	 */
	public CRectangle getRectangle() {
		return mo_Rectangle;
	}

	/**
	 * 表单表格位置
	 * 
	 * @param value
	 */
	public void setRectangle(CRectangle value) {
		mo_Rectangle = value;
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 表格单元标识
	 */
	public int TableID = -1;

	/**
	 * 是否内部插入连线操作
	 */
	public boolean InsideHandle = false;

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		this.Form = null;
		// 所包含的表单连线集合
		if (this.FormLines != null) {
			while (this.FormLines.size() > 0) {
				CFormLine lo_Line = FormLines.get(0);
				this.FormLines.remove(lo_Line);
				lo_Line.ClearUp();
				lo_Line = null;
			}
			this.FormLines = null;
		}

		// 表单表格位置
		if (mo_Rectangle != null) {
			mo_Rectangle.ClearUp();
			mo_Rectangle = null;
		}

		super.ClearUp();
	}

	/**
	 * 当前对象是否可用
	 * 
	 * @param ai_SpaceLength
	 * @return
	 * @throws Exception 
	 */
	public String IsValid(int ai_SpaceLength) throws Exception {
		try {
			return "";
		} catch (Exception e) {
			this.Raise(e, "IsValid", null);
			return e.toString();
		}
	}

	/**
	 * 初始化必要边线
	 */
	public void initFormTable() {
		this.InsideHandle = true;

		// #插入边线
		// #左边线
		InsertLine(mo_Rectangle.getRectLeft(), mo_Rectangle.getRectTop(),
				mo_Rectangle.getRectLeft(), mo_Rectangle.getRectBottom());
		// #上边线
		InsertLine(mo_Rectangle.getRectLeft(), mo_Rectangle.getRectTop(),
				mo_Rectangle.getRectRight(), mo_Rectangle.getRectTop());
		// #右边线
		InsertLine(mo_Rectangle.getRectRight(), mo_Rectangle.getRectTop(),
				mo_Rectangle.getRectRight(), mo_Rectangle.getRectBottom());
		// #底边线
		InsertLine(mo_Rectangle.getRectLeft(), mo_Rectangle.getRectBottom(),
				mo_Rectangle.getRectRight(), mo_Rectangle.getRectBottom());

		this.InsideHandle = false;
	}

	/**
	 * 插入表格连线
	 * 
	 * @param al_StartX
	 *            起点横坐标
	 * @param al_StartY
	 *            起点纵坐标
	 * @param al_EndX
	 *            终点横坐标
	 * @param al_EndY
	 *            终点纵坐标
	 * @return
	 */
	public CFormLine InsertLine(int al_StartX, int al_StartY, int al_EndX,
			int al_EndY) {
		int ll_StartX = al_StartX;
		int ll_StartY = al_StartY;
		int ll_EndX = al_EndX;
		int ll_EndY = al_EndY;
		int ll_Start = 0;
		int ll_End = 0;
		if (this.InsideHandle == false) {
			if ((ll_StartX - ll_EndX) >= (ll_StartY - ll_EndY)) {
				ll_EndY = ll_StartY;
				ll_Start = mo_Rectangle.getRectLeft();
				ll_End = mo_Rectangle.getRectRight();
				for (CFormLine lo_Line : this.FormLines) {
					if (lo_Line.StartX == lo_Line.EndX) {
						if (lo_Line.StartX <= ll_StartY
								& lo_Line.EndY >= ll_StartY) {
							if ((lo_Line.StartX <= ll_StartX)
									&& (lo_Line.StartX >= ll_Start)) {
								ll_Start = lo_Line.StartX;
							}
							if ((lo_Line.StartX >= ll_EndX)
									&& (lo_Line.StartX <= ll_End)) {
								ll_End = lo_Line.StartX;
							}
						}
					}
				}
				ll_StartX = ll_Start;
				ll_EndX = ll_End;
			} else {
				ll_EndX = ll_StartX;
				ll_Start = mo_Rectangle.getRectTop();
				ll_End = mo_Rectangle.getRectBottom();
				for (CFormLine lo_Line : this.FormLines) {
					if (lo_Line.StartX == lo_Line.EndY) {
						if (lo_Line.StartX <= ll_StartX
								& lo_Line.EndX >= ll_StartX) {
							if ((lo_Line.StartY <= ll_StartY)
									&& (lo_Line.StartY >= ll_Start)) {
								ll_Start = lo_Line.StartY;
							}
							if ((lo_Line.StartY >= ll_EndY)
									&& (lo_Line.StartY <= ll_End)) {
								ll_End = lo_Line.StartY;
							}
						}
					}
				}
				ll_StartY = ll_Start;
				ll_EndY = ll_End;
			}
		}

		CFormLine lo_Line = new CFormLine(this.Logon);
		lo_Line.FormTable = this;
		int ll_Id = this.Form.Cyclostyle.FormLineMaxID + 1;
		this.Form.Cyclostyle.FormLineMaxID = ll_Id;
		lo_Line.LineID = ll_Id;
		lo_Line.StartX = ll_StartX;
		lo_Line.StartY = ll_StartY;
		lo_Line.EndX = ll_EndX;
		lo_Line.EndY = ll_EndY;
		lo_Line.LineType = EFormLineType.TableLineType;
		this.FormLines.add(lo_Line);
		return lo_Line;
	}

	/**
	 * 从Xml节点导入到对象
	 * 
	 * @param ao_Node
	 *            导入节点
	 * @param ai_Type
	 *            导入类型：=0-正常导入；=1-短属性导入；
	 * @throws Exception 
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) throws Exception {
		int ll_Left = 0;
		int ll_Top = 0;
		int ll_Width = 0;
		int ll_Height = 0;
		if (ai_Type == 1) {
			this.TableID = Integer.parseInt(ao_Node.getAttribute("ID"));
		} else {
			this.TableID = Integer.parseInt(ao_Node.getAttribute("TableID"));
		}

		// #导入表格连线
		NodeList lo_List = ao_Node.getElementsByTagName("Line");
		for (int i = 0; i < lo_List.getLength(); i++) {
			Element lo_Node = (Element) lo_List.item(i);
			CFormLine lo_Line = new CFormLine(this.Logon);
			lo_Line.Import(lo_Node, ai_Type);
			lo_Line.FormTable = this;
			this.FormLines.add(lo_Line);
			if (i == 0)
				ll_Left = lo_Line.StartX;
			ll_Top = lo_Line.StartY;
			if (i == 3)
				ll_Width = lo_Line.EndX - ll_Left;
			ll_Height = lo_Line.EndY - ll_Top;
		}
		mo_Rectangle.moveRectangle(ll_Left, ll_Top, ll_Width, ll_Height);
	}

	/**
	 * 从对象导出到Xml节点
	 * 
	 * @param ao_Node
	 *            导出节点
	 * @param ai_Type
	 *            导出类型：=0-正常导出；=1-短属性导出；
	 */
	@Override
	protected void Export(Element ao_Node, int ai_Type) {
		if (ai_Type == 1) {
			ao_Node.setAttribute("ID", Integer.toString(this.TableID));
		} else {
			ao_Node.setAttribute("TableID", Integer.toString(this.TableID));
		}

		// #导出表格连线
		for (CFormLine lo_Line : this.FormLines) {
			Element lo_Node = ao_Node.getOwnerDocument().createElement("Line");
			lo_Line.Export(lo_Node, ai_Type);
			ao_Node.appendChild(lo_Node);
		}
	}

	/**
	 * 对象保存到数据库行对象中
	 * 
	 * @param ao_Set
	 *            保存的结果集
	 * @param ai_Type
	 *            保存方式：=0-正常保存；=1-短属性保存；
	 */
	@Override
	protected void Save(Map<String, Object> ao_Set, int ai_Type) {
		// 不需要保存-无需实现
	}

	/**
	 * 从数据库行对象中读取数据到对象
	 * 
	 * @param ao_Set
	 *            打开的结果集
	 * @param ai_Type
	 *            打开方式：=0-正常打开；=1-短属性打开；
	 */
	@Override
	public void Open(Map<String, Object> ao_Set, int ai_Type) {
		// 不需要打开-无需实现
	}

	/**
	 * 对象打包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导出；=1-短属性导出；
	 */
	@Override
	protected void Write(MBag ao_Bag, int ai_Type) {
		if (ai_Type == 1) {
			ao_Bag.Write("ID", this.TableID);
		} else {
			ao_Bag.Write("ml_TableID", this.TableID);
		}
		// #导出表格连线
		if (ai_Type == 1) {
			ao_Bag.Write("FLC", this.FormLines.size());
		} else {
			ao_Bag.Write("FormLinesCount", this.FormLines.size());
		}
		for (CFormLine lo_Line : this.FormLines) {
			lo_Line.Write(ao_Bag, ai_Type);
		}
	}

	/**
	 * 对象解包
	 * 
	 * @param ao_Bag
	 *            包对象
	 * @param ai_Type
	 *            类型：=0-正常导入；=1-短属性导入；
	 */
	@Override
	protected void Read(MBag ao_Bag, int ai_Type) {
		if (ai_Type == 1) {
			this.TableID = ao_Bag.ReadInt("ID");
		} else {
			this.TableID = ao_Bag.ReadInt("ml_TableID");
		}
		// #导入表格连线
		int li_Count = 0;
		if (ai_Type == 1) {
			li_Count = ao_Bag.ReadInt("FLC");
		} else {
			li_Count = ao_Bag.ReadInt("FormLinesCount");
		}
		int ll_Left = 0, ll_Top = 0, ll_Width = 0, ll_Height = 0;
		for (int i = 0; i < li_Count; i++) {
			CFormLine lo_Line = new CFormLine(this.Logon);
			lo_Line.FormTable = this;
			lo_Line.Read(ao_Bag, ai_Type);
			this.FormLines.add(lo_Line);
			if (i == 0) {
				ll_Left = lo_Line.StartX;
			}
			ll_Top = lo_Line.StartY;
			if (i == 3) {
				ll_Width = lo_Line.EndX - ll_Left;
			}
			ll_Height = lo_Line.EndY - ll_Top;
		}
		mo_Rectangle.moveRectangle(ll_Left, ll_Top, ll_Width, ll_Height);
	}

}
