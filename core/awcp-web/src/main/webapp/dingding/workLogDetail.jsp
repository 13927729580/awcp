<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<link rel="stylesheet" type="text/css" href="<%=basePath%>dingding/css/workLogView.css">
</head>
<body>
	<input type='hidden' id='ID' name='ID' value="${id }" />
	<input type='hidden' id='dynamicPageId' name='dynamicPageId' value="${dynamicPageId }" />
	<div class="report-wrap" id="viewReportPage">
		<div class="vwrp-top" id="vwrpTopDiv" style="">
			<div class="vwrp-topwrap global-hairLine">
				<img <c:if test="${user.notComment}">style="display: none" </c:if>
					class="vwrp-top-icon" src="<%=basePath %>dingding/img/comment.png">
				<img <c:if test="${user.notRead}">style="display: none" </c:if>
					class="vwrp-top-icon" src="<%=basePath %>dingding/img/read.png">
				
				<!-- 创建者用户信息 -->
				<div class="vwrp-top-userinfo global-bottomLine" id="vwrpUserInfo">
					<a href="javascript:void(0)" style="text-decoration: none;"> 
						<c:if test="${user.imgType=='img'}">
							<img class="vwrp-top-head" src="${user.img }" />
						</c:if> 
						<c:if test="${user.imgType=='divImg'}">
							<div class="vwrp-top-head" style="line-height: 2.7rem; text-align: center; 
								color: white; background-color: yellowgreen;">${user.subName }</div>
						</c:if>
					</a>
					<div class="vwrp-top-nameinfo">
						<span class="vwrp-top-username">${user.name }</span>
						<p>${user.createTime }</p>
					</div>
				</div>
				
				<!-- 文本内容信息 -->
				<ul class="vwrp-top-fieldlist" id="vwrpFieldList">
					<c:forEach var="txt" items="${txtList}">
						<li class="vwrp-top-fieldwrap global-bottomLine">
							<div class="vwrp-top-fieldname">${txt.title }</div>
							<div class="vwrp-top-fieldvalue">${txt.value }</div>
						</li>
					</c:forEach>
				</ul>
				
				<!-- 图片，文件，位置信息 -->
				<c:forEach var="other" items="${otherList}">
					<c:if test="${other.type=='img' }">
						<div class="imgs-scroll" style="border-top: 1px solid #e4e4e4;">
							<div class="vwrp-top-imglist" id="vwrpImgList">
								<c:forEach var="img" items="${other.data }">
									<img class="img" src="${img }">
								</c:forEach>
							</div>
						</div>
					</c:if>
					<c:if test="${other.type=='file' }">
						<div id="files">
							<c:forEach var="file" items="${other.data }">
								<div class="fileItem">
									<textarea style="display: none">${file.json }</textarea>
									<div class="fileIcon icon_file ${file.css }"></div>
									<div class="fileName">${file.fileName }</div>
									<div class="fileSize">${file.fileSize }KB</div>
								</div>
							</c:forEach>
						</div>
					</c:if>
					<c:if test="${other.type=='location' }">
						<div class="vwrp-top-location">${other.data }</div>
					</c:if>
				</c:forEach>
			</div>
			
			<!-- 已读信息 -->
			<div style="" class="vwrp-reader-show" id="vwrpRederShow">
				<span class="vwrp-reader-tip" data-id="hasRead"></span> 
				<span class="vwrp-reader-num" id="vwrpReaderNum">${readers.count }<span data-id="person"></span></span>
				<ul class="vwrp-reader-show-list" id="vwrpReaderShowList">
					<c:forEach var="reader" items="${readers.data }">
						<li class="vwrp-reader-show-comment">
							<c:if test="${reader.imgType=='img'}">
								<img class="vwrp-reader-show-img" src="${reader.img }" />
							</c:if> <c:if test="${reader.imgType=='divImg'}">
								<div class="vwrp-reader-show-img" style="line-height: 2.2rem; text-align: center; 
									color: white; background-color: #94d0da;">${reader.subName }</div>
							</c:if>
							<p class="vwrp-reader-show-name">${reader.name }</p></li>
					</c:forEach>
				</ul>
			</div>
	
			<!-- 评论信息 -->
			<div style="" class="vwrp-comment global-hairLine" id="vwrpComment">
				<div id="vwrpCommentLay">
					<c:if test="${comments.count==0 }">
						<span class="vwrp-comment-num" id="vwrpCommentTitle"
							data-id="noComment"></span>
					</c:if>
					<c:if test="${comments.count!=0 }">
						<span class="vwrp-comment-num" id="vwrpCommentTitle">
							<span data-id="totalHas"></span><b>${comments.count }</b><span data-id="reply"></span>
						</span>
					</c:if>
				</div>
				<ul class="vwrp-comment-list" id="vwrpCommentList">
					<c:forEach var="comment" items="${comments.data }">
						<li class="vwrp-comment-wrap">
							<c:if test="${comment.imgType=='img'}">
								<img class="vwrp-reader-show-img" src="${comment.userImg }" />
							</c:if> 
							<c:if test="${comment.imgType=='divImg'}">
								<div class="vwrp-comment-head" style="line-height: 2.2rem; text-align: center; 
									color: white; background-color: #86ecd9;">${comment.subName }</div>
							</c:if>
							<div class="vwrp-comment-content global-bottomLine">
								<span class="vwrp-comment-name">${comment.name }</span> 
								<span class="vwrp-comment-time">${comment.date }</span>
								<p class="vwrp-comment-text">${comment.content }</p>
							</div>
						</li>
					</c:forEach>
				</ul>
				<a name="vwrp-bottom-anchor"></a>
				<div id="bottomAnchor" style="height: 7.5px;"></div>
			</div>
		</div>
	</div>
	<!-- 添加评论 -->
	<div id="vwrpReportFoot">
		<div class="vwrp-comment-inputwrap" id="vwrpCommentInputWrap" style="display: none;">
			<div class="vwrp-digg-content">
				<div class="vwrp-digg-scroll" id="vwrpDiggScroll">
					<span class="vwrp-digg-item" data-id="comment1"></span>
					<span class="vwrp-digg-item" data-id="comment2"></span>
					<span class="vwrp-digg-item" data-id="comment3"></span><br>
					<span class="vwrp-digg-item" data-id="comment4"></span>
					<span class="vwrp-digg-item" data-id="comment5"></span>
					<span class="vwrp-digg-item" data-id="comment6"></span>
					<span class="vwrp-digg-item" data-id="comment7"></span><br>
					<span class="vwrp-digg-item" data-id="comment8"></span>
					<span class="vwrp-digg-item" data-id="comment9"></span>
					<span class="vwrp-digg-item" data-id="comment10"></span>
				</div>
			</div>
			<textarea class="vwrp-comment-input" id="vwrpCommentInput" maxlength="500"> </textarea>
			<button class="vwrp-comment-submit" id="vwrpCommentSubmit" type="submit" data-id="sendButton"></button>
		</div>
		<div class="vwrp-report-action-area" id="vwrpReportActionArea" style="display:block">
			<div class="vwrp-report-previous-area" id="switchToPrevious">
				<img id="previousImg" src="https://gw.alicdn.com/tfs/TB1SGUiSpXXXXaWXVXXXXXXXXXX-64-64.png">
			</div>
			<div class="vwrp-report-comment-area" id="switchToComment">
				<img src="https://gw.alicdn.com/tfs/TB1QZHJSpXXXXbqapXXXXXXXXXX-64-64.png">
				<span class="vwrp-report-action-comment-text" data-id="comment"></span>
			</div>
			<div class="vwrp-report-next-area" id="switchToNext">
				<img id="nextImg" src="https://gw.alicdn.com/tfs/TB1nWL2SpXXXXaWXVXXXXXXXXXX-64-64.png">
			</div>
		</div>
	</div>
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
		dd.ready(function(){});
		
		var imgs = [];
		$(".img").each(function(){
			imgs.push($(this).attr("src"));
		});
		$(".img").click(function(){
			dd.biz.util.previewImage({
				"urls":imgs,
				"current":$(this).attr("src")
			});
		});
		
		$(".fileItem").click(function(){
			Comm.getData("dingding/spaceid.do",{"_method":"get","type":"download"});
			var e = JSON.parse($(this).children("textarea").val());
			if(window.hasOwnProperty("DingTalkPC")){
				e.fileSize = e.fileSize * 1000;
			}
			e.corpId = dd_config.corpId;
			dd.biz.cspace.preview(e);				
		});
	</script>
</body>
</html>