<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
<input type="hidden" name="pageId" id="pageId"/>
<input type="hidden" name="componentType" id="componentType">
<div class="form-group">
	<label class="col-md-2 control-label required">名称</label>
	<div class="col-md-4">
		<input name="name" class="form-control" id="name" type="text" value="">
		<div class="input-group-btn">
             	<button class="btn btn-default" type="button" id="showKeywords">查看关键字</button>
		    </div>
	</div>
</div>	
<div class="form-group"><!-- 显示1列数据 -->
	<!-- 自己输入or下拉选择(dataname.itemcode) -->
	<label class="col-md-offset-1 col-md-1 control-label required">附件存储数据源</label>
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
	<label class="col-md-2 control-label required">序号</label>
	<div class="col-md-4">
		<input name="order" class="form-control" id="order" type="text" value="">
	</div>
</div>


<div class="form-group">
	<label class="col-md-2 control-label ">上传位置</label>
	<div class="col-md-4">
		<select name="uploadType" class="form-control" id="uploadType">
			<option value="1">mongdb</option>
			<option value="2">文件夹</option>
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
