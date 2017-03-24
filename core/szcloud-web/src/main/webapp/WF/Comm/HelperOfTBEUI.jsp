<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/";

	%>
<!DOCTYPE HTML>
<html>
<head>
<title>词汇选择</title>
    <link href="../Scripts/easyUI/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="../Scripts/easyUI/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="../Scripts/jquery-1.7.2.min.js" type="text/javascript"></script>
    <script src="../Scripts/easyUI/jquery.easyui.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        //加载grid后回调函数
        var selectId;
        var whichTaps;
        var getTaps = 0;
        var getText;

        function LoadDataCallBack(js, scorp) {
            $("#pageloading").hide();
            if (js == "") js = "[]";

            if (js.status && js.status == 500) {
                $("body").html("<b>访问页面出错，请联系管理员。<b>");
                return;
            }
            getSelectedTabInd();
            var pushData = eval('(' + js + ')');
            $(whichTaps).tree({
                idField: 'id',
                iconCls: 'tree-folder',
                data: pushData,
                checkbox: true,
                collapsed: true,
                animate: true,
                width: 300,
                height: 400,
                lines: true,
                onContextMenu: function (e, row) {
                    e.preventDefault();
                    $(whichTaps).tree("select", row.id);
                    $('#whatToDo').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                    selectId = row.id;
                },
                onExpand: function (node) {
                    if (node) {
                    }
                },
                onClick: function (node) {
                    if (node) {
                        $(whichTaps).tree("check", node.target);
                    }
                }
            });
        }

        function getSelectedTabInd() {
            var getTab = $('#tabs').tabs('getSelected');
            var getTaps = $('#tabs').tabs('getTabIndex', getTab);
            if (getTaps == 0) {
                whichTaps = "#pulicWordsGrid";
            }
            else {
                whichTaps = "#myWordsGrid";
            }
        }

        function LoadData() {
            var params = {
                method: "getMyData",
                getTaps: getTaps
            };
            queryData(params, LoadDataCallBack, this);
        }

        //初始化
        $(function () {
            LoadData();
            $('#tabs').tabs({
                border: false,
                onSelect: function (title) {

                    if (title == '我的词汇') {
                        getTaps = 1;
                    }
                    else {
                        getTaps = 0;
                    }
                    LoadData();
                }
            });
        });
        //添加同节点
        function insertSameNode() {
            $.messager.prompt('提示', '请输入词汇内容', function (r) {
                if (r) {
                    var params = {
                        method: "insertSameNodeMet",
                        selectId: selectId,
                        setText: encodeURI(r),
                        getTaps: getTaps
                    };
                    queryData(params, LoadDataCallBack, this);
                    LoadData();
                    selectId = "";
                }
            });
        }
        //添加子节点
        function insertSonNode() {
            $.messager.prompt('提示', '请输入词汇内容', function (r) {
                if (r) {
                    var params = {
                        method: "insertSonNodeMet",
                        selectId: selectId,
                        setText: encodeURI(r),
                        getTaps: getTaps
                    };
                    queryData(params, LoadDataCallBack, this);
                    LoadData();
                    selectId = "";
                }
            });
        }
        //编辑节点
        function editNode() {
            $.messager.prompt('提示', '请输入词汇内容', function (r) {
                if (r) {
                    var params = {
                        method: "editNodeMet",
                        selectId: selectId,
                        setText: encodeURI(r),
                        getTaps: getTaps
                    };
                    queryData(params, LoadDataCallBack, this);
                    LoadData();
                    selectId = "";
                }
            });
        }
        //删除节点
        function delNode() {
            $.messager.confirm('提示', '确定删除吗?', function (r) {
                if (r) {
                    var params = {
                        method: "delNodeMet",
                        selectId: selectId,
                        getTaps: getTaps
                    };
                    queryData(params, LoadDataCallBack, this);
                    LoadData();
                    selectId = "";
                }
            });
        }

        //关闭
        function closeMet() {
        	var getText=null;
       	    window.returnValue = getText;
            window.close();
        }
        
        //保存所选
        function saveChecked() {
            var nodes = $('#tabs').tree('getChecked');
            getText = '';
            for (var i = 0; i < nodes.length; i++) {
            	//alert(JSON.stringify(nodes[i]));
                if (nodes[i].attributes["IsParent"] == 0) {
//                    if (getText != '') {
//                        getText += ',';
//                    }
                    getText += nodes[i].text;
                }
            }            
            if (getText == '') {
                $.messager.alert("提示", "您没有选中项!", "info");
                return;
            }
            window.returnValue = getText;
            window.close();
        }
        //公共方法
        function queryData(param, callback, scope, method, showErrMsg) {
            if (!method) method = 'GET';
            $.ajax({
                type: method, //使用GET或POST方法访问后台
                dataType: "text", //返回json格式的数据
                contentType: "application/json; charset=utf-8",
                url: "<%=basePath%>WF/Comm/HelperOfTBEUI.do", //要访问的后台地址
                data: param, //要发送的数据
                async: false,
                cache: false,
                complete: function () { }, //AJAX请求完成时隐藏loading提示
                error: function (XMLHttpRequest, errorThrown) {
                    callback(XMLHttpRequest);
                },
                success: function (msg) {//msg为返回的数据，在这里做数据绑定
                    var data = msg;
                    callback(data, scope);
                }
            });
        }
    </script>
</head>
<body>
    <div class="easyui-tabs" id="tabs" data-options="tabWidth:112" style="width: auto;
        height: auto;">
        <div title="全局词汇" style="height: 400px;">
            <div id="Div1" style="background-color: #fafafa; height: 25px;">
                <a id="A1" style="float: left; background-color: #fafafa; margin-left: 10px;" href="#"
                    class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'" onclick="saveChecked()">
                    确定</a>&nbsp; &nbsp;<a id="A2" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'"
                        onclick="closeMet()">关闭</a>
            </div>
            <div region="center" border="true">
                <ul class="easyui-tree" id="pulicWordsGrid" style="margin-left: 3px; margin-top: 5px;">
                </ul>
            </div>
        </div>
        <div title="我的词汇" style="height: 400px;">
            <div id="tb" style="background-color: #fafafa; height: 25px;">
                <a id="isOk" style="float: left; background-color: #fafafa; margin-left: 10px;" href="#"
                    class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'" onclick="saveChecked()">
                    确定</a>&nbsp; &nbsp;<a id="close" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'"
                        onclick="closeMet()">关闭</a>
            </div>
            <div region="center" border="true">
                <ul class="easyui-tree" id="myWordsGrid" style="margin-left: 3px; margin-top: 5px;">
                </ul>
            </div>
        </div>
    </div>
    <div id="whatToDo" class="easyui-menu" style="width: 120px;">
        <div data-options="iconCls:'icon-add'" onclick="insertSameNode()">
            新增同级词汇</div>
        <div data-options="iconCls:'icon-add'" onclick="insertSonNode()">
            新增子级词汇</div>
        <div data-options="iconCls:'icon-save'" onclick="editNode()">
            修改词汇</div>
        <div data-options="iconCls:'icon-save'" onclick="delNode()">
            删除词汇</div>
    </div>
</body>
</html>
