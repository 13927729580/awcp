<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zui/dist/css/zui.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/layout-default-latest.css"/>
<style>
html,body{overflow-x:hidden;}
a,a:visited{ text-decoration:none;color:#666;}
a:hover{ text-decoration:none;}
.container{width:100%;margin:0px; padding:0; max-width:1900px;}
.nav-tabs{ padding-left:6px; padding-top:4px;/*border:#ccc 1px solid;border-top-left-radius:4px;border-top-right-radius:4px;*/
	background : -webkit-linear-gradient(top, #fafafa 0%, #f5f5f5 100%);
	background : -moz-linear-gradient(top, #fafafa 0%, #f5f5f5 100%);
	background : -ms-linear-gradient(top, #fafafa 0%, #f5f5f5 100%);
	background : -o-linear-gradient(top, #fafafa 0%, #f5f5f5 100%);
	background : linear-gradient(top, #fafafa 0%, #f5f5f5 100%);
}
#functionTabContainer .nav-tabs{ padding-left:6px; padding-top:4px;/*border:#ccc 1px solid;border-top-left-radius:4px;border-top-right-radius:4px;*/
	background : none;
	border-bottom:#a2e4de 1px solid;
}
#functionTabContainer .nav-tabs > li.active > a, #functionTabContainer .nav-tabs > li.active > a:hover, #functionTabContainer .nav-tabs > li.active > a:focus{ background-color:#a2e4de; color:#666;border-bottom-color:transparent;border:#a2e4de 1px solid;color:#fff;}
#functionTabContainer .nav-tabs > li > a{line-height:1;}
.alert{ margin-bottom:4px;}
.alert li{ margin-right:4px; margin-bottom:4px;}
.tab-content{ border-top:none;/*border:#ccc 1px solid;border-bottom-left-radius:4px;border-bottom-right-radius:4px;*/}
.row{padding:10px; padding-bottom:none;}
.panel{ margin-bottom:10px; box-shadow:none; -webkit-box-shadow:none; border:#ddd 1px dotted;}
.panel > .panel-heading{ background-color:#fafafa; border-color:#eee;}
.panel > .panel-heading i{color:#999;}
.with-icon.alert{ padding:0; margin:0;opacity:.6;-webkit-opacity:.6;-moz-opacity:.6;}
.with-icon.alert > [class*='icon-']{ font-size:18px; margin-top:5px;}
.with-icon.alert > .content{padding:.4em;}
.text-gray{color:#aaa;}
.alert button{ margin-right:4px; margin-bottom:4px;}
.panel-body .tip{ border:#eee 1px dashed; text-align:center; height:36px; line-height:36px;color:#eee;}
.SearchList{ height:102px; overflow:auto; padding:8px;}
.rolenamelist,.postnamelist{ list-style:none; padding:0;}
.rolenamelist li,.postnamelist li{ width:70px; float:left; display:inline-block; padding:4px 6px; text-align:center; color:#999; border:#eee 1px solid; margin-right:2px; margin-bottom:2px; cursor:pointer; border-radius:4px;-moz-border-radius:4px;-webkit-border-radius:4px;-o-border-radius:4px;}
.rolenamelist li:hover,.postnamelist li:hover{color:#fff; background-color:#3497DB;}
.rolenamelist li i,.postnamelist li i{ padding-right:2px;}
#tab3 .alertcontent{ background-color:#fefefe;}
</style>
<title>岗位与人员</title>
</head>
<body id="main">
<input type="hidden" value="${groupId}" id="groupID">
<div class="container">
<ul class="nav nav-tabs clearfix" id="myTab">
     <li  class="active">
         <a href="#tab1" data-toggle="tab" onclick="">岗位关联人员</a>
     </li>
     <li>
         <a href="#tab2" data-toggle="tab">角色关联人员</a>
     </li>
     <li>
         <a href="#tab3" data-toggle="tab">角色关联功能</a>
     </li>
</ul>

<div class="tab-content clearfix">
     <div class="tab-pane active" id="tab1">
        <div class="alert alert-success with-icon clearfix" id="postTip">
                <i class="icon-info-sign"></i>
                <div class="content"><strong>温馨提示：</strong>请选择岗位，再添加人员与岗位对应。</div>
                    <i class="icon-times pull-right" style="cursor:pointer;" onClick="closePostTip();"></i>
      </div>
        <div class="row">
              <div class="col-xs-3">
                    <div class="panel">
                        <div class="panel-heading"><i class="icon-cubes"></i> 岗位列表 <i class="icon-arrow-right pull-right"></i></div>
                        <div class="panel-body" id="post">
                          <div id="tree1" class="ztree"></div>
                        </div>
                    </div>
              </div>
                <div class="col-xs-3">
                  <div class="alert">
                                <input type="hidden" value="" id="postID">
                                    <input type="hidden" value="" id="postdeptId">
                                    <input type="hidden" value="" id="postMemberID">
                                    <input type="hidden" value="" id="postMemberName">
                                    <h4><i class="icon-cubes"></i> <span id="postname" class="ptname"></span></h4>
                                    <hr>
                                    <div id="userlistPost" class="belong userlistPost">
                                    		<ul class="postnamelist"></ul>
                                    </div>
                                    
                    </div>
                    
              </div>
                <div class="col-xs-6">
                    <div class="panel">
                        <div class="panel-heading"><i class="icon-th-large"></i> 组织列表 <i class="icon-arrow-right pull-right"></i></div>
                        <div class="panel-body" id="postdepartment">
                          <div id="tree2" class="ztree"></div>
                        </div>
                    </div>
                    <div class="panel select-panel" style="height:162px;">
                  <div class="panel-heading">
                    <div class="col-xs-4"  style="margin-top: 5px;">
                      <button class="btn btn-mini" hosPost="checkAll">全选</button>
                      <button class="btn btn-mini" hosPost="unCheckAll">全不选</button>
                      </div>
                    <div class="col-xs-8 input-group">
                      <input type="text" class="form-control skeyword" placeholder="请输入姓名" name="memberName" id="searchPostName"/>
                        <span class="input-group-btn"><button class="btn btn-default searchBtn" type="button" onclick="searchPostMember()">搜索</button></span>
                    </div>
                  </div>
                  <div class="panel-body SearchList" id="userPostSearchList">
                    <div class="tip">请搜索人员</div>
                  </div>
                     </div>
                     <div class="text-center">
                          <button class="btn btn-success" onclick=" return savePost()"><i class="icon-save"></i> 保存</button>
                     </div>
              </div>
            </div>
       </div>
       <div class="tab-pane" id="tab2">
             <div class="alert alert-info with-icon clearfix" id="roleTip">
                <i class="icon-info-sign"></i>
                <div class="content"><strong>温馨提示：</strong>请选择角色，再添加人员与角色对应。</div>
                    <i class="icon-times pull-right" style="cursor:pointer;" onClick="closeRoleTip();"></i>
      </div>
        <div class="row">
              <div class="col-xs-3">
                    <div class="panel">
                        <div class="panel-heading"><i class="icon-cubes"></i> 角色列表 <i class="icon-arrow-right pull-right"></i></div>
                        <div class="panel-body" id="role">
                          <div id="tree3" class="ztree"></div>
                        </div>
                    </div>
              </div>
                <div class="col-xs-3">
                  <div class="alert">
                                <input type="hidden" value="" id="roleID">
                                    <input type="hidden" value="" id="roleMemberID">
                                    <input type="hidden" value="" id="roleMemberName">
                                    <h4><i class="icon-cubes"></i> <span id="rolename" class="ptname"></span></h4>
                                    <hr>
                                    <div id="userlistRole" class="belong userlistRole">
                                        <ul class="rolenamelist"></ul>
                                    </div>
                                    
                    </div>
                    
              </div>
                <div class="col-xs-6">
                    <div class="panel">
                        <div class="panel-heading"><i class="icon-th-large"></i> 组织列表 <i class="icon-arrow-right pull-right"></i></div>
                        <div class="panel-body" id="roledepartment">
                          <div id="tree4" class="ztree"></div>
                        </div>
                    </div>
                 <div class="panel select-panel" style="height:162px;">
                  <div class="panel-heading">
                    <div class="col-xs-4"  style="margin-top: 5px;">
                      <button class="btn btn-mini" hosRole="checkAll">全选</button>
                      <button class="btn btn-mini" hosRole="unCheckAll">全不选</button>
                    </div>
                    <div class="col-xs-8 input-group">
                      <input type="text" class="form-control skeyword" placeholder="请输入姓名" name="memberName" id="searchRoleName"/>
                        <span class="input-group-btn"><button class="btn btn-default searchBtn" type="button" onclick="searchRoleMember()">搜索</button></span>
                    </div>
                  </div>
                  <div class="panel-body SearchList" id="userRoleSearchList">
                    <div class="tip">请搜索人员</div>
                  </div>
                 </div>
                 <div class="text-center">
                    <button class="btn btn-success" onclick=" return saveRole()"><i class="icon-save"></i> 保存</button>
                  </div>
              </div>
            </div>
                        
        </div>
        <div class="tab-pane" id="tab3">
          <div class="alert alert-success with-icon clearfix" id="functionTip">
                <i class="icon-info-sign"></i>
                <div class="content"><strong>温馨提示：</strong>请选择角色，再配置功能。</div>
                    <i class="icon-times pull-right" style="cursor:pointer;" onClick="closeFunctionTip();"></i>
      </div>
             <div class="row">
              <div class="col-xs-3">
                    <div class="panel">
                        <div class="panel-heading"><i class="icon-cubes"></i> 角色列表 <i class="icon-arrow-right pull-right"></i></div>
                        <div class="panel-body" id="function">
                          <div id="tree5" class="ztree"></div>
                        </div>
                    </div>
              </div>
                <div class="col-xs-9">
                    <iframe style="position: absolute; margin: 0px; left: 2px; right: 20px; top: 0px; bottom: 2px; height: 706px; width: 1250px; z-index: 0; display: block; visibility: visible;" class="ui-layout-center ui-layout-pane ui-layout-pane-center" class="ui-layout-center" src="" name="sysEditFrame" id="sysEditFrame" scrolling="auto" frameborder="0" width="100%"></iframe>
              </div>
             </div>
                        
        </div>
</div>

</div>
<%@ include file="/resources/include/common_form_js.jsp" %> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.post.role.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery-ui-latest.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.layout-latest.js"></script>
<script type="text/javascript">
//保存岗位与人员之间的关联
function savePost(){
  if($("#postname").text().replace(/[\r\n\t\x]/g,"")==""){
      alertMessage('请先选择岗位，再保存！'); 
      return false; 
   }
  var flag=window.confirm("确定要保存吗？");
  if(flag){
    var flagsec=0;
    $.ajax({
    type:"post",
    dataType:"json",
    url:"savePost.do",
    async:false,//同步
    data:{"postId":$("#postID").val(),"userIds":$("#postMemberID").val(),"deptIds":$("#postdeptId").val()},
    success:function(data){
      flagsec = data;
    },
    fail:function(e){
      alert(e);
    }
    });
    if(flagsec==1){
      alertMessage('保存成功！');
      return true;
    }else{
      alertMessage('保存失败！');
      return false;
    }
  }
}
//保存角色与人员之间的关联
function saveRole(){
  if($("#rolename").text().replace(/[\r\n\t\x]/g,"")==""){
      alertMessage('请先选择角色，再保存！'); 
      return false; 
   }
   var flag=window.confirm("确定要保存吗？");
  if(flag){
    var flagsec=0;
    $.ajax({
    type:"post",
    dataType:"json",
    url:"saveRole.do",
    async:false,//同步  
    data:{"roleId":$("#roleID").val(),"userIds":$("#roleMemberID").val()},
    success:function(data){
      flagsec = data;
    },
    fail:function(e){
      alert(e);
    }
    });
    if(flagsec){
      alertMessage('保存成功！');
      return true;
    }else{
      alertMessage('保存失败！');
      return false;
    }
  }
}
</script>
</body>
</html>