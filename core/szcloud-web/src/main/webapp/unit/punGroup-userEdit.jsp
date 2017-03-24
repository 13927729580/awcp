<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
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
<title>人员信息管理</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="../resources/include/common_css.jsp"%><!-- 注意加载路径 -->
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
				<li><a href="#">组织机构与权限</a></li>
				<!-- <li><a href="#">开发者</a></li> -->
				<li class="active">成员管理</li>
			</ul>
		</div>

		<div class="row" id="buttons">
			<button type="button" class="btn btn-info" id="allchecker">
				<i class="icon-plus-sign"></i>新增成员
			</button>
			<button type="button" class="btn btn-info" id="removePerson">
				<i class="icon-plus-sign"></i>删除成员
			</button>
			<button type="button" class="btn btn-info" id="alleidt">
				<i class="icon-trash"></i>编辑岗位
			</button>
		</div>

		<div class="row" id="datatable">
		<form method="post" id="userList">
			<input name="positionName" type="hidden" value="${vo.name}">
			<!--部门 or 小组ID-->
			<input type="hidden" name="groupId" value="${groupId}">
			<table class="table datatable table-bordered">
				<thead>
					<tr>
						<th class="hidden"><input type="hidden" name="rootGroupId" value="${rootGroupId}"></th>
						<th>姓名</th>
						<th>用户工号</th>
						<th>岗位</th>
						<th>联系方式</th>
					</tr>
				</thead>
				<tbody>
						<c:forEach items="${userList}" var="vo">
							<tr>
								<td class="hidden formData">
									<input id="boxs" type="text" value="${vo.userId}"> 
								</td>
								<td>${vo.name}</td>
								<td>${vo.userIdCardNumber}</td>
								<td>
									<c:forEach items="${vo.positionList}" var="p" varStatus="sta">
									<c:choose>
										<c:when test="${0==sta.index}">
											${p.name}
										</c:when>
										<c:otherwise>
											;${p.name}
										</c:otherwise>
									</c:choose>
		            				</c:forEach></td>
								<td>${vo.mobile}</td>
							</tr>
						</c:forEach>
				</tbody>
			</table>
			</form>
		</div>
	</div>

	<%@ include file="../resources/include/common_js.jsp"%>
	<script type="text/javascript">
		  $(function(){
			  var groupId = ${groupId};
			  var rootGroupId = ${rootGroupId};
			  var count=0;//默认选择行数为0
			  
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
					  		//alert(1);
						  	this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
						  	//console.log(this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs"));
						  	//console.log(this.$table.find("tbody tr").eq(checkArray[i]).find("input[name='boxs']"));
					  }
				  }
					
			  });
			  
			  $('#allchecker').bind("click", function(){
					//alert(treeId+"-"+treeNode.tId)
					location.href="<%=basePath%>punUserGroupController/get.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId;
				});
			$('#removePerson').bind("click", function(){
				//alert($("#userList").serialize());
          		<%--var _selects = new Array();
				$("input[name='boxs']").each(function(){
					var value=$(this).val();
					_selects.push(value);
				});--%>
				//alert(_selects);
				$("#userList").attr("action","<%=basePath%>punUserGroupController/removeUserFromGroup.do").submit();
				return false;
			});

				$('#alleidt').bind("click", function(){
					if(count != 1){
						alert("请选择一个人员编辑");
						return false;
					}
					var userId;
					
					var check_array=document.getElementsByName("boxs"); 
					userId = check_array[0].value;
// 					for(var i = 0; i< check_array.length;i++){
// 						if(undefined != check_array[i].value || "" !=check_array[i].value)
// 							userId=parseInt(check_array[i].value);
// 					}
					<%-- alert("<%=basePath%>punUserGroupController/getUserForEdit.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId + "&userId=" + userId); --%>
					location.href="<%=basePath%>punUserGroupController/getUserForEdit.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId + "&userId=" + userId;
				});

		});
	</script>
</body>
</html>
