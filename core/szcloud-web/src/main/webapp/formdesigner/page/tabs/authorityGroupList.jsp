<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="btn-group mb10">
	
	<input type="hidden" value="${vo.id}" id="dynamicPageId"/>
	<button class="btn btn-sm btn-info" id="addAuthorityGroup">
		<i class="icon-plus-sign"></i>添加
	</button>
	<button class="btn btn-sm btn-info" id="deleteAuthority">
		<i class="icon-remove"></i>删除
	</button>
	<button class="btn btn-sm btn-info" id="addAuthorityValue">
		<i class="icon-plus-sign"></i>权限配置
	</button>
	<!--
	<button class="btn btn-sm btn-info" id="updateAuthority">
		<i class="icon-plus-sign"></i>编辑
	</button>-->
	
</div>


<div class="powerTable" contenteditable="false">
	<table class="table table-bordered" id="powerTable" align="left">
		<thead>
			<tr>
				<th width="20"><input type="checkbox" id="checkAllAuthority" /></th>
				<th>名称</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>最后更新时间</th>
				<th>描述</th>
				<th width="50">序号</th>
			</tr>
		</thead>
		<tbody id="powerTable_body">
		</tbody>
	</table>
</div>
