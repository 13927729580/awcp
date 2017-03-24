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
		          <li class="active">页面动作管理</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
					<div class="btn-group">
							              <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
							              		<i class="icon-plus-sign"></i>新增  <span class="caret"></span> 
							              </button>
							              <ul class="dropdown-menu" >
							                <li><a href="javascript:void(0)" onclick="addAct('2001');">保存</a></li>
							               <li><a href="javascript:void(0)" onclick="addAct('2002');">返回</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2003');">删除</a></li> 
							                <li><a href="javascript:void(0)" onclick="addAct('2004');">启动流程</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2005');">流程调整</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2006');">流程回撤</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2007');">编辑审批人</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2008');">保存（带流程）</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2009');">新增</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2010');">更新</a></li>
							                
							                <li><a href="javascript:void(0)" onclick="addAct('2011');">流程流转</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2012');">打开</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2013');">审批</a></li>
							                <li><a href="javascript:void(0)" onclick="addAct('2014');">Pdf打印</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2015');">保存不带校验</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2018');">流程转发</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2019');">流程传阅</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2020');">流程图</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2021');">流程归档</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2022');">流程办结</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2023');">流程退回</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2024');">加签</a></li>
											<li><a href="javascript:void(0)" onclick="addAct('2025');">移交</a></li>
						              </ul>
						            </div>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="in">
					<form action="" id="createForm" class="clearfix" method="post">
						<input type="hidden" name="currentPage" value="0" />
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
							<button class="btn btn-primary" type="submit">提交</button>
						</div>
					</form>
				</div>
			</div>
			
			<form  method="post" id="manuList">	
	        <input type="hidden" name="currentPage" value="${currentPage}">	
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
							<td><a href="<%=basePath%>/fd/act/edit.do?_selects=${vo.pageId}" >${vo.name}</a></td>
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
	function addAct(type){
		var url = "<%=basePath%>fd/act/edit.do?type="+type;
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
				$("#manuList").attr("action","<%=basePath%>fd/act/edit.do").submit();
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
        		$("#manuList").attr("action","<%=basePath%>fd/act/delete.do").submit();
    		}
			return false;
    	});
    	
    });
	</script>


</body>
</html>


