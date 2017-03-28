var tabs=function(){
    this.names=$(".tabs").attr("lang").split(';');//tab名称
    this.url=$(".swiper-container").attr("lang").split(';');//tab链接
    this.init();
}
tabs.prototype={
    init:function(){
        var _this=this;
        $.each(_this.names,function(i,e){
            //添加tab头部dom节点
            var _html_name='<a href="#" hidefocus="true" class="">'+e+'</a>';
            $(".wrap .tabs").append(_html_name);
        })
        $.each(_this.url,function(i,e){
            //添加iframe  链接
            var _html_url='<div class="swiper-slide">'
                _html_url+='<div class="content-slide" style="height: 100%;padding: 0;">'
                _html_url+='<div class="iftame_parent">'
                _html_url+='<iframe height="100%" width="100%" src="'+basePath+e+'" id="tabIframe'+i+'" name="tabIframe'+i+'"></iframe>'
                _html_url+='</div>'
                _html_url+='</div>'
                _html_url+='</div>'
            $(".swiper-container .swiper-wrapper").append(_html_url);
        })
        $(function(){
            window.setTimeout(function(){
                //将tab切换第一个弄成选中状态
                if($(".wrap .tabs a")){
                    $(".wrap .tabs a").eq(0).addClass("active");
                }
                if($(".swiper-container .swiper-wrapper .swiper-slide")){
                    $(".swiper-container .swiper-wrapper .swiper-slide").eq(0).addClass("swiper-slide-visible swiper-slide-active")
                }
                //swiper插件
                var tabsSwiper = new Swiper('.swiper-container',{
                    speed:500,
                    onSlideChangeStart: function(){
                        $(".tabs .active").removeClass('active');
                        $(".tabs a").eq(tabsSwiper.activeIndex).addClass('active');
                    }
                });

                //获取让tab自适应屏幕的宽度
                //var _tabs=$(".wrap .tabs a");
                //_tabs.css("width",10+"%")


                $(".tabs a").on('touchstart mousedown',function(e){
                    e.preventDefault()
                    $(".tabs .active").removeClass('active');
                    $(this).addClass('active');
                    tabsSwiper.swipeTo($(this).index());
                });

                $(".tabs a").click(function(e){
                    e.preventDefault();
                })
            },300)

        })
    }

}



