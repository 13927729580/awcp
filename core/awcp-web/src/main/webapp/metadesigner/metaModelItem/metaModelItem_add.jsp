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
			<form class="form-horizontal" id="groupForm" action="metaModelItems/save.do" method="post">
				<legend>
					元数据属性编辑
					<c:if test="${result!=null&&''!=result}">
						<span style="color: red">(${result})</span>
					</c:if>
				</legend>
				<div class="form-group">
					<label class="col-md-2 control-label required">属性名称(中)</label>
					<div class="col-md-4">
						<input type="hidden" id="type" value="${type }">
						<input type="hidden" id="modelId" name="modelId" value="${modelId }">
						<input name="itemName" class="form-control" id="itemName"
							type="text" placeholder="">
					</div>
					<label class="col-md-2 control-label required">属性名称(英)</label>
					<div class="col-md-4">
						<input name="itemCode" class="form-control" id="itemCode"
							type="text" placeholder="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">属性类型</label>
					<div class="col-md-4">
						<select data-placeholder="请选择属性类型..." name="itemType" class="chosen-select form-control" tabindex="2">
							<option value="int">int</option>
							<option value="bigint">bigint</option>
							<option value="float">float</option>
							<option value="double">double</option>
							<option value="varchar">varchar</option>
							<option value="char">char</option>
							<option value="text">text</option>
							<option value="date">date</option>
							<option value="datetime">datetime</option>
						</select>
					</div>
					<label class="col-md-2 control-label">属性长度</label>
					<div class="col-md-4">
						<input name="itemLength" class="form-control" id="itemLength"
							type="text" placeholder="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">是否主键</label>
					<div class="col-md-4">
						<select data-placeholder="请选择是否主键..." name="usePrimaryKey" id="usePrimaryKeys" class="chosen-select form-control" tabindex="2">
							<option value="0" selected="selected">NO</option>
							<option value="1">YES</option>
						</select>
					</div>
					<label class="col-md-2 control-label required">是否索引</label>
					<div class="col-md-4">
						<select data-placeholder="请选择是否索引..." name="useIndex" class="chosen-select form-control" tabindex="2">
							<option value="0" selected="selected">无</option>
							<option value="-1">主键</option>
							<option value="1">唯一</option>
							<option value="2">普通</option>
						</select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">是否为空</label>
					<div class="col-md-4">
						<select data-placeholder="请选择是否为空..." id="useNull" class="chosen-select form-control" tabindex="2" name="useNull">
							<option value="0" id="un1">NO</option>
							<option value="1" id="un2" selected="selected">YES</option>
						</select>
					</div>
					<label class="col-md-2 control-label">默认值</label>
					<div class="col-md-4">
						<input name="defaultValue" class="form-control" id="defaultValue"
							type="text" placeholder="">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">备注</label>
					<div class="col-md-4">
						<input name="remark" class="form-control" id="remark"
							type="text" placeholder="">
					</div>
				</div>
				
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
						<a class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</a>
						<a href="<%=basePath %>metaModelItems/queryResultByParams.do?id=${modelId }" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
		            </div>
		        </div>
			</form>
			</div>
		</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript">
       		$(document).ready(function(){
       			if('${type}'=='yes'){
       				$("#usePrimaryKeys").attr("disabled","true");
       			}
       			
       			$("#usePrimaryKey").change(function(){
       				if($(this).val()==1){
       					$("#un1").attr("selected","selected");
           				$("#useNull").attr("disabled","false");
       				}else{
       					$("#useNull").removeAttr("disabled");
       				}
       			});
       			
       		});
		
		
		$(function(){
		    $("#saveBtn").on("click",function(){
                var itemType=$("select[name='itemType']").val();
                var itemLength=$.trim($("input[name='itemLength']").val());
                if(itemType!="date"&&itemType!="datetime"&&itemType!="text"&&itemLength.length==0){
                    alertMessage(itemType+"类型必须指定长度");
                    return false;
                }
                $("#groupForm").submit();

			})
		   $.formValidator.initConfig({formID:"groupForm",debug:false,onError:function(){alertMessage("请按提示正确填写内容");}});
		  	$("#itemCode").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[a-zA-Z][a-zA-Z0-9_]*$",onError:"输入格式不正确"}).ajaxValidator({
		  		type:"get",
		  		url:"metaModelItems/dataValidate.do",
		  		data:{"modelId":$("#modelId").val(),"id":$("#id").val()},
		  		datatype:"json",
		  		success:function(data){
		  			if(data==0){
		  				return true;
		  			}else{
		  				return false;
		  			}
		  		},
		  		buttons:$("saveBtn"),
		  		error: function () { alertMessage("元数据属性已经存在，请重新输入"); },
                onerror: "请不要重复添加",
                onwait: "正在验证是否重复,请稍候..."
		  	});
		  	$("#itemLength").formValidator({empty:true,onShow:"请输入正整数"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
		  	$("#itemType").formValidator({onshow:"（必填）",onfocus:"（必填）请选择选项",oncorrect: "（正确）"}).inputValidator({min:0,onerror:"请选择"});
		});
         </script>	
	</body>
</html>