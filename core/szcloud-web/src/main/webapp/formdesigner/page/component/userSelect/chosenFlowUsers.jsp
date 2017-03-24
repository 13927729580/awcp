<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
	String actId = request.getParameter("actId");
	String data = request.getParameter("data");
	String name = request.getParameter("name");
	String master = request.getParameter("master");
	String comment = request.getParameter("comment");
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>菜单编辑页面</title>
 	    <%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
 		<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
 		<style>
 		#main{padding:10px;}
		.selectpanel{ padding:4px 2px 0px 2px;}
		.selectpanel .nav>li>a{padding:6px 12px;}
		.selectpanel .nav-tabs{border-bottom:0px;}
		.checkbar{margin-bottom:6px;}
		.select-panel .panel-heading{padding:2px 15px;}
		.select-panel .panel-body{height:354px;;max-height:400px;}
		.member-panel .panel-body{height:354px;}
		.departpanel{padding:2px;padding-top:0px;padding-bottom:0px;}
		.departpanel .nav-tabs{border-bottom:0px; margin-bottom:-2px;}
		.departpanel li a{padding:4px;}
		.grouppanel{padding:2px;padding-top:0px;padding-bottom:0px;}
		.grouppanel .nav-tabss{border-bottom:0px; margin-bottom:-2px;}
		.grouppanel li a{padding:4px;}
		.ztree i{font-family:ZenIcon,Helvetica Neue,Helvetica,Tahoma,Arial,sans-serif;font-size:14px;}
		
		#myDepartTab{}
		#myDepartTab li,#groupTab li{clear:both;width:26px;padding-top:2px;}
		#departTab{border-bottom:none;}
		.nav-tabss>li.active>a, .nav-tabss>li.active>a:hover, .nav-tabss>li.active>a:focus{
		color:gray;cursor:default;border:#ddd 1px solid;background-color:#fff;border-radius:4px 0 0 4px;border-right-color:transparent;
		}
		</style>
	</head>
	<body id="main" class="inner-form">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-3 col-xs-3">					
					<div class="panel tree-panel" style="/*width:200px*/">
					<div class="panel-heading selectpanel">
                    <ul class="nav nav-tabs clearfix" id="myTab">
                    <li  class="active"><a href="#tab1" data-toggle="tab" onClick="searchNormalPerson()">部门</a></li>
                    <li><a href="#tab1" data-toggle="tab" onClick="searchGroup()">分组</a></li>
                    <li><a href="#tab1" data-toggle="tab" onClick="searchRole()">角色</a></li>
                    <li><a href="#tab1" data-toggle="tab" onClick="searchJob()">岗位</a></li>
                    </ul>                     
					</div>
                  
					<div class="panel-heading" id="search" style="display:block; background:none;">
						
					</div>
                  
					<div class="panel-heading departpanel" id="departTab" style="display:block; background:none;height:0px;">
					<!-- 
					<ul class="nav nav-tabss clearfix" id="myDepartTab">
                    <li  class="active"><a href="#tab1" data-toggle="tab" onClick="searchNormalPerson()">常用联系人</a></li>
                    <li ><a href="#tab1" data-toggle="tab" onClick="searchNormalDepart()">常用部门</a></li>
                    <li><a href="#tab1" data-toggle="tab" onClick="searchAllDepart()">所有部门</a></li>   
					</ul>
					 -->
					</div>
                  
                  
					  <!--
					  <div class="panel-heading grouppanel" id="groupTab" style="display:none; background:none;height:0px;">
						  <ul class="nav nav-tabss clearfix" id="myGroupTab">
						<li  class="active"><a href="#tab1" data-toggle="tab" onClick="searchDefineGroup()">自定义分组</a></li>   
					   </ul>
					  </div>
					  -->
                                    
					<div class="panel-body" id="tab1" style="height:316px;padding-left:20px;margin-left:26px;">
						<ul id="tree1" class="ztree"></ul>
					</div>
					</div>
				</div>
				
                <div class="col-md-9 col-xs-9">
					<div class="row">
						<div class="col-md-6 col-xs-6">
						<div class="panel select-panel">
			            <div class="panel-heading">
			            	<div class="col-xs-6"  style="margin-top: 5px;">
			            	<div class="btn-group" style="width: 164px;">
			            		<button class="btn btn-xs" js="checkAll">全选</button>
			            		<button class="btn btn-xs" js="unCheckAll">全不选</button>
			            	</div>
			                </div>
			            	<div class="col-xs-6 input-group">
			            		
								<div class="input-group" style="height: 30px">
								<!-- 
			            		<input type="text" class="form-control" placeholder="请输入姓名" name="memberName" id="memberName"/>
										<span class="input-group-btn"> <button class="btn btn-default" type="button" id="subeq" onclick="subSearch()"><i class="icon-search"></i>筛选</button> </span>
								 -->
								</div>
								
			            	</div>
			            </div>
			            <div class="panel-body">
			            	<div class="tip">请点击左侧菜单</div>
			            </div>
						</div>
						
						</div>
                
						<div class="col-md-6 col-xs-6">
							<div class="panel member-panel">
							<input type="hidden" id="datas" value="">
						<div class="panel-heading">已选择 <button class="btn btn-xs pull-right" js="deleteAll">全删除</button></div>
								<div class="panel-body">
							<div class="tip">请从左侧选择框选择用户</div>
								</div>
							</div>
						</div>
					</div> 
					<div class="row checkbar">
						<div class="col-md-4  col-xs-4  text-left">
						<!--<span class="btn btn-default" id="toMessage"><i class="icon-check-empty"></i> 短信通知</span>-->
						</div>
						<div class="col-md-4  col-xs-4  text-left">
						<!--<span class="btn btn-default" id="toOnline"><i class="icon-check-empty"></i> 转在办</span>-->
						</div>
						<div class="col-md-4  col-xs-4  text-right">
								<button type="button" class="btn btn-success" id="subBtn"><i class="icon-save"></i>确定</button>
								<!--<a href="javascript:;" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>-->
						</div>					
					</div>
				</div>
			</div>

		</div>		
		<%@ include file="/resources/include/common_form_js.jsp" %>	
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.selectPanel.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jsonpath-0.8.0.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/workFlow.js"></script>
		<script type="text/javascript">
		
		var groupId = ${sessionScope.current_user.groupId};
         $(function(){
        	//init
        	//close dialog 
        	try {
 				var dialog = top.dialog.get(window);
 			} catch (e) {
 				return;
 			}
 			
			$("#search").css("padding","4px");
 			
		   $("#undoBtn").click(function(){//关闭弹窗 取消
			   dialog.close("");
		   });
		   $("#subBtn").click(function(){//关闭弹窗 提交
		        
		        var memberIDs=[];
		        var memberNames=[];
		        $.each(item,function(i,n){
					memberIDs.push(i);
					memberNames.push(n);
				})
			    var userIds = memberIDs.join(",");
			    var userNames=memberNames.join(",");
		        parent.frames["main"].document.getElementById("actId").value = "<%=actId%>";
		        parent.frames["main"].document.getElementById("masterDataSource").value = "<%=master%>";
		        parent.frames["main"].document.getElementById("toNode").value = "${param.toNode}";
				
				parent.frames["main"].document.getElementById("slectsUserIds").value = userIds;
				//工作安排表单中需要的人员name
				
				try{
					parent.frames["main"].document.getElementById("slectsUserNames").value =userNames;
				}catch(e){
				
				}
				AJAX(3);

		   });
		 });
		 
		 var basePath = "<%=basePath%>";
		 
		 function search(){
		 
		 	if($("#search").is(":hidden")==true){
		 		$("#search").show();
		 	}else{
		 		$("#search").hide();
		 	}

		 }

         </script>	
	</body>
</html>
