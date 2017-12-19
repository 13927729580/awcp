<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label required">数据显示别名</label>
		<div class="col-md-4 col-sm-4">
			<input name="extra" class="form-control" id="extra" type="text" value="">
		</div>
		<label class="col-md-2 col-sm-2 control-label required">类型</label>
		<div class="col-md-4 col-sm-4">
			<select id="funType" class="chosen-select form-control" tabindex="2" name="funType">
				<option value="0">获取位置</option>
				<option value="1">获取用户</option>
				<option value="2">获取组织</option>	
				<option selected="selected" value="3">其他</option>		
			</select>
		</div>
	</div>
</div>

<%@ include file="../common/basicAttr.jsp" %>
	
