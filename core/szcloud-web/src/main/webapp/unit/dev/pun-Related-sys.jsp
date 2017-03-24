<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sc" uri="szcloud"%>
<%@page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>系统管理</title>
<%-- <jsp:include page="" flush=”true”/> --%>
<%@ include file="/resources/include/common_css.jsp"%><!-- 注意加载路径 -->
</head>
<body id="main">
	<div class="container-fluid">
		<!--list页面整体布局结构 
			<div class="row" id="breadcrumb">面包屑导航</div>
			<div class="row" id="buttons">按钮区</div>
			<div class="row" id="searchform">搜索区</div>
			<div class="row" id="datatable">数据展示区</div>
			<div class="row" id="pagers">分页</div>
			-->
		<div class="row" id="breadcrumb">
			<ul class="breadcrumb">
				<li><i class="icon-location-arrow icon-muted"></i></li>
				<c:if test="${relateOrNot==0}">
					<li class="active">待关联应用系统管理</li>
				</c:if>
				<c:if test="${relateOrNot==1}">
					<li class="active">已关联应用系统管理</li>
				</c:if>
			</ul>
		</div>

		<div class="row" id="buttons">
		<c:if test="${relateOrNot==0}">
			<button type="button" class="btn btn-success" id="relatesysBtn">
				<i class="icon-plus-sign"></i>关联
			</button>
			</c:if>
			<c:if test="${relateOrNot==1}">
			<button type="button" class="btn btn-info" id="notRelatesysBtn">
				<i class="icon-trash"></i>取消关联
			</button>
			<button type="button" class="btn btn-warning" id="roleManageBtn">
				<i class="icon-edit"></i>管理角色
			</button>
			</c:if>
			<button type="button" class="btn btn-info" id="searchBtn"
				data-toggle="collapse" data-target="#collapseButton">
				<i class="icon-search"></i>
			</button>
			
		</div>

		<div class="row" id="searchform">
			<div id="collapseButton" class="collapse">
				<form method="post" action="<%=basePath%>unit/punSysQuery.do" id="createForm">
					<input type="hidden" name="currentPage" value="0" />
					<div class="col-md-3">
						<div class="input-group">
							<span class="input-group-addon">系统名</span> <input name="sysName" class="form-control" id="sysName" type="text" />
						</div>
					</div>
					<div class="col-md-3">
						<div class="input-group">
							<span class="input-group-addon">状态</span> 
							<select name="relateOrNot" class="form-control">
                             	<option value="0">未关联</option>
                             	<option value="1">已关联</option>
                            </select>
						</div>
					</div>
					<div class="col-md-3 btn-group">
						<button class="btn btn-primary" type="submit">确定</button>
						<a class="btn" data-toggle="collapse"
							data-target="#collapseButton">取消</a>
					</div>
				</form>
			</div>
		</div>

		<div class="row" id="datatable">
			<table class="table datatable table-bordered">
				<thead>
					<tr>
						<th class="hidden"></th>
						<th>系统名</th>
					</tr>
				</thead>
				<tbody>
					<form method="post" id="showList">
						<input type="hidden" name="currentPage" value="${currentPage}">
						<c:forEach items="${vos}" var="vo">
							<tr>
								<td class="hidden formData"><input id="boxs" type="name" value="${vo.sysId}"></td>
								<td>${vo.sysName}</td>
							</tr>
						</c:forEach>
					</form>
				</tbody>
			</table>
		</div>
		<div class="row navbar-fixed-bottom text-center" id="pagers">
			<sc:PageNavigation dpName="vos"></sc:PageNavigation>
		</div> 
	</div>

	<%@ include file="/resources/include/common_js.jsp"%>
	<script type="text/javascript" src="<%=basePath%>resources/scripts/pageTurn.js"></script>
	<script type="text/javascript">
		  $(function(){
			  var count=0;//默认选择行数为0
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
					
			  });
          	//新增
      		$("#relatesysBtn").click(function(){
      			if(count>0){
      				if(!window.confirm("确定为组织添加该系统？")){
      					return false;
      				}
      				$("#showList").attr("action","<%=basePath%>unit/relateSys.do").submit();
      			}else{
      				alert("请至少选择一项进行操作");
      			}
      			return false;
      		});
      		
      		//update
      		$("#roleManageBtn").click(function(){
      			if(count == 1){
      				$("#showList").attr("action","<%=basePath%>unit/manageRole.do").submit();
      			}else{
      				alert("请选择一项进行操作");
      			}
      			return false;
      		});
      		
          	//delete
          	$("#notRelatesysBtn").click(function(){
      			if(count>0){
      				if(!window.confirm("确定组织不再需要该系统？")){
      					return false;
      				}
      				$("#showList").attr("action","<%=basePath%>unit/cancelRelate.do").submit();
      			}else{
      				alert("请至少选择一项进行操作");
      			}
      			return false;
							})
		});
		  function test(){
				 
		  }
		  $(document).ready(function(){
				LoadMenuBtn();
				$(window).resize(function(){
					LoadMenuBtn();
				});	  
				

				function LoadMenuBtn(){
					if ($(window).width() < 340){
					 	LoadControl(0);
					}else if(($(window).width() >= 340)&&($(window).width() < 360)){
					 	LoadControl(1);
					}else if(($(window).width() >= 360)&&($(window).width() < 440)){
					 	LoadControl(2);
					}else if(($(window).width() >= 440)&&($(window).width() < 500)){
					 	LoadControl(3);
					}else if(($(window).width() >= 500)&&($(window).width() < 620)){
					 	LoadControl(4);
					}else if(($(window).width() >= 620)&&($(window).width() < 767)){
					 	LoadControl(5);
					}else if(($(window).width() >= 767)&&($(window).width() < 992)) {
					 	LoadControl(6);
					}else{
					 	LoadControl(8);
					}
				}
				function LoadControl(flag){
					var MenuDiv = jQuery("<div>",{ "class": "btn-group dropdown" });
					var MenuListDiv = '<button class="btn" type="button">更多</button><button class="btn dropdown-toggle" type="button" data-toggle="dropdown"><span class="caret"></span></button>';
					var DropDownMenuDiv = jQuery("<ul>", { "class": "dropdown-menu","role":"menu","aria-labelledby":"dropdownMenu"});
					var menuGroup = jQuery("<span>");
					var Links = '';
					var Lis = '';
					$.each($('#btns a'),function(key, element) {
						if(key < flag){
							if($(this).attr("class") == "undefined"){
				 				Links = '<a href="'+$(this).attr("href")+'" class="btn btn-info">'+$(this).html()+'</a>';
				 			} else {
								Links = '<a href="'+$(this).attr("href")+'" class="'+$(this).attr("class")+'">'+$(this).html()+'</a>';
				 			}
							menuGroup.append(Links);
						}else{
							var Lis = '<li><a  tabindex="-1" href="'+$(this).attr("href")+'">'+$(this).html()+'</a></li>';
							DropDownMenuDiv.append(Lis);
						}				
					});	
					MenuDiv.append(DropDownMenuDiv).append(MenuListDiv);
					menuGroup.append(MenuDiv);
					$('#btns').html(menuGroup.html());
					$('.dropdown-menu').css("padding","5px");
					$('.dropdown-menu').css("padding-left","8px");
					$('.dropdown-menu li').css("margin-bottom","2px");
					$('#btns a').css("margin-right","4px");
				}
				$('.checkboxCtrl').each(function() {
					var $trigger = $(this);
							$trigger.click(function(){
								var group = $trigger.attr("group");
								if ($trigger.is(":checkbox")) {
									var type = $trigger.is(":checked") ? "all" : "none";
									if (group) $.checkbox.select(group, type, parent);
								} else {
									if (group) $.checkbox.select(group, $trigger.attr("selectType") || "all", parent);
								}
					});
		        });
				$.checkbox = {
					selectAll: function(_name, _parent){
						this.select(_name, "all", _parent);
					},
					unSelectAll: function(_name, _parent){
						this.select(_name, "none", _parent);
					},
					selectInvert: function(_name, _parent){
						this.select(_name, "invert", _parent);
					},
					select: function(_name, _type, _parent){

						$checkboxLi = $('.C-tabList').find(":checkbox[name='"+_name+"']");

						switch(_type){
							case "invert":
							
								$checkboxLi.each(function(){
									$checkbox = $(this);
									$checkbox.attr('checked', !$checkbox.is(":checked"));
								});
								break;
							case "none":

							$checkboxLi.each(function(){
								$checkboxLi.attr('checked', false);	
							});
							break;
							default:

							$checkboxLi.each(function(){
								$checkboxLi.attr('checked', true);	
							});
							break;
						}
					}
				};
				$('.searchLayer').click(function (event) {   
		            event.stopPropagation();  
		            //set the position 
		            var offset = this.$table.offset();  
					 if(($(window).width()-offset.left)<300){
						 $('#searchTopDiv').css({ top: offset.top + this.$table.height() + "px", left: $(window).width()-300 });  
					 } else {
						 $('#searchTopDiv').css({ top: offset.top + this.$table.height() + "px", left: offset.left });  
					 }
		            //toggle hide or show the tipdiv  
		             $('#searchTopDiv').toggle('fast');  
		        }); 
				$('#btnSearch').click(function (event) { $('#searchTopDiv').fadeOut(400) });
			});
	</script>
</body>
</html>
