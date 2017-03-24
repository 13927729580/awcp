<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/head/head1.jsp"%>
    <%
    FrmsModel frm=new FrmsModel();
    frm.init();
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <title>ccForm 自由表单设计器</title>
    <style type="text/css">
    html, body {
	    height: 100%;
	    overflow: auto;
	    text-align:center;
    }
    body {
	    padding: 0;
	    margin: 0;
	    text-align:center;
            font-weight: 700;
        }
    #silverlightControlHost {
	    height: 100%;
	    text-align:center;
	    
    }
    </style>
    <script src="../../../Silverlight.js" type="text/javascript"></script>
    <script type="text/javascript">
        function onSilverlightError(sender, args) {
            var appSource = "";
            if (sender != null && sender != 0) {
              appSource = sender.getHost().Source;
            }
            
            var errorType = args.ErrorType;
            var iErrorCode = args.ErrorCode;

            if (errorType == "ImageError" || errorType == "MediaError") {
              return;
            }

            var errMsg = "Silverlight 应用程序中未处理的错误 " +  appSource + "\n" ;

            errMsg += "代码: "+ iErrorCode + "    \n";
            errMsg += "类别: " + errorType + "       \n";
            errMsg += "消息: " + args.ErrorMessage + "     \n";

            if (errorType == "ParserError") {
                errMsg += "文件: " + args.xamlFile + "     \n";
                errMsg += "行: " + args.lineNumber + "     \n";
                errMsg += "位置: " + args.charPosition + "     \n";
            }
            else if (errorType == "RuntimeError") {           
                if (args.lineNumber != 0) {
                    errMsg += "行: " + args.lineNumber + "     \n";
                    errMsg += "位置: " +  args.charPosition + "     \n";
                }
                errMsg += "方法名称: " + args.methodName + "     \n";
            }

            alert(errMsg);
        }
    </script>
</head>
<body>
 <form id="form1" runat="server" style="height:100%">
    <div id="silverlightControlHost" >
        <object data="data:application/x-silverlight-2," type="application/x-silverlight-2" width="100%" height="100%">
		  <param name="source" value="../../../ClientBin/CCFormDesigner.xap"/>
		  <param name="onError" value="onSilverlightError" />
		  <param name="background" value="white" />
		  <param name="minRuntimeVersion" value="4.0.50826.0" />
		  <param name="autoUpgrade" value="true" />
		  <a href="http://go.microsoft.com/fwlink/?LinkID=149156&v=4.0.50826.0" style="text-decoration:none">
 			  <img src="http://go.microsoft.com/fwlink/?LinkId=161376" alt="获取 Microsoft Silverlight" style="border-style:none"/>
		  </a>
	    </object><iframe id="_sl_historyFrame" style="visibility:hidden;height:0px;width:0px;border:0px"></iframe></div>
    </form>
</body>
</html>