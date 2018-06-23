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
	var CREATERANDOMID = guid();
	var qrcodeimgurl_perfix = '${pageContext.request.contextPath}/WEB-INF/qrcodeimgs/tmp/';
	$(function(){
	
		changeQRCodeImg($("#ssxm").val());
	
		/*
		 * 1、绑定生成二维码的类型点击事件
		 */
		 $(".sclxRadio").each(function(){
		 	$(this).click(function(){
		 		if(this.value == '01') {
		 			//版本控制中心二维码，隐藏输入框、展示下拉框
		 			disabledFormItemInContainer("txtInputDiv", true);
		 			enabledFormItemInContainer("ssxmDiv", true);
		 		}else {
					//任意内容二维码，隐藏下拉框、展示输入框
					disabledFormItemInContainer("ssxmDiv", true);
		 			enabledFormItemInContainer("txtInputDiv", true);
				}
		 	});
		 });
		 
		/*
		 * 2、绑定所属项目的点击事件
		 */
		 $("#ssxm").change(function(){
		 	var ssxm = this.value;
		 	changeQRCodeImg(ssxm);
		 });
		 
		/*
		 * 3、绑定文本框输入改变事件
		 */
		 $("#txtInput").change(function(){
		 	
		 }); 
		
	});
	
	function changeQRCodeImg(ssxm) {
		$.ajax({
	 		type: "post",
	 		url: "qrCode_fillQRCode.action?timestr="+Date.parse(new Date()),
	 		dataType: "json",
	 		data: "sclx=01"+"&content="+ssxm,
	 		success: function(data){
	 			if (data.statusCode == "0000") {
	 				$("#qrcodeUrlInput").val(data.arg2);
	 				$("#qrcodeImg").attr("src", "${pageContext.request.contextPath}"+data.arg1);
				}else {
					layer.alert(data.msg);
				}
	 		}
	 	});
	}
	
	//保存到本地
	function createQRCode() {
		var ssxm = $("#ssxm").val();
		window.location.href = "qrCode_writeImgTofile.action?ssxm="+ssxm;
	}
	
</script>
	</head>
<body >
	<section id="contentMain">
		<h2 class="infoTitle">二维码生成</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="" method="post" enctype="multipart/form-data" 
        			id="" class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
             
				<ul class="formUl" style="padding-top: 0;">
			        <!-- <li class="clearfix">
						<label class="labelLeft">生成二维码类型：</label>
						<div class="labelRight">
						    <label class="" style="color: rgb(102, 102, 102);">
						    	<input type="radio" class="sclxRadio" name="sclx"
						    		value="01" title="版本控制中心二维码"
						    	   checked="checked" />版本控制中心二维码
						    </label>
						    &nbsp;  &nbsp;
						    <label class="" style="color: rgb(179, 179, 179);">
						    	<input type="radio" class="sclxRadio" name="sclx"
						    		value="02" title="任意地址二维码"/>任意地址二维码
						    </label>
						</div>
                    </li> -->
                    
                    <!-- 所属项目div，生成类型为版本控制中心二维码时候显示 -->
                    <div id="ssxmDiv">
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft"><span class="required">*</span>所属项目：</label>
				            <div class="labelRight">
				            	<select id="ssxm" name="" title="所属项目" alt="notnull" class="selectStyle" style="width: 301px">
				            		<jadlhtml:optionsCollection label="mc" name="dic" property="t_dm_ssxm" value="dm" />
				            	</select>
				            	<div class="errorMsg"></div>
				            </div>
				        </li>
			        </div>
			        
                    <!-- 文本内容div，生成类型为任意文本二维码时候显示 -->
                    <div id="txtInputDiv" style="display: none;">
				        <li class="clearfix" style="height: auto;">
				            <label class="labelLeft">文本输入：</label>
				            <div class="labelRight">
				            	<textarea id="txtInput" name="txtInput" class="areaText"></textarea>
				            </div>
				        </li>
			        </div>
                    
                     <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">二维码地址：</label>
			            <div class="labelRight">
			            	<textarea id="qrcodeUrlInput" readonly="readonly" style="height: 60px;" name="qrcodeUrlInput" class="areaText"></textarea>
			            </div>
			        </li>
			         
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">二维码：</label>
			            <div class="labelRight">
			            	<div style="width: 310px;height: 310px;">
				            	<img id="qrcodeImg" />
			            	</div>
			            </div>
			        </li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">
			            	<input type="button" id="insert" class="defaultBtn" onclick="createQRCode()" value="保存到本地" />
			            </label>
			        </li>
			        
			        <!-- 操作按钮 -->
			        <!-- <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight">
			                <input type="button" id="insert" class="defaultBtn" onclick="createQRCode()" value="保存到本地" />
			            </div>
			        </li> -->
			    </ul>
			</form>
		</div>    
	</section>	
</body>
</html>

