// JavaScript Document


$(document).bind("click",function(){
	if($('.btn-close').size()>0){
	$('.btn-close').popover('hide');
	}
})

function InitQuickReply(pageId){
	

					$("#note"+pageId).popover({
						delay: { show: 500, hide: 100 },
						html:true,
						title:'常用意见<a href="javascript:void(0);" onClick="QuickReplySetting(\''+pageId+'\')" class="pull-right"><i class="icon-cog"></i>修改</a>',
						placement:'top',
						trigger:'click',
						content:function(){
						
							  //ajax获取自定义审批设置
							  var ReplyHtml = '<style>#selectList a{text-overflow:ellipsis; word-wrap: break-word;word-break: break-all;'+
	                          'white-space: nowrap;width: 100%;display: inline-block;overflow: hidden;vertical-align: middle;}'+
                              '</style><nav class="menu" data-toggle="menu" style="width: 260px" id="selectList"><ul class="nav nav-primary">';
								
								$.ajax({
	  								type:'get',
	  								url:basePath+'commondWords/getNoteAll.do',
	  								async:false,
	  								cache: false,
            						success:function(data){
									if(data!=null&&data!=""){
									var jsonData=data;
									$.each(jsonData,function(i,item){
									ReplyHtml +='<li><a href="javascript:void(0);" onClick=DataToReplyForm(\''+item.conment+'\',\''+pageId+'\')> '+item.conment+'</a></li>';
									});
									ReplyHtml +='</ul></nav>';
									}else{
									ReplyHtml +='<li>暂无快捷审批意见，请设置</li></ul></nav>';
									}
									
									}
									
									});
									
									return ReplyHtml;
			                    }
			
						});

	
}

function DataToReplyForm(ReplyTitle,pageId){
	var CurrentDate = new Date();
	$("#"+pageId).val("");
	$("#"+pageId).val($("#"+pageId).val()+'['+CurrentDate.toLocaleString()+']'+ReplyTitle);
	$('#note'+pageId).popover('hide');
}


function QuickReplySetting(pageId){

 var contentHtml = '<style>.pull-right{margin-right:4px;}#noteList span{text-overflow:ellipsis; word-wrap: break-word;word-break: break-all;white-space: nowrap;'+
	                             'width: 70%;display: inline-block;overflow: hidden;vertical-align: middle;}'+
                                  '</style><script src='+basePath+'resources/scripts/defineNote.js type="text/javascript"></script>'+
	                              '<div class="panel"><div class="panel-heading">已设置</div><div class="panel-body" style="height:290px;overflow-y:auto;">'+
			                      '<nav class="menu" data-toggle="menu">'+
  								  '<ul class="nav nav-primary" id="noteList">';
$.ajax({
	  		type:'get',
	  		url:basePath+'commondWords/getNoteAll.do',
	  		async:false,
	  		cache: false,
            success:function(data){
				if(data!=null&&data!=null){
					var jsonData=data;
					$.each(jsonData,function(i,item){
					contentHtml+='<li id='+item.id+'><a href="javascript:void(0)"><button class="btn btn-mini btn-danger pull-right" onClick="deleteNoteItem(\''+item.id+'\',\''+basePath+'\')"><i class="icon icon-trash"></i> 删除</button> '+
												'<button class="btn btn-mini btn-success pull-right" onClick="modifyNoteItem(\''+item.id+'\',\''+item.conment+'\',\''+basePath+'\')"><i class="icon icon-pencil"></i> 修改</button><i class="icon-copy"></i><span> '+item.conment+'</span></a></li>';
					})
				contentHtml+='</ul></nav></div></div>'	;
				contentHtml+='<div class="input-group">';
            	contentHtml+='<input type="text" class="form-control" placeholder="请输入快捷审批意见" id="addInput">';
            	contentHtml+='<span class="input-group-btn">';
              	contentHtml+='<button class="btn btn-success" type="button" onClick="addNoteItem(\''+basePath+'\')"><i class="icon-plus"></i> 添加</button>';
            	contentHtml+='</span>';
          		contentHtml+='</div>';
				top.dialog({
					id: 'reply-dialog',
					title: '修改-常用意见',
					width:'600',
					height:'400',
					content:contentHtml,	
					okValue: '确定',
					ok:function(){	
                    		
   
					  },
					 cancelValue: '取消',
					 cancel: function () {
							 
					  }		
					}).showModal();

				 }},error:function(){
					alert("网络异常，请稍后再试！");
					}
						
			})
	
			parent.$("#note"+pageId).popover('hide');
				
}

function addNoteItem(basePath){
	
	if($("#addInput").val()==null||$("#addInput").val()==""){
			alert("请输入内容");
			return false;
	}
	
	
	$.ajax({
	  		type:'post',
	  		url:basePath+'commondWords/addNote.do',
	  		async:false,
	  		data:'str='+$("#addInput").val(),
            success:function(data){
				if(data){
					alert('添加成功！');
					if(data!=null){
					   newItemHtml ='<li id='+data.id+'><a href="javascript:void(0)"><button class="btn btn-mini btn-danger pull-right" onClick="deleteNoteItem(\''+data.id+'\',\''+basePath+'\')"><i class="icon icon-trash"></i> 删除</button> <button class="btn btn-mini btn-success pull-right" onClick="modifyNoteItem(\''+data.id+'\',\''+$("#addInput").val()+'\',\''+basePath+'\')"><i class="icon icon-pencil"></i> 修改</button><i class="icon-copy"></i><span> '+$("#addInput").val()+'</span></a></li>';		
					   $("#noteList").append(newItemHtml);
					}
					
				}
			},error:function(){
					alert("添加失败，请稍后再试！");
					var newItemHtml;
			}
	})
}

function modifyNoteItem(noteID,noteContent,basePath){
	var modifyHtml='<div id="modContent"  style=""><form id="newContent" action="" ><textarea id="noteContent" style="width:360px;height:120px;border:#ccc 1px solid;line-height:32px;font-size:14px;" name="noteName">'+noteContent+'</textarea></form></div>';
	top.dialog({
		id: 'myNoteID',
    	title: '修改审批意见',
    	content:modifyHtml,
		height: '120px',
    	width: '360px',
		okValue: '确定',
		ok:function(data){
			     var newContent = top.$("#noteContent").val();
		    	 if(top.$("#noteContent").val()==""){
		     		alert('审批意见不能为空');
			 		return false;
		  		 }
		         $.ajax({
						type:'post',
						url:basePath+'commondWords/editNote.do',
						data:'id='+noteID+'&str='+top.$("#noteContent").val()+'',
						async:false,
						success:function(data){
							if(data){
								alert('修改成功！');
								if(data!=null){
								$("#"+data.id).remove();
					   			newItemHtml ='<li id='+data.id+'><a href="javascript:void(0)"><button class="btn btn-mini btn-danger pull-right" onClick="deleteNoteItem(\''+data.id+'\',\''+basePath+'\')"><i class="icon icon-trash"></i> 删除</button> <button class="btn btn-mini btn-success pull-right" onClick="modifyNoteItem(\''+data.id+'\',\''+data.conment+'\',\''+basePath+'\')"><i class="icon icon-pencil"></i> 修改</button><i class="icon-copy"></i><span> '+data.conment+'</span></a></li>';		
					   			$("#noteList").append(newItemHtml);
								}
								
							}
						},error:function(){
								alert("修改失败，请稍后再试！");
								//如果成功，更新页面，放在error只为演示使用
								$("#note"+noteID+" span").text(newContent);
						}
				})
		 
		          
		  },
              cancelValue: '取消',
	          cancel: function () {
                 
          }

	}).showModal();	
	
}

function deleteNoteItem(noteID,basePath){
	$.ajax({
	  		type:'get',
	  		url:basePath+'commondWords/delNote.do',
	  		data:'id='+noteID,
	  		async:false,
            success:function(data){
				if(data.flag==00){
					$("#"+noteID).remove();
					
					alert('删除成功！');
				}
			},error:function(){
					//如果成功，删除记录，放在error只为演示使用
					$("#"+noteID).remove();
					alert("删除失败，请稍后再试！");
					
			}
	})
			
}