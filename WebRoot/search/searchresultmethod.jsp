<%@ page language="java"  pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="page.tld" prefix="page"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<html:base />
	<%@ include file="../include/meta.inc"%>
	<!-- <link rel="stylesheet" type="text/css" href="../common/main.css"> -->
	<link href="../newcss/style.css" rel="stylesheet" type="text/css" />
	<link href="../newcss/add.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../common/tableManager.js"></script>
	<script type="text/javascript" src="../common/searchUtils.js"></script>
	<title>查询结果</title>
	<script type="text/javascript">
	var selectrow=0;
	var titlelen = 0;
	//提交查询前设置查询条件
	function checkparamter(){
	  	return setSearchParamter({searchcol:document.getElementsByName("searchcol")[0].value,
	  		searchoper:document.getElementsByName("searchcond")[0].value,
	  		searchfield:'searchfield'});
	  	
	}
	  
	//双击行事件
	function dblClick(obj){ 
		onSearchdblClick(obj, {fields:document.getElementsByName("searchfields")[0].value,
	  		targets:document.getElementsByName("targetfields")[0].value,
	  		parobj:document.getElementsByName("consistfields")[0].value});
  
	  	parent.doSearchFinished();
	  	//如果不是IE浏览器则执行
	  
	  	if(!(/msie/i.test(navigator.userAgent))||document.documentMode == 10) {
	  		
	  		parent.afterSelect();
	  	}
	}  
	function init(){
		document.getElementsByName("searchfield")[0].focus();
	}
  	</script>
</head>
<body onload="init();" scroll="no">
	<s:form method="post" onsubmit="checkparamter();">
			<s:hidden name="searchfields" value="%{#parameters.searchfields[0]}" />
			<s:hidden name="targetfields" value="%{#parameters.targetfields[0]}" />
			<s:hidden name="consistfields" value="%{#parameters.consistfields[0]}" />
			<s:hidden name="baseconditions" value="%{#parameters.baseconditions[0]}" />
			<s:hidden name="forward" value="%{#parameters.forward[0]}" />
			<s:hidden name="queryparamter" value="%{#request.queryparamter}" />
			<s:hidden name="searchcol" value="%{#request.searchcol}" />
			<s:hidden name="searchcond" value="%{#request.searchcond}" />
			<s:hidden name="multiselect" value="%{#request.multiselect}" />
		<table cellpadding="0" align="center"  border="0">
			<tr>
				<td>
					<table width="100%" border="0" class="tableSearch1">
			    		<s:iterator id="stitle" value="#request.searchtitle">
							<tr>
								<td class="wtxt"  align="right"><s:property />：</td>
								<td>
									<input type="text"  name="searchfield" size="25" class="jqInp wdateInp" />
								</td>
								<td>&nbsp;</td>
							</tr>
							
						</s:iterator>
					</table>
				</td>
				<td>
					<table width="100%" border="0" class="tableSearch1">
						<tr>
							<td>
								<input type="submit" class="subGg" class="Button_Silver"  value="查询"  />
							</td>
						</tr>
						<tr>
							<td>
								<input type="button" class="subGg" class="Button_Silver" name="search_bnt" value="关 闭" onclick="parent.doSearchFinished();" />
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
						<tr>
							<s:iterator status="sta" value="#request.titles">
								<s:if test="#sta.first">
									<td nowrap>
								</s:if>
								<s:elseif test="#sta.last">
									<td  nowrap>
								</s:elseif>
								<s:else>
									<td  nowrap>
								</s:else>
								<s:property />
								
								</td>
							</s:iterator>
						</tr>
                   <s:iterator id="result" value="#request.list">
					<tr ondblclick="dblClick(this)" style="cursor: hand">
						<s:iterator id="rescol" status="sta" value="#request.resultcol">
							<s:if test="#sta.index< #request.titlelength">
							<td  nowrap>
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
</html>