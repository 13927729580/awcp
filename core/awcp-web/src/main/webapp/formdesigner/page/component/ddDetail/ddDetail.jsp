<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="sc" uri="szcloud" %>
<%@ page isELIgnored="false"%> 
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
	<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
</head>
<body id="main">
	<div class="container-fluid">
		<div class="row" id="buttons">
			<button type="button" class="btn btn-success" id="saveComponent"><i class="icon-save"></i>保存</button>
			<button type="button" class="btn btn-info" id="cancelComponent"><i class="icon-trash"></i>取消</button>
		</div>
		<form id="componentForm" action="">
			<div class="row" id="tab">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#tab1" data-toggle="tab">基本</a></li>
					<li class=""><a href="#tab2" data-toggle="tab">列</a></li>
					<li class=""><a href="#tab3" data-toggle="tab">状态</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/component/ddDetail/basicInfo.jsp" %></div>
				<div class="tab-pane " id="tab2"><%@ include file="/formdesigner/page/component/ddDetail/columns.jsp" %></div>
				<div class="tab-pane " id="tab3">
					<div class="form-group">
						<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">隐藏脚本(服务器脚本)</a></label>	
						<div class="col-md-4">
							<textarea name='hiddenScript' id='hiddenScript' rows='4' class='form-control'></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2"><a href="javascript:openCodeMirror('disabledScript');">禁用脚本(服务器脚本)</a></label>	
						<div class="col-md-4">
							<textarea name='disabledScript' id='disabledScript' rows='4' class='form-control'></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2"><a href="javascript:openCodeMirror('readonlyScript');">只读脚本(服务器脚本)</a></label>	
						<div class="col-md-4">
							<textarea name='readonlyScript' id='readonlyScript' rows='4' class='form-control'></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2"><a href="javascript:openCodeMirror('defaultValueScript');">默认值脚本(服务器脚本)</a></label>	
						<div class="col-md-4">
							<textarea name='defaultValueScript' id='defaultValueScript' rows='4' class='form-control'></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-2">加载成功后事件(客户端脚本)</label>	
						<div class="col-md-4">
							<textarea name='onchangeScript' id='onchangeScript' rows='4' class='form-control'></textarea>
						</div>
					</div>				
				</div>
			</div>
		</form>
	</div>
	<%@ include file="/resources/include/common_form_js.jsp" %>
	<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script src="<%=basePath%>/resources/plugins/echarts-2.2.1/src/component.js"></script>
	<script src="<%=basePath%>/formdesigner/scripts/form.cpcommons.js"></script>
	<script type="text/javascript">	
		var basePath = "<%=basePath%>";
		$('document').ready(function(){	
			initializeDocument("${componentType}","${_ComponentTypeName[componentType]}");
				
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			if(dialog != null){
				data = dialog.data;
			}
					
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});	
			
			$("#order").formValidator({
				onShow:"请输入序号",
				onFocus:"请输入顺序",
				onCorrect:"合法"
			}).regexValidator({
				regExp:"intege1",
				dataType:"enum",
				onError:"正整数格式不正确"
			});
			
			$("#layoutId").formValidator({
				onFocus:"请选择布局",
				onCorrect:"符合要求"
			}).inputValidator({
				min:1,
				empty:{leftEmpty:false,rightEmpty:false},
				onError:"请选择布局"
			});

			$("#addDetail").click(function(){
				var selects = "<select class='form-control' name='details[][type]'>" + 
							  "<option value='1001'>单行文本框</option>" + 
							  "<option value='1002'>日期文本框</option>" + 
							  "<option value='1005'>多行输入框</option>" + 
							  "<option value='1006'>下拉选项框</option>" + 
							  "<option value='1010'>隐藏框</option>" +
							  "<option value='1011'>文件上传框</option>" + 
							  "<option value='1016'>图片上传框</option>" + 							 
							  "</select>";
				var str = "<tr>";			
				str += "<td style='padding:0px;'><input class='form-control' name='details[][title]' type='text' /></td>";
				str += "<td style='padding:0px;'><input class='form-control' name='details[][field]' type='text' /></td>";
				str += "<td style='padding:0px;'><input class='form-control' name='details[][order]' type='text' /></td>";
				str += "<td style='padding:0px;'>" + selects + "</td>";
				str += "<td style='text-align:center;padding:0px;'><a href='javascript:void(0)' class='removeTr'>删除</a></td>";
				str += "</tr>";
				$("#detailsBody").append(str);
			});
		});
		
		
		$("#detailsBody").on("click",".removeTr",function(){
			$(this).parents("tr").remove();
		});
	</script>
</body>
</html>