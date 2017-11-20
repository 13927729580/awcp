<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String operation = request.getParameter("operation");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>菜单查询页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_css.jsp" %><!-- 注意加载路径 -->
	</head>
	<body id="main">
		<div class="container-fluid">
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-success" id="editBtn"><i class="icon-edit"></i>修改</button>
			</div>
			
			<div class="row sortable" id="datatable">
			    <form  method="post" id="manuList">	
	            <input type="hidden" name="currentPage" value="${currentPage}">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th><input type="checkbox" id="checkAllKeywords" /></th>
							<th  class="sort text-ellipsis" data-flex="false" data-width="">关键字名</th>
						</tr>
					</thead>
					<tbody id="keywordsContent">	
					</tbody>
				</table>
				</form>
			</div>

			<!-- searchform 位置 -->
			
			<!--第<input type="text" size="5" class="col"/>列
			<a href="javascript:;" class="btn btn-success" id="lookUp">查看选中行的列信息</a>-->
			<!-- <div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div> --><!-- 分栏 分页栏 -->
			<!--<div class="row navbar-fixed-bottom text-center" id="line-pagers">
				<sc:PageNavigation dpName="vos" css="line-pagers"></sc:PageNavigation> 
			</div>--><!-- 独行 分页栏 -->

		</div>
		
		<div class="hidden" id="editDiv">
			<form class="form-horizontal" id="editForm" action="#" method="post">
			<input name="id" type="hidden">
				<div class="form-group">
					<label class="col-md-2 control-label">关键字名：</label>
					<div class="col-md-4">
						<input name="dataValue" class="form-control" id="dataValue" type="text" >
					</div>
				</div>
			</form>
		</div>
		<%@ include file="/resources/include/common_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>

		<script type="text/javascript">
		var basePath = "<%=basePath%>";
		var operation = "<%=operation%>";
		$(function(){
			initKeywords();
			operationProcess();//对操作类型进行解析，如果是查看则隐藏按钮组
			
			//add
      		$("#addBtn").click(function(){
      		
      			top.dialog({ 
					id: 'keywords-dialog' + Math.ceil(Math.random()*1000000),
					title: '系统关键字',
					url: basePath + "formdesigner/page/component/keywords-edit.jsp?operation=1",
					onclose : function() {
						initKeywords();
					}
				}).show();
      			return false;
      		});
      		
      		//popup edit
      		$("#editBtn").click(function(){//
      			var count = $(":checkbox[name='keyword']:checked").size();
      			
      			if(count == 1){
      				var id = $(":checkbox[name='keyword']:checked")[0].value;
      				top.dialog({ 
					id: 'keywords-dialog' + Math.ceil(Math.random()*1000000),
					title: '系统关键字',
					url: basePath + "formdesigner/page/component/keywords-edit.jsp?operation=2&&keywordId="+id,
					onclose : function() {
						initKeywords();
					}
					}).show();
      			}else{
      				alertMessage("请选择某个资源进行操作");
      			}
      			return false;
      			
      		});
      		
      		//delete
          	$("#deleteBtn").click(function(){
          		var arr = $("input:checkbox[name='keyword']:checked");
          		var count = arr.length;
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
          			var ids = "";
          			for(var i=0;i<count;i++){
          				ids += ","+arr[i].value;
          			}
          			ids = ids.substring(1);
          			alert(ids);
          			$.ajax({
          				type:'post',
          				url:basePath + "manage/deleteKeyword.do",
          				success:function(msg){
          					initKeywords();
          				}
          			});
	          		
          		}
      			return false;
          	});
          	

          });
          
          function initKeywords(){
          	$.ajax({
				type : 'post',
				url : basePath + "manage/getKeywords.do",
				success : function(msg){
					$("#keywordsContent").empty();
					for(var i=0;i<msg.length;i++){
						$("#keywordsContent").append("<tr><td><input type='checkbox' name='keyword' value='"+msg[i].id+"'/></td> "+
								+"<input type='hidden' name='keywordId' value='"+msg[i].id+"'/><td>"+msg[i].dataValue+"</td></tr>");
					}
				}
			});
          }
          
          function operationProcess(){
          	//如果是查看，隐藏按钮
          	if(operation=='0'){
          		$("#buttons").hide();
          	}
          }
		</script>
	</body>
</html>
