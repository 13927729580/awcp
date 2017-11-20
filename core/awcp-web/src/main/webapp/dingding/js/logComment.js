$("#vwrpReportActionArea").click(function(){
	$("#vwrpReportActionArea").hide();
	$(".report-wrap").css("padding-bottom","10rem");
	$("#vwrpCommentInputWrap").show();
});

$("#vwrpCommentSubmit").click(function(){
	var content = $(".vwrp-comment-input").val();
	Comm.getData("api/execute/saveLogs",{
		content:content,
		recordId:$("input[name='ID']").val(),
		pageId:$("#dynamicPageId").val()
	});
	appendCommentLi(content);
});

$(".vwrp-digg-item").click(function(){
	var content = $(this).text();
	Comm.getData("api/execute/saveLogs",{
		content:content,
		recordId:$("input[name='ID']").val(),
		pageId:$("#dynamicPageId").val()
	});
	appendCommentLi(content);
});

function appendCommentLi(content){
	var userInfo = Comm.getData("api/execute/getNameAndHeadImg",{});
	var name = userInfo.name;
	var img = userInfo.USER_HEAD_IMG;
	var date = new Date().format("yyyy-MM-dd hh:mm:ss");
	var content = content;
	if(img){
		img = '<img class="vwrp-comment-head" src="' + img + '">';
	}
	else{
		img = getDivImg(name);
	}
	var html = '<li class="vwrp-comment-wrap">' +
		   	   '<a href="javascript:void(0)" style="text-decoration: none;">' + img + '</a>' +
		   	   '<div class="vwrp-comment-content global-bottomLine">' +
		   	   '<span class="vwrp-comment-name">' + name + '</span>' +
		   	   '<span class="vwrp-comment-time">' + date + '</span>' +
		   	   '<p class="vwrp-comment-text">' + content + '</p>' +
		   	   '</div></li>';
	$(".vwrp-comment-list").append(html);
	var count = $("#vwrpCommentTitle").find("b").text();
	$("#vwrpCommentLay").html("<span class='vwrp-comment-num' id='vwrpCommentTitle'>" + dd_res.totalHas + 
							  "<b>" + ++count + "</b>" + dd_res.reply + "</span>");
	$("#vwrpReportActionArea").show();
	$(".report-wrap").css("padding-bottom","5.35rem");
	$("#vwrpCommentInputWrap").hide();
}

function getDivImg(name){ 
	var img = "";
	if(name){
		img = '<div style="background-color: #86ecd9;text-align: center;border-radius: 50%; width: 2.2rem;height: 2.2rem;' +
			  'line-height: 2.2rem;font-size: 13px;margin-bottom: 4px;">' + Comm.handleName(name) + '</div>';
	}
	return img;
}