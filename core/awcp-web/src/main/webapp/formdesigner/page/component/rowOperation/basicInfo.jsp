<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
<input type="hidden" name="pageId" id="pageId"/>
<input type="hidden" name="componentType" id="componentType">
<div class="form-horizontal">
	<div class="form-group">
		<label class="col-md-1 col-sm-1 control-label required">名称</label>
		<div class="col-md-4 col-sm-4">
			<input type="text" class="form-control" name="name" id="name">
		</div>	
		<label class="col-md-2 col-sm-2 control-label">列头名称</label>
		<div class="col-md-4 col-sm-4">
			<input type="text" class="form-control" name="columnName" id="columnName">
		</div>				
	</div>
	<div class="form-group">
		<label class="col-md-1 col-sm-1 control-label required">序号</label>
		<div class="col-md-4 col-sm-4">
			<input name="order" class="form-control" id="order" type="text" value="">
		</div>
		<label class="col-md-2 col-sm-2 control-label">列宽</label>
		<div class="col-md-4 col-sm-4">
			<input name="width" class="form-control" id="width" type="text" value="">
		</div>
	</div>
</div>	

