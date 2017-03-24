<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/base/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>autocomplete测试</title>
<base href="<%=basePath%>">
<%@include file="../include/head.jsp"%>
</head>
<body class="C_formBody">
<div class="C_btnGroup clearfix">
		<div class="C_btns">
			<button class="btn btn-info" id="txx" target="什么都不是"><i class="icon-save"></i>保存</button>
		</div>
</div>


	<div class="form-group">
		<label class="col-md-2 control-label">姓名：</label>
		<div class="col-md-4">
			<input name="name" class="form-control" id="name" type="text"  value="">
		</div>
	</div>	
		<div class="form-group">
			<label class="col-md-2 control-label">不知道：</label>
			<div class="col-md-4">
				<!-- <select id="test" name="test" style="width:300px" >
					<option></option>
					<option>xx</option>
					<option value="sss">jj</option>
					<option>cc</option>
					<option>xx</option>
					<option>00</option>
					<option>rr</option>
					<option>qq</option>
				</select>  -->
				<input name="test"  id="test" type="text"  value="">
				</div>
		</div>
		
		




	
	<%@include file="../include/foot.jsp"%>
	<script type="text/javascript">
	/* 	
		$("#btn").click(function(){
			
			var tem= $("#test").select2("val");
			alert(tem);
		}); */
		
		
		$(document).ready(function(){
			
			 var json=[{'id':'001','itemName':'zyg'},{'id':'002','itemName':'ljw'},{'id':'003','itemName':'wjc'},{'id':'004','itemName':'fq'}];
			
			
			var json1=[{'id':'007','text':'zyg'},{'id':'009','text':'ljw'},{'id':'100','text':'wjc'},{'id':'004','text':'fq'}]; 
			
			
			var websites = ["Google", "NetEase", "Sohu", "Sina", "Sogou", "Baidu", "Tencent",
			                  "Taobao", "Tom", "Yahoo", "JavaEye", "Csdn", "Alipay"
			                  ];
			$("#txx").click(function(){
			var act =$(this);
			var id= act.id;
			alert(id);
			var target = act.attr("target");
			alert(target);
			alert(act.target);
		});
			 
			
			/*  $("#test").AutoComplete(websites);  */
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 //格式化option中的内容
		/* 	  function format(state) {
				  //  if (!state.id) return state.name; // optgroup
				    return  state.itemName;
				}  */
			 
			 
			 
		//基于select形式的select2 不能有data	 
		/* 	 $("#test").select2(); */
			 
			//如果不是{id:'xx',text:'xxx'} 的形式，通过text指定 key作为option中的内容
		 /*   $("#test").select2({
				 data:{results:json,text:function(item){
					 return item.itemName;
				 }},
				// minimumInputLength: 0,
				 //multiple:true,
				// placeholder: "Select a State",
				 formatResult:format,
				 formatSelection:format ,
				 //query:function(query){
				//	 query.callback({results:json});
				// } 
		  
		  });   */
		
		
		
		 /*   $("#test").select2({
				 data:{results:json,text:'itemName'},
				// minimumInputLength: 0,
				 //multiple:true,
				// placeholder: "Select a State",
				 formatResult:format,
				 formatSelection:format  //,
				// query:function(query){
				//	 query.callback({results:json});
				// } 
		  
		  });   
		   */
		
		
			 //设置select2的选中项
			 //$("#test").select2("data",{'id':'002','itemName':'ljw'});
			 
			
			<%--  $("#test").select2({
				 placeholder: "Select a State",
				 ajax:{
					 url: "<%=basePath%>/fm/tc/s2tid.do",
				     dataType: 'json',
				     quietMillis: 100,
				     data: function (term, page) { // page is the one-based page number tracked by Select2
				            return {
				                q: term, //search term
				                page_limit: 10, // page size
				                page: page // page number
				            };
				        },
				     results: function (data) {
				            return  {results:data};
				        }
				 }
			 });  --%>
			 
			 
				
			
			//select2 ajax
			 <%--  $("#test").select2({
				 placeholder: "Select a State",
				 ajax:{
					 url: "<%=basePath%>/fm/tc/s2t.do",
				     dataType: 'json',
				     quietMillis: 100,
				     data: function (term, page) { // page is the one-based page number tracked by Select2
				            return {
				                q: term, //search term
				                page_limit: 10, // page size
				                page: page // page number
				            };
				        },
				     results: function (data) {
				            return  {results:data};
				        }
				 },
				 formatResult:format,
				 formatSelection:format}); 
			 --%>
			
			
			
			
				});
		
		
	</script>


</body>

</html>