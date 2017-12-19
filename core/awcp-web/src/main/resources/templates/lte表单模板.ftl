<#include "templates/parseAdminLTE.ftl">
<#noparse>
<#assign path = request.getContextPath()>
<#assign basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/">
</#noparse>
<#list dataAlias as dataAlia>
	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>
             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!''>
        </#if></#noparse>
</#list>
<#noparse>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<link rel="stylesheet" href="${basePath}template/AdminLTE/css/bootstrap.min.css">
	<link rel="stylesheet" href="${basePath}template/AdminLTE/css/font-awesome.min.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/bootstrapValidator.css">
	<link rel="stylesheet" href="${basePath}template/AdminLTE/css/ionicons.min.css">
	<script>
		var basePath = '${basePath!''}';
		var cssFile={},scriptFile={};
		//引入组件样式表
	</#noparse>
	<#if components??>
			<#if components?keys??>
				<#list components?keys as layoutId>
					<#if components[layoutId]??>
					<#list components[layoutId] as component>
						<@parseComponentCSSFile component />
					</#list>
					</#if>
				</#list>
			</#if> 
	</#if>
	<#noparse>
	for(var e in cssFile){document.write('<link rel="stylesheet" href="'+e+'">')}
	cssFile=null;
	</script>
	
	<link rel="stylesheet" href="${basePath}template/AdminLTE/css/AdminLTE.css">
	<link rel="stylesheet" href="${basePath}template/AdminLTE/css/main.css">
	<!--[if lt IE 9]>
	  <script src="${basePath}resources/plugins/zui/assets/html5shiv.js"></script>
	  <script src="${basePath}resources/plugins/zui/assets/respond.js"></script>
        
	<![endif]-->
</head>
<body>
    <div class="content"> 
		<div class="row opeBtnGrop" id="buttons">
			<div class="col-md-12">
</#noparse>
				<#if pageActs??>
					<#list pageActs?sort_by("order") as act>
						<@convertButton act />
					</#list>
				</#if>
			</div>
		</div>
		<div class="row" id="newTitle"></div>
<#noparse>
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<form class="form-horizontal form-condensed" id="groupForm" action="${basePath}workflow/wf/excute.do" role="customForm" method="post">
						<div class="box-body">
							<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
							<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
							<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
							<input type="hidden" name="instanceId" value="${ (vo.instanceId)!""}" id="instanceId"/>
							<input type="hidden" name="taskId" value="${ (vo.taskId)!""}" id="taskId"/>
							<input type="hidden" name="nodeId" value="${ (vo.nodeId)!""}" id="nodeId"/>
							<input type="hidden" name="actId" value="${ (vo.actId)!""}" id="actId"/>
							<input type="hidden" name="WorkID" value="${ (vo.workItemId)!""}" id="WorkID"/>
							<input type="hidden" name="FK_Node" value="${ (vo.entryId)!""}" id="FK_Node"/>
							<input type="hidden" name="FK_Flow" value="${ (vo.flowTempleteId)!""}" id="FK_Flow"/>
							<input type="hidden" name="FID" value="${ (vo.fid)!""}" id="FID"/>
							<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
							<input type="hidden" name="toNode" value="" id="toNode">
							<input type="hidden" name="IsRead" value="${(IsRead)!""}" id="IsRead">
							<input type="hidden" name="slectsUserIds" value="${ (vo.slectsUserIds)!""}" id="slectsUserIds"/>
							<input type="hidden" name="slectsUserNames" value="${ (vo.slectsUserNames)!""}" id="slectsUserNames"/>                       
							<input type="hidden" name="masterDataSource" value="" id="masterDataSource"/>
							<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
			</#noparse>
							
								<#if layouts??>
									<#if components??>
										<#list layouts?sort_by("order") as layout>
											<@parseLayout layout components/>
										</#list> 
									</#if>
								</#if>
			<#noparse>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<script src="${basePath}resources/JqEdition/jquery-1.10.2.js"></script>
	<script src="${basePath}venson/js/common.js"></script>
	<script src="${basePath}template/AdminLTE/js/common.js"></script>
	<script src="${basePath}template/AdminLTE/js/bootstrap.min.js"></script>
	<script src="${basePath}resources/scripts/jquery.serializejson.min.js"></script>
	<script src="${basePath}resources/scripts/common.js"></script>
	<script src="${basePath}resources/scripts/platform.document.js"></script>
	</#noparse>
	<script>
	//引入组件脚本
	<#if components??>
			<#if components?keys??>
				<#list components?keys as layoutId>
					<#if components[layoutId]??>
					<#list components[layoutId] as component>
						<@parseComponentScriptFile component />
					</#list>
					</#if>
				</#list>
			</#if> 
	</#if>

<#noparse>

	for(var e in scriptFile){document.write('<script src="'+e+'"><\/script>')}
	scriptFile=null;
	</script>
	<script>
</#noparse>		
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
		
		
	        <#if page??>
				<#if page.getAfterLoadScript()??>
					${page.getAfterLoadScript()}
				</#if>
			</#if>
			
			
			<#list pageActs?sort_by("order") as act>
				<#if true>
					<@convertScript act />
				</#if>
			</#list>
			
	</script>
</body>
</html>