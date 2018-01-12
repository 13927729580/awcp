<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group"> 
		<label class="col-md-2 col-sm-2 control-label required">数据源别名</label>
		<div class="col-md-10 col-sm-10">
			<input type="text" class="form-control" name="dataAlias" id="dataAlias">
		</div>
		
	</div>
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label">是否分页</label>
		<div class="col-md-4 col-sm-4">	
			<select data-placeholder="是否分页" id="hasPager" class="form-control" tabindex="2" name="hasPager">
				<option value="1">是</option>
				<option value="0">否</option>			
			</select>
		</div>
		<label class="col-md-2 col-sm-2 control-label required">分页条数</label>
		<div class="col-md-4 col-sm-4">
			<input name="pageSize" class="form-control" id="pageSize" type="text" value="10">
		</div>
	</div>
</div>
<%@ include file="../common/basicAttr_novalue.jsp" %>
