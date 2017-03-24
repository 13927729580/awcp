<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<title></title>
	<link rel="StyleSheet" href="<%=basePath %>WF/Style/dtree.css" type="text/css" />
 	<script type="text/javascript" src="<%=basePath %>resources/JqEdition/jquery-1.9.1.min.js"></script>
 	<script type="text/javascript" src="<%=basePath%>WF/Scripts/dtree.js"></script>
  	<script type="text/javascript" src="<%=basePath%>im/js/3rd/contextMenu/jquery.contextMenu.js"></script>

  <script type="text/javascript">
  
    dTree.prototype.isParentNode = function(id) {
	    /* var isParentNode = false;
		var n=0;
		for (n; n<this.aNodes.length; n++) 
		{	
		    
		    if(this.aNodes[n].pid == -1)
		    {
		      continue;
		    }
		   // alert(this.aNodes[n].pid  + "----" +id);
			if (this.aNodes[n].pid == id) 
			{
			   isParentNode = true;
			   break;
			}
		}
		return isParentNode; */
		return true;
	};
	 var jqueryDtree = new dTree('jqueryDtree');
	   $.ajax({ 
                	   contentType: "application/json", 
                       url:'<%=basePath%>menu/index.do', 
                       type:'post', //数据发送方式 
                       dataType:'json', //接受数据格式 
                       data:{},
                       error:function(json){
                               alert("error");
                             },
                       async: false ,
                       success: function(json){
							    for(var i=0; i<json.length; i++)  
							    {  
							   		 var id = json[i].id;
							         var nodeId = json[i].nodeId;
							         var parentId = json[i].parentId;
							         var hrefAddress =  json[i].hrefAddress; 
							         var nodeName = json[i].nodeName;
							         jqueryDtree.add(id,parentId,nodeName,"<%=basePath%>base/center.jsp","","right","","",false);
							    } 
                          }
                 });
                 
    $(document).ready(function() {
      $('a').contextMenu('jqueryDtreeMenu', {
		onContextMenu: function(e) {
		  var nodeId = $(e.target).attr('id').substr(jqueryDtree.obj.length+1);
          if(jqueryDtree.isParentNode(nodeId))
		  {
		  	return true;
		  }else
		   return false;
        },
        bindings: {

          'add': function(t) {
			parent.document.getElementById("right").src = "<%=basePath%>base/menu/addmenu.jsp";
          },

          'edit': function(t) {
			parent.document.getElementById("right").src = "<%=basePath%>base/menu/editmenu.jsp";

          },

          'delete': function(t) {
			parent.document.getElementById("right").src = "<%=basePath%>base/menu/deletemenu.jsp";
          }

        }

      });
	  
    });

  </script>


</head>

<body>
<div class="dtree">

	<!-- <p><a href="javascript: jqueryDtree.openAll();">展开</a> | <a href="javascript: jqueryDtree.closeAll();">折叠</a></p>
 -->
	<script type="text/javascript">

		document.write(jqueryDtree);
	</script>

</div>

  <div class="contextMenu" id="jqueryDtreeMenu">

    <ul>

      <li id="add"><img src="../img/folder.png" />新增</li>

      <li id="edit"><img src="../img/email.png" />修改</li>

      <li id="delete"><img src="../img/cross.png" />删除</li>

    </ul>

  </div>


</body>

</html>
