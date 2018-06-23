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
	<title>定时任务列表</title>
</head>

<script type="text/javascript">

	//跳转到list界面
	function tolist(ptlx) {
		window.location.href = "../app/autotasklist.action";
	}
	
	//编辑任务
	function taskEdit(id){
		search("autotask_edit.action?autotaskBean.id="+id);
	}
	
	//注销任务
	function taskZx(id,appname,version){
		if(confirm("您确定要取消"+appname+"【"+version+"】升级任务？")){
			$.ajax({
		   		type: "POST",
		   		url: "autotask_remove.action",
		   		dataType:"json",
		   		data:"autotaskBean.id="+id,
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
     	var fields =new Array("appid","todozt");
		getparamter(fields, "queryparamter", "queryparamtername");    
		if(action != null && action != ""){
			document.forms["taskForm"].action = action; 
		}
      	document.forms["taskForm"].submit();
    }
	
  	//选择APP应用
	function selectApp() {
		var conditions = "";
		openSeachWindow("applist", conditions, "appid,appname", "appid,appname","", "");
	}



</script>
<body >
<section id="contentMain">

	<form action="../app/autotasklist.action" id="taskForm" name="taskForm" method="post">
		<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
		<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />

		<div class="topDiv">
			<div class="topLeft">
				<h2 class="infoTitle">定时任务列表</h2>
				<a type="button" class="addSomeBtn addBtnFora" style="cursor: pointer;margin-bottom: 0;" onclick="taskEdit('')">新增任务</a>
			</div>
			<div class="topRight">
				<div class="searchItemsDiv">
					<div>
						<div class="searchItem">
							<span>APP</span>
							<input type="text" name="appname" readonly="readonly" id="appname"
									class="inputText readonly"  onclick="selectApp();" />
								<input type="hidden" name="appid" title="APP"  alt="notnull;" id="appid"  />
							
						</div>
						<div class="searchItem">
							<span>执行状态</span>
							<select id="todozt" name="todozt" title="执行状态"  class="selectStyle" >
			            		<option value=""></option>
			            		<jadlhtml:optionsCollection label="mc" name="dic" property="t_dm_todozt" value="dm" />
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
				<colgroup style="width: 25%" />
				
				<colgroup style="width: 15%" />
				<colgroup style="width: 10%" />
				
			<tbody>
				<tr class="title">
					<th style="text-align: center;">序号</th>
					<th style="text-align: center;">APP</th>
					<th style="text-align: center;">执行时间 ——> 【升级版本】</th>
					
					<th style="text-align: center;">执行状态</th>
					<th style="text-align: center;">操作</th>
					
				</tr>
				<c:set var="todozt_yes" value="<%=SystemConstants.APPTASKTODOZT_YES %>" ></c:set>
				
				<c:if test="${empty list}">
					<tr><td colspan="5" align="center" style="text-align: center;" class="noData">暂时没有任务信息</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr>
							<td style="text-align: center;">${status.index+1}</td>
							<td style="text-align: center;">${item.appname}</td>
							<td style="text-align: center;">${item.todotime} ——> 【${item.version}】</td>
						
							<td style="text-align: center;">
							<c:choose>
								<c:when test="${item.todozt == todozt_yes}">
									<font style="color: green;font-weight: bold;">${item.todozt_dicvalue}</font>
								</c:when>	
								<c:otherwise>
								<font style="color: #FF8000;font-weight: bold;">${item.todozt_dicvalue}</font>
								</c:otherwise>
							</c:choose>
							</td>
							<td style="text-align: center;">
								<c:if test="${item.todozt != todozt_yes}">
									<a href="javascript:void(0)" onclick="taskEdit('${item.id}')" >编辑</a>
								</c:if>
								<a href="javascript:void(0)" onclick="taskZx('${item.id}','${item.appname}','${item.version}')" >注销</a>
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
