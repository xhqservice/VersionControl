<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>系统环境下载</title>
		<%@ include file="../include/include.jsp"%>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">下载</h2>
		
		<!-- <pre class="layui-code" lay-title="测试">[23/Nov/2017:14:42:42 +0800] 192.168.60.1 - - "GET /test/ HTTP/1.1" 404 170 "-" "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0" "-"
			[17/Nov/2017:12:44:59 +0800] 192.168.60.129 - - "GET / HTTP/1.1" 200 612 "http://b6c466b5.ngrok.io/" "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36" "unknown, 171.13.14.146"
			[17/Nov/2017:12:44:59 +0800] 192.168.60.129 - - "GET / HTTP/1.1" 200 612 "-" "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36" "106.120.162.108"
			[17/Nov/2017:12:44:38 +0800] 192.168.60.129 - - "GET /commonTest HTTP/1.1" 404 0 "-" "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36" "101.199.108.120"
			[17/Nov/2017:12:44:31 +0800] 192.168.60.129 - - "GET /favicon.ico HTTP/1.1" 404 572 "http://b6c466b5.ngrok.io/" "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36" "1.119.129.17"
			[17/Nov/2017:12:44:29 +0800] 192.168.60.129 - - "GET / HTTP/1.1" 200 612 "-" "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36" "1.119.129.17"
			[17/Nov/2017:12:44:22 +0800] 192.168.60.129 - - "GET / HTTP/1.1" 200 612 "-" "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E)" "1.119.129.17"
			[17/Nov/2017:12:44:07 +0800] 192.168.60.129 - - "GET / HTTP/1.1" 200 612 "-" "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36" "101.199.108.50"
			[17/Nov/2017:12:44:04 +0800] 192.168.60.129 - - "GET /commonTest HTTP/1.1" 404 0 "http://b6c466b5.ngrok.io/commonTest" "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Tablet PC 2.0)" "106.120.161.68"
			[17/Nov/2017:12:43:49 +0800] 192.168.60.129 - - "GET / HTTP/1.1" 200 612 "http://b6c466b5.ngrok.io/" "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; Tablet PC 2.0)" "106.120.161.68"
		</pre>  -->     
		
		<!-- <div class="contentDiv mainContent">
				
		</div> -->
	</section>
</body>
<script type="text/javascript">
	layui.use('element', function(){
		var element = layui.element;
	});
	layui.use('code', function(){ //加载code模块
		layui.code({
			about:false
		}); //引用code方法
	});
	$(function(){
		var index = layer.load(1, {shade: false});
		window.setInterval(function(){
			layer.alert("别等了，还没开发呢！");
		}, 10*1000);
	});
</script>
</html>

