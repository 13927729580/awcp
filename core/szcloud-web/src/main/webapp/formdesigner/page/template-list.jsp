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
 		<title>菜单查询页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../../resources/include/common_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--list页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="buttons">按钮区</div>
			<div class="row" id="searchform">搜索区</div>
			<div class="row" id="datatable">数据展示区</div>
			<div class="row" id="pagers">分页</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		         <!--  <li><a href="#">首页</a></li>
		          <li><a href="#">表单设计</a></li> -->
		          <li class="active">动态页面模板管理</li>
		        </ul>
			</div>
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
			</div>
		
			<div class="row" id="datatable">
			<form  method="post" id="manuList">	
				<div class="row" id="searchform">
					<input type="hidden" name="currentPage" value="1" />
					<div class="col-md-3">
						<div class="input-group">
							<span class="input-group-addon">模板名称/ID</span>
							<input name="fileName" class="form-control" id="fileName" type="text"/>
						</div>
					</div>
					<div class="col-md-3 btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
						<button style="margin: 0 5px;" class="btn btn-success" type="reset">清空</button>
					</div>
				</div>
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>ID</th>
							<th>模板名称</th>
							<th>模板路径</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value="${vo.id}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
							<td>${vo.id}</td> 
							<td>${vo.fileName}</td> 
							<td><a href="<%=basePath%>pfmTemplateController/get.do?boxs=${vo.id}" target="_self">查看模板</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</form>
			</div>
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div>
		</div>
		
		<%@ include file="/resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>

		<script type="text/javascript">
		  $(":text[name='fileName']").val('${vo.fileName }');
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
      			var url = "<%=basePath%>pfmTemplateController/get.do";
      			location.href = url;
      			return false;
      		})

      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","<%=basePath%>pfmTemplateController/get.do").submit();
      			}else{
      				alertMessage("请选择一个模版信息进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#deleteBtn").click(function(){
          		//var count = $("input:checkbox[name='boxs']:checked").length;
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
	          		$("#manuList").attr("action","<%=basePath%>pfmTemplateController/delete.do").submit();
          		}
      			return false;
          	});
          	
          });
		</script>
	</body>
</html>
