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
		<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/dynamicpage.constant.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/page/map.js"></script>
 		<%-- <jsp:include page="" flush="true"/> --%>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link href="<%=basePath%>resources/plugins/treeTable/vsStyle/jquery.treeTable.css" rel="stylesheet" type="text/css" />
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="close"><i class="icon-plus-sign"></i>关闭</button>
			</div>
			<div class="row" id="dataform">
				<form class="form-horizontal" role="form" method="post" id="authority">
					<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
					<input type="hidden" name="nodeIds" id="nodeIds"/>
			
			<table id="table" class="table table-bordered"  width="100%">
                <thead id="authorityGroup">

                </thead>
             </table>
			
				</form>
				
			</div>
		</div>
		
		<div class="row">
			
		
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript">
		
		$('document').ready(function() {
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			if(dialog != null){
				data = dialog.data;
				if(data.pageId){
					$("#dynamicPageId").val(data.pageId);
				}
				if(data.nodeIds){
					$("#nodeIds").val(data.nodeIds);
				}
				
			}	
			
			if (dialog != null) {
				dialog.title('权限组配置');
				dialog.width(800);
				dialog.height(600);
				dialog.reset(); // 重置对话框位置
			}
			
			//查询数据
				$.ajax({
						   type: "POST",
						   url: "<%=basePath%>authority/getGroupListByPageId.do",
						   data:"dynamicPageId=" + $("#dynamicPageId").val()+
						   "&nodes="+$("#nodeIds").val(),
						   success: function(data){
								if(data != null){
								 $("#authorityGroup").empty();
								 $.each(data,function(index,item){
								 	var check=item.bakInfo;
								 	var show=new RegExp('check');
								 	var str="";
								 	if(show.test(check)){
								 		str="<tr><td><input type='checkbox' id='"+item.id+"' onclick='saveAuthority(\""+item.id+"\")' checked='checked'>"+item.name+"</td></tr>"
								 	
								 	}else{
								 		str="<tr><td><input type='checkbox' id='"+item.id+"' onclick='saveAuthority(\""+item.id+"\")'>"+item.name+"</td></tr>"
								 	}
								 	$("#authorityGroup").append(str);
								 });
						    }
						  }
						});	

});
	
	function saveAuthority(value){
	  
	  var status=1;
	  if($("#"+value).is(':checked')==false){
	  	status=0;
	  }
	
	   $.ajax({
			    type: "POST",
				url: "<%=basePath%>authority/addWorkflowNodeAuthority.do",
				data: "authorityGroupId="+value +
				"&status="+status+ 
				"&nodes="+$("#nodeIds").val()+
				"&dynamicPageId="+$("#dynamicPageId").val(),
				success: function(data){
				if(data != null){	
				if(dialog != null){
					
				}
				}else{
					alert("保存失敗");
				}
			}
	});	
	
}
	
	</script>
	</body>
</html>
