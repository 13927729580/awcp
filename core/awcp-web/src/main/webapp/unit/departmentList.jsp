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
		<style type="text/css">
			.ui-layout-pane{padding:0;border-radius:5px;-webkit-border-radius:5px;-moz-border-radius:5px}
			.ui-layout-pane-north{border:none;}
			.ui-layout-resizer-open{background:#fff;}
			.ui-layout-resizer-hover{background:#F3FBFF;}
			.f_hover{
				display: block;
				float: left;
				width: 50%;
				height:40px;
				line-height:40px;
				border: 1px solid rgb(221, 221, 221);
				background: rgb(255, 255, 255) none repeat scroll 0% 0%;
				border-bottom: 0px;
				border-left: 0px;
				cursor: pointer;
			}
			.f_default{
				display: block;
				float: left;
				width: 50%;
				height:40px;
				line-height:40px;
				border: 1px solid rgb(221, 221, 221);
				border-left:0px;
				cursor: pointer;
			}
			.f{
				height: 40px;
				line-height: 40px;
				padding: 0px;
				text-align: center;
				width: 100%;
			}
			#p2{
				display: none;}
				
			.ztree li span.button.edit {background-image:url("<%=basePath%>/resources/images/zTreeEdit.png"); margin-right:2px; background-position:-110px -48px; vertical-align:top; *vertical-align:middle;width:16px;height:16px;}
			
		</style>
	</head>
	<body id="main">
	
						
			<div class="panel ui-layout-west">
				
				<div class="panel-body" id="p1" style="height:320px;">
					<ul id="tree1" class="ztree"></ul>
				</div>
			</div>
			<iframe class="ui-layout-center" src="<%=basePath%>punUserGroupController/getUsers.do" name="sysEditFrame" id="sysEditFrame" scrolling="auto" frameborder="0" width="100%"></iframe>
		
		<%@ include file="../../../resources/include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery-ui-latest.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/jquery.layout-latest.js"></script>

         <script type="text/javascript">//page_group
         var setting = {
	   			view: {
	   				showLine: false,
	   				showIcon: false,//showIconForTree,
	   				addHoverDom: addHoverDom
	   			},
	   			data: {
	   				simpleData: {
	   					enable: true
	   				}
	   			}
	   		};
        var tNodes1 = "";
        var tNodes2 = "";
        var myLayout;
        $(function(){
        	$.ajax({
                type:"post",
                url:"<%=basePath%>taskManagement/persons.do?type=manage",
                data:"",
                async: false,
                datatype:"json",
                success:function(json){
                        //先将date转化为json
                       tNodes1=json;
                      
                }
            });
        	
        	myLayout = $('body').layout({
    			west__size:					240
    		,	west__spacing_closed:		20
    		,	west__togglerLength_closed:	100
    		,	west__togglerAlign_closed:	"middle"
    		,	west__togglerContent_closed:"菜单树"
    		,	west__togglerTip_closed:	"打开并固定菜单"
    		,	west__sliderTip:			"Slide Open Menu"
    		,	west__slideTrigger_open:	"mouseover"
    		,	north__closeable: false
    		,	north__resizable:false
    		,	north__spacing_open:0
    		,	center__maskContents:true // IMPORTANT - enable iframe masking
    		
    		});

        	 /* var count=0;//默认选择行数为0
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
	   		  }); */
        	 
	   		$.fn.zTree.init($("#tree1"), setting, tNodes1);
	   		$.fn.zTree.init($("#tree2"), setting, tNodes2);
	   		//var treeObj = $.fn.zTree.getZTreeObj("tree1");
	   		//var node = treeObj.getNodeByParam("id", "529614", null);
	   		//treeObj.selectNode(node);
	   		//getIframeHeight();
	   		
        });
        
        
        function getIframeHeight(){//iframe's height
        	var wH = $(window).height();
        	var cH = $(".container-fluid").height()+$(".container-fluid").offset().top+25;
        	$("#sysEditFrame").height((wH-cH));
        }
        $(window).resize(function(){
        	//getIframeHeight();
        })
        $("#f1").click(function(){
				$("#f1").removeClass("f_default");
				$("#f1").addClass("f_hover");
				$("#f2").removeClass("f_hover");
				$("#f2").addClass("f_default");
				$("#p1").show();
				$("#p2").hide();
			});
			$("#f2").click(function(){
				$("#f2").removeClass("f_default");
				$("#f2").addClass("f_hover");
				$("#f1").removeClass("f_hover");
				$("#f1").addClass("f_default");
				$("#p2").show();
				$("#p1").hide();
			});
			
			
			
function addHoverDom(treeId, treeNode) {

	var sObj = $("#" + treeNode.tId + "_span");

	if ($("#"+treeNode.tId+"_edit").length>0){
		return;
	}
	var addStr = "<span class='button edit' id='"+ treeNode.tId+"_edit"
		+ "' title='修改组织菜单' onfocus='this.blur();'></span>";
	sObj.after(addStr);

	var editBtn = $("#"+treeNode.tId+"_edit");
	
	if (editBtn) editBtn.bind("click", function(){
        
        var groupId=treeNode.id;
        var number="";
        
        $.ajax({
                type:"post",
                url:"<%=basePath%>unit/getById.do?",
                data:"groupId="+groupId,
                async: false,
                datatype:"json",
                success:function(data){
                       
                      if(data!=null&&data!=""&&data.number!=""&&data.number!=null){
                      
                      		number=data.number;
                      		number=number.substr(number.lastIndexOf("-")+1,number.length);
                      }
                }
            });
        
        
        var content="<div class='input-group'>"+
	    "<span class='input-group-addon'><i class='icon-pencil'></i>&nbsp;&nbsp;编号</span>"+
        "<input name='userNumber' value='"+number+"' type='text' class='form-control'  placeholder='编号'></div>"
        
		var d = dialog({
		    title: '修改编号',
		    content:content,
		    okValue: '确定',
		    ok: function () {
		    	var $number = $("input[name='userNumber']");
		    	if($number.val()==""){
		    		$number.parent().addClass("has-error");
		    		return false;
		    	}else{
		    		location.href="<%=basePath%>unit/editGroup.do?groupId="+groupId+"&number="+$number.val();
					
		        }
		    },
		    cancelValue: '取消',
		    cancel: function () {}
		});
		d.width(320).show();
		return false;
	});
};
			
			
        </script>
         
	</body>
</html>
