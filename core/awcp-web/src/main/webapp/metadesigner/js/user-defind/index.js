$(".dialog").each(function(i, item){
	$(item).tab();
});

var setting = {
	async:{
		autoParam:["projectId"],
		dataType:"json",
		enable:true,
		url : "../../metaModelClass/queryByProjectId.do",
		dataFilter : function(treeId, parentNode, responseData){
			$.each(responseData, function(i, chart){
				chart.projectId = parentNode.id;
				chart.isParent = false;
				chart.className=chart.className;
				chart.type = "chart";
				chart.icon = "../images/uml/uml_chart.png";
			});
			return responseData;
		}
	},
	callback : {
		beforeAsync : function(treeId, treeNode){
			if(treeNode && (treeNode.type == "project")){
				return true;
			} else {
				return false;
			}
		},
		onClick : function(event, treeId, treeData){
			if(treeData.type == "project"){
				$("#add_chart").show();
			} else {
				$("#add_chart").hide();
				if(treeData.type == "chart" && !mainTab.isExistTab(treeData.id)){
					mainTab.addTab({
						title : treeData.name,
						afterAdd : function(tab, panel){
							tab.addClass("tab_"+treeData.id);
							$.get('template.html').done(function(data) {
								alert(data);
								panel.html(data);
								panel.find('#canvas').umlCanvas(treeData);
								/*将project和chart缓存在工具栏上，以待后续操作用到*/
								panel.find(".tools_bar:first").data("project", treeData.getParentNode());
								panel.find(".tools_bar:first").data("chart", treeData);
							});
						},
						closeable : true
					});
				}
			}
		}
	}
};


/*常用的三个变量*/
var projectTree = $.fn.zTree.init($("#projectTree"), setting, []); //工程树    最顶层对象
var mainTab = $(".main_panel").tab(); 	//右边tab
var dialog = $().dialog();		  		//页面对话框

$.ajax({
	url:"../../projectManage/findAll.do",
	data:"",
	type:"get",
	dataType:"json",
	success:function(data){
		$.each(data, function(i, d){
			alert(d.sysId);
			d.isParent = true;
			d.projectId = d.sysId;
			d.name=d.name;
			d.sysId = d.sysId;
			d.name=d.sysName;
			d.type = "project";
		});
		var obj=eval(data);
		projectTree.addNodes(null, obj);
	},
	
	
	error : function(){
		//alert("获取工程出错");
	}
});


//新建工程		
/*创建工程对话框*/
$("#add_project").click(function(){
	var form = $('<form class="dialog_form">\
					<div><label>项目名称(中)</label>&nbsp;&nbsp;<input type="text" id="name" oninput="var name=$(this);if(name.val()) {name.removeClass(\'not_null\')} else {name.addClass(\'not_null\')}"/></div>\
					<div><label>项目名称(英)</label>&nbsp;&nbsp;<input type="text" id="code" oninput="var code=$(this);if(code.val()){code.remove.Class(\'not_null\')}else{code.addClass(\'not_null\')}"/></div>\
					<div class="dialog_buttons">\
						<button type="button">创建</button>\
						<button type="button" onclick="dialog.close();">取消</button>\
					</div>\
				 </form>');
	form.find("button:first").click(function(){
		var btn = $(this);
		var input = btn.parent().prev();
		if(!(input.val()==null)) {
			$.post('../../projectManage/save.do',{"projectName":$("#name").val(),"projectCode":$("#name").val()}, function(data){/*, {"modelName":name.val(),"modelCode":code.val()}*/
				if(data!=null) {
					projectTree.addNodes(null, [{name:$("#code").val(), id:data, isParent:true, type:"project", projectId:data}]);
					dialog.close();
				} else {
					btn.html("创建失败");
					setTimeout(function(){btn.html('创建');}, 500);
				}
			});
			return;
		}
		input.addClass("not_null");
	});
	dialog.show().setTitle("创建元数据模型分类").setBody(form);
});

/*添加模型*/
/**
 * 添加图
 */
$("#add_chart").click(function(){
	var project = projectTree.getSelectedNodes()[0];
	var form = $('<form class="dialog_form">\
					<div><label>模型分类(中)</label>&nbsp;&nbsp;<input type="text" id="className" oninput="var input=$(this);if(input.val()) {input.removeClass(\'not_null\')} else {input.addClass(\'not_null\')}"/></div>\
					<div><label>模型分类(英)</label>&nbsp;&nbsp;<input type="text" id="classCode" oninput="var input=$(this);if(input.val()){input.removeClass(\'not_null\')}else{input.addClass(\'not_null\')}"/></div>\
					<div><label>分类名称(中)</label>&nbsp;&nbsp;<input type="text" id="name" oninput="var input=$(this);if(input.val()) {input.removeClass(\'not_null\')} else {input.addClass(\'not_null\')}"/></div>\
					<div><label>分类名称(英)</label>&nbsp;&nbsp;<input type="text" id="classCode" oninput="var input=$(this);if(input.val()){input.removeClass(\'not_null\')}else{input.addClass(\'not_null\')}"/></div>\
					<div><label>模型名称(中)</label>&nbsp;&nbsp;<input type="text" id="modelName" oninput="var input=$(this);if(input.val()) {input.removeClass(\'not_null\')} else {input.addClass(\'not_null\')}"/></div>\
					<div><label>模型名称(英)</label>&nbsp;&nbsp;<input type="text" id="modelCode" oninput="var input=$(this);if(input.val()){input.removeClass(\'not_null\')}else{input.addClass(\'not_null\')}"/></div>\
					<div><label>模型描述&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;<input type="text" id="modelDesc" oninput="var input=${this};if(input.val()){input.removeClass(\'not_null\')}else{input.addClass(\'not_null\')}"/></div>\
					<div><label>所属表名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;<input type="text" id="tableName" oninput="var input=${this};if(input.val()){input.removeClass(\'not_null\')}else{input.addClass(\'not_null\')}"/></div>\
					<div><label>项目名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;<input type="text" id="projectName" oninput="var input=${this};if(input.val()){input.removeClass(\'not_null\')}else{input.addClass(\'not_null\')}"/></div>\
					<div class="dialog_buttons">\
						<button type="button">创建</button>\
						<button type="button" onclick="dialog.close();">取消</button>\
					</div>\
				<form>');
	
	form.find("button:first").click(function(){
		var btn = $(this),
			input = btn.parent().prev(),
			project = projectTree.getSelectedNodes()[0];
			console.log(project);
		if(input.val()!=null) {
			var c=project.projectId;
			alert(c);
			$.ajax({
			    type:"post",
				url: "../../metaModelClass/save.do",
				dataType:"json",
				data:{"name" :$("#className").val(),"classCode":$("#classCode").val(),"sysId":c},
				data:{"chartName":$("#classCode").val(),"classId":dialog.projectId },
				success:function(data){
					if(data!=null){
						projectTree.addNodes(projectTree.getSelectedNodes()[0], [{name:$("#className").val(), id: data, isParent:false, type:"chart", projectId:c,icon:"../images/uml/uml_chart.png"}]);
						projectTree.addNodes(projectTree.getSelectedNodes()[0], [{name:$("#className").val(), id: data, isParent:false, type:"chart", projectId:dialog.projectId,icon:"../images/uml/uml_chart.png"}]);
							/**if(project.isAjaxing){}*/
						dialog.close();
					}else{
						btn.html("创建失败");
						setTimeout(function(){btn.html('创建');}, 500);
					}
				},
				error:function(){
					alert("添加失败");
				}
			});
			return;
		}
		input.addClass("not_null");
	});
	
	dialog.projectId = project.id;
	console.log(project);
	dialog.show().setTitle("添加UML图").setBody(form);
});
	
/**
 * 
 * 点击保存代码时触发
 * 生成代码。生成代码的按钮所在的工具栏已经缓存了当前的画布对象了
 */
mainTab.panels.delegate(".generateCode", "click", function(){
	var form = $('<form class="dialog_form">\
					<label>代码文件包名</label><input type="text" oninput="var input=$(this);if(input.val()) {input.removeClass(\'not_null\')} else {input.addClass(\'not_null\')}"/>\
					<div class="dialog_buttons">\
						<button type="button">确定</button>\
						<button type="button" onclick="dialog.close();">取消</button>\
					</div>\
				</form>');
				
	form.find("button:first").click(function(){
		var input = $(this).parent().prev();
		if(input.val()){
			var canvas=dialog.canvas,
				project=dialog.project,
				chart=dialog.chart;
			var domainsChart = {};
			domainsChart.id=chart.id;
			domainsChart.name=chart.name;
			domainsChart.project={};
			domainsChart.project.id=project.id;
			domainsChart.project.name=project.name;
			domainsChart.domainShapeDtos=canvas.getModels();
			domainsChart.domainShapeInfo="";
			domainsChart.lineInfo="";
			console.log(JSON.stringify(domainsChart));
			$.ajax({
				headers: { 
			        'Content-Type': 'application/json' 
			    },
				url:"domains-chart/gencode?packageName=" + input.val(),
				data:JSON.stringify(domainsChart),
				type:"post",
				success:function(data){
					window.location.assign(data + "/" + domainsChart.name + ".zip");
				},
				error : function(){
				}
			});
		}
	});
	var toolBar = $(this).parents(".tools_bar:first");
	dialog.canvas = toolBar.data("canvas");
	dialog.project = toolBar.data("project");
	dialog.chart = toolBar.data("chart");
	dialog.show().setTitle("生成代码").setBody(form);
});


/**
 * 保存代码
 */
mainTab.panels.delegate(".saveUml", "click", function(){
	var toolBar = $(this).parents(".tools_bar:first"),
		canvas = toolBar.data("canvas"),
		metaModelChartVO = {},
		chart = toolBar.data("chart");
		project = toolBar.data("project");
	metaModelChartVO.id=chart.id;
	metaModelChartVO.name=chart.name;
	metaModelChartVO.project={};
	metaModelChartVO.project.name=project.name;
	metaModelChartVO.lineInfo=canvas.getLineString(),
	metaModelChartVO.domainShapeInfo=canvas.getModelString();
	
	domainsChart.id=chart.id;
	domainsChart.name			= chart.name;
	domainsChart.project		= {};
	domainsChart.project.id		= project.id;
	domainsChart.project.name	= project.name;
	domainsChart.lineInfo		= canvas.getLineString(),
	domainsChart.domainShapeInfo = canvas.getModelString();
	
	$.ajax({
		headers: { 
	        'Accept': 'application/json',
	        'Content-Type': 'application/json' 
	    },
		url:"domains-chart/save",
		data:JSON.stringify(domainsChart),
		type:"post",
		dataType:"json",
		success:function(data){
			console.log(data);
		},
		error :function(){
			
		}
	});
});


$("#addModel").click(function(){
	$.ajax({
		type:"get",
		url:"../../metaModel/saves.do",
		data:{"modelName":$("#modelName").val(),"modelCode":$("#modelCode").val(),"tableName":$("#tableName").val(),"modelType":$("#modelType").val(),"modelDesc":$("#modelDesc").val(),"id":$("#id").val()},
		datatype:"json",
		success:function(date){
			if(date.id!=0){
				alert("fdas");
				$("#id").attr("value",date.id);
				$("#modelId").attr("value",date.id);
				alert("保存成功");
			}else{
				alert("您输入的元数据模型已经存在，请重新输入");
			}
		}
	});
});

$("#addItem").click(function(){
	$.ajax({
		type:"post",
		url:"../../metaModelItems/saves.do",
		data:{"itemName":$("#itemName").val(),"itemCode":$("#itemCode").val(),"itemType":$("#itemType").val(),"itemLength":$("#itemLength").val(),"remark":$("#remark").val(),"modelId":$("#id").val()},
		datatype:"json",
		success:function(date){
			if(date.id!=0){
				alert("添加成功");
			}else{
				alert("您输入的属性已经存在，请重新输入");
			}
		}
	});
});


