<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="form-group">
	<label class="col-md-2 control-label required">操作按钮</label>
	<div class="col-md-4">
		<label class="checkbox-inline"><input name="operateAdd" type="checkbox" value="1">新增</label> 
		<label class="checkbox-inline"><input name="operateEdit" type="checkbox" value="1">编辑</label> 
		<label class="checkbox-inline"><input name="operateSave" type="checkbox" value="1">保存</label>
		<label class="checkbox-inline"><input name="operateUndo" type="checkbox" value="1">还原</label>
		<label class="checkbox-inline"><input name="operateDelete" type="checkbox" value="1">删除</label>
	</div>
</div>	

<div class="form-group">
	<label class="col-md-2 control-label required">新增弹出页面</label>
	<div class="col-md-4">
		<select name="alertPage" class="form-control" id="alertPage" type="text" value=""></select>
	</div>
</div>	

<div class="form-group">
	<label class="col-md-2 control-label required">弹出页面高度</label>
	<div class="col-md-4">
		<input name="viewHeight" class="form-control" id="viewHeight" type="text" value="" />		
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">弹出页面宽度</label>
	<div class="col-md-4">
		<input name="viewWidth" class="form-control" id="viewWidth" type="text" value="" />
	</div>
</div>

<div class="text-right">
	<button type="button" class="btn btn-info" id="addParament"><i class="icon-plus"></i>新增传参</button>	
</div>

<div class="columnsTable" contenteditable="false" >
	<table class="table table-bordered" id="vt" align="right">
		<thead>
			<tr>
				<th>传入参数</th>
				<th width="10%">————》</th>
				<th>存储组件</th>
				<th width="10%">删除</th>
			</tr>
		</thead>
		<tbody id="paramentBody"></tbody>
	</table>
</div>