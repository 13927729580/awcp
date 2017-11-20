$(document).ready(function(){
	
//一级列表点击
    $(".title-one").on("click",function(){   	
        $(this).parents('li').siblings('li').find('a').removeClass('title-one2');
        $(this).parents('li').siblings('li').find('span').removeClass('spanctive');
        $(this).toggleClass("title-one2");
        $(this).find('span').toggleClass('spanctive');
        
        //点击其它一级列表的时候当前的ul2隐藏
        if ($(this).next().is(":hidden")) {
            $(".ul2").hide();
        }
        if($(this).next().length!=0){
            $(this).next().slideToggle();
        }

    })
//二级列表点击
    $(".title-two").on("click",function(){
        $(".ul3").hide(300);
        $(this).next().show();        
		if($(this).children().eq(1).attr('src')=="images/xiala2.png"){
			$(this).children().eq(1).attr("src","images/xiala3.png")
		}else if($(this).children().eq(1).attr('src')=="images/xiala3.png"){
			$(this).children().eq(1).attr("src","images/xiala2.png")
		};
		$(this).siblings().find('img').attr("src","images/xiala3.png")
         
   
        //二级列表下面的页面的弹出框

   })   
   //点击时添加样式
  $(".ul2>.title-two").on("click",function(){
        $(".ul2>.title-two").css("background","");
        $(".ul2>li").children().css("color","");
        $(this).css("background","#0497f6");
        $(this).children().css("color","#fff");
    })  
    
//三级列表点击
    $(".ul3>li").on("click",function(){
        $(".ul3>li").css("background","");
        $(".ul3>li").children().css("color","");
        $(this).css("background","#56b3fd");
        $(this).children().css("color","#fff");
    })
   
    function nextpage(url){
        var _html='<iframe   id="ifrm0" name="main"  width="100%" height="100%" src="'+url+'" frameborder="0" data-id="#" seamless></iframe>';
        $("#content-main").html(_html);

    }
//点击左侧显示右边主题内容
    $(".navlang").on("click",function(){
        nextpage($(this).attr("lang"));

    })
//点击按钮关闭快捷

    $(".quikeclose").on("click",function(){
        $("#content-main").css("height",710);
        $(".quike").slideToggle();
        var _height=$(".quike").height();
        if(_height==95){
            $("#content-main").css("height",820)
        }

    })

//=================================点击弹出框模块=============================================================

        //默认页面的弹出框
        //alertpage();

    function  alertpage(){
        window.setTimeout(function(){
            window.frames["main"].document.getElementsByClassName("pop")[0].onclick=function(){
                $(".popup1").css("display","block");
                $(".shade").css("display","block");

            }
            window.frames["main"].document.getElementsByClassName("pop2")[0].onclick=function(){
                $(".popup2").css("display","block");
                $(".shade").css("display","block");
            }
        },1000)
    }

    //导入页面的点击上传文件
    $("#file_input").on('change',function(){
        var file = this.files[0]; //获取file对象
        var reader = new FileReader(); //声明一个FileReader实例
        var _upname='<p style="margin-bottom:10px;">'+this.files[0].name+'</p>';
        $(".upname").append(_upname);
        var _upsize='<p style="margin-bottom:10px;">'+this.files[0].size+''+"kb"+'</p>'
        $(".upsize").append(_upsize);
        var _upchose='<p class="nullp" style="margin-bottom:10px;"></p>';
        $(".upchose").append(_upchose);
        var _loadelet='<img src="./images/xiazai.jpg"><img src="./images/delet2.jpg">';
        $(".nullp").html(_loadelet);

    });


    //点击按钮关闭
    $(".clos").on("click",function(){
        $(".popup").css("display","none");
        $(".shade").css("display","none");
    })

    $(".btn1").on("click",function(){
         $(".popup").css("display","none");
        $(".shade").css("display","none");
    })
})
