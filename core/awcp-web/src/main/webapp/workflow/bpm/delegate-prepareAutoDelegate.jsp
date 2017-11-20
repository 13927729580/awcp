<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>菜单编辑页面</title>
<%-- <jsp:include page="" flush=”true”/> --%>

<%@ include file="../../resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->
<%@ include file="../../resources/include/common_form_js.jsp"%>
<script type="text/javascript"
	src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
<link rel="stylesheet"
	href="<%=basePath%>resources/plugins/select2/select2.css" />
<link rel="stylesheet"
	href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css" />
<script type="text/javascript"
	src="<%=basePath%>resources/plugins/select2/select2.js"></script>
<script type="text/javascript"
	src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
<script type="text/javascript"
	src="<%=basePath%>resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
<link rel="stylesheet"
	href="<%=basePath%>resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css" />
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="dataform">
			<form id="userRepoForm" method="post" action="<%=basePath%>workflow/bpm/delegate-autoDelegate.do" class="form-horizontal">
				<legend>
					自动委托添加
					<%-- <c:if test="${model == null}">自动委托添加 </c:if>
			<c:if test="${model != null}"> 自动委托编辑 <input id="userRepo_id" type="hidden" name="id" value="${model.id}"> </c:if> --%>
				</legend>
				<div class="form-group">
					<label class="col-md-2 control-label required"
						for="bpm-category_name">代理人</label>
					<div class="col-md-4 userPicker">
						<input type="text" name="attorney" id="attorney"
							style="width: 175px;" value="">

					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required"
						for="bpm-category_name">开始时间</label>
					<div class="col-md-4">
						<input type="text" name="startTime" class="form-control form-date"
							placeholder="选择或者输入一个日期：yyyy-MM-dd"
							style="background-color: white; cursor: default; width: 175px;">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required"
						for="bpm-category_name">结束时间</label>
					<div class="col-md-4">
						<div class="input-append datepicker date"
							style="padding-left: 0px;">
							<input type="text" name="endTime" class="form-control form-date"
								placeholder="选择或者输入一个日期：yyyy-MM-dd"
								style="background-color: white; cursor: default; width: 175px;">
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required"
						for="bpm-category_priority">流程定义</label>
					<div class="col-md-4">

						<input type="text" class="form-control" id="processDefinitionIds"
							name="processDefinitionIds" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-md-offset-2 col-md-10">
						<button id="submitButton" class="btn btn-success">
							<i class="icon-save"></i>保存
						</button>
						&nbsp;
						<button type="button" onclick="history.back();" class="btn">
							<i class="icon-undo"></i>返回
						</button>
					</div>
				</div>
			</form>
		</div>

		<%@ include file="../../resources/include/common_form_css.jsp"%>
		<div id="createModelTemplate" title="选择用户"
			style="display: none; width: 80%">
			<div class="row" id="datatable">
				<form method="get" id="createForm" action="modeler-list.do">
					<table class="table datatable table-bordered">
						<thead>
							<tr>
								<th class="hidden"></th>
								<th>用户名</th>
								<th>真实姓名</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${users}" var="vo">
								<tr>
									<td class="hidden formData"><input id="id" type="hidden"
										value="${vo.ref}"></td>
									<td>${vo.ref}</td>
									<td>${vo.name}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			$('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
					  }
				  }

			  });

		if ($.fn.datetimepicker) {
					$('.form-datetime').datetimepicker({
						language : 'zh-CN',
						weekStart : 1,
						todayBtn : 1,
						autoclose : 1,
						todayHighlight : 1,
						startView : 2,
						forceParse : 0,
						showMeridian : 1,
						format : 'yyyy-mm-dd hh:ii'
					});
					$('.form-date').datetimepicker({
						language : 'zh-CN',
						weekStart : 1,
						todayBtn : 1,
						autoclose : 1,
						todayHighlight : 1,
						startView : 2,
						minView : 2,
						forceParse : 0,
						format : 'yyyy-mm-dd'
					});
					$('.form-time').datetimepicker({
						language : 'zh-CN',
						weekStart : 1,
						todayBtn : 1,
						autoclose : 1,
						todayHighlight : 1,
						startView : 1,
						minView : 0,
						maxView : 1,
						forceParse : 0,
						format : 'hh:ii'
					});
				}

		if($.fn.select2){
      		 $("#processDefinitionIds").select2({
      			// tags:["P:产品经理","D:开发甲","D:开发乙","D:开发丙","D:开发丁"],
      			 tags:	$(processDefinitions),
      			 placeholder: "请选择流程",
      			 tokenSeparators: [",", " "]
      		 });
      		}

      	$("#attorney").click(function(){
      		var cont = document.getElementById('createModelTemplate');
      		cont.style.display = 'block';
      		var createModule = dialog({
				    title: '选择用户',
				    content:cont,
				    id: 'EF893L',
				    width:'500px',
				    okValue: '选择',
				    ok: function () {
				    	//alert($("#attorney").val());
				    	//alert($("#id").val());
				    	if(count!=1){
				    		alert("您只能选择一个用户");
				    	}else{
						$("#attorney").val($("input[name='id']").val());
				         this.close();return false;}
				    },
		      		cancelValue: '取消',
				    cancel: function () { this.close();return false;}
				});
      			createModule.showModal();
      	});
      });
         </script>

</body>
</html>