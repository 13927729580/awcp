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
 		<title>公文列表</title>
 		<%@ include file="../../resources/include/common_css.jsp" %>
 		<link rel="stylesheet" href="../../resources/plugins/treeTable/default/jquery.treeTableNew.css">
	</head>
	<body id="main">
		<div class="container-fluid">
			
			<div class="row" id="buttons">
				<!--<button type="button" class="btn btn-success" id="deleteBtn">
					<i class="icon-edit">删除</i>
				</button>-->
				<button type="button" class="btn" id="rebackBtn">
					<i class="icon-edit">返回</i>
				</button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton">
					<form action="<%=basePath%>workflow/wf/templateList.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="1" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">公文模板类型</span>
								<!--<input class="form-control" id="templateType" type="text"/>-->
								<select name="templateTypeId" class="form-control">
									<option value="" >请选择</option>
						     	 	<c:forEach items="${templateTypes}" var="item">
							  	 	   <option value="${item.TypeId}" <c:if test="${item.TypeId==templateTypeId}">selected="selected"</c:if> >${item.TypeName}</option>
							  		</c:forEach>
							  </select>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input class="form-control" name="templateName" value="${templateName}" type="text"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<!--<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a> -->
						</div>
					</form>
				</div>
			</div>	
			
			<div class="row" id="datatable">
				<form  action="<%=basePath%>/workflow/wf/templateList.do"  method="post" id="menuList">	
	            <input type="hidden" name="currentPage" value="${currentPage}">	
				<table id="treeTable1" class="table table-bordered">
					<thead>
						<tr>
							
					        <th>公文名称</th>
					        <th style="width:8%">轨迹图</th>
							
						</tr>
					</thead>
					<tbody id="tableTbody">
					
					</tbody>
				</table>
				</form>
			</div>
		
		</div>
		
		<%@ include file="../../resources/include/common_js.jsp" %>
		<!--<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>-->
		
		<link rel="stylesheet" href="<%=basePath%>base/resources/artDialog/css/ui-dialog.css">
		<script src="<%=basePath%>base/resources/artDialog/dist/dialog-min.js"></script>
		<script src="<%=basePath%>resources/plugins/treeTable/jquery.treeTableNew.js?v=201506131313" type="text/javascript"></script>
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
			  
			  $("#rebackBtn").click(function(){
	          	   $("#menuList").attr("action","<%=basePath%>/workflow/wf/listPersonalTasks.do").submit();
      			   return false;
      		  });
      		
			  $("#deleteBtn").click(function(){
			  	if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
	          		$("#menuList").attr("action","<%=basePath%>/workflow/wf/deleteTemplate.do").submit();
          		}
      			return false;
      		});
		  });
		</script>
		



<script type="text/javascript">
        $(function(){
					var groupStr = ${tableJson};
					
					var dateArray = eval(groupStr);
					
					var trs = "";
					$.each(dateArray,function(n,value){ 
						
						
						if((value.PID==null)||(value.PID=="")){
							trs +="<tr id="+value.ID+"><td  class='hidden formData'></td><td><span class='prev_span'></span><span class='default_active_node default_open' arrow='true'></span><span controller='true'>"+value.Name+"</span></td></tr>";
						}
						else
						{
							trs +="<tr id="+value.ID+" pId="+value.PID+"><td  class='hidden formData'><input id='id' type='hidden' value="+value.ID+"></td><td><span class='prev_span'><span class='default_node default_vertline'></span></span><span class='default_node default_leaf' arrow='true'></span><a href='<%=basePath%>WF/MyFlow.jsp?FK_Flow="+value.ID+"&FK_Node="+value.NodeID+"&T='>"+value.Name+"</a></td><td><a href='<%=basePath %>WF/Admin/CCFlowDesigner/Truck.html?FK_Flow="+value.ID+"&DoType=Chart&T=sf367e7a6-eb72-494d-a05d-8f59ba838863151012110127'>流程图</a></td></tr>";
						
						}
					});
					
					$('table.table tbody').html("");
					$('table.table tbody').append(trs);
					
            var option = {
                theme:'default',
                expandLevel : 2,
                beforeExpand : function($treeTable, id) {
                    //判断id是否已经有了孩子节点，如果有了就不再加载，这样就可以起到缓存的作用
					
                    if ($('.' + id, $treeTable).length) { return; }
                 
                },
                onSelect : function($treeTable, id) {
                    window.console && console.log('onSelect:' + id);
                    
                }

            };
            $('#treeTable1').treeTable(option);

           
        });
		
</script> 
		
		
	</body>
</html>
