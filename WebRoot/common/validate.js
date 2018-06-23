
/** ===============================================================
 *
 * 此JS包含FORM表单的验证。HTML中只需引入此JS即可。
 * 此JS包含STRING的两个方法：
 * 1、计算实际长度的方法（汉字算两个字符）
 * 2、删除字符串两头空格的方法
 *
 * =================================================================*/

var x_flag = "\n";
function setFlag(f) {
	x_flag = f;
}
function alertErrorMsg(errorMsg) {
	alert(errorMsg);
}
// 检查form
function checkForm(f) {
	var res = true;
	var hasFocus = false;
	for (var elementIndex = 0; elementIndex < f.elements.length; elementIndex++) {
		var ele;
		try {
			ele = f.elements[elementIndex];
			var eleTagn = ele.tagName;
			var eleType = ele.type;
		
			if ((eleTagn == "INPUT"  
					&& (eleType == "text" || eleType == "password" 
							|| eleType == "checkbox" || eleType == "radio"))
				|| eleTagn == "TEXTAREA" || eleTagn == "SELECT") {
				/*检查CHECKBOX。CHECKBOX仅检查是否必须选择以及选择的项数*/
				if (eleType == "text" || eleType == "password" || eleType == "textarea" || eleType.startWith("select")) {
					if (ele.disabled == true) {
						//如果禁用的不校验
						continue;
					}
					var errMsg = checkElement(ele);
					
					if (errMsg != "") {
						res = false;
						//给后面的错误信息赋值
						var errObj = $(ele).nextAll(".errorMsg")[0];
						$(errObj).html(errMsg);
						if (!hasFocus) {
							$('html,body').animate({scrollTop:$(ele).offset().top-80}, 100); 
							$(ele).focus();
							hasFocus = true;
						}
					}else {
						var errObj = $(ele).nextAll(".errorMsg");
						$(errObj).html("");
					}
				}
			}
		}
		catch (e) {
		}
	}
	return res;
}
function checkNull(checkType, checkValue, titleValue) {
	
	var pat = /notnull/g;
	if (pat.test(checkType) && checkValue.trim() == "") {
		return titleValue + "\u4e0d\u80fd\u4e3a\u7a7a\uff0c\u8bf7\u8f93\u5165\u3002" + x_flag;
	}
	return "";
}
function checkInputLength(checkType, checkValue, titleValue) {
	
	if (!checkType || checkType == "") {
		return "";
	}
	
	var checkArr = checkType.split(";");
	if (!checkArr || checkArr.length == 0) {
		return "";
	}
	
	for(var i=0;i<checkArr.length;i++) {
		var pat = /length(<=|=|>=)(\d+)/g;
		if (!pat.test(checkArr[i]) || checkValue == "") {
			continue;
		}
		var opt = RegExp.$1;
		var val = parseInt(RegExp.$2, 10);
		var len = checkValue.length2();
		var l = ((val % 2) == 1) ? ((val - 1) / 2) : val / 2;
		var m = "\u3002";
		if (l != 0) {
			m = "\u6216" + l + "\u4f4d\u6c49\u5b57\u3002";
		}
		if (opt == "<=" && len > val) {
			return titleValue + "\u6709\u8bef\u3002\u8bf7\u8f93\u5165\u4e0d\u8d85\u8fc7" + val + "\u4f4d\u5b57\u7b26" + m + x_flag;
		} else {
			if (opt == "=" && len != val) {
				return titleValue + "\u6709\u8bef\u3002\u8bf7\u8f93\u5165" + val + "\u4f4d\u5b57\u7b26" + m + x_flag;
			}else {
				if (opt == ">=" && len < val) {
					return titleValue + "\u6709\u8BEF\u3002\u8BF7\u8F93\u5165\u4E0D\u5C11\u4E8E" + val + "\u4f4d\u5b57\u7b26" + m + x_flag;
				}
			}
		}
	}
	
	return "";
}
function checkElement(field) {
	var titleValue = field.title;

	if (!titleValue) {
		return "";
	}
	var checkValue = field.value;
	//var checkType = field.alt;	--非IE下，select拿不到alt属性
	var checkType = field.getAttribute("alt");
	
	if (!checkType) {
		return "";
	}
	var errorMsg = "";
	/*检查空值。通过notnull标识*/
	errorMsg += checkNull(checkType, checkValue, titleValue);
	/*检查输入项的长度，汉字的长度为2。通过length<=标识*/
	errorMsg += checkInputLength(checkType, checkValue, titleValue);
	return errorMsg;
}
