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
 		<title>表单类型页面</title>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
		<link rel="stylesheet" href="<%=basePath %>./resources/styles/zTreeStyle/zTreeStyle.css">
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="#">首页</a></li>
		          <li><a href="#">xxxx</a></li>
		          <li class="active">表单编辑</li>
		        </ul>
			</div>
			<div class="row" id="buttons">
				<div class="btn-group">
		              <button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
		              		<i class="icon-plus-sign"></i>弹窗 <span class="caret"></span> 
		              </button>
		              <ul class="dropdown-menu">
		                <li><a href="javascript:;" onclick="addModal({title:'提示',url:'./main.jsp'});">iframe窗口</a></li>
		                <li><a href="javascript:;" onclick="addModal({title:'普通窗口',content:'abc'});">普通窗口</a></li>
		              </ul>
	            </div>
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>保存</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>提交</button>
			</div>
			<div id="dataform">
				<form class="form-horizontal form-condensed" id="groupForm" action="<%=basePath %>document/excute.do" method="post">
					<div class="form-group">
				      <label class=" col-md-1 control-label required">标题</label>
				      <div class="col-md-9">
				         <input name="title" class="form-control" id="title" type="text" placeholder="" value="">
				      </div>
				    </div>
					<div class="form-group">
						<label class=" col-md-1 control-label">上级任务</label>
						<div class="col-md-9">
							<select class="form-control">
								<option value="1">某项目1</option>
								<option value="2">某项目2</option>
								<option value="3">某项目3</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">类型</label>
						<div class="col-md-4">
							<select id="select-cds-type" class="form-control">
								<option value=""></option>
								<option value="cusadvice">A:客户合理化建议</option>
								<option value="saleadvice">A2:售前合理化建议</option>
								<option selected="selected" value="demo">A3:外单位交办</option>
								<option value="cor">B1:公司内部会议</option>
								<option value="coradvice">B2:公司内部合理化建议</option>
								<option value="cordemo">B3：公司内部交办</option>
								<option value="dep">C：部门任务</option>
								<option value="per">D：个人任务</option>
								<option value="closed">Closed</option>
							</select>
						</div>
						<label class="col-md-1 control-label">审核人</label>
						<div class="col-md-4">
							<input type="text" class="form-control" id="select-cds-member"/>
							<!-- <select id="select-member" class="form-control">
								<option value="productManager">P:产品经理</option>
								<option value="dev1">D:开发甲</option>
								<option value="dev2">D:开发乙</option>
								<option value="dev3">D:开发丙</option>
								<option value="dev4">D:开发丁</option>
							</select> -->
						</div>
					</div>
					<div class="form-group"><!-- 显示1列数据 -->
						<label class=" col-md-1 control-label">任务描述</label>
						<div class="col-md-9">
							<textarea name="content" class="form-control" id="content"
								rows="10"></textarea>
						</div>
					</div>
					<div class="form-group"><!-- 显示2列数据 -->
						<label class=" col-md-1 control-label">所属部门</label>
						<div class="col-md-4">
							<input name="keywords" disabled="" class="form-control" id="department"
								type="text" value="信息技术部">
						</div>
						<label class="col-md-1 control-label">所属组</label>
						<div class="col-md-4 ">
							<input name="keywords" disabled="" class="form-control" id="group"
								type="text" value="信息组">
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">启动日期</label>
						<div class="col-md-4">
							<div class="input-group date form-date"
								data-link-field="dtp_input2" data-date-format="dd MM yyyy"
								data-date="" data-link-format="yyyy-mm-dd">
								<input class="form-control" type="text" size="16" readonly
									value=""> <span class="input-group-addon"><span
									class="icon-remove"></span></span> <span class="input-group-addon"><span
									class="icon-calendar"></span></span>
							</div>
						</div>
						<label class="col-md-1 control-label">填报周期</label>
						<div class="col-md-4">
							<select class="form-control">
								<option value="1">周</option>
								<option value="2">月</option>
								<option value="3">季度</option>
								<option value="4">半年</option>
								<option value="5">年</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">完成时间</label>
						<div class="col-md-4">
							<div class="input-group date form-time"
								data-link-field="dtp_input3" data-date-format="hh:ii"
								data-date="" data-link-format="hh:ii">
								<input class="form-control" type="text" size="16" readonly
									value=""> <span class="input-group-addon"><span
									class="icon-remove"></span></span> <span class="input-group-addon"><span
									class="icon-time"></span></span>
							</div>
						</div>
						<label class="col-md-1 control-label">紧急程度</label>
						<div class="col-md-4">
							<select class="form-control">
								<option value="1">普通</option>
								<option value="2">紧急</option>
								<option value="3">加急</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">选择日期</label>
						<div class="col-md-4">
							<input class="form-control form-date" type="text"
								placeholder="选择或者输入一个日期：yyyy-MM-dd">
						</div>
						<label class="col-md-1 control-label">选择时间</label>
						<div class="col-md-4">
							<input class="form-control form-time" type="text" readonly
								placeholder="选择或者输入一个时间：hh:mm">
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">组合时间</label>
						<div class="col-md-4">
							<div class="input-group date form-datetime"
								data-link-field="dtp_input1"
								data-date-format="dd MM yyyy - HH:ii p"
								data-date="1979-09-16T05:25:07Z">
								<input class="form-control" type="text" size="16"
									value=""> <span class="input-group-addon"><span
									class="icon-remove"></span></span> <span class="input-group-addon"><span
									class="icon-th"></span></span>
							</div>
						</div>
						<label class="col-md-1 control-label">日期时间</label>
						<div class="col-md-4">
							<input class="form-control form-datetime" type="text" readonly
								placeholder="选择或者输入一个日期+时间：yyyy-MM-dd hh:mm">
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">联动菜单</label>
						<div class="col-md-9">
							<div class="input-group">
				                <select class="form-control">
				                  <option value="1">省份</option>
				                  <option value="2">北京</option>
				                  <option value="3">上海</option>
				                  <option value="3">广州</option>
				                </select>
				                <span class="input-group-addon fix-border fix-padding"></span>
				                <select class="form-control">
				                  <option value="1">市/县</option>
				                  <option value="1">...</option>
				                </select>
				                <span class="input-group-addon fix-border fix-padding"></span>
				                <input type="text" class="form-control" placeholder="社区/乡/镇">
				              </div>
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">创建人</label>
						<div class="col-md-4">
							<input name="keywords" class="form-control" id="builder2"
								type="text" value="" title="请填写创建人，4个字符至8个字符">
						</div>
						<div class="col-md-4">
							<label class="checkbox-inline"> <input name="optionsCheck"
								type="checkbox" checked="" value="option1"> 123
							</label> <label class="checkbox-inline"> <input name="optionsCheck"
								type="checkbox" value="option2"> 234
							</label> <label class="checkbox-inline"> <input name="optionsCheck"
								type="checkbox" value="option2"> 345
							</label>
						</div>
					</div>
				
					<div class="form-group">
						<label class=" col-md-1 control-label">关键字</label>
						<div class="col-md-9">
							<input name="keywords" class="form-control" id="keywords"
								type="text" value="">
						</div>
					</div>
					<div class="form-group">
						<label class=" col-md-1 control-label">备注说明</label>
						<div class="col-md-9">
							<textarea name="content" class="form-control" id="content"
								rows="10"></textarea>
						</div>
					</div>
				
					<div class="form-group">
						<label class=" col-md-1 control-label">附件路径</label>
						<div class="col-md-9">
							<div class="input-group">
								<span class="input-group-addon">http://www.baidu.com/</span> <input
									name="alias" class="form-control" id="alias" type="text"
									value=""> <span class="input-group-addon">.rar</span>
							</div>
						</div>
					</div>
				
				
					<div class="form-group">
						<label class=" col-md-1 control-label">保存为</label>
						<div class="col-md-9">
							<label class="radio-inline"> <input name="optionsRadios"
								type="radio" checked="" value="option1"> 草稿
							</label> <label class="radio-inline"> <input name="optionsRadios"
								type="radio" value="option2"> 私人
							</label> <label class="radio-inline"> <input name="optionsRadios"
								type="radio" value="option2"> 公开
							</label>
						</div>
					</div>
				</form>
				</div>
			
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>base/resources/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript">//自己按需求可以提取单独的js文件 存放与resources/scripts文件夹下 命名规则：form.文件名.js
         $(function(){//page_mix
        	// datetime picker
        	if($.fn.select2){
        		 $("#select-type").select2();
        		 $("#select-member").select2({ 
        			 tags:["P:产品经理","D:开发甲","D:开发乙","D:开发丙","D:开发丁"],
        			 placeholder: "请选择审核人",
        			 tokenSeparators: [",", " "]});
        		 $("#keywords").select2({
                     tags:[],
                     tokenSeparators: [",", " "],
                     maximumSelectionSize: 5
        		 });
        		 
        	}
       	    if($.fn.datetimepicker)
       	    {
       	        $('.form-datetime').datetimepicker(
       	        {
       		    language:  'zh-CN',
       	            weekStart: 1,
       	            todayBtn:  1,
       	            autoclose: 1,
       	            todayHighlight: 1,
       	            startView: 2,
       	            forceParse: 0,
       	            showMeridian: 1,
       	            format: 'yyyy-mm-dd hh:ii'
       	        });
       	        $('.form-date').datetimepicker(
       	        {
       	            language:  'zh-CN',
       	            weekStart: 1,
       	            todayBtn:  1,
       	            autoclose: 1,
       	            todayHighlight: 1,
       	            startView: 2,
       	            minView: 2,
       	            forceParse: 0,
       	            format: 'yyyy-mm-dd'
       	        });
       	        $('.form-time').datetimepicker({
       	            language:  'zh-CN',
       	            weekStart: 1,
       	            todayBtn:  1,
       	            autoclose: 1,
       	            todayHighlight: 1,
       	            startView: 1,
       	            minView: 0,
       	            maxView: 1,
       	            forceParse: 0,
       	            format: 'hh:ii'
       	        });
       	    }
		   $.formValidator.initConfig({formID:"groupForm",debug:false,onSuccess:function(){
				$("#groupForm").submit();
				  return false;
		    },onError:function(){alert("请按提示正确填写内容");}});
			//$("#parentMenuId").formValidator({empty:true,onShow:"请输入父节点",onCorrect:"符合格式要求"}).regexValidator({regExp:"num1",dataType:"enum",onError:"正整数格式不正确"});
			$("#title").formValidator({onShow:"请输入标题名称",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		    $("#menuName").formValidator({onShow:"请输入资源名称",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		    $("#menuAddress").formValidator({onShow:"请输入资源地址",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		    $("#menuSeq").formValidator({onShow:"请输入资源顺序",onFocus:"至少1个长度",onCorrect:"合法"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
         });		
         </script>
         
         <script>//page_group
        $(function(){
        	 var count=0;//默认选择行数为0
	   		  $('table.datatable').datatable({
	   			  checkable: true,
	   			  checksChanged:function(event){
	   				  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
	   				  var checkArray = event.checks.checks;
	   				  count = checkArray.length;//checkbox checked数量
	   				  for(var i=0;i<count;i++){//给隐藏数据机上name属性
	   					  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
	   				  }
	   			  }				
	   		  });
        	 
	   		$.fn.zTree.init($("#tree1"), setting, tNodes1);
        })
         </script>
         
         <script>//父子页面
         function createTable(){
        	 //$("#inputForm").submit();//返回一个标识符
        	 var count1=0;//默认选择行数为0
	   		  $('table.datatable1').datatable({
	   			  checkable: true,
	   			  checksChanged:function(event){
	   				  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
	   				  var checkArray = event.checks.checks;
	   				  count1 = checkArray.length;//checkbox checked数量
	   				  for(var i=0;i<count1;i++){//给隐藏数据机上name属性
	   					  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
	   				  }
	   			  }				
	   		  });
        	 $('div.table').show();
         };
         $(function(){        	 
        	 $('table.datatable1').datatable({
	   			  checkable: false,
	   			  checksChanged:function(event){
	   				  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
	   				  var checkArray = event.checks.checks;
	   				  count1 = checkArray.length;//checkbox checked数量
	   				  for(var i=0;i<count1;i++){//给隐藏数据机上name属性
	   					  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
	   				  }
	   			  }				
	   		  });
        })
         </script>
         
         <script>
         $(function(){//page_condensed紧凑表单页面
        	 if($.fn.select2){
        		 $("#select-cds-type").select2();
        		 $("#select-cds-member").select2({ 
        			 tags:["P:产品经理","D:开发甲","D:开发乙","D:开发丙","D:开发丁"],
        			 placeholder: "请选择审核人",
        			 tokenSeparators: [",", " "]});
        		 $("#keywords").select2({
                     tags:[],
                     tokenSeparators: [",", " "],
                     maximumSelectionSize: 5
        		 });
        		 
        	}
         })
         </script>
	</body>
</html>
