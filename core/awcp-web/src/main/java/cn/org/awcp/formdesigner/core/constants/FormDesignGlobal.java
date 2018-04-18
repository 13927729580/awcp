package cn.org.awcp.formdesigner.core.constants;

import java.util.HashMap;
import java.util.Map;

public class FormDesignGlobal {
	public static final String PAGE_ACT_CODE_PREFIX = "";
	public static final String DOCUMENT_VIEW = "document/view.do?id=&dynamicPageId=";

	public static final Map<String, String> COMPOENT_TYPE_NAME = new HashMap<String, String>();

	public static final Map<String, String> COMPOENT_TYPE_URL = new HashMap<String, String>();
	public static final Map<String, String> LAYOUT_TYPE_SHOW = new HashMap<String, String>();
	static {
		COMPOENT_TYPE_NAME.put("1001", "单行文本框");// 计算值、隐藏、只读、禁用
		COMPOENT_TYPE_NAME.put("1002", "日期文本框");// 计算值、隐藏、只读、禁用
		COMPOENT_TYPE_NAME.put("1003", "多选复选框");// 计算值、隐藏、只读、禁用、选项
		COMPOENT_TYPE_NAME.put("1004", "radio单选按钮");// 计算值、隐藏、只读、禁用、选项
		COMPOENT_TYPE_NAME.put("1005", "多行输入框");// 计算值、隐藏、只读、禁用
		COMPOENT_TYPE_NAME.put("1006", "下拉选项框");// 计算值、隐藏、只读、禁用、选项
		// COMPOENT_TYPE_NAME.put("1007", "密码框");// 计算值、隐藏、只读、禁用
		COMPOENT_TYPE_NAME.put("1008", "列框");// 计算值、隐藏
		COMPOENT_TYPE_NAME.put("1009", "标签");// 计算值、隐藏
		COMPOENT_TYPE_NAME.put("1010", "隐藏框");// 计算值、禁用
		COMPOENT_TYPE_NAME.put("1011", "文件上传框");// 计算值、隐藏、只读、禁用
		COMPOENT_TYPE_NAME.put("1012", "包含组件框");// 计算值、隐藏、只读、禁用
		// COMPOENT_TYPE_NAME.put("1013", "搜索组件框");// 计算值、隐藏、只读、禁用
		// COMPOENT_TYPE_NAME.put("1014", "间隔文本");// 计算值、隐藏
		// COMPOENT_TYPE_NAME.put("1015", "tips提示");// 计算值、隐藏
		COMPOENT_TYPE_NAME.put("1016", "图片上传框");// 计算值、隐藏、只读、禁用
		COMPOENT_TYPE_NAME.put("1017", "表格组件");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1018", "纯文本");// 计算值、隐藏、禁用

		COMPOENT_TYPE_NAME.put("1019", "富文本框");// 计算值、隐藏、禁用
		COMPOENT_TYPE_NAME.put("1020", "用户选择框");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1021", "审批意见");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1022", "审批状态");// 计算值、隐藏、禁用

		// COMPOENT_TYPE_NAME.put("1023", "word编辑");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1024", "附件");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1026", "动态选择框");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1025", "标签页面");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1027", "树形");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1028", "角色选择框");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1032", "数字文本输入框");// 计算值、隐藏、禁用
		COMPOENT_TYPE_NAME.put("1033", "级联下拉框");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1034", "tab切换");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1035", "地图");// 计算值、隐藏、禁用
		COMPOENT_TYPE_NAME.put("1036", "搜索条件");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1037", "签名");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1038", "grid表格");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1039", "easyUI基础-树表格");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1040", "easyUI行编辑表格");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1041", "easyUI嵌套表格");// 计算值、隐藏、禁用
		// COMPOENT_TYPE_NAME.put("1042", "表格組件-easyUI");// 计算值、隐藏、禁用
		COMPOENT_TYPE_NAME.put("1043", "表格行操作组件");
		
		/*
		 * 钉钉控件
		 */
		COMPOENT_TYPE_NAME.put("1101", "事件控件");
		COMPOENT_TYPE_NAME.put("1102", "手机省市县控件");
		COMPOENT_TYPE_NAME.put("1103", "钉钉明细控件");

		COMPOENT_TYPE_URL.put("1001", "formdesigner/page/component/input/input.jsp");
		COMPOENT_TYPE_URL.put("1002", "formdesigner/page/component/datetime/datetime.jsp");
		COMPOENT_TYPE_URL.put("1003", "formdesigner/page/component/checkbox/checkbox.jsp");
		COMPOENT_TYPE_URL.put("1004", "formdesigner/page/component/radio/radio.jsp");
		COMPOENT_TYPE_URL.put("1005", "formdesigner/page/component/textarea/textarea.jsp");
		COMPOENT_TYPE_URL.put("1006", "formdesigner/page/component/select/select.jsp");
		// COMPOENT_TYPE_URL.put("1007","formdesigner/page/component/password/password.jsp");
		COMPOENT_TYPE_URL.put("1008", "formdesigner/page/component/column/column.jsp");
		COMPOENT_TYPE_URL.put("1009", "formdesigner/page/component/label/label.jsp");
		COMPOENT_TYPE_URL.put("1010", "formdesigner/page/component/hidden/hidden.jsp");
		COMPOENT_TYPE_URL.put("1011", "formdesigner/page/component/file/file.jsp");
		COMPOENT_TYPE_URL.put("1012", "formdesigner/page/component/containPage/containPage.jsp");
		// COMPOENT_TYPE_URL.put("1013","formdesigner/page/component/searchComponent/searchComponent.jsp");
		// COMPOENT_TYPE_URL.put("1014","formdesigner/page/component/span/span.jsp");
		// COMPOENT_TYPE_URL.put("1015","formdesigner/page/component/tips/tips.jsp");
		COMPOENT_TYPE_URL.put("1016", "formdesigner/page/component/image/image.jsp");
		COMPOENT_TYPE_URL.put("1017", "formdesigner/page/component/dataGrid/dataGrid.jsp");
		// COMPOENT_TYPE_URL.put("1018","formdesigner/page/component/pureText/pureText.jsp");

		COMPOENT_TYPE_URL.put("1019", "formdesigner/page/component/kindEditor/kindEditor.jsp");
		COMPOENT_TYPE_URL.put("1020", "formdesigner/page/component/userSelect/userSelect.jsp");
		// COMPOENT_TYPE_URL.put("1021",
		// "formdesigner/page/component/approvalStatus/approvalStatus.jsp");
		// COMPOENT_TYPE_URL.put("1022",
		// "formdesigner/page/component/approvalViews/approvalViews.jsp");
		// COMPOENT_TYPE_URL.put("1023",
		// "formdesigner/page/component/mainText/mainText.jsp");
		// COMPOENT_TYPE_URL.put("1024",
		// "formdesigner/page/component/appendix/appendix.jsp");
		// COMPOENT_TYPE_URL.put("1026","formdesigner/page/component/dynamicSelect/dynamicSelect.jsp");
		// COMPOENT_TYPE_URL.put("1025","formdesigner/page/component/tabs/tabs.jsp");
		// COMPOENT_TYPE_URL.put("1027","formdesigner/page/component/zTree/zTree.jsp");
		// COMPOENT_TYPE_URL.put("1028","formdesigner/page/component/roleSelect/roleSelect.jsp");
		// COMPOENT_TYPE_URL.put("1032", "formdesigner/page/component/input/input.jsp");
		COMPOENT_TYPE_URL.put("1033", "formdesigner/page/component/multilevelLinkage/multilevelLinkage.jsp");
		// COMPOENT_TYPE_URL.put("1034", "formdesigner/page/component/tab/tab.jsp");
		// COMPOENT_TYPE_URL.put("1035","formdesigner/page/component/aMap/amap.jsp");
		COMPOENT_TYPE_URL.put("1036", "formdesigner/page/component/searchComponent/addSearch.jsp");
		// COMPOENT_TYPE_URL.put("1037", "formdesigner/page/component/image/image.jsp");
		// COMPOENT_TYPE_URL.put("1038","formdesigner/page/component/gridTable/table.jsp");
		// COMPOENT_TYPE_URL.put("1039", "formdesigner/page/component/easyui/grid.jsp");
		// COMPOENT_TYPE_URL.put("1040",
		// "formdesigner/page/component/easyui/rowediting.jsp");
		// COMPOENT_TYPE_URL.put("1041",
		// "formdesigner/page/component/easyui/nestgrid.jsp");
		// COMPOENT_TYPE_URL.put("1042",
		// "formdesigner/page/component/easyui/dataGrid.jsp");
		COMPOENT_TYPE_URL.put("1043", "formdesigner/page/component/rowOperation/rowOperation.jsp");
		
		/*
		 * 钉钉组件
		 */
		COMPOENT_TYPE_URL.put("1101", "formdesigner/page/component/function/function.jsp");
		COMPOENT_TYPE_URL.put("1102", "formdesigner/page/component/address/address.jsp");
		COMPOENT_TYPE_URL.put("1103", "formdesigner/page/component/ddDetail/ddDetail.jsp");
		LAYOUT_TYPE_SHOW.put("1", "列");
		LAYOUT_TYPE_SHOW.put("2", "行");
	}

}
