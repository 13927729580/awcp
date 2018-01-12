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
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
	<title>动态表单编辑页面</title>
	<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
	<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
</head>
<body id="main">
	<input type="hidden" value="${vo.dataJson}" name="dataJson" id="modelJsonArray" >		
	<input type="hidden" name="id" value="${vo.id}" id="id">
	<div id="groupform">
		<div class="col-xs-3">			
			<div class="btn-group mb10">
				<div class="btn-group">
					<button type="button" class="btn btn-sm btn-success dropdown-toggle" data-toggle="dropdown">
						<i class="icon-plus-sign"></i>新增 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="javascript:;" onclick="addLayout(this);">新增</a></li>
						<li><a href="javascript:;" onclick="addQuickLayout(this);">快捷新增</a></li>
						<li><a href="javascript:;" onclick="excelLayout(this);">excel快捷新增</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" class="btn btn-sm btn-success dropdown-toggle" data-toggle="dropdown">
						<i class="icon-edit"></i>批量修改 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li><a href="javascript:void(0)" onclick="batchModifyTreeProportion(this);">批量修改布局占比</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifyTreeHeight(this);">批量修改行高列宽</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifyTreeAlign(this);">批量修改对齐方式</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifyTreeBorder(this);">批量修改边框</a></li>
						<li><a href="javascript:void(0)" onclick="batchTreeModify(this);">同类型批量修改</a></li>
						<li><a href="javascript:void(0)" onclick="batchTreeHeight(this);">批量清除宽高</a></li>
					</ul>
				</div>
			</div>
			<div class="btn-group mb10">
				<button type="button" class="btn btn-sm btn-success" id="merageLayout1">
					<i class="icon-plus"></i>合并布局
				</button>
				<button type="button" class="btn btn-sm btn-success" id="copyLayout1">
					<i class="icon-copy"></i>复制布局
				</button>
				<button type="button" class="btn btn-sm btn-success" id="refreshLayoutOrder1">
					<i class="icon-refresh"></i>重置序号
				</button>
			</div>
			<div id="collapseButton" class="mb10">
				<input type="hidden" name="currentPage" value="1" />
				<div class="input-group">
					<span class="input-group-addon">行</span>
					<input name="rows1" class="form-control" id="rows1" type="text" value="${rows }"/>
					<span class="input-group-addon fix-border">列</span>
					<input name="columns1" class="form-control" id="columns1" type="text" value="${columns }"/>
					<span class="input-group-btn"><button class="btn btn-primary" type="button" onclick="searchLayoutInTree()">提交</button></span>
				</div>
			</div>
			<div class="panel">
				<div class="panel-body">
					<ul id="tree1" class="ztree"></ul>
				</div>
				<div>
					<button type="button" class="btn btn-sm btn-success hidden" id="addLayoutBtn" onclick="CheckAllNodes();">全选</button>
					<button type="button" class="btn btn-sm btn-success hidden" id="addLayoutBtn" onclick="CancelAllNodes();">取消</button>
				</div>
			</div>			
		</div>
		<div class="col-xs-9" style="margin-bottom:0;">
			<div class="btn-group mb10">
				<div class="btn-group">
					<button type="button" class="btn  btn-info dropdown-toggle" data-toggle="dropdown">
						<i class="icon-plus-sign"></i>添加 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<c:forEach items="${_COMPOENT_TYPE_NAME }" var="cs">
							<li><a href="javascript:void(0)" onclick="editComponent('${cs.key}','${pageId }',null);">${cs.value}</a></li>
						</c:forEach>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" class="btn  btn-info dropdown-toggle" data-toggle="dropdown">
						<i class="icon-plus-sign"></i>快捷新增 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li><a href="javascript:void(0)" onclick="quickComWithLabel();">带文本组件</a></li>
						<li><a href="javascript:void(0)" onclick="quickComByDataSource();">以数据源新增</a></li>
					</ul>
				</div>
				<div class="btn-group">
					<button type="button" class="btn  btn-info dropdown-toggle" data-toggle="dropdown">
						<i class="icon-plus-sign"></i>批量修改 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu">
						<li><a href="javascript:void(0)" onclick="modifiDataItemCode();">数据源</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifiLayout();">布局</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifiAllowNull(1);">允许为空</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifiAllowNull(0);">不允许为空</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifiStyle();">样式</a></li>
						<li><a href="javascript:void(0)" onclick="batchModifiAll(0);">批量修改</a></li>
						<li><a href="javascript:void(0)" onclick="modifiDataAlias();">批量修改数据源别名</a></li>
						<li><a href="javascript:void(0)" onclick="modifiComponentType();">批量修改组件类型</a></li>
						<li><a href="javascript:void(0)" onclick="modifiValidator();">批量修改组件校验</a></li>
						<li><a href="javascript:void(0)" onclick="modifiValidator();">批量修改组件打印样式</a></li>
						<li><a href="javascript:void(0)" onclick="modifiPrintHeight();">批量修改组件打印行高</a></li>
						<li><a href="javascript:void(0)" onclick="modifiSameType();">同类型批量修改</a></li>
					</ul>
				</div>
				<button class="btn  btn-info" id="deleteComponent">
					<i class="icon-remove"></i>删除
				</button>
				<button class="btn  btn-info" id="copyComponent">
					<i class="icon-remove"></i>复制
				</button>
				<button class="btn  btn-info" id="refreshComponentOrder">
					<i class="icon-remove"></i>重置序号
				</button>
				<button class="btn  btn-info" id="refreshContainerComponent">
					<i class="icon-remove"></i>重置包含组件
				</button>				
			</div>
			<div class="componentTable" contenteditable="false">
				<table class="table table-bordered" id="componentTable1" align="left">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox" id="checkAllComponent" /></th>
							<th width="25%">name</th>
							<th width="10%">类型</th>
							<th width="30%">数据源</th>
							<th width="15%">名称</th>
							<th width="10%">布局</th>
							<th width="5%">序号</th>
						</tr>
					</thead>
					<tbody id="componentt">
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<%@ include file="/resources/include/common_form_js.jsp" %>
	<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/dynamicpage.constant.js"></script>
	<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/form.dpcommon.js"></script>
	<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/form.cpcommons.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/map.js"></script>		
	<script type="text/javascript" src="<%=basePath%>resources/scripts/layout.js"></script>
	<script type="text/javascript" src="<%=basePath%>formdesigner/page/script/layout_tree.js"></script>
	<script type="text/javascript" src="<%=basePath%>formdesigner/page/script/data.js"></script>
	<script type="text/javascript" >//自己按需求可以提取单独的js文件 存放与resources/scripts文件夹下 命名规则：form.文件名.js
		var basePath = "<%=basePath%>";
			$(document).ready(function() {
				var dynamicPageId=$("#id").val();
				loadComponentTable(dynamicPageId);
				initLayoutTree(dynamicPageId);
			});
			
			$("#deleteComponent").click(function(){					
				var count = $(":checkbox[name='component']:checked").size();
				if(count > 0){
					var dynamicPageId = $("#id").val();
					edit("component","remove",null,null,dynamicPageId);
					return false;
				}else{
					alert("请选择组件操作");
					return false;
				}				
			});
			
			function initLayoutTree(dynamicPageId){
				//发送ajax请求
				$.ajax({
					type:"GET",
					url:"layout/listLayoutInTree.do",
					data : "dynamicPageId=" + dynamicPageId,
					async:false,
					//回调函数
					success:function(jsonData){
						var json = eval(jsonData);
						
						$.fn.zTree.init($("#tree1"), setting, json);	   	
					},
				    error: function (XMLHttpRequest, textStatus, errorThrown) { 
		                  alert(errorThrown); 
				    }
				});
			}
			
			var setting = {
				view: {
					showLine: true,
					showIcon: true,
					addHoverDom: addHoverDom,
					removeHoverDom: removeHoverDom
				},
				check:{
					enable:true,
					chkboxType: { "Y": "", "N": "" }
				},
				edit: {
					enable: true,
					editNameSelectAll: true,
					showRemoveBtn: true,
					showRenameBtn: true
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback:{
					onClick: zTreeOnClick,
					beforeEditName: onEdit,
					beforeRemove: beforeRemove
				}
			};	
			
			function onEdit(e,treeNode){
				var dynamicPageId=$("#id").val();
				//alert(dynamicPageId+","+treeId);
				var postData={};
				var treeObj = $.fn.zTree.getZTreeObj("tree1");
				postData.pageId=treeNode.id;
			    postData.dynamicPageId=dynamicPageId;
				top.dialog({
			    	id: 'add-dialog' + Math.ceil(Math.random()*10000),
			    	title: '载入中...',
			   	 	url: "formdesigner/page/layout/layoutEdit.jsp",
			    	data:postData,
			    	onclose: function () {
			          	if (this.returnValue) {
			           		var ret= this.returnValue;
			                treeNode.name = ret.name;	  							
					        var item = {"id":ret.id,"pId":ret.description,"name":ret.name};				         
					        treeObj.updateNode(treeNode,false);					    
						    alertMessage("修改成功");
				      	}
			    	}
				}).showModal();
				return false;
				
			}
			
			function addHoverDom(treeId, treeNode) {
				var sObj = $("#" + treeNode.tId + "_span");
				if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
				var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
						+ "' title='add node' onfocus='this.blur();'></span>";
				sObj.after(addStr);
				var btn = $("#addBtn_"+treeNode.tId);
				if (btn) btn.bind("click", function(){
					var dynamicPageId=$("#id").val();
					var postData={};
					postData.parentId=treeNode.id;
			        postData.dynamicPageId=dynamicPageId;
					top.dialog({
			            id: 'add-dialog' + Math.ceil(Math.random()*10000),
			            title: '载入中...',
			            url: "formdesigner/page/layout/layoutEdit.jsp",
			            data:postData,
			            onclose: function () {
		                   	if (this.returnValue) {
		                		var ret= this.returnValue;	  							
				            	var item = {"id":ret.id,"pId":ret.description,"name":ret.name};
				            	$.fn.zTree.getZTreeObj("tree1").addNodes(treeNode,item);
					    		alertMessage("添加成功");
			                }
			           }
			
					}).showModal();
					return false;
				});
			};
			
			function removeHoverDom(treeId, treeNode) {
				$("#addBtn_"+treeNode.tId).unbind().remove();
			};
			
			function beforeRemove(treeId, treeNode) {
				if(window.confirm("确定删除？")){
					var dynamicPageId=$("#id").val();
					removeTreeLayout(dynamicPageId,treeNode.id,treeNode);
				}
				return false;
			}
			
			function removeTreeLayout(dynamicPageId,layoutId,treeNode){
				var _selects=new Array();
				var treeObj = $.fn.zTree.getZTreeObj("tree1");
				var nodes = treeObj.getCheckedNodes(true);
				if(layoutId!=null){
					_selects.push(layoutId);
				}else{
					
				}
				$.ajax({
					url:"layout/deleteByAjax.do",
					type:"POST",
					async:false,
					data:{_selects:_selects.join(",")},
					success:function(ret){
						if("1"==ret){
							//fresh();
							$(":checkbox[name='allLayout']").prop("checked",false);
							loadLayoutTable(dynamicPageId);
							$.fn.zTree.getZTreeObj("tree1").removeNode(treeNode);
							alert("删除成功！");
						}else{
							alert("删除失败！");
						}
					
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) { 
		             	 alert(errorThrown); 
			    	}
				});
			}
			
			function zTreeOnClick(event, treeId, treeNode) {
				var dynamicPageId=$("#id").val();
				var objectType = "component";
 			   	$.ajax({
					type:"GET",
					url:"component/getComponentListByLayoutId.do",
					data : "dynamicPageId=" + dynamicPageId + "&layoutId=" + treeNode.id + "&pageSize=9999",
					async:false,
					//回调函数
					success:function(data){
						$("#" + objectType + "t").empty();
						$.each(data, function(idx, item) {
							addRow(objectType, item);
						});
					},
				    error: function (XMLHttpRequest, textStatus, errorThrown) { 
		                alert(errorThrown); 
				    }
				});
			};
			
			function addRow_tree(objectType, comObject) {
				//	alert(1);
				var object = commStore[objectType];
				// 标示符
				var unique = object.unique;
				// table 列
				var table = object.table;
				// 是否是超链接字段
				var href = object.href;
				item = eval("(" + comObject.content + ")");
				var str = "<tr id='" + item[unique] + "'><td><input type='checkbox' name='"
					+ objectType + "' value='" + item[unique] + "'/></td>";

				$.each(table, function(i, t) {
				// 判断是否是href字段，若是加事件，不是else
				if (href == t) {
					var name = (item['componentType']=='1008') ? item['columnName']:item[t];	//对于列组件，则显示列头名称，其他显示名称
					str += "<td><a href='javascript:void(0);' onclick='editComponent(\""
						+ item.componentType
						+ "\",\""
						+ item.dynamicPageId
						+ "\",\""
						+ item[unique]
						+ "\")'>"
						+ name
						+ "</a></td>";
				} else {
				if (item[t] != undefined) {
					if(t=='dataItemCode' && (item['componentType']=='1009' || item['componentType']=='1014')){			//对于label，列表显示文本
						str  += "<td>" + item['title'] + "</td>";
					} else if ( t == 'componentType') {
						str  += "<td>" + pageConstant.getComponentTypes().get(item[t])  + "</td>";
					}else if ( t == 'layoutName') {
						str += "<td><a href='javascript:void(0);' onclick='editLayout(\"layout\",\"update\",\""
							+ item.layoutId
							+ "\",\"null\",\""
							+ item.dynamicPageId
							+ "\")'>"
							+ item[t]
							+ "</a></td>";
					}else {
						str += "<td>" + item[t] + "</td>";
					}
				
				} else {
					if(t=='dataItemCode' && (item['componentType']=='1009' || item['componentType']=='1014')){			//对于label，列表显示文本
						str  += "<td>" + item['title'] + "</td>";
					} else if ( t == 'componentType') {
						str  += "<td>" + pageConstant.getComponentTypes().get(item[t])  + "</td>";
					} else{
						str += "<td/>";
					}
				
					}
				}
			});
			str += "</tr>";
			$("#" + objectType + "tt").append(str);
			}
			
			
         </script>
	</body>
</html>


