$(function(){
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
	var fileId=getBusIdByWord(docId); 
	initForWord(fileId,1);
});

//初始化数据
function initForWord(fileId,type){
	
	if(type==1){
	
	if(fileId==null||fileId==""||fileId==undefined){
	    //产生一个临时的fileID
        fileId=MathRand();
        $("#mainTextId").val(fileId);  
	 }else{
	    //标示表单不是新建
	 	$("#mainTextId").val("1"); 
	 }
	 
	 }
	 
	    $.ajaxSetup({ 
		async : false
	    }); 
	    var action=basePath+"/common/file/get.do";
	    $("#toedit").empty();
		$.post(action,{"id":fileId},function(data){

			if(data.msg=="文件不存在"){
				$("#toedit").append(""+
				"<a href='javascript:void(0)' onclick='newFileForWord(\""+fileId+"\",1)'>版本编辑</a>"+
				"&nbsp;<a href='javascript:void(0)' onclick='newFileForWord(\""+fileId+"\",-1)'>非版本编辑</a>"+
		        "");
			
			}else{
				
			    var obj=JSON.parse(data.msg);
			    $("#toedit").append(""+
					"<a href='javascript:void(0)' onclick='editForWord(\""+obj.id+"\",1)'>版本编辑</a>"+
					"&nbsp;<a href='javascript:void(0)' onclick='editForWord(\""+obj.id+"\",-1)'>非版本编辑</a>"+
			    "");
			}
			
		    
		});

}

function editForWord(fileId,version){
	
	//获取用户名
    var userName=getValueForWord();
    
    var bit=InitType();
	var url="";
	if(bit=="32"){
		url=basePath + "/formdesigner/page/component/mainText/word32.jsp"
	}else{
		url=basePath + "/formdesigner/page/component/mainText/word.jsp"
	}

	var data = { "fileId" : fileId, "type" :"edit","version":version,"userName":userName};
	var width=$(document).width();
	var height=$(document).height();
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url :url,
		data : data,
		width :width,
		height:height,
		onclose : function() {
				initForWord(fileId,2);
		}
	
	}).showModal();
	
	return false;
	
	
}


function newFileForWord(fileId,version){
	
	//获取用户名
    var userName=getValueForWord();
	var bit=InitType();
	var url="";
	if(bit=="32"){
		url=basePath + "/formdesigner/page/component/mainText/word32.jsp"
	}else{
		url=basePath + "/formdesigner/page/component/mainText/word.jsp"
	}
	
	var data = { "fileId" : fileId, "type" :"newFile","version":version,"userName":userName};
	var width=$(document).width();
	var height=$(document).height();
	top.dialog({
		id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
		title : '载入中...',
		url :url,
		data : data,
		width :width,
		height:height,
		onclose : function() {
				initForWord(fileId,2);
		}
	
	}).showModal();
	
	return false;
	
	
}




function getValueForWord(){

		var strVal="";
		var action=basePath+"/fd/mainText/getUser.do";
		$.ajax({
			  type: "POST",
			  async: false,
		      url: action,
			  success: function(data){
					strVal=data.name;
			  }	
			});
		return strVal;
}
	
function InitType(){
	var str="";
	if(window.navigator.platform == "Win32"){
		str="32";
	}else{
		str="64";
	}
	return str;
}


//获取业务id
function getBusIdByWord(docId){

		var busessid="";
		if(docId!=""){
			$.ajaxSetup({ 
			async : false
		    });
		    var action = basePath+"document/getDocumentById.do?documentid="+docId; 
	    	$.get(action,function(data){
		 		busessid=data.recordId;
		 	});
		    
		    return busessid;
		}
	
}

//生成随机数

function MathRand(){ 
	var Num=""; 
	for(var i=0;i<6;i++){ 
	Num+=Math.floor(Math.random()*10); 
	} 

	return Num;
}

function saveMainText(busId,type){
	
	var id=$("#mainTextId").val();
	//获取业务ID
	busId=getBusIdByWord(busId);
	//新建正文需把临时ID修改成业务ID
	if(id!="" && id!="1"){
		var action=basePath+"/fd/mainText/save.do?id="+id+"&busId="+busId;
		
		$.ajaxSetup({ 
			async : false
		});
	    $.get(action,function(data){
			//发送不执行刷新
		 	if(data!=null && type==1){
		 		initForWord(busId,1);
		 	}
		 	
		});
		
	  }else{
	  	return true;
	  }

}

