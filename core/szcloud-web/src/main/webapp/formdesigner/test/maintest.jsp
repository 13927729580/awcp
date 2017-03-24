<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@include file="../include/head.jsp" %>
</head>
<body>
	<input type="text" id="testJson" value="${u.name }"> <br/>
	<label>${u.name }</label>
	<form id="form1" action="<%=basePath %>test/d2.do" method="post">
		<input type="hidden" name="id"><br/>
		<input type="text" name="sex"value="1"><br/>
		<input type="hidden" id="myname" name="name" ><input type="button" value="选择" onclick="test();"><br/>
		<input type="submit" value="保存">
	</form>
	<textarea id="t" rows="50" cols="600">
	${u.name }
	</textarea>

	<%@include file="../include/foot.jsp" %>
	<script type="text/javascript">
	$('document').ready(function() {
		var json = JSON.parse($("#testJson").val());
		//alert(json.preLoadScript);
		$("#t").val(json.preLoadScript)
		
	});
		function test(){
			dialog(
				{ 	id: 'act-dialog',
					title: '选择动作',
					url:'<%=basePath%>test/d1.do',
					onclose: function () {
						if (this.returnValue) {
							var ret= this.returnValue;
							$("#myname").val(ret);
						}
					}
				}
			).showModal();
		}
	
	</script>
</body>
</html>