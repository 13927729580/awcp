<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/basicAttr.jsp" %>
<div class="form-group">
	<label class="col-md-2 control-label required">是否只允许拍照上传(0：否,1：是)</label>
	<div class="col-md-4">
		<input name="extra" class="form-control" id="extra" type="text" value="0">
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
<div class="form-group">
	<label class="col-md-2 control-label required">最大上传文件数</label>
	<div class="col-md-4">
		<input name="maxCount" class="form-control" id="maxCount" type="text" value="">
	</div>
</div>
<div class="form-group">
	<label class="col-md-2 control-label required">高度</label>
	<div class="col-md-4">
		<input name="imageHeight" class="form-control" id="imageHeight" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">宽度</label>
	<div class="col-md-4">
		<input name="imageWidth" class="form-control" id="imageWidth" type="text" value="">
	</div>
</div>





