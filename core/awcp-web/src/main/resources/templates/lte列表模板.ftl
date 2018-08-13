<#include "templates/parseAdminLTE.ftl">
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
<head lang="en">
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/bootstrap.min.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/bootstrap-table.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/select2/select2.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/square/green.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/AdminLTE.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/font-awesome.min.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/ionicons.min.css">
    <link rel="stylesheet" href="${basePath}template/AdminLTE/css/main.css">
    <script>
		var basePath = '${basePath!''}';
		var cssFile={},scriptFile={};
		//引入组件样式表
		for(var e in cssFile){document.write('<link rel="stylesheet" href="'+e+'">')}
		cssFile=null;
	</script>
</head>
<body>
	<!-- Main content -->
  <section class="content">
		<div class="opeBtnGrop">
</#noparse>
		<#if pageActs??>
			<#list pageActs?sort_by("order") as act>
				<#if !act['place']??||act['place']!='1' >
					<@convertButton act />
				</#if>
			</#list>
		</#if>
<#noparse>
		</div>
		<div class="fixedBtnBox"></div>
		<div class="row">
		<div class="col-md-12">
			<div class="box box-info">
				<div class="box-body">
						<form class="form-horizontal" id="groupForm" action="${basePath}document/view.do" method="post">
							<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
							<input type="hidden" id="currentPage" name="currentPage" value="${currentPage!"0"}">
							<input type="hidden" id="pageSize" name="pageSize" value="${</#noparse>${dataAlias[0]}_list_paginator.getLimit()<#noparse>}</#noparse><#noparse>">
							<input type="hidden" id="totalCount" name="totalCount" value="${</#noparse>${dataAlias[0]}_list_paginator.getTotalCount()<#noparse>}</#noparse><#noparse>">
							<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
							<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
							<input type="hidden" name="instanceId" value="${ (vo.instanceId)!""}" id="instanceId"/>
							<input type="hidden" name="taskId" value="${ (vo.taskId)!""}" id="taskId"/>
							<input type="hidden" name="nodeId" value="${ (vo.nodeId)!""}" id="nodeId"/>
							<input type="hidden" name="actId" value="${ (vo.actId)!""}" id="actId"/>
							<input type="hidden" name="orderBy" value="${ (vo.orderBy)!""}" id="orderBy"/>
							<input type="hidden" name="allowOrderBy" id="allowOrderBy" value="${ (vo.allowOrderBy)!""}"/>
							<input type="hidden" name="WorkID" value="${ (vo.workItemId)!""}" id="WorkID"/>
							<input type="hidden" name="FK_Node" value="${ (vo.entryId)!""}" id="FK_Node"/>
							<input type="hidden" name="FK_Flow" value="${ (vo.flowTempleteId)!""}" id="FK_Flow"/>
							<input type="hidden" name="FID" value="${ (vo.fid)!""}" id="FID"/>
							<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
							<input type="hidden" name="toNode" value="" id="toNode">
							<input type="hidden" name="slectsUserIds" value="${ (vo.slectsUserIds)!""}" id="slectsUserIds"/>
							<input type="hidden" name="slectsUserNames" value="${ (vo.slectsUserNames)!""}" id="slectsUserNames"/>
							<input type="hidden" name="masterDataSource" value="" id="masterDataSource"/>
							<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
							<table id="bootstrapTable" class="table table-hover">
								</#noparse>
								<#if components?? >
									<#list components?sort_by("order") as c>
										<#if c['componentType']=='1036'>
											<@convertAddSearch c />
										</#if>
									</#list>
								</#if>
								<#noparse>
								<thead>
									<tr>
										<th class="hidden"></th>
										<th data-width="" data-field="" data-checkbox="true"></th>
										<th data-width="50">序号</th>
							</#noparse>
								<#if components?? >
									 <#list components?sort_by("order") as c>
										<#if c['componentType']=='1008' >
											<@parseColumn c />
										</#if>
									</#list>
								</#if>
								<#if hasRowButton?? && hasRowButton>
									<th>操作</th>
								</#if>
							<#noparse>
									</tr>
								</thead>
								<tbody>
									<#if </#noparse>${dataAlias[0]}<#noparse>_list?? && </#noparse>${dataAlias[0]}<#noparse>_list?size gte 1>
										<#list
											</#noparse>
											${dataAlias[0]}_list as ${dataAlias[0]}>
											<#noparse>
										<tr>
											<td class="hidden formData">
												<input type="hidden" value="${</#noparse>${dataAlias[0]}<#noparse>.ID!''}">
											</td>
											<td></td>
											</#noparse>
									<td><#noparse>${(</#noparse>${dataAlias[0]}_list_paginator.getPage() * ${dataAlias[0]}_list_paginator.getLimit() - ${dataAlias[0]}_list_paginator.getLimit() + ${dataAlias[0]}_index + 1<#noparse>)}</#noparse>
									</td>
									<#if components?? >
										 <#list components?sort_by("order") as c>
											<#if c['componentType']=='1008' >
												<@parseColumnData c />
											</#if>
										</#list>
									</#if>
									<#if hasRowButton?? && hasRowButton>
										<td>
											<#list pageActs?sort_by("order") as act>
													<#if act['place']??&&act['place']=='1' >
														<@convertButton act />
													</#if>
											</#list>
                                        </td>
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
			</div>
		</div>
	</section>
	<script src="${basePath}template/AdminLTE/js/jquery.min.js"></script>
	<script src="${basePath}template/AdminLTE/js/bootstrap.min.js"></script>
	<script src="${basePath}resources/scripts/common.js"></script>
	<script src="${basePath}resources/scripts/platform.document.js"></script>
    <script src="${basePath}template/AdminLTE/js/editTable.js"></script>
    <script src="${basePath}template/AdminLTE/js/bootstrap-table.js"></script>
	<script src="${basePath}template/AdminLTE/js/bootstrap-table-zh-CN.min.js"></script>
	<script src="${basePath}template/AdminLTE/js/select2/select2.full.js"></script>
	<script src="${basePath}template/AdminLTE/js/select2/zh-CN.js"></script>
	<script src="${basePath}template/AdminLTE/js/icheck.js"></script>
	<script src="${basePath}template/AdminLTE/js/bootstrap-datetimepicker.min.js"></script>
	<script src="${basePath}template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js"></script>
	<script src='${basePath}venson/js/common.js'></script>
	<script src='${basePath}venson/js/addSearch.js'></script>
	<script>
		for(var e in scriptFile){document.write('<script src="'+e+'"><\/script>')}
		scriptFile=null;
		var basePath = '${basePath}';
		var count=0;//默认选择行数为0
	$(document).ready(function(){
		$(document).keyup(function(event) {
	    	if (event.keyCode == 13) {
	    		$("#groupForm").submit();
	    	}
	    });
		 $("#bootstrapTable").bootstrapTable({
        	 pageSize:parseInt($("#pageSize").val()),
        	 pageNumber:parseInt($("#currentPage").val()),
        	 totalRows:parseInt($("#totalCount").val()),
        	 sidePagination:"server",
             showColumns:true,
        	 pagination:true,
        	 onPageChange:function(number, size){
        		$("#pageSize").val(size);
        		$("#currentPage").val(number);
        		$("#groupForm").submit();
        	 },
        	 onClickRow:function(row,$element,field){
        	 	if(field && (field+"").indexOf("rowOperation")!=-1){
        	 		return false;
        	 	}
        	  	var $checkbox=$element.find(":checkbox").eq(0);
        	  	if($checkbox.get(0).checked){
					$checkbox.get(0).checked=false;
					$element.find("input[type='hidden']").removeAttr("name","_selects");
					$element.removeClass("selected");
        	  	}else{
					$checkbox.get(0).checked=true;
					$element.find("input[type='hidden']").attr("name","_selects");
					$element.addClass("selected");
        	  	}
				count = $("input[name='_selects']").length;
        	 },
        	 onCheck: function(row,$element){
				  $element.closest("tr").find("input[type='hidden']").attr("name","_selects");
				  count = $("input[name='_selects']").length;
        	 },
        	 onUncheck:function(row,$element){
        		 $element.closest("tr").find("input[type='hidden']").removeAttr("name");
				 count = $("input[name='_selects']").length;
        	 },
        	 onCheckAll: function (rows) {
				 $("#groupForm .table td input[type='hidden']").attr("name","_selects");
				 count = $("input[name='_selects']").length;
             },
             onUncheckAll: function (rows) {
                 $("#groupForm .table td input[type='hidden']").removeAttr("name");
				 count = $("input[name='_selects']").length;
             },
			 onSort:function(name, order){
				 $("#allowOrderBy").val("1");
				 var orderBy =$("#orderBy").val();
				 console.log(orderBy,name)
				 if(orderBy.indexOf(name)!=-1&&orderBy.indexOf("desc")!=-1){
					 $("#orderBy").val(name+" asc");
				 }else{
					 $("#orderBy").val(name+" desc");
				 }
        		$("#groupForm").submit();
			 }
         });
</#noparse>
		<#if components?? >
				<#list components?sort_by("order") as c>
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