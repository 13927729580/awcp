<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <%@ include file="/resources/include/common_lte_css.jsp"%>
</head>
<body>
	<section class="content">
		<div class="opeBtnGrop">
			<a href="#" class="btn btn-success"  id="relateUserBtn">关联用户</a>
			<a href="#" onclick="document.getElementById('userList').submit()" class="btn btn-primary">搜索</a>
			<a href="#" onclick="document.getElementById('userList').reset()" class="btn btn-info">清空</a>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<form method="post" id="userList">
							<input type="hidden" id="currentPage" name="currentPage" value="${vos.getPaginator().getPage()}">
							<input type="hidden" id="pageSize" name="pageSize" value="${vos.getPaginator().getLimit()}">
							<input type="hidden" id="totalCount" name="totalCount" value="${vos.getPaginator().getTotalCount()}">
							<div class="row form-group">
								<div class="col-md-6">	
									<div class="input-group">
						                <span class="input-group-addon">角色名</span>
						                <input name="roleName" class="form-control" id="roleName" type="text" value="${vo.roleName}" />
						            </div>
								</div>	
							</div>	
							<table class="table table-hover">
								<thead>
									<tr>
										<th class="hidden"></th>
										<th data-width="" data-field="" data-checkbox="true"></th>
							            <th>序号</th>
										<th>角色名</th>
									</tr>
								</thead>
								<tbody>
										<c:forEach items="${vos}" var="vo" varStatus="status">
											<tr>
												<td class="hidden"><input type="hidden" value="${vo.roleId}"></td>
												<td></td>
									            <td>${vos.getPaginator().getPage()*vos.getPaginator().getLimit()-vos.getPaginator().getLimit()+status.index +1}</td>
					           					<td>${vo.roleName}</td>
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

	<%@ include file="../resources/include/common_lte_js.jsp"%>
	<script type="text/javascript">
		  $(function(){
			 
			  var count=0;//默认选择行数为0
			  $(".table").bootstrapTable({
					 pageSize:parseInt($("#pageSize").val()),
		        	 pageNumber:parseInt($("#currentPage").val()),
		        	 totalRows:parseInt($("#totalCount").val()),
		        	 sidePagination:"server",
		        	 pagination:true,
		        	 onPageChange:function(number, size){
		        		$("#pageSize").val(size);
		        		$("#currentPage").val(number);
		        		$("#userList").attr("action","<%=basePath%>unit/manageRole.do").submit();
		        	 },
		        	 onClickRow:function(row,$element,field){
		        	  	  var $checkbox=$element.find(":checkbox").eq(0);
		        	  	  if($checkbox.get(0).checked){
							  $checkbox.get(0).checked=false;
							  $element.find("input[type='hidden']").removeAttr("name","roleId");
		        	  	  }else{
							  $checkbox.get(0).checked=true;
							  $element.find("input[type='hidden']").attr("name","roleId");
		        	  	  }
						  count = $("input[name='roleId']").length;
		        	 },
		        	 onCheck: function(row,$element){
						  $element.closest("tr").find("input[type='hidden']").attr("name","roleId");
						  count = $("input[name='roleId']").length;
		        	 },
		        	 onUncheck:function(row,$element){
		        		 $element.closest("tr").find("input[type='hidden']").removeAttr("name");
						 count = $("input[name='roleId']").length;
		        	 }
		         });
			//relate user
	      		$("#relateUserBtn").click(function(){
	      			if(count == 1){
	      				$("#userList").attr("action","<%=basePath%>unit/roleRelateUserQuery.do").submit();
	      			}else{
	      				Comm.alert("请选择一项进行操作");
	      			}
	      			return false;
	      		});
			});
		  
	</script>
</body>
</html>
