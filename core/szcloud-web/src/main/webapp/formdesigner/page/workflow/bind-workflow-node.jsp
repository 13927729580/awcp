<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>绑定流程页面</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
	</head>
	<body id="main" class="inner-frame">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addWorkFlowBtn"><i class="icon-plus-sign"></i>保存</button>
<!-- 				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button> -->
			</div>
			<div class="row" id="searchform">
				<form action="<%=basePath%>fd/workflow/notBindNodeList.do" id="createForm" class="clearfix" method="post">
					<div class="col-sm-4">
						<input type="hidden" id="pageId" name="pageId" value="${pageId}"/>
						<!-- 弹出框选择流程 -->
						<div class="input-group">
							<span class="input-group-addon">选择流程</span>
							<input name="workflowId" class="form-control" id="workflowId" type="hidden" value="${workflowId}"/>
							<input name="workflowName" readonly="readonly" class="form-control" id="workflowName" type="text" value="${workflowName}"/>
							<div class="input-group-btn"> <button class="btn btn-default" type="button" id="workflowSelect">选择</button> </div>
						</div>
					</div>
					<div class="col-sm-4 btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
					</div>
				</form>
			</div>
			<div class="row">
			<form  method="post" id="manuList">	
				<table class="table datatable1 table-bordered">
					<thead>
						<tr>
							<th class="hidden">
								<input type="hidden" id="pageId" name="pageId" value="${pageId}"/></th>
							<th>结点名</th>
							<th>流程名</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${vos}" var="vo" varStatus="status">
						<tr id="tr${vo.NODEID}">
							<td class="hidden formData">
								<input id="nodeIds" type="hidden" value="${vo.NODEID}"/>
								<input id="worknodeName" type="hidden" value="${vo.NAME}"/>
								<input id="workFlowId" type="hidden" value="${workflowId}"/>
								<input id="workflowName" type="hidden" value="${workflowName}"/>
								<input id="priority" type="hidden" value="${status.index + 1}"/>
							</td>
							<td>${vo.NAME}</td>
							<td>${workflowName}</td>
						</tr>
						</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
				
				
			</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script type="text/javascript">
		//勾选节点，修改内容，提交到后台的是节点列表
   	    var count = 0;
		$(document).ready(function(){ 
        	$('table.datatable1').datatable({
	   			checkable: true,
	   			checksChanged:function(event){
	   				datatableChanged(event,this);
	   			}	
	   		});
        	//点击，弹出流程列表页面
        	$("#workflowSelect").click(function(){
        		top.dialog({ 
					id: 'select-workflow-dialog' + Math.ceil(Math.random()*1000000),
					title: '选择流程',
					height:600,
					width:300,
					url: "<%=basePath%>fd/workflow/templateList.do",
					onclose : function() {
						if (this.returnValue) {
							$("#workflowName").val(this.returnValue.name);
							$("#workflowId").val(this.returnValue.id);
						}
					}
				}).width(400).height(500).show();//document.getElementById("workflowName")
        	});
        	
       	 	var dialog;
    	 	try {
				dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}			 
        	 //保存按钮
			$("#addWorkFlowBtn").click(function() {
				if(count<1){
					alertMessage("请至少选择一个结点");
					return false;
				} else {
					$.ajax({
						type : "POST",
						url : "<%=basePath%>fd/workflow/relate.do",
						data : $("#manuList").serialize(),
						success : function(data) {
							dialog.close(data);
							dialog.remove();
						}
					});/**/
				}
				return false;
			});
			
			function datatableChanged(event,obj){//checksChanged.zui.datatable
				  var _$table = obj.$table; 
			      var checkArray = event.checks.checks;
 				  count = checkArray.length;//checkbox checked数量
				  _$table.find("tbody .formData").find("input").removeAttr("name");
 				  for(var i=0;i<count;i++){//给隐藏数据机上name属性
 					 	_$table.find("tbody tr#"+checkArray[i]).find("input#nodeIds").attr("name","nodes["+i+"].id");
  					  	_$table.find("tbody tr#"+checkArray[i]).find("input#worknodeName").attr("name","nodes["+i+"].name");
  					  	_$table.find("tbody tr#"+checkArray[i]).find("input#workFlowId").attr("name","nodes["+i+"].workflowId");
  					  	_$table.find("tbody tr#"+checkArray[i]).find("input#workflowName").attr("name","nodes["+i+"].workflowName");
  						_$table.find("tbody tr#"+checkArray[i]).find("input#priority").attr("name","nodes["+i+"].priority");
 				  }
			};

		});
			
		</script>
	</body>
</html>
