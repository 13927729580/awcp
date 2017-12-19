/**
 * Created by nsccsz-hc on 14-4-4.
 */
/*
 *参数说明: Img:图片ID;Width:预览宽度;Height:预览高度;ImgType:支持文件类型;Callback:选择文件显示图片后回调方法;
 *使用方法:
 <div>
 <img id="ImgPr" width="120" height="120" /></div>
 <input type="file" id="up" />
 把需要进行预览的IMG标签外 套一个DIV 然后给上传控件ID给予uploadPreview事件
 $("#up").uploadPreview({ Img: "ImgPr", Width: 120, Height: 120, ImgType: ["gif", "jpeg", "jpg", "bmp", "png"], Callback: function () { }});
 */
jQuery.fn.extend({
    uploadPreview: function (opts) {
        var _self = this, _this = $(this);
        var _parent = _this.parents(".uploadPreview");
        opts = jQuery.extend({
                Img: "ImgPr", 
				Width: 100, 
				Height: 100, 
				ImgType: ["gif", "jpeg", "jpg", "bmp", "png","mp4","flv","m4v"], 
				Callback: function () { }
            }
            , opts || {});
        $("#" + opts.Img).css({width:opts.Width,height:opts.Height}).css("margin-left","auto").css("margin-right","auto");        
        _self.getObjectURL = function (file) {
            var url = null;
            if (window.createObjectURL != undefined) {
                url = window.createObjectURL(file);
            }
            else if (window.URL != undefined) {
                url = window.URL.createObjectURL(file);
            }
            else if (window.webkitURL != undefined) {
                url = window.webkitURL.createObjectURL(file);
            }
            return url;
        };
        _this.change(function () {
            if (this.value) {
                if (!RegExp("\.(" + opts.ImgType.join("|") + ")$", "i").test(this.value.toLowerCase())) {
                    dialogAlert("请选择图片或视频，例如:" + opts.ImgType.join("，"));
                    this.value = "";
                    return false;
                }
                var videosType=["mp4","flv","m4v"];
                if (!RegExp("\.(" + videosType.join("|") + ")$", "i").test(this.value.toLowerCase())) {
                	if (/msie/.test(navigator.userAgent.toLowerCase())) {
                        try {
                            $("#" + opts.Img).attr('src', _self.getObjectURL(this.files[0]));
                        }
                        catch (e) {
                            var src = "";
                            var obj = $("#" + opts.Img);
                            var div = obj.parent("div")[0];
                            _self.select();
                            if (top != self) {
                                window.parent.document.body.focus();
                            }
                            else {
                                _self.blur();
                            }
                            src = document.selection.createRange().text;
                            document.selection.empty();
                            obj.hide();
                            obj.parent("div").css({
                                'filter': 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)', 'width': opts.Width + 'px', 'height': opts.Height + 'px'
                            });
                            div.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = src;
                        }
                    }else {
                        $("#" + opts.Img).attr('src', _self.getObjectURL(this.files[0]));
                    }
                }else{
                	$("#" + opts.Img).attr('src',basePath+"images/video_icon.png" );
                }
                
                opts.Callback();
                uploadFile(this);
            }
        });
    }
});
var loadUploadPreview = function(img,photo,callback){//img:img标签ID;photo:input标签ID;callback:调用上传方法。
	$("#"+img).attr("alt","选择图片或视频");
	var _parent = $("#"+photo).parents(".uploadPreview");
	$("#"+photo).uploadPreview({ Img: img, Width: 120, Height: 120 });
	_parent.find(".photo-btn a.submit").remove();
//	_parent.find(".photo-btn a.submit").click(function(){
//		var fileVal = _parent.find("#"+photo).val();
//		if(fileVal == ""||fileVal == undefined){
//			dialogAlert("请先上传图片或视频.");
//		}else{
//			uploadFile(_parent.find("#"+photo)[0]);
//		}
//	});	
	
	_parent.find(".photo-btn a.delete").click(function(){
		var photoVal = _parent.find(".photo").val();
		Comm.getData("api/common/file/remove",{"_method":"get","ids":photoVal});
		_parent.find(".photo").val("");
		_parent.find(".photo-con").remove();
		_parent.find(".msg").text("");
		$("#"+photo).remove();
		_parent.prepend('<div class="photo-con"><img id="'+img+'" alt="选择图片或视频"/></div><input type="file" name="'+photo+'" id="'+photo+'"/>');
		$("#"+photo).uploadPreview({ Img: img, Width: 120, Height: 120 });
	});
 	$("body").on("click","#"+img,function(){
		$("#"+photo).click();
	});
}
function uploadFile(that){
	var $parent=$(that).parent(".uploadPreview");
	var $msg=$parent.find(".msg");
	$msg.text("正在上传...");
	$.ajaxFileUpload({  
		url:basePath+ "common/file/upload.do?uploadType="+$("#uploadImgType").val(),            //需要链接到服务器地址  
		secureuri:true,  
		fileElementId:$(that).attr("id"),                        //文件选择框的id属性  
		dataType :"json",
		success: function(data){      				
			if(data.status==0){
				$parent.find(".photo").val(data.data);
				$msg.text("上传成功");
			}else{
				$msg.text(data.message);
			}
		} 
	}); 
}