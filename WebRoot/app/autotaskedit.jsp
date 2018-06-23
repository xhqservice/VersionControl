<%@page import="com.jadlsoft.utils.DateUtils"%>
<%@page import="com.jadlsoft.utils.SystemConstants"%>
<%@page import="com.jadlsoft.utils.MBConstant"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>定时升级任务编辑</title>
		<%@ include file="../include/include.jsp" %>
		<script type="text/javascript">
	
	  	//保存
	  	function checkInput(){
	  		
	  		$("#insert").attr("disabled", true);
	  		//1.校验表单
			var err = checkForm(document.forms["autotaskForm"]);
			if(!err){
				$("#insert").attr("disabled", false);
				return false;
			}
			
			
			toSubmit();
			
		}
	  	
	  	function toSubmit(){
	  		var autoTaskId = $("#autoTaskId").val();
			
			if(autoTaskId == null || autoTaskId == ""){
				document.forms["autotaskForm"].action = "../app/autotask_save.action";
			}else{
				
				document.forms["autotaskForm"].action="../app/autotask_update.action";
			}
			document.forms["autotaskForm"].submit();
	  	}
	  
		//选择APP应用
		function selectApp() {
			var conditions = "";
			openSeachWindow("applist", conditions, "appid,appname,version", "appid,appname,version","", "");
		}
		
		//选择版本
		function selectVersion() {
			var appid = $("#appid").val();
			if(appid == null || appid == ''){
				layer.alert("请先选择应用！");
				return false;
			}
			var conditions = "appid@equals@"+appid;  
			openSeachWindow("appversion", conditions, "version", "version","", "");
		}
		
		
  	</script>
	</head>
<body >

	<section id="contentMain">
		<h2 class="infoTitle">定时任务编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="autotaskForm" method="post" id="autotaskForm" class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
             
              	<input type="hidden" name="autotaskBean.id" id="autoTaskId" value="${autotaskBean.id}"/>
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="itemTitle">基本信息编辑</li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>APP：</label>
			             <div class="labelRight">
			            	<input type="text" name="autotaskBean.appname" title="APP"  alt="notnull;" readonly="readonly" id="appname" value="${autotaskBean.appname}"
									class="inputText readonly"  onclick="selectApp();" />
								<input type="hidden" name="autotaskBean.appid" title="APP"  alt="notnull;" id="appid" value="${autotaskBean.appid}" />

		               <div class="errorMsg"></div>
		               </div>
		              
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>版本：</label>
			            <div class="labelRight">
			            	<input type="text" name="autotaskBean.version" readonly="readonly" title="版本" alt="notnull" id="version" value="${autotaskBean.version}" class="inputText readonly"  onclick="selectVersion();" />
							
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>执行升级时间：</label>
			            <div class="labelRight">
			            		<input style="float: none;" alt="notnull" title="执行升级时间"  id="todotime" class="inputText" 
	               					name="autotaskBean.todotime" type="text" value="${autotaskBean.todotime}" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'});" />
			            	<div class="errorMsg"></div>
			            </div>
			        </li>
			      
			       
			      
			        <!-- 操作按钮 -->
			        <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight">
			                <input type="button" id="insert" class="defaultBtn" onclick="checkInput()" value="保存" />
			                <input type="button" class="defaultBtn backBtn" onclick="window.location='../app/autotasklist.action'" value="返回" />
			            </div>
			        </li>
			    </ul>
			</form>
		</div>    
	</section>	
</body>
</html>

