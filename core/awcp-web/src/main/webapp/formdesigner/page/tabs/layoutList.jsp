<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<div class="btn-group mb10">
	<button type="button" class="btn btn-sm btn-success" id="addLayoutBtn"
		onclick="addLayout(this);">
		<i class="icon-plus-sign"></i>新增
	</button>
	<button class="btn btn-sm btn-info" id="deleteLayout">
		<i class="icon-remove"></i>删除
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="addNewLayoutBtn" onclick="addChildLayout(this);">
		<i class="icon-plus-sign"></i>新增子布局
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="addQuickLayoutBtn" onclick="addQuickLayout(this);">
		<i class="icon-plus-sign"></i>快捷新增
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="merageLayout">
		<i class="icon-plus-sign"></i>合并布局
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="copyLayout">
		<i class="icon-plus-sign"></i>复制布局
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="refreshLayoutOrder">
		<i class="icon-plus-sign"></i>重置序号
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="batchModifyProportionBtn" onclick="batchModifyProportion(this);">
		<i class="icon-plus-sign"></i>批量修改布局占比
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="batchModifyHeightBtn" onclick="batchModifyHeight(this);">
		<i class="icon-plus-sign"></i>批量修改行高列宽
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="batchModifyAlignBtn" onclick="batchModifyAlign(this);">
		<i class="icon-plus-sign"></i>批量修改对齐方式
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="batchModifyBorderBtn" onclick="batchModifyBorder(this);">
		<i class="icon-plus-sign"></i>批量修改边框
	</button>
	<button type="button" class="btn btn-sm btn-success"
		id="batchModifyBtn" onclick="batchModify(this);">
		<i class="icon-plus-sign"></i>同类型批量修改
	</button>
	<button type="button" class="btn btn-sm btn-success" id="batchClearHeight" onclick="batchHeight(this);">
		<i class="icon-plus-sign"></i>批量清除宽高
	</button>
</div>
<div class="row" id="searchform">
				<div id="collapseButton" class="in">
						<input type="hidden" name="currentPage" value="1" />
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">行条件</span>
								<input name="rows" class="form-control" id="rows" type="text" value="${rows }"/>
							</div>
						</div>
						<div class="col-md-3">
							<div class="input-group">
								<span class="input-group-addon">列条件</span>
								<input name="columns" class="form-control" id="columns" type="text" value="${columns }"/>
							</div>
						</div>
						<div class="col-md-3 btn-group">
							<button class="btn btn-primary" type="button" onclick="searchLayout()">提交</button>
						</div>
				</div>
			</div>
<div class="componentTable" contenteditable="false">
	<table class="table table-bordered" id="layoutTable" align="left">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAllLayout" /></th>
				<th>名称</th>
				<th>类型</th>
				<th>占比</th>
				<th>排序</th>
			</tr>
		</thead>
		<tbody id="layoutt">
		</tbody>
	</table>
</div>
