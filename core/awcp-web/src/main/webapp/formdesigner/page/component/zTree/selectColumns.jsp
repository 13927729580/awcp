<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<!-- 放置校验列表 -->

<div class="columnsTable" contenteditable="false" >
	<table class="table table-bordered"  id="vt" align="right">
		<thead>
			<tr>
				<th class="required">ID</th>
				<th class="required">PID</th>
				<th class="required">NAME</th>
			</tr>
		</thead>
		<tbody id="parentColumnsBody">
			<tr>
				<td><input type="text" name="zTreeId" id="zTreeId" value=""/>
				<button class="btn btn-default zTreeId" type="button" id="zTreeId">选择</button>
				</td>
				<td><input type="text" name="pId" id="pId" value=""/>
				<button class="btn btn-default pId" type="button" id="pId">选择</button>
				</td>
				<td>
				<input type="text" name="zTreeName" id="zTreeName" value=""/>
				<button class="btn btn-default zTreeName" type="button" id="zTreeName">选择</button>
				</td>
			</tr>
		</tbody>
	</table>
</div><p/>

