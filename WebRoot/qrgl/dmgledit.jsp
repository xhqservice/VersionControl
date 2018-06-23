<%@page import="com.sun.xml.internal.txw2.Document"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.jadlsoft.utils.SystemConstants"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<%@ taglib uri="page.tld" prefix="page"%>
<%@ taglib uri="/WEB-INF/taglib/jadlbean.tld" prefix="jadlbean"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ include file="../include/include.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>字典维护</title>
</head>
<style type="text/css">
.font_m {
	text-align: center;
}

td {
	font-family: "宋体", Arial Helvetica, sans-serif;
	font-size: 12px;
	align: right;
}
</style>

<script type="text/javascript">  
	    	var newWindow;
			var onRow;
			var dic_addtitle = "字典表操作";
			var dic_width = "300";
			var dic_height = "150";
			var dic_globalcontent ="";
			$(function(){
				dic_globalcontent = $("#dic_hiddendiv").get(0).innerHTML;
	
			});
			
		    //显示修改div
			function toModifyDic(obj1,columns,pkname){
				if(newWindow != null) {
					newWindow.close();
				}
				var dmArray = columns.split("@@");
				//主键不能修改
				if(pkname != null && pkname != 'null' && pkname != ''){
					document.getElementById(pkname).disabled = true;
					document.getElementById("pkcolumn").value = pkname;
				}
				
				//先展示弹出框，否则在谷歌和火狐浏览器下没法给框里面的input赋值
				$("#dic_addbutton").hide();
				$("#dic_editbutton").show();
				var content =  $("#dic_hiddendiv").get(0).innerHTML;
				$("#dic_hiddendiv").get(0).innerHTML = "";
				// show
				
				if(newWindow==null) {
					newWindow = showmsg(dic_addtitle, content, dic_width, dic_height);
				} else {
					
					newWindow.resize(dic_width, dic_height);
					updatewin(newWindow, content, dic_addtitle, dic_width, dic_height);
				}
				newWindow.onclose = function () {
					onwinclose("dic");
				}
				
				//回填内容
				onRow = obj1.parentNode.parentNode;
				for(var i=0; i<dmArray.length-1; i++) {
					//去空格
					var td = $(onRow).find("td").eq(i)[0];
					document.getElementById(dmArray[i]).value = td.innerText.replace(/(^\s*)|(\s*$)/g, "");
					//document.getElementById(dmArray[i]).value = onRow.childNodes[i].innerText.replace(/(^\s*)|(\s*$)/g, "");
				}
			}
			
			function onwinclose (flag) {
				if (flag == "dic") {				
					$("#dic_hiddendiv").get(0).innerHTML = dic_globalcontent; 
				}updatewin(newWindow, "", "");
			}
			
			// 更新行记录
			function updateRowDic(type,columnname,columntype,pkcolumn) {
				var notNullColumn = document.getElementById("notNullColumn").value;
				var notNullColumnArr = notNullColumn.split("@@");
				var msg = "";
				for(var i=0;i<notNullColumnArr.length - 1;i++){
					var value = document.getElementById(notNullColumnArr[i]).value;
					var title = document.getElementById(notNullColumnArr[i]).title;
					if(value == ""){
						msg = msg + title;
						msg = msg + ",不能为空。\n";
					}
				}
				
				if(msg !=""){
					alert(msg);
					return;
				}
				if(!checkForm(document.getElementById("check"))){
					return;
				}
			
				
				var columnnameArr = columnname.split("@@");
				var columnvalue = "";
				for(var i=0;i<columnnameArr.length - 1;i++){
				 	var colvalue = document.getElementById(columnnameArr[i]).value;
				 	if(colvalue == ""){
				 		colvalue = "null";
				 	}
					columnvalue += colvalue;
					columnvalue += "@@";
				}
				
				document.getElementById("type").value = type;
				document.getElementById("pkcolumn").value = pkcolumn;
				document.getElementById("columnname").value = columnname;
				document.getElementById("columnvalue").value = columnvalue;
				document.getElementById("columntype").value = columntype;
	    		document.forms["zdglForm"].action = "../qrgl/dmgl_update.action";
	    		document.forms["zdglForm"].submit();
	    		
	    	
			}
			
			function cancel () {
				newWindow.close();
			}
			
	  
	    	function add(){
	    		$("#dic_editbutton").hide();
				$("#dic_addbutton").show();
				var content = dic_globalcontent;
				$("#dic_hiddendiv").get(0).innerHTML = "";
				if (newWindow==null) {
					newWindow = showmsg(dic_addtitle, content, dic_width, dic_height);
				} else {
					newWindow.resize(dic_width, dic_height); // 处理一个页面中多个不同大小层
					updatewin(newWindow, dic_globalcontent, dic_addtitle, dic_width, dic_height);
				}
				newWindow.onclose = function() {
					onwinclose("dic");
				}
	    	}
	    	
			//删除操作，删除操作单独处理，打开窗口分写
			function deleteDic(obj1,columns,pkname,columntype){
				if(!confirm("确定删除此记录！")){
					return;
				}
				if(newWindow != null) {
					newWindow.close();
				}
				var dmArray = columns.split("@@");
				//回填内容
				onRow = obj1.parentNode.parentNode;
				var columnvalue = "";
				for(var i=0; i<dmArray.length-1; i++) {
					var td = $(onRow).find("td").eq(i)[0];
					columnvalue += td.innerText.replace(/(^\s*)|(\s*$)/g, "");
					columnvalue += "@@";
					//columnvalue += onRow.childNodes[i].innerText.replace(/(^\s*)|(\s*$)/g, "");
					//columnvalue += "@@";
				}
				
				document.getElementById("pkname").value = pkname;
				document.getElementById("columnname").value = columns;
				document.getElementById("columnvalue").value = columnvalue;
				document.getElementById("columntype").value = columntype;
	    		document.forms["zdglForm"].action = "../qrgl/dmgl_remove.action";
	    		document.forms["zdglForm"].submit();
			}	

	  	function search(table){    		
    		document.getElementById("columnname").value = document.getElementById("columnsArr").value;
    		document.forms["zdglForm"].submit();
	  	}
    	
    	function searchReset() {
    		$("#searchkey").val("");
    	}
    	
		</script>
</head>

<body >
<section id="contentMain">
	
	
	<%
		List commentLista = (List) request.getAttribute("commentsList");
		List contentLista = (List) request.getAttribute("contentList");
	%>

	<form action="../qrgl/dmgl_searchDic.action" id="zdglForm" name="zdglForm" method="post">
		<input type="hidden" id="table" name="table" value="${table}" />
		<input type="hidden" id="tablename" name="tablename" value="${tablename}" />
		<input type="hidden" id="columnname" name="columnname" value="" />
		<input type="hidden" id="columnvalue" name="columnvalue" value="" />
		<input type="hidden" id="columntype" name="columntype" value="" />
		<input type="hidden" id="pkname" name="pkname" value="" />
		<input type="hidden" id="pkcolumn" name="pkcolumn" value="" />
		<input type="hidden" id="type" name="type" value="" />
		<div class="topDiv">
			<div class="topLeft">
				<a type="button" class="addSomeBtn addBtnFora" style="cursor: pointer;margin-bottom: 0;" onclick="add()">新增字典值</a>
			</div>
			<div class="topRight">
				<div class="searchItemsDiv">
					<div>
						<div class="searchItem">
							<span>关键字查询</span> <input type="text" name="searchkey" class="inputText"
								id="searchkey" value="${searchkey}" />

						</div>
					</div>
	
				</div>
				
				<div class="searchFunDiv">
					<input type="button" class="defaultBtn" onclick="search()"
						value="查询" /> <input type="button" onclick="searchReset()" class="defaultBtn backBtn"
						value="重置" />
				</div>
			</div>
		</div>
	</form>

	<div style="margin-top: 15px;">
		<table class="listTable" border="0" cellpadding="0" cellspacing="0">
			<c:if test="${not empty commentsList}">
				<c:forEach items="${commentsList}" var="item" varStatus="status">
					<colgroup style='width:auto'></colgroup>
				</c:forEach>
			</c:if>
		
			<colgroup style="width: 10%"></colgroup>
			<tbody>
				<tr class="title">
					<c:if test="${not empty commentsList}">
						<c:forEach items="${commentsList}" var="item" varStatus="status">
							<th style="text-align: center;">${item.comments}</th>
						</c:forEach>
					</c:if>
					<th style="text-align: center;">操作</th>
				</tr>

				<%
					for (int k = 0; k < contentLista.size(); k++) {
						Map contentmap = (Map) contentLista.get(k);
						String pkname = "";
						String columnsname = "";
						String columnstype = "";
					%>
				<tr class="list_table_rows_tr list_table_rows_td">
					<%
						for (int j = 0; j < commentLista.size(); j++) {
							Map commentmap = (Map) commentLista.get(j);
							pkname = String.valueOf(commentmap.get("pk")).toLowerCase();
							columnsname += String.valueOf(commentmap.get("column_name")).toLowerCase();
							columnsname += "@@";
							
							String data_type = String.valueOf(commentmap.get("data_type")).toLowerCase();
							columnstype += data_type;
							columnstype += "@@";
							String colum = String.valueOf(commentmap.get("column_name")).toLowerCase();
						%>
					<td style="text-align: center;">
						<%= contentmap.get(colum) == null ? "" : contentmap.get(colum)%>
					</td>
					<%
					}
					%>

					<td style="text-align: center;">
					
						<a href="javascript:void(0)" onclick="toModifyDic(this,'<%=columnsname%>','<%=pkname%>');" >修改</a>
						<a href="javascript:void(0)" onclick="deleteDic(this,'<%=columnsname%>','<%=pkname%>','<%=columnstype%>');" >删除</a>
				
					</td>


				</tr>
				<%
				}
				%>

			</tbody>
		</table>
	</div>
	<div class="pagelist">
		<%@ include file="../include/page.inc"%>
	</div>




	<br>
	<div id="dic_hiddendiv" style="display:none">
		<form id="check">
			<table width="100%" border="0" cellpadding="2" cellspacing="0">
				<%
					String columnname = "";
					String columntype = "";
					String notNullColumn = "";
					String pkcolumn = "";
					for (int k = 0; k < commentLista.size(); k++) {
						Map columnMap = (Map) commentLista.get(k);
						String comments = String.valueOf(columnMap.get("comments"));
						String data_length = String.valueOf(columnMap.get("data_length"));
						String.valueOf(columnMap.get("column_name")).toLowerCase();
						if (comments == null || comments.equals("") || comments.equals("null")) {
							comments = String.valueOf(columnMap.get("column_name")).toLowerCase();
						}
						String textId = String.valueOf(columnMap.get("column_name")).toLowerCase();
						String data_type = String.valueOf(columnMap.get("data_type")).toLowerCase();
						pkcolumn = String.valueOf(columnMap.get("pk")).toLowerCase();
						columntype += data_type;
						columntype += "@@";
						columnname += textId;
						columnname += "@@";
						String isNull = String.valueOf(columnMap.get("nullable")).toLowerCase();
				%>
				<input type="hidden" id="columns" value='<%=columnname %>' />
				<tr>
					<td align="right" style="text-align:right">
						<%
						if (isNull.equalsIgnoreCase("N")) {
							notNullColumn += textId;
							notNullColumn += "@@";
						%> <font color="#FF0000">*</font> <%
						}
						%> <%=comments%> ： <br>
					</td>
					<% 
					
					
					 %>
					<td><input maxlength="<%=data_length%>"
						alt="length<=<%=data_length %>" type="text" id='<%=textId%>'
						title='<%=comments %>' /> <br>
					</td>
				</tr>
				<%
				}
				%>
				<input type="hidden" id="columnsArr" value='<%=columnname %>' />
				<input type="hidden" id="notNullColumn" value='<%=notNullColumn %>' />
				<tr height="40">
					<td class="font_m" colspan="2">
						<input type="button"id="dic_addbutton" class="Button_Silver" value="添&nbsp;加" onclick="updateRowDic('insert','<%=columnname %>','<%=columntype %>','<%=pkcolumn %>');"> 
						<input type="button" id="dic_editbutton" value="修&nbsp;改" onclick="updateRowDic('update','<%=columnname %>','<%=columntype %>','<%=pkcolumn %>');" class='Button_Silver' style="display:none">
						 &nbsp; 
						<input type="button" value="取&nbsp;消" onclick="cancel();" class="Button_Silver">
						<br>
					</td>
				</tr>
			</table>
		</form>
	</div>

	</div>
</body>
</html>
