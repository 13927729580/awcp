function page (currentPage,currentPageSize){
	if(arguments.length == 1){
		var currentPage = $(".C_pager input.form-control").val(); 
		currentPage = parseInt(currentPage);
		if(currentPage==""){
			alertMessage("请输入要跳转的页码");return false;
		}
		else if(currentPage > parseInt($(".CNum span").text())){
			alertMessage("不能超过最大页数");return false;
		}else if(isNaN(currentPage)){
			alertMessage("请正确填写页数");return false;
		}
	}
	$("input[name='currentPage']").val(currentPage);
	var $createForm ;
	
	if($("#groupForm").length>0){
		$createForm=$("#groupForm");
	}else if($("#createForm").length>0){
		$createForm=$("#createForm");
		
	}else if($("#manuList").length>0){
		$createForm=$("#manuList");
	}else{
		alert("未找到表单");
		return;
	}
	$createForm.append('<input type="hidden" name="pageSize" value="'+currentPageSize+'">');
	$createForm.submit();
};
function alertMessage(message){
	var md = dialog({
	    content: message
	});
	md.show();
	setTimeout(function () {
	    md.close().remove();
	}, 2000);
}