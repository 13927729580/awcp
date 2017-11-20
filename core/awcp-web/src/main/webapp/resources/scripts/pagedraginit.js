function getTotalPage(basePath){
	 var dynamicPageId=$("#dynamicPageId").val();
	 var docId = $('#docId').val();
	 $.ajax({
			url:basePath+"document/getValue.do",
			data:{
				"currentPage":1,
				"pageSize":6,
				"dynamicPageId":dynamicPageId,
				"docId":docId
			},
			type:"post",
			success:function(data){
				$.each(data.totalPages,function(key,value){
					total = parseInt(value['size']);
					$("#totalPage").val(total);
				})
			}
	  })
}

function PageDragInit(ListView,currentType,basePath){
	var dragger = new DragLoader(ListView.find('.wrapper')[0], {
    threshold: 60,
    dragDownRegionCls: 'pulldownwrap',
    dragUpRegionCls: 'pullupwrap',
    dragDownHelper: function(status) {
      if (status == 'default') {
        return '<div class="pulldown"><span class="icon"></span><span class="label">下拉刷新...</span></div>';
      } else if (status == 'prepare') {
        return '<div class="pulldown flip"><span class="icon"></span><span class="label">放开刷新...</span></div>';
      } else if (status == 'load') {
        return '<div class="pulldown loading"><div style="margin:0 auto;"><span class="icon"></span><span class="label">加载中...</span></div></div>';
      }
    },
    dragUpHelper: function(status) {
    	if (status == 'default') {
		   return '<div class="pullup"><span class="icon"></span><span class="label">上拉加载更多...</span></div>';
		} else if (status == 'prepare') {
		   return '<div class="pullup flip"><span class="icon"></span><span class="label">松开加载更多...</span></div>';
		} else if (status == 'load') {
		  return '<div class="pullup loading"><span class="icon"></span><span class="label">加载中...</span></div>';
		}
    }
  });
  dragger.on('dragDownLoad', function() {
    setTimeout(function() {
	  window.location.reload();
      dragger.reset();
    }, 2000);
  });
  var currentPage,currentUrl,handleTotalPage;	
	  	//每页记录数
	  	var pageSize = 6;
	  //待办
      if(currentType=="handle"){
			showCurrentPage("handle","firstView");
	  //已办
      }else if(currentType=="dealed"){
      		
			showCurrentPage("dealed","secondView");
      }else if(currentType=="compile"){
    		
			showCurrentPage("compile","thirdlyView");
	  //邮件
    }else{
      		showCurrentPage("","wrapper");
	  //任务
      }
  dragger.on('dragUpLoad', function(){
  		dragger.reset();
  		if(currentType=="handle"){
  			currentPage = parseInt($("#currentPage1").val());
			currentPage=currentPage+1;
			handleTotalPage = $("#totalPage1").val();
			currentUrl = basePath+'oa/app/getUntreatedData.do';
  			$("#currentPage1").val(currentPage);
  		}else if(currentType=="dealed"){
  			currentPage = parseInt($("#currentPage2").val());
	  		currentPage=currentPage+1;
			handleTotalPage = $("#totalPage2").val();
			currentUrl = basePath+'oa/app/getHandledData.do';
  			$("#currentPage2").val(currentPage);
  		}else if(currentType=="compile"){
  			currentPage = parseInt($("#currentPage3").val());
	  		currentPage=currentPage+1;
			handleTotalPage = $("#totalPage3").val();
			currentUrl = basePath+'oa/app/getCompileData.do';
  			$("#currentPage3").val(currentPage);
  		}else
  		{
  			currentPage = parseInt($("#currentPage").val());
	  		currentPage=currentPage+1;
	  		var dynamicPageId=$("#dynamicPageId").val();
	  		var docId = $('#docId').val();
	  		handleTotalPage=$("#totalPage").val();
	  		var pageId=$("#pageId").val();
	  		currentUrl = basePath+'document/getValueStr.do?dynamicPageId='+dynamicPageId+'&docId='+docId;
  		}
	    //当前页码，总页数
	  	$.ajax({
			url:currentUrl,
			data:{
				"currentPage":currentPage,
				"pageSize":pageSize,
			},
			type:"post",
			success:function(data){
						 if(data!=null && currentType=="handle"){
								 	for(var i = 0;i < data.length; i++){
										var str="";
										if(currentPage < handleTotalPage || currentPage == handleTotalPage){									
												str+="<li class='ui-li-has-thumb'>";
												str+="<a  onclick=\"javascript:locationhref('"+basePath+"','"+data[i].FK_Flow+"','"+data[i].FK_Node+"','"+data[i].WorkID+"','"+data[i].FID+",0')\" class='ui-btn ui-nodisc-icon ui-alt-icon ui-btn-icon-right ui-icon-carat-r'>";	
												str+="<h3>"+data[i].Title+"</h3>";
												$('#one .wrapper').append(str);
												$('#one .wrapper').trigger("create");
												deleteload();
											}
										}
							}else  if(data!=null && currentType=="dealed"){
										for(var i = 0;i < data.length; i++){
											var str="";
											if(currentPage < handleTotalPage || currentPage == handleTotalPage){									
													str+="<li class='ui-li-has-thumb'>";	
													str+="<a  onclick=\"javascript:locationhref('"+basePath+"','"+data[i].FK_Flow+"','"+data[i].FK_Node+"','"+data[i].WorkID+"','"+data[i].FID+",1')\" class='ui-btn ui-nodisc-icon ui-alt-icon ui-btn-icon-right ui-icon-carat-r'>";	
													str+="<h3>"+data[i].Title+"</h3>";
													str+="</a></li>";
													$('#two .wrapper').append(str);
													$('#two .wrapper').trigger("create");
												}
										}
								
							}else  if(data!=null && currentType=="compile"){
								for(var i = 0;i < data.length; i++){
									var str="";
									if(currentPage < handleTotalPage || currentPage == handleTotalPage){									
											str+="<li class='ui-li-has-thumb'>";	
											str+="<a onclick=\"javascript:locationhref('"+basePath+"','"+data[i].FK_Flow+"','"+data[i].FK_Node+"','"+data[i].WorkID+"','"+data[i].FID+",1')\" class='ui-btn ui-nodisc-icon ui-alt-icon ui-btn-icon-right ui-icon-carat-r'>";	
											str+="<h3>"+data[i].Title+"</h3>";
											str+="</a></li>";
											$('#three .wrapper').append(str);
											$('#three .wrapper').trigger("create");
										}
								}
						
					}else  if(data!=null && currentType=="emailShou"){
										$('#test_ul').append(data.data);
										$('#test_ul').trigger("create");
										deleteload();
										$("#currentPage").val(currentPage);
							}else  if(data!=null && currentType=="emailFa"){
								$.each(data.mail_outbox_list,function(key,item){
									var str="";
									if(currentPage < handleTotalPage || currentPage == handleTotalPage)
									{
										//	var str="<li class='ui-li-has-thumb'><div class='behind'><a href='#' onclick='listDel(\""+item.id+"\")' id='"+item.id+"' class='ui-btn //delete-btn'>删除</a></div><a id='"+item.id+"' //onclick='aclick(this.id)'><h3>"+item.MAIL_TITLE+"</h3><p><span>"+item.RECI_USERS+"</span>&emsp;<span>"+item.CREA_DATE+"</span></p></a></li>";
					
										str+="<li class='ui-li-has-thumb'>";	
										str+="<div class='behind'>";
										str+="<a href='#' id='"+item.ID+"' class='ui-btn delete-btn' onclick='listDel(this.id)'>删除</a>";
										str+="</div>";
										str+="<a id='"+item.ID+"' onclick='aclick(this.id)' class='ui-btn ui-nodisc-icon ui-alt-icon ui-btn-icon-right ui-icon-carat-r'>";	
										str+="<h3>"+item.MAIL_TITLE+"</h3>";
										str+="<p><span>"+item.RECI_USERS+"</span>&nbsp;";
										str+="<span>"+item.SEND_DATE+"</span></p>";
										str+="</a></li>";
										$('#test_ul').append(str);
										$('#test_ul').trigger("create");
										deleteload();
										$("#currentPage").val(currentPage);
										}
								});
							}else  if(data!=null && currentType=="emailCao"){
								$.each(data.mail_outbox_list,function(key,item){
									var str="";
									if(currentPage < handleTotalPage || currentPage == handleTotalPage)
									{
										//	var str="<li class='ui-li-has-thumb'><div class='behind'><a href='#' onclick='listDel(\""+item.id+"\")' id='"+item.id+"' class='ui-btn //delete-btn'>删除</a></div><a id='"+item.id+"' //onclick='aclick(this.id)'><h3>"+item.MAIL_TITLE+"</h3><p><span>"+item.RECI_USERS+"</span>&emsp;<span>"+item.CREA_DATE+"</span></p></a></li>";
					
										str+="<li class='ui-li-has-thumb'>";	
										str+="<div class='behind'>";
										str+="<a href='#' id='"+item.ID+"' class='ui-btn delete-btn' onclick='listDel(this.id)'>删除</a>";
										str+="</div>";
										str+="<a id='"+item.ID+"' onclick='aclick(this.id)' class='ui-btn ui-nodisc-icon ui-alt-icon ui-btn-icon-right ui-icon-carat-r'>";	
										str+="<h3>"+item.MAIL_TITLE+"</h3>";
										str+="<p><span>"+item.RECI_USERS+"</span>&nbsp;";
										str+="<span>"+item.CREA_DATE+"</span></p>";
										str+="</a></li>";
										$('#test_ul').append(str);
										$('#test_ul').trigger("create");
										deleteload();
										$("#currentPage").val(currentPage);
										}
								});
							}else  if(data!=null && currentType=="task"){
										if(currentPage < handleTotalPage || currentPage == handleTotalPage)
									{
										$('#test_ul').append(data.data);
										$('#test_ul').trigger("create");
										deleteload();
										$("#currentPage").val(currentPage);
										}
							}else{
								if(currentPage < handleTotalPage || currentPage == handleTotalPage)
									{
								$('#test_ul').append(data.data);
								$('#test_ul').trigger("create");
								deleteload();
								$("#currentPage").val(currentPage);
								}
							}
							
							
				
				}
	  		});
	});
}


//显示当前页面
function showCurrentPage(currentType,ulClass){
	var pageHtml = '<div id="pagePaganition"><span id="current"></span>/<span id="totalNumber"></span></div>';
	$("#hospage").append(pageHtml);
	var scrollTag=setInterval("$('#pagePaganition').fadeOut(500)",2000);
	$("."+ulClass).scroll(function(){
		//每页显示条数
		var currentPage,totalPage;
		if(currentType=="handle")
		{
			currentPage=$('#currentPage1').val();
			totalPage=$("#totalPage1").val();
			$("#current").text(currentPage);
			$("#totalNumber").text(Math.floor(totalPage/6)+1);
			$("#pagePaganition").fadeIn(100);
		}else if(currentType=="dealed")
		{
			currentPage=$('#currentPage2').val();
			totalPage=$("#totalPage2").val();
			$("#current").text(currentPage);
			$("#totalNumber").text(Math.floor(totalPage/6)+1);
			$("#pagePaganition").fadeIn(100);
		}else if(currentType=="compile")
		{
			currentPage=$('#currentPage3').val();
			totalPage=$("#totalPage3").val();
			$("#current").text(currentPage);
			$("#totalNumber").text(Math.floor(totalPage/6)+1);
			$("#pagePaganition").fadeIn(100);
		}else{
			currentPage=$('#currentPage').val();
			totalPage=$("#totalPage").val();
			$("#current").text(currentPage);
			$("#totalNumber").text(totalPage);
			$("#pagePaganition").fadeIn(100);
		}
	});
}

//回到顶部
function gotoTop(min_height){
    //预定义返回顶部的html代码，它的css样式默认为不显示
    var gotoTop_html = '<div id="gotoTop"><a href="#header"><i class="icon-arrow-up"></i></a></div>';
    //将返回顶部的html代码插入页面上id为page的元素的末尾 
    $("#hospage").append(gotoTop_html);
	
    $("#gotoTop").click(//定义返回顶部点击向上滚动的动画
        function(){$('.wrapper').animate({scrollTop:0},200);
    }).hover(//为返回顶部增加鼠标进入的反馈效果，用添加删除css类实现
        function(){$(this).addClass("hover");},
        function(){$(this).removeClass("hover");
    });
	
    //获取页面的最小高度，无传入值则默认为600像素
    min_height ? min_height = min_height : min_height = 600;
    //为窗口的scroll事件绑定处理函数
    $(".wrapper").scroll(function(){
        //获取窗口的滚动条的垂直位置
        var s = $(".wrapper").scrollTop();
        //当窗口的滚动条的垂直位置大于页面的最小高度时，让返回顶部元素渐现，否则渐隐
        if( s > min_height){
            $("#gotoTop").fadeIn(100);
        }else{
            $("#gotoTop").fadeOut(200);
        };
    });
};

function prevent_default(e) {
        e.preventDefault();
    }

    function disable_scroll() {
        $(document).on('touchmove', prevent_default);
    }

    function enable_scroll() {
        $(document).unbind('touchmove', prevent_default);
    }
    
    function locationhref(a,b,c,d,e,f)
    {
    	if(f==1){
    		location.href=a+"WF/MyFlow.jsp?FK_Flow="+b+"&FK_Node="+c+"&WorkID="+d+"&FID="+e+"&flag=1";
    	}else{
    		location.href=a+"WF/MyFlow.jsp?FK_Flow="+b+"&FK_Node="+c+"&WorkID="+d+"&FID="+e;
    	}
    	//location.href=a+"workflow/wf/openTask.do?WorkItemID="+b+"&EntryID="+c+"&flowTaskType="+d+"&workType="+e;
    }
    
function deleteload(){
	var x;
    $('.swipe-delete li > a').on('touchstart', function(e){
            $('.swipe-delete li > a.open').css('left', '0px').removeClass('open') // close em all
            $(e.currentTarget).addClass('open')
            x = e.originalEvent.targetTouches[0].pageX 
        })
        .on('touchmove', function(e) {
			if(e.originalEvent.targetTouches[0].pageX > x)
			return
            var change = e.originalEvent.targetTouches[0].pageX - x;
            change = Math.min(Math.max(-100, change), 100);
            e.currentTarget.style.left = change + 'px';
            if (change < -10) {disable_scroll(); }
        })
        .on('touchend', function(e) {
            var left = parseInt(e.currentTarget.style.left)
            var new_left;
            if (left < -35) {
                new_left = '-100px';
            } else {
                new_left = '0px';
            }
            
            $(e.currentTarget).animate({left: new_left}, 200)
            enable_scroll()
        });  

   
}     