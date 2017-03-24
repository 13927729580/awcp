<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<div class="btn-group mb10">
	<div class="btn-group">
		<button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown">
			<i class="icon-plus-sign"></i>添加 <span class="caret"></span>
		</button>
		<ul class="dropdown-menu" role="menu">
			<c:forEach items="${_COMPOENT_TYPE_NAME }" var="cs">
				<li><a href="javascript:void(0)" onclick="editComponent('${cs.key}','${vo.id }',null);">${cs.value}</a></li>
			</c:forEach>
		</ul>
	</div>
		
	<div class="btn-group">
		<button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown">
			<i class="icon-plus-sign"></i>快捷新增 <span class="caret"></span>
		</button>
		<ul class="dropdown-menu" role="menu">
			<li><a href="javascript:void(0)" onclick="quickComWithLabel();">带文本组件</a></li>
			<li><a href="javascript:void(0)" onclick="quickComByDataSource();">以数据源新增</a></li>
		</ul>
	</div>
	<div class="btn-group">
		<button type="button" class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown">
			<i class="icon-plus-sign"></i>批量修改 <span class="caret"></span>
		</button>
		<ul class="dropdown-menu" role="menu">
			<li><a href="javascript:void(0)" onclick="batchModifiAllowNull(1);">允许为空</a></li>
			<li><a href="javascript:void(0)" onclick="batchModifiAllowNull(0);">不允许为空</a></li>
			<li><a href="javascript:void(0)" onclick="batchModifiStyle();">样式</a></li>
			<li><a href="javascript:void(0)" onclick="batchModifiAll(0);">批量修改</a></li>
			<li><a href="javascript:void(0)" onclick="modifiDataAlias();">批量修改数据源别名</a></li>
			<li><a href="javascript:void(0)" onclick="modifiComponentType();">批量修改组件类型</a></li>
			<li><a href="javascript:void(0)" onclick="modifiValidator();">批量修改组件校验</a></li>
			<li><a href="javascript:void(0)" onclick="modifiValidator();">批量修改组件打印样式</a></li>
			<li><a href="javascript:void(0)" onclick="modifiPrintHeight();">批量修改组件打印行高</a></li>
			<li><a href="javascript:void(0)" onclick="modifiSameType();">同类型批量修改</a></li>
		</ul>
	</div>
	
	<button class="btn btn-sm btn-info" id="deleteComponent">
		<i class="icon-remove"></i>删除
	</button>
	<button class="btn btn-sm btn-info" id="copyComponent">
		<i class="icon-remove"></i>复制
	</button>
	<button class="btn btn-sm btn-info" id="refreshComponentOrder">
		<i class="icon-remove"></i>重置序号
	</button>
	<button class="btn btn-sm btn-info" id="refreshContainerComponent">
		<i class="icon-remove"></i>重置包含组件
	</button>
	
</div>

<div id="collapseButton" class="in">
	<input type="hidden" name="currentPage" value="1">
	<div class="form-group">					
		<div class="col-md-3">
			<div class="input-group">
				<span class="input-group-addon">布局行条件</span>
				<input name="rows1" class="form-control" id="rowValue" type="text" value="">
			</div>
		</div>
		<div class="col-md-3">
			<div class="input-group">
				<span class="input-group-addon">布局列条件</span>
				<input name="columns1" class="form-control" id="colValue" type="text" value="">
			</div>
		</div>			
		<div class="col-md-3">
			组件类型：
			<select id="typeId">
				<option value="">-请选择-</option>
				<c:forEach items="${_COMPOENT_TYPE_NAME }" var="cs">
					<option value="${cs.key}">${cs.value}</option>
				</c:forEach>
			</select>
		</div>		
	</div>

	<div class="form-group">
		<div class="col-md-3">
			<div class="input-group">
				<span class="input-group-addon">组件或文本</span>
				<input name="cname" class="form-control" id="cname" type="text" value="">
			</div>
		</div>				 
		<div class="col-md-3">
			<div class="input-group">
				<span class="input-group-addon">数据源</span>
				<input name="dataCode" class="form-control" id="dataCode" type="text" value="">
			</div>
		</div>
		<div class="col-md-3 btn-group">
			<button class="btn btn-primary" type="button" onclick="loadComByCondition()">提交<tton>
		</div>
	</div>
	
</div>

<div class="componentTable" contenteditable="false">
	<table class="table table-bordered" id="componentTable" align="left">
		<thead>
			<tr>
				<th width="20"><input type="checkbox" id="checkAllComponent" /></th>
				<th>名称</th>
				<th width="100">类型</th>
				<th>数据源</th>
				<th>默认值</th>
				<th width="130">布局</th>
				<th>描述</th>
				<th width="50">序号</th>
			</tr>
		</thead>
		<tbody id="componentt">
		</tbody>
	</table>
</div>
