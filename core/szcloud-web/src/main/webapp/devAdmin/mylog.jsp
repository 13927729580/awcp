<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../resources/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>调试日志</title>
<link rel="stylesheet" href="${ctxBase}/plugins/zui/dist/css/zui.css" >
<style>
	<%--谷歌滚动条--%>
	::-webkit-scrollbar{width:14px;}
	::-webkit-scrollbar-track{background-color:#bee1eb;}
	::-webkit-scrollbar-thumb{background-color:#00aff0;}
	::-webkit-scrollbar-thumb:hover {background-color:#9c3}
	::-webkit-scrollbar-thumb:active {background-color:#00aff0}

	body{
		background-color: #000000;
	}
	.text{
		color: #00ad3a;
	}
	pre{
		background-color: #000000;
		border:0;
		white-space: pre-wrap;
		word-wrap: break-word;
	}
	.btn-position{
		margin-top: 15px;
		margin-left: 15px;
	}
	.logDiv{
		margin-left: 30px;
	}
	#stuff
	{
		position: fixed;
		bottom: 20px;
		right: 15px;
		/*z-index: 888;*/
		width: 100px;
	}
</style>
</head>
<body>
<div class="logDiv">
	<div id="mark"></div>
	<table width="95%">
		<thead>
		<tr>
			<%-- <td><a href="${basePath }debug/clear.do">清空日志</a><a href="${basePath }debug/view.do">刷新</a></td> --%>
			<td><a class="btn btn-primary btn-position" href="clear.do">清空日志</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn btn-primary btn-position" href="view.do">刷新日志</a></td>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td width="95%"><div style="font-size: 15px">${logs }</div></td>
		</tr>
		</tbody>
	</table>
</div>
<div id="stuff">
	<a class="btn btn-primary btn-position" href="clear.do">清空日志</a>
	<a class="btn btn-primary btn-position" href="view.do">刷新日志</a>&nbsp
	<a class="btn btn-primary btn-warning" style="margin-top: 20px"  onclick="scroll()"><i class="icon icon-chevron-sign-up"></i> 回到顶部</a>
</div>
<script src="${ctxBase}/JqEdition/jquery-1.8.3.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#stuff').hide();
		$(window).scroll(function() {
			if ($(window).scrollTop() >= 100) {
				$('#stuff').fadeIn(400);
			} else {
				$('#stuff').fadeOut(200);
			}
		})
	});

//	var scroll ={
//		top : function(){
//			$('html,body').animate({scrollTop:$('#mark').offset().top},800,'swing');
//		}
//	}
	var scroll = function(){
		$('html,body').animate({scrollTop:$('#mark').offset().top},800,'swing');
	}

</script>
</body>

</html>