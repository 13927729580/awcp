<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>动态页面表单</title>
	<base href="<%=basePath%>">
	<%@ include file="/resources/include/common_css.jsp" %>
</head>
<body id="main">
	<div class="container-fluid">
		<h3>父级页面</h3>	
		<ul>
			<c:if test="${empty parent}">该页面尚未被引用</c:if>		
			<c:forEach items="${parent}" var="map">
				<li>
					<a href="<%=basePath%>fd/edit.do?_selects=${map.value.id}" target="sysEditFrame">
						${map.value.name}
					</a>&nbsp;&nbsp;&nbsp;
					<button class="btn btn-warning btn-sm" onclick="publishPage(${map.value.id})">
						<i class="icon-edit"></i>发布
					</button>
					&nbsp;&nbsp;&nbsp;
						
					<button class="btn btn-info btn-sm" onclick="viewPage(${map.value.id})">
						<i class="icon-zoom-out"></i>预览
					</button>	
					
				</li>
			</c:forEach>
		</ul>
		<h3>包含页面</h3>	
		<ul>
		<c:if test="${empty child}">该页面无包含组件</c:if>
			<c:forEach items="${child}" var="vo">
				<li>
					<a href="<%=basePath%>fd/edit.do?_selects=${vo.value.id}" target="sysEditFrame">
						${vo.value.name}
					</a>&nbsp;&nbsp;&nbsp;
					<button class="btn btn-warning btn-sm" onclick="publishPage(${vo.value.id})">
						<i class="icon-edit"></i>发布
					</button>&nbsp;&nbsp;&nbsp;
					<button class="btn btn-info btn-sm" onclick="viewPage(${vo.value.id})">
						<i class="icon-zoom-out"></i>预览
					</button>	
				</li>
			</c:forEach>
		</ul>			
	</div>

	<%@ include file="/resources/include/common_js.jsp" %>
	<script type="text/javascript">
	$(function(){
    	try {
			  var dialog = top.dialog.get(window);
			}catch (e) {
			}
			if(dialog){
			  dialog.height($(document).height());
				dialog.width($(document).width());
				dialog.reset();
			}
		$("a").click(function(){
			dialog.close();
		});
    
	
    });
    function publishPage(dynamicPageId){	
		if(window.confirm("确定发布？")){
			$.ajax({
				type:"GET",
				url:"<%=basePath%>fd/publishOnePage.do?id="+dynamicPageId,
				async:false,
				//回调函数
				success:function(jsonData){
				}
			});
		}
		return false;
	}
    function viewPage(dynamicPageId){	
		top.dialog(
			{   id: 'add-dialog' + Math.ceil(Math.random()*10000),
				title: '页面预览',
				url:'<%=basePath%>document/view.do?dynamicPageId='+dynamicPageId,
				width:1024,
				height:768,
				okValue:'确定',
				ok:function(){},
				cancelValue:'取消',
				cancel:function(){}
			}
		).showModal();
		return false;
	}
	</script>
</body>
</html>


