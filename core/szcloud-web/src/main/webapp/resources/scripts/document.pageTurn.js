function page (currentPage,currentPageSize){
	if(arguments.length == 1){
		var currentPageSize = arguments[0];
		var currentPage = $("#pagers input.form-control").val(); 
		currentPage = parseInt(currentPage);
		if(currentPage==""){
			alertMessage("请输入要跳转的页码");return false;
		}
		else if(currentPage > parseInt($("#pagers #totalpage").text())){
			alertMessage("不能超过最大页数");return false;
		}else if(isNaN(currentPage)){
			alertMessage("请正确填写页数");return false;
		}
	}
	$("input[name='currentPage']").val(currentPage);
	var $createForm = $("#groupForm");
	$createForm.append('<input type="hidden" name="pageSize" value="'+currentPageSize+'">');
	$createForm.submit();
};
function resetPagePosition(){//分页栏position更改
	var $body = $("body");
	var $page = ($("#pagers").length==0)?$("#line-pagers"):$("#pagers");
	if($page.length==0){
		return false;
	}else{
		$page.removeAttr("style");//清楚page定位样式
		var bH=$body.height(),pT=$page.offset().top;
		if(bH>=pT){
			//$page.css("position","static");
			$page.removeClass("navbar-fixed-bottom");
		}else{
			//$page.css("position","fixed");
			$page.addClass("navbar-fixed-bottom");
		}
	}
}
$(function(){
	var $page = ($("#pagers").length==0)?$("#line-pagers"):$("#pagers");
	var $pagerSelect = $page.find("select");
	$pagerSelect.val($pagerSelect.data("pagesize"));
	$pagerSelect.change(function(){
		var pageSize = $(this).val();
		var $createForm = $("#groupForm");
		$createForm.append('<input type="hidden" name="pageSize" value="'+pageSize+'">');
		$createForm.submit();
	});
});
$(window).bind('scroll resize load',function(){
	resetPagePosition();
});
$(window).scroll(function(){
	var $datatable = $("div.datatable");
	var scrollTop = $(window).scrollTop();
	var offsetTop = $datatable.offset().top;
	var top = ($(".datatable-head").height() == null)?40:($(".datatable-head").height()+10);
	if(scrollTop>offsetTop){
		$(".row#buttons").addClass("navbar-fixed-top").css({
			top:top,
			textAlign:'center'
		});
		$(".row#buttons button").addClass("btn-sm");
	}else{
		$(".row#buttons").removeClass("navbar-fixed-top").removeAttr("style");
		$(".row#buttons button").removeClass("btn-sm");
	}
});