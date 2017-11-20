/*
 * common.js <br>常用功能函数<br> author:venson
 * 
 */
// 后台接口前缀
var baseUrl = window.location.protocol + "//" + window.location.host + "/awcp/";
var basePath = baseUrl;
// 前台跳转前缀
var baseUIUrl = window.location.protocol + "//" + window.location.host + "/awcp/";
var Comm = {};


Comm.getUrlParam= function (name,url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
    var r = (url)?url.match(reg):window.location.search.substr(1).match(reg);
    if (r)
        return decodeURIComponent(r[2]);
    else
        return null; // 返回参数值
}

//页面权限ID
var privilegesID = Comm.getUrlParam("id")||22;
Comm.deleteObject = function (url, table) {
	table=table||".table"
    var $table;
    if (typeof (table) == "object") {
        $table = table;
    } else {
        $table = $(table);
    }
    var row = $table.bootstrapTable('getSelections');
    if (row.length <= 0){
    	alert("请勾选要删除的对象！");	
    }        
    else {
    	if(confirm('是否要删除选择的' + row.length + '个对象')){
    		$.each(row, function (i, e) {
                Comm.deleteAjax(url, $table, e.id);
            });
    	}
    }
}

Comm.modifyObject = function (url, table) {
    var obj = Comm.getSelectedRow($(table));
    if (obj)
        location.href = url + "?objId=" + obj.id + "&id=" + privilegesID;
    else
        alert("请点击要修改的行");
}


/**
 * 封装删除ajax函数 <br>
 * param: url 接口地址 <br>
 * param: table bootstrap表格 <br>
 * param: ids 删除的id
 */
Comm.deleteAjax = function (url, table, ids) {
    $.ajax({
        type: "POST",
        url: baseUrl + url,
        data: {
            id: ids,
            "privilegesID": privilegesID
        },
        dataType: "json",
        success: function (data) {
            if (data.status === 0) {
                if (table) {
                    table.bootstrapTable('remove', {
                        field: 'id',
                        values: [ids]
                    });
                }
            } else if(data.status === -4){
                alert("您还未登录,请先登录！");
                location.href=baseUrl+"login.html";
            }else{
            	alert("错误提示：" + data.message);
            }
        },
        error: function (data) {
            alert("连接服务器出错，请检查！");
        }
    });
}

/**
 * 封装删除ajax函数 <br>
 * param: url 接口地址 <br>
 * param: params 参数
 */
Comm.removeAjax = function (url, params,_this) {
    $.ajax({
        type: "POST",
        url: baseUrl + url,
        data: params,
        dataType: "json",
        async:false,
        success: function (data) {
            if (data.status === 0) {
                if(_this){
                	_this.each(function(i){
                        _this.eq(i).parent().remove();
                    })
                }
            } else if(data.status === -4){
                alert("您还未登录,请先登录！");
                location.href=baseUrl+"login.html";
            }else{
                alert("错误提示：" + data.message);
                throw new Error("删除失败");
            }
        },
        error: function (data) {
            alert("连接服务器出错，请检查！");
            throw new Error("删除失败");
        }
    });
}

/**
 * 伪多态保存函数
 */
Comm.saveAjax = function (url, param, handle) {
    if (typeof (arguments[0]) == "object") {
        Comm.saveDataByObject(arguments[0]);
    } else {
        var l = arguments.length;
        var option = {};
        if (l == 2) {
            option.url = arguments[0];
            option.params = arguments[1];
            Comm.saveDataByObject(option);
        } else if (l == 3) {
            option.url = arguments[0];
            option.params = arguments[1];
            option.handle = arguments[2];
            Comm.saveDataByObject(option)
        }
    }
}

/**
 * 封装保存ajax函数 <br>
 * param: option
 * 参数格式{"url":"","async":false,"tip","#tag","params":{},"handle"}
 */
Comm.saveDataByObject = function (option) {
    option.async = (!option.async) ? false : true;
    option.type = (!option.type) ? "POST" : option.type;
    option.tip = (!option.tip) ? '#saveId' : option.tip;
    if (typeof (option.params) == "object") {
        option.params.privilegesID = privilegesID;
    }
    if (!option.success) {
        option.success = function (response) {
            if (response.status == 0) {
                if (option.handle) {
                    if (option.handle == "current") {
                        location.href = location.href;
                    } else if (option.handle == "back") {
                        location.href = document.referrer;
                    } else if (option.handle == "returnValue") {
                        window.primaryKey = response.data;
                    } else if (option.handle == "refresh") {
                        $('.modal').modal('hide');
                        $('.table').bootstrapTable('refresh');
                    }
                }
            } else if(data.status === -4){
                alert("您还未登录,请先登录！");
                location.href=baseUrl+"login.html";
            }else {
                if (response.message)
                    $(option.tip).tips({
                        msg: response.message
                    });
                else
                    $(option.tip).tips({
                        msg: "保存失败，请联系管理员！"
                    });
                throw new Error("保存失败！");
            }
        }
    }
    if(!option.error){
        option.error = function () {
            $(option.tip).tips({
                msg: "网络出错，请检查网络！"
            });
            throw new Error("保存失败！");
        }
    }
    $.ajax({
        type: option.type,
        url: baseUrl + option.url,
        data: option.params,
        async: option.async,
        dataType: "json",
        success: option.success,
        error: option.error
    });
}

/* 根据页面定义的样式自动校验参数 */
Comm.checkError = function (id) {
    id = (id) ? id : "#saveForm";
    var hasError = false;
    if($(id + " :checkbox").length>0){
    	var parent=$(id + " :checkbox").parent();
    	$.each(parent,function(i1,p){
    		if($(this).data('require')){
        		var hasChecked=false;
        		$(this).find(":checkbox").each(function(i,e){
                	if(e.checked){
                		hasChecked=true;
                		return;
                	}
                })
                if(!hasChecked){
                	$(this).find(":checkbox").last().tips({
                        msg:"请至少勾选一个选项！"
                    });
                	throw new error("请至少勾选一个选项!");
                }
        	}
    	});
    }
    $(id + " :input").not(":button").not(":file").each(function (i, e) {
        var $label = $(this).prev("label");
        if ($(this).data('require')) {
            if ($.trim(e.value).length == 0) {
                if (e.lang) {
                    $(this).tips({
                        msg: e.lang + "不能为空！"
                    });
                } else {
                    var text = $.trim($label.text());
                    if (text.length > 0) {
                        text = text.substring(0, text.length - 1);
                        $(this).tips({
                            msg: text + "不能为空！"
                        });
                    } else {
                        $(this).tips({
                            msg: "该值不能为空！"
                        });
                    }
                }
                hasError = true;
            }
        }
    })
    return hasError;
}
    
/* 根据页面存在的表单自动获取参数 */
Comm.getParameters = function (id) {
    id = (id) ? id : "#saveForm";
    var params = {};
    $(id + " :input").not(":button").not(":checkbox").not(":file").each(function (i, e) {
        if (e.value) {
            params[e.name] = e.value
        } else {
            params[e.name] = "";
        }
    })
    return params;
}

Comm.resetForm = function (id) {
    id = (id) ? id : "#saveForm";
    $(id + " :input").not(":button").each(function (i, e) {
        if ($(this).is("input[type='checkbox']")) {
            this.checked = false;
        } else if ($(this).is("select")) {
            $(this).val('').trigger('change');
        }  else {
            if (!$(this).data('filter')) {
                $(this).val('');
            }
        }
    })
}


Comm.setText = function (url, tag, params) {
    params.privilegesID = privilegesID;
    var data = this.getData(url, params);
    $(tag).text(data);
}

/**
 * ajax设置编辑数据 <br>
 * param: url 接口地址 <br>
 * param: objId 对象Id <br>
 * param: filter 过滤的参数
 */
Comm.setData = function (url, objId, id) {
    var data = Comm.getData(url, {
        "id": objId,
        "privilegesID": privilegesID
    });
    Comm.setFormData(data, id);
}

Comm.setFormData = function (data, id) {
    id = (id) ? id : "#saveForm";
    $(id + " :input").not(":button").each(function (i, e) {
        if ($(this).is("input[type='text']") || $(this).is("textarea")) {
            $(this).val(data[e.name])
        } else if ($(this).is("select")) {
            $(this).val(data[e.name]).trigger('change');
        }else if($(this).is("input[type='radio']")){
        	if($(this).val()==data[e.name]){
        		this.checked=true;
        	}else{
        		this.checked=false;
        	}
        	
        }
    })
}

Comm.getData = function (url, params, cache) {
    params = (params) ? params : {};
    cache = (cache) ? cache : false;
    var data;
    if (cache) {
    	var name = this.builderName(url, params);
        // if (!parent.window.cache[name]) {
        if (!this.get(name)) {
            $.ajax({
                type: "POST",
                url: baseUrl + url,
                data: params,
                async: false,
                dataType: "json",
                success: function (d) {
                	if(d.hasOwnProperty("status")){
                		 if (d.status == 0) {
                         	data = d.data;
                             if(typeof(data)=="string"){
                             	try{
                             		data=JSON.parse(data);
                             	}catch (e) {
         							
         						}
                             }
                             // parent.window.cache[name] = data;
                             Comm.set(name, data);
                         } else if(d.status === -4){
                             Comm.alert("您还未登录,请先登录！");
                             if(Comm.isMobile()){
                             	location.href=baseUrl+"dingding/index.html";
                             }else{
                             	location.href=baseUrl+"login.html";
                             }
                         }else {
                        	 Comm.alert(d.message, "error");
                             throw new Error(d.message);
                         }
                	}else{
                		data = d;
                	}
                },
                error: function () {
                	Comm.alert("网络出错，请联系管理员！", "error");
                    throw new Error("网络出错，请联系管理员！");
                }
            });
        } else {
            // data = parent.window.cache[name];
            data = this.get(name);
        }
    } else {
        $.ajax({
            type: "POST",
            url: baseUrl + url,
            data: params,
            async: false,
            dataType: "json",
            success: function (d) {
            	if(d.hasOwnProperty("status")){
            		if (d.status == 0) {
                        data = d.data;
                        if(typeof(data)=="string"){
                        	try{
                        		data=JSON.parse(data);
                        	}catch (e) {
    							
    						}
                        }
                    }else if(d.status === -4){
                    	Comm.alert("您还未登录,请先登录！");
                        if(Comm.isMobile()){
                        	location.href=baseUrl+"dingding/index.html";
                        }else{
                        	location.href=baseUrl+"login.html";
                        }
                    }else {
                    	Comm.alert(d.message, "error");
	                     throw new Error(d.message);
                    }
            	}else{
            		data = d;
            	}                
            },
            error: function () {
            	Comm.alert("网络出错，请联系管理员！", "error");
                throw new Error("网络出错，请联系管理员！");
            }
        });
    }
    return data;
}

Comm.builderName = function (url, params) {
    var name = url.replace(/\//g, "$");
    if (typeof (params) == 'object') {
        var n = [];
        for (var i in params) {
            n.push(params[i]);
        }
        return name + n.join('');
    } else {
        return name + params.replace(/\&/g, "$");
    }
}

/**
 * 下载excel模板 <br>
 * param: templateName 模板名称 <br>
 * param: downloadName 下载文件名称
 */
Comm.downloadTemplate = function (templateName, downloadName) {
    window.location.href = baseUrl + "attachment/excelDownload?fileName=/template/" + templateName + "&downName=" + downloadName;
}

/**
 * 下载excel模板 <br>
 * param: fileName 下载文件名称
 */
Comm.exportExcel = function (fileName) {
    var data = $('.table').bootstrapTable('getData', true);
    this.postByForm('excel/exportExcelByCreate', {
        "json": JSON.stringify(data),
        "cloums": JSON.stringify(pageColumns),
        "fileName": fileName
    })
}

Comm.getNowFormatDate=function(){
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

Comm.postByForm = function (URL, PARAMS,target) {
    target=target||"_blank";
    var temp = document.createElement("form");
    PARAMS.privilegesID = privilegesID;
    temp.method = "post";
    temp.target = target;
    temp.style.display = "none";
    if(typeof(PARAMS)=="object"){
	    for (var x in PARAMS) {
	        var opt = document.createElement("textarea");
	        opt.name = x;
	        opt.value = PARAMS[x];
	        // alert(opt.name)
	        temp.appendChild(opt);
	    }
    }else{
    	URL+="?"+PARAMS;
    }
    temp.action = baseUrl + URL;
    document.body.appendChild(temp);
    temp.submit();
    temp.parentNode.removeChild(temp);
}

Comm.post = function (url, response, params) {
    url = baseUrl + url;
    if (typeof (params) == "object") {
        params = params ? params : {};
        params.privilegesID = privilegesID;
    } else {
        params = params ? params : "";
        params = "privilegesID=" + privilegesID + params;
    }
    $.post(url, params, response, "json");
}

Comm.fileTypeJudge=function(file,str){
	var $file=$(file);
	var flag=true; 
	var rightFileType;  
    var pojo;  
    if (str == "photo") {  
        rightFileType = ["jpg", "png","jpeg"];  
        pojo = "图片";  
    } else if (str == "doc") {  
        rightFileType = ["docx", "doc", "txt"];  
        pojo = "文档";  
    }else if (str == "excel") {  
        rightFileType = ["xls", "xlsx"];  
        pojo = "excel";  
    }else if (str == "word") {  
        rightFileType = ["docx", "doc"];  
        pojo = "word文档";  
    }else {  
        return;  
    }  
    var fileType = $file.val().substring($file.val().lastIndexOf(".") + 1);  
    // 得到needle的类型
    var type = typeof fileType;  
    if(type == 'string' || type =='number') {
        for(var i=0;i<rightFileType.length;i++) {  
            if(rightFileType[i] == fileType) {  
            	return true; 
            }  
        }  
    }  
    alert("请上传"+pojo+"格式文件！");
    $file.val("");
    return false;  
}
  

Comm.uploadFile = function (handle, tag,type) {
    if(type){
    	if (!Comm.fileTypeJudge($(":file").get(0),type))
            return;
    }else{
    	type="";
    }
    var url = "";
    tag=tag||$("#uploadForm");
    if (type == "photo") {
        url = "attachment/uploadImg";
    } else if (type == "excel") {
        url = "attachment/excelUpload";
    } else if (type == "word") {
        url = "attachment/wordUpload";
    } else {
        url = "common/file/upload.do";
    }
    var options = {
        success: handle, // 提交后的回调函数
        url: baseUrl + url, // 默认是form的action，如果申明，则会覆盖
        type: "POST", // 默认值是form的method("GET" or "POST")，如果声明，则会覆盖
        dataType: "json" // html（默认）、xml、script、json接受服务器端返回的类型
    }
    tag.ajaxSubmit(options);
}

Comm.uploadImage = function (handle) {
    var options = {
        success: handle, // 提交后的回调函数
        url: baseUrl + "attachment/uploadImg", // 默认是form的action，如果申明，则会覆盖
        type: "POST", // 默认值是form的method("GET" or "POST")，如果声明，则会覆盖
        dataType: "json", // html（默认）、xml、script、json接受服务器端返回的类型
    }
    $(tag).ajaxSubmit(options);
}

Comm.paramToString = function (obj) {
    var params = [];
    for (var key in obj) {
        params.push("&" + key + "=");
        params.push(obj[key]);
    }
    return params.join('');
}

Comm.uniqueArr = function (arr) {
    arr.sort();
    var re = [arr[0]];
    for (var i = 1; i < arr.length; i++) {
        if (arr[i]&&arr[i] !== re[re.length - 1]) {
            re.push(arr[i]);
        }
    }
    return re;
}

Comm.set = function (key, value) {
	if( typeof(value)=="object" ){
		localStorage.setItem(key, JSON.stringify(value));
	}else{
		localStorage.setItem(key, value);
	}
}
Comm.get = function (key) {
	var data;
	var obj=localStorage.getItem(key);
	try{
		data=JSON.parse(obj);
	}catch (e) {
		data=obj;
	}
	return data;
}
Comm.indexOfArr = function (arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val)
            return i;
    }
    return -1;
};
Comm.removeArr = function (arr, val) {
    var index = this.indexOfArr(arr, val);
    if (index > -1) {
        arr.splice(index, 1);
    }
    return arr;
};

Comm.openWin=function(url,iHeight,iWidth){
	url=url||"npc/handleOpinion.html";
	url=baseUrl+url;
    iWidth=iWidth||850;                          // 弹出窗口的宽度;
    iHeight=iHeight||800;                         // 弹出窗口的高度;
    var name='newwindow';                            // 网页名称，可为空;
    // 获得窗口的垂直位置
    var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
    // 获得窗口的水平位置
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
    window.open(url, name, 'height=' + iHeight + ',width=' + iWidth +',top=' + iTop + ',left=' + iLeft + ',scrollbars=yes, resizable=yes,status=no,toolbar=no,menubar=no,location=no,titlebar=no');
    // window.open("AddScfj.aspx", "newWindows",
	// 'height=100,width=400,top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,
	// resizable=yes,location=no, status=no');
}

Comm.isMobile=function() {  
   var userAgentInfo = navigator.userAgent;
   var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];  
   var flag = false;  
   for (var v = 0; v < Agents.length; v++) {  
       if (userAgentInfo.indexOf(Agents[v]) !=-1) { flag = true; break; }  
   }  
   return flag;  
}

Comm.go=function(url){
	location.href=baseUrl+url;
}

Comm.setSelectData=function($tag,data){
	$tag.html("<option></option>");
	var html=[];
	$.each(data,function(i,e){
		html.push('<option value="'+e.id+'">'+e.text+'</option>');
	})
	$tag.append(html.join(''));
}

Comm.alert=function(message){
	if(window.hasOwnProperty("dd")){
		var _default={message: "",title:dd_res.tip,buttonName:dd_res.okButton};
		if(typeof(message)=="object"){
			_default=$.extend(_default,message);
		}else{
			_default.message=message;
		}
		dd.device.notification.alert(_default);
	}else if(top.dialog){
		top.dialog({
			id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
			title : '提示框',
			content: message,
			width : 350,
			height : 50,
			okValue:"确定",
			cancelValue: "取消",
			cancel: true
		}).showModal();
	}else {
		alert(message);
	}
}

Comm.confirm=function(message){
	var _default={message: "",title:dd_res.tip,buttons:[dd_res.cancelButton, dd_res.okButton],fn:function(data){}};
	if(typeof(message)=="object"){
		_default=$.extend(_default,message);
	}else{
		_default.message=message;
	}
	if(window.hasOwnProperty("dd")){
		dd.device.notification.confirm({
		    message: _default.message,
		    title: _default.title,
		    buttonLabels: _default.buttons,
		    onSuccess : function(result) {
		    	_default.fn(result.buttonIndex);
		    },
		    onFail : function(err) {}
		});
	}else{
		_default.fn(confirm(_default.message))
	}
}

Comm.prompt=function(message){
	var _default={message: "",title:dd_res.tip,buttons:[dd_res.cancelButton, dd_res.okButton],fn:function(data){}};
	if(typeof(message)=="object"){
		_default=$.extend(_default,message);
	}else{
		_default.message=message;
	}
	if(window.hasOwnProperty("dd")){
		dd.device.notification.prompt({
		    message: _default.message,
		    title: _default.title,
		    buttonLabels: _default.buttons,
		    onSuccess : function(result) {
		    	_default.fn(result.value);
		    },
		    onFail : function(err) {}
		});
	}else{
		_default.fn(prompt(_default.message))
	}
}

Comm.toast=function(message){
	if(window.hasOwnProperty("dd")){
		dd.device.notification.toast({
		    text: message //提示信息
		});
	}
}

/**
 * Format date to a string
 *
 * @param  string   format
 * @return string
 */
Date.prototype.format = function(format)
{
    var date = {
        'M+': this.getMonth() + 1,
        'd+': this.getDate(),
        'h+': this.getHours(),
        'm+': this.getMinutes(),
        's+': this.getSeconds(),
        'q+': Math.floor((this.getMonth() + 3) / 3),
        'S+': this.getMilliseconds()
    };
    if (/(y+)/i.test(format))
    {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date)
    {
        if (new RegExp('(' + k + ')').test(format))
        {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k] : ('00' + date[k]).substr(('' + date[k]).length));
        }
    }
    return format;
};

/**
 * 表单校验
 * @param id 需要校验的表单ID 默认值为：#groupForm
 * */
Comm.validDDParam=function(id){
	//设置默认值
	id = (id)?id:"#groupForm";
	//获取所有需要校验的对象
	var req = $(id+" .required");
	var len = req.length;
	for (var i=0;i<len;i++ ) {
		//找到同级表单元素
		var _dom = req.eq(i);
		var label = "";
		if(window.hasOwnProperty("DingTalkPC")){
			label = $(_dom).parentsUntil(".colDiv").find("span").eq(0);
		}
		else{
			label = $(_dom).parentsUntil(".input_control").parent().find(".colFirst").find("label");
		}	
		//如果为空则提示
		if (!_dom.val()) {
			dd.device.notification.toast({
			    text: label.text()+dd_res.required_tips, //提示信息
			    duration: 1
			});
			return false;
		} 
	}
	return true;
}

Comm.handleName=function(name){
	if(!name){
		return "";
	}
	var str = /[\u4e00-\u9fa5]/;
	//如果是中文
	if(str.test(name)){
		var len=name.length;
		//长度大于2则返回后面两个
		if(len>2){
			return name.substring(len-2,len)
		}else{
			//等于或者小于2直接返回
			return name;
		}
	}else{
		//如果是英文则直接返回首字符
		return name.substring(0,1);
	}
}

Comm.workAgree=function(that){
	dd.device.notification.prompt({
	    message: dd_res.wf_p_e_c,
	    title: dd_res.wf_opinion_tip_title,
	    buttonLabels: [dd_res.okButton],
	    onSuccess : function(result) {
	    	var actId = $(that).attr("id");
	    	$("#actId").val(actId);
	    	var content=result.value;
	    	content=content?content:dd_res.wf_agree;
	    	$("#work_logs_content").val(content);
	    	var data = Comm.getData("workflow/wf/excute.do",$("#groupForm").serialize());
	    	dd.device.notification.alert({
	    		"message":data.message,
	    		"title":dd_res.notice,
	    		"buttonName":dd_res.okButton,
	    		onSuccess: function(data) {
	    			location.href=location.href;
	    		}
	    	});
	    },
	    onFail : function(err) {}
	});
}

Comm.workCommit=function(that){
	if(this.validDDParam()){
		var actId = $(that).attr("id");
		$("#actId").val(actId);
		var data = Comm.getData("workflow/wf/excute.do",$("#groupForm").serialize());
		dd.device.notification.alert({"message":data.message,"title":dd_res.notice,"buttonName":dd_res.okButton,onSuccess: function() {
			if(data.success){
				var url="dingding/wf/openTask.do?FK_Flow=" + data.flowTempleteId + "&WorkID="
				+ data.WorkItemID+ "&FID=" + data.FID + "&FK_Node=" + data.EntryID
				+ "&dynamicPageId=" + data.dynamicPageId;
				location.href=baseUrl+url;
			}
		}});
	}
}

Comm.workReject=function(that){
	dd.device.notification.prompt({
	    message: dd_res.wf_p_e_c,
	    title: dd_res.wf_opinion_tip_title,
	    buttonLabels: [dd_res.okButton],
	    onSuccess : function(result) {
	    	var actId = $(that).attr("id");
	    	$("#actId").val(actId);
	    	var actType="&actType=2027";
	    	var content=result.value;
	    	content=content?dd_res.wf_approval_reject+": "+content:dd_res.wf_approval_reject;
	    	$("#work_logs_content").val(content);
	    	var data = Comm.getData("workflow/wf/excute.do",$("#groupForm").serialize()+actType);
	    	dd.device.notification.alert({
	    		"message":data.message,
	    		"title":dd_res.notice,
	    		"buttonName":dd_res.okButton,
	    		onSuccess: function(data) {
	    			location.href=location.href;
	    		}
	    	});
	    },
	    onFail : function(err) {}
	});	
}

Comm.workUndo=function(that){
	var actId = $(that).attr("id");
	$("#actId").val(actId);
	var actType="&actType=2026";
	var data = Comm.getData("workflow/wf/excute.do",$("#groupForm").serialize()+actType);
	dd.device.notification.alert({"message":data.message,"title":dd_res.notice,"buttonName":dd_res.okButton,onSuccess: function(data) {
		location.href=location.href;
	}});
}

Comm.workTransmit=function(that){
	var actId = $(that).attr("id");
	$("#actId").val(actId);
	dd.biz.contact.choose({
		"corpId":dd_config.corpId,	
		 onSuccess: function(data){
				var selectIds=[];
				var selectUserNames=[];
				for(var i=0;i<data.length;i++){
					selectIds.push(data[i].emplId);
					selectUserNames.push(data[i].name);
				}
				$("#slectsUserIds").val(selectIds.join(","));
				var content = dd_res.wf_w_h_b_h_o_t + selectUserNames.join(",")
				$("#work_logs_content").val(content);
				var data = Comm.getData("workflow/wf/excute.do",$("#groupForm").serialize());
				dd.device.notification.alert({"message":data.message,"title":dd_res.notice,"buttonName":dd_res.okButton,onSuccess: function(data) {
					location.href=location.href;
				}});
		},
	    onFail: function(err){
	    	console.log(JSON.stringify(err));
	    }
	});
}
Comm.cookie = function(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + options.path : '';
        var domain = options.domain ? '; domain=' + options.domain : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

Comm.ddConfig = function(){
	window.dd_config = Comm.getData("dingding/config.do",{"_method":"get","url":location.href});
	dd.config({
	    agentId : dd_config.agentid,	 	// 必填，微应用ID
	    corpId : dd_config.corpId,			//必填，企业ID
	    timeStamp : dd_config.timeStamp,	// 必填，生成签名的时间戳
	    nonceStr : dd_config.nonceStr,		// 必填，生成签名的随机串
	    signature : dd_config.signature,	// 必填，签名
	    jsApiList : ["biz.chat.chooseConversation","biz.contact.choose","biz.customContact.multipleChoose","biz.util.multiSelect","biz.util.uploadImage","biz.cspace.preview","biz.util.uploadImageFromCamera","biz.util.uploadAttachment","device.geolocation.get","biz.contact.departmentsPicker","runtime.permission.requestOperateAuthCode" ,"device.notification.confirm","device.notification.alert","device.notification.toast"]
	});

	dd.error(function(error){	//dd.config验证失败会执行error函数
	    if(error.errorCode==3 || error.errorCode=="PC_1003"){
	        Comm.getData("api/execute/clearLocalStorage",{"_method":"get"});
	        location.href = location.href;
	    }else{
	        console.log("dd error: " + JSON.stringify(error));
	    }
	});
}

//对于html页面，如果是在钉钉中打开，且没有登录，则跳转到goto.html页面先做授权登录
;(function(){
	var userAgentInfo = navigator.userAgent;
	if(userAgentInfo.indexOf("DingTalk")!=-1 && location.href.indexOf("goto.html")==-1){
		var No = Comm.cookie("No");		
		if(!No){		
			var url = baseUrl + "dingding/goto.html?url=" + encodeURIComponent(location.href);
			location.href = url;	
		}
	}	
})();