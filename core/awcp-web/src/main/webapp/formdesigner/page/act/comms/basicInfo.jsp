<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="pageId" id="pageId" value="${act.pageId }" />
<input type="hidden" name="code" id="code" value="${act.code }" />
<input type="hidden" name="actType" id="actType" value="${act.actType }"/>
<c:if test="${result!=null&&''!=result}">
	<span style="color: red">(${result})</span>
</c:if>
<div class="form-group">					
	<div class="col-md-offset-1 col-md-11">
		<button type="button" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
	</div>
</div>
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
	<label class="col-md-1 control-label required">按钮位置</label>
	<div class='col-md-5'>
		<div class="input-group">
			<input name="buttonGroup" id="buttonGroup" type="hidden" value="<c:if test="${act.buttonGroup eq null}">1</c:if> ${act.buttonGroup}">
			<select name="place" id="place" class="form-control">
				<option value="0" selected>页面顶部</option>
				<option value="1">表格行内</option>
			</select><script>var place="${act.place}"||"0"; document.getElementById("place").value=place;</script>
			<span class="input-group-addon required">顺序</span>
			<input name="order" class="form-control" id="order" type="text" value="${act.order }">
		</div>
	</div>
</div>

<div class="form-group">
	<label class="col-md-1 control-label required">名称</label>
	<div class='col-md-5'>
		<input name="name" class="form-control" id="name" type="text" value="${act.name }">
	</div>
	<label class="col-md-1 control-label">英文名称</label>
	<div class='col-md-5'>
		<input name="enName" class="form-control" id="enName" type="text" value="${act.enName }">
	</div>
</div>

<div class="form-group ">
	<label class="col-md-1 control-label">动作类型</label>
	<div class='col-md-5'>
		<select class=" form-control" id="selectActType" disabled="disabled">
		</select>
	</div>
	<label class="col-md-1 control-label">风格</label>
	<div class='col-md-5'>
		<div class="btn-group" id="js-btn-set">
			<div class="btn-group">
				<button id="btnIcon" type="button"
					class="btn btn-default dropdown-toggle" data-toggle="dropdown">
					<i class="<c:if test="${act.icon eq null}">icon-file</c:if> ${act.icon}"></i>图标
					<input type="hidden" name="icon" value="<c:if test="${act.icon eq null}">icon-file</c:if> ${act.icon}" />
				</button>
				<div class="dropdown-menu icon-menu">
					<a href="javascript:;" class="fa fa-save"></a>
					<a href="javascript:;" class="fa fa-edit"></a>
					<a href="javascript:;" class="fa fa-remove"></a>
					<a href="javascript:;" class="fa fa-eye"></a>
					<a href="javascript:;" class="fa fa-search"></a>
					<a href="javascript:;" class="fa fa-filter"></a>
					<a href="javascript:;" class="fa fa-power-off"></a>
					<a href="javascript:;" class="fa fa-share-alt"></a>
					<a href="javascript:;" class="fa fa-download"></a>
					<a href="javascript:;" class="fa fa-reply"></a>
					<a href="javascript:;" class="fa fa-rotate-left"></a>
					<a href="javascript:;" class="fa fa-rotate-right"></a>
					<a href="javascript:;" class="fa fa-home"></a>
					<a href="javascript:;" class="fa fa-print"></a>
					<a href="javascript:;" class="fa fa-trash"></a>
					<a href="javascript:;" class="fa fa-tasks"></a>
					<a href="javascript:;" class="fa fa-file"></a>
					<a href="javascript:;" class="fa fa-book"></a>
					<a href="javascript:;" class="fa fa-plus"></a>
					<a href="javascript:;" class="fa fa-minus"></a>
					<a href="javascript:;" class="fa fa-window-close"></a>
					<a href="javascript:;" class="fa fa-check-circle"></a>
				</div>
			</div>
			<div class="btn-group">
				<button id="btnColor" type="button"
					class="btn btn-default dropdown-toggle" data-toggle="dropdown">
					<span class="label label-dot label-<c:if test="${act.color eq null}">default</c:if>${act.color}"></span>图标名称
					<input type="hidden" name="color" value="<c:if test="${act.color eq null}">default</c:if>${act.color}" />
				</button>
				<div class="dropdown-menu color-menu">
					<span class="label label-default" data-color="default">默认</span>
					<span class="label label-primary" data-color="primary">首要</span>
					<span class="label label-success" data-color="success">成功</span> 
					<span class="label label-info" data-color="info">信息</span> 
					<span class="label label-warning" data-color="warning">警告</span> 
					<span class="label label-danger" data-color="danger">危险</span> 
				</div>
			</div>
		</div>
	</div>
</div>