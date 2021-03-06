<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" name="dynamicPageId" id="dynamicPageId"/>
<input type="hidden" name="pageId" id="pageId"/>
<input type="hidden" name="componentType" id="componentType">
<div class="form-group">
	<label class="col-md-2 control-label required">名称</label>
	<div class="col-md-4">
		<input name="name" class="form-control" id="name" type="text" value="">
		<div class="input-group-btn">
             	<button class="btn btn-default" type="button" id="showKeywords">查看关键字</button>
		</div>
	</div>
</div>	
<div class="form-group"><!-- 显示1列数据 -->
	<!-- 自己输入or下拉选择(dataname.itemcode) -->
	<label class="col-md-offset-1 col-md-1 control-label required">附件存储数据源</label>
	<div class="col-md-4">
		<div class="input-group" id="selectInput">
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
	<label class="col-md-2 control-label required">序号</label>
	<div class="col-md-4">
		<input name="order" class="form-control" id="order" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">样式库样式</label>
	<div class="col-md-4">
		<select name="css" class="form-control" id="css">
			<option value="">请选择</option>
			<c:forEach items="${styles}" var="style">
				<option value="${style.id }">${style.name }</option>
			</c:forEach>
		</select>
	</div>
</div>	

<div class="form-group">
	<label class="col-md-2 control-label ">自定义样式</label>
	<div class="col-md-4">
		<input name="style" class="form-control" id="style" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label ">附件类型</label>
	<div class="col-md-4">
		
		<input type="checkbox" name="mtType"  value="1"/>非版本控制&nbsp;&nbsp;
		<input type="checkbox" name="mtType1"  value="2"/>版本控制&nbsp;&nbsp;
		<input type="checkbox" name="mtType2"  value="3"/>URL链接&nbsp;&nbsp;
		<input type="checkbox" name="mtType3"  value="4"/>公文链接&nbsp;&nbsp;
		<input type="checkbox" name="mtType4"  value="5" />TIF格式扫描附件&nbsp;&nbsp;
		<input type="checkbox" name="mtType5"  value="6" />JPG格式扫描附件&nbsp;&nbsp;
		
		
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">单个文件大小（M）</label>
	<div class="col-md-4">
		<input name="singleSize" class="form-control" id="singleSize" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">总共文件大小（M）</label>
	<div class="col-md-4">
		<input name="maxSize" class="form-control" id="maxSize" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">最大上传文件数</label>
	<div class="col-md-4">
		<input name="maxCount" class="form-control" id="maxCount" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">上传文件类型（格式：jpg,txt,）</label>
	<div class="col-md-4">
		<input name="fileKind" class="form-control" id="fileKind" type="text" value="">
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label required">布局</label>
	<div class="col-md-4">
		<div class="input-group">
		<input type="text" readonly="readonly" class="form-control" name="layoutName" id="layoutName">
		<div class="input-group-btn"> <button class="btn btn-default" type="button" id="layoutSelect">选择</button> </div>
		<input type="hidden" readonly="readonly"  class="form-control" name="layoutId" id="layoutId">
		</div>
	</div>
</div>

<div class="form-group">
	<label class="col-md-2 control-label">描述：</label>
	<div class="col-md-4">
		<textarea name='description' id='description' rows='4'
			class='form-control'>${vo.description}</textarea>
	</div>
</div>
