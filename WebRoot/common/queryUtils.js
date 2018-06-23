/*
 * 查询参数相关功能
*/
//加载查询页面时,回填从列表页面上点击重新查询传回的查询条件,
window.onload = function (){
	if(document.getElementById("queryparamtername") ){
		var params = document.getElementById("queryparamtername").value.split(";");
		for(var i=0;i<params.length;i++) {
			var param = params[i].split("~");
			if(param.length != 3){
				return false;
			}
			//modify by 张俊吉 at 2008-08-25 增加 try catch 语句，处理回填不上的错误。
			try{
				if(document.getElementById(param[2]).tagName == "SELECT"){
					for(var j=0;j<document.getElementById(param[2]).options.length;j++){
						if(document.getElementById(param[2]).options[j].text == param[1]){
							document.getElementById(param[2]).selectedIndex = j;
						}
					}
				}else{
					document.getElementById(param[2]).value = param[1];
				}
			}catch(e){
			}
		}
		afterOnload();
		//makecxtj();
	}
}
//可以覆盖此函数来实现使用者自己控制基础查询和高级查询之间的切换
function makecxtj(){
	control_cxtj('jccxTable', 'gjcxTable');
}
function afterOnload(){}

function control_cxtj(para1, para2) {}

function getspecialparam(field, defvalue)
{
	return defvalue;
}
/**
 * 组织查询条件
 * param fields:页面上的各个查询条件域所对组成的查询列表
 * param queryfield:存放查询条件的隐藏域
 * param queryfieldname:存放在列表页面上显示的查询条件的隐藏域
 * 标识符说明:
 * 		* : 右LIKE
 * 		** : 左右LIKE
 * 		@ : 对应数据库字段为Date类型的查询条件
 * 		$ : 只用做在列表页面上显示给用户看而不传入后台组织成查询语句的查询条件
 * 		# : 时间范围的查询条件
 * 		没有任何标识符的按等于处理
 * 关于时间范围的用法:
 * 以字段zcrq为例,如果对应数据库的字段类型为Date,起始和结束域分别命名为:zcrqfrom_date,zcrqto_date;
 * 如果对应数据库的字段类型为其他(多是Varchar(2)),起始和结束域分别命名为:zcrqfrom,zcrqto. 
 */
function getparamter(fields, queryfield, queryfieldname)
{
 	var result = "";
	var searchConditions = "";
	var tempValue;
 	for(var i=0;i<fields.length;i++)
 	{
 		var field = fields[i];
 		var subsearch = false;
 		var rangesearch = false;
 		if(field.indexOf("*")>0) {
 			if(field.indexOf("**")>0){
 				field = field.substr(0, field.indexOf("**"));
 				tempValue = getspecialparam(field, getOneFieldBothSideLikeParamter(field));
 				result += tempValue;
 			}else{
 				field = field.substr(0, field.indexOf("*"));
 				tempValue = getspecialparam(field, getOneFieldLikeParamter(field));
 				result += tempValue;
 			}
			searchConditions += getFieldNameAndValue(field, tempValue);
 			continue;
 		}
 		if(field.indexOf("#")>0) {
 			field = field.substr(0, field.indexOf("#"));
 			tempValue = getspecialparam(field,getOneFieldRangeParamter(field)); 
			result += tempValue;
			searchConditions += getRangeFieldNameAndValue(field, tempValue);
			continue;
 		}
 		if(field.indexOf("@")>0) {
 			var oper = field.substr(field.indexOf("@") + 1, field.length);
 			if(oper == ""){
 				oper = "=";
 			}
 			field = field.substr(0, field.indexOf("@"));
 			tempValue = getspecialparam(field,getOneFieldDateTypeParamter(field, oper)); 
			result += tempValue;
			searchConditions += getRangeFieldNameAndValue(field, tempValue);
			continue;
 		}
 		if(field.indexOf("$")>0) {
 			field = field.substr(0, field.indexOf("$"));
			tempValue = document.getElementById(field).value;
			searchConditions += getFieldNameAndValue(field, tempValue);
			continue;
 		}
 		
 		
		tempValue = getspecialparam(field,getOneFieldParamter(field));
		
		result += tempValue;		
		searchConditions += getFieldNameAndValue(field, tempValue);
 	}
 	if(result!=="") {
 		result = "&&" + result.substr(1);
 	}
 	
 	var objparamter=document.getElementsByName(queryfield);
 	if(objparamter && objparamter.length>0){
		objparamter[0].value = result;
	}
	
 	if(searchConditions!=="") {
 		searchConditions = searchConditions.substr(1);
 	}
 	var objparamtername=document.getElementsByName(queryfieldname);
 	if(objparamtername && objparamtername.length>0){
		objparamtername[0].value = searchConditions;
	}
}
function getOneFieldResult(fieldname, oper, value){
	if(value!==null && value!=="" && value.indexOf("~")<0 && value.indexOf("&"))
	{
		return "~" + fieldname +"~"+oper+"~" + value;
	}
	return "";
}
function getOneFieldParamter(fieldname){
	var obj = document.getElementById(fieldname);
	if(obj!==null)
	{
		return getOneFieldResult(fieldname, "=", obj.value);
	}
	return "";
}
function getOneFieldLikeParamter(fieldname){
	var obj = document.getElementById(fieldname);
	if(obj!==null && obj.value)
	{
		return getOneFieldResult(fieldname, "like", obj.value + "%");
	}
	return "";
}
function getOneFieldBothSideLikeParamter(fieldname){
	var obj = document.getElementById(fieldname);
	if(obj!==null && obj.value)
	{
		return getOneFieldResult(fieldname, "like","%" + obj.value + "%");
	}
	return "";
}
function getOneFieldDateTypeParamter(fieldname, oper){
	var obj = document.getElementById(fieldname);
	if(obj!==null && obj.value)
	{
		return getOneFieldResult(fieldname, oper, "to_date" + obj.value);
	}
	return "";
}
function getOneFieldRangeParamter(fieldname){
	var objfrom = document.getElementById(fieldname + "from");
	var objto = document.getElementById(fieldname + "to");
	var s = "";

 	if(objfrom!==null){
		s = getOneFieldResult(fieldname, ">=", objfrom.value);
	}else{
		objfrom = document.getElementById(fieldname + "from_date");
		if(objfrom!==null && objfrom.value){
			s = getOneFieldResult(fieldname, ">=", "to_date" + objfrom.value);
		}
	}
	if(objto!==null){
		s += getOneFieldResult(fieldname, "<=", objto.value);
	}else{
		objto = document.getElementById(fieldname + "to_date");
		if(objto!==null && objto.value){
			s += getOneFieldResult(fieldname, "<=", "to_date" + objto.value);
		}	
	}
	return s;
}
function getFieldNameAndValue(field, fieldValue){
	if(fieldValue != ""){
		var obj = document.getElementById(field);
		if(obj.title == null || obj.title == ""){
			return "";
		}
		if(obj.tagName == "SELECT"){
			return (";" + obj.title + "：~" + obj.options[obj.selectedIndex].text + "~" + field);
		}else{
			return (";" + obj.title + "：~" + obj.value + "~" + field);
		}
	}
	return "";
}
function getRangeFieldNameAndValue(fieldname, fieldValue){
	if(fieldValue != ""){
		var objfrom = document.getElementById(fieldname + "from");
		var objto = document.getElementById(fieldname + "to");
		var sc = "";
	
	 	if(objfrom!==null){
			sc += (";" + objfrom.title + "：~" + objfrom.value + "~" + fieldname + "from");
		}else{
			objfrom = document.getElementById(fieldname + "from_date");
			if(objfrom!==null && objfrom.value){
				sc += (";" + objfrom.title + "：~" + objfrom.value + "~" + fieldname + "from_date");
			}
		}
		if(objto!==null){
			sc += (";" + objto.title + "：~" + objto.value + ";" + "~" + fieldname + "to");
		}else{
			objto = document.getElementById(fieldname + "to_date");
			if(objto!==null && objto.value){
				sc += (";" + objto.title + "：~" + objto.value + "~" + fieldname + "to_date");
			}	
		}
		return sc;
	}
	return "";
}

/*查询结果倒序排列  李洪磊 2008-09-02
*  queryfield为提交的隐藏字段，如‘queryparamter’， orderparamter为排序字段， condition为排序条件‘desc’为倒序，其它值为顺序
*/
function resultOrderBy(queryfield, orderparamter, condition) {
	var objparamter=document.getElementsByName(queryfield);
 	if(objparamter && objparamter.length>0){
		objparamter[0].value += '~db_resultorderby~=~' + orderparamter;
		if(condition == "desc") {
			objparamter[0].value += ' DESC';
		}
	}
	
}