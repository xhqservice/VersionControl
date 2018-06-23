<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>用户编辑</title>
		<%@ include file="../include/include.jsp"%>
		<script type="text/javascript">
  	//校验域填写是否完整
  	function save(){
  		$("#insert").attr("disabled", true);
		var err = checkForm(document.forms[0]);
		if(!err){
			$("#insert").attr("disabled", false);
			return false;
		}
		var uid = $("#uid").val();
		if(uid == null || uid == ""){
			//新建
			document.getElementById("userFormId").action="usersave.action";
			document.getElementById("userFormId").submit();
		}else{
			//修改
			document.getElementById("userFormId").action="userupdate.action";
			document.getElementById("userFormId").submit();
		}
	}
  	</script>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">用户编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="userFormId"
              		method="post" id="userFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input name="userBean.id" type="hidden" id="uid" value="${userBean.id}" />
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>用户id：</label>
			            <div class="labelRight">
			            	<input type="text" name="userBean.userid" id="userid"
								value="${userBean.userid}" class="inputText"
								size="30" title="用户id"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>用户名：</label>
			            <div class="labelRight">
			            	<input type="text" name="userBean.username" id="userid"
								value="${userBean.username}" class="inputText"
								size="30" title="用户名"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>所属单位：</label>
			            <div class="labelRight">
			            	<input type="text" name="userBean.qydm" id="qydm"
								value="${userBean.qydm}" class="inputText"
								size="30" title="所属单位"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>行政区划：</label>
			            <div class="labelRight">
			            	<input type="text" name="userBean.xzqh" id="xzqh"
								value="${userBean.xzqh}" class="inputText"
								size="30" title="行政区划"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>联系人：</label>
			            <div class="labelRight">
			            	<input type="text" name="userBean.lxr" id="lxr"
								value="${userBean.lxr}" class="inputText"
								size="30" title="联系人"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>联系人联系电话：</label>
			            <div class="labelRight">
			            	<input type="text" name="userBean.lxrlxdh" id="lxrlxdh"
								value="${userBean.lxrlxdh}" class="inputText"
								size="30" title="联系人联系电话"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			         <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>所属角色：</label>
			            <div class="labelRight">
			            	<select name="userBean.role.roleid" class="selectStyle" style="width: 300px">
								<c:forEach var="role" items="${rolelist}">
									<option value="${role.roleid}" ${userBean.roleid == role.roleid ? "selected='selected'" : " "} >${role.rolename}</option>
								</c:forEach>	
							</select>
							<div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <!-- 操作按钮 -->
			        <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight">
			                <input type="button" class="defaultBtn" onclick="save()" value="保存" />
			                <input type="button" class="defaultBtn backBtn" onclick="history.back(-1)" value="返回" />
			            </div>
			        </li>
			    </ul>
			</form>
		</div>    
	</section>
</body>
</html>

