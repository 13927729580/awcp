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
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>开发组管理</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li class="active">组织机构与权限</li>
		          <li class="active">开发组管理</li>
		        </ul>
			</div>
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
				<button type="button" class="btn btn-warning" id="includeBtn"><i class="icon-edit"></i>关联用户</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>dev/punDevGroupList.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">组名（中文）</span>
								<input name="nameCn" class="form-control" id="nameCn" value="${vo.nameCn}" type="text"/>
							</div>
							
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">组名（英文）</span>
								<input name="nameEn" class="form-control" id="nameEn" value="${vo.nameEn}" type="text"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<a class="btn" data-toggle="collapse" data-target="#collapseButton">取消</a>
	                         
						</div>
					</form>
				</div>
			</div>
			<div class="row" id="datatable">
				<form  method="post" id="manuList">	
            	<input type="hidden" name="currentPage" value="${currentPage}">		
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>组名（中文）</th>
							<th>组名（英文）</th>
							<th>组名（简称）</th>
							<!-- <th>组类型</th> -->
							<th>组织机构代码</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData"> <input id="boxs" type="hidden"
								value="${vo.id}">
							 </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${vo.nameCn}</td>
							<td>${vo.nameEn}</td>
							<td>${vo.nameShort}</td>
							<%-- <td><c:if test="${vo.type==1}">非根组织</c:if>
					                <c:if test="${vo.type==0}">根组织</c:if></td> --%>
							<td>${vo.code}</td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
					</form>
			</div>
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div>
		</div>
		
		<%@ include file="/resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/common.js"></script>

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
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
					  }
				  }
					
			  });
          	//add
      		$("#addBtn").click(function(){
      			$("#manuList").attr("action","<%=basePath%>dev/punDevGroupAdd.do").submit();
      			return false;
      		})
      		
      		//授权
      		/* $("#authorize").click(function(){
				var basePath = $("#basePath").val();
				var url = basePath+"unit/resAuthor.do";
				$("#ResAuthor").attr("action",url).submit();
				return false;
		    }) */

      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","<%=basePath%>dev/punDevGroupGet.do").submit();
      			}else{
      				alertMessage("请选择一个组织进行操作");
      			}
      			return false;
      		});
      		//关联
      		$("#includeBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","<%=basePath%>dev/punDevGroupInclude.do").submit();
      			}else{
      				alertMessage("请选择一个组织进行操作");
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
	          		$("#manuList").attr("action","<%=basePath%>dev/punDevGroupDelete.do").submit();
          		}
      			return false;
          	});
          	
          });
		</script>
	</body>
</html>
