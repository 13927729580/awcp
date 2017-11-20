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
 		<title>菜单编辑页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		
	</head>
	<body id="main">
	
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="saveExcelLayout"><i class="icon-plus-sign"></i>保存</button>
				
			</div>
			
			<div class="row" id="dataform">
				<form class="form-horizontal" role="form" method="post" id="layoutForm"
					action="<%=basePath%>layout/excelSave.do"  enctype="multipart/form-data">
					<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>	
					<div class="form-group">
						<label class="col-md-2 control-label">上传excel文件</label>
						<div class="col-md-4">
							<input id="excelFile" type="file" class="chosen-select" name="excelFile" value="" multipart/>
						</div>
					</div>
							
				</form>
				
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		
		<script type="text/javascript">
		
		
		$('document').ready(function() {
			
			if(this.returnValue){
				alert(this.returnValue);
			}
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			
			if(dialog != null){
				data = dialog.data;
				if(data.dynamicPageId){
					$("#dynamicPageId").val(data.dynamicPageId);
				}
			}	
			
			function validateCheckOut(dynamicPageId){
				var flag;
				$.ajax({
				type:"POST",
				url:"fd/validateCheckOut.do?id=" + dynamicPageId,
		
				async:false,
				dataType:'json',
				//回调函数
				success:function(result){
					if(result.value == 1){
						flag = true;
					}else{
						alert(result);
						flag = false;
					}	   	
				},
 		 	 	 error: function (XMLHttpRequest, textStatus, errorThrown) { 
 		  	       alert(errorThrown); 
  		 	       flag = false;
  		 	 	 }
				});
				return flag;
			}
			
			
			$("#saveExcelLayout").click(function(){
				
					if(!validateCheckOut($("#dynamicPageId").val())){	//校验是否已签出
			
						return false;
					}
				//	var formJson=$("#layoutForm").serializeJSON();
				//	var rows = $("#rows").val();
				//	var cods = $("#cods").val();
				//	var name = $("#name").val();
				//	var layoutId = $("#pageId").val();
				//	var parentId =  $("#parentId").val();
				//	var dynamicPageId=$("#dynamicPageId").val();
				//	var excelFile = $("#excelFile").val();
					var excelFile = $("#layoutForm").submit();
					
				//	var sjContent = JSON.stringify(formJson);
				
					
				});	
							
		});
	</script>
	</body>
</html>
