<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
	<title>数据源-领域模型</title>
	<%@ include file="/resources/include/common_form_css.jsp" %>
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="buttons">
			<button class="btn btn-success" id="saveBtn" ><i class="icon-save"></i>保存</button>
			<button class="btn btn-warning" id="cancelBtn"><i class="icon-remove"></i>取消</button>
		</div>
		<div class="row" id="dataform">
			<div class="alert alert-info">提示：<br>如果是确定的记录条数，则选择“多行数据”、“不分页”、设置固定记录即可<br>请不要在sql语句中写入limit等分页字样</div>
			<form class="form-horizontal"  id="datasourceForm"  >
			 	<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
			 	<input type="hidden" name="id" value="" id="id"/>	
				<div class="form-group">
					<label class="col-md-1 control-label required">别名：</label>
					<div class="col-md-2">
						<input type='text' name='name'    id='name' value="" class='form-control' />
					</div>
					<label class="col-md-1 control-label required">数据记录</label>
					<div class="col-md-2">
						<select class="form-control" name="isSingle" id = "isSingle">
							<option value="0">单行数据</option>
							<option value="1">多行数据</option>
						</select>
					</div>
					<label class="col-md-1 control-label required">分页</label>
					<div class="col-md-2">
						<select class="form-control" name="isPage" id = "isPage">
							<option value="0">不分页</option>
							<option value="1">分页</option>
						</select>
					</div>
					<label class="col-md-1 control-label required">固定记录</label>
					<div class="col-md-2">
						<input type='text' name='limitCount' id='limitCount' value="0" class='form-control' />
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">描述：</label>
					<div class="col-md-11">
						<input name='description' id='description' class='form-control'/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">SQL语句</label>
					<div class="col-md-11">
						<textarea rows="8"  name="sqlScript" id="sqlScript" class='form-control'></textarea>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-1 control-label">DELETE SQL语句</label>
					<div class="col-md-11">
						<textarea rows="3"  name="deleteSql" id="deleteSql" class='form-control'></textarea>
					</div>
				</div>
		        <div class="form-group" id="modelSelect" >
					<label class="col-md-1 control-label">选择模型</label>
					<div class="col-md-5">
						<select  id="modelCode" class="form-control"   name="modelCode">
							<option value="">-----------请选择模型------------</option>
						</select>
					</div>
				</div>	
			</form>
			<form class="form-horizontal"  id="dataitemForm"  >
				<div  contenteditable="false">
			    	<table class="table table-bordered">
			            <thead>
				        	<tr>
				                <th width="50"><input type="checkbox" id="checkAllItem"/></th>
				                <th>名称（中文）</th>
				                <th>名称（英文）</th>
				                <th width="70">类型</th>
				                <th width="70">长度</th>
				                <th width="70">默认值</th>
				                <th width="70">不允许空</th>
				            </tr>
			            </thead>
			            <tbody id="modelitemtable">
			            </tbody>
			    	</table>
		        </div>
	        </form>
		</div>
	</div>
	<%@ include file="/resources/include/common_form_js.jsp" %>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/json2.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>resources/scripts/map.js"></script>
	<script type="text/javascript" src="<%=basePath %>formdesigner/page/script/data.js"></script>
	<script type="text/javascript">
		//key modelCode; value tableName
		var models = new Map();
		
		//勾选模型属性时触发
		function changeItemMap(ched){
			if($(ched).prop("checked")){
				itemMap.put($(ched).val(),true);
			}else{
				itemMap.remove($(ched).val());
			}
			freshSQL();
		}
		
		//初始化一行模型属性数据
		function initItemRow(item, flag){
			$("#modelitemtable").append("<tr>");
			//是否为选择状态
			if(flag){
				$("#modelitemtable").append("<td><input type='checkbox' checked='checked' name='item' onclick='changeItemMap(this)' value='"+item.itemCode+"'></td>");
			} else {
				$("#modelitemtable").append("<td><input type='checkbox' name='item' onclick='changeItemMap(this)' value='"+item.itemCode+"'></td>");
			}
			if(item.itemName){
				$("#modelitemtable").append("<td>"+item.itemName+"</td>");
			} else {
				$("#modelitemtable").append("<td></td>");
			}
			if(item.itemCode){
				$("#modelitemtable").append("<td>"+item.itemCode+"</td>");
			} else {
				$("#modelitemtable").append("<td></td>");
			}
			if(item.itemType){
				$("#modelitemtable").append("<td>"+item.itemType+"</td>");
			} else {
				$("#modelitemtable").append("<td></td>");
			}
			if(item.itemLength){
				$("#modelitemtable").append("<td>"+item.itemLength+"</td>");
			} else {
				$("#modelitemtable").append("<td></td>");
			}
			if(item.defaultValue){
				$("#modelitemtable").append("<td>"+item.defaultValue+"</td>");
			} else {
				$("#modelitemtable").append("<td></td>");
			}
			if(item.useNull){
				$("#modelitemtable").append("<td>是</td>");
			} else {
				$("#modelitemtable").append("<td></td>");
			}
			$("#modelitemtable").append("</tr>");
		}
		
		//初始化模型属性
		function initModelItem(obj){
			if($("#modelCode").val()){
				$.ajax({
					type : "POST",
					url : "<%=basePath%>metaModel/queryModelItemByModel.do",
					data : {"modelCode" : $("#modelCode").val()}, 
					success : function(data){
						$("#modelitemtable").empty();
						itemMap.clear();
						var json = eval(data);
						var modelItemCodes = new Array();
						if(!empty(obj)){
							if(obj.modelItemCodes) {
								modelItemCodes = obj.modelItemCodes.split(",");
							}
						}
						
						$.each(json,function(idx,item){
							if($.inArray(item.itemCode, modelItemCodes) > -1){
								itemMap.put(item.itemCode, true);
								initItemRow(item, true);
							} else {
								initItemRow(item, false);
							}
						});
					}
				});
			}
		}
		
		/**
		 * 初始化选择模型列表，如果是编辑，则将标记指定的模型为选择状态
		 * 页面打开时只加载一次，后续可能会加上分页和搜索的功能
		 * //TODO 
		 **/
		function initModel(modelCode){
			$.ajax({
				type:"GET",
				async:false,
				url:"<%=basePath%>metaModel/queryPageByModel.do",
				success:function(data){
					var json = eval(data);
					$.each(json, function(idx, item){
						models.put(item.modelCode,item.tableName);
						if(item.modelCode==modelCode){
							$("#modelCode").append("<option value='"+item.modelCode+"' selected='selected'>"+item.modelName+"</option>");
						}else{
							$("#modelCode").append("<option value='"+item.modelCode+"'>"+item.modelName+"</option>");
						}
					});
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					//alert(errorThrown);
				}
			});
		}		
		
		$(document).ready(function(){
		   	$.formValidator.initConfig({
		   		formID:"dataform",
		   		debug:false,
		   		onError:function(){
		   			//alert("请按提示正确填写内容");
		   		}
		   	});
		   	//alert(4);
		    $("#name").formValidator({onShow:"请输入模型别名名称,不超过40字符",onCorrect:"符合格式要求"})
				.inputValidator({min:1,max:40,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写名称"});
	       	try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				//return;
			}
			//通过dialog获取数据，如果有，则是编辑状态，否则是新增状态
			var model = dialog.data;
			if(!empty(model)) {
				var modelCode  = model.modelCode;
				$("#name").val(model.name);
				$("#description").val(model.description);
				$("#id").val(model.id);
				if(model.isPage) {
					$("#isPage").val(model.isPage).trigger("change");
				}				
				if(model.isSingle) {
					$("#isSingle").val(model.isSingle).trigger("change");
				}
				if(model.limitCount) {
					$("#limitCount").val(model.limitCount);
				}
				$("#sqlScript").val(model.sqlScript);
				
				$("#deleteSql").val(model.deleteSql);
				
				initModel(modelCode);
				//初始化模型属性
				initModelItem(model);				
			} else{ 
				$("#id").val(guidGenerator());
				initModel();
			}
			if(dialog){
				dialog.height($(document).height());
				dialog.reset();
			}
			
			$("#modelCode").select2();
			$("#checkAllItem").click(function(){
				if($(this).prop("checked")){
					$(":checkbox[name='item']").prop("checked",true);
					$(":checkbox[name='item']").each(function(idx,item){
						itemMap.put($(item).val(),true);
					});
				}else{
					$(":checkbox[name='item']").prop("checked",false);
					$(":checkbox[name='item']").each(function(idx,item){
						itemMap.remove($(item).val());
					});
				}
				freshSQL();
			});
			//onchange
			$("#modelCode").change(function(){
				itemMap.clear();
				$("#modelitemtable").empty();
				sqlTableName = models.get($("#modelCode").val());
				initModelItem(model);
				if(dialog){
					dialog.height($(document).height());
					dialog.reset();
				}
			});
			//保存
			$("#saveBtn").click(function(){
				//alert($.formValidator.pageIsValid('1'));
				if($.formValidator.pageIsValid('1')){
					var formData = $("#datasourceForm").serializeJSON();
					var codes=[];
					$(":checkbox[name='item']").each(function(){
						//alert($(this).prop("id") + " : " + $(this).prop("checked"));
						if($(this).prop("checked")){
							codes.push($(this).val());
						}
					});
					if(codes.length > 0){
						formData.modelItemCodes = codes.join(",");
						//alert(formData.modelItemCodes);
						dialog.close(formData);
						dialog.remove();
					} else {
						alert("请选择模型属性！");
					}
				} else {
					alert("请按提示正确填写内容");
				}
			});
			//取消
			$("#cancelBtn").click(function(){
				dialog.remove();
			});
        });
	</script>	
</body>
</html>
