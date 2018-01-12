<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <div class="form-group">
	<div class="col-md-4">
		<input name="validateInputTip" class="form-control" id="inputTip" type="text" placeholder="输入提示" value="">
	</div>
	<div class="col-md-4">
		<input name="validateErrorTip" class="form-control" id="errorTip" type="text" placeholder="错误提示" value="">
	</div>
</div>
<div class="form-group">
	<div class="col-md-4">
		<label class="checkbox-inline"><input name="validateAllowNull" type="radio" value="1" checked="checked">允许为空</label>
		<label class="checkbox-inline"><input name="validateAllowNull" type="radio" value="0">不允许为空</label>
	</div>
</div> -->
<input type="hidden" id="validatJson" name="validatJson" value="">	
<!-- 放置校验列表 -->
<div class="text-right">
	<a class="btn btn-info" id="validatorStore" >校验库</a>
	<button type="button" class="btn btn-danger" id="delValidatorBtn"><i class="icon-trash"></i>删除</button>
</div>
<div class="validatorTable" contenteditable="false" >
	<table class="table table-bordered" id="vt" align="right">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll"/></th>
				<th width="30%">名称</th>
				<th width="30%">校验类型</th>
				<th width="30%">描述</th>
			</tr>
		</thead>
		<tbody id="validatort"></tbody>
	</table>
</div>