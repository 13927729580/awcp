// JavaScript Document		

$(function(){
	
	
		
	$.divselect("#divselect","#inputselect");	
	
	//drop-down menu of style changing
	/*var styleNav;
	jQuery(".multiUl li").hover(function(){
	var curItem = jQuery(this);
		styleNav = setTimeout(function(){//延时触发
			curItem.find("blockquote").slideDown('fast');
			styleNav = null;
	});
	}, function(){
		if(styleNav!=null)clearTimeout(styleNav);
		jQuery(this).find("blockquote").slideUp('fast');
	});	*/			
})


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

	
$(document).ready(function(){	 
 	
	//drop down menu
	 var szCloud={};
	$('[_t_nav]').hover(function(){
		var _nav = $(this).attr('_t_nav');
		clearTimeout( szCloud[ _nav + '_timer' ] );
		szCloud[ _nav + '_timer' ] = setTimeout(function(){
		$('[_t_nav]').each(function(){
		$(this)[ _nav == $(this).attr('_t_nav') ? 'addClass':'removeClass' ]('nav-up-selected');
		});
		$('#'+_nav).stop(true,true).slideDown(200);
		}, 150);
	},function(){
		var _nav = $(this).attr('_t_nav');
		clearTimeout( szCloud[ _nav + '_timer' ] );
		szCloud[ _nav + '_timer' ] = setTimeout(function(){
		$('[_t_nav]').removeClass('nav-up-selected');
		$('#'+_nav).stop(true,true).slideUp(200);
		}, 150);
	});
	 initial(86);
	 
	 //scroll bar
	 $(".ContentBox").jScrollPane();
	 
	 //change top menu style when clicked
	 $(".navigation-v3 li").each(function () {

       $('.navigation-v3 li a').click(function () {
            $('.navigation-v3 li a').removeClass("selected");  
            $(this).addClass("selected");    
        });
     });
	 $(".nav-down-menu").each(function () {
		   $(this).css("overflow-x","auto"); 
		   $(this).css("overflow-y","hidden"); 
	 });
	 $(".navigation-down-inner").each(function () {  
			   $(this).css("overflow-x","auto");
			   $(this).css("width",$(this).children("dl").length*160);
			   //$(this).children("dl").each(function () {
		        //   $(this).jScrollPane();
		      // });
			  
		   });
	
	 $("em").click(function(){	
		    if("emon" == $(this).attr('class')){
				$(this).removeClass("emon");
				$(this).addClass("emoff"); 
				$(".ContentBox").jScrollPane();
				}
				else{
				$(this).removeClass("emoff");
				$(this).addClass("emon"); 
				$(".ContentBox").jScrollPane();
					
				}
	});
	
	
	//hide or show the logo
	$("#comArrow").click(function(){	
		    if("comArrowUp" == $(this).attr('class')){
				$(this).removeClass("comArrowUp");
				$(this).addClass("comArrowDown"); 
				$(".TopBox").css("margin-top","-56px");
				$(".navigation-down").css("top","30px");
				initial(30);
				$(".ContentBox").jScrollPane();
				}
				else{
				$(this).removeClass("comArrowDown");
				$(this).addClass("comArrowUp"); 
				$(".TopBox").css("margin-top","0px");
				$(".navigation-down").css("top","86px");
				initial(86);		
				}
	});
	
	// page frame initial
	function initial(sHeight){
		$(".ContentBox").css("height",$(window).height()-sHeight);
		$(".nav-down-menu").css("height",$(window).height()-sHeight);
		$(".navigation-down dl").each(function () {
            $(this).css("height",$(window).height()-sHeight);
     	});
	}
	
	
	//initial tree of drop-dowm menu
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
			{ id:1, pId:0, name:"公文管理",url:"javascript:void(0)", target:"main",open:true},
			{ id:11, pId:1, name:"公文办理",url:"", target:"main",click:"alert('暂无待办公文！');"},
			{ id:111, pId:11, name:"新建公文",url:"addinfo.html", target:"main"},
			{ id:112, pId:11, name:"公文草稿",url:"addform.html", target:"main"},
			{ id:113, pId:11, name:"待办公文",url:"http://www.baidu.com", target:"main"},
			{ id:114, pId:11, name:"已办公文",url:"http://www.baidu.com", target:"main"},
			{ id:12, pId:1, name:"流转应用",url:"http://www.baidu.com", target:"main"},
			{ id:121, pId:12, name:"流转审批",url:"http://www.baidu.com", target:"main"},
			{ id:122, pId:12, name:"公文审批",url:"http://www.baidu.com", target:"main"},
			{ id:123, pId:12, name:"财务审批",url:"http://www.baidu.com", target:"main"},
			{ id:124, pId:12, name:"项目审批",url:"http://www.baidu.com", target:"main"},
			{ id:13, pId:1, name:"公文归档", isParent:true,url:"http://www.baidu.com", target:"main"},
			{ id:2, pId:0, name:"产品管理",url:"javascript:void(0)", target:"main"},
			{ id:21, pId:2, name:"需求管理", open:true,url:"http://www.baidu.com", target:"main"},
			{ id:211, pId:21, name:"全部需求",url:"http://www.baidu.com", target:"main"},
			{ id:212, pId:21, name:"评审需求",url:"http://www.baidu.com", target:"main"},
			{ id:213, pId:21, name:"草稿激活",url:"http://www.baidu.com", target:"main"},
			{ id:214, pId:21, name:"待办需求",url:"http://www.baidu.com", target:"main"},
			{ id:22, pId:2, name:"项目管理",url:"http://www.baidu.com", target:"main"},
			{ id:221, pId:22, name:"项目名称",url:"http://www.baidu.com", target:"main"},
			{ id:222, pId:22, name:"项目代号",url:"http://www.baidu.com", target:"main"},
			{ id:223, pId:22, name:"项目状态",url:"http://www.baidu.com", target:"main"},
			{ id:224, pId:22, name:"项目进度",url:"http://www.baidu.com", target:"main"},
			{ id:23, pId:2, name:"产品文档",url:"http://www.baidu.com", target:"main"},
			{ id:231, pId:23, name:"需求分析",url:"http://www.baidu.com", target:"main"},
			{ id:232, pId:23, name:"市场分析",url:"http://www.baidu.com", target:"main"},
			{ id:233, pId:23, name:"系统分析",url:"http://www.baidu.com", target:"main"},
			{ id:234, pId:23, name:"实施方案",url:"http://www.baidu.com", target:"main"},
			{ id:3, pId:0, name:"系统维护",url:"javascript:void(0)", target:"main",isParent:true},
			{ id:4, pId:0, name:"组织机构",url:"javascript:void(0)", target:"main",isParent:true},
			{ id:41, pId:4, name:"组列表",url:"organize.html", target:"main"},
			{ id:42, pId:4, name:"人员列表",url:"member.html", target:"main"},
			{ id:5, pId:0, name:"信息修改",url:"memberinfo.html", target:"main",isParent:true}
		];
	 function showIconForTree(treeId, treeNode) {
			return !treeNode.isParent;
		};
	$.fn.zTree.init($("#slideeMenuTree"), setting, tNodes1);

});