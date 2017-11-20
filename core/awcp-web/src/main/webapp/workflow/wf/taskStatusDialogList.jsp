<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>流程管理</title>
 		<%@ include file="../../resources/include/common_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">

			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>/workflow/wf/taskStatus.do" id="menuList" class="clearfix">
						<input type="hidden" name="currentPage" value="1" />
						
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a>
						</div>
					</form>
				</div>
			</div>
			
			<div class="row" id="datatable">
				<form  action="<%=basePath%>/workflow/wf/taskStatus.do"  method="post" id="createForm">	
	            <input type="hidden" name="currentPage" value="${currentPage}">
	            <input type="hidden" name="taskType" id="taskType" value="${taskType}">	
	            <input type="hidden" name="message" value="${message}" id="message">
	            <input type="hidden" name="id" id="id" value="${id}">
	            <input type="hidden" name="entryID" id="entryID" value="${entryID}">
	            <input type="hidden" name="dialog" id="dialog" value="dialog">
				<table class="table datatable table-bordered">
					<thead>
					        <th data-width="130">办理时间</th>
					        <th data-width="160">从节点</th>
							<th data-width="100">人员</th>
					        <th class="text-ellipsis">到达节点</th>
					        <th data-width="180">人员</th>
					        <th data-width="100">活动</th>
					        <th data-width="160">信息</th>
					        <th data-width="160">执行人</th>
					</thead>
					<tbody>
					<c:forEach items="${ls}" var="item">
						<tr>
							<td>${item.RDT}</td>
							<td>${item.NDFromT}</td>
							<td>${item.EmpFromT}</td>
							<td>${item.NDToT}</td>
							<td>${item.EmpToT}</td>
							<td>
								<c:if test="${item.ActionType == 0}">  
  											空白
　　								</c:if>   
　　								<c:if test="${item.ActionType == 1}">  
  									草稿
　　								</c:if>    
　　								<c:if test="${item.ActionType == 2}">  
  									运行中
　　								</c:if>    
　　								<c:if test="${item.ActionType == 3}">  
  									已完成
　　								</c:if>    
　　								<c:if test="${item.ActionType == 4}">  
  									挂起
　　								</c:if>    
　　								<c:if test="${item.ActionType == 5}">  
  									退回
　　								</c:if>    
　　								<c:if test="${item.ActionType == 6}">  
  									移交
　　								</c:if>   
　　								<c:if test="${item.ActionType == 7}">  
  									删除
　　								</c:if>   
　　								<c:if test="${item.ActionType == 8}">  
  									加签
　　								</c:if>   
　　								<c:if test="${item.ActionType == 9}">  
  									调用起子流程
　　								</c:if>   
　　								<c:if test="${item.ActionType == 10}">  
  									启动子流程
　　								</c:if>     
　　								<c:if test="${item.ActionType == 11}">  
  									子线程前进
　　								</c:if>    
　　								<c:if test="${item.ActionType == 12}">  
  									取回
　　								</c:if>    
　　								<c:if test="${item.ActionType == 13}">  
  									 恢复已完成的流程
　　								</c:if>    
　　								<c:if test="${item.ActionType == 14}">  
  									强制终止流程
　　								</c:if>    
　　								<c:if test="${item.ActionType == 15}">  
  									 挂起
　　								</c:if>    
　　								<c:if test="${item.ActionType == 16}">  
  									取消挂起
　　								</c:if>    
　　								<c:if test="${item.ActionType == 17}">  
  									 强制移交
　　								</c:if>    
　　								<c:if test="${item.ActionType == 18}">  
  									催办
　　								</c:if>    
　　								<c:if test="${item.ActionType == 19}">  
  									撤销流程
　　								</c:if>    
　　								<c:if test="${item.ActionType == 20}">  
  									撤销流程
　　								</c:if>    
　　								<c:if test="${item.ActionType == 21}">  
  									抄送
　　								</c:if>    
　　								<c:if test="${item.ActionType == 22}">  
  									 工作审核
　　								</c:if>    
　　								<c:if test="${item.ActionType == 23}">  
  									删除子线程
　　								</c:if>    
　　								<c:if test="${item.ActionType == 24}">  
  									请求加签
　　								</c:if>    
　　								<c:if test="${item.ActionType == 25}">  
  									加签向下发送
　　								</c:if>    
　　								<c:if test="${item.ActionType == 26}">  
  									自动条转的方式向下发送
　　								</c:if>    
　　								<c:if test="${item.ActionType == 27}">  
  									队列发送
　　								</c:if>    
　　								<c:if test="${item.ActionType == 28}">  
  									协作发送
　　								</c:if> 
							</td>
							<td>${item.Msg}</td>
							<td>${item.Exer}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</form>
			</div>
			
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="models"></sc:PageNavigation> 
			</div>
		</div>
		
		<%@ include file="../../resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
		
		<link rel="stylesheet" href="<%=basePath%>base/resources/artDialog/css/ui-dialog.css">
		<script src="<%=basePath%>base/resources/artDialog/dist/dialog-min.js"></script>

		<script type="text/javascript">
		
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						 this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
					  }
				  }
			  });
			  
			 var message = $("#message").val();
			 if(message != null && message != ''){
			 	alertMessage(message);
			 }
			  
			  $("#returnBtn").click(function(){
			       //if(${taskType} == 1)
      				//	$("#menuList").attr("action","<%=basePath%>/workflow/wf/listPersonalTasks.do").submit();
      				//else
      				//    $("#menuList").attr("action","<%=basePath%>/workflow/wf/listHistoryTasks.do").submit();
      				if(${taskType} == 1)
			     		  window.open("<%=basePath%>/workflow/wf/listPersonalTasks.do","_self");
      				else if(${taskType} == 2)
      					window.open("<%=basePath%>/workflow/wf/listHistoryTasks.do","_self");
      				else
      					window.open("<%=basePath%>/workflow/wf/inDoingTasks.do","_self");
      			return false;
      		});
		  });
		</script>
	</body>
</html>
