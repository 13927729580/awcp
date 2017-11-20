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
            <object id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" 
            codebase="<%=basePath%>resources/plugins/ntfo/OfficeControl.cab#version=5,0,2,4" width="100%" height="600px" > 
			<param name="IsNoCopy" value="1"> 
			<param name="BorderStyle" value="1"> 
			<param name="BorderColor" value="14402205"> 
			<param name="TitlebarColor" value="14402205"> 
			<param name="TitlebarTextColor" value="0"> 
			<param name="Caption" value="Office正文在线编辑"> 
			<param name="IsShowToolMenu" value="-1"> 
			<param name="IsHiddenOpenURL" value="0"> 
			<param name="MakerCaption" value="中国兵器工业信息中心通达科技"> 
			<param name="MakerKey" value="86A1BE7CFEC466B6186E5425AFABB417B6016D68"> 
			<param name="ProductCaption" value="Office Anywhere 3.0"> 
			<param name="ProductKey" value="65F867689D7FF5CD02FBE4AA23764306F715A9E2"> 
			<span STYLE="color:red"><br />不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span> 
			</object> 
    	 </div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>	
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
