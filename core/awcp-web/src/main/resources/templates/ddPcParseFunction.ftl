<#------  布局组件解析    ------>
<#macro parseLayout root components>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		<div class="rowDiv">
	<#else>
		<div class="colDiv">
	</#if>
	<#if (root['childLayouts']??) >
		<#local layoutChilden = root['childLayouts'] >
		<#list layoutChilden?sort_by("order") as layout>
			<@parseLayout layout components/>
		</#list>
	<#else>
		<#local rootId = root['pageId']/>
		<#if (components[rootId]??) >
			<#local coms = components[rootId]/>
			<#local comSize = 0/>
			<#list coms as c>
				<#if c['componentType']!='1010' >
					<#local comSize = comSize+1/>
				</#if>
			</#list>
			<#if comSize gt 1 >
				<div class="input-group">
			</#if>
			<#list coms?sort_by("order") as c>
				<@parseComponent c />
			</#list>
			<#if comSize gt  1 >
				</div>
			</#if>
		</#if>
	</#if>
	</div>
</#macro>

<#------  组件解析	------>
<#macro parseComponent c >
	<#local componetType=c['componentType']?number>
	<#switch componetType>
		<#case 1001>
			<@convertInputext c/>
			<#break>
		<#case 1002>
			<@convertDatetime c/>
			<#break>
		<#case 1005>
			<@convertTextarea c/>
			<#break>
		<#case 1006>
			<@convertSelect c/>
			<#break>
		<#case 1010>
			<@convertHidden c/>
			<#break>
		<#case 1011>
			<@convertFile c/>
			<#break>
		<#case 1016>
			<@convertImage c/>
			<#break>
		<#default>
	</#switch>
</#macro>

<#------  组件脚本解析	------>
<#macro convertComponentScript component>
	<#local componetType=component['componentType']?number>
	<#switch componetType>
		<#case 1001>
			<@convertInputextScript component/>
			<#break>
		<#case 1011>
			<@convertFileScript component/>
			<#break>
		<#case 1016>
			<@convertImageScript component/>
			<#break>
		<#default>
	</#switch>
</#macro>





<#-------------------------------------------hidden框组件begin---------------------------------------->
<#macro convertHidden c >
	<input type='hidden' <#if c['name']?? >name='${c['name']}'</#if> id='${(c['pageId'])!""}'
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0>value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"</#if> 		
	/>
</#macro>
<#-------------------------------------------hidden框组件end---------------------------------------->





<#--  日期选择框	-->
<#macro convertDatetime c>		
<div style="display: block;" class="createreport_get_report">
	<#local placeholder = 'select' />
	<#if c['placeholder']?? && c['placeholder']!=''>
		<#local placeholder = c['placeholder'] />			
	</#if>
	<#if c['title']?? >
		<div class="colFirst"><span><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></span>
		<#if c['required']?? && c['required'] == '1'><span class="red_stat">*</span></#if>
		</div>
	</#if>
	<div class="colSecond">
		<input  class='<#if c['required']?? && c['required'] == '1'>required</#if>' id='${(c['pageId'])!""}'
			<#if c['name']?? >name='${c['name']}'</#if> readonly='readonly'
			<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
			<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
				value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
			</#if>	
			dateType='${c['dateType']}'
			data-placeholder='${placeholder+c['required']}'
		 />
	 </div>
</div>
</#macro>





<#-------------------------------------------图片上传框begin------------------------------------------->
<#macro convertImage c >
<div style="display: block;" class="createreport_add_img">
	<div>
		<div class="colFirst" style="padding-top: 10px;"><span><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></span></div>
		<div id="img_list" class="colSecond" style="padding-top: 10px;">
            <ul>
            	<li id="add_img_btn">
            		<img class="add_img_btn" src="<#noparse>${basePath}</#noparse>dingding/img/pc/uploadphoto.jpg">
            	</li>
            </ul>
        </div>
        <input type="hidden" 
	       		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
				<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	       		class="photo <#if c['required']?? && c['required'] == '1'>required</#if>" 
	       		name="${c['name']!""}" 
	       		value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"/>
	</div>
</div>
</#macro>
<#-------------------------------------------图片上传框end------------------------------------------------>

<#-------------------------------------------图片上传框脚本begin------------------------------------------->
<#macro convertImageScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var $imgInput = $('input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			var $imgList = $imgInput.prev("[id='img_list']");
			var imageData = $imgInput.val();
			ddPc.imgInit(imageData,$imgList);
			
			var imgMax = </#noparse>${component['maxCount']}<#noparse>;
			$imgList.find(".add_img_btn").click(function(){
				ddPc.uploadImage(imgMax,$imgInput,$imgList);
			});
		})();
	</#if>	
	</#noparse>
</#macro>
<#--------------------------------------------- 图片上传框脚本 end--------------------------------------->





<#-------------------------------------------附件上传框begin------------------------------------------->
<#macro convertFile c >
<div style="display: block;" class="createreport_add_img">
	<div class="colFirst"><label><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<span style="width: 20px;height: 18px;font-size: 14px;color: #38adff;cursor: pointer;">添加附件</span>
	</div>
    <div id="files" style="padding-left: 0px;clear: inherit;overflow: auto;" class="colSecond"></div>
    <textarea style="display:none;"
       	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse> 
       	class="attachment <#if c['required']?? && c['required'] == '1'>required</#if>" 
       	name="${c['name']!""}"><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></textarea>
</div>
</#macro>
<#-------------------------------------------附件上传框end------------------------------------------->

<#--附件上传框脚本-->
<#macro convertFileScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var $fileTextarea = $('textarea[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			var $files = $fileTextarea.prev();
			var fileMax = </#noparse>${component['maxCount']}<#noparse>;
			
			ddPc.initFile($fileTextarea,$files);
			
			$files.prev().children("span").click(function(){			
				ddPc.uploadFile(fileMax,$files,$fileTextarea);
			});	
		})();
	</#if>	
	</#noparse>
</#macro>
<#-- 附件上传框脚本end-->





<#--  多行输入框	-->
<#macro convertTextarea c >
<div style="display: block;" class="createreport_get_report">
	<#if c['title']?? >
		<div class="colFirst">
			<span><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></span>
			<#if c['required']?? && c['required'] == '1'><span class="red_stat">*</span></#if>
		</div>
	</#if>	
	<#local placeholder = 'input' />
	<#if c['placeholder']?? && c['placeholder']!=''>
		<#local placeholder = c['placeholder'] />			
	</#if>
	<div class="colSecond">
		<textarea autoheight="true"
		class='<#if c['css']?? && c['css']?length gt 0>${c['css']}</#if> <#if c['required']?? && c['required'] == '1'>required</#if> '			
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		id='${(c['pageId'])!""}'
		<#if c['name']?? >name='${c['name']}'</#if>
		data-placeholder='${placeholder+c['required']}'
		><#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 ><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></#if></textarea>
	</div>
</div>
</#macro>





<#-------------------------------------------输入框组件begin---------------------------------------->
<#macro convertInputext c >
<div style="display: block;" class="createreport_get_report">
	<#if c['title']?? >
		<div class="colFirst"><span><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></span>
		<#if c['required']?? && c['required'] == '1'><span class="red_stat">*</span></#if>
		</div>
	</#if>
	<#local placeholder = 'input' />
	<#if c['placeholder']?? && c['placeholder']!=''>
		<#local placeholder = c['placeholder'] />			
	</#if>	
	<div class="colSecond">
		<input type='text' class='<#if c['css']?? && c['css']?length gt 0>${c['css']}</#if> <#if c['required']?? && c['required'] == '1'>required</#if>'			
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>		
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>			
		style='border: 1px solid rgb(230, 229, 230);'
		id='${(c['pageId'])!""}'
		<#if c['name']?? >name='${c['name']}'</#if>	
		data-placeholder='${placeholder+c['required']}'
		/>
	</div>
</div>
</#macro>
<#-------------------------------------------输入框组件end---------------------------------------->

<#------------------------------------文本框脚本------------------------------------------->
<#macro convertInputextScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var that=$('input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			</#noparse>${component['onchangeScript']}<#noparse>;
		})();
	</#if>	
	</#noparse>
</#macro>
<#---------------------------------- 文本框脚本 end----------------------------------------->





<#-------------------------------------------下拉框select begin------------------------------------------->
<#macro convertSelect c >
<div style="display: block;" class="createreport_get_report">
	<#if c['title']?? >
		<div class="colFirst"><span><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></span>
		<#if c['required']?? && c['required'] == '1'><span class="red_stat">*</span></#if>
		</div>
	</#if>
	<div class="colSecond">
	    <select <#if c['name']?? >name='${c['name']}'</#if> id='${(c['pageId'])!""}'
	    	class='<#if c['required']?? && c['required'] == '1'>required</#if>'
			<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0>
				value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"	
			</#if>	>
			<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
		</select>
	</div>
</div>
</#macro>
<#-------------------------------------------下拉框select end------------------------------------------>







<#-----------------button begin------------------------------------------------------------->
<#macro convertButton c>
	<button type="submit" class="createreport_submit_btn" id='${c.getPageId()}' 
		<#noparse>
			<#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden'])?? && pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden']>
				style="display:none;"
			</#if>
		</#noparse>
	>
		<#noparse>${others['title_</#noparse>${c.getPageId()}<#noparse>']!''}</#noparse>
	</button>
</#macro>
<#macro convertScript c>
	$("#${c.getPageId()}").click(function(){		
		<#if c.getClientScript()??>
			${c.getClientScript()}
		</#if>		
	});
</#macro>
<#-----------------button end------------------------------------------------------------->