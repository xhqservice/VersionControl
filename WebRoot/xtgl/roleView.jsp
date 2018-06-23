<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>角色信息查看</title>
		<%@ include file="../include/include.jsp"%>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">角色信息详情</h2>
        <div class="contentDiv mainContent">
        	<form action="" 
              		method="post" id="" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	
				<ul class="formUl" style="padding-top: 0;">
					<li class="clearfix" style="height: auto;">
			            <label class="labelLeft">角色id：</label>
			            <div class="labelRight">
			            	${role.roleid}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">角色名：</label>
			            <div class="labelRight">
			            	${role.rolename}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">角色权限列表：</label>
			            <div class="labelRight">
			            	<c:if test="${not empty role.permisses}">
			            		<table class="api_table" border="0" cellpadding="0" cellspacing="0">
			            			<c:forEach var="permiss" items="${role.permisses}">
			            				<tr>
			            					<td><a style="color: #FE9900;" href="javascript:void(0)" 
			            						onclick="window.location='permissview.action?pid='+${permiss.permissid};">${permiss.permissname}</a></td>
			            				</tr>
			            			</c:forEach>
 			            		</table>
							</c:if>
							<c:if test="${empty role.permisses}">
								该角色没有任何权限
							</c:if>
			            </div>
			        </li>
			    </ul>
			</form>
			<div style="margin-left: 60px;margin-top: 10px;">
				<input type="button" class="defaultBtn" value="编辑"
					onclick="window.location='roleedit.action?rid='+'${role.roleid}'" />
				<input type="button" class="defaultBtn backBtn" value="返回"
					onclick="history.back(-1)" />
			</div>
		</div>    
	</section>
</body>
</html>

