<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="container-fluid">
	<!-- <div class="form-group">
		<label class="col-md-2 control-label required">下拉框宽度</label>
		<div class="col-md-4">
			<input name="tableWidth" class="form-control" id="tableWidth" type="text" value="300">
		</div>
	</div>   -->
	
	<div class="form-group">
		<label class="col-md-2 control-label">表格宽度</label>
		<div class="col-md-4">
			<input name="panelWidth" class="form-control" id="panelWidth" type="text" value="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label">表格高度</label>
		<div class="col-md-4">
			<input name="panelHeight" class="form-control" id="panelHeight" type="text" value="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label required">数据唯一字段</label>
		<div class="col-md-4">
			<input name="idField" class="form-control" id="idField" type="text" value="id">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label required">显示字段</label>
		<div class="col-md-4">
			<input name="textField" class="form-control" id="textField" type="text" value="name">
		</div>
	</div>
	<div class="form-group">
	<label class="col-md-2 control-label">是否允许多选</label>
		<div class="col-md-4">
			<select data-placeholder="是否允许多选" id="tablesingleSelect" class="form-control" tabindex="2" name="tablesingleSelect">
				<option value="true">否</option>
				<option value="false">是</option>
			</select>
		</div>
	</div>
	<div class="form-group">
	<label class="col-md-2 control-label">是否带分页器</label>
		<div class="col-md-4">
			<select data-placeholder="是否带分页器" id="tableHasPager" class="form-control" tabindex="2" name="tableHasPager">
				<option value="false">否</option>
				<option value="true">是</option>
			</select>
		</div>
	</div>
	<!-- <div class="form-group">
		<label class="col-md-2 control-label required combogrid">label名称</label>
		<div class="col-md-4">
			<input name="comboLabel" class="form-control" id="comboLabel" type="text" value="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 control-label required combogrid">label位置</label>
		<div class="col-md-4">
			<select data-placeholder="单元格宽度" id="labelPosition" class="form-control" name="labelPosition">
				<option value="left">左</option>
	  			<option value="top">上</option>
	  			<option value="right">右</option>
	  			<option value="bottom">下</option>
			</select>
		</div>
	</div> -->
	<div class="form-group">
		<label class="col-md-2 control-label">单元格宽度</label>
		<div class="col-md-4">
			<select data-placeholder="单元格宽度" id="fitColumns" class="form-control" name="fitColumns">
				<option value="true">自动设置</option>
	  			<option value="false">手动设置</option>
			</select>
		</div>
	</div>
</div>