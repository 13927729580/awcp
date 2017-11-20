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
			<button type="button" class="btn btn-success" id="quickModifyAll"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
			<div class="componentTable" contenteditable="false">
				<table class="table table-bordered" id="componentTable" align="left">
					<thead>
						<tr>
							<th></th>
							<th></th>
							<th>批量修改<input type='checkbox' name='layoutAllChange' id='layoutAllChange' value='all'/></th>
							<th>批量修改<input type='checkbox' name='dataItemAllChange' id='dataItemAllChange' value='all'/></th>
							<th>批量修改<input type='checkbox' name='allowNullAllChange' id='allowNullAllChange' value='all'/></th>
						</tr>
						<tr>
							<th>序号</th>
							<th>名称</th>
							<th>布局</th>
							<th>数据源</th>
							<th>是否允许为空</th>
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
			
			if(dialog != null){
				
				dialog.title("批量修改");
				dialog.width(1200);
				dialog.height(500); 
				dialog.reset();     // 重置对话框位置
			}
			
			if(dialog != null){
				data = dialog.data;
			}
			if(data.dynamicPageId && $("#dynamicPageId")){
				$("#dynamicPageId").val(data.dynamicPageId);
			}
			var dataSource = null;
			if(data != null && data.dataSource)
				dataSource = data.dataSource;
			
			//所选的组件ids	
			var _selects = null;
			if(data != null && data._selects)
				_selects = data._selects;
			
			if(_selects != null){
				$.ajax({
				   type: "POST",
				   url: basePath + "component/toBatchModifiAll.do",
				   data: "_selects="+_selects,
				   async:false,
				   success: function(data){
						if(data != null){	
							loadComponent(data);
						}else{
							alert("加载组件失败");
						}
				   }
				});	
			}
			
			$('.layoutSelect').bind("click",function(){
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				
				var all = $(":checkbox[name='layoutAllChange']:checked").size();
				
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
										if(all == 1){
											$(":input[name='layoutName']").val(this.returnValue.name);
											$(":input[name='layoutId']").val(this.returnValue.id);
										}else{
											$("#layoutName-"+index).val(this.returnValue.name);
											$("#layoutId-"+index).val(this.returnValue.id);
										}
										
									}
								}
							}).show(document.getElementById("layoutName-"+index));
					   	}
					});
				} else {
					alert("请先保存表单");
				}
			});
			
			$('.dataItemCodeSelect').click(function(){
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				
				var all = $(":checkbox[name='dataItemAllChange']:checked").size();
				if($("#dynamicPageId").val()){
					top.dialog({
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						title : '数据源选择',
						url : basePath + "formdesigner/page/component/common/dataSourceSelect.jsp",
						data : dataSource,
						onclose : function() {
							if (this.returnValue) {
								if(all==1){
									$(":input[name='dataItemCode']").val(this.returnValue);
								}else{
									$("#dataItemCode-"+index).val(this.returnValue);
								}
										
							}
						}
	
					}).show(document.getElementById("dataItemCode-"+index));
				} else {
					alert("请先保存表单");
				}
			});
			
			$(".isAllowNull").click(function() {
				var _this = $(this);
				var index = _this.attr("id").split("-")[1];
				var all = $(":checkbox[name='allowNullAllChange']:checked").size();
				if ($(this).prop("checked")) {
					if(all==1){
						$(":checkbox[name='isAllowNull']").prop("checked", true);
					}else{
						$("#isAllowNull-"+index).prop("checked", true);
					}
				}else{
					if(all==1){
						$(":checkbox[name='isAllowNull']").prop("checked", false);
					}else{
						$("#isAllowNull-"+index).prop("checked", false);
					}
				}
				
			});
			
			
			
		$("#cancelComponent").click(function(){
				if(dialog != null){
					dialog.close();
					dialog.remove();
				}
		});
		
		
			$("#quickModifyAll").click(function(){
		//		if($.formValidator.pageIsValid('1')){
		//			var formJson=$("#componentForm").serializeJSON();
		//			var name = $("#name").val();
					
		//			var description =  $("#description").val();
					var dynamicPageId=$("#dynamicPageId").val();
		//			var sjContent = JSON.stringify(formJson);
					
					var _dataItemCode=new Array();		//数据源
					$(":input[name='dataItemCode']").each(function(){
						var value=$(this).val();
						if(value != ""){
							_dataItemCode.push(value);
						}else{
							_dataItemCode.push("00000000");			//对于空字符串，下面的join函数会直接忽略掉，所有push一个特殊字符00000000 占位符，防止controller处理出现数组越界
						}
						
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
					var _isAllowNull=new Array();			//布局Id
					$(":checkbox[name='isAllowNull']").each(function(){
						var value=$(this).prop("checked");
						if(value==true){
							_isAllowNull.push('1');
						}else{
							_isAllowNull.push('0');
						}
						
					});
					
					
					var data = { "dynamicPageId" : dynamicPageId, "_selects" : _selects , "_dataItemCode" : _dataItemCode.join(",") , "_layoutName" : _layoutName.join(",") , "_layoutId" : _layoutId.join(",") ,"_isAllowNull" : _isAllowNull.join(",")};
					$.ajax({
						type: "POST",
						url: basePath + "component/quickModifyAll.do",
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
			
			
		function loadComponent(data){
			$("#componentt").empty();
			$.each(data, function(index, item) {
				var content = eval("(" + item.content + ")");
				var str="<tr id="+content['pageId']+">";
						str+="<td>"+(index+1)+"</td>";
						str+="<td>"+content['name']+"</td>";
						
						
						str+="<td>"+"<div>  <input type='text' readonly='readonly'  name='layoutName' value='" + content['layoutName'] + "' id='layoutName-"+index+"'>"
						+	"<button class='btn btn-default layoutSelect' type='button' id='layoutSelect-"+index+"'>选择</button> </div>"
						+	"<input type='hidden' readonly='readonly'   name='layoutId' value='" + content['layoutId']+ "' id='layoutId-"+index+"'>" + "</td>";
						str+="<td>"+"<div><input type='text' readonly='readonly'  name='dataItemCode' value='" + content['dataItemCode'] + "' id='dataItemCode-"+index+"'>"
						+	"<button class='btn btn-default dataItemCodeSelect' type='button' id='dataItemCodeSelect-"+index+"'>选择</button> </div>"
						+	"</td>";
						
					if(content['validateAllowNull']=='1'){
						str+="<td>"+"<input type='checkbox' name='isAllowNull' class='isAllowNull' id='isAllowNull-'" + index + "' checked/></td>";
					}else{
						str+="<td>"+"<input type='checkbox' name='isAllowNull' class='isAllowNull' id='isAllowNull-'" + index + "'/></td>";
					}
						
						str+="</tr>";
						
					$("#componentt").append(str);
			});
			
			return false;
		}
			
		
		
	
	</script>
</body>
</html>