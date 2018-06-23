
<%@page import="com.jadlsoft.utils.SystemConstants"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>APP版本信息</title>
	<%@ include file="../include/include.jsp"%>
	<script type="text/javascript">
		function reloadFrame(frameId) {
			var src = "";
			if(frameId == 'AndroidApp'){
				var paraValue = "appid~=~${appJbxxBean.appid}";
				var paramName = "appid~${appJbxxBean.appid}~appid;";
				src = "../app/appversionlist.action?queryparamter=%26%26"+paraValue+"&queryparamtername="+paramName;
			}else if(frameId == 'IosApp'){
				src = "../app/appversion_edit_ios.action?appVersionBean.appid=${appJbxxBean.appid}";
			}
			$("#"+frameId).attr("src", src);
		}
		
		//初始化
		$(function(){
			$("#li_AndroidApp").click();
			
		});
	</script>
</head>
<body>
	
	<section id="contentMain">
		<div class="topDiv">
			
			<div class="topRight">
				<ul class="formUl" style="padding-top: 0px;padding-bottom: 0px;">
					<li class="itemTitle" style="padding: 0px">版本维护</li>
			        
			        <li class="clearfix" style="height: auto;padding: 0px">
						 <label class="labelLeft">APPID：</label>
			            	<div class="labelRight">
			            	${appJbxxBean.appid}
			            
			           	 </div>
						<label class="labelLeft" style="padding-left: 100px">APP名称：</label>
			            <div class="labelRight">
			            	${appJbxxBean.appname}
			            </div>
			            <div class="labelRight" style="padding-left: 50px">
			           		<input type="button" class="defaultBtn backBtn" onclick="window.location='../app/applist.action'" value="返回APP" />
			            </div>
			            
			          </li>
			          
			</ul>
				
			</div>
		</div>
		
		
		<div class="contentDiv mainContent" style="border: 0px">
			
			<div class="layui-tab layui-tab-card" style="margin-top: 3px;">
				<ul class="layui-tab-title">
				    <li onclick="reloadFrame('AndroidApp')" id="li_AndroidApp" style="font-weight: bold;">安卓版本</li>
				    <li onclick="reloadFrame('IosApp')" id="li_IosApp" style="font-weight: bold;">IOS版本</li>
				</ul>
				<div class="layui-tab-content" style="height: 700px;">
				    <div class="layui-tab-item layui-show" style="height: 100%;">
						<iframe frameborder="0" id="AndroidApp" style="width: 100%;height: 100%;border: none;" src=""></iframe>
				    </div>
				    <div class="layui-tab-item" style="height: 100%;">
				    	<iframe frameborder="0" id="IosApp" style="width: 100%;height: 100%;border: none;" src=""></iframe>
				    </div>
				</div>
			</div>
		</div>
</body>
<script type="text/javascript">
	layui.use('element', function(){
		var element = layui.element;
	});
</script>
</html>