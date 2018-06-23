<%@page import="com.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>安卓版本列表</title>
	<%@ include file="../include/meta.inc" %>
</head>

<script type="text/javascript">

	//设为默认
	function appversionToDefault(version){
		confirmDo("确认将【"+version+"】设为默认版本吗？", function(){
			ajax_setDefaultVersion(version);
		});
	}
	
	//ajax_设为默认
	function ajax_setDefaultVersion(version){
		var appid = $("#appid").val();
		$.ajax({
	   		type: "POST",
	   		url: "appversion_setdefault.action",
	   		dataType:"json",
	   		data:"appVersionBean.version="+version+"&appVersionBean.appid="+appid,
			success: function(data){
				if(data.statusCode == "0000"){
					layer.alert(data.msg, function(){
						search();
					});
					
				}else{
					layer.alert(data.msg);
					return false;
				}
			}
		});
		
	}
	
	
	//编辑版本
	function appversionEdit(id){
		var appid = $("#appid").val();
		search("appversion_edit.action?appVersionBean.id="+id+"&appVersionBean.appid="+appid);
		
		
	}
	
	//注销版本
	function appversionZx(id,version){
		if(confirm("您确定要注销版本【"+version+"】吗？")){
			$.ajax({
		   		type: "POST",
		   		url: "appversion_remove.action",
		   		dataType:"json",
		   		data:"appVersionBean.id="+id,
				success: function(data){
					if(data.statusCode == "0000"){
						search();
					}else{
						layer.alert(data.msg);
						return false;
					}
				}
			});
		}
	}
	
	//表单提交
    function search(action){
     	var fields =new Array("appid","version**");
		getparamter(fields, "queryparamter", "queryparamtername");    
		if(action != null && action != ""){
			document.forms["versionForm"].action = action; 
		}
		
      	document.forms["versionForm"].submit();
    }
	

  //重置
	function reset(){
	
		$("#version").val("");
	}
</script>
<body >
<body style="background: white;">
<section id="contentMain" style="padding: 10px;margin: 0px;border: none;">
	
	<form action="../app/appversionlist.action" id="versionForm" name="versionForm" method="post">
		<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
		<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />
		<input type="hidden" id="appid" name="appid" title="应用ID" value="" />
		
		<div class="topDiv">
			<div class="topLeft">
				
				<a type="button" class="addSomeBtn addBtnFora" style="cursor: pointer;margin-bottom: 0;" onclick="appversionEdit('')">上传新版本</a>
			</div>
			<div class="topRight">
				<div class="searchItemsDiv">
					<div>
						<div class="searchItem">
							<span>版本号</span>
							<input name="version" id="version" title="版本号" class="inputText" />
						</div>
						
						
					</div>
					
				</div>
				
				<div class="searchFunDiv">
					<input type="button" class="defaultBtn" onclick="search()" value="查询" />
					<input type="button" onclick="reset()" class="defaultBtn backBtn" value="重置" />
				</div>
			</div>
		</div>
	</form>
	
	
		<c:set var="version_default" value="<%=SystemConstants.APPVERSION_DEFAULT %>"></c:set>
		<div style="margin-top: 15px;">
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
          		<colgroup style="width: 5%" />
				<colgroup style="width: 15%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				
				<colgroup style="width: 10%" />
				
			<tbody>
				<tr class="title">
					<th style="text-align: center;">序号</th>
					<th style="text-align: center;">版本号</th>
					<th style="text-align: center;">APK</th>
					<th style="text-align: center;">更新时间</th>
				
					<th style="text-align: center;">操作</th>
					
				</tr>
				<c:if test="${empty list}">
					<tr><td colspan="5" align="center" style="text-align: center;" class="noData">暂无版本信息</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
						<c:choose>
							<c:when test="${item.isdefault == version_default }">
								<tr style="background-color:#FFF8DC ">
							</c:when>
							<c:otherwise>
								<tr >
							</c:otherwise>
						</c:choose>
							<td style="text-align: center;">${status.index+1}</td>
							<td style="text-align: center;">
								<c:choose>
									<c:when test="${item.isdefault == version_default }">
										<font style="font-weight: bold;color: #1A1A1A">${item.version}【默认版本】</font>
									</c:when>
									<c:otherwise>
										${item.version}
									</c:otherwise>
								</c:choose>
							
								
								
							</td>
							<td style="text-align: center;">
							<a href="${pageContext.request.contextPath}${item.apksrc}">${item.apk}</a>
							
							
							</td>
							
							<td style="text-align: center;">${item.zhxgsj}</td>
							
							
							<td style="text-align: center;">
								<c:if test="${item.isdefault != version_default }">
									<a href="javascript:void(0)" onclick="appversionToDefault('${item.version}')" >设为默认</a>
									&nbsp;|&nbsp;
								</c:if>
								
								<a href="javascript:void(0)" onclick="appversionEdit('${item.id}')" >编辑</a>
								<a href="javascript:void(0)" onclick="appversionZx('${item.id}','${item.version}')" >注销</a>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="pagelist">
		<%@ include file="../include/page.inc"%>
	</div>
</section>

</body>
</html>
