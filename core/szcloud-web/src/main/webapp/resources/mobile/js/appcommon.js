
var userid_str=new Array();//存储用户选择的人的ID集合
var username_str=new Array();
var trappend;
var allId;
var params;
	$(document).on("pageinit","#hospage2",function(){ // 当进入页面二时
		juese();
	});
		
	function selectUser(id,type,param)
	{
		params=param;
		if(type == "transfer")
		{
			allId = id;
			location.href="#hospage2";
			$("#deliver").val("transfer");
		}else if(type == "deliver")
		{
			location.href="#hospage2";
			$("#deliver").val("deliver");
		}else if(type == "GuDing")
		{
			location.href="#hospage3";
		}
	}

	function plaseMeanSelectUser()
	{
		$("#GuDing").val("GuDing");
		location.hash="#hospage2";

	}


	function loadpage(){
		 $(".ui-navbar ui-tabbar ul li").removeClass("ui-state-hover");
    	 $("#header .ui-input-search").css("margin-top","7px").css("margin-bottom","5px").css("margin-left","40px").css("margin-right","80px");
   		 $("#chen_test li").removeClass("ui-li-static ui-body-inherit");
    	 $("#chen_test_cylxr li").removeClass("ui-li-static ui-body-inherit");
    	 $("#chen_test_cybm li").removeClass("ui-li-static ui-body-inherit");
    	 $(".ui-collapsible-heading-toggle").css("padding","0px").css("padding-top","5px").css("padding-bottom","2px").css("border-left","0px").css("border-right","0px").removeClass("ui-btn-icon-left").addClass("ui-btn-icon-right");
	}
	
//点击分组
function fenzu(){
	$("#one").hide();
	$("#three").hide();
	$("#four").hide();
	$("#two").show();
	$.ajax({
    	url:basePath+"UserGroup/queryGroups.do?",
    	type:"POST", 
    	async:false,
    	dataType:'JSON',
    	success:function(msg){
    		var html="";
			html+="<div data-role='collapsibleset'>";
			for(var i=0;i<msg.length;i++){
				html+="<div data-role='collapsible'  data-collapsed='true'>";
	        	html+="<h6 id='"+msg[i].ID+"' onclick='fz_click_name(this.id)'>";      		 
	        	html+="<a href='#' data-rel='dialog' data-transition='slideup' id='fz"+msg[i].ID+"_div_a'>";      		 	
            	html+="<div class='ui-btn-group-contain' style='width: 55px;margin-left:5px;'>";
				html+="<span class='ui-link ui-btn ui-btn-orange  ui-btn-inline ui-shadow ui-corner-all' role='button'  data-role='button' data-icon='home' data-inline='true' data-iconpos='notext' style='padding:8px;width:30px;'><i class='icon-group' style='padding-top:0.25em;font-size:24px'></i></span>";                                    
            	html+="</div>";                    
            	html+="<h3 style='margin-top:0.8em;font-size:16px;color:#333;'>"+msg[i].GroupName+"</h3>";                    
            	html+="</a>";                
	        	html+="</h6>";      		 
	        	html+="<fieldset data-role='controlgroup'  data-iconpos='right'> ";      		 
	        	html+="<ul data-role='listview' id='chen_"+msg[i].ID+"'  data-inset='true' data-filter='true' data-filter-placeholder='搜索姓名 ...'>";
						    
				html+="</ul>";
				html+=" </fieldset>";
				html+="</div>";
			}
			html+="</div>";
      		$("#two").html(html);
      		$("#two").trigger('create');
      		$(".ui-collapsible-heading-toggle").css("padding","0px").css("padding-top","5px").css("padding-bottom","2px").css("border-left","0px").css("border-right","0px").removeClass("ui-btn-icon-left").addClass("ui-btn-icon-right");
    	}
    });
}

function bumen(){
	$("#one").show();
	$("#two").hide();
	$("#three").hide();
	$("#four").hide();
}
//点击角色
function juese(){
$("#one").hide();
$("#two").hide();
$("#four").hide();
$("#three").show();
	$.ajax({
		url:basePath+"unit/listRolesInSysByAjax.do?",
		type:"POST", 
    	async:false,
    	data:{'boxs':110},
    	dataType:'JSON',
    	success:function(msg){
    		var html="";
			html+="<div data-role='collapsibleset'>";
			for(var i=0;i<msg.length;i++){
				html+="<div data-role='collapsible'  data-collapsed='true'>";
	        	html+="<h6 id='"+msg[i].roleId+"' onclick='js_click_name(this.id)'>";      		 
	        	html+="<a href='#' data-rel='dialog' data-transition='slideup' id='js"+msg[i].roleId+"_div_a'>";      		 	
            	html+="<div class='ui-btn-group-contain' style='width: 55px;margin-left:5px;'>";
				html+="<span class='ui-link ui-btn ui-btn-orange  ui-btn-inline ui-shadow ui-corner-all' role='button'  data-role='button' data-icon='home' data-inline='true' data-iconpos='notext' style='padding:8px;width:30px;'><i class='icon-group' style='padding-top:0.25em;font-size:24px'></i></span>";                                    
            	html+="</div>";                    
            	html+="<h3 style='margin-top:0.8em;font-size:16px;color:#333;'>"+msg[i].roleName+"</h3>";                    
            	html+="</a>";                
	        	html+="</h6>";      		 
	        	html+="<fieldset data-role='controlgroup'  data-iconpos='right'> ";      		 
	        	html+="<ul data-role='listview' id='chenjuese_"+msg[i].roleId+"'  data-inset='true' data-filter='true' data-filter-placeholder='搜索姓名 ...'>";
						    
				html+="</ul>";
				html+=" </fieldset>";
				html+="</div>";
			}
			html+="</div>";
      		$("#three").html(html);
      		$("#three").trigger('create');
      		$(".ui-collapsible-heading-toggle").css("padding","0px").css("padding-top","5px").css("padding-bottom","2px").css("border-left","0px").css("border-right","0px").removeClass("ui-btn-icon-left").addClass("ui-btn-icon-right");
    	}
    });
}
//点击岗位
function gangwei(){
$("#one").hide();
$("#two").hide();
$("#three").hide();
$("#four").show();
	$.ajax({
    	url:basePath+"punPositionController/pageListByAjax.do?groupId=529614",
    	type:"GET", 
    	async:false,
    	dataType:'JSON',
    	success:function(msg){
    		var html="";
			html+="<div data-role='collapsibleset'>";
			for(var i=0;i<msg.length;i++){
				html+="<div data-role='collapsible'  data-collapsed='true'>";
	    		html+="<h6 id='"+msg[i].positionId+"' onclick='gw_click_name(this.id)'>";      		 
	    		html+="<a href='#' data-rel='dialog' data-transition='slideup' id='gw"+msg[i].positionId+"_div_a'>";      		 	
            	html+="<div class='ui-btn-group-contain' style='width: 55px;margin-left:5px;'>";
				html+="<span class='ui-link ui-btn ui-btn-orange  ui-btn-inline ui-shadow ui-corner-all' role='button'  data-role='button' data-icon='home' data-inline='true' data-iconpos='notext' style='padding:8px;width:30px;'><i class='icon-group' style='padding-top:0.25em;font-size:24px'></i></span>";                                    
            	html+="</div>";                    
            	html+="<h3 style='margin-top:0.8em;font-size:16px;color:#333;'>"+msg[i].name+"</h3>";                    
            	html+="</a>";                
	        	html+="</h6>";      		 
	        	html+="<fieldset data-role='controlgroup'  data-iconpos='right'> ";      		 
	        	html+="<ul data-role='listview' id='chengangwei_"+msg[i].positionId+"'  data-inset='true' data-filter='true' data-filter-placeholder='搜索姓名 ...'>";
						    
				html+="</ul>";
				html+=" </fieldset>";
				html+="</div>";
			}
			html+="</div>";
      		$("#four").html(html);
      		$("#four").trigger('create');
      		$(".ui-collapsible-heading-toggle").css("padding","0px").css("padding-top","5px").css("padding-bottom","2px").css("border-left","0px").css("border-right","0px").removeClass("ui-btn-icon-left").addClass("ui-btn-icon-right");
    	}
    });
}

//点击分组
function fz_click_name(groupid){
	if($("#fz"+groupid+"_div_a").parent().hasClass("ui-icon-plus")){
		$.ajax({
    		url:basePath+"UserGroup/queryUser.do",
    		type:"POST", 
    		async:false,
    		data:{'GroupID':groupid},
    		dataType:'JSON',
    		success:function(msg){
    			var html="";
    			for(var i=0;i<msg.length;i++){
    				var aa="";
					if(i==0){
						aa="ui-first-child";
					}
					if(i==msg.length-1){
						aa="ui-last-child";
					}
					html+="<li class='"+aa+"'><input type='checkbox' name='fz_checkbox-"+msg[i].userId+"' id='fz_checkbox-"+msg[i].userId+"' class='custom'  onclick='click_li("+msg[i].userId+",\""+msg[i].name+"\",\"fz_checkbox-\")' /><label for='fz_checkbox-"+msg[i].userId+"'><i class='icon-user' style='font-size: 24px;'></i><span style='margin-left: 10px;'>"+msg[i].name+"</span></label></li>";
					//html+="<li class='"+aa+"'><div class='ui-checkbox'><label class='ui-btn ui-corner-all ui-btn-inherit ui-btn-icon-right "+aa+" ui-checkbox-off' for='fz_checkbox-"+msg[i].userId+"'><i class='icon-user' style='font-size: 24px;'></i><span style='line-height: 30px;margin-left: 10px;'>"+msg[i].name+"</span></label><input name='fz_checkbox-"+msg[i].userId+"' id='fz_checkbox-"+msg[i].userId+"' class='custom' type='checkbox'></div></li>";
    			}
      			$("#chen_"+groupid).html(html);
      			$("#chen_"+groupid).trigger('create');
      			$("#chen_"+groupid).removeClass("ui-li-static ui-body-inherit");
    		}
    	});	
	}else{
		$("#chen_"+groupid).empty();
	}
}
//点击角色
function js_click_name(positionid){
	if($("#js"+positionid+"_div_a").parent().hasClass("ui-icon-plus")){
		$.ajax({
    		url:basePath+"punUserGroupController/getUserListByWhere.do",
    		type:"POST", 
    		async:false,
    		data:{'role':positionid},
    		dataType:'JSON',
    		success:function(msg){
    			var html="";
    			for(var i=0;i<msg.length;i++){
    				var aa="";
					if(i==0){
						aa="ui-first-child";
					}
					if(i==msg.length-1){
						aa="ui-last-child";
					}
					html+="<li class='"+aa+"'><input type='checkbox' name='js_checkbox-"+msg[i].userId+"' id='js_checkbox-"+msg[i].userId+"' class='custom' onclick='click_li("+msg[i].userId+",\""+msg[i].name+"\",\"js_checkbox-\")'/><label for='js_checkbox-"+msg[i].userId+"'><i class='icon-user' style='font-size: 24px;'></i><span style='margin-left: 10px;'>"+msg[i].name+"</span></label></li>";
					
					//html+="<li class='"+aa+"' onclick='click_li("+msg[i].userId+",\""+msg[i].name+"\",\"js_checkbox-\")'><div class='ui-checkbox'><label class='ui-btn ui-corner-all ui-btn-inherit ui-btn-icon-right "+aa+" ui-checkbox-off' for='js_checkbox-"+msg[i].userId+"'><i class='icon-user' style='font-size: 24px;'></i><span style='line-height: 30px;margin-left: 10px;'>"+msg[i].name+"</span></label><input name='js_checkbox-"+msg[i].userId+"' id='js_checkbox-"+msg[i].userId+"' class='custom' type='checkbox'></div></li>";
    			}
      			$("#chenjuese_"+positionid).html(html);
      			$("#chenjuese_"+positionid).trigger('create');
      			$("#chenjuese_"+positionid).removeClass("ui-li-static ui-body-inherit");
    		}
    	});	
	}else{
		$("#chenjuese_"+positionid).empty();
	}
}
//点击岗位
function gw_click_name(positionid){
	if($("#gw"+positionid+"_div_a").parent().hasClass("ui-icon-plus")){
		$.ajax({
    		url:basePath+"punUserGroupController/getUserListByWhere.do",
    		type:"POST", 
    		async:false,
    		data:{'job':positionid},
    		dataType:'JSON',
    		success:function(msg){
    			var html="";
    			for(var i=0;i<msg.length;i++){
    				var aa="";
					if(i==0){
						aa="ui-first-child";
					}
					if(i==msg.length-1){
						aa="ui-last-child";
					}
					html+="<li class='"+aa+"'><input type='checkbox' name='gw_checkbox-"+msg[i].userId+"' id='gw_checkbox-"+msg[i].userId+"' class='custom' onclick='click_li("+msg[i].userId+",\""+msg[i].name+"\",\"gw_checkbox-\")'/><label for='gw_checkbox-"+msg[i].userId+"'><i class='icon-user' style='font-size: 24px;'></i><span style='margin-left: 10px;'>"+msg[i].name+"</span></label></li>";
					
					//html+="<li class='"+aa+"' onclick='click_li("+msg[i].userId+",\""+msg[i].name+"\",\"gw_checkbox-\")'><div class='ui-checkbox'><label class='ui-btn ui-corner-all ui-btn-inherit ui-btn-icon-right "+aa+" ui-checkbox-off' for='gw_checkbox-"+msg[i].userId+"'><i class='icon-user' style='font-size: 24px;'></i><span style='line-height: 30px;margin-left: 10px;'>"+msg[i].name+"</span></label><input name='gw_checkbox-"+msg[i].userId+"' id='gw_checkbox-"+msg[i].userId+"' class='custom' type='checkbox'></div></li>";
    			}
      			$("#chengangwei_"+positionid).html(html);
      			$("#chengangwei_"+positionid).trigger('create');
      			$("#chengangwei_"+positionid).removeClass("ui-li-static ui-body-inherit");
    		}
    	});	
	}else{
		$("#chengangwei_"+positionid).empty();
	}
}
//删除数组中的某个元素
Array.prototype.indexOf_shuzu = function(val) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == val) return i;
	}
	return -1;
};
Array.prototype.remove_shuzu = function(val) {
	var index = this.indexOf_shuzu(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};
//列表点击的方法
function click_li(userid,username,sub_id){
	var ischeck=$("#"+sub_id+userid).attr("data-cacheval");
	if(ischeck=="true"){
		userid_str.remove_shuzu(userid);
		username_str.remove_shuzu(username);
		$(".choice-list li a").each(function(){
			if($(this).attr("data-id")==userid){
				$(this).parent().remove();
			}
		});
	}else{
		userid_str.push(userid);
		username_str.push(username);
		$(".choice-list").append("<li><span>"+username+"</span><a class='choice-close' data-id='"+userid+"' onclick='delete_li("+userid+",\""+username+"\")'>×</a></li>");
	}
}
//点击上面×的方法(注：需要把列表中的复选框去除选中)
function delete_li(userid,username){
	$(".choice-list li a").each(function(){
		if($(this).attr("data-id")==userid){
			$(this).parent().remove();
			userid_str.remove_shuzu(userid);
			username_str.remove_shuzu(username);
			$("body").find("*[id*='"+userid+"']").each(function(i,ele){
				$(ele).prev().removeClass("ui-checkbox-on").addClass("ui-checkbox-off");
        		$(ele).attr("data-cacheval","true");
  			});
		}
	});
}
//确定
function confirm_data(){
	
	if($("#deliver").val() == "deliver")
	{
		if(userid_str.length>0){
			//调用转发
			var nameJson= eval("({\"name\":\"name\",\"value\":\"" +$("#sendMobileFlowName").val() + "\"})");
			var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + $("#suggestion").val() + "\"})");
			var activityJson= eval("[{\"name\":\"activity" +$("#sendMobileFlowResult").val()+ "\",\"value\":\"" + userid_str + "\"}]");
			activityJson.push(nameJson);
			activityJson.push(commentJson);
			$("#slectsUserIds").val(JSON.stringify(activityJson));
			AJAXMobile(2);
		}else{
			 $.alert.open("请至少选择一个人员。");
		}
		
		
	}else if($("#deliver").val() == "transfer")
	{
		//回显当前所选用户*/
		$("#"+allId).val(username_str);
		$("#"+allId).parent().prev().val(userid_str);
		location.href="#hospage";
	}else if($("#GuDing").val() == "GuDing")
	{
			var choice=$("#choice").val();
			var nameJson= eval("({\"name\":\"name\",\"value\":\"" +$("#sendMobileFlowName").val() + "\"})");
			var commentJson= eval("({\"name\":\"comment\",\"value\":\"" + $("#suggestion").val() + "\"})");
			var activityJson= eval("[{\"name\":\"activity" +choice+ "\",\"value\":\"" + userid_str + "\"}]");
			var choiceJson= eval("({\"name\":\"choice\",\"value\":\"" + choice + "\"})");
					
			activityJson.push(nameJson);
			activityJson.push(commentJson);
			activityJson.push(choiceJson);
			$("#slectsUserIds").val(JSON.stringify(activityJson));
			AJAXMobile(2);

	}
	
}