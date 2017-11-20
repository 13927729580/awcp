//var tabs=function(nodeId){
//	if(nodeId){
//		this.nodeId = nodeId;
//		this.names=$(nodeId).attr("tabName").split(';');//tab名称
//		this.url=$($(nodeId)).attr("tabUrl").split(';');//tab链接
//		this.init();
//	}
//}
//
//tabs.prototype = {
//	init:function(){
//		var _this = this;
//		for(var i =0 ;i<_this.names.length;i++){
//			var ifarmeName= "tabIframe"+i;
//			_this.addTab(_this.names[i],_this.url[i],ifarmeName,_this.names[0])
//		}
//		
//	},
//	//easyUI 创建tab标签页  判断title标签页是否存在  存在则选中title页      不存在新建title页 
//	addTab:function(title,url,ifarmeName,selected,isClose){ 
//		var _this = this;
//		var isClose = isClose?true:false;
//		
//		if ($(_this.nodeId).tabs('exists', title)){
//			$(_this.nodeId).tabs('select', title);
//		} else {
//			var tab = $(_this.nodeId).tabs('getTab',selected);
//			var index =$(_this.nodeId).tabs('getTabIndex',tab);
//			var content = '<iframe scrolling="auto" frameborder="0" id="'+ifarmeName+'" name="'+ifarmeName+'" src="'+basePath+url+'" style="width:100%;height:100%;"></iframe>';
//			$(_this.nodeId).tabs('add',{
//				selected:index,    	//选中
//				title:title,   		
//				content:content,	//显示内容
//				closable:isClose	//是否带关闭按钮  boolean 
//			});
//		}
//		
//	}
//}

var tabs=function(dom){
this.names=$(".tabs").attr("lang").split(';');// tab名称
this.url=$(".swiper-container").attr("lang").split(';');// tab链接
this.init();
}
tabs.prototype={
init:function(){
  var _this=this;
  $.each(_this.names,function(i,e){
      // 添加tab头部dom节点
      var _html_name='<a href="#" hidefocus="true" class="">'+e+'</a>';
      $(".wrap .tabs").append(_html_name);
  })
  $.each(_this.url,function(i,e){
      // 添加iframe 链接
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
          // 将tab切换第一个弄成选中状态
          if($(".wrap .tabs a")){
              $(".wrap .tabs a").eq(0).addClass("active");
          }
          if($(".swiper-container .swiper-wrapper .swiper-slide")){
              $(".swiper-container .swiper-wrapper .swiper-slide").eq(0).addClass("swiper-slide-visible swiper-slide-active")
          }
          // swiper插件
          var tabsSwiper = new Swiper('.swiper-container',{
              speed:500,
              onSlideChangeStart: function(){
                  $(".tabs .active").removeClass('active');
                  $(".tabs a").eq(tabsSwiper.activeIndex).addClass('active');
              }
          });

          // 获取让tab自适应屏幕的宽度
          // var _tabs=$(".wrap .tabs a");
          // _tabs.css("width",10+"%")


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