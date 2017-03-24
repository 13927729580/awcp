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
		$("link[label=def]").attr("href","./resources/styles/theme_"+style+".css");
		$.cookie("styleOn",style,{expires:30});
		//window.location.reload();
	});

	var cookie_style_list = $.cookie("styleOn");
		  if(cookie_style_list==null){
			  $("link[label=def]").attr("href","./resources/styles/theme_blue.css");
		  }else{
			  $("link[label=def]").attr("href","./resources/styles/theme_"+cookie_style_list+".css");
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
		fail:function(e){
			alert(e);
			result = false;
		}

	});
	return result;
}
function sendCloudAjaxXml(url,data){//调用sendCloudAjax("./getZTreeNode.do",{"userId":1,"groupId":1,"roleId":1})
	var result="";
	$.ajax({
		type:"post",
		dataType:"text",
		url:url,
		async:false,//同步
		data:data,
		success:function(dataText){
			result = dataText;
		},
		fail:function(e){
			alert(e);
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
	var height = $(window).height()-sHeight;
	if($(window).width() < 767){
		$('#navbar').css({"top":0,"height":"auto"});
		$('#navbar-collapse').css("height","auto");
		$(".C-contentFrame").css({
			"top":sHeight,
			"height":height,
			"position":"absolute",
			"overflow":"hidden",
			"box-sizing":"content-box"
		});
	} else {
		$('#navbar').css({'top':sHeight,'height':height});
		$('#navbar-collapse').css("height",height-76);
		$(".C-contentFrame").css({
			"top":sHeight,
			"height":height,
			"position":"absolute",
			"overflow":"hidden"
		});

		//added 6-25
		$('#navbar').css('height','');

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
		$('#LogoBar').html('<img src="./resources/images/logo_s.png" border="0">'+$(".sysname.cn-name").text());
		$('#TopBox').hide();
		$('#comArrow').hide();
		initialHeight(42);
		$("#navbar").css("top",0);
		$('#settingTip').show();
		$('#departInfoContainer').hide();
		$('.navbar-header').show();
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
			initialHeight(60);
			$("#navbar").css("top",0);
		} else {
			initialHeight(30);
			$("#navbar").css("top",0);
		}
		$('#settingTip').hide();
		$('#departInfoContainer').show();
		$('.navbar-header').hide();



	}
}).resize();


function LoadSearchForm(){
	var searchDiv = jQuery("<div>", { "class": "ganttview-vtheader" });
	$('#search-collapse').append(searchDiv);
}
//**********************************
//取消固定菜单
//modified by abner at 2015-6-29 10:51
//**********************************

function delNormalMenu(menuId){
	var menuIdParent = $('#'+menuId).parent();
	var menuId = $("#"+menuId).attr("onclick");
	menuId = menuId.substr(0,menuId.length-1);
	menuId = menuId.substr(10,menuId.length);

	//alert(menuId);
	$.ajax({
		type:"post",
		dataType:"json",
		url:"./punGeneralMenu/remove.do",
		async:false,//同步
		data:{"menuId":menuId},
		success:function(dataText){
			var result = dataText;
			if(result.success){
				top.dialog({
				    title: '系统信息',
				    content: result.msg
				}).showModal();
			    menuIdParent.remove();
			
			}else{
				top.dialog({
				    title: '系统信息',
				    content: result.msg
				}).showModal();
			}
		},
		fail:function(e){
			top.dialog({
				    title: '系统信息',
				    content: e.msg
				}).showModal();
			//alert(e);
		}
	});

}
//***************************************
//常用菜单设置按钮显示
//modified by abner at 2015-6-27 16:00
//***************************************

function showNormalMenu(normalMenuID){
		$('#'+normalMenuID).show();
}

//***************************************
//常用菜单设置按钮隐藏
//modified by abner at 2015-6-27 16:00
//***************************************

function hideNormalMenu(normalMenuID){
		$('#'+normalMenuID).hide();
}

//************************************
//菜单点击计数
//modified by abner at 2015-6-27 16:00
//************************************

function clickNode(menuId){

	$.ajax({
		type:"post",
		dataType:"json",
		url:"./punMenuCount/save.do",
		async:false,//同步
		data:{"menuId":menuId},
		success:function(dataText){
		    //alert(dataText.success);
		},
		fail:function(e){
			//alert(e);
		}

	});

	hideStartMenu();

}

//**********************************
//设置固定菜单
//modified by abner at 2015-6-27 16:00
//**********************************

function setNormalMenu(menuId){
	var htmlText = $('#'+menuId).children(1).text();
	var menuId = $("#"+menuId).attr("onclick");
	menuId = menuId.substr(0,menuId.length-1);
	menuId = menuId.substr(10,menuId.length);

	//alert(menuId);
	$.ajax({
		type:"post",
		dataType:"json",
		url:"./punGeneralMenu/save.do",
		async:false,//同步
		data:{"menuId":menuId},
		success:function(dataText){
			var result = dataText;
			if(result.success){
				top.dialog({
				    title: '系统信息',
				    content: result.msg
				}).showModal();				
			}else{
				top.dialog({
				    title: '系统信息',
				    content: result.msg
				}).showModal();
			}
		},
		fail:function(e){
			top.dialog({
				    title: '系统信息',
				    content: e.msg
				}).showModal();
			//alert(e);
		}
	});

}
	//************************************
	//常用菜单初始化
	//modified by abner at 2015-6-27 16:00
	//************************************
	function initialAllTreeMenu(){

			var normalNodes = sendCloudAjax("./punGeneralMenu/selectPunMenu.do",{});
			var thisobject = normalNodes;

			//初始化固定菜单

			var regularJson = eval(normalNodes.regular);
			if(regularJson.length>0){
				var currentNode = "";
				var regluarNodes = "";
				var regluarUL = '<ul class="ztree" id="tree_ul1">'
				$(regluarUL).appendTo("#GeneralMenu");
				$.each(regularJson,function(index,item){
					var currentNode = "<li tabindex="+index+" class=level1 id=tree100_"+index+" hidefocus=true treenode=''>"+
					"<span title='' class='button level"+index+" switch noline_docu' id='tree100_"+index+"_switch' treenode_switch=''></span>"+
					"<span title='取消固定菜单' class='starticocancel' id='tree100_"+index+"_b' style='display: none;' onmouseover=showNormalMenu('tree100_"+index+"_b') onmouseout=hideNormalMenu('tree100_"+index+"_b') onclick=delNormalMenu('tree100_"+index+"_a')></span>"+
					"<a class=level1 id=tree100_"+index+"_a onmouseover=showNormalMenu('tree100_"+index+"_b') onmouseout=hideNormalMenu('tree100_"+index+"_b') onclick=clickNode("+item.id+") href="+item.url+" target=main treenode_a=''>"+
         			"<span title='' class='button icon"+item.id+"_ico_docu' id='tree100_"+index+"_ico' treenode_ico=''></span><span id='tree100_"+index+"_span'>"+item.name+"</span>"+
                    "</a>"+
					"</li>";
					regluarNodes += currentNode;

				});
				$("#tree_ul1").html("");
				$(regluarNodes).appendTo("#tree_ul1");
			}

			//初始化常用菜单

			var commonJson = eval(normalNodes.common);
			if(commonJson.length>0){
				currentNode = "";
				var commonNodes = "";
				var commonUL = '<ul class="ztree" id="tree_ul2">'
				$(commonUL).appendTo("#GeneralMenu");
				$.each(commonJson,function(index,item){
					var currentNode = "<li tabindex="+index+" class=level1 id=tree200_"+index+" hidefocus=true treenode=''>"+
					"<span title='' class='button level"+index+" switch noline_docu' id='tree200_"+index+"_switch' treenode_switch=''></span>"+
					"<a class=level1 id=tree200_"+index+"_a  onclick=clickNode('"+item.id+"') href="+item.url+" target=main treenode_a=''>"+
         			"<span title='' class='button icon"+item.id+"_ico_docu' id='tree200_"+index+"_ico' treenode_ico=''></span><span id='tree200_"+index+"_span'>"+item.name+"</span>"+
                    "</a>"+
					"</li>";
					commonNodes += currentNode;

				});
				$("#tree_ul2").html("");
				$(commonNodes).appendTo("#tree_ul2");
			}

			//初始化非常用菜单

			var nocommonJson = eval(normalNodes.noCommon);
			if(nocommonJson.length>0){
				currentNode = "";
				var nocommonNodes = "";
				var nocommonUL = '<ul class="ztree" id="tree_ul3">'
				$(nocommonUL).appendTo("#noGeneralMenu");
				$.each(nocommonJson,function(index,item){
					var currentNode = "<li tabindex="+index+" class=level1 id=tree300_"+index+" hidefocus=true treenode=''>"+
					"<span title='' class='button level"+index+" switch noline_docu' id='tree300_"+index+"_switch' treenode_switch=''></span>"+
					"<a class=level1 id=tree300_"+index+"_a  onclick=clickNode('"+item.id+"') href="+item.url+" target=main treenode_a=''>"+
         			"<span title='' class='button icon"+item.id+"_ico_docu' id='tree300_"+index+"_ico' treenode_ico=''></span><span id='tree300_"+index+"_span'>"+item.name+"</span>"+
                    "</a>"+
					"</li>";
					nocommonNodes += currentNode;

				});
				$("#tree_ul3").html("");
				$(nocommonNodes).appendTo("#tree_ul3");
			}

			bindNormalMenu();
	  }

function bindNormalMenu(){
	$('.ztree a').bind('click',function(e){
		$('.ztree a').removeClass("curSelectedNode");
		$(this).addClass("curSelectedNode");

	});

}
//******************************
//隐藏开始菜单

function hideStartMenu(){
	$("body").bind('click',function(e){
                var e = e || window.event;
                var elem = e.target || e.srcElement;
                while (elem) {
                    if ((elem.id && elem.id=='dvDockStart')||(elem.id && elem.id=='dvDockList')) {
                        return;
                    }
                    elem = elem.parentNode;
                }
                //hideDockAppList();
    });

}

$(document).ready(function(){
	//initial page frame when loading
    if($(window).width() < 767) {
	 	initialHeight(42);
	 	initialWidth(0);
	 	$("#navbar").css("top",0);
	} else {
		initialHeight(60);
		initialWidth(200);
		$("#navbar").css("top",0);
	}
	//Arrow control show or hide of top frame
	$("#comArrow").click(function(){
		if("comArrowUp" == $(this).attr('class')){
			$(this).removeClass("comArrowUp");
			$(this).addClass("comArrowDown");

			$(".TopBox").css("margin-top","-56px");
			initialHeight(30);
			$("#navbar").css("top",0);
		} else {
			$(this).removeClass("comArrowDown");
			$(this).addClass("comArrowUp");
			$(".TopBox").css("margin-top","0px");
			initialHeight(60);
			$("#navbar").css("top",0);
		}
	});

	//initial trees of left navbar
	var showIconForTree = function(treeId, treeNode){
		return !treeNode.isParent;
	};
	var zTreeOnClick = function(event, treeId, treeNode) {
	    $(".ztree a").removeClass("curSelectedNode");
	    $(".ztree #"+treeNode.tId+"_a").addClass("curSelectedNode");
	};
	var setting = {
		view: {
			showLine: false

		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick: zTreeOnClick
		}
	};

			//*************************************
			//常用菜单显示|隐藏
			//modified by abner at 2015-6-27 16:00
			//*************************************

			$('#normalTip').click(function(){
  				$('#GeneralMenu').toggle(300);
  				if($(this).hasClass("icominius")){
  					$(this).removeClass("icominius").addClass("icoplus");
  				}
  				else
  				{
  					$(this).removeClass("icoplus").addClass("icominius");
  				}
  			});

			//************************************
			//非常用菜单显示|隐藏
			//modified by abner at 2015-6-27 16:00
			//************************************

  			$('#nonormalTip').click(function(){
  				$('#noGeneralMenu').toggle(300);
  				if($(this).hasClass("icominius")){
  					$(this).removeClass("icominius").addClass("icoplus");
  				}
  				else
  				{
  					$(this).removeClass("icoplus").addClass("icominius");
  				}
  			});



	//create Menu
	var tNodes = sendCloudAjax("./getZTreeNode.do",{"userId":1,"groupId":1,"roleId":1});

//************************************
//主框架菜单添加图标
//modified by abner at 2015-6-27 16:00
//************************************

	function treeNodeID(ID){
		return 'icon'+ID;
	}

//************************************
//主框架菜单添加点击事件
//modified by abner at 2015-6-27 16:00
//************************************
    $.each(tNodes,function(i,item){
    	$.each(item,function(j,nodeitem){
    		nodeitem.iconSkin = treeNodeID(nodeitem.id);
    	})
    });



	$.each(tNodes,function(i,item){

		 dataLi ='<div class="ztree_position"><div class="ztree_cover"></div><ul id="tree'+i+'" class="ztree"></ul></div>';
		 $(dataLi).appendTo(".CloudMenu");
		 $.fn.zTree.init($("#tree"+i),setting,item);

		 $("#dvDockItems").height(i*32);

	});
	$("#dvDockList").height($("#dvDockList").height()+20);


});

//展开抽屉菜单
function clickTreeNode(nodeID){
	nodeID = nodeID.substr(0,nodeID.length-1);
	$('#'+nodeID+'switch').click();
}


