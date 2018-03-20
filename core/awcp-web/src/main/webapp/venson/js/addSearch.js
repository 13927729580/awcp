/**
 * 搜索
 * 
 * 使用方法：
 *  new addSearch(option)
 * 
 * @author venson
 */
var addSearch = function(option){
	this.option = $.extend(addSearch.DEFAULTS,option);
	this.init();
}
addSearch.DEFAULTS={
	selectLabel:$("#selectLabel").val(),	//下拉框标签
	selectName:$("#selectName").val(),		//下拉框name值
	selectOption:$("#selectOption").val(),	//下拉框数据（sql/Key=value）
	textLabel:$("#textLabel").val(), 	//文本标签
	textName:$("#textName").val(), 		//文本Name值	
	dateSelectLabel:$("#dateSelectLabel").val(),	//日期选择框标签
	dateSelectName:$("#dateSelectName").val(), 		//日期选择框Name值
	textPrefix:'search_text',  
	selectPrefix:'search_select', 
	dateSelectPrefix:'search_dateSelect',
	url:'api/execute',  		//后端接口
	container:'.search_main'	//容器
}

addSearch.prototype={
	init:function(){
		//判断是否有文本框
		if($.trim(this.option.textLabel)){
			this.initText();
		}
		
		//判断是否有下拉框 
		if($.trim(this.option.selectLabel)){
			this.initSelect();
		}
			
		//判断是否有时间选择框
		if($.trim(this.option.dateSelectLabel)){
			this.initDateSelect();
		}
	},
	
	initText:function(){
		var textLabels=this.option.textLabel.split("@");
		var textNames=this.option.textName.split("@");
		var textPrefix=this.option.textPrefix;
		var that=this;
		$.each(textLabels,function(i,e){
			if(!$.trim(e)){
				return;
			}
			var name = textNames[i];
			var html = '<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3" style="margin-bottom:10px">' + 
					   '<div class="input-group"><span class="input-group-addon">' + e + '</span>' + 
					   '<input name="' + name + '" class="' + textPrefix + ' form-control"/></div></div>';
			var $warp=$(html);
			$(that.option.container).append($warp);
			var $tag=$warp.find("."+textPrefix);
			that.setDefaultValue($tag,"1");
		})
	},
	
	initSelect:function(){
		var selectLabels=this.option.selectLabel.split("@");
		var selectNames=this.option.selectName.split("@");
		var selectOptions=this.option.selectOption.split("@");
		var selectPrefix=this.option.textPrefix;
		var container=this;
		var that=this;
		$.each(selectLabels,function(i,e){
			if(!$.trim(e)){
				return;
			}
			var option =selectOptions[i];
			var name =selectNames[i];
			var html = '<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3" style="margin-bottom:10px">' + 
					   '<div class="input-group"><span class="input-group-addon">' + e + '</span>' + 
					   '<select name="' + name + '" class="' + selectPrefix + ' form-control"></select></div></div>';
			var data;
			//查找是否是动态语句查找
			if(option.indexOf("=")==-1||option.indexOf("?")!=-1){
				data=Comm.getData(that.option.url+"/"+option,{"_method":"get"});
			}else{
				var options=option.split(";");
				data=[];
				$.each(options,function(i1,o){
					if(!$.trim(o)){
						return;
					}
					var arr=o.split("=");
					data.push({id:arr[0],text:arr[1]});
				})
			}
			$warp=$(html);
			$(that.option.container).append($warp);
			var $tag=$warp.find("."+selectPrefix);			
			Comm.setSelectData($tag,data);
			$tag.select2();
			that.setDefaultValue($tag,"2");
		})
	},
	
	initDateSelect:function(){
		var dateSelectLabels=this.option.dateSelectLabel.split("@");
		var dateSelectNames=this.option.dateSelectName.split("@");
		var dateSelectPrefix=this.option.dateSelectPrefix;
		var that=this;
		$.each(dateSelectLabels,function(i,e){
			if(!$.trim(e)){
				return;
			}
			var name = dateSelectNames[i];
			var html='<div class="col-xs-6 col-sm-6 col-md-3 col-lg-3" style="margin-bottom:10px">' + 
					 '<div class="input-group"><span class="input-group-addon">' + e + '</span>' + 
					 '<input type="datetime" name="' + name + '" class="' + dateSelectPrefix + ' form-control"/></div></div>';
			var $warp = $(html);
			$(that.option.container).append($warp);
			var $tag = $warp.find("."+dateSelectPrefix);		
			$tag.datetimepicker({
				language : 'zh-CN',
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				format : 'yyyy-mm-dd'
			});
			that.setDefaultValue($tag,"3");
		})
	},
	
	setDefaultValue:function($tag,type){
		var name = $tag.attr("name");
		var that = this;
		//查看是否是新打开页面，如果是则清空之前的搜索条件
		if(location.href.indexOf("?dynamicPageId=")!=-1){
			Comm.set(that.option.selectPrefix+name,"");
			Comm.set(that.option.textPrefix+name,"");
			Comm.set(that.option.dateSelectPrefix+name,"");
		}
		if(type=="1"){
			$tag.val(Comm.get(that.option.textPrefix+name));
			$tag.bind("keyup",function(){
				Comm.set(that.option.textPrefix+name,this.value);
			});
		} else if(type=="2"){
			$tag.val(Comm.get(that.option.selectPrefix+name)).select2();
			$tag.bind("change",function(){
				Comm.set(that.option.selectPrefix+name,this.value);
			});
		} else if(type=="3"){
			$tag.val(Comm.get(that.option.dateSelectPrefix+name));
			$tag.bind("change",function(){
				Comm.set(that.option.dateSelectPrefix+name,this.value);
			});
		} 
	}	
}
