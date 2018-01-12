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
			<ul class="nav nav-tabs">
			     <li>
			     	<a href="<%=basePath%>unit/selfGroupGet.do">组织信息编辑</a>
			     </li>
			     <li>
			       <a href="<%=basePath%>unit/punGroupEdit.do">组织树编辑</a>
			     </li>
			     <li class="active">
					<a href="javascript:;">职务</a>
			     </li>
			</ul>
			<div style="margin-top:20px;"></div>
			<div class="opeBtnGrop">
				<button type="button" class="btn btn-success" id="allchecker"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-warning" id="alleidt"><i class="icon-edit"></i>编辑</button>
				<button type="button" class="btn btn-warning" id="deleteBtn"><i class="icon-edit"></i>删除</button>
			</div>
			
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<form  method="get" id="userList">	
							<input type="hidden" id="currentPage" name="currentPage" value="${vos.getPaginator().getPage()}">
							<input type="hidden" id="pageSize" name="pageSize" value="${vos.getPaginator().getLimit()}">
							<input type="hidden" id="totalCount" name="totalCount" value="${vos.getPaginator().getTotalCount()}">
							<table class="table table-hover">
								<thead>
									<tr>
										<th class="hidden"></th>
										<th data-width="" data-field="" data-checkbox="true"></th>
										<th>职务名称</th>
										<th>等级</th>
									</tr>
								</thead>
								<tbody>
								<c:forEach items="${vos}" var="vo">
						          <tr> 	
						            <td class="hidden formData">
						            	<input data-name="${vo.name}" data-grade="${vo.grade}" type="hidden" value="${vo.positionId}">
						            </td>
						            <td></td>
					            	<td>${vo.name}</td>
					            	<td>${vo.grade}</td>
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
			  var count=0;
			  $(".table").bootstrapTable({
					 pageSize:parseInt($("#pageSize").val()),
		        	 pageNumber:parseInt($("#currentPage").val()),
		        	 totalRows:parseInt($("#totalCount").val()),
		        	 sidePagination:"server",
		        	 pagination:true,
		        	 onPageChange:function(number, size){
		        		$("#pageSize").val(size);
		        		$("#currentPage").val(number);
		        		$("#userList").attr("action","<%=basePath%>punPositionController/pageList.do").submit();
		        	 },
		        	 onClickRow:function(row,$element,field){
		        	  	  var $checkbox=$element.find(":checkbox").eq(0);
		        	  	  if($checkbox.get(0).checked){
							  $checkbox.get(0).checked=false;
							  $element.find("input[type='hidden']").removeAttr("name","positionId");
		        	  	  }else{
							  $checkbox.get(0).checked=true;
							  $element.find("input[type='hidden']").attr("name","positionId");
		        	  	  }
						  count = $("input[name='positionId']").length;
		        	 },
		        	 onCheck: function(row,$element){
						  $element.closest("tr").find("input[type='hidden']").attr("name","positionId");
						  count = $("input[name='positionId']").length;
		        	 },
		        	 onUncheck:function(row,$element){
		        		 $element.closest("tr").find("input[type='hidden']").removeAttr("name");
						 count = $("input[name='positionId']").length;
		        	 }
		         });
			  
				var dialogContent = function(){
					var name,grade;
					if(arguments.length==0){
					    name="";grade="";
					}else if(arguments.length==2){
					    name=arguments[0];grade=arguments[1];
					}
					var content='<div class="input-group">'+
					'<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;名称</span>'+
			        '<input name="groupName" type="text" class="form-control" value="'+name+'" placeholder="名称">'+
					'<span class="input-group-addon"><i class="icon-pencil"></i>&nbsp;&nbsp;等级</span>'+
			        '<input name="grade" id="gradeValue" type="number" class="form-control" value="'+grade+'" placeholder="等级"></div>';
					return content;
				};
				
				$('#allchecker').bind("click", function(){
					var d = dialog({
					    title: '添加职务信息',
					    content: dialogContent(),
					    okValue: '确定',
					    ok: function () {
					    	var $groupName = $("input[name='groupName']");
					    	var $grade = $("input[name='grade']");
					    	if(!$groupName.val()||!$grade){
					    		Comm.alert("名称或等级不能为空");
					    		return false;
					    	}else{
								//添加菜单
								$.ajax({
								   type: "POST",
								   url: "<%=basePath%>punPositionController/save.do",
								   data: "&name="+$groupName.val()+"&grade="+$grade.val(),
								   success: function(data){
									    if(data.status==0){
									    	Comm.alert("添加成功");
									     	window.location.href="<%=basePath%>punPositionController/pageList.do";
									    }else{
									    	Comm.alert(data.message);
									    }
								   }
								})			
								
						        return true;
					    	}
					        
					    },
					    cancelValue: '取消',
					    cancel: function () {}
					});
					d.width(320).show();
					return false;
				});
				
				$('#alleidt').bind("click", function(){
					var rows=$("input[name='positionId']");
					if(count!=1){
						Comm.alert("请选择一个岗位编辑");
						return false;
					}
					var positionId =rows.val();
					var positionName = rows.attr("data-name");
					var grade=rows.attr("data-grade");
					var d = dialog({
					    title: '编辑职务名称',
					    content: dialogContent(positionName,grade),
					    okValue: '确定',
					    ok: function () {
					    	var $groupName = $("input[name='groupName']");
					    	var $grade = $("#gradeValue");
					    	if($groupName.val()==""){
					    		return false;
					    	}else{
								//添加菜单
								$.ajax({
								   type: "POST",
								   url: "<%=basePath%>punPositionController/update.do",
								   data: "name="+$groupName.val()+"&grade="+$grade.val()+"&positionId="+positionId,
								   success: function(data){
									    if(data.status==0){
									    	Comm.alert("编辑成功");
									     	window.location.href="<%=basePath%>punPositionController/pageList.do";
									    }else{
									    	Comm.alert(data.message);
									    }
								   }
								})			
								
						        return true;
					    	}
					        
					    },
					    cancelValue: '取消',
					    cancel: function () {}
					});
					d.width(320).show();
					return false;
				});
				
				$("#deleteBtn").click(function(){
					if(count<1){
						Comm.alert("请至少选择一项操作");
						return false;
					}
					if(window.confirm("确定删除？")){
						$("#userList").attr("action","<%=basePath%>punPositionController/remove.do").submit();
					}
					return false;
				})
				
          	
          });
		</script>
	</body>
</html>
