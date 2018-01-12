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
					<li class=""><a href="#tab4" data-toggle="tab">列</a></li>
					<li class=""><a href="#tab6" data-toggle="tab">过滤条件</a></li>
					<li class=""><a href="#tab5" data-toggle="tab">操作</a></li>
					<li class=""><a href="#tab2" data-toggle="tab">校验</a></li>
					<li class=""><a href="#tab7" data-toggle="tab">打印</a></li>
					<li class=""><a href="#tab3" data-toggle="tab">状态</a></li>
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/component/dataGrid/basicInfo.jsp" %></div>
				<div class="tab-pane " id="tab4"><%@ include file="/formdesigner/page/component/dataGrid/columns.jsp" %></div>
				<div class="tab-pane " id="tab6"><%@ include file="/formdesigner/page/component/dataGrid/conndition.jsp" %></div>
				<div class="tab-pane " id="tab5"><%@ include file="/formdesigner/page/component/dataGrid/operate.jsp" %></div>
				<div class="tab-pane" id="tab2"><%@ include file="/formdesigner/page/component/common/validators.jsp" %></div>
				<div class="tab-pane" id="tab7"><%@ include file="/formdesigner/page/component/common/dataGridPdfConfi.jsp" %></div>
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
			//初始化页面列表
			var str = "<option value=''></option>";
			$.ajax({
			   type: "POST",
			   async:false,
			   url: basePath + "fd/listJson.do",
			   success: function(data){
			   		$("#relatePageId").empty();
					if(data != null){
						$.each(data, function(index, item) {
							str += "<option value='" + item.id+ "'>" + item.name + "</option>";
						});
						$("#alertPage").append(str);
					}else{
						alert("加载组件失败");
					}
			   	}
			});	
		
			initializeDocument("${componentType}","${_ComponentTypeName[componentType]}");
				
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			if(dialog != null){
				data = dialog.data;
			}
			
			var dataSource = null;
			if(data!=null && data.dataSource){
				dataSource = data.dataSource;
			}
						
			var index=0;
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});	
			
			$("#order").formValidator({onShow:"请输入序号",onFocus:"请输入顺序",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
			$("#layoutId").formValidator({onFocus:"请选择布局",onCorrect:"符合要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择布局"});
				
			$("#addColumn").click(function(){
				var str="<tr id=dataTr'"+index+"'>";			
				str+="<td><input style='width:100%' name='columns[][columnTitle]' id='columnTitle-"+index+"'" + "type=text" + "/>"+"</td>";
				str+="<td><input style='width:100%' name='columns[][columnField]' id='columnField-"+index+"'" + "type=text" + "/>"+"</td>";
				str+="<td><input style='width:100%' name='columns[][columnWidth]' id='columnWidth-"+index+"'" + "type=text" + "/>"+"</td>";
				str+="<td><input style='width:100%' name='columns[][order]' id='columnOrder-"+index+"'/>"+"</td>";
				str+="<td style='text-align:center;'><select class='editType' name='columns[][editType]' id='editType-'"+ index +"'>";
				str+="<option value='0'>无</option><option value='text'>文本</option><option value='numberbox'>数字</option>"+
				+"<option value='datebox'>日期框</option><option value='combobox'>下拉框</option><option value='checkbox'>选择框</option></select></td>";
				str+="<td><input class='editValue' disabled='disabled' style='width:100%' name='columns[][editValue]' id='editValue-"+index+"'/>"+"</td>";
				str+="<td style='text-align:center;'><a href='javascript:void(0)' class='removeTr'>删除</a>"+"</td>";
				str+="</tr>";
				index++;
				$("#columnsBody").append(str);
			});
			$(".editType").off("change").on("change","",function(){
				var val=$(this).val();
				if(val=="combobox"||val=="checkbox"){
					$(this).parent("td").next("td").children(".editValue").attr("disabled",false);
				}else{
					$(this).parent("td").next("td").children(".editValue").attr("disabled",true);
				}
			})		
			var connIndex=0;
			$("#addConndition").click(function(){
				var str="<tr id=connTr'"+connIndex+"'>";			
					str+="<td><input style='width:100%' name='connditions[][paramKey]' id='paramKey-"+connIndex+"'" + "type=text" + "/>"+"</td>";
					str+="<td style='text-align:center;'>---》</td>";
					str+="<td><input style='width:100%' name='connditions[][paramValue]' id='paramValue-"+connIndex+"'" + "type=text" + "/>"+"</td>";
					str+="<td style='text-align:center;'>"+"<input type='checkbox' name='connditions[][isFinal]' id='isFinal-'"+ connIndex +"' value='1'/></td>";
					str+="<td style='text-align:center;'><a href='javascript:void(0)' class='removeConnTr'>删除</a>"+"</td>";
					str+="</tr>";
					connIndex++;
				$("#connditionBody").append(str);
			});
			
			$("#alertPage").select2();
			
			$("#addParament").click(function(){
				var str = "<tr><td><input type='text' style='width:75%' name='param[][exportParam]' value=''>" +	
						  "<button class='btn btn-default exportParam' type='button'>选择</button></td><td>——》</td>" +					
						  "<td><input type='text' style='width:75%' name='param[][storeParam]' value=''>" +
						  "<input type='hidden' name='param[][storeParamId]' value=''>" + 
						  "<button class='btn btn-default storeIdSelect' type='button'>选择</button></td>" + 
						  "<td><a href='javascript:void(0)' class='removeParamTr'>删除</a>" + "</td></tr>";
				$("#paramentBody").append(str);
			});
				
			$("#paramentBody").on("click",".exportParam",function(){
				var _this = $(this);
				if($("#dynamicPageId").val()){
					top.dialog({
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						title : '数据源选择',
						height:400,
						url : basePath + "formdesigner/page/component/common/dataSourceSelect.jsp",
						data : dataSource,
						onclose : function() {
							if (this.returnValue) {
								_this.prev().val(this.returnValue);
							}
						}
					}).show();
				} else {
					alert("请先保存表单");
				}
			});
			
			$("#paramentBody").on("click",".storeIdSelect",function(){
				var _this = $(this);
				if($("#alertPage").val()){
					top.dialog({
						id : 'edit-dialog' + Math.ceil(Math.random() * 10000),
						title : '数据源选择',
						height:400,
						url : basePath + "formdesigner/page/component/common/storeParamSelect.jsp",
						data : $("#alertPage").val(),
						onclose : function() {
							if (this.returnValue) {
								_this.prev().prev().val(this.returnValue.storeCode);
								_this.prev().val(this.returnValue.storeId);
							}
						}
					}).show();
				} else {
					alert("请先选择弹出页面");
				}
			});
		});
		
		
		$("#columnsBody").on("click",".removeTr",function(){
			var _this = $(this);
			_this.parents("tr").remove();
		});
		
		$("#paramentBody").on("click",".removeParamTr",function(){
			var _this = $(this);
			_this.parents("tr").remove();
		});
		
		$("#connditionBody").on("click",".removeConnTr",function(){
			var _this = $(this);
			_this.parents("tr").remove();
		});
	</script>
</body>
</html>