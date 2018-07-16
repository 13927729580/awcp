<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="text-right">
	<button type="button" class="btn btn-info" id="addColumn"><i class="icon-plus"></i>新增</button>
</div>
<div class="columnsTable" contenteditable="false" >
	<table class="table table-bordered" id="vt" align="right">
		<thead>
			<tr>
				<th width="15%" class="required" >名称</th>
				<th width="20%" class="required">数据源</th>
				<th width="8%">宽度</th>
				<th width="8%" class="required">序号</th>
				<th width="11%">类型</th>
				<th width="30%">类型值</th>
				<th width="8%">删除</th>
			</tr>
		</thead>
		<tbody id="columnsBody"></tbody>
	</table>
</div>