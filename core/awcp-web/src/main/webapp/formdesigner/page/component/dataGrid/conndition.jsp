<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 放置校验列表 -->
<div class="text-right">
	<button type="button" class="btn btn-info" id="addConndition"><i class="icon-plus"></i>新增</button>	
</div>
<div class="connditionTable" contenteditable="false" >
	<table class="table table-bordered"  id="vt" align="right">
		<thead>
			<tr>
				<th>条件参数</th>
				<th width="6%">---》</th>
				<th>值</th>
				<th width="10%">是否常量</th>
				<th width="6%">删除</th>
			</tr>
		</thead>
		<tbody id="connditionBody"></tbody>
	</table>
</div>