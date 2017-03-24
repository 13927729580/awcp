<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="renderer" content="webkit">
	<title>用户详细信息编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>

</head>
<body id="main" class="inner-form">
		<div class="container-fluid">			
			<div class="row">
				<form class="form-horizontal" id="groupForm" method="post">
						<input type="hidden" name="menuId" value="${vo.menuId}" /> 
						<input type="hidden" name="sysId" value="${vo.sysId}" /> 
						<input type="hidden" name="parentMenuId" value="${vo.parentMenuId}"/>
				<div class="form-group">
					<label class="col-md-2 control-label required">菜单名称:</label>
					<div class="col-md-4">
						<input name="menuName" class="form-control" id="menuName"
							type="text" placeholder="" value="${vo.menuName}">
					</div>	
					<label class="col-md-2 control-label required">序列号码:</label>
					<div class="col-md-4">
						<input name="menuSeq" class="form-control" id="menuSeq"
							type="text" placeholder="" value="${vo.menuSeq}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">动态表单:</label>
					<div class="col-md-4">
						<input  class="form-control" id="dynamicPageId" 
								type="text" placeholder="" name="dynamicPageId" value="${vo.dynamicPageId}"/>
					</div>
					<label class="col-md-2 control-label">菜单地址:</label>
					<div class="col-md-4">
						<input name="menuAddress" class="form-control" id="menuAddress"
							type="text" placeholder="" value="${vo.menuAddress}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label">图标:</label>
					<div class="col-md-4">
						<input  class="form-control" id="dynamicPageId" 
								type="text" placeholder="" name="menuIcon" value="${vo.menuIcon}"/>
					</div>
					<label class="col-md-2 control-label">是否为常用:</label>
					<div class="col-md-4">
						<label class="radio-inline">
							<input name="menuFlag"  type="radio" ${vo.menuFlag==1?"checked":""} value="1">
							是
						</label>
						<label class="radio-inline">
							<input name="menuFlag"  type="radio" ${vo.menuFlag==0?"checked":""} value="0">
							否
						</label>
					</div>
				</div>
				<div class="form-group">
						<label class="col-md-7 control-label">(0是web菜单，1是app头部菜单，2是app中间菜单，3是app底部菜单)类型:</label>
					<div class="col-md-5">
						<input  class="form-control" type="text" name="type" value="${vo.type}"/>
					</div>
				</div>
				<div class="form-group"><!-- 表单提交按钮区域 -->
		            <div class="col-md-offset-2 col-md-10">
		              	<button  class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
		              	<c:if test="${result!=null&&''!=result}">
							<span style="color: red">(${result})</span>
						</c:if>
		            </div>
		        </div>
			</form>
			</div>
					
		</div>

		<%@ include file="/resources//include/common_form_js.jsp" %>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2.js"></script>
		<script type="text/javascript" src="<%=basePath%>resources/plugins/select2/select2_locale_zh-CN.js"></script>
		<script type="text/javascript">//自己按需求可以提取单独的js文件 存放与resources/scripts文件夹下 命名规则：form.文件名.js
         $(function(){//page_condensed紧凑表单页面
    	
            	 var dialog;
            	 try {
     				dialog = top.dialog.get(window);
     			 } catch (e) {
     				return;
     			 }
     			 
     		if($.fn.select2){//使用select 2获取动态页面	
           		 $("#dynamicPageId").select2({
           			    placeholder: "查找动态页面",
           			    minimumInputLength: 1,
           			 	allowClear :true,
           			    ajax: {
           			    	type:"POST",
           			        url: "<%=basePath%>dev/dynamicPageAjaxQuery.do",
           			        dataType: 'json',
           			        quietMillis: 1000,
           			        data: function (term, page) {
           			            return {
           			            	pageName: term, 
           			            };
           			        },
           			        results: function (data, page) {
    
           			            return { results: data};
           			        },
           			        cache: true
           			    },
           			 
           			    initSelection: function(element, callback) {
           			    	var id = $(element).val();
           			    	if(id != ""){
           			    		$.ajax("<%=basePath%>dev/dynamicPageAjaxQuery.do", {
                                	data: {dynamicPageId: id},
                               	 	dataType: "json"
                            	}).done(function(data) {
                                	callback(data[0]);
                           		});
           			    	}
           			    },
           			    formatResult: repoFormatResult, 
           			    formatSelection: repoFormatSelection,  
           			    dropdownCssClass: "bigdrop",
           			    escapeMarkup: function (m) { return m; }
           			});	 
           	}            
           	 function repoFormatResult(repo) {
           		 	var markup = '<div>' + repo.name + '</div>';
            	     return markup;
   			
           	   }
           	   function repoFormatSelection(repo) {
            	      return repo.name;
           	   }
 			 
               $("#saveBtn").click(function(){
            	   if(!$.formValidator.pageIsValid('1')){
           				return false;
           			}
            	   var menuAddress = $("#menuAddress").val();
            	   var dynamicpageId = $("#dynamicPageId").val();
            	   if(menuAddress!=""&&dynamicpageId!=""){
            		   alertMessage("地址和动态表只能选填一项");
            		   return false;
            	   }
            		   $.ajax({
    						type : "POST",
    						url : "<%=basePath%>dev/punMenuAJAXSave.do",
							data : $("#groupForm").serialize(),
							success : function(data) {
								if (data.status == 1) {//failed
									alertMessage(data.message);
								} else {
									dialog.close(data.data);
									dialog.remove();
								}
						}
					})
					return false;
				});
   
				$.formValidator.initConfig({
					formID : "groupForm",
					debug : false,
					onSuccess : function() {
					},
					onError : function() {
						alert("请按提示正确填写内容");
					}
				});
				$("#menuName").formValidator({
					onShow : "请输入资源名称",
					onFocus : "至少1位",
					onCorrect : "合法"
				}).inputValidator({
					min : 1,
					max : 20,
					empty : {
						leftEmpty : false,
						rightEmpty : false,
						emptyError : "不能有空符号"
					},
					onError : "1~20位"
				});
				
			$("#menuAddress").formValidator({
					empty : true,
					onShow : "菜单地址"
				}).inputValidator({
					max : 128,
					onError : "最长128位"
				});
				$("#menuSeq").formValidator({
					onShow : "请输入资源顺序",
					onFocus : "为1~2位的数字",
					onCorrect : "符合要求"
				}).regexValidator({
					regExp : "^\\d{1,2}$",
					onError : "请输入正确的资源顺序"
				});

			})
		</script>


</body>
</html>
