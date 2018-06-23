<%@page import="com.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ include file="../include/include.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>APP应用列表</title>
</head>

<script type="text/javascript">

	//跳转到list界面
	function tolist(ptlx) {
		window.location.href = "../app/applist.action";
	}
	
	//编辑app
	function appEdit(id){
		search("app_edit.action?appJbxxBean.id="+id);
	}
	
	//注销app
	function appZx(id,appname){
		if(confirm("您确定要注销APP【"+appname+"】吗？")){
			$.ajax({
		   		type: "POST",
		   		url: "app_remove.action",
		   		dataType:"json",
		   		data:"appJbxxBean.id="+id,
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
     	var fields =new Array("appid**","appname**","ssxm**");
		getparamter(fields, "queryparamter", "queryparamtername");    
		if(action != null && action != ""){
			document.forms["appForm"].action = action; 
		}
      	document.forms["appForm"].submit();
    }
	
	//版本维护
	function versionEdit(id){
		window.location.href = "../app/app_version.action?appJbxxBean.id="+id;
	}
	
	
	//更新规则
	function updateRuleEdit(id){
		window.location.href = "../app/app_updaterule.action?appJbxxBean.id="+id;
	}
	
	


</script>
<body >
<section id="contentMain">

	<form action="../app/applist.action" id="appForm" name="appForm" method="post">
		<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
		<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />

		<div class="topDiv">
			<div class="topLeft">
				<h2 class="infoTitle">APP应用列表</h2>
				<a type="button" class="addSomeBtn addBtnFora" style="cursor: pointer;margin-bottom: 0;" onclick="appEdit('')">新增APP</a>
			</div>
			<div class="topRight">
				<div class="searchItemsDiv">
					<div>
						<div class="searchItem">
							<span>APPID</span>
							<input name="appid" id="appid" title="应用ID" class="inputText" />
						</div>
						<div class="searchItem">
							<span>APP名称</span>
							<input name="appname" id="appname" title="应用名称" class="inputText" />
						</div>
						<div class="searchItem">
							<span>所属平台</span>
							<select name="ssxm" id="ssxm" title="所属平台" 
			               		class="selectStyle" >
								<option value=""></option>
								<jadlhtml:optionsCollection label="mc" name="dic"
									property="t_dm_ssxm" value="dm" />
							</select>
						</div>
					</div>
					
				</div>
				
				<div class="searchFunDiv">
					<input type="button" class="defaultBtn" onclick="search()" value="查询" />
					<input type="reset" class="defaultBtn backBtn" value="重置" />
				</div>
			</div>
		</div>
	</form>
	
		<div style="margin-top: 15px;">
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
          		<colgroup style="width: 5%" />
				<colgroup style="width: 15%" />
				<colgroup style="width: 15%" />
				<colgroup style="width: 15%" />
				<colgroup style="width: 15%" />
			
				<colgroup style="width: 20%" />
				
			<tbody>
				<tr class="title">
					<th style="text-align: center;">序号</th>
					<th style="text-align: center;">APPID</th>
					<th style="text-align: center;">APP名称</th>
					<th style="text-align: center;">当前版本</th>
					<th style="text-align: center;">所属平台</th>
					<th style="text-align: center;">操作</th>
					
				</tr>
				<c:if test="${empty list}">
					<tr><td colspan="7" align="center" style="text-align: center;" class="noData">暂时没有APP信息</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr>
							<td style="text-align: center;">${status.index+1}</td>
							<td style="text-align: center;">${item.appid}</td>
							
							<td style="text-align: center;">${item.appname}</td>
							<td style="text-align: center;font-weight: bold;">${item.version}</td>
							<td style="text-align: center;">${item.ssxm_dicvalue}</td>
							
	
							<td style="text-align: center;">
								
								<a href="javascript:void(0)" onclick="versionEdit('${item.id}')" >版本维护</a>
								<a href="javascript:void(0)" onclick="updateRuleEdit('${item.id}')" >更新规则</a>
								&nbsp;|&nbsp;
								<a href="javascript:void(0)" onclick="appEdit('${item.id}')" >编辑</a>
								<a href="javascript:void(0)" onclick="appZx('${item.id}','${item.appname}')" >注销</a>
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
