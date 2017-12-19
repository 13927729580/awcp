<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="form-horizontal row">
	<label class="col-md-2 col-sm-2 control-label required">单用户</label>
	<div class="col-md-10 col-sm-10">
		<select id="isSingle" class="chosen-select form-control" tabindex="2" name="isSingle">
			<option value="N">否</option>
			<option value="Y">是</option>
		</select>
	</div>
</div>
<%@ include file="../common/basicAttr.jsp" %>