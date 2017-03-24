<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
    <HEAD>
        <TITLE>菜单管理</TITLE>
        <!-- <META content="text/html; charset=g" http-equiv=Content-Type> -->
        <link rel="StyleSheet" href="<%=basePath%>WF/Style/dtree.css" type="text/css" />
		<script type="text/javascript" src="<%=basePath %>resources/JqEdition/jquery-1.9.1.min.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=basePath%>WF/Scripts/dtree.js" charset="utf-8"></script>
 
<style type="text/css">
        body,td,th {
            font-size: 12px;
        }
</style>
<style type="text/css">
 /* 普通超连接样式 */ 
 /* 未访问过 #113333 */
a:link {
    font-family: "宋体";
    font-size: 12px;
    color: #0000FF;
    text-decoration: none;
}
 
/* 访问过 #113333 */
a:visited {
    font-family: "宋体";
    font-size: 12px;
    color: #0000FF;
    text-decoration: none;
}
 

/* 鼠标在上边 */
a:hover {
    font-family: "宋体";
    font-size: 12px;
    color: #CC6600;
    text-decoration: none;
}
 
/* 鼠标点击时 */
a:active {
    font-family: "宋体";
    font-size: 12px;
    color: #006600;
    text-decoration: none;
}
 
</style>
 
    </HEAD>
    <BODY  topmargin="0" leftmargin="0" >
     
        <table width="100%" align="center" cellpadding="0" cellspacing="0">
             
            <tr>
                <td colspan="1" bgcolor="#FFFFFF" valign="top" width="35%" style="padding-left: 30px;">
                     
                </td>
                <td bgcolor="#FFFFFF" valign="top" height="65%" align="left">
                 <form  method="post" id="regform">
                    <table width="70%"   cellpadding="1" cellspacing="0" border="0" style="margin-top: 10px;">
                         
                        <tr>
                            <td width="20%" align="left">父 菜 单： </td>
                            <td width="30%" align="left">
                            <input type="text" id="menu_parent_name"   style="width: 150px;"> 
                            <input type="hidden" id="menu_parent" name="menu_parent"><!-- 父菜单id -->
                            <input type="hidden" id="oprate" name="oprate"><!-- radio -->
                            <input type="hidden" id="menu_id" name="menu_id"><!-- 父菜单id -->
                            </td>
                            <td width="20%" align="right"></td>
                            <td width="30%" align="left">
                             
                            </td>
                        </tr>
                         
                    </table>
                    </form>
                </td>
            </tr>
        </table>
        <div id="treediv" style="display: none;position:absolute;overflow:scroll;  width: 150px;height:200px;  padding: 5px;background: #fff;color: #fff;border: 1px solid #cccccc"  >
        <div align="right"><a href="##" id="closed"><font color="#000">关闭&nbsp;</font></a></div>
            <script language="JavaScript" type="text/JavaScript">
                        //树代码
                        //mydtree = new dTree('mydtree','imgmenu/','no','no');
                        mydtree = new dTree('mydtree');
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
												    // mydtree.add(id,parentId,nodeName,"","","right","","_self",false);
												     mydtree.add(id, parentId,nodeName,"javascript:setvalue('"+id+"','"+nodeName+"')",nodeName, "_self",false)
											   }  
                                          }
                            });
                       document.write(mydtree);
                    </script>
        </div>
    </BODY>
 
 
 
 
 
    <script type="text/javascript" charset="utf-8">
 
 
 
        //生成弹出层的代码
     	//弹出层
        xOffset = 0;//向右偏移量
        yOffset = 25;//向下偏移量
 
        var toshow = "treediv";//要显示的层的id
        var target = "menu_parent_name";//目标控件----也就是想要点击后弹出树形菜单的那个控件id
 
 
        $("#"+target).click(function (){
            $("#"+toshow)
            .css("position", "absolute")
            .css("left", $("#"+target).position().left+xOffset + "px")
            .css("top", $("#"+target).position().top+yOffset +"px").show();
        });
        //关闭层
        $("#closed").click(function(){
            $("#"+toshow).hide();
        });
         
        //判断鼠标在不在弹出层范围内
         function   checkIn(id){
            var yy = 20;   //偏移量
            var str = "";
            var eventV = arguments.callee.caller.arguments[0] || window.event;
            //var   x=window.event.clientX;   
            //var   y=window.event.clientY;   
            var   x=eventV.clientX;   
            var   y=eventV.clientY; 
            var   obj=$("#"+id)[0];
            if(x>obj.offsetLeft&&x<(obj.offsetLeft+obj.clientWidth)&&y>(obj.offsetTop-yy)&&y<(obj.offsetTop+obj.clientHeight)){   
                return true;
            }else{   
                return false;
            }   
          }   
        //点击body关闭弹出层
            $(document).click(function(){
                var is = checkIn("treediv");
                if(!is){
                    $("#"+toshow).hide();
                }
            });
    //弹出层
        //生成弹出层的代码
        //点击菜单树给文本框赋值------------------菜单树里加此方法
        function setvalue(id,name){
            $("#menu_parent_name").val(name);
            $("#menu_parent").val(id);
            $("#treediv").hide();
        }
     
    </script>
 
</HTML>