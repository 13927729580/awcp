<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<input type="hidden" name="pageId" id="pageId" value="${act.pageId }" />
	<input type="hidden" name="code" id="code" value="${act.code }" />
	<div class="form-group">
		<label class="col-md-1 control-label">所属页面</label>
		<div class="col-md-5">
			<select class=" form-control" tabindex="2" name="dynamicPageId" id="dynamicPageId">
				<c:forEach items="${pages}" var="page">
					<c:choose>
						<c:when test="${act.dynamicPageId ==  page.id}">
							<option value="${page.id}" selected="selected">${page.name}</option>
						</c:when>
						<c:otherwise>
							<option value="${page.id}">${page.name}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
		</div>
		<label class="col-md-1 control-label">按钮组</label>
		<div class='col-md-5'>
			<div class="input-group">
				<input name="buttonGroup" class="form-control" id="buttonGroup" type="text" value="${act.buttonGroup }"> 
				<span class="input-group-addon">顺序</span> 
				<input name="order" class="form-control" id="order" type="text" value="${act.order }">
			</div>
		</div>		
	</div>

	<div class="form-group">
		<label class="col-md-1 control-label">名称</label>
		<div class='col-md-5'>
			<input name="name" class="form-control" id="name" type="text" value="${act.name }"> 
		</div>
		<label class="col-md-1 control-label">英文名称</label>
		<div class='col-md-5'>
			<input name="enName" class="form-control" id="enName" type="text" value="${act.enName }"> 
		</div>
		
		<label class="col-md-1 control-label">风格</label>
		<div class='col-md-5'>
			<div class="btn-group" id="js-btn-set">	          
	            <div class="btn-group">
					<button id="btnIcon" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<i class="<c:if test="${act.icon eq null}">icon-file</c:if> ${act.icon}"></i>图标
						<input type="hidden" name="icon" value="<c:if test="${act.icon eq null}">icon-file</c:if> ${act.icon}"/>
					</button>
					<div class="dropdown-menu icon-menu">
						<a href="javascript:;" class="icon-save"></a>
						<a href="javascript:;" class="icon-search"></a>
						<a href="javascript:;" class="icon-off"></a>
						<a href="javascript:;" class="icon-share"></a>
						<a href="javascript:;" class="icon-edit"></a>
						<a href="javascript:;" class="icon-remove"></a>
						<a href="javascript:;" class="icon-download"></a>
						<a href="javascript:;" class="icon-repeat"></a>
						<a href="javascript:;" class="icon-undo"></a>
						<a href="javascript:;" class="icon-home"></a>
						<a href="javascript:;" class="icon-print"></a>
						<a href="javascript:;" class="icon-trash"></a>
						<a href="javascript:;" class="icon-tasks"></a>
						<a href="javascript:;" class="icon-reply"></a>
						<a href="javascript:;" class="icon-file"></a>
						<a href="javascript:;" class="icon-file-text"></a>
						<a href="javascript:;" class="icon-plus"></a>
						<a href="javascript:;" class="icon-minus"></a>
					</div>
	            </div>
	            <div class="btn-group">
					<button id="btnColor" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						<span class="label label-dot label-<c:if test="${act.color eq null}">default</c:if>${act.color}"></span>图标名称
						<input type="hidden" name="color" value="<c:if test="${act.color eq null}">default</c:if>${act.color}"/>
					</button>
					<div class="dropdown-menu color-menu">
						<span class="label label-default" data-color="default">#696969</span>
						<span class="label label-primary" data-color="primary">流程信息</span>
						<span class="label label-success" data-color="success">保存</span>
						<span class="label label-info" data-color="info">办理意见</span>
						<span class="label label-warning" data-color="warning">承办完毕</span>
						<span class="label label-danger" data-color="danger">发送</span>
						<span class="label label-sback" data-color="sback">回退</span>
						<span class="label label-back" data-color="back">回退直</span>
						<span class="label label-close" data-color="close">关闭</span>
						<span class="label label-print" data-color="print">打印</span>
					</div>
	            </div>
          	</div>
        </div>				
	</div>