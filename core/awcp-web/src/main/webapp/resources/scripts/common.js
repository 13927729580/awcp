function saveFormPage(dynamicPageId,actId){
	$("#actId").val(actId);		
	$.ajax({
	   type: "POST",
	   url: basePath + "document/excuteOnly.do",
	   data:$("#groupForm").serialize(),
	   async : false,
	   success: function(data){
			if(data==1){
				alert("保存成功");			
				location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId;
			}
			else{
				alert(data);
			}
	   }
	});	
}

function excuteNormalAct(dynamicPageId,actId){
	var count = $("input[name='_selects']").length;
	if(count != 1){
		alert("请至选择一项进行操作");
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
				alert("执行成功");			
				location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId;
			}
			else{
				alert(data);
			}
	   }
	});
}

function backToViewPage(dynamicPageId){
	location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId;
}

function editFormPage(dynamicPageId){
	var count = $("input[name='_selects']").length;
	if(count!=1){
		alert("请选择一项进行操作");
		return false;
	}
	var id = $("input[name='_selects']").val();
	location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId + "&id=" + id;	
}

function deleteRecord(dynamicPageId,actId){
	var count = $("input[name='_selects']").length;
	if(count==0){
		alert("请至少选择一项进行操作");
		return false;
	}
	if(!confirm("是否确认删除?")){
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
				alert("删除成功");			
				location.href = basePath + "document/view.do?dynamicPageId=" + dynamicPageId;
			}
			else{
				alert(data);
			}
	   }
	});
}

function addModal(e){
	title = e.title?e.title:'提示';
	url = e.url?e.url:'';
	content = e.content?e.content:'';
	top.dialog({
		title: title,
		url:url,
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
	var msg = $.messager.show(message, {placement: 'top',type:'info',icon:'info-sign'});
};

function dialogAlert(data){
		var d = top.dialog({
			id:'Info' + $.uuid(),
			title: '操作提示',
			width:'630',
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
		d.showModal();
}

function dialogConfirm(msg,fn){
	var d = top.dialog({
		id:'Confirm' + $.uuid(),
		title: '确认提示框',
		width:'630',
		height:'auto',
		content: msg,
		cancelValue: 'Cancel',
		cancel: function () {
			return false;	
		},
		cancel: false,
		button: [
					{
						value: 'OK',
						callback: function (){
							this.close("Y");
							this.remove();
						},
					},
					{
						value: 'Cancel',
						callback: function () {
							this.close("N");
							this.remove();
						},
					}
			]
	});			
	d.showModal();		
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

//给文件框添加校验
function addFileValidator(ids){
	for(var i=0;i<ids.length;i++){
		var selector = '#'+ids[i]+' .orgfile';
		$(selector).formValidator({
			onFocus:"",
			onCorrect:"",
		}).inputValidator({
			min:1,
			empty:{leftEmpty:true,rightEmpty:true,emptyError:""},
			onError:"请上传文件"
		});
	}	
}

//给文本输入框添加校验
function addInputValidator(ids){
	for(var i=0;i<ids.length;i++){
		var selector = '#'+ids[i];
		$(selector).formValidator({
			onFocus:"",onCorrect:"",
		}).inputValidator({
			min:1,
			empty:{leftEmpty:true,rightEmpty:true,emptyError:""},
			onError:"请输入内容"
		});
	}	
}

//buttons可以随滚动条滚动
$(window).scroll(function(){
	var offsetTop = 0;
	var headTop = 0;
	if($("#searchform").length>0){
		offsetTop = $("#searchform").next().offset().top;		
	}else if($("#groupForm").length>0){
		offsetTop = $("#groupForm").offset().top;
	}
	if(offsetTop>0){
		var scrollTop = $(window).scrollTop();
		var top = ($(".datatable-head").height() == null)?40:($(".datatable-head").height());
		if(scrollTop>offsetTop){
			$(".row#buttons").addClass("navbar-fixed-top").css({
				top:top,
				textAlign:'center'
			});
			$(".row#buttons button:visible").addClass("btn-sm");
		}else{
			$(".row#buttons:visible").removeClass("navbar-fixed-top").removeAttr("style");
			$(".row#buttons button:visible").removeClass("btn-sm");
		}
	}
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

//如果页面链接参数存在readonly==true则禁用所有表单组件
if($.trim($("#readonly").val())=="true"){
	$(":input").not(":button[value='返回']").prop("disabled",true);
}