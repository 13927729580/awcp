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
			<td style="border:1px #000 solid" class="col-xs-${root['proportion']} col-sm-offset-${root['offset']}">
		<#else>
			<td style="border:1px  #000 solid" class="col-xs-${root['proportion']}" colspan="${root['proportion']}">
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
				<#if c['componentType']!='1013'&&c['componentType']!='1036' >
					<@parseComponent c />
				</#if>
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
		<#case 1032>
			<@convertNumberInputText c/>
			<#break>
		<#case 1033>
			<@convertMultilevelLinkage c/>
			<#break>
		<#case 1034>
			<@converttab c/>
			<#break>
		<#case 1035>
			<@convertMap c/>
			<#break>
		<#case 1036>
			<@convertAddSearch c/>
			<#break>
		<#case 1037>
			<@convertSignature c/>
			<#break>
		<#case 1038>
			<@convertGridTable c/>
			<#break>
		<#case 1039>
			<@convertEasyUiBaseGrid c/>
			<#break>
		<#case 1040>
			<@convertEasyUiRowEditingGrid c/>
			<#break>
		<#case 1041>
			<@convertEasyUiNestGrid c/>
			<#break>
		<#case 1042>
			<@convertEasyUiGridComponent c/>
			<#break>
		<#default>
	</#switch>
</#macro>

<#-------------------------------------------数字输入框组件begin---------------------------------------->
<#macro convertNumberInputText c >
	<input type='text' class='isNumber 
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
	'
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
<#-------------------------------------------数字输入框组件end---------------------------------------->

<#-------------------------------------------Map Begin---------------------------------------->
<#macro convertMap c>
    <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.3&key=${c['ibs_key']}&plugin=AMap.Autocomplete,AMap.PlaceSearch"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
    <div id='container' style="height: ${c['ibs_height']}px"></div>
    <!--<div id="tip"></div>-->
    <div id="myPageTop" style="margin-right: 10px">
        <table>
            <tr>
                <td>
                    <label>请输入关键字：</label>
                </td>
            </tr>
            <tr>
                <td>
                    <input id="tipinput"/>
                </td>
            </tr>
        </table>
    </div>
</#macro>
<#macro convertMapScript c>

</#macro>
<#-------------------------------------------Map end---------------------------------------->


<#-------------------------------------------签名解析------------------------------------->
<#macro convertSignature c >

		<div>
			<input type="hidden" name="${c['name']!""}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"/>
			<img style="width:100px;" id="${c['pageId']}_Img"
	      	<#noparse><#if </#noparse>(${c['dataItemCode']}) <#noparse>?? &&</#noparse>(${c['dataItemCode']})<#noparse>?length gt 0></#noparse>
	      		src="<#noparse>${basePath}</#noparse>common/file/showPicture.do?id=<#noparse>${</#noparse>${c['dataItemCode']}<#noparse>}</#noparse><#noparse>"
	      	</#if></#noparse>
	      		alt="未找到该用户的签名"/>
	    </div>

</#macro>
<#-------------------------------------------签名解析END------------------------------------->



<#-------------------------------------------tab---------------------------------------->
<#macro converttab c >
	<div class="wrap">
        <div id="easyui-tabs${c['name']}" class="tabs" lang="${c['tab_name']}">
        </div>
       <div class="swiper-container" lang='<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>'>
            <div class="swiper-wrapper" style="height:100%">
            </div>
        </div>
    </div>
</#macro>
<#macro converttabScript c >	<#------ tab脚本 ----->

 	new tabs("#easyui-tabs${c['name']}");
 	
</#macro>

    
<#-------------------------------------------老版tab---------------------------------------->
<#-----
	<div class="wrap">
        <div class="tabs" lang="${c['tab_name']}">
        </div>
       <div class="swiper-container" lang='<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>'>
            <div class="swiper-wrapper" style="height:100%">
            </div>
        </div>
    </div>
--->
<#-----
	  	new tabs();
    	setTimeout(function(){
    		var w = $(".tabs").width();
    		console.log(w);
    		var width = $(".swiper-slide").length * w;
    		
			$(".swiper-wrapper").attr("style","").css("width",width + "px")
			console.log($(".swiper-wrapper"))
			$(".swiper-slide").attr("style","").css("width",w + "px")
			console.log()
			
		},500)
--->

 




<#-------------------------------------------EasyUiBaseGrid---------------------------------------->
<#macro convertEasyUiBaseGrid c >	<#------ EasyUiBaseGrid脚本 ----->
    <table id="dg${c['name']!""}" class="${c['gridType']!''}" data-options="url:'<#noparse>${basePath}</#noparse>${c['dataSource']}',method:'post',${c['tableConf']}">
		<thead>
			<tr>
				<#if c['gridConf']?exists>  
	            	<#list c['gridConf']?split("@") as thName>
	            			<#assign thArr=(thName?split("|"))>
            				<th data-options="${thArr[0]}">${thArr[1]}</th>
	            	</#list>  
			   	</#if>  
			</tr>
		</thead>
	</table>
	
</#macro>
<#-------------------------------------------EasyUiRowEditingGrid---------------------------------------->
<#macro convertEasyUiRowEditingGrid c >	<#------ EasyUiRowEditingGrid脚本 ----->

<table id="dg${c['name']!""}" class="easyui-datagrid" data-options="
				iconCls: 'icon-edit',
				singleSelect: true,
				${c['tableConf']!""}
				toolbar: '#tb',
				url: '<#noparse>${basePath}</#noparse>${c['dataSource']}',
				method: 'post',
				onClickCell: onClickCell,
				onEndEdit: onEndEdit
			">
			<thead>
				<tr>
					<#list c['gridConf']?split("@") as thName>
	            			<#assign thArr=(thName?split("|"))>  
            				<th data-options="${thArr[0]}">${thArr[1]}</th>
	            	</#list>  
				</tr>
			</thead>
		</table>

		<div id="tb" style="height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="append()">增加</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">移除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">接受更改</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="reject()">还原</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="getChanges()">获取变动记录</a>
		</div>
		

		
		<script type="text/javascript">
				var dg = 'dg${c['name']!""}'
				
				var editIndex = undefined;

				function endEditing() {
					if(editIndex == undefined) {
						return true
					}
					if($('#'+dg).datagrid('validateRow', editIndex)) {
						$('#'+dg).datagrid('endEdit', editIndex);
						editIndex = undefined;
						return true;
					} else {
						return false;
					}
				}

				function onClickCell(index, field) {
					if(editIndex != index) {
						if(endEditing()) {
							$('#'+dg).datagrid('selectRow', index)
								.datagrid('beginEdit', index);
							var ed = $('#'+dg).datagrid('getEditor', {
								index: index,
								field: field
							});
							if(ed) {
								($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
							}
							editIndex = index;
						} else {
							setTimeout(function() {
								$('#'+dg).datagrid('selectRow', editIndex);
							}, 0);
						}
					}
				}

				function onEndEdit(index, row) {
					var ed = $(this).datagrid('getEditor', {
						index: index,
						field: 'productid'
					});
				
					//row.productname = $(ed.target).combobox('getText');
				
				}


				function append() {
					if(endEditing()) {
						$('#'+dg).datagrid('appendRow', {
							status: 'P'
						});
						editIndex = $('#'+dg).datagrid('getRows').length - 1;
						$('#'+dg).datagrid('selectRow', editIndex)
							.datagrid('beginEdit', editIndex);
					}
				}

				function removeit() {
					if(editIndex == undefined) {
						return
					}
					$('#'+dg).datagrid('cancelEdit', editIndex)
						.datagrid('deleteRow', editIndex);
					editIndex = undefined;
				}

				function accept() {
					if(editIndex == undefined) {
						return true
					}
					if($('#'+dg).datagrid('validateRow', editIndex)) {
						$('#'+dg).datagrid('endEdit', editIndex);
						editIndex = undefined;
						return true;
					} else {
						return false;
					}
					onClickCell(index, field);
					
//					if(endEditing()) {
//						$('#'+dg).datagrid('acceptChanges');
//					}
				}

				function reject() {
					$('#'+dg).datagrid('rejectChanges');
					editIndex = undefined;
				}

				function getChanges() {
					var rows = $('#'+dg).datagrid('getChanges');
					//alert(rows.length + ' rows are changed!');
				}
		
		</script>
	
</#macro>

<#-------------------------------------------nestGrid---------------------------------------->
<#macro convertEasyUiNestGrid c >	<#------ nestGrid脚本 ----->
    <table id="dg${c['name']!""}" class="easyui-datagrid" style="height:${c['gridHeight']!""};"></table>
<script>
	var dg = 'dg${c['name']!""}';
	window.onload=function(){				
		$('#'+dg).datagrid({
	        view: detailview,
	        url:"<#noparse>${basePath}<#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>",
			${c['gridCellConf']}
			columns:[[ ${c['gridConf']} ]],
			detailFormatter:function(index,row){
	          	return '<div style="padding:2px"><table class="ddv"></table></div>';
	        },
	        onExpandRow: function(index,row){
	          	var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
	          	ddv.datagrid({
		            url:'<#noparse>${basePath}</#noparse>${c['sonDataSource']},
		            height:'auto',
		        	${c['sonGridCellConf']}
		            columns:[[ ${c['sonGridConf']} ]],
		            onResize:function(){
		              	$('#'+dg).datagrid('fixDetailRowHeight',index);
		            },
		            onLoadSuccess:function(){
			            setTimeout(function(){
			                $('#'+dg).datagrid('fixDetailRowHeight',index);
		              	},0);
		        	}
	      		});
	      		$('#'+dg).datagrid('fixDetailRowHeight',index);
	        }
		});
	}
</script>
</#macro>


<#-------------------------------------------easyUI表格組件---------------------------------------->
<#macro convertEasyUiGridComponent c >
	<table id="dg${c['name']!""}" style=
		"
	<#if (c['panelWidth']?? || c['panelWidth']=="" ) || c['tableType'] == "combogrid" || c['tableType'] == "combotree">
		width:100%;
	<#else>
		width:${c['panelWidth']!""}px;
	</#if>
		
	<#if c['panelHeight']?? && c['panelHeight']!= "">
		height:${c['panelHeight']!""}px;
	</#if>
		"
	></table>
<#--右键菜单dom-->
<#if c['tableType'] == 'treegrid' &&  c['contextMenu']??>
	<div id="mm${c['name']!""}" class="easyui-menu" style="width:120px;">
		<div onclick="append${c['name']!""}()" data-options="iconCls:'icon-add'">Add New Directory</div>
		<div onclick="edit${c['name']!""}()" data-options="iconCls:'icon-edit'">Edit Directory</div>
		<div onclick="removeIt${c['name']!""}()" data-options="iconCls:'icon-remove'">Delete Directory</div>
		<div class="menu-sep"></div>
		<div onclick="collapse${c['name']!""}()">Move Up</div>
		<div onclick="expand${c['name']!""}()">Move Down</div>
	</div>
</#if>

	
	
</#macro>
<#macro convertEasyuiScript c >            <#------------------脚本----------------->
	<#--普通表格和树表格计算高度-->
	<#if (!c['panelHeight']?? || c['panelHeight']=="") && (c['tableType'] == "datagrid" || c['tableType'] == "treegrid")>
		
		var btnHeight =  $(".search_main_warp").height();
		$("#dg${c['name']!""}").css("height",$(window).height()-btnHeight- 25 +"px")
	</#if>
	
	var Index${c['name']!""} = undefined;
	var cellChangeData${c['name']!""};
	var _this = this;
	<#--下拉选择数据    在编辑中  会重复触发请求 -->
	<#if c['setEdit']??>
		<#list c['cellConf']?split(",") as index>
			<#assign celleditType = "celleditType${index}">
			<#assign editSelecturl = "editSelecturl${index}">
			<#if c[celleditType]?? && c[celleditType] == 'combobox'>
				var combobox${c['name']!""};
				$.ajax({
					type:'post',
					async:false,
					url:'<#noparse>${basePath}</#noparse>${c[editSelecturl]!""}',
					success:function(res){
						if($.isArray(res)){
							combobox${c['name']!""} = res;
						}else if($.isArray(res.rows)){
							combobox${c['name']!""} = res.rows;
						}else if($.isArray(res.data)){
							combobox${c['name']!""} = res.data;
						}
					}
				})
			</#if>
		</#list>
	</#if>
	
	
	$("#dg${c['name']!""}").${c['tableType']!""}({
		url:'<#noparse>${basePath}<#if others??>${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>'
		
		,singleSelect:${c['tablesingleSelect']!"false"}
		
		,columns: [[
			<#list c['cellConf']?split(",") as index>
				<#assign cellfield = "cellfield${index}">
				<#assign celltxt = "celltxt${index}">
				<#assign cellwidth = "cellwidth${index}">
				<#assign cellalign = "cellalign${index}">
				{field:'${c[cellfield]!""}',title:'${c[celltxt]!""}',width:${c[cellwidth]!"80"},align:'${c[cellalign]!""}',
					<#if c['setEdit']??>
						<#assign celleditType = "celleditType${index}">
						
						<#if c[celleditType]?? && c[celleditType] == 'combobox'>
							<#assign editSelectfield = "editSelectfield${index}">
							<#assign editSelecttext = "editSelecttext${index}">
							<#assign onchangeScript = "onchangeScript${index}">
							
							formatter:function(value,row){
							    <#if c[editSelecttext] != ''>
									return row.${c[editSelecttext]!""};
								</#if>
							},
							editor:{
								type:'${c[celleditType]!""}',
								options:{
									onSelect:function(value){cellChangeData${c['name']!""} = value;},
									onChange:function(newValue,oldValue,rowData){var rowData =cellChangeData${c['name']!""}; ${c[onchangeScript]!""}},
									valueField:'${c[editSelectfield]!""}',
									textField:'${c[editSelecttext]!""}',
									data:combobox${c['name']!""},
									required:true
								}
							}
						<#elseif c[celleditType]?? && c[celleditType] == 'checkbox'>
							formatter:function(value,row){
								var on = '<input type=checkbox checked=checked/>';
								var off = '<input type=checkbox />';
								if(row.${c[cellfield]} == 1){
									return on;
								}else{
									return off;
								}
							},editor:{type:'checkbox',options:{on:'1',off:'0'}}
						<#else>
							editor:'${c[celleditType]!""}'
						</#if>
					</#if>	
				},
        	</#list>
		]]
			
		<#if c['panelWidth']?? && c['tableType'] == "combogrid" || c['tableType'] == "combotree">
			,panelWidth:'${c['panelWidth']!""}'
		</#if>
		
		<#if c['tableType'] == "combotree">
			,required: true
		</#if>
		
		
		<#if c['idField']??>
			,idField:'${c['idField']!""}'
		</#if>
		
		<#if c['tableType'] == 'treegrid' || c['tableType'] == 'combotreegrid'>
			,treeField:'${c['textField']!""}'
		<#else>
			,textField:'${c['textField']!""}'
		</#if>
		
		<#if c['fitColumns']??>
			,fitColumns:${c['fitColumns']!""}
		</#if>
			
		<#if c['tableHasPager']??>
			,pagination:${c['tableHasPager']!""}
		</#if>
		
		<#if  c['contextMenu']??>
			,onContextMenu: onContextMenu${c['name']!""}
		</#if>
		
		<#if  c['tableType']?? && c['tableType'] == "combogrid">
			<#--下拉--->
			,onChange:function(newValue, oldValue){
				${c['combogridChangeScript']!""}
			}
		</#if>
		
		<#if c['tableType']?? && c['tableType'] =="datagrid" >
			<#--事件-->
			,onClickRow:function(rowIndex, rowData){
				${c['datagridClickScript']!""}
			}
		</#if>
		
		<#if c['tableType']?? && c['tableType'] =="treegrid" >
			<#--事件-->
			,onClickRow:function(rowData){
				${c['treegridClickScript']!""}
			}
		</#if>
		
		<#--编辑-->
		<#if c['setEdit']??>
			,onClickCell : function(index, field) {
					if(Index${c['name']!""} != index){
						if(endEditing${c['name']!""}()){
							$("#dg${c['name']!""}").${c['tableType']!""}('selectRow', index)
								.${c['tableType']!""}('beginEdit', index);
							var ed = $("#dg${c['name']!""}").${c['tableType']!""}('getEditor', {
								index: index,
								field: field
							});
							if(ed) {
								($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
							}
							Index${c['name']!""} = index;
						<#--自动新增空白列-->	
						<#if c['tableType']?? && c['setRowsAdd']?? && c['tableType'] == "datagrid">
							var rows = $('#dg${c['name']!""}').datagrid('getRows');
							var len = rows.length-1;
							if(!isNull(rows[len])){
								$('#dg${c['name']!""}').datagrid('appendRow', {});
							}
							if(isNull(rows[len]) && index == len ){
								$('#dg${c['name']!""}').datagrid('appendRow', {});
							}
						</#if>
							
						} else {
							setTimeout(function() {
								$("#dg${c['name']!""}").${c['tableType']!""}('selectRow', Index${c['name']!""});
							}, 0);
						}
					}
				}
				/*----------------------------编辑下拉选择类型------------------------------*/
				<#list c['cellConf']?split(",") as index>
					<#assign cellfield = "cellfield${index}">
					<#assign celleditType = "celleditType${index}">
					<#if c[celleditType]?? && c[celleditType] == 'combobox'>
						<#assign editSelectfield = "editSelectfield${index}">
						<#assign editSelecttext = "editSelecttext${index}">
						,onEndEdit:function(index,row){
						  <#if c[cellfield]!= '' && c[editSelecttext] != ''>
							var ed = $(this).datagrid('getEditor', {
								index: index,
								field: '${c[cellfield]!""}'
							});
							row.${c[editSelecttext]!""} = $(ed.target).combobox('getText');
						  </#if>
						}
					</#if>
				</#list>
			
		</#if>
		/*------------------------数据加载完----------------------------------*/
			,onLoadSuccess:function(){
			<#if c['setEdit']??>
				<#if c['tableType']?? && c['tableType'] == "datagrid">
					var rows = $('#dg${c['name']!""}').datagrid('getRows');
					if(rows.length == 0){
						$('#dg${c['name']!""}').datagrid('appendRow', {});
					}
				</#if>
			</#if>
			<#if  c['LoadSuccessScript']??>
				${c['LoadSuccessScript']!""}
			</#if>
			}
				
	})
	
		
		<#if c['setEdit']??>
			//编辑js
			function endEditing${c['name']!""}(){
				if(Index${c['name']!""} == undefined) {
					return true
				}
				if($('#dg${c['name']!""}').${c['tableType']!""}('validateRow', Index${c['name']!""})) {
					$("#dg${c['name']!""}").${c['tableType']!""}('endEdit', Index${c['name']!""});
					Index${c['name']!""} = undefined;
					return  true;
				} else {
					return  false;
				}
			}
		</#if>
		<#if c['contextMenu']??>
			//右键菜单
			function onContextMenu${c['name']!""}(e,row){
				if (row){
					e.preventDefault();
					$(this).treegrid('select', row.id);
					$("#mm${c['name']}").menu('show',{
						left: e.pageX,
						top: e.pageY
					});				
				}
			}
		</#if>
		<#if c['contextMenu']?? && c['tableType'] == "treegrid">
			function append${c['name']!""}(){
				var node = $('#dg${c['name']!""}').treegrid('getSelected');
				
			}
			function edit${c['name']!""}(){
				var node = $('#dg${c['name']!""}').treegrid('getSelected');
			}
			function removeIt${c['name']!""}(){
				var node = $('#dg${c['name']!""}').treegrid('getSelected');
			}
			function collapse${c['name']!""}(){
				var node = $('#dg${c['name']!""}').treegrid('getSelected');
			}
			function expand${c['name']!""}(){
				var node = $('#dg${c['name']!""}').treegrid('getSelected');
			}
		</#if>
</#macro>
<#-------------------------------------------easyUI表格組件end---------------------------------------->





<#-------------------------------------------搜索条件begin---------------------------------------->
<#macro convertAddSearch c >
		<div class="search_main">
			<input type="hidden" value="${c['selectOption']}" id="selectOption">
			<input type="hidden" value="${c['selectLabel']}" id="selectLabel">
			<input type="hidden" value="${c['selectName']}" id="selectName">
			<input type="hidden" value="${c['textLabel']}" id="textLabel">
			<input type="hidden" value="${c['textName']}" id="textName">
		</div>
		<!--
			<input type="submit" value="搜索" class="btn btn-primary">  
			<input type="reset" value="清空" class="btn btn-primary">
		-->
</#macro>
<#macro convertAddSearchScript c >	<#------ 搜索条件脚本 ----->
	new addSearch();
</#macro>
<#-------------------------------------------搜索条件end---------------------------------------->
<#-------------------------------------------级联下拉框begin---------------------------------------->
<#macro convertMultilevelLinkage c >
	<div>
		<input id="select_select2_sql" type="hidden" value="${c['select_select2_sql']}"/>
		<input id="select_select2_name" type="hidden" value="${c['select_select2_name']}"/>
		<input id="select_select2_label" type="hidden" value="${c['select_select2_label']}"/>
		<input type="hidden" class="select_select2_finaVal" name="${c['name']}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" />
		<div class="select_select2_main"></div>
	</div>
</#macro>
<#macro convertMultilevelLinkageScript c >	<#------ 级联下拉框脚本 ----->
;(function(){
/**
 * 三级联动插件 1.0
 * 
 * 使用方法：
 *  var sql='province;city;area';
	var label='省：;市：;县：';
	var name='provinceId;cityId;id'
	var option={name:name,sql:sql,label:label};
 *  new multilevelLinkage(option)
 * 
 * @author venson
 */

var multilevelLinkage=function(option){
		this.option=$.extend(multilevelLinkage.DEFAULTS,option);
		this.option.names=this.option.name.split(';');
		this.option.labels=this.option.label.split(';');
		this.option.sqls=this.option.sql.split(';');
		this.init();
	}

	multilevelLinkage.DEFAULTS={
		name:$("#select_select2_name").val(),	//select标签name属性值
		label:$("#select_select2_label").val(),   //label标签值
		sql:$("#select_select2_sql").val(),     //sql语句数据来源
		idPrefix:'select_select2',  //id前缀
		url:'api/execute',  //后端接口
		container:'.select_select2_main'
	}
	multilevelLinkage.prototype={
			init:function(){
				var that=this;
				$.each(that.option.sqls,function(i,e){
					if(!$.trim(e)){
						return;
					}
					//添加dom节点
					var html='<div class="col-md-2"><div class="input-group"><span class="input-group-addon">'+that.option.labels[i]+'</span><select id="'+that.option.idPrefix+i+'" name="'+that.option.names[i]+'" class="'+that.option.idPrefix+' form-control"></select></div></div>';
					
					$warp=$(html);
					$(that.option.container).append($warp);
					var $select2=$warp.find("."+that.option.idPrefix)
					//判断是否为第一节点，如果是则预加载数据
					if(i==0){
						that.setData('#'+that.option.idPrefix+'0',Comm.getData(that.option.url+"/"+e));
					}else{
						$select2.parent().hide();
					}
					//绑定事件
					$select2.on('change',function(){
						that.clearData('#'+that.option.idPrefix+(i+1));
						//判断数据是否为空
						if($.trim($(this).val())){
							//最后一级
							 if(i==that.option.sqls.length-1){
								 var finalVal=that.getFinalVal(that.option.names);
								 $(that.option.container).prev("input[type='hidden']").val(finalVal);
								return;
							}else{
								var sql=that.option.sqls[i+1];
								var paramName=$(this).attr("name");
								if(sql){
									var param={};
									param[$(this).attr("name")]=$(this).val();
									that.setData('#'+that.option.idPrefix+(i+1),Comm.getData(that.option.url+"/"+$.trim(sql),that.getParams(sql)));
								}
							}
						}
					})
				})
			},
			getParams:function(sql){
				var that=this;
				var param={};
				param.APIId=$.trim(sql);
				$.each(that.option.names,function(i,e){
					var val=$("."+that.option.idPrefix+'[name='+e+']').val();
					if(val){
						param[e]=val;
					}
				})
				return param;
			},
			clearData:function(tag){
				var i=tag.replace('#'+this.option.idPrefix,'')/1;
				//隐藏下级下拉框
				if(i<this.option.sqls.length-1&&i!=0){
					for(i;i<this.option.sqls.length;i++){
						$('#'+this.option.idPrefix+i).parent().hide();
					}
				}
			},
			getFinalVal:function(names){
				var that=this;
				var finalVal=[];
				$.each(names,function(i,e){
					finalVal.push($("."+that.option.idPrefix+'[name='+e+'] option:selected').text());
				})
				return finalVal.join(' ');
			},
			setData:function(tag,data){
				if(data.length==0){
					Comm.alert("未找到数据。");
					return;
				}
				/*
				$(tag).width(200);
				$(tag).html("<option></option>");
				$(tag).select2({
					  data: data
				})*/
				
				$(tag).parent().show();
				Comm.setSelectData($(tag),data);
			}
			
			
	}
	new multilevelLinkage();
})();
</#macro>
<#-------------------------------------------级联下拉框end---------------------------------------->


<#-------------------------------------------输入框组件begin---------------------------------------->
<#macro convertInputext c >
	<input type='text' class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if>
	'
	<#if c['style']??&&c['style']=='hasNum' >
		onkeyup='this.value=this.value.replace(/[^\-?\d.]/g,"")'
	</#if>
	
	style='
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>display:none;</#if></#noparse>
	<#if c['style']?? >
		${c['style']}
	</#if>
	'
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
		<div class='<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
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

	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
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
										<!--<th width="10%" class="text-center">序号</th>-->
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
	<input type='text' class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
	'data-input="chosen"
	<#if c['style']?? >
		style='${c['style']}'

	</#if>

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
	<input type='text' class='chosenName text-ellipsis form-control
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
	'
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	style='cursor:text;position: relative;color:#999;height:auto;'>
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
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
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
	<input type='text' class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		form-control form-date
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>'
	<#if c['name']?? >
		name='${c['name']}'
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
	<#if c['style']?? >
		style='${c['style']}'

	</#if>

	value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"

	id='${(c['pageId'])!""}'
	<#if c['description']?? >
		title='${c['description']}'
	</#if>
	/>
</#macro>

<#-------------------------------------------日期end-------------------------------------------->

<#-------------------------------------------单选框radio begin------------------------------------------>
<#macro convertRadio c>

	<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
</#macro>
<#-------------------------------------------单选框radio end------------------------------------------>

<#-------------------------------------------多选框checkbook begin------------------------------------------>
<#macro convertCheckbox c>
	<label class="radio-inline hidden"><input type="checkbox" checked name="${c['name']}" value="" id="${c['pageId']}"></label>
	<#noparse><#if others??> ${others['</#noparse>${c['name']}<#noparse>']!''}</#if></#noparse>
</#macro>

<#macro convertChangeScript component >
	<#if component['onchangeScript']??><#--onchange 事件 -->
		$('#${(component['pageId'])!""}').change(function(){
			${component['onchangeScript']}
			});
	</#if>
</#macro>
<#macro convertDatetimeScript component >
	(function(){
		var dateType="${component['dateType']}";
		var id='#${(component['pageId'])!""}';
		var minView,startView;
		if(dateType.length>10){
			$(id).datetimepicker({
					language : 'zh-CN',
					weekStart : 1,
					todayBtn : 1,
					autoclose : 1,
					todayHighlight : 1,
					startView : 2,
					forceParse : 0,
					showMeridian : 1,
					format : dateType
			});
		}else if(dateType.length<10){
				$(id).datetimepicker({
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
		}else{
			$(id).datetimepicker({
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
		}
		
	})();
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
	<select class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
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
<#-------------------------------------------下拉框select end------------------------------------------>

<#-------------------------------------------角色select start------------------------------------------>
<#macro convertRoleSelect c >
	<select class='
	<#if c['css']?? && c['css']?length gt 0>
		${c['css']}
	<#else>
		${"form-control"}
	</#if>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
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
	
	<#if c['defineNote']?? && c['defineNote']=='1'>
	   <a   class='btn btn-mini btn-success btn-close' id='note${(c['pageId'])!""}' style='top: 8px; right: 6px; position: absolute;'><i class='icon-eye-open'></i></a>
	</#if>
	
	<textarea   class='
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
</#macro>
<#-------------------------------------------textarea end------------------------------------------>


<#-------------------------------------------commondwords end-------------------------------------------->


<#macro convertTextareaScript component >

	<#if component['defineNote']?? && component['defineNote']=='1'>
	
	$(function(){
		var pageId='${(component['pageId'])!""}';
	    InitQuickReply(pageId);
	});
	
	</#if>


</#macro>


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
	<label  class="control-label <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse> 	
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
	<label  class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>"
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
			 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>'
			 <#if c['style']?? >
				style='display:none;width:100%; ${c['style']}'
			 </#if>
			 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
			 <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>
		><#if c['dataItemCode']??  && c['dataItemCode']?length gt 0><#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse></#if></textarea>

</#macro>

<#macro convertKindEditorScript c >
	KindEditor.ready(function(K){
		  			var options ={
			            allowFileManager : true,
			            allowUpload : true,
			            uploadJson : basePath+'common/file/uploadImg.do',
			            //height:"300px",
			            //readonlyMode:true,
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
			                        reader.readAsDataURL(file);
			                    });
			                }
			
			            }
			        };
			        var editor_${c['name']} = K.create('textarea[name="${c['name']}"]',options);
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'></#noparse>editor_${c['name']}<#noparse>.readonly("false");</#if></#noparse>
				});
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
	<div class="uploadPreview">
			<input type="hidden" id="uploadImgType" value="${c['uploadType']!''}"/>
			<input type="hidden" class="photo" name="${c['name']!""}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>"/>
	      	<div class="photo-con"><img id="${c['pageId']}_Img"
	      	<#noparse><#if </#noparse>(${c['dataItemCode']}) <#noparse>?? &&</#noparse>(${c['dataItemCode']})<#noparse>?length gt 0></#noparse>
	      		src="<#noparse>${basePath}</#noparse>common/file/showPicture.do?id=<#noparse>${</#noparse>${c['dataItemCode']}<#noparse>}</#noparse>"
	      	<#noparse></#if></#noparse>
	      		alt="点击选择图片"/></div>
	      	<input type="file" id="${c['pageId']}" name="${c['pageId']}"/>
	      	<div class="photo-btn"><a class="btn delete" href="javascript:;">删除</a><span class="msg text-danger"></span></div>
	</div>
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
	<div id="${c['pageId']}" class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>
	<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'> disabled</#if></#noparse>">
		<div class="row" id="${c['pageId']}_buttons">
			<#if c['operateAdd']?? && c['operateAdd']=='1'>
				<button id='${c['pageId']}_Add' class='btn btn-info'>新增</button>
			</#if>
			<#if c['operateEdit']?? && c['operateEdit']=='1'>
				<button id='${c['pageId']}_Edit' class='btn btn-info'>编辑</button>
			</#if>
			<#if c['operateDelete']?? && c['operateDelete']=='1'>
				<button id='${c['pageId']}_Delete' class='btn btn-success'>删除</button>
			</#if>
		</div>


		<table class="table gridtable table-bordered">
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
		<#if c['hasPager']?? && c['hasPager']=='1'>
			<div class="datapager">
				<div class="m_p_bottom">
					<span><select class="form-control showSize" data-pagesize="0"><option value="10">10</option><option value="20">20</option><option value="50">50</option></select>&nbsp;&nbsp;条记录/页</span>
					<span class="direction"><a class="backward" href="javascript:;" title="首页"><i class="icon-step-backward"></i>&nbsp;首页</a>
						<a class="left" href="javascript:;" title="上一页"><i class="icon-double-angle-left"></i>&nbsp;上一页</a>
						<a class="right" href="javascript:;" title="下一页">下一页&nbsp;<i class="icon-double-angle-right"></i></a>
						<a class="forward" href="javascript:;" title="末页">末页&nbsp;<i class="icon-step-forward"></i></a>
					</span>
					<span>当前第<label class="text-danger currentpage">0</label>页</span>
					<span>共<label class="text-danger totalpage">0</label>页</span>
					<span>共<label class="text-danger total">0</label>条记录</span>
					<span>跳至<label>第</label><input class="form-control showPage" type="text"><label>页</label></span>
				</div>
			</div>
		</#if>
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
								  ,</#list></#if>pageSize:999}});

	<#if c['operateAdd']?? && c['operateAdd']=='1'>
		$("#${(c['pageId'])}_Add").click(function(){

			var paramData = new Map();
			var str="";
			<#if c['param']?? && c['param']?size gt 0>
				<#list c['param'] as item>
					str+="&";
					str+="${item['storeParamId']}";
			        str+="=";
			        str+="<#noparse>${(</#noparse>${item['exportParam']}<#noparse>)!''}</#noparse>";
					paramData.put('${item['storeParamId']}','<#noparse>${(</#noparse>${item['exportParam']}<#noparse>)!''}</#noparse>');
				</#list>
			</#if> 
			
		    
			top.dialog({
				id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
				title : '新增...',
				url : "<#noparse>${basePath}</#noparse>document/view.do?dynamicPageId=${c['alertPage']}"+str,
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
			if(ids == null || ids == "" || ids.indexOf(",")>0){
				alert("请选中一项操作");
				return false;
			}
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
	<div class="row" style="width:100%;" id="searchform">
		<div id="collapseButton" class="in">
			<form class="form-horizontal form-condensed" id="createForm" action="<#noparse>${basePath}</#noparse>workflow/wf/openTask.do" role="customForm" method="post">
			<#noparse>
			<input type="hidden" name="currentPage" value="${currentPage!"1"}">
    		<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
			<input type="hidden" name="dynamicPageId" value="${ (vo.dynamicPageId)!""}" id="dynamicPageId"/>
			<input type="hidden" name="workflowId" value="${ (vo.workflowId)!""}" id="workflowId"/>
			<input type="hidden" name="update" value="${ (vo.update)?string("true","false")}" id="update"/>
			<input type="hidden" name="dynamicPageName" value="${ (vo.dynamicPageName)!""}" id="dynamicPageName"/>
			<input type="hidden" name="orderBy" value="${ (vo.orderBy)!""}" id="orderBy"/>
			<input type="hidden" name="allowOrderBy" value="${ (vo.allowOrderBy)!"1"}" id="allowOrderBy"/>
			<input type="hidden" name="WorkID" value="${ (vo.workItemId)!""}" id="WorkID"/>
			<input type="hidden" name="FK_Node" value="${ (vo.entryId)!""}" id="FK_Node"/>
			<input type="hidden" name="FK_Flow" value="${ (vo.flowTempleteId)!""}" id="FK_Flow"/>
            <input type="hidden" name="FID" value="${ (vo.fid)!""}" id="FID"/>
			<input type="hidden" name="nodeId" value="${ (vo.nodeId)!""}" id="nodeId"/>
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

	<div class="row row-table" id="searchform">
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
			<input type="hidden" name="WorkID" value="${ (vo.workItemId)!""}" id="WorkID"/>
			<input type="hidden" name="FK_Node" value="${ (vo.entryId)!""}" id="FK_Node"/>
			<input type="hidden" name="FK_Flow" value="${ (vo.flowTempleteId)!""}" id="FK_Flow"/>
            <input type="hidden" name="FID" value="${ (vo.fid)!""}" id="FID"/>
			<input type="hidden" name="nodeId" value="${ (vo.nodeId)!""}" id="nodeId"/>
			</#noparse>
			<div class="col-xs-11 col-table">
				<#if c['layoutAndCom']?? >
					${c['layoutAndCom']}
				</#if>
			</div>
			<div class="col-xs-1 btn-group col-table">

				<button class="btn btn-primary" type="submit">搜索</button>
				<#----<button class="btn" type="reset">重置</button>-->
			</div>
			</form>
		</div>
	</div>

</#macro>
<#-------------------------------------------搜索框组件end---------------------------------------->

<#-------------------------------------------文件上传框组件begin---------------------------------------->
<#macro convertFile c >
	<input type="hidden" id="uploadType" value="${c['uploadType']!''}"/>
	<input type="hidden" id="isIndex" value="${c['isIndex']!''}"/>
	<#if c['showType']?? && c['showType']=='2' >
		<div id='${(c['pageId'])!""}' class="uploader" >
			<input type="hidden" class="orgfile" name="${c['name']}" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" />
		    <div class="btns mb10">
		        <div class="picker"><i class="icon-upload-alt"></i> 上传附件</div>
		        <div class="btn btn-primary" style="background:rgb(0, 183, 238); border-radius: 3px; border: medium none; padding: 6px 12px;margin-right:12px;float:left;" id="MoreDownload"><i class="icon-download-alt"></i> 下载附件</div>
		        <div class="btn btn-primary" style="background:rgb(0, 183, 238); border-radius: 3px; border: medium none; padding: 6px 12px;float:left;" id="FileRemove"><i class="icon-trash"></i> 删除附件</div>
		    </div>
		    <div class="table table-bordered uploader-list oldlist">
		    	<table class="table datatable table-bordered table-hover" id="uploadListTable">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>文件名称</th>
							<th>文件类型</th>
							<th>文件大小</th>
						</tr>
					</thead>
					<tbody></tbody>
					<tfoot>
						<tr><td colspan="5" class="tips"></td></tr>
					</tfoot>
				</table>
			</div>
		</div>
	<#else>
    	<div style="position: relative;">
			<input type="text" value="<#noparse><#if (</#noparse>${c['dataItemCode']}<#noparse>)?? && (</#noparse>${c['dataItemCode']}<#noparse>)?length gt 32></#noparse>-<#noparse></#if></#noparse>" 
			 readonly="readonly" style="width: 310px;">
            <input         
            <#noparse><#if (</#noparse>${c['dataItemCode']}<#noparse>)?? && (</#noparse>${c['dataItemCode']}<#noparse>)?length gt 32></#noparse>disabled="disabled"<#noparse></#if></#noparse> 
            type="button" value="选择文件">                     
            <input name="file" style="left: 320px;  opacity: 0; position: absolute; top: 11px; width: 70px;" class="file" onchange="upload(this)" type="file"
            <#noparse><#if (</#noparse>${c['dataItemCode']}<#noparse>)?? && (</#noparse>${c['dataItemCode']}<#noparse>)?length gt 32></#noparse>disabled="disabled"<#noparse></#if></#noparse> 
            >            
			<input id='${(c['pageId'])!""}' name="${c['name']}" lang="${c['dataItemCode']}" class="fileName" value="<#noparse>${(</#noparse>${c['dataItemCode']}<#noparse>)!''}</#noparse>" type="hidden">			
			<#if c['description']?? && c['description']?length gt 0>
				<input class="add"
				<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
				<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>				
				 type="button" value='${(c['description'])!""}'>
			</#if>		
    	</div>
	</#if>
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

				"
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
				</#if>
			</#if>
		}else{
			alert("请选择一条记录！");
		}	
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
<#macro convertComponentScript component>

	<#local componetType=component['componentType']?number>
	<#switch componetType>
		<#case 1002>
			<@convertDatetimeScript component/>
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
		<#case 1011>
			<@convertFileScript component/>
			<#break>
		<#case 1012>
			<@convertContainPageScript component/>
			<#break>
		<#case 1013>
			<@convertContainPageScript component/>
			<#break>
		<#case 1016>
			<@convertImageScript component/>
			<#break>
		<#case 1017>
			<@convertdataGridScript component/>
			<#break>
		<#case 1019>
			<@convertKindEditorScript component/>
			<#break>
		<#case 1024>
			<@convertAppendixScript component/>
			<#break>
		<#case 1025>
			<@tabsActiveScript component/>
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
		<#case 1021>
			<@convertCommondWordsScript component/>
			<#break>
		<#case 1005>
			<@convertTextareaScript component/>
			<#break>
		<#case 1033>
			<@convertMultilevelLinkageScript component/>
			<#break>
		<#case 1034>
			<@converttabScript component/>
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
		class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse> <#if c['alloworderby']?? && c['alloworderby'] == "1">sort</#if> text-ellipsis"  title="<#if c['columnName']?? >${c['columnName']}</#if>">
		<#if c['columnName']?? >
		${c['columnName']}
		</#if>
		<#if c['alloworderby']?? && c['alloworderby'] == "1"><span id="${c['sortName']!''}" class="hidden"></span><i class="icon-sort"></i></#if>
		<#--icon-caret-up icon-caret-down-->
		</th>
	<#--<#noparse></#if></#noparse>-->
</#macro>

<#macro parseColumnData c >
	<#--<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] != 'true'></#noparse> -->
		<td class="<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>">
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
                  <div id='suggessBody${(c['pageId'])!""}' class='clearfix' style='min-height:100px;'>
                      <div class='col-xs-12' colspan='12'>


                      	<p  class='pull-right clearfix'>

                      		<#if c['freeFlow']?? && c['freeFlow']=='1'>

							<input type='hidden'  id='but${(c['pageId'])!""}'  value='添加意见'
                      		class='btn btn-primary <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>'

                      		<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['disabled'])?? && status['</#noparse>${c['name']}<#noparse>']['disabled'] == 'true'>disabled="disabled"</#if></#noparse>
							<#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['readonly'])?? && status['</#noparse>${c['name']}<#noparse>']['readonly'] == 'true'>readonly="readonly"</#if></#noparse>

                      		onClick="openDialog('${(c['pageId'])!""}','${(c['showType'])!""}')" style='margin-right:8px;'/>

				     		<#else>

							<input type='hidden' id='but${(c['pageId'])!""}' value='添加意见'
                      		class='btn btn-primary <#noparse><#if (status['</#noparse>${c['name']}<#noparse>']['hidden'])?? && status['</#noparse>${c['name']}<#noparse>']['hidden'] == 'true'>hidden</#if></#noparse>'

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