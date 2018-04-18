function convertDateTimeScript($input,dateType){
	var dateReg = /^\d{4}-\d{2}-\d{2}$/;
	var dateTimeReg = /^\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}$/;
	var value = $input.val();
	var $span = $input.prev();
	//判断是否有值存在
	if(value.length>=10){
		$span.text(value.substring(0,dateType.length));		
	}
	$input.parent().click(function(){
		value = $input.val();
		//判断是否有值存在
		if(value.length>=10){
			$span.text(value.substring(0,dateType.length));		
		} else{
			if(!dateReg.test(value) && !dateTimeReg.test(value)){
				if(dateType=="yyyy-MM-dd"){
					value = new Date().format("yyyy-MM-dd");
				} else{
					value = new Date().format("yyyy-MM-dd hh:mm");
				}
			}
		}
		
		if(dateType=="yyyy-MM-dd"){
			dd.biz.util.datepicker({
				"format":dateType,
				"value":value,
				onSuccess: function(data) {
					$input.val(data.value);	
					$span.text(data.value);				
			    },
			    onFail: function(err) {
			    	console.log(JSON.stringify(err));
			    }
			});
		} else{
			dd.biz.util.datetimepicker({
				"format":"yyyy-MM-dd hh:mm",
				"value":value,
				onSuccess: function(data) {
					$input.val(data.value);	
					$span.text(data.value);				
			    },
			    onFail: function(err) {
			    	console.log(JSON.stringify(err));
			    }
			});
		}					
	});
}

function convertSelectScript($input){
	var optionStr = $.trim($input.next().val());		
	var optionArr = optionStr.split(";");	
	var source = [];
	var selectedKey;
	var firstKey;			
	for(var i=0;i<optionArr.length;i++){
		var e = optionArr[i];
		if(e){
			var arr = e.split("=");
			if(i==0){
				firstKey = arr[0];
			}
			var val = arr[0];
			var key;	//显示值		
			if(dd_res.lang=="en"){
				key = arr[0];							
			} else{
				key = arr[1];
			}	
			var obj = {"key":key,"value":val};
			source.push(obj);
			if(val == $input.val()){
				selectedKey = key;
			}			
		}
	}
	var $span = $input.parent().find(".dd_select");
	$span.text(selectedKey);
	$input.parent().click(function(){
		var selected = $span.text() || firstKey;
		dd.biz.util.chosen({
			"source":source,
			"selectedKey":selected,
			onSuccess: function(data) {
				$span.text(data.key);
				$input.val(data.value);
		    },
		    onFail: function(err) {
		    }
		});
			
	});
}

function convertImageScript($imgInput,imgMax){
	var images = $imgInput.parent().parent().next(".images");
	images.on("click",".crrp-body-imgwrap-img",function(){
		dd.biz.util.previewImage({"urls":data,"current":this.src});
	});
	images.on("click",".crrp-body-removeimg",function(){
		$(this).parent().remove();
		var arr = [];
		images.find(".crrp-body-imgwrap-img").each(function(){
			arr.push($(this).attr("src"));
		});
		$imgInput.val(arr.join());
	});
	var imageData = $imgInput.val();
	if(imageData){
		var data = imageData.split(",");
		$.each(data,function(i,e){
			var html = '<div class="crrp-body-imgwrap">' + 
					   '<div class="crrp-body-removeimg" style="background: url(' + baseUrl + 
					   '/dingding/img/r_close_x.png) no-repeat center;"></div>' +
					   '<img class="crrp-body-imgwrap-img" src="' + e + '"></div>';
			images.append(html);
		});		
	}
	$imgInput.parent().click(function(){
		imageData = $imgInput.val();
		var len = 0;
		if(imageData){
			len = imageData.split(",").length;
			if(imgMax-len <= 0){
				dd.device.notification.alert({"message":"只能选择"+imgMax+"张图片","title":dd_res.tip,"buttonName":dd_res.okButton})
				return;
			}
		}
		
		dd.biz.util.uploadImage ({
			"compression":true,
			"multiple": true, //是否多选，默认false
			"max": imgMax-len, //最多可选个数
			onSuccess: function(data) {
				for(var i=0;i<data.length;i++){
					var html = '<div class="crrp-body-imgwrap">' + 
					    '<div class="crrp-body-removeimg" style="background: url(' + baseUrl + 
					    '/dingding/img/r_close_x.png) no-repeat center;"></div>' +
					    '<img class="crrp-body-imgwrap-img" src="' + data[i] + '"></div>';
					images.append(html);
				}
				//如果之前存在则累加
				if(imageData){
					var arr=imageData.split(",");
					var newArr=[];
					for(var i=0;i<arr.length;i++){
						if($.trim(arr[i])){
							newArr.push(arr[i]);
						}
					}
					data=newArr.concat(data);
				}
				imageData = data.join(",");
				$imgInput.val(imageData);				
		    }
		});				
	});
}
function convertFileScript($attInput,attMax){
	var atts = $attInput.parent().parent().next(".atts");
	atts.on("click",".eventBox",function(){
		Comm.getData("dingding/spaceid.do",{"_method":"get","type":"download"});
		var e = JSON.parse($(this).parent().attr("data-json"));
		e.corpId = dd_config.corpId;
		dd.biz.cspace.preview(e);
	});
	
	atts.on("click",".fileClose",function(){
		$(this).parent().remove();
		var arr = [];
		$(".fileItem").each(function(){
			var obj = JSON.parse($(this).attr("data-json"));
			arr.push(obj);
		});
		$attInput.val(JSON.stringify(arr));
	});
	
	var attData = $attInput.val();
	attData = (attData)?JSON.parse(attData):false;
	if(attData){
		$.each(attData,function(i,e){
			var fileName = e.fileName;
			var fileType = e.fileType;
			if(fileType!="jpg" && fileType!="pdf"){
				fileType = "unknow";
			}
			var fileSize = (e.fileSize/1024.0).toFixed(2);
			var html = "<div class='fileItem' data-json='" + JSON.stringify(e) + 
					   "'><div class='eventBox'>" + 
					   "<div class='fileIcon icon_file icon_file_" + fileType + "'></div>" +
					   "<div class='fileName'>" + fileName + "</div>" +
					   "<div class='fileSize'>" + fileSize + "KB</div>" +
					   "</div><div class='fileClose'></div></div>";
			atts.append(html);
		});
	}
	
	$attInput.parent().click(function(){
		attData = $attInput.val();
		attData = (attData)?JSON.parse(attData):false;
		var len = 0;
		if(attData){
			len = attData.length;
			if(attMax-len<=0){
				dd.device.notification.alert({"message":"只能上传"+attMax+"个附件","title":dd_res.tip,"buttonName":dd_res.okButton})
				return;
			}
		}
		var data = Comm.getData("dingding/spaceid.do",{"_method":"get"});
		dd.biz.util.uploadAttachment({
		    image:{multiple:true,compress:false,max:attMax-len,spaceId: data},
		    space:{corpId:dd_config.corpId,spaceId:data,isCopy:1 , max:attMax-len},
		    file:{spaceId:data,max:attMax-len},
		    types:["photo","camera","file","space"],
		    onSuccess : function(result) {
		    	$.each(result.data,function(i,e){
					var fileName = e.fileName;
					var fileType = e.fileType;
					if(fileType!="jpg" && fileType!="pdf"){
						fileType = "unknow";
					}
					var fileSize = (e.fileSize/1024.0).toFixed(2);
					var html = "<div class='fileItem' data-json='" + JSON.stringify(e) + 
							   "'><div class='eventBox'>" + 
							   "<div class='fileIcon icon_file icon_file_" + fileType + "'></div>" +
							   "<div class='fileName'>" + fileName + "</div>" +
							   "<div class='fileSize'>" + fileSize + "KB</div>" +
							   "</div><div class='fileClose'></div></div>";
					atts.append(html);
				})
		    	var data;
		    	if(attData){
	    			data = attData.concat(result.data);
		    	}else{
		    		data = result.data;
		    	}
		    	$attInput.val(JSON.stringify(data));
				atts.off("click").on("click",".eventBox",function(){
					Comm.getData("dingding/spaceid.do",{"_method":"get","type":"download"});
					var e = JSON.parse($(this).parent().attr("data-json"));
					e.corpId = dd_config.corpId;
					dd.biz.cspace.preview(e);
				});
				$(".fileClose").click(function(){
					$(this).parent().remove();
					var arr = [];
					$(".fileItem").each(function(){
						var obj = JSON.parse($(this).attr("data-json"));
						arr.push(obj);
					});
					$attInput.val(JSON.stringify(arr));
				});
		    },
		    onFail : function(err) {
		    	alert(JSON.stringify(err));
		    }
		});
				
	});
}

