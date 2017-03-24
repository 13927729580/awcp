<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
 		<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="tab">
				<ul class="nav nav-tabs"><!-- 标签页nav里面的 href的值对应的是 tab-content里面的tab-pane的id值 -->
					<li class="active"><a href="#tab1" data-toggle="tab">Menu</a></li>
					<li class=""><a href="#tab2" data-toggle="tab">Button</a></li>
				</ul>
			</div>
			<div class="row tab-content">
 				<div class="tab-pane active" id="tab1"><jsp:include page="/unit/punRoleMenuAccess-edit.jsp"/></div> 
				<div class="tab-pane" id="tab2"><jsp:include page="/unit/componentAccess-edit.jsp"/></div> 
			</div>
		</div>
		
		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script>
			var roleId = ${vo.roleId};
			var setting = {
				check: {
					enable: true
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					beforeClick: zTreeBeforeClick,
					beforeCheck: zTreeBeforCheck 		
				}
			};	

			function zTreeBeforeClick(){
				return false;
			};
		
			function zTreeBeforCheck(treeId, treeNode){	
				var checked = treeNode.checked;
				var result = function(url,data){
					var val ;
					$.ajax({
						url:url,
						data:data,
						async:false,
						success:function(data){
							val = data.status=="0"?true:false;
						}
					});
					return val;
				};
				if(!checked){//添加权限
					checkResult = result("<%=basePath%>unit/punRoleAccessAJAXSave.do",{ 
						roleId: roleId, 
						resourceId: treeNode.id, 
						operType:"1"
					});
					checkResult || dialogAlert("Failed");
					return checkResult;
					 
				}else{//删除权限
					checkResult = result("<%=basePath%>unit/punRoleAccessAJAXDelete.do", { 
						roleId: roleId, 
						resourceId: treeNode.id 
					});
					checkResult || dialogAlert("Failed");
					return checkResult;
						
				}
					
			};
			
			function creatMenu(){
				var dataLi="";
				var dataMap = ${menuJson};
				$.each(dataMap,function(i,item){				
					dataLi ='<ul id="tree'+i+'" class="ztree"></ul>';
					$(dataLi).appendTo(".CloudTreeMenu");
					$.fn.zTree.init($("#tree"+i),setting,item);					  
				});
			}

			function alertMessage(message){
				var md = dialog({
					content: message
				});
				md.show();
				setTimeout(function () {
					md.close().remove();
				}, 2000);
			}
			
			$("document").ready(function(){
				creatMenu();
			});
		</script>		
		<script type="text/javascript">
			$(function(){
				$("#groupForm input[type='checkbox']").each(function(){
					$(this).click(function(){
						var roleId = ${vo.roleId};
						var resourceId = $(this).val();
						if($(this).is(":checked")){
							$.ajax({
								url:"<%=basePath%>unit/punResoAccessAJAXSave.do",
								data:{roleId:roleId,resourceId:resourceId},
								async:false,
								success:function(data){						
								}
							});		
						}else{
							$.ajax({
								url:"<%=basePath%>unit/punResoAccessAJAXDelete.do",
								data:{roleId:roleId,resourceId:resourceId},
								async:false,
								success:function(data){
								}
							});
						}
					});
				});
				
				$("#checkAll").click(function(){
					$("#groupForm input[type='checkbox']").each(function(){
						if(!$(this).is(":checked")){
							$(this).prop("checked",true);
							var roleId = ${vo.roleId};
							var resourceId = $(this).val();
							$.ajax({
								url:"<%=basePath%>unit/punResoAccessAJAXSave.do",
								data:{roleId:roleId,resourceId:resourceId},
								async:false,
								success:function(data){						
								}
							});	
						}
					});
				});
				
				$("#uncheckAll").click(function(){
					$("#groupForm input[type='checkbox']").each(function(){
						if($(this).is(":checked")){
							$(this).removeAttr("checked");
							var roleId = ${vo.roleId};
							var resourceId = $(this).val();
							$.ajax({
								url:"<%=basePath%>unit/punResoAccessAJAXDelete.do",
								data:{roleId:roleId,resourceId:resourceId},
								async:false,
								success:function(data){
								}
							});	
						}
					});
				});
				
				$("#saveBtn").click(function(){
					window.location = "<%=basePath%>unit/punRoleMenuAccessEdit.do?boxs=${roleId}&sysId=${sysId}";
					return false;
				});		
			});
		</script>		
	</body>
</html>
