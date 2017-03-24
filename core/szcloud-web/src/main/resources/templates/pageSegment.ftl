<#----表单片段模板----->
<#include "templates/parseFunction.ftl">

		<--MYLayoutAndCom-->
			<#if pageActs??>
				<#list pageActs?sort_by("order") as act>
					<#noparse><@shiro.hasPermission name="11111:</#noparse>${(pageActPermission[act.getPageId()])!""}<#noparse>"></#noparse>
						<@convertButton act />
					<#noparse></@shiro.hasPermission></#noparse>
				</#list>
			</#if>
		
			 <#if layouts??>
			 	<#if components??>
			 		<#list layouts?sort_by("order") as layout>
			 			<@parseLayout layout components/>
			 		</#list> 
			 	</#if>
			 </#if>
		<--MYLayoutAndCom/-->
		
			<--MYpageJScript-->
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
			<--MYpageJScript/-->
		
			<--MYvalidate-->
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
			<--MYvalidate/-->
		
		<--MYpageActScript-->
		<#list pageActs?sort_by("order") as act>
			<#if true>
				<@convertScript act />
			</#if>
		</#list>
		<--MYpageActScript/-->

