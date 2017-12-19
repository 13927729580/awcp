<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
	<input type="hidden" name="pageId" id="pageId"/>
	<input type="hidden" name="componentType" id="componentType">
	<div class="form-group">
		<label class="col-sm-2 col-sm-2 control-label required">名称(name)</label>
		<div class="col-md-10 col-sm-10">
			<input name="name" class="form-control" id="name" type="text" value="">
			<div class="input-group-btn">
	             	<button class="btn btn-default" type="button" id="editName">编辑名称</button>
			 </div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label required">文本(label-zh)</label>
		<div class="col-md-4 col-sm-4">
			<input name="title" class="form-control" id="title" type="text" value="">
		</div>
		<label class="col-md-2 col-sm-2 control-label">文本(label-en)</label>
		<div class="col-md-4 col-sm-4">
			<input name="enTitle" class="form-control" id="enTitle" type="text" value="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label required">序号</label>
		<div class="col-md-4 col-sm-4">
			<input name="order" class="form-control" id="order" type="text" value="">
		</div>
		<label class="col-md-2 col-sm-2 control-label required">布局</label>
		<div class="col-md-4 col-sm-4">
			<div class="input-group">
			<input type="text" readonly="readonly" class="form-control" name="layoutName" id="layoutName">
			<div class="input-group-btn"> <button class="btn btn-default" type="button" id="layoutSelect">选择</button> </div>
			<input type="hidden" readonly="readonly"  class="form-control" name="layoutId" id="layoutId">
			</div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label ">自定义样式(style)</label>
		<div class="col-md-10 col-sm-10">
			<textarea class="form-control" name='style' id='style' rows='4'
				class='form-control'></textarea>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label">描述：(title)</label>
		<div class="col-md-10 col-sm-10">
			<textarea class="form-control" name='description' id='description' rows='4'
				class='form-control'></textarea>
		</div>
	</div>
</div>