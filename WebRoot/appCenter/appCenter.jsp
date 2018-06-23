<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>丹灵云APP商店</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/app/appCenter.css" />
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/common/commonUtil.js" type="text/javascript" charset="utf-8"></script>
<script src="${pageContext.request.contextPath}/js/json.parse.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">

	var consoleType = getAPPConsoleType();

	$(function(){
		setTimeout(function(){
			
			var paraObj = '{"qqlx": "appIndex","ssxm": "${ssxm}","apptype": '+consoleType+'}';
			
			para = encodeURI(paraObj);
			
			$.ajax({
				url: "${pageContext.request.contextPath}/appUpload.do",
				type: "post",
				dataType: "json",
				data: "para="+para+"&key=''",
				success: function(dataMap){
				
					if (dataMap.returntype != '0') {
						alert(dataMap.returnerror);
						return;
					}
				
					data = dataMap.returnmsg;
					if (data == null || data.length==0) {
						alert("当前没有可下载应用！");
						return;
					}
					
					for(var i=0;i<data.length;i++) {
						
						var iconsrc = data[i]["iconsrc"];
						iconsrc = iconsrc == null || iconsrc == "" ? "" : '${pageContext.request.contextPath}'+iconsrc;
						var apksrc = data[i]["apksrc"];
						var appname = data[i]["appname"];
						var appversion = data[i]["version"];
						var appsize = data[i]["apksize"];
						var appdesc = data[i]["appdesc"];
						
						apksrc = apksrc.replace(/\\/g, "\\\\");
						iconsrc = iconsrc.replace(/\\/g, "\\\\");
					
						var liHtml = tmp_item.replace(/{@apksrc}/g, apksrc)
											.replace(/{@iconsrc}/g, iconsrc)
											.replace(/{@appname}/g, appname)
											.replace(/{@appversion}/g, appversion)
											.replace(/{@appdesc}/g, appdesc);
					
						var sizespanHtml = "";
						if (consoleType == '0' && !isBlank(appsize)) {
							sizespanHtml = tmp_sizespan.replace(/{@appsize}/g, appsize);
						}
						liHtml = liHtml.replace(/{@sizeSpan}/g, sizespanHtml);
					
						var liObj = $(liHtml);
										
						$("#appListUl").append(liObj);
					}
				},
				error: function(a,b,c){
				}
			});
		}, 200);
	});
	
	function isWx() {
		var ua = window.navigator.userAgent.toLowerCase();
		if (ua.match(/MicroMessenger/i) == 'micromessenger') {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * APP下载
	 	区分安卓和IOS的
	 */
	function downloadApp(apksrc) {
		if (consoleType == '0') {
			if (isWx()) {
				window.location.href = "${pageContext.request.contextPath}/appCenter/tipforwx.jsp?ssxm=${ssxm}";
				return;
			}
			//android，需要从项目服务器获取下载信息
			window.location.href = "${pageContext.request.contextPath}"+apksrc;
		}else if (consoleType == '1') {
			//ios，跳转到苹果商店下载，直接跳转URL即可
			window.location.href = apksrc;
		}else {
			//不是安卓和IOS
			alert("抱歉，暂不支持该设备类型！");
			return;
		}
	}

	var tmp_sizespan = '<span class="line"></span>{@appsize}';

	var tmp_item = '<li class="cont-item"><div class="item-box"><div class="item-left">'
					+'<a href="javascript:void(0)"><img src="{@iconsrc}" alt=""></a></div>'
					+'<div class="item-body"><a href="javascript:void(0)" class="app-name">{@appname}</a>'
					+'<p class="app-info">{@appversion}{@sizeSpan}'
					+'<p class="app-remark">{@appdesc}</p></p></div>'
					+'<div class="item-right"><a href="javascript:void(0)" class="app-down" '
					+'ontouchstart="this.style.backgroundColor = \'blue\'" '
					+'onclick="downloadApp(\'{@apksrc}\')" '
					+'ontouchend="this.style.backgroundColor = \'rgba(0,122,255, 1)\';">'
					+'下载</a></div></div></li>';
			
</script>
</head>
<body>
	<div class="container">
        <h4 class="title">
            丹灵云APP商店
        </h4>
        <hr>
        <ul id="appListUl" class="cont-wrap">
        </ul>
    </div>
</body>
</html>