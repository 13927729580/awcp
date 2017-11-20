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
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<meta name="renderer" content="webkit">
 		<title>菜单查询页面</title>
 		<%-- <jsp:include page="" flush=”true”/> --%>
 		<%@ include file="../resources/include/common_css.jsp" %><!-- 注意加载路径 -->
 		<link rel="stylesheet" href="<%=basePath%>resources/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
		<style>
        .goback {
            border-bottom: 2px solid #69BDCC;
            height: 35px;
            line-height: 35px;
            cursor: pointer;
            padding-left: 15px;
        }

        .main {
            padding: 15px;
            border-top: 2px solid #69BDCC;
        }

        .useExplain {
            padding: 15px;
            text-align: left;
            word-break: break-all;
            word-wrap: break-word;
        }

            .useExplain ul li {
                height: 35px;
                line-height: 35px;
                border-bottom: 1px solid #69BDCC;
            }

        .main ul li {
            border-bottom: 1px solid #69BDCC;
        }

        .useExplain ul li input {
            background: #17adef;
            width: 180px;
            height: 28px;
            line-height: 28px;
            color: #fff;
            border: 0;
            border-radius: 3px;
        }

        .alertTitle img {
            width: 15%;
            margin-right: 5px;
        }

        .fixDiv {
            position: fixed;
            z-index: 999999999999;
            background-color: white;
            height: 320px;
            width: 480px;
        }


        body {
            /*filter: Alpha(opacity=60);
            background-color: black;
            top: 0;
            left: 0;
            opacity: 0.6;*/
        }
    </style>
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="breadcrumb">
				<ul class="breadcrumb">
		          <li><i class="icon-location-arrow icon-muted"></i></li>
		          <li class="active">用户管理</li>
		        </ul>
			</div>
			
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="addBtn"><i class="icon-plus-sign"></i>新增</button>
				<button type="button" class="btn btn-info" id="deleteBtn"><i class="icon-trash"></i>删除</button>
				<button type="button" class="btn btn-warning" id="updateBtn"><i class="icon-edit"></i>修改</button> 
				<button type="button" class="btn btn-warning" id="importBtn"><i class="icon-edit"></i>导入用户签名</button> 
			</div>
			<div class="row" id="searchform">
				<form method="post" action="<%=basePath%>unit/punUserBaseInfoList.do"
					id="createForm">
					<input type="hidden" name="currentPage" value="0" />						
					<div class="col-md-2">
						<div class="input-group">
							<span class="input-group-addon">姓名</span>
							<input name="name" class="form-control" id="name" type="text" value="${vo.name}"/>
						</div>
					</div>
					<div class="col-md-2">
						<div class="input-group">
							<span class="input-group-addon">部门</span>
							<input name="deptName" class="form-control" id="deptName" type="text" value="${vo.deptName}"/>
						</div>
					</div>
					<div class="col-md-2">
						<div class="input-group">
							<span class="input-group-addon">职务</span>
							<input name="userTitle" class="form-control" id="userTitle" type="text" value="${vo.userTitle}"/>
						</div>
					</div>
					<div class="col-md-2">
						<div class="input-group">
							<span class="input-group-addon">手机号</span>
							<input name="mobile" class="form-control" id="mobile" type="text" value="${vo.mobile}"/>
						</div>
					</div>
					<div class="col-md-2 btn-group pull-right">
						<button class="btn btn-primary" type="submit">提交</button>
						
						<button class="btn" data-toggle="collapse" data-target="#collapseButton" type="reset">取消</button>
					</div>
				</form>
			</div>
			
			<div class="row" id="datatable">
			<form  method="post" id="userList">	
				<table class="table datatable table-bordered">
					<thead>
						<tr>
							<th class="hidden"><input type="hidden" name="currentPage" value="${currentPage}">	</th>
							<th data-width="50px">序号</th>
							<th data-width="100px">姓名</th>
							<th >部门</th>
							<th>用户名</th>
							<th>手机号</th>
							<th>职务</th>
						</tr>
					</thead>
					<tbody>	            		
					<c:forEach items="${vos}" var="vo" varStatus="status">
			          <tr> 	
			            <td class="hidden formData">
			            	<input id="boxs" type="name" value="${vo.userId}">
			            </td>
			            <td>${status.index +1}</td>
			            <td><a href="javascript:void(0)" onclick="update('${vo.userId}')">${vo.name}</a></td>
			            <td>${vo.deptName}</td>
			            <td>${vo.userName}</td>
			            <td>${vo.mobile}</td>
			            <td>${vo.userTitle}</td>
			          </tr>
			          </c:forEach>
					</tbody>
				</table>
				</form>
			</div>
			<div class="row navbar-fixed-bottom text-center" id="pagers">
				<sc:PageNavigation dpName="vos"></sc:PageNavigation> 
			</div>
		</div>
	 
	 <%@ include file="../resources/include/common_js.jsp" %>
	 <script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
	 <script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
	 <script type="text/javascript">
			//update
			function update(id){
		   			if(id)
		   				$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoGet.do?boxs="+id).submit()
		    }
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
          	//新增
      		$("#addBtn").click(function(){
      			var url = "<%=basePath%>unit/intoPunUserBaseInfo.do";
      			location.href = url;
      			return false;
      		})
      		
      		$("#updateBtn").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoGet.do").submit();
      			}else{
      				alert("请选择一个用户进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#deleteBtn").click(function(){
          		if(count<1){
          			alertMessage("请至少选择一项进行操作");
          			return false;
          		}
          		if(window.confirm("确定删除？")){
          			$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoDelete.do").submit();
          		}
      			return false;
          	})
          	
          		//update
      		$("#dataAnalysis").click(function(){
      			if(count <= 1){
      				$("#userList").attr("action","<%=basePath%>ym/sevenDateAnalysisUserId.jsp").submit();
      			}else{
      				alert("请选择一个用户进行操作");
      			}
      			return false;
      		});
      		
      		$("#searchEmp").click(function(){
      			if(count == 1){
      				$("#userList").attr("action","<%=basePath%>document/view.do?id=&dynamicPageId=1769").submit();
      			}else{
      				alert("请选择一个用户进行操作");
      			}
      			return false;
      		});
      		
      		$("#excel").click(function(){
      			loadHaveBg('middle');
      			location.href="<%=basePath%>ymUtils/excelExport.do";
      			cancel();
      			return false;
      		});
      		$("#importBtn").click(function(){
      			alert("请将与用户名对应的图片放置到upload/signature文件夹中！");
      			if(confirm("是否要开始进行导入？")){
      				$.get("<%=basePath%>unit/batchImportSignature.do");
      			}
      			
      		});
          	
          });
          
            window.onload = function () {
            //fixDiv

            //滚动条高度
            var clientHeight = window.document.body.clientHeight;
            $(".fixDiv").css("height", clientHeight + "px");
        }

     

        //带弹出层的加载
        function loadHaveBg(position) {
            Popup.Show(true, "30000000", true, position, "", "", "");
        }

        //隐藏抽奖提示框
        function hideLottery() {
            Popup.HideLottery();
        }



        //取消
        function cancel() {
            Popup.HideCamera();
        }
		</script>
	</body>
</html>
