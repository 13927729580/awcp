<#macro parseLayout root components>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		<div class="outerDiv">
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
			<#local coms = components[rootId] />
			<#list coms?sort_by("order") as c>
				<@parseComponent c />
			</#list>
		</#if>
	</#if>
	</div>
</#macro>


<#macro parseComponent c >
	<#local componetType=c['componentType']?number>
	<#switch componetType>
		<#case 1001>
			<@convertInputext c/>
			<#break>
		<#case 1002>
			<@convertDatetime c/>
			<#break>
		<#case 1003>
			<@convertCheckbox c/>
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
		<#case 1101>
			<@convertFunction c/>
			<#break>
		<#case 1102>
			<@convertAddress c/>
			<#break>
		<#case 1103>
			<@convertDetails c/>
			<#break>
		<#default>
	</#switch>
</#macro>


<#--  按钮	-->
<#macro convertButton c >
	<div class="submit-foot tTap" id='${c.getPageId()}' 
		<#noparse>
			<#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden'])?? && pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden']>
				style="display:none;"
			</#if>
		</#noparse>
	><#noparse>${others['title_</#noparse>${c.getPageId()}<#noparse>']!''}</#noparse></div>
</#macro>


<#--  按钮点击事件脚本	-->
<#macro convertScript c>
	$("#${c.getPageId()}").click(function(){	
		<#if c.isChooseValidate()?? && c.isChooseValidate()>
			if(!Comm.validDDParam())
				return;
		</#if>
		<#if c.getClientScript()??>
			${c.getClientScript()}
		</#if>
	});
</#macro>

<#--  事件控件	-->
<#macro convertFunction c>
<div class="input_control">	
	<#if c['title']?? >
		<div class="colFirst" >
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
	</#if>	
	<div class="colSecond" >
     	<span data-id="${c['placeholder']!''}" class="awcp_fun <#if c['css']?? && c['css']?length gt 0>${c['css']}</#if>"  
     		<#if c['style']?? >style="margin-left: 0px;${c['style']}"</#if> ><#if c['extra']?? && c['extra']?length gt 0 >
			<#noparse>${(</#noparse>${c['extra']}<#noparse>)!""}</#noparse>	
		</#if></span>
    
     	<input class="<#if c['required']?? && c['required'] == '1'>required</#if>" type="hidden" id="${(c['pageId'])!''}"  	
     	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
     	<#if c['name']?? >
			name='${c['name']}'		
		</#if> 
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	 	/>
	 </div>
</div>
</#macro>


<#--  移动端省市县	-->
<#macro convertAddress c>
<div class="input_control">	
	<#if c['title']?? >
		<div class="colFirst" >
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
	</#if>	
	<div class="colSecond">
     	<div>
	        <div class="content-block">
	            <input id="pcaName" type="text" readonly="readonly" placeholder="请选择省市县" 
	            	value="<#noparse>${others['pcaName_</#noparse>${c['name']}<#noparse>']!''}</#noparse>"
	            />
	            <input id="pcaValue" type="hidden" 	
					value="<#noparse>${others['pcaValue_</#noparse>${c['name']}<#noparse>']!''}</#noparse>"
				/>
	        </div>
	    </div>
	 </div>
</div>
</#macro>


<#--  多选框	-->
<#macro convertCheckbox c>
<div class="input_control">	
	<#if c['title']?? >
		<div class="colFirst" >
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
	</#if>	
	<div class="colSecond" >
     	<span data-id='${c['placeholder']!''}' class='dd_select <#if c['css']?? && c['css']?length gt 0>${c['css']}</#if>'  
     		<#if c['style']?? >style='float:right;margin-right: 15px;${c['style']}'</#if>><#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
			<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!""}</#noparse>	
		</#if></span>
     	<input class='<#if c['required']?? && c['required'] == '1'>required</#if>' type='hidden' id='${(c['pageId'])!""}'
	    <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		<#if c['name']?? >
			name='${c['name']}'		
		</#if> 
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	 	/>
	</div>
</div>
</#macro>


<#--  隐藏框	-->
<#macro convertHidden c >
	<input type='hidden' <#if c['name']?? >name='${c['name']}'</#if> 
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		<#if c['dataItemCode']??   && c['dataItemCode']?length gt 0>
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if> 
		id='${(c['pageId'])!""}'
	/>
</#macro>


<#--  附件上传框	-->
<#macro convertFile c >
<div class="input_control">	
	<div>
		<div class="colFirst" >
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
		<div class="colSecond" >
	       	<img src="<#noparse>${basePath}</#noparse>dingding/img/files.png" 
	       		style="float:right;width:22px;" id="picture" />
	       	<textarea 
	       	style="display:none;"
	       	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
			<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse> 
	       	class="attachment <#if c['required']?? && c['required'] == '1'>required</#if>" 
	       	name="${c['name']!""}"><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></textarea>
		</div>
	</div>
    <div class="atts" id="files"></div>
</div>
</#macro>


<#--  图片上传框	-->
<#macro convertImage c >
<div class="input_control">	
	<div>
		<div class="colFirst" >
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
		<div class="colSecond" >
	       	<img src="<#noparse>${basePath}</#noparse>dingding/img/camera.png" 
	       		style="float:right;width:22px;" id="picture" />
	       	<input type="hidden" 
	       		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
				<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	       		class="photo <#if c['required']?? && c['required'] == '1'>required</#if>" 
	       		name="${c['name']!""}" 
	       		value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"/>
       	</div>
	</div>
    <div class="images imgs-scroll"></div>
</div>
</#macro>


<#--  下拉框	-->
<#macro convertSelect c >
<div class="input_control">	
	<#if c['title']?? >
		<div class="colFirst" >
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
	</#if>	
	<div class="colSecond">
	    <span class='dd_select <#if c['css']?? && c['css']?length gt 0>${c['css']}</#if>'  <#if c['style']?? >style='float:right;margin-right: 15px;${c['style']}'</#if>
	    data-id='<#noparse><#if (</#noparse>${c['dataItemCode']}<#noparse>)?? && (</#noparse>${c['dataItemCode']}<#noparse>)?length gt 0 ><#else></#noparse><#if c['placeholder']?? && c['placeholder']?length gt 0 >${c['placeholder']}<#else>请选择_Please Select</#if><#noparse></#if></#noparse>'
	    >    
	    </span>
     	<input  class='<#if c['required']?? && c['required'] == '1'>required</#if>'  type='hidden' id='${(c['pageId'])!""}'
		<#if c['name']?? >
			name='${c['name']}'		
		</#if> 		
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>		
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	 	/>
	 	<input type="hidden" value="<#noparse><#if others??> ${others['options_</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>" />
	</div>
</div>
</#macro>


<#--  多行输入框	-->
<#macro convertTextarea c >
<div class="input_control">	
	<#if c['title']??  && c['title']?length gt 0>
		<div class="colFirst">
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
	</#if>
	<div class="colSecond" style="<#if c['title']?? && c['title']?length==0>width:100%;</#if>">
		<textarea class='<#if c['css']?? && c['css']?length gt 0>${c['css']}</#if> <#if c['required']?? && c['required'] == '1'>required</#if> '	
		<#if c['rowCount']?? >rows='${c['rowCount']}'<#else>rows='3'</#if>			
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		id='${(c['pageId'])!""}'
		style='<#if c['style']?? >${c['style']}</#if>'
		<#if c['name']?? >name='${c['name']}'</#if>
		data-placeholder='<#if c['placeholder']?? && c['placeholder']?length gt 0 >${c['placeholder']}<#else>请输入_Please Input</#if>'
		><#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 ><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></#if></textarea>
	</div>
</div>
</#macro>


<#--  日期选择框	-->
<#macro convertDatetime c>		
<div class="input_control">	
	<#if c['title']?? >
		<div class="colFirst">
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
	</#if>
	<div class="colSecond">
		<span data-id='<#noparse><#if (</#noparse>${c['dataItemCode']}<#noparse>)?? && (</#noparse>${c['dataItemCode']}<#noparse>)?length gt 0 ><#else></#noparse><#if c['placeholder']?? && c['placeholder']?length gt 0 >${c['placeholder']}<#else>请选择_Please Select</#if><#noparse></#if></#noparse>' 
			style='float:right;margin-right: 15px;' class='
			<#if c['css']?? && c['css']?length gt 0>
				${c['css']}
			<#else>
				arrowright 
			</#if>' >
			
			</span>
		<input class='<#if c['required']?? && c['required'] == '1'>required</#if>' type='hidden' id='${(c['pageId'])!""}'
			<#if c['name']?? >
				name='${c['name']}'		
			</#if> 
			<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
			<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
			<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
				value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
			</#if>	
		 />
	 </div>
</div>
</#macro>


<#--  文本输入框	-->
<#macro convertInputext c >
<div class="input_control">	
	<#if c['title']?? >
		<div class="colFirst">
			<label class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		</div>
	</#if>	
	<div class="colSecond">
		<input type='${c['textType']!'text'}' class='<#if c['css']?? && c['css']?length gt 0>${c['css']}</#if> <#if c['required']?? && c['required'] == '1'>required</#if>'			
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>		
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>			
		style='<#if c['style']?? >${c['style']}</#if>'
		id='${(c['pageId'])!""}'
		<#if c['name']?? >name='${c['name']}'</#if>	
		data-placeholder='<#if c['placeholder']?? && c['placeholder']?length gt 0 >${c['placeholder']}<#else>请输入_Please Input</#if>'
		/>
	</div>
</div>
</#macro>


<#--  明细控件	-->
<#macro convertDetails c>
	<#noparse><#if </#noparse>${c['dataAlias']!''}<#noparse>?? && </#noparse>${c['dataAlias']!''}<#noparse>?size gt 0></#noparse>    	     	
 		<#noparse><#list</#noparse> ${c['dataAlias']!''} <#noparse>as item></#noparse>		
 		<div class="${c['dataAlias']!''}" data-index="<#noparse>${item_index+1}</#noparse>">	
			<div class="input_control">
 				<#if c['title']?? >
					<div class="colFirst">
						<label style="font-size: 12px;" class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>">${c["title"]!""}(<span style="font-size: 12px;" class="indexSpan"><#noparse>${item_index+1}</#noparse></span>)</label>
					</div>
					<div class="colSecond">
						<span style="font-size: 14px;float:right;color:blue;display:none;" class="delSpan">删除</span>
					</div>
				</#if>	
			</div>
 			<#if c['details']?? && c['details']?size gt 0>
	 			<#list c['details'] as detail>
	 				<@convertDetail detail item></@convertDetail>
	 			</#list>  
	 		</#if>	
	 	</div>	
 		<#noparse></#list></#noparse>
 	<#noparse>
 	<#else>
 	</#noparse>	
 		<#if c['title']?? >
			<div class="colFirst ${c['dataAlias']!''}">
				<label style="font-size: 12px;" class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>">${c["title"]!""}(<span style="font-size: 12px;" class="indexSpan">1</span>)</label>
			</div>
			<div class="colSecond">
				<span style="font-size: 14px;float:right;color:blue;display:none;" class="delSpan">删除</span>
			</div>
		</#if>	
		</div>
 		<#if c['details']?? && c['details']?size gt 0>
 			<#list c['details'] as detail>
 				<@convertDetail detail></@convertDetail>
 			</#list>  
 		</#if>	
 	<#noparse>
 	</#if>     	
 	</#noparse>
 	<div class="input_control" id="add_${c['dataAlias']!''}">
 		<div class="colFirst" style="width:100%;text-align:center;display:block;padding: 0px;">
 			<label style="line-height: 48px;color:blue">+新增${c["title"]!""}</label>
 		</div>
 	</div>
 	<div style="display:none" id="div_${c['dataAlias']!''}">
 		<div class="input_control">
			<#if c['title']?? >
				<div class="colFirst">
					<label style="font-size: 12px;" class="<#if c['required']?? && c['required']=='1'>requiredLabel</#if>">${c["title"]!""}(<span style="font-size: 12px;" class="indexSpan"></span>)</label>
				</div>
				<div class="colSecond">
					<span style="font-size: 14px;float:right;color:blue;display:none;" class="delSpan">删除</span>
				</div>
			</#if>	
		</div>
 		<#if c['details']?? && c['details']?size gt 0>
 			<#list c['details'] as detail>
 				<@convertDetail detail></@convertDetail>
 			</#list>  
 		</#if>
</#macro>

<#macro convertDetail detail item="">
	<#if detail.type=="1001">
		<div class="input_control" data-type="text">
			<div class="colFirst">
				<label>${detail.title!""}</label>
			</div>
			<div class="colSecond">
				<input type="text" name="${detail.field!""}" data-placeholder="请输入_Please Input" 
					value="<#noparse>${(item.</#noparse>${detail.field!""}<#noparse>)!""}</#noparse>" />
			</div>
		</div>
	<#elseif detail.type=="1002">
		<div class="input_control" data-type="date">
			<div class="colFirst">
				<label>${detail.title!""}</label>
			</div>
		<div class="colSecond">
			<span data-id="请选择_Please Select" style="float:right;margin-right: 15px;" class="arrowright"></span>
			<input type="hidden" name="${detail.field!""}" 
				value="<#noparse>${(item.</#noparse>${detail.field!""}<#noparse>)!""}</#noparse>" />
		 </div>
		</div>
	<#elseif detail.type=="1005">
		<div class="input_control" data-type="textarea">
			<div class="colFirst">
				<label>${detail.title!""}</label>
			</div>
			<div class="colSecond">
				<textarea rows="4" style="resize: none; height: 100px;" name="${detail.field!""}" 
					data-placeholder="请输入_Please Input"><#noparse>${(item.</#noparse>${detail.field!""}<#noparse>)!""}</#noparse></textarea>
			</div>
		</div>
	<#elseif detail.type=="1006">
	
	<#elseif detail.type=="1010">
		<input type="hidden" name="${detail.field!""}" value="<#noparse>${(item.</#noparse>${detail.field!""}<#noparse>)!""}</#noparse>" />
	<#elseif detail.type=="1011">
		<div class="input_control" data-type="file">
			<div>
				<div class="colFirst">
					<label>${detail.title!""}</label>
				</div>
				<div class="colSecond">
			       	<img src="<#noparse>${basePath}</#noparse>dingding/img/files.png"  style="float:right;width:22px;" id="picture">
			       	<textarea style="display: none; resize: none;" class="attachment" name="${detail.field!""}"><#noparse>${(item.</#noparse>${detail.field!""}<#noparse>)!""}</#noparse></textarea>
				</div>
			</div>
		    <div class="atts" id="files"></div>
		</div>
	<#elseif detail.type=="1016">
		<div class="input_control" data-type="img">
			<div>
				<div class="colFirst">
					<label class="">${detail.title!""}</label>
				</div>
				<div class="colSecond">
			       	<img src="<#noparse>${basePath}</#noparse>dingding/img/camera.png" style="float:right;width:22px;" id="picture">
			       	<input type="hidden" class="photo " name="${detail.field!""}" value="<#noparse>${(item.</#noparse>${detail.field!""}<#noparse>)!""}</#noparse>" />
		       	</div>
			</div>
		    <div class="images imgs-scroll"></div>
		</div>
	</#if>
</#macro>

<#macro convertDetailsScript c>
	(function(){
		var dataAlias = "${c['dataAlias']!''}";
		
		function showAndHide(){
			var length = $("." + dataAlias).length;
			if(length > 1){
				$("." + dataAlias).find(".delSpan").show();
			} else{
				$("." + dataAlias).find(".delSpan").hide();
			}
			var index = 1;
			$("." + dataAlias).each(function(){
				$(this).attr("data-index",index);
				$(this).find(".indexSpan").text(index);
				index++;
			});
		}
				
		$("." + dataAlias).parent().on("click",".delSpan",function(){
			var length = $("." + dataAlias).length;
			if(length>1){
				$(this).parent().parent().parent().remove();
				showAndHide();				
			}
		});
		
		function bindEvent($dom){
			$dom.find("[data-type='date']").each(function(){
				convertDateTimeScript($(this).find("input"),"yyyy-MM-dd");
			});
			
			$dom.find("[data-type='img']").each(function(){
				convertImageScript($(this).find("input"), 9);
			});
			
			$dom.find("[data-type='file']").each(function(){
				convertFileScript($(this).find("textarea"), 5);
			});			
		}
		
		bindEvent($("." + dataAlias));
		
		$("#add_" + dataAlias).bind("click",function(){
			var html = $(this).next().html();
			var length = $("." + dataAlias).length + 1;
			html = "<div class='" + dataAlias + "' data-index='" + length + "'>" + html + "</div>";
			$(this).before(html);
			bindEvent($(this).prev());
			showAndHide();
		});
		
		showAndHide();
		
	})();
</#macro>

<#--  组件脚本解析	-->
<#macro convertComponentScript component>
	<#local componetType=component['componentType']?number>
	<#switch componetType>
		<#case 1001>
			<@convertInputextScript component/>
			<#break>
		<#case 1002>
			<@convertDateTimeScript component/>
			<#break>
		<#case 1003>
			<@convertCheckboxScript component/>
			<#break>
		<#case 1006>
			<@convertSelectScript component/>
			<#break>
		<#case 1011>
			<@convertFileScript component/>
			<#break>
		<#case 1016>
			<@convertImageScript component/>
			<#break>
		<#case 1101>
			<@convertFunctionScript component/>
			<#break>
		<#case 1102>
			<@convertAddressScript component/>
			<#break>
		<#case 1103>
			<@convertDetailsScript component/>
			<#break>
		<#default>
	</#switch>
</#macro>


<#--  事件控件脚本	-->
<#macro convertFunctionScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var input = $('input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			var span=input.parent().find(".awcp_fun");
			</#noparse>${(component['onchangeScript'])!""}<#noparse>
		})();
	</#if>	
	</#noparse>
</#macro>


<#--  省市县控件脚本	-->
<#macro convertAddressScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			</#noparse>${(component['onchangeScript'])!""}<#noparse>		
			var area = new LArea();
			area.init({
				trigger: "#pcaName",
				valueTo: "#pcaValue",
				keys: {
					id: "value",
					name: "text"
				},
				type: 2,
				data: [provs_data, citys_data, dists_data],
				fn:fn
			});			
		})();
	</#if>	
	</#noparse>
</#macro>


<#--  多选框脚本	-->
<#macro convertCheckboxScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var $input = $('input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			$input.parent().click(function(){
				var option=</#noparse>${(component['optionScript'])!""}<#noparse>
				var options=[];
				var selectOption=[];
				$.each(option.split(";"),function(i,e){
					if($.trim(e)){
						options.push(e);
						if($input.val().indexOf(e)!=-1){
							selectOption.push(e);
						}
					}
				})
				var $span=$(this).find(".dd_select");
				dd.biz.util.multiSelect({
					"options":options,
					"selectOption":selectOption,
					onSuccess: function(data) {
						var str=[];
						for(var i=0;i<data.length;i++){
							str.push(options[data[i]]);
						}
						$span.text(str.join(","));
						$input.val(str.join(","));
				    },
				    onFail: function(err) {
				    }
				});
			})
		})();
	</#if>	
	</#noparse>
</#macro>


<#--  文本框脚本	-->
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


<#--  附件上传框脚本	-->
<#macro convertFileScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var $attInput = $(':input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			var atts = $attInput.parent().parent().next(".atts");
			var attData = $attInput.val();
			attData = (attData)?JSON.parse(attData):false;
			if(attData){
				$.each(attData,function(i,e){
					var fileName = e.fileName;
					var fileType = e.fileType;
					if(fileType!="jpg" && fileType!="pdf"){
						fileType = "unknow";
					}
					var fileSize = (e.fileSize/1024.0).toFixed(2);
					var html = "<div class='fileItem' data-json='" + JSON.stringify(e) + 
							   "'><div class='eventBox'>" + 
							   "<div class='fileIcon icon_file icon_file_" + fileType + "'></div>" +
							   "<div class='fileName'>" + fileName + "</div>" +
							   "<div class='fileSize'>" + fileSize + "KB</div>" +
							   "</div><div class='fileClose'></div></div>";
					atts.append(html);
				});
				atts.off("click").on("click",".eventBox",function(){
					Comm.getData("dingding/spaceid.do",{"_method":"get","type":"download"});
					var e = JSON.parse($(this).parent().attr("data-json"));
					e.corpId = dd_config.corpId;
					dd.biz.cspace.preview(e);
				});
				
				$(".fileClose").click(function(){
					$(this).parent().remove();
					var arr = [];
					$(".fileItem").each(function(){
						var obj = JSON.parse($(this).attr("data-json"));
						arr.push(obj);
					});
					$attInput.val(JSON.stringify(arr));
				});
			}
			var attMax = </#noparse>${component['maxCount']}<#noparse>;
			$attInput.parent().click(function(){
				attData = $attInput.val();
				attData = (attData)?JSON.parse(attData):false;
				var len = 0;
				if(attData){
					len = attData.length;
					if(attMax-len<=0){
						dd.device.notification.alert({"message":"只能上传"+attMax+"个附件","title":dd_res.tip,"buttonName":dd_res.okButton})
						return;
					}
				}
				var data = Comm.getData("dingding/spaceid.do",{"_method":"get"});
				dd.biz.util.uploadAttachment({
				    image:{multiple:true,compress:false,max:attMax-len,spaceId: data},
				    space:{corpId:dd_config.corpId,spaceId:data, max:attMax-len},
				    file:{spaceId:data,max:attMax-len},
				    types:["photo","camera","file","space"],
				    onSuccess : function(result) {
				    	$.each(result.data,function(i,e){
							var fileName = e.fileName;
							var fileType = e.fileType;
							if(fileType!="jpg" && fileType!="pdf"){
								fileType = "unknow";
							}
							var fileSize = (e.fileSize/1024.0).toFixed(2);
							var html = "<div class='fileItem' data-json='" + JSON.stringify(e) + 
									   "'><div class='eventBox'>" + 
									   "<div class='fileIcon icon_file icon_file_" + fileType + "'></div>" +
									   "<div class='fileName'>" + fileName + "</div>" +
									   "<div class='fileSize'>" + fileSize + "KB</div>" +
									   "</div><div class='fileClose'></div></div>";
							atts.append(html);
						})
				    	var data;
				    	if(attData){
			    			data = attData.concat(result.data);
				    	}else{
				    		data = result.data;
				    	}
				    	$attInput.val(JSON.stringify(data));
						atts.off("click").on("click",".eventBox",function(){
							Comm.getData("dingding/spaceid.do",{"_method":"get","type":"download"});
							var e = JSON.parse($(this).parent().attr("data-json"));
							e.corpId = dd_config.corpId;
							dd.biz.cspace.preview(e);
						});
						$(".fileClose").click(function(){
							$(this).parent().remove();
							var arr = [];
							$(".fileItem").each(function(){
								var obj = JSON.parse($(this).attr("data-json"));
								arr.push(obj);
							});
							$attInput.val(JSON.stringify(arr));
						});
				    },
				    onFail : function(err) {
				    	alert(JSON.stringify(err));
				    }
				});
						
			});
		})();
	</#if>	
	</#noparse>
</#macro>


<#--  图片上传框脚本	-->
<#macro convertImageScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var $imgInput = $('input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			var images = $imgInput.parent().parent().next(".images");
			var imageData = $imgInput.val();
			if(imageData){
				var data = imageData.split(",");
				$.each(data,function(i,e){
					var html = '<div class="crrp-body-imgwrap">' + 
							   '<div class="crrp-body-removeimg" style="background: url(' + baseUrl + 
							   '/dingding/img/r_close_x.png) no-repeat center;"></div>' +
							   '<img class="crrp-body-imgwrap-img" src="' + e + '"></div>';
					images.append(html);
				});
				images.off("click").on("click",".crrp-body-imgwrap-img",function(){
					dd.biz.util.previewImage({"urls":data,"current":this.src});
				});
				$(".crrp-body-removeimg").click(function(){
					$(this).parent().remove();
					var arr = [];
					$(".crrp-body-imgwrap-img").each(function(){
						arr.push($(this).attr("src"));
					});
					$imgInput.val(arr.join());
				});
			}
			var imgMax = </#noparse>${component['maxCount']}<#noparse>;
			$imgInput.parent().click(function(){
				imageData = $imgInput.val();
				var len = 0;
				if(imageData){
					len = imageData.split(",").length;
					if(imgMax-len <= 0){
						dd.device.notification.alert({"message":"只能选择"+imgMax+"张图片","title":dd_res.tip,"buttonName":dd_res.okButton})
						return;
					}
				}
				
				dd.biz.util.</#noparse><#if component['extra']?? && component['extra']=='1'>uploadImageFromCamera<#else>uploadImage</#if><#noparse> ({
					"compression":true,
					"multiple": true, //是否多选，默认false
					"max": imgMax-len, //最多可选个数
					onSuccess: function(data) {
						for(var i=0;i<data.length;i++){
							var html = '<div class="crrp-body-imgwrap">' + 
							    '<div class="crrp-body-removeimg" style="background: url(' + baseUrl + 
							    '/dingding/img/r_close_x.png) no-repeat center;"></div>' +
							    '<img class="crrp-body-imgwrap-img" src="' + data[i] + '"></div>';
							images.append(html);
						}
						//如果之前存在则累加
						if(imageData){
							var arr=imageData.split(",");
							var newArr=[];
							for(var i=0;i<arr.length;i++){
								if($.trim(arr[i])){
									newArr.push(arr[i]);
								}
							}
							data=newArr.concat(data);
						}
						imageData = data.join(",");
						$imgInput.val(imageData);
						images.off("click").on("click",".crrp-body-imgwrap-img",function(){
							dd.biz.util.previewImage({"urls":data,"current":this.src});
						});		
						$(".crrp-body-removeimg").click(function(){
							$(this).parent().remove();
							var arr = [];
							$(".crrp-body-imgwrap-img").each(function(){
								arr.push($(this).attr("src"));
							});
							$imgInput.val(arr.join());
						});	
				    }
				});
						
			});
		})();
	</#if>	
	</#noparse>
</#macro>


<#--  下拉框脚本	-->
<#macro convertSelectScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var $input = $('input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			var optionStr = $.trim($input.next().val());		
			var optionArr = optionStr.split(";");	
			var source = [];
			var selectedKey;
			var firstKey;			
			for(var i=0;i<optionArr.length;i++){
				var e = optionArr[i];
				if(e){
					var arr = e.split("=");
					if(i==0){
						firstKey = arr[0];
					}
					var val = arr[0];
					var key;	//显示值		
					if(dd_res.lang=="en"){
						key = arr[0];							
					}
					else{
						key = arr[1];
					}	
					var obj = {"key":key,"value":val};
					source.push(obj);
					if(val == $input.val()){
						selectedKey = key;
					}			
				}
			}
			var $span = $input.parent().find(".dd_select");
			$span.text(selectedKey);
			$input.parent().click(function(){
				var selected = $span.text() || firstKey;
				dd.biz.util.chosen({
					"source":source,
					"selectedKey":selected,
					onSuccess: function(data) {
						$span.text(data.key);
						$input.val(data.value);
				    },
				    onFail: function(err) {
				    }
				});
					
			});
		})();
		
	</#if>	
	</#noparse>
</#macro>


<#--  日期框脚本组件解析	-->
<#macro convertDateTimeScript component >
	<#noparse><#if (status['</#noparse>${component['name']}<#noparse>']['disabled'])?? && status['</#noparse>${component['name']}<#noparse>']['disabled'] == 'true'>		
	<#else>
		(function(){
			var $input = $('input[name="</#noparse>${(component['name'])!""}<#noparse>"]');
			var dateType = '</#noparse>${component['dateType']!'yyyy-MM-dd'}<#noparse>';
			dateType = dateType.replace("mm","MM")
			convertDateTimeScript($input,dateType);
		})();		
	</#if>	
	</#noparse>
</#macro>