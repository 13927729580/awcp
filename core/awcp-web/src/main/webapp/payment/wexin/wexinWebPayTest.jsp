<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<title>微信支付参数设置</title>
	<%@ include file="/resources/include/common_form_css.jsp"%>
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="dataform">
			<form class="form-horizontal" id="wxForm">
				<input type="hidden" name="outTradeNo" id="outTradeNo" value="<%=outTradeNo %>"> 
				<div class="C_addForm">
					<div class="form-group">
						<div class="col-md-offset-2 col-md-10">
							<button type="button" class="btn btn-success" id="pay"><i class="icon-save"></i>支付</button>
						</div>
					</div>					
					<div class="form-group">
						<label class="col-md-2 control-label required">商品名称</label>
						<div class="col-md-4">
							<input type="text" class="form-control" name="body" id="body" />
						</div>
						<label class="col-md-2 control-label required">价格</label>
						<div class="col-md-4">
							<input type="text" class="form-control" name="totalFee" id="totalFee" />
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="/resources/include/common_form_js.jsp"%>
	<script type="text/javascript">
		$(function(){
			$("#pay").click(function(){
				var outTradeNo = $("#outTradeNo").val();
				var body = $.trim($("#body").val());
				var totalFee = $.trim($("#totalFee").val());
				var reg = /^[0-9]*\.?[0-9]*$/;
				if(body && totalFee && reg.test(totalFee)){
					top.dialog({
						id: 'add-dialog'+$.uuid(),
						title: '支付订单...',
						height:540,
						width:1000,
						url: "<%=basePath%>wxPay/webPay.do?body=" + body + "&totalFee=" + totalFee,
						data:"",
						onclose: function () {
						}
					}).showModal();
				}
				else{
					alert("数据有问题");
				}
			});
		});
	</script>
</body>
</html>
