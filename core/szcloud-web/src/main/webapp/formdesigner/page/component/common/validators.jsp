<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-group">
	<div class="col-md-4">
		<input name="validateInputTip" class="form-control" id="inputTip" type="text" placeholder="输入提示" value="">
	</div>
	<div class="col-md-4">
		<input name="validateErrorTip" class="form-control" id="errorTip" type="text" placeholder="错误提示" value="">
	</div>
</div>
<div class="form-group">
	<div class="col-md-4">
		<label class="checkbox-inline"> <input name="validateAllowNull" type="radio"  value="1">允许为空</label>
		<label class="checkbox-inline"><input name="validateAllowNull" type="radio"   value="0"  checked="checked">不允许为空</label>
	</div>
</div>
<input type="hidden" id="validatJson" name="validatJson" value="">	
<!-- 放置校验列表 -->
<div class="text-right">
	<a class="btn" id="validatorStore" >校验库</a>
    <!-- <div class="btn-group">
       <button type="button" id="addBtn" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
       	<i class="icon-plus-sign"></i>新增  <span class="caret"></span> 
       </button>
       <ul class="dropdown-menu" role="menu">
         <li><a href="javascript:void(0)" onclick="addValidator('1');">范围校验</a></li>
         <li><a href="javascript:void(0)" onclick="addValidator('2');">正则表达式校验</a></li>
         <li><a href="javascript:void(0)" onclick="addValidator('3');">比较验证</a></li>
         <li><a href="javascript:void(0)" onclick="addValidator('4');">自定义校验</a></li>
       </ul>
    </div> -->
	<button type="button" class="btn btn-info" id="delValidatorBtn"><i class="icon-trash"></i>删除</button>
</div>
<div class="validatorTable" contenteditable="false" >
	<table class="table table-bordered"  id="vt" align="right">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAll"/></th>
				<th>名称</th>
				<th>校验类型</th>
				<th>描述</th>
			</tr>
		</thead>
		<tbody id="validatort"></tbody>
	</table>
</div>