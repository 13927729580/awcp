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
 		<title>岗位信息</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../resources/include/common_css.jsp" %><!-- 注意加载路径 -->
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
			<ul class="nav nav-tabs">
			     <li>
			     	<a href="<%=basePath%>unit/selfGroupGet.do?">组织信息编辑</a>
			     </li>
			     <li>
			       <a href="<%=basePath%>unit/punGroupEdit.do?boxs=${vo.groupId}">组织树编辑</a>
			     </li>
			     <li class="active">
					<a href="javascript:;">职务</a>
			     </li>
			   </ul>
			<div style="margin-top:20px;">  
		   </div>
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="allchecker"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-warning" id="alleidt"><i class="icon-edit"></i>编辑</button>
				<button type="button" class="btn btn-warning" id="deleteBtn"><i class="icon-edit"></i>删除</button>
			</div>
			
			<div class="row" id="datatable">
			<form  method="post" id="userList">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden">
								<input type="hidden" id="groupId" name="groupId" value="${vo.groupId}">	
	            				<input type="hidden" name="currentPage" value="${currentPage}">	
							</th>
							<th>职务名称</th>
							<th>等级</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${punPositiontList}" var="vo">
			          <tr> 	
			            <td class="hidden formData">
			            	<input id="boxs" type="hidden" value="${vo.positionId}">
			            	<input id="positionName" type="hidden" value="${vo.name}">
			            	<input id="grade" type="hidden" value="${vo.grade}">
			            </td>
		            	<td>${vo.name}</td>
		            	<td>${vo.grade}</td>
			          </tr>
			          </c:forEach>
					</tbody>
				</table>
				</form>
			</div>
<!-- 			<div class="row navbar-fixed-bottom text-center" id="pagers"> -->
<%-- 				<sc:PageNavigation dpName="vos"></sc:PageNavigation>  --%>
<!-- 			</div> -->
		</div>
	 
	 <%@ include file="../resources/include/common_js.jsp" %>
<%-- 	 <script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script> --%>
	 <script type="text/javascript">
		  $(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
					  this.$table.find("tbody tr").find("input#positionName").removeAttr("name");
					  this.$table.find("tbody tr").find("input#grade").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","positionId");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#positionName").attr("name","positionName");
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#grade").attr("name","grade");
					  }
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
			        '<input name="grade" id="gradeValue" type="text" class="form-control" value="'+grade+'" placeholder="等级"></div>';
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
					    	if($groupName.val()==""){
					    		return false;
					    	}else{
								//添加菜单
								$.ajax({
								   type: "POST",
								   url: "<%=basePath%>punPositionController/save.do",
								   data: "groupId="+$('#groupId').val()+"&name="+$groupName.val()+"&grade="+$grade.val(),
								   success: function(data){
									    if(data.status==0){
									    	alertMessage("添加成功");
									     	window.location.href="<%=basePath%>punPositionController/pageList.do?groupId=" + $('#groupId').val();
									    }else{
									    	alertMessage(data.message);
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
					if(count != 1){
						alert("请选择一个岗位编辑");
						return false;
					}
					var positionId;
					var positionName;
					var check_array=document.getElementsByName("positionId");
					var positionNames=document.getElementsByName("positionName");
					var grade=document.getElementsByName("grade");
					//ljw 2014-12-8 checked and modify
					var positionName = positionNames[0].value;
					var positionId = check_array[0].value;
					var grade = grade[0].value;
// 					for(var i=0;i<check_array.length;i++)
// 			           {        
// 		            	   positionId=parseInt(check_array[i].value);
// 		            	   positionName = positionNames[i].value;
// 		            	   break;
// 			           }
///////////////////////////////end//////////////////////////////////////
					var d = dialog({
					    title: '编辑职务名称',
					    content: dialogContent(positionName,grade),
					    okValue: '确定',
					    ok: function () {
					    	var $groupName = $("input[name='groupName']");
					    	var $grade = $("#gradeValue");
					    	console.log($grade.val());
					    	if($groupName.val()==""){
					    		return false;
					    	}else{
								//添加菜单
								$.ajax({
								   type: "POST",
								   url: "<%=basePath%>punPositionController/update.do",
								   data: "groupId="+$('#groupId').val()+"&name="+$groupName.val()+"&grade="+$grade.val()+"&positionId="+positionId,
								   success: function(data){
									    if(data.status==0){
									    	alertMessage("编辑成功");
									     	window.location.href="<%=basePath%>punPositionController/pageList.do?groupId=" + $('#groupId').val();
									    }else{
									    	alertMessage(data.message);
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
						alert("请至少选择一项操作");
						return false;
					}
					if(window.confirm("确定删除？")){
						$("#userList").attr("action","<%=basePath%>punPositionController/remove.do").submit();
					}
					return false;
				})
				
				function alertMessage(message){
					var md = dialog({
					    content: message
					});
					md.show();
					setTimeout(function () {
					    md.close().remove();
					}, 2000);
				}
          	
          });
		</script>
	</body>
</html>
