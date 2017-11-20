//n:跳转到第几页
//s:每页大小
function page(n,s){
	$("#currentPage").val(n);
	$("#pageSize").val(s);
	$("#createForm").submit();
	return false;
}