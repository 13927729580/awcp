Array.prototype.remove = function(s) {
	for (var i = 0; i < this.length; i++) {
		if (s == this[i])
			this.splice(i, 1);
	}
}

/**
 * Simple Map
 * 
 * 
 * var m = new Map(); m.put('key','value'); ... var s = "";
 * m.each(function(key,value,index){ s += index+":"+ key+"="+value+"/n"; });
 * alert(s);
 * 
 * 
 */
function Map() {
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
	/** 存放数据 */
	this.data = new Object();

	/**
	 * 放入一个键值对
	 * 
	 * @param {String}
	 *            key
	 * @param {Object}
	 *            value
	 */
	this.put = function(key, value) {
		if (this.data[key] == null) {
			this.keys.push(key);
		}
		this.data[key] = value;
	};

	/**
	 * 获取某键对应的值
	 * 
	 * @param {String}
	 *            key
	 * @return {Object} value
	 */
	this.get = function(key) {
		return this.data[key];
	};

	/**
	 * 删除一个键值对
	 * 
	 * @param {String}
	 *            key
	 */
	this.remove = function(key) {
		this.keys.remove(key);
		this.data[key] = null;
	};

	/**
	 * 遍历Map,执行处理函数
	 * 
	 * @param {Function}
	 *            回调函数 function(key,value,index){..}
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		var len = this.keys.length;
		for (var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.data[k], i);
		}
	};

	/**
	 * 获取键值数组(类似Java的entrySet())
	 * 
	 * @return 键值对象{key,value}的数组
	 */
	this.entrys = function() {
		var len = this.keys.length;
		var entrys = new Array(len);
		for (var i = 0; i < len; i++) {
			entrys[i] = {
				key : this.keys[i],
				value : this.data[i]
			};
		}
		return entrys;
	};

	/**
	 * 判断Map是否为空
	 */
	this.isEmpty = function() {
		return this.keys.length == 0;
	};

	/**
	 * 获取键值对数量
	 */
	this.size = function() {
		return this.keys.length;
	};
	/**
	 * 清空map
	 */
	this.clear=function(){
		var len = this.keys.length;
		for (var i = 0; i < len; i++) {
			var k = this.keys[i];
			this.keys.remove(k);
			this.data[k] = null;
		}
	};
	/**
	 * 重写toString
	 */
	this.toString = function() {
		var s = "{";
		for (var i = 0; i < this.keys.length; i++, s += ',') {
			var k = this.keys[i];
			s += k + "=" + this.data[k];
		}
		s += "}";
		return s;
	};
	
	/**
	 * 转化为json字符串--依赖json2。
	 */
	this.toJSON=function(){
		if(this.keys.length==0){
			return "";
		}
		var s = "[";
		for (var i = 0; i < this.keys.length; i++, s += ',') {
			var k = this.keys[i];
			s +=JSON.stringify(this.data[k]);
		}
		if(","==s.charAt(s.length-1)){
			s=s.substring(0,s.length-1);
		}
		s += "]";
		return s;
	};
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

	function guidGenerator() {
	    var S4 = function() {
	       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	    };
	    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	}


	function StringBuilder(){
		 this._element_ = new Array();
		}
		StringBuilder.prototype.append = function(item) {
		 this._element_.push(item);
		}
		StringBuilder.prototype.toString = function() {
		 return this._element_.join("");
		}
		StringBuilder.prototype.toJsonString = function() {
		 return this._element_.join(",");
		}
		StringBuilder.prototype.join = function(separator) {
		 return this._element_.join(separator);
		}
		StringBuilder.prototype.length = function() {
		 return this._element_.length;
		}




	function form2Json(formName){
		var form=document.getElementById(formName);var sb=new StringBuilder(); 
		 for ( var i = 0; i < form.elements.length; i++){
		  var element = form.elements[i]; var name = element.name;
		  if (typeof (name) === "undefined" || (name === null) || (name.length === 0)){continue;}
		  var tagName = element.tagName;
		  if(tagName ==='INPUT'||tagName === 'TEXTAREA'){var type = element.type;
		   if ((type === 'text')||(type === 'password') || (type === 'hidden') || (tagName === 'TEXTAREA')){
		    sb.append("\""+encodeURIComponent(name)+"\":\""+(element.value)+"\"");
		   }else if((type === 'checkbox') || (type === 'radio')){
		    if(element.checked){sb.append("\""+encodeURIComponent(name)+"\":\""+(element.value)+"\"");
		    }else{sb.append("\""+encodeURIComponent(name)+"\":\"\"");}
		   }else{continue;}
		  }else if (tagName === 'SELECT'){var oc = element.options.length;
		   for ( var j = 0; j <oc; j++){var option = element.options[j];
		    if (option.selected){sb.append("\""+encodeURIComponent(name)+"\":\""+(element.value)+"\"");}
		   }
		  }
		 }return "{"+sb.toJsonString()+"}"; 
		}



	function tableToJson(table) {
		var data = [];
		
		// first row needs to be headers
		var headers = [];
		for (var i=0; i<table.rows[0].cells.length; i++) {
			headers[i] = table.rows[0].cells[i].innerHTML.toLowerCase().replace(/ /gi,'');
		}
		
		// go through cells
		for (var i=1; i<table.rows.length; i++) {
			
			var tableRow = table.rows[i];
			var rowData = {};
			
			for (var j=0; j<tableRow.cells.length; j++) {
				
				rowData[ headers[j] ] = tableRow.cells[j].innerHTML;
			
			}
			
			data.push(rowData);
		}		
		
		return data;
	}




