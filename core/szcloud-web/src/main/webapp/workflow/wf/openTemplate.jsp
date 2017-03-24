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
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>动态表单编辑页面</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="buttons">
			<button class="btn btn-success" id="save" ><i class="icon-save"></i>保存</button>
				<button class="btn btn-success" id="send" ><i class="icon-save"></i>发送</button>
				<button class="btn btn-info " id="view"><i class="icon-zoom-out"></i>查看状态</button>
				<button class="btn btn-warning" id="back"><i class="icon-backward"></i>返回</button>
			</div>
			<form class="form-horizontal" id="manuList" method="post">
				<div class="row" id="table">
					<input id="id" type="hidden" value="${workItemID}">
					
					<!--公文表单：收文处理--><!--表格：ID=1;Code=Tabulation1;Name=表格1;-->
<TABLE id=tabForm_Tabulation1 style="HEIGHT: 45px; WIDTH: 691px" borderColor=#000000 cellSpacing=0 borderColorDark=#ffffff cellPadding=0 borderColorLight=#000000 border=0>
<TBODY>
<TR height=1>
<TD width=691></TD></TR>
<TR id=tabForm_TabCell_1_1>
<TD id=tabForm_TabCell_1_1_1 height=23 width=691 align=center><LABEL id=Form1Cell1 class=TextD style="FONT-SIZE: 16pt; FONT-FAMILY: 宋体; COLOR: #ff0000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell1">文件处理表</LABEL> </TD></TR>
<TR id=tabForm_TabCell_1_2>
<TD id=tabForm_TabCell_1_2_1 height=22 width=691 align=center>&nbsp;&nbsp;&nbsp;&nbsp; </TD></TR></TBODY></TABLE><!--表格：ID=2;Code=Tabulation2;Name=表格2;-->
<TABLE id=tabForm_Tabulation2 style="HEIGHT: 82px; WIDTH: 694px" borderColor=#000000 cellSpacing=0 borderColorDark=#ffffff cellPadding=0 borderColorLight=#000000 border=1>
<TBODY>
<TR height=1>
<TD width=106></TD>
<TD width=179></TD>
<TD width=69></TD>
<TD width=85></TD>
<TD width=94></TD>
<TD width=161></TD></TR>
<TR id=tabForm_TabCell_2_1>
<TD id=tabForm_TabCell_2_1_1 height=36 width=106 align=center><LABEL id=Form1Cell59 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell59">文件名称</LABEL> </TD>
<TD id=tabForm_TabCell_2_1_2 height=36 width=588 colSpan=5 align=center><TEXTAREA id=Form1Cell60 title=文件名称[只读] class=TextD style="FONT-SIZE: 12pt; HEIGHT: 30px; FONT-FAMILY: 宋体; WIDTH: 585px; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff"   name=Form1Cell60></TEXTAREA> </TD></TR>
<TR id=tabForm_TabCell_2_2>
<TD id=tabForm_TabCell_2_2_1 height=23 width=106 align=center><LABEL id=Form1Cell9 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell9">收文日期</LABEL> </TD>
<TD id=tabForm_TabCell_2_2_2 height=23 width=179 align=center><INPUT id=Form1Cell15 title="收文日期[只读] 日期格式：YYYY-MM-DD" class=TextC style="FONT-SIZE: 12pt; HEIGHT: 20px; FONT-FAMILY: 宋体; WIDTH: 176px; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff"  > </TD>
<TD id=tabForm_TabCell_2_2_3 height=23 width=69 align=center><LABEL id=Form1Cell61 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell61">份数</LABEL> </TD>
<TD id=tabForm_TabCell_2_2_4 height=23 width=85 align=center><INPUT id=Form1Cell67 title=份数[只读] class=TextD style="FONT-SIZE: 12pt; HEIGHT: 20px; FONT-FAMILY: 宋体; WIDTH: 81px; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff"   type=input name=Form1Cell67> </TD>
<TD id=tabForm_TabCell_2_2_5 height=23 width=94 align=center><LABEL id=Form1Cell63 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell63">密级</LABEL> </TD>
<TD id=tabForm_TabCell_2_2_6 height=23 width=161 align=center><SELECT   id=Form1Cell64 style="FONT-SIZE: 12pt; HEIGHT: 20px; FONT-FAMILY: 宋体; WIDTH: 158px; COLOR: #000000; BACKGROUND-COLOR: #ffffff" name=Form1Cell64><OPTION selected value=" ">　</OPTION><OPTION value=1>普通</OPTION><OPTION value=2>秘密</OPTION><OPTION value=3>机密</OPTION><OPTION value=4>绝密</OPTION></SELECT> </TD></TR>
<TR id=tabForm_TabCell_2_3>
<TD id=tabForm_TabCell_2_3_1 height=23 width=106 align=center><LABEL id=Form1Cell68 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 宋体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell68"><B>来文单位</B></LABEL> </TD>
<TD id=tabForm_TabCell_2_3_2 height=23 width=333 colSpan=3 align=center><INPUT id=Form1Cell69 title=来文单位[只读] class=TextD style="FONT-SIZE: 12pt; HEIGHT: 20px; FONT-FAMILY: 宋体; WIDTH: 330px; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff"   type=input name=Form1Cell69> </TD>
<TD id=tabForm_TabCell_2_3_5 height=23 width=94 align=center><LABEL id=Form1Cell70 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 宋体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell70"><B>紧急程度</B></LABEL> </TD>
<TD id=tabForm_TabCell_2_3_6 height=23 width=161 align=center><SELECT   id=Form1Cell71 style="FONT-SIZE: 12pt; HEIGHT: 20px; FONT-FAMILY: 宋体; WIDTH: 158px; COLOR: #000000; BACKGROUND-COLOR: #ffffff" name=Form1Cell71><OPTION selected value=" "></OPTION><OPTION value=普通>普通</OPTION><OPTION value=急件>急件</OPTION><OPTION value=特急>特急</OPTION></SELECT> </TD></TR></TBODY></TABLE><!--表格：ID=3;Code=Tabulation3;Name=表格3;-->
<TABLE id=tabForm_Tabulation3 style="HEIGHT: 801px; WIDTH: 694px" borderColor=#000000 cellSpacing=0 borderColorDark=#ffffff cellPadding=0 borderColorLight=#000000 border=1>
<TBODY>
<TR id=tabForm_TabCell_3_1>
<TD id=tabForm_TabCell_3_1_1 style="HEIGHT: 202px" width=106 align=center><LABEL id=Form1Cell35 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell35">文件呈办</LABEL> <BR><LABEL id=Form1Cell36 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell36">意 &nbsp; &nbsp;见</LABEL> </TD>
<TD id=tabForm_TabCell_3_1_2 style="HEIGHT: 202px" width=426 align=center><TEXTAREA id=Form1Cell45 title=步骤[秘书科二收文、秘书经理处理]的处理意见 class=TextD style="FONT-SIZE: 12pt; HEIGHT: 100%; FONT-FAMILY: 宋体; WIDTH: 100%; COLOR: #000000; BACKGROUND-COLOR: #ffffff"   name=Form1Cell45></TEXTAREA> </TD>
<TD id=tabForm_TabCell_3_1_3 style="HEIGHT: 202px" vAlign=Left width=162 align=left><INPUT id=Form1Cell56 title=综合办[只读] class=TextD style="FONT-SIZE: 12pt; HEIGHT: 20px; FONT-FAMILY: 宋体; WIDTH: 160px; COLOR: #000000; BACKGROUND-COLOR: #ffffff"   type=input name=Form1Cell56> </TD></TR>
<TR id=tabForm_TabCell_3_2>
<TD id=tabForm_TabCell_3_2_1 style="HEIGHT: 225px" width=106 align=center><LABEL id=Form1Cell37 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell37">领 &nbsp;导</LABEL> <BR><LABEL id=Form1Cell38 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell38">批 &nbsp;示</LABEL> </TD>
<TD id=tabForm_TabCell_3_2_2 style="HEIGHT: 225px" width=426 align=center><TEXTAREA id=Form1Cell46 title="步骤[总裁办总经理意见、集团领导、批示2、批示3、批示4、批示5、总裁办副总经理、集团 领导、批示 2、批示 3、批示 4、批示 5]的处理意见" class=TextD style="FONT-SIZE: 12pt; HEIGHT: 97.45%; FONT-FAMILY: 宋体; WIDTH: 100%; COLOR: #000000; BACKGROUND-COLOR: #ffffff" rows=1   name=Form1Cell46></TEXTAREA> </TD>
<TD id=tabForm_TabCell_3_2_3 style="HEIGHT: 225px" vAlign=Left width=162 align=left><INPUT id=Form1Cell51 title=集团公司领导[只读] class=TextD style="FONT-SIZE: 12pt; HEIGHT: 212px; FONT-FAMILY: 宋体; WIDTH: 170px; COLOR: #000000; BACKGROUND-COLOR: #ffffff"   size=10 type=input name=Form1Cell51>&nbsp;<BR></TD></TR>
<TR id=tabForm_TabCell_3_3>
<TD id=tabForm_TabCell_3_3_1 style="HEIGHT: 204px" width=106 align=center><LABEL id=Form1Cell39 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell39">阅 &nbsp; 者</LABEL> <BR><LABEL id=Form1Cell40 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell40">签 &nbsp; 名</LABEL> </TD>
<TD id=tabForm_TabCell_3_3_2 style="HEIGHT: 204px" width=426 align=center><TEXTAREA id=Form1Cell47 title=步骤[传阅]的处理意见 class=TextD style="FONT-SIZE: 12pt; HEIGHT: 100%; FONT-FAMILY: 宋体; WIDTH: 100%; COLOR: #000000; BACKGROUND-COLOR: #ffffff"   name=Form1Cell47></TEXTAREA> </TD>
<TD id=tabForm_TabCell_3_3_3 style="HEIGHT: 204px" vAlign=Left width=162 align=left><TEXTAREA id=Form1Cell57 title=阅者[只读] class=TextD style="FONT-SIZE: 12pt; HEIGHT: 200px; FONT-FAMILY: 宋体; WIDTH: 171px; COLOR: #000000; BACKGROUND-COLOR: #ffffff"   name=Form1Cell57></TEXTAREA> </TD></TR>
<TR id=tabForm_TabCell_3_4>
<TD id=tabForm_TabCell_3_4_1 style="HEIGHT: 168px" width=106 align=center><LABEL id=Form1Cell41 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell41">办 &nbsp; 理</LABEL> <BR><LABEL id=Form1Cell42 class=TextD style="FONT-SIZE: 12pt; FONT-FAMILY: 黑体; COLOR: #000000; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffffff" name="Form1Cell42">情 &nbsp; 况</LABEL> </TD>
<TD id=tabForm_TabCell_3_4_2 style="HEIGHT: 168px" width=426 align=center><TEXTAREA id=Form1Cell48 title="步骤[部门 领导处理、部门处理、部门领导处理、部门领导阅办]的处理意见" class=TextD style="FONT-SIZE: 12pt; HEIGHT: 100%; FONT-FAMILY: 宋体; WIDTH: 100%; COLOR: #000000; BACKGROUND-COLOR: #ffffff"   name=Form1Cell48></TEXTAREA> </TD>
<TD id=tabForm_TabCell_3_4_3 style="HEIGHT: 168px" vAlign=Left width=162 align=left><INPUT id=Form1Cell58 title=部门领导[只读] class=TextD style="FONT-SIZE: 12pt; HEIGHT: 161px; FONT-FAMILY: 宋体; WIDTH: 170px; COLOR: #000000; BACKGROUND-COLOR: #ffffff"   size=15 type=input name=Form1Cell58> </TD></TR></TBODY></TABLE>
				</div>			
				
			</form>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>		
		<script type="text/javascript" >//自己按需求可以提取单独的js文件 存放与resources/scripts文件夹下 命名规则：form.文件名.js
		$(function(){
			  var count=0;//默认选择行数为0
			  $('table.datatable').datatable({
				  checkable: true,
				  checksChanged:function(event){
					  this.$table.find("tbody tr").find("input#id").removeAttr("name");
					  var checkArray = event.checks.checks;
					  count = checkArray.length;//checkbox checked数量
					  for(var i=0;i<count;i++){//给隐藏数据机上name属性
						  this.$table.find("tbody tr").eq(checkArray[i]).find("input#id").attr("name","id");
					  }
				  }
					
			  });
			  
			$("#save").click(function(){
				$("#manuList").attr("action","saveTask.do").submit();
			});
			  	
			$("#send").click(function(){
				$("#manuList").attr("action","sendTask.do").submit();
			});
			
			$("#view").click(function(){
			
			});
			
			$("#back").click(function(){
				window.open("<%=basePath%>/workflow/wf/templateList.do?currentPage=1","_self");
			});
			
		 });
         </script>
	</body>
</html>
