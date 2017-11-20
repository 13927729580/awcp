/**
 * @param currentPage：当前页面
 * @param currentPageSize：每页显示记录条数
 * @returns
 */
function page (currentPage,currentPageSize){
	if(arguments.length == 1){	//点击跳转
		var currentPageSize = $("#pageSizeSelect").val();
		var currentPage = $("#pagers input.form-control").val(); 
		currentPage = parseInt(currentPage);
		if(currentPage==""){
			alertMessage("请输入要跳转的页码");
			return false;
		} else if(currentPage > parseInt($("#pagers #totalpage").text())){
			alertMessage("不能超过最大页数");
			return false;
		} else if(isNaN(currentPage)){
			alertMessage("请正确填写页数");
			return false;
		}
	}
	//当点击上一页,下一页,首页,末页时传入2个参数
	var $createForm ;
	if($("#groupForm").length > 0){
		$createForm = $("#groupForm");
	} else if($("#createForm").length > 0){
		$createForm = $("#createForm");	
	} else if($("#manuList").length > 0){
		$createForm = $("#manuList");
	} else{
		alertMessage("未找到表单");
		return;
	}
	$createForm.find("input[name='currentPage']").val(currentPage);
	$createForm.append('<input type="hidden" name="pageSize" value="'+currentPageSize+'">');
	$createForm.submit();
};

function resetPagePosition(){//分页栏position更改
	var $body = $("body");
	var $page = ($("#pagers").length==0)?$("#line-pagers"):$("#pagers");
	if($page.length==0){
		return false;
	}else{
		$page.removeAttr("style");//清除page定位样式
		var bH=$body.height(),pT=$page.offset().top;
		if(bH>=pT){
			$page.removeClass("navbar-fixed-bottom");
		}else{
			$page.addClass("navbar-fixed-bottom");
		}
	}
}

$(function(){
	var $page = ($("#pagers").length==0)?$("#line-pagers"):$("#pagers");
	var $pagerSelect = $page.find("select");
	$pagerSelect.val($pagerSelect.data("pagesize"));
	$pagerSelect.change(function(){		
		var $createForm ;	
		if($("#groupForm").length > 0){
			$createForm = $("#groupForm");
		} else if($("#createForm").length > 0){
			$createForm = $("#createForm");
		} else if($("#manuList").length > 0){
			$createForm = $("#manuList");
		} else{
			alertMessage("未找到表单");
			return false;
		}	
		$createForm.find("input[name='currentPage']").val("1");	//如果更改每页显示记录数时,将显示页面设置为第一页
		var pageSize = $(this).val();
		$createForm.append('<input type="hidden" name="pageSize" value="'+pageSize+'">');
		$createForm.submit();
	});
});

$(window).bind('scroll resize load',function(){
	resetPagePosition();
});
