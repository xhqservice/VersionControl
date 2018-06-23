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
	<title>用户更新日志列表</title>
</head>
<script>
//表单提交
function search(){
 	var fields =new Array("dispatchTime#","appname**","version**");
	getparamter(fields, "queryparamter", "queryparamtername");    
	
  	document.forms["logForm"].submit();
}

//选择行政区划
function selectXzqh() {
	var conditions = "";  
	openSeachWindow("xzqh",conditions,"xzqh,xzqhmc","target,xzqhmc","","");
}
</script>

<body >
<section id="contentMain">

	<form action="../app/updateloglist.action" id="logForm" name="logForm" method="post">
		<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
		<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />

		<div class="topDiv">
			<div class="topLeft">
				<h2 class="infoTitle" >用户更新列表</h2>
				
			</div>
			<div class="topRight">
				<div class="searchItemsDiv">
					<div>
						<div class="searchItem">
							<span>APP</span>
							<input name="appname" id="appname" title="APP" class="inputText" />
						</div>
						<div class="searchItem">
							<span>版本</span>
							<input name="version" id="version" title="版本" class="inputText" />
						</div>
						<div class="searchItem">
							<span>下载时间</span>
							
							<input title="起始下载时间" name="xzsjfrom_date" id="xzsjfrom_date" class="inputText" style="width: 70px;float: none;" type="text" onFocus="WdatePicker()" />
							&nbsp;至&nbsp;
							<input title="截止下载时间" name="xzsjto_date" id="xzsjto_date" class="inputText" style="width: 70px;float: none;" type="text" onFocus="WdatePicker()" />
							
						</div>
						
					</div>
					<div>
						
						
						<div class="searchItem">
							<span>设备ID</span>
							<input name="sbid" id="sbid" title="设备ID" class="inputText" />
						</div>
						<div class="searchItem">
							<span>单位代码</span>
							<input name="dwdm" id="dwdm" title="单位代码" class="inputText" />
						</div>
						<div class="searchItem">
							<span>行政区划</span>
							<input name="xzhqmc" id="xzqhmc" title="行政区划" type="text"  readonly="readonly" class="inputText readonly" onclick="selectXzqh()" />
							<input name="xzhq" id="xzqh" title="行政区划" type="hidden" />
							
						</div>	
						
					</div>
					<div>
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
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 10%" />
				
				
			<tbody>
				<tr class="title">
					<th style="text-align: center;">序号</th>
					<th style="text-align: center;">APP</th>
					<th style="text-align: center;">版本</th>
					
					<th style="text-align: center;">所属平台</th>
					<th style="text-align: center;">设备ID</th>
				
					<th style="text-align: center;">单位代码</th>
					<th style="text-align: center;">行政区划</th>
					<th style="text-align: center;">下载时间</th>
				</tr>
				<c:if test="${empty list}">
					<tr><td colspan="8" align="center" style="text-align: center;" class="noData">暂时没有更新日志</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr>
							<td style="text-align: center;">${status.index+1}</td>
							<td style="text-align: center;">${item.appname}</td>
							
							<td style="text-align: center;">${item.version}</td>
							
							<td style="text-align: center;">${item.ssxm_dicvalue}</td>
							<td style="text-align: center;">${item.sbid}</td>
							
							<td style="text-align: center;">${item.dwdm}</td>
							<td style="text-align: center;">${item.xzqh}</td>
							<td style="text-align: center;">${item.xzsj}</td>
	
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
