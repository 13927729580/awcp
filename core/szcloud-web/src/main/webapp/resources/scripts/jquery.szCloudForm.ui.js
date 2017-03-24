// JavaScript Document
$(document).ready(function() {
	$('input[type=text],input[type=password],select,textarea').poshytip({
		className : 'tip-yellowsimple',
		showOn : 'focus',
		showTimeout : 1,
		alignTo : 'target',
		alignX : 'inner-left',
		offsetX : 0,
		offsetY : 5,
		allowTipHover : false
	});
	// zui框架日期选择，模拟多选
	// Chosen
	if ($.fn.chosen)
		$('.chosen-select').chosen();
	if ($.fn.chosenIcons)
		$('#chosenIcons').chosenIcons();

	// datetime picker
	if ($.fn.datetimepicker) {
		$('.form-datetime').datetimepicker({
			weekStart : 1,
			todayBtn : 1,
			autoclose : 1,
			todayHighlight : 1,
			startView : 2,
			forceParse : 0,
			showMeridian : 1,
			format : 'yyyy-mm-dd hh:ii'
		});
		$('.form-date').datetimepicker({
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
		$('.form-time').datetimepicker({
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
	}
});