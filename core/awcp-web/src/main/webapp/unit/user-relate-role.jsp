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
		<meta charset="utf-8">
	    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	    <%@ include file="/resources/include/common_lte_css.jsp"%>
	</head>
	<body>
		<section class="content">
			<div class="row">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="<%=basePath %>unit/manageRole.do">角色管理</a></li>
		          <li class="active">角色关联的用户管理</li>
		        </ul>
			</div>
			<div class="opeBtnGrop">
				<button type="button" class="btn btn-info" id="addBtn"><i class="icon-edit"></i>新增关联用户</button>
				<button type="button" class="btn btn-info" id="cancelBtn"><i class="icon-trash"></i>取消关联</button>
			</div>
			
			<div class="row">
				<div class="col-md-12">	
					<div class="box box-info">
						<div class="box-body">
							<form  method="post" id="userList">
								<input type="hidden" id="currentPage" name="currentPage" value="${vos.getPaginator().getPage()}">
								<input type="hidden" id="pageSize" name="pageSize" value="${vos.getPaginator().getLimit()}">
								<input type="hidden" id="totalCount" name="totalCount" value="${vos.getPaginator().getTotalCount()}">
								<input name="roleId" type="hidden" value="${roleId}"/>
								<div class="row form-group">	
										<div class="col-md-4">
											<div class="input-group">
												<span class="input-group-addon">身份证号</span>
												<input name="userIdCardNumber" class="form-control" id="userIdCardNumber" type="text" value="${vo.userIdCardNumber}"/>
											</div>
										</div>
										<div class="col-md-4">
											<div class="input-group">
												<span class="input-group-addon">姓名</span>
												<input name="name" class="form-control" id="name" type="text" value="${vo.name}"/>
											</div>
										</div>
										<div class="col-md-4 btn-group">
											<button type="submit" class="btn btn-primary">提交</button>
											<button type="reset" class="btn">取消</button>       
										</div>	
								</div>	
								<table class="table table-hover">
									<thead>
										<tr>
											<th class="hidden"></th>
								        	<th data-width="" data-field="" data-checkbox="true"></th>
								        	<th>序号</th>
											<th>身份证号</th>
											<th>姓名</th>
											<th>用户名</th>
											<th>移动电话</th>
											<th>办公电话</th>
											<th>头衔</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${vos}" var="vo" varStatus="status">
								          <tr> 	
									            <td class="hidden formData">
									            	<input type="hidden" value="${vo.userId}">
									            </td>
									            <td></td>
									            <td>${currentPage*vos.getPaginator().getLimit()-vos.getPaginator().getLimit()+status.index +1}</td>
									            <td>${vo.userIdCardNumber}</td>
									            <td>${vo.name}</td>
									            <td>${vo.userName}</td>
									            <td>${vo.mobile}</td>
									            <td>${vo.userOfficePhone}</td>
									            <td>${vo.userTitle}</td>
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
		        		$("#userList").attr("action","<%=basePath%>unit/roleRelateUserQuery.do").submit();
		        	 },
		        	 onClickRow:function(row,$element,field){
			       	  	  var $checkbox=$element.find(":checkbox").eq(0);
			       	  	  if($checkbox.get(0).checked){
								  $checkbox.get(0).checked=false;
								  $element.find("input[type='hidden']").removeAttr("name","boxs");
			       	  	  }else{
								  $checkbox.get(0).checked=true;
								  $element.find("input[type='hidden']").attr("name","boxs");
			       	  	  }
							  count = $("input[name='boxs']").length;
		       	 	 },
		        	 onCheck: function(row,$element){
						  $element.closest("tr").find("input[type='hidden']").attr("name","boxs");
						  count = $("input[name='boxs']").length;
		        	 },
		        	 onUncheck:function(row,$element){
		        		 $element.closest("tr").find("input[type='hidden']").removeAttr("name");
						 count = $("input[name='boxs']").length;
		        	 }, 
		        	 onCheckAll: function (rows) {
		        		 $.each(rows,function(i,e){
		        			$("input[value='"+$($.trim(e["0"])).attr("value")+"']").attr("name","boxs");
		        		 })
						 count = $("input[name='boxs']").length;
		             },
		             onUncheckAll: function (rows) {
		            	 $.each(rows,function(i,e){
		        			$("input[value='"+$($.trim(e["0"])).attr("value")+"']").removeAttr("name");
		        		 })
						 count = $("input[name='boxs']").length;
		             }
		         });
      		
      		//新增关联用户
			$("#addBtn").click(function() {
					top.dialog({
						id : 'add-dialog' + Math.ceil(Math.random() * 10000),
						title : '新增关联用户',
						url : "<%=basePath%>unit/roleNotRelateUserQuery.do?roleId=${roleId}&sysId=${sysId}",
						height : 500,
						skin : "col-md-8",
						onclose : function() {
							window.location = "<%=basePath%>unit/roleRelateUserQuery.do?roleId=${roleId}";
						}
					}).showModal();
					return false;
				});

				//取消关联
				$("#cancelBtn").click(function() {
				if (count > 0) {
					$("#userList").attr("action","<%=basePath%>unit/cancelUserRoleRelation.do").submit();
      			}else{
      				Comm.alert("请至少选择一个用户进行操作");
      			}
      			return false;
      		});
          	
          });
		</script>
	</body>
</html>
