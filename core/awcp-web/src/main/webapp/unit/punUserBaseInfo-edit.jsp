<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>用户详细信息编辑</title>
    <%@ include file="/resources/include/common_lte_css.jsp" %>
    <link rel="stylesheet" href="${basePath}resources/styles/content/uploader.css">
    <link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <style type="text/css">
        .checkbox-inline {
            display: inline-block;
            min-width: 18%;
            padding-left: 0px;
            margin-bottom: 0;
            font-weight: 400;
            vertical-align: middle;
            cursor: pointer;
            text-align: left;
            float: left;
        }
        .checkbox-inline input[type="checkbox"]{
            position:inherit;
            margin-left:0;
            margin-right:5px;
        }
        .ztree{
            padding-left:0;
        }
        .ztree li span.button.switch{
            width:0;
        }
        .radiodiv label:first-child{
        	margin-left:10px;
        }
    </style>
</head>
<body>
	<section class="content">
	    <div class="row" id="breadcrumb">
	        <ul class="breadcrumb">
	            <li><i class="icon-location-arrow icon-muted"></i></li>
	            <li><a href="<%=basePath%>unit/punUserBaseInfoList.do">用户管理</a></li>
	            <li class="active">用户详细信息编辑</li>
	        </ul>
	    </div>
	    <div class="row">
	    	<div class="col-md-12">
	    		<div class="box box-info">
	    			<div class="box-body">
				        <form class="form-horizontal" id="groupForm" method="post">
				            <input type="hidden" name="userId" value="${vo.userId}" />
				            <input type="hidden" name="userIdCardNumber" value="${vo.userIdCardNumber}" />
				            <input type="hidden" name="groupId" value="${vo.groupId}" />
				            <input type="hidden" name="orgCode" value="${vo.orgCode}" />
				            <input type="hidden" id="updatePassword" name="updatePassword" value="yes"/>
				            <div class="form-group">
				                <label class="col-md-1 control-label required">用户名</label>
				                <div class="col-md-4">
				                    <input name="userName" class="form-control" id="userName" type="text" value="${vo.userIdCardNumber}" />
				                </div>
				                <label class="col-md-2 control-label required">姓名</label>
				                <div class="col-md-4">
				                    <input name="name" class="form-control" id="name" type="text" value="${vo.name}" />
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="col-md-1 control-label required">密码</label>
				                <div class="col-md-4">
				                    <input name="userPwd" class="form-control password_userPwd" id="userPwd" 
				                    	type="text" value="${vo.userPwd}" />
				                </div>
				                <label class="col-md-2 control-label required">用户状态</label>
				                <div class="col-md-4">
				                    <input name="userStatus" class="form-control" id="userStatus" type="text"
				                           placeholder="0禁用，1正常，2审核" value="${vo.userStatus}" />
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="col-md-1 control-label">户籍地</label>
				                <div class="col-md-4">
				                    <input name="userHouseholdRegist" class="form-control" id="userHouseholdRegist" 
				                    	type="text" value="${vo.userHouseholdRegist}" />
				                </div>			
				                <label class="col-md-2 control-label">居住地</label>
				                <div class="col-md-4">
				                    <input name="userDomicile" class="form-control" id="userDomicile" 
				                    	type="text" value="${vo.userDomicile}" />
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="col-md-1 control-label">办公电话</label>
				                <div class="col-md-4">
				                    <input name="userOfficePhone" class="form-control" id="userOfficePhone" 
				                    	type="text" value="${vo.userOfficePhone}" />
				                </div>
				                <label class="col-md-2 control-label">移动电话</label>
				                <div class="col-md-4">
				                    <input name="mobile" class="form-control" id="mobile" 
				                    	type="text" value="${vo.mobile}" />
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="col-md-1 control-label">传真</label>
				                <div class="col-md-4">
				                    <input name="userFax" class="form-control" id="userFax" 
				                    	type="text" value="${vo.userFax}" />
				                </div>
				                <label class="col-md-2 control-label">邮箱</label>
				                <div class="col-md-4">
				                    <input name="userEmail" class="form-control" id="userEmail" 
				                    	type="text" value="${vo.userEmail}" />
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="col-md-1 control-label">工号</label>
				                <div class="col-md-4">
				                    <input name="employeeId" class="form-control" id="employeeId" 
				                    	type="text" value="${vo.employeeId}" />
				                </div>
				                <label class="col-md-2 control-label">头衔</label>
				                <div class="col-md-4">
				                    <input name="userTitle" class="form-control" id="userTitle" 
				                    	type="text" value="${vo.userTitle}" />
				                </div>
				            </div>
				            <!-- 
				            <div class="form-group">
				                <label class="col-md-1 control-label">档案帐号</label>
				                <div class="col-md-4">
				                    <input name="userDossierNumber" class="form-control" id="userDossierNumber" 
				                    	type="text" value="${vo.userDossierNumber}" />
				                </div>
				                <label class="col-md-2 control-label">办公室门牌号</label>
				                <div class="col-md-4">
				                    <input name="userOfficeNum" class="form-control" id="userOfficeNum" 
				                    	type="text" value="${vo.userOfficeNum}">
				                </div>		
				            </div> 
				            -->
				            <div class="form-group">
				                <label class="col-md-1 col-sm-1 control-label">角色</label>
				                <div class="col-md-11 col-sm-11 radiodiv">
				                    <c:forEach items="${roleVos}" var="vo">
				                        <c:choose>
				                            <c:when test="${(fn:contains(selectedRole,vo.roleId)&&vo.roleId!=1)}">
				                                <label class="checkbox-inline">
				                                    <input name="roleList" type="checkbox" 
				                                    	value="${vo.roleId}" checked="checked">${vo.roleName}
				                                </label>
				                            </c:when>
				                            <c:otherwise>
				                                <label class="checkbox-inline">
				                                    <input name="roleList" type="checkbox" value="${vo.roleId}">${vo.roleName}
				                                </label>
				                            </c:otherwise>
				                        </c:choose>
				                    </c:forEach>
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="col-md-1 col-sm-1 control-label">职务</label>
				                <div class="col-md-11 col-sm-11 radiodiv">
				                    <c:forEach items="${posiVos}" var="vo">
				                        <c:choose>
				                            <c:when test="${selectedPosition==vo.positionId}">
				                                <label class="checkbox-inline">
				                                    <input name="positionId"  type="radio" 
				                                    	value="${vo.positionId}" checked="checked">${vo.name}
				                                </label>
				                            </c:when>
				                            <c:otherwise>
				                                <label class="checkbox-inline">
				                                    <input name="positionId" type="radio" value="${vo.positionId}"> ${vo.name}
				                                </label>
				                            </c:otherwise>
				                        </c:choose>
				                    </c:forEach>
				                </div>
				            </div>
				            <div class="form-group">
				                <label class="col-md-1  col-sm-1 control-label">组织</label>
				                <div class="col-md-11 col-sm-11 radiodiv">
				                    <input type="hidden" value="${selectedGroup}" id="positionGroupId"
				                           name="positionGroupId"/>
				                    <div id="groups" class="ztree">
				
				                    </div>
				                </div>
				            </div>
				            <div class="form-group"><!-- 表单提交按钮区域 -->
				                <div class="col-md-offset-2 col-md-10">
				                    <a class="btn btn-success" id="saveBtn">保存</a>
				                    <a href="<%=basePath%>unit/punUserBaseInfoList.do" class="btn btn-default" id="undoBtn">取消</a>
				                </div>
				            </div>
				        </form>
	   				</div>
	   			</div>
	   		</div>
	   	</div>
	</section>
	<script src="<%=basePath%>template/AdminLTE/js/jquery.min.js"></script>
	<script src="<%=basePath%>venson/js/common.js"></script>
	<script src="<%=basePath%>resources/scripts/ajaxfileupload.js"></script>
	<script src="<%=basePath%>resources/scripts/uploadPreview.js"></script>
	<script src="<%=basePath%>resources/scripts/uploader.js"></script>
	<script src="<%=basePath%>venson/js/tree.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script type="text/javascript">
	    var basePath = '<%=basePath%>';
	    var zTreeObj;
	    $.get(basePath + "unit/listGroup.do", function (data) {
	        var setting = {
	            view: {
	                selectedMulti: false,
	                showIcon: false
	            },
	            check: {
	                enable: true,
	                chkStyle: "radio",
	                chkboxType: {"Y": "", "N": ""},
	                radioType: "all"
	            },
	            data: {
	                key: {
	                    name: "groupChName"
	                },
	                simpleData: {
	                    enable: true,
	                    idKey: "groupId",
	                    pIdKey: "parentGroupId",
	                    rootPId: 0
	                }
	            },
	            callback: {
	                onCheck: zTreeOnCheck,
	            }
	        }
	
	        zTreeObj = $.fn.zTree.init($("#groups"), setting, data);
	        var groupId = $("#positionGroupId").val();
	        var node = zTreeObj.getNodeByParam("groupId", groupId, null);
	        //选中节点并展开
	        if (node) {
	            zTreeObj.checkNode(node, true, true);
	            if (!zTreeObj.expandNode(node)) {
	                zTreeObj.expandAll(true);
	            }
	        }
	        $(":radio[value='${selectedGroup}']").prop("checked", true);
	    }, "json");
	
	    function zTreeOnCheck(event, treeId, treeNode) {
	        $("#positionGroupId").val(treeNode.checked ? treeNode.groupId : "1")
	    }

	    $(function () {
	        $("#saveBtn").on("click", function () {
	            if(!Comm.validOldForm()){
		    		return false;
				}
	            $.ajax({
	         	   type: "POST",
	         	   url: basePath + "unit/punUserBaseInfoSave.do",
	         	   data:$("#groupForm").serialize(),
	         	   async : false,
	         	   success: function(data){
	         			if(data.status==0){
	         				Comm.alert("保存成功",function(){
	         					location.href = basePath + "unit/punUserBaseInfoList.do";
	         				});	         				
	         			} else{
	         				Comm.alert(data.message);
	         			}
	         	   }
	         	})
	        })
	    });
	</script>
</body>
</html>
