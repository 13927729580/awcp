<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>动态页面表单</title>
	<base href="<%=basePath%>">
	<%@ include file="/resources/include/common_css.jsp" %>
</head>
<body id="main">
	<div class="container-fluid">
		<input type="hidden" id="mes" value="${mes}"/>
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>绑定</button>
		</div>
			
		<div class="row" id="searchform">
			<div id="collapseButton" class="in">
				<form action="<%=basePath%>fd/list-bind.do" id="createForm" class="clearfix" method="post">
					<input type="hidden" name="currentPage" value="1" />
					<div class="col-md-3">
						<div class="input-group">
							<span class="input-group-addon">动态页面名称</span>
							<input name="name" class="form-control" id="name" type="text" value="${name }"/>
						</div>
					</div>
					<div class="col-md-3 btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
					</div>
				</form>
			</div>
		</div>
			
		<form action="<%=basePath%>fd/list.do"  method="post" id="manuList">	
	        <input type="hidden" name="currentPage" value="${currentPage}">	
			<div class="row" id="datatable">
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>名称</th>
							<th>类型</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData">
								<input id="boxs" type="hidden" value="${vo.id}"></td>
							<td><a href="<%=basePath%>fd/edit.do?_selects=${vo.id}" >${vo.name}</a></td>
							<td>
								<c:choose>
									<c:when test="${vo.pageType == 1001 }">普通页面</c:when>
									<c:when test="${vo.pageType == 1002 }">表单页面</c:when>
									<c:when test="${vo.pageType == 1003 }">列表页面</c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</td>
							<td>
								<fmt:formatDate value="${vo.created }" pattern="yyyy-MM-dd"/>
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

	<%@ include file="/resources/include/common_js.jsp" %>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
	<script type="text/javascript">
	
		$(function(){
		  	var count=0;//默认选择行数为0
		  	$('table.datatable').datatable({
			 	checkable: true,
			  	checksChanged:function(event){
				  	this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
				  	var checkArray = event.checks.checks;
				  	count = checkArray.length;//checkbox checked数量
				  	for(var i=0;i<count;i++){//给隐藏数据机上name属性
					  	this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","_selects");
				  	}
			  	}
		  	});
		  
		 	if($("#mes").val()=="error"){
		 		alert("该表单已绑定其他表单请核对");
		 		$("#mes").val("");
		 	}
    		
		 	//add
			$("#addBtn").click(function(){
				if(count<1){
	    			alertMessage("请至少选择一项进行操作");
	    			return false;
	    		}
	    		if(count>4){
	    			alertMessage("最多只能选择思项进行绑定");
	    			return false;
	    		}
	    		if(window.confirm("确定绑定？")){
	        		$("#manuList").attr("action","<%=basePath%>/common/user/binding.do").submit();
	    		}
				return false;
			});
   	 	});
	</script>
</body>
</html>


