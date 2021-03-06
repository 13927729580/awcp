function gotoUrl(obj){
	var params = "";
	for(key in obj){
		params += key + "=" + obj[key] + "&";
	}
	location.href = basePath + "document/view.do?" + params;
}

/**
 * 列表页面进行搜索
 * @returns
 */
function search(){
	$("#groupForm").submit();
}

/**
 * 重置列表页面搜索条件,然后搜索
 * @returns
 */
function resetView(){
	$("#groupForm")[0].reset();
	Comm.clearSearch();
	$("#groupForm").submit();
}

function updateAction(params){
    var str = "";
    var bool = true;
    for(var i=0;i<params.length;i++){
        var key = params[i];
        var val = Comm.getUrlParam(key);
        if(val){
            if(bool){
                str += "?" + key + "=" + val;
                bool = false;
            } else{
                str += "&" + key + "=" + val;
            }
        }
    }
    var action = $("#groupForm").attr("action") + str;
    $("#groupForm").attr("action",action);
}

function updateParamsToGroupForm(params){
    var $groupForm = $("#groupForm");

    for(var i=0;i<params.length;i++){
        var key = params[i];
        var val = Comm.getUrlParam(key);

        if(val){
            var $elem = $groupForm.find("input[name='"+key+"']");

            if($elem.size() == 0){
                var newInputStr = '<input type="hidden" name="'+ key +'" value="' + val + '" id="' +key+ '"/>';
                $groupForm.prepend(newInputStr);
            }
            else{
                $elem.val(val);
            }
        }
    }
}

function newFormPage(dynamicPageId,actId, suffixParams){
    if(!suffixParams){ suffixParams = ''; }

    $("#"+actId).attr("disabled","true");
    location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId + suffixParams;
}

/**
 * 表单页面保存后跳转到对应的列表页面
 * @param dynamicPageId		列表页面的动态页面ID
 * @param actId				保存按钮的actID
 * @param suffixParams		其他参数
 * @returns
 */
function saveFormPage(dynamicPageId,actId,suffixParams){
    $("#buttons").find("button").attr("disabled",true);	//禁用所有按钮,防止提交数据时同时进行其他操作
    $("#actId").val(actId);
    if(!suffixParams){ 
    	suffixParams = "";
    }
    $.ajax({
        type: "POST",
        url: basePath + "document/excuteOnly.do",
        data: $("#groupForm").serialize(),
        async: false,
        success: function(data){
            if(data==1){
                Comm.alert("保存成功",function(){
                    location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId + suffixParams;
                });
            } else{
            	if(data.message){
            		Comm.alert(data.message,function(){
                        $("#buttons").find("button").attr("disabled",false);
                    });
            	} else{
            		Comm.alert("保存失败",function(){
                        $("#buttons").find("button").attr("disabled",false);
                    });
            	}               
            }
        }
    });
}

/**
 * 弹窗的表单页面保存后关闭该页面
 * @param actId	动作ID
 * @returns
 */
function savePageThenClose(actId){
	$("#buttons").find("button").attr("disabled",true);
	$("#actId").val(actId);
	$.ajax({
	    type: "POST",
	    url: basePath + "document/excuteOnly.do",
	    data:$("#groupForm").serialize(),
	    async : false,
	    success: function(data){
			if(data==1){
				Comm.alert("保存成功",function(){
					top.dialog({id:window.name}).close();
				});
			} else{
				if(data.message){
            		Comm.alert(data.message,function(){
                        $("#buttons").find("button").attr("disabled",false);
                    });
            	} else{
            		Comm.alert("保存失败",function(){
                        $("#buttons").find("button").attr("disabled",false);
                    });
            	}     			
			}
	    }
	});
}

function excuteNormalAct(dynamicPageId,actId){
    var count = $("input[name='_selects']").length;
    if(count != 1){
        Comm.alert("请至选择一项进行操作");
        return false;
    }
    $("#actId").val(actId);
    $.ajax({
        type: "POST",
        url: basePath + "document/excuteOnly.do",
        data:$("#groupForm").serialize(),
        async : false,
        success: function(data){
            if(data==1){
                Comm.alert("执行成功",function(){
                    location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId;
                });
            } else{
                Comm.alert(data);
            }
        }
    });
}

function backToViewPage(dynamicPageId,suffixParams){
    if(!suffixParams){ suffixParams = '';}
    location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId + suffixParams;
}

function goBack(dynamicPageId){
    if(dynamicPageId){
        location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId;
    } else{
        var menuName = parent.$("#tab-menu").find("a.active").text();
        if(menuName=="在办件"){
			location.href = basePath + "venson/inDoingTasks.html";
        } else if(menuName=="待办件"){
			location.href = basePath + "venson/listPersonalTasks.html";
        } else if(menuName=="办结件"){
			location.href = basePath + "venson/listHistoryTasks.html";
        } else{
            history.back();
        }
    }
}

/**
 * 关闭当前tab页面
 * @returns
 */
function closePage(){
    parent.closeTabByPageId(parent.$("#tab-menu").find("a.active").attr("data-pageid"));
}

/**
 * 打开新的tab页面
 * @param url	链接地址
 * @param title	页面标题
 * @returns
 */
function openPage(url,title){
    parent.addTabs({
        url: url,
        title: title,
        close:true
    });
}

function editFormPage(dynamicPageId, suffixParams){
    if(!suffixParams){ suffixParams = '';}

    var count = $("input[name='_selects']").length;
    if(count!=1){
        Comm.alert("请选择一项进行操作");
        return false;
    }
    var id = $("input[name='_selects']").val();
    location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId + "&id=" + id + suffixParams;
}

function deleteRecord(actId){
    var count = $("input[name='_selects']").length;
    if(count==0){
        Comm.alert("请至少选择一项进行操作");
        return false;
    }
    dialogConfirm("是否确认删除?",function(){
        $("#actId").val(actId);
        $.ajax({
            type: "POST",
            url: basePath + "document/excuteOnly.do",
            data:$("#groupForm").serialize(),
            async : false,
            success: function(data){
                if(data==1){
                    Comm.alert("删除成功",function(){
                        $("#groupForm").submit();
                    });
                } else{
                    Comm.alert("删除失败");
                }
            }
        });
    });
}

function addModal(e){
    var title = e.title?e.title:'提示';
    var url = e.url?e.url:'';
    var content = e.content?e.content:'';
    top.dialog({
        title: title,
        url:url,
        skin:"col-md-4",
        content:content,
        okValue: '确定',
        ok: function () {
            this.title('提交中…');
            return false;
        },
        cancelValue: '取消',
        cancel: function () {}
    }).width(600).showModal();
};

function alertMessage(message){
    $.messager.show(message, {placement: 'top',type:'info',icon:'info-sign'});
};

function dialogAlert(data){
    var d = top.dialog({
        id:'Info' + Comm.uuid(),
        title: '操作提示',
        skin:"col-md-4",
        height:'auto',
        content: data,
        cancelValue: 'Cancel',
        cancel: function () {
            return false;
        },
        cancel: false,
        button: [
            {
                value: 'Close',
            }
        ]
    });
    d.show();
}

function dialogConfirm(msg,fn){
    var d = top.dialog({
        id:'Confirm' + Comm.uuid(),
        title: '确认提示框',
        skin:"col-md-4",
        height:'auto',
        content: msg,
        cancelValue: 'Cancel',
        cancel: function () {
            return false;
        },
        cancel: false,
        button: [
            {
                value: '确认',
                callback: function (){
                    this.close("Y");
                    this.remove();
                },
            },
            {
                value: '取消',
                callback: function () {
                    this.close("N");
                    this.remove();
                },
            }
        ]
    });
    d.show();
    d.addEventListener('close', function () {
        if(this.returnValue=="Y"){
            fn();
        }
        else{
            return false;
        }
    });
}

//当失去焦点时，去除输入框前后空格
function clearBlank(){
    $("input[type='text']").blur(function(){
        $(this).val($(this).val().replace(/^\s+|\s+$/g,""));
    });
}

$(function(){
    //当失去焦点时，去除输入框前后空格
    clearBlank();
});

function formatNumber(num){
    var bool = false;
    if(num.indexOf(".")!=-1){
        bool = true;
    }
    var arr = num.split(".");
    var before = "";
    var after = "";
    if(arr.length==2){
        before = arr[0];
        after = arr[1];
    }
    else{
        before = arr[0];
    }
    var beforeArrReverse = before.split("").reverse();
    var temp = "";
    for(var i = 0; i < beforeArrReverse.length; i ++ )   {
        temp += beforeArrReverse[i] + ((i + 1) % 3 == 0 && (i + 1) != beforeArrReverse.length ? "," : "");
    }
    var result = "";
    if(after!=""){
        result = temp.split("").reverse().join("") + "." + after;
    }
    else{
        result = temp.split("").reverse().join("") + (bool?".":"");
    }
    return result;
}

function checkIsNumber(obj){
    var value = $.trim($(obj).val());
    value = value.replace(/,/g,'');
    if(value && !$.isNumeric(value)){
        $(obj).focus();
        dialogAlert("请输入一个数字.");
        return false;
    }
    else{
        return true;
    }
}

//定义弹窗的高度和宽度
var dialogHeight = $(top).height()-93;
var dialogWidth = 1024;