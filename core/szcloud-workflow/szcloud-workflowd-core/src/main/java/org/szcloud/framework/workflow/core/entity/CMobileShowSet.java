package org.szcloud.framework.workflow.core.entity;

/**
 * 移动集成的显示设置
 * 
 * @author Jackie.Wang
 * 
 */
public class CMobileShowSet {

	/**
	 * 所属的步骤对象
	 */
	public CActivity Activity = null;

	/**
	 * 类型:=1-角色;=2-步骤;=3-表头属性;
	 */
	public int ShowType = 0;

	/**
	 * 对应类型的标识
	 */
	public String Id = "";

	/**
	 * =0-返回是否存在
	 */
	public boolean Exist = false;

	/**
	 * =1-返回显示名称
	 */
	public String ShowName = "";

	/**
	 * =2-返回排列顺序
	 */
	public int Order = 0;

	/**
	 * =3-返回操作类型:=0-只读;=1-可读写；=2-必读写；
	 */
	public int HandleType = 0;

	/**
	 * =4-返回显示格式
	 */
	public String ShowFormat = "";

	/**
	 * =5-返回高度
	 */
	public String Height = "";

	/**
	 * =6-返回位置宽度: =0-缺省（整行）; =1/2-占用50%宽度; =1/3-占用33.3%宽度; =2/3-占用66.7%宽度;
	 * =1/4-占用25%宽度; =2/4-占用50%宽度; =3/4-占用75%宽度;
	 */
	public String Width = "0";

	/**
	 * =7-返回是否换行
	 */
	public boolean IsLine = false;

	/**
	 * =8-返回显示类型: =0-单行文本框； =1-多行文本框； =2-下拉列表框； =3-单选框； =4-复选框； =5-显示内容（只读）
	 */
	public int ShowFlag = 0;

	/**
	 * =9-返回对应元素标识
	 */
	public int CellID = 0;

}
