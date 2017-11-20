<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-group">
	<div class="input-group" id="selectOption">
		<span class="input-group-addon required" title="列显示格式">格式</span>
		<select name="columnFormat" class="form-control" id="columnFormat">
			<option value="">无</option>
			<option value="1">日期</option>
			<option value="2">数字</option>
			<option value="3">字符串</option>
		</select>
		
		<span class="input-group-addon">模式</span>
		
		<input type="text" class="form-control sI_input" name="columnPatten" id="columnPatten">
        <div class="input-group-btn">
          <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
            	<span class="caret"></span>
          </button>
          <ul class="dropdown-menu pull-right sI_select">
          		<li><a href='javascript:;' data='yyyy-MM-dd'>yyyy-MM-dd</a></li>
          		<li><a href='javascript:;' data='yyyy-MM-dd HH:mm'>yyyy-MM-dd HH:mm</a></li>
          		<li><a href='javascript:;' data='yyyy-MM-dd HH:mm:ss'>yyyy-MM-dd HH:mm:ss</a></li>
          		<li><a href='javascript:;' data='yyyy年MM月dd日'>yyyy年MM月dd日</a></li>
          		<li><a href='javascript:;' data='0.00'>保留小数两位</a></li>
          		<li><a href='javascript:;' data='0.0'>保留小数1位</a></li>
          		<li><a href='javascript:;' data=''>字符串截取多少位自填</a></li>
          		<li><a href='javascript:;' data='自行输入格式'>自定义</a></li>
          </ul>
        </div>
	</div>
</div>


