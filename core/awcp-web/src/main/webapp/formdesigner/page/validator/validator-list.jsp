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
<title>校验列表</title>
<base href="<%=basePath%>">
<%@ include file="/resources/include/common_css.jsp" %>
</head>
<body id="main">
<div class="container-fluid">
			<c:choose>
  					<c:when test="${ isSelect }">
  						
  					</c:when>
  					<c:otherwise>
  						<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <!-- <li>首页</li>
		          <li>我的应用产品</li> -->
		          <li class="active">校验管理</li>
		        </ul>
			</div>
  					</c:otherwise>
				</c:choose>
			
			<div class="row" id="buttons">
				<c:choose>
  					<c:when test="${ isSelect }">
  						<button type="button" class="btn btn-info" id="submitBtn"><i class="icon-trash"></i>确定</button>
  					</c:when>
  					<c:otherwise>
  						<div class="btn-group">
		           			<div class="btn-group">
			              		<button type="button" id="addBtn" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
			              			<i class="icon-plus-sign"></i>新增  <span class="caret"></span> 
			              		</button>
			              		<ul class="dropdown-menu" role="menu">
			                		<li><a href="javascript:void(0)" onclick="addValidator('1');">范围校验</a></li>
			                		<li><a href="javascript:void(0)" onclick="addValidator('2');">正则表达式校验</a></li>
			                		<li><a href="javascript:void(0)" onclick="addValidator('3');">比较校验</a></li>
			                		<li><a href="javascript:void(0)" onclick="addValidator('4');">自定义校验</a></li>
			              		</ul>
		            		</div>
	          			</div>
						<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
						<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
  					</c:otherwise>
				</c:choose>				
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="in">
					<form action="" id="createForm" class="clearfix" method="post">
						<input type="hidden" name="isSelect" value="${isSelect }" />
						<input type="hidden" name="currentPage" value="0" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">校验名称</span>
								<input name="name" class="form-control" id="name" type="text" value="${name }"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">校验描述</span>
								<input name="validatorDes" class="form-control" id="validatorDes" type="text" value="${validatorDes }"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="submit"><c:choose>
  									<c:when test="${ isSelect }">
  										搜索
  									</c:when>
  									<c:otherwise>
										提交
									</c:otherwise>
								</c:choose>	</button>
						</div>
					</form>
				</div>
			</div>
			
			<form  method="post" id="manuList">
	        <input type="hidden" name="currentPage" value="${currentPage}">	
	        <input type="hidden" name="isSelect" value="${isSelect}">	
			<div class="row" id="datatable">
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<!-- <th>编码</th> -->
							<th>名称</th>
							<th>描述</th>
						</tr>
					</thead>
					<tbody>	
					<c:forEach items="${vos}" var="vo">
						<tr>
							<td class="hidden formData">
								<input id="boxs" type="hidden" value="${vo.id}"></td>
							<!-- <td><a href="<%=basePath%>fd/validator/edit.do?_selects=${vo.id}" >${vo.code}</a></td> -->
							<td>
								<c:choose>
  									<c:when test="${ isSelect }">
  										${vo.name}
  									</c:when>
  									<c:otherwise>
										<a href="<%=basePath%>fd/validator/edit.do?_selects=${vo.id}" >${vo.name}</a> 
									</c:otherwise>
								</c:choose>	
							</td>
							<td>
								<span class="text-ellipsis">${vo.description }</span>
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
	<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript">
	function addValidator(type){
		var url = "<%=basePath%>fd/validator/edit.do?type="+type;
		location.href = url;
		return false;
	}
	
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
		  	
			//update
			$("#updateBtn").click(function(){
				if(count == 1){
					$("#manuList").attr("action","<%=basePath%>fd/validator/edit.do").submit();
				}else{
					alertMessage("请选择某个资源进行操作");
				}
				return false;
			});
			
	    	//delete
	    	$("#deleteBtn").click(function(){
	    		if(count < 1){
	    			alertMessage("请至少选择一项进行操作");
	    			return false;
	    		}
	    		if(window.confirm("确定删除？")){
	        		$("#manuList").attr("action","<%=basePath%>fd/validator/delete.do").submit();
	    		}
				return false;
	    	});
	    	
	    	//确认选择
	    	$("#submitBtn").click(function(){
	    		try {
					var dialog = top.dialog.get(window);
				} catch (e) {
					
				}
				if(dialog != null){
					var data = $("#manuList input[name='_selects']").val();
					//$("#manuList input[name='_selects']").each(function(){
					//	data += $(this).val() + ",";
					//});
					
					//alert(data);
					//alert(data.length);
					//if(data.length > 1)
					//	data = data.substring(0,data.length-1);
					//alert(data);
					dialog.close(data);
					dialog.remove();
				}
				return false;
	    	});

    });
	</script>


</body>
</html>


