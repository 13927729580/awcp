<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8">
		<link href="<%=basePath %>resources/plugins/zui/assets/chosen/css/chosen.min.css" rel="stylesheet">
		<link href="<%=basePath %>resources/plugins/zui/assets/datetimepicker/css/datetimepicker.min.css" rel="stylesheet">
		<link href="<%=basePath %>base/resources/artDialog/css/ui-dialog.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>base/resources/zui/dist/css/zui.css">
		<link title="default" rel="stylesheet" type="text/css" href="<%=basePath %>base/resources/content/styles/szCloud_common.css" />
		<link title="default" rel="stylesheet" type="text/css" href="<%=basePath %>base/resources/content/styles/style.css" />
  		<script src="<%=basePath %>resources/JqEdition/jquery-1.10.2.js" language="javascript" type="text/javascript"></script>
		<script src="<%=basePath %>base/resources/zui/dist/js/zui.js" language="javascript" type="text/javascript"></script>
		<script src="<%=basePath %>base/resources/artDialog/dist/dialog.js"></script>
		<script src="<%=basePath %>base/resources/artDialog/lib/require.js"></script>
		<script src="<%=basePath %>base/resources/artDialog/lib/sea.js"></script>
		<title></title>
		<script type="text/javascript">
			$(document).ready(function(){
				$("#search").click(function(){
					var id=$("#ssi").val();
					location.href="../metaModelItems/release.do?idss="+id;
				});
				
				$("#modelIds").hide();
				$("#metaModels").hide();
				$("#itemType").change(function(){
					var v=$("#itemType").val();
					if(v=='1' || v=='2'){
						$("#modelIds").show();
					}
					else{
						$("#modelIds").hide();
					}
				});
				$("#itemTypes").change(function(){
					var v=$("#itemTypes").val();
					if(v=='1' || v=='2'){
						$("#metaModels").show();
					}
					else{
						$("#metaModels").hide();
					}
				});
				$("#deleteOperate").click(function(){
					var count = $("input:checkbox[name='choose']:checked").length;
					if(count==1){
						if(confirm("您确认删除吗")){
							var id=$("input:checkbox[name='choose']:checked")[0].value;
							location.href="../metaModelItems/remove.do?classId="+id;
						}
					}else if(count>1){
						alert("只能选择一条数据，请重新选择");
					}else{
						alert("请选择");
					}
					
				});
				
				$("#addUpdate").click(function(){
					var modelId=$("#modelIdj").val();
					location.href="../metaModelItems/toggle.do?modelId="+modelId;
				});
				
				$("#updateOperate").click(function(){
					
					var count = $("input:checkbox[name='choose']:checked").length;
					if(count==1){
						var id=$("input:checkbox[name='choose']:checked")[0].value;
						
						var ajaxRequest=$.get("../metaModelItems/gets.do?id="+id+"&"+Math.random(),function(date){
							
							var jsonobj=eval('('+date+')');
							$("#ff").attr("value",jsonobj.id);
							$("#itemName").attr("value",jsonobj.itemName);
							$("#itemCode").attr("value",jsonobj.itemCode);
							
							 $("#itemTypes option").each(function(){
								if($(this).text()==jsonobj.itemType){
									$(this).attr("selected","selected");
								} 
							 });
							 if(jsonobj.itemType=="多对一" || jsonobj.itemType=="一对一"){
								//获取relaiton
								$.ajax({
									type:"get",
									url:"../metaModelItems/queryRelation.do",
									data:{"itemId":jsonobj.id},
									datatype:"json",
									async:false,
									success:function(date){
										var obj=eval('('+date+')');
										$("#metaModels").show();
										$("#modelIdss option").each(function(){
											if($(this).text()==obj.tableName){
												$(this).attr("selected","selected");
											}
										});
									}
								});
								
							 }
							
							 if(jsonobj.itemLength!=null){
								 $("#itemLength").attr("value",jsonobj.itemLength);
							 }
							
							if(jsonobj.useNull==1){
								$("#useNull1").attr("checked","checked");
							}else{
								$("#useNull2").attr("checked","checked");
							}
							if(jsonobj.usePrimaryKey==1){
								$("#usePrimaryKey1").attr("checked","checked");
							}else{
								$("#usePrimaryKey2").attr("checked","checked");
							}
							if(jsonobj.useIndex==1){
								$("#useIndex1").attr("checked","checked");
							}else{
								$("#useIndex1").attr("checked","checked");
							}
							
							
							if(jsonobj.defaultValue!=null){
								$("#defaultValue").attr("value",jsonobj.defaultValue);
							}
							
							if(jsonobj.remark!=null){
								$("#remark").attr("value",jsonobj.remark);
							}
							dialog.clear();
						});
						$("#updateOperate").attr("data-target","#updateModal");
						$("#updateOperate").attr("data-toggle","modal");
						
					}else if(count>1){
						alert("只能选择一条数据，请重新选择");
						return;
					}else{
						alert("请选择");
						return;
					}
					
				});
			});
		</script>
	</head>
	<body class="C_formBody">
		<input type="hidden" id="ssi" value="${ids }">
		<div style="margin-top: 10px;margin-left:10px ;width: 100%;float: left;">
			    <div class="C_btnGroup">
					<div class="C_btns">
					    <button class="btn btn-success" id="addUpdate"><i class="icon-plus-sign"></i>添加属性</button> <!-- data-toggle="modal" data-target="#addModal" -->
					    <button class="btn btn-info" id="deleteOperate"><i class="icon-trash"></i>删除属性</button> 
					    <button class="btn btn-warning" id="updateOperate"><i class="icon-edit"></i>修改属性</button> 
	                    <button id="search" class="btn" type="submit" data-toggle="dropdown">
	                    	提交
	                    </button>  
                   </div>
                   
                   <div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalTitle" aria-hidden="true">
					      <div class="modal-dialog" style="width: 30%;">
					        <div class="modal-content">
					          <div class="modal-body">
					              <form class="form-horizontal" role="form" action="../metaModelItems/save.do" method="post">
							          <div class="form-group">
							          	<input type="text" name="a" value="1.021">
							          	<input type="hidden" name="modelId" id="modelIdj" value="${ids }">     	
							            <label class="col-md-3 control-label required">字段名称(中)</label>
							            <div class="col-md-9 has-error">
							               <input name="itemName" class="form-control" id="title" type="text" placeholder="" value="">		              
							            </div>
							          </div>
							          <div class="form-group"> 
							            <label class="col-md-3 control-label">字段名称(英)</label>
							            <div class="col-md-9">
							                <input name="itemCode" class="form-control" id="title" type="text" placeholder="" value="">
										</div> 
									  </div>
									  <div class="form-group">       	
							            <label class="col-md-3 control-label">字段类型</label>
							            <div class="col-md-9">
							                <select class="form-control" name="itemType" id="itemType">
							                 	<option value="0">--请选择--</option>
												<option value="int">int</option>
												<option value="bigInt">bigInt</option>
												<option value="varchar">varchar</option>
												<option value="bool">bool</option>
												<option value="boolean">boolean</option>
												<option value="decimal">decimal</option>
												<option value="float">float</option>
												<option value="double">double</option>
												<option value="date">date</option>
												<option value="1">一对一</option>
												<option value="2">多对一</option>
							                </select> 
							               
									</div>
							          </div>
							         <div class="form-group" id="modelIdss"> 
							            <label class="col-md-3 control-label">表关系</label>
							            <div class="col-md-9">
							               	<select  class="form-control" name="modelIdss">
							               		<option value="0">-- 请选择  --</option>
							               		<c:forEach items="${metaModel }" var="l">
							               			<option value="${l.id }">${l.tableName }</option>
							               		</c:forEach>
							               	</select>
							            </div> 
									  </div>
							          <div class="form-group"> 
							            <label class="col-md-3 control-label">字段长度</label>
							            <div class="col-md-9">
							                <input name="itemLength" class="form-control" id="title" type="text" placeholder="" value="">
							            </div> 
									  </div>
							          <div class="form-group"> 
							            <label class="col-md-3 control-label">是否有主键</label>
							            <div class="col-md-9">
							                <input name="usePrimaryKey" type="radio" value="1" style="margin-top: -2px;">&nbsp;是
							                <input name="usePrimaryKey" type="radio" value="0" checked="checked" style="margin-left: 20px;margin-top: -2px;">&nbsp;否
							            </div> 
									  </div>
									  <div class="form-group">       	
							            <label class="col-md-3 control-label">是否有索引</label>
							            <div class="col-md-9">
							                 <input name="useIndex" type="radio" value="1" style="margin-top: -2px;">&nbsp;是
							                 <input name="useIndex" type="radio" value="0" checked="checked" style="margin-left: 20px;margin-top: -2px;">&nbsp;否
							            </div>
							          </div>
									  <div class="form-group">  
							            <label class="col-md-3 control-label">是否为空</label>
							            <div class="col-md-9">
							                 <input name="useNull" type="radio" value="1" style="margin-top: -2px;">&nbsp;是
							                 <input name="useNull" type="radio" value="0" checked="checked" style="margin-left: 20px;margin-top: -2px;">&nbsp;否
							            </div> 
									  </div>
									  <div class="form-group">       	
							            <label class="col-md-3 control-label">默认值</label>
							            <div class="col-md-9">
							                <input name="defaultValue" class="form-control" id="title" type="text" placeholder="" value="">
							            </div>
							          </div>
									  <div class="form-group">  
							            <label class="col-md-3 control-label">备注</label>
							            <div class="col-md-9">
							                <input name="remark" class="form-control" id="title" type="text" placeholder="" value="">
							            </div> 
									  </div>
									  <div class="modal-footer">
							            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							            <button type="submit" class="btn btn-primary">保存</button>
							          </div>
								  </form>
					          </div>
					          
					        </div><!-- /.modal-content -->
					      </div><!-- /.modal-dialog -->
					</div><!-- /.modal -->

				    <div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-labelledby="myModalTitle" aria-hidden="true">
					      <div class="modal-dialog" style="width: 30%;">
					        <div class="modal-content">
					          <div class="modal-body">
					              <form class="form-horizontal" action="../metaModelItems/update.do" role="form" method="post">
							          <div class="form-group">       	
							            <label class="col-md-3 control-label required">字段名称(中)</label>
							            <div class="col-md-9 has-error">
							            	<input type="hidden" value="Math.random()" name="a">
							            	<input type="hidden" id="ff" name="id">
							            	<input type="hidden" name="modelId" value="${ids }">
							               <input name="itemName" id="itemName" class="form-control" type="text" placeholder="" value="">		              
							            </div>
							          </div>
							          <div class="form-group"> 
							            <label class="col-md-3 control-label">字段名称(英)</label>
							            <div class="col-md-9">
							                <input name="itemCode" id="itemCode" class="form-control" type="text" placeholder="" value="">
										</div> 
									  </div>
									  <div class="form-group">       	
							            <label class="col-md-3 control-label">字段类型</label>
							            <div class="col-md-9">
							                <select class="form-control" name="itemType" id="itemTypes">
							                  	<option value="int">int</option>
												<option value="bigInt">bigInt</option>
												<option value="varchar">varchar</option>
												<option value="bool">bool</option>
												<option value="boolean">boolean</option>
												<option value="decimal">decimal</option>
												<option value="float">float</option>
												<option value="double">double</option>
												<option value="date">date</option>
												<option value="1">一对一</option>
												<option value="2">多对一</option>
							                </select>
							            </div>
							          </div>
							          <div class="form-group" id="metaModels"> 
							            <label class="col-md-3 control-label">表关系</label>
							            <div class="col-md-9">
							               	<select  class="form-control" id="modelIdss" name="modelIdsss">
							               		<option value="0">-- 请选择  --</option>
							               		<c:forEach items="${metaModel }" var="l">
							               			<option value="${l.id }">${l.tableName }</option>
							               		</c:forEach>
							               	</select>
							            </div> 
									  </div>
							          <div class="form-group"> 
							            <label class="col-md-3 control-label">字段长度</label>
							            <div class="col-md-9">
							                <input name="itemLength" id="itemLength" class="form-control" type="text" placeholder="" value="">
							            </div> 
									  </div>
							          <div class="form-group"> 
							            <label class="col-md-3 control-label">是否有主键</label>
							            <div class="col-md-9">
							                <input name="usePrimaryKey" id="usePrimaryKey1" type="radio" value="1" style="margin-top: -2px;">&nbsp;是
							                <input name="usePrimaryKey"	id="usePrimaryKey2" type="radio" value="0" style="margin-left: 20px;margin-top: -2px;">&nbsp;否
							            </div> 
									  </div>
									  <div class="form-group">       	
							            <label class="col-md-3 control-label">是否有索引</label>
							            <div class="col-md-9">
							                 <input name="useIndex" id="useIndex1" type="radio" value="1" style="margin-top: -2px;">&nbsp;是
							                 <input name="useIndex" id="useIndex2" type="radio" value="0" style="margin-left: 20px;margin-top: -2px;">&nbsp;否
							            </div>
							          </div>
									  <div class="form-group">  
							            <label class="col-md-3 control-label">是否为空</label>
							            <div class="col-md-9">
							                 <input name="useNull" id="useNull1" type="radio" value="1" style="margin-top: -2px;">&nbsp;是
							                 <input name="useNull" id="useNull2" type="radio" value="0" style="margin-left: 20px;margin-top: -2px;">&nbsp;否
							            </div> 
									  </div>
									  <div class="form-group">       	
							            <label class="col-md-3 control-label">默认值</label>
							            <div class="col-md-9">
							                <input name="defaultValue" id="defaultValue" class="form-control" type="text" placeholder="" value="">
							            </div>
							          </div>
									  <div class="form-group">  
							            <label class="col-md-3 control-label">备注</label>
							            <div class="col-md-9">
							                <input name="remark" id="remark" class="form-control" type="text" placeholder="" value="">
							            </div> 
									  </div>
									  <div class="modal-footer">
							            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							            <button type="submit" class="btn btn-primary">保存</button>
							          </div>
								  </form>
					          </div>
					          
					        </div><!-- /.modal-content -->
					      </div><!-- /.modal-dialog -->
					</div><!-- /.modal -->
					
                </div>
                   
       			<legend style="width: 82%;margin-left: 9%;margin-bottom: -5px;"></legend>
       			
       			<div class="C_tabBox clearfix" style="width: 80%;">
				   <div class="C_tabList">
					<table class="table table-condensed table-hover table-striped tablesorter table-fixed" id="tasktable">
				        <thead>   						
				        	<script>
					                var checkflag = "false";
									function check(fieldName) {
									var field=document.getElementsByName(fieldName);
									if (checkflag == "false") {
										for (i = 0; i < field.length; i++) {
										field[i].checked = true;}
										checkflag = "true";
										return "Uncheck All"; }
									else {
										for (i = 0; i < field.length; i++) {
										field[i].checked = false; }
										checkflag = "false";
										return "Check All"; }
									}
					            </script>
				          <tr class="text-center">
				            <th width="9%" class="text-center">全选&nbsp;<input type="checkbox" value="" style="margin-top: -3px;"onclick="this.value=check('choose')"></th>
				            <th width="9%" class="text-center">属性名称(中)</th>
				            <th width="9%" class="text-center">字段名称(英)</th>
				            <th width="9%" class="text-center">属性类型</th>
				            <th width="9%" class="text-center">属性长度</th>
				            <th width="9%" class="text-center">是否有主键</th>
				            <th width="9%" class="text-center">是否有索引</th>
				            <th width="9%" class="text-center">是否为空</th>
				            <th width="9%" class="text-center">默认值</th>
				            <th width="10%" class="text-center">备注</th>
				          </tr>
				        </thead>
				        <tbody>
				         	<c:forEach items="${mms }" var="k">
						         <tr class="text-center"> 
						            <td><input name="choose" type="checkbox" value="${k.id }"></td>
						           <td>${k.itemName }</td>
									<td>${k.itemCode }</td>
									<td>${k.itemType }</td>
									<td>${k.itemLength }</td>
									<td>
										<c:if test="${k.usePrimaryKey==1 }">
											是
										</c:if>
										<c:if test="${k.usePrimaryKey==0 }">
											否
										</c:if>
										<c:if test="${k.usePrimaryKey==null }">
											否
										</c:if>
									</td>
									<td>
										<c:if test="${k.useIndex==1 }">
											是
										</c:if>
										<c:if test="${k.useIndex==0 }">
											否
										</c:if>
									</td>
									<td>
										<c:if test="${k.useNull==1 }">
											是
										</c:if>
										<c:if test="${k.useNull==0 }">
											否
										</c:if>
									</td>
									<td>${k.defaultValue }</td>
									<td>${k.remark }</td>
						          </tr>
				         	</c:forEach>
				         
				        </tbody>
				        <tfoot>
			          </tfoot>
				      </table>
				   </div>		
       			   <div class="C_pagerBox  clearfix">
				      <div class="C_pager" style="width:480px;">
				                <ul class="pager pager-loose">
				                  <li class="previous"><a href="#">首页</a></li>
				            	  <li class="previous"><a href="#">« 上一页</a></li>
				                  <li><a href="#">1</a></li>
				                  <li><a href="#">2</a></li>
				                  <li><a href="#">3</a></li>
				                  <li><a href="#">4</a></li>
				                  <li class="active"><a href="#">5</a></li>
				                  <li class="next disabled"><a href="#">下一页 »</a></li>
				                  <li class="next disabled"><a href="#">尾页</a></li>
				                </ul>            
				      </div>
					</div>
       			</div>
		</div>		
	</body>
</html>
