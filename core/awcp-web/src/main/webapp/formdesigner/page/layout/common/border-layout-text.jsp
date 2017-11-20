<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-group">
	<label class="col-md-2" title="">高度</label>	
	<div class="col-md-4">
		<div class="input-group">
			<input type="text" class="form-control" name="height" id="height" value="">
			<span class="input-group-addon">厘米</span>
			<select class="form-control" name="heightType" id="heightType">
            	<option value="1" selected="selected">固定值</option>
                <option value="2">最小值</option>
            </select>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="">宽度（列组件设置才有效）</label>	
	<div class="col-md-4">
		<div class="input-group">
			<input type="text" class="form-control" name="width" id="width" value="">
			<span class="input-group-addon">厘米</span>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="">边框</label>	
	<div class="col-md-4">
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="0">无边框</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="1">上边框</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="2">右边框</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="3">下边框</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="4">左边框</label>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">背景颜色</label>	
	<div class="col-md-4">
		<select name="backgroundcolor" class="form-control" id="backgroundcolor">
			<option value="white" selected="selected">白色</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="[居左|居中|居右]、[垂直居中|顶端对齐|底端对齐]">对齐方式</label>	
	<div class="col-md-4">
		<select name="textalign" class="form-control" id="textalign">
			<option value="居左" selected="selected">居左</option>
			<option value="居中">居中</option>
			<option value="居右">居右</option>
		</select>
		<select name="textverticalalign" class="form-control" id="textverticalalign">
			<option value="垂直居中" selected="selected">垂直居中</option>
			<option value="顶端对齐">顶端对齐</option>
			<option value="底端对齐">底端对齐</option>			
		</select>
	</div>
</div>