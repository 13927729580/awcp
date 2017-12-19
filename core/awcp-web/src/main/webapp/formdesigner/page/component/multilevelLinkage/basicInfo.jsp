<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label">api接口名称：</label>
		<div class="col-md-10 col-sm-10">
			<input type="text" name='select_select2_sql' id='select_select2_sql' value='province;city;area' class='form-control'/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label">select标签name值：</label>
		<div class="col-md-10 col-sm-10">
			<input type="text" name='select_select2_name' id='select_select2_name'  class='form-control' value="provinceId;cityId;id"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label">select标签值：</label>
		<div class="col-md-10 col-sm-10">
			<input type="text" name='select_select2_label' id='select_select2_label'  class='form-control' value="省：;市：;县："/>
		</div>
	</div>
</div>
<%@ include file="../common/basicAttr.jsp" %>