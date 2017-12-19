<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label required">高度</label>
		<div class="col-md-4 col-sm-4">
			<input name="imageHeight" class="form-control" id="imageHeight" type="text" value="">
		</div>
		<label class="col-md-2 col-sm-2 control-label required">宽度</label>
		<div class="col-md-4 col-sm-4">
			<input name="imageWidth" class="form-control" id="imageWidth" type="text" value="">
		</div>
	</div>
	<div class="form-group"> 
		<label class="col-md-2 col-sm-2 control-label required">数据源别名</label>
		<div class="col-md-4 col-sm-4">
			<input type="text" class="form-control" name="dataAlias" id="dataAlias">
		</div>
		<label class="col-md-2 col-sm-2 control-label">是否带分页器</label>
		<div class="col-md-4 col-sm-4">	
			<select data-placeholder="是否带分页器" id="hasPager" class="form-control" tabindex="2" name="hasPager">
				<option value="1">是</option>
				<option value="0">否</option>			
			</select>
		</div>
	</div>
</div>
<%@ include file="../common/basicAttr_novalue.jsp" %>
