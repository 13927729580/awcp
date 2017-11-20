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
 		<title>元数据页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../../resources/include/common_form_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--edit页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="dataform">表单区（--输入组--按钮组）</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="<%=basePath%>devAdmin/intoSystem.do?sysId=1">首页</a></li>
		          <li><a href="<%=basePath %>metaModel/selectPage.do">元数据管理</a></li>
		          
		          <li class="active">元数据属性编辑</li>
		        </ul>
			</div>
			<div class="row" id="dataform">
			<form class="form-horizontal" id="groupForm"
				action="metaModelItems/update.do" method="post">
				<legend>
					元数据属性编辑
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				<div class="form-group">
					<label class="col-md-2 control-label required">属性名称(中)</label>
					<div class="col-md-4">
						<input type="hidden" id="id" name="id" value="${vo.id}" />
						<input type="hidden" name="modelId" id="modelId" value="${vo.modelId }">
						<input name="itemName" class="form-control" id="itemName"
							type="text" placeholder="" value="${vo.itemName}">
					</div>
					<label class="col-md-2 control-label required">属性名称(英)</label>
					<div class="col-md-4">
						<input name="itemCode" class="form-control" id="itemCode"
							type="text" placeholder="" value="${vo.itemCode}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">属性类型</label>
					<div class="col-md-4">
						<select data-placeholder="请选择属性类型..." name="itemType" class="chosen-select form-control" tabindex="2">
									<option value="int">int</option>
									<option value="bigInt">bigInt</option>
									<option value="varchar">varchar</option>
									<option value="bool">bool</option>
									<option value="boolean">boolean</option>
									<option value="float">float</option>
									<option value="double">double</option>
									<option value="date">date</option>
									<option value="1">一对一</option>
									<option value="2">多对一</option>
						</select>
					</div>
					<label class="col-md-2 control-label">属性长度</label>
					<div class="col-md-4">
						<input name="itemLength" class="form-control" id="itemLength"
							type="text" placeholder="" value="${vo.itemLength}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">是否主键</label>
					<div class="col-md-4">
						<select disabled="disabled" data-placeholder="请选择是否主键..." name="usePrimaryKey" id="usePrimaryKeys" class="chosen-select form-control" tabindex="2">
							<option value="0">NO</option>
							<option value="1">YES</option>
						</select>
					</div>
					<label class="col-md-2 control-label required">是否索引</label>
					<div class="col-md-4">
						<select data-placeholder="请选择是否索引..." name="useIndex" class="chosen-select form-control" tabindex="2">
							<option value="0">无</option>
							<option value="-1">主键</option>
							<option value="1">唯一</option>
							<option value="2">普通</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">是否为空</label>
					<div class="col-md-4">
						<select data-placeholder="请选择模型分类..." class="chosen-select form-control" tabindex="2" name="useNull">
							<option value="0">NO</option>
							<option value="1">YES</option>
						</select>
					</div>
					<label class="col-md-2 control-label">默认值</label>
					<div class="col-md-4">
						<input name="defaultValue" class="form-control" id="defaultValue"
							type="text" placeholder="" value="${vo.defaultValue}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">备注</label>
					<div class="col-md-4">
						<input name="remark" class="form-control" id="remark"
							type="text" placeholder="" value="${vo.remark}">
					</div>
				</div>
				
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
						<a href="<%=basePath %>metaModelItems/queryResultByParams.do?id=${vo.modelId }" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		        </div>
			</form>
			</div>
		</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
			$(document).ready(function(){
				
				$("select[name='useIndex']").val('${vo.useIndex}');
				$("select[name='useNull']").val('${vo.useNull}');
				$("select[name='itemType']").val('${vo.itemType}');
				if('${vo.usePrimaryKey}'==1){
					$("#usePrimaryKeys").val('1');
					$("#usePrimaryKeys").attr("disabled",false);
				}else{
					$("#usePrimaryKeys").val('0');
				}
				
				$("#selectModel").find("option[value='"+$("#modelId").val()+"']").attr("selected",true); 
				
			});
         </script>	
	</body>
</html>