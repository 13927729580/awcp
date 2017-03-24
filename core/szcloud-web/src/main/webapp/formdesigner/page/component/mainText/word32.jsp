<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>菜单编辑页面</title>
<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
</head>
	<body id="main">
	
	<div class="row" id="buttons">
		<!--<button type="button" class="btn btn-info" onclick="create()"/><i class="icon-trash"></i>新建</button>-->
		<button type="button" class="btn btn-success" onclick="save()"><i class="icon-plus-sign"></i>保存</button>
		<input type="hidden" name="fileId" id="fileId" value=""/>
		<input type="hidden" name="type" id="type" value=""/>
		<input type="hidden" name="userName" id="userName" value=""/>
		<input type="hidden" name="version" id="version" value=""/>
	</div>
	
		 <div>
            <object id="TANGER_OCX" classid="clsid:C39F1330-3322-4a1d-9BF0-0BA2BB90E970" 
            codebase="<%=basePath%>resources/plugins/ntfo/officecontrol/ofctnewclsid.cab#version=5,0,2,2" width="100%" height="600px" > 
			<param name="IsUseUTF8URL" value="-1"> 
			<param name="IsUseUTF8Data" value="-1"> 
			<param name="BorderStyle" value="1"> 
			<param name="BorderColor" value="14402205"> 
			<param name="TitlebarColor" value="15658734"> 
			<param name="isoptforopenspeed" value="0"> 
			<param name="ProductCaption" value="深圳市科技创新委员会"> 
			<param name="ProductKey" value="CB47DC13EBA4551F93ACC051EEA97B902C873B99"> 
			<param name="TitlebarTextColor" value="0"> 
			<param name="MenubarColor" value="14402205"> 
			<param name="MenuButtonColor" value="16180947"> 
			<span STYLE="color:red"><br />不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span> 
			</object>
    	 </div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<!--<script type="text/javascript" src="<%=basePath%>resources/plugins/ntfo/officecontrol/ntkoofficecontrol.js"></script>-->		
		
		<script type="text/javascript">
		
		$('document').ready(function(){
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			var data = null;//dialog.data;
			if(dialog != null){
				data = dialog.data;
				if(data.fileId){
					$("#fileId").val(data.fileId);
				}
				if(data.type){
					$("#type").val(data.type);
				}
				
				if(data.userName){
					$("#userName").val(data.userName);
				}
				
				if(data.version){
					
					$("#version").val(data.version);
				}
				
			}	
			/*
			if(dialog != null){
				dialog.height(1027); 
				dialog.width(768);
			}*/
			
			if($("#type").val()=='edit'){
			   var fileId=$("#fileId").val();
			   var userName=$("#userName").val();
			   if(fileId!=null&&userName!=null){
			   	var activeX = document.getElementById("TANGER_OCX");
		        var action="<%=basePath%>common/file/download.do?fileId="+fileId;
		        activeX.OpenFromURL(action,false);
		        	
		        activeX.TrackRevisions(true);
		        activeX.WebUserName=userName;
		        
		        if($("#version").val()=='-1'){
		            activeX.ActiveDocument.ShowRevisions=false;
		        	activeX.TrackRevisions=false;
		        }
		        activeX.FileSave =false;
		        //activeX.FullScreenMode=true;
			   }else{
			   	alert("用户或表单不存在");
			   }
			   
			}
			
			
			if($("#type").val()=='newFile'){
			   var fileId=$("#fileId").val();
			   var userName=$("#userName").val();
			   if(fileId!=null&&userName!=null){
			   	var activeX = document.getElementById("TANGER_OCX");
		   		activeX.CreateNew('Word.Document');
		        
		        activeX.TrackRevisions(true);
		        activeX.WebUserName=userName;
		        
		        if($("#version").val()=='-1'){
		        	activeX.ActiveDocument.ShowRevisions=false;
		        }
		        
		        activeX.FileSave =false;  
		        //activeX.FullScreenMode=true; 
			   }else{
			   	alert("用户或表单不存在");
			   }
			   
			}
			
			});
			
			
			function save(){
			var fileId=$("#fileId").val();
			if(fileId!=null){
				var activeX = document.getElementById("TANGER_OCX");
		       	var action="<%=basePath%>common/file/upload_word.do?fileName="+fileId;
		        var result=activeX.SaveToURL(action,"fileName");
			    var obj=JSON.parse(result);
			    if(obj.flag==1){
					alert("保存成功");
			    }
			}
		    
	       }

	       function create(){
		   var activeX = document.getElementById("TANGER_OCX");
		   activeX.CreateNew('Word.Document');
	       } 
			
        </script>	
	</body>
</html>
