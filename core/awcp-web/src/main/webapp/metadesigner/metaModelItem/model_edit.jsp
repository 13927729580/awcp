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
		<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" type="text/javascript"></script>
		<script src="<%=basePath %>resources/plugins/tips/jquery.poshytip.js"></script>
		<script src="<%=basePath %>resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" type="text/javascript" charset="UTF-8"></script>
		<script src="<%=basePath %>resources/plugins/formValidator4.1.0/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
		<script type="text/javascript">
		
		$(function(){
   			$.formValidator.initConfig({formID:"creatForm",debug:false,onSuccess:function(){
			   	$("#creatForm").submit();
    		},onError:function(){alert("错误，请看提示")}});
				$("#modelName").formValidator({onFocus:"请输入模型名称(中)",onFocus:"至少1位",onCorrect:"符合要求"}).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
				$("#modelCode").formValidator({ onFocus:"请输入字母",onShow: "请输入字母", onCorrect: "符合要求" }).regexValidator({ regExp: "letter", dataType: "enum", onError: "字母格式不正确" }).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
				$("#tableName").formValidator({ onFocus:"请输入字母",onShow: "请输入字母", onCorrect: "符合要求" }).regexValidator({ regExp: "letter", dataType: "enum", onError: "字母格式不正确" }).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
				$("#projectName").formValidator({ onFocus:"请输入字母",onShow: "请输入字母", onCorrect: "符合要求" }).regexValidator({ regExp: "letter", dataType: "enum", onError: "字母格式不正确" }).inputValidator({min:1,max:20,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"必填"});
			})
			$(document).ready(function() {
				$("#modelIds").hide();
				$("#itemType").change(function(){
					var d=$("#itemType").val();
					if(d=='1' || d=='2'){
						$("#modelIds").show();
						$("#selectLength").hide();
					}
					else if(!(d=="varchar" || d=="dicimal") && !(d=='1' || d=='2')){
						$("#itemLength").attr("disabled",true);
						$("#modelIds").hide();
					}else{
						$("#itemLength").attr("disabled",false);
						$("modelIds").hide();
					}
				});
								$("#addModel").css("display", "none");
								$("#showDialog")
										.click(
												function() {
													$("#addModel").css(
															"display", "block");
													var d = dialog({
														id : "N9007",
														title : "元数据属性",
														content : document
																.getElementById("addModel"),
														width : 500
													});
													d.show();
													
												});
								
								$("#save").click(function() {
													var itemName = $("#itemName").val();
													var itemCode = $("#itemCode").val();
													var itemType = $("#itemType").val();
													var modelIdss = $("#modelIdss").val();
													var itemLength = $("#itemLength").val();
													var usePrimaryKey = $('input[name="usePrimaryKey"]:checked').val();
													var useIndex = $('input[name="useIndex"]:checked').val();
													var useNull = $('input[name="useNull"]:checked').val();
													var defaultValue = $("#defaultValue").val();
													var remark = $("#remark").val();
													var modelId = $("#modelId").val();
													$.ajax({
																type : "post",
																url : "../metaModelItems/save.do?a="+Math.random(),
																data : {
																	"itemName" : itemName,
																	"itemCode" : itemCode,
																	"modelId" : modelId,
																	"itemType" : itemType,
																	"itemLength" : itemLength,
																	"usePrimaryKey" : usePrimaryKey,
																	"useNull" : useNull,
																	"useIndex" : useIndex,
																	"defaultValue" : defaultValue,
																	"remark" : remark,
																	"modelIdss" : modelIdss
																},
																datatype : "json",
																success : function(date) {
																	var obj = eval('('+ date+ ')');
																	$("#str").append('<tr class="text-center"><td><input name="boxs" type="checkbox" value="0"></td>'
																							+ '<td>'
																							+ obj.itemName
																							+ '</td>'
																							+ '<td>'
																							+ obj.itemCode
																							+ '</td>'
																							+ '<td>'
																							+ obj.itemType
																							+ '</td>'
																							+ '<td>'
																							+ obj.itemLength
																							+ '</td>'
																							+ '<td>'
																							+ obj.itemName
																							+ '</td>'
																							+ '<td>'
																							+ obj.itemName
																							+ '</td>'
																							+ '<td>'
																							+ obj.itemName
																							+ '</td>'
																							+ '<td>'
																							+ obj.itemName
																							+ '</td>'
																							+ '</tr>');
																	$("#itemName").attr("value","");
																	$("#modelClose").click();
																}
															});

												});
								$("#backOperate").click(function(){
									location.href="../metaModel/selectPage.do?currentPage="+1;
								});

							});
		</script>
</head>
<body class="C_formBody">
	<div>
	<div><input type="hidden" name="modelId" id="modelId" value="${model.id }"></div>
		<div>
			<div style="margin-top: 10px; margin-left: 10px; width: 100%; float: left;">
				<form class="form-horizontal" action="../metaModel/update.do" method="post" id="creatForm">
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
								<input type="hidden" name="id" value="${model.id }">
								<input type="hidden" name="maxPage" value="1">
								<input name="modelName" class="form-control" id="modelName" type="text" placeholder="" value="${model.modelName }">
							</div>
							<label class="col-md-2 control-label">模型名称(英)</label>
							<div class="col-md-4">
								<input name="modelCode" class="form-control" id="modelCode" type="text" placeholder="" value="${model.modelCode }">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">模型分类</label>
							<div class="col-md-4">
								 <input name="modelClassId" class="form-control" id="modelClassId" type="text" placeholder="" value="${model.modelClassId }">
							</div>
							<label class="col-md-2 control-label">模型描述</label>
							<div class="col-md-4">
								 <input name="modelDesc" class="form-control" id="modelDesc" type="text" placeholder="" value="${model.modelDesc }">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">所属表名</label>
							<div class="col-md-4">
								 <input name="tableName" class="form-control" id="tableName" type="text" placeholder="" value="${model.tableName }">
							</div>
							<label class="col-md-2 control-label">项目名称</label>
							<div class="col-md-4">
								 <input name="projectName" class="form-control" id="projectName" type="text" placeholder="" value="${model.projectName }">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-1 control-label">模型类型</label>
							<div class="col-md-4">
								 <input name="modelType" class="form-control" id="modelType" type="text" placeholder="" value="${model.modelType }">
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!-- 页面展示 -->
		<div class="C_tabBox clearfix">
	   <div class="C_tabList">
	   	<div><button class="btn btn-success" style="float: left;margin-left: 130px" id="showDialog"><i class="icon-plus-sign"></i>添加属性</button></div><!--  data-toggle="modal" data-target="#addModal" -->
	     <form  method="post" id="userList">
			<table class="table table-condensed table-hover table-striped tablesorter table-fixed" id="tasktable">
	        <thead>   						
	        	<script type="text/javascript">
		                var checkflag = "false";
						function check(fieldName) {
						var field=document.getElementsByName(fieldName);
						if (checkflag == "false") {
							for (i = 0; i < field.length; i++) {
							field[i].checked = true;}
							checkflag = "true";
							return "Uncheck All"; }
						else {
							for (i = 0; i < field.length; i++) {
							field[i].checked = false; }
							checkflag = "false";
							return "Check All"; }
						}
		         </script>
	          <tr class="text-center">
	            <th width="10%" class="text-center"><input type="checkbox" value="" style="margin-top: -3px;"onclick="this.value=check('boxs')"></th>
	            <th width="10%" class="text-center"><div class="header"><a href="javascript:void(0)">属性名称(中)</a></div></th>
				<th width="10%" class="text-center"><div class="header"><a href="javascript:void(0)">属性名称(英)</a></div></th>
				<th width="10%" class="text-center"><div class="header"><a href="javascript:void(0)">属性类型</a></div></th>
				<th width="10%" class="text-center"><div class="header"><a href="javascript:void(0)">属性长度</a></div></th>
				<th width="10%" class="text-center"><div class="header"><a href="javascript:void(0)">是否主键</a></div></th>
				<th width="10%" class="text-center"><div class="header"><a href="javascript:void(0)">是否有索引</a></div></th>
				<th width="10%" class="text-center"><div class="header"><a href="javascript:void(0)">默认值</a></div></th>
				<th width="20%" class="text-center"><div class="header"><a href="javascript:void(0)">备注</a></div></th>
	          </tr>
	        </thead>
	        <tbody id="str">
							
	        </tbody>
	        <tfoot>
          </tfoot>
	      </table>
	       </form>
	       
	  	 </div>
		</div>
		<!-- 弹窗 
		<form class="form-horizontal" role="form" method="post" id="addModel">
			<div class="form-group">
				<input type="hidden" name="modelId" value="${model.id }"> <label
					class="col-md-3 control-label required">字段名称(中)</label>
				<div class="col-md-9 has-error">
					<input name="itemName" id="itemName" class="form-control" type="text"
						placeholder="" value="">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">字段名称(英)</label>
				<div class="col-md-9">
					<input name="itemCode" class="form-control" id="itemCode" type="text"
						placeholder="" value="">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">字段类型</label>
				<div class="col-md-9">
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
			</div>
			<div class="form-group" id="modelIds">
				<label class="col-md-3 control-label">表关系</label>
				<div class="col-md-9">
					<select class="form-control" name="modelIdss" id="modelIdss">
						<option value="0">-- 请选择 --</option>
						<c:forEach items="${metaModel }" var="l">
							<option value="${l.id }">${l.tableName }</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">字段长度</label>
				<div class="col-md-9">
					<input name="itemLength" class="form-control" id="itemLength"
						type="text" placeholder="" value="">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">是否有主键</label>
				<div class="col-md-9">
					<input name="usePrimaryKey" type="radio" value="1"
						style="margin-top: -2px;">&nbsp;是 <input
						name="usePrimaryKey" type="radio" value="0" checked="checked"
						style="margin-left: 20px; margin-top: -2px;">&nbsp;否
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">是否有索引</label>
				<div class="col-md-9">
					<input name="useIndex" type="radio" value="1"
						style="margin-top: -2px;">&nbsp;是 <input name="useIndex"
						type="radio" value="0" checked="checked"
						style="margin-left: 20px; margin-top: -2px;">&nbsp;否
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">是否为空</label>
				<div class="col-md-9">
					<input name="useNull" type="radio" value="1"
						style="margin-top: -2px;">&nbsp;是 <input name="useNull"
						type="radio" value="0" checked="checked"
						style="margin-left: 20px; margin-top: -2px;">&nbsp;否
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">默认值</label>
				<div class="col-md-9">
					<input name="defaultValue" class="form-control" id="defaultValue"
						type="text" placeholder="" value="">
				</div>
			</div>
			<div class="form-group">
				<label class="col-md-3 control-label">备注</label>
				<div class="col-md-9">
					<input name="remark" class="form-control" id="remark" type="text"
						placeholder="" value="">
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" id="modelClose" onclick="dialog.list['N9007'].close()">关闭</button>
				<button type="button" class="btn btn-primary" id="save">保存</button>
			</div>
		</form>
		-->
	</div>
</body>
</html>