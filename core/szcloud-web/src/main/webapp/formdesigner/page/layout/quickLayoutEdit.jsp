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
				<button type="button" class="btn btn-success" id="saveLayout"><i class="icon-plus-sign"></i>保存</button>
				<!-- <button type="button" class="btn btn-success" id="editBtn"><i class="icon-edit"></i>弹窗编辑</button> -->
			</div>
			<div class="row" id="dataform">
				<form class="form-horizontal" role="form" method="post" id="layoutForm">
					<input type="hidden"  name="pageId" id="pageId">
					<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
					<input type="hidden" name="others[parentId]" id="parentId"/>
					
					<div class="form-group">
						<label class="col-md-2 control-label">名称</label>
						<div class="col-md-4">
							<input id="name" type="text" class="chosen-select form-control" name="name" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">行数</label>
						<div class="col-md-4">
							<input id="rows" type="text" class="chosen-select form-control" name="rows" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">列数</label>
						<div class="col-md-4">
							<input id="cods" type="text" class="chosen-select form-control" name="cods" value=""/>
						</div>
					</div>
				
				</form>
				
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		
		<script type="text/javascript">
		
		$.formValidator.initConfig({formID:"layoutForm",debug:false,onSuccess:function(){
			//	$("#layoutForm").submit();
				  return false;
		    },onError:function(){alert("错误，请看提示")}});
		$("#name").formValidator({empty:true,onShow:"请输入名称",onCorrect:"符合格式要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		$("#rows").formValidator({onShow:"请输入行数",onFocus:"请输入行数",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
	    $("#cods").formValidator({onShow:"请输入列数",onFocus:"请输入列数",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
	   
		
		$('document').ready(function() {
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			
		//	var layoutId = "";
			if(dialog != null){
				data = dialog.data;
		//		layoutId = data.pageId;
				if(data.dynamicPageId){
					$("#dynamicPageId").val(data.dynamicPageId);
				}
				if(data.parentId){
					$("#parentId").val(data.parentId);
				}
			}	
			
			
	
			// if($.fn.chosen) $('.chosen-select').chosen();
			if (dialog != null) {
				dialog.title('组件配置');
				dialog.width(300);
			//	dialog.height($(document).height);
				dialog.height(600);
				dialog.reset(); // 重置对话框位置
			}
			
			$("#saveLayout").click(function(){
				if($.formValidator.pageIsValid('1')){
					
					var formJson=$("#layoutForm").serializeJSON();
					var rows = $("#rows").val();
					var cods = $("#cods").val();
					var name = $("#name").val();
				//	var layoutId = $("#pageId").val();
					var parentId =  $("#parentId").val();
					var dynamicPageId=$("#dynamicPageId").val();
					
					var sjContent = JSON.stringify(formJson);
					$.ajax({
						   type: "POST",
						   url: "<%=basePath%>layout/quickSave.do",
						   data: "rows="+rows + 
						   "&parentId="+parentId+"&content="+sjContent+
						   "&dynamicPageId=" + dynamicPageId+
						   "&cods=" + cods+"&name=" + name,
						   success: function(data){
								if(data != null){	
							//		$("#layoutId").val(data.id);
									if(dialog != null){
										dialog.close(data);
										dialog.remove();
									}
								}else{
									alert("保存失敗");
								}
						   }
						})	
				}
				
			});
							
		});
	</script>
	</body>
</html>
