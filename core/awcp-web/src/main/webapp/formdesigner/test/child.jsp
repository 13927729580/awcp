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
<%@include file="../include/head.jsp"%>
</head>
<body>
	<form id="form11" action="">
		<div class="form-group">
			<label class="col-md-2 control-label">加载前脚本：</label>
			<div class="col-md-4">
				<textarea name='preLoadScript' id='preLoadScript' rows='4' 
					class='form-control'></textarea>
			</div>
			<label class="col-md-2 control-label">加载后脚本：</label>
			<div class="col-md-4">
				<textarea name='afterLoadScript' id='afterLoadScript' rows='4' 
					class='form-control'></textarea>
			</div>
		</div>
		<input type="button" value="保存" onclick="bb();">
	</form>


	<%@include file="../include/foot.jsp"%>
	<script type="text/javascript">
		$('document').ready(function() {

			
		});
		
		function bb(){
			
			var formJson=$("#form11").serializeJSON();
			alert(formJson);
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			var data;
			if (dialog) {
				data = dialog.data;
				dialog.close(JSON.stringify(formJson));
				dialog.remove();
			}
			
		}
	</script>
</body>
</html>