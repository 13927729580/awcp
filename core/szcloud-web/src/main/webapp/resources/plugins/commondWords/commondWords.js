
function openDialog(pageId,type){
	//var text=$("#span"+pageId).html();
	var text=document.getElementById("suggestion").value;
	if(text!=undefined&&text!=""){
	    var html="<div id='build' title='新建文件夹' style=''><form id='newFolder' action='' ><textarea id='signContent'  style='width:360px;height:120px;border:#ccc 1px solid;line-height:32px;font-size:14px;' name='foldername'>"+text+"</textarea></form></div>";
	 }else{
	 	var html='<div id="build" title="新建文件夹" style=""><form id="newFolder" action="" ><textarea id="signContent"  style="width:360px;height:120px;border:#ccc 1px solid;line-height:32px;font-size:14px;" name="foldername"></textarea></form></div>';
	 }
	 top.dialog({
		id: 'myWorkFlow',
    	title: '审批意见',
    	content:html,
		height: '120px',
    	width: '360px',
		okValue: '确定',
		ok:function(){
		  if(top.$("#signContent").val()==""){
		     alert('审批意见不能为空');
			 return false;
		  }
		//数据保存处理
		 $("#span"+pageId).remove();
		 $("#"+pageId).append(""+
			 "<span class='txtgray' id='span"+pageId+"' style='float:left'>你的意见:"+top.$("#signContent").val()+""+
			 "<i class='icon-remove text-danger' onClick='deleteItem(\""+pageId+"\")' style='margin-left:8px;cursor:pointer;'></i>"+
			 "</span>"+
		 "")
		
		$("#suggestion").val(top.$("#signContent").val());
		$("#suggestionType").val(type);
		//保存pageid
		
		$("#commonType").val(pageId);
		
		  },
              cancelValue: '取消',
	          cancel: function () {
	            /*
                $("#suggestion").val("");
				$("#suggestionType").val("");
				var is=$("#span"+pageId);
				if(is!=undefined){
				 is.remove();
				}
                */
          }

	}).showModal();	
	        
}

//取消输入的意见

function deleteItem(id){

        $("#suggestion").val("");
	    $("#suggestionType").val("");
	    $("#commonType").val("");
	    var is=$("#span"+id);
	    if(is!=undefined){
		is.remove();
		}
}

//删除意见
function deleteText(id,pageId){
	   
	   $.ajaxSetup({ 
			async : false
		    });
		    var url = basePath+"commondWords/delete.do?id="+id+""
	    	$.get(url,function(data){
		 		
		 		if(data!=null){
		 		   $("#suggestion").val("");
	    		   $("#suggestionType").val("");
	    		   $("#commonType").val("");
		 		   initCommondWords(pageId);
		 		}
		 		
		 	});
		
}

//删除意见
function deleteText(id){
	   
	   $.ajaxSetup({ 
			async : false
		    });
		    var url = basePath+"commondWords/delete.do?id="+id+""
	    	$.get(url,function(data){
		 		
		 		if(data!=null){
		 		   $("#suggestion").val("");
	    		   $("#suggestionType").val("");
	    		   $("#commonType").val("");
	    		   return false;
		 		}
		 		
		 	});
		
}


//获取当前环节id
function getActivityId(){
	 var activityId="00";
	 var workId=parent.frames["main"].document.getElementById("WorkItemID").value;
     var EntryID=parent.frames["main"].document.getElementById("EntryID").value;
     if(workId!=""&EntryID!=""){
     	var url = basePath+"commondWords/getActivityID.do?EntryID="+EntryID+"&workId="+workId;
	    $.ajaxSetup({ 
		async : false
	    }); 
	 $.get(url,function(data){
		 activityId=data.flag;
		
	 });
	}
	
	return activityId;
}


//获取流程状态
function getStatus(){
	 var activityId="00";
	 var workId=parent.frames["main"].document.getElementById("WorkItemID").value;
     var EntryID=parent.frames["main"].document.getElementById("EntryID").value;
     if(workId!=""&EntryID!=""){
     	var url = basePath+"commondWords/getActivityID.do?EntryID="+EntryID+"&workId="+workId;
	    $.ajaxSetup({ 
		async : false
	    }); 
	 $.get(url,function(data){
		 activityId=data.type;
		
	 });
	}
	
	return activityId;
}

//创建意见框
function createBox(pageId){

	var url = basePath+"document/getStoreById.do?storeId="+pageId;
	$.ajaxSetup({ 
		async : false
	}); 
    $.get(url,function(data){
      
      var obj = JSON.parse(data.content);
      var activityId =getActivityId();
      //如果流转结束修改button状态
      var status=getStatus();
      if(status!="00"&&status=="1"){
      	//$("#but"+pageId).attr("disabled","disabled");
      	$("#but"+pageId).remove();
      }
      	if(activityId!="00"){
	    var str="flowNodes"+activityId;
		var flow=obj[str];
		if(flow==null||flow==""){
			
		 	 $("#suggessBody"+pageId).remove();
		    }
	     }else{

			var flow2=obj["flowNodes1"];
			
			if(flow2!=null&&flow2!=""){
				//新建表单

			}else{
	
				 $("#suggessBody"+pageId).remove();
			}
	     
	     }
      
    },"json");    

}



//初始化意见内容
function initCommondWords(pageId){
	
	var status=getStatus();
	
	$.ajaxSetup({ 
		async : false,
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		cache: false
	});
	
	var workId=parent.frames["main"].document.getElementById("WorkItemID").value;
    var EntryID=parent.frames["main"].document.getElementById("EntryID").value;
	
	var busessid=getBusId();
	var type="0";
	var nodes=[];
	var url = basePath+"document/getStoreById.do?storeId="+pageId;
    $.get(url,function(data){
      var obj = JSON.parse(data.content);
     
      for(var key in obj)
	  {
      	if(key.indexOf('flowNodes')!='-1'){
      		nodes.push(key);
      	}
      	if(key.indexOf('freeFlow')!='-1'){
      		 type=obj["showType"];
      	}
	  }
    },"json"); 

	if(busessid!=""&&busessid!=undefined&&nodes.length>0){
	var urla = basePath+"commondWords/findTextByWorkId.do?workId="+workId+"&EntryID="+EntryID+"&type="+type+"&nodes="+nodes+"&busId="+busessid;
	var codeUrl=encodeURI(urla)
	 $.get(codeUrl,function(data){
	     $("#"+pageId).empty();
	    
		 $.each(data,function(i,item){
		 	 var date=item.date;
		 	 var date_=new Date(date);
		 	 var str="";
		 	 if(item.flag=="11"){
		 	 	parent.frames["main"].document.getElementById("suggestion").value=item.conment;
		 	 	parent.frames["main"].document.getElementById("suggestionType").value=item.type;
		 	 }
		 	 
		 	 //当前用户未发送的意见
		 	 if(item.flag=="11" && item.status=="1" && status!="00" && status=="0"){
			   
			   var objValue="";
			   
			   try{
			   
			   objValue=parent.frames["main"].document.getElementsByName("yoursuggestion");
		 	   objValue[0].value=item.conment;
			   
			   }catch(e){
			   
			   }
		 	   
		 	   parent.frames["main"].document.getElementById("commonType").value=item.id;
		 	   
		 	    
		 	 }else{
		 	 	str= "<span class='txtgray' style='float:left'>["+date_.Format("yyyy-MM-dd hh:mm")+"]"+item.person+" 意见: "+item.conment+"</span></br>";
		 	 }
		 	 
			 $("#"+pageId).append(""+
			 ""+str+""+
			 "")
		 });
	 });
	
	}
	
}

//获取业务id
function getBusId(){
    
    var docId="";
    try{
		if((parent.frames["main"].document.getElementById("docId")!=null) || (parent.frames["main"].document.getElementById("docId")!="undefined"))
		{
		docId=parent.frames["main"].document.getElementById("docId").value;
		}
		else
		{
		docId= document.getElementById("docId").value;
		}
	}catch(e){
		//window.parent.parent.document.getElementById("docId").value;
	}	
	
	
	//var docId=parent.frames["main"].document.getElementById("docId").value;
		var busessid="";
		if(docId!=""){
			$.ajaxSetup({ 
			async : false
		    });
		    var url = basePath+"document/getDocumentById.do?documentid="+docId; 
	    	$.get(url,function(data){
		 		busessid=data.recordId;
		 	});
		    
		    return busessid;
		}
	
}


//获取业务id保存方法用
function getBusIdBySave(id){
	var docId=id;
		var busessid="";
		if(docId!=""){
			$.ajaxSetup({ 
			async : false
		    });
		    var url = basePath+"document/getDocumentById.do?documentid="+docId;
	    	$.get(url,function(data){
		 		busessid=data.recordId;
		 	});
		    
		    return busessid;
		}
	
}


function saveText(obj,saveType){
		
		$.ajaxSetup({ 
		async : false,
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		cache: false
		});

		
		var docId=obj.docId;
		//如果type为0则意不分类别
		var type=document.getElementById("suggestionType").value;
		
		var id=document.getElementById("commonType").value;

		
		var busessid=getBusIdBySave(docId);
        var workId=obj.WorkItemID;
        var EntryID=obj.EntryID;
        var objValue="";
        var content="";
        try{
          objValue=parent.frames["main"].document.getElementsByName("yoursuggestion");
          content=objValue[0].value;
        }catch(e){
          
        }
        
        if(content==""||content==null && id!=null && id!=""){
           //如果内容为空并且存在id则执行删除操作
           deleteText(id);
        }
       
	    if(workId!=""&&workId!=undefined&&EntryID!=undefined&&EntryID!=""&&busessid!=""&&busessid!=undefined&&content!=null&&content!=""){
	    var urlb = basePath+"commondWords/save.do?id="+id+"&status="+saveType+"&workId="+workId+"&EntryID="+EntryID+"&busessid="+busessid+"&content="+content;
	    var encodeUrl=encodeURI(urlb)
	    $.get(encodeUrl,function(data){
		 	if(data!=null){
		 		//刷新数据
		 		if(saveType==1){
		 			objValue[0].value=data.conment;
		 			parent.frames["main"].document.getElementById("commonType").value=data.id;
		 		}
		 	
		 	}
		 	
		  });
          }

}


//格式化日期
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

$(function(){
	
	
	try{
	
		$("textarea[name$='yoursuggestion']").keyup(function(){
          var objValue=parent.frames["main"].document.getElementsByName("yoursuggestion");
          parent.frames["main"].document.getElementById("suggestion").value=objValue[0].value;
	    });
	
	}catch(e){
	
	}
	

});

