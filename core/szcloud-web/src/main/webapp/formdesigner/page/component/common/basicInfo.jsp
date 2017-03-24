<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
<input type="hidden" name="pageId" id="pageId"/>
<input type="hidden" name="componentType" id="componentType">

<div class="form-group">
	<label class="col-md-2 control-label required">名称</label>
	<div class="col-md-4">
		<div class="input-group" id="selectInput">
			<input name="name" class="form-control" id="name" type="text" value="">
			<div class="input-group-btn">
              <button class="btn btn-default" type="button" id="layoutSelect">查看关键字</button>
              </ul>
            </div>
		</div>
	</div>
</div>	

<div class="form-group">
	<label class="col-md-2 control-label required">类型</label>
	<div class="col-md-4">
		<select name="componentType" class="form-control" id="componentType" type="select" value="">
			<option value="1001">单行文本框</option>
			<option value="1002">日期文本框</option>
			<option value="1005">多行输入框</option>
			<option value="1006">下拉选项框</option>
			<option value="1007">密码框</option>
		</select>
	</div>
</div>	

<div class="form-group"><!-- 显示1列数据 -->
	<!-- 自己输入or下拉选择(dataname.itemcode) -->
	<label class="col-md-offset-1 col-md-1 control-label required">数据源</label>
	<div class="col-md-4">
		<div class="input-group" id="selectInput">
            <input type="text" class="form-control sI_input" name="dataItemCode" id="dataItemCode">
            <div class="input-group-btn">
              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
                	<span class="caret"></span>
              </button>
              <ul class="dropdown-menu pull-right sI_select">
              </ul>
            </div>
         </div>
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">显示文本</label>
	<div class="col-md-4">
		<input name="title" class="form-control" id="title" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">文本布局</label>
	<div class="col-md-4">
		<div class="input-group">
		<input type="text" readonly="readonly" class="form-control" name="labelLayoutName" id="labelLayoutName">
		<div class="input-group-btn"> <button class="btn btn-default" type="button" id="labelLayoutSelect">选择</button> </div>
		<input type="hidden" readonly="readonly"  class="form-control" name="labelLayoutId" id="labelLayoutId">
		</div>
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">序号</label>
	<div class="col-md-4">
		<input name="order" class="form-control" id="order" type="text" value="">
	</div>
</div>

<!-- <div class="form-group">
	<label class="col-md-2 control-label">文本类型</label>
	<div class="col-md-4">
		<select id="contentType" class="chosen-select form-control"
			tabindex="2" name="contentType">
			<option value=""></option>
			<option value="1001">文本</option>
			<option value="1002">数字</option>
		</select>
	</div>
</div> -->

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