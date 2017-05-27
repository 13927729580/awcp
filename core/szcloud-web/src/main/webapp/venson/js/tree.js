/**
 * 树函数
 * version 1.0
 * author: venson
 */
var Tree = function(id, name) {
	this.name = name;
	this.id = id;
	this.number = 1;
	this.children = [];
}
//set 方法
Tree.prototype.setName = function(name) {
	this.name = name;
}
Tree.prototype.setNumber = function(number) {
	this.number = number;
}

//是否有子节点
Tree.prototype.hasChild = function() {
	if (this.children.length == 0) {
		return false;
	} else {
		return true;
	}
}
//查找所有的父级节点
Tree.prototype.findPid = function(data, key) {
	var roots = [];
	$.each(data, function(i, e) {
		roots.push(e[key.pid]);
	})
	return roots.unique().sort();
}
//查找子节点
Tree.prototype.findChild = function(data, key, pids) {
	var that = this;
	//自动查找pid
	if (!pids) {
		pids = that.findPid(data, key);
	}
	var number = 1;
	$.each(data, function(i, e) {
		if(!that.name&&e[key.id]== that.id){
			that.name=e[key.name];
		}
		if (e[key.pid] == that.id) {
			var children = new Tree(e[key.id],e[key.name]);
			children.setNumber(number);
			number++;
			that.children.push(children);
			// 判断子节点是否为父节点
			if ($.inArray(children.id, pids) != -1) {
				children.findChild(data, key, pids);
			}
		}

	})
}
//遍历子节点
Tree.prototype.eachChild = function(level, callback) {
	//回调函数
	callback(level, this);
	if (this.hasChild()) {
		level++;
		$.each(this.children, function(i, e) {
			e.eachChild(level, callback);
		})
	}
}
//去重
Array.prototype.unique = function() {
	var res = [];
	var json = {};
	for (var i = 0; i < this.length; i++) {
		if (!json[this[i]]) {
			res.push(this[i]);
			json[this[i]] = 1;
		}
	}
	return res;
}