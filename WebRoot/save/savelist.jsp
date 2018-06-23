<%@ page language="java" contentType="application/vnd.ms-excel; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
</head>
<body>
	<table border="1">
		<tr style="height: 22px">
			<s:iterator id="title" value="#request.titlelist" status="status">
				<td nowrap><s:property /></td><!-- ${title} -->
			</s:iterator>
		</tr>
		<s:if test="#request.list!=null">
			<s:iterator id="data" value="#request.list" status="status">
				<tr>
					<s:iterator id="field" value="#request.fieldlist">
						<td style="vnd.ms-excel.numberformat:@" nowrap>
						<s:property value="%{#data[#field]}"/><!-- ${data[field]} -->
						 </td>
					</s:iterator>
				</tr>
			</s:iterator>
		</s:if>
	</table>
</body>
</html>