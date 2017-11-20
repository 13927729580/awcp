<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
<input type="hidden" name="pageId" id="pageId"/>
<input type="hidden" name="componentType" id="componentType">
<div class="form-group">
	<label class="col-md-2 control-label required">名称</label>
	<div class="col-md-4">
		<input name="name" class="form-control" id="name" type="text" value="">
		<div class="input-group-btn">
             	<button class="btn btn-default" type="button" id="editName">编辑名称</button>
		 </div>
		<div class="input-group-btn">
             	<button class="btn btn-default" type="button" id="showKeywords">查看关键字</button>
		    </div>
	</div>
</div>	

<div class="form-group"><!-- 显示1列数据 -->
	<label class="col-md-offset-1 col-md-1 control-label required">数据源別名</label>
	<div class="col-md-4">
		<div id="selectInput">
            <textarea type="text" class="form-control" name="tableDataSources" id="tableDataSources" >"api/execute/test?resName=rows";</textarea>
     	</div>
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">序号</label>
	<div class="col-md-4">
		<input name="order" class="form-control" id="order" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">表格类型</label>
	<div class="col-md-4">
		<select name="tableType" class="form-control" id="tableType">
			<option value="datagrid">基础表格</option>
			<option value="treegrid">树形表格</option>
			<option value="combogrid">下拉网格</option>
			<option value="combotreegrid">下拉树</option>
			
		</select>
	</div>
</div>	


<div class="form-group">
	<label class="col-md-2 control-label required">布局</label>
	<div class="col-md-4">
		<div class="input-group">
		<input type="text" readonly="readonly" class="form-control" name="layoutName" id="layoutName">
		<div class="input-group-btn"> <button class="btn btn-default" type="button" id="layoutSelect">选择</button> </div>
		<input type="hidden" readonly="readonly"  class="form-control" name="layoutId" id="layoutId">
		</div>
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label">描述：</label>
	<div class="col-md-4">
		<textarea name='description' id='description' rows='4'
			class='form-control'>${vo.description}</textarea>
	</div>
</div>
