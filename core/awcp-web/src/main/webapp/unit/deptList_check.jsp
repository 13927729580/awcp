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
 		<title>树形菜单</title>
 		<%@ include file="../../../resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link rel="stylesheet" href="<%=basePath%>resources/styles/zTreeStyle/szcloud.css">
		<link rel="stylesheet" href="<%=basePath%>resources/styles/layout-default-latest.css"/>
	</head>
	<body id="main">
	        <input type="hidden" name="userIds" id="userIds"/>
	        <input type="hidden" name="oldGroupId" id="oldGroupId"/>
	        <input type="hidden" name="newGroupId" id="newGroupId"/>
			<div class="container-fluid">
				<div class="row">
				
				 <div class="panel-heading" id="search">
			                 
			            	<button class="btn btn-primary" id="subeq" onclick="save()">保存</button>
			     </div>
				
				 <div class="panel ui-layout-west">
					<div class="panel-body" id="p1">
					<ul id="tree1" class="ztree"></ul>
					</div>
				 </div>
				</div>
			</div>

		<%@ include file="../../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.layout-latest.js"></script>

         <script type="text/javascript">//page_group
         
         $('document').ready(function() {
         
         try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			if(dialog != null){
				data = dialog.data;
				
				if(data.userIds!=null){
					$("#userIds").val(data.userIds);
				}
				
				if(data.oldGroupId!=null){
					$("#oldGroupId").val(data.oldGroupId);
				}
			}	
			
         
         });
         var setting = {
	   			view: {
	   				showLine: false,
	   				showIcon: false
	   			},
	   			data: {
	   				simpleData: {
	   					enable: true
	   				}
	   			},
	   			callback:{
        		onClick:selectItem
    			}
	   		};
        var tNodes1 = "";
        var myLayout;
        $(function(){
        	$.ajax({
                type:"post",
                url:"<%=basePath%>queryDeptTreeData.do?type=simple",
                data:"",
                async: false,
                datatype:"json",
                success:function(json){
                        //先将date转化为json
                       tNodes1=json;
                      
                }
            });
	   		$.fn.zTree.init($("#tree1"), setting, tNodes1);
        });	
        
        
        function save(){
           try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
           var userIds=$("#userIds").val();
           //选中组织
           var groupId=$("#newGroupId").val();
           var oldGroupId=$("#oldGroupId").val();
           if($("#newGroupId").val()==""||$("#newGroupId").val()==null){
           		alert("请选择要移动的部门");
           		return false;
           }
           if(userIds!=""&&groupId!=""){
           $.ajax({
                type:"post",
                url:"<%=basePath%>punUserGroupController/userGroupEdit.do?",
                data:"oldGroupId="+oldGroupId+"&userIds="+userIds+"&groupId="+groupId,
                async: false,
                datatype:"json",
                success:function(data){
                       
                      if(data!=null&&data!=""){
                      		
                      		dialog.close();
                      }
                }
            });
            
            }
        }
        
        function selectItem(event, treeId, treeNode) {
            $("#newGroupId").val(treeNode.id);
        } 
			
        </script>
         
	</body>
</html>
