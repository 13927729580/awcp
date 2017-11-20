<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>页面动作编辑页面</title>
<%@ include file="/resources/include/common_css.jsp"%>

</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
				<li><a href="#">首页</a></li>
				<li><a href="#">我的应用产品</a></li>
				<li class="active">页面动作编辑</li>
			</ul>
		</div>
		<div class="row" id="dataform">
			<form class="form-horizontal" id="actForm"
				action="<%=basePath%>fd/act/save.do" method="post">
				<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
				<%@ include file="comms/basicInfo.jsp"%>
				<div class="form-group ">
					<label class="col-md-1 control-label">动作类型</label>
					<div class='col-md-5'>
						<select class=" form-control" name="actType" disabled="disabled"
							id="actType">
							<option value="2004" selected="selected">启动流程</option>
						</select>
					</div>
					 <label class="col-md-1 control-label">启动方式</label>
					<div class="col-md-5">
						<label class="radio-inline"> <input name="extbute[pattern]"
							type="radio"
							<c:if test="${'1' eq act.extbute['pattern']}"> checked="checked"</c:if>
							value="1">用户选择
						</label> <label class="radio-inline"> <input name="extbute[pattern]"
							type="radio"
							<c:if test="${'2' eq act.extbute['pattern']}"> checked="checked"</c:if>
							value="2">脚本启动
						</label> <label class="radio-inline"> <input name="extbute[pattern]"
							type="radio"
							<c:if test="${'3' eq act.extbute['pattern']}"> checked="checked"</c:if>
							value="3">选择流程
						</label>
					</div> 
				</div>
				 <div class="form-group hidden" id="selectworkflowdiv">
					<label class="col-md-1 control-label">选择流程</label>
					<div class="col-md-5">
						<select class=" form-control" name="extbute[workflowId]" id="workflowId">
							<option value="${act.extbute['workflowId']}"></option>
						</select>
					</div>
				</div>
				<div class="form-group hidden" id="scriptdiv">
					<label class="col-md-1 control-label">启动脚本</label>
					<div class="col-md-5">
						<textarea name='extbute[startScript]' id='startScript'
							rows='4' class='form-control'>${act.extbute['startScript']}</textarea>
					</div>
				</div> 
				<%@ include file="comms/scripts.jsp"%>
				<%@ include file="comms/confirm.jsp"%>
				<div class="form-group">
					<!-- 表单提交按钮区域 -->
					<div class="col-md-offset-2 col-md-10">
						<button type="button" class="btn btn-success" id="saveBtn">
							<i class="icon-save"></i>保存
						</button>
					</div>
				</div>
			</form>
		</div>


	</div>

	<%@ include file="/resources/include/common_form_js.jsp"%>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/map.js"></script>
	<script type="text/javascript" src="<%=basePath%>formdesigner/page/script/act.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript">
		$("#dynamicPageId").change(function(){
		var $btnSet = $("#js-btn-set"),
          			$btnSetIcon = $btnSet.find("#btnIcon"),
          			$btnSetColor = $btnSet.find("#btnColor");
          		$btnSet.find(".icon-menu a").click(function(){
          			var icon = $(this).attr("class");
          			$btnSetIcon.find("i").attr("class",icon);
          			$btnSetIcon.find("input").val(icon);
          		});
				$btnSet.find(".color-menu span").click(function(){
					var common = "label label-dot";
				    var color = $(this).data("color");
				    $btnSetColor.find("span").attr("class",common+" label-"+color);
          			$btnSetColor.find("input").val(color);
          		});
			var dpId=$("#dynamicPageId").val();
			$("#workflowId").empty();
			$.ajax({
				type:"GET",
				url:"fd/act/getWorkflowByPageId.do?id="+dpId,
				success:function(ret){
					var json = eval(ret);
					$.each(json,function(idx,item){
						var str = "<option value=\""+item.workId+"\" >"+item.workName+"</option>";
						$("#workflowId").append(str);
					});
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
					alert(errorThrown);
				}
			});
		});
		$(":radio[name='extbute[pattern]']").change(function() {
			$("#scriptdiv").addClass("hidden");
			$("#selectworkflowdiv").addClass("hidden");
			$("#workflowId").attr("disabled","disabled");
			$("#startScript").attr("disabled","disabled");
			if ("2" == $(this).val()) {
				$("#scriptdiv").removeClass("hidden");
				$("#startScript").removeAttr("disabled");
				$("#workflowId option").eq(0).attr("selected", "selected");
			} else if ("3" == $(this).val()) {
				var dpId=$("#dynamicPageId").val();
				$("#workflowId").removeAttr("disabled");
				$("#workflowId").empty();
				$.ajax({
					type:"GET",
					url:"fd/act/getWorkflowByPageId.do?id="+dpId,
					success:function(ret){
						var json = eval(ret);
						$.each(json,function(idx,item){
							if(workflowId==item.workId){
								var str = "<option value=\""+item.workId+"\" selected=\'' >"+item.workName+"</option>";
							}else{
								var str = "<option value=\""+item.workId+"\" >"+item.workName+"</option>";
							}
							$("#workflowId").append(str);
						});
					},
					error:function(XMLHttpRequest, textStatus, errorThrown){
						alert(errorThrown);
					}
				});
				$("#selectworkflowdiv").removeClass("hidden");
				$("#startScript").html(" ");
				$("#startScript").val(" ");
			} else if ("1" == $(this).val()) {
				$("#startScript").html(" ");
				$("#startScript").val(" ");
				$("#workflowId option").eq(0).attr("selected", "selected");
			}

		});
		$(document).ready(function() {
			/* $.formValidator.initConfig({formID:"actForm",debug:false,onSuccess:function(){
				$("#actForm").submit();
				return false;
			   },onError:function(){alert("请按提示正确填写内容");}}); */
			//$("#buttonGroup").formValidator({empty:true,onShow:"输入所属按钮组",onFocus:"只能输入1-99之间的数字哦",onCorrect:"正确"}).inputValidator({min:0,max:2,onErrorMin:"你输入的值必须大于等于1",onError:"按钮组在1-99之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
			//$("#order").formValidator({empty:true,onShow:"请输入按钮顺序",onFocus:"请输入整数",onCorrect:"合法"}).inputValidator({min:0,max:2,onErrorMin:"你输入的值必须大于等于1",onError:"按钮组在1-99之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
			//$("#name").formValidator({onShow:"请输入动作名称",onFocus:"2-8个字符",onCorrect:"合法"}).inputValidator({min:2,max:10,onErrorMax:"不能超过10个字符",onErrorMin:"至少2个字符",empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不合法，请确认"});
			//$("#description").formValidator({onShow:"请输入描述信息",onFocus:"请输入描述信息",onCorrect:"合法"}).inputValidator({min:1,max:225,onError:"输入错误"});
			// $("#height").formValidator({empty:true,onFocus:"请输入整数",onCorrect:"合法"}).inputValidator({min:0,max:3,onErrorMin:"你输入的值必须大于等于0",onError:"高度在0-999之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
			// $("#width").formValidator({empty:true,onFocus:"请输入整数",onCorrect:"合法"}).inputValidator({min:0,max:3,onErrorMin:"你输入的值必须大于等于0",onError:"宽度在0-999之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"}); 
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			var radio = $(":radio[name='extbute[pattern]']:checked").val();
			 if(empty(radio)){
				$(":radio[name='extbute[pattern]']:first").prop("checked","checked");
			} 
			if (dialog) {
				$("#dynamicPageId").attr("disabled", "disabled");
			}
			var pattern = $(":radio[name='extbute[pattern]']:checked").val();
			if("2"==pattern){
				$("#scriptdiv").removeClass("hidden");
			}else if("3"==pattern){
				$("#selectworkflowdiv").removeClass("hidden");
				var dpId=$("#dynamicPageId").val();
				$.ajax({
					type:"GET",
					url:"fd/act/getWorkflowByPageId.do?id="+dpId,
					success:function(ret){
						var json = eval(ret);
						var workflowId = $("#workflowId option").eq(0).val();
						$.each(json,function(idx,item){
							if(workflowId==item.workId){
								var str = "<option value=\""+item.workId+"\" selected=\'' >"+item.workName+"</option>";
							}else{
								var str = "<option value=\""+item.workId+"\" >"+item.workName+"</option>";
							}
							$("#workflowId").append(str);
						});
					},
					error:function(XMLHttpRequest, textStatus, errorThrown){
						alert(errorThrown);
					}
				});
			}
			
			$("#saveBtn").click(function() {
				$("#actType").removeAttr("disabled");
				if (dialog) {
					$("#dynamicPageId").removeAttr("disabled");
					var data = $("#actForm").serializeJSON();
					data.dynamicPageName=$("#dynamicPageId option；selected").text();
					var buttons = new Array();
					$(":checkbox[name='buttons']:checked").each(function() {
						buttons.push($(this).val());
					});
					data['buttons'] = buttons.join(",");
					$.ajax({
						type : "POST",
						async : false,
						url : "fd/act/saveByAjax.do",
						data : data,
						success : function(ret) {
							var json = eval(ret);
							dialog.close(json);
							dialog.remove();
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							alert(errorThrown);
						}
					});
				} else {
					var name=$("#dynamicPageId option:selected").text();
					$("#actForm").append("<input type='hidden' name='dynamicPageName' value='"+name+"'/>");
					$("#actForm").submit();
				}
				return false;
			});
		});
	</script>
</body>
</html>
