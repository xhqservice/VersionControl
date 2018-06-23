<%@page import="com.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
 <!-- 点击运行日志弹出层 -->
<div id="logDiv" class="log_div" style="display: none">
	
</div>

<script type="text/javascript" >

/**
 * 模板
 */ 
//日志table 头部
var template_log_tablehead = "<table id='tableId' class='api_table' border='0' cellpadding='0' cellspacing='0' "
									+"style='width:90%;'>"
								+"<colgroup style='width: 40%'/>"
								+"<colgroup style='width: 30%'/>"
								+"<colgroup style='width: 30%'/>"
								+"<tr class='title'><td>文件名</td>"
								+"<td>文件大小</td><td>操作</td>"
								+"</tr>";
//日志table的每一个tr
var template_log_tabletr = '<tr><td>{@logname}</td>'
							+'<td>{@logsize}</td>'
							+'<td><a href="javascript:void(0)" '
							+	'onclick="showLog(&quot;{@logname}&quot;,&quot;{@download_Ip}&quot;)">'
							+	'查看</a><a id="" href="javascript:void(0)" '
							+	'onclick="downloadLog(&quot;{@logname}&quot;,&quot;{@download_Ip}:{@dk}&quot;)">'
							+	'下载</a></td></tr>';
 

//查看日志列表
function tomcatLogs(ptlx,fwqip_ww,fwqip,dk){

	showBindLayer("logDiv", "60%", "80%", "Tomcat日志列表");

    var socket_Ip = fwqip;
    var download_Ip = fwqip_ww;
	if(ptlx != '<%=SystemConstants.PTLX_YUN%>'){
		socket_Ip = fwqip_ww;
	}
	var html;
	// 先清空之前的内容
	var thisTable = document.getElementById("tableId");
	if (thisTable != null) {
		thisTable.parentNode.removeChild(thisTable);	
	}
	var load_index = layer.load();
	//异步发出请求，获得日志文件列表
	$.ajax({
		type: "POST",
   		url: "../fwqgl/fwqgetTomcatLogs.action",
   		dataType:"json",
   		data:"fwqip="+socket_Ip,
		success: function(data){
			layer.close(load_index);
			// 将值拼成HTML显示到页面中
			if (data != null && data.length>0) {
				//循环之前的HTML代码
				var html_table = template_log_tablehead;
				//循环生成的HTML代码
				var html_tr = "";
				for(var i=0;i<data.length;i++){
					var logName = data[i][0];	// 日志文件的名字
					var logsize = data[i][1];	//日志文件大小
					html_tr = html_tr.concat(template_log_tabletr
												.replace(/{@logname}/g, logName)
												.replace(/{@logsize}/g, logsize)
												.replace(/{@download_Ip}/g, download_Ip)
												.replace(/{@dk}/g, dk));
					
				}
			
				html = html_table+html_tr+"</table>";
			}else {
				// 说明没有日志
				html = "<table id='tableId'><tr><td>未找到相关日志，可能主机未开启或者主机的日志设置没有打开</td></tr></table>";
			}

			$("#logDiv").append(html);
		},
		error : function(a,b) {
			layer.close(load_index);
			// 说明没有日志
			html = "<table id='tableId'><tr><td>未找到相关日志，可能主机未开启或者主机的日志设置没有打开</td></tr></table>";
			$("#logDiv").append(html);
		}
	});
}
		
//查看日志
function showLog(logName,fwqdz){
	// 根据主机ip和日志的名称
	// 打开一个新的窗口，在新窗口中直接显示日志的内容
	
	//var url = "http://"+fwqdz+"/fwzxutils/fileDeal.do?methodName=showTomcatLog&logName="+encodeURI(logName);
	//window.open(url);
	var fwqip;
	if (fwqdz.indexOf(":") == -1) {
		fwqip = fwqdz;
	}else {
		fwqip = fwqdz.split(":")[0];
	}
	window.open("../fwqgl/fwqtoshowLogOnline.action?logname="+logName+"&fwqip="+fwqip);
}

function downloadLog(logName, fwqdz) {
	$.ajax({
		url:"http://"+fwqdz+"/fwzxutils/fileDeal.do?methodName=isTomcatLogExist&logName="+logName,
		type:"get",
		dataType:"jsonp",	//浏览器的跨域请求问题，使用jsonp解决
		success:function(data){
			if (data.isFileExist == "error") {
				layer.alert("日志不存在,可能已被删除");
			}else if (data.isFileExist == "yes") {
				location.href = "http://"+fwqdz+"/fwzxutils/fileDeal.do?methodName=downTomcatLog&logName="+logName;
			}
		},
		error: function(a,b){
			alert(a);
			alert(b);
		}
	});
}

//查看应用日志
function showFwqYyLog(fwqip, dk, warname) {
	var appname = warname.substring(0,warname.length-4);
	var url = "../yygl/yyisYyLogExist.action";
	//发送异步请求获取文件是否存在
	$.ajax({
		type: "POST",
   		url: url,
   		data:"appname="+appname+"&ip="+fwqip,
   		dataType:"json",
		success: function(resultBean){
			if(resultBean.statusCode == "0000"){
				//说明存在，直接访问
				var u = "http://"+fwqip+":"+dk+"/fwzxutils/fileDeal.do?methodName=showAppLog&yyLogPath="+encodeURI(resultBean.arg1);
				window.open(u);
			}else{
				layer.alert(resultBean.msg);
				return false;
			}
		}
	});
}
		
</script>