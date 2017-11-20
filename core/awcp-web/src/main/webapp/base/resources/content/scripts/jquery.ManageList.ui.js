// JavaScript Document
$(function(){
			
				//nav bar of listpage
				$('.navbar-collapse > .nav > li > a').each(function()
				{
					var $this = $(this);
					var href = $this.attr('href');
					var target = href.substring(href.indexOf('#'), href.length);
					$this.attr('data-target', target);
				});
				$('body').scrollspy({target: '#navbar-collapse'});
			
				// navbar collapse
				$('.navbar-collapsed .nav > .nav-heading').click(function(event)
				{
					var $nav = $(this).closest('.nav');
					if($nav.hasClass('collapsed'))
					{
						if($(window).width() < 767)
						{
							$('.navbar-collapsed .nav').not($nav).children('li:not(.nav-heading)').slideUp('fast', function(){
								$(this).closest('.nav').addClass('collapsed');
							});
							
						}
						$nav.removeClass('collapsed').children('li:not(.nav-heading)').slideDown('fast');
					}
					else
					{
						$nav.children('li:not(.nav-heading)').slideUp('fast', function(){$nav.addClass('collapsed')});
					}
				});
				
				
})
		  

$(document).ready(function(){	 

	//initial page frame when loading
    if($(window).width() < 767){
	 	initialHeight(42);
	 	initialWidth(0);
	}
	else{
		initialHeight(86);
		initialWidth(200);
	}
	
	 

	//Page Frame initial height
	function initialHeight(sHeight){
	
			if($(window).width() < 767){
			$('#navbar').css('top', 0);
			$(".C-contentFrame").css("top",0);
			$(".C-contentFrame").css("height",$(window).height()-sHeight);	
			$(".C-contentFrame").css("position","fixed");	
			$(".C-contentFrame").css("overflow","hidden");
			$(".C-contentFrame").css("box-sizing","content-box");
			}
			else{
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
			}
			else
			{
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
			  $('#LogoBar').html('<img src="content/images/szCloudlogo_mobile.png" border="0">');
			  $('#TopBox').hide();
			  $('#comArrow').hide();
			  initialHeight(42);
			  $('#settingTip').show();

			  
		  }else{
			  if($("#search-collapse").children().length > 0){
			  $("#search-collapse").children().remove();
			  }
			  $('#LogoBar').css("padding",10);
			  $('#LogoBar').html('<i class="icon-desktop"></i> 后台管理');
			  $(".C-contentFrame").css("overflow","hidden");
			  $('#TopBox').show();
 			  $('#comArrow').show();			  
			  if("comArrowUp" == $("#comArrow").attr('class')){
				initialHeight(86);
				}
				else{
				initialHeight(0);		
				}
			  $('#settingTip').hide();
			  
			 
			  }
		  }).resize();
		  
		 
	
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
			{ id:114, pId:11, name:"列表页面",url:"main.html", target:"frameRight"},
			{ id:12, pId:1, name:"复合页面",url:"javascript:void(0)", target:"frameRight",click:"alert('以下是复合页面！');"},
			{ id:121, pId:12, name:"表单表格",url:"multipage1.html", target:"frameRight"},
			{ id:122, pId:12, name:"标签表格",url:"multipage2.html", target:"frameRight"},
			{ id:123, pId:12, name:"复合表格",url:"multipage3.html", target:"frameRight"},
			{ id:124, pId:12, name:"项目审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:13, pId:1, name:"公文归档", isParent:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:2, pId:0, name:"产品管理",url:"javascript:void(0)", target:"frameRight"},
			{ id:21, pId:2, name:"需求管理", open:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:211, pId:21, name:"全部需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:212, pId:21, name:"评审需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:213, pId:21, name:"草稿激活",url:"http://www.baidu.com", target:"frameRight"},
			{ id:214, pId:21, name:"待办需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:22, pId:2, name:"项目管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:221, pId:22, name:"项目名称",url:"http://www.baidu.com", target:"frameRight"},
			{ id:222, pId:22, name:"项目代号",url:"http://www.baidu.com", target:"frameRight"},
			{ id:223, pId:22, name:"项目状态",url:"http://www.baidu.com", target:"frameRight"},
			{ id:224, pId:22, name:"项目进度",url:"http://www.baidu.com", target:"frameRight"},
			{ id:23, pId:2, name:"产品文档",url:"http://www.baidu.com", target:"frameRight"},
			{ id:231, pId:23, name:"需求分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:232, pId:23, name:"市场分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:233, pId:23, name:"系统分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:234, pId:23, name:"实施方案",url:"http://www.baidu.com", target:"frameRight"},
			{ id:3, pId:0, name:"系统维护",url:"javascript:void(0)", target:"frameRight",isParent:true},
			{ id:4, pId:0, name:"组织机构",url:"javascript:void(0)", target:"frameRight",isParent:true},
			{ id:41, pId:4, name:"组列表",url:"organize.html", target:"frameRight"},
			{ id:42, pId:4, name:"人员列表",url:"member.html", target:"frameRight"},
			{ id:5, pId:0, name:"信息修改",url:"memberinfo.html", target:"frameRight",isParent:true}
		];
		var tNodes2 =[
			{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
			{ id:11, pId:1, name:"公文办理",url:"", target:"frameRight",click:"alert('暂无待办公文！');"},
			{ id:111, pId:11, name:"新建公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:112, pId:11, name:"公文草稿",url:"http://www.baidu.com", target:"frameRight"},
			{ id:113, pId:11, name:"待办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:114, pId:11, name:"已办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:12, pId:1, name:"流转应用",url:"http://www.baidu.com", target:"frameRight"},
			{ id:121, pId:12, name:"流转审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:122, pId:12, name:"公文审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:123, pId:12, name:"财务审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:124, pId:12, name:"项目审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:13, pId:1, name:"公文归档", isParent:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:2, pId:0, name:"产品管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:21, pId:2, name:"需求管理", open:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:211, pId:21, name:"全部需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:212, pId:21, name:"评审需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:213, pId:21, name:"草稿激活",url:"http://www.baidu.com", target:"frameRight"},
			{ id:214, pId:21, name:"待办需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:22, pId:2, name:"项目管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:221, pId:22, name:"项目名称",url:"http://www.baidu.com", target:"frameRight"},
			{ id:222, pId:22, name:"项目代号",url:"http://www.baidu.com", target:"frameRight"},
			{ id:223, pId:22, name:"项目状态",url:"http://www.baidu.com", target:"frameRight"},
			{ id:224, pId:22, name:"项目进度",url:"http://www.baidu.com", target:"frameRight"},
			{ id:23, pId:2, name:"产品文档",url:"http://www.baidu.com", target:"frameRight"},
			{ id:231, pId:23, name:"需求分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:232, pId:23, name:"市场分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:233, pId:23, name:"系统分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:234, pId:23, name:"实施方案",url:"http://www.baidu.com", target:"frameRight"},
			{ id:3, pId:0, name:"系统维护",url:"http://www.baidu.com", target:"frameRight",isParent:true}
		];
		var tNodes3 =[
			{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
			{ id:11, pId:1, name:"公文办理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:111, pId:11, name:"新建公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:112, pId:11, name:"公文草稿",url:"http://www.baidu.com", target:"frameRight"},
			{ id:113, pId:11, name:"待办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:114, pId:11, name:"已办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:12, pId:1, name:"流转应用",url:"http://www.baidu.com", target:"frameRight"},
			{ id:121, pId:12, name:"流转审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:122, pId:12, name:"公文审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:123, pId:12, name:"财务审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:124, pId:12, name:"项目审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:13, pId:1, name:"公文归档", isParent:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:2, pId:0, name:"产品管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:21, pId:2, name:"需求管理", open:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:211, pId:21, name:"全部需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:212, pId:21, name:"评审需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:213, pId:21, name:"草稿激活",url:"http://www.baidu.com", target:"frameRight"},
			{ id:214, pId:21, name:"待办需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:22, pId:2, name:"项目管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:221, pId:22, name:"项目名称",url:"http://www.baidu.com", target:"frameRight"},
			{ id:222, pId:22, name:"项目代号",url:"http://www.baidu.com", target:"frameRight"},
			{ id:223, pId:22, name:"项目状态",url:"http://www.baidu.com", target:"frameRight"},
			{ id:224, pId:22, name:"项目进度",url:"http://www.baidu.com", target:"frameRight"},
			{ id:23, pId:2, name:"产品文档",url:"http://www.baidu.com", target:"frameRight"},
			{ id:231, pId:23, name:"需求分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:232, pId:23, name:"市场分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:233, pId:23, name:"系统分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:234, pId:23, name:"实施方案",url:"http://www.baidu.com", target:"frameRight"},
			{ id:3, pId:0, name:"系统维护",url:"http://www.baidu.com", target:"frameRight",isParent:true}
		];
		var tNodes4 =[
			{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
			{ id:11, pId:1, name:"公文办理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:111, pId:11, name:"新建公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:112, pId:11, name:"公文草稿",url:"http://www.baidu.com", target:"frameRight"},
			{ id:113, pId:11, name:"待办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:114, pId:11, name:"已办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:12, pId:1, name:"流转应用",url:"http://www.baidu.com", target:"frameRight"},
			{ id:121, pId:12, name:"流转审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:122, pId:12, name:"公文审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:123, pId:12, name:"财务审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:124, pId:12, name:"项目审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:13, pId:1, name:"公文归档", isParent:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:2, pId:0, name:"产品管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:21, pId:2, name:"需求管理", open:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:211, pId:21, name:"全部需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:212, pId:21, name:"评审需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:213, pId:21, name:"草稿激活",url:"http://www.baidu.com", target:"frameRight"},
			{ id:214, pId:21, name:"待办需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:22, pId:2, name:"项目管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:221, pId:22, name:"项目名称",url:"http://www.baidu.com", target:"frameRight"},
			{ id:222, pId:22, name:"项目代号",url:"http://www.baidu.com", target:"frameRight"},
			{ id:223, pId:22, name:"项目状态",url:"http://www.baidu.com", target:"frameRight"},
			{ id:224, pId:22, name:"项目进度",url:"http://www.baidu.com", target:"frameRight"},
			{ id:23, pId:2, name:"产品文档",url:"http://www.baidu.com", target:"frameRight"},
			{ id:231, pId:23, name:"需求分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:232, pId:23, name:"市场分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:233, pId:23, name:"系统分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:234, pId:23, name:"实施方案",url:"http://www.baidu.com", target:"frameRight"},
			{ id:3, pId:0, name:"系统维护",url:"http://www.baidu.com", target:"frameRight",isParent:true}
		];
		var tNodes5 =[
		{ id:1, pId:0, name:"公文管理",url:"http://www.baidu.com", target:"frameRight",open:true},
			{ id:11, pId:1, name:"公文办理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:111, pId:11, name:"新建公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:112, pId:11, name:"公文草稿",url:"http://www.baidu.com", target:"frameRight"},
			{ id:113, pId:11, name:"待办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:114, pId:11, name:"已办公文",url:"http://www.baidu.com", target:"frameRight"},
			{ id:12, pId:1, name:"流转应用",url:"http://www.baidu.com", target:"frameRight"},
			{ id:121, pId:12, name:"流转审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:122, pId:12, name:"公文审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:123, pId:12, name:"财务审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:124, pId:12, name:"项目审批",url:"http://www.baidu.com", target:"frameRight"},
			{ id:13, pId:1, name:"公文归档", isParent:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:2, pId:0, name:"产品管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:21, pId:2, name:"需求管理", open:true,url:"http://www.baidu.com", target:"frameRight"},
			{ id:211, pId:21, name:"全部需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:212, pId:21, name:"评审需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:213, pId:21, name:"草稿激活",url:"http://www.baidu.com", target:"frameRight"},
			{ id:214, pId:21, name:"待办需求",url:"http://www.baidu.com", target:"frameRight"},
			{ id:22, pId:2, name:"项目管理",url:"http://www.baidu.com", target:"frameRight"},
			{ id:221, pId:22, name:"项目名称",url:"http://www.baidu.com", target:"frameRight"},
			{ id:222, pId:22, name:"项目代号",url:"http://www.baidu.com", target:"frameRight"},
			{ id:223, pId:22, name:"项目状态",url:"http://www.baidu.com", target:"frameRight"},
			{ id:224, pId:22, name:"项目进度",url:"http://www.baidu.com", target:"frameRight"},
			{ id:23, pId:2, name:"产品文档",url:"http://www.baidu.com", target:"frameRight"},
			{ id:231, pId:23, name:"需求分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:232, pId:23, name:"市场分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:233, pId:23, name:"系统分析",url:"http://www.baidu.com", target:"frameRight"},
			{ id:234, pId:23, name:"实施方案",url:"http://www.baidu.com", target:"frameRight"},
			{ id:3, pId:0, name:"系统维护",url:"http://www.baidu.com", target:"frameRight",isParent:true}
		];
        function showIconForTree(treeId, treeNode) {
			return !treeNode.isParent;
		};
		    $.fn.zTree.init($("#tree1"), setting, tNodes1);
			$.fn.zTree.init($("#tree2"), setting, tNodes2);
			$.fn.zTree.init($("#tree3"), setting, tNodes3);
			$.fn.zTree.init($("#tree4"), setting, tNodes4);
			$.fn.zTree.init($("#tree5"), setting, tNodes5);
			
			
});