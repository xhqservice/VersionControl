    //列表导出按钮
    function DC () {
		document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		document.write("	<tr align='center'>");
		document.write("		<td nowrap>");
		document.write("			<input type=button Class='daochu' value='' onclick='savelist()'>");
		document.write("		</td>");
		document.write("	</tr>");
		document.write("</table>");
	}
    
    //列表打印、导出按钮
    function PE (pIds) {
    	document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
    	document.write("	<tr align='center'>");
    	document.write("		<td nowrap>");
    	document.write("			<input type=button Class='dayin' value='' onclick='printCurrentPage(\"" + pIds + "\")'>");
    	document.write("			<input type=button Class='daochu' value='' onclick='savelist()'>");
    	document.write("		</td>");
    	document.write("	</tr>");
    	document.write("</table>");
    }
    //列表打印、导出、返回按钮
    function PEB (pIds) {
    	document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
    	document.write("	<tr align='center'>");
    	document.write("		<td nowrap>");
    	document.write("			<input type=button Class='dayin' value='' onclick='printCurrentPage(\"" + pIds + "\")'>");
    	document.write("			<input type=button Class='daochu' value='' onclick='savelist()'>");
    	document.write("			<input type=button Class='fanhui' value='' onclick='javascript:window.history.back()'>");
    	document.write("		</td>");
    	document.write("	</tr>");
    	document.write("</table>");
    }
  //列表返回按钮
    function BACK (pIds) {
    	document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
    	document.write("	<tr align='center'>");
    	document.write("		<td nowrap>");
    	document.write("			<input type=button Class='fanhui' value='' onclick='javascript:window.history.back()'>");
    	document.write("		</td>");
    	document.write("	</tr>");
    	document.write("</table>");
    }
    
  //列表关闭按钮
    function CLS (pIds) {
		document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		document.write("	<tr align='center'>");
		document.write("		<td nowrap>");
		document.write("			<input type=button Class='guanbi' value='' onclick='cancel()'>");
		document.write("		</td>");
		document.write("	</tr>");
		document.write("</table>");
	}
	
	//列表添加、打印、导出按钮
    function PEadd (pIds) {
		document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		document.write("	<tr align='center'>");
		document.write("		<td nowrap>");
		document.write("			<input type=button Class='tianjia' value='' onclick='add()'>");
		document.write("			<input type=button Class='dayin' value='' onclick='printCurrentPage(\"" + pIds + "\")'>");
		document.write("			<input type=button Class='daochu' value='' onclick='savelist()'>");
		document.write("		</td>");
		document.write("	</tr>");
		document.write("</table>");
	}
	
    //列表打印按钮
    function PE_oneButton (pIds) {
		document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		document.write("	<tr align='center'>");
		document.write("		<td nowrap>");
		document.write("			<input type=button Class='dayin' value='' onclick='printCurrentPage(\"" + pIds + "\")'>");
		document.write("		</td>");
		document.write("	</tr>");
		document.write("</table>");
	}
    //关闭当前窗口  add by 罗平
    function closeWin(){
    	window.close();
    }
    //列表打印、导出、关闭按钮  add by 罗平
    function PE_exportCloseButton (pIds) {
		document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		document.write("	<tr align='center'>");
		document.write("		<td nowrap>");
		document.write("			<input type=button Class='dayin' value='' onclick='printCurrentPage(\"" + pIds + "\")'>");
		document.write("			<input type=button Class='daochu' value='' onclick='savelist()'>");
		document.write("			<input type=button Class='guanbi' value='' onclick='closeWin()'>");
		document.write("		</td>");
		document.write("	</tr>");
		document.write("</table>");
	}
    //列表打印、关闭按钮  add by 罗平
    function PE_closeButton (pIds) {
		document.write("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");
		document.write("	<tr align='center'>");
		document.write("		<td nowrap>");
		document.write("			<input type=button Class='dayin' value='' onclick='printCurrentPage(\"" + pIds + "\")'>");
		document.write("			<input type=button Class='guanbi' value='' onclick='closeWin()'>");
		document.write("		</td>");
		document.write("	</tr>");
		document.write("</table>");
	}
	
    //列表保存
	function savelist() {
		var pageform = document.getElementsByName("pager")[0];
		var url = window.location.href;
		if(url.substring(url.length - 1) == "#"){
			url = url.substring(0 , url.length - 1);
		}
		if(url.indexOf("saveaction=savelist")<0) {
			if(url.indexOf("?") > 0) {
				var para = url.substring(url.indexOf("?") + 1);
				url = url.substring(0 , url.indexOf("?"));
				url = url + "?saveaction=savelist&" + para;
			} else {
				url = url + "?saveaction=savelist";
			}
				
			pageform.action = url;			
		}
		pageform.target = "_blank";
		pageform.submit();
		pageform.action = "";
		pageform.target = "_self";
	}
	
	//排序
	function setSort(fieldname){
		var pageform = document.getElementsByName("pager")[0];		
		//var queryparamter = document.getElementById("queryparamter");
		/*ZFJ 修改排序功能*/
		var queryparamter = pageform.queryparamter;
		if(queryparamter) {
			var param = queryparamter.value;
			if(param.indexOf("db_resultorderby~=~")>=0){
				if(param.indexOf("db_resultorderby~=~" + fieldname)>=0){
					if(param.substr(param.length-5)==" DESC") {
						queryparamter.value = param.substr(0, param.length-5);
					} else {
						queryparamter.value = param + " DESC";
					}
				} else {
					queryparamter.value = param.substr(0,param.indexOf("db_resultorderby~=~")) 
						+ "db_resultorderby~=~" + fieldname;
				}
			} else {
				if(param!=null && param!="" && param.substring(0,2)=="&&") param = param + "~";
				else param = "&&";
				queryparamter.value = param + "db_resultorderby~=~" + fieldname;
			}
		} else {
			var url = window.location.href;
			if(url.indexOf("?")>0) {
				url = url + "&";
			} else {
				url = url + "?";
			}
			pageform.action = url + "queryparamter="+encodeURIComponent("&&db_resultorderby~=~" + fieldname);
		}
		var choice = document.getElementById("choice");
		if(choice) choice.value = "first";
		
		pageform.submit();
	}
	
	//点击排序
	function clickSort(indexlist,fieldlist) {
		var objTD = window.event.srcElement; 
		
		if(objTD.tagName != "TD" && objTD.tagName != "TH"){
			objTD = objTD.parentElement;
			if(objTD.tagName != "TD" && objTD.tagName != "TH") return;
		}
		
		for(var i=0;i<indexlist.length && i<fieldlist.length;i++) {
			if(indexlist[i]==objTD.cellIndex) {
				setSort(fieldlist[i]);
				break;
			}
		}
	}
	
	//设置排序标志
	function setSortflag(indexlist,fieldlist,tableid){
		/*ZFJ 2008-07-17 修改排序功能*/
		//var queryparamter = document.getElementById("queryparamter");
		var pageform = document.getElementsByName("pager")[0];	
		if(!pageform){
			return;
		}
		var queryparamter = pageform.queryparamter;
		/*ZFJ 2008-07-17 修改结束*/
		if(queryparamter) {
			var param = queryparamter.value;
			if(param.indexOf("db_resultorderby~=~")>=0){
				var sortfield = param.substr(param.indexOf("db_resultorderby~=~")+19);
				var sortstr = "↑";
				if(sortfield.substr(sortfield.length-5)==" DESC") {
					sortfield = sortfield.substr(0, sortfield.length-5);
					sortstr = "↓";
				} 
				
				for(var i=0;i<fieldlist.length && i<indexlist.length;i++){
					if(fieldlist[i]==sortfield) {
						setCellFlag(tableid, indexlist[i],sortstr);
						break;
					}
				}
			}
		}
		var objTable = document.getElementById(tableid);
		if(!objTable) {
			try {
				objTable = document.getElementsByTagName("TABLE")[tableid];
			} catch(e) {
				return;
			}
		}
		if(objTable) {
			var objTr = objTable.rows[0];
			for(var i=0;i<fieldlist.length && i<indexlist.length;i++){ 
				var objCell = objTr.cells[indexlist[i]];
				if(objCell) {
					objCell.style.fontWeight = 'bold';
				}
			}
			
		}
		
	}
	
	//设置对应表格的标志
	function setCellFlag(tableid, cellindex, flagtext) {
		var objTable = document.getElementById(tableid);
		if(!objTable) {
			try {
				objTable = document.getElementsByTagName("TABLE")[tableid];
			} catch(e) {
				return;
			}
		}
		if(objTable) {
			var objTr = objTable.rows[0];
			var objCell = objTr.cells[cellindex];
			if(objCell) {
				objCell.innerHTML = objCell.innerHTML + "<font color=orange>"  + flagtext + "</font>";
			}
		}
	}
