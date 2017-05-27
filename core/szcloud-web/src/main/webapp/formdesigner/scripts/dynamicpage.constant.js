//var pageComponentMap = new Map();
//pageComponentMap.put("1001", "单行文本框");
//pageComponentMap.put("1002", "日期文本框");
//pageComponentMap.put("1003", "多选复选框");
//pageComponentMap.put("1004", "radio单选按钮");
//pageComponentMap.put("1005", "多行输入框");
//pageComponentMap.put("1006", "下拉选项框");
//pageComponentMap.put("1007", "密码框");
//pageComponentMap.put("1008", "列框");
//pageComponentMap.put("1009", "标签");
//pageComponentMap.put("1010", "隐藏框");
//pageComponentMap.put("1011", "文件上传框");
//pageComponentMap.put("1012", "包含组件框");
//pageComponentMap.put("1013", "搜索组件框");

var pageConstant = {
	type : "",
	getComponentTypes : function(){
		if(this.type == null || this.type == '') {
			this.type = new Map();
			this.type.put("1001", "单行文本框");
			this.type.put("1002", "日期文本框");
			this.type.put("1003", "多选复选框");
			this.type.put("1004", "radio单选按钮");
			this.type.put("1005", "多行输入框");
			this.type.put("1006", "下拉选项框");
//			this.type.put("1007", "密码框");
			this.type.put("1008", "列框");
			this.type.put("1009", "标签");
			this.type.put("1010", "隐藏框");
			this.type.put("1011", "文件上传框");
			this.type.put("1012", "包含组件框");
//			this.type.put("1013", "搜索组件框");
//			this.type.put("1014", "间隔文本");
//			this.type.put("1015", "tips提示");
			this.type.put("1016", "图片上传框");
			this.type.put("1017", "表格组件");
//			this.type.put("1018", "纯文本");


			this.type.put("1019", "富文本框");
//			this.type.put("1020", "用户选择框");
//			this.type.put("1021", "审批意见");
//			this.type.put("1022", "审批状态 ");
			this.type.put("1023", "word编辑");
			this.type.put("1024", "附件");

			this.type.put("1025", "标签页面");
			this.type.put("1026", "动态选择框");
			this.type.put("1027", "树形");
//			this.type.put("1028", "角色选择框");
//			this.type.put("1029", "单人选择框");
//			this.type.put("1030", "下级用户选择框");
//			this.type.put("1031", "sql用户选择框");
			this.type.put("1032", "数字文本输入框");
			this.type.put("1033", "级联下拉框");
			this.type.put("1034", "tab切换");
//            this.type.put("1035","地图");
            this.type.put("1036","搜索条件");
            this.type.put("1037","签名");
//            this.type.put("1038","grid表格");
		}
		return this.type;
	}
}