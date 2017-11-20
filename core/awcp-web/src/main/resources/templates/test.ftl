<#include "templates/parseFunction.ftl">
<#noparse>
<#assign path = request.getContextPath()>
<#assign basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/">
</#noparse>
<#list dataAlias as dataAlia>
	<#noparse><#if </#noparse>${dataAlia}<#noparse>_list?? && </#noparse>${dataAlia}<#noparse>_list?size gt 0>
             <#assign</#noparse> ${dataAlia}<#noparse> = </#noparse>${dataAlia}<#noparse>_list[0]!''>
        </#if></#noparse>
</#list>
<#noparse>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
	<meta name="renderer" content="webkit">
    <meta http-equiv="pragma" content="no-cache">
 	<meta http-equiv="cache-control" content="no-cache"> 
    <meta http-equiv="expires" content="0">
 	<link rel="stylesheet" href="${basePath}resources/plugins/zui/dist/css/zui_define.css"/>
	<link rel="stylesheet" href="${basePath}resources/styles/main.css"/>
	<link rel="stylesheet" href="${basePath}base/resources/artDialog/css/ui-dialog.css"/>
	<link rel="stylesheet" href="${basePath}resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
    <link rel="stylesheet" href="${basePath}resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css"/>
	<link rel="stylesheet" href="${basePath}resources/styles/content/uploader.css">
	<link rel="stylesheet" href="${basePath}resources/styles/content/layout.css">
	<link href="${basePath}formdesigner/page/component/tab/tab.css" rel="stylesheet" />
	<style>
		label.control-label{font-weight:bold;}
		.form-condensed .form-group{margin-bottom:0px;}
		.check-all{text-align:center;}
		.hides{display: block;width: 0;height: 0;overflow: hidden;}
	</style>
	<!--[if lt IE 9]>
	  <script src="${basePath}resources/plugins/zui/assets/html5shiv.js"></script>
	  <script src="${basePath}resources/plugins/zui/assets/respond.js"></script>
        
	<![endif]-->	
</head>
<body id="main">
    <div class="container"> 
		<div class="row" id="buttons">
</#noparse>
			<#if pageActs??>
				<#list pageActs?sort_by("order") as act>
					<@convertButton act />
				</#list>
			</#if>
		</div>
		<div class="row" id="newTitle"></div>
<#noparse>
		<div class="row">
			<form class="form-horizontal form-condensed" id="groupForm" action="${basePath}workflow/wf/excute.do" role="customForm" method="post" style="border:#333 1px solid">
				<input type="hidden" name="docId" value="${ (vo.id)!""}" id="docId"/>
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
</#noparse>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">		
			 		<#if layouts??>
					 	<#if components??>
					 		<#list layouts?sort_by("order") as layout>
					 			<@parseLayout2 layout components/>
					 		</#list> 
					 	</#if>
			 		</#if>
<#noparse>
				</table>
			</form>
		</div>
	</div>
	<script src="${basePath}resources/JqEdition/jquery-1.10.2.js"></script>
	<script src="${basePath}resources/plugins/zui/dist/js/zui.js"></script>
	<script src="${basePath}resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
	<script src="${basePath}base/resources/artDialog/dist/dialog-plus-min.js"></script>
	<script src="${basePath}resources/plugins/tips/jquery.poshytip.js"></script>
	<script src="${basePath}resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" charset="UTF-8"></script>
	<script src="${basePath}resources/plugins/formValidator4.1.0/formValidatorRegex.js" charset="UTF-8"></script>
	<script src="${basePath}resources/scripts/jquery.serializejson.min.js"></script>
	<script src="${basePath}resources/scripts/ajaxfileupload.js"></script>
	<script src="${basePath}resources/scripts/uploadPreview.js"></script>
	<script src="${basePath}resources/scripts/map.js"></script>
	<script src="${basePath}resources/scripts/common.js"></script>
	<script src="${basePath}resources/scripts/platform.document.js"></script>
	<script src="${basePath}resources/plugins/webuploader-0.1.5/dist/webuploader.js"></script>
	<script src="${basePath}resources/scripts/uploader.js"></script>
	<script src="${basePath}resources/plugins/select2/select2.js"></script>
    <script src="${basePath}resources/scripts/ntkoWord.js"></script>
	<script src="${basePath}venson/js/common.js"></script>
	<script>
		var basePath = '${basePath!''}';
		function initTable(){
	        if($("div.datatable").size()>0){
	            $("div.datatable").remove();
			}
			$("table.table").removeAttr("style");
			var count=0;//默认选择行数为0
				  
			$("table.datatable").datatable({
				checkable: true,
				datareload:true,
				checksChanged:function(event){
					this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
					var checkArray = event.checks.checks;
					count = checkArray.length;
					for(var i=0;i<count;i++){//给隐藏数据机上name属性
						this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","_selects");
					}
				}
			});
			$("#datatable").find("div.datatable-rows").find("table tr td").each(function(){
				$(this).css("text-align","left");
			});    	  
			$("#datatable").find("div.datatable-rows").find("table tr td:first-child").each(function(){
				$(this).css("text-align","center");
			});                   
		}
    </script>
	<script src="${basePath}resources/plugins/select2/select2_locale_zh-CN.js"></script>
	<script src="${basePath}resources/plugins/zui/assets/kindeditor/kindeditor-min.js"></script>
    <script src="${basePath}resources/plugins/zui/assets/kindeditor/lang/zh_CN.js"></script>
    <script src="${basePath}resources/plugins/zTree_v3/zTree.js" ></script>
    <script src="${basePath}resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js" ></script>
    <script src="${basePath}resources/scripts/dynamicSelect.js"></script>
    <script src="${basePath}resources/scripts/defineNote.js"></script>
	<script  src="${basePath}resources/scripts/platform.dataGrid.js"></script>   
	<script src="${basePath}resources/scripts/plateform.userSelect.js"></script>
	<script src="${basePath}resources/scripts/plateform.subUserSelect.js"></script>
	<script src="${basePath}resources/scripts/plateform.sqlUserSelect.js"></script>
    <script src="${basePath}formdesigner/page/component/tab/swiper.min.js"></script>
    <script src="${basePath}formdesigner/page/component/tab/tab.js"></script>
 	<script src="${basePath}template/easyui/js/jquery.easyui.min.js"></script>
	<script>
</#noparse>		
		$("document").ready(function(){
            $(".chosenName").css("height","auto");
			$(".disabled").find("input,select,textarea,button,a").attr("disabled","disabled");		
			initTable();
            $("label").css("text-align","center");
            $("td").css("text-align","center");
            $("div.webuploader-container").css("float","left");
            $("div.mb10").css("margin-top","10px");
            $("div.mb10").addClass("clearfix");
            $("div.uploader-list").css("overflow","hidden");
            $("div.picker").css("float","left");
            $(".uploader-list tfoot td").css("border-left","none");
            $(".radio-inline").css("line-height","20px");
            $(".radio-inline").css("margin-left","10px");
            $("div.datatable-head-span").css("width","100%");
            $("#newTitle").html('<label class="control-label " style="width: 100%; text-align: center; font-weight: 800; font-size: 24px;">'+$(".container").find("tr").eq(0).find("label").eq(0).text()+'</label>');
            $(".container").find("table").find("tr").eq(0).remove();
			$("table.table thead tr th").each(function(){
                $(this).css("text-align","center");
            });
			$("#datatable").find("div.datatable-head").find("table").css("table-layout","fixed");
			$("#datatable").find("div.datatable-rows").find("table tr td").each(function(){
                $(this).css("text-align","left");
			});    	  
			$("#datatable").find("div.datatable-rows").find("table tr td:first-child").each(function(){
                $(this).css("text-align","center");
			});  
			
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
				
	        <#if page??>
				<#if page.getAfterLoadScript()??>
					${page.getAfterLoadScript()}
				</#if>
			</#if>
			
			$("div.datatable-head-span").css("width","100%");
		});	
		
function checkRequiredData(){
	<#if components??>
 		 <#if components?keys??>
	 		  <#list components?keys as layoutId>
		 			<#if components[layoutId]??>
			 			<#list components[layoutId] as component>
			 			<#if component['componentType']!='1012' && component['componentType']!='1009'>		
			 				<#if valdatorsMap[component['pageId']]?? || (component['validateAllowNull']?? && component['validateAllowNull']?number==0)>
								<#if component['componentType']=='1011'>
									var files = $('#${(component['pageId'])!""} .orgfile').val();
									if(!files){
										var tip = '${(component['validateErrorTip'])!''}';
										if(!tip){
											tip = '请确认附件已上传';
										}
										alert(tip);
										return false;
									}
								<#elseif component['componentType']=='1003' || component['componentType']=='1004'>
									var checkedCount = $('input[name="${(component['name'])!""}"]:checked').length;
									if(checkedCount==1){//默认有一个空的选择
										var tip = '${(component['validateErrorTip'])!''}';
										if(!tip){
											tip = '请选择数据';
										}
										alert(tip);
										return false;
									}
								<#else>
									var data = $('#${(component['pageId'])!""}').val();
									if(!data){
										var tip = '${(component['validateErrorTip'])!''}';
										if(!tip){
											tip = '请输入内容';
										}
										alert(tip);
										return false;
									}
								</#if>
			 				</#if>	
			 			</#if>
			 			</#list>
		 			</#if>
	 		  </#list>
 		</#if> 
	</#if>
	return true;
}
		
		<#list pageActs?sort_by("order") as act>
			<#if true>
				<@convertScript act />
			</#if>
		</#list>
	</script>
	<script>	
		$("table").each(function(index, element) {
			var currentTable = $(this);
		    if(currentTable.parent().parent("td").length>0){
				currentTable.find("tr").eq(0).find("td").css("border-top","none");
				currentTable.find("tr").each(function(index, element) {
		            $(this).children().eq(0).css("border-left","none");
		        });
			}
		});

		function formatUploadTable(){
		    var $table = $("div[id='datatable-uploadListTable']").find("table.table");
			$table.css("table-layout","fixed");
			$table.find("tr").each(function(){
				var $tr = $(this);
				$tr.find("td[data-index='0']").css("width","10%");
				$tr.find("td[data-index='1']").css("width","30%");
				$tr.find("td[data-index='2']").css("width","30%");
				$tr.find("td[data-index='3']").css("width","30%");
				$tr.find("th[data-index='check']").css("width","10%");
				$tr.find("th[data-index='1']").css("width","30%");
				$tr.find("th[data-index='2']").css("width","30%");
				$tr.find("th[data-index='3']").css("width","30%");		
			})		                        	  
		}
	</script>
</body>
</html>