<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-group">
	<label class="col-md-2 control-label ">是否建立文件索引</label>
	<div class="col-md-4">
		<select name="isIndex" class="form-control" id="isIndex">
			<option value="0">否</option>
			<option value="1">是</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2 control-label ">上传位置</label>
	<div class="col-md-4">
		<select name="uploadType" class="form-control" id="uploadType">
			<option value="0">mongdb</option>
			<option value="1">文件夹</option>
		</select>
	</div>
</div>
<div id="filePathDiv" class="form-group" style="display: none;">
	<label class="col-md-2 control-label required">上传文件路径</label>
	<div class="col-md-4">
		<input name="filePath" class="form-control" id="filePath" type="text" value="">
	</div>
</div>
<%@ include file="../common/basicAttr.jsp" %>
<div class="form-group">
	<label class="col-md-2 control-label required">显示方式</label>
	<div class="col-md-4">
		<select name="showType" class="form-control" id="showType">
			<option value="1">普通格式</option>
			<option value="2">表格格式</option>
		</select>
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">单个文件大小（M）</label>
	<div class="col-md-4">
		<input name="singleSize" class="form-control" id="singleSize" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">总共文件大小（M）</label>
	<div class="col-md-4">
		<input name="maxSize" class="form-control" id="maxSize" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">最大上传文件数</label>
	<div class="col-md-4">
		<input name="maxCount" class="form-control" id="maxCount" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">上传文件类型（格式：jpg,txt,）</label>
	<div class="col-md-4">
		<input name="fileKind" class="form-control" id="fileKind" type="text" value="">
	</div>
</div>
