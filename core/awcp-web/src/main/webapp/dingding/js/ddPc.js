var ddPc = {
			
	/* 钉钉Pc端图片组件处理Js脚本	*/
	/**初始化图片数据
	 * imageData:所有上传图片的src
	 * $imgList：图片所在的div
	 */
	imgInit : function(imageData,$imgList){
		if(imageData && $imgList){
			var data = imageData.split(",");
			$.each(data,function(i,e){
				var html = '<li><div class="createreport_del_div">' + 
						   '<a class="createreport_del_a" href="javascript:void(0)" onclick="ddPc.imgDelete(this)"></a>' + 
						   '<img class="uploadImg" src="' + e + '"></div></li>';
				$imgList.prepend(html);
			});	
			//图片回显
			$imgList.find(".uploadImg").bind("click",function(){
				dd.biz.util.previewImage({
					"urls":data,
					"current":this.src
				});
			});
		}
	},
		
	/**
	 * 删除图片
	 */
	imgDelete : function(obj){
		var $imgList = $(obj).parent().parent().parent();
		$(obj).parent().parent().remove();
		ddPc.setImgInput($imgList);
	},
	
	/**
	 * 设置保存图片数据的隐藏框的值
	 */
	setImgInput : function($imgList){
		var arr = [];
		$imgList.find(".uploadImg").each(function(){
			arr.push($(this).attr("src"));
		});
		$imgList.next().val(arr.join());
	},
	
	/**
	 * 上传图片
	 * imgMax：最多允许上传图片的个数
	 */
	uploadImage : function(imgMax,$imgInput,$imgList){
		var imageData = $imgInput.val();
		var len = 0;
		if(imageData){
			len = imageData.split(",").length;
			if(imgMax-len <= 0){
				dd.device.notification.alert({
					"message":dd_res.imgMax.replace("imgMax",imgMax),
					"title":dd_res.tip,
					"buttonName":dd_res.okButton
				});
				return false;
			}
		}
		dd.biz.util.uploadImage({
			"compression":true,
			"multiple": true, //是否多选，默认false
			"max": imgMax-len, //最多可选个数
			onSuccess: function(data) {
				for(var i=0;i<data.length;i++){
					var html = '<li><div class="createreport_del_div">' + 
					   		   '<a class="createreport_del_a" href="javascript:void(0)" onclick="ddPc.imgDelete(this)"></a>' + 
					   		   '<img class="uploadImg" src="' + data[i] + '"></div></li>';
					$imgList.prepend(html);
				}
				$imgList.find(".uploadImg").unbind("click").bind("click",function(){
					dd.biz.util.previewImage({"urls":data,"current":this.src});
				});
				ddPc.setImgInput($imgList);
		    }
		});
	},
	
	
	
	
	
	/*   钉钉Pc端文件组件处理js脚本		*/
	/**
	 * 文件初始化
	 */
	initFile : function($fileTextarea,$files){
		var fileData = $fileTextarea.val();
		fileData = fileData?JSON.parse(fileData):false;
		if(fileData){
			for(var i=0;i<fileData.length;i++){
	    		var fileName = fileData[i].fileName;
	    		var fileSize = (fileData[i].fileSize/1024.0).toFixed(2);
	    		var json = encodeURIComponent(JSON.stringify(fileData[i]));
	    		var fileType = getFileCss(fileData[i].fileType);
	    		var html = '<div class="fileItem" data-json="' + json + '">' + 
	    				   '<div class="eventBox" onclick="ddPc.previewFile(this)">' +
	    				   '<div class="fileIcon icon_file ' + fileType + '"></div>' +
	    				   '<div class="fileName">' + fileName + '</div>' +
	    				   '<div class="fileSize">' + fileSize + 'KB</div></div>' +
	    				   '<div class="fileClose" onclick="ddPc.removeFile(this)"></div></div>';
	    		$files.append(html);
	    	}
		}
	},

	/**
	 * 文件上传
	 */
	uploadFile : function(fileMax,$files,$fileTextarea){
		var fileData = $fileTextarea.val();
		fileData = fileData?JSON.parse(fileData):false;
		var len = 0;
		if(fileData){
			len = fileData.length;
			if(fileMax-len <= 0){
				dd.device.notification.alert({
					"message":dd_res.fileMax.replace("fileMax",fileMax),
					"title":dd_res.tip,
					"buttonName":dd_res.okButton
				});
				return;
			}
		}
		var data = Comm.getData("dingding/spaceid.do",{"_method":"get"}) + "";
		dd.biz.util.uploadAttachment({
	        "image": {
	            "multiple": true,
	            "compress": false,
	            "max": fileMax-len,
	            "spaceId": data
	        },
	        "space": {
	            "corpId": dd_res.corpId,
	            "spaceId": data,
	            "isCopy": 1,
	            "max": fileMax-len
	        },
	        "file": {
	            "spaceId": data,
	            "max": fileMax-len
	        },
	        "types": ["photo", "camera", "file", "space"],
	        onSuccess: function (res) {
	        	var arr = res.data;
	        	for(var i=0;i<arr.length;i++){
	        		var fileName = arr[i].fileName;
	        		var fileSize = (arr[i].fileSize/1024.0).toFixed(2);
	        		var json = encodeURIComponent(JSON.stringify(arr[i]));
	        		var fileType = getFileCss(arr[i].fileType);
	        		var html = '<div class="fileItem" data-json="' + json + '">' + 
	        				   '<div class="eventBox" onclick="ddPc.previewFile(this)">' +
	        				   '<div class="fileIcon icon_file ' + fileType + '"></div>' +
	        				   '<div class="fileName">' + fileName + '</div>' +
	        				   '<div class="fileSize">' + fileSize + 'KB</div></div>' +
	        				   '<div class="fileClose" onclick="ddPc.removeFile(this)"></div></div>';
	        		$files.append(html);
	        	}
	        	ddPc.setFileTextarea($files,$fileTextarea);
	        }
		});
	},

	/**
	 * 删除文件
	 */
	removeFile : function(obj){
		var $files = $(obj).parent().parent();
		var $fileTextarea = $files.next();
		$(obj).parent().remove();
		ddPc.setFileTextarea($files,$fileTextarea);
	},

	/**
	 * 文件预览
	 */
	previewFile : function(obj){
		Comm.getData("dingding/spaceid.do",{"_method":"get","type":"download"});
		var e = JSON.parse(decodeURIComponent($(obj).parent().attr("data-json")));
		e.corpId = dd_config.corpId;
		dd.biz.cspace.preview(e);
	},

	/**
	 * 设置选中的文件的值
	 */
	setFileTextarea : function($files,$fileTextarea){
		var arr = [];
		$files.find(".fileItem").each(function(){
			arr.push(JSON.parse(decodeURIComponent($(this).attr("data-json"))));
		});
		$fileTextarea.val(JSON.stringify(arr));
	},
};


//钉钉时间组件初始化
$("input[dateType]").each(function(){
	var dateType = $(this).attr("dateType");
	if(dateType=="yyyy-mm-dd"){
		dateType = "Y-m-d";
	} else if(dateType=="yyyy-mm-dd HH:ii"){
		dateType = "Y-m-d H:i";
	} else if(dateType=="dd/mm/yyyy"){
		dateType = "d/m/Y";
	} else if(dateType=="dd/mm/yyyy HH:ii"){
		dateType = "d/m/Y H:i";
	} else {
		dateType = "Y-m-d";
	}
	$(this).datetimepicker({
		lang:"ch",
		format:dateType,
		timepicker:false,
		onChangeDateTime:function(){
			
		}
	});
});


//根据图片类型获取文件图片样式
function getFileCss(fileType){
	if(fileType.indexOf("zip")!=-1 || fileType.indexOf("rar")!=-1){
		return "icon_file_zip";
	} else if(fileType.indexOf("xsl")!=-1 || fileType.indexOf("xlsx")!=-1){
		return "icon_file_xsl";
	} else if(fileType.indexOf("doc")!=-1 || fileType.indexOf("docx")!=-1){
		return "icon_file_word";
	} else if(fileType.indexOf("ppt")!=-1 || fileType.indexOf("pptx")!=-1){
		return "icon_file_ppt";
	} else if(fileType.indexOf("pdf")!=-1){
		return "icon_file_pdf";
	} else if(fileType.indexOf("jpg")!=-1 || fileType.indexOf("jpeg")!=-1 || 
				fileType.indexOf("png")!=-1 || fileType.indexOf("gif")!=-1){
		return "icon_file_jpg";
	} else {
		return "icon_file_unknow";
	}
}


//钉钉日志页面中获取发送人
ddPc.addSendHtml = function(){
	var html = '<div class="createreport_rep_to" id="createreportRepTo" style="display: block;">' +
	  		   '<div><span class="title" id="textChooseUser">发给谁</span>' + 
	  		   '<span class="remark" id="textDeleteWhenClick">（点击头像可删除）</span></div>' +
	  		   '<div id="rep_to_list"><ul id="receiveUserList">' + 
	  		   '<li id="select_rep_to_btn" onclick="ddPc.addUser();">' + 
	  		   '<img src="' + baseUrl + 'dingding/img/pc/addUser.png"><div class="text">添加人</div></li></ul>' +
	  		   '<ul id="receiveConvList" style="display: none;">' + 
	  		   '<li class="crrp-body-headwrap" id="crrpAddConv" onclick="ddPc.addGroup();">' + 
	  		   '<div class="global-addConv"><img src="' + baseUrl + 'dingding/img/pc/addUser.png"></div>' + 
	  		   '<div class="text">添加群</div></li></ul></div></div>';
	$("#groupForm").append(html); 
	ddPc.initUsers();
}

/**
 * 新增用户
 */
ddPc.addUser = function(){
	var users = [];
	var userIds = $("input[name='Sendfor']").val();
	if(userIds){
		users = userIds.split(",");
	}
	dd.biz.contact.choose({
		"startWithDepartmentId":0,
		"isNeedSearch":true,
		"multiple":true,
		corpId:dd_config.corpId,	
		title:"选择用户",
		users:users,
		max: 60,//返回最多一个人数据
		onSuccess: function(data) {
			ddPc.setUsers(data,true);
	    },
	    onFail: function(err) {
	    	console.log(JSON.stringify(err));
	    }
	});
}

//用名字整出来的用户头像的随机背景色
function getRandomColor(){
	var arr = ["#5e97f6","#ff8e6b","#5ec9f6","#f65e5e","#78919d","#9a89b9"];
	return arr[Math.floor(Math.random()*arr.length)];
}

/**
 * 初始化用户数据
 */
ddPc.initUsers = function(){
	var userIds = $("input[name='Sendfor']").val();
	if(userIds){
		var userIds = "'" + userIds.replace(/,/g,"','") + "'";
		var data = Comm.getData("api/execute/getUserNameAndImg",{ddUserIds:userIds});
		ddPc.setUsers(data,false);
	}	
}

/**
 * 设置用户显示
 */
ddPc.setUsers = function(data,bool){
	$("#select_rep_to_btn").prevAll().remove();
	if(bool){
		var names = [];	//存储用户名
		var ids = [];	//存储钉钉用户ID
	}	
	for(var i=0;i<data.length;i++){
		var name = data[i].name;		//用户姓名
		var emplId = data[i].emplId;	//钉钉用户ID
		var img = data[i].avatar || data[i].img;		//用户头像
		if(bool){
			names.push(name);
			ids.push(emplId);
		}		
		if(img){//有头像时
			img = '<img src="' + img + '">';
		}
		else{//没头像，用用户名做一个头像
			img = '<div class="userLogo" style="background-color:' + getRandomColor() + '">' + 
					Comm.handleName(name) + '</div>';
		}
		var html = '<li onclick="ddPc.deleteUser(this)" data-userId="' + emplId + '" data-name="' + name + '">' + img + 
				   '<div class="text">' + name + '</div></li>';
		$("#select_rep_to_btn").before(html);	
	}	
	if(bool){
		$("input[name='Sendfor']").val(ids.join());
		$("input[name='username']").val(names.join());
	}
}

/**
 * 删除用户
 */
ddPc.deleteUser = function(obj){
	$(obj).remove();
	var ids = [];
	var names = [];
	$("#select_rep_to_btn").prevAll().each(function(){
		ids.push($(this).attr("data-userId"));
		names.push($(this).attr("data-name"));
	});
	$("input[name='Sendfor']").val(ids.join());
	$("input[name='username']").val(names.join());
}




//钉钉审核选人
ddPc.addApprovalHtml = function(){
	var html = '<div><div class="webApproverField ddComponent"><label class="ddLabel">审批人</label>' +
			   '<div class="ddEdit"><span class="ddAddBtn">添加审批人</span>' + 
			   '<ul class="approverLine approverList"></ul><span></span></div></div>' + 
			   '<div class="webApproverField ddComponent"><label class="ddLabel">抄送人</label><div class="ddEdit">' + 
			   '<ul class="approverLine ccList"><div class="addPersonBox" ><span>添加抄送人</span></div></ul></div></div></div>';
	$("#groupForm").append(html);
	
	//添加执行人
	$(".ddAddBtn").click(function(){
		dd.biz.contact.choose({
			corpId:dd_config.corpId,	
			"startWithDepartmentId":0,
			"isNeedSearch":true,
			"multiple":true,
			title:"选择用户",
			onSuccess: function(data) {
				var arrow = '<div class="approverNewArrowContent">' + 
				'<img class="approverNewArrow " src="https://gw.alicdn.com/tps/TB1Cfh9OFXXXXbzXpXXXXXXXXXX-54-33.png" > </div>';
				var html = getUserLists(data);		
				if($(".approver").length!=0){
					html = arrow + html;
				}
				$(".approverList").append(html);
				addEvent();
				setSlectsUserIds();
		    },
		    onFail: function(err) {
		    	console.log(JSON.stringify(err));
		    }
		});	
	});
	
	$(".ddAddBtn").hide();
	
	//添加抄送人
	$(".addPersonBox").click(function(){
		dd.biz.contact.choose({
			corpId:dd_config.corpId,	
			"startWithDepartmentId":0,
			"isNeedSearch":true,
			"multiple":true,
			title:"选择用户",
			onSuccess: function(data) {
				$(".ccList").prepend(getUserLists(data));
				addEvent();
				setCClist();
		    },
		    onFail: function(err) {
		    	console.log(JSON.stringify(err));
		    }
		});
	});
	
	$(".addPersonBox").hide();
	
	var data = Comm.getData("dingding/getFlowExcutor.do",{dynamicPageId:$("#dynamicPageId").val()});	
	if(data){
		$("#slectsUserIds").val(JSON.stringify(data));
		var arr = data;
		var ddUserIds = "'" + arr.join().replace(/,/g,"','") + "'";
		var users = Comm.getData("api/execute/getUserNameAndImg",{ddUserIds:ddUserIds});
		console.log(users);
		var html = "";
		for(var i=0;i<arr.length;i++){
			var subArr = arr[i].split(",");	
			for(var j=0;j<subArr.length;j++){
				var emplId = subArr[j];
				var nameAndImg = getUserNameAndImg(users,emplId);
				var img = nameAndImg.img;
				var name = nameAndImg.name;	
				if(!img){
					img = '<div class="approverColor tFBH tFBAC tFBJC" style="background:' + getRandomColor() +
						  '" ><span>' + Comm.handleName(name) + '</span></div>';
				} else{
					img = '<img src="' + img + '">';
				} 
				html += '<li class="approver" data-userId="' + emplId + 
						'"><div class="approverAvatar"><span class="deleteAvatar">╳</span>' + img + 
						'<div class="approverName">' + name + '</div></div></li>';
			}
			if(i != arr.length-1){
				html += '<div class="approverNewArrowContent">' + 
				'<img class="approverNewArrow " src="https://gw.alicdn.com/tps/TB1Cfh9OFXXXXbzXpXXXXXXXXXX-54-33.png" > </div>';
			}
		}
		$(".approverList").append(html);
		//addEvent();
		setSlectsUserIds();
	}
	
	var data = Comm.getData("api/execute/getCCList",{dynamicPageId:$("#dynamicPageId").val()});
	if(data){
		$("#CC_slectsUserIds").val(data.JSON);
		var arr = JSON.parse(data.JSON);
		var ddUserIds = "'" + arr.join().replace(/,/g,"','") + "'";
		var users = Comm.getData("api/execute/getUserNameAndImg",{ddUserIds:ddUserIds});
		var html = "";
		for(var i=0;i<arr.length;i++){
			var emplId = arr[i];
			var nameAndImg = getUserNameAndImg(users,emplId);
			var img = nameAndImg.img;
			var name = nameAndImg.name;	
			if(!img){
				img = '<div class="approverColor tFBH tFBAC tFBJC" style="background:' + getRandomColor() +
					  '" ><span>' + Comm.handleName(name) + '</span></div>';
			} else{
				img = '<img src="' + img + '">';
			} 
			html += '<li class="approver" data-userId="' + emplId + 
					'"><div class="approverAvatar"><span class="deleteAvatar">╳</span>' + img + 
					'<div class="approverName">' + name + '</div></div></li>';
		}
		$(".ccList").prepend(html);
		//addEvent();
		setCClist();
	}
	
}

function getUserNameAndImg(users,emplId){
	var nameAndImg = {};
	for(var i=0;i<users.length;i++){
		if(emplId == users[i].emplId){
			nameAndImg.img = users[i].img;
			nameAndImg.name = users[i].name;
		}
	}
	return nameAndImg;
}

//删除事件
function addEvent(){
	$(".approverList").find(".deleteAvatar").unbind("click").bind("click",function(){
		var $approver = $(this).parent().parent();
		if($approver.next(".approverNewArrowContent").length==1
				&& ($approver.prev().length==0 || $approver.prev(".approverNewArrowContent").length==1)){
			$approver.next(".approverNewArrowContent").remove();
		}
		if($approver.prev(".approverNewArrowContent").length==1
				&& ($approver.next().length==0 || $approver.next(".approverNewArrowContent").length==1)){
			$approver.prev(".approverNewArrowContent").remove();
		}
		$approver.remove();
		setSlectsUserIds();
	});
	
	$(".ccList").find(".deleteAvatar").unbind("click").bind("click",function(){
		$(this).parent().parent().remove();
		setCClist();
	});
}

function getUserLists(data){
	var html = "";
	for(var i=0;i<data.length;i++){
		var name = data[i].name;		//用户姓名
		var emplId = data[i].emplId;	//钉钉用户ID
		var img = data[i].avatar;		//用户头像
		if(!img){
			img = '<div class="approverColor tFBH tFBAC tFBJC" style="background:' + getRandomColor() +
				  '" ><span>' + Comm.handleName(name) + '</span></div>';
		} else{
			img = '<img src="' + img + '">';
		} 
		html += '<li class="approver" data-userId="' + emplId + 
				'"><div class="approverAvatar"><span class="deleteAvatar">╳</span>' + img + 
				'<div class="approverName">' + name + '</div></div></li>'
	}
	return html;
}

function setCClist(){
	var users = [];
	$(".ccList").find(".approver").each(function(){
		users.push($(this).attr("data-userId"));
	});
	$("#CC_slectsUserIds").val(JSON.stringify(users));
}

function setSlectsUserIds(){
	var users = [];
	var temp = [];
	$(".approverList").find(".approver").each(function(){
		if($(this).next(".approverNewArrowContent").length == 1){
			temp.push($(this).attr("data-userId"));
			users.push(temp.join());
			temp = [];
		} else{
			temp.push($(this).attr("data-userId"));
		}
	});
	if(temp.length != 0){
		users.push(temp.join());
	}
	$("#slectsUserIds").val(JSON.stringify(users));
}






