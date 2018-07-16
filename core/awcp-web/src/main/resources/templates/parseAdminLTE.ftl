<#macro parseLayout root components>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		<div class="form-group">		<#-- <tr> -->
	<#else>
		<div class="col-sm-${root['proportion']} col-md-${root['proportion']} col-md-${root['proportion']}">
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
			<#list coms?sort_by("order") as c>
				<#if c['componentType']!='1036' >
					<@parseComponent c />
				</#if>
			</#list>
		</#if>
	</#if>
	<#if (root['layoutType']?number ==2) >			<#--如果是行 -->
		</div>	<#-- <tr> -->
	<#else>
		</div>
	</#if>
</#macro>
<#--解析组件CSS文件 -->
<#macro parseComponentCSSFile c >
	<#local componetType=c['componentType']?number>
	<#switch componetType>
		<#case 1002>
			cssFile["<#noparse>${basePath}</#noparse>template/AdminLTE/css/bootstrap-datetimepicker.min.css"]=null;
			<#break>
		<#case 1003>
		<#case 1004>
			cssFile["<#noparse>${basePath}</#noparse>template/AdminLTE/css/square/green.css"]=null;
			<#break>
		<#case 1006>
			cssFile["<#noparse>${basePath}</#noparse>template/AdminLTE/css/select2/select2.css"]=null;
			<#break>
		<#case 1011>
		<#case 1016>
			cssFile["<#noparse>${basePath}</#noparse>template/AdminLTE/css/fileinput/fileinput.css"]=null;
			<#break>	
		<#case 1017>
			cssFile["<#noparse>${basePath}</#noparse>template/easyui/css/easyui.css"]=null;
			<#break>
		<#default>
	</#switch>
</#macro>
<#--解析组件CSS文件结束 -->
<#--解析组件脚本文件 -->
<#macro parseComponentScriptFile c >
	<#local componetType=c['componentType']?number>
	<#switch componetType>
		<#case 1002>
			scriptFile["<#noparse>${basePath}</#noparse>template/AdminLTE/js/bootstrap-datetimepicker.min.js"]=null;
			scriptFile["<#noparse>${basePath}</#noparse>template/AdminLTE/js/bootstrap-datetimepicker.zh-CN.js"]=null;
			<#break>
		<#case 1003>
		<#case 1004>
			scriptFile["<#noparse>${basePath}</#noparse>template/AdminLTE/js/icheck.js"]=null;
			<#break>
		<#case 1006>
			scriptFile["<#noparse>${basePath}</#noparse>template/AdminLTE/js/select2/select2.full.js"]=null;
			scriptFile["<#noparse>${basePath}</#noparse>template/AdminLTE/js/select2/zh-CN.js"]=null;
			<#break>
		<#case 1011>
		<#case 1016>
			scriptFile["<#noparse>${basePath}</#noparse>template/AdminLTE/js/fileinput/fileinput.js"]=null;
			scriptFile["<#noparse>${basePath}</#noparse>template/AdminLTE/js/fileinput/zh.js"]=null;
			<#break>
		<#case 1017>
			scriptFile["<#noparse>${basePath}</#noparse>template/easyui/js/jquery.easyui.min.js"]=null;
			<#break>
		<#case 1019>
			scriptFile["<#noparse>${basePath}</#noparse>resources/plugins/zui/assets/kindeditor/kindeditor-min.js"]=null;
			scriptFile["<#noparse>${basePath}</#noparse>resources/plugins/zui/assets/kindeditor/lang/zh_CN.js"]=null;
			<#break>
		<#case 1020>
			scriptFile["<#noparse>${basePath}</#noparse>resources/scripts/plateform.userSelect.js"]=null;
			<#break>
		<#case 1033>
			scriptFile["<#noparse>${basePath}</#noparse>venson/js/multilevelLinkage.js"]=null;
			<#break>
		<#default>
	</#switch>
</#macro>
<#--解析组件脚本文件结束 -->

<#macro parseComponent c >
	<#local componetType=c['componentType']?number>
	<#switch componetType>
		<#case 1001><#--单行文本框-->
			<@convertInputext c/>
			<#break>
		<#case 1002><#--日期文本框-->
			<@convertDatetime c/>
			<#break>
		<#case 1003><#--多选框-->
		<#case 1004><#--单选框-->
			<@convertCheckbox c/>
			<#break>
		<#case 1005><#--多行输入框-->
			<@convertTextarea c/>
			<#break>
		<#case 1006><#--下拉选项框-->
			<@convertSelect c/>
			<#break>
		<#case 1009><#--标签-->
			<@convertLabel c/>
			<#break>
		<#case 1010><#--隐藏框-->
			<@convertHidden c/>
			<#break>
		<#case 1011><#--文件上传框-->
		<#case 1016><#--图片上传-->
			<@convertFile c/>
			<#break>
		<#case 1012><#--包含组件-->
			<@convertContainPage c/>
			<#break>
		<#case 1017><#--表格组件-->
			<@convertdataGrid c/>
			<#break>
		<#case 1019><#--富文本编辑框-->
			<@convertkindEditor c/>
			<#break>
		<#case 1020><#--用户选择框-->
			<@convertuserSelect c/>
			<#break>
		<#case 1033><#--级联下拉框-->
			<@convertMultilevelLinkage c/>
			<#break>
		<#case 1036><#--搜索组件-->
			<@convertAddSearch c/>
			<#break>
		<#case 1043><#--流程意见组件-->
			<@convertSuggestion c/>
			<#break>
		<#default>
	</#switch>
</#macro>
<#-----------------组件脚本解析 begin------------------------------------------------------------->
<#macro convertComponentScript component>

	<#local componetType=component['componentType']?number>
	<#switch componetType>
		<#case 1001>
			<@convertInputextScript component/>
			<#break>
		<#case 1002>
			<@convertDatetimeScript component/>
			<#break>
		<#case 1003>
		<#case 1004>
			<@convertCheckboxScript component/>
			<#break>
		<#case 1005>
			<@convertTextareaScript component/>
			<#break>
		<#case 1006>
			<@convertSelectScript component/>
			<#break>
		<#case 1011>
		<#case 1016>
			<@convertFileScript component/>
			<#break>
		<#case 1012>
			<@convertContainPageScript component/>
			<#break>
		<#case 1017>
			<@convertdataGridScript component/>
			<#break>
		<#case 1019>
			<@convertKindEditorScript component/>
			<#break>
		<#case 1033>
			<@convertMultilevelLinkageScript component/>
			<#break>
		<#case 1036>
			<@convertAddSearchScript c/>
			<#break>
		<#case 1043>
			<@convertSuggestionScript component/>
			<#break>
		<#default>

	</#switch>
</#macro>

<#-----------------组件脚本解析 end------------------------------------------------------------->

<#-------------------------------------------输入框组件begin---------------------------------------->
<#macro convertInputext c >
	<div class="customGroup"  data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>">
			<#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<input type='${c['textType']!'text'}' class='dataItemCode 
		<#if c['css']?? && c['css']?length gt 0>
			${c['css']}
		<#else>
			${"form-control"}
		</#if>'
		<#if c['textType']??&&c['textType']=='number' >
			onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")'
		</#if>
		style='<#if c['style']?? >${c['style']}</#if>'
		<#if c['name']?? >
			name='${c['name']}'
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
				value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>
		id='${(c['pageId'])!""}'
		<#if c['description']?? >
			title='${c['description']}'
		</#if>
		/>
	</div>
</#macro>

<#macro convertInputextScript c >
(function(){
	var that = $('#${(c['pageId'])!""}');
	var temp = that.val();
	if(that.attr("type")=="money"){
		that.keyup(function(){
			var val = that.val();
			var reg1 = /^\d+\.\d{0,2}$/;
			var reg2 = /^\d+$/;
			if(val == ""){
				temp = val;
				return false;
			}
			if(reg1.test(val) || reg2.test(val)){
				temp = val;
			} else{
				that.val(temp);
			}
		});
	}
	<#if c['onchangeScript']??><#--onchange 事件 -->
		${c['onchangeScript']}
	</#if>
})();
</#macro>
<#-------------------------------------------输入框组件end---------------------------------------->


<#-------------------------------------------日期begin------------------------------------------>
<#macro convertDatetime c>
	<div class="customGroup"  data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<input type='datetime' readonly='readonly' class='dataItemCode 
		<#if c['css']?? && c['css']?length gt 0>
			${c['css']}
		<#else>
			${"form-control"}
		</#if>'	    
		style='
		<#noparse><#if ((status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true') || 
					   ((status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true')><#else>background-color: #fff;</#if></#noparse>
		<#if c['style']?? >${c['style']}</#if>'
		<#if c['name']?? >
			name='${c['name']}'
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		<#if c['dataItemCode']?? && c['dataItemCode']?length gt 0 >
				value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>
		id='${(c['pageId'])!""}'
		<#if c['description']?? >
			title='${c['description']}'
		</#if>
		/>
</div>
</#macro>
<#macro convertDatetimeScript component >
	(function(){
		var dateType="${component['dateType']}";
		var id='#${(component['pageId'])!""}';
		if(dateType=="yyyy-mm-dd" || dateType=="dd/mm/yyyy"){
			$(id).datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				format : dateType
			});
		} else if(dateType=="yyyy-mm-dd hh:ii" || dateType=="dd/mm/yyyy hh:ii"){
			$(id).datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 0,
				format : dateType
			});
		} else if(dateType=="yyyy-mm-dd hh:ii:ss"){
			$(id).datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 0,
				format : dateType
			});
		} else if(dateType=="hh:ii"){
			$(id).datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 1,
				minView : 0,
				format : dateType
			});
		} else if(dateType=="yyyy"){
			$(id).datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 4,
				minView : 4,
				format : dateType
			});
		} else if(dateType=="yyyy-mm"){
			$(id).datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 3,
				minView : 3,
				format : dateType
			});
		}
	})();
</#macro>
<#-------------------------------------------日期end-------------------------------------------->
<#-------------------------------------------多选框checkbook------------------------------------------>
<#macro convertCheckbox c>
	<div class="customGroup" data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" class="form-group" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<div class="dataItemCode" data-type="checkbox" id="${c['pageId']}">
			<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
		</div>
	</div>
</#macro>

<#macro convertCheckboxScript component >
	
	(function(){
		$('#${(component['pageId'])!""} input').iCheck({
				checkboxClass: 'icheckbox_square-green',
				radioClass: 'iradio_square-green',
			});
		<#if component['onchangeScript']??><#--onchange 事件 -->
			$('#${(component['pageId'])!""} input').on('ifChecked', function(event){
		  		${(component['onchangeScript'])!''}
			});
		</#if>
	})();
</#macro>
<#-------------------------------------------多选框end------------------------------------------>




<#-------------------------------------------textarea begin------------------------------------------>
<#macro convertTextarea c >
	<div class="customGroup" data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<textarea   class='dataItemCode 
		<#if c['css']?? && c['css']?length gt 0>
			${c['css']}
		<#else>
			${"form-control"}
		</#if><#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
		'

		<#if c['style']??  && c['style']?length gt 0>
			style='${c['style']}'
		<#else>
			style='width:100%'
		</#if>
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
</div>
</#macro>

<#macro convertTextareaScript component >

	<#if component['defineNote']?? && component['defineNote']=='1'>
		(function(){
			var pageId='${(component['pageId'])!""}';
	    	//InitQuickReply(pageId);
		})();
	</#if>

</#macro>

<#-------------------------------------------textarea end------------------------------------------>


<#-------------------------------------------下拉框select begin------------------------------------------->
<#macro convertSelect c >
	<div class="customGroup" data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<select class='dataItemCode 
		<#if c['css']?? && c['css']?length gt 0>
			${c['css']}
		<#else>
			${"form-control"}
		</#if>
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
	</div>
</#macro>

<#macro  convertSelectScript component  >
	(function(){
        var $select=$('#${(component['pageId'])!""}');
		<#if component['supportMulti']?? && component['supportMulti']=='1'>
			$select.children("option").eq(0).remove();
			$select.select2({
				 placeholder:'请选择',
				 width:"100%",
				 maximumSelectionLength:${(component['selectNumber'])},
				 multiple: true,
				 language:'zh-CN'
			});

		<#else>
			$select.select2({
				 width:"100%",
				 placeholder:'请选择',
				 allowClear:!0,
				 language:'zh-CN'
			});
		</#if>
 		var val=$select.attr("value");
		var arr=[];
		if($.trim(val).length>0){
			var arr1=val.split(";");
			for(var i=0;i<arr1.length;i++){
				if(arr1[i].length>0){
					arr.push(arr1[i]);
				}
			}
        }
		if(arr.length>0){
			$select.val(arr).trigger("change");
		}else{
			 $select.val([]).trigger("change");
		}

		<#if component['onchangeScript']??><#--onchange 事件 -->
			$select.on("change",function(){
				${component['onchangeScript']}
			});
		</#if>
	})();
</#macro>
<#-------------------------------------------下拉框select end------------------------------------------>
<#-------------------------------------------标签------------------------------------------>
<#macro convertLabel c >
	<p  class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
		<#if c['isRequired']?? && c['isRequired'] == '1'>
			required
		<#elseif c['isRequired']?? && c['isRequired'] == '3'>	
			centerLabel
		<#else>
			rightLabel
		</#if>
	 "
		<#if c['style']?? >
			style='${c['style']}'
		</#if>
		id="${(c['pageId'])!''}"

		<#if c['name']?? >
		name='${c['name']}'
		</#if>
		
		<#if c['description']?? >
		lang='${c['description']}'
		</#if>
		
	>

		${c['title']!""}
	</p>
</#macro>
<#-------------------------------------------标签 end------------------------------------------>
<#-------------------------------------------hidden框组件begin---------------------------------------->
<#macro convertHidden c >
	<div  data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="display:none;">
		<input class="dataItemCode" type='hidden'
		<#if c['name']?? >
			name='${c['name']}'
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		<#if c['dataItemCode']??   && c['dataItemCode']?length gt 0>
				value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"
		</#if>
		id='${(c['pageId'])!""}'
		/>
	</div>
</#macro>
<#-------------------------------------------hidden框组件end---------------------------------------->


<#-------------------------------------------文件上传框组件begin---------------------------------------->
<#macro convertFile c >
	<div class="customGroup" data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" class="form-group" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<div class="content">
			<div class="row" id="${(c['pageId'])!''}">
				<div class="col-xs-12">
					<div class="box box-default">
						<div class="box-body">
							<div class="file-preview">
								<h4 class="col-md-12">已上传</h4>
			    				<div class="file-drop-disabled fileList"></div>
							</div>
							<input type="hidden" class="uploadType" value="${c['uploadType']!''}"/>
							<input type="hidden" class="isIndex" value="${c['isIndex']!''}"/>
							<input type="hidden" class="dataItemCode orgfile"
							<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
							<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
							 name="${c['name']}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" />
		        			<input class="attachment" multiple type="file" name="file">  
	        			</div>
	        		</div>
				</div>
			</div>
		</div>
	</div>
</#macro>


<#macro convertFileScript c >	<#------ file上传组件校验脚本 ----->
	(function(){
		var $parent=$("#${(c['pageId'])!""}");
		var $file=$parent.find(".attachment");
		var $fileList=$parent.find(".fileList");
		var $orgfile=$parent.find(".orgfile");
		var $uploadType=$parent.find(".uploadType");
		var $isIndex=$parent.find(".isIndex");
		var val=$orgfile.val();
		var hiddenStyle = "";
		if($orgfile.attr("disabled")){
			hiddenStyle = 'style="display:none;"';
		}
		var allowedFileExtensions;
		<#if c['componentType']=="1016" >
			allowedFileExtensions=['jpg', 'gif', 'png'];
		<#else>
			allowedFileExtensions=("${c['fileKind']!''}"?"${c['fileKind']!''}".split(","):[]);
		</#if>
			if(allowedFileExtensions.length>0){
				$file.attr("accept",allowedFileExtensions.map(m=>"."+m).join(","));
			}
		if(val.length>20){
				var html=[];
				var arr=val.split(";");
				$.each(arr,function(i,e){
					if(!e||e.length<20) return;
					var data=Comm.getData("api/common/file/get",{"_method":"get","id":e});
					if(data!=-1){
						html.push('<div class="file-preview-frame" valNum="'+e+'">');
						html.push('<div class="kv-file-content">');
						if(data.contentType.indexOf("image")!=-1){
							html.push('<img width="100" height="100" src="'+basePath+'api/common/file/showPicture?id='+e+'">');
						}else{
							html.push('<img width="100" height="100" src="'+basePath+'images/Online-do-work.png">');
						}
						html.push('</div>');
						html.push('<div class="file-thumbnail-footer">');
						html.push('<div class="file-footer-caption" title="'+data.fileName+'">'+data.fileName+' <br><samp>('+(data.size/1000).toFixed(2)+' KB)</samp></div>');
						html.push('<div class="file-actions">');
						html.push('<div class="file-footer-buttons">');
						html.push('<button type="button" ' + hiddenStyle + ' class="kv-file-remove btn btn-xs btn-default" title="删除文件"><i class="glyphicon glyphicon-trash text-danger"></i></button>');
						html.push('<button type="button" style="margin:0 3px;" class="kv-file-download btn btn-xs btn-default" title="下载"><i class="glyphicon glyphicon-download-alt"></i></button>');
						html.push('<button type="button" class="kv-file-preview btn btn-xs btn-default" title="查看详情"><i class="glyphicon glyphicon-zoom-in"></i></button>');
						html.push('</div>');
						html.push('</div>');
						html.push('</div>');
						html.push('</div>');
					}else{
						removeVal(e);
					}
				});
				if(html.length>0){
					html.push('<div class="clearfix"></div>');
					$fileList.html(html.join(''));
				}else{
					$fileList.parent().remove();
				}
			}else{
					$fileList.parent().remove();
			}
		$file.fileinput({
                language: 'zh', //设置语言
                uploadUrl: basePath+"common/file/upload.do", //上传的地址
                allowedFileExtensions:allowedFileExtensions,//接收的文件后缀
                uploadExtraData:{"uploadType":$uploadType.val(),"isIndex":$isIndex.val()},
                uploadAsync: true, //默认异步上传
                showUpload: true, //是否显示上传按钮
                showRemove : true, //显示移除按钮
                showPreview : true, //是否显示预览
                showCaption: true,//是否显示标题
                browseClass: "btn btn-primary", //按钮样式     
                dropZoneEnabled: false,//是否显示拖拽区域
                <#if c['maxSize']?? && c['maxSize']?length gt 0 >
					maxFileSize :${c['maxSize']}*1024,
				</#if>
                <#if c['maxCount']?? && c['maxCount']?length gt 0 >
					maxFileCount : '${c['maxCount']}',
				</#if>
                enctype: 'multipart/form-data',
                validateInitialCount:true,
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
                previewSettings: {
                    image: {width: "100px", height: "100px"},
                }
            });
            if($orgfile.attr("disabled")){
            	$parent.find(".file-input").hide();
            }           
		//删除
		$parent.on("click",".kv-file-remove",function(){
			var $this = $(this);
			var $parentDiv=$this.parents(".file-preview-frame");
			var thisValNum = $parentDiv.attr("valNum");
			if(!thisValNum){
				$parentDiv.remove();
				if($fileList.find(".file-preview-frame").length==0){
					$fileList.parent().remove();
				}
			}else{
				if(thisValNum.length>20){
					var data=Comm.getData("api/common/file/remove",{"ids":thisValNum});
					if(data!=-1){
						removeVal(thisValNum);
						$parentDiv.remove();
						if($fileList.find(".file-preview-frame").length==0){
							$fileList.parent().remove();
						}
					}
				}
			}
		})
		//下载
		$parent.on("click",".kv-file-download",function(){
			var $this = $(this);
			var $parentDiv=$this.parents(".file-preview-frame");
			var thisValNum = $parentDiv.attr("valNum");
			if(thisValNum&&thisValNum.length>20){
				location.href=baseUrl+"api/common/file/download?fileId="+thisValNum;
			}
		})
		//预览
		$parent.on("click",".kv-file-preview",function(){
			var $this = $(this);
			var $parentDiv=$this.parents(".file-preview-frame");
			var thisValNum = $parentDiv.attr("valNum");
			if(thisValNum&&thisValNum.length>20){
				top.dialog({
					id : 'preview-dialog' + Math.ceil(Math.random() * 10000),
					title : '预览',
					url : baseUrl+"api/common/file/preview?fileId="+thisValNum,
					skin:"col-md-8",
					height:"40em"
				}).showModal();
			}
		})
		//重复文件处理
		$file.on("filebatchselected", function(event, files) {
			var fileLen = $(".file-input .file-live-thumbs > .file-preview-frame").length;
			for(var i=0;i<fileLen;i++){
				if(i!=fileLen-1){
					var title = $(".file-input .file-live-thumbs >  .file-preview-frame").eq(i).find(".file-footer-caption").attr("title");
					if(files[files.length-1].name==title && title!=undefined){
						files.pop();
						$(".file-input .file-live-thumbs >  .file-preview-frame").eq(fileLen-1).next().remove();
						$(".file-input .file-live-thumbs >  .file-preview-frame").eq(fileLen-1).remove();
						break;
					}
				}
			}
		});
		$file.on('fileerror', function(event, data, msg) {
			onError(event, data, msg);
		});
		$file.on("fileuploaded", function (event, data, previewId, index) {
             onSuccess(data,previewId);
        });
		//成功处理
	   function onSuccess(data,previewId){
		   var result=data.response;
		   if(result.status==0){
		       	 var arr = $orgfile.val().split(";");
	             arr.push(result.data);
	             var val=arr.join(";");
	             //清除第一个多余的分号
	             if(val.indexOf(";")==0){
	             	val=val.substring(1);
	             }
	             $orgfile.val(val);
	             $("#"+previewId+"").attr("valNum",result.data);
		   }else{
		      	Comm.alert(result.message);
		   }
	   }
	   //出错处理
	   function onError(event, data, msg){
		   Comm.alert(msg);
	   }
	   
	   //移除value
	   function removeVal(thisValNum){
			var val=$orgfile.val();
			var idArr=val.split(";");
			for(var i=0; i<idArr.length; i++) {
			    if(idArr[i] == thisValNum) {
			      idArr.splice(i, 1);
			      break;
			    }
			}
			$orgfile.val(idArr.join(";"));
		}
	})();
</#macro>


<#-------------------------------------------file框组件end---------------------------------------->

<#-------------------------------------------包含页面框框组件begin---------------------------------------->
<#macro convertContainPage c >
	<div name="${c['name']!''}" class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'> disabled</#if>
	</#noparse>">
	<#if c['layoutAndCom']?? >
		${c['layoutAndCom']}
	</#if></div>
</#macro>


<#macro convertContainPageScript component >
	<#if component['pageJScript']??>
				${component['pageJScript']}
	</#if>
</#macro>
<#-------------------------------------------包含页面框框组件end---------------------------------------->


<#-------------------------------------------dataGrid框组件begin---------------------------------------->
<#macro convertdataGrid c >
	<div class="customGroup">
		<label class="control-label"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<div id="easyui_dg_${c['pageId']}" style="border:solid 1px #ccc" class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>">
			<div id="tb_${c['pageId']}" style="height:auto">
				<#if c['operateAdd']?? && c['operateAdd']=='1'>
					<a href="javascript:void(0)" class="btn-xs btn-default easyui-add"><i class="fa fa-fw fa-plus"></i>增加</a>
				</#if>
				<#if c['operateEdit']?? && c['operateEdit']=='1'>
					<a href="javascript:void(0)" class="btn-xs btn-default easyui-edit"><i class="fa fa-fw fa-edit"></i>更改</a>
				</#if>
				<#if c['operateSave']?? && c['operateSave']=='1'>
					<a href="javascript:void(0)" class="btn-xs btn-default easyui-save"><i class="fa fa-fw fa-save"></i>保存</a>
				</#if>
				<#if c['operateUndo']?? && c['operateUndo']=='1'>
					<a href="javascript:void(0)" class="btn-xs btn-default easyui-undo"><i class="fa fa-fw fa-undo"></i>还原</a>
				</#if>
				<#if c['operateDelete']?? && c['operateDelete']=='1'>
					<a href="javascript:void(0)" class="btn-xs btn-default easyui-remove"><i class="fa fa-fw fa-remove"></i>移除</a>
				</#if>
			</div>
			<table id="dg_${c['pageId']}" style="width:100%"></table>
		</div>
	</div>
</#macro>

<#macro convertdataGridScript c >
	(function(){
		var queryParams={<#if c['connditions']?? ><#list c['connditions'] as item>"${item['paramKey']!''}":
	<#if item['isFinal']?? && item['isFinal']=='1'>
				 	 "${item['paramValue']!''}"
	<#else>
				  	 "<#noparse>${(</#noparse>${item['paramValue']}<#noparse>)!''}</#noparse>"
	</#if>,</#list></#if>pageSize:10};
		var basePath="<#noparse>${basePath}</#noparse>";
		var $table=$("#dg_${c['pageId']}");
		var pageSize=parseInt("${c['pageSize']!'10'}");
		$table.datagrid({
				  pagination: ("${c['hasPager']!'0'}"=="1"?true:false),//分页控件 
				  pageNumber:1,
				  pageSize:pageSize,
				  rownumbers: true,//行号
				  border: false, 
				  collapsible: false,
				  checkOnSelect:false,
				  selectOnCheck:false,
				  fitColumns:true,
				  onBeforeLoad:function(param){
					   param.pageSize=param.rows;
					   param.currentPage=param.page;
				  },
				  loadFilter:function(data){
						var d={};
		  				if(data.status==0){
							d.rows=data.data;
							d.total=data.total;  								  				
		  				}else{
		  					d.rows=[];
		  					d.total=0;
		  				}
		  				return d;
				  },     						  
				  url:basePath+"document/refreshDataGrid.do?componentId=${c['pageId']}" + ("${c['hasPager']!'0'}"!="1"?"&pageSize=9999":""),
				  queryParams:queryParams,
				  columns:[[
				  <#if c['columns']?? >
				  	{field:'ck',checkbox:true}
					 <#list c['columns'] as item>
						,{
						field:"${item['columnField']!''}",
						title:"${item['columnTitle']!''}",
						<#if item['editType']?? && item['editType']!='0'>
							<#if item['editType']=='checkbox'>
								editor:{
									type:"checkbox",
									options:{on:"${item['editValue']!''}",off:""}
								},
							<#elseif item['editType']?? && item['editType']=='combobox'>  
								editor:{
									type:"combobox",
									options:getCombobox("${item['editValue']!''}")
								},
							<#else>  
								editor:"${item['editType']}",
							</#if>
						</#if>  
						<#if item['columnWidth']??>
						width:"${item['columnWidth']!''}"
						</#if>
						}
					</#list>
				  </#if>
				  ]],
				  toolbar: "tb_${c['pageId']}",
				  onClickCell:onClickCell,
				  onLoadSuccess:onLoadSuccess
		});
		function onLoadSuccess(){
			<#if c['onchangeScript']??><#--onchange 事件 -->
				${c['onchangeScript']}
			</#if>
		}
		function getCombobox(value){
			var result={
				valueField:'id',
				textField:'text',data:[],
				formatter: function(row){
					var opts = $(this).combobox('options');
					return row[opts.textField];
				}
			};
			if(value.indexOf("=")!=-1){
				var options=value.split(";");
				var len=options.length;
				for(var i=0;i<len;i++){
					if(options[i]){
						var option=options[i].split("=");
						result.data.push({id:option[0],text:option[1]});
					}
				}
			}else{
				$.ajax({ 
			          type : "get", 
			          url : basePath+value, 
			          async : false, 
			          success : function(data){
			          	if(data.status==0){
				            result.data = data.data;
			          	}
			          } 
		          });
			}
			return result;
		}
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($table.datagrid('validateRow', editIndex)){
				$table.datagrid('endEdit', editIndex);
				editIndex = undefined;
				//编辑完渲染后重新执行回调事件
				onLoadSuccess();
				return true;
			} else {
				return false;
			}
		}
		function onClickCell(index, field){
			if (editIndex != index){
				if (endEditing()){
					$table.datagrid('selectRow', index)
							.datagrid('beginEdit', index);
					editIndex = index;
				}
			}
		}
		var p = $table.datagrid('getPager');
	    $(p).pagination({
	    	pageSize:pageSize,
	        pageList: [10, 20, 30, 40, 50],//可以设置每页记录条数的列表 
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页',
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    });
		$("#tb_${(c['pageId'])}").on("click","a",function(){
			//增加
			if($(this).hasClass("easyui-add")){
				var paramData = {};
				var str="";
				<#if c['param']?? && c['param']?size gt 0>
					<#list c['param'] as item>
						str+="&";
						str+="${item['storeParamId']}";
				        str+="=";
				        str+="<#noparse>${(</#noparse>${item['exportParam']}<#noparse>)!''}</#noparse>";
						paramData['${item['storeParamId']}']='<#noparse>${(</#noparse>${item['exportParam']}<#noparse>)!''}</#noparse>';
					</#list>
				</#if> 
			    
				top.dialog({
					id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
					title : '新增...',
					url : "<#noparse>${basePath}</#noparse>document/view.do?dynamicPageId=${c['alertPage']}"+str,
					data: paramData,
					skin:"col-md-8",
					onclose : function() {
						if (this.returnValue) {
							$("#dg_${c['pageId']}").datagrid('reload');
						}
					}
				}).showModal();
	
				return false;
			
			}
			//编辑
			else if($(this).hasClass("easyui-edit")){
				var ids = $("#dg_${c['pageId']}").datagrid('getChecked');
				if(!ids||ids.length!=1){
					Comm.alert("请勾选中一项操作");
					return false;
				}
				top.dialog({
					id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
					title : '编辑...',
					url : "<#noparse>${basePath}</#noparse>document/view.do?dynamicPageId=${c['alertPage']}&id=" + ids[0]["ID"],
					skin:"col-md-8",
					onclose : function() {
						if (this.returnValue) {
							$("#dg_${c['pageId']}").datagrid('clearChecked').datagrid('reload');
						}
					}
				}).showModal();
			}
			//保存
			else if($(this).hasClass("easyui-save")){
				//先结束编辑
				endEditing();
				var rows = $("#dg_${c['pageId']}").datagrid('getChanges');
				if(!rows||rows.length==0){
					return;
				}
				var data = {<#if c['connditions']?? ><#list c['connditions'] as item>${item['paramKey']!''}:
									  <#if item['isFinal']?? && item['isFinal']=='1'>
									 	 '${item['paramValue']!''}'
									  <#else>
									  	 '<#noparse>${(</#noparse>${item['paramValue']}<#noparse>)!''}</#noparse>'
									  </#if>
									  ,</#list></#if>pageSize:10};
				data.method = 'save';
				data.json = JSON.stringify(rows);
				$("#dg_${c['pageId']}").datagrid('load',data);
			}
			//撤销
			else if($(this).hasClass("easyui-undo")){
				//先结束编辑
				endEditing();
				var rows = $("#dg_${c['pageId']}").datagrid('getChanges');
				if(rows&&rows.length>0){
					$("#dg_${c['pageId']}").datagrid('reload');
				}
			}
			//删除
			else if($(this).hasClass("easyui-remove")){
				var ids = $("#dg_${c['pageId']}").datagrid('getChecked');
				if(!ids||ids.length==0){
					Comm.alert('请勾选数据');
				}else{
					var data = {<#if c['connditions']?? ><#list c['connditions'] as item>${item['paramKey']!''}:
									  <#if item['isFinal']?? && item['isFinal']=='1'>
									 	 '${item['paramValue']!''}'
									  <#else>
									  	 '<#noparse>${(</#noparse>${item['paramValue']}<#noparse>)!''}</#noparse>'
									  </#if>
									  ,</#list></#if>pageSize:10};
					data.method = 'delete';
					data._selects = ids.map(x=>x.ID).join(",");
					$("#dg_${c['pageId']}").datagrid('clearChecked').datagrid('load',data);
				}
				return false;
			}
		})
	
	})();


</#macro>
<#-------------------------------------------dataGrid框组件end---------------------------------------->

<#-------------------------------------------流程意见组件begin---------------------------------------->
<#macro convertSuggestion c >
    <div style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none</#if></#noparse>">
        <div class="input-group" style="margin-bottom: 6px;margin-top:20px;">
            <span class="input-group-addon" style="font-weight:bold;">本次处理意见</span>
            <textarea style="width:100%" name="work_logs_content" rows="3"
                      class="dataItemCode form-control"
                    <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
                    <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
            ></textarea>
        </div>
        <div class="customGroup">
            <button type="button" id="expandBtn" class="btn btn-box-tool" style="float:right;margin-right:12px;"><i class="fa fa-plus"></i></button>
            <label class="control-label"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
            <div style="display:none;" id="div_${c['pageId']}" style="width: 100%;" class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
        <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'> disabled</#if></#noparse>">
            <table id="tb_${c['pageId']}" class="table table-bordered">
                <#if c['columns']?? && c['columns']?size gt 0>
                        <thead>
                            <#list c['columns'] as item>

                            </#list>
                        </thead>
                <#else>
                        <thead>
                            <th data-field="dept" style="width:20%">部门</th>
                            <th data-field="name" style="width:20%">姓名</th>
                            <th data-field="time" style="width:20%">时间</th>
                            <th data-field="node" style="width:20%">节点</th>
                            <th data-field="suggest" style="width:20%">意见</th>
                        </thead>
                        <tbody>

                        </tbody>
                </#if>
                </table>
            </div>
        </div>
    </div>
</#macro>

<#macro convertSuggestionScript c >
	(function(){
        var pageId="${(c['pageId'])}";
        var null2Empty=function(obj){
            return obj?obj:"";
        }
        $.get(basePath+'api/workflow/wf/comments/'+pageId+location.search,function(res){
            var data=res.data;
            var html=[];
			var fields=[];
			$("#tb_"+pageId+" th").each((i,e)=>fields.push($(e).attr("data-field")));
            if(data){
                $.each(data,function(i,e){
					html.push("<tr>");
					$.each(fields,(j,o)=>html.push("<td>"+null2Empty(e[o])+"</td>"));
    				html.push("</tr>");
                })
            }else{
                html.push("<tr style='text-align: center;'><td colspan='5'>暂无流程处理意见<td></tr>");
            }
            $("#tb_"+pageId+" tbody").html(html.join(''));
        })

		$("#expandBtn").on("click",function(){
			if($(this).next().next().css("display") == "none"){
				$(this).find("i").removeClass("fa-plus").addClass("fa-minus");
				$(this).next().next().show();
			}else{
				$(this).find("i").removeClass("fa-minus").addClass("fa-plus");
				$(this).next().next().hide();
			}
		})
	})();
</#macro>
<#-------------------------------------------流程意见框组件end---------------------------------------->

<#-------------------------------------------convertkindEditor------------------------------------------>

<#macro convertkindEditor c >
	<div class="customGroup" data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<textarea id='${(c['pageId'])!""}'
				<#if c['name']?? >
					name='${c['name']}'
				</#if>
			 class='dataItemCode form-control kindeditor'
				 <#if c['style']?? >
					style='display:none;width:100%; ${c['style']}'
				 </#if>
				 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
				 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
			><#if c['dataItemCode']??  && c['dataItemCode']?length gt 0><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></#if></textarea>
	</div>
		

</#macro>

<#macro convertKindEditorScript c >
	(function(){
		KindEditor.ready(function(K){
		  			var options ={
			            allowFileManager : false,
			            allowUpload : true,
						urlType:"domain",//绝对路径
			            uploadJson : basePath+'common/file/uploadImg.do',
			            height:("${c['imageHeight']!'300'}"?"${c['imageHeight']!'300'}px":"300px"),
			            width:("${c['imageWidth']!'100%'}"?"${c['imageWidth']!'100%'}":"100%"),
			            bodyClass : 'article-content',
			            afterBlur: function(){this.sync();$(this.container[0]).removeClass('focus');
			            },
			            afterFocus: function(){$(this.container[0]).addClass('focus');},
			            afterCreate : function(){
			                var doc = this.edit.doc; 
			                var cmd = this.edit.cmd; 
			                if(K.WEBKIT){
			                    $(doc.body).bind('paste', function(ev){
			                        var $this = $(this);
			                        var original =  ev.originalEvent;
			                        var file =  original.clipboardData.items[0].getAsFile();
			                        var reader = new FileReader();
			                        reader.onload = function (evt){
			                            var result = evt.target.result;
			                            var arr = result.split(",");
			                            var data = arr[1]; // raw base64
			                            var contentType = arr[0].split(";")[0].split(":")[1];
			                            //html = '<img src="' + result + '" alt="" />';
			                            //cmd.inserthtml(html);
			                            //post本地图片到服务器并返回服务器存放地址
										$.ajax({
			                            	type:"post",
			                            	url: basePath+"common/file/generateImageByBase64Str.do",
			                            	dataType:"html",
			                            	data:{imgStr:result},
			                            	success:function(data){
			                            	var html = '<img src="'+basePath+'/attached/image/' +data + '" alt="" />';
			                            	cmd.inserthtml(html)
			                            	}
			                            });

			                        };
			                        if(file){
				                        reader.readAsDataURL(file);
			                        }
			                    });
			                }
			
			            }
			        };
			        var editor_${c['name']} = K.create('textarea[name="${c['name']}"]',options);
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'></#noparse>editor_${c['name']}<#noparse>.readonly("false");</#if></#noparse>
				});
	})();
</#macro>

<#-------------------------------------------convertkindEditor end------------------------------------------>

<#-------------------------------------------用户选择框组件begin---------------------------------------->
<#macro convertuserSelect c >
	<div class="customGroup" data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label <#if c['required']?? && c['required']=='1'>required</#if>"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<input class="dataItemCode " type='hidden' data-input="chosen"
		<#if c['isSingle']?? >
			data-isSingle='${c['isSingle']}'
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
		<input type='text' class='chosenName text-ellipsis form-control'
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		>
	</div>
</#macro>
<#-------------------------------------------用户选择框组件end---------------------------------------->





<#-------------------------------------------搜索条件begin---------------------------------------->
<#macro convertAddSearch c >
		<div class="search_main row">
			<input type="hidden" value="${c['selectOption']!''}" id="selectOption">
			<input type="hidden" value="${c['selectLabel']!''}" id="selectLabel">
			<input type="hidden" value="${c['selectName']!''}" id="selectName">
			<input type="hidden" value="${c['textLabel']!''}" id="textLabel">
			<input type="hidden" value="${c['textName']!''}" id="textName">
			<input type="hidden" value="${c['dateSelectLabel']!''}" id="dateSelectLabel">
			<input type="hidden" value="${c['dateSelectName']!''}" id="dateSelectName">
		</div>
</#macro>
<#macro convertAddSearchScript c >	<#------ 搜索条件脚本 ----->
	new addSearch();
</#macro>
<#-------------------------------------------搜索条件end---------------------------------------->
<#-------------------------------------------级联下拉框begin---------------------------------------->
<#macro convertMultilevelLinkage c >
	<div class="customGroup" data-required="${c['required']!'0'}" data-placeholder="${c['placeholder']!''}" style="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>">
		<label class="control-label"><#noparse>${others['title_</#noparse>${c['name']}<#noparse>']!''}</#noparse></label>
		<input id="select_select2_sql" type="hidden" value="${c['select_select2_sql']}"/>
		<input id="select_select2_name" type="hidden" value="${c['select_select2_name']}"/>
		<input id="select_select2_label" type="hidden" value="${c['select_select2_label']}"/>
		<input type="hidden" class="select_select2_finaVal dataItemCode" name="${c['name']}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" />
		<div class="select_select2_main row"></div>
	</div>
</#macro>
<#macro convertMultilevelLinkageScript c >	<#------ 级联下拉框脚本 ----->
</#macro>
<#-------------------------------------------级联下拉框end---------------------------------------->



<#-----------------button begin------------------------------------------------------------->
<#macro convertButton c >
    <#--解析导入按钮-->
    <#if c.getActType()==2004>
    <script>
        <#--导入文件上传JS文件-->
        scriptFile["<#noparse>${basePath}</#noparse>venson/js/jquery/jquery.form.js"]=null;
    </script>
    <form style="display:inline-block;vertical-align:middle;" enctype="multipart/form-data">
        <button type="button"  id='${c.getPageId()}' class="${c.getCss()!''} btn <#if c.getStyle()??>${c.getStyle()}</#if>  <#if c.getColor()??>btn-${c.getColor()}</#if>"
         onclick="document.getElementById('import_${c.getPageId()}').click()"
         <#noparse>
            <#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden'])?? && pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden']>
                style="display:none;"
            </#if>
        </#noparse>
         ><i class="${c.getIcon()!''}"></i>${c.getName()}</button>
        <input type="file" style="display:none;"  name="file" id="import_${c.getPageId()}">
        <span id="importTips_${c.getPageId()}"></span>
    </form>
    <#--解析其它按钮-->
    <#else>
        <button type="button" data-valid="${c.isChooseValidate()?string("true","flase")}" id='${c.getPageId()}' title="${c.getDescription()!''}"
                class="${c.getCss()!''} btn <#if c.getStyle()??>${c.getStyle()}</#if> <#if c['place']??&&c['place']=='1' >btn-sm</#if> <#if c.getColor()??>btn-${c.getColor()}</#if>"
                <#noparse>
                    <#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['backId'])??>
                        backId="${pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['backId']!''}"
                    </#if>
                </#noparse>
            <#if c.getExtbute()??>
                target='${c.getExtbute()['target']!''}'
            </#if>
                <#noparse>
                    <#if (pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden'])?? && pageActStatus['</#noparse>${c.getPageId()}<#noparse>']['hidden']>
                        style="display:none;"
                    </#if>
                </#noparse>
             >
            <i class='<#if c.getIcon()??>${c.getIcon()}</#if>'></i>
            ${c.getName()}</button>
    </#if>
</#macro>


<#macro convertScript c>
<#--解析导入按钮-->
<#if c.getActType()==2004>
	$("#import_${c.getPageId()}").on("change",function(){
			$tips=$("#importTips_${c.getPageId()}");
			$tips.removeClass("text-success");
  			$tips.addClass("text-danger");
  			$("#actId").val("${c.getPageId()}");
  			$tips.text("正在导入...")
    		if(!$.trim($(this).val())){
    			return;
    		}
    		var $form=$(this).parent();
	   		var options = {
			        success: function(data){
		  				$tips.text("");
			        	if(data.status==0){
				        	data=Comm.getData("api/document/executeAct?"+$("#groupForm").serialize()+"&_method=get&fileId="+data.data)
			        		${c.getClientScript()}
			        	}else{
				        	Comm.alert(data.message);
			        	}
			  			
			        }, 
			        url: baseUrl+"api/common/file/upload",
			        type: "POST", 
			        dataType: "json", 
			    }
	   		$form.ajaxSubmit(options);
    	});
	<#--解析其它按钮-->
	<#else>
	<#--表格行内按钮-->
		<#if c.getPlace()??&&c.getPlace()=="1">
			$("#groupForm").on("click","#${c.getPageId()}",function(){
				$('#groupForm .table').bootstrapTable('uncheckAll');
				var $row = $(this).parent().parent();
				var $checkbox=$row.find(":checkbox").eq(0);
				$checkbox.get(0).checked=true;
				$row.find("input[type='hidden']").attr("name","_selects");
				$row.addClass("selected");
				count = $("input[name='_selects']").length;
				var id = $row.find(".formData").children("input").val();
				var obj = {id:id};
		<#--页面顶部按钮-->
		<#else>
			$("#${c.getPageId()}").click(function(){
		</#if>
		<#if c.isChooseValidate()??&&c.isChooseValidate()>
				if(!Comm.validForm())
					return;
		</#if>
		<#if c.getClientScript()??>
			${c.getClientScript()}
		</#if>
		});

</#if>
</#macro>
<#-----------------button end------------------------------------------------------------->

<#------------------------------列组件 begin------------------------------------------------------->

<#macro parseColumn c >
		<th 
		<#if c['width']??>	
			data-width="${c['width']}"
		</#if>
		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>class="hidden"</#if></#noparse>
		<#if c['sortName']?? && c['sortName'] != "">
			data-field="${c['sortName']}"
		<#else>
			data-field="${c['dataItemCode']}"
		</#if>
		<#if c['alloworderby']?? && c['alloworderby'] == "1">
			data-sortable="true"
		</#if>
		>
		<#if c['columnName']?? >
			${c['columnName']}
		</#if>
		
		</th>
</#macro>

<#macro parseColumnData c >
		<td <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>class="hidden"</#if></#noparse>>
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
		</td>
</#macro>

<#------------------------------列组件 end--------------------------------------------------------->



<#------------------------------校验--------------------------------------------------------->
<#macro parseValidForm components >
		(function(){
			
		})();
</#macro>

<#------------------------------校验end--------------------------------------------------------->


