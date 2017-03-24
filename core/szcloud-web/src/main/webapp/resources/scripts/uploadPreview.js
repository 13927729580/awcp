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
				ImgType: ["gif", "jpeg", "jpg", "bmp", "png"], 
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
                    dialogAlert("请选择图片，例如:" + opts.ImgType.join("，"));
                    this.value = "";
                    return false;
                }
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
                }
                else {
                    $("#" + opts.Img).attr('src', _self.getObjectURL(this.files[0]));
                }
                opts.Callback();
            }
        });
    }
});
var loadUploadPreview = function(img,photo,callback){//img:img标签ID;photo:input标签ID;callback:调用上传方法。
	var _parent = $("#"+photo).parents(".uploadPreview");
	$("#"+photo).uploadPreview({ Img: img, Width: 120, Height: 120 });
	_parent.find(".photo-btn a.submit").click(function(){
		var fileVal = _parent.find("#"+photo).val();
		if(fileVal == ""||fileVal == undefined){
			dialogAlert("请先上传图片.");
		}else{
			//callback(fileVal);
				var formData = new FormData();
				var name = $("input").val();
				formData.append("file",$("#"+photo)[0].files[0]);
				formData.append("fileVal",fileVal );
				$.ajax({
				type:'post',
				url:basePath+ "common/file/upload.do",            //需要链接到服务器地址  
				data:formData, 
				// 告诉jQuery不要去处理发送的数据
				processData : false, 
				// 告诉jQuery不要去设置Content-Type请求头
				contentType : false,
				dataType :"json",
				success:function(data,status){
					console.log(data,status)
					if(data.flag==1){
						_parent.find(".photo").val(data.msg);
						dialogAlert("上传成功.");
					}

				},error:function(err){
					console.log(err)
				}
			})
			
			
//			$.ajaxFileUpload({  
//				url:basePath+ "common/file/upload.do",            //需要链接到服务器地址  
//				secureuri:true,  
//				fileElementId:photo,                        //文件选择框的id属性  
//				dataType :"json",
//				success: function(data, status){      				
//					if(data.flag==1){
//						_parent.find(".photo").val(data.msg);
//						dialogAlert("Upload success.");
//					}
//				},error: function (data, status, e){  
//      			//showDialogWithMsg('ideaMsg','Tips','File error！');  
//				}  
//			}); 
		}
	});	
	_parent.find(".photo-btn a.delete").click(function(){
		var photoVal = _parent.find(".photo").val();
		_parent.find(".photo").val("");
		_parent.find(".photo-con").remove();
		$("#"+photo).remove();
		_parent.prepend('<div class="photo-con"><img id="'+img+'" alt="点击选择图片"/></div><input type="file" name="'+photo+'" id="'+photo+'"/>');
		$("#"+photo).uploadPreview({ Img: img, Width: 120, Height: 120 });
	});
 	$("body").on("click","#"+img,function(){
		$("#"+photo).click();
	});
}