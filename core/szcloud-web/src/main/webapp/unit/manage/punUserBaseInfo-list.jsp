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
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>菜单查询页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_css.jsp" %><!-- 注意加载路径 -->
	</head>
	<body id="main">
		<div class="container-fluid">
			<!--list页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="buttons">按钮区</div>
			<div class="row" id="searchform">搜索区</div>
			<div class="row" id="datatable">数据展示区</div>
			<div class="row" id="pagers">分页</div>
			-->
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="<%=basePath%>manage/intoPunManageAppGroupList.do">应用组织管理</a></li>
		          <li class="active">用户管理</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
<!-- 				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button> -->
<!-- 				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button> -->
				<button type="button" class="btn btn-warning" id="resetPwd"><i class="icon-edit"></i>重置密码</button>
				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button>
			</div>
			
			<div class="row" id="searchform">
				<div id="collapseButton" class="collapse">
					<form action="<%=basePath%>manage/punUserBaseInfoList.do"
						id="createForm">
						<input type="hidden" name="currentPage" value="0" />
						<input type="hidden" name="boxs" value="${groupId}"/>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">用户名</span>
								<input name="userName" class="form-control" id="userName" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">姓名</span>
								<input name="name" class="form-control" id="name" type="text"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">身份证号</span>
								<input name="userIdCardNumber" class="form-control" id="userIdCardNumber" type="text"/>
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
			<form  method="post" id="userList">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"><input type="hidden" name="currentPage" value="${currentPage}">	</th>
							<th>用户名</th>
							<th>姓名</th>
							<th>身份证号</th>
						</tr>
					</thead>
					<tbody>
	            		
					<c:forEach items="${vos}" var="vo">
			          <tr> 	
			            <td class="hidden formData">
			            	<input id="boxs" type="name" value="${vo.userId}">
			            </td>
			            <td>${vo.userName}</td>
			            <td>${vo.name}</td>
			            <td>${vo.userIdCardNumber}</td>
			          </tr>
			          </c:forEach>
					</tbody>
				</table>
				</form>
			</div>
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
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
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","userIds");
					  }
				  }
					
			  });
			  
			  //重置密码
			  $('#resetPwd').bind("click", function(){
				  	if(count!=1){
				  		alert("请选择一项进行操作！");
				  		return false;
				  	}
				  	var userIds = $("input[name='userIds']");
				  	var userId = userIds[0].value;
					var content='<div class="form-horizontal">'+
					'<div class="form-group">'+
				    '<label class="col-md-3 control-label required">新密码</label>'+
				    '<div class="col-md-9"><input id="newPwd" name="newPwd" type="password" class="form-control"  placeholder="新密码"></div>'+
				    '</div><div class="form-group">'+
				    '<label class="col-md-3 control-label required">重复密码</label>'+
				    '<div class="col-md-9"><input id="newPwd2" name="newPwd2" type="password" class="form-control"  placeholder="重复新密码"></div>'+
				    '</div></div>';	
					var d = dialog({
					    title: '重置密码',
					    content: content,
					    okValue: '确定',
					    ok: function () {
					    	var newPwd = $("#newPwd").val();
					    	var newPwd2 = $("#newPwd2").val();
					    	if(newPwd == ""){
					    		alert("新密码必填");
					    		return false;
					    	}
					    	if(newPwd2 == ""){
					    		alert("重复密码必填");
					    		return false;
					    	}
					    	if(newPwd!=newPwd2){
					    		alert("新密码和重复密码输入不一致");
					    		return false;
					    	}
					    	if(!window.confirm("确定修改")){
					    		return false;
					    	}
								//修改密码
								$.ajax({
								   type: "POST",
								   url: "<%=basePath%>manage/resetPwd.do",
								   data: "userId="+userId+"&userPwd="+newPwd,
								   success: function(data){
									    if(data.status==0){
									    	alert(data.message);
									    }else{
									    	alert(data.message);
									    }
								   }
								});			
								
						        return true;
					        
					    },
					    cancelValue: '取消',
					    cancel: function () {}
					});
					d.width(450).showModal();
					return false;
				});
		
          	//新增
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>unit/intoPunUserBaseInfo.do";
      			location.href = url;
      			return false;
      		})
      		
      		//update
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoGet.do").submit();
      			}else{
      				alert("请选择一个用户进行操作");
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
          			$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoDelete.do").submit();
          		}
      			return false;
          	})
          	
          });
		</script>
	</body>
</html>
