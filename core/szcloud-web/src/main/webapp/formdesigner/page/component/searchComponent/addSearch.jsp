
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
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css"/>
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
					<li class=""><a href="#tab3" data-toggle="tab">状态</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1">
					<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
					<input type="hidden" name="pageId" id="pageId"/>
					<input type="hidden" name="componentType" id="componentType">
					
					<div class="form-group">
						<label class="col-md-2 control-label required">名称</label>
						<div class="col-md-4">
							<input name="name" class="form-control" id="name" type="text" value="">
							<div class="input-group-btn">
					             	<button class="btn btn-default" type="button" id="showKeywords">查看关键字</button>
							    </div>
						</div>
					</div>	
					
					
					<div class="form-group">
						<label class="col-md-2 control-label required">布局</label>
						<div class="col-md-4">
							<div class="input-group">
							<input type="text" readonly="readonly" class="form-control" name="layoutName" id="layoutName">
							<div class="input-group-btn"> <button class="btn btn-default" type="button" id="layoutSelect">选择</button> </div>
							<input type="hidden" readonly="readonly"  class="form-control" name="layoutId" id="layoutId">
							</div>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label required">序号</label>
						<div class="col-md-4">
							<input name="order" class="form-control" id="order" type="text" value="">
						</div>
					</div>
					
						<div class="form-group">
							<label class="col-md-2 control-label">option：</label>
							<div class="col-md-4">
								<textarea name='selectOption' id='selectOption' rows='4' class='form-control'>
SELECT a.code  as id,a.name as text FROM test_province a@
1=深圳市;2=坪山区
								</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">下拉框标签值：</label>
							<div class="col-md-4">
								<input type="text" name='selectLabel' id='selectLabel'  class='form-control' value="名称@ID"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">下拉框name值：</label>
							<div class="col-md-4">
								<input type="text" name='selectName' id='selectName'  class='form-control' value="name@id"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">文本name值：</label>
							<div class="col-md-4">
								<input type="text" name='textLabel' id='textLabel'  class='form-control' value="角色@组织"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">文本标签值：</label>
							<div class="col-md-4">
								<input type="text" name='textName' id='textName'  class='form-control' value="role@group"/>
							</div>
						</div>	
					
					<div class="form-group">
						<label class="col-md-2 control-label required">搜索框放置位置</label>
						<div class="col-md-4">
							<select name="searchLocation" class="form-control" id="searchLocation" type="text" value="">
								<option value="1">页面上方</option>
								<option value="2">页面下方</option>
							</select>
						</div>
					</div>	
					

				</div>
				<div class="tab-pane" id="tab3"><%@ include file="/formdesigner/page/component/common/hidden-disabled-readonly-defaultvalue.jsp" %></div>
			</div>
		</form>
	</div>

		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script src="<%=basePath%>/formdesigner/page/component/component.js"></script>
		<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>
		<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
		
			
			//初始化页面列表
			var basePath = "<%=basePath%>";
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
						$("#relatePageId").append(str);
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
				dialog.height(800); 
				dialog.width(500);
				dialog.reset();     // 重置对话框位置
			}
			
			
			
			
			$("#relatePageId").select2();
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
				.inputValidator({min:1,max:100,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写名称"})
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
			
			$("#order").formValidator({onFocus:"至少1位,最多4位",onCorrect:"符合要求"})
				.inputValidator({min:1,max:4,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写序号"});
			
		

	});
	

	</script>
	
	<script>	
		
			
	</script>
</body>
</html>