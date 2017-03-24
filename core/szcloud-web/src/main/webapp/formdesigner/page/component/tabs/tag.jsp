<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<!-- 放置校验列表 -->
<div class="text-right">
	<button type="button" class="btn btn-info" id="addColumn"><i class="icon-trash"></i>新增</button>
	
</div>
<div class="columnsTable" contenteditable="false" >
	<table class="table table-bordered"  id="vt" align="right">
		<thead>
			<tr>
				<th class="required">标签Id</th>
				<th class="required">名称</th>
				<!-- <th>类型</th> -->
				<!-- <th>所属组标签ID</th> -->
				<th class="required">加载页面</th>
				<!-- <th>是否默认加载</th> -->
				<th class="required">序号</th>
				<th>删除</th>
				
			</tr>
		</thead>
		<tbody id="tagsBody"></tbody>
	</table>
</div>