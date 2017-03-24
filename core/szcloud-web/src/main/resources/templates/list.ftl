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
	<link rel="stylesheet" href="${basePath}resources/plugins/zui/dist/css/zui.min.css"/>
	<!--[if lt IE 9]>
	  <script src="${basePath}resour
	  ces/plugins/zui/assets/html5shiv.js"></script>
	  <script src="${basePath}resources/plugins/zui/assets/respond.js"></script>
	<![endif]-->	
</head>
<body  id="main">
    <div class="container-fluid">
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
			<#if pageActs??>
				<#list pageActs?sort_by("order") as act>
					<#noparse><@shiro.hasPermission name="11111:</#noparse>${(pageActPermission[act.getPageId()])!""}<#noparse>"></#noparse>
						<@convertButton act />
					<#noparse></@shiro.hasPermission></#noparse>
				</#list>
			</#if>
<#noparse>
		</div>
		
		<div class="row" id="datatable">
				<form class="form-horizontal" id="groupForm" action="${basePath}document/excute.do" method="post">	
	        		<input type="hidden" name="currentPage" value="${currentPage!"0"}">	
	        		<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
					<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
					<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
					<input type="hidden" name="actId" value="" id="actId"/>
					<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
					<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
					<table class="table datatable table-bordered">
						<thead>
							<tr>
								<th class="hidden"></th>
</#noparse>
					<#if components?? >
						 <#list components?sort_by("order") as c>
							<@parseColumn c />
						</#list>
					</#if>
<#noparse>
							</tr>
						</thead>
						<tbody>
		            	<#if </#noparse>${dataAlias[0]}<#noparse>_list?? >	
		            	 	<#list
</#noparse>
		            	 		${dataAlias[0]}_list as ${dataAlias[0]}>
<#noparse> 
								<tr>
								<td class="hidden formData"><input id="boxs" type="hidden"
								value="${</#noparse>${dataAlias[0]}<#noparse>.id}"> 	
</#noparse>				
						<#if components?? >
							 <#list components?sort_by("order") as c>
							 
								<@parseColumnData c />
							</#list>
						</#if>
<#noparse>				
								</tr>
							</#list>
						</#if>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</#noparse>
<#noparse>	
	<script type="text/javascript" src="${basePath}resources/scripts/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/zui/dist/js/zui.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/artDialog/dist/dialog-plus-min.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/tips/jquery.poshytip.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/common.js"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/platform.document.js"></script>
	<script type="text/javascript">
			$(document).ready(function(){
			var count=0;
		  	$('table.datatable').datatable({
			  	checkable: true,
			  	checksChanged:function(event){
				  	this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
				  	var checkArray = event.checks.checks;
				  	count = checkArray.length;
				  	for(var i=0;i<count;i++){
					  	this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","_selects");
				  	}
			  	}
		  	});
</#noparse>
		<#list pageActs?sort_by("order") as act>
			<#if true>
				<@convertScript act />
			</#if>
		</#list>  
<#noparse>
		});
</#noparse>		  
		
	</script>
</body>
</html>