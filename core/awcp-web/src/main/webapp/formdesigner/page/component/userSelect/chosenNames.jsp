<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
<%
    String datas = request.getParameter("datas");
	String isSingle = request.getParameter("isSingle");
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
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
		.select-panel .panel-body{height:354px;max-height:400px;}
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
	<input type="hidden" id="datas" value="<%=datas%>" />
	<input type="hidden" id="isSingle" value="<%=isSingle%>" />
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-3 col-xs-3">		
				<div class="panel tree-panel" style="width:270px">
		            <div class="panel-heading selectpanel">
		            	<ul class="nav nav-tabs clearfix" id="myTab">
    						<li class="active"><a href="#tab1" data-toggle="tab" onClick="searchDept()">部门</a></li>
    						<li><a href="#tab1" data-toggle="tab" onClick="searchRole()">角色</a></li>
    						<li><a href="#tab1" data-toggle="tab" onClick="searchJob()">岗位</a></li>								
						</ul>			               
		            </div>
		            <div class="panel-heading" id="search" style="display:block; background:none;">
                       	<form class="form-condensed">
                       		<div class="input-group">
                            	<input type="text" placeholder="请输入姓或名" id="wq" class="form-control">
                                <span class="input-group-btn"> <button class="btn btn-default" type="button" id="subeq" onclick="subSearch()"><i class="icon-search"></i></button> </span>
                            </div>
                        </form>
                  	</div>	
                  	 <div class="panel-body" id="tab1" style="min-height:320px;">
		            	<ul id="tree1" class="ztree"></ul>
		            </div>  			
				</div>	                                  
			</div>		
           	<div class="col-md-9 col-xs-9">
               	<div class="row">
					<div class="col-md-6 col-xs-6">
						<div class="panel select-panel">
							<div class="panel-heading">
				            	<div class="col-xs-5"  style="margin-top: 5px;">
					            	<div class="btn-group" style="width: 160px;">
					            		<button class="btn btn-xs" id="checkAll">全选</button>
					            		<button class="btn btn-xs" id="unCheckAll">全不选</button>
					            	</div>
				                </div>
				            	<div class="col-xs-7 input-group">
				            		<input type="text" class="form-control" placeholder="请输入姓名" name="memberName" id="memberName"/>
				                	<span class="input-group-btn"><button class="btn btn-default searchBtn" type="button" onclick="searchMember()">筛选</button></span>
				            	</div>
							</div>
							<div class="panel-body">
								<div class="tip">请点击左侧菜单</div>
							</div>
						</div>
					</div>           
					<div class="col-md-6 col-xs-6">
						<div class="panel member-panel">							
							<div class="panel-heading">已选择 <button class="btn btn-xs pull-right" id="deleteAll">全删除</button></div>
							<div class="panel-body" id="useUser">
								<div class="tip">请从中间选择框选择用户</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row checkbar">
					<div class="col-md-4  col-xs-4  text-left"></div>
					<div class="col-md-4  col-xs-4  text-right">
						<button type="button" class="btn btn-success" id="subBtn"><i class="icon-save"></i>确定</button>											
					</div>               			
				</div>               					
			</div>
		</div>
	</div>		
	<%@ include file="/resources/include/common_form_js.jsp" %>	
	<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.selectPanel.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/jsonpath-0.8.0.js"></script>
	<script type="text/javascript">		
		var basePath = "<%=basePath%>";
		var groupId = ${sessionScope.current_user.groupId};              
        $(function(){       
     		try {
 				var dialog = top.dialog.get(window);
 			} catch (e) {
 				return;
 			}
 				
		   	$("#undoBtn").click(function(){//关闭弹窗 取消
			   	dialog.close("");
		   	});
		   
		   	$("#subBtn").click(function(){//关闭弹窗 提交
			   	dialog.close(item);
		   	});
		});
		 
		$("#toMessage").click(function(){
        	$(this).toggleClass("btn-info btn-default");
        	$(this).find("i").toggleClass("icon-check icon-check-empty");
    	});
    	
    	$("#toOnline").click(function(){
        	$(this).toggleClass("btn-info btn-default");
        	$(this).find("i").toggleClass("icon-check icon-check-empty");
    	});
	</script>	
</body>
</html>
