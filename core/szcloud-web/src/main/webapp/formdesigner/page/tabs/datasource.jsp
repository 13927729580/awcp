<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="btn-group mb10">
	<button type="button" class="btn btn-sm btn-info" onclick="editDatasource()" >
		<i class="icon-plus-sign"></i>添加
	</button>
	<button class="btn btn-sm btn-info" id="deleteModel">
		<i class="icon-remove"></i>删除
	</button>
	<button class="btn btn-sm btn-info" id="copyModel">
		<i class="icon-remove"></i>复制
	</button>
	<button class="btn btn-sm btn-info" id="freshModel">
		<i class="icon-remove"></i>刷新
	</button>
</div>
<div class="modelTable" contenteditable="false">
	<table class="table table-bordered" id="modelTable" align="left">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAllDatasource" /></th>
				<th>名称</th>
				<th>描述</th>
			</tr>
		</thead>
		<tbody id="datasourcebody">
		</tbody>
	</table>
</div>
