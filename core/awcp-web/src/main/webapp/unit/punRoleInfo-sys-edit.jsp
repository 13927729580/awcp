<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>角色编辑页面</title>
    <%@ include file="/resources/include/common_lte_css.jsp" %>

</head>
<body>
<section class="content">
    <div class="row">
        <div class="col-md-12">
            <div class="box box-info">
                <div class="box-body">
                    <legend class=" text-center">
                        角色编辑
                        <c:if test="${result!=null&&''!=result}">
                            <span style="color:red">(${result})</span>
                        </c:if>
                    </legend>
                    <form class="form-horizontal" id="creatForm" action="<%=basePath%>unit/saveRoleInSys.do"
                          method="post">
                        <input type="hidden" name="roleId" value="${vo.roleId}"/>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">角色名：</label>
                            <div class="col-md-4">
                                <input name="roleName" class="form-control" id="roleName" type="text"
                                       value="${vo.roleName}">
                            </div>
                            <label class="col-md-2 control-label">描述：</label>
                            <div class="col-md-4">
                                <input name="dictRemark" class="form-control" id="dictRemark" type="text"
                                       value="${vo.dictRemark}">
                            </div>
                        </div>
                        <div class="form-group"><!-- 表单提交按钮区域 -->
                            <div class="col-md-12 text-center">
                                <a class="btn btn-success" id="saveBtn">保存</a>
                                <a href="<%=basePath%>unit/listRolesInSys.do" class="btn btn-info">取消</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>
<%@ include file="../resources/include/common_lte_js.jsp"%>
<script type="text/javascript">
    $("#saveBtn").on("click",function () {
        var roleName=$.trim($("#roleName").val());
        if(roleName){
            $("#creatForm").submit();
        }else{
            Comm.alert("角色名称不能为空");
        }
    })
</script>
</body>
</html>
