<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>功能菜单查看</title>
		<%@ include file="../include/include.jsp"%>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">菜单信息详情</h2>
        <div class="contentDiv mainContent">
        	<form action="" 
              		method="post" id="" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	
				<ul class="formUl" style="padding-top: 0;">
					<li class="clearfix" style="height: auto;">
			            <label class="labelLeft">功能代码：</label>
			            <div class="labelRight">
			            	${gnlbBean.gncode}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">功能名称：</label>
			            <div class="labelRight">
			            	${gnlbBean.gnname}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">链接：</label>
			            <div class="labelRight">
			            	${gnlbBean.link}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">顺序号：</label>
			            <div class="labelRight">
			            	${gnlbBean.sort}
			            </div>
			        </li>
			    </ul>
			</form>
			<div style="margin-left: 60px;margin-top: 10px;">
				<input type="button" class="defaultBtn" value="编辑"
					onclick="window.location='gnlbedit.action?gid='+'${gnlbBean.gncode}';" />
				<input type="button" class="defaultBtn backBtn" value="返回"
					onclick="history.back(-1)" />
			</div>
		</div>    
	</section>
</body>
</html>

