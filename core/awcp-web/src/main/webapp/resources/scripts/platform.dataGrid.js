/**
 * Created by nsccsz-hc
 */
(function($){	
    $.fn.extend({
        dataGrid:function(options){
        	var defaults = {
        			url:"",
                    data:{pageSize:10},
                    sortable:false
                };
            var options = $.extend(defaults,options);
            return this.each(function(){
            	var $body = $("body"),
            		$obj=$(this),
            		$gridtable=$obj.find('table.gridtable'),//获取表格
            		$page = $obj.find(".datapager"),//获取分页栏
            		$th = $gridtable.find("th");//获取表头th
                var tdLength = $th.length-1;
                var getPageInfo={};
                var items={
                    page:function(currentPage,totalPage,total){
                        $page.find(".currentpage").text(currentPage);
                        $page.find(".totalpage").text(totalPage);
                        $page.find(".total").text(total);
                        getPageInfo.currentPage=parseInt($page.find(".currentpage").text());
                        getPageInfo.totalPage=parseInt($page.find(".totalpage").text());
                        getPageInfo.total=parseInt($page.find(".total").text());
                        $page.find(".direction a").removeClass("disabled");
                        if (totalPage==1) {
                            $page.find(".direction a").addClass("disabled");
                        }else if(currentPage==1){
                            $page.find("a.backward,a.left").addClass("disabled");
                        }else if(currentPage == totalPage){
                            $page.find("a.forward,a.right").addClass("disabled");
                        }else return;                       
                    },
                    load:function(a){
                        //var a = (a==undefined)?options.data:a;
                    	//$obj.find(".datatable-rows").html("loading……");
                        $.post(options.url,options.data,function(data){             
                            /**creat table**/
                            var result=data.data;
                            var row = "";
                            var firstTD = "<td class='hidden'>"+$th.eq(0).html()+"</td>";
                            $.each(result,function(index,item){
                                row += "<tr id='"+item.ID+"'>"+firstTD;
                                for (var i = 1; i <= tdLength; i++) {
                                    row += "<td>"+item[$th.eq(i).attr('field')]+"</td>";
                                };
                                row += "</tr>";
                            });
                            $gridtable.find("tbody").html(row);
                            $gridtable.datatable("load");
                            /**set value to pageBanner**/
                            items.page(data.currentPage,data.totalPage,data.total);
                        },"json");
                    }
                }; 
                if (options.sortable) {
                    $gridtable.find("thead th:not(.hidden)").addClass("sort").append("<i class='icon-sort'></i>");
                }             
                $gridtable.datatable({
                    checkable: true,
                    checksChanged:function(event){                     
                        
                        this.$table.find("tbody tr").find("input").removeAttr("name");
                        var checkArray = event.checks.checks;
                        var count = checkArray.length;//checkbox checked数量
                        this.$table.data("ids",checkArray.toString());
                        for(var i=0;i<count;i++){//给隐藏数据集上name属性
                          this.$table.find("tbody tr").eq(checkArray[i]).find("input").each(function(){
                              var _this = $(this);
                              _this.attr("name",_this.attr("id"));
                          });
                        }
                    }             
                });
                items.load();                
               /**分页栏**/ 
                $page.find(".direction a").on("click",function(){//分页
                    var disabled = $(this).hasClass("disabled");
                    var type = $(this).attr("class");
                	if(!disabled){
                        switch(type){
                            case "backward":options.data.currentPage=1;items.load();break;
                            case "forward":options.data.currentPage=getPageInfo.totalPage;items.load();break;
                            case "left":options.data.currentPage=(getPageInfo.currentPage-1);items.load();break;
                            case "right":options.data.currentPage=(getPageInfo.currentPage+1);items.load();break;
                            default:return false;
                        }                         
                    }else{
                        return false;
                    }
                });
                $page.find(".showSize").on("change",function(){//条数
                    options.data.pageSize = $(this).val();
                    options.data.currentPage = 1;
                    items.load();
                    return true;
                });
                $page.find(".showPage").on("blur",function(){//页面跳转
                    var val = $(this).val();
                    var reg = /^\+?[1-9]\d*$/;
                    if(val==""){
                        return false;
                    }else if(reg.test(val)&&val<=getPageInfo.totalPage){
                    	options.data.currentPage = val;
                        items.load();
                    }else{
                        alertMessage("请输入不超过最大页码的正整数！");
                        $(this).val("");
                    }
                });
                /**排序**/
                if (options.sortable) {//排序必须参数：sort,order(asc\desc)
                    $obj.on("click",".sort",function(){
                        var _this = $(this)
                        var icon = _this.find("i");
                        var iconClass = icon.attr("class");
                        var pIcon = $th.eq(_this.data("index"));
                        var sort = pIcon.attr("field");
                        options.data.sort = sort;//排序类型
                        if(iconClass=="icon-caret-down"){//升序
                          pIcon.find("i").attr("class","icon-caret-up");
                          options.data.order = "asc";
                        }else{//降序
                          pIcon.find("i").attr("class","icon-caret-down");
                          options.data.order = "desc";
                        }
                        items.load();
                    });                        
                };

            });           
        }
    });
})(jQuery);