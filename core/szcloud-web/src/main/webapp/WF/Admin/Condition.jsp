<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/head/head1.jsp"%>
<%
	String CondType=request.getParameter("CondType");
	String FK_Flow=request.getParameter("FK_Flow");
	String FK_MainNode=request.getParameter("FK_MainNode");
	String FK_Node=request.getParameter("FK_Node");
	String FK_Attr=request.getParameter("FK_Attr");
	String DirType=request.getParameter("DirType");
	String ToNodeID=request.getParameter("ToNodeID");
	String CurrentCond="";
	ConditionModel cmodel=new ConditionModel(CurrentCond,CondType,FK_Flow,FK_MainNode,FK_Node,FK_Attr,DirType,ToNodeID);
	cmodel.init();
%>
 <script type="text/javascript">
 function SelectAll(cb_selectAll) {
	    var arrObj = document.all;
	    if (cb_selectAll.checked) {
	        for (var i = 0; i < arrObj.length; i++) {
	            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox') {
	                arrObj[i].checked = true;
	            }
	        }
	    } else {
	        for (var i = 0; i < arrObj.length; i++) {
	            if (typeof arrObj[i].type != "undefined" && arrObj[i].type == 'checkbox')
	                arrObj[i].checked = false;
	        }
	    }
	}
        var currCond = '<%=CurrentCond %>';

        function changeCond(c) {
            if (c == null || c.value.length == 0) return;

            $('#mainCond').layout('panel', 'center').panel('setTitle', c.text);
            $('#context').attr('src', c.value + '.jsp?CondType=<%=CondType %>&FK_Flow=<%=FK_Flow %>&FK_MainNode=<%=FK_MainNode %>&FK_Node=<%=FK_Node %>&FK_Attr=<%=FK_Attr %>&DirType=<%=DirType %>&ToNodeID=<%=ToNodeID %>');
        }

        $(document).ready(function () {
            if (currCond.length > 0) {
                $('#cond').combobox('select', currCond);
            }
            else {
                $('#cond').combobox('select', 'Cond');
            }
        });
    </script>
</head>
<body class="easyui-layout">
<form method="post" action="" class="am-form" id="form1">
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			<form id="form1" runat="server">
				<input type="hidden" id="FormHtml" name="FormHtml" value="">
			    <div data-options="region:'center',border:false">
			        <div id="mainCond" class="easyui-layout" data-options="fit:true">
			            <div data-options="region:'north',border:false" style="height:35px; padding:5px">
			                <label for="">
			                    	请选择方向条件设置类型：</label>
			                <select id="cond" class="easyui-combobox" name="cond" data-options="onSelect:function(rec){ changeCond(rec); }">
			                    <option value="Cond">表单条件</option>
			                    <option value="CondStation">岗位条件</option>
			                    <option value="CondDept">部门条件</option>
			                    <option value="CondBySQL">SQL条件</option>
			                    <option value="CondByPara">开发者参数</option>
			                    <option value="CondByUrl">Url条件</option>
			                </select>
			            </div>
			            <div data-options="region:'center',title:' '" style="overflow-y:hidden;">
			             <iframe id="context" scrolling="no" frameborder="0" src=""
			                    style="width: 100%; height: 100%;"></iframe>
			            </div>
			        </div>
			    </div>
			    </form>
			</div>
		</div>
	</form>
    
</body>
</html>