<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
	<meta charset="utf-8">
    <title>支付中心</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
	<script src="<%=basePath %>resources/scripts/jquery-2.2.3.min.js"></script>
	<script src="<%=basePath %>payment/wexin/js/qrcode.js"></script>
</head>
<body>	
	<table style="margin:0 auto;width: 600px;border:1px solid #E0E0E0;border-spacing: 0px;border-collapse: collapse;" >
		<tr>
		<td style="border:1px solid #E0E0E0;padding:12px;text-align:center;">
			<span style="font-size: 40px;font-weight: bold;margin-left: 30px;">收银台</span></td>
		</tr>
		<tr>
			<td style="border:1px solid #E0E0E0;padding:12px;">
				<span style="font-size: 14px;line-height: 20px;">订单编号:${orderNo }</span><br/>
				<span style="font-size: 14px;line-height: 20px;">订单商品:${subject }</span><br/>
				<span style="font-size: 14px;line-height: 20px;">应付金额:${totalAmount }</span><br/>
			</td>
		</tr>
		<tr>
			<td style="border:1px solid #E0E0E0;padding:12px;vertical-align: middle;">
			<div style="border: solid 1px #ff8040;padding: 8px;">
				<img src="<%=basePath%>payment/wexin/img/wx_logo.png" style="vertical-align: middle" width="30" height="30" /> 微信支付
			 	<img src="<%=basePath%>payment/wexin/img/wx_intro.png" style="vertical-align: middle" width="60" height="30">
			 	<span style="font-size: 12px;font-weight: lighter;margin-left: 6px;color: darkgray;">亿万用户的选择,更快更安全</span>	
			 	<span style="float: right;font-size: 16px;margin-top:6px;">支付:<span style="color:#ff8040">${totalAmount }</span>元</span>
			 </div>
			<div id="qrcode" style="text-align: center;"></div>	
			<div style="text-align: center;"><img src="<%=basePath%>payment/wexin/img/desc.png" width="200" height="60"/></div>
			</td>
		</tr>
	</table>
	<script>
		var basePath = "<%=basePath%>";
		var url = "${payment_result}";
		var qr = qrcode(10, 'H');
		qr.addData(url);
		qr.make();
		$("#qrcode").append(qr.createImgTag());	
		$("#qrcode").find("img").attr("width","200px").attr("height","200px");
		
		var orderId = "${orderNo}";
		var twork;
		var seconds = 0;
		
		function getNotice(){
			$.ajax({		
				type: "get",		
				url: basePath + "api/execute/getOrderState",		
				async : false,	
				data:{
					orderId:orderId
				},
				success: function(data){			
					if(data.data==1){
                        clearInterval(twork);
						alert("支付成功。");
                        top.dialog({id :window.sessionStorage.getItem("dialog_id")}).close();
				        return false;
					}
				}	
			});
			seconds++;
			if(seconds>300){
                clearInterval(twork);
				alert("支付超时，支付失败");
                top.dialog({id :window.sessionStorage.getItem("dialog_id")}).close();
		        return false;
			}
			
		}
        twork= window.setInterval("getNotice()",1000);
	</script>
</body>
</html>

