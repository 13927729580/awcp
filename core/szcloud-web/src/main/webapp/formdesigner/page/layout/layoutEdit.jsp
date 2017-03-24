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
 		<title>菜单编辑页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="saveLayout"><i class="icon-plus-sign"></i>保存</button>
				<!-- <button type="button" class="btn btn-success" id="editBtn"><i class="icon-edit"></i>弹窗编辑</button> -->
			</div>
				<form class="form-horizontal" role="form" method="post" id="layoutForm">
					<input type="hidden"  name="pageId" id="pageId">
					<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
					<input type="hidden" name="parentId" id="parentId"/>
					
					<div class="row" id="tab">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tab1" data-toggle="tab">基本</a></li>
							<li class=""><a href="#tab2" data-toggle="tab">格式</a></li>
						</ul>
					</div>
					<div class="row tab-content">
						<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/layout/common/basicInfo.jsp" %></div>
						<div class="tab-pane" id="tab2"><%@ include file="/formdesigner/page/layout/common/border-layout-text.jsp" %></div>
					</div>
				</form>
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		
		<script type="text/javascript">
		
		$.formValidator.initConfig({formID:"layoutForm",debug:false,onSuccess:function(){
		//	$("#layoutForm").submit();
			  return false;
	    },onError:function(){alert("错误，请看提示")}});
	$("#name").formValidator({empty:true,onShow:"请输入名称",onCorrect:"符合格式要求"}).inputValidator({min:1,max:50,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
	$("#proportion").formValidator({onShow:"请输入布局占比",onFocus:"请输入占比",onCorrect:"合法"}).regexValidator({regExp:"num1",dataType:"enum",onError:"正整数格式不正确"});
   // $("#order").formValidator({onShow:"请输入顺序",onFocus:"请输入顺序",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
   
         
		
		//在更新时加载组件值;
		function loadLayoutData(storeObject){
			var item = eval("("+storeObject.content+")");
			if(item.pageId != null){
				if(item.pageId) 	$("#pageId").val(item.pageId);
				if(item.name) $("#name").val(item.name);
				if(item.top) $("#top").val(item.top);
				if(item.left) $("#left").val(item.left);
				if(item.proportion) $("#proportion").val(item.proportion);
				if(item.order) $("#order").val(item.order);
				if(item.parentId) $("#parentId").val(item.parentId);
				if(item.systemId) $("#systemId").val(item.systemId);
				
				if(item.layoutType)	$("#layoutType option[value='"+item.layoutType+"']").attr("selected","true");
				//alert(item.height);
				if(item.height)	{$("#height").val(item.height);}
				if(item.heightType) { $("#heightType").val(item.heightType).trigger("change");}
				if(item.width)	{$("#width").val(item.width);}
				if(item.textstyle) { 
					var v = item.textstyle;
					var array = v.split(",");
					for(var i = 0; i < array.length; i++) {
						var value = array[i];
						$("input[name='textstyle'][value='"+value+"']").attr("checked","checked");
					}
				}
				if(item.backgroundcolor) { $("#backgroundcolor").val(item.backgroundcolor).trigger("change");}
				if(item.textalign) { $("#textalign").val(item.textalign).trigger("change");}
				if(item.textverticalalign) { $("#textverticalalign").val(item.textverticalalign).trigger("change");}
			}
		}
		
		$('document').ready(function() {
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			
			var layoutId = "";
			if(dialog != null){
				data = dialog.data;
				layoutId = data.pageId;
				if(data.dynamicPageId){
					$("#dynamicPageId").val(data.dynamicPageId);
				}
				if(data.parentId){
					$("#parentId").val(data.parentId);
				}
			}	
			//如果是编辑，则加载组件内容
			if(layoutId && layoutId.indexOf(",") > 0){
				$("#pageId").val(layoutId);
				 $("#name").val("not allowed");
				 $("#name").attr("readonly","true");
				 $("#order").val("0");
				 $("#order").attr("readonly","true");
			}else if(layoutId){
				$.ajax({
					   type: "POST",
					   url: "<%=basePath%>layout/getLayoutById.do",
					   data: "storeId="+layoutId,
					   success: function(data){
							if(data != null && data.id != null){	
								loadLayoutData(data);
							//	alert("加载组件成功..." + data.id);
							}else{
								alert("保存失敗");
							}
					   }
					});	
			}
	
			// if($.fn.chosen) $('.chosen-select').chosen();
			if (dialog != null) {
				dialog.title('组件配置');
				dialog.width(300);
			//	dialog.height($(document).height);
				dialog.height(600);
				dialog.reset(); // 重置对话框位置
			}
			
			function validateCheckOut(dynamicPageId){
				var flag;
				$.ajax({
				type:"POST",
				url:"fd/validateCheckOut.do?id=" + dynamicPageId,
		
				async:false,
				dataType:'json',
				//回调函数
				success:function(result){
					if(result.value == 1){
						flag = true;
					}else{
						alert(result);
						flag = false;
					}	   	
				},
 		 	 	 error: function (XMLHttpRequest, textStatus, errorThrown) { 
 		  	       alert(errorThrown); 
  		 	       flag = false;
  		 	 	 }
				});
				return flag;
			}

			$("#saveLayout").click(function(){
				
				if(!validateCheckOut($("#dynamicPageId").val())){	//校验是否已签出
			
					return false;
				}
		
				if($.formValidator.pageIsValid('1')){
					var textstyle = "";
					$("input[name='textstyle']:checked").each(function(){
						 textstyle += $(this).val() + ",";
					});
					textstyle = textstyle.substring(0,textstyle.length-1);
					var formJson=$("#layoutForm").serializeJSON();
					//alert(textstyle);
					formJson.textstyle=textstyle;
					var name = $("#name").val();
					var layoutId = $("#pageId").val();
					var description =  $("#parentId").val();
					var dynamicPageId=$("#dynamicPageId").val();
					var order=$("#order").val();
					var sjContent = JSON.stringify(formJson);
					$.ajax({
						   type: "POST",
						   url: "<%=basePath%>layout/save.do",
						   data: "id="+layoutId + "&name="+name + 
						   "&description="+description+"&content="+sjContent+
						   "&dynamicPageId=" + dynamicPageId+
						   "&order=" + order,
						   success: function(data){
								if(data != null){	
									$("#layoutId").val(data.id);
									if(dialog != null){
										dialog.close(data);
										dialog.remove();
									}
								}else{
									alert("保存失敗");
								}
						   }
						})	
				}
			});
							
		});
	</script>
	</body>
</html>
