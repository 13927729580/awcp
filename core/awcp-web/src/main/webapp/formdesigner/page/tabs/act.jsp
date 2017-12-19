<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="btn-group mb10">
	<button class="btn btn-sm btn-info" id="actStore">
		<i class="icon-search"></i>动作库
	</button>
	<div class="btn-group">
		<button type="button" class="btn btn-sm btn-success dropdown-toggle"
			data-toggle="dropdown">
			<i class="icon-plus-sign"></i>新增 <span class="caret"></span>
		</button>
		<ul class="dropdown-menu">
			<li><a href="javascript:void(0)" onclick="addAct('2000');">普通</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2001');">保存</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2002');">返回</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2003');">删除</a></li> 
			<li><a href="javascript:void(0)" onclick="addAct('2009');">新增</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2010');">更新</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2014');">Pdf打印</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2016');">Excel导出</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2004');">导入</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2006');">流程回撤</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2008');">保存（带流程）</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2018');">流程转发</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2019');">流程传阅</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2017');">流程办结</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2023');">流程退回</a></li>
			<li><a href="javascript:void(0)" onclick="addAct('2024');">加签</a></li>				
		</ul>
	</div>
	<button class="btn btn-sm btn-info" id="batchModifyActType">
		<i class="icon-plus-sign"></i>修改动作类型
	</button>
	<button class="btn btn-sm btn-info" id="deleteAct">
		<i class="icon-remove"></i>删除
	</button>
</div>
<div class="actTable" contenteditable="false">
	<table class="table table-bordered" id="acttable" align="left">
		<thead>
			<tr>
				<th><input type="checkbox" id="checkAllAct" /></th>
				<th>名称</th>
				<th>类型</th>
			</tr>
		</thead>
		<tbody id="actdatabody"></tbody>
	</table>
</div>