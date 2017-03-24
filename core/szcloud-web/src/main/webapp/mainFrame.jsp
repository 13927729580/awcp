<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/resources/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>深圳市人大办公自动化系统</title>
    <link rel="stylesheet" type="text/css" href="./resources/plugins/easyUI/css/easyui.css">
    <link href="base/resources/artDialog/css/ui-dialog.css"
	rel="stylesheet">
    <style type="text/css">
        div,body,ul,li,img,p{padding: 0;margin: 0;}
        .left{float: left;}
        .right{float: right;}
        ul li{list-style: none;}
        #home_header .home_header_right ul li{ float: left;padding: 0 2px; font-size: 14px;}
        #home_header .home_header_c{padding: 22px 48px;width: 100%;text-align: center}
        #home_header .home_header_left{ height:96px; margin: 2px 0;position: absolute;left: 0;}
        #home_header .home_header_right{text-align: right; position: absolute;right: 0;bottom: 0;}


        #home_header{width: 100%;height: 100px;color: #fff;background: #b1180f; position: relative;}
        #home_header .home_header_left img{ vertical-align: middle; margin-top: 8px;}
        #home_header .home_header_right ul{display: inline-block;}
        #home_header .home_header_right ul li img{width: 42px; width: 42px;display: block;padding:10px 0 10px 7px;}
        #home_header .home_header_c p{line-height:26px;font-size: 18px; text-align: center;}

        #main_layout .panel-icon, .panel-tool{display: none;}
        #main_layout .accordion .accordion-header{height: 40px;background: #f1f1f3;}
        #main_layout .accordion .accordion-header-selected{background: #004976;}
        #main_layout .accordion .accordion-header-selected .panel-title{color: #fff;}
        #main_layout .accordion .accordion-header .panel-title{font-size: 16px;padding: 10px 0 0 22px;}
        .panel-header, .panel-body{border-color: #e4e5e6;}
        #main_layout .accordion-noborder .accordion-body ul li{line-height: 28px;font-size: 12px;font-weight:bolder;cursor:pointer;padding:0 10px;}
        .menu_img{vertical-align:middle;width: 24px;}
        .menu_p{display: inline-block;}
        a{text-decoration:none;color: #fff;}

        .tabs-closable{font-size: 16px;}
        #tTabs .tabs{height: 30px;}

        .tabs li a.tabs-close{background-size: 54px 33px;width: 15px;height: 14px;}
    </style>
    <!--<link rel="stylesheet" type="text/css" href="./css/demo.css">-->
</head>
	<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
<script src="./venson/js/common.js"></script>
<script type="text/javascript">
Comm.set("userName",'${userName}');
Comm.set("userId",'${userId}');
Comm.set("userGroupName",'${userGroupName}');
Comm.set("groupNamePY",'${groupNamePY}');
Comm.set("userTitle",'${userTitle}');
Comm.set("untreatedName",'${untreatedName}');
</script>
<body>

<div id="main_layout" class="easyui-layout"  style="width:100%; height:680px;">
    <!-- 头部 -->
    <div data-options="region:'north'" style="overflow: hidden;">
        <div id="home_header" >
            <div class="home_header_left left" >
                <img src="./images/top-bg-left_1.jpg">
            </div>
            <div class="home_header_c left">
                <p>尊敬的<span>${userName}</span>${userTitle }:<span id="nowTime"></span></p>
                <p>欢迎使用OA系统</p>
            </div>
            <div class="home_header_right right" style="overflow: hidden;">
                <ul>
                    <li class="synchronized" lang="10620">
                        <img src="./images/Online-do-work.png">
                        <span>网上办文</span>
                    </li>
                    <li>
                        <img src="./images/Electronic-business.png">
                        <span>会议管理</span>
                    </li>
                    <li class="synchronized" lang="10636">
                        <img src="./images/government-affairs.png">
                        <span>政务活动</span>
                    </li>
                    <li>
                        <img src="./images/Data-Management.png">
                        <span>短信平台</span>
                    </li>
                    <li>
                        <img src="./images/email.png">
                        <span>资料管理</span>
                    </li>
                    <li id="allinfo">
                        <img src="./images/Personal.png">
                        <span>个人管理</span>
                    </li>
                    <li onclick="location.href='logout.do'">
                       	<img src="./images/logout.png">
                       	<span>退出系统</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <!-- 侧边 -->
    <div data-options="region:'west'" style="width: 250px; padding: 11px 5px 5px 11px">
        <div style="background: #eaeaea;font-size: 18px; height: 48px;width: 280px;">
            <img style="padding: 9px 10px 0 6px;" src="./images/home.png">
            <span class="main_grbg_bg" style="line-height: 48px; display: inline-block;font-size: 18px;">个人办公</span>
        </div>
        <div class="easyui-accordion" id="menu" data-options="fit:true,border:false" style="border: 1px solid #e4e5e6;padding-bottom: 48px;"></div>
    </div>
    <!-- 中间 -->
    <div data-options="region:'center',iconCls:'icon-ok'">
        <div id="tTabs" class="easyui-tabs" data-options="fit:true,border:false,plain:true">
        	<div title="我的桌面" style="overflow: auto;"> 
        		<iframe width="99%" onload="this.height=document.body.clientHeight-135"  src="npc/desktop/home.html" scrolling="auto" frameborder="0"></iframe>
        	</div>
        </div>
    </div>

</div>
	<script src="base/resources/content/scripts/Z-Dialog.js"></script>
    <script type="text/javascript" src="./resources/plugins//easyUI/js/jquery.easyui.min.js"></script>
	
	<script src="base/resources/artDialog/dist/dialog-plus-min.js"></script>
	<!--消息提醒-->
	<script type="text/javascript" src="${ctxBase}/plugins/artDialog4/artDialog.js?skin=silver"></script>
    <script src="${ctxBase}/plugins/artDialog4/plugins/iframeTools.source.js"></script>
	<script type="text/javascript">
		$("#nowTime").text(Comm.getTime());
		/*coding by venson*/
		//获取用户菜单
		var data=Comm.getData("getUserMenu.do");
		//初始化菜单
		initMenu(data.children);
		function initMenu(data){
			$.each(data,function(i,menu){
				var menu_li = getMenu(menu);
				$("#menu").append(menu_li);
			});
		}
		function getMenu(menu){
			var menu_li=[];
			var url=menu.url=="#"?"#":baseUIUrl+menu.url;
			//判断是否存在子节点
			if(menu.children.length>0){
				menu_li.push("<div id='"+menu.id+"'  title='"+menu.name+"'>");
				menu_li.push(" <ul> ");
				$.each(menu.children,function(j,cmenu){
					menu_li.push(getMenu(cmenu));
				});
				menu_li.push(" </ul> ");
				menu_li.push('</div>');
			}else{
				menu_li.push('<li id="'+menu.id+'" class="menu_li" lang="'+url+'" >');
				menu_li.push('<i><img src="'+menu.icon+'" class="menu_img"></i><p class="menu_p">'+menu.name+'</p>');
				menu_li.push('</li>');
			}
			return menu_li.join('');
		}
		//添加菜单点击事件
		$(".menu_li").click(function(){
			var url=this.lang;
			var title=$(this).text();
			Comm.openTab(url,title)
			//点击颜色变化
			$(".easyui-accordion ul li").css("background","");
		    $(this).css("background","#fbec88");
		});
	    $(".synchronized").click(function(){
	    	$("#menu #"+this.lang).prev().trigger("click");
	    })
		/*coding by yk*/
		$(document).ready(function(){
	        var height1 = $(window).height();
	        $("#main_layout").attr("style","width:100%;height:"+height1+"px");
	        $("#main_layout").layout("resize",{
	            width:"100%",
	            height:height1+"px"
	        });
	    });
	</script>
</body>
</html>