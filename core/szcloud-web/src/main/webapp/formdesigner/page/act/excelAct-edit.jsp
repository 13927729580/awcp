<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>页面动作编辑页面</title>
<%@ include file="/resources/include/common_css.jsp"%>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/styles/uploader.css">
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
				<li><a href="#">首页</a></li>
				<li><a href="#">我的应用产品</a></li>
				<li class="active">页面动作编辑</li>
			</ul>
		</div>
		<div class="row" id="dataform">
			<form class="form-horizontal" id="actForm"
				action="<%=basePath%>fd/act/save.do" method="post">
				<c:if test="${result!=null&&''!=result}">
					<span style="color: red">(${result})</span>
				</c:if>
				<%@ include file="comms/basicInfo.jsp"%>
				<div class="form-group ">
					<label class="col-md-1 control-label">动作类型</label>
					<div class='col-md-5'>
						<select class=" form-control" name="actType" id="actType"
							disabled="disabled">
							<option value="2016" selected="selected">Excel导出</option>
						</select>
					</div>
					
					
				</div>
				<div class="form-group ">
					<label class="col-md-1 control-label">参数脚本</label>
					<div class='col-md-5'>
						<textArea class=" form-control"  rows='4' name="extbute[script]" id="target">
							${act.extbute['script']}
						</textArea>
					</div>
				</div>
				<div class="form-group ">
					<label class="col-md-1 control-label">sql脚本</label>
					<div class='col-md-5'>
						<textArea class=" form-control"  rows='4' name="extbute[sqlScript]" id="sqlScript">
							${act.extbute['sqlScript']}
						</textArea>
					</div>
				<div class="col-md-2"><label class="control-label">excel模板上传（如果要自定义模板）</label></div>
				<div class="col-md-4">
					<div id="uploader" class="uploader" ><!-- data-orgfile="d3e5dc4d-7f50-4bab-a587-ce242100a953;6f91c202-7c60-47fe-b6d0-9135fab54dee;d98bb0bf-8584-490c-83c0-aa0c1dc26e78;bd7cc78c-670c-4f5f-8b1e-8de92fa2dd9d" -->
						<input type="hidden" name="extbute[templateFileId]" class="orgfile" value="${act.extbute['templateFileId']}"/>
						<div class="uploader-list oldlist"></div>					
						<hr>
						<!--用来存放文件信息-->
					    <div class="uploader-list thelist"></div>				    
					    <div class="btns">
					        <div class="picker">选择文件</div>
					        <a href="javascript:;" class="btn btn-default ctlBtn">开始上传</a>
					    </div>
					    <div class="tips"></div>
					</div>
				</div>				
				</div>
				<div class="form-group ">
					
				</div>
				<%@ include file="comms/scripts.jsp"%>
				<%@ include file="comms/confirm.jsp"%>
				<div class="form-group">
					<!-- 表单提交按钮区域 -->
					<div class="col-md-offset-2 col-md-10">
						<button type="button" class="btn btn-success" id="saveBtn">
							<i class="icon-save"></i>保存
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<%@ include file="/resources/include/common_form_js.jsp"%>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/map.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript"
		src="<%=basePath%>formdesigner/page/script/act.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/webuploader-0.1.5/dist/webuploader.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/uploader.js"></script><!-- 上传控件实现方法 -->
	<script type="text/javascript">
		
		$(document)
				.ready(
						function() {
						var $btnSet = $("#js-btn-set"),
          			$btnSetIcon = $btnSet.find("#btnIcon"),
          			$btnSetColor = $btnSet.find("#btnColor");
          		$btnSet.find(".icon-menu a").click(function(){
          			var icon = $(this).attr("class");
          			$btnSetIcon.find("i").attr("class",icon);
          			$btnSetIcon.find("input").val(icon);
          		});
				$btnSet.find(".color-menu span").click(function(){
					var common = "label label-dot";
				    var color = $(this).data("color");
				    $btnSetColor.find("span").attr("class",common+" label-"+color);
          			$btnSetColor.find("input").val(color);
          		});
							/* $.formValidator.initConfig({formID:"actForm",debug:false,onSuccess:function(){
								$("#actForm").submit();
								return false;
							   },onError:function(){alert("请按提示正确填写内容");}}); */
							//$("#buttonGroup").formValidator({empty:true,onShow:"输入所属按钮组",onFocus:"只能输入1-99之间的数字哦",onCorrect:"正确"}).inputValidator({min:0,max:2,onErrorMin:"你输入的值必须大于等于1",onError:"按钮组在1-99之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
							//$("#order").formValidator({empty:true,onShow:"请输入按钮顺序",onFocus:"请输入整数",onCorrect:"合法"}).inputValidator({min:0,max:2,onErrorMin:"你输入的值必须大于等于1",onError:"按钮组在1-99之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
							//$("#name").formValidator({onShow:"请输入动作名称",onFocus:"2-8个字符",onCorrect:"合法"}).inputValidator({min:2,max:10,onErrorMax:"不能超过10个字符",onErrorMin:"至少2个字符",empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不合法，请确认"});
							//$("#description").formValidator({onShow:"请输入描述信息",onFocus:"请输入描述信息",onCorrect:"合法"}).inputValidator({min:1,max:225,onError:"输入错误"});
							// $("#height").formValidator({empty:true,onFocus:"请输入整数",onCorrect:"合法"}).inputValidator({min:0,max:3,onErrorMin:"你输入的值必须大于等于0",onError:"高度在0-999之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"});
							// $("#width").formValidator({empty:true,onFocus:"请输入整数",onCorrect:"合法"}).inputValidator({min:0,max:3,onErrorMin:"你输入的值必须大于等于0",onError:"宽度在0-999之间，请确认"}).regexValidator({regExp:"^[0-9]*[1-9][0-9]*$",onError:"输入格式不正确"}); 
							try {
								var dialog = top.dialog.get(window);
							} catch (e) {
								return;
							}
							var radio = $(":radio[name='pattern']:checked")
									.val();
							if (empty(radio)) {
								$(":radio[name='pattern']:first").prop(
										"checked", "checked");
							}
							if(dialog){
								$("#dynamicPageId").attr("disabled","disabled");
							}
							
							$("#saveBtn")
									.click(
											function() {
												$("#actType").removeAttr("disabled");
												$("#actClientScript").val("actNewRun("+$("#target").val()+");\n");
												if (dialog) {
													$("#dynamicPageId").removeAttr("disabled");
													var data = $("#actForm")
															.serializeJSON();
													data.dynamicPageName=$("#dynamicPageId option:selected").text();
													var buttons = new Array();
													$(
															":checkbox[name='buttons']:checked")
															.each(
																	function() {
																		buttons
																				.push($(
																						this)
																						.val());
																	});
													data['buttons'] = buttons
															.join(",");
													$
															.ajax({
																type : "POST",
																async : false,
																url : "fd/act/saveByAjax.do",
																data : data,
																success : function(
																		ret) {
																	var json = eval(ret);
																	dialog
																			.close(json);
																	dialog
																			.remove();
																},
																error : function(
																		XMLHttpRequest,
																		textStatus,
																		errorThrown) {
																	alert(errorThrown);
																}
															});
												} else {
													var name=$("#dynamicPageId option:selected").text();
													$("#actForm").append("<input type='hidden' name='dynamicPageName' value='"+name+"'/>");
													$("#actForm").submit();
												}
												return false;
											});
											$("#uploader").loadUploader();
						});
	</script>
	<script>	
		
		/**
 * 文件上传
 */
 var basePath = "<%=basePath%>";
(function ($) {
    var $ = jQuery,
        state = 'pending',
        BASE_URL = 'resources/plugins/webuploader-0.1.5/dist';

    var fileAjax = function (method, data, callback) {
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
            server: basePath + 'common/file/uploadDirect.do',
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
        // 上传错误提示
        uploader.on('error', function (handler) {
            if (handler == 'F_DUPLICATE') {
                alert('该文件已存在');
            } else if (handler == 'F_EXCEED_SIZE') {
                alert('单个文件大小不能超过2MB');
            } else if (handler == 'Q_EXCEED_NUM_LIMIT') {
                alert("超出文件个数限制,只能上传" + (option.fileNumLimit - option.fileNum) + "个文件");
            } else if (handler == 'Q_EXCEED_SIZE_LIMIT') {
                alert('超出文件大小限制');
            } else if (handler == 'Q_TYPE_DENIED') {
                alert('不支持该文件类型');
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
                    var tHtml = '<tr id="'+item.id+'" class="item"><td class="text-center">'+(option.fileNum+1)+'</td>' +
                        '<td><a class="download" href="' + basePath + 'common/file/download.do?fileId=' + item.id + '" data-id="' + item.id + '">' + _file[0] + '</a></td>' +
                        '<td>'+_file[1]+'</td>';
                        tHtml += option.isEditor?('<td data-id="'+selectOption.val()+'">'+selectOptionText+'</td>'):'';
                        tHtml +='<td>'+toMb(item.length)+'mb</td><td class="text-center">';
                        tHtml += option.isEditor?('<a href="' + basePath + 'common/file/preview.do?fileId=' + item.id + '" target="_blank" class="view btn btn-mini">查看</a><a href="javascript:;" data-id="'+item.id+'" class="editor btn btn-mini">编辑</a>'):'';
                        tHtml += '<a href="javascript:;" class="cancel btn btn-mini">删除</a></td></tr>';
                    option.$oldlist.find("table tbody").append(tHtml);
                }else{
                    var dHtml = '<div id="' + item.id + '" class="item clearfix">'
                        + '<h4 class="info" data-size="' + item.length + '"><a class="download" href="' + basePath + 'common/file/download.do?fileId=' + item.id + '" data-id="' + item.id + '">' + item.filename + "</a>&nbsp;&nbsp;--" + toMb(item.length) + 'Mb<span class="cancel"><i class="icon-remove"></i></span></h4>'
                        + '</div>';
                    option.$oldlist.append(dHtml);
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
                            var tHtml = '<tr id="'+file.id+'" class="item"><td class="text-center">'+(i+1)+'</td>' +
                                '<td><a class="download" href="' + basePath + 'common/file/download.do?fileId=' + file.id + '" data-id="' + file.id + '">' + _file[0] + '</a></td>' +
                                '<td>'+_file[1]+'</td>';
                            tHtml += option.isEditor?('<td>'+file.type+'</td>'):'';
                            tHtml += '<td>'+toMb(file.length)+'mb</td><td class="text-center">';
                            if(option.isReadonly){
                                tHtml +="无";
                            }else{
                                tHtml += option.isEditor?('<a href="'+basePath+'common/file/preview.do?fileId='+file.id+'" target="_blank" class="view btn btn-mini">查看</a><a href="javascript:;" data-id="'+file.id+'" class="editor btn btn-mini">编辑</a>'):'';
                                tHtml += '<a href="javascript:;" class="cancel btn btn-mini">删除</a>';
                            }
                            tHtml += '</td></tr>';
                            option.$oldlist.find("table tbody").append(tHtml);
                        }else{
                            var dHtml = '<div id="' + file.id + '" class="item clearfix">';
                                dHtml += '<h4 class="info" data-size="' + file.length + '"><a class="download" href="' + basePath + 'common/file/download.do?fileId=' + file.id + '" data-id="' + file.id + '">' + file.filename + "</a>&nbsp;&nbsp;--" + toMb(file.length) + 'Mb';
                                dHtml += option.isReadonly?'':'<span class="cancel"><i class="icon-remove"></i></span>'
                                dHtml += '</h4></div>';
                            option.$oldlist.append(dHtml);
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
        if(option.isReadonly){option.$uploader.find(".btns").remove();}//如果为只读，移除上传按钮
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
                fileNumLimit: 1,
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
})(jQuery)
		
	
	</script>
</body>
</html>
