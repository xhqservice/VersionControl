<%@page import="com.jadlsoft.utils.DateUtils"%>
<%@page import="com.jadlsoft.utils.SystemConstants"%>

<%@ page language="java" contentType="text/html; charset=utf-8"%>

<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>IOS版本编辑</title>
		<%@ include file="../include/meta.inc" %>
		<script type="text/javascript">
	
	  	//保存
	  	function checkInput(){
	  		
	  		$("#insert").attr("disabled", true);
	  		//1.校验表单
			var err = checkForm(document.forms["appversionForm"]);
			if(!err){
				$("#insert").attr("disabled", false);
				return false;
			}

			ajax_submit();

		
		}
	 
	  	//ajax提交
		function ajax_submit(){
			var appVersionId = $("#appVersionId").val();
			var url = "";
			if(appVersionId != null && appVersionId !=""){
				url = "appversion_update_ios.action";
			}else{
				url = "appversion_save_ios.action";
			}

			$("#appversionForm").ajaxSubmit({
	       		type: "post", 
	            url: url,
	            enctype:"multipart/form-data",
	            async: true,
	            dataType: "json",
	            success: function(data) {
	            	layer.alert(data.msg);
	        	}
	      	});
			$("#insert").attr("disabled", false);
		}
		
  	</script>
	</head>
<body style="background: white;">
<section id="contentMain" style="padding: 10px;margin: 0px;border: none;">
		
        <div class="contentDiv mainContent">
        	<form action="" name="appversionForm" method="post" id="appversionForm" class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
             
              	<input type="hidden" name="appVersionBean.id" id="appVersionId" value="${appVersionBean.id}"/>
              	<input type="hidden" name="appVersionBean.appid" id="appId" value="${appVersionBean.appid}"/>
              	
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="itemTitle">IOS版本编辑</li>
			         <li class="clearfix" style="height: auto;">
			         <label class="labelLeft"><span class="required">*</span>版本号：</label>
			            <div class="labelRight">
							<input type="text" name="appVersionBean.version" id="version" value="${appVersionBean.version}"
								class="inputText" title="版本" alt="notnull;"  maxlength="100" style="width: 700px" size="30" />
		               <div class="errorMsg"></div>
			         </li>
			        <li class="clearfix" style="height: auto;">
			        	
			            <label class="labelLeft"><span class="required">*</span>苹果商店下载地址：</label>
			            <div class="labelRight">
							<input type="text" name="appVersionBean.apksrc" id="apksrc" value="${appVersionBean.apksrc}"
								class="inputText" title="苹果商店APP下载地址" alt="notnull;"  maxlength="100" style="width: 700px" size="30" />
		               <div class="errorMsg"></div>
		              
		               </div>
		              
			        </li>
			        
			        <!-- 操作按钮 -->
			        <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight">
			                <input type="button" id="insert" class="defaultBtn" onclick="checkInput()" value="保存" />
			               
			            </div>
			           
			        </li>
			    </ul>
			</form>
		</div>    
	</section>	
</body>
</html>

