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
				<button type="button" class="btn btn-success" id="save"><i class="icon-plus-sign"></i>保存</button>
			</div>
			<div class="row" id="dataform">
				<form class="form-horizontal" role="form" method="post" id="binding">
					<input type="hidden" name="pageId" id="pageId"/>
					
					<div class="form-group">
						<label class="col-md-2 control-label">PAGEID_PC</label>
						<div class="col-md-4">
							<input id="PC" type="text" class="chosen-select form-control" name="PC" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">PAGEID_LIST</label>
						<div class="col-md-4">
							<input id="PC_LIST" type="text" class="chosen-select form-control" name="PC_LIST" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">PAGEID_APP</label>
						<div class="col-md-4">
							<input id="APP" type="text" class="chosen-select form-control" name="APP" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">PAGEID_APP_LIST</label>
						<div class="col-md-4">
							<input id="APP_LIST" type="text" class="chosen-select form-control" name="APP_LIST" value=""/>
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
		
				if(data.id){
					$("#pageId").val(data.id);
				}

			}	
			
			
	
			// if($.fn.chosen) $('.chosen-select').chosen();
			if (dialog != null) {
				dialog.title('组件配置');
				dialog.width(500);
			//	dialog.height($(document).height);
				dialog.height(200);
				dialog.reset(); // 重置对话框位置
			}
			
			//查询数据
				$.ajax({
						   type: "POST",
						   url: "<%=basePath%>/common/user/get.do",
						   data: "id="+$("#pageId").val(),
						   success: function(data){
								if(data != null){	
							     $("#PC").val(data.PAGEID_PC);
							     $("#PC_LIST").val(data.PAGEID_PC_LIST);
								 $("#APP").val(data.PAGEID_APP);
								 $("#APP_LIST").val(data.PAGEID_APP_LIST);
						    }
						  }
						});	
			
			
			$("#save").click(function(){
				if($.formValidator.pageIsValid('1')){
					
					var pc = $("#PC").val();
					var pc_list = $("#PC_LIST").val();
					var app = $("#APP").val();
					var app_list=$("#APP_LIST").val();
					var id=$("#pageId").val();
					
					$.ajax({
						   type: "POST",
						   url: "<%=basePath%>/common/user/update.do",
						   data: "pc="+pc + 
						   "&id=" +id+
						   "&pc_list="+pc_list+"&app_list="+app_list+"&app="+app,
						   success: function(data){
								if(data != null){	
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
