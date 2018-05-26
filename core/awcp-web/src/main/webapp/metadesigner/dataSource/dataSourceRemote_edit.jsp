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
    <title>元数据页面</title>
    <%@ include file="/resources/include/common_lte_css.jsp"%>
</head>
<body>
<section class="content">
    <div class="row">
        <div class="col-md-12">
            <div class="box box-info">
                <div class="box-body">
                    <form class="form-horizontal" id="groupForm" action="dataSourceManage/save.do" method="post">
                        <legend>数据源
                            <c:if test="${result!=null&&''!=result}">
                                <span style="color: red">(${result})</span>
                            </c:if>
                        </legend>
                        <div class="form-group">
                            <input name="id" class="form-control" id="id"
                                   type="hidden" value="${vo.id}">
                            <label class="col-md-2 control-label required">名称</label>
                            <div class="col-md-4">
                                <input
                                        <c:if test="${not empty vo.name}">disabled="disabled"</c:if> name="name"
                                        class="form-control" id="name" type="text" value="${vo.name}">
                            </div>
                            <input name="domain" class="form-control" id="domain"
                                   type="hidden" value="remote">
                            <label class="col-md-2 control-label required">类型</label>
                            <div class="col-md-4">
                                <select data-placeholder="请选择属性类型..." id="sourceType" class="chosen-select form-control"
                                        tabindex="2" name="sourceType">
                                    <option value="MYSQL"
                                            <c:if test="${vo.sourceType=='MYSQL'}">selected="selected"</c:if>>MYSQL
                                    </option>
                                    <%--<option value="ORACLE"<c:if test="${vo.sourceType=='ORACLE'}">selected="selected"</c:if>>ORACLE</option>
                                    <option value="DB2"<c:if test="${vo.sourceType=='DB2'}">selected="selected"</c:if>>DB2</option>
                                    <option value="SQL SERVER"<c:if test="${vo.sourceType=='SQL SERVER'}">selected="selected"</c:if>>SQL SERVER</option>
                                    <option value="SYBASE"<c:if test="${vo.sourceType=='SYBASE'}">selected="selected"</c:if>>SYBASE</option>--%>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">URL地址</label>
                            <div class="col-md-4">
                                <input name="sourceUrl" class="form-control" id="sourceUrl"
                                       type="text" placeholder="" value="${vo.sourceUrl}">
                            </div>
                            <label class="col-md-2 control-label">驱动</label>
                            <div class="col-md-4">
                                <input name="sourceDriver" class="form-control" id="sourceDriver"
                                       type="text"
                                       <c:if test="${empty vo.sourceDriver}">value="com.mysql.jdbc.Driver"</c:if>
                                       <c:if test="${not empty vo.sourceDriver}">value="${vo.sourceDriver}"</c:if> >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">用户名</label>
                            <div class="col-md-4">
                                <input name="userName" class="form-control" id="userName"
                                       type="text" value="${vo.userName}">
                            </div>
                            <label class="col-md-2 control-label required">密码</label>
                            <div class="col-md-4">
                                <input name="userPwd" class="form-control" id="userPwd"
                                       type="text" value="${vo.userPwd}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">初始化个数</label>
                            <div class="col-md-2">
                                <input name="prototypeCount" class="form-control" type="number"
                                       <c:if test="${empty vo.prototypeCount}">value="5"</c:if>
                                       <c:if test="${not empty vo.prototypeCount}">value="${vo.prototypeCount}"</c:if>>
                            </div>
                            <label class="col-md-2 control-label required">最小个数</label>
                            <div class="col-md-2">
                                <input name="minimumConnectionCount" class="form-control" type="number"
                                       <c:if test="${empty vo.minimumConnectionCount}">value="5"</c:if>
                                       <c:if test="${not empty vo.minimumConnectionCount}">value="${vo.minimumConnectionCount}"</c:if>>
                            </div>
                            <label class="col-md-2 control-label required">最大个数</label>
                            <div class="col-md-2">
                                <input name="maximumConnectionCount" class="form-control" type="number"
                                       <c:if test="${empty vo.maximumConnectionCount}">value="20"</c:if>
                                       <c:if test="${not empty vo.maximumConnectionCount}">value="${vo.maximumConnectionCount}"</c:if>>
                            </div>
                        </div>
                        <div class="form-group"><!-- 表单提交按钮区域 -->
                            <div class="col-md-offset-2 col-md-10">
                                <button type="button" class="btn btn-primary" id="test">测试</button>
                                <button type="button" class="btn btn-success" disabled="disabled" id="saveBtn">保存</button>
                                <button type="button" class="btn" id="undoBtn">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="<%=basePath%>resources/scripts/en_Us_error_message.js"></script>
<script src="<%=basePath%>template/AdminLTE/js/jquery.min.js"></script>
<script src="<%=basePath%>venson/js/common.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        try {
            var dialog = top.dialog.get(window);
        } catch (e) {
            return;
        }
        $("#sourceDriver").attr("readonly", "readonly");
        $("#sourceType").change(function () {
            var v = $(this).val();
            if (v == "MYSQL") {
                $("#sourceDriver").attr("value", "com.mysql.jdbc.Driver");
            }
            if (v == "SQL SERVER") {
                $("#sourceDriver").attr("value", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            }
            if (v == "DB2") {
                $("#sourceDriver").attr("value", "com.ibm.db2.jdbc.app.DB2.Driver");
            }
            if (v == "SYBASE") {
                $("#sourceDriver").attr("value", "com.sybase.jdbc.SybDriver");
            }
            if (v == "ORACLE") {
                $("#sourceDriver").attr("value", " oracle.jdbc.OracleDriver");
            }
        });
        $("#saveBtn").click(function () {
            if (Comm.validOldForm()) {
                var data = Comm.getData("dataSourceManage/save.do", $("#groupForm").serialize());
                if (data ===1) {
                    Comm.alert("成功");
                    dialog.close();
                } else {
                    Comm.alert("数据源名已经存在");
                }
            }
        });
        $("#undoBtn").click(function () {
            dialog.close();
        });
        $("#test").click(function () {
            if (Comm.validOldForm()) {
                var data = Comm.getData("dataSourceManage/testDataSource.do", $("#groupForm").serialize());
                if (data === 1) {
                    Comm.alert("成功");
                    $("#saveBtn").attr("disabled",false);
                } else {
                    $("#saveBtn").attr("disabled",true);
                    Comm.alert("无法连接，请检查连接信息");
                }
            }
        });

    });
</script>
</body>
</html>