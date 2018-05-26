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
<!DOCTYPE html >
<html>
<head>
    <title>元数据管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <%@ include file="/resources/include/common_lte_css.jsp" %>
</head>
<body>
<section class="content">
    <div class="container-fluid">
        <div class="opeBtnGrop">
            <button type="button" class="btn  btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
            <button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
            <button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button>
            <button type="button" class="btn btn-info" id="searchBtn"><i class="icon-search"></i>搜索</button>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="box box-info">
                    <div class="box-body">
                        <form method="post" id="manuList">
                            <input type="hidden" id="currentPage" name="currentPage" value="${list.getPaginator().getPage()}">
                            <input type="hidden" id="pageSize" name="pageSize" value="${list.getPaginator().getLimit()}">
                            <input type="hidden" id="totalCount" name="totalCount" value="${list.getPaginator().getTotalCount()}">
                            <div class="row form-group" id="searchDiv">
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">数据源名称</span>
                                        <input name="name" class="form-control" id="name" type="text"/>
                                    </div>
                                </div>
                            </div>
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th class="hidden"></th>
                                    <th data-width="" data-field="" data-checkbox="true"></th>
                                    <th>数据源名称</th>
                                    <th>初始化连接数(个)</th>
                                    <th>最小连接数(个)</th>
                                    <th>最大连接数(个)</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="k">
                                    <tr>
                                        <td class="hidden formData"><input type="hidden" value="${k.id}"></td>
                                        <td></td>
                                        <td>${k.name }</td>
                                        <td>${k.prototypeCount}</td>
                                        <td>${k.minimumConnectionCount}</td>
                                        <td>${k.maximumConnectionCount }</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <!-- /.content -->
    <%@ include file="../../resources/include/common_lte_js.jsp"%>
    <script type="text/javascript">


        $(function () {
            var count=0;//默认选择行数为0
            $(".table").bootstrapTable({
                pageSize:parseInt($("#pageSize").val()),
                pageNumber:parseInt($("#currentPage").val()),
                totalRows:parseInt($("#totalCount").val()),
                sidePagination:"server",
                pagination:true,
                onPageChange:function(number, size){
                    $("#pageSize").val(size);
                    $("#currentPage").val(number);
                    $("#userList").submit();
                },
                onClickRow:function(row,$element,field){
                    var $checkbox=$element.find(":checkbox").eq(0);
                    if($checkbox.get(0).checked){
                        $checkbox.get(0).checked=false;
                        $element.find("input[type='hidden']").removeAttr("name","id");
                    }else{
                        $checkbox.get(0).checked=true;
                        $element.find("input[type='hidden']").attr("name","id");
                    }
                    count = $("input[name='id']").length;
                },
                onCheck: function(row,$element){
                    $element.closest("tr").find("input[type='hidden']").attr("name","id");
                    count = $("input[name='id']").length;
                },
                onUncheck:function(row,$element){
                    $element.closest("tr").find("input[type='hidden']").removeAttr("name");
                    count = $("input[name='id']").length;
                },
                onCheckAll: function (rows) {
                    $.each(rows,function(i,e){
                        $("input[value='"+$($.trim(e["0"])).attr("value")+"']").attr("name","id");
                    });
                    count = $("input[name='id']").length;
                },
                onUncheckAll: function (rows) {
                    $.each(rows,function(i,e){
                        $("input[value='"+$($.trim(e["0"])).attr("value")+"']").removeAttr("name");
                    });
                    count = $("input[name='id']").length;
                }
            });

            //新增
            $("#addBtn").click(function () {
                top.dialog({
                    id: 'add-dataSource' + Math.ceil(Math.random() * 10000),
                    url: "<%=basePath %>dataSourceManage/get.do",
                    skin: "col-md-6",
                    title: "添加数据源",
                    onclose: function () {
                        location.href = "<%=basePath%>dataSourceManage/selectPage.do";
                    }
                }).showModal();
            })
            //update
            $("#updateBtn").click(function () {
                if (count == 1) {
                    top.dialog({
                        id: 'add-dataSource' + Math.ceil(Math.random() * 10000),
                        url: "<%=basePath%>/dataSourceManage/get.do?id=" + $(":input[name='id']").val(),
                        skin: "col-md-6",
                        title: "添加数据源",
                        onclose: function () {
                            location.href = "<%=basePath%>dataSourceManage/selectPage.do";

                        }
                    }).showModal();
                } else {
                    alertMessage("请选择某个资源进行操作");
                }
            });

            //delete
            $("#deleteBtn").click(function () {
                if (count < 1) {
                    alertMessage("请至少选择一项进行操作");
                    return false;
                }
                Comm.confirm("确认删除？",function(){
                    $("#manuList").attr("action", "remove.do").submit();
                })
            });
            $("#searchBtn").click(function(){
                $("#manuList").submit();
            });

        });
    </script>
</body>
</html>