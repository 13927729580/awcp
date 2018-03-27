<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <%@ include file="/resources/include/common_lte_css.jsp"%>
</head>
<body id="main">
	<section class="content">
    	<div class="opeBtnGrop">
	    	<a href="#" class="btn btn-primary"  id="addBtn">新增</a>
			<a href="#" class="btn btn-danger" id="deleteBtn">删除</a>
			<a href="#" class="btn btn-info" id="searchBtn">搜索</a>	
    	</div>	
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<form method="post" id="funcList">
							<input type="hidden" id="currentPage" name="currentPage" value="${vos.getPaginator().getPage()}">
							<input type="hidden" id="pageSize" name="pageSize" value="${vos.getPaginator().getLimit()}">
							<input type="hidden" id="totalCount" name="totalCount" value="${vos.getPaginator().getTotalCount()}">	
							<div class="row form-group">
								<div class="col-md-3">
									<div class="input-group">
							        	<span class="input-group-addon">函数名</span>
							            <input name="name" type="text" id="name" class="form-control"  value="${name}">
						            </div>
								</div>									
							</div>							
							<table class="table table-hover">  
							    <thead>
							        <tr>
							        	<th class="hidden"></th>
							        	<th data-width="" data-field="" data-checkbox="true"></th>
							            <th data-width="30%">名称</th>
										<th>描述</th>
							        </tr>
							    </thead>
							    <tbody>
									<c:forEach items="${vos}" var="vo" varStatus="status">
							          <tr> 	
							            <td class="hidden formData"><input type="hidden" value="${vo.id}"></td>
							            <td></td>
							            <td><a href="<%=basePath%>/func/edit.do?_selects=${vo.id}" >${vo.name}</a></td>
										<td>${vo.description }</td>
							          </tr>
							       </c:forEach>
								</tbody>
							</table>  
						</form>
					</div>				
				</div>				
			</div>
		</div>
    </section>
	<%@ include file="/resources/include/common_lte_js.jsp"%>
	<script>
		$(document).ready(function(){
			var count = 0;//默认选择行数为0
			$(".table").bootstrapTable({
				pageSize:parseInt($("#pageSize").val()),
				pageNumber:parseInt($("#currentPage").val()),
				totalRows:parseInt($("#totalCount").val()),
				sidePagination:"server",
				pagination:true,
				onPageChange:function(number, size){
					$("#pageSize").val(size);
					$("#currentPage").val(number);
					$("#funcList").submit();
				},
				onClickRow:function(row,$element,field){
					var $checkbox=$element.find(":checkbox").eq(0);
					if($checkbox.get(0).checked){
						$checkbox.get(0).checked=false;
						$element.find("input[type='hidden']").removeAttr("name","_selects");
					}else{
						$checkbox.get(0).checked=true;
						$element.find("input[type='hidden']").attr("name","_selects");
					}
					count = $("input[name='_selects']").length;
				},
				onClickCell:function(field,value,row,$element){

				},
				onCheck: function(row,$element){
					$element.closest("tr").find("input[type='hidden']").attr("name","_selects");
					count = $("input[name='_selects']").length;
				},
				onUncheck:function(row,$element){
					$element.closest("tr").find("input[type='hidden']").removeAttr("name");
					count = $("input[name='_selects']").length;
				},
				onCheckAll: function (rows) {
					$.each(rows,function(i,e){
						$("input[value='"+$($.trim(e["0"])).attr("value")+"']").attr("name","_selects");
					});
					count = $("input[name='_selects']").length;
				},
				onUncheckAll: function (rows) {
					$.each(rows,function(i,e){
						$("input[value='"+$($.trim(e["0"])).attr("value")+"']").removeAttr("name");
					});
					count = $("input[name='_selects']").length;
				}
			});
		  	
	    	//新增
			$("#addBtn").click(function(){
				location.href = basePath + "func/edit.do";
				return false;
			});
		
	    	//删除
	    	$("#deleteBtn").click(function(){
	    		if(count<1){
	    			Comm.alert("请至少选择一项进行操作");
	    			return false;
	    		}
	    		Comm.confirm("确定删除？",function(){
	    			$("#funcList").attr("action",basePath + "func/delete.do").submit();
	    		});
				return false;
	    	});	
	    	
	    	//搜索
	    	$("#searchBtn").click(function(){
	    		$("#funcList").attr("action",basePath + "func/list.do").submit();
	    		return false;
	    	});
    	});
	</script>
</body>
</html>


