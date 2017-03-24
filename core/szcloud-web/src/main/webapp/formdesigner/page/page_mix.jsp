<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="dataform">
<form class="form-horizontal" id="groupForm"
	action="#" method="post">
	<legend>
		表单编辑
		<c:if test="${result!=null&&''!=result}">
			<span style="color: red">(${result})</span>
		</c:if>
	</legend>
	
	<div class="form-group">
      <label class="col-md-offset-1 col-md-1 control-label required">标题</label>
      <div class="col-md-9">
         <input name="title" class="form-control" id="title" type="text" placeholder="" value="">
      </div>
    </div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">上级任务</label>
		<div class="col-md-9">
			<select class="form-control">
				<option value="1">某项目1</option>
				<option value="2">某项目2</option>
				<option value="3">某项目3</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">类型</label>
		<div class="col-md-4">
			<select id="select-type" class="form-control">
				<option value=""></option>
				<option value="cusadvice">A:客户合理化建议</option>
				<option value="saleadvice">A2:售前合理化建议</option>
				<option selected="selected" value="demo">A3:外单位交办</option>
				<option value="cor">B1:公司内部会议</option>
				<option value="coradvice">B2:公司内部合理化建议</option>
				<option value="cordemo">B3：公司内部交办</option>
				<option value="dep">C：部门任务</option>
				<option value="per">D：个人任务</option>
				<option value="closed">Closed</option>
			</select>
		</div>
		<label class="col-md-1 control-label">审核人</label>
		<div class="col-md-4">
			<input type="text" class="form-control" id="select-member"/>
			<!-- <select id="select-member" class="form-control">
				<option value="productManager">P:产品经理</option>
				<option value="dev1">D:开发甲</option>
				<option value="dev2">D:开发乙</option>
				<option value="dev3">D:开发丙</option>
				<option value="dev4">D:开发丁</option>
			</select> -->
		</div>
	</div>
	<div class="form-group"><!-- 显示1列数据 -->
		<label class="col-md-offset-1 col-md-1 control-label">任务描述</label>
		<div class="col-md-9">
			<textarea name="content" class="form-control" id="content"
				rows="10"></textarea>
		</div>
	</div>
	<div class="form-group"><!-- 显示2列数据 -->
		<label class="col-md-offset-1 col-md-1 control-label">所属部门</label>
		<div class="col-md-4">
			<input name="keywords" disabled="" class="form-control" id="department"
				type="text" value="信息技术部">
		</div>
		<label class="col-md-1 control-label">所属组</label>
		<div class="col-md-4 ">
			<input name="keywords" disabled="" class="form-control" id="group"
				type="text" value="信息组">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">启动日期</label>
		<div class="col-md-4">
			<div class="input-group date form-date"
				data-link-field="dtp_input2" data-date-format="dd MM yyyy"
				data-date="" data-link-format="yyyy-mm-dd">
				<input class="form-control" type="text" size="16" readonly
					value=""> <span class="input-group-addon"><span
					class="icon-remove"></span></span> <span class="input-group-addon"><span
					class="icon-calendar"></span></span>
			</div>
		</div>
		<label class="col-md-1 control-label">填报周期</label>
		<div class="col-md-4">
			<select class="form-control">
				<option value="1">周</option>
				<option value="2">月</option>
				<option value="3">季度</option>
				<option value="4">半年</option>
				<option value="5">年</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">完成时间</label>
		<div class="col-md-4">
			<div class="input-group date form-time"
				data-link-field="dtp_input3" data-date-format="hh:ii"
				data-date="" data-link-format="hh:ii">
				<input class="form-control" type="text" size="16" readonly
					value=""> <span class="input-group-addon"><span
					class="icon-remove"></span></span> <span class="input-group-addon"><span
					class="icon-time"></span></span>
			</div>
		</div>
		<label class="col-md-1 control-label">紧急程度</label>
		<div class="col-md-4">
			<select class="form-control">
				<option value="1">普通</option>
				<option value="2">紧急</option>
				<option value="3">加急</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">选择日期</label>
		<div class="col-md-4">
			<input class="form-control form-date" type="text"
				placeholder="选择或者输入一个日期：yyyy-MM-dd">
		</div>
		<label class="col-md-1 control-label">选择时间</label>
		<div class="col-md-4">
			<input class="form-control form-time" type="text" readonly
				placeholder="选择或者输入一个时间：hh:mm">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">组合时间</label>
		<div class="col-md-4">
			<div class="input-group date form-datetime"
				data-link-field="dtp_input1"
				data-date-format="dd MM yyyy - HH:ii p"
				data-date="1979-09-16T05:25:07Z">
				<input class="form-control" type="text" size="16"
					value=""> <span class="input-group-addon"><span
					class="icon-remove"></span></span> <span class="input-group-addon"><span
					class="icon-th"></span></span>
			</div>
		</div>
		<label class="col-md-1 control-label">日期时间</label>
		<div class="col-md-4">
			<input class="form-control form-datetime" type="text" readonly
				placeholder="选择或者输入一个日期+时间：yyyy-MM-dd hh:mm">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">联动菜单</label>
		<div class="col-md-9">
			<div class="input-group">
                <select class="form-control">
                  <option value="1">省份</option>
                  <option value="2">北京</option>
                  <option value="3">上海</option>
                  <option value="3">广州</option>
                </select>
                <span class="input-group-addon fix-border fix-padding"></span>
                <select class="form-control">
                  <option value="1">市/县</option>
                  <option value="1">...</option>
                </select>
                <span class="input-group-addon fix-border fix-padding"></span>
                <input type="text" class="form-control" placeholder="社区/乡/镇">
              </div>
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">创建人</label>
		<div class="col-md-4">
			<input name="keywords" class="form-control" id="builder2"
				type="text" value="" title="请填写创建人，4个字符至8个字符">
		</div>
		<div class="col-md-4">
			<label class="checkbox-inline"> <input name="optionsCheck"
				type="checkbox" checked="" value="option1"> 123
			</label> <label class="checkbox-inline"> <input name="optionsCheck"
				type="checkbox" value="option2"> 234
			</label> <label class="checkbox-inline"> <input name="optionsCheck"
				type="checkbox" value="option2"> 345
			</label>
		</div>
	</div>

	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">关键字</label>
		<div class="col-md-9">
			<input name="keywords" class="form-control" id="keywords"
				type="text" value="">
		</div>
	</div>
	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">备注说明</label>
		<div class="col-md-9">
			<textarea name="content" class="form-control" id="content"
				rows="10"></textarea>
		</div>
	</div>

	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">附件路径</label>
		<div class="col-md-9">
			<div class="input-group">
				<span class="input-group-addon">http://www.baidu.com/</span> <input
					name="alias" class="form-control" id="alias" type="text"
					value=""> <span class="input-group-addon">.rar</span>
			</div>
		</div>
	</div>


	<div class="form-group">
		<label class="col-md-offset-1 col-md-1 control-label">保存为</label>
		<div class="col-md-9">
			<label class="radio-inline"> <input name="optionsRadios"
				type="radio" checked="" value="option1"> 草稿
			</label> <label class="radio-inline"> <input name="optionsRadios"
				type="radio" value="option2"> 私人
			</label> <label class="radio-inline"> <input name="optionsRadios"
				type="radio" value="option2"> 公开
			</label>
		</div>
	</div>

	<div class="form-group"><!-- 表单提交按钮区域 -->
        <div class="col-md-offset-2 col-md-8">
          	<button type="submit" class="btn btn-success" id="saveBtn"><i class="icon-save"></i>保存</button>
			<a href="#" class="btn" id="undoBtn"><i class="icon-undo"></i>取消</a>
        </div>
    </div>
</form>
</div>