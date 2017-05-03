function pageInit(jqgridID, URL, listName, colModel, multiselect, jsonReader, subGrid) {
	//创建jqGrid组件
	jQuery("#" + jqgridID).jqGrid({
		//		url: 'data/JSONData.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		height: '100%',
		autowidth: true,
		autoScroll: false,
		//caption: "表格名称", //表格的标题名字
		colNames: listName, //jqGrid的列显示名字
		colModel: colModel,
		rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#pager2', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "desc", //排序方式,可选desc,asc
		multiselect: multiselect, //多选
		mtype: "post", //向后台请求数据的ajax的类型。可选post,get
		viewrecords: false,
		jsonReader: jsonReader,
		editurl: URL.editURL,
		//		多级表格
		subGrid: subGrid.subGrid,
		subGridUrl: subGrid.subGridUrl,
		subGridModel: subGrid.subGridModel,
		caption: "Subgrid with JSON Data",
		//树
		 loadonce : true,
		treeGrid: true,
		ExpandColumn: 'name',
		caption: "Treegrid example"
	});
	/*创建jqGrid的操作按钮容器*/
	/*可以控制界面上增删改查的按钮是否显示*/
	jQuery("#" + jqgridID).jqGrid('navGrid', '#pager2', { edit: true, add: true, del: true });
	jQuery("#" + jqgridID).getGridParam('userData')
	
	//
	$.ajax({
		type: "get",
		url: URL.getDataURL,
		async: true,
		success: function(res) {
			data = res;
			var mygrid = jQuery("#" + jqgridID)[0];
			var myjsongrid = eval("data");
			mygrid.addJSONData(myjsongrid);
			myjsongrid = null;
			jsonresponse = null;
		}
	});
	
	var jqGridFN = {};
	//获取多选的ID 
	jqGridFN.getIds = function() {
		var s;
		s = jQuery("#" + jqgridID).jqGrid('getGridParam', 'selarrrow');
		return s;
	}
	jqGridFN.edit = function() {
		$('#edit_' + jqgridID).trigger('click');
	}
	jqGridFN.add = function() {
		$('#add_' + jqgridID).trigger('click');
	}
	jqGridFN.del = function() {
		$('#del_' + jqgridID).trigger('click');
	}
	jqGridFN.search = function() {
		$('#search_' + jqgridID).trigger('click');
	}
	jqGridFN.refresh = function() {
		$('#refresh_' + jqgridID).trigger('click');
	}

	return jqGridFN;
}