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
		<form id="form" method="post" action="document/refreshState.do">
			<button id="t">保存</button>
			<label >不知道：</label>
				<input name="test"  id="test" type="text"  value="">
				
				<label class="col-md-2 control-label">所属高新技术领域</label>
		<div class="col-md-4">
			<input name="test"  id="test" type="text"  value="">
		</div>
	</form>
	<%@include file="../include/foot.jsp"%>
	<script type="text/javascript">
		$(document).ready(function(){
			
			$("#t").click(function(){
				var v= $("#test").val();
				alert(v);
			});
			 var json=[{'id':'001','itemName':'zyg'},{'id':'002','itemName':'ljw'},{'id':'003','itemName':'wjc'},{'id':'004','itemName':'fq'}];
			//var json1=[{'id':'001','text':'zyg'},{'id':'002','text':'ljw'},{'id':'003','text':'wjc'},{'id':'004','text':'fq'}]; 
			var websites = ["Google", "NetEase", "Sohu", "Sina", "Sogou", "Baidu", "Tencent",
			                  "Taobao", "Tom", "Yahoo", "JavaEye", "Csdn", "Alipay","set","sita","sit",
			                  "succcess","small","sweet","smart","solo","suggest","secret","simaliery","simulation","simple",
			                  "sample","security","since","sdf"
			                  ];
			
			var emails = [
			                           { name: "Peter Pan", to: "peter@pan.de" },
			                          { name: "Molly", to: "molly@yahoo.com" },
			                          { name: "Forneria Marconi", to: "live@japan.jp" },
			                          { name: "Master <em>Sync</em>", to: "205bw@samsung.com" },
			                          { name: "Dr. <strong>Tech</strong> de Log", to: "g15@logitech.com" },
			                          { name: "Don Corleone", to: "don@vegas.com" },
			                          { name: "Mc Chick", to: "info@donalds.org" },
			                          { name: "Donnie Darko", to: "dd@timeshift.info" },
			                          { name: "Quake The Net", to: "webmaster@quakenet.org" },
			                          { name: "Dr. Write", to: "write@writable.com" },
			                         { name: "GG Bond", to: "Bond@qq.com" },
			                         { name: "Zhuzhu Xia", to: "zhuzhu@qq.com" }
			                      ];
			
		 	 
			require.config({
			    paths: {
			        jquery: '${ctxBase }/resources/autocomplete/lib/jquery',
			    }
			});
			 
			require(['jquery'], function($) {
				$("#test").autocomplete(websites,{ 
					//width: 320, //下拉框宽度
					max: 20, 	//下拉框的提示项最多有几条
					highlight: false, //没搞清楚，高亮？没效果啊
					multiple: true, //是否支持多选
					multipleSeparator: ",", //多选分隔符
					scroll: true, //是否可以滚动
					scrollHeight: 300 //滚动窗的高度
					}); 
			});  
			 
			 
			
			/*  $("#test").autocomplete(websites,{ 
				//width: 320, //下拉框宽度
				max: 20, 	//下拉框的提示项最多有几条
				highlight: false, //没搞清楚，高亮？没效果啊
				multiple: true, //是否支持多选
				multipleSeparator: ",", //多选分隔符
				scroll: true, //是否可以滚动
				scrollHeight: 300 //滚动窗的高度
				});   */
			
			/* 
			$("#test").autocomplete(json,{ 
				//width: 320, 
				max: 4, 
				highlight: false, 
				//multiple: true, 
				multipleSeparator: "", 
				scroll: true, 
				scrollHeight: 300 ,
				formatItem:function(item){
					//return 的值用于与输入框中的值相匹配
					return item.itemName;
				},
				formatResult:function(item){
					//return的值用于设置 input框的value
					return item.itemName;
				}
				});
			//.result(function(){})设置选中之后的触发的事件 */
			
			<%--  $("#test").autocomplete("<%=basePath%>fm/tc/s2t.do",{ 
					//width: 320, //下拉框宽度
					delay:1000,
					dataType:"json",
					max: 20, 	//下拉框的提示项最多有几条
					highlight: false, //没搞清楚，高亮？没效果啊
					multiple: true, //是否支持多选
					multipleSeparator: ",", //多选分隔符
					scroll: true, //是否可以滚动
					scrollHeight: 300, //滚动窗的高度
					formatItem:function(item){
						//return 的值用于与输入框中的值相匹配
						return item.itemName;
					},
					formatResult:function(item){
						//return的值用于设置 input框的value
						return item.id;
					},
					parse:function(data){
						return $.map(data,function(item){
							return {
								data:item,
								value:item.itemName,
								result:item.id
							};
						});
					}
					});
			 --%>
			
		});
		
		
		
	
		
		$("#parent").onchange(function(){
		var $component = $("#child");
			var code  = $(this).val();
$.ajax({
	type:'POST',
	data:code,
	url:basePath+'document/refreshState.do?componentId='+$component.attr("id"),
	success:function(result){
		//判断组件类型
	//var type=$component.attr("type");
		//控件类型是select
	$component.empty();
	Object json = eval(result);
	$.each(json,function(idx,item){
		//var templateId = $("#templateId option").eq(0).val();
		$component.append("<option value=\""+item.code+"\">"+item.dataValue+"</option>");
	});
	//$component.append(result);
	},
	error : function(XMLHttpRequest,textStatus,errorThrown) {
		alert(errorThrown);
	}
});
		});
		
	</script>


</body>

</html>