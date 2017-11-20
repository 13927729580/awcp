
//审批状态
$(function(){
if($(".approvalStatus").size() > 0){
	$('table.approvalStatus').datatable({});
	$.ajax({
		type:"post",
		url:basePath+"workItem/getWorkItem.do",
		async:false,
		dataType:"json",
		success:function(data){
			var option ='';
			var status ='流转状态('+data[0].workFlowStatus+')';
			$.each(data,function(i, item){
			 option +='<tr><td>'+item.activityName+'</td><td>'+item.participant+'</td><td>'+item.inceptDate+'</td><td>'+item.handleDate+'</td><td>'+item.handleStatus+'</td><td>'+item.handleView+'</td><td>'+item.sendStep+'</td><td>'+item.sender+'</td></tr>';
			});
			$("table.table").find("tbody").append(option);
			$(".workflowstatus").find("li").append(status);
			
		}
	});
}
});