<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>角色列表</title>
	<%@ include file="../include/include.jsp"%>
	<style type="text/css">
		a {
			text-decoration: none;
		}
	</style>
	<script type="text/javascript">
	
		// 查看角色
		function viewRole(rid){
			window.location="roleview.action?rid="+rid;
		}
	
		//编辑角色
		function editRole(rid){
			window.location="roleedit.action?rid="+rid;
		}
		// 删除角色
		function deleteRole(rid){
			confirmGo("确定删除角色吗?", "roledelete.action?rid="+rid);
		}
	</script>
</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">角色列表</h2>
		<a type="button" class="addSomeBtn addBtnFora" style="margin-bottom: 20px;cursor: pointer;" onclick="window.location='roleedit.action'">增加角色</a>
		<div>
			<table class="listTable" border="0" cellpadding="0" cellspacing="0">
					<colgroup style="width: 30%"/>
					<colgroup style="width: 30%"/>
					<colgroup style="width: 40%"/>
				<tbody>
					<tr class="title">
						<th >序号</th>
                        <th >角色名称</th>
		                <th >操作</th>
					</tr>
					<c:if test="${empty list}">
						<tr><td colspan="3" align="center" style="text-align: center;" class="noData">暂时没有角色信息</td></tr>
					</c:if>
					<c:if test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>
									${status.index+1}
								</td>
								<td >
									${item.rolename}
								</td>
								<td>
									<a href="javascript:void(0)" 
											onclick="viewRole('${item.roleid}')" >查看</a>
									<a href="javascript:void(0)" 
											onclick="editRole('${item.roleid}')" >编辑</a>
									<a href="javascript:void(0)" 
											onclick="deleteRole('${item.roleid}" >删除</a>
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
