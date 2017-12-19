<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label required">日期格式</label>
		<div class="col-md-10 col-sm-10">
			<select id="dateType" class="chosen-select form-control"
				tabindex="2" name="dateType">
				<option value="yyyy-mm-dd">yyyy-mm-dd</option>
				<option value="yyyy-mm-dd HH:ii">yyyy-mm-dd HH:ii</option>
				<option value="HH:ii">HH:ii</option>
				<option value="yyyy-mm-dd HH:ii:ss">yyyy-mm-dd HH:ii:ss</option>
				<option value="yyyy">yyyy</option>
				<option value="yyyy-mm">yyyy-mm</option>
				<option value="dd/mm/yyyy">dd/mm/yyyy</option>
				<option value="dd/mm/yyyy HH:ii">dd/mm/yyyy HH:ii</option>
			</select>
		</div>
	</div>
</div>
<%@ include file="../common/basicAttr.jsp" %>
