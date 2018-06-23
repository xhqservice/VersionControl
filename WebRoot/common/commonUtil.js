
/** ===============================================================
 *
 * 公用的工具js，将常用的通用方法在这里定义
 * 	String 增强
 * 	Array 增强
 * 	...
 * 	
 * =================================================================*/

/**
 *	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ String增强 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
String.prototype.endWith = function (s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length) {
		return false;
	}
	if (this.substring(this.length - s.length) == s) {
		return true;
	} else {
		return false;
	}
	return true;
};
String.prototype.startWith = function (s) {
	if (s == null || s == "" || this.length == 0 || s.length > this.length) {
		return false;
	}
	if (this.substr(0, s.length) == s) {
		return true;
	} else {
		return false;
	}
	return true;
};
String.prototype.length2 = function () {
	var cArr = this.match(/[^\x00-\xff]/ig);
	return this.length + (cArr == null ? 0 : cArr.length);
};
String.prototype.trim = function () {
	var i, b = 0, e = this.length;
	for (i = 0; i < this.length; i++) {
		if (this.charAt(i) != " ") {
			b = i;
			break;
		}
	}
	if (i == this.length) {
		return "";
	}
	for (i = this.length - 1; i >= b; i--) {
		if (this.charAt(i) != " ") {
			e = i;
			break;
		}
	}
	return this.substring(b, e + 1);
};

/**
 *	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 数组增强 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
Array.prototype.indexOf = function(val) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == val) return i;
	}
	return -1;
};

Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};

Array.prototype.contains = function ( needle ) {
  	for (i in this) {
    	if (this[i] == needle) return true;
  	}
  	return false;
};

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ JsonUtils ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

function parseToJson(str) {
	
	var obj = {};
	
	if (isBlank(str)) {
		return {};
	}
	if (!(str.startWith("{") && str.endWith("}"))) {
		return {};
	}
	var entryArr = str.split(",");
	if (entryArr.length>0) {
		for(var i=0;i<entryArr.length;i++) {
			var oneEntry = entryArr[i].split("=");
			if (oneEntry.length != 2) {
				continue;
			}
			var key = oneEntry[0];
			var value = oneEntry[1];
			
			if (value != null && !isBlank(value)) {
				if (value.startWith("{")) {
					obj[key] = parseToJson(value);
				}else {
					if (value.startWith("[") && value.endWith("]")) {
						obj[key] = parseToJsonArr(value);
					}else {
						obj[key] = value+"";
					}
				}
			}
		}
	}
	
	return obj;
}

function parseToJsonArr(str) {
	var objArr = new Array();
	
	if (isBlank(str)) {
		return objArr;
	}
	if (!(str.startWith("[") && str.endWith("]"))) {
		return objArr;
	}
	var jsonArr = str.split(",");
	if (jsonArr.length>0) {
		for(var i=0;i<jsonArr.length;i++) {
			var value = jsonArr[i];
			if (value != null && value.startWith("{") && value.endWith("}")) {
				objArr.push(parseToJson(value));
			}else {
				objArr.push(value+"");
			}
		}
	}
	
	return objArr;
}

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ jquery相关 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

/**
 * 左右抖动效果
 */
jQuery.fn.shake = function (intShakes , intDistance , intDuration) {
	this.each(function () {
		var jqNode = $(this);
        jqNode.css({ position: 'relative' });
        for (var x = 1; x <= intShakes; x++) {
            jqNode.animate({ left: (intDistance * -1) }, (((intDuration / intShakes) / 4)))
            .animate({ left: intDistance }, ((intDuration / intShakes) / 2))
            .animate({ left: 0 }, (((intDuration / intShakes) / 4)));
        }
    });
    return this;
};

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 表单相关 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

/**
 * 禁用指定容器下的所有表单元素（提交时候不会带上里面的参数信息）
 * @param parentId 父容器id
 * @param isShowSelf 是否隐藏本身
 */
function disabledFormItemInContainer(parentId, isHideSelf) {
	$("#"+parentId+" input").each(function(){
		$(this).attr("disabled", true);
	});
	$("#"+parentId+" textarea").each(function(){
		$(this).attr("disabled", true);
	});
	$("#"+parentId+" select").each(function(){
		$(this).attr("disabled", true);
	});
	if (isHideSelf) {
		$("#"+parentId).hide();
	}
}

/**
 * 启用容器下的表单元素（从禁用状态变为正常状态）
 * @param parentId 父容器id
 * @param isShowSelf 是否显示本身
 */
function enabledFormItemInContainer(parentId, isShowSelf) {
	$("#"+parentId+" input").each(function(){
		$(this).attr("disabled", false);
	});
	$("#"+parentId+" textarea").each(function(){
		$(this).attr("disabled", false);
	});
	$("#"+parentId+" select").each(function(){
		$(this).attr("disabled", false);
	});
	if (isShowSelf) {
		$("#"+parentId).show();
	}
}


/**
 * 	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 常用工具方法 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
/*
 * 生成GUID
 */
function S4() {
   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}
function guid() {
   return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}
/*
 * 是否为空：null、""、"    "、undefined 
 */
function isBlank(val) {
	if (val == undefined || val == null || val == "") {
		return true;
	}else {
		if ((val+"").replace(/" "/g, "") == "") {
			return true;
		}
		return false;
	}
}

/**
 * 获取移动端设备类型  0：Android、1：IOS、2：其他
 */
function getAPPConsoleType() {
	var u = navigator.userAgent, app = navigator.appVersion;
	var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
	var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
	if (isAndroid) {
		return "0";
	}else if (isiOS) {
		return "1";
	}else {
		return "2";
	}
}

/**
 * 判断是否全为字母或者数字
 * @param val
 */
function isAllZmOrSz(val) {
	val = val.trim();
	if (isBlank(val)) {
		return false;
	}
	var Regx = /^[A-Za-z0-9]*$/;
    if (Regx.test(val)) {
        return true;
    }
    return false;
}

/**
 * 判断是否全为字母
 * @param val
 */
function isAllZm(val) {
	val = val.trim();
	if (isBlank(val)) {
		return false;
	}
	var Regx = /^[A-Za-z]*$/;
    if (Regx.test(val)) {
        return true;
    }
    return false;
}

/**
 * 判断是否全为数字
 * @param val
 */
function isAllSz(val) {
	val = val.trim();
	if (isBlank(val)) {
		return false;
	}
	var Regx = /^[0-9]*$/;
    if (Regx.test(val)) {
        return true;
    }
    return false;
}

/**
 * 邮箱校验
 * @param val
 */
function checkEmail(val) {
	val = val.trim();
	if (isBlank(val)) {
		return false;
	}
	
	var Regx = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if (Regx.test(val)) {
        return true;
    }
    return false;
}

/**
 * 手机号码校验
 * @param val
 */
function checkSjhm(val) {
	val = val.trim();
	if (isBlank(val)) {
		return false;
	}
	
	var Regx = /^1[34578]\d{9}$/;
	if (Regx.test(val)) {
        return true;
    }
    return false;
}

/**
 * 兼容获取窗口可视区域的宽高
 * @returns Object对象，{width: xxx,height:xxx}
 */
function getWindowClient(){
	 if(window.innerHeight !== undefined){
		 return {
			 "width": window.innerWidth,
			 "height": window.innerHeight
        };
    }else if(document.compatMode === "CSS1Compat"){
            return {
            	"width": document.documentElement.clientWidth,
            	"height": document.documentElement.clientHeight
            };
   }else{
     return {
    	 "width": document.body.clientWidth,
    	 "height": document.body.clientHeight
     };
   }
}

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 基于layer的弹出层 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 *  ***  该弹出层几乎所有的方法都是异步完成的，因此：需要把弹出之后的操作放到回调函数里面完成  
 *  	eg: 
 *  		layer.alert("hello");
 *  		layer.alert("world");
 *  	以上代码并不会先弹出一个hello，点击之后再弹出一个world，而是只能看到一个弹出结果（因为是异步的，layer.alert的时候不会像js自带alert一样阻塞进程）
 *  	想要实现类似js自带的那种效果，需要这样写：
 *  		layer.alert("hello", function(){
 *  			layer.alert("world");
 *  		});
 *  	每个方法后面基本上都提供的有回调函数,具体参考文档
 *  
 *  
 *  
 *  ***	 layui跟layer的问题描述
 *  		layui是完整的前台提供，其中弹出层模块即layer，layer可以单独使用，layui提供的有其他的功能模块，如选项卡...等操作
 *  
 *  	*问题如下：
 *  			之前使用的是layer的单独模块，弹出层没有什么问题，但是现在部分地方用到了选项卡操作，需要引入完整的layui，这样弹出层使用的
 *  		是layui提供的，但是使用layui，再使用原来的弹出方式会有bug（具体是layui的bug还是使用方式不对，现在没有测试），点击关闭按钮
 *  		之后弹出的div并没有消失。
 *  
 * 		*当前解决方式：
 * 				layui本身也是提倡开发者使用模块化加载方式的（具体可以参照文档），即用到哪个模块时候在js中定义使用该模块，因此，这里就采用
 * 			了这种方式，我用到了选项卡操作，而选项卡需要依赖 element 组件，因此需要在使用的界面中加载该模块，这样不需要的模块就不会被加载进来，
 * 			降低资源性能消耗。
 * 				layui.use('element', function(){
					var element = layui.element;
				});
			上面代码即在该界面中加载了element模块（组件）
 * 			
 */


/*window.alert = function(msg) {
	layer.alert(msg);
};*/

/**
 * 确认弹出框
 * @param msg 提示内容
 * @param fnY 确定函数（调用时候可以直接使用匿名函数）
 * @param fnN 取消的操作（如果没有可以不写，默认是直接返回false）
 */
function confirmDo(msg, fnY, fnN) {
	if (typeof fnY != 'function') {
		fnY = function(){
			return false;
		};
	}
	if (typeof fnN != 'function') {
		fnN = function(){
			return false;
		};
	}
	layer.confirm(msg, {}, function(index){
		fnY();
		layer.close(index);
	}, function(index){
		fnN();
		layer.close(index);
	});
}

/**
 * 弹出询问框，确定就跳转
 * @param msg 展示内容
 * @param location 跳转路径
 * @param windowObj 在哪个窗口跳转，可以不写该参数，默认就是window   比如要在父窗口打开（适用于iframe） window.parent
 */
function confirmGo(msg, location, windowObj) {
	layer.confirm(msg, {
		btn: ['确定','取消']
	},function(){
		if (windowObj) {
			windowObj.location.href = location;
		}else {
			window.location.href = location;
		}
	},function(){
		return;
	});
}

/**
 * 固定内容（Div）弹出层
 * @param objid 带展示对象id
 * @param width 宽度，如果使用像素需要带上px  如：200px、20%
 * @param height 高度，同宽度
 * @param title 标题，没有就不展示
 */
function showBindLayer(objid, width, height, title){
	if (isBlank(width)) {
		width = "auto";
	}
	if (isBlank(height)) {
		height = "auto";
	}
	if (isBlank(height)) {
		title = false;
	}
    layer.open({
    	type: 1,
    	title: title,
    	area:[width,height],
        closeBtn: 1,
        content:$("#"+objid)
    });
}

/**
 * iframe层弹出
 * @param title	标题
 * @param width	宽度，如果使用像素需要带上px  如：200px、20%
 * @param height 高度，同宽度
 * @param url 展示url地址
 */
function showFrameLayer(title, width, height, url) {
	if (isBlank(width)) {
		width = "auto";
	}
	if (isBlank(height)) {
		height = "auto";
	}
	layer.open({
		type: 2,
		title: title,
		shadeClose: true,
		shade: 0.8,
		area: [width,height],
		content: url
	});
}

