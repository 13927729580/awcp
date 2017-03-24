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
					<input type="hidden" name="parentId" id="parentId"/>
					
					<div class="form-group">
						<label class="col-md-2 control-label">名称</label>
						<div class="col-md-4">
							<input id="name" type="text" class="chosen-select form-control" name="name" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">横坐标X</label>
						<div class="col-md-4">
							<input id="top" type="text" class="chosen-select form-control" name="top" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">纵坐标Y</label>
						<div class="col-md-4">
							<input id="left" type="text" class="chosen-select form-control" name="left" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">布局类型</label>
						<div class="col-md-4">
								<select name="layoutType" id="layoutType" class="chosen-select form-control">
									<option value="1">垂直布局</option>
									<option value="2">水平布局</option>
								</select>
						</div>
						
					</div><!-- radio tab -->
					
					<div class="form-group">
						<label class="col-md-2 control-label">占比</label>
						<div class="col-md-4">
							<input id="proportion" type="text" class="chosen-select form-control" name="proportion" value=""/>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-md-2 control-label">顺序</label>
						<div class="col-md-4">
							<input id="order" type="text" class="chosen-select form-control" name="order" value=""/>
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
	$("#name").formValidator({empty:true,onShow:"请输入名称",onCorrect:"符合格式要求"}).inputValidator({min:1,max:50,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
	$("#proportion").formValidator({onShow:"请输入布局占比",onFocus:"请输入占比",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
   // $("#order").formValidator({onShow:"请输入顺序",onFocus:"请输入顺序",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
   
         
		
		//在更新时加载组件值;
		function loadLayoutData(storeObject){
			var item = eval("("+storeObject.content+")");
			if(item.pageId != null){
				if(item.pageId) 	$("#pageId").val(item.pageId);
				if(item.name) $("#name").val(item.name);
				if(item.top) $("#top").val(item.top);
				if(item.left) $("#left").val(item.left);
				if(item.proportion) $("#proportion").val(item.proportion);
				if(item.order) $("#order").val(item.order);
				if(item.parentId) $("#parentId").val(item.parentId);
				if(item.systemId) $("#systemId").val(item.systemId);
				
				if(item.layoutType)	$("#layoutType option[value='"+item.layoutType+"']").attr("selected","true");
			}
		}
		
		$('document').ready(function() {
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			
			var layoutId = "";
			if(dialog != null){
				data = dialog.data;
				layoutId = data.pageId;
				if(data.dynamicPageId){
					$("#dynamicPageId").val(data.dynamicPageId);
				}
				if(data.parentId){
					$("#parentId").val(data.parentId);
				}
			}	
			//如果是编辑，则加载组件内容
			if(layoutId){
				$.ajax({
					   type: "POST",
					   url: "<%=basePath%>layout/getLayoutById.do",
					   data: "storeId="+layoutId,
					   success: function(data){
							if(data != null && data.id != null){	
								loadLayoutData(data);
							//	alert("加载组件成功..." + data.id);
							}else{
								alert("保存失敗");
							}
					   }
					});	
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
					var name = $("#name").val();
					var layoutId = $("#pageId").val();
					var description =  $("#parentId").val();
					var dynamicPageId=$("#dynamicPageId").val();
					var order=$("#order").val();
					var sjContent = JSON.stringify(formJson);
					$.ajax({
						   type: "POST",
						   url: "<%=basePath%>layout/save.do",
						   data: "id="+layoutId + "&name="+name + 
						   "&description="+description+"&content="+sjContent+
						   "&dynamicPageId=" + dynamicPageId+
						   "&order=" + order,
						   success: function(data){
								if(data != null){	
									$("#layoutId").val(data.id);
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
