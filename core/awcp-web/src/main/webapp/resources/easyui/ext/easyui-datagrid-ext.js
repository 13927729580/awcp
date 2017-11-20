/**
 * @fileName easyui-datagrid-ext.js
 * @Description easyui datagrid 功能扩展：鼠标经过某列变色效果
 * @author HongWang
 * @date 2017-4-11
 */
(function($){
	$.extend($.fn.treegrid.defaults, {
 	   loader:function(param,success,error){
			var that = $(this);
			var opts = that.treegrid("options");
			if (!opts.url) {
				return false;
			}
			var cache = that.data().treegrid.cache;
			if (!cache) {
				$.ajax({
					type : opts.method,
					url : opts.url,
					data : param,
					dataType : "json",
					success : function (data) {
						var data_0=machineEasyuiData(data);
						that.data().treegrid['cache'] = data_0;
						success(data_0);
					},error : function () {
						error.apply(this, arguments);
					}
				});
			} else {
				success(cache);
			}
		}
    });
	$.extend($.fn.datagrid.defaults, {
 	   loader:function(param,success,error){
			var that = $(this);
			var opts = that.datagrid("options");
			if (!opts.url) {
				return false;
			}
			var cache = that.data().datagrid.cache;
			if (!cache) {
				$.ajax({
					type : opts.method,
					url : opts.url,
					data : param,
					dataType : "json",
					success : function (data) {
						var data_0=machineEasyuiData(data);
						that.data().datagrid['cache'] = data_0;
						success(data_0);
					},error : function () {
						error.apply(this, arguments);
					}
				});
			} else {
				success(cache);
			}
		}
    });
	$.extend($.fn.propertygrid.defaults, {
 	   loader:function(param,success,error){
			var that = $(this);
			var opts = that.propertygrid("options");
			if (!opts.url) {
				return false;
			}
			var cache = that.data().propertygrid.cache;
			if (!cache) {
				$.ajax({
					type : opts.method,
					url : opts.url,
					data : param,
					dataType : "json",
					success : function (data) {
						var data_0=machineEasyuiData(data);
						that.data().propertygrid['cache'] = machineEasyuiData(data_0);
						success(data_0);
					},error : function () {
						error.apply(this, arguments);
					}
				});
			} else {
				success(cache);
			}
		}
    });
    $.extend($.fn.datagrid.defaults.view, {
 	   onAfterRender: function(target){
 	       columnCssChange();
 	   }
    });
	$.extend($.fn.treegrid.defaults.view, {
 	   onAfterRender: function(target){
 	       columnCssChange();
 	   }
    });
	$.extend($.fn.propertygrid.defaults, {
 	   onLoadSuccess: function(target){
 	       columnCssChange();
 	   }
    });
})(jQuery);
function columnCssChange(){
	$(".datagrid-header-row td").mouseover(function() {
	   var field=$(this).attr('field');
	   $(".datagrid-header-row td[field='"+field+"']").addClass("datagrid-header-mouseover");
	   $(".datagrid-body td[field='"+field+"']").addClass("datagrid-body-mouseover");
   });
   $(".datagrid-header-row td").mouseleave(function() {
	   var field=$(this).attr('field');
	   $(".datagrid-header-row td[field='"+field+"']").removeClass("datagrid-header-mouseover");
	   $(".datagrid-body td[field='"+field+"']").removeClass("datagrid-body-mouseover");
   });
}
function machineEasyuiData(data){
	var newData;
	if(data.total!=null&&data.total!=undefined){
		if(data.data!=null&&data.data!=undefined){
			newData={total:data.total,rows:data.data};
		}else{
			newData=data;
		}
	}else if(data.total == null && $.isArray(data.data)){
		newData={total:data.data.length,rows:data.data};
	}else if(data.total == null && $.isArray(data.rows)){
		newData={total:data.rows.length,rows:data.rows};
	}else{
		newData={total:data.length,rows:data};
	}
	return newData;
}