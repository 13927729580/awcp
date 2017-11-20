<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="cn.org.awcp.venson.controller.base.ControllerHelper" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="format-detection" content="telephone=no, email=no">
	<title></title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>dingding/css/wfView.css">
</head>
<body>
	<div id="MF_APP"><div><div class="dealed"><div class="modalWrapper active" >
		<div class="modal" >
			<div class="tFlexbox tFlexboxV approve approve-bg">
				<div class="approve-box hasfoot" style="margin-bottom:0px;" >
					<div class="scale-border-b approve-top approve-intro">
						<span ></span>
						<!-- 审批人信息 -->
						<div class="approve-head tTap scale-border-b">
							<div>
								<c:if test="${user.imgType=='img'}">
									<img class="approve-user-header" src="${user.img }" />
								</c:if> 
								<c:if test="${user.imgType=='divImg'}">
									<div class="tFBH tFBAC tFBJC approve-user-header" style="background: #8A8A8A;">
										${user.subName }
									</div>
								</c:if>
							</div>
							<p class="username" >${user.name }</p>
							<c:choose>
							    <c:when test="${user.approved==3}">
							    <p class="status status-completed"><%=ControllerHelper.getMessage("wf_approval_result") %></p>
							    </c:when>
							    <c:when test="${user.approved==13}">
							    <p class="status status-completed"><%=ControllerHelper.getMessage("wf_approval_reject") %></p>
							    </c:when>
							    <c:when test="${user.approved==12}">
							    <p class="status status-completed"><%=ControllerHelper.getMessage("wf_approval_undo") %></p>
							    </c:when>						    
							    <c:otherwise>
							    <p class="status status-running"><%=ControllerHelper.getMessage("waitting_for") %>[${user.nextStepPerson }] <%=ControllerHelper.getMessage("wf_approval") %></p>
							    </c:otherwise>
							</c:choose>
						</div>
						<c:choose>
						    <c:when test="${user.approved==3}">
						    	<div class="approve-result agree pullout"></div>
						    </c:when>
						    <c:when test="${user.approved==13}">
						     <div class="approve-result refuse pullout"></div>
						    </c:when>
						    <c:when test="${user.approved==12}">
						      	<div class="approve-result revoke pullout"></div>
						    </c:when>						    
						    <c:otherwise></c:otherwise>
						</c:choose>
						<div class="approve-formschema">
							<div class="line component-textfield">
								<label class="label"><%=ControllerHelper.getMessage("punUserBaseInfo-list_No") %></label>
								<span class="component-content">
									<span>${vo.workItemId }</span>
								</span>
							</div>
							<div class="line component-textareafield">
								<label class="label" ><%=ControllerHelper.getMessage("punGroup-usersView_Department") %></label>
								<span>${user.groups }</span>
							</div>
								
							<c:forEach var="item" items="${data }">
								<c:if test="${item.type=='textfield' }">
									<div class="line component-textfield">
										<label class="label">${item.title }</label>
										<span class="component-content">
											<span>${item.value }</span>
										</span>
									</div>
								</c:if>
								<c:if test="${item.type=='datefield' }">
									<div class="line component-dddatefield">
										<label class="label">${item.title }</label>
										<span class="component-content">${item.value }</span>
									</div>
								</c:if>
								<c:if test="${item.type=='textarea' }">
									<div class="line component-textarea">
										<label class="label">${item.title }</label>
										<div class="component-content">
											<p>${item.value }</p>
										</div>
									</div>
								</c:if>
								<c:if test="${item.type=='selectfield' }">
									<div class="line component-ddselectfield">
										<label class="label">${item.title }</label>
										<span class="component-content">${item.value }</span>
									</div>
								</c:if>
								<c:if test="${item.type=='file' }">
									<div class="line tAttachment-pannel">
										<label class="label">${item.title }</label>
										<div class="tAttachment-list">
											<c:forEach var="file" items="${item.value }">
												<div class="tAttachment-content tTap scale-border-all">
													<textarea style="display: none">${file.json }</textarea>
													<img src="https://gtms03.alicdn.com/tps/i3/TB11uk.KFXXXXaXXpXXZCKuHFXX-56-56.png" class="item-img">
													<div class="tAttachment-list-item">
														<div class="item-message">
															<span class="item-message-name" >${file.fileName }</span>
															<p class="item-message-size">${file.fileSize }KB</p>
														</div>
													</div>
												</div>
											</c:forEach>
										</div>
									</div>
								</c:if>
								<c:if test="${item.type=='img' }">
									<div class="line component-ddphotofield">
										<label class="label">${item.title }</label>
										<div class="annex-view" >
											<c:forEach var="src" items="${item.value }">
												<div class="annex-img-view tTap">
													<img src="${src }" class="img-view" >
												</div>
											</c:forEach>
										</div>
									</div>
								</c:if>
							</c:forEach>	
						</div>
					</div>
					<!-- 查看历史记录 -->
					<div class="attendance-record-content">
						<p ><%=ControllerHelper.getMessage("view_history_record") %></p>
						<div class="attendance-count" >
							<span class="attendance-count-text"></span>
							<img src="https://gw.alicdn.com/tps/TB1ErSvNVXXXXbNXXXXXXXXXXXX-64-64.png" class="attendance-arrow" >
						</div>
					</div>	
					<!-- 审批信息 -->
					<div>
						<div class="approval-tline-box scale-border-t-b">
							<div class="approval-tline">
								<c:forEach var="workLog" items="${workLogs }">
									<c:if test="${workLog.current_node!=-1 && workLog.current_node!=preNode }"><div style="height:10px"></div></c:if>
									<div data-node="${workLog.current_node}" class="time-node ${workLog.css } <c:if test="${workLog.current_node%2==1 }">node_0</c:if> <c:if test="${workLog.current_node%2==0 }">node_1</c:if>">
										<div class="nodebox tTap">
											<div class="nodebox-inner">
												<div>
													<c:if test="${workLog.imgType=='img' }"	>
														<img class="node-avatar" src="${workLog.user_head_img}">
													</c:if>
													<c:if test="${workLog.imgType=='divImg' }"	>
														<div class="tFBH tFBAC tFBJC node-avatar" style="background: #8A8A8A;">
															${workLog.subName }
														</div>
													</c:if>
						                            <span></span>
												</div>
												<p class="username">${workLog.name }</p>
												<p class="result-line">
													<c:choose>
													    <c:when test="${workLog.state==-1}">
													      	<%=ControllerHelper.getMessage("wfState_start") %>
													    </c:when>
													    <c:when test="${workLog.state==0}">
													      	<%=ControllerHelper.getMessage("wfState_agree") %>
													    </c:when>
													    <c:when test="${workLog.state==1}">
													      	<%=ControllerHelper.getMessage("wfState_reject") %>
													    </c:when>
													    <c:when test="${workLog.state==2}">
													      	<%=ControllerHelper.getMessage("wfState_pending") %>
													    </c:when>
													    <c:when test="${workLog.state==3}">
													      	<%=ControllerHelper.getMessage("wfState_cancel") %>
													    </c:when>
													    <c:otherwise></c:otherwise>
													</c:choose>
												</p>
												<span></span>
												<div class="opreratetime" style="display:inline">
													<span>${workLog.date }</span>
						                            <span></span>
												</div>
												<p class="node-comment"><span class="reason">${workLog.content }</span></p>
												<div class="fileField"></div>
											</div>
										</div>
									</div>									
									<c:set var="preNode" value="${workLog.current_node }"/>
								</c:forEach>
							</div>
						</div>
					</div>			
					<span></span>
					<span></span>
				</div>	
				<!-- 按钮 -->			
				<div class="tFlexbox tAlignCenter tJustifyCenter">
					<c:forEach var="act" items="${acts}">
						<div id="${act.id }" class="tFlex1 approval-action tTap action-button">${act.name }</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div></div></div></div>
	<form id="groupForm" style="display: none;">
		<input type="hidden" name="docId" id="docId" value="${vo.id}" />
		<input type="hidden" name="recordId" id="recordId" value="${vo.recordId}" />
		<input type="hidden" name="dynamicPageId" id="dynamicPageId" value="${vo.dynamicPageId}" />
		<input type="hidden" name="workflowId" id="workflowId" value="${vo.workflowId}" />
		<input type="hidden" name="instanceId" id="instanceId" value="${vo.instanceId}" />
		<input type="hidden" name="taskId" id="taskId" value="${vo.taskId}" />
		<input type="hidden" name="nodeId" id="nodeId" value="${vo.nodeId}" />
		<input type="hidden" name="WorkID" id="WorkID" value="${vo.workItemId}" />
		<input type="hidden" name="FK_Node" id="FK_Node" value="${vo.entryId}" />
		<input type="hidden" name="FK_Flow" id="FK_Flow" value="${vo.flowTempleteId}" />
		<input type="hidden" name="FID" id="FID" value="${vo.fid}" />
		<input type="hidden" name="update" id="update" value="${vo.update}" />
		<input type="hidden" name="dynamicPageName" id="dynamicPageName" value="${vo.dynamicPageName}" />	
		<input type="hidden" name="message" id="message" value="${message}" >
		<input type="hidden" name="commonType" id="commonType" value="${commonType}" />
		<input type="hidden" name="suggestion" id="suggestion" value="${suggestion}" />		
		<input type="hidden" name="suggestionType" id="suggestionType" value="${suggestionType}" />
		<input type="hidden" name="toNode" id="toNode" value="" >
		<input type="hidden" name="masterDataSource" id="masterDataSource" value="" />
        <input type="hidden" name="IsRead" id="IsRead" value=""  />
        <input type="hidden" name="actId" id="actId" value="" />
        <input type="hidden" name="slectsUserIds" id="slectsUserIds" value="" />
		<input type="hidden" name="slectsUserNames" id="slectsUserNames"  />
		<input type="hidden" name="CC_slectsUserIds" id="CC_slectsUserIds"  />
		<input type="hidden" name="dd_message_title" id="dd_message_title" />
		<input type="hidden" name="work_logs_content" id="work_logs_content"  />
	</form>
	<script src="<%=basePath%>resources/JqEdition/jquery-1.10.2.js"></script>
	<script src="<%=basePath%>venson/js/common.js"></script>
	<script>		
		if(Comm.isMobile()){
			document.write("<script src='<%=basePath%>dingding/js/dingtalk.js' charset='utf-8'><\/script>");
		}
		else{
			document.write("<script src='https://g.alicdn.com/dingding/dingtalk-pc-api/2.9.0/index.js' charset='utf-8'><\/script>");
		}
	</script>
	<script>
		if(window.hasOwnProperty("DingTalkPC")){
			window.dd = DingTalkPC;
		}
	</script>
	<script src="<%=basePath%>dingding/js/resource.js"></script>
	<script src="<%=basePath%>dingding/js/logComment.js"></script>
	<script>
		//钉钉jsapi授权
		Comm.ddConfig();	
	
		dd.ready(function(){
			var imgs = [];
			$(".annex-img-view").each(function(){
				imgs.push($(this).children("img").attr("src"));
			});
			$(".annex-img-view").click(function(){
				dd.biz.util.previewImage({
					"urls":imgs,
					"current":$(this).children("img").attr("src")
				});
			});
			
			$(".tAttachment-content").click(function(){
				Comm.getData("dingding/spaceid.do",{"_method":"get","type":"download"});
				var e = JSON.parse($(this).children("textarea").val());
				if(window.hasOwnProperty("DingTalkPC")){
					e.fileSize = e.fileSize * 1000;
				}
				e.corpId = dd_config.corpId;
				dd.biz.cspace.preview(e);				
			});
		});
		
		//查看历史记录
		$(".attendance-record-content").click(function(){
			location.href = baseUrl + 
				"dingding/historyRecord.html?dynamicPageId=${vo.dynamicPageId}&recordId=${vo.recordId}&creator=${user.creator}";
		});
	</script>
	<c:forEach var="act" items="${acts}">
		<script>
			dd.ready(function(){
				$("#${act.id}").click(function(){
					${act.clientScript}
				});
			});
		</script>
	</c:forEach>
</body>
</html>