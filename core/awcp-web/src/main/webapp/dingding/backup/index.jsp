<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
		<title></title>
		<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js" charset="utf-8"></script>
		<script src="https://g.alicdn.com/dingding/open-develop/1.0.0/dingtalk.js" charset="utf-8"></script>
		<style>
			body, div, h1, p {
				margin: 0px;
				padding: 0px;
			}
			
			body {
				font-size: 14px;
				color: #333;
				background-color: #eee;
			}
		</style>
		<link rel="stylesheet" href="./css/app.css">
	</head>
<body>
	<div id="app-container">
		<div class="app-container">
			<div class="banner-slider">
				<div class="banner">
					<img src="<%=basePath %>dingding/img/ouson.jpg" class="banner-image">
				</div>
			</div>
			<div class="index-admin">
				<div class="admin">
					<div class="admin-manager">
						<img src="" class="admin-image">
						<div class="admin-hello"></div>
					</div>
					<div class="admin-edit">group edit</div>
				</div>
			</div>

			<div class="index-all-applist">
				<div class="applist-title">
					<h1 class="applist-text">all apps</h1>
				</div>
				<div class="grid">
					<div class="cell" id="logs" value="2">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsZGkszIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">logs</div>
						</div>
					</div>
					<div class="cell" id="notice" value="-2">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsZHFszIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">notice</div>
						</div>
					</div>
					<div class="cell" id="approval" value="-4">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsZGQszIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">approval</div>
						</div>
					</div>
					<div class="cell" id="dingmail" value="-5">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsah9MzIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">ding-mail</div>
						</div>
					</div>
					<div class="cell" id="dingdisk" value="-3">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsahiMzIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">ding-disk</div>
						</div>
					</div>
					<div class="cell" id="checkin" value="-8">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcrvqOszIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">check-in</div>
						</div>
					</div>
					<div class="cell" id="attendance" value="158">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsYpu8zIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">attendance</div>
						</div>
					</div>
					<!-- <div class="cell" id="ireport" value="">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsxZiszIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">i-report</div>
						</div>
					</div>
					<div class="cell" id="crm" value="">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOjlM0h8zIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">CRM</div>
						</div>
					</div>
					<div class="cell" id="officetel"value="">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcevpQczIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">office-tel</div>
						</div>
					</div>
					-->
					<div class="cell" id="telecon" value="-9">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsajBszIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">telecon</div>
						</div>
					</div>
					<div class="cell" id="video" value="1288">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="http://static.dingtalk.com/media/lALOcsaifMzIzMg_200_200.png" class="cell-image">
							</div>
							<div class="cell-text">videocon</div>
						</div>
					</div>
					<div class="cell" id="mine">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="img/tongyuanmeng.png" class="cell-image">
							</div>
							<div class="cell-text">my-web</div>
						</div>
					</div>
					<div class="cell" id="baidu">
						<div class="cell-box">
							<div class="cell-image-container">
								<img src="img/baidu.jpg" class="cell-image">
							</div>
							<div class="cell-text">baidu</div>
						</div>
					</div>
				</div>
			</div>
			<div class="add-manager" id="add_manage">
				<div class="add-manager-container">
					<img src="https://gw.alicdn.com/tps/TB1O.peOFXXXXbvXpXXXXXXXXXX-42-42.png" class="add-manager-add-icon">
					<div class="add-manager-text">add/manage</div>
					<img src="https://gw.alicdn.com/tps/TB12pIZOpXXXXaxXVXXXXXXXXXX-16-28.jpg" class="add-manager-more-icon">
				</div>
			</div>
		</div>
	</div>
	<script src="<%=basePath %>resources/scripts/jquery.cookies.js"></script>
	<script type="text/javascript">
		var basePath = "<%=basePath %>";
	
	    function updateName(userInfo){
	        var dateTime = new Date().getHours();
	        var isAdmin = userInfo.isAdmin;
	        var name = userInfo.name;
	        var nb = {};
	        if(name){
	            if (dateTime >= 5 && dateTime <= 12) {
	                nb.wh = isAdmin ? 'Good Morning,Manager': 'Good Morning,' + name;
	                nb.whImage = 'https://gw.alicdn.com/tps/TB1ubtjOFXXXXbzXpXXXXXXXXXX-36-36.jpg';
	            } else if (dateTime > 12 && dateTime <= 18) {
	                nb.wh = isAdmin ? 'Good Afternoon,Manager': 'Good Afternoon,' + name;
	                nb.whImage = 'https://gw.alicdn.com/tps/TB1ubtjOFXXXXbzXpXXXXXXXXXX-36-36.jpg';
	            } else {
	                nb.wh = isAdmin ? 'Good Evening,Manager': 'Good Evening,' + name;
	                nb.whImage = 'https://gw.alicdn.com/tps/TB15FNwOFXXXXbqXXXXXXXXXXXX-36-36.jpg';
	            }
	        }
	        return nb;
	    }
	
	    function updateUI(userInfo){
	        var nb = updateName(userInfo);
	        var html = '<img src="'+ nb.whImage+'" class="admin-image">'
	        + '<div class="admin-hello">'
	        + nb.wh
	        + '</div>';
	        $('.admin-manager').html(html);
	    }
	    
	    function getUserInfo(corpId){
	    	var code=$.cookie("authCode");
	    	if(code){
	    		var data = Comm.getData("dingding/sso.do?code="+code,{"_method":"get"});
             	updateUI(data);
	    	}else{
	    		 authCode(corpId).then(function(result){
	 	            var code = result.code;
	 	            $.cookie("authCode",code);
	 	            var data=Comm.getData("dingding/sso.do?code="+code,{"_method":"get"});
	             	updateUI(data);
	 	        });
	    	}
	       
	    }
	    
	    function authCode(corpId){
	        return new Promise(function(resolve, reject){
	            dd.ready(function(){
	                dd.runtime.permission.requestAuthCode({
	                    corpId: corpId,
	                    onSuccess: function(result) {
	                        resolve(result);
	                    },
	                    onFail : function(err) {
	                        reject(err);
	                    }
	                });
	            });
	        });
	    }
	    
	    //userInfo = getUserInfo("ding01e62a657ed7f66335c2f4657eb6378f"); 
        dd.ready(function(){
        	userInfo = getUserInfo("ding01e62a657ed7f66335c2f4657eb6378f");  	
        })
        
		
        var corpid = "ding01e62a657ed7f66335c2f4657eb6378f";
	    $(".cell").bind("click",function(){
	    	var appid = $(this).attr("value");
			location.href = "dingtalk://dingtalkclient/action/switchtab?index=2&name=work&scene=1&corpid=" + corpid + "&appid="+appid;
		});
	    
	    $("#baidu").bind("click",function(){
	    	window.location.href = "http://www.baidu.com";
	    });
	    
	    $("#mine").bind("click",function(){
	    	location.href = "http://www.tongyuanmeng.com";	
	    });
	    $("#add_manage").bind("click",function(){
	    	location.href = "http://119.29.226.152:8083/awcp/dingding/appList.jsp";
	    });
	    
	</script>
	<script>
	   
	</script>
</body>
</html>