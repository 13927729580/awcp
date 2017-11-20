/**
 * Created by nsccsz-hc
 */
(function($){
    $.fn.extend({
        twoWay:function(options){
        	var defaults = {
                    maxHeight:205,
                };
            var options = $.extend(defaults,options);
            return this.each(function(){
                var body = $("body");
                var obj = $(this);
                var leftPanel = obj.find(".twoWay-left");
                var rightPanel = obj.find(".twoWay-right");
                var objId = obj.attr("id");
                //set panel's maxheight;
                obj.find(".panel ul").css({"maxHeight":options.maxHeight,"overflow":"auto"});
                //select the list
                body.on("click","#"+objId+" li",function(){
                	var isOn = $(this).hasClass("on");
                	if(isOn) $(this).removeClass("on");
                	else $(this).addClass("on");
                });
                var input = '<input type="hidden" name="flow" value="default"/>';//默认值default。如果后台接收到的是default值，不需做任何操作。
                obj.find(".twoWay-btns").append(input);
                var setValue = function(){
                	var ids = [];
                	leftPanel.find("li").each(function(){
                		ids.push($(this).data("id"));
                	});                	
                	obj.find(".twoWay-btns input").val(ids);
                }
                //left to right
                obj.find(".btn-forward").click(function(){
                	var items = leftPanel.find(".on");
                	if(items.length != 0){
	                	items.clone().prependTo(".twoWay-right ul");
	                	items.remove();
	                	rightPanel.find("li").removeClass("on");
	                	setValue();
                	}
                });
                //right to left
                obj.find(".btn-backward").click(function(){
                	var items = rightPanel.find(".on");
                	if(items.length != 0){
	                	items.clone().prependTo(".twoWay-left ul");
	                	items.remove();
	                	leftPanel.find("li").removeClass("on");
	                	setValue();
                	}
                });
            });           
        }
    });
})(jQuery);