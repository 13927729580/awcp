$("input[data-input='chosenSqlUser']").each(function(){			
	var _this = $(this);
	var name = _this.attr("name");//获取当前组件name属性
	var value = _this.val();
	/*如果默认有值的话，回显当前所选用户*/
	if(value!=""){
		$.ajax({
			type:'post',
			url:basePath + 'unit/getNamesByUserIds.do',
			data:{userIds:value},
			dataType:"html",
			async:'false',
			success:function(data){
				_this.next().val(data);
				_this.hide();				
			}
		});
	}else{
	   _this.next().val("请选择用户");
	   _this.hide();
	}
	_this.parent().on("click",".chosenSqlUserName",function(){
		var _obj = $(this);
		var sql = $(this).prev().attr("title");
		top.dialog({
			title: "用户选择框",
			url:basePath+"formdesigner/page/component/userSelect/chosenSqlUserNames.jsp?datas="+_this.val()+"&sql="+sql,
			id:name,
			onclose: function () {
				var value=this.returnValue;
				var memberIDs=[];
				var memberNames=[];
				this.title('提交…');
				if(value!=""){
					$.each(value,function(i,n){
						memberIDs.push(i);
						memberNames.push(n);
					})
					_this.val(memberIDs.join(","));
					if(!$.isEmptyObject(value)){
						_obj.val(memberNames.join(","));
						_obj.append("<i class='icon-remove' style='position:absolute;top:8px;right:10px;z-index:99;cursor:pointer'></i>");						
					}else{
						_obj.val("请选择用户");
					}
				}
				return false;			        
			}
		}).width(1000).showModal();
	});
	_this.parent().on("click",".icon-remove",function(){
		_this.val("");
		$(this).parent().val("请选择用户");
		return false;
	})

});
	  		
	  		
	  		
	  		
//人员计数
function userCount(data){

	var url="";
    url = basePath+"common/user/userCount.do";
    var type=document.getElementById("slectsUserIds").value;
    if(type!=null && type!="" && type=="10"){
    	url = basePath+"common/user/groupCount.do";
    }
    var data = {"user":data};
    
    if(url!=""){
    $.post(url,data,function(data){
    	if(data!=null&&data!=""){
    		parent.frames["main"].document.getElementById("slectsUserIds").value="";
    		return true;
    		
    	}
    },"json"); 
    
    }

};
