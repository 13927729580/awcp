<#include "templates/parseFunction.ftl">
<#include "templates/pagerParserFunction.ftl">
<#noparse>
<#assign path = request.getContextPath()>
<#assign basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/">
</#noparse>
<#list dataAlias as dataAlia>
	<#noparse>
	<#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>
             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!''>
        </#if>
	</#noparse>
</#list>
<#noparse>
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
	<link rel="stylesheet" href="${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" />	
    <link rel="stylesheet" href="${basePath}resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css"/>
</head>
<body id="main">
    <div class="container-fluid">   	
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
		<div class="row" id="searchform">
			<div id="collapseButton" class="collapse">
				<form action="${basePath}document/view.do" id="createForm" class="clearfix">
					<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
					<input type="hidden" name="currentPage" value="${currentPage!"0"}" id="currentPage"/>
					<input type="hidden" name="fkId" value="${fkId!""}" id="fkId" />
					<input type="hidden" name="title" value="${title!""}" id="title" />					
				</form>
			</div>
		</div>
</#noparse>		
		<#if components?? >
			<#list components?sort_by("order") as c>				
				<#if c['componentType']=='1013' && (c['searchLocation']!'1')=='1'>
					<@parseComponent c />
				</#if>
			</#list>
		</#if>
		<div class="row" id="newTitle">
			<label class="control-label " style="width: 100%; text-align: center; font-weight: 800; font-size: 16px;">			
			</label>
		</div>
<#noparse>			
		<div class="row" id="datatable">
			<form class="form-horizontal" id="groupForm" action="${basePath}document/view.do" method="post">	
				<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
				<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
				<input type="hidden" name="currentPage" value="${currentPage!"0"}">	
	        	<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
				<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
				<input type="hidden" name="actId" value="" id="actId"/>
				<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
				<input type="hidden" name="orderBy" value="${ (vo.orderBy)!""}" id="orderBy"/>
				<input type="hidden" name="allowOrderBy" value="${ (vo.allowOrderBy)!"1"}" id="allowOrderBy"/>
				<input type="hidden" name="fkId" value="${fkId!""}" id="fkId" />
				<input type="hidden" name="title" value="${title!""}" id="title" />
				<table class="table datatable table-bordered sortable">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th data-width="70">No.</th>
</#noparse>
					<#if components?? >
						 <#list components?sort_by("order") as c>
						 	<#if c['componentType']!='1013' >
								<@parseColumn c />
							</#if>
						</#list>
					</#if>
<#noparse>
						</tr>
					</thead>
					<tbody id="dataList">
		            	<#if </#noparse>${dataAlias[0]}<#noparse>_list?? && </#noparse>${dataAlias[0]}<#noparse>_list?size gte 1>	
		            	 	<#list
</#noparse>
		            	 		${dataAlias[0]}_list as ${dataAlias[0]}>
<#noparse> 
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value="${</#noparse>${dataAlias[0]}<#noparse>.ID!''}"></td> 	
</#noparse>				
							<td><#noparse>${(</#noparse>${dataAlias[0]}_list_paginator.getTotalCount() - ${dataAlias[0]}_list_paginator.getPage() * ${dataAlias[0]}_list_paginator.getLimit() + ${dataAlias[0]}_list_paginator.getLimit() - ${dataAlias[0]}_index <#noparse>)?string('0000')}</#noparse></td>
						<#if components?? >
							 <#list components?sort_by("order") as c>
							 	<#if c['componentType']!='1013' >
									<@parseColumnData c />
								</#if>
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
</#noparse>	
		<#if components?? >
			<#list components?sort_by("order") as c>
				<#if c['componentType']=='1013' && (c['searchLocation']??&&c['searchLocation']=='2')>
					<@parseComponent c />
				</#if>
			</#list>
		</#if>
		<div class="row navbar-fixed-bottom text-center" id="pagers">	
			<@pageNavigation  dataAlias[0] />
<#noparse>
		</div>
	</div>
</#noparse>
<#noparse>	
	<script src="${basePath}resources/scripts/jquery-1.10.2.js"></script>
	<script src="${basePath}resources/plugins/zui/dist/js/zui.js"></script>
	<script src="${basePath}resources/plugins/artDialog/dist/dialog-plus-min.js"></script>
	<script src="${basePath}resources/plugins/tips/jquery.poshytip.js"></script>
	<script src="${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" charset="UTF-8"></script>
	<script src="${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js" charset="UTF-8"></script>
    <script src="${basePath}resources/scripts/jquery.serializejson.min.js"></script>
	<script src="${basePath}resources/scripts/common.js"></script>
	<script src="${basePath}resources/scripts/platform.document.js"></script>
	<script src="${basePath}resources/scripts/pageTurn.js"></script>
	<script src="${basePath}resources/scripts/platform.document.orderBy.js"></script>
	<script src="${basePath}resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
	<script type="text/javascript">
		var basePath = '${basePath}';
		$(document).ready(function(){
			var title = $("#title").val();
			if(title){
				$("div#newTitle label").text(title);
			}
			else{
				$("div#newTitle").remove();
			}
			
			
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
			
			$(".input-group .input-group-addon").each(function(item){
				 $(this).removeClass("fix-border").removeClass("fix-padding");
			});
			
			$('.form-date').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				format : 'yyyy-mm-dd'
			});
			
			$('.form-english-date').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				format : 'dd/mm/yyyy'
			});
			
			$('.form-english-date-long').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'dd/mm/yyyy hh:ii'
			});
			
			$('.form-date').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				format : 'yyyy-mm-dd'
			});
			
			$('.form-datetime').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				forceParse : 0,
				showMeridian : 1,
				format : 'yyyy-mm-dd hh:ii'
			});
			
			$('.form-time').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'hh:ii'
			});
			
			$('.form-date-long-medium').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'yyyy-mm-dd hh:ii'
			});
			
			$('.form-date-long-long').datetimepicker({
				language : 'en',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				maxView : 1,
				forceParse : 0,
				format : 'yyyy-mm-dd hh:ii:ss'
			});
                                        
			initOrderBy();
			
			$.formValidator.initConfig({			
				debug : false,
				onSuccess : function() {
					
				},
				onError : function() {
					alert("Error.")
				}
			});
</#noparse>
		<#if components?? >
			<#list components?sort_by("order") as c>
				<#if c['componentType']=='1013' >
					<@convertContainPageScript c />
				</#if>
			</#list>
		</#if>
		
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