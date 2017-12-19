/**
 * 三级联动插件 1.0
 * 
 * 使用方法：
 *  var sql='province;city;area';
	var label='省：;市：;县：';
	var name='provinceId;cityId;id'
	var option={name:name,sql:sql,label:label};
 *  new multilevelLinkage(option)
 * 
 * @author venson
 */

var multilevelLinkage=function(option){
		this.option=$.extend(multilevelLinkage.DEFAULTS,option);
		this.option.names=this.option.name.split(';');
		this.option.labels=this.option.label.split(';');
		this.option.sqls=this.option.sql.split(';');
		this.init();
	}

	multilevelLinkage.DEFAULTS={
		name:$("#select_select2_name").val(),	//select标签name属性值
		label:$("#select_select2_label").val(),   //label标签值
		sql:$("#select_select2_sql").val(),     //sql语句数据来源
		idPrefix:'select_select2',  //id前缀
		url:'api/execute',  //后端接口
		container:'.select_select2_main'
	}
	multilevelLinkage.prototype={
			init:function(){
				var that=this;
				$.each(that.option.sqls,function(i,e){
					if(!$.trim(e)){
						return;
					}
					//添加dom节点
					var html='<div class="col-md-4"><div class="input-group"><span class="input-group-addon">'+that.option.labels[i]+'</span><select id="'+that.option.idPrefix+i+'" name="'+that.option.names[i]+'" class="'+that.option.idPrefix+' form-control"></select></div></div>';
					
					$warp=$(html);
					$(that.option.container).append($warp);
					var $select2=$warp.find("."+that.option.idPrefix)
					//判断是否为第一节点，如果是则预加载数据
					if(i==0){
						that.setData('#'+that.option.idPrefix+'0',Comm.getData(that.option.url+"/"+e));
					}else{
						$select2.parent().hide();
					}
					//绑定事件
					$select2.on('change',function(){
						that.clearData('#'+that.option.idPrefix+(i+1));
						//判断数据是否为空
						if($.trim($(this).val())){
							//最后一级
							 if(i==that.option.sqls.length-1){
								 var finalVal=that.getFinalVal(that.option.names);
								 $(that.option.container).prev("input[type='hidden']").val(finalVal);
								return;
							}else{
								var sql=that.option.sqls[i+1];
								var paramName=$(this).attr("name");
								if(sql){
									var param={};
									param[$(this).attr("name")]=$(this).val();
									that.setData('#'+that.option.idPrefix+(i+1),Comm.getData(that.option.url+"/"+$.trim(sql),that.getParams(sql)));
								}
							}
						}
					})
				})
			},
			getParams:function(sql){
				var that=this;
				var param={};
				param["_method"]="get";
				param.APIId=$.trim(sql);
				$.each(that.option.names,function(i,e){
					var val=$("."+that.option.idPrefix+'[name='+e+']').val();
					if(val){
						param[e]=val;
					}
				})
				return param;
			},
			clearData:function(tag){
				var i=tag.replace('#'+this.option.idPrefix,'')/1;
				//隐藏下级下拉框
				if(i<this.option.sqls.length-1&&i!=0){
					for(i;i<this.option.sqls.length;i++){
						$('#'+this.option.idPrefix+i).parent().hide();
					}
				}
			},
			getFinalVal:function(names){
				var that=this;
				var finalVal=[];
				$.each(names,function(i,e){
					finalVal.push($("."+that.option.idPrefix+'[name='+e+'] option:selected').text());
				})
				return finalVal.join(' ');
			},
			setData:function(tag,data){
				if(data.length==0){
					Comm.alert("未找到数据。");
					return;
				}
				/*
				$(tag).width(200);
				$(tag).html("<option></option>");
				$(tag).select2({
					  data: data
				})*/
				
				$(tag).parent().show();
				Comm.setSelectData($(tag),data);
			}
			
			
	}
	new multilevelLinkage();