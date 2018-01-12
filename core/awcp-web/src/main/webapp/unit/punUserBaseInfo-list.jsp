<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <%@ include file="/resources/include/common_lte_css.jsp"%>
</head>
<body>
    <!-- Main content -->
    <section class="content">
    	<div class="opeBtnGrop">
	    	<a href="#" class="btn btn-primary"  id="addBtn">新增</a>
			<a href="#" class="btn btn-success" id="updateBtn">修改</a>
			<a href="#" class="btn btn-danger" id="deleteBtn">删除</a>
			<a href="#" onclick="document.getElementById('userList').submit()" class="btn btn-primary">搜索</a>
			<a href="#" onclick="document.getElementById('userList').reset()" class="btn btn-info">清空</a>
			<a href="#" class="btn btn-info" id="syncUser">钉钉同步</a>
    	</div>
		
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-body">
						<form method="post" id="userList">
								<input type="hidden" id="currentPage" name="currentPage" value="${vos.getPaginator().getPage()}">
								<input type="hidden" id="pageSize" name="pageSize" value="${vos.getPaginator().getLimit()}">
								<input type="hidden" id="totalCount" name="totalCount" value="${vos.getPaginator().getTotalCount()}">				
								<div class="row form-group">
									<div class="col-md-2">
										<div class="input-group">
								                <span class="input-group-addon">姓名</span>
								                <!-- /btn-group -->
								                <input name="name" type="text" id="name" class="form-control"  value="${vo.name}">
							            </div>
									</div>
									<div class="col-md-2">
										<div class="input-group">
							                <span class="input-group-addon">部门</span>
							                <!-- /btn-group -->
							                <input name="deptName" class="form-control" id="deptName" type="text" value="${vo.deptName}">
							            </div>
									</div>
									<div class="col-md-2">
										<div class="input-group">
							                <span class="input-group-addon">职务</span>
							                <!-- /btn-group -->
							                <input name="userTitle" class="form-control" id="userTitle" type="text" value="${vo.userTitle}">
							            </div>
									</div>
									<div class="col-md-2 ">
										<div class="input-group">
							                <span class="input-group-addon">手机</span>
							                <!-- /btn-group -->
							                <input name="mobile" class="form-control" id="mobile" type="text" value="${vo.mobile}">
							            </div>
									</div>
								</div>
								<table class="table table-hover">  
								    <thead>
								        <tr>
								        	<th class="hidden"></th>
								        	<th data-width="" data-field="" data-checkbox="true"></th>
								            <th>序号</th>
								            <th>姓名</th>
								            <th>部门</th>
								            <th>用户名</th>
								            <th>手机号</th>
								            <th>职务</th>
								        </tr>
								    </thead>
								    <tbody>
										<c:forEach items="${vos}" var="vo" varStatus="status">
								          <tr> 	
								            <td class="hidden formData">
								            	<input type="hidden" value="${vo.userId}">
								            </td>
								            <td></td>
								            <td>${currentPage*vos.getPaginator().getLimit()-vos.getPaginator().getLimit()+status.index +1}</td>
								            <td><a href="javascript:void(0)" data-id='${vo.userId}' class="userName">${vo.name}</a></td>
								            <td>${vo.deptName}</td>
								            <td>${vo.userIdCardNumber}</td>
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
    <%@ include file="../resources/include/common_lte_js.jsp"%>
<script>
	  $(function(){
		 var count=0;//默认选择行数为0
		 $(".table").bootstrapTable({
			 pageSize:parseInt($("#pageSize").val()),
        	 pageNumber:parseInt($("#currentPage").val()),
        	 totalRows:parseInt($("#totalCount").val()),
        	 sidePagination:"server",
        	 pagination:true,
        	 onPageChange:function(number, size){
        		$("#pageSize").val(size);
        		$("#currentPage").val(number);
        		$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoList.do").submit();
        	 },
        	 onClickRow:function(row,$element,field){
	       	  	  var $checkbox=$element.find(":checkbox").eq(0);
	       	  	  if($checkbox.get(0).checked){
						  $checkbox.get(0).checked=false;
						  $element.find("input[type='hidden']").removeAttr("name","boxs");
	       	  	  }else{
						  $checkbox.get(0).checked=true;
						  $element.find("input[type='hidden']").attr("name","boxs");
	       	  	  }
					  count = $("input[name='boxs']").length;
       	 	 },
        	 onClickCell:function(field,value,row,$element){
        		 var userId = parseInt($element.find("a").attr("data-id"));
    			 if(userId)
    	   				$("#userList").attr("action","<%=basePath%>unit/punUserBaseInfoGet.do?boxs="+userId).submit()
        	 },
        	 onCheck: function(row,$element){
				  $element.closest("tr").find("input[type='hidden']").attr("name","boxs");
				  count = $("input[name='boxs']").length;
        	 },
        	 onUncheck:function(row,$element){
        		 $element.closest("tr").find("input[type='hidden']").removeAttr("name");
				 count = $("input[name='boxs']").length;
        	 }, 
        	 onCheckAll: function (rows) {
        		 $.each(rows,function(i,e){
        			$("input[value='"+$($.trim(e["0"])).attr("value")+"']").attr("name","boxs");
        		 })
				 count = $("input[name='boxs']").length;
             },
             onUncheckAll: function (rows) {
            	 $.each(rows,function(i,e){
        			$("input[value='"+$($.trim(e["0"])).attr("value")+"']").removeAttr("name");
        		 })
				 count = $("input[name='boxs']").length;
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
  				Comm.alert("请选择一个用户进行操作");
  			}
  			return false;
  		});
  		
      	//delete
      	$("#deleteBtn").click(function(){
      		if(count<1){
      			Comm.alert("请至少选择一项进行操作");
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
  				Comm.alert("请选择一个用户进行操作");
  			}
  			return false;
  		});
  		
  		$("#searchEmp").click(function(){
  			if(count == 1){
  				$("#userList").attr("action","<%=basePath%>document/view.do?id=&dynamicPageId=1769").submit();
  			}else{
  				Comm.alert("请选择一个用户进行操作");
  			}
  			return false;
  		});
  		
  		$("#importBtn").click(function(){
  			if(confirm("是否要开始进行导入？")){
  				$.get("<%=basePath%>unit/batchImportSignature.do");
  			}
  			
  		});
  		$("#syncUser").click(function(){
  			Comm.confirm("此操作会改变人员组织架构，请确认是否同步！",function(){
  				var that=this;
  				that.content("正在同步，请耐心等候.....");
  				var syncUser=function(dialog){
  					var data=Comm.getData("api/dingding/syncUser",{"_method":"get"});
  					dialog.close();
  	  	  			if(data==-1){
  	  	  				Comm.alert("钉钉中未找到可同步人员");
  	  	  			}else{
  	  	  				Comm.alert("同步成功",function(){
  			  				location.reload();
  	  	  				});
  	  	  			}
  				}
  				setTimeout(function(){syncUser(that)},50);
  				return false;
  			})
  		});
      	
      });
</script>
</body>
</html>