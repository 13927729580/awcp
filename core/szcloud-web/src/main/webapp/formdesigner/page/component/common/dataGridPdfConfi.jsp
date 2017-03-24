<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-group">
		<label class="col-md-2 control-label">宽度比例,按比例分配,支持小数（如：3,2,3,2）</label>
		<div class="col-md-4">
			<input type='text' name='proportions' id='proportions' 	class='form-control' />
		</div>
        	 
        <label class="col-md-2 control-label">pdf打印模板页面ID</label>
		<div class="col-md-4">
			<input type='text' name='pdfTemplatePage' id='pdfTemplatePage' value="${vo.pdfTemplatePage}"	class='form-control' />
		</div>
       
</div>	
<div class="form-group">
		<label class="col-md-2 control-label">pdf打印最小条数</label>
        <div class="col-md-4">
			<input type='text' name='minLineCount' id='minLineCount' class='form-control' />
		</div>
        	 
        <label class="col-md-2 control-label">pdf打印最大条数</label>
		<div class="col-md-4">
			<input type='text' name='maxLineCount' id='maxLineCount' 	class='form-control' />
		</div>
</div>
<div class="form-group">
	 <label class="col-md-2 control-label">每一条数据打印行高</label>
		<div class="col-md-4">
			<input type='text' name='lineHeight' id='lineHeight' class='form-control' />
		</div>
	
	<label class="col-md-2 control-label">pdf打印行高类型</label>
		<div class="col-md-4">
		
			<select data-placeholder="选择高度类型" id="lineHeightType" class="form-control" tabindex="2" name="lineHeightType">
				<option value="">请选择</option>
				<option value="1">固定行高</option>
				<option value="2">最小行高</option>
				
			</select>
		</div>
</div>