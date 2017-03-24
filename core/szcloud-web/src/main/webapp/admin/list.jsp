<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>   
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>政务云应用管理平台</title>
<link rel="stylesheet" type="text/css" href="./base/resources/zui/dist/css/zui.css">
<link rel="stylesheet" type="text/css" href="./resources/styles/zTreeStyle/zTreeStyle.css">
<link title="default" rel="stylesheet" type="text/css" href="./base/resources/content/styles/szCloud_common.css" />
<link title="default" rel="stylesheet" type="text/css" href="./resources/plugins/zTree_v3/css/style.css" />
<link title="default" rel="stylesheet" type="text/css" href="./resources/plugins/zTree_v3/css/jquery.jscrollpane.css" />
<link href="./base/resources/artDialog/css/ui-dialog.css"	rel="stylesheet" >

</head>

<body>
<div class="comArrowUp" id="comArrow"></div>
<div class="TopBox">
	<!--logo start-->
	<div class="C_logoBox">
    	<ul>
        	<li class="C_logo"></li>
            <li class="C_date">管理员，下午好  2014年8月29日</li>
        </ul>
    </div>
    <!--logo end-->
    <div class="C_menuBox">
    	<!--searchBar start-->
    	<div class="C_searchBar">
        	<div class="C_search">
            	<ul>
                	<li>
                    	<div class="SearchText">待办事项</div>
                        <div class="SearchInputBox"><input type="text" class="SearchInput" onfocus="javascript:if(this.value=='请输入关键字')this.value='';" onBlur="javascript:if(this.value=='')this.value='请输入关键字';">
                        </div>
                        <div class="SearchButton"></div>
                    </li>
                    <li>
                    	<span><a href="javascript:void(0)">设置</a>  <a href="logout.do">退出</a></span>
                    </li>
                </ul>
            </div>
        </div>
        <!--searchBar end-->
    	<div class="C_blank"></div>
        <!--dropMenu start-->
        <div class="C_navMenuBox">
        
                        <div class="MenuList">
                          <!--navigation-up start-->
                          <div class="navigation-up">
                              <div class="navigation-inner">
                                  <!--v3 start-->
                                  <div class="navigation-v3">
                                      <ul>
                                      </ul>
                                      <!--message start-->
                                      <div class="C_mesBox">
                                          <a href="javascript:void(0)">
                                          <span style=" height:30px;vertical-align:middle;"><img src="content/images/style_01/TopIconMes.png" alt=""/></span>
                                          <span style=" height:30px;vertical-align:middle;">消息</span>
                                          </a>
                                      </div>
                                      <!--message end-->
                                  </div>
                                  <!--v3 end-->
                              </div>
                          </div>
                          <!--navigation-up end-->
                          
                      </div>
        	          <!--menulist end-->
        
        </div>
        <!--dropMenu end-->
    </div>
</div>



	
<!--navigation-down start-->
                         
                          <div class="navigation-down">
                              <div id="platform" class="nav-down-menu menu-1" style="display: none;" _t_nav="platform">
                                  <div class="navigation-down-inner">
                                      <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                          <dd>
                                              <!--空位，防止水平滚动条遮蔽-->&nbsp;
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      
                                      <dl class="odd clearfix">
                                          <dt>公文统计1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>公文设置1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我文设置1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的文设1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我文设置1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的文设1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我文设置1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的文设1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我文设置1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的文设1</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div id="online" class="nav-down-menu menu-1" style="display: none;" _t_nav="online">
                                  <div class="navigation-down-inner">
                                  <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>公文统计2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                       <dl class="odd clearfix">
                                          <dt>公文统计2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护2</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div id="project" class="nav-down-menu menu-1" style="display: none;" _t_nav="project">
                                  <div class="navigation-down-inner">
                                      <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      
                                      <dl class="odd clearfix">
                                          <dt>公文统计3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置3</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div id="workflow" class="nav-down-menu menu-1" style="display: none;" _t_nav="workflow">
                                  <div class="navigation-down-inner">
                                  <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>公文统计4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                       <dl class="odd clearfix">
                                          <dt>公文统计4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护4</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div id="business" class="nav-down-menu menu-1" style="display: none;" _t_nav="business">
                                  <div class="navigation-down-inner">
                                      <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      
                                      <dl class="odd clearfix">
                                          <dt>公文统计5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                       <dl class="odd clearfix">
                                          <dt>流转应用管理5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置5</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div id="workflow" class="nav-down-menu menu-1" style="display: none;" _t_nav="workflow">
                                  <div class="navigation-down-inner">
                                      <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>公文统计6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>公文统计6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护6</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div id="plan" class="nav-down-menu menu-1" style="display: none;" _t_nav="plan">
                                  <div class="navigation-down-inner">
                                      <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      
                                      <dl class="odd clearfix">
                                          <dt>公文统计7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置7</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div id="moremenu" class="nav-down-menu menu-1" style="display: none;" _t_nav="moremenu">
                                  <div class="navigation-down-inner">
                                      <dl style="margin-left:0px;" class="odd clearfix">
                                          <dt>公文流转8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">新建公文</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文草稿</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">待办公文</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>新建流转应用8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">流转审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">公文审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">财务审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>流转应用管理8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">人事审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">综合审批</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">项目审批</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>我的公文设置8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文授权</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">类别授权</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>公文统计8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                      <dl class="odd clearfix">
                                          <dt>公文统计8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">按模板统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">个人公文统计</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">步骤处理统计</a>
                                          </dd>
                                      </dl>
                                      <dl class="even clearfix">
                                          <dt>系统维护8</dt>
                                          <dd>
                                              <a href="javascript:void(0)">公文锁管理</a>
                                          </dd>
                                          <dd>
                                              <a href="javascript:void(0)">模板维护</a>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              
                          
                          </div>
<!--navigation-down end-->


<div class="ContentBox clearfix">

    <!--treeMenu start-->
	<div class="C_treeMenuBox">
       <ul class="CloudMenu"></ul>    
    </div>
    <!--treeMenu end-->   
    
    <div class="C_contentFrame">
    	<iframe src="main.jsp" id="main" name="main" scrolling="auto" frameborder="0" width="100%" height="100%;"></iframe> 
    </div>


</div>

<script type="text/javascript" src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js"> </script>
<script src="./base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
<script src="./base/resources/zui/src/js/transition.js" language="javascript" type="text/javascript"></script>
<script src="./base/resources/content/scripts/jquery.mousewheel.js" language="javascript" type="text/javascript"></script>
<script src="./base/resources/content/scripts/jquery.jscrollpane.min.js" language="javascript" type="text/javascript"></script>
<script src="./base/resources/content/scripts/jquery.ztree.core-3.4.min.js" language="javascript" type="text/javascript"></script>
<script src="./base/resources/content/scripts/jquery.szCloudList.ui.js" language="javascript" type="text/javascript"></script>

<script type="text/javascript" 	src="./base/resources/artDialog/dist/dialog-plus-min.js"></script>
<script language="javascript">
var setting = {
	view: {
		showLine: false,
		showIcon: true
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback:{
		onClick: zTreeOnClick
	}
};	

function showIconForTree(treeId, treeNode) {
	return !treeNode.isParent;
};
function zTreeOnClick(event, treeId, treeNode) {
    $(".ztree a").removeClass("curSelectedNode");
    $(".ztree #"+treeNode.tId+"_a").addClass("curSelectedNode");
};
function creatMenu(){
	var dataLi="";
	$.ajax({
		type:"post",
		dataType:"json",
		url:"<%=basePath%>getZTreeNode.do",
		async:false,//同步
		data:{"userId":1,"groupId":1,"roleId":1},
		success:function(dataMap){
			 $.each(dataMap,function(i,item){
				 dataLi ='<li><ul id="tree'+i+'" class="ztree"></ul></li>';//<li><div class="header"><span class="label">'+item[0].name+'</span><span class="arrow down"></span></div><ul id="tree'+i+'" class="ztree"></ul></li>
				  $(dataLi).appendTo(".CloudMenu");
				  $.fn.zTree.init($("#tree"+i),setting,item);
				  //$("#tree"+i+" > li").eq(0).remove();
			});		
		}
		
	});
}

$(document).ready(function(){

	creatMenu();
	
	/*treeMenu*/
	$(".CloudMenu li .header").live("click",function(){		   
		var arrow = $(this).find("span.arrow");
		if(arrow.hasClass("up")){
			arrow.removeClass("up");
			arrow.addClass("down");
		}else if(arrow.hasClass("down")){
			arrow.removeClass("down");
			arrow.addClass("up");
		}
		$(this).parent().find("ul.ztree").slideToggle(300);
		
	});
	
});
</script>
</body>
</html>
