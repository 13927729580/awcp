<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/head/head1.jsp"%>
<%
	RefLeftModel rlm=new RefLeftModel(request,basePath);
	rlm.init();
	
	UIEnModel uem=new UIEnModel(request,response);
	uem.init();
	
	UIEnsModel uiem=new UIEnsModel(request,response);
	uiem.init();
	
	String ensName = request.getParameter("EnsName");

%>
<base target=_self  />
<script language="javascript" type="text/javascript">
	$(window).resize(function() {

	});

	function SelectAll(cb_selectAll) {
		var arrObj = document.all;
		if (cb_selectAll.checked) {
			for (var i = 0; i < arrObj.length; i++) {
				if (typeof arrObj[i].type != "undefined"
						&& arrObj[i].type == 'checkbox') {
					arrObj[i].checked = true;
				}
			}
		} else {
			for (var i = 0; i < arrObj.length; i++) {
				if (typeof arrObj[i].type != "undefined"
						&& arrObj[i].type == 'checkbox')
					arrObj[i].checked = false;
			}
		}
	}
	
	  function selectTab(tabTitle) {
          $('#nav-tab').tabs('select', tabTitle);
      }

      $(document).ready(function () {
          
          var tabts = $("#rightFrame a.tabs-inner");

          $.each(tabts, function (i) {
              $(this).attr('title', $("#rightFrame div[data-g='" + $(this).text() + "']").attr('data-gd'));
          });

          //选中上次保存之前当前打开的标签
          var urlParams = location.search.substr(1).split('&');
          $.each(urlParams, function () {
              var a = this.split('=');
              if (a[0] == 'tab') {
                  $('#nav-tab').tabs('select', decodeURIComponent(a[1]));
                  return;
              }
          });
      });
</script>

<script type="text/javascript">
	function selectTab(tabTitle) {
		$('#nav-tab').tabs('select', tabTitle);
	}

	$(document).ready(
			function() {
				if (false) {
					$('body').layout('collapse', 'west');
				}

				var tabts = $("#rightFrame a.tabs-inner");

				$.each(tabts, function(i) {
					$(this).attr(
							'title',
							$(
									"#rightFrame div[data-g='" + $(this).text()
											+ "']").attr('data-gd'));
				});

				//选中上次保存之前当前打开的标签
				var urlParams = location.search.substr(1).split('&');
				$.each(urlParams, function() {
					var a = this.split('=');
					if (a[0] == 'tab') {
						$('#nav-tab').tabs('select', decodeURIComponent(a[1]));
						return;
					}
				});
			});
</script>
<script language="javascript">
	var currShow;

	function ShowEn(url, wName, h, w) {
		var s = "dialogWidth=" + parseInt(w) + "px;dialogHeight=" + parseInt(h)
				+ "px;resizable:yes";
		var val = window.showModalDialog(url, null, s);
		window.location.href = window.location.href;
	}

	function ImgClick() {
	}

	function OpenAttrs(ensName) {
		var url = '../Sys/EnsAppCfg.jsp?EnsName=' + ensName;
		var s = 'dialogWidth=680px;dialogHeight=480px;status:no;center:1;resizable:yes'
				.toString();
		val = window.showModalDialog(url, null, s);
		window.location.href = window.location.href;
	}

	//在右侧框架中显示指定url的页面
	function OpenUrlInRightFrame(ele, url) {
		if (ele != null && ele != undefined) {
			//if (currShow == $(ele).text()) return;

			currShow = $(ele).parents('li').text();//有回车符

			$.each($(ele).parents('ul').children('li'), function(i, e) {
				$(e).children('div').css('font-weight',
						$(e).text() == currShow ? 'bold' : 'normal');
			});

			$('#rightFrame').empty();
			$('#rightFrame').append(
					'<iframe scrolling="no" frameborder="0"  src="' + url
							+ '" style="width:100%;height:100%;"></iframe>');
		}
	}
	
	//点击保存
	function onSave(){
		var param = window.location.search;

		var url = "<%=basePath%>DES/save.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	
	//点击保存并关闭
	function onSaveOrClose(){
		var param = window.location.search;
		var url = "<%=basePath%>DES/saveorclose.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	
	//点击删除
	function onDelete(){
		if(!confirm("确认删除吗？")){
			return;
		}
		var param = window.location.search;
		$("#FormHtml").val($("#form1").html());
		var url = "<%=basePath%>DES/Delete.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	
	//点击新建
	function onNew(){
		var  str = "<%=ensName%>";
		if(str==null || str==""){
			var param = window.location.search;
			window.location = "UIEn.jsp"+param.substring(0, param.indexOf('&'));
		} else{
			window.location = "UIEn.jsp?EnsName="+"<%=ensName%>";
		}
	}
	
	//点击保存新建
	function onSaveAndNew(){
		var param = window.location.search;
		var url = "<%=basePath%>DES/SaveAndNew.do"+param;
		$("#form1").attr("action", url);
		$("#form1").submit();
	}
	function WinOpen(url) {
        var newWindow = window.open(url, 'z', 'height=500,width=600,top=100,left=200,toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no');
        newWindow.focus();
        return;
    }
	
</script>
<body class="easyui-layout" leftmargin="0" topmargin="0" onkeypress="javascript:Esc();">
<form method="post" action="" class="am-form" id="form1">
		<div id="leftFrame" data-options="region:'west',title:'功能列表',split:true" style="width: 200px; padding: 5px">
			<%=rlm.Pub1.toString() %>
		</div>
		<div id="rightFrame" data-options="region:'center',noheader:true">
			<div class="easyui-layout" data-options="fit:true">
			    <div data-options="region:'north',noheader:true,split:false,border:false" style="height: 30px;
			        padding: 2px; background-color: #E0ECFF">
			        <%=uiem.ToolBar1.toString() %>
			    </div>
				<div data-options="region:'center',noheader:true,border:false">
					<%=uiem.UCEn1.pub.toString() %>
				</div>
			</div>
		</div>
	</form>
</body>
</html>
