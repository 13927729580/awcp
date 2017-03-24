<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sc" uri="szcloud"%>
<c:set var="ctxRes" value="${pageContext.request.contextPath}/resources"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="renderer" content="webkit">
<title>政务云应用管理平台</title>
<link rel="stylesheet" href="<%=basePath %>base/resources/zui/dist/css/zui.css" >
	<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
<style>
	body{position:relative;overflow:hidden;}
	body>header{position:fixed;top:0;width:100%;}
	
	.row-main{padding:60px 10px 0;}
	.nav{background:#}
	i.icon{margin:0 2px;}
</style>
</head>
<body>
	<header>
		<nav class="navbar navbar-inverse">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
              <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
              </button>
              <a class="navbar-brand" href="#">logo</a>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="navbar-collapse">
              <ul class="nav navbar-nav">
                <li ><a href="#">Link</a></li>
                <li><a href="#">Link</a></li>
                <li class="dropdown active">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon icon-user"></i>胡成<b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <li><a href="#">修改密码</a></li>
                    <li class="divider"></li>
                    <li><a href="#">退出</a></li>
                  </ul>
                </li>
              </ul>
            </div><!-- /.navbar-collapse -->
          </nav>
	</header>
	
	<div class="row row-main">
		<div class="col-sm-2 hidden-xs">
			<div class="panel">
              <div class="panel-heading">菜单导航</div>
              <div class="panel-body">
              	<ul class="nav navbar-nav">
		            <li class="active"><a href="###"><i class="icon-home"></i> 首页 <i class="icon-heart pull-right"></i></a></li>
		            <li><a href="###"><i class="icon-user"></i> 个人资料 <span class="label label-badge label-success pull-right">4</span></a></li>
		            <li><a href="###"><i class="icon-comment"></i> 消息 <span class="label label-dot label-danger"></span></a></li>
		            <li>
		              <a class="dropdown-toggle" data-toggle="dropdown" href="###">
		                更多 <span class="caret"></span>
		              </a>
		              <ul class="dropdown-menu">
		                <li><a href="">Lorem ipsum.</a></li>
		                <li><a href="">Optio, fuga.</a></li>
		                <li><a href="">Dicta, vero.</a></li>
		                <li><a href="">Doloribus, obcaecati.</a></li>
		              </ul>
		            </li>
		          </ul>
              </div>
              <div class="panel-footer">面板脚注</div>
            </div>	
		</div>
		<div class="col-sm-10">10</div>	
	</div>
	
	
  
	<script src="${ctxRes}/scripts/jquery.cookies.js"></script>
	<script src="${ctxRes}/plugins/zui/dist/js/zui.js" type="text/javascript"></script>
	<script src="${ctxRes}/plugins/zTree_v3/js/jquery.ztree.all-3.5.js" type="text/javascript"></script>
	<!--[if lt IE 9]>
	  <script src="${ctxRes}/plugins/zui/assets/html5shiv.js"></script>
	  <script src="${ctxRes}/plugins/zui/assets/respond.js"></script>
	<![endif]-->
	<script>
	$("header").resize(function(e){
		//alert(e.width())
		if($(this).width()<768){
			
		}
	})
	</script>
</body>
</html>