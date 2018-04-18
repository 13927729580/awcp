<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="text-right">
	<button type="button" class="btn btn-info" id="addDetail"><i class="icon-plus"></i>新增</button>
</div>
<div class="columnsTable" contenteditable="false" >
	<table class="table table-bordered" id="vt" align="right">
		<thead>
			<tr>
				<th width="25%" class="required" >名称</th>
				<th width="25%" class="required">数据源</th>
				<th width="10%" class="required">序号</th>
				<th width="20%" class="required">组件类型</th>
				<th width="6%">删除</th>
			</tr>
		</thead>
		<tbody id="detailsBody"></tbody>
	</table>
</div>