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
	<label class="col-md-2 control-label required">序号</label>
	<div class="col-md-4">
		<input name="order" class="form-control" id="order" type="text" value="">
	</div>
</div>
<!--
<div class="form-group">
	<label class="col-md-2 control-label">宽度</label>
	<div class="col-md-4">
		<input name="width" class="form-control" id="width" type="text" value="">
	</div>
</div>	

<div class="form-group">
	<label class="col-md-2 control-label">高度</label>
	<div class="col-md-4">
		<input name="imageHeight" class="form-control" id="imageHeight" type="text" value="">
	</div>
</div>
-->

<div class="form-group">
	<label class="col-md-2 control-label required">样式库样式</label>
	<div class="col-md-4">
		<select name="css" class="form-control" id="css">
			<option value="">请选择</option>
			<c:forEach items="${styles}" var="style">
				<option value="${style.id }">${style.name }</option>
			</c:forEach>
		</select>
	</div>
</div>	

<div class="form-group">
	<label class="col-md-2 control-label ">自定义样式</label>
	<div class="col-md-4">
		<input name="style" class="form-control" id="style" type="text" value="">
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