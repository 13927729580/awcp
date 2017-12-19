/**
 * 文件上传
 */
(function($) {
	//定义初始参数
	var $ = jQuery,
		state = 'pending',
		BASE_URL = 'resources/plugins/webuploader-0.1.5/dist';

	//定义fileAjax函数
	var fileAjax = function(method, data, callback) {
		$.ajax({
			url: basePath + 'common/file/' + method + '.do',
			data: data,
			async: false,
			success: function(result) {
				callback(result);
			}
		});
	};

	//定义将文件大小由b转为Mb函数
	var toMb = function(n) {
		return(n / (1024 * 1024)).toFixed(2)
	};
	
	var uploadType = $("#uploadType").val();
	var uploadServer = uploadType==2?"uploadToFolder.do":"upload.do";
	var keyOption = {};
	var initUploader = function(option) {
		var itemOption = {
			auto: option.auto, //自动上传           
			compress: false, // 不压缩image
			swf: basePath + BASE_URL + '/Uploader.swf', 		// swf文件路径          
			server: basePath + 'common/file/' + uploadServer, 	// 文件接收服务端。          
			pick: option.$pick, // 选择文件的按钮。可选。内部根据当前运行是创建，可能是input元素，也可能是flash.           
			fileNumLimit: option.fileNumLimit - option.fileNum, //验证文件总数量            
			fileSizeLimit: option.fileSizeLimit - option.fileSize, //验证文件总大小是否超出限制, 超出则不允许加入队列     
			fileSingleSizeLimit: option.fileSingleSizeLimit, //验证单个文件大小
			//上传类型限制
			accept: {
				title: 'Images',
				extensions: option.extensions, //'gif,jpg,jpeg,bmp,png',
				mimeTypes: '*/*'
			}
		};

		var uploader = option.createUploader(itemOption);	//webuploader生成的对象
		option.item = uploader;

		uploader.on('beforeFileQueued', function(file) {
			if(itemOption.fileNumLimit == 0 || file.size > itemOption.fileSizeLimit) {
				dialogAlert("文件大小超出限制.");
				return false;
			}
		});
		
		//上传前增加额外参数
		uploader.on('uploadBeforeSend', function(object,data,headers) {
			data.uploadFolder=option.$uploader.attr("id");
			data.isIndex=$("#isIndex").val();
			data.uploadType=$("#uploadType").val();
		});
		
		// 当有文件添加进来的时候
		uploader.on('fileQueued', function(file) {
			if(!option.auto) {
				option.$list.append('<div id="' + file.id + '" class="item clearfix">' +
					'<h4 class="info">' + file.name + '-' + toMb(file.size) + 
					'Mb<span class="cancel"><i class="icon-remove"></i></span></h4>' +
					'<p class="state">等待上传...</p>' + '</div>');
			} else {
				return true;
			}
		});

		uploader.on('error', function(handler) {
			if(handler == 'F_DUPLICATE') {
				dialogAlert('文件已经存在.');
			} else if(handler == 'F_EXCEED_SIZE') {
				dialogAlert('单个文件大小不能超过 2MB.');
			} else if(handler == 'Q_EXCEED_NUM_LIMIT') {
				dialogAlert("文件上传数不能超过  " + (option.fileNumLimit) + " 个.");
			} else if(handler == 'Q_EXCEED_SIZE_LIMIT') {
				dialogAlert('文件总大小不能超出 '+toMb(fileSizeLimit)+" MB");
			} else if(handler == 'Q_TYPE_DENIED') {
				if(itemOption.accept.extensions != '') {
					dialogAlert('请上传 ' + itemOption.accept.extensions + ' 等格式文件.');
				} else {
					dialogAlert("请上传文件.");
				}
			}
		});

		// 文件上传过程中创建进度条实时显示。
		uploader.on('uploadProgress', function(file, percentage) {
			var $li = $('#' + file.id),
				$percent = $li.find('.progress .progress-bar');
			// 避免重复创建
			if(!$percent.length) {
				$percent = $('<div class="progress progress-striped active">' +
					'<div class="progress-bar" role="progressbar" style="width: 0%">' +
					'</div>' + '</div>').appendTo($li).find('.progress-bar');
			}
			$li.find('p.state').text('上传中');
			$percent.css('width', percentage * 100 + '%');
		});

		uploader.on('uploadSuccess', function(file, response) {
			$('#' + file.id).find('p.state').css('color', 'green').text('Uploaded');
			$('#' + file.id).remove();
			fileAjax("get", { 'id': response.data }, function(result) {
				if(result.data){
					var item = result.data;
					var tHtml = '<tr id="' + item.id + '" class="item"><td class="formData text-center"><input type="checkbox" name="_selects"/><input id="boxs" type="hidden" value="' + item.id + '">' +
						'<td  class="text-center"><a class="download" data-toggle="modal" data-backdrop="static" data-width="1000px" data-height="500px" data-iframe="' + basePath + 'common/file/preview.do?fileId=' + item.id + '" data-id="' + item.id + '">' + item.fileName + '</a></td>' +
						'<td  class="text-center">' + item.contentType + '</td>';
					tHtml += '<td  class="text-center">' + toMb(item.size) + 'mb</td></tr>';
					option.$oldlist.find("table tbody").append(tHtml);
					option.fileNum = option.fileNum + 1;
					option.fileSize = option.fileSize + item.size;
					option.$oldlist.off("click").on("click","input[type='checkbox']",function(){
						if($(this).is(':checked')){
							$(this).parent().parent().addClass("active");
						}else{
							$(this).parent().parent().removeClass("active");
						}
					})
				}			
			});

			//增加文件至附件列表
			var msg = (option.orgFile == "") ? response.data : (";" + response.data);
			option.orgFile = option.orgFile.concat(msg);
			option.$uploader.find(".filenumber").text(option.fileNum);
			option.$uploader.find(".filesize").text(toMb(option.fileSize));
			option.$uploader.find(".orgfile").val(option.orgFile);
		});

		$("#iframeModal").modal({ backdrop: 'static' });

		uploader.on('uploadError', function(file) {
			$('#' + file.id).find('p.state').css('color', 'red').text('上传失败');
		});

		uploader.on('uploadComplete', function(file) {
			$('#' + file.id).find('.progress').fadeOut();
		});

		uploader.on('all', function(type) {
			if(type === 'startUpload') {
				state = 'uploading';
			} else if(type === 'stopUpload') {
				state = 'paused';
			} else if(type === 'uploadFinished') {
				state = 'done';
			}

			if(state === 'uploading') {
				option.$btn.text('暂停上传');
			} else {
				option.$btn.text('开始上传');
			}
		});

		option.$btn.on('click', function() {
			if(state === 'uploading') {
				uploader.stop();
			} else {
				uploader.upload(); //文件上传
			}
		});

		option.$list.on('click', 'span', function() { //移除文件
			var file = $(this).parents(".item");
			uploader.removeFile(file.attr("id"), true);
			file.remove();
		});
	};

	//获取已上传文件列表
	var getList = function(option) {
		option.orgFile = option.$uploader.find(".orgfile").val(); //获取附件ids
		if(typeof(option.orgFile) == "string" ) {
			option.fileArray = option.orgFile.split(";");
			option.fileNum = option.fileArray.length;
			var newFileNum = option.fileNum;
			for(var i = 0; i < option.fileNum; i++) {
				fileAjax("get", { 'id': option.fileArray[i] }, function(result) {
					var file = result.data;
					if(file !=-1) {
						var tHtml = '<tr id="' + file.id + '" class="item"><td class="formData text-center"><input type="checkbox" name="_selects"/><input id="boxs" type="hidden" value="' + file.id + '"> </td>' +
							'<td  class="text-center"><a class="download"  data-backdrop="static" data-width="1000px" data-height="500px"  data-toggle="modal" data-iframe="' + basePath + 'common/file/preview.do?fileId=' + file.id + '" data-id="' + file.id + '">' + file.fileName + '</a></td>' +
							'<td  class="text-center">' + file.contentType + '</td>';
						/*tHtml += option.isEditor ? ('<td>' + file.contentType + '</td>') : '';*/
						tHtml += '<td  class="text-center">' + toMb(file.size) + 'mb</td></tr>';
						option.$oldlist.find("table tbody").append(tHtml);
						option.$oldlist.off("click").on("click","input[type='checkbox']",function(){
							if($(this).is(':checked')){
								$(this).parent().parent().addClass("active");
							}else{
								$(this).parent().parent().removeClass("active");
							}
						})
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
		} else return;
	};
	
	//删除已上传文件;n为文件id
	var deleteFile = function(id) {
		if(id){
			fileAjax("remove", { "ids": id }, function(result) {
				if(result.status==0){
					return true;
				}else{
					return false;
				}
			});
		}
	};

	var init = function(option) { //option:uploader参数；uploader:uploader标识符
		//如果为只读，移除上传按钮
		if(option.isReadonly) {
			option.$uploader.find(".btns div:last-child").remove();
			option.$uploader.find(".btns div:first-child").remove();
		}
//		option.$uploader.find(".btns div[id='MoreDownload']").remove();
		getList(option);
		initUploader(option);
		option.$uploader.find(".tips").html("提示: <label class='filenumber'>" + option.fileNum + "</label>/" + option.fileNumLimit + " 已上传, 文件大小 <label class='filesize'>" + toMb(option.fileSize) + "</label>/" + toMb(option.fileSizeLimit) + "Mb。");
		return option;
	};

	//document.load;
	//dom id ,uploaderStyle,fileNumLimit,fileSizeLimit(MB),fileSingleSizeLimit(MB).extensions
	var getOption = function(dom, us, ie, ir, fnl, fsl, fssl, ft) { 
		this.createUploader = function(n) {
			if(us != "default") $("#" + dom).find(".ctlBtn").remove();
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
		this.fileNumLimit = fnl; 						//最大上传文件数
		this.fileNum = 0; 								//已上传文件数
		this.fileSizeLimit = fsl * 1024 * 1024; 		//最大上传文件大小
		this.fileSize = 0; 								//已上传文件总大小
		this.fileSingleSizeLimit = fssl * 1024 * 1024;	//单个文件上传大小
		this.fileArray = [];
		this.extensions = ft; //文件类型
		this.orgFile = ''; //默认附件ids
	};

	//页面jquery对象调用该方法初始化上传文件表格 
	$.fn.extend({
		loadUploader: function(options) {
			var defaults = {
				uploaderStyle: "default",
				isEditor: false,
				isReadonly: false,
				fileNumLimit: 5,
				fileSizeLimit: 10,
				fileSingleSizeLimit: 10,
				extensions: '*'
			}
			var options = $.extend(defaults, options);
			return this.each(function() {
				var o = options;
				var $obj = $(this);
				var id = $obj.attr("id");
				option = new getOption(id, o.uploaderStyle, o.isEditor, o.isReadonly,
						o.fileNumLimit, o.fileSizeLimit, o.fileSingleSizeLimit, o.extensions);
				keyOption[id] = option;
				init(option);
			});
		}
	});

	$(".uploader").on("click", ".oldlist .cancel", function() { //删除上传文件
		var _this = $(this),
			_item = _this.parents(".item"),
			_itemId = _item.attr("id"), //获取当前文件ID
			_option = _this.parents(".uploader"),
			_optionId = _option.attr("id");
		var reloadUploader = function(option) {
			var msgIndex = _item.index(); //option.orgFile.split(";").length;
			var msgId = (msgIndex == 0) ? (option.orgFile.split(";").length == 1 ? _itemId : (_itemId + ";")) : (";" + _itemId); //截取附件ids
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
	$(".uploader #MoreDownload").bind("click", function(event) {
		var _option = $(this).parents(".uploader");
		var _optionId = _option.attr("id");
		var _thisOption = keyOption[_optionId];
		var arr = [];
		$(event.target).parent().parent().find(".uploader-list tbody tr.active").each(function() {		
			var _itemId = $(this).children('td').eq(0).children('input[type="hidden"]').eq(0).val(); //获取当前文件ID			
			arr.push(_itemId);
		});	
		if(arr.length == 0){
			dialogAlert("请至少选择一项进行下载");
		}else if(arr.length ==1){
			location.href=basePath+"api/common/file/download?fileId="+arr[0];
		}else{
			location.href=basePath+"api/common/file/batchDownload?fileName=附件"+arr.map(m=>"&fileIds="+m).join('');
		}
		
	})
	$(".uploader #FileRemove").bind("click", function(event) {
		var _option = $(this).parents(".uploader");
		var _optionId = _option.attr("id");
		var _thisOption = keyOption[_optionId];
		var deleteArr = [];
		$(event.target).parent().parent().find(".uploader-list tbody tr.active").each(function() {		
			var _itemId = $(this).children('td').eq(0).children('input[type="hidden"]').eq(0).val(); //获取当前文件ID			
			deleteFile(_itemId);
			deleteArr.push(_itemId);
		});	
		if(deleteArr.length == 0){
			dialogAlert("请至少选择一项进行删除");
			return false;
		} else if(_thisOption.orgFile.split(";").length == deleteArr.length){
			_thisOption.orgFile = "";
		}
		else{
			_thisOption.orgFile = _thisOption.orgFile.replace(";" + deleteArr.join(";"),"").replace(deleteArr.join(";") + ";","");
		}
		_thisOption.$uploader.find(".orgfile").val(_thisOption.orgFile);
		_thisOption.fileSize = 0;
		_thisOption.fileNum = 0;
		_thisOption.$list.html("");
		if(option.auto)
			_thisOption.$oldlist.find("table tbody").html("");
		else
			_thisOption.$oldlist.html("");
		_thisOption.item.destroy();
		init(_thisOption, _optionId);
	});	

})(jQuery)