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
	<base href="<%=basePath%>">
		<meta charset="utf-8">
		<link href="base/resources/zui/dist/css/zui.min.css" rel="stylesheet">
		<link href="base/resources/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
		<link href="base/resources/zui/assets/datetimepicker/css/datetimepicker.min.css" rel="stylesheet">
		<link href="base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/zui.css">
		<link title="default" rel="stylesheet" type="text/css" href="base/resources/content/styles/szCloud_common.css" />
		<link title="default" rel="stylesheet" type="text/css" href="base/resources/zui/dist/css/style.css" />
  		<script src="resources/JqEdition/jquery-1.10.2.js" language="javascript" type="text/javascript"></script>
		<script src="base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
		<script src="base/resources/artDialog/dist/dialog.js"></script>
		<script src="base/resources/artDialog/lib/require.js"></script>
		<script src="base/resources/artDialog/lib/sea.js"></script>
		<script type="text/javascript" src="base/resources/content/scripts/szcoud.js"></script>
		<title>用户管理</title>
		<script type="text/javascript">
		  $(function(){
          	//新增
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>manage/intoPunManageRoleInfo.do";
      			location.href = url;
      			return false;
      		})
      		
      		//update
      		$("#updateBtn").click(function(){
      			var count = $("input:checkbox[name='boxs']:checked").length;
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>manage/punManageRoleInfoGet.do").submit();
      			}else{
      				alert("请选择一项进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#deleteBtn").click(function(){
          		if(window.confirm("确定删除？")){
	          		$("#userList").attr("action","<%=basePath%>manage/punManageRoleInfoDelete.do").submit();
          		}
      			return false;
          	})
          	
        	//updata Access
          	$("#accessBtn").click(function(){
          		var checkedDom = $("input:checkbox[name='boxs']:checked");
      			var count = checkedDom.length;
      			if(count == 1){
      				<%-- $("#userList").attr("action","<%=basePath%>manage/punManageSysMenuAccessEdit.do").submit(); --%>
      				var roleId =checkedDom.val();
      				//alert(roleId);
      				window.location.href="<%=basePath%>manage/punSysMenuAccessEdit.do?roleId="+roleId;
      			}else{
      				alert("请选择一项进行操作");
      			}
      			return false;
      		});
          	
          });
		</script>
	</head>
	<body>
		<div class="whole" style="width: auto;margin:20px ;">
			    <div class="C_btnGroup clearfix">
					<div class="C_btns">
						<style>.modal-dialog{width:50%;}</style>
					    <button class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button> 
					    <button class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button> 
					    <button class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button> 
					    <button class="btn btn-danger" id="accessBtn"><i class="icon-edit"></i>修改权限</button>
					</div>
					
		 <div class="C_search" style="float:left; margin-left:20px;">
    		<div class="input-group" style="width:100px;">
                <span class="input-group-addon"><i class="icon-search"></i></span>
                <span class="input-group-btn">
                  <div class="dropdown">
                      <button class="btn dropdown-toggle" type="button" data-toggle="dropdown">
                      	  搜索
                        <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                        <li>
                          <a id="createBtn" href="javascript:;" data-toggle="show" data-target="#createForm" data-triggers="click,hide,self">综合搜索</a>
                          <form class="hide" action="<%=basePath%>manage/punManageRoleInfoList.do" id="createForm">
                          	<div style="margin-bottom: 10px;">
                             <span class="CItem">角色名：</span>
                             <input name="roleName" class="form-control" id="roleName" type="text" style="width: 360px;margin-bottom: 10px;" value="${vo.roleName}"></input>
                              <input type="hidden" name="currentPage" value="0">
                            </div>
                            <button class="btn btn-primary" type="submit" style="float:left; margin:4px;">确定</button>
                            <button class="btn" data-toggle="hide" data-target="#createForm" data-triggers="click,show,#createBtn" style="float:left; margin:4px;">取消</button>
                          </form>
                        </li>
                      </ul>
                    </div>
                </span>
            </div>          
    </div>
                   
			</div>
		
	<div class="C_tabBox clearfix">
	   <div class="C_tabList">
	     <form method="post" id="userList">
			<table class="table table-condensed table-hover table-striped tablesorter table-fixed" id="tasktable">
	        <thead>   						
	        	<script>
		                var checkflag = "false";
						function check(fieldName) {
						var field=document.getElementsByName(fieldName);
						if (checkflag == "false") {
							for (i = 0; i < field.length; i++) {
							field[i].checked = true;}
							checkflag = "true";
							return "Uncheck All"; }
						else {
							for (i = 0; i < field.length; i++) {
							field[i].checked = false; }
							checkflag = "false";
							return "Check All"; }
						}
		            </script>
	          <tr class="text-center">
	            <th width="10%" class="text-center"><input type="checkbox" value="" style="margin-top: -3px;"onclick="this.value=check('boxs')"></th>
	            <th width="45%" class="text-center"><div class="header"><a href="javascript:void(0)">系统</a></div></th>
				<th width="45%" class="text-center"><div class="header"><a href="javascript:void(0)">角色名</a></div></th>
	          </tr>
	        </thead>
	        <tbody>
	          <c:forEach items="${vos}" var="vo">
	          <tr class="text-center"> 	
	            <td>
	            	<input name="boxs" type="checkbox" value="${vo.roleId}">
	            	<input type="hidden" name="currentPage" value="${currentPage}">
	            </td>
	            <td>${vo.sysId}</td>
	            <td>${vo.roleName}</td>
	          </tr>
	          </c:forEach>
	        </tbody>
	        <tfoot>
          </tfoot>
	      </table>
	       </form>
	   </div>
	</div>
	<sc:PageNavigation dpName="vos"></sc:PageNavigation>
	<script type="text/javascript" src="base/resources/unit/pageTurn.js"></script>		
	</body>
</html>
