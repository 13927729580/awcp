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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
		<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" type="text/javascript"></script>
		<script src="<%=basePath %>resources/plugins/tips/jquery.poshytip.js"></script>
		<script src="<%=basePath %>resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" type="text/javascript" charset="UTF-8"></script>
		<script src="<%=basePath %>resources/plugins/formValidator4.1.0/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
		<script type="text/javascript">
			$(function(){
   			$.formValidator.initConfig({formID:"creatForm",debug:false,onSuccess:function(){
			   	$("#creatForm").submit();
    		},onError:function(){alert("错误，请看提示")}});
				$("#itemName").formValidator({onFocus:"请输入属性名称",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
				$("#itemCode").formValidator({onFocus:"请输入属性名称",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
				$("#itemType").formValidator({onFocus:"请输入属性类型",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
			})
			
			$(document).ready(function(){
				$("#addRelation").hide();
				$("#itemType").change(function(){
					var d=$("#itemType").val();
					if(d=='1' || d=='2'){
						$("#addRelation").show();
						$("#selectLength").hide();
					}
					else if(!(d=="varchar" || d=="dicimal")){
						$("#addRelation").hide();
						$("#selectLength").show();
						$("#itemLength").attr("disabled",true);
					}else{
						$("#addRelation").hide();
						$("#selectLength").show();
						$("#itemLength").attr("disabled",false);
					}
				});
			});
		</script>
</head>
<body class="C-formBody">
	<div>
		<div>
			<div style="margin-top: 10px; margin-left: 10px; width: 100%; float: left;">
				<form class="form-horizontal" action="saves.do" method="post" id="creatForm">
					<div style="float: left;margin-left: 130px">
						<div class="C_btnGroup">
							<div class="C_btns">
								<button class="btn btn-info" type="submit" id="showItem">保存</button>
							</div>
						</div>
					</div>
					<div class="C_addForm">
						<legend class=" text-center"> 元数据模型属性编辑 </legend>
						<div class="form-group">
							<label class="col-md-1 control-label">字段名称(中)</label>
							<div class="col-md-4">
								<input type="hidden" name="a" value="1.201">
								<input type="hidden" name="maxPage" value="1">
								<input type="hidden" name="modelId" value="${modelId }">
								<input name="itemName" class="form-control" id="itemName" type="text" placeholder="" value="">
							</div>
							<label class="col-md-2 control-label">字段名称(英)</label>
							<div class="col-md-4">
								<input name="itemCode" class="form-control" id="itemCode" type="text" placeholder="" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">字段类型</label>
							<div class="col-md-4">
								 <select class="form-control" name="itemType" id="itemType">
							        <option value="0">--请选择--</option>
									<option value="int">int</option>
									<option value="bigInt">bigInt</option>
									<option value="varchar">varchar</option>
									<option value="bool">bool</option>
									<option value="boolean">boolean</option>
									<option value="decimal">decimal</option>
									<option value="float">float</option>
									<option value="double">double</option>
									<option value="date">date</option>
									<option value="1">一对一</option>
									<option value="2">多对一</option>
							      </select> 
							</div>
							<div id="selectLength">
								<label class="col-md-2 control-label">字段长度</label>
								<div class="col-md-4">
									 <input name="itemLength" class="form-control" id="itemLength" type="text" placeholder="" value="">
								</div>
							</div>
							<div id="addRelation">
								<label class="col-md-2 control-label">表关系</label>
								<div class="col-md-4">
									<select class="form-control" name="modelIdss">
										<option value="0">-- 请选择 --</option>
										<c:forEach items="${metaModel }" var="l">
											<option value="${l.id }">${l.tableName }</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-1 control-label">是否主键</label>
							<div class="col-md-4">
								 <input name="usePrimaryKey" type="radio" value="1"
									style="margin-top: -2px;">&nbsp;是 <input
									name="usePrimaryKey" type="radio" value="0" checked="checked"
									style="margin-left: 20px; margin-top: -2px;">&nbsp;否
							</div>
							<label class="col-md-2 control-label">是否索引</label>
							<div class="col-md-4">
								 <input name="useIndex" type="radio" value="1"
									style="margin-top: -2px;">&nbsp;是 <input name="useIndex"
									type="radio" value="0" checked="checked"
									style="margin-left: 20px; margin-top: -2px;">&nbsp;否
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-1 control-label">是否为空</label>
							<div class="col-md-4">
								 <input name="useNull" type="radio" value="1"
									style="margin-top: -2px;">&nbsp;是 <input name="useNull"
									type="radio" value="0" checked="checked"
									style="margin-left: 20px; margin-top: -2px;">&nbsp;否
							</div>
							<label class="col-md-2 control-label">默认值</label>
							<div class="col-md-4">
								 <input name="defaultValue" class="form-control" id="defaultValue" type="text" placeholder="" value="">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">备注</label>
							<div class="col-md-4">
								<input name="remark" class="form-control" id="remark" type="text" placeholder="" value="">
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>