var currentFwqAll = new Array();	//当前所有的服务器信息（用于更新时候对比），初始化时候push进去，方便存储   只存储id
var currentTgFwqAll = new Array();	//当前所有的外网服务器信息（用于更新时候对比），初始化时候push进去，方便存储   只存储id

/**
 * 切换服务器展示区域内容的类型
 */
var yyyxzt_zc = "0";	//应用运行正常
var yyyxzt_yc = "1";	//应用运行异常

$(function(){
	
	/**
	 * 加载内网服务器展示列表
	 */
	loadFwqByPtlx(ptlx_yun);
	
	/**
	 * 200毫秒后展示外网服务器列表
	 */
	setTimeout(function(){
		//加载外网服务器
		loadFwqByPtlx(ptlx_tg);
	}, 200);
	
	/**
	 * 5秒后开启自动更新页面操作
	 */
	setTimeout(function(){
		setInterval(function(){
			updateMonitorInfo();
		}, 60*1000);
	}, 5*1000);
	
});

/**
 * 执行监控界面的更新操作
 */
function updateMonitorInfo() {
	
	/**
	 * 关闭所有弹出层
	 */
	layer.closeAll();
	
	/**
	 * 0、获取数据
	 */
	
	var errInfo;
	var fwqAll;
	var tgfwqAll;
	
	$.ajax({
		type: "POST",
   		url: "../index/indexgetRefreshInfo.action",
   		data:"ptlx="+ptlx_yun,
   		dataType:"json",
   		async: false,
		success: function(data){
			errInfo = {};
			fwqAll = new Array();
			
			if (data == null || data == "") {
				return;
			}
			errInfo = data["errInfo"];
			fwqAll = data["fwqAll"];
		},
		error: function(){
			return;
		}
	});
	
	$.ajax({
		type: "POST",
		url: "../index/indexgetRefreshInfo.action",
		data:"ptlx="+ptlx_tg,
		dataType:"json",
		async: false,
		success: function(data){
			errInfo = {};
			tgfwqAll = new Array();
			
			if (data == null || data == "") {
				return;
			}
			tgfwqAll = data["fwqAll"];
		},
		error: function(){
			return;
		}
	});
	
	if (!errInfo || !fwqAll || !tgfwqAll) {
		return;
	}
	
	/**
	 * 0、更新界面杂项
	 */

	
	/**
	 * 1、更新服务器概况展示信息
	 */
	var yuntotalNum = errInfo.errInfo01.totalNum;
	var yunsucNum = errInfo.errInfo01.sucNum;
	var yunerrNum = errInfo.errInfo01.errNum;
	var yuninfoNum = errInfo.errInfo01.infoNum;
	
	var tgtotalNum = errInfo.errInfo02.totalNum;
	var tgsucNum = errInfo.errInfo02.sucNum;
	var tgerrNum = errInfo.errInfo02.errNum;
	var tginfoNum = errInfo.errInfo02.infoNum;
	
	$("#yuntotalNum").html(yuntotalNum);
	$("#yunsucNum").html(yunsucNum);
	$("#yunerrNum").html(yunerrNum);
	$("#yuninfoNum").html(yuninfoNum);
	
	$("#tgtotalNum").html(tgtotalNum);
	$("#tgsucNum").html(tgsucNum);
	$("#tgerrNum").html(tgerrNum);
	$("#tginfoNum").html(tginfoNum);
	
	/**
	 * 2、更新内网服务器
	 */
	updateMonitorInfoByPtlx(ptlx_yun, fwqAll);
	
	/**
	 * 3、更新外网服务器
	 */
	updateMonitorInfoByPtlx(ptlx_tg, tgfwqAll);
	
}

/**
 * 根据平台类型更新监控服务器
 * @param ptlx
 */
function updateMonitorInfoByPtlx(ptlx, fwqAll) {
	
	var currentFwqList = currentFwqAll;
	if (ptlx_tg == ptlx) {
		currentFwqList = currentTgFwqAll;
	}
	
	//操作之前，将新信息进行备份，之后要用
	var tempFwqAll = fwqAll.slice();

	//遍历服务器，将要添加的、修改的、删除的存到数组中
	var fwqToAddArr = new Array();
	var fwqToUpdateArr = new Array();
	var fwqToRemoveArr = new Array();
	
	//遍历服务器，将要修改的和要删除的进行整理
	
	for(var i=0;i<currentFwqList.length;i++) {
		var currentFwqId = currentFwqList[i];
		var flag = true;
		
		if (fwqAll[fwqAll.length-1]) {
			for(var k in fwqAll) {
				if (fwqAll[k]["id"].indexOf(currentFwqId) != -1) {
					//新的中包含有当前的该服务器,放入修改数组中
					fwqToUpdateArr.push(fwqAll[k]);
					delete(fwqAll[k]);
					flag = false;
					break;
				}
			}
		}
		
		if (flag) {
			//新的中不包含当前该服务器，将当前的这个放入删除的数组中
			fwqToRemoveArr.push(currentFwqId);
		}
	}
	//现在新的数组中剩下的就是要添加的
	if (fwqAll[fwqAll.length-1]) {
		for(var n in fwqAll) {
			fwqToAddArr.push(fwqAll[n]);
		}
	}
	//将对应的数组中的数据做相应的处理
	if (fwqToAddArr && fwqToAddArr.length>0) {
		$(fwqToAddArr).each(function(){
			createFwqItem(this, ptlx);
		});
	}
	if (fwqToUpdateArr && fwqToUpdateArr.length>0) {
		$(fwqToUpdateArr).each(function(){
			updateFwqItem(this, ptlx);
		});
	}
	if (fwqToRemoveArr && fwqToRemoveArr.length>0) {
		$(fwqToRemoveArr).each(function(){
			removeFwqItem(this, ptlx);
		});
	}
	
	//执行完成之后，需要更新当前的数组信息
	if (ptlx_tg == ptlx) {
		currentTgFwqAll.length = 0;
		for(var i=0;i<tempFwqAll.length;i++) {
			currentTgFwqAll.push(tempFwqAll[i]["id"]);
		}
	}else {
		currentFwqAll.length = 0;
		for(var i=0;i<tempFwqAll.length;i++) {
			currentFwqAll.push(tempFwqAll[i]["id"]);
		}
	}
}

/**
 * 根据平台类型异步加载所有服务器
 * @param ptlx
 */
function loadFwqByPtlx(ptlx) {
	$.ajax({
		type: "post",
		url: "../index/indexgetRefreshInfo.action",
		data: "ptlx="+ptlx,
		dataType: "json",
		success: function(data){
			if (data == null || data == "") {
				return;
			}
			
			var fwqList = data["fwqAll"];
			if (fwqList != null && fwqList.length>0) {
				for(var i=0;i<fwqList.length;i++) {
					createFwqItem(fwqList[i], ptlx);
					if (ptlx_tg == ptlx) {
						currentTgFwqAll.push(fwqList[i]["id"]);
					}else {
						currentFwqAll.push(fwqList[i]["id"]);
					}
				}
			}else {
				if (ptlx_tg == ptlx) {
					$("#tgBlankTip").show();
				}else {
					$("#yunBlankTip").show();
				}
			}
		},
		error: function(a,b){
			
		}
	});
}

/**
 * 展示服务器详情
 * @param fwqid
 */
function showFwqInfo(fwqid) {
	showBindLayer("fwqinfoDiv"+fwqid,"450px","","服务器详情");
}

/**
 * 展示应用详情
 * @param fwqid
 */
function showYyInfo(fwqid) {
	showBindLayer("yyinfoDiv"+fwqid,"450px","","应用详情");
}


/**
 * 切换服务器展示区域内容
 * @param showStatus  查看服务器状态类型  	全部、正常、异常、警告
 * @param ptlx	动态的平台类型		
 */
function switchServerShow(showStatus, ptlx) {
	
	if (ptlx_tg == ptlx) {
		$("#currentPtlx").html("外网");
	}else {
		$("#currentPtlx").html("内网");
	}
	
	//动态类型	--从相应的服务器展示列表中挑选选中状态的服务器展示，其他的隐藏
	var ch =  "#servershowArea_yun .fwqitem";
	if (ptlx_tg == ptlx) {
		ch =  "#servershowArea_tg .fwqitem";
		$("#servershowArea_tg").show();
		$("#servershowArea_yun").hide();
	}else {
		$("#servershowArea_yun").show();
		$("#servershowArea_tg").hide();
	}
	
	var isBlank = true;
	
	if ("" == showStatus) {
		//展示所有的
		$(ch).each(function(){
			isBlank = false;
			$(this).show();
		});
	}else {
		$(ch).each(function(){
			if ($(this).hasClass("fwqstatus_"+showStatus)) {
				$(this).show();
				isBlank = false;
			}else {
				$(this).hide();
			}
		});
	}
	
	/*
	 * 处理如果没有服务器的展示
	 */
	if (isBlank) {
		if (ptlx_tg == ptlx) {
			$("#tgBlankTip").show();
		}else {
			$("#yunBlankTip").show();
		}
	}else {
		if (ptlx_tg == ptlx) {
			$("#tgBlankTip").hide();
		}else {
			$("#yunBlankTip").hide();
		}
	}
	
}


/**
 * 查看更多提示信息
 */
function showMoreErrinfo(fwqid) {
	showBindLayer("showMoreErrinfo"+fwqid,"450px","","异常提示信息");
}

/**
 * 更新一个服务器div
 * @param fwq	服务器id
 * @param ptlx	平台类型
 */
function updateFwqItem(fwq, ptlx) {
	
	/**
	 * 这里使用简单方法，先删除、再添加
	 */
//	removeFwqItem(fwq["id"], ptlx);
//	createFwqItem(fwq, ptlx);
	
	/**
	 * 0、处理最外层div中的标记该服务器状态的类
	 */
	var classes = $("#fwqitem"+fwq["id"]).attr("class");
	var classArr = classes.split(" ");
	for(var i=0;i<classArr.length;i++) {
		if (classArr[i].startWith("fwqstatus_")) {
			$("#fwqitem"+fwq["id"]).removeClass(classArr[i]);
		}
	}
	
	$("#fwqitem"+fwq["id"]).addClass("fwqstatus_"+fwq["showStatus"]);
	
	/**
	 * 1、更新服务器基本信息（标题） fwqname	--通过class找到直接赋值
	 */
	$(".fwqnameVar").each(function(){
		$(this).html(fwq["fwqname"]);
	});
	
	/**
	 * 2、更新content部分	--重新生成，替换
	 */
	var contentDiv = createFwqItemContent(fwq, ptlx);
	$("#fwqitem"+fwq["id"]+" .servershow-item-content").html($(contentDiv).html());
	
	/**
	 * 3、更新服务器详情	--通过id直接赋值（暂时直接生成替换了）
	 */
	var fwqinfoDiv = createFwqItemFwqInfo(fwq);
	$("#fwqinfoDiv"+fwq["id"]).html($(fwqinfoDiv).html());
	
	/**
	 * 4、更新应用信息	--重新生成，替换
	 */
	var yyinfoDiv = createFwqItemYyInfo(fwq);
	$("#yyinfoDiv"+fwq["id"]).html($(yyinfoDiv).html());
}

/**
 * 移除一个服务器div
 * @param fwqid	服务器id
 * @param ptlx 平台类型
 */
function removeFwqItem(fwqid, ptlx) {
	/**
	 * 1、移除div
	 */
	$("#fwqitem"+fwqid).remove();
	
	/**
	 * 2、从当前所有服务器中移除
	 */
	if (ptlx_tg == ptlx) {
		currentTgFwqAll.remove(fwqid);
	}else {
		currentFwqAll.remove(fwqid);
	}
}

/**
 * 创建一个服务器（以及对应的应用信息和服务器详情）
 * @param fwq 服务器对象
 * @param ptlx 平台类型
 * @returns
 */
function createFwqItem(fwq, ptlx) {
	
	/**
	 * 1、生成主展示item
	 */
	
	/*
	 * 1.0、最外层div
	 */
	var tDiv = $(tmp_servershow_item.replace(/{@showStatus}/g, fwq["showStatus"])
									.replace(/{@fwqid}/g, fwq["id"]));
	/*
	 * 1.1、次外层div
	 */
	var t0Div = $(tmp_servershow_item_t0);
	/*
	 * 1.2、标题
	 */
	var titleDiv = $(tmp_servershow_item_title.replace(/{@fwqname}/g, fwq['fwqname'])
											.replace(/{@fwqid}/g, fwq['id'])
											.replace(/{@ptlx}/g, fwq['ptlx'])
											.replace(/{@fwqip_ww}/g, fwq['fwqip_ww'])
											.replace(/{@fwqip}/g, fwq['fwqip'])
											.replace(/{@dk}/g, fwq['dk'])
											);
	/*
	 * 1.3、中间内容 
	 */
	var contentDiv = createFwqItemContent(fwq, ptlx);
	
	/*
	 * 1.4、下层操作	查看详情和查看应用信息
	 */
	var actionDiv = $(tmp_servershow_item_action.replace(/{@fwqid}/g, fwq['id']));
	
	/*
	 * 1.5、组装
	 */
	$(t0Div).append(titleDiv);
	$(t0Div).append(titleDiv);
	$(t0Div).append(contentDiv);
	$(t0Div).append(actionDiv);
	
	$(tDiv).append(t0Div);
	
	/**
	 * 2、生成应用信息弹出层
	 */
	var yyinfoDiv = createFwqItemYyInfo(fwq);
	$(tDiv).append(yyinfoDiv);
	
	/**
	 * 3、生成服务器详情弹出层
	 */
	var fwqinfoDiv = createFwqItemFwqInfo(fwq);
	$(tDiv).append(fwqinfoDiv);
	
	/**
	 * 4、追加进页面中
	 */
	if (ptlx_tg == ptlx) {
		$("#servershowArea_tg").append(tDiv);
	}else {
		$("#servershowArea_yun").append(tDiv);
	}
	
}

/**
 * 创建展示主内容的中间content部分
 */
function createFwqItemContent(fwq, ptlx) {
	var cpuColor = "green";
	var memeryColor = "green";
	
	var cpuPercent = fwq['cpuused'].split("%")[0];
	if (Number(cpuPercent) > Number(getThreshold("cpu"))) {
		cpuColor = "red";
	}
	
	var memPercent = fwq['memeryused'].split("%")[0];
	if (Number(memPercent) > Number(getThreshold("mem"))) {
		memeryColor = "red";
	}
	
	/*
	 * 1.3.2、处理右侧展示信息
	 */
	var msgInfoList = fwq['msgInfoList'];
	var errinfoHtml = "";
	if (!msgInfoList || msgInfoList == null || msgInfoList.length == 0) {
		errinfoHtml = tmp_servershow_item_content_errinfoItem
							.replace(/{@errinfoColor}/g, "green")
							.replace(/{@errinfoMsg}/g, "一切正常");
	}else {
		for(var i =0;i<msgInfoList.length;i++) {
			if (i == 3) {
				errinfoHtml += '<li><a href="javascript:void(0)" onclick="showMoreErrinfo(\''+fwq["id"]+'\')">更多>></a></li>';
				
				var moreTrHtml = '';
				for(var k =0;k<msgInfoList.length;k++){
					moreTrHtml += tmp_servershow_item_content_errinfoMoreLayer_item.replace(/{@errmsg}/g, msgInfoList[k]["msg"]);
				}
				errinfoHtml += tmp_servershow_item_content_errinfoMoreLayer
												.replace(/{@fwqid}/g, fwq["id"])
												.replace(/{@trList}/g, moreTrHtml);
			}
			if (i<3) {
				if (msgInfoList[i]["level"] == "error") {
					errinfoHtml += tmp_servershow_item_content_errinfoItem
									.replace(/{@errinfoColor}/g, "red")
									.replace(/{@errinfoMsg}/g, msgInfoList[i]["msg"]);
				}
				if (msgInfoList[i]["level"] == "info") {
					errinfoHtml += tmp_servershow_item_content_errinfoItem
									.replace(/{@errinfoColor}/g, "#FFA815")
									.replace(/{@errinfoMsg}/g, msgInfoList[i]["msg"]);
				}
				if (msgInfoList[i]["level"] == "success") {
					errinfoHtml += tmp_servershow_item_content_errinfoItem
									.replace(/{@errinfoColor}/g, "green")
									.replace(/{@errinfoMsg}/g, msgInfoList[i]["msg"]);
				}
			}
		}
	}
	
	var contentDiv = $(tmp_servershow_item_content.replace(/{@cpuColor}/g, cpuColor)
										.replace(/{@cpuused}/g, fwq['cpuused'])
										.replace(/{@memeryColor}/g, memeryColor)
										.replace(/{@memeryused}/g, fwq['memeryused'])
										.replace(/{@errinfoList}/g, errinfoHtml)
										);
	return contentDiv;
}

/**
 * 创建一个服务器对应的应用信息
 * @param fwq 服务器对象，需要包含应用信息
 * @returns
 */
function createFwqItemYyInfo(fwq) {
	
	/**
	 * 0、准备数据
	 */
	var fwqid = fwq["id"];
	var fwqip = fwq["fwqip"];
	var fwqip_ww = fwq["fwqip_ww"];
	var dk = fwq["dk"];
	var yyInfo = fwq["yylist"];
	
	/**
	 * 1、生成Div
	 */
	
	/*
	 * 1.1、生成动态的应用tr集合
	 */
	var yyinfoHtml = "";
	if (yyInfo != null && yyInfo.length>0) {
		for(var i =0;i<yyInfo.length;i++) {
			//模板所需数据：yyname yyztColor yyztMsg fwqip_ww dk warname fwqip
			var yyname = yyInfo[i]["yyname"];
			var warname = yyInfo[i]["warname"];
			var yyztColor = "green";
			var yyztMsg = "正常";
			if (yyInfo[i]["yyyxzt"] == null || isBlank(yyInfo[i]["yyyxzt"])) {
				yyztColor = "#F8D080";
				yyztMsg = "检测中";
			}else if (yyInfo[i]["yyyxzt"] == yyyxzt_yc) {
				yyztColor = "red";
				yyztMsg = "异常";
			}
			yyinfoHtml += tmp_servershow_yyinfo_yyTrItem
								.replace(/{@yyname}/g, yyname)
								.replace(/{@yyztColor}/g, yyztColor)
								.replace(/{@yyztMsg}/g, yyztMsg)
								.replace(/{@fwqip_ww}/g, fwqip_ww)
								.replace(/{@dk}/g, dk)
								.replace(/{@warname}/g, warname)
								.replace(/{@fwqip}/g, ptlx_tg==fwq["ptlx"] ? fwqip_ww : fwqip);
		}
	}else {
		yyinfoHtml += '<tr><td colspan="4" style="text-align: center;font-size: 16px;padding: 8px;">暂时没有应用信息</td></tr>';
	}
	
	/*
	 * 1.2、生成最外成div
	 */
	
	var t0Div = $(tmp_servershow_yyinfo.replace(/{@fwqid}/g, fwqid)
								.replace(/{@yyTrList}/g, yyinfoHtml));
	
	return t0Div;
}


/**
 * 创建一个服务器对应的服务器详情div
 * @param fwq 服务器对象，需要包含应用信息
 * @returns
 */
function createFwqItemFwqInfo(fwq) {
	//模板所需数据	fwqid fwqczxt_dicvalue szjf fwqym threadcount jvmmemory jvmthreadcount updatetime
	var fwqinfoDiv = $(tmp_servershow_fwqinfo.replace(/{@fwqid}/g, fwq["id"])
											.replace(/{@fwqczxt_dicvalue}/g, fwq["fwqczxt_dicvalue"])
											.replace(/{@szjf}/g, fwq["szjf"])
											.replace(/{@fwqym}/g, fwq["fwqym"])
											.replace(/{@threadcount}/g, fwq["threadcount"])
											.replace(/{@jvmmemory}/g, fwq["jvmmemory"])
											.replace(/{@jvmthreadcount}/g, fwq["jvmthreadcount"])
											.replace(/{@updatetime}/g, fwq["updatetime"])
											);
	return fwqinfoDiv;
}


/**
 * 应用访问
 */
function viewFwqYy(fwqip, dk, warname) {
	var appname = warname.substring(0,warname.length-4);
	var url = "http://"+fwqip+":"+dk+"/"+appname;
	window.open(url);
}

/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~  模板 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

/**
 * ======================= 服务器展示单个item ===========================
 */

/**
 * 服务器展示item最外层 fwqid showStatus
 */
var tmp_servershow_item = '<div id="fwqitem{@fwqid}" class="layui-col-sm6 layui-col-lg4 layui-col-xs12 fwqitem fwqstatus_{@showStatus}"></div>';

/**
 * 服务器展示item次外层
 */
var tmp_servershow_item_t0 = '<div class="servershow-item"></div>';

/**
 * 服务器展示item标题	fwqid fwqname ptlx fwqip_ww fwqip dk
 */
var tmp_servershow_item_title = '<div class="servershow-item-title">'
								+ '<span class="fwqnameVar{@fwqid}">{@fwqname}</span>' + '【<a href="javascript:void(0)" '
								+ 'onclick="tomcatLogs(\'{@ptlx}\',\'{@fwqip_ww}\', \'{@fwqip}\',\'{@dk}\')">Tomcat日志</a>】</div>';

/**
 * 服务器展示item的中间层	cpuColor cpuused memeryColor memeryused errinfoList
 */
var tmp_servershow_item_content = '<div class="servershow-item-content layui-row">'
								+ '<div class="servershow-item-content-left layui-col-sm4 layui-col-xs12">'
								+ '<div class="item">'
								+ 'CPU：<span class="cpuusedVar"><font color="{@cpuColor}">{@cpuused}</font></span></div>'
								+ '<div class="item">'
								+ '内存： <span class="memusedVar"><font color="{@memeryColor}">{@memeryused}</font></span></div></div>'
								+ '<!-- 中间横线、竖线 -->'
								+ '<div style="display: none;" class="line-vertical layui-show-sm-inline-block"></div>'
								+ '<div class="line-horizon layui-hide-sm"></div>'
								+ '<div class="servershow-item-content-right layui-col-sm7 layui-col-xs12">'
								+ '<ul>'
								+ '{@errinfoList}'
								+ '</ul></div></div>';
/**
 * 服务器展示item的中间层的错误信息提示单个	errinfoColor errinfoMsg
 */
var tmp_servershow_item_content_errinfoItem = '<li style="color: {@errinfoColor};">{@errinfoMsg}</li>';

/**
 * 服务器展示item的中间层的错误信息 更多弹出层	fwqid trList
 */
var tmp_servershow_item_content_errinfoMoreLayer = '<div id="showMoreErrinfo{@fwqid}" class="displaynone" style="padding-bottom: 10px;">'
													+ '<table class="api_table" border="0" cellpadding="0" cellspacing="0">'
													+ '{@trList}</table></div>';

/**
 * 服务器展示item的中间层的错误信息 更多弹出层 中的每一项	errmsg
 */
var tmp_servershow_item_content_errinfoMoreLayer_item = '<tr><td style="color: #FFA815">{@errmsg}</td></tr>';

/**
 * 服务器展示item的最下层	fwqid
 */
var tmp_servershow_item_action = '<div class="servershow-item-action layui-row">'
								+ '<div id="showinfo{@fwqid}" class="layui-col-xs6 showinfo" onclick="showFwqInfo(\'{@fwqid}\')">'
								+ '查看详情</div>'
								+ '<div id="showyy{@fwqid}" class="layui-col-xs6 showyy" onclick="showYyInfo(\'{@fwqid}\')">'
								+ '应用信息</div></div>';


/**
 * ======================= 服务器展示单个item对应的应用信息弹出层 ===========================
 */

/**
 * 应用信息	fwqid yyTrList（动态的应用tr HTML）
 */
var tmp_servershow_yyinfo = '<div id="yyinfoDiv{@fwqid}" class="yyinfoDiv displaynone" style="padding: 5px 10px;">'
							+ '<table class="api_table" border="0" cellpadding="0" cellspacing="0">'
							+ '<tr class="title"><th>应用名</th><th>运行状态</th><th>日志</th><th>操作</th></tr>'
							+ '{@yyTrList}</table></div>';

/**
 * 应用信息 的单个应用tr		yyname yyztColor yyztMsg fwqip_ww dk warname fwqip
 */
var tmp_servershow_yyinfo_yyTrItem = '<tr><td>{@yyname}</td>'
							+ '<td><font color="{@yyztColor}">{@yyztMsg}</font></td>'
							+ '<td><a href="javascript:void(0)" '
							+ 'onclick="showFwqYyLog(\'{@fwqip_ww}\',\'{@dk}\',\'{@warname}\')">日志</a></td>'
							+ '<td><a href="javascript:void(0)" '
							+ 'onclick="viewFwqYy(\'{@fwqip}\',\'{@dk}\',\'{@warname}\')">访问</a></td></tr>';

/**
 * ======================= 服务器展示单个item对应的服务器详情弹出层 ===========================
 */

/**
 * 服务器详情 fwqid fwqczxt_dicvalue szjf fwqym threadcount jvmmemory jvmthreadcount updatetime
 */
var tmp_servershow_fwqinfo = '<div id="fwqinfoDiv{@fwqid}" class="fwqinfoDiv displaynone" style="padding: 5px 10px;">'
							+ '<table class="api_table" border="0" cellpadding="0" cellspacing="0">'
							+ '<tr><td>操作系统</td><td><span id="czxt{@fwqid}">{@fwqczxt_dicvalue}</span></td></tr>'
							+ '<tr><td>所在机房</td><td><span id="szjf{@fwqid}">{@szjf}</span></td></tr>'
							+ '<tr><td>对外地址</td><td><span id="dwdz{@fwqid}">{@fwqym}</span></td></tr>'
							+ '<tr><td>线程数量</td><td><span id="xcsl{@fwqid}">{@threadcount}</span></td></tr>'
							+ '<tr><td>JVM可用堆内存</td><td><span id="jvmkydnc{@fwqid}">{@jvmmemory}</span></td></tr>'
							+ '<tr><td>JVM运行线程数</td><td><span id="jvmyxxcs{@fwqid}">{@jvmthreadcount}</span></td></tr>'
							+ '<tr><td>信息更新时间</td><td><span id="xxgxsj{@fwqid}">{@updatetime}</span></td></tr>'
							+ '</table></div>';
