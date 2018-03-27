<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="form-horizontal">
	<div class="form-group">
		<label class="col-md-2 col-sm-2 control-label required">类型</label>
		<div class="col-md-10 col-sm-10">
			<select name="textType" class="form-control" id="textType" >
				<option selected="selected" value="text">文本</option>
				<option value="number">数字</option>
				<option value="tel">电话</option>
				<option value="money">金钱</option>
				<option value="email">邮箱</option>
				<option value="password">密码</option>
			</select>
		</div>
	</div>
</div>
<%@ include file="../common/basicAttr.jsp" %>