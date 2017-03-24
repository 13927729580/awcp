<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="<%=basePath%>WF/assets/css/jflow.css"
	rel="stylesheet">
</head>
<body>
	<div class="am-cf am-padding">
		<div class="am-fl am-cf">
			<strong class="am-text-primary am-text-lg">首页</strong> / <small>JFlow简介</small>
		</div>
	</div>
	<div>
		<table>
			<tr>
				<td class="tdMain">
					<div class="titleMain">
						<b>驰骋工作流引擎简介</b>
					</div>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;驰骋工作流引擎研发与2003年，具有.net与java两个版本，这两个版本代码结构，数据库结构，设计思想，功能组成，
						操作手册，完全相同. 导入导出的流程模版，表单模版两个版本完全通用。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						CCFlow是.net版本的简称，由济南团队负责研发，JFlow是在java版本的简称，在ccflow的基础上升级改造而来，由深圳研发团队研发。两款产品向社会100%开源，十多年来，我们一直践行自己的诺言，真心服务中国IT产业，努力提高产品质量，成为了国内知名的老牌工作流引擎。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;驰骋工作流引擎操作简单、概念通俗易懂、操作手册完善（计:14万操作手册说明书）、代码注释完整、案例丰富翔实、单元测试完整。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;驰骋工作流引擎包含表单引擎与流程引擎两大部分，并且两块完美结合，协同高效工作.</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;流程与表单界面可视化的设计，可配置程度高，适应于中国国情的多种场景的需要。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;驰骋工作流引擎发展与2003年，历经十多年的发展，在国内拥有最广泛的研究群体与应用客户群，是大型集团企业IT部门、软件公司、研究院、高校研究与应用的产品。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;济南驰骋信息技术公司是国内为数不多的开源软件并且持续盈利的公司之一，在工作流程引擎领域是国内唯一盈利的开源软件。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;驰骋工作流引擎不仅仅能够满足中小企业的需要，也能满足通信级用户的应用，先后在西门子、海南航空、中船、陕汽重卡、山东省国土资源厅、华电国际、江苏测绘院、厦门证券、天津港等国内外大型企业政府单位服役。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ccflow拥有完整的解决方案:ccform表单引擎、ccgpm权限管理系统、ccsso单点登录系统、ccoa驰骋OA、CCIM即时通讯(能够满足20万人同时在线).以上的解决方案除ccim以外都是开源的一体化的解决方案。</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;jflow 方便方便与您的开发框架集成，与第三方组织机构集成. 既有配置类型的开发适用于业务人员，IT维护人员，
					也有面向程序员的高级引擎API开发。<br />
					</p>
					<div class="titleMain">
						<b>济南驰骋团队&深圳研发团队</b><br/> 2015年5月
					</div>
				</td>
			</tr>
		</table>
	</div>
	<!-- 	<table class="am-table"> -->
	<!-- 		<tr> -->
	<!-- 			<td rowspan="2" ><img class="img1" -->
	<%-- 				src="<%=basePath%>WF/assets/i/ccProLine.png" /></td> --%>
	<!-- 			<td align="center" height="40px"><p style="font-size: 30px;color: blue">JFlow介绍</p></td> -->

	<!-- 		</tr> -->
	<!-- 		<tr> -->
	<!-- 			<td> -->
	<!-- <ul> -->

	<!-- <li>驰骋工作流引擎是济南驰骋信息技术有限公司向社会提供的一款100%开源软件，我们向社会承诺，核心代码100%的开源，多年以来我们践行自己的诺言，努力提高产品质量，奉献社会，成为了国内知名的老牌工作流引擎。</li> -->

	<!-- <li>驰骋工作流引擎具有.net与java两个版本，这两个版本代码解构，数据库解构，设计思想，功能组成， 操作手册，完全相同. 导入导出的流程模版，表单模版两个版本完全通用。</li> -->

	<!-- <li>驰骋工作流引擎简称cc， 为了区别两个版本的产品.net版本称为ccflow，网站 http://ccflow.org,  java版本称为jFlow.网站 http://jflow.cn 。</li> -->

	<!-- <li>驰骋工作流引擎包含表单引擎与流程引擎两大部分，并且两块完美结合，协同高效工作. 流程与表单界面可视化的设计， 节点，流程属性达到300多项，可配置程度高，适应于中国国情的多种场景的需要。</li> -->

	<!-- <li>驰骋工作流引擎发展与2003年，历经十多年的发展，ccflow在.net的BPM领域，在国内拥有最广泛的研究群体与应用客户群，是大型集团企业IT部门、软件公司、研究院、高校研究与应用的产品。</li> -->
	<!-- <li>济南驰骋信息技术公司是国内为数不多的开源软件并且持续盈利的公司之一，.net的BPM领域中，在国内唯一盈利的开源软件。</li> -->

	<!-- <li>驰骋工作流引擎不仅仅能够满足中小企业的需要，也能满足通信级用户的应用，先后在西门子、海南航空、陕汽重卡、山东省国土资源厅、华电国际、山东省国税局、江苏测绘院、厦门证券、天津港等国内外大型企业政府单位服役。</li> -->

	<!-- <li>ccflow拥有完整的解决方案:ccform表单引擎、ccgpm权限管理系统、ccsso单点登录系统、ccoa驰骋OA、CCIM即时通讯(能够满足20万人同时在线).以上的解决方案除ccim以外都是开源的，是整体的。</li> -->

	<!-- <li>ccflow & jflow 方便方便与您的开发框架集成，与第三方组织机构集成. 既有配置类型的开发适用于业务人员，IT维护人员， 也有面向向程序员的高级引擎API开发。</li> -->

	<!-- </ul>			</td> -->
	<!-- 		</tr> -->
	<!-- 	</table> -->
</body>
</html>