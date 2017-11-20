<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="page-header tab-pane " style="dispaly:none" id="hiddenConfig" >
	<div class="form-group">
		<label class="col-md-1 control-label">是否提示</label>
		<div class="col-md-5">
			<c:choose>
				<c:when test="${act.confirm == 'true' }">
					<label class="radio-inline"> <input name="confirm"
						type="radio" checked="checked" value="1">是
					</label>
					<label class="radio-inline"> <input name="confirm"
						type="radio" value="0">否
					</label>
				</c:when>
				<c:otherwise>
					<label class="radio-inline"> <input name="confirm"
						type="radio" value="1">是
					</label>
					<label class="radio-inline"> <input name="confirm"
						type="radio" checked="checked" value="0">否
					</label>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<c:choose>
		<c:when test="${act.contentType == '1' }">
			<div class="form-group ">
				<label class="col-md-1 control-label">提示类型</label>
				<div class='col-md-5'>
					<select class=" form-control col-md-1 "
						name="contentType" id="contentType">
						<option value="1" selected="selected">自定义</option>
						<option value="2">静态页面</option>
						<option value="3">动态页面</option>
					</select>
				</div>
			</div>
			<div class="form-group ">
				<label class="col-md-1 control-label" id="label-1">提示内容</label> <label
					class="col-md-1 control-label" style="display: none" id="label-2">URL</label>
				<label class="col-md-1 control-label " style="display: none"
					id="label-3">动态页面</label>
				<div class='col-md-11'>
					<input name="content" id="content"
						class="form-control" type="text"
						value="${act.content }">
				</div>
			</div>
		</c:when>
		<c:when test="${act.contentType == '2' }">
			<div class="form-group ">
				<label class="col-md-1 control-label">提示类型</label>
				<div class='col-md-5'>
					<select class=" form-control col-md-1 "
						name="contentType" id="contentType">
						<option value="1">自定义</option>
						<option value="2" selected="selected">静态页面</option>
						<option value="3">动态页面</option>
					</select>
				</div>
			</div>
			<div class="form-group ">
				<label class="col-md-1 control-label" style="display: none"
					id="label-1">提示内容</label> <label class="col-md-1 control-label"
					id="label-2">URL</label> <label class="col-md-1 control-label "
					style="display: none" id="label-3">动态页面</label>
				<div class='col-md-11'>
					<input name="content" id="content"
						class="form-control" type="text"
						value="${act.content }">
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div class="form-group ">
				<label class="col-md-1 control-label">提示类型</label>
				<div class='col-md-5'>
					<select class=" form-control col-md-1 "
						name="contentType" id="contentType">
						<option value="1">自定义</option>
						<option value="2">静态页面</option>
						<option value="3" selected="selected">动态页面</option>
					</select>
				</div>
			</div>
			<div class="form-group ">
				<label class="col-md-1 control-label" style="display: none"
					id="label-1">提示内容</label> <label class="col-md-1 control-label"
					style="display: none" id="label-2">URL</label> <label
					class="col-md-1 control-label " id="label-3">动态页面</label>
				<div class='col-md-11'>
					<input name="content" id="content"
						class="form-control" type="text"
						value="${act.content }">
				</div>
			</div>
		</c:otherwise>
	</c:choose>

	<div class="form-group">
		<label class="col-md-1 control-label">标题</label>
		<div class='col-md-5'>
			<input name="tittle" class="form-control" id="tittle"
				type="text" value="${act.tittle }">
		</div>
		<label class="col-md-1 control-label">窗口大小</label>
		<div class='col-md-5'>
			<div class="input-group">
				<span class="input-group-addon">高度</span> <input type="text"
					name="height" id="height"
					value="${act.height }" class=" form-control"
					placeholder=" px"> <span class="input-group-addon">宽度</span>
				<input type="text" name="width" id="width"
					value="${act.width }" class=" form-control"
					placeholder=" px">
			</div>
		</div>
	</div>

	<div class="form-group">
		<label class="col-md-1 control-label">页面按钮</label>
		<div class='col-md-5'>
			
			<label class="checkbox-inline"> <input
						name="buttons" type="checkbox" value="1" <c:if test="${fn:contains(act.buttons,'1')}"> checked="checked"</c:if>>确认
					</label>
					<label class="checkbox-inline"><input
						name="buttons" type="checkbox" value="0" <c:if test="${fn:contains(act.buttons,'0')}"> checked="checked"</c:if>>取消</label>
		</div>
	</div>
</div>