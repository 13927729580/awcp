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
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="saveComponent"><i class="icon-plus-sign"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<div class="row" id="tab">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#tab1" data-toggle="tab">基本</a></li>
					<li class=""><a href="#tab2" data-toggle="tab">校验</a></li>
					<li class=""><a href="#tab3" data-toggle="tab">状态</a></li>
					<li class=""><a href="#tab4" data-toggle="tab">打印</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1">
						<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
						<input type="hidden" name="pageId" id="pageId"/>
						<input type="hidden" name="componentType" id="componentType">
						<div class="form-group">
							<label class="col-md-2 control-label required">名称</label>
							<div class="col-md-4">
								<input name="name" class="form-control" id="name" type="text" value="">
								<div class="input-group-btn">
						             	<button class="btn btn-default" type="button" id="showKeywords">查看关键字</button>
								    </div>
								<div class="input-group-btn">
						             	<button class="btn btn-default" type="button" id="editName">编辑名称</button>
								 </div>
							</div>
						</div>	
						
						<%--<div class="form-group"><!-- 显示1列数据 -->
							<!-- 自己输入or下拉选择(dataname.itemcode) -->
							<label class="col-md-offset-1 col-md-1 control-label required">数据源</label>
							<div class="col-md-4">
								<div class="input-group" id="selectInput">
						            <input type="text" class="form-control sI_input" name="dataItemCode" id="dataItemCode">
						            <div class="input-group-btn">
						              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
						                	<span class="caret"></span>
						              </button>
						              <ul class="dropdown-menu pull-right sI_select">
						              </ul>
						            </div>
						         </div>
							</div>
						</div>--%>
						
						<div class="form-group">
							<label class="col-md-2 control-label required">序号</label>
							<div class="col-md-4">
								<input name="order" class="form-control" id="order" type="text" value="">
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-md-2 control-label required">布局</label>
							<div class="col-md-4">
								<div class="input-group">
								<input type="text" readonly="readonly" class="form-control" name="layoutName" id="layoutName">
								<div class="input-group-btn"> <button class="btn btn-default" type="button" id="layoutSelect">选择</button> </div>
								<input type="hidden" readonly="readonly"  class="form-control" name="layoutId" id="layoutId">
								</div>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-2 control-label">Name值：</label>
							<div class="col-md-4">
								<input type="text" name='tab_name' id='tab_name'  class='form-control' value="百度;微博"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-2 control-label">URL地址：</label>
							<div class="col-md-4">
								<textarea name='tab_url' id='tab_url' rows='4' class='form-control'>
www.baidu.com;
www.weibo.com
								</textarea>
							</div>
						</div>
				
				</div>
				<div class="tab-pane" id="tab2"><%@ include file="/formdesigner/page/component/common/validators.jsp" %></div>
				<div class="tab-pane " id="tab3"><%@ include file="/formdesigner/page/component/common/hidden-disabled-readonly-defaultvalue.jsp" %></div>
				<div class="tab-pane" id="tab4"><%@ include file="/formdesigner/page/component/common/font-layout-text-backgroud.jsp" %></div>
			</div>
		</form>
	</div>

		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script src="<%=basePath%>/formdesigner/page/component/component.js"></script>
		<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>
		<script src="<%=basePath%>/formdesigner/page/component/tab/swiper.min.js"></script>
		<script src="<%=basePath%>/formdesigner/page/component/tab/tab.js"></script>
		<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){
			initializeDocument("${componentType}","${_ComponentTypeName[componentType]}");
			//添加数据
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});

			$("#order").formValidator({onShow:"请输入序号",onFocus:"请输入顺序",onCorrect:"合法"})
			.regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
			$("#name").formValidator({onShow:"请输入名称",onFocus:"请输入组件名称",onCorrect:"合法"})
			.regexValidator({regExp:"[_a-zA-Z][_a-zA-Z0-9]*",dataType:"string",onError:"名称只能包含字母、数字、下划线(以字母或下划线开头)"});
			$("#layoutId").formValidator({onFocus:"请选择布局",onCorrect:"符合要求"})
				.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择布局"});
		});
		
		
		
		
		//在更新时加载组件值；
		/*function loadComponentData(storeObject){
			var item = eval("("+storeObject.content+")");
			if(item.pageId != null){
				loadCommonComponentData(item);
			}
		}*/
	
	</script>
</body>
</html>