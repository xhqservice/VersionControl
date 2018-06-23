<%@page import="com.jadlsoft.struts.action.UserUtils"%>
<%@page import="com.jadlsoft.model.xtgl.UserSessionBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/meta.inc" %>
<%
	UserSessionBean uB = (UserSessionBean) request.getSession().getAttribute(UserUtils.USER_SESSION);
 %>

<body>
	<header class="clearfix">
	    <a href="http://www.mbfwyun.com/" class="centerLogo"
	    	target="_blank" style="position:relative;border: none;">
	    	<img style="position:relative; left:-5px;" src="../images/logo.png" alt="聚合数据">
	    </a>
	    <div class="centerSearch">
	        <input class="searchText" name="q" type="text" placeholder="搜索">
	        <button id="searchBtn" type="button" class="searchBtn centerIcon"></button>
	    </div>
	    <ul class="headerUl clearfix">
	        <li class="headerLi1">
	            <div class="headerInfo"><span><%= uB.getUserName() %></span><i class="headerIcon1 centerIcon"></i></div>
	           	<a class="centerLogout" href="javascript:void(0);" onclick="logout()">退出登录</a>
	        </li>
	        <!-- <li>正则
	            <a href="http://www.mbfwyun.com/" target="_blank">常见问题</a>
	        </li> -->
	        <li class="headerLi3">
	            <div class="headerFoucs"><span>关注丹灵云</span><i class="headerIcon1 centerIcon"></i></div>
	            <div class="logoCo">
	            	<img src="../images/danlingyun_ewm.png" width="90px;" height="90px;" />
	            </div>
	        </li>
	        <li>
	            <a class="headerContact" href="http://www.mbfwyun.com/" target="_blank">联系我们</a>
	        </li>
	    </ul>
	</header>
	<script type="text/javascript">
		$(document).ready(function(){
	        $('.headerLi1').mouseenter(function(){
				$('.centerLogout').slideDown();
			}).mouseleave(function(){
				$('.centerLogout').stop(true).slideUp();
			});
			
			$('.headerLi3').mouseenter(function(){
				$('.logoCo').slideDown();
			}).mouseleave(function(){
				$('.logoCo').stop(true).slideUp();
			});
	    })
	    function logout() {
	    	confirmGo("您确定要退出系统吗？", "../dologinloginout.action");
	    }
	    
	</script>
</body>
</html>