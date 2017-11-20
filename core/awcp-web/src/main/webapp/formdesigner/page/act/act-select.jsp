<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>动作列表</title>
<base href="<%=basePath%>">
<%@ include file="/resources/include/common_css.jsp" %>
</head>
<body id="main">
<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <!-- <li>首页</li>
		          <li>我的应用产品</li> -->
		          <li class="active">页面动作库</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="okBtn"><i class="icon-plus-sign"></i>确定</button>
				<button type="button" class="btn btn-info" id="cancelBtn"><i class="icon-trash"></i>取消</button>
			</div>
			<div class="row" id="searchform">
				<div id="collapseButton" class="in">
					<form action="" id="createForm" class="clearfix" method="post">
						<input type="hidden" name="currentPage" value="1"  />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">动作名称</span>
								<input name="name" class="form-control" id="name" type="text" value="${name }"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">动作描述</span>
								<input name="actDes" class="form-control" id="actDes" type="text" value="${actDes }"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary"  type="submit">提交</button>
						</div>
					</form>
				</div>
			</div>
			
			<form  method="post" id="manuList">	
	        <input type="hidden" name="currentPage" value="${currentPage}">
	        <input type="hidden" name="dynamicPageId" value="${dynamicPageId}" id="dynamicPageId">	
	        <input type="hidden" name="order" value="${order}" id="order">
			<div class="row" id="datatable">
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>名称</th>
							<th>类型</th>
							<th>描述</th>
							<th>所属动态页面</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData">
								<input id="boxs" type="hidden" value="${vo.pageId}"></td>
							<td>
								<span class="text-ellipsis">${vo.name}</span></td>
							<td>
								<span class="text-ellipsis">${actTypes[vo.actType] }</span>
							</td>
							<td>
								<span class="text-ellipsis">${vo.description }</span>
							</td>
							<td>
								<span class="text-ellipsis">${vo.dynamicPageName }</span>
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
	$(document).ready(function(){
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
		var dialog;
		try {
			dialog = top.dialog.get(window);
			dialog.height($(document).height());
			dialog.reset();
		} catch (e) {
			//return;
		}
		 $("#okBtn").click(function(){
			 var arr=new Array();
			 $("#boxs[name='_selects']").each(function(){
				 var id= $(this).val();
				 arr.push(id);
			 });
			 if(arr.length > 0) {
			 $.ajax({
				 url:"<%=basePath%>fd/act/copy.do?dynamicPageId="+$('#dynamicPageId').val(),
				 type:"POST",
				 async:false,
				 data:{_selects:arr.join(",")},
				 success:function(ret){
						var json=eval(ret);
						dialog.close(json);
						dialog.remove();
				 },
				 error: function (XMLHttpRequest, textStatus, errorThrown) { 
		                  alert(errorThrown); 
				 }
			 });
			 }
		 });
    });
	</script>
</body>
</html>


