$("input[data-input=select]").each(function(){//用户地址选择组件
	  			var _this = $(this);
	  			var id=_this.attr("id");
	  			_this.hide().after('<div class="chosenName text-ellipsis form-control" style="cursor:text;position: relative;color:#999">请选择</div>');
	  			_this.parent().on("click",".chosenName",function(){
	  				var _obj = $(this);
	  				var postData={};
					postData.id=id;
	  				top.dialog({
						title: "选择框",
						url:basePath+"formdesigner/page/component/dynamicSelect/dynamicChosen.jsp",
						id:id,
						data:postData,
					    onclose: function () {
					    	var value=this.returnValue;
					    	var memberIDs=[];
					    	var memberNames=[];
					        this.title('提交中…');
					        if(value!=""){
					        	$.each(value,function(i,n){
					        		memberIDs.push(i);
					        		memberNames.push(n);
					        	})
					        	_this.val(memberIDs.join(","));
					        	if(!$.isEmptyObject(value)){
					        		_obj.html(memberNames.join(","));
					        		_obj.append("<i class='icon-remove' style='position:absolute;top:8px;right:10px;z-index:99;cursor:pointer'></i>");
					      		}else{
					      			_obj.html("请选择");
					      		}
					      	}
					      	return false;			        
					    }
					}).width(620).showModal();
	  			});
	  			_this.parent().on("click",".icon-remove",function(){
	  				_this.val("");
	  				$(this).parent().html("请选择");
	  				return false;
	  			})

	  		});