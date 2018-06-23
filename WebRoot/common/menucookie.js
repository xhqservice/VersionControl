function getCookie(){
	return myCookie.get("gncode");
}
function addCookie(code){
	myCookie.add("gncode",code,1);
}

function delCookie(){
	myCookie.del("gncode");
}

/*
	@ cookie存取与调用
*/
var myCookie={
	add:function(objName,objValue,objHours){
		if(objHours==undefined){ objHours=0; }
		var str = objName + "=" + escape(objValue);
		if(objHours > 0){
			var date = new Date();
			var ms = objHours*3600*1000;
			date.setTime(date.getTime() + ms);
			str += "; expires=" + date.toGMTString();
		}
		document.cookie = str+";path=/";
	},
	get:function(objName){
		var arrStr = document.cookie.split("; ");
		for(var i = 0;i < arrStr.length;i ++){
			var temp = arrStr[i].split("=");
			if(temp[0] == objName) return unescape(temp[1]);
		}
	},
	del:function(objName){
		var date = new Date();
		date.setTime(date.getTime() - 10000);
		document.cookie = objName + "=a; path=/; expires=" + date.toGMTString();
	},
	allName:function(){
		var str = document.cookie;
		if(str == ""){
			str = "没有保存任何cookie";
		}
	}
}





