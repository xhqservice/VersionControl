<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>成功信息显示页</title>
		<%@ include file="../include/meta.inc"%>
		<link rel="stylesheet" type="text/css" href="../common/main.css">
		<script type="text/javascript" src="../common/tableManager.js"></script>
		<script type="text/javascript">
			function init() {
				setTimeout("defaultUrl()", 30000);
			}		

			function defaultUrl() {
			if("<s:property value="#request.data.defaulturl"/>"!=null)
					window.location = "<s:property value="#request.data.defaulturl"/>"; 
			}
		</script>
	</head>
	<body onload="init();" >
		<div class="right_w">
		<script>pageTitle('首页', '提示信息');</script>
		<table width="98%" border="0" cellspacing="0" cellpadding="0" class="index_w">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="right_ts">
						<tr>
							<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="ts_mj" align="center">
									<tr>
										<td>
											<div style="margin-top: 13px;">
												<div class="ts_mjl">
													<ul>
														<li class="ts_ico">
															<img src="../images/er/ts_ico1.jpg" />
														</li>
														<li class="ts_h">
															<h1>
																提示信息:
															</h1>
															<s:property value="#request.data.description"/>
														</li>
													</ul>
												</div>
												<div class="ts_mjr">
													<img src="../images/er/ts_ico2.jpg" />
												</div>
											</div>
											<div style="clear: both"></div>
											<s:if test="#request.data.linkurl != null">
												<div class="ts_title">
													您可以进行以下操作：
												</div>
												<div class="ts_nav">
													<ul>
														<%--<logic:iterate name="data" property="linkurl" id="link">
														<!-- 
															<li class="t_nav2" onclick="this.className='t_nav3'"
																onmouseout="this.className='t_nav2'">
																<a href="<bean:write name="link" property="linkurl" />"
																	target="<bean:write name="link" property="target"/>">
																	<bean:write name="link" property="linkname" /> </a>
															</li>
														 -->
														 	<li><a href="<bean:write name="link" property="linkurl" />" class="ad" target="<bean:write name="link" property="target"/>"><cite><img src="../images/er/ts_ico3.gif" /><bean:write name="link" property="linkname" /></cite></a></li>
														</logic:iterate>--%>
														<!--2010-10-20石凌修改-->
														<!--注销的时候没有传bean，为空， 当linkurl存在"[$"时，不能解析成url,所以不能显示 -->
														<%
														HashMap data = (HashMap)request.getAttribute("data");
														List linkurlList =  (List)data.get("linkurl");
														 for(int i = 0;i<linkurlList.size();i++){
														 	Map linkurlMap = (Map)linkurlList.get(i);
														 	String linkurl = (String)linkurlMap.get("linkurl");
														 	if(linkurl!=null&&linkurl.indexOf("[$")==-1){
														%>
													 		<li><a href="<%=linkurlMap.get("linkurl") %>" class="ad" target="<%=linkurlMap.get("target") %>"><cite><img src="../images/er/ts_ico3.gif" /><%=linkurlMap.get("linkname") %></cite></a></li>
													 	<%} }%>
													</ul>
												</div>
											</s:if>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</div>
	</body>
</html>