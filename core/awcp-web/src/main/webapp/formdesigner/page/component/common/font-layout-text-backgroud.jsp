<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-group">
	<label class="col-md-2" title="宋体,黑体等">字体名称</label>	
	<div class="col-md-4">
		<select name="fontfamily" class="form-control" id="fontfamily">
			<option value="宋体" selected="selected">宋体</option>
			<option value="黑体">黑体</option>
			<option value="仿宋">仿宋GB2312</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">字体尺寸</label>	
	<div class="col-md-4">
		<select name="fontsize" class="form-control" id="fontsize">
			<option value="44">初号</option>
			<option value="36">小初</option>			
			<option value="28">28</option>			
			<option value="26">一号</option>			
			<option value="24">小一</option>			
			<option value="22">二号</option>			
			<option value="20">20</option>			
			<option value="18">小二</option>			
			<option value="16">三号</option>			
			<option value="15">小三</option>			
			<option value="14">四号</option>			
			<option value="12">小四</option>			
			<option value="10.5" selected="selected">五号</option>			
			<option value="9">小五</option>			
			<option value="7.5">六号</option>			
			<option value="6.5">小六</option>			
			<option value="5.5">七号</option>			
			<option value="5">八号</option>			
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="">文字方向</label>	
	<div class="col-md-4">
		
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">字体颜色</label>	
	<div class="col-md-4">
		<select name="fontcolor" class="form-control" id="fontcolor">
			<option value="black" selected="selected">黑色</option>	
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">字体背景颜色</label>	
	<div class="col-md-4">
		<select name="backgroundcolor" class="form-control" id="backgroundcolor">
			<option value="white" selected="selected">白色</option>
		</select>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2" title="[常规|加粗、斜体、下划线、删除线]、[上标|下标]">字体样式</label>	
	<div class="col-md-4">
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="weight">加粗</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="italic">斜体</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="linethrough">删除线</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="sup">上标</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="sub">下标</label>
		<label class="checkbox-inline"><input type="checkbox" name="textstyle" value="underline">下划线</label>
		<!-- <select name="text-style-decoration" id="text-style-decoration">
			
		</select> -->
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
<div class="form-group">
	<label class="col-md-2" title="多行文本框中有此属性：缩进、间距(段前，段后，)、换行、分页">行和段落间距</label>	
	<div class="col-md-4">
		<div class="input-group">
			<span class="input-group-addon">缩进</span>
			<input type="text" class="form-control" name="textindent" id="textindent" value="0">
			<span class="input-group-addon">pt</span>
			<span class="input-group-addon">行间距</span>
			<input type="text" class="form-control" name="lineheight" id="lineheight" value="1">
			<span class="input-group-addon">倍</span>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-md-2">option打印所有选项</label>	
	<div class="col-md-4">
		<select name="printAllOption" class="form-control" id="printAllOption">
			<option value="0">否</option>	
			<option value="1">是</option>	
		</select>
	</div>
</div>

<div class="form-group">
	<label class="col-md-2">是否上标(仅label和span有效)</label>	
	<div class="col-md-4">
		<select name="textRise" class="form-control" id="textRise">
			<option value="0">否</option>	
			<option value="1">是</option>	
		</select>
	</div>
</div>