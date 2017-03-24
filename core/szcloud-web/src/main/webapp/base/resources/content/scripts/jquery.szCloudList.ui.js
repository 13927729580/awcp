// JavaScript Document
$(function(){
	//drop-down menu of style changing
	var styleNav;
	jQuery(".multiUl li").hover(function(){
	var curItem = jQuery(this);
		styleNav = setTimeout(function(){//延时触发
			curItem.find("blockquote").slideDown('fast');
			styleNav = null;
	});
	}, function(){
		if(styleNav!=null)clearTimeout(styleNav);
		jQuery(this).find("blockquote").slideUp('fast');
	});
	
	
	
	//change the style from cookies
	$(".ChildNavIn a").click(function(){ 
		var style = $(this).attr("id"); 
		$("link[label=def1]").attr("href","./base/resources/content/styles/"+style+"/zui.min.css");
		$("link[label=def2]").attr("href","./base/resources/content/styles/"+style+"/style.css");
		$("link[label=def3]").attr("href","./base/resources/content/styles/"+style+"/jquery.jscrollpane.css");
		$("link[label=def4]").attr("href","./base/resources/content/styles/"+style+"/zTreeStyle/zTreeStyle.css");	
		$.cookie("styleOn",style,{expires:30}); 
		//window.location.reload();							
	}); 
	
	var cookie_style_list = $.cookie("styleOn"); 
		  if(cookie_style_list==null){ 
			  $("link[label=def1]").attr("href","./base/resources/content/styles/style01/zui.min.css");
			  $("link[label=def2]").attr("href","./base/resources/content/styles/style01/style.css");
			  $("link[label=def3]").attr("href","./base/resources/content/styles/style01/jquery.jscrollpane.css");
			  $("link[label=def4]").attr("href","./base/resources/content/styles/style01/zTreeStyle/zTreeStyle.css");	
		  }else{ 
			  $("link[label=def1]").attr("href","./base/resources/content/styles/"+cookie_style_list+"/zui.min.css");
			  $("link[label=def2]").attr("href","./base/resources/content/styles/"+cookie_style_list+"/style.css");
			  $("link[label=def3]").attr("href","./base/resources/content/styles/"+cookie_style_list+"/jquery.jscrollpane.css");
			  $("link[label=def4]").attr("href","./base/resources/content/styles/"+cookie_style_list+"/zTreeStyle/zTreeStyle.css");
    } 
	//select type on top search bar
	$.divselect("#divselect","#inputselect");
	//nav bar of listpage
	$('.navbar-collapse > .nav > li > a').each(function() {
		var $this = $(this);
		var href = $this.attr('href');
		var target = href.substring(href.indexOf('#'), href.length);
		$this.attr('data-target', target);
	});
	$('body').scrollspy({target: '#navbar-collapse'});
	// navbar collapse
	$('.navbar-collapsed .nav > .nav-heading').on("click",function(event) {
		var $nav = $(this).closest('.nav');
		if($nav.hasClass('collapsed')) {
			if($(window).width() < 767) {
				$('.navbar-collapsed .nav').not($nav).children('li:not(.nav-heading)').slideUp('fast', function(){
					$(this).closest('.nav').addClass('collapsed');
				});
			}
			$nav.removeClass('collapsed').children('li:not(.nav-heading)').slideDown('fast');
		} else {
			$nav.children('li:not(.nav-heading)').slideUp('fast', function(){$nav.addClass('collapsed')});
		}
	});
});
function sendCloudAjax(url,data){//调用sendCloudAjax("./getZTreeNode.do",{"userId":1,"groupId":1,"roleId":1})
	var result="";
	$.ajax({
		type:"post",
		dataType:"json",
		url:url,
		async:false,//同步
		data:data,
		success:function(dataMap){
			result = dataMap;
		},
		error:function(){
			result = false;
		}
		
	});
	return result;
}	
//drop-down menu is imitated 
jQuery.divselect = function(divselectid,inputselectid) { 
	$(divselectid+" cite").click(function(){ 
		var ul = $(divselectid+" ul"); 
		if(ul.css("display")=="none"){ 
			ul.slideDown("fast"); 
		}else{ 
			ul.slideUp("fast"); 
		} 
	}); 
	$(divselectid+" a").click(function(){ 
		var txt = $(this).text(); 
		$(divselectid+" cite").html(txt); 
		var value = $(this).attr("selectid"); 
		$(inputselectid).val(value); 
		$(divselectid+" ul").hide(); 
	}); 
}; 

//Page Frame initial height
function initialHeight(sHeight){
	if($(window).width() < 767){
		$('#navbar').css('top', 0);
		$(".C-contentFrame").css("top",sHeight);
		$(".C-contentFrame").css("height",$(window).height()-sHeight);	
		$(".C-contentFrame").css("position","fixed");	
		$(".C-contentFrame").css("overflow","hidden");
		$(".C-contentFrame").css("box-sizing","content-box");
	} else {
		$('#navbar').css('top', sHeight);
		$(".C-contentFrame").css("top",sHeight);
		$(".C-contentFrame").css("height",$(window).height()-sHeight);	
		$(".C-contentFrame").css("position","fixed");	
		$(".C-contentFrame").css("overflow","hidden");
	}
	
}
//Page Frame initial width
function initialWidth(sWidth){
	if($(window).width()<767){
		$(".C-contentFrame").css("width","100%");
		$(".C-contentFrame").css("margin-left",0);
	} else {
		$(".C-contentFrame").css("width",$(window).width()-sWidth);
		$(".C-contentFrame").css("margin-left",sWidth +"px");
	}
}
	
//change frame of page when resize the window
$(window).resize(function(){
	$('#main').css('min-height', $(window).height()-86);
	initialWidth(200); 
	if($(window).width() < 767){
		if($("#search-collapse").children().length == 0){
			$("#search-collapse").prepend($("#search-collapse-Template").html());
		}
		$('#LogoBar').css("padding",0);
		$('#LogoBar').html('<img src="./base/resources/content/images/szCloudlogo_mobile.png" border="0">');
		$('#TopBox').hide();
		$('#comArrow').hide();
		initialHeight(42);
		$('#settingTip').show();
	}else{
		if($("#search-collapse").children().length > 0){
			$("#search-collapse").children().remove();
		}
		$('#LogoBar').css("padding",10);
		$('#LogoBar').html('菜单导航');
		$(".C-contentFrame").css("overflow","hidden");
		$('#TopBox').show();
			$('#comArrow').show();			  
		if("comArrowUp" == $("#comArrow").attr('class')){
			initialHeight(86);
		} else {
			initialHeight(30);		
		}
		$('#settingTip').hide();
		  
		 
	}
}).resize();
	  
function LoadSearchForm(){
	var searchDiv = jQuery("<div>", { "class": "ganttview-vtheader" });
	$('#search-collapse').append(searchDiv);
}
$(document).ready(function(){	 
	//initial page frame when loading
    if($(window).width() < 767) {
	 	initialHeight(42);
	 	initialWidth(0);
	} else {
		initialHeight(86);
		initialWidth(200);
	}
	//Arrow control show or hide of top frame
	$("#comArrow").click(function(){	
		if("comArrowUp" == $(this).attr('class')){
			$(this).removeClass("comArrowUp");
			$(this).addClass("comArrowDown"); 

			$(".TopBox").css("margin-top","-56px");	
			initialHeight(30);
		} else {
			$(this).removeClass("comArrowDown");
			$(this).addClass("comArrowUp"); 
			$(".TopBox").css("margin-top","0px");
			initialHeight(86);		
		}
	});
	
	

	//initial trees of left navbar
	var setting = {
		view: {
			showLine: false,
			showIcon: showIconForTree
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};

	var tNodes1 =[
		{ id:1, pId:0, name:"公文管理",url:"javascript:void(0)", target:"frameRight",open:true},
		{ id:11, pId:1, name:"简单页面",url:"javascript:void(0)", target:"frameRight",click:"alert('以下是表单页面！');"},
		{ id:111, pId:11, name:"校验表单",url:"addinfo.html", target:"frameRight"},
		{ id:112, pId:11, name:"跨行表单",url:"addform.html", target:"frameRight"},
		{ id:113, pId:11, name:"标签表单",url:"tabpage.html", target:"frameRight"},
		{ id:114, pId:11, name:"已办公文",url:"javascript:void(0)", target:"frameRight"},
		{ id:12, pId:1, name:"复合页面",url:"javascript:void(0)", target:"frameRight",click:"alert('以下是复合页面！');"},
		{ id:121, pId:12, name:"表单表格",url:"multipage1.html", target:"frameRight"},
		{ id:122, pId:12, name:"标签表格",url:"multipage2.html", target:"frameRight"},
		{ id:123, pId:12, name:"复合表格",url:"multipage3.html", target:"frameRight"},
		{ id:124, pId:12, name:"项目审批",url:"http://www.baidu.com", target:"frameRight"},
		{ id:13, pId:1, name:"公文归档", isParent:false,url:"http://www.baidu.com", target:"frameRight"},
	];
	var tNodes2 =[
		{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
		{ id:11, pId:1, name:"公文办理",url:"", target:"frameRight",click:"alert('暂无待办公文！');"},
	];
	var tNodes3 =[
		{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
		{ id:11, pId:1, name:"公文办理",url:"http://www.baidu.com", target:"frameRight"},
	];
	var tNodes4 =[
		{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
		{ id:2, pId:0, name:"产品管理",url:"http://www.baidu.com", target:"frameRight"},
		{ id:3, pId:0, name:"系统维护",url:"http://www.baidu.com", target:"frameRight",isParent:false}
	];
	var tNodes5 =[
	{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
		{ id:11, pId:1, name:"公文办理",url:"http://www.baidu.com", target:"frameRight"},
		{ id:2, pId:0, name:"产品管理",url:"http://www.baidu.com", target:"frameRight"},
		{ id:3, pId:0, name:"系统维护",url:"http://www.baidu.com", target:"frameRight",isParent:false}
	];
    function showIconForTree(treeId, treeNode) {
		return !treeNode.isParent;
	};
    $.fn.zTree.init($("#tree1"), setting, tNodes1);
	$.fn.zTree.init($("#tree2"), setting, tNodes2);
	$.fn.zTree.init($("#tree3"), setting, tNodes3);
	$.fn.zTree.init($("#tree4"), setting, tNodes4);
	$.fn.zTree.init($("#tree5"), setting, tNodes5);
	//create Menu
	var tNodes = sendCloudAjax("./getZTreeNode.do",{"userId":1,"groupId":1,"roleId":1});
	$.each(tNodes,function(i,item){
		 dataLi ='<li><ul id="tree'+i+'" class="ztree"></ul></li>';
		  $(dataLi).appendTo(".CloudMenu");
		  $.fn.zTree.init($("#tree"+i),setting,item);
	});
			
			
});