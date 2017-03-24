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
<link rel="stylesheet" href="<%=basePath%>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/>
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
				</ul>
			</div>
			<div class="row tab-content">
				<div class="tab-pane active" id="tab1"><%@ include file="/formdesigner/page/component/approvalStatus/basicInfo.jsp" %></div>
				<div class="tab-pane" id="tab2"><%@ include file="/formdesigner/page/component/common/validators.jsp" %></div>
				<div class="tab-pane " id="tab3"><%@ include file="/formdesigner/page/component/common/hidden-disabled-readonly-defaultvalue.jsp" %></div>
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
			
			//var url = "<%=basePath%>component/validateComponentNameInPage.do?dynamicPageId="+$("#dynamicPageId").val()+"&componentName="+$("#name").val()+"&componentId="+$("#pageId").val();
			//alert(url);
			
			//validator
			$.formValidator.initConfig({
				submitOnce:true,
				formID:"componentForm",
				autoTip:false,
				onError:function(msg,obj,errorlist){              
					alert(msg);          
				}    
			});/*
			$("#name").formValidator({onFocus:"请输入组件名称(至少1位,最多32位)",onShow:"请输入名称",onCorrect:"符合要求"})
				.inputValidator({min:1,max:100,empty:{leftEmpty:false,rightEmpty:false},onError:"请填写名称"})
				.regexValidator({regExp:"[_a-zA-Z][_a-zA-Z0-9]*",dataType:"string",onError:"名称只能包含字母、数字、下划线(以字母或下划线开头)"})
				.ajaxValidator({
					dataType : "json", 
					//data : "dynamicPageId="+$("#dynamicPageId").val()+"&componentName="+$("#name").val()+"&componentId="+$("#pageId").val(),
					//data : {"dynamicPageId":$("#dynamicPageId").val(),"componentName" : $("#name").val(),"componentId":$("#pageId").val()},
					async : false, 
					url : "<%=basePath%>component/validateComponentNameInPage.do?dynamicPageId="+$("#dynamicPageId").val()+"&componentName="+$("#name").val()+"&componentId="+$("#pageId").val(),
					//buttons : $("#saveComponent"),
					success :  function(data){
						
						return data;
					},
					onError : "该名称不可用，请更换名称"
				});*//**/
			$("#dataItemCode").formValidator({onFocus:"请选择或输入数据源",onCorrect:"符合要求"})
				.inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false},onError:"请选择或输入数据源"});
			$("#order").formValidator({onShow:"请输入序号",onFocus:"请输入顺序",onCorrect:"合法"})
				.regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
			$("#rowCount").formValidator({onShow:"请输入行数",onFocus:"请输入行数",onCorrect:"合法"})
				.regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
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
		
		
		//点击，弹出流程列表页面
        	$("#workflowSelect").click(function(){
        		top.dialog({ 
					id: 'select-workflow-dialog' + Math.ceil(Math.random()*1000000),
					title: '选择流程',
					url: "<%=basePath%>fd/workflow/templateList.do?type=2",
					onclose : function() {
						if (this.returnValue) {
							$("#workflowName").val(this.returnValue.name);
							$("#workflowId").val(this.returnValue.id);
						}
					}
				}).width(400).height(500).show();//document.getElementById("workflowName")
        	});
        	
        	

        	$("#submitSelect").click(function(){
        		
        		var workflowId=$("#workflowId").val();
        		var workflowName=$("#workflowName").val();
        		var pageId=$("#dynamicPageId").val();
        		
        		var url = basePath+"fd/workflow/notBindNodeListByAjax.do?workflowId="+workflowId+"&workflowName="+workflowName+"";
				if(workflowId!=""&&workflowName!=""&&pageId!=""){
				
			    $.ajaxSetup({ 
				  async : false
				}); 
			
	 		    $.get(url,function(data){
	 		       $("#boxId").empty();
	 		       $("#showType").empty();
		       		$.each(data,function(i,item){
		 	 		$("#boxId").append(""+
		 	 	    "<tr>"+
			            "<td><span>"+item.name+"</span></td>"+
			            "<td>"+
			            "<input type='checkbox' name='flowNodes"+item.id+"' value='"+item.name+"'/>"+
			            "</td>"+
			         "</tr>"+
		 	 		 "");
		 	 		 //填充类别数据
		 	 		 $("#showType").append(""+
		 	 		    "<option value='"+item.name+"'>"+item.name+"</option>"+
		 	 		 "")
		 	 		 
		       		});
	            });
				
				}
				
           });
           
           
           //此方法用于数据的回显
           function showValue(nodes){
           		
           	    var workflowId=$("#workflowId").val();
        		var workflowName=$("#workflowName").val();
        		var pageId=$("#dynamicPageId").val();
        		
        		var url = basePath+"fd/workflow/notBindNodeListByAjax.do?workflowId="+workflowId+"&workflowName="+workflowName+"";
				if(workflowId!=""&&workflowName!=""&&pageId!=""){
				
			    $.ajaxSetup({ 
				  async : false
				}); 
			
	 		    $.get(url,function(data){
	 		       $("#boxId").empty();
	 		       $("#showType").empty();
		       		$.each(data,function(i,item){
		       		var str="";
		       		if(nodes.toString().indexOf(item.name)!='-1'){
		       			str="<input type='checkbox' name='flowNodes"+item.id+"' checked='checked' value='"+item.name+"'/>"
		       		}else{
		       			str="<input type='checkbox' name='flowNodes"+item.id+"' value='"+item.name+"'/>"
		       		}
		 	 		$("#boxId").append(""+
		 	 	    "<tr>"+
			            "<td><span>"+item.name+"</span></td>"+
			            "<td>"+
			            ""+str+""+
			            "</td>"+
			         "</tr>"+
		 	 		 "");
		 	 			
		 	 		//填充类别数据
		 	 		 $("#showType").append(""+
		 	 		    "<option value='"+item.name+"'>"+item.name+"</option>"+
		 	 		 "");
		 	 			
		       		});
		       		
	            });
				
				}
           
           }
	
	</script>
</body>
</html>