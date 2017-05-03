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
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>动态表单编辑页面</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %>
		<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button class="btn btn-success" id="savePage" ><i class="icon-save"></i>保存</button>
				<button class="btn btn-info " id="export"><i class="icon-zoom-out"></i>预览</button>
				<button class="btn btn-warning" id="publishBtn"><i class="icon-edit"></i>发布</button>				
				<button class="btn btn-info " id="relation"><i class="icon-zoom-out"></i>引用关系</button>
				<button class="btn btn-info " id="catTemplate"><i class="icon-zoom-out"></i>查看发布后内容</button>
				<button class="btn btn-info " id="sysKeyWords"><i class="icon-zoom-out"></i>系统关键字</button>
				<button class="btn btn-warning" id="ret"><i class="icon-backward"></i>返回</button>
			</div>
			<c:if test="${result!=null && ''!=result}">
				<span style="color: red">(${result})</span>
			</c:if>
			<form class="form-horizontal" id="groupForm" action="<%=basePath%>fd/save.do" method="post">
				<div class="row" id="tab">
					<ul class="nav nav-tabs"><!-- 标签页nav里面的 href的值对应的是 tab-content里面的tab-pane的id值 -->
						<li class="active"><a href="#basicInfo" data-toggle="tab">基本</a></li>
						<li class=""><a href="#datasource" data-toggle="tab">数据源</a></li>
						<c:if test="${ not empty vo.id }">
							<li class=""><a href="#componentConfig" data-toggle="tab">组件</a></li>
						</c:if>
						<c:if test="${ not empty vo.id }">
							<li class=""><a href="#actConfig" data-toggle="tab">动作</a></li>
						</c:if>
						<c:if test="${ not empty vo.id }">
							<li class=""><a href="#workflow" data-toggle="tab">流程</a></li>
						</c:if>
						<c:if test="${ not empty vo.id }">
							<li class=""><a href="#locLayout" data-toggle="tab">布局</a></li>
						</c:if>
						<c:if test="${ not empty vo.id }">
							<li class=""><a href="<%=basePath%>layout/turnToLayoutComponent.do?dynamicPageId=${vo.id}"  target="_blank">组件和布局</a></li>
						</c:if>
						<c:if test="${ not empty vo.id }">
							<li class=""><a href="#authority" data-toggle="tab">权限组配置</a></li>
						</c:if>
					</ul>
				</div>			
				<div class="row tab-content">
					<div class="tab-pane active" id="basicInfo"><%@ include file="tabs/basicInfo.jsp" %></div>
					<div class="tab-pane" id="datasource"><%@ include file="tabs/datasource.jsp" %></div>
					<c:if test="${ not empty vo.id }">
						<div class="tab-pane" id="componentConfig"><%@ include file="tabs/component.jsp" %></div> 
					</c:if>
					<c:if test="${ not empty vo.id }">
						<div class="tab-pane" id="actConfig"><%@ include file="tabs/act.jsp" %></div>
					</c:if>
				 	 <c:if test="${ not empty vo.id }">
				    	<div class="tab-pane" id="workflow"><jsp:include page="/fd/workflow/list.do?pageId=${vo.id}"/></div> 
				    </c:if> 
					<c:if test="${ not empty vo.id }">
						<div class="tab-pane" id="locLayout"><jsp:include page="tabs/layoutList.jsp"/></div> 
					</c:if>
					<c:if test="${ not empty vo.id }">
						<div class="tab-pane" id="authority"><jsp:include page="tabs/authorityGroupList.jsp"/></div> 
					</c:if>
				</div>
			</form>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/dynamicpage.constant.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/form.dpcommon.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/form.cpcommons.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/map.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/page/script/act.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/layout.js"></script>
		<c:if test="${ not empty vo.id }">
			<script type="text/javascript" src="<%=basePath%>formdesigner/page/workflow/workflow.js"></script>
		</c:if>
		<script type="text/javascript" src="<%=basePath%>formdesigner/page/style/style.js"></script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/page/script/data.js"></script>
		<script type="text/javascript" >
			var basePath = "<%=basePath%>";
		</script>
		<script type="text/javascript" src="<%=basePath%>formdesigner/page/tabs/authorityGroup.js"></script>
		<script type="text/javascript" >//自己按需求可以提取单独的js文件 存放与resources/scripts文件夹下 命名规则：form.文件名.js
			function addAct(type){
				act_add(type);
			}
		
			//显示或隐藏列表页面所需参数
			function showListConfig(){
				var pType = $("#pageType").val();
				if(pType=='1003'){
					$(".listPageConfig").show();
				}else{
					$(".listPageConfig").hide();
				}
			} 
			
			$(document).ready(function() {
				var id = '${vo.id}';
				var tips = "${tips}";
				if(tips != ""){
					alert(tips);
				}
				$.ajax({
					type:"POST",
					url:"fd/validateCheckOut.do",
					data:{id:id},
					async:false,
					dataType:'json',
					//回调函数
					success:function(result){
						if(result.value != 1){
							$(":input").prop("disabled", true);
							$("a").prop("href","javascript:void(0)");
							$("a").prop("target","_self");
						}   	
					}
				});
				
				$("#pageType").select2();
				initStyle();
				initAct();
				initDataSource();
				initTemplate();
				$("#templateId").select2();
				var dynamicPageId='${vo.id}';
				loadComponentTable(dynamicPageId);
				loadLayoutTable(dynamicPageId);
				
				$("#pageType").change(function(){
					showListConfig();
				});
				showListConfig();
				
				$("#savePage").click(function(){
					var id = '${vo.id}';
					$.ajax({
						type:"POST",
						url:"fd/validateCheckOut.do",
						data:{id:id},
						async:false,
						dataType:'json',
						success:function(result){
							if(result.value == 1){
								$("#groupForm").submit();
							}else{
								alert(result.value);
							}	   	
						},
				  	 	error: function (XMLHttpRequest, textStatus, errorThrown) { 
		           	    	alert(errorThrown); 
				   	 	}
					});
				});
				
				$("#deleteComponent").click(function(){
					var count = $(":checkbox[name='component']:checked").size();
					if(count > 0){
						var dynamicPageId = '${vo.id}';
						edit("component","remove",null,null,dynamicPageId);
						return false;
					}else{
						alert("请选择组件操作");
						return false;
					}
				});
				
				$("#ret").click(function(){	
					window.open("<%=basePath%>fd/list.do?currentPage=0","_self");
				});
				
				$("#export").click(function(){	
					top.dialog({ id: 'add-dialog' + Math.ceil(Math.random()*10000),
						title: '页面预览',
						url:'<%=basePath%>document/view.do?dynamicPageId='+'${vo.id}',
						width:1024,
						height:768,
						okValue:'确定',
						ok:function(){},
						cancelValue:'取消',
						cancel:function(){}
					}).showModal();
					return false;
				});
				
				$("#publishBtn").click(function(){
		    		if(window.confirm("确定发布？")){
		        		$("#groupForm").attr("action","<%=basePath%>fd/publishOnePage.do").submit();
		    		}
					return false;
		    	});
		    	
		    	$("#sysKeyWords").click(function(){
					top.dialog({ 
						id: 'keywords-dialog' + Math.ceil(Math.random()*1000000),
						title: '系统关键字',
						url: basePath + "formdesigner/page/component/keywords-list.jsp?operation=1",
						height:900,
						width:500,
						onclose : function() {
						}
					}).show();
				});
			});
			
			function initTemplate(){
				//发送ajax请求
				$.ajax({
					type:"GET",
					url:"pfmTemplateController/getAllByAjax.do",
					async:false,
					//回调函数
					success:function(jsonData){
						var json = eval(jsonData);
						$.each(json,function(idx,item){
							var templateId = $("#templateId").val();
							if(item.id==templateId){
								$("#templateId").append("<option value=\""+item.id+"\" selected='selected'>"+item.fileName+"</option>");
							}else{
								$("#templateId").append("<option value=\""+item.id+"\">"+item.fileName+"</option>");
							}
						});
					},
				    error: function (XMLHttpRequest, textStatus, errorThrown) { 
		                  alert(errorThrown); 
				    }
				});
			}
			
			$("#relation").click(function(){
	    		var id = '${vo.id}';
				top.dialog({ id: 'add-dialog' + Math.ceil(Math.random()*10000),
					title: '页面引用关系',
					url:'<%=basePath%>fd/relation.do?_select='+id,
					width:500,
					height:500,
					okValue:'确定',
					ok:function(){},
					cancelValue:'取消',
					cancel:function(){}
				}).showModal();
				return false;
			});
			
			$("#catTemplate").click(function(){
	    		var id = '${vo.id}';
				top.dialog({ 
					id: 'add-dialog' + Math.ceil(Math.random()*10000),
					title: '页面发布后的模版内容',
					url:'<%=basePath%>fd/catTemplate.do?_select='+id,
					width:800,
					height:600,
					okValue:'确定',
					ok:function(){},
					cancelValue:'取消',
					cancel:function(){}
				}).showModal();
				return false;
			});
         </script>
	</body>
</html>
