<#---列表片段模板--->
<#include "templates/parseFunction.ftl">


<--MYLayoutAndCom--> 
		<div class="row" id="buttons">

			<#if pageActs??>
				<#list pageActs?sort_by("order") as act>
					
						<@convertButton act />
					
				</#list>
			</#if>

		</div>
	
		
<#noparse>					
		<div class="row" id="datatable">
				
					<table class="table datatable table-bordered">
						<thead>
							<tr>
								<th class="hidden"></th>
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
						<tbody id="includeList1">
		            	<#if </#noparse>${dataAlias[0]}<#noparse>_list?? >	
		            	 	<#list
</#noparse>
		            	 		${dataAlias[0]}_list as ${dataAlias[0]}>
<#noparse> 
								<tr>
								<td class="hidden formData"><input id="boxs" type="hidden"
								value="${</#noparse>${dataAlias[0]}<#noparse>.id}"></td> 	
</#noparse>				
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
			
		</div>
	</div>
</#noparse>
<--MYLayoutAndCom/-->

<--MYpageJScript-->
<--MYpageJScript/-->
<--MYvalidate-->
<--MYvalidate/-->

<--MYpageActScript-->
		<#list pageActs?sort_by("order") as act>
			<#if true>
				<@convertScript act />
			</#if>
		</#list> 
<--MYpageActScript/--> 
