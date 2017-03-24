<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="screen-desktop device-desktop">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
<meta name="renderer" content="webkit">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<link rel="stylesheet" href="<%=basePath%>base/resources/zui/dist/css/zui_define.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/main.css">
<link rel="stylesheet" href="<%=basePath%>base/resources/artDialog/css/ui-dialog.css">
<link rel="stylesheet" href="<%=basePath%>resources/plugins/tips/tip-yellowsimple/tip-yellowsimple.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/content/uploader.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/content/layout.css">
<style>
label.control-label {
	font-weight: bold;
}
.form-condensed .form-group {
	margin-bottom: 0px;
}
.check-all {
	text-align: center;
}
.hides {
	display: block;
	width: 0;
	height: 0;
	overflow: hidden;
}
</style>
<!--[if lt IE 9]>
	  <script src="<%=basePath%>resources/plugins/zui/assets/html5shiv.js"></script>
	  <script src="<%=basePath%>resources/plugins/zui/assets/respond.js"></script>
        
	<![endif]-->
<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
<style id="poshytip-css-tip-yellowsimple" type="text/css">
div.tip-yellowsimple {
	visibility: hidden;
	position: absolute;
	top: 0;
	left: 0;
}
div.tip-yellowsimple table.tip-table, div.tip-yellowsimple table.tip-table td {
	margin: 0;
	font-family: inherit;
	font-size: inherit;
	font-weight: inherit;
	font-style: inherit;
	font-variant: inherit;
	vertical-align: middle;
}
div.tip-yellowsimple td.tip-bg-image span {
	display: block;
	font: 1px/1px sans-serif;
	height: 10px;
	width: 10px;
	overflow: hidden;
}
div.tip-yellowsimple td.tip-right {
	background-position: 100% 0;
}
div.tip-yellowsimple td.tip-bottom {
	background-position: 100% 100%;
}
div.tip-yellowsimple td.tip-left {
	background-position: 0 100%;
}
div.tip-yellowsimple div.tip-inner {
	background-position: -10px -10px;
}
div.tip-yellowsimple div.tip-arrow {
	visibility: hidden;
	position: absolute;
	overflow: hidden;
	font: 1px/1px sans-serif;
}
</style>
</head>
<body id="main">
<div class="container">
  <div class="row" id="buttons">
    <button id="871e0ff4-57ca-4a85-acad-19230bd8f1c6" title="" class=" btn   btn-default

				" target=""> <i class="icon-file"></i> 取消挂起</button>
    <button id="c1bf168b-4a69-477c-bfcb-46f2215b2519" title="" class=" btn   btn-default

				" target="1338"> <i class="icon-file"></i> 返回</button>
  </div>
  <div class="row" id="newTitle">
    <label class="control-label " style="width: 100%; text-align: center; font-weight: 800; font-size: 24px;"> 挂起方式 </label>
  </div>
  <div class="row">
    <form class="form-horizontal form-condensed" id="groupForm" action="http://localhost:8899/szcloud/workflow/wf/excute.do" role="customForm" method="post" style="border:#333 1px solid">
      
      <input type="hidden" name="WorkID" value="" id="WorkID">
      <input type="hidden" name="FK_Node" value="" id="FK_Node">
      <input type="hidden" name="FK_Flow" value="" id="FK_Flow">
      <input type="hidden" name="FID" value="" id="FID">
     
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table-c">
        <tbody>
          <tr>
            <td style="border: 1px solid rgb(0, 0, 0); text-align: center;" class="col-xs-2" colspan="2"><label class="control-label " style="text-align: center;" id="d27cf297-bb08-4699-a543-a1b20faa5131" name="241f1d705010cef91b04eca9ee0cd243"> 挂起方式 </label></td>
            <td style="border: 1px solid rgb(0, 0, 0); text-align: center;" class="col-xs-5" colspan="5"><label class="radio-inline" style="text-align: center; line-height: 20px; margin-left: 10px;">
                <input type="radio" name="RB_HungWay" value="0">
                	永久挂起</label>
            </td>
            <td style="border: 1px solid rgb(0, 0, 0); text-align: center;" class="col-xs-5" colspan="5"><label class="radio-inline" style="text-align: center; line-height: 20px; margin-left: 10px;">
                <input type="radio" name="RB_HungWay" value="1">
                	在指定的日期自动解除挂起</label>
            </td>
          </tr>
          <tr>
            <td style="border: 1px solid rgb(0, 0, 0); text-align: center;" class="col-xs-2" colspan="2">
            	<label class="control-label " style="text-align: center;">解除流程挂起的日期 </label>
            </td>
            <td style="border: 1px solid rgb(0, 0, 0); text-align: center;" class="col-xs-10" colspan="10">
            	<input type="text" name="TB_RelData" id="TB_RelData" class="form-control form-date" placeholder="选择或者输入一个日期：yyyy-MM-dd">
            </td>
          </tr>
          <tr>
            <td style="border: 1px solid rgb(0, 0, 0); text-align: center;" class="col-xs-12" colspan="12">
            	<label class="control-label " style="text-align: center;">挂起原因 </label>
            </td>
          </tr>
          <tr>
            <td style="border: 1px solid rgb(0, 0, 0); text-align: center;" class="col-xs-12" colspan="12">
            	<textarea class="form-control" style="width:100%" name="72a57330be2a51bcbb19513af66593be" rows="4" id="2a56f37f-a294-4652-b5c5-b2ad11a55bfb" title=""></textarea>
            </td>
          </tr>
          
        </tbody>
      </table>
    </form>
  </div>
</div>
<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>base/resources/zui/dist/js/zui.js"></script> 
<script type="text/javascript" src="<%=basePath%>base/resources/artDialog/dist/dialog-plus-min.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/tips/jquery.poshytip.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidator-4.1.0.a.js" charset="UTF-8"></script> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/formValidator4.1.0/formValidatorRegex.js" charset="UTF-8"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/ajaxfileupload.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/uploadPreview.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/map.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/common.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/platform.document.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/webuploader-0.1.5/dist/webuploader.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/uploader.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/scripts/ntkoWord.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/highChart/highcharts.js"></script> 
<script type="text/javascript" src="<%=basePath%>resources/plugins/highChart/exporting.js"></script> 
<script>
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
<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script> 
<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/kindeditor/kindeditor-min.js"></script> 
<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/kindeditor/lang/zh_CN.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/plugins/zTree_v3/zTree.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/scripts/dynamicSelect.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/scripts/workFlow.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/plugins/commondWords/commondWords.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/scripts/defineNote.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/scripts/platform.dataGrid.js"></script> 

<script type="text/javascript" src="<%=basePath%>/resources/scripts/plateform.userSelect.js"></script> 
<script type="text/javascript" src="<%=basePath%>/resources/scripts/plateform.subUserSelect.js"></script> 
<script type="text/javascript">
		
$("document").ready(function(){
                $(".chosenName").css("height","auto");

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

</script>
<script>
function afterPageLoad() {
    $.getScript('<%=basePath%>resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js', function() {
        if($.fn.datetimepicker)
        {
            $('.form-datetime').datetimepicker(
            {
                weekStart: 1,
                todayBtn:  1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                forceParse: 0,
                showMeridian: 1,
                format: 'yyyy-mm-dd hh:ii'
            });
            $('.form-date').datetimepicker(
            {
                language:  'zh-CN',
                weekStart: 1,
                todayBtn:  1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                minView: 2,
                forceParse: 0,
                format: 'yyyy-mm-dd'
            });
            $('.form-time').datetimepicker({
                language:  'zh-CN',
                weekStart: 1,
                todayBtn:  1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 1,
                minView: 0,
                maxView: 1,
                forceParse: 0,
                format: 'hh:ii'
            });
        }
    });
}
</script>

</body>
</html>