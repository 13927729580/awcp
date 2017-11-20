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
					<li class=""><a href="#tab2" data-toggle="tab">标签</a></li>
					<li class=""><a href="#tab3" data-toggle="tab">状态</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/component/tabs/basicInfo.jsp" %></div>
				<div class="tab-pane" id="tab2"><%@ include file="/formdesigner/page/component/tabs/tag.jsp" %></div>
				<div class="tab-pane " id="tab3"><%@ include file="/formdesigner/page/component/common/hidden-disabled-readonly-defaultvalue-load.jsp" %></div>
			</div>
		</form>
	</div>

		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script src="<%=basePath%>/formdesigner/page/component/component.js"></script>
		<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>
		<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		var options = "";
		$('document').ready(function(){
			$.ajax({
				type:'post',
				url:basePath+'fd/listJson.do',
				success:function(data){
					if(data != null){
						$.each(data, function(index, item) {
							options += "<option value='" + item.id+ "'>" + item.name + "</option>";
						});
					}else{
						alert("加载组件失败");
					}
					initializeDocument("${componentType}","${_ComponentTypeName[componentType]}");
					if(dialog != null){
						dialog.height(800); 
						dialog.width(1000);
						//dialog.reset();     // 重置对话框位置
					}
				}
			});
			
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			
			if(dialog != null){
				data = dialog.data;
			}
			
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/*
			$("#name").formValidator({onFocus:"请输入组件名称(至少1位,最多32位)",onShow:"请输入名称",onCorrect:"符合要求"})
				.inputValidator({min:1,max:100,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写名称"})
				.regexValidator({regExp:"[_a-zA-Z][_a-zA-Z0-9]*",dataType:"string",onError:"名称只能包含字母、数字、下划线(以字母或下划线开头)"})
				.ajaxValidator({
					dataType : "json", 
					async : false, 
					url : "<%=basePath%>component/validateComponentNameInPage.do?dynamicPageId="+$("#dynamicPageId").val()+"&componentName="+$("#name").val()+"&componentId="+$("#pageId").val(),
					success :  function(data){
						return data;
					},
					onError : "该名称不可用，请更换名称"
				});*//**/
			//$("#dataItemCode").formValidator({onFocus:"请选择或输入数据源",onCorrect:"符合要求"})
			//	.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择或输入数据源"});
			$("#order").formValidator({onShow:"请输入序号",onFocus:"请输入顺序",onCorrect:"合法"})
				.regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
			$("#layoutId").formValidator({onFocus:"请选择布局",onCorrect:"符合要求"})
				.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择布局"});
		});
		
		
		
		
		//在更新时加载组件值；
		/*function loadComponentData(storeObject){
			var item = eval("("+storeObject.content+")");
			if(item.pageId != null){
				loadCommonComponentData(item);
			}
		}*/
		
		$("#addColumn").click(function(){
			
					var str="<tr id=dataTr'"+tagIndex+"'>";
						str+="<td><input name='tags[][tagsId]' style='width:100px;' id='tagsId-" + tagIndex + "' type=text" + "/>"+"</td>";
						str+="<td><input name='tags[][tagsTitle]' style='width:250px;' id='tagsTitle-" + tagIndex + "' type=text" + "/>"+"</td>";
						//str+="<td><select name='tags[][tagsType]' style='width:100px;' id='tagsType-" + index +"' class='form-control'><option value='1'>普通标签</option> <option value='2'>组标签</option> </select>"+"</td>";
						//str+="<td><input name='tags[][parentTags]' style='width:100px;' id='parentTags-" + index + "' type=text" + "/>"+"</td>";
						str+="<td><select name='tags[][relatePageId]' style='width:250px;' id='relatePageId-" + tagIndex + "' class='form-control pageSelect'>"+options+"<select></td>";
						//str+="<td><select name='tags[][isLoad]' style='width:100px;' id='isLoad-" + index + "' class='form-control'><option value='2'>否</option> <option value='1'>是</option>  </select>"+"</td>";
						str+="<td><input name='tags[][order]' style='width:100px;' id='order-" + tagIndex + "' type=text" + "/>"+"</td>";
						str+="<td><a href='javascript:void(0)' class='removeTr'>删除</a>"+"</td>";
						str+="</tr>";
						//index++;
						//alert(index);
					$("#tagsBody").append(str);
					
					$("#relatePageId-" + tagIndex).select2();
					tagIndex++;
					
		});
	
		
		/* $("body").on("click",".pageSelect",function(){
			var _this = $(this);
			_this.select2();
		}); */
		
		$("body").on("change",".pageSelect",function(){
			var _this = $(this);
			var value = _this.val();
			 $(".pageSelect").not(_this).find(":selected").each(function() {
        		//sum += Number($(this).val());
        		
        		if(_this != $(this)){
        			var temVal = $(this).val();
        			if(value==temVal){
        				alert("不支持不同tab选同一个表单");
        			}
        		}
        		
        		
    		 });
		});
	
		$("body").on("click",".removeTr",function(){
				var _this = $(this);
				//alert(_this.parents("tr").html());
				_this.parents("tr").remove();
		});
	</script>
</body>
</html>