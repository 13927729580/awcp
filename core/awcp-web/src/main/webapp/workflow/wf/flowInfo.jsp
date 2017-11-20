<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.lang.*"%>     
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	cn.org.awcp.core.utils.DesUtils des = new cn.org.awcp.core.utils.DesUtils("leemenz");
	String rec = request.getParameter("msg"); 
	String msg = "";
	try {
			msg =des.decrypt(rec);
			System.out.println("==================="+msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
%>
<!DOCTYPE html>
<html>
	<head><base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>动态表单编辑页面</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
		<script type="text/javascript" src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
		<script>
			$(function(){
				var info="<%=msg%>";
				var num=info.split("@");
				var num1 = "";
				if((num[1]!=null || !(typeof num[1] == "undefined"))
				   num1=num[1];
				   
			    var num2 = "";
				if((num[2]!=null || !(typeof num[2] == "undefined"))&&num[2].indexOf("工作轨迹")>=0)
				   num2=num[2];
				   
				var num3 = "";
				if((num[3]!=null || !(typeof num[3] == "undefined"))&&num[3].indexOf("任务自动发送")>=0)
				   num3=num[3];
				   
				var num4 = "";
				if((num[4]!=null || !(typeof num[4] == "undefined"))&&num[4].indexOf("下一步")>=0)
				   num4=num[4];
				   
				var num5 = "";
				if(num[5]!=null || !(typeof num[5] == "undefined"))
				   num5=num[5];
				   
				var htmlString = '<style>.ui-dialog-body{overflow-x:hidden;overflow-y:auto;}</style><p style-"margin-top:0px;">'+num1+'</p><br/><div class="board panel" data-id="board1" style="width:100%;"><div class="panel-heading"><strong>处理人:</strong></div><div class="panel-body"><div class="board-list"><div class="board-item">'+num3+'</div><div class="board-item board-item-empty"><i class="icon-plus"></i> 移动到末尾</div><div class="board-item board-item-shadow"></div></div></div></div><br/><p><span>'+num4+'</span><span style="float:right;">'+num2.replace("查看","")+'</span></p>';
		var d = top.dialog({
			id:'workFlowTest',
			title: '操作提示',
			width:'630',
			height:'auto',
			content: htmlString,
			okValue: '指定特定的处理人处理',
			ok: function () {
				top.dialog({
						title: "用户选择框",
						url:"<%=basePath%>formdesigner/page/component/userSelect/chosenNames.jsp?",
						id:name,
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
					        		_obj.val(memberNames.join(","));
					        		_obj.append("<i class='icon-remove' style='position:absolute;top:8px;right:10px;z-index:99;cursor:pointer'></i>");
					      			//加入常用联系人
	  								userCount(memberIDs.join(","));
					      		}else{
					      			_obj.val("请选择用户");
					      		}
					      	}
					      	return false;			        
					    }
					}).width(1000).showModal();
				return false;
			},
			cancelValue: '指定特定的处理人处理',
			cancel: function () {
				alert('您选择了指定特定的处理人处理,正在打开...');
				//var dialog = top.dialog({id:'workFlowTest'});
				//dialog.remove();
				showOtherUser();
				return false;
				
			},
			cancel: false,
			button: [
					{
						value: '关闭'
					}
				]
		});
		d.showModal();
			})
			function showOtherUser(){
	 			var d = top.dialog({
					title: '用户选择',
					width:'240',
					content: '此页面为用户选择页面',
					okValue: '确定',
					ok: function () {
						alert('您选择了确定');
					},
					cancelValue: '取消',
					cancel: function () {
						alert('您选择了关闭');
					}
				});
				d.showModal();
			}
		</script>
	</head>
<body>
</body>

</html>