<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<meta charset="utf-8">
		<link href="<%=basePath %>base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
		<link href="<%=basePath %>base/resources/zui/assets/datetimepicker/css/datetimepicker.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>base/resources/zui/dist/css/zui.css">
		<link href="../../base/resources/zui/dist/css/zui.min.css" rel="stylesheet">
		<link href="<%=basePath %>base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
		<link href="<%=basePath %>base/resources/zui/assets/datetimepicker/css/datetimepicker.min.css" rel="stylesheet">
		<link href="<%=basePath %>base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
		<link title="default" rel="stylesheet" type="text/css" href="<%=basePath %>base/resources/content/styles/szCloud_common.css" />
		<link title="default" rel="stylesheet" type="text/css" href="<%=basePath %>base/resources/content/styles/style.css" />
  		<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" language="javascript" type="text/javascript"></script>
		<script src="<%=basePath %>base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
		<script src="<%=basePath %>base/resources/artDialog/dist/dialog.js"></script>
		<script src="<%=basePath %>base/resources/artDialog/lib/require.js"></script>
		<script src="<%=basePath %>base/resources/artDialog/lib/sea.js"></script>
		<link rel="stylesheet" href="<%=basePath %>resources/plugins/tips/tip-green/tip-green.css" type="text/css" />
		<link rel="stylesheet" href="<%=basePath %>resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
		<script src="<%=basePath %>resources/plugins/tips/jquery.poshytip.js"></script>
		<script src="<%=basePath %>resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" type="text/javascript" charset="UTF-8"></script>
		<script src="<%=basePath %>resources/plugins/formValidator4.1.0/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
		<script type="text/javascript">
		
		</script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#backOperate").click(function(){
					location.href="../metaModel/queryResult.do?currentPage="+1;
				});
			});
			$(function(){
	   				$.formValidator.initConfig({formID:"creatForm",debug:false,onSuccess:function(){
					   	$("#creatForm").submit();
		    		},onError:function(){alert("错误，请看提示")}});
						$("#modelName").formValidator({onFocus:"请输入模型名称(中)",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
						$("#modelCode").formValidator({ onFocus:"请输入字母",onShow: "请输入字母", onCorrect: "符合要求" }).regexValidator({ regExp: "letter", dataType: "enum", onError: "字母格式不正确" }).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
						$("#tableName").formValidator({ onFocus:"请输入字母",onShow: "请输入字母", onCorrect: "符合要求" }).regexValidator({ regExp: "letter", dataType: "enum", onError: "字母格式不正确" }).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
						$("#projectName").formValidator({ onFocus:"请输入字母",onShow: "请输入字母", onCorrect: "符合要求" }).regexValidator({ regExp: "letter", dataType: "enum", onError: "字母格式不正确" }).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
	   				
				})
		</script>
		
</head>
<body class="C_formBody">
	<div>
		<div>
			<div style="margin-top: 10px; margin-left: 10px; width: 100%; float: left;">
				<form class="form-horizontal" action="../metaModel/save.do" method="post" id="creatForm">
						
					<div style="float: left;margin-left: 130px">
							<div class="C_btnGroup">
								<div class="C_btns">
									<button class="btn btn-info" type="submit" id="showItem">保存</button>
									<button class="btn btn-info" id="backOperate"><i class="icon-trash"></i>返回</button>
								</div>
							</div>
					</div>
					<div class="C_addForm">
						<legend class=" text-center"> 元数据编辑 </legend>
						<div class="form-group">
							<label class="col-md-1 control-label">模型名称(中)</label>
							<div class="col-md-4">
								<input type="hidden" name="maxPage" value="1">
								<input name="modelName" class="form-control" id="modelName" type="text" placeholder="" value="">
							</div>
							<label class="col-md-2 control-label">模型名称(英)</label>
							<div class="col-md-4">
								<input name="modelCode" class="form-control" id="modelCode" type="text" placeholder="" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">模型分类</label>
							<div class="col-md-4">
								 <select class="form-control" name="modelClassId" id="modelClassId">
									<c:forEach items="${class }" var="ls">
										<option value="${ls.id }">${ls.classCode }</option>
									</c:forEach>
								</select>
							</div>
							<label class="col-md-2 control-label">模型描述</label>
							<div class="col-md-4">
								 <input name="modelDesc" class="form-control" id="modelDesc" type="text" placeholder="" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">所属表名</label>
							<div class="col-md-4">
								 <input name="tableName" class="form-control" id="tableName" type="text" placeholder="" value="">
							</div>
							<label class="col-md-2 control-label">项目名称</label>
							<div class="col-md-4">
								 <input name="projectName" class="form-control" id="projectName" type="text" placeholder="" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">模型类型</label>
							<div class="col-md-4">
								 <select class="form-control" name="modelType" id="modelType">
							        <option value="1">系统模型</option>
							        <option value="2">领域模型</option>
							     </select>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>