<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label required">日期格式</label>
		<div class="col-md-10 col-sm-10">
			<select id="dateType" class="chosen-select form-control"
				tabindex="2" name="dateType">
				<option value="yyyy-mm-dd">yyyy-mm-dd</option>
				<option value="yyyy-mm-dd hh:ii">yyyy-mm-dd hh:ii</option>
				<option value="hh:ii">hh:ii</option>
				<option value="yyyy-mm-dd hh:ii:ss">yyyy-mm-dd hh:ii:ss</option>
				<option value="yyyy">yyyy</option>
				<option value="yyyy-mm">yyyy-mm</option>
				<option value="dd/mm/yyyy">dd/mm/yyyy</option>
				<option value="dd/mm/yyyy hh:ii">dd/mm/yyyy hh:ii</option>
			</select>
		</div>
	</div>
</div>
<%@ include file="../common/basicAttr.jsp" %>
