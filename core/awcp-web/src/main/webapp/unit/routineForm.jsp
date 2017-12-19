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
<head lang="en">
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="../template/AdminLTE/css/bootstrap.min.css">
    <link rel="stylesheet" href="../template/AdminLTE/css/bootstrap-table.css">
    <link rel="stylesheet" href="../template/AdminLTE/css/AdminLTE.css">
    <link rel="stylesheet" href="../template/AdminLTE/css/ionicons.min.css">
    <link rel="stylesheet" href="../template/AdminLTE/css/main.css">
    <title>用户管理</title>
</head>
<body>
    <!-- Main content -->
    <section class="content">
    	<div class="opeBtnGrop">
	    	<a href="#" class="btn btn-primary"  id="addBtn">新增</a>
			<a href="#" class="btn btn-success" id="updateBtn">修改</a>
			<a href="#" class="btn btn-danger" id="deleteBtn">删除</a>
			<a href="#" class="btn btn-info" id="importBtn">导入用户签名</a>
    	</div>
		
		<div class="row" id="searchform">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<div class="row">
							<form class="form-horizontal" method="post" action="<%=basePath%>unit/punUserBaseInfoList.do"
					id="createForm">
							<input type="hidden" name="currentPage" value="0" />	
							<div class="col-md-2">
								<div class="input-group">
					                <div class="input-group-btn">
					                  <button type="button" class="btn">姓名</button>
					                </div>
					                <!-- /btn-group -->
					                <input name="name" type="text" id="name" class="form-control"  value="${vo.name}">
					            </div>
							</div>
							<div class="col-md-2">
								<div class="input-group">
					                <div class="input-group-btn">
					                  <button type="button" class="btn">部门</button>
					                </div>
					                <!-- /btn-group -->
					                <input name="deptName" class="form-control" id="deptName" type="text" value="${vo.deptName}">
					            </div>
							</div>
							<div class="col-md-2">
								<div class="input-group">
					                <div class="input-group-btn">
					                  <button type="button" class="btn">职务</button>
					                </div>
					                <!-- /btn-group -->
					                <input name="userTitle" class="form-control" id="userTitle" type="text" value="${vo.userTitle}">
					            </div>
							</div>
							<div class="col-md-2 ">
								<div class="input-group">
					                <div class="input-group-btn">
					                  <button type="button" class="btn">手机号</button>
					                </div>
					                <!-- /btn-group -->
					                <input name="mobile" class="form-control" id="mobile" type="text" value="${vo.mobile}">
					            </div>
							</div>
							<div class="col-md-2 btn-group pull-right">
									<button type="submit" class="btn btn-primary">提交</button>
									<button type="reset" class="btn">取消</button>
							</div>
							</form>
						</div>
					</div>
					<div class="box-body"  id="datatable">
						<form  method="post" id="userList">	
								<table data-toggle="table" class="table table-hover"  data-pagination="true" id="tabProduct">  
								    <!-- <tr>  
								      <td align="center" bgcolor="#EFEFEF" name="Num"><input type="checkbox" name="checkbox" value="checkbox" /></td>  
								      <td bgcolor="#EFEFEF" Name="Num" EditType="TextBox">序号</td>  
								      <td bgcolor="#EFEFEF" Name="ProductName" EditType="DropDownList" DataItems="{text:'A',value:'a'},{text:'B',value:'b'},{text:'C',value:'c'},{text:'D',value:'d'}">商品名称</td>  
								      <td bgcolor="#EFEFEF" Name="Amount" EditType="TextBox">数量</td>  
								      <td bgcolor="#EFEFEF" Name="Price" EditType="TextBox">单价</td>  
								      <td bgcolor="#EFEFEF" Name="SumMoney" Expression="Amount*Price" Format="#,###.00">合计</td>  
								    </tr>   -->
								    <thead>
								        <tr>
								        	<th class="hidden"><input type="hidden" name="currentPage" value="${currentPage}">	</th>
								        	<th data-width="" data-field="" data-checkbox="true"></th>
								            <th data-width="" data-field="" data-EditType="TextBox">序号</th>
								            <th data-width="" data-field="" data-EditType="TextBox">姓名</th>
								            <th data-width="" data-field="" data-EditType="TextBox">部门</th>
								            <th data-width="" data-field="" data-EditType="TextBox">用户名</th>
								            <th data-width="" data-field="" data-EditType="TextBox">手机号</th>
								            <th data-width="" data-field="" data-EditType="TextBox">职务</th>
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
					
				</div>
				
			</div>
		</div>
    </section>
    <!-- /.content -->
    <script src="../template/AdminLTE/js/jquery.min.js"></script>
    <script src="../template/AdminLTE/js/editTable.js"></script>
    <script src="../template/AdminLTE/js/bootstrap-table.js"></script>
<script>
    $(function(){
        /*$("#innerContent").slimScroll({
            height: '1080px'
        });*/
    	// 
		function update(id){
	   			if(id)
	   				$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoGet.do?boxs="+id).submit()
	    }
	  $(function(){
		  var count=0;//默认选择行数为0
		 
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
    })
</script>
</body>
</html>