<#macro parseLayout root components>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		<div class="form-group">
	<#else>
		<#if (root['offset']??) >
			<div class="col-xs-${root['proportion']} col-sm-offset-${root['offset']}">
		<#else>
			<div class="col-xs-${root['proportion']}">
		</#if>
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

<#macro parseLayout2 root components>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		<tr >		<#-- <tr> -->
	<#else>
		<#if (root['offset']??) >
			<td style='text-align:center' class="col-xs-${root['proportion']} col-sm-offset-${root['offset']}">
		<#else>
			<td style='text-align:center' class="col-xs-${root['proportion']}" colspan="${root['proportion']}">
		</#if>
	</#if>
	<#if (root['childLayouts']??) >
		<#local layoutChilden = root['childLayouts'] >
		<#list layoutChilden?sort_by("order") as layout>
			<@parseLayout2 layout components/>
		</#list>
	<#else>
		<#local rootId = root['pageId']/>
		<#if (components[rootId]??) >
		
		<#local coms = components[rootId]/>
			<#list coms?sort_by("order") as c>
				<@parseComponent c />
			</#list>
		</#if>
	</#if>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		</tr>		<#-- <tr> -->
	<#else>
		</td>
	</#if>
</#macro>

<#macro parseLayout3 root components>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		<tr >		<#-- <tr> -->
	<#else>
		<#if (root['offset']??) >
			<td>
		<#else>
			<td>
		</#if>
	</#if>
	<#if (root['childLayouts']??) >
		<#local layoutChilden = root['childLayouts'] >
		<#list layoutChilden?sort_by("order") as layout>
			<@parseLayout3 layout components/>
		</#list>
	<#else>
		<#local rootId = root['pageId']/>
		<#if (components[rootId]??) >
		
		<#local coms = components[rootId]/>
			<#list coms?sort_by("order") as c>
				<@parseComponent c />
			</#list>
		</#if>
	</#if>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		</tr>		<#-- <tr> -->
	<#else>
		</td>
	</#if>
</#macro>

<#macro parseTitle root components>
	<#local rootId = root['pageId']/>
	<#if (components[rootId]??) >
	   you components[rootId]
       <#local coms = components[rootId]/>
           <#list coms?sort_by("order") as c>
           list ${c}
				<@parseComponent c />
		   </#list>
    </#if>
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
		<#case 1004>
			<@convertRadio c/>
			<#break>
		<#case 1005>
			<@convertTextarea c/>
			<#break>
		<#case 1006>
			<@convertSelect c/>
			<#break>			
		<#case 1007>
			<@convertPassword c/>
			<#break>		
		<#case 1009>
			<@convertLabel c/>
			<#break>
		<#case 1010>
			<@convertHidden c/>
			<#break>
		<#case 1011>
			<@convertFile c/>
			<#break>
		<#case 1012>
			<@convertContainPage c/>
			<#break>
		<#case 1013>
			<@convertSearchCom c/>
			<#break>
		<#case 1014>
			<@convertSpan c/>
			<#break>
		<#case 1015>
			<@convertTips c/>
			<#break>
		<#case 1016>
			<@convertImage c/>
			<#break>
		<#case 1017>
			<@convertdataGrid c/>
			<#break>
		<#case 1018>
			<@convertPureText c/>
			<#break>
		<#case 1025>
			<@tabs c/>
			<#break>
		<#case 1019>
			<@convertkindEditor c/>
			<#break>
		<#case 1020>
			<@convertuserSelect c/>
			<#break>
		<#case 1023>
			<@convertWord c/>
			<#break>
		<#case 1024>
			<@convertAppendix c/>
			<#break>
		<#case 1021>
			<@convertCommondWords c/>
			<#break>	
		<#case 1022>
			<@convertApprovalStatus c/>
			<#break>
		<#case 1026>
			<@convertDynamicSelect c/>
			<#break>
		<#case 1027>
			<@convertTree c/>
			<#break>
		<#case 1028>
			<@convertRoleSelect c/>
			<#break>	
		<#default>
	</#switch>
</#macro>
<#-------------------------------------------输入框组件begin---------------------------------------->
<#macro convertInputext c >
	<input type='text' class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if> 
	
	'
	<#if c['style']?? >
		style='
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>
		${c['style']}'
		
	</#if> 
	
	<#if c['name']?? >
		name='${c['name']}'
	</#if> 
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
		
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		
	</#if> 
	<#if c['others']??>
		<#if c['others']['placeHolder']?? >
			placeHolder='${c['others']['placeHolder']}'
		</#if> 
	</#if>
	id='${(c['pageId'])!""}'
	
	<#if c['description']?? >
		title='${c['description']}'
	</#if> 
	/>
</#macro>
<#-------------------------------------------输入框组件end---------------------------------------->

<#-------------------------------------------树形组件开始---------------------------------------->
<#macro convertTree c >
	<input type='hidden' id='treePageId' value="${c['pageId']}"/>
			<div class="row">
				<div class="col-md-4 col-xs-4">
					<div class="panel tree-panel">
			            <div class="panel-heading">组织结构</div>				            
			            <div class="panel-body">
			            	<ul id="tree1" class="ztree"></ul>
			            </div>
			        </div>
			    </div>
			</div>
				
	</div>

</#macro>
<#-------------------------------------------树形组件end---------------------------------------->

<#-------------------------------------------正文组件begin---------------------------------------->
<#macro convertWord c >
	
		<input type='hidden' id='mtId' value="${c['dynamicPageId']}"/>
		<div class='<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	    <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		'>
 	        <div id="toedit">
 	        </div>
		</div>
	
</#macro>

<#-------------------------------------------正文组件end---------------------------------------->

<#-------------------------------------------附件组件开始---------------------------------------->
<#macro convertAppendix c >

    <div class='
	
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	'>
		<div>附件名称:<input type='text' name='appendixName' id='appendixName'
 		style='width:100%;border-left:0;border-right:0;border-top:0;border-bottom: 1px solid #000000;'>
 		<div><br/>

		<div id='${(c['pageId'])!""}' class="uploader" >
						<input type="hidden" class="orgfile" name="${c['name']}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" />
					    <div class="btns mb10">
					        <div class="picker">上传附件</div>
					                    附件类型:<select name='matinTextType'>
					                    <#if c['mtType']??>
											<option value=c['mtType']/>非版本控制
										</#if>
										<#if c['mtType1']?? >
											<option value=c['mtType1']/>版本控制
										</#if>
										 <#if c['mtType2']?? >
											<option value=c['mtType2']/>URL链接
										</#if>
										 <#if c['mtType3']?? >
											<option value=c['mtType3']/>公文链接
										</#if>
										 <#if c['mtType4']?? >
											<option value=c['mtType4']/>TIF格式扫描附件
										</#if>
										 <#if c['mtType5']?? >
											<option value=c['mtType5']/>JPG格式扫描附件
										</#if>
					                </select>
					       </div>					   
					    <div class="uploader-list oldlist">
					    	<table class="table datatable table-bordered table-hover">
								<thead>
									<tr>
										<th class="hidden"></th>
										<th width="10%" class="text-center">序号</th>
										<th>文件名称</th>
										<th>文件类型</th>
										<th>附件类型</th>
										<th>文件大小</th>
										<!--<th width="150px" class="text-center">操作</th>-->
									</tr>
								</thead>
								<tbody></tbody>
								<tfoot><tr><td colspan="5" class="tips"></td></tr></tfoot>
							</table>	
					    </div>
					</div>
	<div>
	
</#macro>


<#macro convertAppendixScript c >	<#------ 附件组件校验脚本 ----->
	$("#${(c['pageId'])!""}").loadUploader({
		<#if c['maxCount']?? && c['maxCount']?length gt 0 >
			fileNumLimit : '${c['maxCount']}',
		</#if> 
		<#if c['maxSize']?? && c['maxSize']?length gt 0 >
			fileSizeLimit : '${c['maxSize']}',
		</#if>
		<#if c['singleSize']?? && c['singleSize']?length gt 0>
			fileSingleSizeLimit : '${c['singleSize']}',
		</#if>
		<#if c['showType']?? && c['showType']=='2'>
			uploaderStyle:'table',
		</#if>
		<#if c['fileKind']?? && c['fileKind']?length gt 0>
			extensions : '${c['fileKind']}',
		</#if>
		uploaderStyle:"table",isEditor:true
		});
		
</#macro>

<#-------------------------------------------附件组件结束---------------------------------------->



<#-------------------------------------------用户选择框组件begin---------------------------------------->
<#macro convertuserSelect c >
	<input type='hidden' class='
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	'data-input="chosen"
	<#if c['style']?? >
		style='${c['style']}'

	</#if>

	<#if c['name']?? >
		name='${c['name']}'
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >

			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"

	</#if>
	id='${(c['pageId'])!""}'
	/>
	<input type='text' id="${c['name']}" onclick="selectUser('${c['name']}','transfer')" class='chosenName text-ellipsis form-control
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	'
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	>
</#macro>
<#-------------------------------------------用户选择框组件end---------------------------------------->



<#-------------------------------------------动态选择框组件begin---------------------------------------->
<#macro convertDynamicSelect c >
	<input type='text' class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if> 
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	'data-input="select"
	<#if c['style']?? >
		style='${c['style']}'
	
	</#if> 
	
	<#if c['name']?? >
		name='${c['name']}'
	</#if> 
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
			
		
	</#if> 
	<#if c['others']??>
		<#if c['others']['placeHolder']?? >
			placeHolder='${c['others']['placeHolder']}'
		</#if> 
	</#if>
	id='${(c['pageId'])!""}'
	
	<#if c['description']?? >
		title='${c['description']}'
	</#if> 
	/>
</#macro>
<#-------------------------------------------动态选择框组件end---------------------------------------->

<#-------------------------------------------日期begin------------------------------------------>
<#macro convertDatetime c>
	<input type='text' class='app-date' data-type='datetime'
	<#if c['name']?? >
		name='${c['name']}'
	</#if>
	id='${(c['pageId'])!""}'
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0>
		<#noparse><#if (</#noparse> ${c['dataItemCode']} <#noparse>)?? &&(</#noparse> ${c['dataItemCode']} <#noparse>) ?length gt 0></#noparse>
			<#local dateType = 'yyyy-MM-dd' />
			<#if c['dateType']?? && c['dateType']=='form-date-long-medium'>
				<#local dateType = 'yyyy-MM-dd HH:mm' />
			<#elseif c['dateType']?? && c['dateType']=='form-time-medium'>
				<#local dateType = 'yyyy-MM-dd HH:mm' />
			<#elseif c['dateType']?? && c['dateType']=='form-date-long-long'>
				<#local dateType = 'yyyy-MM-dd HH:mm:ss' />
			<#elseif c['dateType']?? && c['dateType']=='form-year'>
				<#local dateType = 'yyyy' />
			<#elseif c['dateType']?? && c['dateType']=='form-year-month'>
				<#local dateType = 'yyyy-MM' />
			</#if>
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?datetime("</#noparse>${dateType}<#noparse>")?string("</#noparse>${dateType}<#noparse>")}</#noparse>"
		<#noparse><#else> </#noparse>
			value=""
		<#noparse></#if> </#noparse>
	/>
	</#if>
</#macro>

<#-------------------------------------------日期end-------------------------------------------->

<#-------------------------------------------单选框radio begin------------------------------------------>
<#macro convertRadio c>
	
	<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
</#macro>
<#-------------------------------------------单选框radio end------------------------------------------>

<#-------------------------------------------多选框checkbook begin------------------------------------------>
<#macro convertCheckbox c>
	<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
</#macro>

<#macro convertChangeScript component >
	<#if component['onchangeScript']??><#--onchange 事件 -->
		$('#${(component['pageId'])!""}').change(function(){
			${component['onchangeScript']}
			});
	</#if>
</#macro>

<#-------------------------------------------角色选择框option赋值 begin------------------------------------------>

<#macro convertOptionScript component >
	

	//获取系统id
	var id="${(component['pageId'])}";
	var systemId="";
	$.ajaxSetup({ 
		async : false
	}); 
	$.post(basePath+"component/getComponentById.do",{"storeId":id},function(data){
		systemId=data.systemId;
	});
	var url = basePath+"unit/listRolesInSysByAjax.do?boxs="+systemId;
	$.ajaxSetup({ 
		async : false
	}); 
	 $.get(url,function(data){
		 $.each(data,function(i,item){
			$('#${(component['pageId'])!""}').append(""+
			
			"<option value='"+item.roleId+"'>"+item.roleName+"</option>"+
			
			"");
		 });
	 });
	
</#macro>

<#-------------------------------------------角色选择框option赋值 end------------------------------------------>

<#macro convertRadioChangeScript component >
	<#if component['onchangeScript']??><#--onchange 事件 -->
		$(':radio[name="${(component['name'])!""}"]').change(function(){
			${component['onchangeScript']}
			});
	</#if>
</#macro>
<#-------------------------------------------多选框checkbook end------------------------------------------>

<#-------------------------------------------下拉框select begin------------------------------------------->
<#macro convertSelect c >
	<select 
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['name']?? >
		name='${c['name']}'
	</#if>
	
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	
	<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0>

			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"

	</#if>
	id='${(c['pageId'])!""}'>
		<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
	</select>
</#macro>
<#-------------------------------------------下拉框select end------------------------------------------>

<#-------------------------------------------角色select start------------------------------------------>
<#macro convertRoleSelect c >
	<select class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	'
	<#if c['style']?? >
		style='${c['style']}'
	
	</#if>	
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['name']?? >
		name='${c['name']}'
	</#if> 	
	<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0>
		
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		
	</#if> 	
	id='${(c['pageId'])!""}'
	<#if c['description']?? >
		title='${c['description']}'
	</#if> 
	>
		<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
	</select>
</#macro>
<#-------------------------------------------角色select end------------------------------------------>

<#-------------------------------------------textarea begin------------------------------------------>
<#macro convertTextarea c >
	<textarea   class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if>
	'
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['name']?? >
		name='${c['name']}'
	</#if> 
	<#if c['rowCount']?? >
		rows='${c['rowCount']}'
	<#else>
		rows='4'
	</#if> 
	id='${(c['pageId'])!""}'	
	<#if c['description']?? >
		title='${c['description']}'
	</#if> 
	><#if c['dataItemCode']??  && c['dataItemCode']?length gt 0><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></#if></textarea>
</#macro>
<#-------------------------------------------textarea end------------------------------------------>

<#macro convertPassword c >
	<input type='password' class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if> 
	'
	<#if c['style']?? >
		style='${c['style']}'
	
	</#if>
	
	<#if c['name']?? >
		name='${c['name']}'
	</#if> 
	
	<#if  c['dataItemCode']??  && c['dataItemCode']?length gt 0>
		
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		
	</#if> 
	<#if c['others']??>
		<#if c['others']['placeHolder']?? >
			placeHolder='${c['others']['placeHolder']}'
		</#if> 
	</#if>
	id='${(c['pageId'])!""}'
	
	<#if c['description']?? >
		title='${c['description']}'
	</#if> 
	/>
</#macro>



<#macro convertLabel c >
	<label  class="control-label
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['isRequired'])?? && status['</#noparse>${c['name']}<#noparse>']['isRequired'] == '1'>required</#if></#noparse> "
	
		<#if c['style']?? >
			style="
			<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>
			${c['style']}"
		
		</#if>
		id="${(c['pageId'])!''}"
		
		<#if c['name']?? >
		name='${c['name']}'
		</#if> 
	>
		
		${c['title']!""}
	</label>
</#macro>

<#macro convertSpan c >
	<span  class="input-group-addon fix-border fix-padding"
		<#if c['style']?? >
			style='${c['style']}'
		
		</#if>
	>
		
		${c['title']!""}
	</span>
</#macro>

<#macro convertPureText c >
	<label  class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>"
		<#if c['style']?? >
			style='${c['style']}'
		
		</#if>
		id="${(c['pageId'])!''}"
		
		<#if c['name']?? >
		name='${c['name']}'
		</#if> 
	>
		<#if c['textType']?? && c['textType']=='1'>
			${c['dataItemCode']}
		<#else>
			<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
				<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>
			</#if> 
		</#if>
	</label>
</#macro>

<#macro convertkindEditor c >
	
		<textarea id='${(c['pageId'])!""}' 
			<#if c['name']?? >
				name='${c['name']}'
			</#if> 
		 class='form-control kindeditor 
			 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>'
			 style="height:100px;"
			 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
			 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		><#if c['dataItemCode']??  && c['dataItemCode']?length gt 0><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></#if></textarea>
		
</#macro>

<#macro convertKindEditorScript c >
	var ${c['name']}_editor = K.create('textarea[name="${c['name']}"]',options);
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'></#noparse>${c['name']}<#noparse>_editor.readonly("false");</#if></#noparse>
</#macro> 

<#macro convertTips c >
	<div class="form-group">
		<div class="alert alert-danger alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
				<p><strong class="icon-info-sign">&nbsp;&nbsp;</strong><span>${c['title']!""}</span></p>
		</div>
	</div>	
</#macro>

<#macro convertImage c >
	<input type="hidden" class="photo" name="${c['name']!""}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"/>
	<input type="file" name="upload_img" id="upload_img" style="width: 300px; height: 30px; line-height: 30px;" onchange="javascript:setImagePreview();"/>
    <span id="localImag"><img id="upload_preview"
    	<#noparse><#if </#noparse>(${c['dataItemCode']}) <#noparse>?? &&</#noparse>(${c['dataItemCode']})<#noparse>?length gt 0></#noparse>
	      		src="<#noparse>${basePath}</#noparse>common/file/showPicture.do?id=<#noparse>${</#noparse>${c['dataItemCode']}<#noparse>}</#noparse>"
	      	<#noparse></#if></#noparse>
     width=-1 height=-1 style="diplay:none"/></span>
</#macro>

<#-------------------------------------------hidden框组件begin---------------------------------------->
<#macro convertHidden c >
	<input type='hidden'
	
	<#if c['name']?? >
		name='${c['name']}'
	</#if> 
	
	<#if c['dataItemCode']??   && c['dataItemCode']?length gt 0>
		
			value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		
	</#if> 
	id='${(c['pageId'])!""}'
	/>
</#macro>
<#-------------------------------------------hidden框组件end---------------------------------------->


<#-------------------------------------------dataGrid框组件begin---------------------------------------->
<#macro convertdataGrid c >
	<div id="${c['pageId']}" class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'> disabled</#if></#noparse>">
		<table width="100%">
				<thead>
				<tr>
					<th class="hidden" width="10"><input id="boxs" type="hidden"/></th>
					<#if c['columns']?? >
					 <#list c['columns']?sort_by("order") as item>
						<th data-width="${item['columnWidth']!''}" field="${item['columnField']!''}"> ${item['columnTitle']!''}</th>
					</#list>
					</#if>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
			<tfoot>
			</tfoot>
		</table>
	</div>
		
</#macro>

<#macro convertdataGridScript c >
	
	$("#${c['pageId']}").dataGrid({url:'<#noparse>${basePath}</#noparse>document/refreshDataGrid.do?componentId=${c['pageId']}',
								  data:{<#if c['connditions']?? ><#list c['connditions'] as item>${item['paramKey']!''}:
								  <#if item['isFinal']?? && item['isFinal']=='1'>
								 	 '${item['paramValue']!''}'
								  <#else>
								  	 '<#noparse>${(</#noparse>${item['paramValue']}<#noparse>)!''}</#noparse>'
								  </#if>
								  ,</#list></#if>pageSize:10}});
	
	<#if c['operateAdd']?? && c['operateAdd']=='1'>
		$("#${(c['pageId'])}_Add").click(function(){
			
			var paramData = new Map();
			<#if c['param']?? && c['param']?size gt 0>
				<#list c['param'] as item>
					paramData.put('${item['storeParamId']}','<#noparse>${(</#noparse>${item['exportParam']}<#noparse>)!''}</#noparse>');
				</#list>
			</#if>
			
			top.dialog({
				id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
				title : '新增...',
				url : "<#noparse>${basePath}</#noparse>document/view.do?dynamicPageId=${c['alertPage']}",
				data: paramData,
				width : ${(c['viewWidth'])!600},
				height: ${(c['viewHeight'])!600},
				onclose : function() {
					if (this.returnValue) {
						$("#${c['pageId']}").dataGrid({url:'<#noparse>${basePath}</#noparse>document/refreshDataGrid.do?componentId=${c['pageId']}',
								  data:{<#if c['connditions']?? ><#list c['connditions'] as item>${item['paramKey']!''}:
								  <#if item['isFinal']?? && item['isFinal']=='1'>
								 	 '${item['paramValue']!''}'
								  <#else>
								  	 '<#noparse>${(</#noparse>${item['paramValue']}<#noparse>)!''}</#noparse>'
								  </#if>
								  ,</#list></#if>pageSize:10}});
					}
				}
			}).showModal();
			
			return false;
			
		});
	</#if>
	
	<#if c['operateDelete']?? && c['operateDelete']=='1'>
		$("#${(c['pageId'])}_Delete").click(function(){
			var ids = $("#${c['pageId']} table.gridtable").data("ids");
			if(ids == undefined){
				alert('请选择数据');
			}else{
				var data = {<#if c['connditions']?? ><#list c['connditions'] as item>${item['paramKey']!''}:
								  <#if item['isFinal']?? && item['isFinal']=='1'>
								 	 '${item['paramValue']!''}'
								  <#else>
								  	 '<#noparse>${(</#noparse>${item['paramValue']}<#noparse>)!''}</#noparse>'
								  </#if>
								  ,</#list></#if>pageSize:10};
				data.method = 'delete';
				data._selects = ids;
				$("#${c['pageId']}").dataGrid({url:'<#noparse>${basePath}</#noparse>document/refreshDataGrid.do?componentId=${c['pageId']}',
								  data:data});
			}
			return false;
		});
	</#if>
	
	<#if c['operateEdit']?? && c['operateEdit']=='1'>
		$("#${(c['pageId'])}_Edit").click(function(){
			var ids = $("#${c['pageId']} table.gridtable").data("ids");
			top.dialog({
				id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
				title : '编辑...',
				url : "<#noparse>${basePath}</#noparse>document/view.do?dynamicPageId=${c['alertPage']}&id=" + ids,
				width : ${(c['viewWidth'])!600},
				height: ${(c['viewHeight'])!600},
				onclose : function() {
					if (this.returnValue) {
						$("#${c['pageId']}").dataGrid({url:'<#noparse>${basePath}</#noparse>document/refreshDataGrid.do?componentId=${c['pageId']}',
								  data:{<#if c['connditions']?? ><#list c['connditions'] as item>${item['paramKey']!''}:
								  <#if item['isFinal']?? && item['isFinal']=='1'>
								 	 '${item['paramValue']!''}'
								  <#else>
								  	 '<#noparse>${(</#noparse>${item['paramValue']}<#noparse>)!''}</#noparse>'
								  </#if>
								  ,</#list></#if>pageSize:10}});
					}
				}
			}).showModal();
			
			return false;

		});
	</#if>
	
</#macro>
<#-------------------------------------------dataGrid框组件end---------------------------------------->



<#-------------------------------------------包含页面框框组件begin---------------------------------------->
<#macro convertContainPage c >
	<div name="${c['name']!''}" class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'> disabled</#if>
	</#noparse>">
	<#if c['layoutAndCom']?? >
		${c['layoutAndCom']}
	</#if></div> 
</#macro>


<#macro convertContainPageScript component >	<#------ Image加载脚本 ----->
	<#if component['pageJScript']??>
				${component['pageJScript']}
	</#if>
</#macro>
<#-------------------------------------------包含页面框框组件end---------------------------------------->


<#-------------------------------------------搜索框框框组件begin---------------------------------------->
<#macro convertSearchCom c >
	<#------------------------------------------
	<div class="row" id="searchform">
		<div id="collapseButton" class="in">
			<form class="form-horizontal form-condensed" id="createForm" action="<#noparse>${basePath}</#noparse>document/view.do" role="customForm" method="post">
			<#noparse>
			<input type="hidden" name="currentPage" value="${currentPage!"1"}">	
    		<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
			<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
			<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
			<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
			<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
			<input type="hidden" name="orderBy" value="${ (vo.orderBy)!""}" id="orderBy"/>
			<input type="hidden" name="allowOrderBy" value="${ (vo.allowOrderBy)!"1"}" id="allowOrderBy"/>
			</#noparse>		
				<#if c['layoutAndCom']?? >
					${c['layoutAndCom']}
				</#if> 
				<div class="btn-group">
					<button class="btn btn-primary" type="submit">搜索</button>
				</div>
			</form>
		</div>
	</div>
	------------------->
	
	<div class="row row-table" id="searchform" style="display:none;width:100%">
		<div id="collapseButton" class="in">

			<#----<form class="clearfix form-condensed" id="createForm" action="<#noparse>${basePath}</#noparse>document/view.do" role="customForm" method="post">
			--->

			<form class="clearfix" id="createForm" action="<#noparse>${basePath}</#noparse>document/view.do" role="customForm" method="post">

			<#noparse>
			<input type="hidden" name="currentPage" value="${currentPage!"1"}">	
    		<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
    		<input type="hidden" name="selectId" value="${ (vo.selectId)!""}" id="selectId"/>
			<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
			<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
			<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
			<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
			<input type="hidden" name="orderBy" value="${ (vo.orderBy)!""}" id="orderBy"/>
			<input type="hidden" name="allowOrderBy" value="${ (vo.allowOrderBy)!"1"}" id="allowOrderBy"/>
			</#noparse>	
			<div class="col-xs-10 col-table">	
				<#if c['layoutAndCom']?? >
					${c['layoutAndCom']}
				</#if> 
			</div>
			<div class="col-xs-2 btn-group col-table">
				
				<button class="btn btn-primary" type="submit">搜索</button>
				<#----<button class="btn" type="reset">重置</button>-->
			</div>
			</form>
		</div>
	</div>
	
</#macro>
<#-------------------------------------------搜索框组件end---------------------------------------->
<#-------------------------------------------文件上传框组件begin---------------------------------------->
<#macro  convertFile c >
    <div id="uploading">
        <ul>
            <li class="upload_imgs" id='${(c['pageId'])!""}'>
                <img class="file_imgs" src="../../npc/images/ion_ins.png">
                <input type="hidden" class="orgfile" name="${c['name']}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" />
                <input id="file_input" onchange="readFile(this)" name="fileinput" class="upload" type="file">
            </li>
        </ul>
    </div>
	<#--<div id='${(c['pageId'])!""}'>
		<div class="ui-li-has-thumb" >
         		<div id="currentFilePath"></div>
         </div>
         <div class="ui-li-has-thumb" >
         			<span class="file"><div><i class="icon-plus-sign-alt icon-x"></i>选择文件</div>
    						<input type="hidden" class="orgfile" name="${c['name']}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" />
    						<input type="file" name="fileToUpload" id="fileToUpload"/>
					</span>
                    <span class="upfile" onclick="uploadFile()"><i class="icon-upload-alt icon-x"></i>立即上传
					</span>
         </div>
                <div style="background-size:cover;">
                    <table id="fileList" class=" table-stroke"  data-role="table"  data-mode="columntoggle">
                            <tr>
                                <th colspan="1">文件名称</th>
                                <th colspan="1">操作</th>
                            </tr>
                    </table>
				</div>
		</div>-->
</#macro>


<#macro convertFileScript c >	<#------ file上传组件校验脚本 ----->
	$("#${(c['pageId'])!""}").loadUploader({
		<#if c['maxCount']?? && c['maxCount']?length gt 0 >
			fileNumLimit : '${c['maxCount']}',
		</#if> 
		<#if c['maxSize']?? && c['maxSize']?length gt 0 >
			fileSizeLimit : '${c['maxSize']}',
		</#if>
		<#if c['singleSize']?? && c['singleSize']?length gt 0>
			fileSingleSizeLimit : '${c['singleSize']}',
		</#if>
		<#if c['showType']?? && c['showType']=='2'>
			uploaderStyle:'table',
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>
		isReadonly:true
		</#if></#noparse>
		<#if c['fileKind']?? && c['fileKind']?length gt 0>
			extensions : '${c['fileKind']}'
		</#if>});
</#macro>


<#macro convertImageScript c >	<#------ Image加载脚本 ----->
	loadUploadPreview("${c['pageId']}_Img","${c['pageId']}",${c['imageWidth']},${c['imageHeight']});
</#macro>
<#-------------------------------------------file框组件end---------------------------------------->


<#-----------------button begin------------------------------------------------------------->
<#macro convertButton c >
	
		<button id='${c.getPageId()}' title="${c.getDescription()!''}" 
				class="${c.getCss()!''} btn <#if c.getStyle()??>${c.getStyle()}</#if>  <#if c.getColor()??>btn-${c.getColor()}</#if> 
				
					<#noparse>
					<#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden'])?? && pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden']>
						hidden
					</#if> 
					</#noparse>
				" 
				<#noparse>
					<#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['backId'])??>
						backId="${pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['backId']!''}"
					</#if> 
				</#noparse>
			<#if c.getExtbute()??>
				target='${c.getExtbute()['target']!''}'
			</#if> 
				
			 >
			<i class='<#if c.getIcon()??>${c.getIcon()}</#if>'></i>
			${c.getName()}</button>
	
</#macro>

<#macro convertButton2 c>
	
		<button id='${c.getPageId()}'  class="mbtn_${c.getActType()!''}" 

					<#noparse>
					<#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden'])?? && pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden']>
						style="display:none"
					</#if>
					</#noparse> 
					<#noparse>
					<#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['backId'])??>
						backId="${pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['backId']!''}"
					</#if>
				</#noparse>
			<#if c.getExtbute()??>
				target='${c.getExtbute()['target']!''}'
			</#if>
					>
			${c.getName()}</button>
	
</#macro>

<#macro convertDialog pageAct>
	var dialogId = "platform_artdialog_${pageAct.getPageId()}";
	var mydialog = top.dialog.get(dialogId);
	if(typeof(mydialog) == "undefined"){	
		mydialog = dialog({
			id : dialogId,
			title : "${pageAct.getTittle()!"提示"}",
			<#if pageAct.getContentType() ==1>
				content : "${pageAct.getContent()}",
			<#elseif pageAct.getContentType() ==2>
				url : "${pageAct.getContent()}",
			<#elseif pageAct.getContentType() ==3>
				<#local docId = pageAct.getContent()?split(",")[0]>
				<#local pageId = pageAct.getContent()?split(",")[1]>
				url : "url?docId=${docId}&pageId=${pageId}",
			</#if>
			
				<#local buttonStr = pageAct.getButtons()>
			<#if buttonStr?contains("1") >
				okValue : '确定',
				ok: function () {
						${pageAct.getClientScript()};
				return true;
				},
			</#if>
			<#if buttonStr?contains("0")>
				cancelValue: '取消',
				cancel: true,
			</#if>
			height : "${pageAct.getHeight()}",
			width : "${pageAct.getWidth()}",
			});
			};
			if(!mydialog.open){
			mydialog.show();
			} else {
			mydialog.close();
			}
</#macro>

<#macro convertScript c>
$("#${c.getPageId()}").click(function(){	
	<#if c.getActType()==2010>
	if(count==1){
		<#if c.isConfirm()>
			<@convertDialog c/>
		<#else>
			<#if c.getClientScript()??>
				${c.getClientScript()}
			</#if>}else{
				alert("请选择一条记录！");}
		</#if>
	<#else>
		<#if c.isChooseValidate()??&&c.isChooseValidate()>
			<#noparse>
				<#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['chooseNum'])?? </#noparse> >
					var choosenum = <#noparse>${pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['chooseNum']};
				</#if> 
			</#noparse>
			
			if(count!=choosenum){
				if(count<choosenum){
					var difference = choosenum-count;
					alertMessage("请选择"+choosenum+"条数据！请再选择"+difference+"条数据!");
				}else if(count>choosenum){
					var difference = count-choosenum;
					alertMessage("请选择"+choosenum+"条数据！请少选择"+difference+"条数据!");
				}
			}else{
				<#if c.isConfirm()>
					<@convertDialog c/>
				<#else>
					<#if c.getClientScript()??>
						${c.getClientScript()}
					</#if>
				</#if>
			}
		<#else>
			<#if c.isConfirm()>
				<@convertDialog c/>
			<#else>
				<#if c.getClientScript()??>
					${c.getClientScript()}
				</#if>
			</#if>			
		</#if>		
	 </#if>
		});
</#macro>
<#-----------------button end------------------------------------------------------------->
<#-----------------组件脚本解析 begin------------------------------------------------------------->
<#macro convertComponentScriptApp component>

	<#local componetType=component['componentType']?number>
	<#switch componetType>
		
		<#case 1017>
			<@convertdataGridScript component/>
			<#break>
		<#case 1003>
			<@convertChangeScript component/>
			<#break>
		<#case 1004>
			<@convertRadioChangeScript component/>
			<#break>
		<#case 1006>
			<@convertChangeScript component/>
			<#if component['supportSearch']?? && component['supportSearch']=='1'>
				
				<#if component['supportMulti']?? && component['supportMulti']=='1'>
				$('#${(component['pageId'])!""}').attr("multiple","multiple");
			
				$('#${(component['pageId'])!""}').select2({
					 maximumSelectiossssnLength: ${(component['selectNumber'])}
				});
				
				<#else>
				$('#${(component['pageId'])!""}').removeAttr("multiple");
				
				$('#${(component['pageId'])!""}').select2();
			    
			  </#if>
			</#if>
			<#break>
		<#case 1028>
			<@convertChangeScript component/>
			<@convertOptionScript component/>
			<#if component['supportSearch']?? && component['supportSearch']=='1'>
				
				<#if component['supportMulti']?? && component['supportMulti']=='1'>
				$('#${(component['pageId'])!""}').attr("multiple","multiple");
			
				$('#${(component['pageId'])!""}').select2({
					 maximumSelectiossssnLength: ${(component['selectNumber'])}
				});
				
				<#else>
				$('#${(component['pageId'])!""}').removeAttr("multiple");
				
				$('#${(component['pageId'])!""}').select2();
			    
			    </#if>
			    
			</#if>
			<#break>
		<#default>
		
	</#switch>
	
<#----	
<#if component['componentType']=='1012'>
	<#if component['pageJScript']??>
		${component['pageJScript']}
	</#if>	
<#elseif component['componentType']=='1017'>
	<@convertdataGridScript component/>
<#else>
	<#if component['componentType']=='1006'>
		
	</#if>
	<#if component['onchangeScript']??>
		$('#${(component['pageId'])!""}').change(function(){
			${component['onchangeScript']}
			});
	</#if>
</#if>	----->

</#macro>

<#-----------------组件脚本解析 end------------------------------------------------------------->

<#------------------------------列组件 begin------------------------------------------------------->

<#macro parseColumn c >
	<#--<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] != 'true'></#noparse> -->
		<th <#if c['width']??>	data-width="${c['width']}"</#if>
		class=" <#if c['alloworderby']?? && c['alloworderby'] == "1">sort</#if> text-ellipsis"  title="<#if c['columnName']?? >${c['columnName']}</#if>"
		style="
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>
		"
		>
		<#if c['columnName']?? >
		${c['columnName']}
		</#if>
		<#if c['alloworderby']?? && c['alloworderby'] == "1"><span id="${c['dataItemCode']!''}" class="hidden"></span><i class="icon-sort"></i></#if>
		<#--icon-caret-up icon-caret-down-->
		
		</th> 
	<#--<#noparse></#if></#noparse>-->
</#macro>

<#macro parseColumnData c >
	<#--<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] != 'true'></#noparse> -->
		<td <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>>
			<#if (c['columnFormat']!'')?length gt 0>
				<#if c['columnFormat']=='1'>	<#--日期-->
					<#noparse><#if (</#noparse>${c['dataItemCode']!''}<#noparse>)??></#noparse>	<#--如果日期值不为空-->
						<#noparse><#if (</#noparse>${c['dataItemCode']!''}<#noparse>!'')?length gt 10></#noparse>
							<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?datetime("yyyy-MM-dd HH:mm:ss")?string("</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
						<#noparse><#else></#noparse>
							<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?datetime("yyyy-MM-dd")?string("</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
						<#noparse></#if></#noparse>
					<#noparse></#if></#noparse>
				<#elseif c['columnFormat']=='2'>	<#--数字-->
					<#noparse><#if (</#noparse>${c['dataItemCode']!''}<#noparse>)??></#noparse>
						<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?number?string("</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
					<#noparse></#if></#noparse>
				<#elseif c['columnFormat']=='3'>	<#--字符串-->
						<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?substring(0,"</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
				</#if>
			<#else>
				<#noparse>${</#noparse>${c['dataItemCode']!''}<#noparse>!''}</#noparse>
			</#if>
		</td>&emsp;
	<#--<#noparse></#if></#noparse>-->
</#macro>

<#macro parseColumnData1 c >
	<#--<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] != 'true'></#noparse> -->
		<h3><#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>
			<#if (c['columnFormat']!'')?length gt 0>
				<#if c['columnFormat']=='1'>	<#--日期-->
					<#noparse><#if (</#noparse>${c['dataItemCode']!''}<#noparse>)??></#noparse>	<#--如果日期值不为空-->
						<#noparse><#if (</#noparse>${c['dataItemCode']!''}<#noparse>!'')?length gt 10></#noparse>
							<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?datetime("yyyy-MM-dd HH:mm:ss")?string("</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
						<#noparse><#else></#noparse>
							<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?datetime("yyyy-MM-dd")?string("</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
						<#noparse></#if></#noparse>
					<#noparse></#if></#noparse>
				<#elseif c['columnFormat']=='2'>	<#--数字-->
					<#noparse><#if (</#noparse>${c['dataItemCode']!''}<#noparse>)??></#noparse>
						<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?number?string("</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
					<#noparse></#if></#noparse>
				<#elseif c['columnFormat']=='3'>	<#--字符串-->
						<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)?substring(0,"</#noparse>${c['columnPatten']}<#noparse>")}</#noparse>
				</#if>
			<#else>
				<#noparse>${</#noparse>${c['dataItemCode']!''}<#noparse>!''}</#noparse>
			</#if></h3><p>
	<#--<#noparse></#if></#noparse>-->
</#macro>

<#------------------------------列组件 end--------------------------------------------------------->

<#------------------------------校验解析begin----------------------------------------------------->
<#macro validate c >	
	<#local validateType=c['validatorType']?number>
	<#switch validateType>
		<#case 1>
			<@convertInputValidate c/>
			<#break>
		<#case 2>
			<@convertRegexValidate c/>
			<#break>
		<#case 3>
			<@convertCompareValidate c/>
			<#break>
		<#case 4>
			<@convertCustomValidate c/>
			<#break>
		<#default>
	</#switch>
	
</#macro>



<#------------------------------校验解析end----------------------------------------------------->

<#macro convertInputValidate c >
	.inputValidator({
	<#if c['type']?? >
		type:"${c['type']}"
	</#if>
	<#if c['min']?? >
		,min:<#if c['type']?? && (c['type']=='string' || c['type']=='date' || c['type']=='datatime')>"</#if>${c['min']}<#if c['type']?? && (c['type']=='string' || c['type']=='date' || c['type']=='datatime')>"</#if>
	</#if>
	<#if c['max']?? >
		,max:<#if c['type']?? && (c['type']=='string' || c['type']=='date' || c['type']=='datatime')>"</#if>${c['max']}<#if c['type']?? && (c['type']=='string' || c['type']=='date' || c['type']=='datatime')>"</#if>
	</#if>
	
	<#if c['onError']?? >
		,onError:"${c['onError']}"
	</#if>
	<#if c['onErrorMin']?? >
		,onErrorMin:"${c['onErrorMin']}"
	</#if>
	<#if c['onErrorMax']?? >
		,onErrorMax:"${c['onErrorMax']}"
	</#if>
	<#if c['empty']?? >
		,empty:"${c['empty']}"
	</#if>
	})
</#macro>

<#macro convertRegexValidate c >
	.regexValidator({
	<#if c['dataType']?? >
		dataType:"${c['dataType']}'"
		regExp:"${c['regExp']}"
	<#else>
		regExp:"${c['regExp']}"
	</#if>
	<#if c['compareType']?? >
		,compareType:"${c['compareType']}"
	</#if>
	<#if c['param']?? >
		,param:"${c['param']}"
	</#if>
	<#if c['onError']?? >
		,onError:"${c['onError']}"
	</#if>
	})
</#macro>

<#macro convertCompareValidate c >
	.compareValidator({
	<#if c['desID']?? >
		dataType:"${c['desID']}",
	</#if>
	<#if c['operateor']?? >
		,operateor:"${c['operateor']}"
	</#if>
	<#if c['dataType']?? >
		,dataType:"${c['dataType']}"
	</#if>
	<#if c['onError']?? >
		,onError:"${c['onError']}"
	</#if>
	})
</#macro>

<#macro convertCustomValidate c >
	.functionValidator({fun:${c['clientScript']}})
</#macro>


<#------------------------------校验解析end------------------------------------------------------->

<#-------------------------------------------commondwords begin------------------------------------------>
<#macro convertCommondWords c >
	 
                <div width="100%" height="100px" id="suggestion_table">
                  <div id='suggessBody${(c['pageId'])!""}'>
                      <div class='col-xs-12' colspan='12'>
                      
                      
                      	<p  class='pull-right clearfix'>
                      	
                      		<#if c['freeFlow']?? && c['freeFlow']=='1'>
								
							<input type='hidden' id='but${(c['pageId'])!""}'  value='添加意见' 
                      		class='btn btn-primary <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>' 
                      		
                      		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
							<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
                      		
                      		onClick="openDialog('${(c['pageId'])!""}','${(c['showType'])!""}')" style='margin-right:8px;'/>
				
				     		<#else>
								
							<input type='hidden' id='but${(c['pageId'])!""}' value='添加意见' 
                      		class='btn btn-primary <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>style="display:none"</#if></#noparse>' 
                      		
                      		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
							<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
                      		
                      		onClick="openDialog('${(c['pageId'])!""}','0')" style='margin-right:8px;'/>
			    
			          	    </#if>
                      	</p>
                      	<span class='sign' id='${(c['pageId'])!""}'>
                           
                        </span>
                      </div>
                  </div>
                </div>
    
	 
</#macro>
<#-------------------------------------------commondwords end-------------------------------------------->


<#macro convertCommondWordsScript component >

	$(function(){
		var pageId='${(component['pageId'])!""}';
	    createBox(pageId);
	    //var showId='suggessText'
		initCommondWords(pageId);
	});

</#macro>


<#-------------------------------------------approvalStatus begin------------------------------------------>
<#macro convertApprovalStatus c >
	<div class="panel dashboard">
		<style>
	   	  li{list-style:none;}
	    </style>
	    <div class="panel-heading">
	      <div class="panel-actions">
	        <a href="javascript:;" onclick="$(this).parents('.panel').find('table tbody').hide()">收缩状态</a>
	        <a href="javascript:;" onclick="$(this).parents('.panel').find('table tbody').show()">展开状态</a>
	      </div>
	      <div class="workflowstatus">
	        	<li></li>
	      </div>
	    </div>
	    <table class="table table-striped table-hover approvalStatus">
	      <thead>
	        <tr>
	          <th>处理步骤</th>
	          <th>处理人</th>
	          <th>收到时间</th>
	          <th>处理时间</th>
	          <th>状态</th>
	          <th>处理意见</th>
	          <th>下一步骤</th>
	          <th>接收人</th>
	        </tr>
	      </thead>
	      <tbody>
	      </tbody>
	    </table>
	  </div>
</#macro>
<#-------------------------------------------approvalStatus end-------------------------------------------->

<#--
 <@parseLayout layout components/>  
<@convertButton pageAct />

<@convertButtonScript pageAct /> -->

<#-- switch 样例
<#switch value>
 <#case refValue1>
 ...
 <#break>
 <#case refValue2>
 ...
 <#break>
 ...
 <#case refValueN>
 ...
 <#break>
 <#default>
 ...
</#switch>
-->

<#----------------------------------------------tabs组件------------------------------------------------>
	<#macro tabs c>
		<#if c['tags']??>
			<ul class="nav nav-tabs" id="${c['name']}">
					<#list c['tags']?sort_by("order") as item>
						<#if item['tagsType']??&&item['tagsType'] == '1'>
							<li <#if item['isLoad']??&&item['isLoad']=='1'>class='active'</#if> id="li${item['tagsId']}" ><a href="#tab_${item['relatePageId']}" data-toggle="tab" <#if item['isLoad']??&&item['isLoad']=='1'>isLoad="${item['isLoad']}"</#if>><#if item['tagsTitle']??>${item['tagsTitle']}</#if></a></li>
						<#elseif item['tagsType']??&&item['tagsType'] == '2'>
							<li class="dropdown 
								<#if item['isLoad']??&&item['isLoad']=='1'>
									active
								<#else>
									<#if item['childTags']??>
										<#list item['childTags']?sort_by("order") as child>
											<#if child['isLoad']??&&child['isLoad']=='1'>
												active
												<#break>
											</#if>
										</#list>
									</#if>
								</#if>">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown"><#if item['tagsTitle']??>${item['tagsTitle']}</#if><b class="caret"></b></a>
								<ul class="dropdown-menu">
								<#if item['childTags']??>
									<#list item['childTags']?sort_by("order") as child>
										<li id="li${child['tagsId']}"><a href="#tab_${child['relatePageId']}" data-toggle="tab" <#if child['isLoad']??&&child['isLoad']=='1'>isLoad="${child['isLoad']}"</#if>><#if child['tagsTitle']??>${child['tagsTitle']}</#if></a></li>
									</#list>
								</#if>
								</ul>
							</li>
						</#if>
					</#list>
			</ul>
			<div class="tab-content" style="<#if c['imageHeight']??>height:${c['imageHeight']}px;</#if><#if c['width']??>${c['width']}</#if>">
				<#list c['tags']?sort_by("order") as item>
					<#if item['tagsType']??&&item['tagsType'] == '1'>
						<div class="tab-pane in active" id="tab_${item['relatePageId']}" >
						</div>
					<#elseif item['tagsType']??&&item['tagsType'] == '2'>
						<#if item['childTags']??>
									<#list item['childTags']?sort_by("order") as child>
										<div class="tab-pane in active" id="tab_${child['relatePageId']}">
										</div>
									</#list>
						</#if>
					</#if>
				</#list>
			</div>
		</#if>
		
	</#macro>
	
	<#macro tabsActiveScript c>
		<#if c['tags']??>
		
			function initIsLoad(){
				var as = <#noparse>$("#</#noparse>${c['name']}").find("a");
				for(var i=0;i<as.length;i++){
					if(typeof($(as[i]).attr("isLoad"))!="undefined"&&$(as[i]).attr("isLoad")=="1"){
						var a_href = $(as[i]).attr("href");
						var dynamicPageId = a_href.substring(a_href.lastIndexOf("_")+1);
						//获取所有的hidden框的值作为参数传过去
						var hiddenCom = $("form div input:hidden");
						var condition = "";
						for(var i=0;i<hiddenCom.length;i++){
							if(typeof(hiddenCom[i].name)!="undefined"&&hiddenCom[i].name!=null&&hiddenCom[i].name!=""){
								condition = condition+"&"+hiddenCom[i].name+"="+hiddenCom[i].value;
							}
						}
						condition = condition + "&" + "dynamicPageId="+dynamicPageId;
						condition = condition.substring(1,condition.length); 
						if(typeof($(a_href).attr("type"))=="undefined"){
							$.ajax({
								type:'post',
								dataType:'text',
								url:basePath+'document/view.do',
								data:condition,
								success:function(result){
									
										$(a_href).attr("type","1");
										var comstr = result.substring(result.indexOf("<--MYLayoutAndCom-->")+"<--MYLayoutAndCom-->".length, result.indexOf("<--MYLayoutAndCom/-->"));
										$(a_href).append(comstr);
										var pageJScript = result.substring(result.indexOf("<--MYpageJScript-->")+"<--MYpageJScript-->".length, result.indexOf("<--MYpageJScript/-->"));
										var pageActScript = result.substring(result.indexOf("<--MYpageActScript-->")+"<--MYpageActScript-->".length, result.indexOf("<--MYpageActScript/-->"));
										var pageValidate = result.substring(result.indexOf("<--MYvalidate-->")+"<--MYvalidate-->".length, result.indexOf("<--MYvalidate/-->"));
										var comScript = "\n " + pageJScript + "\n " + pageActScript + + "\n";
										$(a_href).append("<script type='text/javascript'>"+comScript+"<"+"/script>");
									}
								
							});
						}
					}
				}
			}
			
			initIsLoad();
			
			<#noparse>$("#</#noparse>${c['name']} a").click(function(){
					var a_href = $(this).attr("href");
					if(a_href!=null&&a_href!=""&&a_href!="#"){
						var dynamicPageId = a_href.substring(a_href.lastIndexOf("_")+1);
						//获取所有的hidden框的值作为参数传过去
						var hiddenCom = $("form div input:hidden");
						var condition = "";
						for(var i=0;i<hiddenCom.length;i++){
							if(typeof(hiddenCom[i].name)!="undefined"&&hiddenCom[i].name!=null&&hiddenCom[i].name!=""){
								condition = condition+"&"+hiddenCom[i].name+"="+hiddenCom[i].value;
							}
						}
						condition = condition + "&" + "dynamicPageId="+dynamicPageId;
						condition = condition.substring(1,condition.length); 
						if(typeof($(a_href).attr("type"))=="undefined"){
							$.ajax({
								type:'post',
								dataType:'text',
								url:basePath+'document/view.do',
								data:condition,
								success:function(result){
									
										$(a_href).attr("type","1");
										var comstr = result.substring(result.indexOf("<--MYLayoutAndCom-->")+"<--MYLayoutAndCom-->".length, result.indexOf("<--MYLayoutAndCom/-->"));
										$(a_href).append(comstr);
										var pageJScript = result.substring(result.indexOf("<--MYpageJScript-->")+"<--MYpageJScript-->".length, result.indexOf("<--MYpageJScript/-->"));
										var pageActScript = result.substring(result.indexOf("<--MYpageActScript-->")+"<--MYpageActScript-->".length, result.indexOf("<--MYpageActScript/-->"));
										var pageValidate = result.substring(result.indexOf("<--MYvalidate-->")+"<--MYvalidate-->".length, result.indexOf("<--MYvalidate/-->"));
										var comScript = "\n " + pageJScript + "\n " + pageActScript + + "\n";
										$(a_href).append("<script type='text/javascript'>"+comScript+"<"+"/script>");
									}
								
							});
						}
					}
				});
		</#if>
		<#--<#if c['tabScript']??>
			${c['tabScript']}
		</#if>-->
	</#macro>
	
	
<#macro parseLayoutNoHidden root components>
	
		<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
			<div class="form-group">
		<#else>
			<#if (root['offset']??) >
				<div class="col-xs-${root['proportion']} col-sm-offset-${root['offset']}">
			<#else>
				<div class="col-xs-${root['proportion']}">
			</#if>
		</#if>
	
	<#-- -->
	<#if (root['childLayouts']??) >
		<#local layoutChilden = root['childLayouts'] >
		<#list layoutChilden?sort_by("order") as layout>
			<@parseLayoutNoHidden layout components/>
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
				<@parseComponentNoHidden c />
			</#list>
			
			<#if comSize gt  1 >
				</div>
			</#if>
			
		</#if>
	</#if>
	</div>
</#macro>

<#macro parseComponentNoHidden c >
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
		<#case 1004>
			<@convertRadio c/>
			<#break>
		<#case 1005>
			<@convertTextarea c/>
			<#break>
		<#case 1006>
			<@convertSelect c/>
			<#break>			
		<#case 1007>
			<@convertPassword c/>
			<#break>		
		<#case 1009>
			<@convertLabel c/>
			<#break>
		<#case 1011>
			<@convertFile c/>
			<#break>
		<#case 1012>
			<@convertContainPage c/>
			<#break>
		<#case 1013>
			<@convertSearchCom c/>
			<#break>
		<#case 1014>
			<@convertSpan c/>
			<#break>
		<#case 1015>
			<@convertTips c/>
			<#break>
		<#case 1016>
			<@convertImage c/>
			<#break>
		<#case 1017>
			<@convertdataGrid c/>
			<#break>
		<#case 1018>
			<@convertPureText c/>
			<#break>
		<#case 1021>
			<@convertCommondWords c/>
			<#break>
		<#case 1022>
			<@convertApprovalStatus c/>
			<#break>
		<#case 1023>
			<@convertWord c/>
			<#break>
		<#case 1024>
			<@convertAppendix c/>
			<#break>
		<#case 1025>
			<@tabs c/>
			<#break>
		<#case 1026>
			<@convertDynamicSelect c/>
			<#break>
		<#case 1027>
			<@convertTree c/>
			<#break>
		<#case 1028>
			<@convertRoleSelect c/>
			<#break>
		<#default>
	</#switch>
</#macro>