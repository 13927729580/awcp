<#-----带列表片段页面的主页面模板------>
<#include "templates/parseFunction.ftl">
<#noparse>
<#assign path = request.getContextPath()>
<#assign basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/">
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
 		
 	<link rel="stylesheet" href="${basePath}resources/plugins/zui/dist/css/zui.min.css"/>
	<link rel="stylesheet" href="${basePath}resources/styles/main.css"/>
	<link rel="stylesheet" href="${basePath}resources/plugins/artDialog/css/ui-dialog.css"/>
	<link rel="stylesheet" href="${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
	<!--[if lt IE 9]>
	  <script src="${basePath}resources/plugins/zui/assets/html5shiv.js"></script>
	  <script src="${basePath}resources/plugins/zui/assets/respond.js"></script>
	<![endif]-->	
</head>
<body  id="main">
    	 <div class="row" id="breadcrumb">
			<ul class="breadcrumb">
	          <li><i class="icon-location-arrow icon-muted"></i></li>
	          <li><a href="#">首页</a></li>
	          <li><a href="#">组织机构与权限</a></li>
	          <li class="active">菜单编辑</li>
	        </ul>
		</div> 
		<div class="row" id="buttons">
</#noparse>
		<#--	 后续会有权限 
		<#list pageActs as act>
				
				<#if true>
					${act.html}
				</#if>
			</#list>
		-->	
		
			<#if pageActs??>
				<#list pageActs?sort_by("order") as act>
					<#noparse><@shiro.hasPermission name="11111:</#noparse>${(pageActPermission[act.getPageId()])!""}<#noparse>"></#noparse>
						<@convertButton act />
					<#noparse></@shiro.hasPermission></#noparse>
				</#list>
			</#if>
		
<#noparse>
		</div>
		<div class="row" id="dataform">
			<form class="form-horizontal form-condensed" id="groupForm" action="${basePath}document/excute.do" role="customForm" method="post">
				<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
				<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
				<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
				<input type="hidden" name="instanceId" value="${ (vo.instanceId)!""}" id="instanceId"/>
				<input type="hidden" name="taskId" value="${ (vo.taskId)!""}" id="taskId"/>
				<input type="hidden" name="nodeId" value="${ (vo.nodeId)!""}" id="nodeId"/>
				<input type="hidden" name="actId" value="${ (vo.actId)!""}" id="actId"/>
				<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
				<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
</#noparse>
			<#-- ${layout} 最终目标以layout为准
				<#list components as c>
					<#if c_index%2 == 0>
						<div class="form-group">
							<div class="col-md-2">
								${c.html}
							</div>
					</#if>
					<#if c_index%2 == 1>
							<div class="col-md-4">
								${c.html}
							</div>
						</div>
					</#if>
					<#if !c_has_next && c_index%2 == 0>
						</div>
					</#if>
				</#list>
			 -->
			 <#if layouts??>
			 	<#if components??>
			 		<#list layouts?sort_by("order") as layout>
			 			<@parseLayout layout components/>
			 		</#list> 
			 	</#if>
			 </#if>
<#noparse>

			</form>
		</div>
	<script type="text/javascript" src="${basePath}resources/scripts/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/zui/dist/js/zui.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/artDialog/dist/dialog-plus-min.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/tips/jquery.poshytip.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/common.js"></script>
	
	<script type="text/javascript" src="${basePath}resources/scripts/platform.document.js"></script>
</#noparse>	
	<script type="text/javascript">
		$(document).ready(function(){
		
			var count=0;
		  	$('table.datatable').datatable({
			  	checkable: true,
			  	storage:false,
			  	checksChanged:function(event){
				  	this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
				  	var checkArray = event.checks.checks;
				  	count = checkArray.length;
				  	for(var i=0;i<count;i++){
					  	this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","_selects");
				  	}
			  	}
		  	});
			
			<#if components??>
			 		 <#if components?keys??>
				 		  <#list components?keys as layoutId>
					 			<#if components[layoutId]??>
					 			<#list components[layoutId] as component>
					 				<@convertComponentScript component />
					 			</#list>
					 			</#if>
				 		  </#list>
			 		</#if> 
			</#if>
			
			$.formValidator.initConfig({
				formID : "groupForm",
				debug : false,
				onSuccess : function() {
					
				},
				onError : function() {
					alert("错误，请看提示")
				}
			});
			<#if components??>
			 		 <#if components?keys??>
				 		  <#list components?keys as layoutId>
					 			<#if components[layoutId]??>
						 			<#list components[layoutId] as component>
						 			<#if component['componentType']!='1012' && component['componentType']!='1009'>		
						 				<#if valdatorsMap[component['pageId']]?? || (component['validateAllowNull']?? && component['validateAllowNull']?number==0)>
						 					$('#${(component['pageId'])!""}_${(component['name'])!""}').formValidator({onFocus:"${component['validateInputTip']}",onCorrect:"${component['validateErrorTip']}",
						 						
						 					})
						 					<#if (component['validateAllowNull']?number ==0) >
						 						.inputValidator({min:1,empty:{leftEmpty:true,rightEmpty:true,emptyError:""},onError:"Please input the content"})
       										</#if>
						 					<#if valdatorsMap[component['pageId']]??>
							 					<#list valdatorsMap[component['pageId']] as v>
							 						<@validate v/>
							 					</#list>;
							 				</#if>
						 				</#if>	
						 			</#if>
						 			</#list>
					 			</#if>
				 		  </#list>
			 		</#if> 
			</#if>
		
		
		<#if components??>
			 		 <#if components?keys??>
				 		  <#list components?keys as layoutId>
					 			<#if components[layoutId]??>
					 			<#list components[layoutId] as component>
					 				<#if component['pageValidate']??>
					 					${component['pageValidate']}
					 				</#if>
					 			</#list>
					 			</#if>
				 		  </#list>
			 		</#if> 
		</#if>
		<#if page??>
			<#if page.getAfterLoadScript()??>
				${page.getAfterLoadScript()}
			</#if>
		</#if>
		});	
		<#list pageActs?sort_by("order") as act>
			<#if true>
				<@convertScript act />
			</#if>
		</#list>
		
		<#if components??>
			 		 <#if components?keys??>
				 		  <#list components?keys as layoutId>
					 			<#if components[layoutId]??>
					 			<#list components[layoutId] as component>
					 				<#if component['pageActScript']??>
					 					${component['pageActScript']}
					 				</#if>
					 			</#list>
					 			</#if>
				 		  </#list>
			 		</#if> 
		</#if>
		
		<#--<#list components as c>
			<#if true>
				${c.script}
			</#if>
		</#list>
		 -->
	</script>
</body>
</html>