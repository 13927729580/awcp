$(".fileName").each(function(i,e){
	var that=$(this);
	if(that.val()){
		var fileNames=that.val().split(";")
		var html=[];
		html.push('<div>');
		$.each(fileNames,function(k,o){
			if(o){
				var data=Comm.getData("common/file/get.do",{id:o});
				if(data.id){
					filename=data.filename;
					if(data.filename.length>18){
						filename=filename.substring(0,13)+"..."+filename.substring(data.filename.length-5);
					}
					html.push("<p>");
					html.push('<a class="preview" lang="'+data.id+'" href="javascript:void(0)">'+filename+'</a>');
					html.push('<a style="float:right;margin-right:150px;" class="remove" lang="'+data.id+'" title="'+e.lang+'"  href="javascript:void(0)">删除</a>');
					html.push('<a style="float:right;margin-right:100px;" class="download" lang="'+data.id+'" href="javascript:void(0)">下载</a>');
					html.push("</p>");
				}
			}
		})
		html.push('</div>');
		that.parent().append(html.join(''));
		
	}
})



$("input[value='添加']").click(function() {
	var name = $(this).prev(".fileName").attr("name");
	var html = [];
	html.push('<div style="position: relative;"> ');
	html.push('<input type="text" readonly="readonly" style="width: 310px;"> ');
	html.push('<input type="button" value="选择文件"> ');
	html.push('<input name="file" class="file" onchange="upload(this)" type="file" style="left: 320px;  opacity: 0; position: absolute; top: 11px; width: 70px;"> ');
	html.push('<input class="fileName" name="' + name + '" type="hidden"> ');
	html.push('<input onclick="removeFile(this)" type="button" value="删除"> ');
	$(this).parent().parent().append(html.join(''));
})
$(".download").click(function() {
	Comm.openWin("common/file/download.do?fileId="+this.lang)
})
$(".preview").click(function() {
	Comm.openWin("common/file/preview.do?fileId="+this.lang,800,1000)
})
$(".remove").click(function() {
	var name=$(this).parent("p").children(".preview").text();
	if(confirm("是否要删除文件【 "+name+" 】？")){
		var id=this.lang;
		var hid=$(this).parent().parent().prevAll(":hidden");
		var val=hid.val().replace(id,"");
		hid.val(val);
		if(hid.val().length<32){
			hid.prevAll(":text").val("");
			hid.prevAll("input").prop("disabled",false);
		}
		if(id){
			Comm.removeAjax("common/file/delete.do",{ids:id},$(this));
		}
	}
})

function removeFile(obj) {
	$(obj).parent().remove();
}

function upload(obj) {
	if ($(obj).parent("form").length == 0) {
		var html = '<form method="post" style="display:inline" enctype="multipart/form-data"></form>';
		$(obj).wrap(html);
	}
	var form = $(obj).parent("form");
	form.prevAll(":text").val($(obj).val());
	Comm.uploadFile(handle, form);
	var html="<span class='img-wrap' style='color:red;font-size:12px;'>文件正在上传,请稍后...<img width='20' height='20' src='"+baseUrl+"images/progress.gif' ></span>";
	var $imgWrap=$(html);
	form.parent().append($imgWrap);
	$(".btn").prop("disabled",true);
	function handle(data) {
		if (data.flag == 1) {
			var fileName=form.parent().children(".fileName");
			fileName.val(data.msg);
			$imgWrap.css("color","green")
			$imgWrap.text("文件上传成功！");
			$(".btn").prop("disabled",false);
		} else {
			alert(data.msg)
			$(".btn").prop("disabled",false);
			$imgWrap.text("很抱歉，文件上传失败，请与管理员联系！");;
		}

	}
}