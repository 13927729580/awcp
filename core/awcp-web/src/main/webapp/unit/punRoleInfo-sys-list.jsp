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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <title>用户管理</title>
    <%@ include file="/resources/include/common_lte_css.jsp" %>
</head>
<body>
<section class="content">
    <div class="opeBtnGrop">
        <a href="#" class="btn btn-success" id="addBtn">新增</a>
        <a href="#" class="btn btn-danger" id="deleteBtn">删除</a>
        <a href="#" class="btn btn-warning" id="updateBtn">修改</a>
        <a href="#" class="btn btn-info" id="accessBtn">授权</a>
        <a href="#" class="btn btn-primary" id="relateUserBtn">关联用户</a>
        <a href="#" onclick="document.getElementById('userList').submit()" class="btn btn-primary">搜索</a>
        <a href="#" onclick="document.getElementById('userList').reset()" class="btn btn-info">清空</a>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="box box-info">
                <div class="box-body">
                    <form method="post" id="userList">
                        <input type="hidden" id="currentPage" name="currentPage"
                               value="${vos.getPaginator().getPage()}">
                        <input type="hidden" id="pageSize" name="pageSize" value="${vos.getPaginator().getLimit()}">
                        <input type="hidden" id="totalCount" name="totalCount"
                               value="${vos.getPaginator().getTotalCount()}">
                        <div class="row form-group">
                            <div class="col-md-6">
                                <div class="input-group">
                                    <span class="input-group-addon">角色名</span>
                                    <input name="roleName" class="form-control" id="roleName" type="text"
                                           value="${vo.roleName}"/>
                                </div>
                            </div>
                        </div>
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th class="hidden"></th>
                                <th data-width="" data-field="" data-checkbox="true"></th>
                                <th>名称</th>
                                <th>备注</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${vos}" var="vo">
                                <tr>
                                    <td class="hidden"><input type="hidden" value="${vo.roleId}"></td>
                                    <td></td>
                                    <td>${vo.roleName}</td>
                                    <td>${vo.dictRemark}</td>
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
<%@ include file="../resources/include/common_lte_js.jsp" %>
<script type="text/javascript">
    $(function () {
        var count = 0;//默认选择行数为0
        $(".table").bootstrapTable({
            pageSize: parseInt($("#pageSize").val()),
            pageNumber: parseInt($("#currentPage").val()),
            totalRows: parseInt($("#totalCount").val()),
            sidePagination: "server",
            pagination: true,
            onPageChange: function (number, size) {
                $("#pageSize").val(size);
                $("#currentPage").val(number);
                $("#userList").attr("action", "<%=basePath%>unit/listRolesInSys.do").submit();
            },
            onClickRow: function (row, $element, field) {
                var $checkbox = $element.find(":checkbox").eq(0);
                if ($checkbox.get(0).checked) {
                    $checkbox.get(0).checked = false;
                    $element.find("input[type='hidden']").removeAttr("name", "boxs");
                } else {
                    $checkbox.get(0).checked = true;
                    $element.find("input[type='hidden']").attr("name", "boxs");
                }
                count = $("input[name='boxs']").length;
            },
            onCheck: function (row, $element) {
                $element.closest("tr").find("input[type='hidden']").attr("name", "boxs");
                count = $("input[name='boxs']").length;
            },
            onUncheck: function (row, $element) {
                $element.closest("tr").find("input[type='hidden']").removeAttr("name");
                count = $("input[name='boxs']").length;
            }
        });
        //新增
        $("#addBtn").click(function () {
            window.location = "<%=basePath%>unit/intoPunRoleInfo.do";
            return false;
        });

        //update
        $("#updateBtn").click(function () {
            if (count == 1) {
                $("#userList").attr("action", "<%=basePath%>unit/editRoleInSys.do").submit();
            } else {
                Comm.alert("请选择一项进行操作");
            }
            return false;
        });

        //delete
        $("#deleteBtn").click(function () {
            if (count < 1) {
                Comm.alert("请至少选择一项进行操作");
                return false;
            }
            if (window.confirm("确定删除？")) {
                $("#userList").attr("action", "<%=basePath%>unit/delRoleInSys.do").submit();
            }
            return false;
        });

        //updata Access
        $("#accessBtn").click(function () {
            if (count == 1) {
                $("#userList").attr("action", "<%=basePath%>unit/punRoleMenuAccessEdit.do").submit();
            } else {
                Comm.alert("请选择一项进行操作");
            }
            return false;
        });
        $("#relateUserBtn").click(function () {
            if (count == 1) {
                $("#userList").attr("action", "<%=basePath%>unit/roleRelateUserQuery.do?roleId=" + $("input[name='boxs']").val()).submit();
            } else {
                Comm.alert("请选择一项进行操作");
            }
            return false;
        });
    })
</script>
</body>
</html>
