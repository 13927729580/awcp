<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>样式编辑页面</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <!-- <li><a href="#">首页</a></li>
		          <li><a href="#">我的应用产品</a></li> -->
		          <li class="active">样式编辑页面</li>
		        </ul>
			</div>
			
			<div class="row" id="dataform">
				<form class="form-horizontal"  id="styleForm"  action="<%=basePath%>fd/style/save.do" method="post" >
					 	<c:if test="${result!=null&&''!=result}">
							<span style="color: red">(${result})</span>
						</c:if>
					 	<input type="hidden" name="pageId" value="${vo.pageId}"/>	
				  		<input type="hidden" name="code" value="${vo.code}"/>	
						<div class="form-group">       	
				            <label class="col-md-4 control-label required">样式名称：</label>
				            <div class="col-md-4">
				                <input name="name" class="form-control" id="name" type="text" value="${vo.name}">
				            </div>
				 	    </div>
						<div class="form-group">       	
				            <label class="col-md-4 control-label required">样式描述：</label>
				            <div class="col-md-4">
				                <input name="description" class="form-control" id="description" type="text" value="${vo.description}">
				            </div>  
				 	    </div>
						<div class="form-group">       	
				            <label class="col-md-4 control-label required">样式脚本：</label>
				            <div class="col-md-4">
				                <textArea name="script" class="form-control" id="script" rows="4">${vo.script}</textArea>
				            </div>
						</div>
						<div class="form-group"><!-- 表单提交按钮区域 -->
	           			 	<div class="col-md-offset-4 col-md-8">
	              				<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
	              				<a href="<%=basePath%>fd/style/list.do?currentPage=1" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
	            			</div>
	       				</div>
				</form>
			</div>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript">
         $(document).ready(function(){
        	 try {
 				var dialog = top.dialog.get(window);
 			} catch (e) {
 				return;
 			}
		  /*  $.formValidator.initConfig({formID:"styleForm",debug:false,onSuccess:function(){
				$("#actForm").submit();
				return false;
		    },onError:function(){alert("请按提示正确填写内容");}});
			$("#name").formValidator({empty:true,onShow:"请输入样式名称",onFocus:"至少1个长度",onCorrect:"ok"}).inputValidator({min:1,max:15,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
			$("#description").formValidator({onShow:"请输入描述信息",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		    $("#script").formValidator({onShow:"请输入样式脚本",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"}); 
		 */
		    $("#saveBtn").click(function(){
				if(dialog){
					var data = $("#styleForm").serializeJSON();
					$.ajax({
						type:"POST",
						async:false,
						url:"<%=basePath%>fd/style/saveByAjax.do",
						data:data,
						success:function(ret){
							var json=eval(ret);
							dialog.close(json);
							dialog.remove();
						},
						error: function (XMLHttpRequest, textStatus, errorThrown) { 
			                  alert(errorThrown); 
					    }
					});
				}else{
					$("#styleForm").submit();
				}
				return false;
			});
         });
         </script>	
	</body>
</html>
