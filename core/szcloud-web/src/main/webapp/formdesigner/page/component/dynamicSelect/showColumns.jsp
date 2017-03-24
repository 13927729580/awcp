<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<!-- 放置校验列表 -->
<div class="columnsTable" contenteditable="false" >
	<table class="table table-bordered"  id="vt" align="right">
		<thead>
			<tr>
				<th class="required">显示值</th>
				<th class="required">显示条件</th>
				<th class="required">返回值</th>
			</tr>
		</thead>
		<tbody id="showColumnsBody"></tbody>
			  <tr>
			  	<td><input type="text" name="showName" id="showName" value=""/>
				<button class="btn btn-default showName" type="button" id="showName">选择</button>
				</td>
				<td><input type="text" name="showWhere" id="showWhere" value=""/>
				<button class="btn btn-default showWhere" type="button" id="showWhere">选择</button>
				</td>
				<td><input type="text" name="returnVal" id="returnVal" value=""/>
				<button class="btn btn-default returnVal" type="button" id="returnVal">选择</button>
				</td>
			  </tr>
	</table>
</div>