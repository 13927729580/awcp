<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
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
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<form method="get" action="<%=basePath%>unit/searchByType.do" id="userList">
							<input type="hidden" id="groupId" value="${groupId}"/>
							<input type="hidden" id="rootGroupId" value="${rootGroupId}"/>
							<%-- <input type="hidden" id="currentPage" name="currentPage" value="${userList.getPaginator().getPage()}">
							<input type="hidden" id="pageSize" name="pageSize" value="${userList.getPaginator().getLimit()}">
							<input type="hidden" id="totalCount" name="totalCount" value="${userList.getPaginator().getTotalCount()}"> --%>
							<div class="row form-group">
								<div class="col-xs-4">
									<div class="input-group">
										<span class="input-group-addon">按类别搜索</span>
										<select name="searchType" id="searchType" class="form-control">
											<option value="" >请选择</option>
									  	 	<option value="0" selected>部门</option>
									  	 	<option value="1">人员</option>
									  </select>
									</div>
								</div>
								<div class="col-xs-4">
									<div class="input-group">
										<span class="input-group-addon">名称</span>
										<input class="form-control" id="searchName" name="searchName" value="${searchName}" type="text"/>
									</div>
								</div>
								<div class="col-xs-2 btn-group">
									<button class="btn btn-primary" type="submit">提交</button>
									<button class="btn" type="button" onClick="removeUser()">移动到</button>
								</div>
							</div>
							<table class="table table-bordered">
								<thead>
									<tr>
										<th class="hidden"></th>
										<th data-width="" data-field="" data-checkbox="true"></th>
										<th>姓名</th>
										<th>用户名</th>
										<th>部门</th>
										<th>岗位</th>
										<th>编号</th>
									</tr>
								</thead>
								<tbody>
										<c:forEach items="${userList}" var="vo">
											<tr>
												<td class="hidden">
													<input  type="hidden" value="${vo.userId}"> 
												</td>
												<td></td>
												<td>${vo.name}</td>
												<td>${vo.userIdCardNumber}</td>
												<td>${vo.deptName}</td>
												<td>
													<c:forEach items="${vo.positionList}" var="p" varStatus="sta">
													<c:choose>
														<c:when test="${0==sta.index}">
															${p.name}
														</c:when>
														<c:otherwise>
															;${p.name}
														</c:otherwise>
													</c:choose>
						            				</c:forEach></td>
												<td>
												<c:choose>
														<c:when test="${not empty vo.number}">
															${vo.number}<span style="float:right"><a href="javascript:void(0);" onClick="userEdit('${vo.userId}','${vo.number}')">编辑</a></span>
														</c:when>
														<c:otherwise>
															${vo.number}<span style="float:right"><a href="javascript:void(0);" onClick="userEdit('${vo.userId}','-1')">添加</a></span>
														</c:otherwise>
											    </c:choose>
												
												
												</td>
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
			  var groupId =$("#groupId").val();
			  var rootGroupId =$("#rootGroupId").val();
			  var count=0;//默认选择行数为0
			  var searchType = '${searchType}';
			  if(searchType!=""){
				  $("#searchType").val(searchType);  
			  }
			  $(".table").bootstrapTable({
					 <%-- pageSize:parseInt($("#pageSize").val()),
		        	 pageNumber:parseInt($("#currentPage").val()),
		        	 totalRows:parseInt($("#totalCount").val()),
		        	 sidePagination:"server",
		        	 pagination:true,
		        	 onPageChange:function(number, size){
		        		$("#pageSize").val(size);
		        		$("#currentPage").val(number);
		        		$("#userList").attr("action","<%=basePath%>punUserGroupController/getUsers.do").submit();
		        	 }, --%>
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
		});
		
		//用户编号编辑
		function userEdit(id,number){
			number=parseInt(number);
			if(!number||!id){
			  	return false;
			}
			var content="<div class='input-group'>"+
			"<span class='input-group-addon'><i class='icon-pencil'></i>&nbsp;&nbsp;编号</span>"+
        	"<input name='userNumber' type='text' class='form-control' value='"+(number!=-1?number:"")+"' placeholder='编号'></div>"
			var d = dialog({
						    title: '修改编号',
						    content:content,
						    okValue: '确定',
						    ok: function () {
						    	var $number = $("input[name='userNumber']");
						    	if($number.val()==""){
						    		$number.parent().addClass("has-error");
						    		return false;
						    	}else{
						    		var groupId=$("#groupId").val();
						    		location.href="<%=basePath%>punUserGroupController/getUsers.do?user="+id+"&groupId="+groupId+"&number="+$number.val();
						    	}
						    	
						    },
						    cancelValue: '取消',
						    cancel: function () {}
					}).width(320).show();
		}
		//人员编辑分组
		function removeUser(){
			var groupId=$("#groupId").val();
			var value =[]; 
			$('input[name="boxs"]').each(function(){
				value.push($(this).val()); 
			});
			if(value.length==0){
			  Comm.alert("请至少选择一项进行操作");
			  return false;
			} 
			top.dialog({
						title: "选择要移动的部门",
						url:"<%=basePath%>unit/deptList_check.jsp",
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						data:{userIds:value,oldGroupId:groupId},
						height:600,
					    onclose: function () {
					    	var value=this.returnValue;
					    	//刷新数据
					    	location.href="<%=basePath%>punUserGroupController/getUsers.do?groupId="+groupId;
					    	
					    }
					}).width(600).showModal();
		
		}
		
	</script>
</body>
</html>
