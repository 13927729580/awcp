<#macro parseLayout root components>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		<div class="outerDiv">
	<#else>
		<div class="input_control">
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
</#macro>


<#--  移动端省市县	-->
<#macro convertAddress c>
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
</#macro>


<#--  多选框	-->
<#macro convertCheckbox c>
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
</#macro>


<#--  图片上传框	-->
<#macro convertImage c >
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
</#macro>


<#--  下拉框	-->
<#macro convertSelect c >
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
</#macro>


<#--  多行输入框	-->
<#macro convertTextarea c >
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
</#macro>


<#--  日期选择框	-->
<#macro convertDatetime c>		
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
</#macro>


<#--  文本输入框	-->
<#macro convertInputext c >
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
				    space:{corpId:dd_config.corpId,spaceId:data,isCopy:1 , max:attMax-len},
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
			var dateReg = /^\d{4}-\d{2}-\d{2}$/;
			var dateTimeReg = /^\d{4}-\d{2}-\d{2}\s\d{2}:\d{2}$/;
			var value = $input.val();
			var $span = $input.prev();
			//判断是否有值存在
			if(value.length>=10){
				$span.text(value.substring(0,dateType.length));		
			}
			$input.parent().click(function(){
				value = $input.val();
				//判断是否有值存在
				if(value.length>=10){
					$span.text(value.substring(0,dateType.length));		
				}else{
					if(!dateReg.test(value) && !dateTimeReg.test(value)){
						if(dateType=="yyyy-MM-dd"){
							value = new Date().format("yyyy-MM-dd");
						}
						else{
							value = new Date().format("yyyy-MM-dd hh:mm");
						}
					}
				}
				
				if(dateType=="yyyy-MM-dd"){
					dd.biz.util.datepicker({
						"format":dateType,
						"value":value,
						onSuccess: function(data) {
							$input.val(data.value);	
							$span.text(data.value);				
					    },
					    onFail: function(err) {
					    	console.log(JSON.stringify(err));
					    }
					});
				}
				else{
					dd.biz.util.datetimepicker({
						"format":"yyyy-MM-dd hh:mm",
						"value":value,
						onSuccess: function(data) {
							$input.val(data.value);	
							$span.text(data.value);				
					    },
					    onFail: function(err) {
					    	console.log(JSON.stringify(err));
					    }
					});
				}					
			});
		})();		
	</#if>	
	</#noparse>
</#macro>