<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="<%=basePath%>resources/mobile/resources/styles/jquery.mobile-1.4.5.css">
<link rel="stylesheet" href="<%=basePath%>resources/mobile/resources/styles/font-awesome.min.css">
<link rel="stylesheet" href="<%=basePath%>resources/mobile/resources/styles/mobile.drable.css">
<script src="<%=basePath%>resources/JqEdition/jquery-2.2.3.min.js"></script>
<script src="<%=basePath%>resources/scripts/pagedraginit.js"></script>
<script src="<%=basePath%>resources/mobile/js/jquery.mobile-1.4.5.js"></script>
<script src="<%=basePath%>resources/mobile/resources/scripts/jquery.drag.vendor.js"></script>
<script src="<%=basePath%>resources/mobile/resources/scripts/jquery.drag.adapter.js"></script>
<script src="<%=basePath%>resources/mobile/resources/scripts/jquery.dragloader.js"></script>
<script type="text/javascript" src="<%=basePath%>venson/js/common.js"></script>
<style>
	html,body{
		overflow:hidden;
	}
	.ui-listview > .ui-li-has-thumb > .ui-btn, .ui-listview > .ui-li-static.ui-li-has-thumb {
	    min-height: 2.8em;
	}
	.ui-li-has-thumb a h3 {
	    color: #333;
	    font-size: 1em;
	    font-weight: bold;
	    margin: 0.6em 0;
	    padding: 0;
	}
</style>

</head>
<body>
<div data-role="page" id="hospage">
	<div data-role="header" data-position="fixed" style="height:40px" class="ui-header-blue" id="header">
<!-- 		<a href="#panel-reveal" class="ui-nodisc-icon" id="btn-menu"><i class="icon-th-list icon-2-4x"></i></a>
 -->	
 		<a href="javascript:void(0)" onclick="Comm.go('apps/index.html')" class="ui-btn-left ui-alt-icon ui-nodisc-icon ui-btn ui-icon-carat-l ui-btn-icon-notext ui-corner-all" data-role="button" role="button">Back</a>	
 		<h3 style="font-size:20px"><b>个人办公</b></h3>
		<div class="ui-btn-right" data-position="fixed">
			
		</div>
		<!--<a href="#" class="ui-btn ui-corner-all ui-shadow ui-btn-icon-left ui-icon-gear">设置</a>-->

	</div>
	<input type='hidden' value="1" id="currentPage1" />
	<input type='hidden' id="totalPage1" value="${totalPage1}" />
	<input type='hidden' value="1" id="currentPage2" />
	<input type='hidden' id="totalPage2"  value="${totalPage2}"/>
	<input type='hidden' value="1" id="currentPage3" />
	<input type='hidden' id="totalPage3"  value="${totalPage3}"/>
	<div style="overflow:hidden" class="ui-content" id="main1Content" role="main" data-role="content">
    	<div class="ui-field-contain" data-role="fieldcontain">
            <div id="tabs" data-role="tabs">
              <div class="ui-navbar ui-tabbar" role="navigation" data-role="navbar">
                <ul>
                  <li><a id="oneA" href="#one" onclick="Comm.set('record',1)" class="ui-tabs-left ui-btn-active ui-state-persist">待办件</a></li>
                  <li><a id="oneB"  href="#two" onclick="Comm.set('record',2)" class="ui-tabs-right">在办件</a></li>
                  <li><a id="oneC"  href="#three" onclick="Comm.set('record',3)" class="ui-tabs-right">办结件</a></li>
                </ul>
              </div>
              <div class="ui-body-d ui-content" id="one">
                	<ul data-role="listview" data-split-icon="carat-r" data-split-theme="d" class="wrapper swipe-delete clearfix firstView">
                		<c:if test="${entryList1 != null}">
							<c:forEach items="${entryList1}" var="el1">  
								<li class="ui-li-has-thumb">
                            		<a id="${el1.WorkID}" href="javascript:location.href='<%=basePath%>WF/MyFlow.jsp?FK_Flow=${el1.FK_Flow}&FK_Node=${el1.FK_Node}&WorkID=${el1.WorkID}&FID=${el1.FID}&flag=0<c:if test="${el1.state == '退回'}">&isReturn=1</c:if>'">
                               			<h3>${el1.Title}
	                               			<c:if test="${el1.state == '退回'}">  
	  											<span style="color: red;font-size: 12px;">(退回件)</span>
	　　										</c:if>   
										</h3>
                                 			<p><span>时间:${el1.RDT}</span><span style="float:right;margin-right:30px;">发起人:${el1.StarterName}</span></p>
                            			</a>
                       			 </li>
							</c:forEach>             			
                		</c:if>
                    </ul>
              </div>
              <div id="two">
                <ul data-role="listview" data-split-icon="carat-r" data-split-theme="d" data-inset="true" class="wrapper secondView">
                		<c:if test="${entryList2 != null}">
							<c:forEach items="${entryList2}" var="el2">  
								<li class="ui-li-has-thumb">
                            		<a id="${el2.WorkID}" href="javascript:location.href='<%=basePath%>WF/MyFlow.jsp?FK_Flow=${el2.FK_Flow}&FK_Node=${el2.FK_Node}&WorkID=${el2.WorkID}&FID=${el2.FID}&flag=1'">
                               			<h3>${el2.Title}</h3>
                                		<%-- <p><span>完成时间:${el2.RDT}</span><span style="float:right;margin-right:30px;">创建人:${el2.StarterName}</span></p> --%>
                           			</a>
                       			 </li>
							</c:forEach>             			
                		</c:if>
                    </ul>
              </div>
               <div id="three">
                <ul data-role="listview" data-split-icon="carat-r" data-split-theme="d" data-inset="true" class="wrapper thirdlyView">
                		<c:if test="${entryList3 != null}">
							<c:forEach items="${entryList3}" var="el3">  
								<li class="ui-li-has-thumb">
                            		<a id="${el3.WorkID}" href="javascript:location.href='<%=basePath%>WF/MyFlow.jsp?FK_Flow=${el3.FK_Flow}&FK_Node=${el3.FK_Node}&WorkID=${el3.WorkID}&FID=${el3.FID}&flag=2'">
                               			<h3>${el3.Title}</h3>
                                		<%-- <p><span>完成时间:${el3.RDT}</span><span style="float:right;margin-right:30px;">创建人:${el3.StarterName}</span></p> --%>
                           			</a>
                       			 </li>
							</c:forEach>             			
                		</c:if>
                    </ul>
              </div>
            </div>
      	</div>
    </div>
    <div data-role="footer" data-position="fixed">
		<div data-role="navbar" id="navbar">
			<ul>
            	<li><a href="<%=basePath%>apps/index.html" class="ui-nodisc-icon ui-btn-icon-top ui-btn-nav-link" rel="external"><i class="icon-home icon-2x"></i>首页</a></li>
           		<li><a href="<%=basePath%>oa/app/newTask.do" class="ui-nodisc-icon ui-btn-icon-top ui-btn-nav-link" rel="external"><i class="icon-plus icon-2x"></i>添加</a></li>
			</ul>
		</div>		
	</div>
	<%-- <jsp:include page="left.jsp"/>  --%> 
</div>

<script>
$(document).ready(function() {
    $("#header").removeClass("ui-bar-inherit");
	$("#btn-menu").removeAttr("class");
	$("#btn-menu").addClass("ui-nodisc-icon ui-btn-left btn-menu ");
	$("#navbar").addClass("hostabbar");
	$("ul.wrapper").height($(window).height()-$("#header").height()-$(".ui-filterable").height()-$(".ui-footer").height()-4);
	$("ul.wrapper").css("top",$("#header").height()+$(".ui-filterable").height());
	$("ul.wrapper1").height($(window).height()-$("#header").height()-$(".ui-filterable").height()-$(".ui-footer").height()-4);
	$("ul.wrapper1").css("top",$("#header").height()+$(".ui-filterable").height());
});

//********************************
//编辑删除
//********************************
$(function() {
    deleteload();
});
</script>

<script>
	setTimeout((function(){
		var record=Comm.get("record");
		if(record==1){
			$("#oneA,#oneB,#oneC").removeClass("ui-btn-active");
			$("#oneA").addClass("ui-btn-active");
			$("#oneA").trigger("click");
		}else if(record==2){
			$("#oneA,#oneB,#oneC").removeClass("ui-btn-active");
			$("#oneB").addClass("ui-btn-active");
			$("#oneB").trigger("click");
		}else{
			$("#oneA,#oneB,#oneC").removeClass("ui-btn-active");
			$("#oneC").addClass("ui-btn-active");
			$("#oneC").trigger("click");
		}
	}),5)
	function aclick(id)
	{
		location.href=basePath+"document/view.do?id="+id+"&dynamicPageId=1405";
	}
	function listDel1(id)
	{
			if(confirm("确定删除？")){
				$.ajax({
					url:"<%=basePath%>appPublicDel/deletePersonalTask.do?id="+id,
					type: 'POST',
					success: function(data){
						if(data=='1')
						{
							alert("删除成功!");
						$("#"+id).parents('li').slideUp('fast');
						}
						if(data=='0')
						{
							alert("你没有权限!");
							return;
						}
						if(data=='2')
						{
							alert("删除失败!");
							return;
						}
					}
				})
			}
	}
</script>

<script>

//********************************
//刷新分页
//********************************
(function() {
  PageDragInit($('#one'),"handle","<%=basePath%>");
  PageDragInit($('#two'),"dealed","<%=basePath%>");
  PageDragInit($('#three'),"compile","<%=basePath%>");
}());
gotoTop();
</script>
</body>
</html>