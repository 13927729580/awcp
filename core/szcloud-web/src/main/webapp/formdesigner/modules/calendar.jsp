<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
 <%@ taglib prefix="sc" uri="szcloud" %>
 <%@page isELIgnored="false"%> 
 <%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="renderer" content="webkit">
 		<title>calendar</title>
 		
 		<%@ include file="/resources/include/common_form_css.jsp" %><!-- 注意加载路径 -->
 		
		<link rel="stylesheet" href="<%=basePath %>resources/styles/layout.css"/>
		<link rel="stylesheet" href="<%=basePath %>resources/styles/calendar.css"/><!--日历css-->
		<link rel="stylesheet" href="<%=basePath %>base/resources/zui/assets/datetimepicker/css/datetimepicker.css"/><!--日期css-->
		<!--[if lt IE 9]>
		  <script src="../plugins/zui/assets/html5shiv.js"></script>
		  <script src="../plugins/zui/assets/respond.js"></script>
		<![endif]-->
 		
	</head>
	<body>
		
		<div style="padding:10px;background-color:#fff;">
			<div id="calendar" class="calendar"></div><!-- 日历容器 -->
			<!-- <div class="btn-group">
                <button class="btn btn-default" data-toggle="tooltip" data-placement="left" title="" data-original-title="Tooltip on left" id="refresh">刷新</button>
                <button class="btn btn-default" id="create">新增事件</button>
                <button class="btn btn-default">第三个</button>
                <a href="javascript:;" onclick="abc(this);return false">123</a>
             </div> -->
             <div class="modal fade" id="myModal"><!-- 弹窗内容 begin -->
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
			        <h4 class="modal-title">日程事件</h4>
			      </div>
			      <div class="modal-body">
			        <form class="form-horizontal form-condensed" role="form" method="post">
			          <input type="hidden" id="userId" name="userId" value="${userId}"/>
			          <div class="form-group">
			            <label class="col-xs-2 control-label">事件标题</label>
			            <div class="col-xs-10">
			               <input type="text" name="title" id="title" value="" class="form-control">
			            </div>
			          </div>
					 <div class="form-group">
			            <label class="col-xs-2 control-label"></label>
			            <div class="col-xs-10">
				            <label class="radio-inline text-primary"> <input type="radio" name="calendar" value="primary" checked>primary</label>
				            <label class="radio-inline text-success"> <input type="radio" name="calendar" value="success">success</label>
				            <label class="radio-inline text-danger"> <input type="radio" name="calendar" value="danger">danger</label>
				            <label class="radio-inline text-important"> <input type="radio" name="calendar" value="important">important</label>
				            <label class="radio-inline text-warning"> <input type="radio" name="calendar" value="warning">warning</label>
				            <label class="radio-inline text-info"> <input type="radio" name="calendar" value="info">info</label>
			            </div>
			          </div>
			          <div class="form-group">
			            <label class="col-xs-2 control-label">开始时间</label>
			            <div class="col-xs-4">
			              <div class="input-group date form-datetime">
				              <input class="form-control" name="start" id="start" size="16" type="text" value="" readonly="">
				              <span class="input-group-addon"><span class="icon-calendar"></span></span>
				            </div>
			            </div>
			            <label class="col-xs-2 control-label">结束时间</label>
			            <div class="col-xs-4">
			              <div class="input-group date form-datetime">
				              <input class="form-control" name="end" id="end" size="16" type="text" value="" readonly="">
				              <span class="input-group-addon"><span class="icon-calendar"></span></span>
				            </div>
			            </div>
			          </div>
			          
			          <div class="form-group">
			            <label class="col-xs-2 control-label">事件内容</label>
			            <div class="col-xs-10">
			              <textarea name="desc" id="desc" rows="10" class="form-control"></textarea>
			            </div>
			          </div>
			        </form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			        <button type="button" class="btn btn-primary" id="sumbit">确定</button>
			      </div>
			    </div>
			  </div>
			</div><!-- 弹窗内容 end -->
		</div>
			<script src="<%=basePath %>resources/JqEdition/jquery-2.2.3.min.js"></script>
			<script type="text/javascript" src="<%=basePath %>base/resources/zui/src/js/calendar.js"></script><!--日历-->
	
		<script type="text/javascript" src="<%=basePath %>base/resources/zui/dist/js/zui.js"></script>

		<script type="text/javascript" src="<%=basePath %>base/resources/zui/assets/datetimepicker/js/datetimepicker.js"></script>
		<script type="text/javascript" src="<%=basePath %>base/resources/artDialog/dist/dialog-plus-min.js"></script>
		<script type="text/javascript" src="<%=basePath %>resources/scripts/common.js"></script>
		<script>
		  var basePath = "<%=basePath%>";
		  var userId = $("#userId").val();
		  var now = new Date();
		  var myModal = $("#myModal").html(); 
		  var calEventGenerater = function()
		      {
			  		//ajax取当前用户的日历列表，数据格式 jsonArray：返回jsonArray
			  		var e = new Array();;
		          /* var e =
		          {		
		        	  id:parseInt(Math.random()*100000000),
		              title: (Math.random() > 0.5 ? ('和' + peoples[Math.floor(Math.random()*peoples.length)]) : '') + events[Math.floor(Math.random()*events.length)],
		              desc: descs[Math.floor(Math.random()*descs.length)],
		              calendar: calendars[Math.floor(Math.random()*calendars.length)],
		              allDay: Math.random() > 0.9,//true,false 默认fasle 
		              start: start,
		              end: start.clone().addDays(Math.random() * 1.5)//时间格式  ： xxxx/xx/xx xx:xx
		          }; */
		          $.ajax({
		        	  type:"post",
		        	  url:basePath+"calendar/findByUserId.do",
		        	  data:"userId="+userId,
		        	  async:false,
		        	  dataType:"json",
		        	  success:function(data){
		        		  //alert(data);
		        		  var tem = null;
		        		  for(var i=0;i<data.length;i++){
		        			  tem = {
		        					  id:data[i].id,
		        		              title: data[i].title,
		        		              desc: data[i].desc,
		        		              calendar: data[i].calendar,
		        		              allDay: data[i].allDay,//true,false 默认fasle 
		        		              start: new Date(data[i].start),
		        		              end: new Date(data[i].end)//时间格式  ： xxxx/xx/xx xx:xx  
		        			  }
		        			  e.push(tem);
		        		  }
		        	  }
		          });
		          return e;
		      };
		  var calDataGenerater = function()
		      {
		          var data =
		          {
		              calendars:
		              {
		                  success: {color: '#229f24'},
		                  warning: {color: '#e48600'},
		                  danger: {color: '#d2322d'},
		                  info: {color: '#39b3d7'},
		                  important: {color: '#81511c'}
		              },
		              events: []
		          };
		          
		          var result = calEventGenerater();
		          
		          for (var i = result.length - 1; i >= 0; i--)
		          {
		              data.events.push(result[i]);
		          }
		          
		
		          return data;
		      };
		  
	      var addPopTip = function(item){//提示
	        $.each(item.options.data.events,function(index,item){
	        	$("[data-id='"+item.id+"']").tooltip({
					  container:"body",
					  placement:"bottom"
				  });
	        })
	      };
	      var popUp = function(){//弹窗
	    	 var $modal = $("#myModal");
     		 $modal.html(myModal);
     		 $modal.find(".form-datetime").datetimepicker({
			        format: "yyyy/mm/dd hh:ii",
			        autoclose: true,
			        todayBtn: true,
			        pickerPosition: "bottom-left"
			    });
     		 $modal.modal();
     		 $modal.find("form :input").change(function() {$modal.find("form").data("changed",true)});
     		 return $modal;
	      };
	      var validateForm = function(form){//添加行程表单 验证！ 
	      	var $form = $(form);
	      	var isValidate = true;
	      	$form.find(":input").each(function(){
	      		if($(this).val()==""){
	      			$(this).parent().addClass("has-error");
	    	      	isValidate = false;
	      		}
	      	});
	      	$form.find(":input").on({
	      		blur:function(){
	      			if($(this).val()==""){
		      			$(this).parent().addClass("has-error");
		      			isValidate = false;
		      		}else{
		      			$(this).parent().removeClass("has-error");
		      			isValidate = true;
		      		}	
	      		}
	      	});
	      	return isValidate;
	      }

		  $(function(){ 
			var item;
		    $('.calendar').each(function()
		    {
		        var $this = $(this);
		        var data = calDataGenerater();
		        var calendar = $this.calendar({data: data,dragThenDrop:true,
		        	clickEvent:function(e){
		        		$modal = popUp();
		        		$modal.find(".modal-title").html("修改日程事件");
		        		$modal.find(".modal-footer").append('<button type="button" class="btn btn-danger" id="delete">移除</button>');
						$modal.find("form input[type=text]").each(function(){
							$(this).val(e.event[$(this).attr("name")].format("yyyy/MM/dd hh:mm"));
						});
						$modal.find("input[value="+e.event.calendar+"]").attr("checked","checked");
						$modal.find("form textarea").text(e.event.desc);
						$modal.find("#sumbit").click(function(){//确定修改
							var updateEvent = {}
							updateEvent.event = e.event.id;
							updateEvent.changes=[];
							var $form = $modal.find("form");
							if($form.data("changed")){
								if(validateForm($form)){
									var data = $form.serializeArray();
									data.push({"name":"id","value":""+e.event.id});
									//todo  ajax 传回数据，data修改DB
									$.ajax({
					        			type:"post",
					        			url:basePath+"calendar/save.do",
					        			data:data,
					        			success:function(result){
											//alert(result);
											result.start = new Date(result.start);
											result.end = new Date(result.end);
											updateEvent.changes.push($.extend(true,{},{change: "title",to: result.title}));
											updateEvent.changes.push($.extend(true,{},{change: "desc",to: result.desc}));
											updateEvent.changes.push($.extend(true,{},{change: "calendar",to: result.calendar}));
											updateEvent.changes.push($.extend(true,{},{change: "start",to: result.start}));
											updateEvent.changes.push($.extend(true,{},{change: "end",to: result.end}));
											item.updateEvents(updateEvent);
					        				$.messager.show("修改成功！");
					        				$modal.modal("hide");
					        				addPopTip(item);
					        			}
					        		});
								}
							}else {
								$modal.modal("hide");
								return false;
							}
						});
						$modal.find("#delete").click(function(){//确定删除
							var id = e.event.id;
							
							//todo ajax删除记录 
							$.ajax({
				        			type:"post",
				        			url:basePath+"calendar/delete.do",
				        			data:"id="+id,
				        			success:function(result){
				        				
				        					item.removeEvents(id);
					        				$.messager.show("删除成功！");
											$modal.modal("hide");
											addPopTip(item);
				        				
				        				
				        			}
				        		});
							
						})
		        	},
		        	clickCell:function(e){
		        		$modal = popUp();
		        		$modal.find(".modal-title").html("新增日程事件");
			        	var now = new Date(e.date);
			        	$modal.find("input[name=start],input[name=end]").val(now.format("yyyy/MM/dd hh:mm"));
			        	$modal.find("#sumbit").click(function(){
			        		var $form = $modal.find("form")
		        		    if(validateForm($form)){
			        			var data = $form.serializeArray();
				        		/* var newEvent={};
				        		$.each(data,function(index,item){
				        			newEvent[item.name]=item.value;
				        		}); */
				        		//item.addEvents(newEvent);
				        		//todo  ajax 调后台，存入数据库 传回去的数据 data
				        		$.ajax({
				        			type:"post",
				        			url:basePath+"calendar/save.do",
				        			data:data,
				        			success:function(result){
										//alert(data);
										result.start = new Date(result.start);
										result.end = new Date(result.end);
										item.addEvents(result);
				        				$.messager.show("新增成功！");
				        				$modal.modal("hide");
				        				addPopTip(item);
				        			}
				        		});
				        		
				        		
				        		
			        		}
			        	})
		        	}
		        });
		         item = calendar.data('zui.calendar');
		         addPopTip(item);
		  });
		  /*  $("#refresh").click(function(){
		    	item.display('month', '2014-8-14');
		    	alert("refresh");
		    });
	        $("#create").click(function(){
	        	var newEvent = {title: '日程表格插件', desc: '要吃更多', start: '2015/3/18 12:00', end: '2015/3/19 13:00'};
		       item.addEvents(newEvent);
	        }) */
	});
  	</script>
	</body>
</html>