var selectUsers = [];
function setApprovalPerson(){
	if($("#WorkID").val()){	//流程已经开启时,不执行下面代码
		return false;
	}
	var data = Comm.getData("dingding/getFlowExcutor.do",{dynamicPageId:$("#dynamicPageId").val()});//当抛出异常时,不执行后面代码
	var html = '<div class="approver-content"><div><div class="approverContainer">' +
		   	   '<div class="approverTitle">' + dd_res.approver + '</div>' +
		   	   '<ul class="approverLineFlow"><div class="add-person-pannel">' + 
		   	   '<img src="' + baseUrl + 'dingding/img/add2.png" class="tIcon icon tingle-add-person approver-icon">' + 
		   	   '</div></ul></div></div></div>';
	$("#buttons").before(html);	
	$("#slectsUserIds").val(JSON.stringify(data));
	if(data){
		var arr = data;
		var ddUserIds = "'" + arr.join().replace(/,/g,"','") + "'";
		var users = Comm.getData("api/execute/getUserNameAndImg",{ddUserIds:ddUserIds});
		var html = "";
		for(var i=0;i<arr.length;i++){
			var subArr = arr[i].split(",");
			for(var j=0;j<subArr.length;j++){
				var emplId = subArr[j];
				var nameAndImg = getUserNameAndImg(users,emplId);
				var avatar = nameAndImg.img;
				var name = nameAndImg.name;				
				var img = "";
				if(avatar){
					img = '<img src="' + avatar + '" />';
				} else{
					img = '<div class="approverColor tFBH tFBAC tFBJC">' + Comm.handleName(name) + '</div>';
				}
				html += '<li class="approver" data-userId="' + emplId + '"><div class="approverAvatar">' + img + 
						'<div class="approverName">' + name + '</div></div></li>';
			}
			if(i != arr.length-1){
				html += '<div class="approverNewArrowContent" >' +
				'<img class="approverNewArrow " src="' + baseUrl + 'dingding/img/arrow.png" > </div>';
			}
		}
		$(".add-person-pannel ").before(html);
		$(".add-person-pannel ").hide();//不让添加
		//addApprover();	
		$(".approverContainer").css("padding-bottom","0px");
		$(".approverLineFlow").css("overflow","hidden");
		//addEvent();//不让删除
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

function addEvent(){
	$(".approver-content .approver").unbind("click").bind("click",function(){
		var $this = $(this);
		if($this.next(".approverNewArrowContent").length==1
				&& ($this.prev().length==0 || $this.prev(".approverNewArrowContent").length==1)){
			$this.next(".approverNewArrowContent").remove();
		}
		$(this).remove();
		var children = $(".approverLineFlow").children().get();
		var tempArr = [];
		selectUsers = [];
		for(var i=0;i<children.length;i++){
			var child = children[i];
			if($(child).hasClass("approver")){
				tempArr.push($(child).attr("data-userid"));
			} if($(child).hasClass("approverNewArrowContent")){
				selectUsers.push(tempArr.join(","));
				tempArr = [];
			} if($(child).hasClass("add-person-pannel")){
				$("#slectsUserIds").val(JSON.stringify(selectUsers));
			} 
		}
	});
}

function addApprover(){
	$(".add-person-pannel").click(function(){
		dd.biz.contact.choose({
			"startWithDepartmentId":0,
			"isNeedSearch":true,
			"multiple":true,
			"corpId":dd_config.corpId,
			"users":[],
			"disabledUsers":[],
			"title":"选择用户",
			onSuccess: function(data) {
				var html = "";
				var arr = [];
				for(var i=0;i<data.length;i++){
					var avatar = data[i].avatar;
					var name = data[i].name;
					var emplId = data[i].emplId;
					arr.push(emplId);
					var img = "";
					if(avatar){
						img = '<img src="' + avatar + '" />';
					} else{
						img = '<div class="approverColor tFBH tFBAC tFBJC">' + Comm.handleName(name) + '</div>';
					}
					html += '<li class="approver" data-userId="' + emplId + '"><div class="approverAvatar">' + img + 
							'<div class="approverName">' + name + '</div></div></li>';
				}
				html += '<div class="approverNewArrowContent" >' +
						'<img class="approverNewArrow " src="' + baseUrl + 'dingding/img/arrow.png" > </div>';
				selectUsers.push(arr.join(","));
				$("#slectsUserIds").val(JSON.stringify(selectUsers));
				$(".add-person-pannel ").before(html);		
				addEvent();
		    },
		    onFail: function(err) {
		    	log.e(JSON.stringify(err));
		    }
		});				
	});
}

//根据抄送人预设生成抄送人
var ccUsers = [];
function addCCPerson(){
	if($("#WorkID").val()){//流程已经开启时,不执行下面代码
		return false;
	}
	var html = '<div class="approvalCarbonCopy" style="margin-bottom: 58px;">' + 
			   '<div class="approverContainer carboncopy-content" style="padding-bottom:0px;">' +
			   '<div class="CarbonCopyApproverSubtitle" ><span class="approverBigTitle">' + dd_res.ccList + '</span></div>' +
			   '<ul class="carbon-approverLine">' +
			   '<div class="addPersonBox">' + 
			   '<img class="tIcon icon tingle-add-person" src="' + baseUrl + 'dingding/img/add2.png" /></div>' +
			   '</ul></div></div>';
	$("#buttons").before(html);
	
	var data = Comm.getData("api/execute/getCCList",{dynamicPageId:$("#dynamicPageId").val()});
	if(data && data.JSON){
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
				img = '<div class="approve-node-avatar">' + Comm.handleName(name) + '</div>';
			} else{
				img = '<img class="approve-node-avatar" style="margin-bottom: -5px;" src="' + img + '" />';
			} 
			html += '<li class="approver" data-userId="' + emplId + '"><div class="approverUser carbonCopyUser">' + img + 
					'<div class="approverName">' + name + '</div></div></li>';
		}
		$(".addPersonBox").before(html);
		$(".addPersonBox").hide(); //不让新增
		//addCC();
	}
	
}

function addCC(){
	$(".addPersonBox").click(function(){
		dd.biz.contact.choose({
			"startWithDepartmentId":0,
			"isNeedSearch":true,
			"multiple":true,
			"corpId":dd_config.corpId,
			"users":ccUsers,
			"disabledUsers":[],
			"title":"选择用户",
			onSuccess: function(data) {
				var html = "";
				for(var i=0;i<data.length;i++){
					var avatar = data[i].avatar;
					var name = data[i].name;
					var emplId = data[i].emplId;
					ccUsers.push(emplId);
					var img = "";
					if(avatar){
						img = '<img class="approve-node-avatar" style="margin-bottom: -5px;" src="' + avatar + '" />';
					} else{
						img = '<div class="approve-node-avatar">' + Comm.handleName(name) + '</div>';
					}
					html += '<li class="approver" data-userId="' + emplId + '"><div class="approverUser carbonCopyUser">' + img + 
							'<div class="approverName">' + name + '</div></div></li>';
				}
				$(".addPersonBox").prevAll().remove();
				$(".addPersonBox").before(html);
				$("#CC_slectsUserIds").val(JSON.stringify(ccUsers));
				$(".approvalCarbonCopy .approver").unbind("click").bind("click",function(){
					$(this).remove();
					ccUsers = [];
					$(".carbon-approverLine").children(".approver").each(function(){
						ccUsers.push($(this).attr("data-userid"));
					});
					$("#CC_slectsUserIds").val(JSON.stringify(ccUsers));
				});
		    },
		    onFail: function(err) {
		    	log.e(JSON.stringify(err));
		    }
		});				
	});
}