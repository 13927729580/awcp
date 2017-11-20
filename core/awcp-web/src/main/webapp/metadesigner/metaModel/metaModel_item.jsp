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
 		<%@ include file="../../resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zui/assets/datetimepicker/css/datetimepicker.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
		<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>
	</head>
	<body id="main">
		<div class="container-fluid">
		
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li><a href="<%=basePath%>devAdmin/intoSystem.do?sysId=1">首页</a></li>
		          <li><a href="<%=basePath %>metaModel/queryResult.do">元数据管理</a></li>
		          <li class="active">表单编辑</li>
		        </ul>
			</div>
			
			
			<div class="row tab-content">
				<!-- 父子页面 -->
				<div class="tab-pane active" id="tab2">
					<div id="formData">
						<div class="panel">
							<div class="panel-heading">
								<div class="btn-group">
									<div class="btn-group">
														<legend>
															元数据模型编辑
														</legend>
													</div>
								</div>
							</div>
							<form method="get" class="form-horizontal" id="inputForm" action="" >
							<div class="panel-body">
													<div class="form-group">
												      <label class="col-md-1 control-label required">模型名称(中)</label>
												      <div class="col-md-3">
												      	 <input type="hidden" id="id" name="id" value="${vo.id }">
												         <input name="modelName" id="modelName" class="form-control" type="text" placeholder="" value="${vo.modelName }">
												      </div>
												      <label class="col-md-1 control-label required">模型名称(英)</label>
												      <div class="col-md-3">
												         <input name="modelCode" id="modelCode" class="form-control"type="text" placeholder="" value="${vo.modelCode }">
												      </div>
												      <label class="col-md-1 control-label required">模型分类</label>
												      <div class="col-md-3">
															<select name="modelClassId" id="modelClassId">
																<c:forEach items="${class}" var="k">
																	<option value="${k.id }">${k.classCode }</option>
																</c:forEach>						
															</select>		         
												      </div>
												    </div>
												    <div class="form-group">
												      <label class="col-md-1 control-label">模型描述</label>
												      <div class="col-md-3">
												         <input name="modelDesc" id="modelDesc" class="form-control" type="text" placeholder="" value="${vo.modelDesc }">
												      </div>
												      <label class="col-md-1 control-label required">列表名称</label>
												      <div class="col-md-3">
												         <input name="tableName" id="tableName" class="form-control" type="text" placeholder="" value="${vo.tableName }">
												      </div>
												      <label class="col-md-1 control-label required">项目名称</label>
												      <div class="col-md-3">
												         <input name="projectName" id="projectName" class="form-control" type="text" placeholder="" value="${vo.projectName }">
												      </div>
												    </div>
												    <div class="from-group">
												    	<label class="col-md-1 control-label">模型分类</label>
												    	<div class="col-md-3">
												    		<select name="itemType" id="itemName">
												    			<option value="1">领域模型</option>
												    			<option value="2">系统模型</option>
												    		</select>
												    	</div>
												    </div>
													
												</div>
										<div class="panel-footer">
											<div class="btn-group">
												<button type="button" onclick="createTable()" class="btn btn-sm btn-success">生成表格</button>
											</div>
										</div>
										</form>
						</div>
						<div class="table" style="display:none">
							<div class="btn-group" style="margin-bottom: 20px">
									<button type="button" class="btn btn-sm btn-primary">修改</button>
									<button type="button" class="btn btn-sm btn-success">新增</button>
									<button type="button" class="btn btn-sm btn-info">删除</button>
								</div>
								<form method="post" id="listForm" action="#">
									<table class="table datatable1 table-bordered">
										<thead>
											<tr>
												<th class="hidden"></th>
												<th>属性名称(中)</th>
												<th>属性名称(英)</th>
												<th>属性类型</th>
												<th>属性长度</th>
												<th>是否主键</th>
												<th>是否索引</th>
												<th>是否为空</th>
												<th>默认值</th>
												<th>备注</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td class="hidden formData"><input id="boxs" type="hidden"
													value=""></td>
												<td>${vo.modelName }</td>
												<td class="text-ellipsis">${vo.modelCode }</td>
												<td>${vo.modelClassId }</td>
												<td>${vo.modelDesc }</td>
												<td>${vo.tableName }</td>
												<td>${vo.projectName }</td>
												<td>${vo.modelType }</td>
											</tr>
										</tbody>
										<tfoot>
										</tfoot>
									</table>
								</form>
								<ul class="pager pager-pills" style="margin: 0;">
									<li class="previous disabled"><a href="javascript:;">«</a></li>
									<li class="next"><a href="javascript:;">»</a></li>
								</ul>
						</div>
					</div>
				</div>
			</div>
			<!-- demo end -->
		</div>
		
		<%@ include file="../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zui/assets/datetimepicker/js/datetimepicker.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript">//自己按需求可以提取单独的js文件 存放与resources/scripts文件夹下 命名规则：form.文件名.js
         $(function(){//page_mix
        	 $.formValidator.initConfig({formID:"inputForm",debug:false,onSuccess:function(){
					$("#inputForm").submit();
					  return false;
			    },onError:function(){alert("请按提示正确填写内容");}});
			    $("#modelName").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[\u4E00-\u9FA5A-Za-z0-9_]+$",onError:"输入格式不正确"});
			  	$("#modelCode").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[a-zA-Z][a-zA-Z0-9_]*$",onError:"输入格式不正确"});
			  	$("#modelDesc").formValidator({empty:true,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"});
			  	$("#tableName").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[a-zA-Z][a-zA-Z0-9_]*$",onError:"输入格式不正确"});
			  	$("#projectName").formValidator({empty:false,onShow:"请输入资源信息"}).inputValidator({min:1,max:225,onError:"请输入0-225长度的数据"}).regexValidator({regExp:"^[a-zA-Z][a-zA-Z0-9_]*$",onError:"输入格式不正确"});
	         	
         });		
         </script>
         
         
         <script>//父子页面
         $(document).ready(function(){
    		 $('div.table').show();
    	 });
         function createTable(){
        	 
        	 //$("#inputForm").submit();//返回一个标识符
        	 var count1=0;//默认选择行数为0
	   		  $('table.datatable1').datatable({
	   			  checkable: true,
	   			  checksChanged:function(event){
	   				  this.$table.find("tbody tr").find("input#boxs").removeAttr("name");
	   				  var checkArray = event.checks.checks;
	   				  count1 = checkArray.length;//checkbox checked数量
	   				  for(var i=0;i<count;i++){//给隐藏数据机上name属性
	   					  this.$table.find("tbody tr").eq(checkArray[i]).find("input#boxs").attr("name","boxs");
	   				  }
	   			  }				
	   		  });
        	 
         };
        $(function(){        	 
        	 
        })
         </script>
	</body>
</html>
