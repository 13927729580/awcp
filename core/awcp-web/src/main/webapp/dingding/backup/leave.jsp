<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<title>请假</title>
	<base href="<%=basePath%>">
	<link href="<%=basePath %>dingding/css/mui.min.css" rel="stylesheet" />
	<link href="<%=basePath %>dingding/css/myDingding.css" rel="stylesheet" />
</head>
<body>
	<div class="outerDiv">
		<div class="input_control">
             <label>请假类型</label>
             <span style="float:right;" id="leaveType">请选择(必填)</span>
         </div>
	</div>
		
	<div class="outerDiv">
        <div class="input_control">
        	<label>开始时间</label>
            <span style="float:right;" id="startDate">请选择(必填)</span>
        </div>
        <div class="input_control">
            <label>开始时间</label>
            <span style="float:right;" id="endDate">请选择(必填)</span>
        </div>
        <div class="input_control">
            <label>时长(天)</label>
            <input type="number" placeholder="请输入时长(必填)" id="days"/>
        </div>        
    </div>
	
	<div class="outerDiv">
		<div class="input_control">
			<label style="margin-top:10px;float:left;">请假事由</label>
			<textarea id="leaveReason" placeholder="请输入请假事由(必填)" rows="3"></textarea>
		</div>
	</div>
	
	<div class="outerDiv">
		<div class="input_control">
        	<label>图片</label>
           	<img src="<%=basePath %>dingding/img/camera.jpg" 
           		style="margin:8px 15px 0px 0px;float:right;width:34px;heigth:34px;" id="picture" />
           	<input type="hidden" id="imageData" /> 
	        <div id="images"></div>
        </div>
	</div>
	
	<div class="outerDiv" style="height:160px;margin-bottom:70px;">
		<div class="input_control">
        	<label>审批人</label>
        	<input type="hidden" id="personData" value="" />
        	<div id="addCheckPerson" class="selectPersonIcon">
	       		<img src="<%=basePath %>dingding/img/add.gif" class="personImg"/>
	          	<label class="personLabel">添加</label>
       		</div> 
        </div>       
	</div>
	<div class="outerDiv">
		<div class="input_control">
        	<label>附件</label>
           	<img src="<%=basePath %>dingding/img/camera.jpg" 
           		style="margin:8px 15px 0px 0px;float:right;width:34px;heigth:34px;" id="picture" />
           	<input type="hidden" id="attachment" /> 
	        <div id="files"></div>
        </div>
	</div>
	
	<!-- <div class="buttons">
		<button id="submitBtn">提交</button>
		<button id="agreeBtn">同意</button>
		<button id="forwardBtn">转发</button>
	</div> -->
	
	<script type="text/javascript" src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js"></script>
	<script src="https://g.alicdn.com/dingding/open-develop/1.6.9/dingtalk.js"></script>
	<script src="<%=basePath %>venson/js/common.js"></script>
	<script>
		var basePath = "<%=basePath %>";
		var workId = "${param.workId}";
		var dd_config = {};
		
		Date.prototype.format = function(format){
	        var date = {
	            'M+': this.getMonth() + 1,
	            'd+': this.getDate(),
	            'h+': this.getHours(),
	            'm+': this.getMinutes(),
	            's+': this.getSeconds(),
	            'q+': Math.floor((this.getMonth() + 3) / 3),
	            'S+': this.getMilliseconds()
	        };
	        if (/(y+)/i.test(format)){
	            format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
	        }
	        for (var k in date){
	            if (new RegExp('(' + k + ')').test(format)){
	                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k] : ('00' + date[k]).substr(('' + date[k]).length));
	            }
	        }
	        return format;
	    };
	    var dd_config = Comm.getData("dingding/config.do",{"_method":"get","url":location.href});
	    dd.config({
    		agentId : dd_config.agentid,	 	// 必填，微应用ID
    		corpId : dd_config.corpId,			//必填，企业ID
    		timeStamp : dd_config.timeStamp,	// 必填，生成签名的时间戳
    		nonceStr : dd_config.nonceStr,		// 必填，生成签名的随机串
			signature : dd_config.signature,	// 必填，签名
			jsApiList : ["biz.contact.choose","biz.util.datetimepicker","biz.util.uploadImage",
				"biz.util.uploadImageFromCamera","biz.util.uploadAttachment"]
		});
		$.ajax({
	        url: basePath + "dingding/getDdJsapiData.do",
	        type:"POST",
	        async:false,   
	        data:{
	           url: location.href,
	           agentId : "113974887"
	        },
	        dataType:"json", 
	        success:function(result){
	        	
	        }
		});

		function hideAndShow(){
			if($("#images").html()){
				$("#images").show();
			}
			else{
				$("#images").hide();
			}
		}
		
		dd.ready(function() {
			if(workId){
				$("#submitBtn").hide();	
			}
			else{
				$("#agreeBtn").hide();
				$("#forwardBtn").hide();
			}
			
			$("#submitBtn").bind("click",function(){
				var leaveType = $.trim($("#leaveType").text());
				var startDate = $.trim($("#startDate").text());
				var endDate = $.trim($("#endDate").text());
				var days = $("#days").val();
				var reason = $("#leaveReason").val();
				var imageData = $("#imageData").val();
				var personData = $("#personData").val();
				$.ajax({
			        type: "POST",
			        url: basePath + "workflow/create/abcde.do",
			        data:{
			        	"_method":"post",
			        	"user":personData
			        },
			        dataType: "json",
			        async:false,
			        success: function (data) {
			        	if(data.status==0){
			        		alert("提交成功.");
			        	}
			        },
			        error:function(data){
			        	alert(data);
			        }
			    });	
				
			});
			
			$("#forwardBtn").click(function(){
		 		$.ajax({
			        type: "POST",
			        url: basePath + "workflow/transfer/" + workId + ".do",
			        data:{"_method":"post","user":$("#personData").val()},
			        dataType: "json",
			        async:false,
			        success: function (data) {
			        	if(data.status==0){
			        		alert("转发成功.");
			        	}
			        }
			    });
			})
			
			$("#agreeBtn").click(function(){
				$.ajax({
			        type: "POST",
			        url: basePath + "workflow/execute/" + workId + ".do",
			        data:{"_method":"post"},
			        dataType: "json",
			        async:false,
			        success: function (data) {
			        	alert(data.message);
			        }
			    });
			})
			
			$("#picture").parent().bind("click",function(){
				var imageData = $("#imageData").val();
				var max = 9;
				if(imageData){
					max -= imageData.split(",").length;
				}
				dd.biz.util.uploadImage({
					"compression":true,
					"multiple": true, //是否多选，默认false
					"max": max, //最多可选个数
					onSuccess: function(data) {
						if(imageData){
							imageData += "," + data.join();
						}
						else{
							imageData = data.join();
						}
						$("#imageData").val(imageData);
						for(var i=0;i<data.length;i++){
							$("#images").append("<img class='uploadImg' src='" + data[i] + "' />");
						}				
						hideAndShow();
				    }
				});
			});
			$("#attachment").parent().click(function(){
				var data=Comm.getData("dingding/spaceid.do",{"_method":"get"});
				dd.biz.util.uploadAttachment({
				    image:{multiple:true,compress:false,max:9,spaceId: data},
				    space:{corpId:dd_config.corpId,spaceId:data,isCopy:1 , max:9},
				    file:{spaceId:data,max:1},
				    types:["photo","camera","file","space"],
				    onSuccess : function(result) {
				       alert(JSON.stringify(result));
				    },
				    onFail : function(err) {
				    	alert(JSON.stringify(err));
				    }
				});
			})
			function checkDate(){
				var startDate = $.trim($("#startDate").text());
				var endDate = $.trim($("#endDate").text());
				if(startDate!="请选择(必填)" && endDate!="请选择(必填)"){
					if(startDate > endDate){
						$("#startDate").text("请选择(必填)");
						$("#endDate").text("请选择(必填)");
						alertMessage("开始时间不能大于结束时间");
					}
				}
			}
						
			$("#startDate,#endDate").parent().bind("click",function(event){
				var $target = $(event.target);
				var value = $.trim($target.text());
				if(value=="请选择(必填)"){
					value = new Date().format("yyyy-MM-dd hh:mm");
				}
				dd.biz.util.datetimepicker({
					"format":"yyyy-MM-dd HH:mm",
					"value":value,
					onSuccess: function(data) {
						$target.text(data.value);
						checkDate();
				    },
				    onFail: function(err) {
				    	log.e(JSON.stringify(err));
				    }
				});
			});
			
			$("#addCheckPerson").click(function(){
				var personData = $("#personData").val();
				var max = 4;
				var arr = [];
				if(personData){
					arr = personData.split(",");
					max = 4 - arr.length;
				}	
				if(max==0){
					alertMessage("最多选择4个用户");
					return false;
				}
				dd.biz.contact.choose({
					"startWithDepartmentId":0,
					"isNeedSearch":true,
					"multiple":true,
					"max":max,
					"corpId":dd_config.corpId,
					"users":[],
					"disabledUsers":arr,
					"title":"选择用户",
					"limitTips":"请选择不多于4个用户",
					onSuccess: function(data) {
						var html = "";
						for(var i=0;i<data.length;i++){
							var avatar = data[i].avatar;
							var name = data[i].name;
							var emplId = data[i].emplId;
							arr.push(emplId);
							var img = "";
							if(avatar){
								img = '<img src="' + avatar + '" class="personImg" />';
							}
							else{
								var temp = "";
								if(name.length>2){
									temp = name.substring(name.length-2);
								}	
								else{
									temp = name;
								}
								img = '<div style="width:50px;height:50px;border-radius: 50px;border:1px solid #ccc;margin-bottom:6px;' + 
									  'background-color:#008080;text-align:center;padding:15px 0px 15px 0px;" >' + temp + '</div>';
							}
							html += '<div class="selectPersonIcon">' + img +  
									'<label class="personLabel">' + name + '</label></div>';
						}
						$("#addCheckPerson").before(html);
						$("#personData").val(arr.join());
				    },
				    onFail: function(err) {
				    	log.e(JSON.stringify(err));
				    }
				});				
			});
			
			$("#leaveType").parent().click(function(){
				var leaveType = $.trim($("#leaveType").text());
				if(leaveType=="请选择(必填)"){
					leaveType == "年假";
				}
				dd.biz.util.chosen({
					"source":[{"key":"年假","value":"年假"},
							  {"key":"事假","value":"事假"},
							  {"key":"病假","value":"病假"},
							  {"key":"调休","value":"调休"},
							  {"key":"产假","value":"产假"},
							  {"key":"婚假","value":"婚假"},
							  {"key":"例假","value":"例假"},
							  {"key":"丧假","value":"丧假"}],
					"selectedKey":leaveType,
					onSuccess: function(data) {
						$("#leaveType").text(data.key);
				    },
				    onFail: function(err) {
				    	log.e(JSON.stringify(err));
				    }
				});
			});
			
			function alertMessage(msg){
				dd.device.notification.alert({
					"message":msg,
					"title":"提示",
					"buttonName":"确定"
				});
			}
		});
		dd.error(function(error){
			alert('dd error: ' + JSON.stringify(error));
		});
	</script>
</body>
</html>