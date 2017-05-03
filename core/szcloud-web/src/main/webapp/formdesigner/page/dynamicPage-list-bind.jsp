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
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
			<button type="button" class="btn btn-danger" id="deleteBtn"><i class="icon-trash"></i>删除</button>
			<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
		</div>
			
		<div class="row" id="searchform">
			<div id="collapseButton" class="in">
				<form action="<%=basePath%>common/user/list-bind.do" id="createForm" class="clearfix" method="post">
					<input type="hidden" name="currentPage" value="1" />
					<div class="col-md-3">
						<div class="input-group">
							<span class="input-group-addon">动态页面ID</span>
							<input name="name" class="form-control" id="name" type="text" value="${name}"/>
						</div>
					</div>
					<div class="col-md-3 btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
					</div>
				</form>
			</div>
		</div>
			
			<form action="<%=basePath%>common/user/list-bind.do"  method="post" id="manuList">	
	        <input type="hidden" name="currentPage" value="${currentPage}">	
			<div class="row" id="datatable">
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>ID</th>
							<th>PAGEID_PC</th>
							<th>PAGEID_PC_LIST</th>
							<th>PAGEID_APP</th>
							<th>PAGEID_APP_LIST</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${vos}" var="vo" varStatus="index">
						<tr>
							<td class="hidden formData">
								<input id="boxs" type="hidden" value="${vo.id}"></td>
							<td>${index.index+1}</td>
							<td>${vo.PAGEID_PC}</td>
							<td>${vo.PAGEID_PC_LIST}</td>
							<td>${vo.PAGEID_APP}</td>
							<td>${vo.PAGEID_APP_LIST}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</form>
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
    	
		  	//add
			$("#addBtn").click(function(){
				var url = "<%=basePath%>fd/list-bind.do";
				location.href = url;
				return false;
			});
		
			//update
			$("#updateBtn").click(function(){
				if(count == 1){
				
					var id=$("input[name='_selects']").val();
					var urlStr="formdesigner/page/dynamicPage-bind-update.jsp";
					var postData={};
					postData.id=id;
		
					top.dialog({
						id: 'add-dialog' + Math.ceil(Math.random()*10000),
						title: '载入中...',
						url: urlStr,
						data:postData,
						onclose: function () {
							if (this.returnValue) {
								var ret= this.returnValue;
								alert("保存成功");
								location.href="<%=basePath%>/common/user/list-bind.do";
							}
						}
					}).showModal();
					return false;
				}else{
					alertMessage("请选择某个资源进行操作");
				}
				return false;
			});

	    	//delete
	    	$("#deleteBtn").click(function(){
	    		if(count<1){
	    			alertMessage("请至少选择一项进行操作");
	    			return false;
	    		}
	    		if(window.confirm("确定删除？")){
	        		$("#manuList").attr("action","<%=basePath%>/common/user/delete.do").submit();
	    		}
				return false;
	    	});
    	});
	</script>
</body>
</html>


