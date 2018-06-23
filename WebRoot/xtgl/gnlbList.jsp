<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="page.tld" prefix="page"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>功能菜单列表</title>
	<%@ include file="../include/include.jsp"%>
	<script type="text/javascript">
	
		// 查看功能菜单
		function viewGnlb(gid){
			window.location="gnlbview.action?gid="+gid;
		}
	
		//编辑功能菜单
		function editGnlb(gid){
			window.location="gnlbedit.action?gid="+gid;
		}
		// 删除功能菜单
		function deleteGnlb(gid){
			confirmGo("确定删除功能菜单吗?", "gnlbdelete.action?gid="+gid);
		}
	</script>
</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">功能菜单列表</h2>
		<a type="button" class="addSomeBtn addBtnFora" style="margin-bottom: 20px;cursor: pointer;" onclick="window.location='gnlbedit.action'">增加菜单</a>
		<div>
			<table class="listTable" border="0" cellpadding="0" cellspacing="0">
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 20%"/>
					<colgroup style="width: 20%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 10%"/>
					<colgroup style="width: 20%"/>
				<tbody>
					<tr class="title">
						<th >序号</th>
	                   	<th >功能代码</th>
	                   	<th >功能名称</th>
	                   	<th >功能链接</th>
	                   	<th >功能顺序号</th>
	                   	<th >是否为父菜单</th>
		               	<th >操作</th>
					</tr>
					<c:if test="${empty list}">
						<tr><td colspan="7" align="center" style="text-align: center;" class="noData">暂时没有菜单信息</td></tr>
					</c:if>
					<c:if test="${not empty list}">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td>
									${status.index+1}
								</td>
								<td>
									${item.gncode}
								</td>
								<td >
									${item.gnname}
								</td>
								<td >
									${item.link}
								</td>
								<td >
									${item.sort}
								</td>
								<td >
									<c:if test="${fn:endsWith(item.gncode, '0000')}">
										是
									</c:if>
								</td>
								<td>
									<a href="javascript:void(0)" 
											onclick="viewGnlb('${item.gncode}')" >查看</a>
									<a href="javascript:void(0)" 
											onclick="editGnlb('${item.gncode}')" >编辑</a>
									<a href="javascript:void(0)" 
											onclick="deleteGnlb('${item.gncode}')" >删除</a>
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
