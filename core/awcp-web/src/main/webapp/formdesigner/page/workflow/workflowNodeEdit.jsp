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
 		<title>绑定流程页面</title>
 		<%@ include file="/resources/include/common_css.jsp" %>
	</head>
	<body id="main" class="inner-frame">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addWorkFlowBtn"><i class="icon-plus-sign"></i>保存</button>
<!-- 				<button type="button" class="btn btn-info" id="searchBtn" data-toggle="collapse" data-target="#collapseButton"><i class="icon-search"></i></button> -->
			</div>
			<div class="row" id="searchform">
				<form action="<%=basePath%>fd/workflow/queryNode.do" id="createForm" class="clearfix">
					<div class="col-sm-4">
						<input type="hidden" id="pageId" name="pageId" value="${pageId}"/>
						<div class="input-group">
							<span class="input-group-addon">流程名</span>
							<input name="wname" class="form-control" id="wname" type="text" value="${wname}"/>
						</div>
					</div>
					<div class="col-sm-4 btn-group">
						<button class="btn btn-primary" type="submit">提交</button>
					</div>
				</form>
			</div>
			<div class="row">
			<form  method="post" id="manuList">	
				<table class="table datatable1 table-bordered">
					<thead>
						<tr>
							<th class="hidden">
								<input type="hidden" name="version" value=""/>
								<input type="hidden" name="pageId" value="${pageId}"/>
								<input type="hidden" name="workFlowId" value="${workFlowId}"/>
								<input type="hidden" name="slectedNodes" value="${slectedNodes}" id="slectedNodes"/>
							</th>
							<th>结点名</th>
							<th>流程名</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${vos}" var="vo">
						<tr id="tr${vo.nodeId}">
							<td class="hidden formData">
								<input id="nodeIds" type="hidden" value="${vo.nodeId}"/>
								<input id="workFlowId" type="hidden" value="${vo.workId}"/>
								<input id="worknodeName" type="hidden" value="${vo.worknodeName}"/>
								<input id="workName" type="hidden" value="${vo.workName}"/>
								<input id="priority" type="hidden" value="${vo.priority}"/>
								<input id="version" type="hidden" value="${vo.version}"/>
							</td>
							<td>${vo.worknodeName}</td>
							<td>${vo.workName}(版本：${vo.version })</td>
						</tr>
						</c:forEach>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
				
				<%-- <table id="formDataTable" class="table table-bordered table-hover">
					<thead>
						<tr>
							<th class="hidden"><input type="hidden" id="wVars" value="${fn:length(wVars)}"/></th>
							<th>序号</th>
							<th>变量</th>
							<th>值</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody >
						<c:if test="${!empty wVars }">
						<c:forEach items="${wVars}" var="wVar" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td>${wVar.variableName}<input type="hidden"  name="wVars.variableName" id="" value="${wVar.variableName}"/></td>
								<td>${wVar.value}<input type="hidden"  name="wVars.value" id="" value="${wVar.value}"/></td>
								<td><a href="javascript:;" class="mybtn">删除</a></td>
							</tr>
						</c:forEach>
						</c:if>
					</tbody>
					<tfoot>
					</tfoot>
				</table> --%>
			</form>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<div class="input-group">
						<span class="input-group-addon">变量名</span>
						<input name="key" class="form-control" id="key" type="text"/>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="input-group">
						<span class="input-group-addon">值</span>
						<input name="value" class="form-control" id="value" type="text"/>
					</div>
				</div>
				<div class="col-sm-4 btn-group">
					<button class="btn btn-primary" id="addVarai">提交</button>
				</div>
			</div>
					
		</div>
		
		<%@ include file="/resources/include/common_js.jsp" %>
		<script type="text/javascript">
   	     var count = 0;
   	 	 var varArray = new Array();
		 $(function(){ 
        	 $('table.datatable1').datatable({
	   			  checkable: true,
	   			  ready:function(event){
	   			  	  datatableReady(event,this);
	   			  },
	   			  checksChanged:function(event){
	   				  datatableChanged(event,this);
	   			  }	
	   		  });
        	  

        	 var index = 0;//下标
        	 initVarArray();//初始化
        	 varTableEvent();//绑定事件
        	 varArrayShow();//显示
        	 var dialog;
        	 try {
 				dialog = top.dialog.get(window);
 			 } catch (e) {
 				return;
 			 }
 			 
 			//新增变量
        	 $("#addVarai").click(function(){
         		var tmpVar = {};
         		var len = varArray.length;
         		var key = $("#key").val().trim();//变量名
         		if(typeof(key)==undefined||""==key){
         			alertMessage("变量名必填");
         			return false;
         		}
         		var value = $("#value").val().trim();//值
         		if(typeof(value)==undefined||""==value){
         			alertMessage("值必填");
         			return false;
         		}
         		tmpVar["key"] = key;
         		tmpVar["value"] = value;
         		varArray[len] = tmpVar;
         		varArrayShow(varArray);//展示
         		$("#key").val("")
         		$("#value").val("")
         		return false;
        	 });
 			 
        	 //新增按钮
			$("#addWorkFlowBtn").click(function() {
				if(count<1){
					alertMessage("请至少选择一个结点");
					return false;
				}
					$.ajax({
						type : "POST",
						url : "<%=basePath%>fd/workflow/relate.do",
						data : $("#manuList").serialize(),
						success : function(data) {
							var result = JSON.stringify(data);
							dialog.close(result);
							dialog.remove();
						}
					})
					return false;
				});

			})

			//初始化变量Array
			function initVarArray(){
			 	var total = $("#wVars").val();
			 	for(var j=0;j<total;j++){
					var temp = {};
					var tr = $('#formDataTable tbody tr').eq(j);
					var variableName = $(":hidden[name='wVars.variableName']", tr).val();
					temp["key"] = variableName; 
					var value = $(":hidden[name='wVars.value']", tr).val();
					temp["value"] = value; 
					varArray[j] = temp;
				}
			 	return false;
		 	}
		
			//展示
			function varArrayShow() {
				var result = "";
				for (var i = 0; i < varArray.length; i++) {
					var tmp = i + 1;
					result = result
							+ "<tr>"
							+ "<td class='hidden formData'></td>"
							+ "<td>"+ tmp+ "</td>"
							+ "<td>"+ varArray[i].key
							+ "<input type='hidden' name='vars["+i+"].variableName' value='"+varArray[i].key+"'/></td>"
							+ "<td>" + varArray[i].value+ "<input type='hidden' name='vars["+i+"].value' value='"+varArray[i].value+"'</td>"
							+ "<td><a class='mybtn' id='"+tmp+"'>删除</a></td>"
					"</tr>";
				}
				$("#formDataTable>tbody").html(result);
				varTableEvent();
				return false;
			}

			//table事件
			function varTableEvent() {
				//行事件
				$("#formDataTable tbody tr").dblclick(function() {
					var index = $(this).find("td a").eq(0).attr("id");
					if (typeof (index) == undefined || "" == index) {
						return false;
					}
					var vars = $(this).find(":input[type='hidden']");
					var key = vars.eq(0).val();
					var value = vars.eq(1).val();
					varArray.splice(index - 1, 1);
					varArrayShow(varArray);
					$("#key").val(key);
					$("#value").val(value);
					return false;
				});
				
				//删除事件
				$(".mybtn").click(function() {
					var index = $(this).attr("id");
					varArray.splice(index - 1, 1);
					varArrayShow(varArray)
					return false;
				});

			};
			
			function datatableReady(event,obj){//ready.zui.datatable
				  var _$row = obj.$rows;
				  var _$table = obj.$table;
  				  var checkedRows='${slectedNodes}';
  				  if(checkedRows==""){
  					  return
  				  }else{
  					  var result = $.parseJSON(checkedRows);
  					  count = result.length;
  					 $.each(result,function(n,item){
	   				    _$row.filter('[data-id="tr'+item.nodeId+'"]').addClass("active");
	   					_$table.find("tbody tr#tr"+item.nodeId).find("input#nodeIds").attr("name","nodes["+n+"].nodeId");
  					 	_$table.find("tbody tr#tr"+item.nodeId).find("input#workFlowId").attr("name","nodes["+n+"].workId");
  					    _$table.find("tbody tr#tr"+item.nodeId).find("input#worknodeName").attr("name","nodes["+n+"].worknodeName");
  					    _$table.find("tbody tr#tr"+item.nodeId).find("input#workName").attr("name","nodes["+n+"].workName");
  					  	_$table.find("tbody tr#tr"+item.nodeId).find("input#priority").attr("name","nodes["+n+"].priority");
  					  	_$table.find("tbody tr#tr"+item.nodeId).find("input#version").attr("name","nodes["+n+"].version");
	   				  });
  				  };
			};
			function datatableChanged(event,obj){//checksChanged.zui.datatable
				  var _$table = obj.$table; 
			      var checkArray = event.checks.checks;
 				  count = checkArray.length;//checkbox checked数量
				  _$table.find("tbody .formData").find("input").removeAttr("name");
 				  for(var i=0;i<count;i++){//给隐藏数据机上name属性
 					 	_$table.find("tbody tr#"+checkArray[i]).find("input#nodeIds").attr("name","nodes["+i+"].nodeId");
  					  	_$table.find("tbody tr#"+checkArray[i]).find("input#workFlowId").attr("name","nodes["+i+"].workId");
  					  	_$table.find("tbody tr#"+checkArray[i]).find("input#worknodeName").attr("name","nodes["+i+"].worknodeName");
  					  	_$table.find("tbody tr#"+checkArray[i]).find("input#workName").attr("name","nodes["+i+"].workName");
  						_$table.find("tbody tr#"+checkArray[i]).find("input#priority").attr("name","nodes["+i+"].priority");
  						_$table.find("tbody tr#"+checkArray[i]).find("input#version").attr("name","nodes["+i+"].version");
 				  }
			};
			
		</script>
	</body>
</html>
