<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
		<div class="row" id="dataform">
			<form class="form-horizontal" id="actForm"
				action="<%=basePath%>fd/act/save.do" method="post">
				<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
				<%@ include file="comms/basicInfo.jsp"%>
				<%@ include file="comms/scripts.jsp"%>
			</form>
		</div>
	</div>

	<%@ include file="/resources/include/common_form_js.jsp"%>
	<script src="<%=basePath%>resources/scripts/map.js"></script>
	<script src="<%=basePath%>formdesigner/page/script/act.js"></script>
	<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript">
		$("#dynamicPageId").change(function(){
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
            $.formValidator.initConfig({formID:"actForm",debug:false});
            $("#buttonGroup").formValidator({onFocus:"请输入按钮组"}).inputValidator({min:1,onError:"必填"});
            $("#name").formValidator({onFocus:"请输入名称"}).inputValidator({min:1,onError:"必填"});
            $("#order").formValidator({onFocus:"请输入序号"}).inputValidator({min:1,onError:"必填"});
			$("#saveBtn").click(function() {
				$("#actType").removeAttr("disabled");
				if (dialog) {
                    if ($.formValidator.pageIsValid('1')) {
                        $("#dynamicPageId").removeAttr("disabled");
                        var data = $("#actForm").serializeJSON();
                        data.dynamicPageName = $("#dynamicPageId option；selected").text();
                        var buttons = new Array();
                        $(":checkbox[name='buttons']:checked").each(function () {
                            buttons.push($(this).val());
                        });
                        data['buttons'] = buttons.join(",");
                        $.ajax({
                            type: "POST",
                            async: false,
                            url: "fd/act/saveByAjax.do",
                            data: data,
                            success: function (ret) {
                                var json = eval(ret);
                                dialog.close(json);
                                dialog.remove();
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                alert(errorThrown);
                            }
                        });
                    } else {
                        var name = $("#dynamicPageId option:selected").text();
                        $("#actForm").append("<input type='hidden' name='dynamicPageName' value='" + name + "'/>");
                        $("#actForm").submit();
                    }
                    return false;
                }
			});
		});
	</script>
</body>
</html>
