<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
<%
	String path = request.getContextPath();
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
 		<%@ include file="../../../resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
		<link rel="stylesheet" href="<%=basePath%>resources/styles/layout-default-latest.css"/>
		<style type="text/css">
			.ui-layout-pane{padding:0;border-radius:5px;-webkit-border-radius:5px;-moz-border-radius:5px}
			.ui-layout-pane-north{border:none;}
			.ui-layout-resizer-open{background:#fff;}
			.ui-layout-resizer-hover{background:#F3FBFF;}
		</style>
	</head>
	<body id="main">
		<div class="row ui-layout-north" id="breadcrumb">
			<ul class="breadcrumb">
	          <li><i class="icon-location-arrow icon-muted"></i></li>
	          <li class="active">系统编辑</li>
	        </ul>
		</div>
		<div class="panel ui-layout-west">
			<div class="panel-heading">${vo.sysName}</div>
			<div class="panel-body">
				<ul id="tree1" class="ztree"></ul>
			</div>
		</div>
		<iframe class="ui-layout-center" src="<%=basePath%>fd/list.do" name="sysEditFrame" id="sysEditFrame" scrolling="auto" frameborder="0" width="100%"></iframe>
		
		<%@ include file="../../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.layout-latest.js"></script>
        <script type="text/javascript">
         	var setting = {
	   			view: {
	   				showLine: false,
	   				showIcon: false//showIconForTree
	   			},
	   			data: {
	   				simpleData: {
	   					enable: true
	   				}
	   			}
	   		};
         	var tNodes1 =[
				{ id:1, pId:0, name:"系统信息编辑",url:"<%=basePath%>dev/punSystemGet.do?boxs=${sysId}",target:"sysEditFrame"},
				//{ id:2, pId:0, name:"系统数据源管理",url:"<%=basePath%>dataSys/config.do?id=${sysId}",target:"sysEditFrame"},
				{ id:3, pId:0, name:"菜单管理",url:"<%=basePath%>dev/punSysMenuEdit.do?sysId=${sysId}",target:"sysEditFrame"},
				{ id:4, pId:0, name:"角色管理",url:"<%=basePath%>unit/listRolesInSys.do?boxs=${sysId}",target:"sysEditFrame"},
				{ id:5, pId:0, name:"元数据管理",url:"<%=basePath%>metaModel/selectPage.do",target:"sysEditFrame"},
				//{ id:6, pId:0, name:"数据源管理",url:"<%=basePath%>dataSourceManage/selectPage.do",target:"sysEditFrame"},
				{ id:7, pId:0, name:"动态页面管理",url:"<%=basePath%>fd/list.do",target:"sysEditFrame",open:true},
				{ id:71, pId:7, name:"样式库管理",url:"<%=basePath%>fd/style/list.do",target:"sysEditFrame"},
				{ id:72, pId:7, name:"页面动作库管理",url:"<%=basePath%>fd/act/list.do",target:"sysEditFrame"},
				{ id:73, pId:7, name:"校验库管理",url:"<%=basePath%>fd/validator/list.do",target:"sysEditFrame"},
				{ id:74, pId:7, name:"动态页面模版管理",url:"<%=basePath%>pfmTemplateController/pageList.do",target:"sysEditFrame"},
				{ id:75, pId:7, name:"打印管理",url:"<%=basePath%>formdesigner/page/print/printList.jsp",target:"sysEditFrame"},
				{ id:75, pId:7, name:"函数库管理",url:"<%=basePath%>func/list.do",target:"sysEditFrame"},
				{ id:91, pId:7, name:"页面绑定管理",url:"<%=basePath%>common/user/list-bind.do",target:"sysEditFrame"},
				{ id:92, pId:7, name:"页面接口管理",url:"<%=basePath%>document/view.do?dynamicPageId=1",target:"sysEditFrame"},
				{ id:93, pId:7, name:"页面模块管理",url:"<%=basePath%>document/view.do?dynamicPageId=4",target:"sysEditFrame"},
				{ id:94, pId:7, name:"报表管理",url:"<%=basePath%>document/view.do?dynamicPageId=21",target:"sysEditFrame"},				
				{ id:8, pId:0, name:"流程管理",url:null,target:"sysEditFrame",open:true},
				{ id:81, pId:8, name:"流程设计器",url:"<%=basePath%>WF/Admin/xap/Designer.jsp",target:"_blank",open:true},
				{ id:9, pId:0, name:"调试日志",url:"<%=basePath%>debug/view.do",target:"sysEditFrame"}
				//{ id:81, pId:8, name:"流程分类",url:"<%=basePath%>workflow/bpm/bpm-category-list.do",target:"sysEditFrame"},
				//{ id:82, pId:8, name:"自动委托",url:"<%=basePath%>workflow/bpm/delegate-listMyDelegateInfo.do",target:"sysEditFrame"},
				//{ id:85, pId:8, name:"流程管理旧",url:"<%=basePath%>workflow/bpm/console-listProcessDefinitions-old.do",target:"sysEditFrame"},
				//{ id:73, pId:7, name:"流程管理",url:"<%=basePath%>workflow/bpm/bpm-process-list.do",target:"sysEditFrame"},
				//{ id:84, pId:8, name:"运行管理",url:"<%=basePath%>workflow/bpm/console-listProcessInstances.do",target:"sysEditFrame"},
    		];
	        var myLayout;
	        $(function(){
	        	myLayout = $('body').layout({
	    			west__size:					240
	    		,	west__spacing_closed:		20
	    		,	west__togglerLength_closed:	100
	    		,	west__togglerAlign_closed:	"middle"
	    		,	west__togglerContent_closed:"菜单树"
	    		,	west__togglerTip_closed:	"打开并固定菜单"
	    		,	west__sliderTip:			"Slide Open Menu"
	    		,	west__slideTrigger_open:	"mouseover"
	    		,	north__closeable: false
	    		,	north__resizable:false
	    		,	north__spacing_open:0
	    		,	center__maskContents:		true // IMPORTANT - enable iframe masking
	    		
	    		});

        	 	var count=0;//默认选择行数为0
	   		  	$('table.datatable').datatable({
	   			  	checkable: true,
	   			  	checksChanged:function(event){
	   				  	this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
	   				  	var checkArray = event.checks.checks;
	   				  	count = checkArray.length;//checkbox checked数量
	   				  	for(var i=0;i<count;i++){//给隐藏数据机上name属性
	   					  	this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
	   				  	}
	   			  	}				
	   		  	});
        	 
	   			$.fn.zTree.init($("#tree1"), setting, tNodes1);	   			   		
        	});
        </script>         
	</body>
</html>
