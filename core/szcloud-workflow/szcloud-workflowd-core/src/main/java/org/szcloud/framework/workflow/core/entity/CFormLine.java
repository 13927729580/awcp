package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.emun.EFormLineType;
import org.szcloud.framework.workflow.core.emun.ELineBorderStyle;
import org.szcloud.framework.workflow.core.emun.ELineDisplayType;
import org.szcloud.framework.workflow.core.module.MBag;
import org.w3c.dom.Element;

/**
 * 表单连线对象：是组成公文表单定义对象的元素对象。
 * 
 * @author Jackie.Wang
 * 
 */
public class CFormLine extends CBase {
	/**
	 * 初始化
	 */
	public CFormLine() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CFormLine(CLogon ao_Logon) {
		super(ao_Logon);
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的表单定义
	 */
	public CForm Form = null;

	/**
	 * 所属的表单表格
	 */
	public CFormTable FormTable = null;

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 连线标识
	 */
	public int LineID = -1;

	/**
	 * 连线类型
	 */
	public int LineType = EFormLineType.FormLineType;

	/**
	 * 连线位置 - 起点横坐标
	 */
	public int StartX = 0;

	/**
	 * 连线位置 - 起点纵坐标
	 */
	public int StartY = 0;

	/**
	 * 连线位置 - 终点坐标
	 */
	public int EndX = 0;

	/**
	 * 连线位置 - 终点纵坐标
	 */
	public int EndY = 0;

	/**
	 * 连线颜色
	 */
	public int BorderColor = 0x000000;

	/**
	 * 连线线形
	 */
	public int BorderStyle = ELineBorderStyle.SolidStyle;

	/**
	 * 设置连线线形
	 * 
	 * @param value
	 */
	public void setBorderStyle(int value) {
		if (value < ELineBorderStyle.TransparentStyle
				| value > ELineBorderStyle.Inside_SolidStyle) {
			return;
		}
		this.BorderStyle = value;
	}

	/**
	 * 读取连线表现类型
	 */
	public int getLineDisplayType() {
		if (this.LineType == EFormLineType.FormLineType) {
			return ELineDisplayType.CommondAnyLine;
		} else {
			if (this.LineID < 5) {
				return ELineDisplayType.TableBorder;
			} else {
				if (this.StartY == this.EndY) {
					return ELineDisplayType.HorizontalLine;
				} else {
					return ELineDisplayType.VerticalLine;
				}
			}
		}
	}

	/**
	 * 连线宽度
	 */
	public int BorderWidth = 1;

	/**
	 * 设置连线宽度
	 * 
	 * @param value
	 */
	public void setBorderWidth(int value) {
		if (value < 1 | value > 10) {
			return;
		}
		this.BorderWidth = value;
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		/**
		 * 所属的表单定义
		 */
		this.Form = null;

		/**
		 * 所属的表单表格
		 */
		this.FormTable = null;
		
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
		try {
			/**
			 * #导入基本信息
			 */
			this.LineID = Integer.parseInt(ao_Node.getAttribute("ID"));
			this.LineType = Integer.parseInt(ao_Node.getAttribute("Type"));
			this.BorderColor = Integer.parseInt(ao_Node
					.getAttribute("BorderColor"));
			this.BorderStyle = Integer.parseInt(ao_Node
					.getAttribute("BorderStyle"));
			this.BorderWidth = Integer.parseInt(ao_Node
					.getAttribute("BorderWidth"));
			/**
			 * #导入位置
			 */
			this.StartX = Integer.parseInt(ao_Node.getAttribute("StartX"));
			this.StartY = Integer.parseInt(ao_Node.getAttribute("StartY"));
			this.EndX = Integer.parseInt(ao_Node.getAttribute("EndX"));
			this.EndY = Integer.parseInt(ao_Node.getAttribute("EndY"));
		} catch (Exception e) {
			this.Raise(e, "Import", null);
		}
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
		// 导出基本信息
		ao_Node.setAttribute("ID", Integer.toString(this.LineID));
		ao_Node.setAttribute("Type", Integer.toString(this.LineType));
		ao_Node.setAttribute("BorderColor", Long.toString(this.BorderColor));
		ao_Node.setAttribute("BorderStyle", Integer.toString(this.BorderStyle));
		ao_Node.setAttribute("BorderWidth", Integer.toString(this.BorderWidth));
		// 导出位置
		ao_Node.setAttribute("StartX", Integer.toString(this.StartX));
		ao_Node.setAttribute("StartY", Integer.toString(this.StartY));
		ao_Node.setAttribute("EndX", Integer.toString(this.EndX));
		ao_Node.setAttribute("EndY", Integer.toString(this.EndY));
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
		// #导出基本信息
		ao_Bag.Write("ml_LineID", this.LineID);
		ao_Bag.Write("mi_LineType", this.LineType);
		ao_Bag.Write("ml_BorderColor", this.BorderColor);
		ao_Bag.Write("mi_BorderStyle", this.BorderStyle);
		ao_Bag.Write("mi_BorderWidth", this.BorderWidth);
		// #导出位置
		ao_Bag.Write("ml_StartX", this.StartX);
		ao_Bag.Write("ml_StartY", this.StartY);
		ao_Bag.Write("ml_EndX", this.EndX);
		ao_Bag.Write("ml_EndY", this.EndY);
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
			// #导入基本信息
			this.LineID = ao_Bag.ReadInt("ID");
			this.LineType = ao_Bag.ReadInt("LT");
			this.BorderColor = ao_Bag.ReadInt("BC");
			this.BorderStyle = ao_Bag.ReadInt("BS");
			this.BorderWidth = ao_Bag.ReadInt("BW");
			// #导入位置
			this.StartX = ao_Bag.ReadInt("SX");
			this.StartY = ao_Bag.ReadInt("SY");
			this.EndX = ao_Bag.ReadInt("EX");
			this.EndY = ao_Bag.ReadInt("EY");
		} else {
			// #导入基本信息
			this.LineID = ao_Bag.ReadInt("ml_LineID");
			this.LineType = ao_Bag.ReadInt("mi_LineType");
			this.BorderColor = ao_Bag.ReadInt("ml_BorderColor");
			this.BorderStyle = ao_Bag.ReadInt("mi_BorderStyle");
			this.BorderWidth = ao_Bag.ReadInt("mi_BorderWidth");
			// #导入位置
			this.StartX = ao_Bag.ReadInt("ml_StartX");
			this.StartY = ao_Bag.ReadInt("ml_StartY");
			this.EndX = ao_Bag.ReadInt("ml_EndX");
			this.EndY = ao_Bag.ReadInt("ml_EndY");
		}
	}
}
