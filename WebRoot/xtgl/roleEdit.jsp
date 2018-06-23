<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>角色编辑</title>
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
				var rid = $("#rid").val();
				if(rid == null || rid == ""){
					//新建
					document.getElementById("roleFormId").action="rolesave.action";
				}else{
					//修改
					document.getElementById("roleFormId").action="roleupdate.action";
				}
				document.getElementById("roleFormId").submit();
			}
  		</script>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">角色编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="roleFormId"
              		method="post" id="roleFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input type="hidden" value="${roleBean.roleid}" name="role.roleid" id="rid" />
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>角色名：</label>
			            <div class="labelRight">
			            	<input type="text" name="role.rolename" id="rolename"
								value="${roleBean.rolename}" class="inputText"
								size="30" title="角色名"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">权限列表：</label>
			            <div class="labelRight">
			            	<c:if test="${not empty permisslist}">
								<c:forEach var="permiss" items="${permisslist}">
									<span>
										<c:set var="flag" value="false"></c:set>
										<c:forEach items="${roleBean.permisses}" var="per">
											<c:if test="${per.permissid == permiss.permissid}">
										    	<c:set var="flag" value="true"></c:set>   
										   </c:if>
										</c:forEach>
										<c:if test="${flag==true}">
											<input id="ckbx${permiss.permissid}" type="checkbox" name="role.permisses" checked="checked" value="${permiss.permissid}" /> <label for="ckbx${permiss.permissid}">${permiss.permissname}</label>
										</c:if>
										<c:if test="${flag==false}">
											<input id="ckbx${permiss.permissid}" type="checkbox" name="role.permisses" value="${permiss.permissid}" /> <label for="ckbx${permiss.permissid}">${permiss.permissname}</label>
										</c:if>	
									</span>
								</c:forEach>
							</c:if>
							<c:if test="${empty permisslist}">
								当前无任何权限可选
							</c:if>
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

