<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="text-right">
	<button type="button" class="btn btn-info" id="addButton"><i class="icon-plus"></i>新增</button>
</div>
<div class="buttonsTable" contenteditable="false" >
	<table class="table table-bordered" id="vt" align="right">
		<thead>
			<tr>
				<th width="6%">名称</th>
				<th width="6%">ClassName</th>
				<th width="4%">颜色</th>
				<th width="4%">序号</th>
				<th width="24%">客户端脚本</th>
				<th width="24%">服务端脚本</th>
				<th width="24%">隐藏脚本</th>
				<th width="4%">删除</th>
			</tr>
		</thead>
		<tbody id="buttonsBody"></tbody>
	</table>
</div>