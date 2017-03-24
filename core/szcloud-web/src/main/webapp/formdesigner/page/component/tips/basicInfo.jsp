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

<div class="form-group">
	<label class="col-md-2 control-label">文本类型</label>
	<div class="col-md-4">
		<select id="textType" class="chosen-select form-control"
			tabindex="2" name="textType">
			<option value="1">固定字符串</option>
			<option value="2">后台变量（显示后台数据）</option>
		</select>
	</div>
</div> 

<div class="form-group"><!-- 显示1列数据 -->
	<!-- 自己输入or下拉选择(dataname.itemcode) -->
	<label class="col-md-offset-1 col-md-1 control-label required">数据源或固定字符串</label>
	<div class="col-md-4">
		<div class="input-group" id="selectInput">
            <input type="text" class="form-control sI_input" name="title" id="title">
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
<!--
<div class="form-group">
	<label class="col-md-2 control-label required">提示文本</label>
	<div class="col-md-4">
		<input name="title" class="form-control" id="title" type="text" value="">
	</div>
</div>	
-->

<div class="form-group">
	<label class="col-md-2 control-label required">序号</label>
	<div class="col-md-4">
		<input name="order" class="form-control" id="order" type="text" value="">
	</div>
</div>


<div class="form-group">
	<label class="col-md-2 control-label">描述：</label>
	<div class="col-md-4">
		<textarea name='description' id='description' rows='4'
			class='form-control'>${vo.description}</textarea>
	</div>
</div>