<#macro pageNavigation dataAlias>
	
	<#----
	<#local currentPage = bean.getPaginator().getPage() >
	<#local pageCount = bean.getPaginator().getTotalPages() >
	<#local pageSize = bean.getPaginator().getLimit() >
	<#local totalCount = bean.getPaginator().getTotalCount() >
	---->	
		<div class="m_p_top"><ul class="pager">
		<#noparse>
		<#if </#noparse> ${dataAlias}_list_paginator.getPage() <#noparse> gt 1> </#noparse>
			<li class="previous"><a href='javascript:page(1,<#noparse>${</#noparse>${dataAlias}_list_paginator.getLimit()})'>首页</a></li>
			<li class="previous"><a href='javascript:page(<#noparse>${</#noparse>${dataAlias}_list_paginator.getPage() - 1},<#noparse>${</#noparse>${dataAlias}_list_paginator.getLimit()})'>«上一页</a></li>
		<#noparse><#else></#noparse>
			<li class="previous disabled"><a href='javascript:return false;'>首页</a></li>
			<li class="previous disabled"><a href='javascript:return false;'>«上一页</a></li>
		<#noparse></#if></#noparse>
		
		<#noparse>
		<#if </#noparse> ${dataAlias}_list_paginator.getPage() lt ${dataAlias}_list_paginator.getTotalPages()>
			<li class="next"><a href='javascript:page(<#noparse>${</#noparse>${dataAlias}_list_paginator.getPage() + 1},<#noparse>${</#noparse>${dataAlias}_list_paginator.getLimit()})'>下一页 »</a></li>
			<li class="next"><a href='javascript:page(<#noparse>${</#noparse>${dataAlias}_list_paginator.getTotalPages()},<#noparse>${</#noparse>${dataAlias}_list_paginator.getLimit()})'>末页</a></li>
		<#noparse><#else></#noparse>
			<li class="next disabled"><a href='javascript:return false;'>下一页 »</a></li>
			<li class="next disabled"><a href='javascript:return false;'>末页</a></li>
		<#noparse></#if></#noparse>
		
		</ul></div><div class="m_p_bottom">
		<span><select id='pageSizeSelect' class='form-control' data-pageSize='<#noparse>${</#noparse>${dataAlias}_list_paginator.getLimit()}'>
				<#--新增一个option，数据为用户配置的默认每页显示记录数pageSize-->
				<option value='<#noparse>${(vo.getPageSize())!50}</#noparse>'><#noparse>${(vo.getPageSize())!50}</#noparse></option>
				<option value='10'>10</option><option value='15'>15</option><option value='20'>20</option><option value='25'>25</option><option value='50'>50</option>
			</select></span>
		<span>当前第<label class="text-danger"><#noparse>${</#noparse>${dataAlias}_list_paginator.getPage()}</label>页</span>
		<span>共<label class="text-danger" id="totalpage"><#noparse>${</#noparse>${dataAlias}_list_paginator.getTotalPages()}</label>页</span>
		<span>共有<label class="text-danger"><#noparse>${</#noparse>${dataAlias}_list_paginator.getTotalCount()}</label>条记录</span>
		<#--这里新增input框中的value为当前页-->
		<span><label>第</label><input class="form-control" type="text" value="<#noparse>${</#noparse>${dataAlias}_list_paginator.getPage()}"><label>页</label>
		<a href="javascript:;" onclick="page(<#noparse>${</#noparse>${dataAlias}_list_paginator.getLimit()})" class="btn btn-sm btn-primary">跳转</a></span></div>
</#macro>