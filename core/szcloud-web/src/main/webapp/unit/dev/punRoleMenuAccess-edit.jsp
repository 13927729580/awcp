<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<div class="whole" style="margin:20px;">
	<div class="C_btns">
		<%-- <a class="btn btn-info" href="<%=basePath%>unit/punRoleAccessList.do?currentPage=0">保存并返回</a> --%>
		<a class="btn btn-info" href="<%=basePath%>unit/listRolesInSys.do?boxs=${vo.sysId}">保存并返回</a>
	</div>
	<div style="margin-top:20px;">
      <ul class="CloudTreeMenu"></ul>    
   </div>
</div>
<p><%-- ${menuJson} --%></p>
<script language="javascript">
var roleId = ${vo.roleId};
var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		beforeClick: zTreeBeforeClick,
		beforeCheck: zTreeBeforCheck 
		//onCheck:zTreeOnCheck
	}
};	

function zTreeBeforeClick(){
	return false;
};
function zTreeBeforCheck(treeId, treeNode){
	//alertMessage(treeNode.id + ", " + treeNode.name + "," + treeNode.checked);
	var checked = treeNode.checked;
	var result = function(url,data){var val ;$.ajax({
		url:url,data:data,async:false,success:function(data){
			val = data.status=="0"?true:false;
		}
	});return val;};
	if(!checked){//添加权限
		checkResult = result("<%=basePath%>unit/punRoleAccessAJAXSave.do",{ roleId: roleId, resourceId: treeNode.id, operType:"1"});
		checkResult || alertMessage("抱歉，操作失败");
		return checkResult;
		//checkResult?(return true):(alertMessage("抱歉，操作失败");return false;) 
	}else{//删除权限
		checkResult = result("<%=basePath%>unit/punRoleAccessAJAXDelete.do", { roleId: roleId, resourceId: treeNode.id })
		checkResult || alertMessage("抱歉，操作失败");
		return checkResult;
		//checkResult?(return true):(alertMessage("抱歉，操作失败");return false;)	
	}
		
};
function creatMenu(){
	var dataLi="";
	var dataMap = ${menuJson};
	$.each(dataMap,function(i,item){
		//alert(123)
		dataLi ='<li><ul id="tree'+i+'" class="ztree"></ul></li>';
		$(dataLi).appendTo(".CloudTreeMenu");
		$.fn.zTree.init($("#tree"+i),setting,item);
		  //$("#tree"+i+" > li").eq(0).remove();
	});
}

function alertMessage(message){
	var md = dialog({
	    content: message
	});
	md.show();
	setTimeout(function () {
	    md.close().remove();
	}, 2000);
}
$(document).ready(function(){
	creatMenu();
});
</script>