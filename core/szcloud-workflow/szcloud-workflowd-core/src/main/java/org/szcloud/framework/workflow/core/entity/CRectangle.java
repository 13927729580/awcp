package org.szcloud.framework.workflow.core.entity;

import java.util.Map;

import org.szcloud.framework.workflow.core.base.CBase;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.module.MBag;
import org.w3c.dom.Element;

/**
 * 长方形对象
 * 
 * @author Jackie.Wang
 * 
 */
public class CRectangle extends CBase {

	/**
	 * 初始化
	 */
	public CRectangle() {
		super();
	}

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public CRectangle(CLogon ao_Logon) {
		super(ao_Logon);
	}

	/**
	 * 清除释放对象的内存数据
	 * @throws Exception 
	 */
	public void ClearUp() throws Exception {
		super.ClearUp();
	}

	// #==========================================================================#
	// 变量定义
	// #==========================================================================#
	/**
	 * 左边距
	 */
	public int Left = 0;

	/**
	 * 上边距
	 */
	public int Top = 0;

	/**
	 * 右边距
	 */
	public int Right = 0;

	/**
	 * 底边距
	 */
	public int Bottom = 0;

	/**
	 * 宽度
	 */
	public int Width = 0;

	/**
	 * 高度
	 */
	public int Height = 0;

	/**
	 * 读取左边距
	 */
	public int getRectLeft() {
		return this.Left;
	}

	/**
	 * 设置左边距
	 */
	public void setRectLeft(int value) {
		if (value < this.Right) {
			this.Left = value;
		} else {
			this.Left = this.Right;
			this.Right = value;
		}
		this.Width = this.Right - this.Left;

		// if (RisizeEvent != null)
		// RisizeEvent();
	}

	/**
	 * 读取上边距
	 */
	public int getRectTop() {
		return this.Top;
	}

	/**
	 * 设置上边距
	 */
	public void setRectTop(int value) {
		if (value < this.Bottom) {
			this.Top = value;
		} else {
			this.Top = this.Bottom;
			this.Bottom = value;
		}
		this.Height = this.Bottom - this.Top;

		// if (RisizeEvent != null)
		// RisizeEvent();
	}

	/**
	 * 读取右边距
	 */
	public int getRectRight() {
		return this.Right;
	}

	/**
	 * 设置右边距
	 */
	public void setRectRight(int value) {
		if (value > this.Left) {
			this.Right = value;
		} else {
			this.Right = this.Left;
			this.Left = value;
		}
		this.Width = this.Right - this.Left;

		// if (RisizeEvent != null)
		// RisizeEvent();
	}

	/**
	 * 读取底边距
	 */
	public int getRectBottom() {
		return this.Bottom;
	}

	/**
	 * 设置底边距
	 */
	public void setRectBottom(int value) {
		if (value > this.Top) {
			this.Bottom = value;
		} else {
			this.Bottom = this.Top;
			this.Top = value;
		}
		this.Height = this.Bottom - this.Top;

		// if (RisizeEvent != null)
		// RisizeEvent();
	}

	/**
	 * 读取宽度
	 */
	public int getRectWidth() {
		return this.Width;
	}

	/**
	 * 设置宽度
	 */
	public void setRectWidth(int value) {
		this.Width = value > 0 ? value : 0;
		this.Right = this.Left + this.Width;
		getRectWidth();
		// if (RisizeEvent != null)
		// RisizeEvent();
	}

	/**
	 * 读取高度
	 */
	public int getRectHeight() {
		return this.Height;
	}

	/**
	 * 设置高度
	 */
	public void setRectHeight(int value) {
		this.Height = value > 0 ? value : 0;
		this.Bottom = this.Top + this.Height;

		// if (RisizeEvent != null)
		// RisizeEvent();
	}

	/**
	 * 移动长方形
	 * 
	 * @param al_Left
	 *            左边距
	 * @param al_Top
	 *            上边距
	 * @param al_Width
	 *            宽度
	 * @param al_Height
	 *            高度
	 */
	public void moveRectangle(int al_Left, int al_Top, int al_Width,
			int al_Height) {
		int ll_Left = al_Left < 0 ? 0 : al_Left;
		int ll_Top = al_Top < 0 ? 0 : al_Top;

		if (al_Width >= 0) {
			this.Right = ll_Left + al_Width;
			this.Width = al_Width;
		} else {
			this.Right = this.Right + ll_Left - this.Left;
		}
		this.Left = ll_Left;

		if (al_Height >= 0) {
			this.Bottom = ll_Top + al_Height;
			this.Height = al_Height;
		} else {
			this.Bottom = this.Bottom + ll_Top - this.Top;
		}
		this.Top = ll_Top;

		// if (RisizeEvent != null)
		// RisizeEvent();
	}

	// #==========================================================================#
	// 事件定义
	// #==========================================================================#
	/**
	 * 尺寸变化事件
	 */
	// public delegate void RisizeEventHandler();
	// private RisizeEventHandler RisizeEvent;
	//
	// public event RisizeEventHandler Risize
	// {
	// add
	// {
	// RisizeEvent = (RisizeEventHandler) System.Delegate.Combine(RisizeEvent,
	// value);
	// }
	// remove
	// {
	// RisizeEvent = (RisizeEventHandler) System.Delegate.Remove(RisizeEvent,
	// value);
	// }
	// }

	/**
	 * 当前对象是否可用
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
	 */
	@Override
	protected void Import(Element ao_Node, int ai_Type) {
		if (ai_Type == 1 && ao_Node.hasAttribute("L")) {
			this.Left = Integer.parseInt(ao_Node.getAttribute("L"));
			this.Top = Integer.parseInt(ao_Node.getAttribute("T"));
			this.Width = Integer.parseInt(ao_Node.getAttribute("W"));
			this.Height = Integer.parseInt(ao_Node.getAttribute("H"));
		} else {
			this.Left = Integer.parseInt(ao_Node.getAttribute("Left"));
			this.Top = Integer.parseInt(ao_Node.getAttribute("Top"));
			this.Width = Integer.parseInt(ao_Node.getAttribute("Width"));
			this.Height = Integer.parseInt(ao_Node.getAttribute("Height"));
		}

		this.Right = this.Left + this.Width;
		this.Bottom = this.Top + this.Height;
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
		ao_Node.setAttribute("Left", Integer.toString(this.Left));
		ao_Node.setAttribute("Top", Integer.toString(this.Top));
		ao_Node.setAttribute("Width", Integer.toString(this.Width));
		ao_Node.setAttribute("Height", Integer.toString(this.Height));
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
			ao_Bag.Write("L", this.Left);
			ao_Bag.Write("M", this.Top);
			ao_Bag.Write("W", this.Width);
			ao_Bag.Write("H", this.Height);
		} else {
			ao_Bag.Write("ml_Left", this.Left);
			ao_Bag.Write("ml_Top", this.Top);
			ao_Bag.Write("ml_Width", this.Width);
			ao_Bag.Write("ml_Height", this.Height);
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
			this.Left = ao_Bag.ReadInt("L");
			this.Top = ao_Bag.ReadInt("T");
			this.Width = ao_Bag.ReadInt("W");
			this.Height = ao_Bag.ReadInt("H");
		} else {
			this.Left = ao_Bag.ReadInt("ml_Left");
			this.Top = ao_Bag.ReadInt("ml_Top");
			this.Width = ao_Bag.ReadInt("ml_Width");
			this.Height = ao_Bag.ReadInt("ml_Height");
		}
		this.Right = this.Left + this.Width;
		this.Bottom = this.Top + this.Height;
	}

}
