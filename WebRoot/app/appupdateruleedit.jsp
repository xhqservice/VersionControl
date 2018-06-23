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
	
	  	//保存
	  	function checkInput(){
	  		$("#insert").attr("disabled", true);
	  		//1.校验表单
			var err = checkForm(document.forms["updateruleForm"]);
			if(!err){
				$("#insert").attr("disabled", false);
				return false;
			}

			ajax_submit();

		
		}
	  
	  	
	  	//ajax提交
		function ajax_submit(){
			var updateruleId = $("#updateruleId").val();
			var url = "";
			if(updateruleId != null && updateruleId !=""){
				url = "appupdaterule_update.action";
			}else{
				url = "appupdaterule_save.action";
			}

			
			$("#updateruleForm").ajaxSubmit({
	       		type: "post", 
	            url: url,
	            enctype:"multipart/form-data",
	            async: true,
	            dataType: "json",
	            success: function(data) {
		            if (data.statusCode == '0000') {
		            	layer.alert("保存成功",function(){
		            		toRuleList();
		            	});
		          	}else{
		          		layer.alert(data.msg);
		          		return false
		          	}
		           
	        	}
	      	});
		}
			
	  	function toRuleList(){
	  		var targetlx = $("#targetlx").val();
	  		var paraValue = "appid~=~${updateruleBean.appid}~targetlx~=~"+targetlx;
			var paramName = "appid~${updateruleBean.appid}~appid;targetlx~"+targetlx+"~targetlx";
			src = "../app/appupdaterulelist.action?queryparamter=%26%26"+paraValue+"&queryparamtername="+paramName;
			window.location = src;
	  	}
	  
	  	//选择版本
		function selectVersion() {
			var appid = $("#appid").val();
			var conditions = "appid@equals@"+appid;  
			openSeachWindow("appversion", conditions, "version", "version","", "");
		}
		
		//选择行政区划
		function selectXzqh() {
			var conditions = "";  
			openSeachWindow("xzqh",conditions,"xzqh,xzqhmc","target,xzqhmc","","");
		}
		
		var TARGETLX_DQ = '<%=SystemConstants.TARGETLX_DQ%>'
		function targetSelect(){
			var value = $("#targetlx").val();
			var name = $("#targetlx").find("option:selected").text();
			$("#target_title").html(name);
			$("#target").attr("title",name);
			
			if(TARGETLX_DQ == value){
				$("#xzqhmc").show();
				$("#target").hide();
				$("#dqBtn").show();
			}else{
				
				$("#xzqhmc").hide();
				$("#target").show();
				$("#dqBtn").hide();
			}
		}
		
		//初始化
		$(function(){ 
			
			$("#targetlx").val('${updateruleBean.targetlx}');
			targetSelect();
		});
  	</script>
	</head>
<body style="background: white;">
<section id="contentMain" style="padding: 10px;margin: 0px;border: none;">
		
        <div class="contentDiv mainContent">
        	<form action="" name="updateruleForm" method="post" id="updateruleForm" class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
             
              	<input type="hidden" name="updateruleBean.id" id="updateruleId" value="${updateruleBean.id}"/>
              	<input type="hidden" name="updateruleBean.appid" id="appid" value="${updateruleBean.appid}"/>

				<ul class="formUl" style="padding-top: 0;">
			        <li class="itemTitle">更新规则编辑</li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>指定版本：</label>
			            <div class="labelRight">
			            	<input type="text" name="updateruleBean.version" readonly="readonly" title="版本" alt="notnull" id="version" value="${updateruleBean.version}" class="inputText readonly"  onclick="selectVersion();" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>指定目标：</label>
			            <div class="labelRight">
		           		<select id="targetlx" name="updateruleBean.targetlx" onchange="targetSelect(this);" title="指定目标" alt="notnull" class="selectStyle" style="width: 301px">
		            		
		            		<jadlhtml:optionsCollection label="mc" name="dic" property="t_dm_targetlx" value="dm" />
		            	</select>
		              	<div class="errorMsg"></div>
		               	</div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span><font id="target_title"></font>：</label>
			            <div class="labelRight">
			            	<input type="text" name="updateruleBean.target"  title=""  alt="notnull;"  id="target" value="${updateruleBean.target}"
									class="inputText" />
							<input type="text" name="xzqhmc" id="xzqhmc" readonly="readonly" class="inputText readonly" value="<jadlbean:write name="updateruleBean" property="target" actualkey="xzqh"  />"  />
							<input type="button" id="dqBtn" style="display: none" class="defaultBtn" onclick="selectXzqh()" value="选择地区" />
			                <div class="errorMsg"></div>
			                
			            </div>
			        </li>
			        
			         <li class="clearfix" style="height: auto;">
				            <label class="labelLeft"><span class="required"></span>有效时间：</label>
				            <div class="labelRight">
				            	<input type="text" title="开始时间"  name="updateruleBean.kssj" style="width: 140px" class="inputText" id="begin_time" value="${updateruleBean.kssj }"
								onclick="WdatePicker({maxDate:'#F{$dp.$D(\'end_time\')}',dateFmt:'yyyy-MM-dd HH:00:00'})" /> 
								至
		        				<input type="text" title="结束时间" class="inputText" style="width: 140px" name="updateruleBean.jssj"    id="end_time"  value="${updateruleBean.jssj }"
								onclick="WdatePicker({minDate:'#F{$dp.$D(\'begin_time\')}',dateFmt:'yyyy-MM-dd HH:00:00'})" /> 
				            	
				            	<div class="errorMsg"></div>
				            	<br/><i class="tipsFile">不设置默认永久生效</i>
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

