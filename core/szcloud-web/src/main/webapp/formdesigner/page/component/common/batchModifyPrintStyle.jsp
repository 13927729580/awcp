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
			<button type="button" class="btn btn-success" id="batchModifyPrintStyle"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
		
			<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
			
<div class="form-group">
	<label class="col-md-2" title="宋体,黑体等">字体名称</label>	
	<div class="col-md-4">
		<select name="fontfamily" class="form-control" id="fontfamily">
			<option value="" selected="selected">请选择</option>
			<option value="宋体">宋体</option>
			<option value="黑体">黑体</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">字体尺寸</label>	
	<div class="col-md-4">
		<select name="fontsize" class="form-control" id="fontsize">
			<option value="" selected="selected">请选择</option>
			<option value="44">初号</option>
			<option value="36">小初</option>			
			<option value="28">28</option>			
			<option value="26">一号</option>			
			<option value="24">小一</option>			
			<option value="22">二号</option>			
			<option value="20">20</option>			
			<option value="18">小二</option>			
			<option value="16">三号</option>			
			<option value="15">小三</option>			
			<option value="14">四号</option>			
			<option value="12">小四</option>			
			<option value="10.5">五号</option>			
			<option value="9">小五</option>			
			<option value="7.5">六号</option>			
			<option value="6.5">小六</option>			
			<option value="5.5">七号</option>			
			<option value="5">八号</option>			
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="">文字方向</label>	
	<div class="col-md-4">
		
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">字体颜色</label>	
	<div class="col-md-4">
		<select name="fontcolor" class="form-control" id="fontcolor">
			<option value="" selected="selected">请选择</option>
			<option value="black">黑色</option>	
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">字体背景颜色</label>	
	<div class="col-md-4">
		<select name="backgroundcolor" class="form-control" id="backgroundcolor">
			<option value="" selected="selected">请选择</option>
			<option value="white">白色</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="[常规|加粗、斜体、下划线、删除线]、[上标|下标]">字体样式</label>	
	<div class="col-md-4">
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="weight">加粗</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="italic">斜体</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="linethrough">删除线</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="sup">上标</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="sub">下标</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="underline">下划线</label>
		<!-- <select name="text-style-decoration" id="text-style-decoration">
			
		</select> -->
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="[居左|居中|居右]、[垂直居中|顶端对齐|底端对齐]">对齐方式</label>	
	<div class="col-md-4">
		<select name="textalign" class="form-control" id="textalign">
			<option value="" selected="selected">请选择</option>
			<option value="居左">居左</option>
			<option value="居中">居中</option>
			<option value="居右">居右</option>
		</select>
		<select name="textverticalalign" class="form-control" id="textverticalalign">
			<option value="" selected="selected">请选择</option>
			<option value="垂直居中">垂直居中</option>
			<option value="顶端对齐">顶端对齐</option>
			<option value="底端对齐">底端对齐</option>			
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="多行文本框中有此属性：缩进、间距(段前，段后，)、换行、分页">行和段落间距</label>	
	<div class="col-md-4">
		<div class="input-group">
			<span class="input-group-addon">缩进</span>
			<input type="text" class="form-control" name="textindent" id="textindent" value="">
			<span class="input-group-addon">pt</span>
			<span class="input-group-addon">行间距</span>
			<input type="text" class="form-control" name="lineheight" id="lineheight" value="">
			<span class="input-group-addon">倍</span>
		</div>
	</div>
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
			var data = null;
			
			
			var _selects = null;
			
			if(dialog != null){
				data = dialog.data;
				if(data != null && data._selects)
					_selects = data._selects;			//选择的组件id串
			}
			if(data.dynamicPageId && $("#dynamicPageId")){
				$("#dynamicPageId").val(data.dynamicPageId);
			}
			
			
			
			
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/**/
			//$("#style").formValidator({onFocus:"请输入样式",onCorrect:"符合要求"})
			//.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请输入样式"});
			
			
			$("#batchModifyPrintStyle").click(function(){
				if($.formValidator.pageIsValid('1')){
					var formJson = $("#componentForm").s
					
					var validatorJson=new Array();
					var validator = $("#select_validator").select2("data");
					if(validator != null){
						$.each(validator, function(index, item) {
							validatorJson.push(item.id);
						});
					}
					
					var dynamicPageId = $("#dynamicPageId").val();
					alert(validator[0].id);
					$.ajax({
						type: "POST",
						url: basePath + "component/batchModifyPrintStyle.do",
						data: "validator="+validatorJson.join(",") +
						"&_selects="+_selects+"&dynamicPageId=" + dynamicPageId,
						async : false,
						success: function(data){
							if("1" == data){	
								alert("修改成功")
								if(dialog != null){
									dialog.close(data);
									dialog.remove();
								}
							}else{
								alert("修改失败");
							}
						}
					});	
				}
				return false;
			});
			
		});
		
		$("#cancelComponent").click(function(){
				if(dialog != null){
					dialog.close();
					dialog.remove();
				}
			});
		
		
	
	</script>
	
	
	<script>	
		$(function(){
			var basePath = "<%=basePath%>";
			var validatorArray = new Array();
			$.ajax({
			   type: "POST",
			   async:false,
			   url: basePath + "fd/validator/getValidatorList.do",
			   success: function(data){
					if(data != null){
						$.each(data, function(index, item) {
							validatorArray.push({id: item.id, text: item.name});
						});
					}else{
						alert("加载组件失败");
					}
			   }
			});	
			var element  = [{id: 1, text: 2},{id: 3, text: 4}];
      	 	if($.fn.select2){
      		 $("#select_validator").select2({ 
      			 //tags:["P:产品经理","D:开发甲","D:开发乙","D:开发丙","D:开发丁"],
      			 tags:	validatorArray,
      			 placeholder: "请选择页面",
      			 tokenSeparators: [",", " "]
      		 });
      		}
       })
	</script>



