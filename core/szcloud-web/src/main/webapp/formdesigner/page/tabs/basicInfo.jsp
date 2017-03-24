<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<input type="hidden" value="${vo.dataJson}" name="dataJson" id="modelJsonArray" >		
<input type="hidden" name="id" value="${vo.id}" id="id">
<div class="form-group">
		<label class="col-md-1 control-label">名称</label>
		<div class="col-md-4">
			<input type='text' name='name' id='name' value="${vo.name}"	class='form-control' />
		</div>
		<label class="col-md-2 control-label">类型</label>
		<div class="col-md-4">
		<!-- 类型 -->
			<select data-placeholder="输入组件名称..." id="pageType" class="form-control" tabindex="2" name="pageType">
				<c:choose>
				        	 		<c:when test="${vo.pageType == '1001' }">
										<option value="1001" selected="selected">普通页面</option>
										<option value="1002">表单页面</option>
										<option value="1003">列表页面</option>
				        	 		</c:when>
				        	 		<c:when test="${vo.pageType == '1002' }">
										<option value="1001">普通页面</option>
										<option value="1002" selected="selected">表单页面</option>
										<option value="1003">列表页面</option>
				        	 		</c:when>
				        	 		<c:otherwise>
										<option value="1001">普通页面</option>
										<option value="1002">表单页面</option>
										<option value="1003" selected="selected">列表页面</option>
				        	 		</c:otherwise>
				        	 	</c:choose>
				
			</select>
		</div>
		
</div>
<div class="form-group listPageConfig" style="display: none;">
	<label class="col-md-1 control-label">显示记录总数</label>
	<div class="col-md-4">
		<c:choose>
			<c:when test="${vo.showTotalCount == '1'}">
				<label class="radio-inline"> <input name="showTotalCount" type="radio" checked="checked" value="1"  >是</label>
				<label class="radio-inline"><input name="showTotalCount" type="radio"   value="0" >否</label>
			</c:when>
			<c:otherwise>
				<label class="radio-inline"> <input name="showTotalCount" type="radio" value="1"  >是</label>
				<label class="radio-inline"><input name="showTotalCount" type="radio"  checked="checked" value="0" >否</label>
			</c:otherwise>
		</c:choose>
			
	</div>
	<label class="col-md-2 control-label">分页</label>
	<div class="col-md-4">
		<c:choose>
			<c:when test="${vo.isLimitPage == '1'}">
				<label class="radio-inline"> <input name="isLimitPage" type="radio" checked="checked" value="1"  >是</label>
				<label class="radio-inline"><input name="isLimitPage" type="radio"   value="0" >否</label>
			</c:when>
			<c:otherwise>
				<label class="radio-inline"> <input name="isLimitPage" type="radio" value="1"  >是</label>
				<label class="radio-inline"><input name="isLimitPage" type="radio"  checked="checked" value="0" >否</label>
			</c:otherwise>
		</c:choose>
		<input type='text' name='pageSize' id='pageSize' value="${vo.pageSize}"	class='form-control' />条记录/页
	</div>
</div>
<div class="form-group listPageConfig" style="display: none;">
	<label class="col-md-1 control-label">显示序号</label>
	<div class="col-md-4">
		<c:choose>
			<c:when test="${vo.showReverseNum == '1'}">
				<label class="radio-inline"> <input name="showReverseNum" type="radio" checked="checked" value="1"  >是</label>
				<label class="radio-inline"><input name="showReverseNum" type="radio"   value="0" >否</label>
			</c:when>
			<c:otherwise>
				<label class="radio-inline"> <input name="showReverseNum" type="radio" value="1"  >是</label>
				<label class="radio-inline"><input name="showReverseNum" type="radio"  checked="checked" value="0" >否</label>
			</c:otherwise>
		</c:choose>
		<input type='text' name='reverseNumMode' id='reverseNumMode' value="${vo.reverseNumMode}" class='form-control' />序号格式化模式（模式格式如：0000）
	</div>
	<label class="col-md-2 control-label">序号排序方式</label>
	<div class="col-md-4">
		<c:choose>
			<c:when test="${vo.reverseSortord == '1'}">
				<label class="radio-inline"> <input name="reverseSortord" type="radio" checked="checked" value="1"  >降序</label>
				<label class="radio-inline"><input name="reverseSortord" type="radio"   value="0" >升序</label>
			</c:when>
			<c:otherwise>
				<label class="radio-inline"> <input name="reverseSortord" type="radio" value="1"  >降序</label>
				<label class="radio-inline"><input name="reverseSortord" type="radio"  checked="checked" value="0" >升序</label>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<div class="form-group">
		<label class="col-md-1 control-label">样式库</label>
        	 <div class='col-md-4'>
        	 <!-- 样式库  -->
        	 	 <div class="input-group">
        	 	 		<select  id="styleId" class=" form-control"  data-placeholder="输入样式名称..." tabindex="2" name="styleId">
							<option value="${vo.styleId}"></option>
						</select>
		        	 		<span class="input-group-btn"> 
		        	 	<button id="addStyle" class="btn btn-default" type="button">新增</button>
		        	  </span>
	        	 </div>
        	 </div>
		<label class="col-md-2 control-label">记录操作</label>
		<div class="col-md-4">
			<c:choose>
				        	 		<c:when test="${vo.isLog=='1'}">
										<label class="radio-inline"> <input name="isLog" type="radio" checked="checked" value="1"  >是</label>
										<label class="radio-inline"><input name="isLog" type="radio"   value="0" >否</label>
				        	 		</c:when>
				        	 		<c:otherwise>
										<label class="radio-inline"> <input name="isLog" type="radio"  value="1"  >是</label>
										<label class="radio-inline"><input name="isLog" type="radio"  checked="checked" value="0" >否</label>
				        	 		</c:otherwise>
			</c:choose>
			
		</div>
</div>	
<div class="form-group">
		<label class="col-md-1 control-label">只读脚本</label>
		<div class="col-md-4">
			<textarea name='readonlyScript' id='readonlyScript' rows='4' 
				class='form-control'>${vo.readonlyScript}</textarea>
		</div>
		<label class="col-md-2 control-label">禁用脚本</label>
		<div class="col-md-4">
			<textarea name='disabledScript' id='disabledScript' rows='4' 
				class='form-control'>${vo.disabledScript}</textarea>
		</div>
</div>	
<div class="form-group">
		<label class="col-md-1 control-label">隐藏脚本</label>
		<div class="col-md-4">
			<textarea name='hiddenScript' id='hiddenScript' rows='4' 
				class='form-control'>${vo.hiddenScript}</textarea>
		</div>
		<label class="col-md-2 control-label">描述</label>
		<div class="col-md-4">
			<textarea name='description' id='description' rows='4' 
				class='form-control'>${vo.description}</textarea>
		</div>
</div>	
<div class="form-group">
		<label class="col-md-1 control-label">加载前脚本</label>
		<div class="col-md-4">
			<textarea name='preLoadScript' id='preLoadScript' rows='4' 
				class='form-control'>${vo.preLoadScript}</textarea>
		</div>
		<label class="col-md-2 control-label">加载后脚本</label>
		<div class="col-md-4">
			<textarea name='afterLoadScript' id='afterLoadScript' rows='4' 
				class='form-control'>${vo.afterLoadScript}</textarea>
		</div>
</div>	
<div class="form-group">
		<label class="col-md-1 control-label">模板库</label>
        	 <div class='col-md-4'>
        	 <!-- 样式库  -->
        	 	 		<select  id="templateId" class="form-control"    tabindex="2" name="templateId">
							<option value="${vo.templateId}"></option>
						</select>
        	 </div>
        	 
        <label class="col-md-2 control-label">pdf打印行高</label>
		<div class="col-md-4">
			<input type='text' name='lineHeight' id='lineHeight' value="${vo.lineHeight}"	class='form-control' />
		</div>
</div>	
<div class="form-group">
		<label class="col-md-1 control-label">pdf打印最小条数</label>
        <div class="col-md-4">
			<input type='text' name='minLineCount' id='minLineCount' value="${vo.minLineCount}"	class='form-control' />
		</div>
        	 
        <label class="col-md-2 control-label">pdf打印最大条数</label>
		<div class="col-md-4">
			<input type='text' name='maxLineCount' id='maxLineCount' value="${vo.maxLineCount}"	class='form-control' />
		</div>
</div>
<div class="form-group">
	<label class="col-md-1 control-label">pdf打印模板页面</label>
		<div class="col-md-4">
			<input type='text' name='pdfTemplatePage' id='pdfTemplatePage' value="${vo.pdfTemplatePage}"	class='form-control' />
		</div>
	
	<label class="col-md-2 control-label">pdf打印行高类型</label>
		<div class="col-md-4">
		
			<select data-placeholder="输入组件名称..." id="lineHeightType" class="form-control" tabindex="2" name="lineHeightType">
				<c:choose>
				        	 		<c:when test="${vo.lineHeightType == '1' }">
				        	 			<option value="">请选择</option>
										<option value="1" selected="selected">固定行高</option>
										<option value="2">最小行高</option>
				        	 		</c:when>
				        	 		<c:when test="${vo.lineHeightType == '2' }">
				        	 			<option value="">请选择</option>
										<option value="1">固定行高</option>
										<option value="2" selected="selected">最小行高</option>
				        	 		</c:when>
				        	 		<c:otherwise>
										<option value="">请选择</option>
										<option value="1">固定行高</option>
										<option value="2">最小行高</option>
				        	 		</c:otherwise>
				        	 	</c:choose>
				
			</select>
		</div>
</div>