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
	<link rel="stylesheet" href="${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/select2/select2.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css">
	<!--[if lt IE 9]>
	  <script src="${basePath}resources/plugins/zui/assets/html5shiv.js"></script>
	  <script src="${basePath}resources/plugins/zui/assets/respond.js"></script>
	<![endif]-->	       
</head>
<body  id="main">
    <div class="container-fluid">   	
		<div class="row" id="buttons">
</#noparse>				
		<#if pageActs??>
			<#list pageActs?sort_by("order") as act>					
					<@convertButton act />
			</#list>
		</#if>				
<#noparse>
		</div>
</#noparse>		
		<#if components?? >
			<#list components?sort_by("order") as c>				
				<#if c['componentType']=='1013' && (c['searchLocation']!'1')=='1'>
					<@parseComponent c />
				</#if>
			</#list>
		</#if>
	
<#noparse>			
		<div class="row" id="datatable">
			<form class="form-horizontal" id="groupForm" action="${basePath}document/view.do" method="post">	
	        	<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
<input type="hidden" name="currentPage" value="${currentPage!"0"}">
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
				<input type="hidden" name="message" value="${(message)!""}" id="message">
				<input type="hidden" name="suggestion" value="${(suggestion)!""}" id="suggestion"/>
				<input type="hidden" name="commonType" value="${(commonType)!""}" id="commonType"/>
				<input type="hidden" name="suggestionType" value="${(suggestionType)!""}" id="suggestionType"/>
				<input type="hidden" name="mainTextId" value="${(mainTextId)!""}" id="mainTextId"/>
				<input type="hidden" name="slectsUserIds" value="${ (vo.slectsUserIds)!""}" id="slectsUserIds"/>
                <input type="hidden" name="slectsUserNames" value="${ (vo.slectsUserNames)!""}" id="slectsUserNames"/>                       
                <input type="hidden" name="masterDataSource" value="" id="masterDataSource"/>
				<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
				<table class="table datatable table-bordered sortable">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th data-width="70">序号</th>
</#noparse>
					<#if components?? >
						<#list components?sort_by("order") as c>
							<#if c['componentType']=='1036'>
								<@convertAddSearch c />
							</#if>
						</#list>
					</#if>
					<#if components?? >
						 <#list components?sort_by("order") as c>
							<#if c['componentType']!='1013'&&c['componentType']!='1036' >
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
						<td><#noparse>${(</#noparse>${dataAlias[0]}_list_paginator.getPage() * ${dataAlias[0]}_list_paginator.getLimit() - ${dataAlias[0]}_list_paginator.getLimit() + ${dataAlias[0]}_index + 1<#noparse>)?string('0000')}</#noparse></td>
						<#if components?? >
							 <#list components?sort_by("order") as c>
								<#if c['componentType']!='1013'&&c['componentType']!='1036' >
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
	<script type="text/javascript" src="${basePath}resources/JqEdition/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="${basePath}base/resources/zui/dist/js/zui.js"></script>
	<script type="text/javascript" src="${basePath}base/resources/artDialog/dist/dialog-plus-min.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/tips/jquery.poshytip.js"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" charset="UTF-8"></script>
	<script type="text/javascript" src="${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${basePath}resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/common.js"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/platform.document.js"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/pageTurn.js"></script>
	<script type="text/javascript" src="${basePath}resources/scripts/platform.document.orderBy.js"></script>
	<script src="${basePath}template/AdminLTE/js/select2/select2.full.js"></script>
	<script src="${basePath}template/AdminLTE/js/select2/zh-CN.js"></script>
	<script src="${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js"></script>
	<script src="${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src='${basePath}venson/js/common.js'></script>
	<script type="text/javascript" src='${basePath}venson/js/addSearch.js'></script>
	<script type="text/javascript">
		var basePath = '${basePath}';
		$(document).ready(function(){
			var nodeID=document.getElementById("FK_Node").value;
			if(nodeID=='3002'){
			var workID=document.getElementById("WorkID").value;
			var FID=document.getElementById("FID").value;
			var Emp=document.getElementById("88e6f232-6dff-45e5-94e8-31e0fb8ad107").value;
			parent.frames["main"].location.href=basePath+"WF/MyFlow.jsp?PFlowNo=030&PNodeID=3002&PEmp="+Emp+"&PFID="+FID+"&PWorkID="+workID+"&FK_Flow=031&FK_Node=3101&T=";
			}
			if(nodeID=='3003'){
			var workID=document.getElementById("WorkID").value;
			var FID=document.getElementById("FID").value;
			var Emp=document.getElementById("88e6f232-6dff-45e5-94e8-31e0fb8ad107").value;
			parent.frames["main"].location.href=basePath+"WF/MyFlow.jsp?PFlowNo=030&PNodeID=3003&PEmp="+Emp+"&PFID="+FID+"&PWorkID="+workID+"&FK_Flow=032&FK_Node=3201&T=";
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
						language : 'zh-CN',
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
						language : 'zh-CN',
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
						language : 'zh-CN',
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
						language : 'zh-CN',
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
						language : 'zh-CN',
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
					alert("错误，请看提示")
				}
			});
</#noparse>
		<#if components?? >
				<#list components?sort_by("order") as c>
					<#if c['componentType']=='1013' >
						<@convertContainPageScript c />
					</#if>
					<#if c['componentType']=='1036'>
						<@convertAddSearchScript c />
					</#if>
				</#list>
			</#if>
		
		<#list pageActs?sort_by("order") as act>
			<#if true>
				<@convertScript act />
			</#if>
		</#list>  
        
        <#if page??>
			<#if page.getAfterLoadScript()??>
				${page.getAfterLoadScript()}
			</#if>
		</#if>
<#noparse>
		});
</#noparse>		  
		
	</script>
</body>
</html>