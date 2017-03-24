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
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
				<button type="button" class="btn btn-success" id="copyBtn"><i class="icon-plus-sign"></i>复制</button>
				<button type="button" class="btn btn-success" id="copyToSystemBtn"><i class="icon-plus-sign"></i>复制到</button>
				<button type="button" class="btn btn-warning" id="publishBtn"><i class="icon-edit"></i>发布</button>
				<button type="button" class="btn btn-info " id="export"><i class="icon-zoom-out"></i>预览</button>
				<button type="button" class="btn btn-info " id="relation"><i class="icon-zoom-out"></i>引用关系</button>
				<button type="button" class="btn  btn-info" id="exportExcel"><i class="icon-plus-sign"></i>导出Excel</button>
				<button type="button" class="btn  btn-info" id="catTemplate"><i class="icon-plus-sign"></i>查看发布后模版</button>
			</div>
			
			<div class="row">
				<div id="collapseButton" class="in">
					<form action="<%=basePath%>fd/list.do" id="groupForm" class="clearfix" method="post">
						<div class="col-md-2">
							<div class="input-group">
								<span class="input-group-addon">页面名称/ID</span>
								<input name="name" class="form-control" id="name" type="text"/>
								<input type="hidden" name="currentPage" value="1">	
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
						<div class="col-md-2">
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
						<div class="col-md-4 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
							<button style="margin: 0 5px;" class="btn btn-success" type="reset">清空</button>
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
									</tr>
								</thead>
								<tbody>	
								<c:forEach items="${vos}" var="vo">
									<tr>
										<td class="hidden formData">
											<input id="boxs" type="hidden" value="${vo.id}"></td>
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
	var tips = "${tips}";
	if(tips != ""){
		alert(tips);
	}
	$(":text[name='name']").val('${name }');
	$("select[name='pageType']").val('${pageType}');
	$("select[name='templateId']").val('${templateId}');
	$("select[name='menuId']").val('${menuId}');
	
	/**
 		* 生成Excel，并下载到本地
 		*/
 		
function downloadFile(id) {
	var id = $("input[name=_selects]").first().val();
	var url="layout/exportExcel.do?dynamicPageId="+id;
	if (typeof (downloadFile.iframe) == "undefined") 
	{ 
			var iframe = document.createElement("iframe"); 
			downloadFile.iframe = iframe; 
			document.body.appendChild(downloadFile.iframe); 
	} 
		downloadFile.iframe.src = url; 
		downloadFile.iframe.style.display = "none"; 
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
					height : 440
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
			top.dialog(
					{ id: 'add-dialog' + Math.ceil(Math.random()*10000),
						title: '页面预览',
						url:'<%=basePath%>document/view.do?dynamicPageId='+id,
						width:1024,
						height:768,
						okValue:'确定',
						ok:function(){},
						cancelValue:'取消',
						cancel:function(){}
					}
			).showModal();
			return false;
		});
		
		

		$("#exportExcel").click(downloadFile);
		
		$("#catTemplate").click(function(){
    		if(count!=1){
    			alertMessage("请选择一项进行操作");
    			return false;
    		}
    		var id = $("input[name=_selects]").first().val();
			top.dialog(
					{ id: 'add-dialog' + Math.ceil(Math.random()*10000),
						title: '页面发布后的模版内容',
						url:'<%=basePath%>fd/catTemplate.do?_select='+id,
						width:800,
						height:600,
						okValue:'确定',
						ok:function(){},
						cancelValue:'取消',
						cancel:function(){}
					}
			).showModal();
			return false;
		});
		
		$("#relation").click(function(){
    		if(count != 1){
    			alertMessage("请选择一项进行操作");
    			return false;
    		}
    		var id = $("input[name=_selects]").first().val();
			top.dialog(
					{ id: 'cat-dialog' + Math.ceil(Math.random()*10000),
						title: '页面引用关系',
						url:'<%=basePath%>fd/relation.do?_select='+id,
						width:800,
						height:500,
						okValue:'确定',
						ok:function(){},
						cancelValue:'取消',
						cancel:function(){}
					}
			).showModal();
			return false;
		});
	/*$("#relation").click(function(){
    		if(count!=1){
    			alertMessage("请选择一项进行操作");
    			return false;
    		}
    		var id = $("input[name=_selects]").first().val();
			top.dialog(
					{ id: 'add-dialog' + Math.ceil(Math.random()*10000),
						title: '页面预览',
						url:'<%=basePath%>fd/relation.do?_select='+id,
						width:1024,
						height:768,
						okValue:'确定',
						ok:function(){},
						cancelValue:'取消',
						cancel:function(){}
					}
			).showModal();
			return false;
		});
    	*/
    });
	</script>
</body>
</html>


