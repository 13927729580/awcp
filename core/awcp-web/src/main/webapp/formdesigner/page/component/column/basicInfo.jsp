<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
<input type="hidden" name="pageId" id="pageId"/>
<input type="hidden" name="componentType" id="componentType">
<div class="form-group">
	<div class="col-md-12">
		<div class="input-group">
			<span class="input-group-addon required">名称</span>
			<input name="name" class="form-control" id="name" type="text" value="">
			<!--新增一个查看关键字的按钮-->
			<div class="input-group-btn">
             	<button class="btn btn-default" type="button" id="showKeywords">查看关键字</button>
		    </div>
			<span class="input-group-addon required">列头名称</span>
			<input type="text" class="form-control" name="columnName" id="columnName" value="">
		</div>
	</div>	
</div>	

<div class="form-group">
	<div class="col-md-12">
		<div class="input-group">
			<span class="input-group-addon required">允许排序</span>
			<select name="alloworderby" class="form-control" id="alloworderby">
				<option value="1">是</option>
				<option value="-1" selected="selected">否</option>
			</select>
			<span class="input-group-addon">排序类型</span>
			<select name="orderby" class="form-control" id="orderby">
				<option value="asc"  selected="selected">升序</option>
				<option value="desc">降序</option>
			</select>
			<span class="input-group-addon">排序字段名称</span>
			<input type="text" class="form-control" name="sortName" id="sortName" value="">
		</div>
	</div>	
</div>	

<div class="form-group"><!-- 显示1列数据 -->
	<!-- 自己输入or下拉选择(dataname.itemcode) -->
	<div class="col-md-12">
		<div class="input-group" id="selectInput">
			<span class="input-group-addon required">数据源</span>
            <input type="text" class="form-control sI_input" name="dataItemCode" id="dataItemCode">
            <div class="input-group-btn">
              <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
                	<span class="caret"></span>
              </button>
              <ul class="dropdown-menu pull-right sI_select">
              </ul>
            </div>
         </div>
	</div>
</div>

<div class="form-group">
	<div class="col-md-12">
		<div class="input-group">
			<span class="input-group-addon required">序号</span>
			<input type="text" class="form-control" name="order" id="order" value="">
			<span class="input-group-addon">列宽</span>
			<input type="text" class="form-control" name="width" id="width" value="">
			<span class="input-group-addon">pdf打印列宽(cm)</span>
			<input type="text" class="form-control" name="pdfWidth" id="pdfWidth" value="">
		</div>
	</div>	
</div>
<div class="form-group">
	<div class="col-md-12" id="selectOption">
		<div class="input-group">
			<span class="input-group-addon required" title="列显示格式">格式化类型</span>
			<select name="columnFormat" class="form-control" id="columnFormat">
				<option selected="selected" value="">无</option>
				<option value="1">日期</option>
				<option value="2">数字</option>
				<option value="3">字符串</option>
			</select>
			<span class="input-group-addon">格式</span>
			<select name="columnPatten" class="form-control" id="columnPatten">
				<option selected="selected" value="yyyy-MM-dd">yyyy-MM-dd</option>
				<option value="yyyy-MM-dd HH:mm">yyyy-MM-dd HH:mm</option>
				<option value="yyyy-MM-dd HH:mm:ss">yyyy-MM-dd HH:mm:ss</option>
				<option value="yyyy年MM月dd日">yyyy年MM月dd日</option>
				<option value="0.00">保留小数两位</option>
				<option value="0.0">保留小数1位</option>
			</select>
		</div>
	</div>
</div>
<div class="form-group">
	<div class="col-md-12">
		<div class="input-group">
			<span class="input-group-addon">描述：</span>
			<textarea name='description' id='description' rows='4' class='form-control'>${vo.description}</textarea>
		</div>
	</div>	
</div>

