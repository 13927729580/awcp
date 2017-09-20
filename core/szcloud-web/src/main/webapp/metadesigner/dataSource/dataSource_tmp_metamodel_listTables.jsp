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
 		<title>数据库表列表</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %>
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="dataform">
				<span>数据库连接信息</span>
				<form class="form-horizontal" id="groupForm" action="" method="post">
					<input type="hidden" name="datasourceJson" id="datasourceJson" value="${datasourceJson}" />
					<div class="form-group">
						<div class="col-md-12">
						<!-- 全选 、通过条件选择 -->
						<label class="checkbox-inline"> 
							<input name="table" type="checkbox"  value="1" id="selectAll">全选
						</label>
						<label class="checkbox-inline"> 
							<input type="checkbox"  value="1" id="selectAllByCondition">通过条件选择
						</label>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-12">
							<table id="tableContent" style="border-style:none;width:100%">
						
							</table>
						</div>
					</div>
					<div class="form-group"><!-- 表单提交按钮区域 -->
			            <div class="col-md-offset-2 col-md-10">
			              	<a href="<%=basePath %>metaModel/selectPage.do" class="btn" id="undoBtn"><i class="icon-undo"></i>返回</a>
			              	<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>创建元数据</button>
			            </div>
			        </div>
				</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/common.js"></script>
		
		<script type="text/javascript">
       		$(document).ready(function(){
       			var items = '${tables}';
				items = items.substring(1,items.length-1);
				var itemArr = items.split(",");
				var html = "";
				var index = 0;
				for(var i=0;i<itemArr.length;i++){
					if(i%4==0){
						html += "<tr><td><label class='checkbox-inline'>" +
								"<input name='tableNames' type='checkbox'  value='" + itemArr[i] + 
								"'> " + itemArr[i] + "</label><td>";
					}					
					else if(i%4==3){
						html += "<td><label class='checkbox-inline'>" +
								"<input name='tableNames' type='checkbox'  value='" + itemArr[i] + 
								"'> " + itemArr[i] + "</label><td></tr>";
					}
					else{
						html += "<td><label class='checkbox-inline'>" +
								"<input name='tableNames' type='checkbox'  value='" + itemArr[i] + 
								"'> " + itemArr[i] + "</label><td>";
					}
				}
				if(itemArr.length%4!=0){
					html += "</tr>";
				}
				$("#tableContent").html(html);
				
       		
       			$("#selectAll").click(function(){
       				var v = $(this).prop("checked");
       				$("input[name='tableNames']").prop("checked", v);
       			});
  				$("#saveBtn").click(function(){
  					//如果选择了
  					var selectTables = "";
  					$("input[name='tableNames']:checked").each(function(i){
  						selectTables += $(this).val() + ",";
  					});
  					if(selectTables.length > 0){
  	  					//删除最后一个","
  	  					var data = $("#groupForm").serializeJSON();
  	  					selectTables = selectTables.substr(0,selectTables.length-1);
  	  					//alert(selectTables);
  	  					//alert(data.tableNames);
  	  					data.tableNames = selectTables;
  	  					$.ajax({
  	  						type : "POST",
  	  						url : "<%=basePath %>metaModel/addByDb.do",
  	  						data : data,
  	  						async : false,
  	  						success : function(rtn) {	  							
  	  							if(rtn.result == "1"){
  	  								//提示成功，跳转
  	  								alert("生成成功！");
  	  								location.href = "<%=basePath %>metaModel/selectPage.do"; 
  	  							} else {
  	  								//提示失败，
  	  								alertMessage( rtn.message );
  	  							}
  	  						}
  	  					});/**/
  					} else {
  						alertMessage("请选择需要需要转换的数据库表。");
  					}
      			});
       			
       		});
         </script>	
	</body>
</html>