<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>用户列表</title>
	<%@ include file="../include/include.jsp"%>
	<style type="text/css">
		a {
			text-decoration: none;
		}
	</style>
	<script type="text/javascript">
		
		//查看用户信息
		function viewUser(uid){
			window.location="userview.action?uid="+uid;
		}
	
		//编辑用户
		function editUser(uid){
			window.location="useredit.action?uid="+uid;
		}
		// 删除用户
		function deleteUser(uid){
			confirmGo("确定删除该用户吗?","userdelete.action?uid="+uid);
		}
	</script>
</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">用户列表</h2>
		<a type="button" class="addSomeBtn addBtnFora" style="margin-bottom: 20px;cursor: pointer;" onclick="window.location='useredit.action'">增加用户</a>
		<div>
			<table class="listTable" border="0" cellpadding="0" cellspacing="0">
	          		<colgroup style="width: 5%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 15%"/>
				<tbody>
					<tr class="title">
						<th >序号</th>
                    	<th >用户id</th>
                        <th >用户名</th>
	                    <th >所属单位</th>
	                    <th >行政区划</th>
	                    <th >录入时间</th>
	                    <th >联系人</th>
	                    <th >联系人联系方式</th>
	                    <th >拥有角色</th>
	                    <th >操作</th>
					</tr>
					<c:if test="${empty list}">
						<tr><td colspan="10" align="center" style="text-align: center;" class="noData">暂时没有用户信息</td></tr>
					</c:if>
					<c:if test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>
									${status.index+1}
								</td>
								<td>
									${item.userid}
								</td>
								<td >
									${item.username}
								</td>
								<td >
									${item.qydm}
								</td>
								<td >
									${item.xzqh}
								</td>
								<td >
									${item.lrsj}
								</td>
								<td>
									${item.lxr}
								</td>
								<td>
									${item.lxrlxdh}
								</td>
								<td>
									${item.rolename}
								</td>
								<td>
									<a href="javascript:void(0)" 
											onclick="viewUser('${item.id}')" >查看</a>
									<a href="javascript:void(0)" 
											onclick="editUser('${item.id}')" >编辑</a>
									<a href="javascript:void(0)" 
											onclick="deleteUser('${item.id}')" >撤销</a>
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
