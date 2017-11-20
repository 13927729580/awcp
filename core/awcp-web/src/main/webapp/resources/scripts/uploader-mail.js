/**
 * 文件上传
 */
(function ($) {
    var $ = jQuery,
        state = 'pending',
        BASE_URL = 'resources/plugins/webuploader-0.1.5/dist';

    var fileAjax = function (method, data, callback){
        $.ajax({
            url: basePath + 'common/file/' + method + '.do',
            data: data,
            async: false,
            success: function (result) {
                callback(result);
            }
        });
    };
    var toMb = function (n) {
        return (n / (1024 * 1024)).toFixed(2)
    };
    var keyOption = {};
    var initUploader = function (option, uploader) {
        var itemOption = {
            //自动上传
            auto: option.auto,
            // 不压缩image
            compress: false,
            // swf文件路径
            swf: basePath + BASE_URL + '/Uploader.swf',
            // 文件接收服务端。
            server: basePath + 'common/file/upload.do',
            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: option.$pick,
            //验证文件总数量
            fileNumLimit: option.fileNumLimit - option.fileNum,
            //验证文件总大小是否超出限制, 超出则不允许加入队列
            fileSizeLimit: option.fileSizeLimit - option.fileSize,
            //验证单个文件大小
            fileSingleSizeLimit: option.fileSingleSizeLimit,
            //上传类型限制
            accept: {
                title: 'Images',
                extensions: option.extensions,//'gif,jpg,jpeg,bmp,png',
                mimeTypes: '*/*'
            }
        };
        //uploader = WebUploader.create(itemOption);
        uploader = option.uploader(itemOption);
        option.item = uploader;
        uploader.on('beforeFileQueued', function (file) {
            if (itemOption.fileNumLimit == 0 || file.size > itemOption.fileSizeLimit) {
                alert("附件超出上传限制");
                return false;
            }
        });
        // 当有文件添加进来的时候
        uploader.on('fileQueued', function (file) {
            if(!option.auto){
                option.$list.append('<div id="' + file.id + '" class="item clearfix">'
                    + '<h4 class="info">' + file.name + '-' + toMb(file.size) + 'Mb<span class="cancel"><i class="icon-remove"></i></span></h4>'
                    + '<p class="state">等待上传...</p>' + '</div>');
            }else{
                return true;
            }
        }); 
        uploader.on('error', function (handler) {
            if (handler == 'F_DUPLICATE') {
                alert('该文件已存在');
            } else if (handler == 'F_EXCEED_SIZE') {
                alert('单个文件大小不能超过2MB');
            } else if (handler == 'Q_EXCEED_NUM_LIMIT') {
                alert("超出文件个数限制,只能上传" + (option.fileNum) + "个文件");
            } else if (handler == 'Q_EXCEED_SIZE_LIMIT') {
                alert('超出文件大小限制');
            } else if (handler == 'Q_TYPE_DENIED') {
            	if(itemOption.accept.extensions!=''){
                	alert('请上传'+itemOption.accept.extensions+'类型的文件');
                }else{
                	alert("请上传文件");
                }
            }
        });

        // 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id),
                $percent = $li.find('.progress .progress-bar');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<div class="progress progress-striped active">'
                    + '<div class="progress-bar" role="progressbar" style="width: 0%">'
                    + '</div>' + '</div>').appendTo($li).find('.progress-bar');
            }

            $li.find('p.state').text('上传中');

            $percent.css('width', percentage * 100 + '%');
        });
		
        uploader.on('uploadSuccess', function (file, response) {
            $('#' + file.id).find('p.state').css('color', 'green').text('已上传');
            $('#' + file.id).remove();
            fileAjax("get", {'id': response.msg}, function (result) {
                var item = eval("(" + result.msg + ")");
                if(option.auto){
                    var _file = item.filename.split(".");
                    var selectOption = option.$uploader.find('select.type');
                    var selectOptionText = selectOption.find("option[value="+selectOption.val()+"]").text();
                    var fileNames="";
                    for(var j=0;j<_file.length-1;j++){
                    	fileNames+=_file[j]+"."
                    }
                    fileNames=fileNames.substring(0,fileNames.length-1)
                    var tHtml = '<tr id="'+item.id+'" class="item"><td class="hidden formData text-center"><input id="boxs" type="hidden" value="'+item.id+'">' +
                        '<td  class="text-center"><a class="download" href="' + basePath + 'common/file/download.do?fileId=' + item.id + '" data-id="' + item.id + '">' + fileNames + '</a></td>' +
                        '<td  class="text-center">'+_file[_file.length-1]+'</td>';
                        tHtml += option.isEditor?('<td data-id="'+selectOption.val()+'">'+selectOptionText+'</td>'):'';
                        //tHtml +='<td>'+toMb(item.length)+'mb</td><td class="text-center">';
                        //tHtml += option.isEditor?('<a  data-backdrop="static" data-width="1000px" data-toggle="modal" data-height="500px" data-iframe="' + basePath + 'common/file/preview.do?fileId=' + item.id + '" target="_blank" class="view btn btn-mini">查看</a><a href="javascript:;" data-id="'+item.id+'" class="editor btn btn-mini">编辑</a>'):'';
                        //tHtml += '<a href="javascript:;" class="cancel btn btn-mini">删除</a>';
                        // tHtml += '<a href="' + basePath + 'common/file/download.do?fileId=' + item.id + '" class="submit btn btn-mini">下载</a></td></tr>';
                        
                        tHtml +='<td  class="text-center">'+toMb(item.length)+'mb</td></tr>';
                        
                    option.$oldlist.find("table tbody").append(tHtml);
                    initTable();
                    //$("#datatable-uploadListTable").append('<div class="datatable-footer"><table class="table  table-datatable table-bordered table-hover"><tfoot><tr><td colspan="5" class="tips" style="text-align: center; border-left-style: none;"></td></tr></tfoot></table></div>');
                    setInterval(formatUploadTable,100);
                }else{
                    var dHtml = '<div id="' + item.id + '" class="item clearfix">'
                        + '<h4 class="info" data-size="' + item.length + '"><a data-backdrop="static" data-width="1000px" data-height="500px" data-iframe="' + basePath + 'common/file/preview.do?fileId=' + item.id + '" data-id="' + item.id + '" data-toggle="modal" class="download">' + item.filename + "</a>&nbsp;&nbsp;--" + toMb(item.length) + 'Mb<span class="cancel"><i class="icon-remove"></i></span></h4>'
                        + '</div>';
                    option.$oldlist.append(dHtml);
                    initTable();
                    //$("#datatable-uploadListTable").append('<div class="datatable-footer"><table class="table  table-datatable table-bordered table-hover"><tfoot><tr><td colspan="5" class="tips" style="text-align: center; border-left-style: none;"></td></tr></tfoot></table></div>');
                    setInterval(formatUploadTable,100);
                }
                option.fileNum = option.fileNum + 1;
                option.fileSize = option.fileSize + item.length;
            });
            //增加文件至附件列表
            var msg = (option.orgFile == "") ? response.msg : (";" + response.msg);
            option.orgFile = option.orgFile.concat(msg);
            option.$uploader.find(".filenumber").text(option.fileNum);
            option.$uploader.find(".filesize").text(toMb(option.fileSize));
            option.$uploader.find(".orgfile").val(option.orgFile);
        });
		
		$("#iframeModal").modal({backdrop : 'static'});
		
		
        uploader.on('uploadError', function (file) {
            $('#' + file.id).find('p.state').css('color', 'red').text('上传出错');
        });

        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress').fadeOut();
        });

        uploader.on('all', function (type) {
            if (type === 'startUpload') {
                state = 'uploading';
            } else if (type === 'stopUpload') {
                state = 'paused';
            } else if (type === 'uploadFinished') {
                state = 'done';
            }

            if (state === 'uploading') {
                option.$btn.text('暂停上传');
            } else {
                option.$btn.text('开始上传');
            }
        });

        option.$btn.on('click', function () {
            if (state === 'uploading') {
                uploader.stop();
            } else {
                uploader.upload();//文件上传
            }
        });
        option.$list.on('click', 'span', function () {//移除文件
            var file = $(this).parents(".item");
            uploader.removeFile(file.attr("id"), true);
            file.remove();
        });


    };
    //获取已上传文件列表
    var getList = function (option) {
        option.orgFile = option.$uploader.find(".orgfile").val();//获取附件ids
        if (option.orgFile != '') {
            option.fileArray = option.orgFile.split(";");
            option.fileNum = option.fileArray.length;
            var newFileNum = option.fileNum;
            for (var i = 0; i < option.fileNum; i++) {
                fileAjax("get", {'id': option.fileArray[i]}, function (result) {
                    if (result.result == 1) {
                        var file = eval("(" + result.msg + ")");
                        if(option.auto){
                            var _file = file.filename.split(".");
                            var fileNames="";
		                    for(var j=0;j<_file.length-1;j++){
		                    	fileNames+=_file[j]+"."
		                    }
		                    fileNames=fileNames.substring(0,fileNames.length-1)
                            var tHtml = '<tr id="'+file.id+'" class="item"><td class="hidden formData text-center"><input id="boxs" type="hidden" value="'+file.id+'"> </td>' +
                                '<td  class="text-center"><a class="download"  data-backdrop="static" data-width="1000px" data-height="500px"  data-toggle="modal" data-iframe="' + basePath + 'common/file/preview.do?fileId=' + file.id + '" data-id="' + file.id + '">' +fileNames + '</a></td>' +
                                '<td  class="text-center">'+_file[_file.length-1]+'</td>';
                            tHtml += option.isEditor?('<td>'+file.type+'</td>'):'';
                            /*
                            tHtml += '<td  class="text-center">'+toMb(file.length)+'mb</td><td class="text-center">';
                            if(option.isReadonly){
                                tHtml +='<a href="' + basePath + 'common/file/download.do?fileId=' + file.id + '" class="submit btn btn-mini">下载</a>';
                            }else{
                                tHtml += option.isEditor?('<a  data-backdrop="static"  data-toggle="modal" data-width="1000px" data-height="500px" data-iframe="'+basePath+'common/file/preview.do?fileId='+file.id+'" target="_blank" class="view btn btn-mini">查看</a><a href="javascript:;" data-id="'+file.id+'" class="editor btn btn-mini">编辑</a>'):'';
                                tHtml += '<a href="javascript:;" class="cancel btn btn-mini">删除</a>';
                                tHtml += '<a href="' + basePath + 'common/file/download.do?fileId=' + file.id + '" class="submit btn btn-mini">下载</a>';
                            }
                            tHtml += '</td></tr>';
                            */
                             tHtml += '<td  class="text-center">'+toMb(file.length)+'mb</td></tr>';
                            
                            option.$oldlist.find("table tbody").append(tHtml);
                            initTable();
                            setInterval(formatUploadTable,100);
                        }else{
                            var dHtml = '<div id="' + file.id + '" class="item clearfix">';
                                dHtml += '<h4 class="info" data-size="' + file.length + '"><a class="download"  data-backdrop="static" data-toggle="modal" data-width="1000px" data-height="500px" data-iframe="' + basePath + 'common/file/preview.do?fileId=' + file.id + '" data-id="' + file.id + '">' + file.filename + "</a>&nbsp;&nbsp;--" + toMb(file.length) + 'Mb';
                                dHtml += option.isReadonly?'':'<span class="cancel"><i class="icon-remove"></i></span>';
                                dHtml += '<a href="' + basePath + 'common/file/download.do?fileId=' + file.id + '" class="submit btn btn-mini">下载</a>';
                                dHtml += '</h4></div>';
                            option.$oldlist.append(dHtml);
                            initTable();
                            setInterval(formatUploadTable,100);
                        }
                        option.fileSize = option.fileSize + file.length;
                    } else {
                        newFileNum = newFileNum - 1;
                        option.orgFile = option.orgFile.replace(option.fileArray[i], "").replace(/^;*|;$/gi, "");
                        option.$uploader.find(".orgfile").val(option.orgFile);
                        return false;
                    }
                });
            }
            option.fileNum = newFileNum;
        }
        else return;
    };
    //删除已上传文件;n为文件id
    var deleteFile = function (id) {
        fileAjax("delete", {"ids": id}, function (result) {
            //$("#"+id).remove();
            return true;
        });
    };

    var init = function (option, uploader) {//option:uploader参数；uploader:uploader标识符
        if(option.isReadonly){option.$uploader.find(".btns div:last-child").remove();option.$uploader.find(".btns div:first-child").remove();}//如果为只读，移除上传按钮
        getList(option);
        initUploader(option, uploader);
        option.$uploader.find(".tips").html("附件信息：已上传<label class='filenumber'>" + option.fileNum + "</label>/" + option.fileNumLimit + "个，大小<label class='filesize'>" + toMb(option.fileSize) + "</label>/" + toMb(option.fileSizeLimit) + "Mb。");
        return option;
    };

    //document.load;
    var getOption = function (dom, us,ie,ir,fnl, fsl, fssl, ft) {//dom id ,uploaderStyle,fileNumLimit,fileSizeLimit(MB),fileSingleSizeLimit(MB).extensions
        this.uploader = function (n) {
            if(us!="default")$("#"+dom).find(".ctlBtn").remove();
            return WebUploader.create(n);
        };
        this.$uploader = $("#" + dom);
        this.$list = this.$uploader.find('.thelist');
        this.$oldlist = this.$uploader.find('.oldlist');
        this.$btn = this.$uploader.find('.ctlBtn');
        this.$pick = this.$uploader.find('.picker');
        this.auto = (us == "default") ? false : true;
        this.isEditor = ie;
        this.isReadonly = ir;
        this.fileNumLimit = fnl;//最大上传文件数
        this.fileNum = 0;//已上传文件数
        this.fileSizeLimit = fsl * 1024 * 1024;//最大上传文件大小20M
        this.fileSize = 0;//已上传文件总大小
        this.fileSingleSizeLimit = fssl * 1024 * 1024;
        this.fileArray = [];
        this.extensions = ft;//文件类型
        this.orgFile = '';//默认附件ids
    };
    $.fn.extend({
        loadUploader: function (options) {
            var defaults = {
                uploaderStyle: "default",
                isEditor:false,
                isReadonly:false,
                fileNumLimit: 5,
                fileSizeLimit: 10,
                fileSingleSizeLimit: 10,
                extensions: '*'
            }
            var options = $.extend(defaults, options);
            return this.each(function () {
                var o = options;
                var obj = $(this);
                var id = obj.attr("id");
                option = new getOption(obj.attr("id"), o.uploaderStyle,o.isEditor,o.isReadonly,o.fileNumLimit, o.fileSizeLimit, o.fileSingleSizeLimit, o.extensions);
                keyOption[id] = option;
                init(option, id);
            });
        }
    });
    $("body").on("click", ".uploader .oldlist .cancel", function () {//删除上传文件
        var _this = $(this),
            _item =_this.parents(".item"),
            _itemId = _item.attr("id"),//获取当前文件ID
            _option = _this.parents(".uploader"),
            _optionId = _option.attr("id");
        var reloadUploader = function (option) {
            var msgIndex = _item.index();//option.orgFile.split(";").length;
            var msgId = (msgIndex == 0) ? (option.orgFile.split(";").length == 1 ? _itemId : (_itemId + ";")) : (";" + _itemId);//截取附件ids
            option.orgFile = option.orgFile.replace(msgId, "");
            option.$uploader.find(".orgfile").val(option.orgFile);
            option.fileSize = 0;
            option.fileNum = 0;
            option.$list.html("");
            if(option.auto)
                option.$oldlist.find("table tbody").html("");
            else
                option.$oldlist.html("");
        };
        var _thisOption = keyOption[_optionId];
        deleteFile(_itemId);
        reloadUploader(_thisOption);
        _thisOption.item.destroy();
        init(_thisOption, _optionId);
    });
    
  	$("#FileRemove").click(function(){
			var ids = new Array();
           $("table.table-datatable tbody tr").each(function(){
			  if($(this).hasClass("active")){
		            var _itemId = $(this).children('td').eq(1).children().eq(0).val();//$(this).attr("id");//获取当前文件ID
		            var _option = $(this).parents(".uploader");
		            var _optionId = _option.attr("id");
			        var reloadUploader = function (option) {
			            var msgIndex = $(this).index();//option.orgFile.split(";").length;
			            var msgId = (msgIndex == 0) ? (option.orgFile.split(";").length == 1 ? _itemId : (_itemId + ";")) : (";" + _itemId);//截取附件ids
			            option.orgFile = option.orgFile.replace(msgId, "");
			            option.$uploader.find(".orgfile").val(option.orgFile);
			            option.fileSize = 0;
			            option.fileNum = 0;
			            option.$list.html("");
			            if(option.auto)
			                option.$oldlist.find("table tbody").html("");
			            else
			                option.$oldlist.html("");
			        };
			        var _thisOption = keyOption[_optionId];
			        //再次编辑邮件发信的附件只做逻辑删除data_doc(oa_mail_doc.doc_name)
              //oa_mail_info.id(data178),oa_mail_info.mail_state(mail_info_state)
              var mailId=$("input[name='data178']").val();
              var mailState=$("input[name='mail_info_state']").val();
              if(mailId!=""&&mailState!=""&&mailState==1){
                var docs=$("input[name='data_doc']").val();
                //for(var i=0;i<ids.length;i++){
                  docs=docs.replace(_itemId,"");
                  docs=docs.replace(";;",";");
                  if(docs.substr(0,1)==';'){
                    docs=docs.substr(1);  
                  }
                  if(docs.substr(-1)==';'){
                    docs=docs.substring(0, docs.length - 1)
                  }
                //}
                $("input[name='data_doc']").val(docs);
                $(this).remove();
                //alert("删除成功");
             }else{
			        deleteFile(_itemId);
			        reloadUploader(_thisOption);
			        _thisOption.item.destroy();
			        init(_thisOption, _optionId);
			       }
			  }
		});
			
			
	});

})(jQuery)
