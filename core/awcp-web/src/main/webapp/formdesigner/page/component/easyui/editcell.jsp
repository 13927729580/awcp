<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<!-- 放置校验列表 -->
<div class="text-right">
	<label class="checkbox-inline"> <input id="contextMenu" name="contextMenu" value="0" type="checkbox">右键菜单</label>
	<label class="checkbox-inline"> <input id="setRowsAdd" name="setRowsAdd" value="0" type="checkbox">自动新增空白列</label>
	<label class="checkbox-inline"> <input id="setEdit" name="setEdit" value="0" type="checkbox">编辑</label>
	<button type="button" class="btn btn-info hedd" id="addColumn"><i class="icon-trash"></i>新增</button>
	<input type="text" id='cellConf' name="cellConf" style="display: none" value="0">
</div>
<table class="table table-auto">
  <thead>
    <tr class="editCellConf">
      <th class="required">列名</th>
      <th class="required">字段名</th>
      <th class="tdWidth">宽度</th>
       <th class="required">对齐方式</th>
      <th class="required editType">编辑类型</th>
      <th class="">操作</th>
    </tr>
  </thead>
  <tbody id="easyuiTableCell">
  </tbody>
</table>
