<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>菜单编辑页面</title>
<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="saveComponent"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<div class="row" id="tab">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#tab1" data-toggle="tab">基本</a></li>
					<li class=""><a href="#tab4" data-toggle="tab">列</a></li>
					<li class=""><a href="#tab6" data-toggle="tab">过滤条件</a></li>
					<li class=""><a href="#tab5" data-toggle="tab">操作</a></li>
					<li class=""><a href="#tab2" data-toggle="tab">校验</a></li>
					<li class=""><a href="#tab7" data-toggle="tab">打印</a></li>
					<li class=""><a href="#tab3" data-toggle="tab">状态</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/component/dataGrid/basicInfo.jsp" %></div>
				<div class="tab-pane " id="tab4"><%@ include file="/formdesigner/page/component/dataGrid/columns.jsp" %></div>
				<div class="tab-pane " id="tab6"><%@ include file="/formdesigner/page/component/dataGrid/conndition.jsp" %></div>
				<div class="tab-pane " id="tab5"><%@ include file="/formdesigner/page/component/dataGrid/operate.jsp" %></div>
				<div class="tab-pane" id="tab2"><%@ include file="/formdesigner/page/component/common/validators.jsp" %></div>
				<div class="tab-pane" id="tab7"><%@ include file="/formdesigner/page/component/common/dataGridPdfConfi.jsp" %></div>
				<div class="tab-pane " id="tab3"><%@ include file="/formdesigner/page/component/common/hidden-disabled-readonly-defaultvalue-show.jsp" %></div>
			</div>
		</form>
	</div>

		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script src="<%=basePath%>/resources/plugins/echarts-2.2.1/src/component.js"></script>
		<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>
		<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
		
		
				//初始化页面列表
			//var basePath = "<%=basePath%>";
			var str = "<option value=''></option>";
			$.ajax({
			   type: "POST",
			   async:false,
			   url: basePath + "fd/listJson.do",
			   success: function(data){
			   		$("#relatePageId").empty();
					if(data != null){
						$.each(data, function(index, item) {
							str += "<option value='" + item.id+ "'>" + item.name + "</option>";
						});
						$("#alertPage").append(str);
					}else{
						alert("加载组件失败");
					}
			   }
			});	
		
			initializeDocument("${componentType}","${_ComponentTypeName[componentType]}");
			
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			if(dialog != null){
				dialog.height(1000); 
				dialog.width(900);
				//dialog.reset();     // 重置对话框位置
			}
			
			if(dialog != null){
				data = dialog.data;
			}
			
			var dataSource = null;
			if(data != null && data.dataSource)
				dataSource = data.dataSource;
			
			
			var index=0;
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/*
			$("#name").formValidator({onFocus:"请输入组件名称(至少1位,最多32位)",onShow:"请输入名称",onCorrect:"符合要求"})
				.inputValidator({min:1,max:32,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写名称"})
				.regexValidator({regExp:"[_a-zA-Z][_a-zA-Z0-9]*",dataType:"string",onError:"名称只能包含字母、数字、下划线(以字母或下划线开头)"})
				.ajaxValidator({
					dataType : "json", 
					//data : "dynamicPageId="+$("#dynamicPageId").val()+"&componentName="+$("#name").val()+"&componentId="+$("#pageId").val(),
					//data : {"dynamicPageId":$("#dynamicPageId").val(),"componentName" : $("#name").val(),"componentId":$("#pageId").val()},
					async : false, 
					url : "<%=basePath%>component/validateComponentNameInPage.do?dynamicPageId="+$("#dynamicPageId").val()+"&componentName="+$("#name").val()+"&componentId="+$("#pageId").val(),
					//buttons : $("#saveComponent"),
					success :  function(data){
						
						return data;
					},
					onError : "该名称不可用，请更换名称"
				});*//**/
			
			$("#order").formValidator({onShow:"请输入序号",onFocus:"请输入顺序",onCorrect:"合法"})
			.regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
			$("#layoutId").formValidator({onFocus:"请选择布局",onCorrect:"符合要求"})
				.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择布局"});
				
				
				
				
				$("#addColumn").click(function(){
					var str="<tr id=dataTr'"+index+"'>";
						
						str+="<td><input name='columns[][columnTitle]' id='columnTitle-"+index+"'" + "type=text" + "/>"+"</td>";
						str+="<td><input name='columns[][columnField]' id='columnField-"+index+"'" + "type=text" + "/>"+"</td>";
						str+="<td><input name='columns[][columnWidth]' id='columnWidth-"+index+"'" + "type=text" + "/>"+"</td>";
						str+="<td><input name='columns[][order]' id='columnWidth-"+index+"'" + "type=text" + "/>"+"</td>";
						str+="<td>"+"<input type='checkbox' name='columns[][sortable]' id='sortable-'"+ index +"' value='1'/></td>";
						
						
						
						
						str+="<td><a href='javascript:void(0)' class='removeTr'>删除</a>"+"</td>";
						str+="</tr>";
						index++;
						//alert(index);
					$("#columnsBody").append(str);
				});
				
				
				var connIndex=0;
				$("#addConndition").click(function(){
					var str="<tr id=connTr'"+connIndex+"'>";
						
						str+="<td><input name='connditions[][paramKey]' id='paramKey-"+connIndex+"'" + "type=text" + "/>"+"</td>";
						str+="<td>---》</td>";
						str+="<td><input name='connditions[][paramValue]' id='paramValue-"+connIndex+"'" + "type=text" + "/>"+"</td>";
						str+="<td>"+"<input type='checkbox' name='connditions[][isFinal]' id='isFinal-'"+ connIndex +"' value='1'/></td>";
						str+="<td><a href='javascript:void(0)' class='removeConnTr'>删除</a>"+"</td>";
						str+="</tr>";
						connIndex++;
						//alert(index);
					$("#connditionBody").append(str);
				});
				
				
			
			
			$("#alertPage").select2();
			
			var myIndex=0;
			$("#addParament").click(function(){
					var str="<tr id=paramTr'"+index+"'>";
						
						str+="<td>"+"<input type='text'   name='param[][exportParam]' id='exportParam-"+myIndex+"' value=''>"
										+	"<button class='btn btn-default exportParam' type='button' id='exportParam-"+myIndex+"' >选择</button>"
										
										+"</td>";
										
						
						str+="<td>————》</td>";
						
						str+="<td>"+"<input type='text'   name='param[][storeParam]' id='storeParam-"+myIndex+"' value=''>"
								   +"<input type='hidden'   name='param[][storeParamId]' id='storeParamId-"+myIndex+"' value=''>"
								   +"<button class='btn btn-default storeIdSelect' type='button' id='storeIdSelect-"+myIndex+"' >选择</button>"
										
								   +"</td>";
						
						
						
						
						str+="<td><a href='javascript:void(0)' class='removeParamTr'>删除</a>"+"</td>";
						str+="</tr>";
						myIndex++;
						
					$("#paramentBody").append(str);
			});
				
				
				
			$("body").on("click",".exportParam",function(){
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				if($("#dynamicPageId").val()){
					top.dialog({
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						title : '数据源选择',
						url : basePath + "formdesigner/page/component/common/dataSourceSelect.jsp",
						data : dataSource,
						onclose : function() {
							if (this.returnValue) {
									$("#exportParam-"+index).val(this.returnValue);
							}
						}
					}).show(document.getElementById("exportParam-"+index));
				} else {
					alert("请先保存表单");
				}
			});
			
			
			$("body").on("click",".storeIdSelect",function(){
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				if($("#alertPage").val()){
					top.dialog({
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						title : '数据源选择',
						url : basePath + "formdesigner/page/component/common/storeParamSelect.jsp",
						data : $("#alertPage").val(),
						onclose : function() {
							if (this.returnValue) {
									$("#storeParam-"+index).val(this.returnValue.storeCode);
									$("#storeParamId-"+index).val(this.returnValue.storeId);
							}
						}
					}).show(document.getElementById("storeParam-"+index));
				} else {
					alert("请先选择弹出页面");
				}
			});
				
				
		});
		
		
		$("body").on("click",".removeTr",function(){
				var _this = $(this);
				//alert(_this.parents("tr").html());
				_this.parents("tr").remove();
		});
		
		$("body").on("click",".removeParamTr",function(){
				var _this = $(this);
				//alert(_this.parents("tr").html());
				_this.parents("tr").remove();
		});
		$("body").on("click",".removeConnTr",function(){
				var _this = $(this);
				//alert(_this.parents("tr").html());
				_this.parents("tr").remove();
		});
		
		
		
	
	</script>
</body>
</html>