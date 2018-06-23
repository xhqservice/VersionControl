<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>用户信息查看</title>
		<%@ include file="../include/include.jsp"%>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">用户信息详情</h2>
        <div class="contentDiv mainContent">
        	<form action="" 
              		method="post" id="" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	
				<ul class="formUl" style="padding-top: 0;">
					<li class="clearfix" style="height: auto;">
			            <label class="labelLeft">用户id：</label>
			            <div class="labelRight">
			            	${userBean.userid}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">用户名：</label>
			            <div class="labelRight">
			            	${userBean.username}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">密码：</label>
			            <div class="labelRight">
			            	${userBean.password}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">所属单位：</label>
			            <div class="labelRight">
			            	${userBean.qydm}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">行政区划：</label>
			            <div class="labelRight">
			            	${userBean.xzqh}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">录入时间：</label>
			            <div class="labelRight">
			            	${userBean.lrsj}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">联系人：</label>
			            <div class="labelRight">
			            	${userBean.lxr}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">联系人联系电话：</label>
			            <div class="labelRight">
			            	${userBean.lxrlxdh}
			            </div>
			        </li>
			        <li class="clearfix">
			            <label class="labelLeft">用户角色：</label>
			            <div class="labelRight">
			            	<a href="javascript:void(0)" style="color: #FE9900" 
			            		onclick="window.location='roleview.action?rid='+${userBean.roleid};">${userBean.rolename}</a>
			            </div>
			        </li>
			        
			    </ul>
			</form>
			<div style="margin-left: 60px;margin-top: 10px;">
				<input type="button" class="defaultBtn" value="编辑"
					onclick="window.location='useredit.action?uid='+'${userBean.id}';" />
				<input type="button" class="defaultBtn backBtn" value="返回"
					onclick="history.back(-1)" />
			</div>
		</div>    
	</section>
</body>
</html>

