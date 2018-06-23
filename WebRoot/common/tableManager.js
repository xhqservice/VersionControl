	//处理导航信息部分的功能函数
	function pageTitle(title, navigation){
		document.write("	<table width='100%' border='0' cellspacing='0' cellpadding='0' class='title'>");
		document.write("	    <colgroup style='width:100%'>");
		document.write("		<tr>");
		document.write("		    <td>");
		document.write("		        <a href='javascript:void(0);'>" + title + " </a> > <a href='javascript:void(0);'>" + navigation + "</a>");
		document.write("	  		</td>");
		document.write("	  	</tr>");
		document.write("	</table>");
	}
	// 此函数第一个参数为要跳转到的页签的编号,第二个参数为该业内的页签总数
	function ShowTag(sid, totalid) {
		for (i = 1; i < totalid + 1; i++) {
			eval("Tag" + i + ".style.display=\"none\";");
			eval("td" + i + ".className='tab_off';");
		}
		eval("Tag" + sid + ".style.display=\"\";");
		eval("td" + sid + ".className='tab_on'");
	}
	// 点击查看按钮时打开弹出页
	function do_open(url, title){
		var win = window.open(url, title, "height="+screen.height+",width="+screen.width+",top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no");
		win.focus(); 
	}
	//点击查看按钮时打开弹出最大化页面
	function do_openMax(url, title, scroll){
		var w = screen.availWidth;
		var h = screen.availHeight;		
		var win = window.open(url, title, "height="+h+",width="+w+",top=0,left=0,toolbar=no,menubar=no,scrollbars="+scroll+",resizable=yes,location=no,status=no");
		win.focus();
	}
    // 公共增加行
    function addRow(tablename,valuefields,hiddenfields) {
    	var objTable = document.getElementById(tablename);
		var objRow = objTable.insertRow();  // 增加行
		// objRow.bgColor=getRowColor("default",objRow.rowIndex);
		objRow.className = "list_table_rows_tr";
		
		// 增加各列
		for(var i=0;i<valuefields.length && i<hiddenfields.length;i++){
			var objCell = objRow.insertCell();
			objCell.innerHTML = getText(valuefields[i])+getHidden(valuefields[i],hiddenfields[i]);
		}
		
		// 为新增的行提供事件绑定支持
		// objRow.onmouseover = MouseOver;
		// objRow.onmouseout = MouseOut;
		// objRow.onclick = SelectRow;
		return objRow;
    }
    // 设置字段的disabled属性,flag的值为true或false
	function setFieldsDisabled(fields, flag){
		for(var i=0;i<fields.length;i++){
			document.getElementById(fields[i]).disabled = flag;
		}
	}
	
	// 选定指定行
	function SelectRow(){
        var objTR = window.event.srcElement; 
		if(objTR.tagName == "TD")
			objTR = objTR.parentElement;
		if(objTR.tagName != "TR") return;
		
		var objTable = objTR.parentElement;
		if(objTable.tagName != "TABLE")
			objTable = objTable.parentElement;
        var isel = objTable.getAttribute("selectedrow");
        // objTable.rows[isel].bgColor = getRowColor("default",isel);
        objTable.rows[isel].className = "Pop_TR";
        // Add on 2008-08-12 By Zong,公枪配备单位修改-部门及下属单位.为防止信息列表标题行样式随其他行的单击事件而改变
        objTable.rows[0].className = "td_a";
        // objTR.bgColor=getRowColor("selected",0);
        objTable.setAttribute("selectedrow", objTR.rowIndex);
        objTR.className = "Pop_TR1";
    }
    // 鼠标移出
    function MouseOut(){
        var objTR = window.event.srcElement; 
		if(objTR.tagName == "TD")
			objTR = objTR.parentElement;
		var objTable = objTR.parentElement;
		if(objTable.tagName != "TABLE")
			objTable = objTable.parentElement;        
        
        var isel = objTable.getAttribute("selectedrow");
        
        if(objTR.rowIndex!=isel) {
            objTR.bgColor = getRowColor("default",objTR.rowIndex);
        } else {
            objTR.bgColor=getRowColor("selected",0);
        }
        
    }
    // 鼠标移入
    function MouseOver(){
        var objTR = window.event.srcElement;
		if(objTR.tagName == "TD")
			objTR = objTR.parentElement;
		
        objTR.bgColor=getRowColor("over",0);
    }
	// 获取表格行的颜色
	function getRowColor(type, rowindex) {
		if (type == "selected") {
			return "#00ccff";
		}
		if (type == "over") {
			return "#f0f0f0";
		}
		if (rowindex % 2 == 0) {
			return "#ffffff";
		} else {
			return "#eeeeff";
		}
	}
    // 增加隐藏域，目前未使用
    function addHidden(hname, hvalue){
    	var hid = document.createElement("input");
    	hid.setAttribute("type","hidden");
    	hid.setAttribute("value",hvalue);
    	hid.setAttribute("name",hname);
    	hid.setAttribute("id",hname);
    	
    	return hid;
    }
    // 获取域的文本，目前只考虑下拉框和输入框
    function getText(field){
    	var obj = document.getElementById(field);
 
    	if(obj == null)
    		return "";
    	if(obj.length>=0) {
    		if(obj.selectedIndex>=0)
    			return obj.options[obj.selectedIndex].text;
    		else{
    			return "";
    		}
		}
		if(obj.type=="checkbox" || obj.type=="radio"){	
			var fs = document.getElementsByName(field);
			for(var i=0;i<fs.length;i++)
				if(fs[i].checked) return fs[i].value;
		}else
    		return obj.value;
    }
    // 获取域的值
    function getValue(field){
// alert(field);
    	var obj = document.getElementById(field);
    	if(obj == null)
    		return "";

    	if(obj.type=="checkbox" || obj.type=="radio"){	
			var fs = document.getElementsByName(field);
			for(var i=0;i<fs.length;i++)
				if(fs[i].checked) return fs[i].value;
		}else
    		return obj.value;
    	
    }
    // 获取隐藏域HTML文本
    function getHidden(valuefield,hiddenfield){
    	return "<input type=\"hidden\" name=\"" + hiddenfield + "\" value=\""+getValue(valuefield)+"\">";
    }
    // 公共增加行，绑定事件列表
    function addRowEvent(tablename,valuefields,hiddenfields, events){
    	var objTable = document.getElementById(tablename);
		var objRow = objTable.insertRow();  // 增加行
		objRow.bgColor=getRowColor("default",objRow.rowIndex);
		
		// 增加各列
		for(var i=0;i<valuefields.length && i<hiddenfields.length;i++){
			var objCell = objRow.insertCell();
			objCell.innerHTML = getText(valuefields[i])+getHidden(valuefields[i],hiddenfields[i]);
			objCell.className="Pop_Text";
		}
		
		// 为新增的行提供事件绑定支持
		// 默认事件支持
		objRow.onmouseover = MouseOver;
		objRow.onmouseout = MouseOut;
		objRow.onclick = SelectRow;
		// 额外事件支持
		for (key in events) {
			objRow.setAttribute(key, events[key]);
		}
		
		return objRow;
    }
    
    // 表删除行
    function deleteRow(tablename){
    	objTable = document.getElementById(tablename);
    	var isel = objTable.getAttribute("selectedrow");
    	if(isel<1) {
    		alert("请选择要删除的信息！");
    		return false;
    	}
    	
    	if(!confirm("您确定要删除选中的数据吗？")) return false;
    	
    	objTable.deleteRow(isel);
    	objTable.setAttribute("selectedrow","0");
    	
    	var iupdate = objTable.getAttribute("updaterow");
    	if(iupdate==isel)
    		iupdate=0;
    	else if(iupdate>isel)
    		iupdate--;
    	objTable.setAttribute("updaterow",iupdate);
    	
    	return true;
    }
    
    // 表更新行
    function updateRow(tablename, valuefields, hiddenfields){
    	objTable = document.getElementById(tablename);
    	iupdate = objTable.getAttribute("updaterow");
    	if(iupdate<1) {
    		alert("您没有选择要修改的信息，请双击一行进行修改！");
    		return;
    	}
    	objTR = objTable.rows[iupdate];
    	// 更新域值
    	for(var i=0;i<valuefields.length && i<hiddenfields.length;i++){
			// var objHidden =
			// document.getElementsByName(bmhiddenfields[i])[iupdate-1];
			var objCell = objTR.cells[i];
			// objHidden.value =
			// document.getElementById(bmvaluefields[i]).value;
			// objCell.innerText = getText(bmvaluefields[i]);
			// objCell.setText(getText(bmvaluefields[i]));
			objCell.innerHTML = getText(valuefields[i])+getHidden(valuefields[i],hiddenfields[i]);
		}
		return objTR;
    }

 	function RowDblClick(valuefields,hiddenfields){
    	var objTR = window.event.srcElement; 
		if(objTR.tagName == "TD")
			objTR = objTR.parentElement;
		var objTable = objTR.parentElement;
		if(objTable.tagName != "TABLE")
			objTable = objTable.parentElement;
			
		var isel = objTR.rowIndex;
		// 设置表格更新行
		objTable.setAttribute("updaterow",isel);
		for(var i=0;i<valuefields.length && i<hiddenfields.length;i++){
			var objHidden = document.getElementsByName(hiddenfields[i])[isel-1];
			document.getElementById(valuefields[i]).value = objHidden.value;
		}
    }
 
    // 从下拉列表域field中获取value对应的文本
    function getTextByValue(field,value){
    	var obj = document.getElementById(field);
    	if(obj == null)
    		return "";
    	for(var i=0;i<obj.length;i++){
			if (obj[i].value == value){
				return obj[i].text;
			}
		}
		return obj.text;
    }

   	// 获取选中行的rowIndex,并更改选中行的样式
	function selectRow(objTR) {
		var objTable = objTR.parentElement;
		if (selectrow>0) {
			objTable.rows[selectrow].className = "Pop_TR";
		}
		objTR.className = "Pop_TR1";
		selectrow = objTR.rowIndex;
	}

	function selectMultiRow(objTR) {
		var line1,line2;
		var objTable = objTR.parentElement;
		for(var i=0;i<objTable.rows.length;i++){
			objTable.rows[i].className = "Pop_TR";
		}
		objTR.className = "Pop_TR1";
       	if(event.shiftKey){
       		if(selectrow == 0){// 按住shift选择时,之前需要选择一行.
       			alert("请先选择起始行！");
       			return 0;
       		}
       		if(selectrow!=objTR.rowIndex){
       			if((selectrow + "").indexOf(",")>0){// 如果上一次也是按住shift选择的情况,则selectrow的值格式为[ling1,line2].这里取line1
       				selectrow = selectrow.split(",")[0];
       			}
	       		if(selectrow<objTR.rowIndex){
	       			line1 = selectrow;
	       			line2 = objTR.rowIndex;
	       		}else if(selectrow>objTR.rowIndex){
	       			line1 = objTR.rowIndex;
	       			line2 = selectrow;
	       		}
				for(var i=line1;i<line2;i++){
					objTable.rows[i].className = "Pop_TR1";
				}
	       		selectrow = line1 + "," + line2;
       		}
       	}else{
			selectrow = objTR.rowIndex;
		}
		objTable.rows[0].className = "td_a";
	}
		

	/**
	 * 复选框的全选与全部取消 all:全选框名称 alone:其他框名称 Add on 2008-05-30
	 */
	function selectAll(all, alone){
		var alone_ = document.getElementsByName(alone);
		if (document.getElementById(all).checked == true){
			for(var i=0;i<alone_.length;i++){
				if(!alone_[i].disabled){
					alone_[i].checked = true;
				}
			}
		}else{
			for(var i=0;i<alone_.length;i++){
				if(!alone_[i].disabled){
					alone_[i].checked = false;
				}
			}
		}
	}
	
	// 设置网页打印的页眉页脚为空
	function pagesetup_null(){
	 	var hkey_root,hkey_path,hkey_key;
	 	var hkey_root="HKEY_CURRENT_USER";
	 	var hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
	 	try{
			var RegWsh = new ActiveXObject("WScript.Shell");
			hkey_key="header";
			RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
			hkey_key="footer";
			RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
	 	}catch(e){}
	}
	
	function setFrameHeight(frame, len){
		if(parseInt(len) < 5){
			document.getElementById(frame).height = (parseInt(len) * 70) ;
		}else{
			document.getElementById(frame).height = (parseInt(len) * 30) ;
		}
	}
	// 公共增加行
    function addRowWithDelete(tablename,valuefields,hiddenfields) {
    	var objTable = document.getElementById(tablename);
		var objRow = objTable.insertRow();  // 增加行
		
		// 增加各列
		for(var i=0;i<valuefields.length && i<hiddenfields.length;i++){
			var objCell = objRow.insertCell();
			objCell.innerHTML = getText(valuefields[i])+getHidden(valuefields[i],hiddenfields[i]);
// alert(objCell.innerHTML);
		}
		
		// 添加删除按钮
		var objCell = objRow.insertCell();
    	objCell.innerHTML = "<img src=\"../images/er/shanchu1.gif\" alt=\"删除\" onmousemove=\"this.src='../images/er/shanchu2.gif'\" onmouseout=\"this.src='../images/er/shanchu1.gif'\" onclick=del(this) style='cursor:hand' border='0'>";
    	// alert(objCell.innerHTML);
		return objRow;
    }
	
	// 添加删除按钮
	function addDeleteRow(objRow,tablename){
    	var objCell = objRow.insertCell();
    	objCell.innerHTML = "<img src=\"../images/er/shanchu1.gif\" alt=\"删除\" onmousemove=\"this.src='../images/er/shanchu2.gif'\" onmouseout=\"this.src='../images/er/shanchu1.gif'\" onclick=del(this) style='cursor:hand' border='0'>";
    }
	
	// 动态获得隐藏域名称
    function getHiddenfields(i,object,hiddenfields){
    	var newHiddenfields = new Array(); 
    	for(var j=0;j<hiddenfields.length;j++){
    		newHiddenfields[j] = object+"["+i+"]."+hiddenfields[j];
    		// alert(newHiddenfields[j]);
    	}
    	return newHiddenfields;
    }
	    
    // 删除行
    function del(obj){
    	var rowObj = obj.parentNode.parentNode;
   		if(!confirm("您确定要删除选中的数据吗？")) return false;
   		rowObj.parentNode.deleteRow(rowObj.rowIndex);
   	} 
    
    /**
	 * 增加一行，有隐藏域，有删除按钮
	 * 
	 * @param tablename
	 *            html table名称
	 * @param showfields
	 *            显示到列表上的域
	 * @param hiddenfields
	 *            隐藏域
	 * @param hiddenvaluefields
	 *            隐藏域的值
	 * @returns
	 */
    function addRowHiddenDelete(tablename,showfields,hiddenfields,hiddenvaluefields) {
    	var objTable = document.getElementById(tablename);
		var objRow = objTable.insertRow();  // 增加行
		
		// 增加各列
		for(var i=0;i<showfields.length && i<hiddenfields.length;i++){
			var objCell = objRow.insertCell();
			objCell.innerHTML = getText(showfields[i])+getHidden(hiddenvaluefields[i],hiddenfields[i]);
// alert(getHidden(hiddenfields[i],hiddenvaluefields[i]));
			// alert(objCell.innerHTML);
		}
		
		// 添加删除按钮
		var objCell = objRow.insertCell();
    	objCell.innerHTML = "<img src=\"../images/er/shanchu1.gif\" alt=\"删除\" onmousemove=\"this.src='../images/er/shanchu2.gif'\" onmouseout=\"this.src='../images/er/shanchu1.gif'\" onclick=del(this) style='cursor:hand' border='0'>";
    	// alert(objCell.innerHTML);
		return objRow;
    }

    // 公共增加行
    function addRow_s2(tablename,objList,valuefields,hiddenfields) {
    	
    	var objTable = document.getElementById(tablename);
		var objRow = objTable.insertRow();  // 增加行
		var i=0, currentRow = objTable.rows.length;
		var hiddenvaluse = "";
		objRow.className = "list_table_rows_tr";
		
		// 增加各列
		for(;i<valuefields.length && i<hiddenfields.length;i++){
			var objCell = objRow.insertCell();
			// objCell.innerHTML =
			// getText(valuefields[i])+getHidden(valuefields[i],hiddenfields[i]);
			objCell.innerHTML = getText(valuefields[i]) + ("<input type=\"hidden\" name=\"" + objList+ "[" + currentRow + "]." + hiddenfields[i] + "\" value=\"" + getValue(hiddenfields[i]) + "\">");
		}
		
		for(;i<hiddenfields.length;i++){
			hiddenvaluse += "<input type=\"hidden\" name=\"" + objList+ "[" + currentRow + "]." + hiddenfields[i] + "\" value=\"" + getValue(hiddenfields[i]) + "\">";
		}
		var objCell = objRow.insertCell();
    	objCell.innerHTML = "<img src=\"../images/er/shanchu1.gif\" alt=\"删除\" onmousemove=\"this.src='../images/er/shanchu2.gif'\" onmouseout=\"this.src='../images/er/shanchu1.gif'\" onclick=del(this) style='cursor:hand' border='0'>" + hiddenvaluse;
    	return objRow;
    }
    //加载时调整页面布局 create by huangbotao date:2012/07/04
    function loadInit(){
    	if(screen.width>=1280&&screen.height>=720){
    		$("#tbody").addClass("tbody2");
			$.each($("td[colspan=3]"), function(n, value) {
				$("td[colspan=3]").eq(n).children().eq(0).addClass("colspan2");
			});
		}
		else{
			$("#tbody").addClass("tbody");
			$.each($("td[colspan=3]"), function(n, value) {	
				$("td[colspan=3]").eq(n).children().eq(0).addClass("colspan");
			});
		}				
    }