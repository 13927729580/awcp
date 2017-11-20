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
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>菜单编辑页面</title>
<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
 		<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
	</head>
	<body id="main" class="inner-form">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-4 col-xs-4">
					<div class="panel tree-panel">
			            <div class="panel-heading">组织结构</div>				            
			            <div class="panel-body">
			            	<ul id="tree1" class="ztree"></ul>
			            </div>
			          </div>
				</div>
				<div class="col-md-8 col-xs-8">
					<div class="panel member-panel">
						<div class="panel-heading">已选择</div>
						<div class="panel-body">
							<div class="tip">请从下方选择框选择用户</div>
						</div>
					</div>
					<div class="panel select-panel">
			            <div class="panel-heading">
			            	<div class="col-xs-4"  style="margin-top: 5px;">
			            		<button class="btn btn-mini" js="checkAll">全选</button>
			            		<button class="btn btn-mini" js="unCheckAll">全不选</button>
			                </div>
			            	<div class="col-xs-8 input-group">
			            		<input type="text" class="form-control" placeholder="请输入名称" name="memberName" id="memberName"/>
			                	<span class="input-group-btn"><button class="btn btn-default searchBtn" type="button" onclick="searchMember()">搜索</button></span>
			            	</div>
			            </div>
			            <div class="panel-body">
			            	<div class="tip">请点击左侧菜单</div>
			            </div>
			        </div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 text-center">
	              	<button type="button" class="btn btn-success" id="subBtn"><i class="icon-save"></i>提交</button>
					<a href="javascript:;" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
	            </div>
			</div>
		</div>
			
		<%@ include file="/resources/include/common_form_js.jsp" %>	
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.dynamicSelect.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jsonpath-0.8.0.js"></script>
		<script type="text/javascript">
         $('document').ready(function() {

        	try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
        	
 			
		   $("#undoBtn").click(function(){//关闭弹窗 取消
			   dialog.close("");
		   });
		   $("#subBtn").click(function(){//关闭弹窗 提交
			   dialog.close(item);
		   });
		 });
         </script>	
	</body>
</html>
