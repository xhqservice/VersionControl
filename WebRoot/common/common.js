//**********************处理列表页面查询条件显示部分及重新查询返回上次查询条件的功能函数***************************
var win, searchConditionDiv;
/**
 * 输出查询条件的基本信息
 * param url:点击重新查询需要跳转的页面
 */
function searchConditions(url){
	document.write("	<table width='100%' border='0' cellpadding='0' cellspacing='0' height='25'>");
	document.write("	    <colgroup style='width:94%'>");
	document.write("	    <colgroup style='width:6%'>");
	document.write("		<tr>");
	document.write("		    <td nowrap>查询条件&gt;");
	document.write("		        <span id='searchConditions' onmouseover='showSearchCondition(1, 300, 200)' onmouseout='showSearchCondition(0, 300, 200)'>");
	document.write("		        </span>");
	document.write("	  		</td>");
	document.write("		    <td nowrap>");
	document.write("		        <input type='button' value='重新查询' onclick=gotoReSearchUrl('" + url + "');>");
	document.write("	  		</td>");
	document.write("	  	</tr>");
	document.write("	</table>");
}

/**
 * 输出查询条件的基本信息
 * param url:点击重新查询需要跳转的页面
 */
function initSearchConditions(){
	if(document.getElementsByName("queryparamtername")[0].value == ""){
		return false;
	}
	var conditions = document.getElementsByName("queryparamtername")[0].value.split(";");
	var condition = "";
	if(conditions.length > 0){
		/*第一个参数*/
		if((conditions[0] != null) && (conditions[0] != "")){
			/*长度大于30*/
			if(conditions[0].split("~")[1].length > 30){
				document.getElementById("searchConditions").innerHTML = "<font color='#FF0000'>" + conditions[0].split("~")[0] + "</font>" + conditions[0].split("~")[1].substring(0 , 30) + "...";
				return;
			}			
			condition += ("<font color='#FF0000'>" + conditions[0].split("~")[0] + "</font>" + conditions[0].split("~")[1]);
		}
		/*第二个参数*/
		if((conditions[1] != null) && (conditions[1] != "")){
			/*长度大于30*/
			if(conditions[1].split("~")[1].length > 15){
				document.getElementById("searchConditions").innerHTML = condition + "，<font color='#FF0000'>" + conditions[1].split("~")[0] + "</font>" + conditions[1].split("~")[1].substring(0 , 15) + "...";
				return;
			}			
			condition += ("，<font color='#FF0000'>" + conditions[1].split("~")[0] + "</font>" + conditions[1].split("~")[1]);
		}		
		/*如果条件大于两个*/
		if(conditions.length > 2){
			condition = condition + "...";
		}		
	}
	document.getElementById("searchConditions").innerHTML = condition;	
}
/**
 * 点击重新查询按钮时提交页面
 * param url:点击重新查询需要跳转的页面
 */
function gotoReSearchUrl(url){
	document.forms("research").action = url.substring(0, url.indexOf(".do")) + "research.do" + url.substring(url.indexOf(".do") + 3, url.length);
	document.forms("research").submit();
}
/**
 * 在查询结果列表页面上显示查询条件
 * param flag:是否显示查询条件层,true显示false隐藏
 * 可以覆盖此函数来改变默认层的大小
 * 示例:在特定的jsp页面中写入如下代码
	function showSearchCondition(flag, width, height){
		customSearchCondition(flag, 500, 400);
	} 
 */
function showSearchCondition(flag, width, height){
	customSearchCondition(flag, width, height);
}
/**
 * 在查询结果列表页面上显示查询条件
 * param flag:是否显示查询条件层,true显示false隐藏
 * param width:层宽度
 * param height:层高度
 */
function customSearchCondition(flag, width, height){
	if(document.getElementsByName("queryparamtername")[0].value == ""){
		return false;
	}
	var context = "";
	var conditions = document.getElementsByName("queryparamtername")[0].value.split(";");
	for(var i=0;i<conditions.length;i++){
		context += (conditions[i].split("~")[0] + conditions[i].split("~")[1] + "<br>");
	}
	if(flag == 1){
		if(searchConditionDiv == null){					
			var left = event.clientX;
			var top = event.clientY;
			searchConditionDiv = new xWin("xWin-1", width, height, left, top, "查询条件", context);					
		}else{
			searchConditionDiv.show();
		}
	}else if(flag == 0){
		searchConditionDiv.close();				
	}
}

//**********************处理导航信息部分的功能函数***************************
function pageTitle(navigation){
	document.write("	<table width='100%' border='0' cellspacing='0' cellpadding='0' align='center'>");
	document.write("	    <colgroup style='width:100%'>");
	document.write("		<tr>");
	document.write("		    <td nowrap>");
	document.write("		        <p style='margin:8px 4px;'><b><font color=#ff6868><img src='../images/daohang.gif' style='margin:0px 0px;' alt='' /></font>当前您的位置：</b>" + navigation + "</p>");
	document.write("	  		</td>");
	document.write("	  	</tr>");
	document.write("	</table>");
}

//**********************处理模式对话框部分的功能函数***************************
//显示模式对话框
function modalDialog(url, title, height, width){
	var ret = window.showModalDialog(url,title,"dialogHeight:"+height+"px;dialogWidth:"+width+"px;center:Yes;resizable:Yes;status:Yes;");
	return ret;
}

//**********************处理复选框部分的功能函数***************************
/**
 *	复选框的全选与全部取消
 *  param allCheckboxName:全选框的name属性的值
 *  param checkboxName:除全选框之外的其他框的name属性的值
 *  用例:<input type="checkbox" border="0" name="selAll"	onclick="selectAll('selAll', 'sel');setSelectKeys(this)"> 全选
 */
function selectAll(allCheckboxName, checkboxsName){
	var alone = document.getElementsByName(checkboxsName);
	if (document.getElementById(allCheckboxName).checked == true){
		for(var i=0;i<alone.length;i++){
			if(!alone[i].disabled){
				alone[i].checked = true;
			}
		}
	}else{
		for(var i=0;i<alone.length;i++){
			if(!alone[i].disabled){
				alone[i].checked = false;
			}
		}
	}
}
/**
 * 单击checkbox时维护一个选中框的关键字数组(selectKeys)和最后一个被点击的CheckBox所在的行号(currentRow).
 * selectKeys格式用于数据库查询语句的in中.
 * param obj:当前点击的checkbox对象
 * param checkboxsName:除全选框之外的其他框的name属性的值
 * 该方法设置三个全局变量:
 * currentSelectedRow:当前被选中checkbox所在的行数
 * checkboxsSelectKeys:当前被选中checkbox的value的值拼成的串,被单引号包含,以逗号做分割
 * checkboxsSelectCount:当前被选中checkbox的总数
 * 用例:<input type="checkbox" name="sel" onclick="setSelectKeys(this);" value="<bean:write name="" property=""/>">
 */
var currentSelectedRow, checkboxsSelectKeys = "", checkboxsSelectCount = 0;
function setSelectKeys(obj, checkboxsName){
	checkboxsSelectCount = 0;
	checkboxsSelectKeys = "";
	sels = document.getElementsByName(checkboxsName);
	for(var i=0;i<sels.length;i++){
		if(sels[i].checked){
			checkboxsSelectKeys += ("'" + sels[i].value + "',");
			checkboxsSelectCount++;
		}
	}
	if(obj != -1){
		currentSelectedRow = obj.parentElement.parentElement.rowIndex
	}
}
/**
 * 判断有没有选中一条操作信息,改方法要和setSelectKeys(obj, checkboxsName)同时使用
 */
function isSelectOneInfo(){
   	if(checkboxsSelectKeys == "") {
   		alert("请先选择您要操作的信息！");
   		return false;
   	}
   	return true;
}
//**********************处理查询页面部分的功能函数***************************

/**
 *	控制对象的显示或隐藏
 *  param becontroler:被控制对象,主要是详细查询条件或列表部分
 *  param controler:控制对象
 */
function controltable(becontroler, controler) {
	if(document.getElementById(becontroler).style.display=='none') { 
		document.getElementById(becontroler).style.display = 'block';
		document.getElementById(controler).innerHTML = "－";
	} else {
		document.getElementById(becontroler).style.display = 'none';
		document.getElementById(controler).innerHTML = "＋";
	}
}
function show_table(becontroler, controler) {
	if(document.getElementById(becontroler).style.display=='none') { 
		document.getElementById(becontroler).style.display = 'block';
		document.getElementById(controler).innerHTML = "－";
	} else {
		document.getElementById(becontroler).style.display = 'none';
		document.getElementById(controler).innerHTML = "＋";
	}
}
/**
 *	控制基础查询与高级查询的切换
 *  param jccxTable:基础查询表
 *  param gjcxTable:高级查询表
 */
function control_cxtj(jccxTable, gjcxTable) {
	if(document.getElementById(jccxTable).style.display=='block') { 
		document.getElementById(gjcxTable).style.display = 'block';
		document.getElementById(jccxTable).style.display = 'none';
	} else {
		document.getElementById(gjcxTable).style.display = 'none';
		document.getElementById(jccxTable).style.display = 'block';
	}
}
/**
 *	功能：判断是否是数字
 *  是：return true.
 *  否：return false.
 *  
 */
function isDigit(s) {   
	var patrn=/^[0-9]{1,20}$/;   
	if (patrn.test(s)) return true;
	return false;   
}
//***********************************************************

function ShowTag(sid, totalid) {
	for (i = 1; i < totalid + 1; i++) {
		eval("Tag" + i + ".style.display=\"none\";");
		eval("td" + i + ".className='tab_off';");
	}
	eval("Tag" + sid + ".style.display=\"\";");
	eval("td" + sid + ".className='tab_on'");
}

/**
 *	其他模块链接到人员管理模块的人员详细信息
 *  param url:访问人员详细信息的地址,可为空
 *  param paras:参数列表,可为空,格式:   key1&value1@key2&value2......
 */
function gotoRyxxxx(url, paras){
	var para;
	try{
		if(url == ""){
			url = "../rygl/ryxx.html?1=1";
		}
		if(paras != ""){
			var array1 = paras.split("@");
			for(var i=0;i<array1.length;i++){
				url += ("&" + array1.split("&")[0] + "=" + array1.split("&")[1]);
			}
		}
	}catch(e){
	}
	window.open(url,"ryxx","");
}

/**
 *	其他模块链接到单位管理模块的单位详细信息
 *  param url:访问单位详细信息的地址,可为空
 *  param paras:参数列表,可为空,格式:   key1&value1@key2&value2......
 */
function gotoDwxxxx(url, paras){
	var para;
	try{
		if(url == ""){
			url = "../dwgl/dwzhxx.html?1=1";
		}
		if(paras != ""){
			var array1 = paras.split("@");
			for(var i=0;i<array1.length;i++){
				url += ("&" + array1.split("&")[0] + "=" + array1.split("&")[1]);
			}
		}
	}catch(e){
	}
	window.open(url,"dwxx","");
}

//获取当前日期
function date() {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth(), 10)+1;
	var day = parseInt(date.getDate(), 10);
	if(month < 10) {
       month = "0" + month;
    }
    if(day < 10) {
       day = "0" + day;
    }
	return year + "-" + month + "-" + day;
}
//获取下个月日期
function dateNextOneMonth() {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth(), 10) + 2;
	var day = parseInt(date.getDate(), 10);
	if(month > 12) {
        year = year + 1;
        month = month - 12;
    }
	if(month < 10) {
       month = "0" + month;
    }
    if(day < 10) {
       day = "0" + day;
    }
	return year + "-" + month + "-" + day;
}
//获取后3个月日期
function dateAfterThreeMonth() {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth(), 10) + 4;
	var day = parseInt(date.getDate(), 10);
	if(month > 12) {
        year = year + 1;
        month = month - 12;
    }
	if(month < 10) {
       month = "0" + month;
    }
    if(day < 10) {
       day = "0" + day;
    }
	return year + "-" + month + "-" + day;
}
//获取后6个月日期
function dateAftersixMonth() {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth(), 10) + 7;
	var day = parseInt(date.getDate(), 10);
	if(month > 12) {
        year = year + 1;
        month = month - 12;
    }
	if(month < 10) {
       month = "0" + month;
    }
    if(day < 10) {
       day = "0" + day;
    }
	return year + "-" + month + "-" + day;
}
//获取下一年日期
function dateAfterOneYear() {
	var date = new Date();
	var year = parseInt(date.getYear(), 10) + 1;
	var month = parseInt(date.getMonth(), 10) + 1;
	var day = parseInt(date.getDate(), 10);
	if(month < 10) {
       month = "0" + month;
    }
    if(day < 10) {
       day = "0" + day;
    }
	return year + "-" + month + "-" + day;
}
//获取下两年日期
function dateAfterTwoYears() {
	var date = new Date();
	var year = parseInt(date.getYear(), 10) + 2;
	var month = parseInt(date.getMonth(), 10) + 1;
	var day = parseInt(date.getDate(), 10);
	if(month < 10) {
       month = "0" + month;
    }
    if(day < 10) {
       day = "0" + day;
    }
	return year + "-" + month + "-" + day;
}
//获取上个月日期
function dateBeforeOneMounth() {
	var date = new Date();
	var year = date.getYear();
	var month = parseInt(date.getMonth());
	var day = parseInt(date.getDate());	
	if(day < 10) {
       day = "0" + day;
    }
	if(month == 0){
		year = year-1;
		return  year+"-12-"+day ;
	}
	if(month < 10) {
       month = "0" + month;
    }
    
	return year+"-"+month+"-"+day;
}

function inImgToDelrow (tableId, obj) {
	if (!confirm("确定删除吗？")) {
		return;
	}
	var table = document.getElementById(tableId);
	var onrow = obj.parentNode.parentNode.rowIndex;
	table.deleteRow(onrow);
}
/*
查询本省行政区划：
页面调用方法：searchXzqh(xzqhValue, xzqhdm, xzqhmc);
xzqhValue：通过<bean:write name="userSessionBean" property="xzqh" />获取行政区划
xzqhdm：隐藏的行政区划id
xzqhmc：显示的行政区划id
*/
function searchXzqh(xzqhValue, arg1, arg2){
	// （dm为要查询的列、参数2：行政区划中省代码、是否去0判断）
	var conditions = getXzqhConditions("dm", xzqhValue, 1);
	// （search.xml中xzqh节点、条件、列(与节点searchcol值对应)、页面id）
	openSeachWindow("xzqh", conditions, "dm,mc", ""+arg1+","+arg2+"", "");
}

/*
查询本省单位：
页面调用方法：searchDw(xzqhValue, dw_dm, dw_mc);
xzqhValue：通过<bean:write name="userSessionBean" property="xzqh" />获取行政区划
dw_dm：隐藏的单位id
dw_mc：显示的单位id
*/
function searchDw(xzqhValue, arg1, arg2){
	// （dwdm为要查询的列、参数2：行政区划中省代码、是否去0判断）
	var conditions = getXzqhConditions("dwdm", xzqhValue, 1);
	// （search.xml中dw节点、条件、列、页面id）
	openSeachWindow("dw", conditions, "dwdm,dwmc", ""+arg1+","+arg2+"", "");
}
function getZzjg(xzqhValue, arg1, arg2){
	// （dwdm为要查询的列、参数2：行政区划中省代码、是否去0判断）
	var conditions = getXzqhConditions("code", xzqhValue, 1);
	// （search.xml中dw节点、条件、列、页面id）
	openSeachWindow("zzjg", conditions, "code,detail", ""+arg1+","+arg2+"", "");
}
//	查询籍贯
function searchJg(arg1, arg2){
	var conditions = "";
	openSeachWindow("xzqh",conditions,"mc,dm", ""+arg1+","+arg2+"", "");
}
/*
查询作业证编号：
页面调用方法：searchZyzbh(xzqhValue, zyzbh, xm){
xzqhValue：通过<bean:write name="userSessionBean" property="xzqh" />获取行政区划
zyzbh：隐藏的单位id
xm：显示的单位id
*/
function searchZyzbh(xzqhValue, zyzbh, xm){
	// （dwdm为要查询的列、参数2：行政区划中省代码、是否去0判断）
	var conditions = getXzqhConditions("dwdm", xzqhValue, 1);
	// （search.xml中dw节点、条件、列、页面id）
	openSeachWindow("zyzbh", conditions, "zyzbh,xm", ""+zyzbh+","+xm+"", "");
}
/*
查询作业证编号：
页面调用方法：searchZyzbh(xzqhValue, zyzbh, xm){
xzqhValue：通过<bean:write name="userSessionBean" property="xzqh" />获取行政区划
zyzbh：隐藏的id
xm：显示的id
*/
function searchZyzbh0(xzqhValue, zyzbh, xm){
	// （dwdm为要查询的列、参数2：行政区划中省代码、是否去0判断）
	var conditions = getXzqhConditions("zyzbh", xzqhValue, 1);
	// （search.xml中zyzbh节点、条件、列、页面id）
	openSeachWindow("zyzbh0", conditions, "zyzbh,xm", ""+zyzbh+","+xm+"", "");
}
/*
查询受控人员单位：
页面调用方法：searchSkryDw(xzqhValue, dwdm, dwmc){
xzqhValue：通过<bean:write name="userSessionBean" property="xzqh" />获取行政区划
dwdm：隐藏的id
dwmc：显示的id
*/
function searchSkryDw(xzqhValue, dwdm, dwmc){
	// （dwdm为要查询的列、参数2：行政区划中省代码、是否去0判断）
	var conditions = getXzqhConditions("dwdm", xzqhValue, 1);
	// （search.xml中skry节点、条件、列、页面id）
	openSeachWindow("skry", conditions, "dwdm,dwmc", ""+dwdm+","+dwmc+"", "");
}
function showHidden (aa, bb) {
	if(document.getElementById(aa).style.display=="none") { 
		document.getElementById(aa).style.display = "";
	}
	if(document.getElementById(bb).style.display=="") { 
		document.getElementById(bb).style.display = "none";
	}
}

//打开单位详细信息页
function openDwxxxxPage(dwdm){
	var url = "../dwgl/dwgl.do?method=getDwXxxxByDwdm&dwdm="+dwdm;
	var w = screen.availWidth;
	var h = screen.availHeight;
	window.open(url, "", "width=" + w + "px, height=" + h + "px, top=0, left=0, toolbar=no, menubar=no, resizable=yes, location=no, status=no");
}
function openDwxxxxPage2(dwdm, dwmc, dwlx, dwzt){	
	if(xzqh.substring(0, 2) != dwdm.substring(0, 2)){
		alert("此单位为非本地单位！");
		return;
	}
	if (!dwmc) dwmc = "";
	if (!dwlx) dwlx = "";
	if (!dwzt) dwzt = "";
	var url = "../dwgl/dwgl.do?method=getDwXxxxByDwdm2&dwdm="+ dwdm + "&dwmc=" + encodeURIComponent(dwmc) + "&dwlx=" + encodeURIComponent(dwlx)+ "&dwzt=" + encodeURIComponent(dwzt);
	var w = screen.availWidth;
	var h = screen.availHeight;
	window.open(url, "", "width=" + w + "px, height=" + h + "px, top=0, left=0, toolbar=no, menubar=no, resizable=yes, location=no, status=no");
}
//打开人员详细信息页
function openRyxxxxPage(zyzbh,xm,gzdw,zyzlx,zyzzt,dwdm){
	var url = "../rygl/rygl.do?method=getRyxxxx&zyzbh=" + zyzbh;
	if(xm && xm != ""){
		url += "&xm=" + encodeURIComponent(xm);
	}
	if(gzdw && gzdw != ""){
		url += "&gzdw=" + encodeURIComponent(gzdw);
	}
	if(zyzlx && zyzlx != ""){
		url += "&zyzlx=" + encodeURIComponent(zyzlx);
	}
	if(zyzzt && zyzzt != ""){
		url += "&zyzzt=" + encodeURIComponent(zyzzt);
	}
	if(dwdm && dwdm != ""){
		url += "&dwdm=" + encodeURIComponent(dwdm);
	}
	var w = screen.availWidth;
	var h = screen.availHeight;
	window.open(url, "", "width=" + w + "px, height=" + h + "px, top=0, left=0, toolbar=no, menubar=no, resizable=yes, location=no, status=no");
}

/*去掉字符串中的空格*/
function trim(str) {
	return (str + '').replace(/(\s+)$/g, '').replace(/^\s+/g, '');
}

function addFjxx(tableId) {
	var table = document.getElementById(tableId);
	if(table.rows.length > 5) {
		alert("最多添加5个附件!");
		return;
	}
	var row = table.insertRow();
	row.setAttribute("height", "25");
	var cell = row.insertCell();
	cell.className = "line_t";
	cell.align = "center";
	cell.innerHTML = "<img src='../images/text.gif' align='absmiddle' border='0'>";
	
	var cell = row.insertCell();
	cell.className = "line_t";
	cell.innerHTML = "<input type='file' name='fjxx' size='45'>";

	var cell = row.insertCell();
	cell.className = "line_t";
	cell.innerHTML = "<input type='text' name='ms' size='45'>";

	var cell = row.insertCell();
	cell.className = "line_t";
	cell.align = "center";
	cell.appendChild(document.createElement("<img src='../images/delete.gif' alt='删除' onclick=\"return fujianDelRow('"+tableId+"', this);\" style=\"cursor:hand\"  border=\"0\">"));
}

/*选择行政区划  resultColumn为回传字段名称，例如'xzqhmc,xzqhdm'中间用逗号隔开，valueColumn为获得的类型'mc,dm',xzqh为查询条件*/
var win_xzqh;
function getXzqh(resultColumn, valueColumn, xzqh) {
	var url = '../xzqh.do?resultColumn=' + resultColumn + '&valueColumn=' + valueColumn;
	
	var zhengze=/^[0-9]{6}$/; //正则表达式
	if(zhengze.exec(xzqh)) {
		url = url + '&condition=' + xzqh;
	}
	if (win_xzqh == null) {
		win_xzqh = showwin("选择行政区划", url, 600, 320);
	} else {
		navwin(win_xzqh, url);
	}
}

//人员卡注册
function ickZhuCeRyk(kbh, rykzt) {
	if(trim(rykzt) == "未注册") {
		window.location.href = '../icgl/ick.do?method=ickHuankaEdit&kbh=' + kbh + '&klx=C&flag=0';
	}
//	 else {
//		alert("此人员已注册过IC卡，不能再次注册！");
//		return false;
//	}
}
//单位卡注册
function ickZhuCeDwk(dwdm) {
	window.location.href = '../icgl/ickZhuCe.do?method=ickZhuCeEditDwk&dwdm=' + dwdm;
}

//获取选中行的rowIndex,并更改选中行的样式
var selectrow = 0;
function selectRow(objTR) {
	var objTable = objTR.parentElement;
	if (selectrow>0) {
		objTable.rows[selectrow].className = "Pop_TR";
	}
	objTR.className = "Pop_TR1";
	selectrow = objTR.rowIndex;
}
//用于直接查询list结果页
function openMiddlePage(url){
	var w = screen.availWidth;
	var h = screen.availHeight;
	window.open(url, "", "width=" + w + "px, height=" + h + "px, top=0, left=0, toolbar=no, menubar=no, resizable=yes, location=no, status=no");
}
//用于显示加解锁原因
var newWindowContent;
function showYY (yyDivTableId, jiasyyDivId, jiesyyDivId, obj) {
	var title = "加解锁原因";
	var width = "400";
	var height = "200";
	document.getElementById(jiasyyDivId).innerHTML = obj.parentNode.nextSibling.childNodes[0].value;
	document.getElementById(jiesyyDivId).innerHTML = obj.parentNode.nextSibling.nextSibling.childNodes[0].value;
	var content = document.getElementById(yyDivTableId).innerHTML;
	if (newWindowContent==null) {
		newWindowContent = showmsg(title, content, width, height);
	} else {
		newWindowContent.resize(width, height); // 处理一个页面中多个不同大小层
		updatewin(newWindowContent, content, title, width, height);
	}
}
function printCurrentPage(strId) {
	var ids = strId.split(",");
	for (var i=0; i<ids.length; i++) {
		if (document.getElementById(ids[i]) != null) {
			document.getElementById(ids[i]).style.display = "none";
		}
	}
	window.focus();
	window.print();
	for (var i=0; i<ids.length; i++) {
		if (document.getElementById(ids[i]) != null) {
			document.getElementById(ids[i]).style.display = "block";
		}
	}
}
//全部清空提示变量
var CLEARALLINFO = "确认要全部清空吗？";


function showhelp(){
	var helpurl="../help/index.html";
	var w = screen.availWidth;
	var h = screen.availHeight-50;
	window.open(helpurl, "", "width=" + w + "px, height=" + h + "px, top=0, left=0, toolbar=no, menubar=no, resizable=yes, location=no, status=yes");
}

//检查上传文件大小
function checkUploadFilePathSize(filePath, errorSpan){
	
	var FileSizeLimit = 3*1024*1024; //3M
	var fso ;
	try{
	 	fso = new ActiveXObject("Scripting.FileSystemObject");
	}catch(e){
		if(fso=="undefined"||fso==undefined){
			if(confirm("未将本站点加入可信站点，请根据登录页的【系统配置说明】进行配置，是否去登录页？")){
				window.location ='../dologinloginout.action?method=loginout';
			}
			return false;
		}
	}
		
    var f = fso.GetFile(filePath);
	if(f.size >  FileSizeLimit){
		document.getElementById(errorSpan).style.display = "block";
		return false;
	}else{
		document.getElementById(errorSpan).style.display = "none";
		return true;
	}
}

//检查上传文件大小
/*function checkUploadFilePathSize(filePath, errorSpan){
	var FileSizeLimit = 3*1024*1024; //3M
	var fso = new ActiveXObject("Scripting.FileSystemObject");   
	alert(fso);
    var  f = fso.GetFile(filePath);
	if(f.size >  FileSizeLimit){
		document.getElementById(errorSpan).style.display = "block";
		return true;
	}else{
		document.getElementById(errorSpan).style.display = "none";
		return false;
	}
}*/

//检查上传文件大小(单位)
function checkUploadFileDwPathSize(filePath, errorSpan){
	var FileSizeLimit = 1*1024*1024; //1M
   	var fso = new ActiveXObject("Scripting.FileSystemObject");   
    var  f = fso.GetFile(filePath);
	if(f.size >  FileSizeLimit){
		document.getElementById(errorSpan).style.display = "block";
		return true;
	}else{
		document.getElementById(errorSpan).style.display = "none";
		return false;
	}
}

/*
function checkUploadFileDwPathSize(filePath, errorSpan){
	var FileSizeLimit = 1*1024*1024; //1M
	var image=new Image();
   	image.dynsrc=filePath;
	if(image.fileSize >  FileSizeLimit){
		document.getElementById(errorSpan).style.display = "block";
		return true;
	}else{
		document.getElementById(errorSpan).style.display = "none";
		return false;
	}
}
*/


//检查上传文件大小
function checkUploadFileFieldSize(field, errorSpan){
	var FileSizeLimit = 3*1024*1024; //3M
	var filePath = document.getElementById(field).value;
	var fso = new ActiveXObject("Scripting.FileSystemObject");   
    var f = fso.GetFile(filePath);
	if(f.size >  FileSizeLimit){
		document.getElementById(errorSpan).style.display = "block";
		return true;
	}else{
		document.getElementById(errorSpan).style.display = "none";
		return false;
	}
}

//检查上传文件存在与否
function checkUploadFile(field, title, FileSize){
	var FileSizeLimit = FileSize*1024*1024; //3M
	var filePath = document.getElementById(field).value;
	if(filePath != null && filePath != ''){
		var fso = new ActiveXObject("Scripting.FileSystemObject");   
    	var f = fso.GetFile(filePath);
		if(f.size >  FileSizeLimit){
			alert(title+' 上传文件不能超过'+FileSize+'M，请重新上传文件！')
			return true;
		}else if(f.size < 0){
			alert(title+' 上传的文件不存在，请重新上传文件！')
			return true;
		}else{
			//document.getElementById(errorSpan).style.display = "none";
			return false;
		}
	}
}

function makeFjCondition(nameArray, eleArray){
		var condition = "";
		for(var i=0; i < nameArray.length; i++){
			if(i == (nameArray.length -1)){
				condition = condition + nameArray[i] + "=" + eleArray[i];
			}else{
				condition = condition + nameArray[i] + "=" + eleArray[i] + "&";
			}
		}
		return condition;
}

//小数判断（最多3位小数）
function onlyFloat(value){
	if(value.indexOf(".") > -1){
		return /^(\d)+\.\d{1,3}?$/g.test(value);
	}else{
		return /^(\d)+?$/g.test(value);
	}
}

//只能输入汉字、字母、数字、下划线
function checkDwmcFormat(dwmc){  
var re1 = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9])*$");
if (!re1.test(dwmc)){
     return true;
}
     return false;
}

//从定时器线程中获取内容并在页面弹框展示（TODO:通用，当前只是用于获取Nginx替换出错的异常信息）
function getDataFromServer(url) {
	$.ajax({
		type : "post",
		url : url,
		success : function(data) {
			if (data != null && data != "") {
				alert(data);
			}
		},
		error : function(a,b) {
			console.log(b);
		}
	});
}

/*var nindex = 1;
var time = setInterval(function(){
	if (nindex == 7) {
		clearInterval(time);
		return;
	}else {
		getDataFromServer("../nginx/nginxgetUpdateConfErrMsg.action");
	}
	nindex ++;
},3000);*/


