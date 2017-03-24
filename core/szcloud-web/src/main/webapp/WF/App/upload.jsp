<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">




  

<

head

>  

    

<

title

>

文件批量上传

</

title

> 

 

<

meta

 

http-equiv

=

"pragma"

 

content

=

"no-cache"

> 

 

<

meta

 

http-equiv

=

"cache-control"

 

content

=

"no-cache"

>

 

 

<

meta

 

http-equiv

=

"expires"

 

content

=

"0"

>

  

 

  

 

<

link

 

href

="

<%=

request.getContextPath()

%>

/css/default.css

" 

rel

=

"stylesheet"

 

type

=

"text/css"

 

/>

   

  

<

link

 

href

="

<%=

request.getContextPath()

%>

/css/uploadify.css

"  

rel

=

"stylesheet"

 

type

=

"text/css"

 

/>

   

    

<

script

 

type

=

"text/javascript"

  

src

="

<%=

request.getContextPath()

%>

/js/jquery-1.3.2.min.js

"

></

script

>

  

    

<

script

 

type

=

"text/javascript"

  

src

="

<%=

request.getContextPath()

%>

/js/swfobject.js

"

></

script

>

   

    

<

script

 

type

=

"text/javascript"

  

src

="

<%=%>/js/jquery.uploadify.v2.1.0.min.js"></script>   

<scripttype="text/javascript">

   






        $(document).ready(

function() {   
            $("#uploadify").uploadify({ 'uploader': 'js/uploadify.swf', 'script': 'uploadDoc.jsp', 
'cancelImg': 'images/cancel.png',
'folder': 'uploads', 
'queueID': 'fileQueue', 
'fileDesc': '请选择doc,docx,xls,xlsx文件',  
'fileExt': '*.doc;*.docx;*.xls;*.xlsx',  
'sizeLimit': 1024*1024*10,  
'auto': false, 
'multi': true,  
'simUploadLimit': 2,
 'buttonText': 'BROWSE'
            });   

        });   

    

</script>
</head>
<body><div id="fileQueue"></div><input type="file" name="uploadify" id="uploadify"/><br><br>
<p><a href="javascript:jQuery('#uploadify').uploadifyUpload()">
开始上传</a><a href="javascript:jQuery('#uploadify').uploadifyClearQueue()">取消所有上传
</a>
</p>
</body>
</html> 
