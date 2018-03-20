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
		<label class="col-md-2 col-sm-2 control-label required">序号</label>
		<div class="col-md-4 col-sm-4">
			<input name="order" class="form-control" id="order" type="text" value="">
		</div>
		<label class="col-md-2 col-sm-2 control-label required">是否必填</label>
		<div class="col-md-4 col-sm-4">
			<input id="required" name="required" type="checkbox" value="1"> 是</label>
		</div>
	</div>

	<div class="form-group"><!-- 显示1列数据 -->
		<!-- 自己输入or下拉选择(dataname.itemcode) -->
		<label class="col-md-2 col-sm-2 control-label required">数据源</label>
		<div class="col-md-10 col-sm-10">
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
		<label class="col-md-2 col-sm-2 control-label required">布局</label>
		<div class="col-md-4 col-sm-4">
			<div class="input-group">
				<input type="text" readonly="readonly" class="form-control" name="layoutName" id="layoutName">
				<div class="input-group-btn"> <button class="btn btn-default" type="button" id="layoutSelect">选择</button> </div>
				<input type="hidden" readonly="readonly"  class="form-control" name="layoutId" id="layoutId">
			</div>
		</div>
		<label class="col-md-2 col-sm-2 control-label required">样式库样式(css)</label>
		<div class="col-md-4 col-sm-4">
			<select name="css" class="form-control" id="css">
				<option value="">请选择</option>
				<c:forEach items="${styles}" var="style">
					<option value="${style.id }">${style.name }</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label ">自定义样式(style)</label>
		<div class="col-md-10 col-sm-10">
			<textarea class="form-control" name='style' id='style' rows='3'
					  class='form-control'></textarea>
		</div>
	</div>
</div>