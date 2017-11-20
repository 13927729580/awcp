<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/basicAttr.jsp" %>


<div class="form-group">
	<div class="input-group">
			<span class="input-group-addon required">模糊搜索</span>
			<label class="checkbox-inline"> <input name="supportSearch"
				type="checkbox" value="1"> 是
			</label>
			<span class="input-group-addon">多选</span>
			<label class="checkbox-inline"> <input name="supportMulti"
				type="checkbox" value="1"> 是
			</label>
		</div>
	<!--<label class="col-md-2 control-label">是否支持模糊搜索</label>
	<div class="col-md-4">
			<label class="checkbox-inline"> <input name="supportSearch"
				type="checkbox" value="1"> 是
			</label>
	</div>
	
	<label class="col-md-2 control-label">是否支持多选</label>
	<div class="col-md-4">
			<label class="checkbox-inline"> <input name="supportMulti"
				type="checkbox" value="1"> 是
			</label>
	</div>-->
</div>


<div class="form-group">
	<label class="col-md-2 control-label">选择数量</label>
	<div class="col-md-4">
		<input name="selectNumber" class="form-control" id="selectNumber" type="text" value="">
	</div>
</div>	
