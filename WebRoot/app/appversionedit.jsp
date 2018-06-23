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
		<title>APP版本编辑</title>
		<%@ include file="../include/meta.inc" %>
		<script type="text/javascript">
		var isEdit = false;
		$(function(){
			isEdit = '${appVersionBean.id}' == null || '${appVersionBean.id}' == '' ? false : true;
		});
	  	//保存
	  	function checkInput(){
	  		
	  		$("#insert").attr("disabled", true);
	  		//1.校验表单
			var err = checkForm(document.forms["appversionForm"]);
			if(!err){
				$("#insert").attr("disabled", false);
				return false;
			}
			//2.验证APK包
			if(!checkApk()){
				$("#insert").attr("disabled", false);
				return false;
			}
			
			//3.判断是否已经检测版本号
			var id  = $("#appVersionId").val();
			var fl = $("#myFile").val();
			if (!(!isBlank(id) && isBlank(fl))) {
				if ($("#isCheckedVersion").val() != "0") {
					$("#insert").attr("disabled", false);
					layer.alert("请先检测版本号！");
					return false;
				}
			}
			
			ajax_submit();
		
		}
	  	
	  	//校验apk
	  	function checkApk(){
	  		var appVersionId = $("#appVersionId").val();
			var apkname = $("#myFile").val();
			if((appVersionId == "" || appVersionId == null) && (apkname == null || apkname == "")){
				layer.alert("请选择APK！");
				return false;
			}
	  		return true;
	  	}
	  	
	 	//校验应用版本
		function checkAppVersion(){
			var appVersionId = $("#appVersionId").val();
			var appId = $("#appId").val();
			var version = $("#version").val();
			var re = false;
			$.ajax({
		   		type: "POST",
		   		url: "appversion_checkAppVersion.action",
		   		dataType:"json",
		   		async : false,
		   		data:"appVersionBean.appid="+appId+"&appVersionBean.id="+appVersionId+"&appVersionBean.version="+version,
				success: function(data){
					if(data.statusCode == "0000"){
						re = true;
					}else{
						layer.alert(data.msg);
						re = false;
					}
				}
			});
			return re;
		}
		
		/**
		 * 检测版本：
		 	 新加界面，自动检测版本并赋值到input框
		 	 编辑界面，检测保证版本跟当前版本一致
		 */
		function detectVersion() {
			var obj = $("#myFile").val();
			if (isBlank(obj)) {
				layer.alert("请先选择APK文件！");
				return false;
			}
			
			var url = "appversion_detectVersion.action";
			var load_index = layer.load();
			$("#appversionForm").ajaxSubmit({
	       		type: "post", 
	            url: url,
	            enctype:"multipart/form-data",
	            async: true,
	            dataType: "json",
	            success: function(data) {
	            	layer.close(load_index);
		            if (data.statusCode == '0000') {
		            	if (!isEdit) {
							$("#version").val(data.arg1);
							$("#versioncode").val(data.arg4);
						}
						$("#isCheckedVersion").val("0");
						$("#tmpApkFilename").val(data.arg2);
						$("#apksize").val(data.arg3);
		          	}else{
		          		var obj = document.getElementById('myFile') ;
						obj.outerHTML=obj.outerHTML;
		          		layer.alert(data.msg);
		          		return false; 
		          	}
	        	},
	        	error: function(){
	        		layer.close(load_index);
	        	}
	      	});
			
		}
	  	
	  	//ajax提交
		function ajax_submit(){
			var appVersionId = $("#appVersionId").val();
			var url = "";
			if(appVersionId != null && appVersionId !=""){
				url = "appversion_update.action";
			}else{
				url = "appversion_save.action";
			}

			//显示等待层
			showWaitDiv(true);
			
			//为了防止再次上传文件，清空文件框内容	--兼容IE和FF
			/* var file = $("#myFile");	 
			file.after(file.clone().val("")); 
			file.remove(); */ 
			
			$("#appversionForm").ajaxSubmit({
	       		type: "post", 
	            url: url,
	            enctype:"multipart/form-data",
	            async: true,
	            dataType: "json",
	            success: function(data) {
		            if (data.statusCode == '0000') {
		            	layer.alert("保存成功",function(){
		            		toVersionList();
		            	});
		          	}else{
		          		layer.alert(data.msg);
		          		return false
		          	}
		            showWaitDiv(false);
	        	}
	      	});
		}
			
	  	function toVersionList(){
	  		var paraValue = "appid~=~${appVersionBean.appid}";
			var paramName = "appid~${appVersionBean.appid}~appid";
			src = "../app/appversionlist.action?queryparamter=%26%26"+paraValue+"&queryparamtername="+paramName;
			window.location = src;
	  	}
	  	
		//保存中DIV层
		function showWaitDiv(boo){
			if(boo){
				$("#waitdiv").show();
				$("#savediv").hide();
			}else{
				$("#waitdiv").hide();
				$("#savediv").show();
			}
		}
		
			

		//初始化
		$(function(){ 
			$("#versiondesc").html('${appVersionBean.versiondesc}');	//应用简介
		});
  	</script>
	</head>
<body style="background: white;">
<section id="contentMain" style="padding: 10px;margin: 0px;border: none;">
		
        <div class="contentDiv mainContent">
        	<form action="" name="appversionForm" method="post" id="appversionForm" class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
             
              	<input type="hidden" name="appVersionBean.id" id="appVersionId" value="${appVersionBean.id}"/>
              	<input type="hidden" name="appVersionBean.appid" id="appId" value="${appVersionBean.appid}"/>
              	<input type="hidden" name="tmpApkFilename" id="tmpApkFilename" value=""/>
              	<!-- 是否检测过版本号 -->
              	<input type="hidden" id="isCheckedVersion" value="1" />
              	
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="itemTitle">安卓版本编辑</li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required"></span>APK上传：</label>
			            <div class="labelRight">
			            	<input type="file" name="myFile" class="inputText" id="myFile" style="width: 377px;height: 30px"
															title="APK" alt="notnull;" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>版本号：</label>
			            <div class="labelRight">
			            
			            <input type="text" name="appVersionBean.version" id="version" value="${appVersionBean.version}"
								class="inputText readonly" title="版本号" alt=""  maxlength="32" size="30" readonly="readonly" />
			            
						<input type="button" class="defaultBtn primaryBtn" onclick="detectVersion()" value="检测版本" />
		               <div class="errorMsg"></div>
		              
		               </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>versioncode：</label>
			            <div class="labelRight">
			            
			            <input type="text" name="appVersionBean.versioncode" id="versioncode" value="${appVersionBean.versioncode}"
								class="inputText readonly" title="版本versioncode" alt=""  maxlength="32" size="30" readonly="readonly" />
			            
		               <div class="errorMsg"></div>
		              
		               </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>安装包大小：</label>
			            <div class="labelRight">
			            <input type="text" name="appVersionBean.apksize" id="apksize" value="${appVersionBean.apksize}"
								class="inputText readonly" title="安装包大小" alt="notnull;"  maxlength="12" size="30" readonly="readonly" />
						
			            
		               <div class="errorMsg"></div>
		              
		               </div>
			        </li>
			         <li class="clearfix" style="height: auto;">
				            <label class="labelLeft"><span class="required">*</span>版本说明：</label>
				            <div class="labelRight">
				            	<textarea id="versiondesc" name="appVersionBean.versiondesc" title="版本说明" alt="notnull;" class="areaText"></textarea>
				            	<div class="errorMsg"></div>
				            </div>
				     </li>
			        <!-- 操作按钮 -->
			        <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight" id="savediv">
			                <input type="button" id="insert" class="defaultBtn" onclick="checkInput()" value="保存" />
			                <input type="button" class="defaultBtn backBtn" onclick="history.back(-1)" value="返回" />
			            </div>
			             <div id="waitdiv" style="display: none">
							<img class="checkwar" style="width: 13px;height: 13px;" src="../images/wait.gif" />
							正在保存应用信息,请稍等...
						</div>
			        </li>
			    </ul>
			</form>
		</div>    
	</section>	
</body>
</html>

