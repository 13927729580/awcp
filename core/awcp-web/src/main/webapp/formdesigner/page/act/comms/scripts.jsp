<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="page-header tab-pane " style="dispaly:none" id="hiddenConfig" >
	<div class="form-group">
		<label class="col-md-1 control-label">描述</label>
		<div class="col-md-5">
			<textarea name='description' id='description' rows='4'
				class='form-control'>${act.description }</textarea>
		</div>
		<label class="col-md-1 control-label">隐藏脚本</label>
		<div class="col-md-5">
			<textarea name='hiddenScript' id='hiddenScript' rows='4'
				class='form-control'>${act.hiddenScript }</textarea>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-1 control-label">服务端脚本</label>
		<div class="col-md-5">
			<textarea name='serverScript' id='serverScript' rows='4'
				class='form-control'>${act.serverScript }</textarea>
		</div>
		<label class="col-md-1 control-label">客户端脚本</label>
		<label class="checkbox-inline"> <input
					id="ajaxExcute"	name="ajaxExcute" type="checkbox" value="1"/>ajax执行
		</label>  
		<label class="checkbox-inline"> <input
					id="ajaxView"	name="ajaxView" type="checkbox" value="1"/>ajax弹框
		</label>  
		<div class="col-md-5">
			<textarea name='clientScript' id='clientScript' rows='4'
				class='form-control'>${act.clientScript }</textarea>
		</div>
	</div>
	<c:if test="${pageType=='1003'}">
		<div class="form-group">
			<label class="col-md-1 control-label">是否校验选择项</label>
			<div class="col-md-5">
				<c:choose>
					<c:when test="${act.chooseValidate == 'true' }">
						<label class="radio-inline"> <input name="chooseValidate"
							type="radio" checked="checked" value="1">是
						</label>
						<label class="radio-inline"> <input name="chooseValidate"
							type="radio" value="0">否
						</label>
					</c:when>
					<c:otherwise>
						<label class="radio-inline"> <input name="chooseValidate"
							type="radio" value="1">是
						</label>
						<label class="radio-inline"> <input name="chooseValidate"
							type="radio" checked="checked" value="0">否
						</label>
					</c:otherwise>
				</c:choose>
			</div>
			
		</div>
		<div class="form-group">
			<label class="col-md-1 control-label">选择项校验脚本</label>
			<div class="col-md-5">
				<div class="option-select">
							<div class="btn-group">
					              <button type="button" class="btn dropdown-toggle" data-toggle="dropdown">
					              		常用脚本 <span class="caret"></span> 
					              </button>
					              <ul class="dropdown-menu">
					              </ul>
				            </div>
				            <button type="button" class="btn btn-success hidden saveBtn"><i class="icon-save"></i></button>
				</div>
				<textarea rows="10" id="option" name="chooseScript" class="option form-control">${act.chooseScript}</textarea>
			</div>
			
		</div>
	</c:if>

</div>
