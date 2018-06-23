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
		<title>APP应用编辑</title>
		<%@ include file="../include/include.jsp" %>
		<script type="text/javascript">
	
	  	//保存
	  	function checkInput(){
	  		
	  		$("#insert").attr("disabled", true);
	  		//1.校验表单
			var err = checkForm(document.forms["appForm"]);
			if(!err){
				$("#insert").attr("disabled", false);
				return false;
			}
			
			var appid = $("#appid").val();
			var appJbxxId = $("#appJbxxId").val();
			if(!isZm(appid)){
				var errObj = $("#appid").nextAll(".errorMsg")[0];
				$(errObj).html("应用ID只能由字母组成！");
				return false;
			}
			
			//2.验证APPID是否存在
			if (appJbxxId == "" && !ajax_checkAppid()) {
				$("#insert").attr("disabled", false);
				return false;
			}

			//3、校验图标
			var v = $("#myFile").val();
			var bficonsrc = "${appJbxxBean.iconsrc}";
			if (isBlank(bficonsrc) && isBlank(v)) {
				$("#insert").attr("disabled", false);
				layer.alert("请上传图标！");
				return false;
			}
		
			if(appJbxxId == null || appJbxxId == ""){
				document.forms["appForm"].action = "../app/app_save.action";
			}else{
				
				document.forms["appForm"].action="../app/app_update.action";
			}
			document.forms["appForm"].submit();
		}
	  	
	  	//校验是否是字母
	  	function isZm(str){
	  		 var Regx = /^[A-Za-z]*$/;
	          if (Regx.test(str)) {
	              return true;
	          }
			return false;
	  	}
			
		//校验应用ID是否存在
		function ajax_checkAppid(){
			
			var appid = $("#appid").val();
			var re = false;
			$.ajax({
		   		type: "POST",
		   		url: "app_checkAppid.action",
		   		dataType:"json",
		   		async : false,
		   		data:"appid="+appid,
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
		 * 异步上传图标
		 */
		function asyncUploadIcon() {
			var url = "app_uploadIcon.action";
			var load_index = layer.load();
			$("#appForm").ajaxSubmit({
	       		type: "post", 
	            url: url,
	            enctype:"multipart/form-data",
	            async: true,
	            dataType: "json",
	            success: function(data) {
	            	layer.close(load_index);
		            if (data.statusCode == '0000') {
		            	//设置图标src地址
		            	var tmpIconFilename = data.arg1;
		            	var path = data.arg2;
		            	$("#icon").attr("src", isBlank(path) ? "" : "${pageContext.request.contextPath}"+path);
						$("#tmpIconFilename").val(tmpIconFilename);
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
			

		//初始化
		$(function(){ 
			$("#appdesc").html('${appJbxxBean.appdesc}');	//应用简介
			$("#ssxm").val('${appJbxxBean.ssxm}');	//所属平台
			var iconsrcStr = "${appJbxxBean.iconsrc}";
			var iconsrc;
			if (!iconsrcStr || isBlank(iconsrcStr)) {
				iconsrc = "";
			}else {
				iconsrc = '${pageContext.request.contextPath}/${appJbxxBean.iconsrc}';
			}
			iconsrc = iconsrc.replace(/\\/g, "\\\\");
			$("#icon").attr("src", iconsrc);	//图标
		});
  	</script>
	</head>
<body >

	<section id="contentMain">
		<h2 class="infoTitle">APP编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="appForm" method="post" enctype="multipart/form-data" 
        			id="appForm" class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
             
              	<input type="hidden" name="appJbxxBean.id" id="appJbxxId" value="${appJbxxBean.id}"/>
              	<input type="hidden" name="tmpIconFilename" id="tmpIconFilename" value=""/>
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="itemTitle">基本信息编辑</li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>APPID：</label>
			             <div class="labelRight">
			            <!-- 为添加界面 -->
						<c:if test="${appJbxxBean.id == null || appJbxxBean.id == ''}">
							<input type="text" name="appJbxxBean.appid" id="appid" value="${appJbxxBean.appid}"
								class="inputText" title="APPID" alt="notnull;length<=19;length>=6"  maxlength="32" size="30" />
						</c:if>
						<!-- 为编辑界面 -->
						<c:if test="${appJbxxBean.id != null && appJbxxBean.id != ''}">
							<input type="text" name="appJbxxBean.appid" id="appid" value="${appJbxxBean.appid}"
								class="inputText readonly" alt="notnull;length<=19;length>=6" title="APPID"  maxlength="32" size="30" readonly="readonly" />
							
						</c:if>
		               <div class="errorMsg"></div>
		               <br/><i class="tipsFile">APPID为6-19个字母组成，添加成功后不可更改</i>
		               </div>
		              
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>APP名称：</label>
			            <div class="labelRight">
			            	<input type="text" name="appJbxxBean.appname" id="appname"
								value="${appJbxxBean.appname}" class="inputText"
								size="30" title="APP名称" alt="notnull;length<=50;" maxlength="50" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>所属平台：</label>
			            <div class="labelRight">
			            	<select id="ssxm" name="appJbxxBean.ssxm" title="所属平台" alt="notnull" class="selectStyle" style="width: 301px">
			            		<option value=""></option>
			            		<jadlhtml:optionsCollection label="mc" name="dic" property="t_dm_ssxm" value="dm" />
			            	</select>
			            	<div class="errorMsg"></div>
			            </div>
			        </li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>图标：</label>
			            <div class="labelRight">
			            	<input type="file" name="myFile" class="inputText" id="myFile" style="width: 300px;height: 25px"
			            		 	onchange="asyncUploadIcon()" title="APK" alt="notnull;" size="30" />
			            	<div id="iconDiv">
			            		<img id="icon" src="" style="max-width: 100px;max-height: 100px;" />
			            	</div>
			            </div>
			        </li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>排序：</label>
			            <div class="labelRight">
			            	<input type="text" name="appJbxxBean.sort" id="sort"
								value="${appJbxxBean.sort}" class="inputText"
								onkeyup="this.value=this.value.replace(/\D/g,'')" 
		                    	onafterpaste="this.value=this.value.replace(/\D/g,'')"
								size="30" title="排序" alt="notnull;length<=6;" maxlength="6" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft"><span class="required"></span>APP简介：</label>
				            <div class="labelRight">
				            	<textarea id="appdesc" name="appJbxxBean.appdesc" class="areaText"></textarea>
				            	<div class="errorMsg"></div>
				            </div>
				     </li>
			       
			      
			        <!-- 操作按钮 -->
			        <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight">
			                <input type="button" id="insert" class="defaultBtn" onclick="checkInput()" value="保存" />
			                <input type="button" class="defaultBtn backBtn" onclick="history.back(-1)" value="返回" />
			            </div>
			        </li>
			    </ul>
			</form>
		</div>    
	</section>	
</body>
</html>

