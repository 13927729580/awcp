/**
 * 
 */

	var commStore = {
			print : {
				
				table : [ "name"],
				unique : 'pageId',
				href : "name",
				
			}	
	}

	function empty(v){ 
			switch (typeof v){ 
				case 'undefined' : return true; 
				case 'string' : if($.trim(v).length == 0) return true; break; 
				case 'boolean' : if(!v) return true; break; 
				case 'number' : if(0 === v) return true; break; 
				case 'object' : 
				if(null === v) return true; 
				if(undefined !== v.length && v.length==0) return true; 
				for(var k in v){return false;} return true; 
				break; 
			} 
			return false; 
	}
		
		


		function addPrintRow(objectType, comObject){
			var object = commStore[objectType];
			//标示符
			var unique=object.unique;
			//table 列
			var table=object.table;
			//是否是超链接字段
			var href=object.href;
			item = eval("(" + comObject.content + ")");
			var str="<tr id='"+item[unique]+"'><td><input type='checkbox' name='"+objectType+"' value='"+item[unique]+"'/></td>";
			
			$.each(table,function(i,t){
				//判断是否是href字段，若是加事件，不是else
				if(href==t){
						str+="<td><a href='javascript:void(0);' onclick='editPrint(\""+item[unique]+"\")'>"+item[t]+"</a></td>";
				}else{
					str+="<td>"+item[t]+"</td>";
				} 
			});
			str+="</tr>";
			$("#"+objectType+"t").append(str);
		}
		
		function loadPrint(name){
			var url = basePath + "print/getPrintBySystemId.do";
			var objectType="print";
			$.ajax({
				type:"GET",
				url:url,
				async:false,
				data:{name:name,sortString:"NAME DESC"},
				success:function(data){
					//clear table
					$("#"+objectType+"t").empty();
					$.each(data,function(idx,item){						
						addPrintRow(objectType, item);
					});
				},
			    error: function (XMLHttpRequest, textStatus, errorThrown) { 
	                  alert(errorThrown); 
			    }
			});
		}

		
	function editPrint(pageId){
		
		var urlStr="formdesigner/page/print/printEdit.jsp";
		
		
			
		var postData={};
		postData.pageId=pageId;
		top.dialog({
			id: 'add-dialog' + Math.ceil(Math.random()*10000),
			title: '载入中...',
			url: urlStr,
			data:postData,
			width : 1200,
			onclose: function () {
				if (this.returnValue) {
					var ret= this.returnValue;
					alert("保存成功");
					loadPrint();
				}
			}
			
		}).showModal();
		return false;
	}
	

	
	function removePrint(objectType){
		var _selects=new Array();
		//var loadUrl = "layout/getLayoutListByPageId.do"
		$(":checkbox[name="+objectType+"]:checked").each(function(){
			var value=$(this).val();
			_selects.push(value);
		});
		$.ajax({
			url:"print/deleteByAjax.do",
			type:"POST",
			async:false,
			data:{_selects:_selects.join(",")},
			success:function(ret){
				if("1"==ret){
					$(":checkbox[name='checkAllPrint']").prop("checked",false);
					loadPrint();
					alert("删除成功！");
				}else{
					alert("删除失败！");
				}
				
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) { 
	              alert(errorThrown); 
		    }
		});
		return false;
	}
	
	
	
	
	
	$("#addPrint").click(function(){
		editPrint("");
	});
	$("#deletePrint").click(function(){
		var count = $(":checkbox[name='print']:checked").size();
		if(count > 0){
			
			removePrint('print');
			return false;
		}else{
			alert("请选择布局操作");
			return false;
		}
	});
	
	
	
	$("#checkAllPrint").click(function(){
		if($(this).prop("checked")){
			$(":checkbox[name='print']").prop("checked",true);
		}else{
			$(":checkbox[name='print']").prop("checked",false);
		}
		
	});
	
	$("#searchPrint").click(function(){
		var name = $("#name").val();
		loadPrint(name);
	});