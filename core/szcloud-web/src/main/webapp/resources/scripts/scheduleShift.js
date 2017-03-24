// JavaScript Document
$(document).ready(function(e) {
    SetEditTableCallback(false);
    
});

$("#isReadOnly").click(function(){
	if($(this).text()=="开始编辑"){
		$(this).html("结束编辑");
		$(this).removeClass("btn-success").addClass("btn-danger");
		$("#add").removeAttr("disabled");
		$("#deleteScheduleShift").removeAttr("disabled");
		SetEditTableCallback(true);
	}else{
		$(this).html("开始编辑");
		$(this).removeClass("btn-danger").addClass("btn-success");
		$("#add").attr("disabled","disabled");
		$("#deleteScheduleShift").attr("disabled","disabled");
		SetEditTableCallback(false);
		
		$("#scheduleShiftListForm").attr("action","scheduleShift/updateList.do").submit();
	}
	
	
});
function SetEditTableCallback(status){
	ShowDiv(status);	
}
function ShowDiv(i){
	if(i){
		$("#mask").remove();
	}else{
		$("body").append("<div id=mask></div>");
		$("#mask").css("background-color","#fff");
		$("#mask").css("width",window.screen.width-24);
		$("#mask").css("height",$("tbody.ui-selectable").height());
		$("#mask").css("position","absolute");
		$("#mask").css("z-index",99998);
		$("#mask").css("top",$("tbody.ui-selectable").scrollTop()+114);
		$("#mask").css("left",$("tbody.ui-selectable").scrollLeft());
		$("#mask").css("opacity",0.01);
	}
	
}

$('#add').bind("click", function(){
			var content='<div class="form-horizontal">'+
			'<div class="form-group">'+
			'<label class="col-md-2 control-label required">班次名称</label>'+
		    '<div class="col-md-10"><input id="shiftName" name="shiftName" type="text" class="form-control"  placeholder="请输入班次名称"></div>'+
		    '</div><div class="form-group">'+
		    '<label class="col-md-2 control-label">两头班</label>'+
		    '<div class="col-md-10"><input id="twice" name="twice" type="checkbox"></div>'+
		    '</div><div class="form-group">'+
		    '<label class="col-md-2 control-label required">班次时间段1</label>'+
		    '<div class="col-md-4">'+
		    '<select id="starttime1" class="form-control">'+
		    '<option value="00:00">00:00</option>'+
		    '<option value="00:30">00:30</option>'+
			'<option value="01:00">01:00</option>'+
			'<option value="01:30">01:30</option>'+
			'<option value="02:00">02:00</option>'+
			'<option value="02:30">02:30</option>'+
			'<option value="03:00">03:00</option>'+
			'<option value="03:30">03:30</option>'+
			'<option value="04:00">04:00</option>'+
			'<option value="04:30">04:30</option>'+
			'<option value="05:00">05:00</option>'+
			'<option value="05:30">05:30</option>'+
			'<option value="06:00">06:00</option>'+
			'<option value="06:30">06:30</option>'+
			'<option value="07:00">07:00</option>'+
			'<option value="07:30">07:30</option>'+
			'<option value="08:00">08:00</option>'+
			'<option value="08:30">08:30</option>'+
			'<option value="09:00" selected="selected">09:00</option>'+
			'<option value="09:30">09:30</option>'+
			'<option value="10:00">10:00</option>'+
			'<option value="10:30">10:30</option>'+
			'<option value="11:00">11:00</option>'+
			'<option value="11:30">11:30</option>'+
			'<option value="12:00">12:00</option>'+
			'<option value="12:30">12:30</option>'+
			'<option value="13:00">13:00</option>'+
			'<option value="13:30">13:30</option>'+
			'<option value="14:00">14:00</option>'+
			'<option value="14:30">14:30</option>'+
			'<option value="15:00">15:00</option>'+
			'<option value="15:30">15:30</option>'+
			'<option value="16:00">16:00</option>'+
			'<option value="16:30">16:30</option>'+
			'<option value="17:00">17:00</option>'+
			'<option value="17:30">17:30</option>'+
			'<option value="18:00">18:00</option>'+
			'<option value="18:30">18:30</option>'+
			'<option value="19:00">19:00</option>'+
			'<option value="19:30">19:30</option>'+
			'<option value="20:00">20:00</option>'+
			'<option value="20:30">20:30</option>'+
			'<option value="21:00">21:00</option>'+
			'<option value="21:30">21:30</option>'+
			'<option value="22:00">22:00</option>'+
			'<option value="22:30">22:30</option>'+
			'<option value="23:00">23:00</option>'+
			'<option value="23:30">23:30</option>'+
		    '</select></div><label class="col-md-2 control-label">至</label><div class="col-md-4">'+
		    '<select id="endtime1" class="form-control">'+
		    '<option value="00:00">00:00</option>'+
		    '<option value="00:30">00:30</option>'+
			'<option value="01:00">01:00</option>'+
			'<option value="01:30">01:30</option>'+
			'<option value="02:00">02:00</option>'+
			'<option value="02:30">02:30</option>'+
			'<option value="03:00">03:00</option>'+
			'<option value="03:30">03:30</option>'+
			'<option value="04:00">04:00</option>'+
			'<option value="04:30">04:30</option>'+
			'<option value="05:00">05:00</option>'+
			'<option value="05:30">05:30</option>'+
			'<option value="06:00">06:00</option>'+
			'<option value="06:30">06:30</option>'+
			'<option value="07:00">07:00</option>'+
			'<option value="07:30">07:30</option>'+
			'<option value="08:00">08:00</option>'+
			'<option value="08:30">08:30</option>'+
			'<option value="09:00">09:00</option>'+
			'<option value="09:30">09:30</option>'+
			'<option value="10:00">10:00</option>'+
			'<option value="10:30">10:30</option>'+
			'<option value="11:00">11:00</option>'+
			'<option value="11:30">11:30</option>'+
			'<option value="12:00">12:00</option>'+
			'<option value="12:30">12:30</option>'+
			'<option value="13:00">13:00</option>'+
			'<option value="13:30">13:30</option>'+
			'<option value="14:00">14:00</option>'+
			'<option value="14:30">14:30</option>'+
			'<option value="15:00">15:00</option>'+
			'<option value="15:30">15:30</option>'+
			'<option value="16:00">16:00</option>'+
			'<option value="16:30">16:30</option>'+
			'<option value="17:00"  selected="selected">17:00</option>'+
			'<option value="17:30">17:30</option>'+
			'<option value="18:00">18:00</option>'+
			'<option value="18:30">18:30</option>'+
			'<option value="19:00">19:00</option>'+
			'<option value="19:30">19:30</option>'+
			'<option value="20:00">20:00</option>'+
			'<option value="20:30">20:30</option>'+
			'<option value="21:00">21:00</option>'+
			'<option value="21:30">21:30</option>'+
			'<option value="22:00">22:00</option>'+
			'<option value="22:30">22:30</option>'+
			'<option value="23:00">23:00</option>'+
			'<option value="23:30">23:30</option>'+
		    '</select></div>'+
		    '</div><div class="form-group">'+
		    '<label class="col-md-2 control-label" id="setClass">班次时间段2</label>'+
		    '<div class="col-md-4">'+
		    '<select id="starttime2" class="form-control" disabled="disabled">'+
		    '<option selected="selected" value="00:00">00:00</option>'+
		    '<option value="00:30">00:30</option>'+
			'<option value="01:00">01:00</option>'+
			'<option value="01:30">01:30</option>'+
			'<option value="02:00">02:00</option>'+
			'<option value="02:30">02:30</option>'+
			'<option value="03:00">03:00</option>'+
			'<option value="03:30">03:30</option>'+
			'<option value="04:00">04:00</option>'+
			'<option value="04:30">04:30</option>'+
			'<option value="05:00">05:00</option>'+
			'<option value="05:30">05:30</option>'+
			'<option value="06:00">06:00</option>'+
			'<option value="06:30">06:30</option>'+
			'<option value="07:00">07:00</option>'+
			'<option value="07:30">07:30</option>'+
			'<option value="08:00">08:00</option>'+
			'<option value="08:30">08:30</option>'+
			'<option value="09:00">09:00</option>'+
			'<option value="09:30">09:30</option>'+
			'<option value="10:00">10:00</option>'+
			'<option value="10:30">10:30</option>'+
			'<option value="11:00">11:00</option>'+
			'<option value="11:30">11:30</option>'+
			'<option value="12:00">12:00</option>'+
			'<option value="12:30">12:30</option>'+
			'<option value="13:00">13:00</option>'+
			'<option value="13:30">13:30</option>'+
			'<option value="14:00">14:00</option>'+
			'<option value="14:30">14:30</option>'+
			'<option value="15:00">15:00</option>'+
			'<option value="15:30">15:30</option>'+
			'<option value="16:00">16:00</option>'+
			'<option value="16:30">16:30</option>'+
			'<option value="17:00">17:00</option>'+
			'<option value="17:30">17:30</option>'+
			'<option value="18:00">18:00</option>'+
			'<option value="18:30">18:30</option>'+
			'<option value="19:00">19:00</option>'+
			'<option value="19:30">19:30</option>'+
			'<option value="20:00">20:00</option>'+
			'<option value="20:30">20:30</option>'+
			'<option value="21:00">21:00</option>'+
			'<option value="21:30">21:30</option>'+
			'<option value="22:00">22:00</option>'+
			'<option value="22:30">22:30</option>'+
			'<option value="23:00">23:00</option>'+
			'<option value="23:30">23:30</option>'+
			'</select></div><label class="col-md-2 control-label">至</label><div class="col-md-4">'+
		    '<select id="endtime2" class="form-control" disabled="disabled">'+
		    '<option selected="selected" value="00:00">00:00</option>'+
		    '<option value="00:30">00:30</option>'+
			'<option value="01:00">01:00</option>'+
			'<option value="01:30">01:30</option>'+
			'<option value="02:00">02:00</option>'+
			'<option value="02:30">02:30</option>'+
			'<option value="03:00">03:00</option>'+
			'<option value="03:30">03:30</option>'+
			'<option value="04:00">04:00</option>'+
			'<option value="04:30">04:30</option>'+
			'<option value="05:00">05:00</option>'+
			'<option value="05:30">05:30</option>'+
			'<option value="06:00">06:00</option>'+
			'<option value="06:30">06:30</option>'+
			'<option value="07:00">07:00</option>'+
			'<option value="07:30">07:30</option>'+
			'<option value="08:00">08:00</option>'+
			'<option value="08:30">08:30</option>'+
			'<option value="09:00">09:00</option>'+
			'<option value="09:30">09:30</option>'+
			'<option value="10:00">10:00</option>'+
			'<option value="10:30">10:30</option>'+
			'<option value="11:00">11:00</option>'+
			'<option value="11:30">11:30</option>'+
			'<option value="12:00">12:00</option>'+
			'<option value="12:30">12:30</option>'+
			'<option value="13:00">13:00</option>'+
			'<option value="13:30">13:30</option>'+
			'<option value="14:00">14:00</option>'+
			'<option value="14:30">14:30</option>'+
			'<option value="15:00">15:00</option>'+
			'<option value="15:30">15:30</option>'+
			'<option value="16:00">16:00</option>'+
			'<option value="16:30">16:30</option>'+
			'<option value="17:00">17:00</option>'+
			'<option value="17:30">17:30</option>'+
			'<option value="18:00">18:00</option>'+
			'<option value="18:30">18:30</option>'+
			'<option value="19:00">19:00</option>'+
			'<option value="19:30">19:30</option>'+
			'<option value="20:00">20:00</option>'+
			'<option value="20:30">20:30</option>'+
			'<option value="21:00">21:00</option>'+
			'<option value="21:30">21:30</option>'+
			'<option value="22:00">22:00</option>'+
			'<option value="22:30">22:30</option>'+
			'<option value="23:00">23:00</option>'+
			'<option value="23:30">23:30</option>'+
		    '</select></div></div><div class="form-group">'+
		    '<label class="col-md-2 control-label required">总时间（小时）</label>'+
		    '<div class="col-md-4"><input id="totalTime" name="totalTime" type="text" class="form-control"  placeholder="小时数"></div>'+
		    '</div><div class="form-group">'+
		    '<label class="col-md-2 control-label required">班次类型</label>'+
		    '<div class="col-md-10">'+
            '<label class="radio-inline"> <input type="radio" name="shiftType" value="1" checked=""> 工作</label>'+
            '<label class="radio-inline"> <input type="radio" name="shiftType" value="2"> 非工作</label>'+
            '<label class="radio-inline"> <input type="radio" name="shiftType" value="3"> 其他</label></div>'+
		    '</div></div>';	
			var d = dialog({
			    title: '新增排班',
			    content: content,
			    lock:false,
			    background:'#fafafa',
			    okValue: '确定',
			    ok: function () {
			    
			    	var shiftName = $("#shiftName").val();
			    	if(shiftName == ""){
			    		alert("请输入班次名称");
			    		return false;
			    	}
			    	
			    	
			    	
			    	var starttime1 = $("#starttime1").val();
			    	var endtime1 = $("#endtime1").val();
			    	
			    	var starttime2 = $("#starttime2").val();
			    	var endtime2 = $("#endtime2").val();
			    	var isCheck = $('#twice').is(':checked');
			    	
			    	if(starttime1 == endtime1){
			    		alert("【班次1】开始时间与结束时间相同，请确认");
			    		return false;
			    	}
			    	
			    	if(isCheck){
			    		if(starttime2 == endtime2){
			    			alert("【班次2】开始时间与结束时间相同，请确认");
			    			return false;
			    		}
			    	}
			    	
			    	var totalTime = $("#totalTime").val();
			    	if(totalTime == ""){
			    		alert("请输入班次总时间");
			    		return false;
			    	}
			    	
			    	var shiftType = $("input[name='shiftType'][type='radio']:checked").val();
			    	
					//增加排班
					$.ajax({
					   type: "POST",
					   url: "scheduleShift/save.do",
					   data: {"shiftName":shiftName,"totalTime":totalTime,"shiftType":shiftType,"starttime1":starttime1,"endtime1":endtime1,"starttime2":starttime2,"endtime2":endtime2,"isCheck":isCheck},
					   success: function(data){
							$("#currentPage").val(1);
							$("#scheduleShiftListForm").attr("action","scheduleShift/updateList.do").submit();
					   }
					})			
					
			        return true;
			        
			    },
			    cancelValue: '取消',
			    cancel: function () {}
			});
			d.width(800).showModal();
			
			workselect();
			return false;
		});

$("#deleteScheduleShift").click(function(){
	var IDS="";
	var ids="";
	$("table.table tbody tr").each(function() {
    if($(this).children().eq(0).find("input").attr('checked')=='checked'){
		var checkToId = $(this).children().eq(0).find("input").attr("id");
		var id =  checkToId.replace('check','id');
		ids = $("#" + id).val();
		IDS += ids +'&';
	}
    });
    
    if(IDS.length!==0){
    	$.ajax({
	    	type:"post",
			dataType:"text",
			url:"scheduleShift/delete.do",
			async:true,//同步
			data:{"ids":IDS},
			success:function(dataMap){
				if(dataMap=="false"){
					alertMessage("删除失败，您选择的记录中有已使用的班次！");
				}else{
					$("#currentPage").val(1);
					$("#scheduleShiftListForm").attr("action","scheduleShift/updateList.do").submit();
				}
			},
			fail:function(e){
				alert(e);
			}
		})			
    }else{
    	alertMessage("请选择一条记录后删除！");
		return;
    }

});

function workselect(){
	$("input[name='twice']").click(function(){
		if($(this).attr('checked')) {
			$("#starttime2").removeAttr("disabled");
			$("#endtime2").removeAttr("disabled");
			$("#setClass").addClass("required");
		}
		else{
			$("#starttime2").attr("disabled","disabled");
			$("#endtime2").attr("disabled","disabled");
			$("#setClass").removeClass("required");
		}
	
	})
}

function changeType(rgb){
	if(!$.browser.msie){ 
	rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/); 
	function hex(x) { 
	return ("0" + parseInt(x).toString(16)).slice(-2); 
	} 
	rgb= "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]); 
	} 
	return rgb; 
} 

