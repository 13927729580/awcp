<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<meta charset="utf-8">
<title>系统业务/计划编辑</title>

<%@ include file="/resources/include/common_form_css.jsp"%><!-- 注意加载路径 -->
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2.css"/>
<link rel="stylesheet" href="<%=basePath%>resources/plugins/select2/select2-bootstrap.css"/>

</head>
<body id="main" class="inner-form">
		<div class="container-fluid">			
			<div class="row">
				<form class="form-horizontal" id="groupForm" method="post">
						<input type="hidden" name="itemId" value="${vo.itemId}" />
						<input type="hidden" name="itemHallCode" value="${vo.itemHallCode}" />  
						<input type="hidden" name="sysId" value="${vo.sysId}" /> 
						<input type="hidden" name="parentId" value="${vo.parentId}"/>
				<div class="form-group">
					<label class="col-md-2 control-label required">业务计划名称:</label>
					<div class="col-md-4">
						<input name="name" class="form-control" id="name"
							type="text" placeholder="" value="${vo.name}">
					</div>	
					<label class="col-md-2 control-label required">序列号码:</label>
					<div class="col-md-4">
						<input name="sequence" class="form-control" id="sequence"
							type="text" placeholder="" value="${vo.sequence}">
					</div>	
				</div>
				<div class="form-group">
					<label class="col-md-2 control-label required">项目类型名称:</label>
					<div class="col-md-4">
						<input name="projectTypeName" class="form-control" id="projectTypeName"
							type="text" placeholder="" value="${vo.projectTypeName}">
					</div>	
					<label class="col-md-2 control-label">大厅事项编码:</label>
					<div class="col-md-4">
						<input name="itemServiceCode" class="form-control" id="itemServiceCode"
							type="text" placeholder="" value="${vo.itemServiceCode}">
					</div>	
				</div>
				
				<div class="form-group">					
					<label class="col-md-2 control-label">动态表单:</label>
					<div class="col-md-4">
						<input  class="form-control" id="dynamicPageId" 
								type="text" placeholder="" name="dynamicPageId" value="${vo.dynamicPageId}"/>
					</div>
					<label class="col-md-2 control-label required">状态:</label>
					<div class="col-md-4">
						<select class="form-control" id="status" name="status">
								<option value="1" <c:if test="${vo.status == 1}"> selected</c:if> >开放</option>
								<option value="2" <c:if test="${vo.status == 2}"> selected</c:if>  >截止</option>
								<option value="0" <c:if test="${vo.status == 0}"> selected</c:if> >关闭</option>							
							</select>
					<%-- <label class="radio-inline">
							<input type="radio" name="status" <c:if test="${vo.status!='0'}"> checked </c:if> value="1"></input>开放
					</label>	 				
					<label class="radio-inline">
						<input type="radio" name="status" <c:if test="${vo.status=='0'}"> checked </c:if>  value="0">关闭
					</label>--%>
					</div>
				</div>	
				<c:if test="${vo.parentId == null || '' == vo.parentId}">			
					<div class="form-group">					
						<label class="col-md-2 control-label">业务计划年度:</label>
						<div class="col-md-4">
							<select class="form-control" id="year" name="year">
								<option value="2015" <c:if test="${vo.year == 2015}"> selected</c:if> >2015</option>
								<option value="2014" <c:if test="${vo.year == 2014}"> selected</c:if>  >2014</option>
								<option value="2013" <c:if test="${vo.year == 2013}"> selected</c:if> >2013</option>
								<option value="2012" <c:if test="${vo.year == 2012}"> selected</c:if> >2012</option>
							</select>
						</div>	
						<label class="col-md-2 control-label">业务计划大类:</label>
						<div class="col-md-4">
							<select class="form-control" id="businessType" name="businessType">
							</select>
						</div>							
					</div>	
				</c:if>				
				<div class="form-group">					
					<label class="col-md-2 control-label">说明:</label>
					<div class="col-md-10">
						<textarea class="form-control" id="comment" name="comment" rows="10">${vo.comment}</textarea>
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
        	 initializeSelect("businessType","${vo.businessType}", "kcwsbxtywlb");
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
           			        url: '<%=basePath%>dev/dynamicPageAjaxQuery.do?sysId=${vo.sysId}',
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
     		function initializeSelect(companyId, defData, parentDictCode){
     			$.ajax({
					type : "POST",
					url : "<%=basePath%>manage/queryChildItemListByCode.do?dictCode=" + parentDictCode,
					success : function(data) {
						if(data != null){
							var optionStr = "";
							for(var i = 0; i < data.length;i++){
								optionStr += "<option value='" + data[i].code + "'";
								if(defData == data[i].code){
									optionStr += "selected";
								}
								optionStr += ">" + data[i].dataValue + "</option>";
							}
							$("#" + companyId).append(optionStr);
						}
				}
			})			
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
            	 /*
            	   var menuAddress = $("#menuAddress").val();
            	   var dynamicpageId = $("#dynamicPageId").val();
            	   if(menuAddress!=""&&dynamicpageId!=""){
            		   alertMessage("地址和动态表只能选填一项");
            		   return false;
            	   } */
            		   $.ajax({
    						type : "POST",
    						url : "<%=basePath%>business/bizItemAJAXSave.do",
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
				$("#name").formValidator({
					onShow : "请输入业务/计划名称",
					onFocus : "至少1位",
					onCorrect : "合法"
				}).inputValidator({
					min : 1,
					max : 100,
					empty : {
						leftEmpty : false,
						rightEmpty : false,
						emptyError : "不能有空符号"
					},
					onError : "1~100位"
				});

				$("#projectTypeName").formValidator({
					onShow : "请输入项目类型名称",
					onFocus : "至少1位",
					onCorrect : "合法"
				}).inputValidator({
					min : 1,
					max : 100,
					empty : {
						leftEmpty : false,
						rightEmpty : false,
						emptyError : "不能有空符号"
					},
					onError : "1~100位"
				});
				$("#sequence").formValidator({
					onShow : "请输入业务/计划顺序",
					onFocus : "为1~2位的数字",
					onCorrect : "符合要求"
				}).regexValidator({
					regExp : "^\\d{1,2}$",
					onError : "请输入正确的业务/计划顺序"
				});

			})
		</script>


</body>
</html>
