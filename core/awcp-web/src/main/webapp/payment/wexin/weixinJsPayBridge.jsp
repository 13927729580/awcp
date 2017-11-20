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
<%
	response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");   
	// 设置 HTTP/1.1 no-cache 头   
	response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");   
	// 设置 IE 扩展 HTTP/1.1 no-cache headers， 用户自己添加   
	response.addHeader("Cache-Control", "post-check=0, pre-check=0");   
	// 设置标准 HTTP/1.0 no-cache header.   
	response.setHeader("Pragma", "no-cache");
 %>
 <%
 	String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "");
 %>
<!DOCTYPE>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<meta name="author" content="qinuoli">
	<title>微信支付</title>
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="format-detection" content="telephone=no">
	<script type="text/javascript" src="<%=basePath%>resources/JqEdition/jquery-1.10.2.js"></script>
	<style type="text/css">
		body {
			padding: 0px;
			margin: 0px auto;
		}
	
		body {
			background-color: #EEE;
		}
	
		.i-assets-container {
			border: 1px solid #E1E1E1;
			vertical-align: top;
			display: block;
			background-color: #FFF;
			margin: 5px 10px;
			color: #A1A1A1 !important;
		}
		
		#container.ui-container {
			color: #666;
		}
		
		.i-assets-content {
			margin: 10px 10px 10px 20px;
		}
		
		.fn-clear:after {
			visibility: hidden;
			display: block;
			font-size: 0px;
			content: " ";
			clear: both;
			height: 0px;
		}
		
		.i-assets-header h3 {
			font-size: 14px;
		}
		
		.fn-left {
			display: inline;
			float: left;
		}
		
		h3 {
			margin: 0px;
			padding: 0px;
			font: 12px/1.5 tahoma, arial, "Hiragino Sans GB", "Microsoft Yahei",
				"宋体";
		}
		
		.i-assets-balance-amount {
			line-height: 24px;
			margin-right: 20px;
		}
		
		.amount {
			font-size: 24px;
			font-weight: 400;
			color: #666;
			margin-left: 25px;
		}
		
		.amount .fen {
			font-size: 18px;
		}
		
		#wx_bottom {
			display: flex;
		}
		
		#wx_bottom {
			overflow: hidden;
			margin: 15px 0px;
		}
		
		#wx_bottom {
			display: box;
			display: -ms-box;
			display: -webkit-box;
			display: flex;
			display: -ms-flexbox;
			display: -webkit-flex;
		}
		
		#wx_bottom  .a {
			width: 100%;
			height: 40px;
			line-height: 40px;
			margin: 0px 10px;
			border: 1px solid #DDD;
			text-align: center;
			border-radius: 3px;
			color: #666;
			background: #fff;
			text-decoration: none;
		}
		
		.WX_search {
			background-color: #EFEFEF;
			height: 40px;
			line-height: 40px;
			position: relative;
			border-bottom: 1px solid #DDD;
			text-align: center;
		}
		
		.pay_buttom {
			margin: 15px 0px;
			width: 100%;
			display: box;
			display: -ms-box;
			display: -webkit-box;
			display: flex;
			display: -ms-flexbox;
			display: -webkit-flex;
			width: 100%;
		}
		
		.pay_buttom a {
			height: 40px;
			line-height: 40px;
			margin: 0px 10px;
			border: 1px solid #DDD;
			text-align: center;
			border-radius: 3px;
			color: #666;
			background: #fff;
			text-decoration: none;
			width: 100%;
			display: block;
			flex: 1;
			-ms-flex: 1;
			-webkit-flex: 1;
			box-flex: 1;
			-ms-box-flex: 1;
			-webkit-box-flex: 1;
		}
	</style>
</head>
<body>
	<div class="WX_search">
		<p>订单支付信息确认</p>
	</div>
	<form action="" method="post">
		<div class="i-assets-container ui-bookblock-item">
			<div class="i-assets-content">
				<div class="i-assets-header fn-clear">
					<h3 class="fn-left">充值单号</h3>
				</div>
				<div class="i-assets-body fn-clear">
					<div class="i-assets-balance-amount fn-left">
						<strong class="amount"><span style="font-size: 15px;"><%=outTradeNo %></span></strong>
					</div>
				</div>
			</div>
			<div class="i-assets-content">
				<div class="i-assets-header fn-clear">
					<h3 class="fn-left">商品名称</h3>
				</div>
				<div class="i-assets-body fn-clear">
					<div class="i-assets-balance-amount fn-left">
						<strong class="amount"><span style="font-size: 15px;">测试微信公众号支付</span></strong>
					</div>
				</div>
			</div>
			<div class="i-assets-content">
				<div class="i-assets-header fn-clear">
					<h3 class="fn-left">支付总金额</h3>
				</div>
				<div class="i-assets-body fn-clear">
					<div class="i-assets-balance-amount fn-left">
						<strong class="amount"><span id="total">0.01</span></strong>元
					</div>

				</div>
			</div>
		</div>

		<div class="i-assets-container ui-bookblock-item">
			<div class="i-assets-content">
				<div class="i-assets-header fn-clear">
					<h3 class="fn-left">您需要支付金额</h3>
				</div>
				<div class="i-assets-body fn-clear">
					<div class="i-assets-balance-amount fn-left"
						style="color: #F37800;">
						<strong class="amount" style="color: #F37800;">0.01</strong>元
					</div>
				</div>
			</div>
		</div>
		
		<div class='pay_buttom'>
			<a href="#" id="pay" style="background: #06C; color: #fff;" onclick="dopay();">确认支付</a> 
			<a href="#" id="cancel_apy" style="background: #cccccc; color: #fff;display: none;">确认支付</a>
		</div>
	</form>
	<script type="text/javascript">  
        var data= {
            "productName" : "测试微信公众号支付",//商品名称
            "totalPrice" : "0.01", //支付的总金额 
            "outTradeNo" :"<%=outTradeNo %>",
            "openId":""
        };
        
        function dopay() {
            $("#loadingDiv").show();
            $("#cancel_apy").show();
            $("#pay").hide();
            $.ajax({
                type : 'POST',
                url :  "<%=basePath %>jsPay.do",
                data : data,
                cache : false,
                error : function(XMLHttpRequest, textStatus, errorThrown) {
                    $("#loadingDiv").hide();
                    $("#cancel_apy").hide();
                    $("#pay").show();
                    alert("系统错误，请稍候重试");
					alert("错误类型textStatus: "+JSON.stringify(textStatus));
                	return false;
                },
                success : function(data) {
                    $("#loadingDiv").hide();  
                    if(data.result=="fail"){
                    	var msg = data.msg;
                    	if(msg=="WEIXIN_VISIT"){
                    	    $("#cancel_apy").hide();
                            $("#pay").show();
                    		alert("请使用手机微信客户端！");
                	    	return false;
                    	}else if(msg=="40001"){
                    	   $("#cancel_apy").hide();
                           $("#pay").show();
                    		alert("微信支付参数配置有误！");
                	    	return false;
                    	}
                    	else if(msg=="40002"){
                    	    $("#cancel_apy").hide();
                            $("#pay").show();
                    		alert("微信支付参数prepay_id配置有误！");
                	    	return false;
                    	}
                    	else if(msg=="fail"){
                    	    $("#cancel_apy").hide();
                            $("#pay").show();
                    		alert("微信支付有误！");
                	    	return false;
                    	}
                    }else if(data.result=="OK"){
                       if(parseInt(data.agent)<5){
                          $("#cancel_apy").hide();
                          $("#pay").show();
                          alert("您的微信版本低于5.0无法使用微信支付");
                          return;  
                       }
                       WeixinJSBridge.invoke('getBrandWCPayRequest',{
                           "appId" : data.appId, //公众号名称，由商户传入  
                           "timeStamp" : data.timeStamp, //时间戳，自 1970 年以来的秒数  
                           "nonceStr" : data.nonceStr, //随机串  
                           "package" : data.packageValue, //商品包信息
                           "signType" : data.signType, //微信签名方式:  
                           "paySign" : data.paySign //微信签名  
                       },function(res) {
                        /* 对于支付结果，res对象的err_msg值主要有3种，含义如下：(当然，err_msg的值不只这3种)
                        1、get_brand_wcpay_request:ok   支付成功后，微信服务器返回的值
                        2、get_brand_wcpay_request:cancel   用户手动关闭支付控件，取消支付，微信服务器返回的值
                        3、get_brand_wcpay_request:fail   支付失败，微信服务器返回的值

                        -可以根据返回的值，来判断支付的结果。
                        -注意：res对象的err_msg属性名称，是有下划线的，与chooseWXPay支付里面的errMsg是不一样的。而且，值也是不同的。
                        */
                        if (res.err_msg == 'get_brand_wcpay_request:ok') {
                        	$.ajax({
                                type : 'GET',
                                url :  "<%=basePath%>successPay.do" ,
                                dataType : 'json',
                                cache : false,
                                success : function(data) {
                                	
                                }
                        	});
                        	alert("支付成功！");
                            window.location.href = data.sendUrl;
                         } else if (res.err_msg == "get_brand_wcpay_request:cancel") {
                            $("#cancel_apy").hide();
                            $("#pay").show();
                            alert("您已手动取消该订单支付。");
                         } else {
                            $("#cancel_apy").hide();
                            $("#pay").show();
                            alert(res.err_msg);
                            alert("订单支付失败。");
                         }
                       });
                    }
                }
            });
        }
        
      	showloading();
      	
      	function showloading(){
        	var _PageHeight = document.documentElement.clientHeight,
			    _PageWidth = document.documentElement.clientWidth;
			//计算loading框距离顶部和左部的距离（loading框的宽度为215px，高度为61px）
			var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,
			    _LoadingLeft = _PageWidth > 120 ? (_PageWidth - 120) / 2 : 0;
			//在页面未加载完毕之前显示的loading Html自定义内容
			var _LoadingHtml = '<div id="loadingDiv" style="display:none;position:absolute;left:0px;width:100%;height:' + _PageHeight + 'px;top:0;background:#f3f8ff;-moz-opacity: 0.63;opacity:0.63;filter:alpha(opacity=40);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 11px; padding-right: 11px; background: #fff  no-repeat scroll 5px 10px; border: 2px solid #95B8E7; color: #696969; font-family:\'Microsoft YaHei\';">提交支付...</div></div>';
			//呈现loading效果
            $("body").append(_LoadingHtml);
       	}     
    </script>

</body>
</html>