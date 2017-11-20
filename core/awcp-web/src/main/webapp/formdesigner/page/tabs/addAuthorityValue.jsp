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
 		<title>菜单编辑页面</title>
		<script type="text/javascript" src="<%=basePath%>formdesigner/scripts/dynamicpage.constant.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/scripts/map.js"></script>
 		<%-- <jsp:include page="" flush="true"/> --%>
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
		<link href="<%=basePath%>resources/plugins/treeTable/vsStyle/jquery.treeTable.css" rel="stylesheet" type="text/css" />
	</head>
	<body id="main">
		<div class="container-fluid">
			<div class="row" id="buttons">
				<button type="button" class="btn btn-success" id="close"><i class="icon-plus-sign"></i>关闭</button>
				<!-- <button type="button" class="btn btn-success" id="editBtn"><i class="icon-edit"></i>弹窗编辑</button> -->
			</div>
			<div class="row" id="dataform">
				<form class="form-horizontal" role="form" method="post" id="authority">
					<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
					<input type="hidden" name="componentId" id="componentId"/>
			
			<table id="treeTable1" class="table table-bordered" width="100%">
                <thead>
                <tr>
                    <th style="width:200px;">组件名称</th>
                    <th><input type="checkbox" id="t1" onClick="allReady()">所有</th>
			  		<th><input type="checkbox" id="t2" onClick="allModiy()">所有</th>
			  		<th><input type="checkbox" id="t3" onClick="allHidden()">所有</th>
                </tr>
                </thead>
                <tbody id="component">
                
                </tbody>
            </table>
			
				</form>
				
			</div>
		</div>
		
		<div class="row">
			
		
		</div>
		
		<%@ include file="/resources/include/common_form_js.jsp" %>
		<script src="<%=basePath%>resources/scripts/json2.js"></script>
		<script src="<%=basePath%>resources/scripts/jquery.serializejson.min.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/treeTable/jquery.treeTable.js"></script>
		<script type="text/javascript">
		
		$.formValidator.initConfig({formID:"authority",debug:false,onSuccess:function(){
			//	$("#authority").submit();
				  return false;
		    },onError:function(){alert("错误，请看提示")}});
		$("#name").formValidator({empty:true,onShow:"请输入名称",onCorrect:"符合格式要求"}).inputValidator({min:1,empty:{leftEmpty:false,rightEmpty:false,emptyError:"不能有空符号"},onError:"不能为空,请确认"});
		$("#order").formValidator({onShow:"请输入序号",onFocus:"请输入序号",onCorrect:"合法"}).regexValidator({regExp:"intege1",dataType:"enum",onError:"正整数格式不正确"});
		
		$('document').ready(function() {
			try {
				var dialog = top.dialog.get(window);
			} catch (e) {
				return;
			}
			
			
			//判断是否有数据，初始化 所属模型
			var data = null;//dialog.data;
			if(dialog != null){
				data = dialog.data;
				if(data.dynamicPageId){
					$("#dynamicPageId").val(data.dynamicPageId);
				}
				if(data.componentId){
					$("#componentId").val(data.componentId);
				}
				
			}	
			
			// if($.fn.chosen) $('.chosen-select').chosen();
			if (dialog != null) {
				dialog.title('权限配置');
				dialog.width(800);
				dialog.height(600);
				dialog.reset(); // 重置对话框位置
			}
			
			//查询组件数据
				$.ajax({
						   type: "POST",
						   async: false,
						   url: "<%=basePath%>authority/getComponentListByPageId.do",
						   data:"dynamicPageId=" + $("#dynamicPageId").val() + "&pageSize=9999"+"&authorityGroup="+$("#componentId").val(),
						   success: function(data){
								if(data != null){
								 $("#component").empty();
								 $.each(data,function(index,item){
								 var vo = eval("(" + item.content + ")");
								 //回显权限值
								   var s=item.scope;
								   var rd=new RegExp('10');
								   var up=new RegExp('11');
								   var he=new RegExp('12');
								   var tab="";
								   //过滤一些组件
								   if(vo.componentType!='1009'&& vo.componentType!='1010'){
								   if(rd.test(s)){
								   		tab="<td><input type='checkbox' name='ready' value='10' id='"+vo.name+"10' onclick='saveAuthority(\""+vo.name+"\",10)' checked='checked'>只读</td>";
								   }else{
								   		tab="<td><input type='checkbox' name='ready' value='10' id='"+vo.name+"10' onclick='saveAuthority(\""+vo.name+"\",10)'>只读</td>";
								   }
								   
								   if(up.test(s)){
								   		tab+="<td><input type='checkbox' name='modiy' value='11' id='"+vo.name+"11' onclick='saveAuthority(\""+vo.name+"\",11)' checked='checked'>修改</td>";
								   }else{
								   		tab+="<td><input type='checkbox' name='modiy' value='11' id='"+vo.name+"11' onclick='saveAuthority(\""+vo.name+"\",11)'>修改</td>";
								   }
								   
								   if(he.test(s)){
								   		tab+="<td><input type='checkbox' name='hidden' value='12' id='"+vo.name+"12' onclick='saveAuthority(\""+vo.name+"\",12)' checked='checked'>隐藏</td>";
								   }else{
								   		tab+="<td><input type='checkbox' name='hidden' value='12' id='"+vo.name+"12' onclick='saveAuthority(\""+vo.name+"\",12)'>隐藏</td>";
								   }
								   
								   if(!vo.configures){
								   
								    $("#component").append(""+
								   "<tr id='"+vo.name+"'>"+
								   "<td>"+pageConstant.getComponentTypes().get(vo.componentType)+"("+vo.name+")</td>"+
								   tab+
								   "</tr>"+
								   "");
								   }
								   
								   }
								   
								    //包含组件框
								    if(vo.configures){
								    var data=vo.configures;
								    $("#component").append(""+
								   "<tr id='"+vo.name+"'>"+
								   "<td><span controller='true'>"+pageConstant.getComponentTypes().get(vo.componentType)+"("+vo.name+")</span></td>"+
								   tab+
								   "</tr>"+
								   "");
								   
								   $.each(data,function(index,con){
								   		var componentId=vo.name+"_"+con.name;
								   		var authorityGroup=$("#componentId").val();
								   		var strVal=getValue(componentId,authorityGroup);
								   		var tab2="";
								   		//过滤一些组件
                      if(con.componentType!='1009'&& con.componentType!='1010'){
                   
								   		if(rd.test(strVal)){
								   			tab2="<td><input type='checkbox' name='ready' value='10' id='"+con.name+"10' onclick='saveAuthority_include(\""+con.name+"\",10,\""+vo.name+"_"+con.name+"\")' checked='checked'>只读</td>";
								   		}else{
								   			tab2="<td><input type='checkbox' name='ready' value='10' id='"+con.name+"10' onclick='saveAuthority_include(\""+con.name+"\",10,\""+vo.name+"_"+con.name+"\")'>只读</td>";
								   		}
								   		if(up.test(strVal)){
								   			tab2+="<td><input type='checkbox' name='modiy' value='11' id='"+con.name+"11' onclick='saveAuthority_include(\""+con.name+"\",11,\""+vo.name+"_"+con.name+"\")' checked='checked'>修改</td>";
								   		}else{
								   			tab2+="<td><input type='checkbox' name='modiy' value='11' id='"+con.name+"11' onclick='saveAuthority_include(\""+con.name+"\",11,\""+vo.name+"_"+con.name+"\")'>修改</td>";
								   		}
								   		if(he.test(strVal)){
								   		
								   			tab2+="<td><input type='checkbox' name='hidden' value='12' id='"+con.name+"12' onclick='saveAuthority_include(\""+con.name+"\",12,\""+vo.name+"_"+con.name+"\")' checked='checked'>隐藏</td>";
								   		}else{
								   			tab2+="<td><input type='checkbox' name='hidden' value='12' id='"+con.name+"12' onclick='saveAuthority_include(\""+con.name+"\",12,\""+vo.name+"_"+con.name+"\")'>隐藏</td>";
								   		}
								   		var str="<tr id='"+con.name+"' pId='"+vo.name+"'>";
								   		str+="<td><span>"+pageConstant.getComponentTypes().get(con.componentType)+"("+con.name+")</span></td>";
								   		str+=tab2;
								   		str+="</tr>";
								   		$("#component").append(str);
								   		
								   		}
								   });
								   
								   }
								 	
								 });
								 
								 refreshTree();	
							    
						    }
						  }
						});	

});
	
	function saveAuthority(componentId,value){
	
	  var status=1;
	  if($("#"+componentId+value).is(':checked')==false){
	  	status=0;
	  }
	 
	   $.ajax({
			    type: "POST",
			    async: false,
				url: "<%=basePath%>authority/addAuthorityValue.do",
				data: "componentId="+componentId+ 
				"&value=" + value+
				"&status=" + status+
				"&authorityGroupId="+$("#componentId").val(),
				success: function(data){
				if(data != null){
				if(dialog != null){
					
				}
				}else{
					alert("保存失敗");
				}
			}
	});	
	

	}
	
	//配置包含组件
	function saveAuthority_include(componentId,value,includeId){
	  
	  var status=1;
	  if($("#"+componentId+value).is(':checked')==false){
	  	status=0;
	  }
	   
	
	   $.ajax({
			    type: "POST",
			    async: false,
				url: "<%=basePath%>authority/addAuthorityValue.do",
				data: "componentId="+includeId + 
				"&value=" + value+
				"&status=" + status+
				"&includeComponent=" + includeId+
				"&authorityGroupId="+$("#componentId").val(),
				success: function(data){
				if(data != null){	
				if(dialog != null){

				}
				}else{
					alert("保存失敗");
				}
			}
	});	
	
	}
			
	
	function allReady(){
   
	 if($('#t1').is(':checked')==true) {

    	//$("input[name='ready']").prop("checked",true);
    	$("input[name='ready']").click();
    	
	 }
	 
	 if($('#t1').is(':checked')==false) {
	    
	 	//$("input[name='ready']").prop("checked",false);
	 	$("input[name='ready']").click();
	 }
	 
	}
	function allModiy(){
	 if($('#t2').is(':checked')==true) {
	 	//$("[name = modiy]:checkbox").prop("checked", true);
	 	$("[name = modiy]:checkbox").click();
	 }
	 if($('#t2').is(':checked')==false) {
	 	//$("[name = modiy]:checkbox").prop("checked", false);
	 	$("[name = modiy]:checkbox").click();
	 }
	 
	}
	function allHidden(){
	 if($('#t3').is(':checked')==true) {
	 	//$("[name = hidden]:checkbox").prop("checked", true);
	 	$("[name = hidden]:checkbox").click();
	 }
	 if($('#t3').is(':checked')==false) {
	 	//$("[name = hidden]:checkbox").prop("checked", false);
	 	$("[name = hidden]:checkbox").click();
	 }
	}
	
	//初始化tree
	function refreshTree(){
		var option = {
        theme:'vsStyle',
        expandLevel :1,
        beforeExpand : function($treeTable, id) {
            //判断id是否已经有了孩子节点，如果有了就不再加载，这样就可以起到缓存的作用
            if ($('.' + id, $treeTable).length) { return; }
            //这里的html可以是ajax请求
            var html = '<tr id="8" pId="6"><td>5.1</td><td>可以是ajax请求来的内容</td></tr>'
                     + '<tr id="9" pId="6"><td>5.2</td><td>动态的内容</td></tr>';

            $treeTable.addChilds(html);
        },
        onSelect : function($treeTable, id) {
            window.console && console.log('onSelect:' + id);
        }

    };
    $('#treeTable1').treeTable(option);
	
	}
	
	
	
	function getValue(componentId,authorityGroup){
		var strVal="";
		$.ajax({
			  type: "POST",
			  async: false,
		      url: "<%=basePath%>authority/getComponentValueByInclude.do",
			  data:"componentId="+componentId+"&authorityGroup="+authorityGroup,
			  success: function(data){
					strVal=data;
			  }	
			});
		return strVal;
	}
	
	$("#close").click(function(){
		top.dialog({id : window.name}).close();
	
	});
	
	</script>
	</body>
</html>
