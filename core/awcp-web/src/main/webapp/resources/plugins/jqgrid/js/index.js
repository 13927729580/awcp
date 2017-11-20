function pageInit( URL, multiselect, jsonReader, subGrid,treeGrid) {
	var jqgridID = 'jqgrid'; //表格ID
	var listName = $("#thHead").val().split("@"); //列名
	var tdName = $("#field").val().split("@"); //字段名
	var colModel = [] //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式,editable是否可以编辑.....
	var len = tdName.length;
	for(var i=0;i<len;i++) {
		if(tdName[i]){
			var td = tdName[i].split(",");
			colModel.push(
				{'name':td[0],editable:td[1]=='true'?true:false,edittype:td[2],sortable:false}
			)
		}
		
	}
	//创建jqGrid组件
	jQuery("#" + jqgridID).jqGrid({
		//		url: 'data/JSONData.json', //组件创建完成之后请求数据的url
		datatype: "json", //请求数据返回的类型。可选json,xml,txt
		height: '100%',
		autowidth: true,
		//caption: "表格名称", //表格的标题名字
		colNames: listName, //jqGrid的列显示名字
		colModel: colModel,
		rowNum: 10, //一页显示多少条
		rowList: [10, 20, 30], //可供用户选择一页显示多少条
		pager: '#pager2', //表格页脚的占位符(一般是div)的id
		sortname: 'id', //初始化的时候排序的字段
		sortorder: "asc", //排序方式,可选desc,asc
		multiselect: multiselect, //多选
		mtype: "post", //向后台请求数据的ajax的类型。可选post,get
		viewrecords: false,
		jsonReader: jsonReader,
		editurl: URL.editURL,
//		autoScroll: false,
		//		多级表格
		subGrid: subGrid.subGrid,
		//		caption: "Grid as Subgrid",
		subGridRowExpanded: function(subgrid_id, row_id) {
			var subgrid_table_id, pager_id; //子树ID
			subgrid_table_id = subgrid_id + "_t";
			//pager_id = "p_" + subgrid_table_id;
			$("#" + subgrid_id).html(
				"<table id='" + subgrid_table_id +
				"' class='scroll'></table><div id='" +
				pager_id + "' class='scroll'></div>");
			var lastsel;
			jQuery("#" + subgrid_table_id).jqGrid({
				autowidth: true,
				url: subGrid.subGridUrl,
				datatype: "json",
				colNames: subGrid.colNames,
				colModel: subGrid.colModel,
				//		                rowNum : 20,
				pager: pager_id,
				height: '100%',
				jsonReader:{ //后台返回数据格式 
						root: "data",
						repeatitems: true,				
						userdata: "userdata",
					}
				,
				onSelectRow: function(id) {
					if(id && id !== lastsel) {
						jQuery("#" + subgrid_table_id).jqGrid('restoreRow', lastsel);
						jQuery("#" + subgrid_table_id).jqGrid('editRow', id, true);
						lastsel = id;
					}
				},
				editurl: "RowEditing",
			});

		},
		//树
		//loadonce : true,
		treeGrid: treeGrid.treeGrid,
		subGridOptions: treeGrid.subGridOptions
		//		ExpandColumn: 'name',
		//		caption: "Treegrid example"
	});
	/*创建jqGrid的操作按钮容器*/
	/*可以控制界面上增删改查的按钮是否显示*/
	jQuery("#" + jqgridID).jqGrid('navGrid', '#pager2', { edit: true, add: true, del: true });
	jQuery("#" + jqgridID).getGridParam('userData')

	//
	$.ajax({
		type: "post",
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