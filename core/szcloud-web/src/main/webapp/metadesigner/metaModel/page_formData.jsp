<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="formData">
	<div class="panel">
		<div class="panel-heading">
			<div class="btn-group">
				<div class="btn-group">
									<legend>
										元数据模型编辑
									</legend>
								</div>
			</div>
		</div>
		<div class="panel-body">
								<form method="get" class="form-horizontal" id="inputForm" action="" >
								<div class="form-group">
							      <label class="col-md-1 control-label required">模型名称(中)</label>
							      <div class="col-md-3">
							      	 <input type="hidden" id="id" name="id" value="">
							         <input name="modelName" id="modelName" class="form-control" type="text" placeholder="" value="">
							      </div>
							      <label class="col-md-1 control-label required">模型名称(英)</label>
							      <div class="col-md-3">
							         <input name="modelCode" id="modelCode" class="form-control"type="text" placeholder="" value="">
							      </div>
							      <label class="col-md-1 control-label required">模型分类</label>
							      <div class="col-md-3">
										<select name="modelClassId" id="modelClassId">
											<c:forEach items="${class}" var="k">
												<option value="${k.id }">${k.classCode }</option>
											</c:forEach>						
										</select>		         
							      </div>
							    </div>
							    <div class="form-group">
							      <label class="col-md-1 control-label">模型描述</label>
							      <div class="col-md-3">
							         <input name="modelDesc" id="modelDesc" class="form-control" type="text" placeholder="" value="">
							      </div>
							      <label class="col-md-1 control-label required">列表名称</label>
							      <div class="col-md-3">
							         <input name="tableName" id="tableName" class="form-control" type="text" placeholder="" value="">
							      </div>
							      <label class="col-md-1 control-label required">项目名称</label>
							      <div class="col-md-3">
							         <input name="projectName" id="projectName" class="form-control" type="text" placeholder="" value="">
							      </div>
							    </div>
							    <div class="from-group">
							    	<label class="col-md-1 control-label">模型分类</label>
							    	<div class="col-md-3">
							    		<select name="itemType" id="itemName">
							    			<option value="1">领域模型</option>
							    			<option value="2">系统模型</option>
							    		</select>
							    	</div>
							    </div>
								</form>
							</div>
		<div class="panel-footer">
			<div class="btn-group">
				<button type="button" onclick="createTable()" class="btn btn-sm btn-success">生成表格</button>
			</div>
		</div>
	</div>
	<div class="table" style="display:none">
		<div class="btn-group" style="margin-bottom: 20px">
				<button type="button" class="btn btn-sm btn-primary">修改</button>
				<button type="button" class="btn btn-sm btn-success">新增</button>
				<button type="button" class="btn btn-sm btn-info">删除</button>
			</div>
			<form method="post" id="listForm" action="#">
				<table class="table datatable1 table-bordered">
					<thead>
						<tr>
							<th class="hidden"></th>
							<th>菜单名</th>
							<th>地址</th>
							<th>隶属于</th>
							<th>菜单类型</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value=""></td>
							<td>test</td>
							<td class="text-ellipsis">test</td>
							<td>12</td>
							<td>21</td>
						</tr>
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value=""></td>
							<td>test</td>
							<td class="text-ellipsis">test</td>
							<td>12</td>
							<td>21</td>
						</tr>
						<tr>
							<td class="hidden formData"><input id="boxs" type="hidden"
								value=""></td>
							<td>test</td>
							<td class="text-ellipsis">test</td>
							<td>12</td>
							<td>21</td>
						</tr>
					</tbody>
					<tfoot>
					</tfoot>
				</table>
			</form>
			<ul class="pager pager-pills" style="margin: 0;">
				<li class="previous disabled"><a href="javascript:;">«</a></li>
				<li class="next"><a href="javascript:;">»</a></li>
			</ul>
	</div>
</div>
