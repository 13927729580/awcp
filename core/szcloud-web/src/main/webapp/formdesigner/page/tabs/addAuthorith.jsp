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
				<button type="button" class="btn btn-success" id="saveAuthority"><i class="icon-plus-sign"></i>保存</button>
				<!-- <button type="button" class="btn btn-success" id="editBtn"><i class="icon-edit"></i>弹窗编辑</button> -->
			</div>
			<div class="row" id="dataform">
				<form class="form-horizontal" role="form" method="post" id="authority">
					<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
					
					<div class="form-group">
						<label class="col-md-2 control-label required">名称</label>
						<div class="col-md-4">
							<input id="name" type="text" class="chosen-select form-control" name="name" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label required">序号</label>
						<div class="col-md-4">
							<input id="order" type="text" class="chosen-select form-control" name="order" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">描述</label>
						<div class="col-md-4">
							<input id="destry" type="text" class="chosen-select form-control" name="destry" value=""/>
						</div>
					</div>
				
				</form>
				
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		
		<script type="text/javascript">
		
		$.formValidator.initConfig({formID:"authority",debug:false,onSuccess:function(){
			//	$("#authority").submit();
				  return false;
		    },onError:function(){alert("错误，请看提示")}});
		$("#name").formValidator({empty:true,onShow:"请输入名称",onCorrect:"符合格式要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		$("#order").formValidator({onShow:"请输入序号",onFocus:"请输入序号",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});

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
		
				if(data.dynamicPageId){
					$("#dynamicPageId").val(data.dynamicPageId);
				}

			}	
			
			
	
			// if($.fn.chosen) $('.chosen-select').chosen();
			if (dialog != null) {
				dialog.title('权限组配置');
				dialog.width(500);
			//	dialog.height($(document).height);
				dialog.height(200);
				dialog.reset(); // 重置对话框位置
			}
			
			$("#saveAuthority").click(function(){
				if($.formValidator.pageIsValid('1')){
					
					var formJson=$("#authority").serializeJSON();
					var order = $("#order").val();
					var destry = $("#destry").val();
					var name = $("#name").val();
					var dynamicPageId=$("#dynamicPageId").val();
					
					$.ajax({
						   type: "POST",
						   url: "<%=basePath%>authority/authorityGroupSave.do",
						   data: "order="+order + 
						   "&dynamicPageId=" + dynamicPageId+
						   "&destry=" + destry+"&name=" + name,
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
