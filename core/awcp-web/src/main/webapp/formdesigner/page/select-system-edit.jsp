<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
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
 		<title>复制到指定系统</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button class="btn btn-success" id="savePage" ><i class="icon-save"></i>保存</button>
				<button class="btn btn-default" id="closeBtn"><i class="icon-backward"></i>关闭</button>
			</div>
			<c:if test="${result!=null&&''!=result}">
				<span style="color: red">(${result})</span>
			</c:if>
			<form class="form-horizontal" id="groupForm" action="<%=basePath%>fd/copyToSystem.do" method="post">
				<input type="hidden" name="_selects" value="" id="_selects">
				<div class="form-group">
					<div class="col-md-12">
						<div class="input-group">
							<span class="input-group-addon required">选择目标系统</span>
							<select name="systemId" class="form-control" id="systemId">
								<c:forEach items="${list}" var="vo">
									<c:choose>
					        	 		<c:when test="${vo.sysId == system.sysId }">
											<option value="${vo.sysId }" selected="selected">${vo.sysShortName }(当前系统)</option>
					        	 		</c:when>
					        	 		<c:otherwise>
											<option value="${vo.sysId }">${vo.sysShortName }</option>
					        	 		</c:otherwise>
					        	 	</c:choose>
				        	 	</c:forEach>
							</select>
						</div>
					</div>	
				</div>
			</form>
		</div>
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript" >
			$(document).ready(function() {
				$("#systemId").select2();
				try {
					var dialog = top.dialog.get(window);
				} catch (e) {
					return;
				}
				
				var data = null;
				if(dialog != null){
					var data = dialog.data;
					if(data != null && data.pageId){
						$("#_selects").val(data.pageId);
					}
				}	
				
				$("#closeBtn").bind("click",function(){
					if(dialog != null ){
						dialog.close();
						dialog.remove();
					}
				});
				
				$("#savePage").click(function(){
					$.ajax({
						url:"<%=basePath%>fd/copyToSystem.do",
						type:"POST",
						async:false,
						data:$("#groupForm").serialize(),
						success:function(o){
							if("1"==o.rtn){
								alert("复制成功！");
								if(dialog != null){
									dialog.close();
									dialog.remove();
								}
							}else{
								alert(o.msg);
							}
						},
						error: function (XMLHttpRequest, textStatus, errorThrown) { 
							alert(errorThrown); 
						}
					});
				});	
			});
		</script>
	</body>
</html>
