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
<!DOCTYPE html >
<html>
<head>
		<title>元数据管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
		<%@ include file="/resources/include/common_css.jsp" %>
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <%-- <li><a href="<%=basePath%>devAdmin/intoSystem.do?sysId=1">首页</a></li>
		          <li><a href="<%=basePath %>metaModel/selectPage.do">元数据管理</a></li> --%>
		          <li>模型:[${vo.modelName }] (编码:${vo.modelCode }) 属性管理</li>
		      </ul>
		</div>
		<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
<!-- 				<button type="button" class="btn btn-info" id="releaseBtn"><i class="icon-trash">发布</i></button> -->
<!-- 				<button type="button" class="btn btn-info" id="createTableBtn"><i class="icon-trash">生成数据库表</i></button> -->
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
		</div>
	
		<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath %>metaModelItems/selectPage.do" id="createForm" class="clearfix">
						<input type="hidden" name="currentPage" value="1" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">字段名称(中)</span>
								<input type="hidden" name="modelId" value="${ids }">
								<input name="itemName" class="form-control" id="itemName" type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon">字段名称(英)</span>
								<input name="itemCode" class="form-control" id="itemCode" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">字段类型</span>
								<select name="itemType" class="form-control">
									<option value="">--请选择字段类型--</option>
									<option value="int">int</option>
									<option value="bigInt">bigInt</option>
									<option value="varchar">varchar</option>
									<option value="bool">bool</option>
									<option value="boolean">boolean</option>
									<option value="float">float</option>
									<option value="double">double</option>
									<option value="date">date</option>
									<option value="1">一对一</option>
									<option value="2">多对一</option>
								</select>
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
				<form  method="post" id="manuList" action="<%=basePath %>metaModelItems/selectPage.do">	
				<input type="hidden" id="modelId" name="modelId" value="${ids }">
            	<input type="hidden" name="currentPage" value="${currentPage}">		
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
						    <th>名称(中)</th>
				            <th>名称(英)</th>
				            <th data-width="70">类型</th>
				            <th data-width="50">长度</th>
				            <th data-width="50">主键</th>
				            <th data-width="50">索引</th>
				            <th data-width="70">允许为空</th>
				            <th data-width="70">默认值</th>
				            <th>备注</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${list}" var="k">
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value="${k.id}"> </td><!-- 用于提交表单数据 tip:id必须为boxs且不带name属性，避免重复提交 -->
									<td>${k.itemName }</td>
									<td>${k.itemCode }</td>
									<td>${k.itemType }</td>
									<td>${k.itemLength }</td>
									<td>
										<c:if test="${k.usePrimaryKey==1 }">
											是
										</c:if>
										<c:if test="${k.usePrimaryKey==0 or empty k.usePrimaryKey}">
											否
										</c:if>
									</td>
									<td>
										<c:if test="${k.useIndex==1 }">
											有
										</c:if>
										<c:if test="${k.useIndex==0 or empty k.useIndex}">
											无
										</c:if>
									</td>
									<td>
										<c:if test="${k.useNull==1 }">
											是
										</c:if>
										<c:if test="${k.useNull==0 or empty k.useNull}">
											否
										</c:if>
									</td>
									<td>${k.defaultValue }</td>
									<td>${k.remark }</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</form>
			</div>
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="list"></sc:PageNavigation> 
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
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","id");
					  }
				  }
					
			  });
          	//add
      		$("#addBtn").click(function(){
      			$("#manuList").attr("action","<%=basePath%>metaModelItems/add_toggle.do").submit();
      			return false;
      		})
      		

      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#manuList").attr("action","<%=basePath%>metaModelItems/update_toggle.do").submit();
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
	          		$("#manuList").attr("action","<%=basePath%>metaModelItems/remove.do").submit();
          		}
      			return false;
          	});
          	
//           	//release
//           	$("#releaseBtn").click(function(){
<%--           		$("#manuList").attr("action","<%=basePath%>metaModelItems/release.do").submit(); --%>
//           		return false;
//           	});
          	
//           	//createTable
//           	$("#createTableBtn").click(function(){
<%--           		$("#manuList").attr("action","<%=basePath%>metaModelItems/createTable.do").submit(); --%>
//           		return false;
//           	});
          	
          });
		</script>
</body>
</html>