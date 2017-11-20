var $window=$(window);
var $body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body'); // opera fix
$(function(){
	//scroll to top
	$("<a href='javascript:;' class='toTop' onclick='scrollToTop()'><i class='icon-chevron-up'>&nbsp;</i>返回顶部</a>").appendTo("body");
	//reset layout
	$(".input-group .input-group-addon").each(function(item){
		if(!($(this).prev().length!=0&&$(this).next().length!=0)) $(this).removeClass("fix-border").removeClass("fix-padding");
	});
	//alret message
	$(".alert-dismissable .close").click(function(){
		$(this).parent().remove();
	});
	var $icon = $(".scrollMenu .icon");
	var $con = $(".scrollMenu .con");
	$icon.click(function(){
		if($con.css("display")=="none"){
			$con.show();
			$icon.removeClass("icon-chevron-down").addClass("icon-chevron-up");
		}else{
			$con.hide();
			$icon.removeClass("icon-chevron-up").addClass("icon-chevron-down");
		}
	});
});
function scrollToTop(){
	$body.animate({scrollTop:0},500);
	return false;
};
$window.bind('scroll resize load',function(){
	var scrollTop = $window.scrollTop();
	var $buttons = $("#buttons");
	if(scrollTop>150){		
		$(".toTop").show();
		//fixed button
		$buttons.addClass("navbar-fixed-top fixed-buttons");
		$buttons.find("button").addClass("btn-sm");
	}else if(scrollTop == 0){
		$(".toTop").hide();
		$buttons.removeClass("navbar-fixed-top fixed-buttons");
		$buttons.find("button").removeClass("btn-sm");
	}
});