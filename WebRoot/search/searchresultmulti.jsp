<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="page.tld" prefix="page"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<html:base />
	<title>查询结果(复选框)--返回多条记录</title>
	<%@ include file="../include/meta.inc"%>
	<script type="text/javascript" src="../common/validate.js"></script>
	<script type="text/javascript" src="../common/tableManager.js"></script>
	<script type="text/javascript" src="../common/searchUtils.js"></script>
	<script type="text/javascript" src="../common/jquery.js"></script>	
	<link href="../newcss/style.css" rel="stylesheet" type="text/css" />
	<link href="../newcss/add.css" rel="stylesheet" type="text/css" />
</head>
<body onload="init();" scroll="no">
	<script type="text/javascript">
	//全选标识
	var allCheckInt = 0; 
	Array.prototype.contains = function(element){
		for(var i = 0; i < this.length ; i++){
			if(this[i] == element){
				allCheckInt++;
				return i;
			}
		}
		return "";
	}
	var selectrow=0;
	var titlelen = 0;
	//提交查询前设置查询条件
	function checkparamter () {
		return setSearchParamter({searchcol:document.getElementsByName("searchcol")[0].value,
				searchoper:document.getElementsByName("searchcond")[0].value,
				searchfield:'searchfield'});
	}	
	//双击行事件
	
	function dblClick (obj) {
		parent.newSearchLine("<%=request.getParameter("forward") %>");
		onSearchdblClick(obj, {fields:document.getElementsByName("searchfields")[0].value,
				targets:document.getElementsByName("targetfields")[0].value,
				parobj:document.getElementsByName("consistfields")[0].value});
		parent.doSearchFinished();
	}
	
	//添加选中数据到父页面
	function add(){
		var item = document.getElementsByName("mxitems");
		if(item){
			for(var i=0; i<item.length; i++){
				if(item[i].checked){
					parent.newSearchLine("<%=request.getParameter("forward") %>");
					var obj=item[i].parentElement;
					while(obj.tagName!="TR") obj = obj.parentElement;
					onSearchmultiClick(obj, {fields:document.getElementsByName("searchfields")[0].value,
					targets:document.getElementsByName("targetfields")[0].value,
					parobj:document.getElementsByName("consistfields")[0].value});
				}
			}
		}
		parent.doSearchFinished();
		
	}
	//初始化多选
	function init () {
		document.getElementsByName("searchfield")[0].focus();
		//var selectrows = document.getElementById("selected").value.split(',');
		//if(selectrows != "null" && selectrows != ""  ){
		//	setSelected(selectrows);	
		//}
		//设置全选
		//if(allCheckInt == document.getElementsByName("mxitems").length){
		//	document.getElementsByName("all")[0].checked= true;
		//}
	}
	//设置选中项
	function setSelected (selectrows) {
		var comparaKey = new Array();
		var comparaValue = new Array();
		var items = document.getElementsByName("mxitems");	
		for (var i = 0; i < items.length; i++) {
			comparaKey[i] = items[i].value;
			comparaValue[i] = items[i];
		}
		for(var i = 0; i < selectrows.length; i++){
			comparaValue[comparaKey.contains(selectrows[i])].checked = true;
		}
		
	}
	
  </script>
	<s:form  method="post" onsubmit="checkparamter();">
		<s:hidden name="searchfields" value="%{#parameters.searchfields[0]}" />
		<s:hidden name="targetfields" value="%{#parameters.targetfields[0]}" />
		<s:hidden name="consistfields" value="%{#parameters.consistfields[0]}" />
		<s:hidden name="baseconditions" value="%{#parameters.baseconditions[0]}" />
		<s:hidden name="queryparamter" value="%{#request.queryparamter}" />
		<s:hidden name="forward" value="%{#parameters.forward[0]}" />
		<s:hidden name="searchcol" value="%{#request.searchcol}" />
		<s:hidden name="searchcond" value="%{#request.searchcond}" />
		<s:hidden name="multiselect" value="%{#request.multiselect}" />
		<input type="hidden" name="selected" id="selected" value="%{#request.selected}">
		<table class="tb_b1" cellpadding="0" align="center"  border="0">
			
			<tr>
				<td>
					<table width="100%" border="0" class="tableSearch1">
						<%
			    			String searchcol = (String)request.getAttribute("searchcol");
			    			String[] cols = searchcol.split(",");
			    			int i=0;
			    		%>
			    		<s:iterator id="stitle" value="#request.searchtitle">
							<tr>
								<td class="wtxt"  align="right">
									<s:property value="stitle" />:
								</td>
								<%
							    	if(cols[i].indexOf("[")<=0){
							    %>
								<td>
									<input type="text" name="searchfield" class="jqInp wdateInp" value="" >
									
								</td>
								<td>&nbsp;</td>
								<%
									}else{
										String dics = cols[i].substring(cols[i].indexOf("[")+1,cols[i].indexOf("]"));
										String[] diccol = dics.split("//");
								%>
								<td>
									<select name="searchfield">
										<option></option>	
											<html:optionsCollection name="dic" property="<%=diccol[0] %>" value="<%=diccol[1] %>"
											label="<%=diccol[2] %>"/>	
									</select>
								</td>
								<%
									}
									i++;
								%>
							</tr>
						</s:iterator>
					</table>
				</td>
				<td>
					<table width="100%" border="0" class="tableSearch1">
						<tr>
							<td>
								<input type="submit" class="subGg" class="Button_Silver" value="查询" />
							</td>
						</tr>
						<tr>
							<td>
								<input type="button" name="" class="subGg" class="Button_Silver" value="确定" onclick="add();" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</s:form>
	 <div class="search">
               <div class="brandTit"><h3>结果列表<span class="bgRight"></span></h3></div>
               <div class="overY">
                 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tab">
						<colgroup style="width:10%"/>
						<s:iterator id="col" value="#request.colwidth">
							<colgroup style="width:<s:property value="col"/>" />
						</s:iterator>
					<tr class="" align="center">
						<td  nowrap>
							<input type="checkbox" name="all" id="all" onclick="allCheck(this,'mxitems');">全选
						</td>
						<s:iterator id="title" status="idxid" value="#request.titles">
							<td  nowrap><s:property value="title" /></td>
						</s:iterator>
					</tr>
					<s:iterator id="result" value="#request.list">
						<tr class="list_table_rows_tr list_table_rows_td" title="请选中后确定" onclick="selectSearchRow(this);" ondblclick="dblClick(this)" style="cursor:hand" align="center">
							<td >
								<input type="checkbox" name="mxitems" id="mxitems" value="">
							</td>
							<s:iterator id="rescol" status="sta" value="#request.resultcol">
					    		<s:if test="#sta.index< #request.titlelength">
								
										<td>
										<s:property value="%{#result[#rescol]}" />
										<s:hidden name="%{#rescol}" value="%{#result[#rescol]}" />
										</td>	
								</s:if>
								<s:else>
									<s:hidden name="%{#rescol}" value="%{#result[#rescol]}" />
								</s:else>
							</s:iterator>
						</tr>
					</s:iterator>
            </table> 
             <%@ include file="../include/pageforsearch.inc"%>
          </div> 
          </div>
    
</body>
</html:html>
