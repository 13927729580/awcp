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
		<!-- <div class="row" id="breadcrumb">
			<ul class="breadcrumb">
	          <li><i class="icon-location-arrow icon-muted"></i></li>
	          <li>首页</li>
	          <li>我的应用产品</li>
	          <li class="active">动态页面管理</li>
	        </ul>
		</div> -->
			
		<div class="row" id="buttons">
			<button class="btn btn-primary" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
			<button class="btn btn-success" id="updateBtn"><i class="icon-edit"></i>修改</button>
			<button class="btn btn-danger" id="deleteBtn"><i class="icon-trash"></i>删除</button>		
			<button class="btn btn-primary" id="copyBtn"><i class="icon-plus-sign"></i>复制</button>
			<button class="btn btn-primary" id="copyToSystemBtn"><i class="icon-plus-sign"></i>复制到</button>
			<button class="btn btn-warning" id="publishBtn"><i class="icon-edit"></i>发布</button>
			<button class="btn btn-warning" id="checkOutBtn"><i class="icon-edit"></i>签出</button>
			<button class="btn btn-warning" id="checkInBtn"><i class="icon-edit"></i>签入</button>
			<button class="btn btn-info " id="export"><i class="icon-zoom-out"></i>预览</button>
			<button class="btn btn-info " id="relation"><i class="icon-zoom-out"></i>引用关系</button>
			<button class="btn  btn-info" id="exportExcel"><i class="icon-plus-sign"></i>导出Excel</button>
			<button class="btn  btn-info" id="catTemplate"><i class="icon-plus-sign"></i>查看发布后模版</button>			
		</div>
			
		<div class="row">
			<div id="collapseButton" class="in">
				<form action="<%=basePath%>fd/list.do" id="groupForm" class="clearfix" method="post">
					<div class="form-group">
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">页面名称/ID</span>
								<input name="name" class="form-control" id="name" value="${name }" type="text"/>
								<input type="hidden" name="currentPage" value="${currentPage }">
							</div>
						</div>
						<div class="col-md-2">
							<div class="input-group">
								<span class="input-group-addon">列表分类</span>
								<select class="form-control"  data-allow-clear="true" tabindex="2" name="pageType">
										<option value=""></option>
										<option value="1001">普通页面</option>
										<option value="1002">表单页面</option>
										<option value="1003">列表页面</option>
								</select>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">页面模板</span>
								<select class="form-control"  data-allow-clear="true" tabindex="2" name="templateId">
										<option value=""></option>
										<c:forEach items="${templates}" var="item" varStatus="status">
											<option value="${item.id}">${item.text }</option>
										</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-2">
							<div class="input-group">
								<span class="input-group-addon">菜单页面</span>
								<select class="form-control"  data-allow-clear="true" tabindex="2" name="menuId">
										<option value=""></option>
										<c:forEach items="${menus}" var="item" varStatus="status">
											<option value="${item.id}">${item.text }</option>
										</c:forEach>
								</select>
							</div>
						</div>
						<div class="col-md-2 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<button style="margin: 0 5px;" class="btn btn-success" type="reset">清空</button>
						</div>
					</div>
					<div class="row" id="datatable">
						<table class="table datatable table-bordered">
							<thead>
								<tr>
									<th class="hidden"></th>
									<th>ID</th>
									<th>名称</th>
									<th>类型</th>
									<th>创建时间</th>
									<th>签出者</th>
									<th>签出状态</th>
								</tr>
							</thead>
							<tbody>	
							<c:forEach items="${vos}" var="vo">
								<tr>
									<td class="hidden formData">
										<input id="boxs" type="hidden" value="${vo.id}">
									</td>
									<td><a href="<%=basePath%>fd/edit.do?_selects=${vo.id}" >${vo.id}</a></td>
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
									<td>${vo.checkOutUser }</td>
									<td>
										<c:choose>
											<c:when test="${vo.isCheckOut == 1 }">签出</c:when>
											<c:when test="${vo.isCheckOut == 0 }">签入</c:when>
											<c:otherwise></c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</form>
			</div>
		</div>
			
		<div class="row navbar-fixed-bottom text-center" id="pagers">
			<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
		</div>
		
		<div>
			<form action="" style="display: none;" id="myForm" method="POST"></form>
		</div>
	</div>

	<%@ include file="/resources/include/common_js.jsp" %>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
	<script type="text/javascript">
		var basePath = "<%=basePath%>";
		<%
			String tips = request.getParameter("tips");
			if(tips!=null&&tips.length()>0){
				tips = new String(tips.getBytes("ISO-8859-1"), "UTF-8");
			}
		%>
		$(function(){
			var tips = "<%=tips%>";
			if(tips != ""&&tips != "null"){
				alertMessage(tips);
			}
			
			$("select[name='pageType']").val('${pageType}');
			$("select[name='templateId']").val('${templateId}');
			$("select[name='menuId']").val('${menuId}');
			
			var height = $(top).height();
    		height = height -145;
			
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
				var url = "<%=basePath%>fd/edit.do";
				location.href = url;
				return false;
			});
		
			//update
			$("#updateBtn").click(function(){
				if(count == 1){
					$("#groupForm").attr("action","<%=basePath%>fd/edit.do").submit();
				}else{
					alertMessage("请选择某个资源进行操作");
				}
				return false;
			});
		
			//copy
			$("#copyBtn").click(function(){
				if(count >= 1){
					$("#groupForm").attr("action","<%=basePath%>fd/copy.do").submit();
				}else{
					alertMessage("请至少选择一项进行操作");
				}
				return false;
			});
			
			//copyToSystem
			$("#copyToSystemBtn").click(function(){
				if(count >= 1){
					var _selects = new Array();
					$("input[name=_selects]").each(function(){
						var value=$(this).val();
						_selects.push(value);
					});
					top.dialog({
						id : 'copyToSystem-dialog' + Math.ceil(Math.random()*10000),
						title : '复制到指定系统',
						url : '<%=basePath%>fd/listSystem.do',
						data : {pageId:_selects.join(",")},
						width : 400,
						height : 360
					}).showModal();
				}else{
					alertMessage("请至少选择一项进行操作");
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
	        		$("#groupForm").attr("action","<%=basePath%>fd/delete.do").submit();
	    		}
				return false;
	    	});
	    	
	    	//publish
	    	$("#publishBtn").click(function(){
	    		if(count<1){
	    			alertMessage("请至少选择一项进行操作");
	    			return false;
	    		}
	    		if(window.confirm("确定发布？")){
	        		$("#groupForm").attr("action","<%=basePath%>fd/publish.do").submit();
	    		}
				return false;
	    	});
	    	
	    	$("#export").click(function(){
	    		if(count!=1){
	    			alertMessage("请选择一项进行操作");
	    			return false;
	    		}
	    		var id = $("input[name=_selects]").first().val();
				top.dialog({
					id: 'add-dialog' + Math.ceil(Math.random()*10000),
					title: '页面预览',
					url:'<%=basePath%>document/view.do?dynamicPageId='+id,
					width:dialogWidth,
					height:height,
					okValue:'确定',
					ok:function(){},
					cancelValue:'取消',
					cancel:function(){}
				}).showModal();
				return false;
			});

	    	//导出excel
			$("#exportExcel").click(function(){
				if(count != 1){
					alertMessage("请选择一项进行操作");
					return false;
				}
				var id = $("input[name=_selects]").val();
				var url = basePath + "layout/exportExcel.do?dynamicPageId=" + id;
				console.log(url);
				$("#myForm").attr("action",url).submit();
				return false;
			});	
		
			$("#catTemplate").click(function(){
	    		if(count!=1){
	    			alertMessage("请选择一项进行操作");
	    			return false;
	    		}
	    		var id = $("input[name=_selects]").first().val();
				top.dialog({ 
					id: 'add-dialog' + Math.ceil(Math.random()*10000),
					title: '页面发布后的模版内容',
					url:'<%=basePath%>fd/catTemplate.do?_select='+id,
					width:dialogWidth,
					height:height,
					okValue:'确定',
					ok:function(){},
					cancelValue:'取消',
					cancel:function(){}
				}).showModal();
				return false;
			});
		
			$("#relation").click(function(){
	    		if(count != 1){
	    			alertMessage("请选择一项进行操作");
	    			return false;
	    		}
	    		var id = $("input[name=_selects]").first().val();
				top.dialog({ 
					id: 'cat-dialog' + Math.ceil(Math.random()*10000),
					title: '页面引用关系',
					url:'<%=basePath%>fd/relation.do?_select='+id,
					width:dialogWidth,
					height:height,
					okValue:'确定',
					ok:function(){},
					cancelValue:'取消',
					cancel:function(){}
				}).showModal();
				return false;
			});
			
			$("#checkOutBtn").click(function(){
				if(count!=1){
					alert("请选择一项进行操作");
					return false;
				}
				var isChecked = $.trim($("tr.active").children("td").last().text());
				if(isChecked=="签出"){
					alert("当为签出时,不能签出。");
					return false;
				}
	    		var id = $("input[name='_selects']").val();
	    		$.ajax({
				   	type: "POST",
				   	url: basePath + "fd/checkOutPage.do",
				   	data:{id:id},
				   	async : false,
				   	dataType:"json",
				   	success: function(data){
				   		if(data.msg=="签出成功"){
				   			$("tr.active").children("td").last().text("签出");
				   			$("tr.active").children("td").last().prev().text(data.userName);
				   		}
						alert(data.msg);
				   	}
				});
	    	});
	    	
			$("#checkInBtn").click(function(){
				if(count!=1){
					alert("请选择一项进行操作");
					return false;
				}
				var isChecked = $.trim($("tr.active").children("td").last().text());
				if(isChecked!="签出"){
					alert("当为签出时,才能签入。");
					return false;
				}
	    		var id = $("input[name='_selects']").val();
	    		$.ajax({
				   	type: "POST",
				   	url: basePath + "fd/checkInPage.do",
				   	data:{id:id},
				   	async : false,
				   	dataType:"html",
				   	success: function(data){
				   		if(data=="签入成功"){
				   			$("tr.active").children("td").last().text("签入");
				   		}
						alert(data);
				   	}
				});
	    	});
    	});
	</script>
</body>
</html>


