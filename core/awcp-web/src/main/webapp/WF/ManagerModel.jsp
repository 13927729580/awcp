<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/head/head.jsp"%>
</head>
<body>
	<div class="am-u-md-6" style="width: 100%">
		<form class="am-form" id="form" method="post" action="">
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd">模块</div>
				<%
					int num = Integer.parseInt(request.getParameter("num"));
					String title = request.getParameter("title");
				%>
				<div class="am-g am-margin-top">
					<div class="am-u-sm-4 am-u-md-2 am-text-right">模块标题</div>
					<div class="am-u-sm-8 am-u-md-4">
						<input type="text" id="title" class="am-input-sm"
							value="<%=title%>">
					</div>
					<div class="am-hide-sm-only am-u-md-6">*必填，不可重复，注意按标题顺序来填写内容!</div>
				</div>

				<%
					for (int i = 0; i < num; i++) {
				%>
				<div class="am-g am-margin-top">
					<div class="am-u-sm-4 am-u-md-2 am-text-right">模块内容</div>
					<div class="am-u-sm-8 am-u-md-10">
						<select data-am-selected="{btnSize: 'sm'}" id="selectType<%=i %>">
							<option value="text">纯文本</option>
							<option value="htmla">超链接</option>
							<option value="table">table</option>
							<option value="form">表单</option>
						</select>
					</div>
				</div>
				<div class="am-g am-margin-top-sm" id="text">
					<div class="am-u-sm-12 am-u-md-2 am-text-right admin-form-text">
						文本内容</div>
					<div class="am-u-sm-12 am-u-md-10">
						<textarea id="textarea<%=i %>" rows="10" placeholder="请使用逗号来分隔要显示的内容"></textarea>
					</div>
				</div>
				
				<div class="am-g am-margin-top">
					<div class="am-u-sm-4 am-u-md-2 am-text-right">请输入列</div>
					<div class="am-u-sm-8 am-u-md-4">
						<input type="text" id="col<%=i%>" class="am-input-sm">
					</div>
					<div class="am-hide-sm-only am-u-md-6">*共12列，输入所占几列</div>
				</div>
				<%
					}
				%>
			</div>
			<div class="am-margin">
				<A href="javascript:void(0)" onclick="GenTemplete()" id="ri"
					target="right"><button type="button"
						class="am-btn am-btn-success">生成模块</button></A>
			</div>
		</form>
		<script type="text/javascript">
		function GenTemplete() {
			var title=$("#title").val();
			var num=<%=num%>;
			var str='';
			for(var i=0;i<num;i++)
			{
				str+='&textarea'+i+'='+$("#textarea"+i).val();
				str+='&selectType'+i+'='+$("#selectType"+i).get(0).selectedIndex;
				str+='&col'+i+'='+$("#col"+i).val();
			}
			$("#form").attr("action","<%=basePath%>WF/Jflow.jsp?num=" 
					+ <%=num%> + "&title="+ title+"&"+str);
			$("#form").submit();
		}
</script>
	</div>
</body>
</html>