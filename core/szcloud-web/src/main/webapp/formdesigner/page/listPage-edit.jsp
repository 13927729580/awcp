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
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>动态表单编辑页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--edit页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="dataform">表单区（--输入组--按钮组）</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="#">首页</a></li>
		          <li><a href="#">动态页面</a></li>
		          <li class="active">表单编辑</li>
		        </ul>
			</div>
			<div class="row" id="buttons">
				<button class="btn btn-success" id="savePage" ><i class="icon-save"></i>保存</button>
				<button class="btn btn-info " id="export"><i class="icon-zoom-out"></i>预览</button>
				<button class="btn btn-warning" id="ret"><i class="icon-backward"></i>返回</button>
			</div>
			
			<div class="row" id="tab">
				<ul class="nav nav-tabs"><!-- 标签页nav里面的 href的值对应的是 tab-content里面的tab-pane的id值 -->
					<li class="active"><a href="#basicInfo" data-toggle="tab">基本</a></li>
					<li class=""><a href="#datasource" data-toggle="tab">数据源</a></li>
					<li class=""><a href="#componentConfig" data-toggle="tab">组件</a></li>
					<li class=""><a href="#actConfig" data-toggle="tab">动作</a></li>
				</ul>
			</div>
			
			<form class="form-horizontal" id="groupForm">
				<div class="row tab-content">
						<div class="tab-pane active" id="basicInfo"><%@ include file="tabs/basicInfo.jsp" %></div>
						<div class="tab-pane" id="datasource"><%@ include file="tabs/datasource.jsp" %></div>
						<div class="tab-pane" id="componentConfig"><%@ include file="tabs/component.jsp" %></div>
						<div class="tab-pane" id="actConfig"><%@ include file="tabs/act.jsp" %></div>
				</div>
			</form>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/form.dpcommon.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/page/script/act.js"></script>
		<script type="text/javascript" >//自己按需求可以提取单独的js文件 存放与resources/scripts文件夹下 命名规则：form.文件名.js
		
		
		$("#addStyle").click(function(){
			top.dialog({
				id: 'add-Style' + Math.ceil(Math.random()*10000),
				title: '载入中...',
				url: "<%=basePath%>/formdesigner/page/style/style-edit.jsp",
				height:600,
				width:500,
				onclose: function () {
					if (this.returnValue) {
						var ret= this.returnValue;
						$("#ds option[value!='']").remove();
						$.each(ret,function(idx,item){
							$("#ds").append("<option value=\""+item.id+"\">"+item.name+"</option>");
						});
					}
				}, 
			}).showModal(); 
			return false;
			
		});
		function initPage(){
			//发送ajax请求
			$.ajax({
				type:"GET",
				url:"<%=basePath%>fd/style/getAllByAjax.do",
				async:false,
				//回调函数
				success:function(jsonData){
					var json = eval(jsonData);
					
					$.each(json,function(idx,item){
						$("#ds").append("<option value=\""+item.id+"\">"+item.name+"</option>");
					});
				},
			    error: function (XMLHttpRequest, textStatus, errorThrown) { 
	                  alert(errorThrown); 
			    }
			});
		}
		function addComponent(type){
			edit("component","add",null,type);
			return false;
		}
		function addModel(type){
			edit("model","add",null,type);
			return false;
		}
			$(document).ready(function() {
				initPage();
				$("#savePage").click(function(){
					$("#form1").submit();
				});
				$("#deleteModel").click(function(){
					edit("model","remove",null,null);
					alert($("#modelJsonArray").val());
					return false;
					});
				$("#deleteComponent").click(function(){
					edit("component","remove",null,null);
					return false;
					});
				
				$("#ret").click(function(){	
					window.open("<%=basePath%>fd/list.do?currentPage=0","_self");
				});
				$("#export").click(function(){	
					top.dialog(
							{ id: 'add-dialog' + Math.ceil(Math.random()*10000),
								title: '页面预览',
								url:'<%=basePath%>document/view.do?pageId='+$("#id").val(),
								okValue:'确定',
								ok:function(){},
								cancelValue:'取消',
								cancel:function(){}
							}
					).showModal();
					
				});
				
			});
			
	
		
         </script>
	</body>
</html>
