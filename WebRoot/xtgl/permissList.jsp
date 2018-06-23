<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>权限列表</title>
	<%@ include file="../include/include.jsp"%>
	<script type="text/javascript">
	
		// 查看权限
		function viewPermiss(pid){
			window.location="permissview.action?pid="+pid;
		}
	
		//编辑权限
		function editPermiss(pid){
			window.location="permissedit.action?pid="+pid;
		}
		// 删除权限
		function deletePermiss(pid){
			confirmGo("确定删除权限吗?", "permissdelete.action?pid="+pid);
		}
	</script>
</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">权限列表</h2>
		<a type="button" class="addSomeBtn addBtnFora" style="margin-bottom: 20px;cursor: pointer;" onclick="window.location='permissedit.action'">增加权限</a>
		<div>
			<table class="listTable" border="0" cellpadding="0" cellspacing="0">
					<colgroup style="width: 30%"/>
					<colgroup style="width: 30%"/>
					<colgroup style="width: 40%"/>
				<tbody>
					<tr class="title">
						<th >序号</th>
                       	<th >权限名称</th>
                   		<th >操作</th>
					</tr>
					<c:if test="${empty list}">
						<tr><td colspan="3" align="center" style="text-align: center;" class="noData">暂时没有权限信息</td></tr>
					</c:if>
					<c:if test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>
									${status.index+1}
								</td>
								<td >
									${item.permissname}
								</td>
								<td>
									<a href="javascript:void(0)" 
											onclick="viewPermiss('${item.permissid}')" >查看</a>
									<a href="javascript:void(0)" 
											onclick="editPermiss('${item.permissid}')" >编辑</a>
									<a href="javascript:void(0)" 
											onclick="deletePermiss('${item.permissid}')" >删除</a>
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
