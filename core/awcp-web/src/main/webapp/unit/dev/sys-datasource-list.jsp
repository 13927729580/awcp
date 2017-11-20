<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>系统管理</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="/resources/include/common_css.jsp"%><!-- 注意加载路径 -->
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row">
				<form class="form-horizontal" id="groupForm" method="post">
						
				<div class="form-group">
					<label class="col-md-2 control-label required">系统名称:</label>
					<div class="col-md-4">
						<input name="menuName" class="form-control" id="menuName"
							type="text" placeholder="" value="${sys.sysName}">
					</div>	
				</div>
			</form>
			<h3>数据源</h3>
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="relatesysBtn"><i class="icon-plus-sign"></i>关联</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
			</div>
			<form action=""  method="post" id="manuList">
			<input type="hidden" name="systemId" id="id" value="${sys.sysId}" /> 	
	        <input type="hidden" name="currentPage" value="${currentPage}">	
			<div class="row" id="datatable">
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>名称</th>
							<th>驱动</th>
							<th>地址</th>
							<th>用户名</th>
							<th>密码</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData">
								<input id="_selects" type="hidden" value="${vo.id}"></td>
							<td>${vo.name}</td>
							<td>
								${vo.sourceDriver}
							</td>
							<td>
								${vo.sourceUrl}
							</td>
							<td>
								${vo.userName}
							</td>
							<td>
								${vo.userPwd}
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			</form>
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div>
	</div>

	<%@ include file="/resources/include/common_js.jsp"%>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
	<script type="text/javascript">
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#_selects").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#_selects").attr("name","_selects");
					  }
				  }
					
			  });
          	//新增
      		$("#relatesysBtn").click(function(){
			top.dialog(
					{ id: 'add-dialog' + Math.ceil(Math.random()*10000),
					title: '关联',
						url:'<%=basePath%>dataSys/toRelate.do?_selects='+$("#id").val(),
						width:1024,
						height:768,
						onclose : function(){
						if (this.returnValue) {
							var ret = this.returnValue;
							window.location.href="<%=basePath%>dataSys/config.do?id="+ret;
						}
					}
						
					}
			).showModal();
			return false;
      		});
      		
      		
      		
          	//delete
          	$("#deleteBtn").click(function(){
      			if(count>0){
      				if(!window.confirm("确定删除该数据源？")){
      					return false;
      				}
      				$("#manuList").attr("action","<%=basePath%>dataSys/removeRelate.do").submit();
      			}else{
      				alert("请至少选择一项进行操作");
      			}
      			return false;
				})
		});
		 
	</script>
</body>
</html>
