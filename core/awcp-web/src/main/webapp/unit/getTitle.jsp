<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";
	String title = request.getParameter("title");
%>
<!DOCTYPE html>
<html>
	<head>	
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
		<%@ include file="../resources/include/common_css.jsp" %>
	</head>
	<body style="padding:12px;">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="okBtn"><i class="icon-save"></i>OK</button>
				<button type="button" class="btn btn-info" id="cancelBtn"><i class="icon-reply"></i>Cancel</button>			
			</div>		
			<div class="row" id="content">
			
			</div>
		</div>
		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script type="text/javascript">
			$(function(){
				$.ajax({
					url:"<%=basePath%>unit/getPositionList.do",
					type:"post",
					async:true,
					dataType:"text",
					success:function(data){
						var arr =  data.split(",");
						var content = "<input type='hidden' id='title' /><table style='width:800px;'>";
						for(var i=0; i <arr.length;i++){
							if((i+1)%3 == 0){
								content += '<td><input type="radio" name="title" value="'+arr[i]+'">'+arr[i]+'</input></td></tr>';
							}
							else if((i+1)%3==1){
								content += '<tr style="height:30px"><td><input type="radio" name="title" value="'+arr[i]+'">'+arr[i]+'</input></td>';
							}
							else{
								content += '<td><input type="radio" name="title" value="'+arr[i]+'">'+arr[i]+'</input></td>';
							}						  
						}	 
						content +='</table>';					  
						$("#content").append(content);
						var title = "<%=title %>";
						$("input[value='"+title+"']").attr("checked",true);
					}			  
				});
				
				try {
					var dialog = top.dialog.get(window);
				} catch (e) {
					return;
				}
				
				$("#cancelBtn").click(function(){//关闭弹窗 取消
				   dialog.close("");
				});
				$("#okBtn").click(function(){//关闭弹窗 提交
					var title = "";
					if($("input[name='title']:checked").length>0){
						title = $("input[name='title']:checked").eq(0).val();
					}
				    dialog.close(title);
				});
			});				
		</script>
	</body>
</html>
