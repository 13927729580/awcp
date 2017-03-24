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