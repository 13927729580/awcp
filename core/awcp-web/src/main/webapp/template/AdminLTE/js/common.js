/**
 * Created by wangzhenyu on 2017/9/8.
 */
//http请求
var serverAddress=window.location.protocol + "//" + window.location.host + "/awcp/";
function httpRequest(type,url,data,callBack){
    if (!url) return;
    $.ajax({
        type: type,
        data:data,
        async:false,
        url:serverAddress+url,
        //contentType: "application/json",
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        //beforeSend: function(request) {
        //},
        success: function (data) {
        	//如果后台返回未登陆则直接跳转至登陆页面
        	if(data.hasOwnProperty("status")){
        		if(data.status==-4){
        			
        		}
        	}
            callBack(data)
        },
        error: function (data) {
        	console.log(data);
            fadePop("请求失败");
        }
    })
}


var scrollDirect= function (fn) {  
    var beforeScrollTop = document.body.scrollTop;  
    fn = fn || function () {  
    };  
    window.addEventListener("scroll", function (event) {  
        event = event || window.event;  
  
        var afterScrollTop = document.body.scrollTop;  
        delta = afterScrollTop - beforeScrollTop;  
        beforeScrollTop = afterScrollTop;  
  
        var scrollTop = $(this).scrollTop();  
        var scrollHeight = $(document).height();  
        var windowHeight = $(this).height();  
        if (scrollTop + windowHeight > scrollHeight - 10) {  //滚动到底部执行事件  
            fn('up');  
            return;  
        }  
        if (afterScrollTop < 10 || afterScrollTop > $(document.body).height - 10) {  
            fn('up');  
        } else {  
            if (Math.abs(delta) < 10) {  
                return false;  
            }  
            fn(delta > 0 ? "down" : "up");  
        }  
    }, false);  
}  
/*提示信息*/
function fadePop(str){
    var html = "<div class='fadePop'><p class='fadePopCon'>"+str+"</p></div>";
    $(".fadePop").remove();
    $("body").append(html);
    $(".fadePop").fadeIn(function(){
        setTimeout(function(){
            $(".fadePop").fadeOut();
        },1000)
    })
}





//获取网页传过来的参数
function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

//1.封装cookie的设置函数
function setCookie(key,value,time){
    var odate = new Date;
    time=(time)?time:7*24*60*60*1000;
    odate.setDate(odate.getDate()+time);
    document.cookie = key +'='+ escape(value) +';expires=' + odate+';path=/;';
}

//2.封装cookie的获取函数
function getCookie(name){
    var arr = document.cookie.split('; ');
    for(var i = 0;i<arr.length ;i++){
        var newArr = arr[i].split('=');
        if(newArr[0]==name){
            return unescape(newArr[1]);
        }
    }
    return false;
}

//返回上一页
$(".header .backPrePage").bind("click",function(){
	window.history.back();
})

