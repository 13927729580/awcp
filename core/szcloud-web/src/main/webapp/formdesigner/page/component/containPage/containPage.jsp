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
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>菜单编辑页面</title>
<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="saveComponent"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<div class="row" id="tab">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#tab1" data-toggle="tab">基本</a></li>
					<li class=""><a href="#tab3" data-toggle="tab">状态</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/component/containPage/basicInfo.jsp" %></div>
				<div class="tab-pane " id="tab3"><%@ include file="/formdesigner/page/component/common/hidden-disabled-readonly-defaultvalue.jsp" %></div>
			</div>
		</form>
	</div>

		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script src="<%=basePath%>/resources/plugins/echarts-2.2.1/src/component.js"></script>
		<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>
		<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			if(dialog != null){
				dialog.height(1000); 
				dialog.width(800);
				//dialog.reset();     // 重置对话框位置
			}
			
			if(dialog != null){
				data = dialog.data;
			}
			
			var dataSource = null;
			if(data != null && data.dataSource)
				dataSource = data.dataSource;
				
			//初始化页面列表
			//var basePath = "<%=basePath%>";
			var str = "<option value=''></option>";
			$.ajax({
			   type: "POST",
			   async:false,
			   url: basePath + "fd/listJson.do",
			   success: function(data){
			   		$("#relatePageId").empty();
					if(data != null){
						$.each(data, function(index, item) {
							str += "<option value='" + item.id+ "'>" + item.name + "</option>";
						});
						$("#relatePageId").append(str);
					}else{
						alert("加载组件失败");
					}
			   }
			});	
			
			initializeDocument("${componentType}","${_ComponentTypeName[componentType]}");
			
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});
			$("#dataItemCode").formValidator({onFocus:"请选择或输入数据源",onCorrect:"符合要求"})
				.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择或输入数据源"});
			$("#order").formValidator({onFocus:"至少1位,最多4位",onCorrect:"符合要求"})
				.inputValidator({min:1,max:4,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写序号"});
			$("#layoutId").formValidator({onFocus:"请选择布局",onCorrect:"符合要求"})
				.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择布局"});
		
			$("#relatePageId").select2();
			$('#relatePageId').change(function(){
				var relatePageId = $(this).val();
				if(relatePageId!=null){
					$.ajax({
			   		type: "POST",
			   		async:false,
			  		url: basePath + "component/getComponentListByPageId.do?dynamicPageId=" + relatePageId + "&pageSize=10000" ,
			  		success: function(data){
			  			//alert(data);
							if(data != null){
			  					var t= new Date().getTime();
								var myIndex=0;
								$("#dataItemCodeList").empty();
								$.each(data, function(index, item) {
									component = eval("(" + item.content + ")");
									if(component['componentType'] != "1009" && component['componentType'] != "1012"  && component['componentType'] != "1014" && component['componentType'] != "1017"){
										var str="<tr id=dataTr'"+myIndex+"'>";
										str+="<td>"+component['name']+ "<input type='hidden' readonly='readonly'   name='configures["+myIndex+"][pageId]' id='relateComId-"+myIndex+"' value='"+component['pageId']+"'/>"
										+ "<input type='hidden' readonly='readonly'   name='configures["+myIndex+"][name]' id='relateComName-"+myIndex+"' value='"+component['name']+"'/>" 
										+ "<input type='hidden' readonly='readonly'   name='configures["+myIndex+"][componentType]' id='relateComType-"+myIndex+"' value='"+component['componentType']+"'/>"
										+"</td>";

										
										str+="<td>"+"<input type='text'   name='configures["+myIndex+"][dataItemCode]' id='dataItemCode-"+myIndex+"' value='"+ component['dataItemCode'] + "'>"
										+	"<button class='btn btn-default dataItemCodeSelect' type='button' id='dataItemCodeSelect-"+myIndex+"' >选择</button>"
										
										+"</td>";
										//alert($("#dataItemCodeList").html());
										$("#dataItemCodeList").append(str);
										myIndex++;
									}
									
								});
								var e = new Date().getTime();
								alert("拼装html片段耗时：" + (e-t)/1000 + "");
								
							}else{
								alert("加载组件失败");
							}
							
					   }
					});	
				}
				
			});
			
			
			$('#openPage').click(function(){
				var id = $("#relatePageId").val();
				if(id!==''){
					var url = basePath + "fd/edit.do?_selects=" + id;
					window.open(url); 
				}else{
					alert("没选择");
				}
			});
			
			
			$("body").on("click",".dataItemCodeSelect",function(){
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				if($("#dynamicPageId").val()){
					top.dialog({
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						title : '数据源选择',
						url : basePath + "formdesigner/page/component/common/dataSourceSelect.jsp",
						data : dataSource,
						onclose : function() {
							if (this.returnValue) {
									$("#dataItemCode-"+index).val(this.returnValue);
							}
						}
					}).show(document.getElementById("dataItemCode-"+index));
				} else {
					alert("请先保存表单");
				}
			});
	});
	
	</script>
</body>
</html>