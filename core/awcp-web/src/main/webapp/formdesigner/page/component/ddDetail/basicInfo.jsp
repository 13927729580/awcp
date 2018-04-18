<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group"> 
		<label class="col-md-2 col-sm-2 control-label required">数据源别名</label>
		<div class="col-md-10 col-sm-10">
			<input type="text" class="form-control" name="dataAlias" id="dataAlias">
		</div>	
	</div>
</div>
<div class="form-horizontal">
	<div class="form-group"> 
		<label class="col-md-2 col-sm-2 control-label required">数据源</label>
		<div class="col-md-10 col-sm-10">
			<textarea rows="4" class="form-control" name="dataSource" id="dataSource"></textarea>
		</div>	
	</div>
</div>
<%@ include file="../common/basicAttr_novalue.jsp" %>
