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
			<button type="button" class="btn btn-success" id="quickComByData"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
			<div class="componentTable" contenteditable="false">
				<table class="table table-bordered" id="componentTable" align="left">
					<thead>
						<tr>
							<th>数据源</th>
							<th>文本</th>
							<th>文本布局</th>
							<th>类型</th>
							<th>布局</th>
							<th>删除</th>
						</tr>
					</thead>
					<tbody id="componentt">
					</tbody>
				</table>
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
		<script src="<%=basePath%>/resources/scripts/map.js"></script>
		<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			var model;
			if(dialog != null){
				data = dialog.data;
			if(data != null && data.dataSource)
					model = data.dataSource;
			}
			
			if(data.dynamicPageId && $("#dynamicPageId")){
				$("#dynamicPageId").val(data.dynamicPageId);
			}
			
			var dataSource=new Map();
			var dataSourceArray = eval(model);
			if(!empty(model)){
				var dataJson = JSON.parse(model);
//				$.each(dataJson,function(idx,item){
//					dataSource.put(item.id,item);
//				});
				evalDataSource(dataSourceArray);
				freshComponentTable(dataSource);
			}
			
		function evalDataSource(modelJsonArray){
			if(modelJsonArray && modelJsonArray.length > 0){
				for(var i = 0; i < modelJsonArray.length;i++){
					if(modelJsonArray[i].modelItemCodes){
						 var itemCodes = modelJsonArray[i].modelItemCodes.split(",");
						 for(var j = 0; j < itemCodes.length; j++){
							dataSource.put(modelJsonArray[i].name + "." + itemCodes[j],modelJsonArray[i].name + "." + itemCodes[j]);
						 }
					}
				}
			}
		}

			function freshComponentTable(dataSource){//refreshActTable act table
				$("#componentt").empty();
				
				dataSource.each(function(key,value,index){
					var str="<tr id=dataTr'"+index+"'>";
						str+="<td>"+value+ "<input type='hidden' readonly='readonly'   name='dataSource' id='dataSource-"+index+"' value='"+value+"'/>"+ "</td>";
						str+="<td><input name='labelTitle' id='labelTitle-"+index+"'" + "type=text" + "/>"+"</td>";
						
						str+="<td>"+"<input type='text' readonly='readonly'  name='labelLayoutName' id='labelLayoutName-"+index+"'>"
						+	"<div  <button class='btn btn-default labelLayoutSelect' type='button' id='labelLayoutSelect-"+index+"'>选择</button> </div>"
						+	"<input type='hidden' readonly='readonly'   name='labelLayoutId' id='labelLayoutId-"+index+"'>"
						+"</td>";
						
						str+="<td>"+"<select name='componentType'  id='componentType-"+index+"' class='componentType' type='select' value=''>"
						+	"<option value='1001'>单行文本框</option>"
						+	"<option value='1002'>日期文本框</option>"
						+	"<option value='1005'>多行输入框</option>"
						+	"<option value='1006'>下拉选项框</option>"
						+	"<option value='1007'>密码框</option>"
						+	"</select>"+"</td>";
						
						str+="<td>"+"<input type='text' readonly='readonly'  name='layoutName' id='layoutName-"+index+"'>"
						+	"<div  <button class='btn btn-default layoutSelect' type='button' id='layoutSelect-"+index+"'>选择</button> </div>"
						+	"<input type='hidden' readonly='readonly'   name='layoutId' id='layoutId-"+index+"'>"
						+"</td>";
						
						str+="<td><a href='javascript:void(0)' class='removeTr'>删除</a>"+"</td>";
						str+="</tr>";
						
					$("#componentt").append(str);
				});
				return false;
			}
			
			
			$('.layoutSelect').click(function(){
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				if($("#dynamicPageId").val()){
					$.ajax({
						url: basePath + "layout/listLayoutInTree.do?dynamicPageId="+$("#dynamicPageId").val(),
						dataType:'json',
						success: function(msg){
							top.dialog({ 
								id: 'select-layout-dialog' + Math.ceil(Math.random()*1000000),
								title: '选择布局',
								url: basePath + "formdesigner/page/component/zTreeLayout.jsp",
								data:msg,
								onclose : function() {
									if (this.returnValue) {
										//alert(this.returnValue.id);
										$("#layoutName-"+index).val(this.returnValue.name);
										$("#layoutId-"+index).val(this.returnValue.id);
									}
								}
							}).show(document.getElementById("layoutName-"+index));
					   	}
					});
				} else {
					alert("请先保存表单");
				}
			});
			
			$('.labelLayoutSelect').click(function(){
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				if($("#dynamicPageId").val()){
					$.ajax({
						url: basePath + "layout/listLayoutInTree.do?dynamicPageId="+$("#dynamicPageId").val(),
						dataType:'json',
						success: function(msg){
							top.dialog({ 
								id: 'select-layout-dialog' + Math.ceil(Math.random()*1000000),
								title: '选择布局',
								url: basePath + "formdesigner/page/component/zTreeLayout.jsp",
								data:msg,
								onclose : function() {
									if (this.returnValue) {
										//alert(this.returnValue.id);
										$("#labelLayoutName-"+index).val(this.returnValue.name);
										$("#labelLayoutId-"+index).val(this.returnValue.id);
									}
								}
							}).show(document.getElementById("labelLayoutName-"+index));
					   	}
					});
				} else {
					alert("请先保存表单");
				}
			});
			
			$('.removeTr').click(function(){
				var _this = $(this);
				//alert(_this.parents("tr").html());
				_this.parents("tr").remove();
			});
			
			
			
		$("#cancelComponent").click(function(){
				if(dialog != null){
					dialog.close();
					dialog.remove();
				}
		});
		
		
			$("#quickComByData").click(function(){
		//		if($.formValidator.pageIsValid('1')){
					var formJson=$("#componentForm").serializeJSON();
					var name = $("#name").val();
					
					var description =  $("#description").val();
					var dynamicPageId=$("#dynamicPageId").val();
					var sjContent = JSON.stringify(formJson);
					
					var _dataSource=new Array();
					$(":input[name='dataSource']").each(function(){
						var value=$(this).val();
						_dataSource.push(value);
					});
					var _labelTitle=new Array();
					$(":input[name='labelTitle']").each(function(){
						var value=$(this).val();
						_labelTitle.push(value);
					});
					var _componentType=new Array();
					$(":input[name='componentType']").each(function(){
						var value=$(this).val();
						_componentType.push(value);
					});
					var _layoutName=new Array();		//布局名称
					$(":input[name='layoutName']").each(function(){
						var value=$(this).val();
						_layoutName.push(value);
					});
					var _layoutId=new Array();			//布局Id
					$(":input[name='layoutId']").each(function(){
						var value=$(this).val();
						_layoutId.push(value);
					});
					var _labelLayoutName=new Array();		//文本布局名称
					$(":input[name='labelLayoutName']").each(function(){
						var value=$(this).val();
						_labelLayoutName.push(value);
					});
					var _labelLayoutId=new Array();			//文本布局Id
					$(":input[name='labelLayoutId']").each(function(){
						var value=$(this).val();
						_labelLayoutId.push(value);
					});
					
					var data = { "dynamicPageId" : dynamicPageId, "_dataSource" : _dataSource.join(",") , "_labelTitle" : _labelTitle.join(",") , "_layoutName" : _layoutName.join(",") , "_componentType" : _componentType.join(",") , "_layoutId" : _layoutId.join(",") ,"_labelLayoutId" : _labelLayoutId.join(",") ,"_labelLayoutName" : _labelLayoutName.join(",")};
					$.ajax({
						type: "POST",
						url: basePath + "component/quickComByData.do",
						data: data,
						async : false,
						success: function(data){
							if(data != null){	
								$("#componentId").val(data.id);
								
								if(dialog != null){
									dialog.close(data);
									dialog.remove();
								}
							}else{
								alert("保存失败");
							}
						}
					});	
		//		}
				return false;
			});
			
		});	
			
			
			
			
		
		
	
	</script>
</body>
</html>