<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" import="org.szcloud.framework.formdesigner.engine.util.DocumentUtils" %>
<%@ page language="java" import="org.szcloud.framework.unit.vo.PunUserBaseInfoVO" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
	<style>
	html{
		height:100%;
	}
	html,body{
		font-family: Microsoft YaHei,Arial;
		color:#333;
	}
	.title{   
        width:300px;   
        white-space:nowrap;   
        word-break:keep-all;   
        overflow:hidden;   
        text-overflow:ellipsis;
        font-color:black; 
    }  
	.welcome{
		padding-top: 20%;
		font-size: 24px;
		text-align: center;
		text-shadow:5px 2px 6px #747474;
		-webkit-text-shadow:5px 2px 6px #747474;
		-moz-text-shadow:5px 2px 6px #747474;
		-o-text-shadow:5px 2px 6px #747474;
	}		
	</style>
	<link href="<%=basePath%>resources/styles/hosstyle.css?v=765345" rel="stylesheet">
	<link href="<%=basePath%>resources/plugins/zui/dist/css/zui.css" rel="stylesheet">
</head>
<body>
	<div class="calendercontainer clearfix">
		<table class="table datatable table-bordered">
			<thead>
				<tr>
					<th class="hidden"><input type="hidden" name="currentPage" value="${currentPage}">	</th>
					<th data-width="150px">Project Name</th>
					<th data-width="150px">Person in Charge</th>
					<th data-width="120px">Created On</th>
					<th data-width="120px">Status</th>
					<th data-width="150px">Current Note</th>
					<th data-width="150px">Comments</th>
				</tr>
			</thead>
			<tbody>	
				<c:forEach items="${projectList}" var="project">
				<tr>
					<td>${project.projectName}</td>
					<td>${project.personInCharge}</td>
					<td>${project.createDate}</td>
					<td>${project.status}</td>
					<td></td> 
					<td>${project.comments}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
						
		<div class="platformContainer clearfix">	    
			<div>
				<table style="width:100%;border:solid 1px #cccccc;border-collapse:collapse;" id="homeTable">					
					<tr style="height:260px;">
						<td width="50%" style="border:solid 1px #cccccc;padding:12px;">
							<div class="hhead clearfix"><a class="headtitle" href="javascript:void(0)">Personal Office</a>
								<p style="float:right;cursor:pointer;" onclick=""><span class="label label-badge label-gray"><i class="icon icon-edit"></i>More</span></p>		
							</div>
							<div class="newbody">
								<ul>
									<c:forEach items="${wfList}" var="item">
									<li>
										<a href="<%=basePath%>WF/MyFlow.jsp?FK_Flow=${item.FK_Flow}&FK_Node=${item.FK_Node}&WorkID=${item.WorkID}&FID=${item.FID}&Paras=&T=1012084929<c:if test="${item.WFState == 8}">&IsAskFor=1</c:if>">
											<b style="font-weight: 700;float:left">• </b>
											<span class="title pull-left" style="width:400px">${item.Title}</span>
										</a>
									</li>
									</c:forEach>
								</ul>
							</div>																	
						</td>
						<td width="50%" style="border:solid 1px #cccccc;padding:12px;">Documents Search</td>
					</tr>
					<tr style="height:260px;" id="myTr">
						<td style="border:solid 1px #cccccc;padding:12px;">
							<div class="hhead clearfix"><a class="headtitle" href="javascript:void(0)">Personal Task List</a>
								<p style="float:right;cursor:pointer;" onclick=""><span class="label label-badge label-gray"><i class="icon icon-edit"></i>More</span></p>
								<p style="float:right;cursor:pointer;" onclick="javascript:window.location ='<%=basePath%>WF/MyFlow.jsp?FK_Flow=034&FK_Node=3401&T='"><span class="label label-badge label-gray"><i class="icon icon-plus"></i> Add Task</span></p>
							</div>
							<div class="hbody clearfix">
							   <div class="newbody">
									<table class="table customTable table-striped ">
										<thead>
											<tr>
												<th style="width:160px">Task Name</th>
												<th style="width:140px">Due Date</th>
												<th style="width:110px">Priority</th>
												<th style="width:110px">Status</th>
											</tr>
										</thead>
										<c:forEach items="${myTaskList}" var="wpc">
											<tr>
												<td><a href="<%=basePath%>WF/MyFlow.jsp?FK_Flow=${wpc.FK_Flow}&FK_Node=${wpc.FK_Node}&WorkID=${wpc.WorkID}&FID=${wpc.FID}&Paras=&T=1012084929<c:if test="${wpc.WFState == 8}">&IsAskFor=1</c:if>">${wpc.taskName}</a></td>
												<td>${wpc.dueDate}</td>
												<td>${wpc.priority}</td>
												<td>${wpc.status}</td> 
											</tr>
										</c:forEach>
									</table>
								</div>
							</div>	
							<!--
							<div class="hhead clearfix"><a class="headtitle" href="javascript:void(0)">Assigned Task List</a>
								<p style="float:right;cursor:pointer;" onclick=""><span class="label label-badge label-gray"><i class="icon icon-edit"></i>More</span></p>
								<p style="float:right;cursor:pointer;" onclick="javascript:window.location ='<%=basePath%>WF/MyFlow.jsp?FK_Flow=034&FK_Node=3401&T='"><span class="label label-badge label-gray"><i class="icon icon-plus"></i> Add Task</span></p>
							</div>
							<div class="hbody clearfix">
							   <div class="newbody">
									<table class="table table-striped customTable" id="myTable">
										<thead>
											<tr>
												<th style="width:160px">Task Name</th>
												<th style="width:100px">Due Date</th>
												<th style="width:160px">Owner</th>
												<th style="width:80px">Priority</th>
												<th style="width:80px">Status</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${assignedTaskList}" var="wpc">
											<tr>
												<td><a href="<%=basePath%>WF/MyFlow.jsp?FK_Flow=${wpc.FK_Flow}&FK_Node=${wpc.FK_Node}&WorkID=${wpc.WorkID}&FID=${wpc.FID}&Paras=&T=1012084929<c:if test="${wpc.WFState == 8}">&IsAskFor=1</c:if>">${wpc.taskName}</a></td>												
												<td>${wpc.dueDate}</td>
												<td>${wpc.joinPerson}</td>
												<td>${wpc.priority}</td>
												<td>${wpc.status}</td> 
											</tr>
										</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							-->
						</td>
						<td style="border:solid 1px #cccccc;padding:12px;">Notice</td>
					</tr>
					<tr style="height:260px;">
						<td style="border:solid 1px #cccccc;padding:12px;">Reminder</td>
						<td style="border:solid 1px #cccccc;padding:12px;">Meetings</td>
					</tr>
				</table>
			</div>
		</div>
	</div>

	<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
	<script src="/szcloud/resources/scripts/jquery.cookies.js"></script>
	<script type="text/javascript">

		$(document).ready(function() {
			var dept = $.cookie('FK_DeptName');
			if(dept=="Agent"){

			}
			var basePath = "<%=basePath%>";
			$.ajax({		
				type: "POST",		
				url: basePath + "ousonUtils/isTradesman.do",		
				async : false,		
				success: function(data){			
					var count = data.count;
					if(count==1){
						
					}
				}	
			});	
		});

		$(function(){
			var height = $("#myTable").height();
			if(height>174){
				$("#myTr").height(height+40);
			}					
			$(".customTable").find("td").css("padding","4px");
			$(".customTable").find("th").css("padding","4px");
		});
		

		$(function(){
			$(".newsset").each(function(){
				if($(this).find("li").size()==0){
					var newclass = $(this).find("a").eq(0).attr("class")+'gray';
					$(this).find("a").eq(0).attr("class",newclass);
				}
			});
		});

		//点击首页栏目设定左侧菜单
		function showCurrentMenu(menuName){
			var currentName = menuName;
			$('#CloudMenu',parent.document).find("a").each(function(){
				if(($(this).attr("title")=="Personal Office") && (currentName == "Personal Office")){
					$(this).click();
					$(this).removeClass("curSelectedNode");
					$(this).next().find("a").each(function(){
						if($(this).attr("title")=="Upcoming Work"){
							$(this).click();
							$(this).addClass("curSelectedNode");
						}
					})
					return false;
				}		
			})
		}
	</script>
</body>
</html>

