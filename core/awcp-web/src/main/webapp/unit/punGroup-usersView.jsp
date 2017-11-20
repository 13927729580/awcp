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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>人员信息管理</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="../resources/include/common_css.jsp"%><!-- 注意加载路径 -->
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
				<!--
				<li><a href="#">组织机构与权限</a></li>
				 <li><a href="#">开发者</a></li> -->
				<li class="active">成员管理</li>
			</ul>
		</div>
		
		<div class="row" id="searchform">
				<div id="collapseButton">
					<form method="post" action="<%=basePath%>unit/searchByType.do" id="search" class="clearfix">
						<input type="hidden" name="currentPage" value="1" />
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
						<div class="col-xs-6">
							<div class="input-group">
								<span class="input-group-addon">名称</span>
								<input class="form-control" id="searchName" name="searchName" value="${searchName}" type="text"/>
							</div>
						</div>
						<div class="col-xs-1 btn-group">
							<button class="btn btn-primary" type="submit">提交</button>
						</div>
						
						
						<div class="col-xs-1 btn-group">
							<button class="btn btn-primary" type="button" onClick="removeUser()">移动到</button>
						</div>
					</form>
				</div>
				
			</div>	
				
		
		<input type="hidden" id="groupId" value="${groupId}"/>
		<input type="hidden" id="rootGroupId" value="${rootGroupId}"/>
		<div class="row" id="datatable">
		<form method="post" id="userList">
			<input name="positionName" type="hidden" value="${vo.name}">
			<!--部门 or 小组ID-->
			<input type="hidden" name="groupId" value="${groupId}">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th class="hidden"><input type="hidden" name="rootGroupId" value="${groupId}"></th>
						<th style="text-align:center;"><input type="checkbox" name="boxAll" id="boxAll" onClick="checkAll()"/></th>
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
								<td align="center">
									<input id="boxs" name="boxs" type="checkbox" value="${vo.userId}"> 
								</td>
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

	<%@ include file="../resources/include/common_js.jsp"%>
	<script type="text/javascript">
		  $(function(){
			  var groupId =$("#groupId").val();
			  var rootGroupId =$("#rootGroupId").val();
			  var count=0;//默认选择行数为0
			  var searchType = '${searchType}';
			  if(searchType!=""){
				  $("#searchType").val(searchType);  
			  }
			  
			  
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
					  		//alert(1);
						  	this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
						  	//console.log(this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs"));
						  	//console.log(this.$table.find("tbody tr").eq(checkArray[i]).find("input[name='boxs']"));
					  }
				  }
					
			  });
			  
			  $('#allchecker').bind("click", function(){
					//alert(treeId+"-"+treeNode.tId)
					location.href="<%=basePath%>punUserGroupController/get.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId;
				});
			$('#removePerson').bind("click", function(){
				//alert($("#userList").serialize());
          		<%--var _selects = new Array();
				$("input[name='boxs']").each(function(){
					var value=$(this).val();
					_selects.push(value);
				});--%>
				//alert(_selects);
				$("#userList").attr("action","<%=basePath%>punUserGroupController/removeUserFromGroup.do").submit();
				return false;
			});

				$('#alleidt').bind("click", function(){
					if(count != 1){
						alert("请选择一个人员编辑");
						return false;
					}
					var userId;
					
					var check_array=document.getElementsByName("boxs"); 
					userId = check_array[0].value;
// 					for(var i = 0; i< check_array.length;i++){
// 						if(undefined != check_array[i].value || "" !=check_array[i].value)
// 							userId=parseInt(check_array[i].value);
// 					}
					<%-- alert("<%=basePath%>punUserGroupController/getUserForEdit.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId + "&userId=" + userId); --%>
					location.href="<%=basePath%>punUserGroupController/getUserForEdit.do?groupId=" + groupId + "&rootGroupId=" + rootGroupId + "&userId=" + userId;
				});

		});
		
		//用户编号编辑
		function userEdit(id,number){
			
			if(number==-1){
			   number="";
			}else{

			  number=number.substr(number.lastIndexOf("-")+1,number.length);
			}
			
			var content="<div class='input-group'>"+
			"<span class='input-group-addon'><i class='icon-pencil'></i>&nbsp;&nbsp;编号</span>"+
        	"<input name='userNumber' type='text' class='form-control' value='"+number+"' placeholder='编号'></div>"
			
			
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
		});
		d.width(320).show();
		return false;
		}
		//人员编辑分组
		function removeUser(){
			var groupId=$("#groupId").val();
			var value =[]; 
			$('input[name="boxs"]:checked').each(function(){
				value.push($(this).val()); 
			});
			if(value.length==0){
			  alert("请至少选择一项进行操作");
			  return false;
			} 
			
			
			top.dialog({
						title: "选择要移动的部门",
						url:"<%=basePath%>unit/deptList_check.jsp",
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						data:{userIds:value,oldGroupId:groupId},
						height:'600',
					    onclose: function () {
					    	var value=this.returnValue;
					    	//刷新数据
					    	location.href="<%=basePath%>punUserGroupController/getUsers.do?groupId="+groupId;
					    	
					    }
					}).width(600).showModal();
		
		}
		
		
		function checkAll(){
   
	 	if($('#boxAll').is(':checked')==true) {
    	$("input[name='boxs']").prop("checked",true);
	 	}
	 
	 	if($('#boxAll').is(':checked')==false) {
	 	$("input[name='boxs']").prop("checked",false);
	 	
	 	}
	 
		}
	</script>
</body>
</html>
