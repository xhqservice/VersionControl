<%@page import="com.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>规则列表</title>
	<%@ include file="../include/meta.inc" %>
</head>

<script type="text/javascript">

	
	
	
	//编辑规则
	function updateruleEdit(id){
		var targetlx = $("#targetlx").val();
		var appid = $("#appid").val();
		search("appupdaterule_edit.action?updateruleBean.id="+id+"&updateruleBean.targetlx="+targetlx+"&updateruleBean.appid="+appid);
	}
	
	//注销规则
	function updateruleZx(id,target,version){
		if(confirm("您确定要注销"+target+"["+version+"]的更新规则吗？")){
			$.ajax({
		   		type: "POST",
		   		url: "appupdaterule_remove.action",
		   		dataType:"json",
		   		data:"updateruleBean.id="+id,
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
     	var fields =new Array("appid","targetlx","version");
		getparamter(fields, "queryparamter", "queryparamtername");    
		if(action != null && action != ""){
			document.forms["updateruleForm"].action = action; 
		}
      	document.forms["updateruleForm"].submit();
    }
  
	//加载完成之后
	function afterOnload(){
		
		var targetlx = document.getElementById("targetlx").value;
		var id = $(window.parent.document).find(".layui-tab-title li[class=layui-this]").attr("id");
		
		if(("li_"+targetlx) != id){
			
			$(window.parent.document).find(".layui-tab-title li").removeAttr("class");
			$(window.parent.document).find(".layui-tab-title li[id=li_"+targetlx+"]").addClass("layui-this");
		}	
		
		if(targetlx == '<%=SystemConstants.TARGETLX_SB%>'){
			$(".targetname").html("设备ID");
		}else if(targetlx == '<%=SystemConstants.TARGETLX_DQ%>'){
			$(".targetname").html("地区");
		}else if(targetlx == '<%=SystemConstants.TARGETLX_DW%>'){
			$(".targetname").html("单位代码");
		}
		
	}

	//选择版本
	function selectVersion() {
		var appid = $("#appid").val();
		var conditions = "appid@equals@"+appid;  
		openSeachWindow("appversion", conditions, "version", "version","", "");
	}
	
	//重置
	function searchreset(){
		$("#target").val("");
		$("#version").val("");
	}
</script>

<body style="background: white;">
<section id="contentMain" style="padding: 10px;margin: 0px;border: none;">
		<input type="hidden" id="targetlx" name="targetlx" title="targetlx" value="" />
	<form action="../app/appupdaterulelist.action" id="updateruleForm" name="updateruleForm" method="post">
		<input type="hidden" id="queryparamter" name="queryparamter" value="${queryparamter}" />
		<input type="hidden" id="queryparamtername" name="queryparamtername" value="${queryparamtername}" />
		<input type="hidden" id="appid"  name="appid" title="APPID" value="" />
		
		
		<div class="topDiv">
			<div class="topLeft">
				
				<a type="button" class="addSomeBtn addBtnFora" style="cursor: pointer;margin-bottom: 0;" onclick="updateruleEdit('')">新增规则</a>
			</div>
			<div class="topRight">
				<div class="searchItemsDiv">
					<div>
						<div class="searchItem">
							<span class="targetname"></span>
							<input name="target" id="target" title="目标" class="inputText" />
						</div>
						<div class="searchItem">
							<span >指定版本</span>
							
							<input name="version" id="version" title="版本" readonly="readonly" class="inputText readonly" onclick="selectVersion();" />
						</div>
					</div>
				</div>
				
				<div class="searchFunDiv">
					<input type="button" class="defaultBtn" onclick="search()" value="查询" />
					<input type="button" onclick="searchreset()" class="defaultBtn backBtn" value="重置" />
				</div>
			</div>
		</div>
	</form>
	
	
		<div style="margin-top: 15px;">
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
          		<colgroup style="width: 5%" />
				<colgroup style="width: 15%" />
				<colgroup style="width: 10%" />
				<colgroup style="width: 25%" />
				
				<colgroup style="width: 10%" />
				
			<tbody>
				<tr class="title">
					<th style="text-align: center;">序号</th>
					<th style="text-align: center;" class="targetname"></th>
					<th style="text-align: center;" >指定版本</th>
					<th style="text-align: center;">有效时间</th>
				
					<th style="text-align: center;">操作</th>
					
				</tr>
				<c:if test="${empty list}">
					<tr><td colspan="5" align="center" style="text-align: center;" class="noData">暂无规则信息</td></tr>
				</c:if>
				<c:if test="${not empty list}">
					<c:forEach items="${list}" var="item" varStatus="status">
					
						<tr >
						
							<td style="text-align: center;">${status.index+1}</td>
							<td style="text-align: center;">
								${item.target}
							</td>
							<td style="text-align: center;">
								${item.version}
							</td>
							
							<td style="text-align: center;">
							<c:choose>
								<c:when test="${empty item.kssj &&  empty item.jssj}">
									永久生效
								</c:when>
								<c:when test="${not empty item.kssj  && not empty item.jssj}">
									${item.kssj}至${item.jssj}
								</c:when>
								<c:when test="${not empty item.kssj}">
									【生效时间】${item.kssj}
								</c:when>
								<c:otherwise>
									【失效时间】${item.jssj}
								</c:otherwise>
								
							</c:choose>
							
							<td style="text-align: center;">
								
								<a href="javascript:void(0)" onclick="updateruleEdit('${item.id}')" >编辑</a>
								<a href="javascript:void(0)" onclick="updateruleZx('${item.id}','${item.target}','${item.version}')" >注销</a>
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
