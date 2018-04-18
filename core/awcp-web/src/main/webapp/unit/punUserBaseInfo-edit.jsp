<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <meta charset="utf-8">
    <title>用户详细信息编辑</title>

    <%@ include file="/resources/include/common_lte_css.jsp" %>
    <link rel="stylesheet" href="${basePath}resources/styles/content/uploader.css">
    <link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <style type="text/css">
        .checkbox-inline {
            display: inline-block;
            min-width: 23%;
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
        #file_signatureImage{display: none}
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

                    <form class="form-horizontal" id="groupForm"
                          action="<%=basePath%>unit/punUserBaseInfoSave.do" method="post">
                        <input type="hidden" name="userId" value="${vo.userId}"/>
                        <input type="hidden" name="userName" value="${vo.userName}"/>
                        <input type="hidden" name="groupId" value="${vo.groupId}"/>
                        <input type="hidden" name="orgCode" value="${vo.orgCode}"/>
                        <legend class=" text-center">
                            用户详细信息编辑
                            <c:if test="${result!=null&&''!=result}">
                                <span style="color:red">(${result})</span>
                            </c:if>
                        </legend>
                        <input type="hidden" id="updatePassword" name="updatePassword" value="no"/>
                        <div class="form-group">
                            <label class="col-md-1 control-label required">用户名</label>
                            <div class="col-md-4">
                                <input name="userIdCardNumber" class="form-control" id="userIdCardNumber" type="text"
                                       placeholder="" value="${vo.userIdCardNumber}">
                            </div>
                            <label class="col-md-2 control-label required">姓名</label>
                            <div class="col-md-4">
                                <input name="name" class="form-control" id="name" type="text" placeholder=""
                                       value="${vo.name}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-1 control-label required">密码</label>
                            <div class="col-md-4">
                                <input name="userPwd" class="form-control password_userPwd" id="userPwd"
                                       type="text" placeholder="" value="${vo.userPwd}">
                            </div>
                            <label class="col-md-2 control-label required">用户状态</label>
                            <div class="col-md-4">
                                <input name="userStatus" class="form-control" id="userStatus" type="text"
                                       placeholder="0禁用，1正常，2审核" value="${vo.userStatus}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-1 control-label">户籍地</label>
                            <div class="col-md-4">
                                <input name="userHouseholdRegist" class="form-control" id="userHouseholdRegist"
                                       type="text" placeholder="" value="${vo.userHouseholdRegist}">
                            </div>

                            <label class="col-md-2 control-label">居住地</label>
                            <div class="col-md-4">
                                <input name="userDomicile" class="form-control" id="userDomicile" type="text"
                                       placeholder="" value="${vo.userDomicile}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-1 control-label">办公电话</label>
                            <div class="col-md-4">
                                <input name="userOfficePhone" class="form-control" id="userOfficePhone" type="text"
                                       placeholder="" value="${vo.userOfficePhone}">
                            </div>
                            <label class="col-md-2 control-label">移动电话</label>
                            <div class="col-md-4">
                                <input name="mobile" class="form-control" id="mobile" type="text" placeholder=""
                                       value="${vo.mobile}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-1 control-label">传真</label>
                            <div class="col-md-4">
                                <input name="userFax" class="form-control" id="userFax" type="text" placeholder=""
                                       value="${vo.userFax}">
                            </div>
                            <label class="col-md-2 control-label">邮箱</label>
                            <div class="col-md-4">
                                <input name="userEmail" class="form-control" id="userEmail" type="text" placeholder=""
                                       value="${vo.userEmail}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-1 control-label">工号</label>
                            <div class="col-md-4">
                                <input name="employeeId" class="form-control" id="employeeId" type="text" placeholder=""
                                       value="${vo.employeeId}">
                            </div>
                            <label class="col-md-2 control-label">头衔</label>
                            <div class="col-md-4">
                                <input name="userTitle" class="form-control" id="userTitle" type="text" placeholder=""
                                       value="${vo.userTitle}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-1 control-label">档案帐号</label>
                            <div class="col-md-4">
                                <input name="userDossierNumber" class="form-control" id="userDossierNumber" type="text"
                                       placeholder="" value="${vo.userDossierNumber}">
                            </div>
                            <label class="col-md-2 control-label">办公室门牌号</label>
                            <div class="col-md-4">
                                <input name="userOfficeNum" class="form-control" id="userOfficeNum" type="text"
                                       placeholder="" value="${vo.userOfficeNum}">
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-md-1 control-label">签名</label>
                            <div class="col-md-4">
                                <div class="uploadPreview" style="text-align: center;">
                                    <input type="hidden" class="photo" name="signatureImg" value="${vo.signatureImg}"/>
                                    <div class="photo-con">
                                        <img id="signatureImage_Img"
                                             style="width: 30px; height: 30px; margin-left: auto; margin-right: auto;"
                                             src="<%=basePath%>common/file/showPicture.do?id=${vo.signatureImg}"
                                             alt="点击选择图片"/>
                                    </div>
                                    <input type="file" id="file_signatureImage" name="file_signatureImage"/>
                                    <div class="photo-btn"><a class="btn delete" href="javascript:;">删除</a><span
                                            class="msg text-danger"></span></div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-1 col-sm-1 control-label">角色</label>
                            <div class="col-md-11 col-sm-11 radiodiv">
                                <c:forEach items="${roleVos}" var="vo">
                                    <c:choose>
                                        <c:when test="${(fn:contains(selectedRole,vo.roleId)&&vo.roleId!=1)}">
                                            <label class="checkbox-inline">
                                                <input name="roleList" type="checkbox" value="${vo.roleId}" checked="checked">${vo.roleName}
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
                                                <input name="positionId"  type="radio" value="${vo.positionId}" checked="checked">${vo.name}
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
        loadUploadPreview("signatureImage_Img", "file_signatureImage", 20, 20);
        $("input[type='password']").attr("readonly", "readonly");
        $("input[type='password']").each(function (i, e) {
            if (!e.value) {
                e.value = 791013;
            }
        })
        $("#changePassword").click(function () {
            if ($(this).is(':checked')) {
                $(".password_userPwd").removeAttr("readonly");
                $("#updatePassword").val("yes");
            }
            else {
                $(".password_userPwd").attr("readonly", "readonly");
                $("#updatePassword").val("no");
            }
        });
        $("#showPassword").click(function () {
            if ($(this).is(':checked')) {
                $(".password_userPwd").attr("type", "text");
            }
            else {
                $(".password_userPwd").attr("type", "password");
            }
        });
        $("#saveBtn").on("click", function () {
            var $required = $("#groupForm .required");
            var len = $required.length;
            for (var i = 0; i < len; i++) {
                var _dom = $required.eq(i);
                var text = _dom.text();
                var dataItemCode = $(_dom).next("div").children(":input");
                if (!dataItemCode.val()) {
                    Comm.alert(text + "不能为空！");
                    return;
                }
            }
            $("#groupForm").submit();
        })
    });
</script>


</body>
</html>
