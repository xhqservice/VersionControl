<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- <%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>权限信息查看</title>
		<%@ include file="../include/include.jsp"%>
		<style type="text/css">
		    .permiss {
		    	margin: 0 auto;
		    	padding-bottom: 10px;
		    }
		    .permiss li {
		    	list-style:none;
		    	line-height:16px;
		    	font-size: 12px;
		    	color: #3399FF;
		    	height: auto;
		    	padding: 5px;
		    }
		</style>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">权限信息详情</h2>
        <div class="contentDiv mainContent">
        	<form action="" 
              		method="post" id="" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	
				<ul class="formUl" style="padding-top: 0;">
					<li class="clearfix" style="height: auto;">
			            <label class="labelLeft">权限id：</label>
			            <div class="labelRight">
			            	${permiss.permissid}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">权限名：</label>
			            <div class="labelRight">
			            	${permiss.permissname}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">权限功能菜单列表：</label>
			            <div class="labelRight">
			            	<c:if test="${not empty permiss.gnlbs}">
								<div class="permiss">
			  						<ul>
			  							<c:forEach var="gnlb" items="${permiss.gnlbs}">
											<c:if test="${fn:endsWith(gnlb.gncode, '0000')}">
												<li><a style="color: red;" href="#" onclick="window.location='gnlbview.action?gid='+'${gnlb.gncode}';"><b>${gnlb.gnname}</b></a></li>
											</c:if>
											<c:if test="${!fn:endsWith(gnlb.gncode, '0000')}">
												<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: #FE9900;" href="#" onclick="window.location='gnlbview.action?gid='+'${gnlb.gncode}';">${gnlb.gnname}</a></li>
											</c:if>
										</c:forEach>
			  						</ul>
			  					</div>
							</c:if>
							<c:if test="${empty permiss.gnlbs}">
								该权限没有任何功能菜单项
							</c:if>
			            </div>
			        </li>
			    </ul>
			</form>
			<div style="margin-left: 60px;margin-top: 10px;clear: both;">
				<input type="button" class="defaultBtn" value="编辑"
					onclick="window.location='permissedit.action?pid='+'${permiss.permissid}';" />
				<input type="button" class="defaultBtn backBtn" value="返回"
					onclick="history.back(-1)" />
			</div>
		</div>    
	</section>
</body>
</html>

