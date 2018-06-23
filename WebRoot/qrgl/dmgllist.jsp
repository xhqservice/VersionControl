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
		<title>字典列表</title>
	</head>
		<script type="text/javascript"> 
		
			function changeImg(flag, id) {
				if (flag == 1) {
					document.getElementById(id).src = "../images/menu3-on.gif";
				} else {
					if (flag == 0) {
						document.getElementById(id).src = "../images/menu3-off.gif";
					}
				}
			} 
			
			//进入字典编辑界面
	    	function changeUrl(table,tablename){
				document.getElementById("table").value = table;
				document.getElementById("tablename").value = tablename;
				document.forms["zdglForm"].submit();
	    	}
		</script>
	</head>
	<body>
	<form name="zdglForm" id="zdglForm" action="../qrgl/dmgl_edit.action" method="post">
		<input type="hidden" name="table" id="table" />
		<input type="hidden" name="tablename" id="tablename" />
	</form>
	<section id="contentMain">
		<h2 class="infoTitle">字典列表</h2>
		
		<div style="margin-top: 15px;">
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
          		<colgroup style="width: 5%" />
				<colgroup style="width: 40%" />
				<colgroup style="width: 40%" />

			<tbody>
				<tr class="title">
					<th style="text-align: center;">序号</th>
					<th style="text-align: center;">字典名称</th>
					<th style="text-align: center;">对应的数据库表</th>
				</tr>
				<c:if test="${empty tableList}">
					<tr><td colspan="3" align="center" style="text-align: center;" class="noData">暂时没有字典信息</td></tr>
				</c:if>
				<c:if test="${not empty tableList}">
					<c:forEach items="${tableList}" var="item" varStatus="status">
						<tr>
							<td style="text-align: center;">${status.index+1}</td>

							<td style="text-align: center;">
							<img id="img${status.index+1}" src="../images/menu3-off.gif" align="absmiddle" border="0" />
								<a href="javascript:void(0)" onclick="changeUrl('${item.table}','${item.tablename}')"
										onmouseover="changeImg(1, 'img${status.index+1}')"
										onmouseout="changeImg(0, 'img${status.index+1}')">
											${item.tablename}
								</a>
							</td>
							
							<td style="text-align: center;">
								${item.table}
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
		
	</div>
	
</section>
	
	</body>
</html>
