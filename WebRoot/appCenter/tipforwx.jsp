<%@page import="com.jadlwork.business.qrgl.IQRCodeManager"%>
<%@page import="com.jadlsoft.utils.SpringBeanFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>提示</title>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<meta name="format-detection" content="telephone=no"/>
<meta name="format-detection" content="email=no"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=0"/>
</head>
<style>
*,*:before,*:after {-webkit-tap-highlight-color:rgba(0,0,0,0);box-sizing:border-box}
*:focus {outline:none}
body,p,img,div,a,header {margin:0;padding:0}
body,html {min-height:100%}
a,img {-webkit-touch-callout:none}
body {color:#333;background:#f5f5f9;font-size:14px;font-family:"Helvetica Neue",Helvetica,STHeiTi,sans-serif}
header {text-align:center;margin-top:75px;height:96px}
header img {width:96px}
p {text-align:center;padding:20px 15px;font-size:16px;line-height:22px;color:#999}
.url{word-break:break-all}
</style>
<body>
<header>
	<img src="../images/tip.png" >
</header>
<%
	String ssxm = request.getParameter("ssxm");
	IQRCodeManager qrCodeManager = (IQRCodeManager)SpringBeanFactory.getBean("qrCodeManager");
	String shortkey = qrCodeManager.getQRCodeUrlBySsxm(ssxm);
	String s = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf(request.getRequestURI()));
	pageContext.setAttribute("shorturl", s + shortkey);
 %>
<p>请确保该链接来源安全，若需浏览，请长按网址复制后使用浏览器访问</p>
<p class="url">
	${shorturl}
</p>
</body>
</html>