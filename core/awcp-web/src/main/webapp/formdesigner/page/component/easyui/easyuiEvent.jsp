<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	
<!-- 脚本 -->
<div id="easyui_event">
	<div class="form-group combogrid">
		<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">下拉框值改变：(newValue, oldValue)</a></label>	
		<div class="col-md-4">
			<textarea name='combogridChangeScript' id='combogridChangeScript' rows='4' class='form-control'></textarea>
		</div>
	</div>
	<div class="form-group datagrid">
		<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">单击行：(rowIndex, rowData)</a></label>	
		<div class="col-md-4">
			<textarea name='datagridClickScript' id='datagridClickScript' rows='4' class='form-control'></textarea>
		</div>
	</div>
	<div class="form-group treegrid">
		<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">单击节点：(rowData)</a></label>	
		<div class="col-md-4">
			<textarea name='treegridClickScript' id='treegridClickScript' rows='4' class='form-control'></textarea>
		</div>
	</div>
	
</div>
	<div class="form-group contextMenu" style="display: none;">
		<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">增加节点：(node)</a></label>	
		<div class="col-md-4">
			<textarea name='treegridAppendScript' id='treegridAppendScript' rows='4' class='form-control'></textarea>
		</div>
	</div>
	<div class="form-group contextMenu" style="display: none;">
		<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">编辑节点：(node)</a></label>	
		<div class="col-md-4">
			<textarea name='treegridEditScript' id='treegridEditScript' rows='4' class='form-control'></textarea>
		</div>
	</div>
	<div class="form-group contextMenu" style="display: none;">
		<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">移除节点：(node)</a></label>	
		<div class="col-md-4">
			<textarea name='treegridRemoveItScript' id='treegridRemoveItScript' rows='4' class='form-control'></textarea>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-2"><a href="javascript:openCodeMirror('hiddenScript');">加载完成</a></label>	
		<div class="col-md-4">
			<textarea name='LoadSuccessScript' id='LoadSuccessScript' rows='4' class='form-control'></textarea>
		</div>
	</div>
