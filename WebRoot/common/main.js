

/* 
	@ 弹出
*/
$.fn.alertBox = function(options) {
	var org = {
		
		id: "",                           //弹出层的ID
		act: "click",                     //选择事件  如click mouseover
		borderWidth: "0px",               //边框的大小
		borderColor: "#62a7db",           //颜色
		borderAlpha: "0.7",               //边框透明度
		bgAlpha: "0.1",                   //全屏背景透明度
		bgColor: "#fff",                  //全屏背景颜色
		backgroundAct: "none",
		level: "5000"                       //层级 如弹框被遮挡时将此数值调大
		
	}
	$.extend(org, options);
	
	var first = false;
	var _cssK = $(this).attr("id")+"k";
	var cssK  = "."+_cssK;
	var _cssB = $(this).attr("id")+"b";
	var cssB  = "."+_cssB;
	var winW  = $(document).width();
	var winH  = $(document).height();
	var conW  = $(org.id).outerWidth();
	var conH  = $(org.id).outerHeight();
	var html  = "";
	var isload= false;
	
	$(org.id).after("<div class="+_cssB+"></div>")
	$(org.id).after("<div class="+_cssK+"></div>")
	
	$(cssK).css({ width:conW+"px", height:conH+"px", position:"absolute", "z-index":org.level, border:org.borderWidth+" solid "+org.borderColor, opacity:org.borderAlpha})
	$(org.id).css({ position:"absolute", left:"0px", top:"0px", "z-index":parseInt(org.level)+1})
	$(cssB).css({ width:"100%", "background-color":org.bgColor, position:"absolute", "z-index":parseInt(org.level)-1, opacity:org.bgAlpha, left:"0px", top:"0px" })
	
	if( $(org.id).attr("data-alertUrl") != undefined ){ html = $(org.id).attr("data-alertUrl"); }
	
	$(org.id).hide(); $(cssK).hide(); $(cssB).hide();	
	
	$(org.id+" .close").click(function(){ hide() })
	if( org.act!="none" ) $(this).bind(org.act,function(){ show() })
	if( org.backgroundAct!="none" ) $(cssB).bind(org.act,function(){ hide() })
	$(this).bind("show", function(event){ show() })
	$(this).bind("hide", function(event){ hide() })
	$(this).bind("remove", function(event){ $(this).unbind() })
	
	$(window).resize(function(){ setLoc() })
	$(window).scroll(function(){ setLoc() })
	
	function reSize(){
		$(cssK).css({ width:$(org.id).outerWidth()+"px", height:$(org.id).outerHeight()+"px"})
	}
	
	function setLoc(){
		if(!$( org.id ).is(":visible"))return;
		navLoc( org.id ); navLoc( cssK ); 
		function navLoc( thisID ){
			var conW  = $(thisID).outerWidth();
			var conH  = $(thisID).outerHeight();
			
			var x = ($(window).width()  / 2) - (conW / 2);
			var y = ($(window).height() / 2) - (conH / 2);
			y=(y-y*0.4)+$(document).scrollTop();
			$( thisID ).css({left:x+"px", top: y+"px"})
		}
		BwinH = $(window).height() + $(document).scrollTop();
		$(cssB).height( BwinH );
		
		if(!first){
			setTimeout(setLoc,10);
			first = true;
		}
	}	
	
	function show(){
		
		$(cssK).fadeIn(0);
		$(org.id).fadeIn(0);
		$(cssB).fadeIn(0);
		
		if(html!="" && isload==false ){
			$(org.id).load( html, function(){
				$(org.id+" .close").click(function(){ hide() });	
				isload = true;
				
				reSize();
				if( $.browser.msie&&($.browser.version == "6.0")&&!$.support.style  ){
					$(cssK).fadeOut(0);
					$(org.id).fadeOut(0);
					$(cssB).fadeOut(0);
					
					$(cssK).fadeIn(300);
					$(org.id).fadeIn(300);
					$(cssB).fadeIn(300);
				}
				setLoc();
			});
		}else{
			setLoc();
		}
	}
	function hide(){
	
		$(cssK).fadeOut(300);
		$(org.id).fadeOut(300);
		$(cssB).fadeOut(300);
	}
}

MabenAlertBoxInit = {
	borderWidth : "0px",               //边框的大小
	borderColor : "#62a7db",              //颜色
	borderAlpha : "0.7",               //边框透明度  * 暂未实现
	bgAlpha     : "0.3",               //全屏背景透明度
	bgColor     : "#fff",              //全屏背景颜色
	boxShadow   : "none",              //阴影
	speedTime   : 300,                 //效果持续时间
	level       : "5000",                //层级 如弹框被遮挡时将此数值调大
	
	initFun     : null,   
	okFun       : null,   // .alertBoxOk
	cancelFun   : null,   // .alertBoxCancel
	hideFun     : null
}

$.alertBoxNew = function(options){
	var thisClass = new MabenAlertBox(options);
	thisClass.id  = options.id;
	thisClass.create();
	thisClass.show();
	thisClass.bindEvent();
	
	if(typeof(options.okFun)    =="function"){ thisClass.okEvent(); }
	if(typeof(options.cancelFun)=="function"){ thisClass.cancelEvent(); }
};

$.fn.alertBoxNew = function(options){
	var org={act:"click"};
	$.extend(org, options);
	var $this = $(this);
	
	var thisClass = new MabenAlertBox(options);
	thisClass.id  = options.id;
	thisClass.create();
	thisClass.bindEvent();
	
	switch( org.act ){
		case "click":
			$this.click(function(){
				thisClass.show();
				if(typeof(options.okFun)    =="function"){ thisClass.okEvent($(this)); }
				if(typeof(options.cancelFun)=="function"){ thisClass.cancelEvent($(this)); }
			});
			break;
		default:
			alert("没有该事件!"); break;
	};
};

function MabenAlertBox(options){
	this.org = jQuery.extend(true, {}, MabenAlertBoxInit);   //对象的深度克隆
	$.extend(this.org, options);
	this.id  = null;    //弹框内容标签id
	this.k   = null;    //弹框内容标签外层 包裹一层div 并添加样式 使其居中在屏幕中间
	this.bg  = null;    //锁屏背景
	
	var $this = this;
	
	this.create=function(){
		$this.createTag();
		$this.createBg();
		dragEvent();
		loadHtml();
	};
	
	//创建外框标签
	this.createTag = function(){
		if( $this.id.data("MabenAlertBox_k") ){ 
			$this.k = $this.id.parent();
			return
		};
		$this.id.data("MabenAlertBox_k", true);
		$this.id.show();
		$this.k = $this.id.wrap( "<div></div>" ).parent();
		$this.k.css({ border:parseInt($this.org.borderWidth)+"px solid "+$this.org.borderColor, display:"none", "box-shadow":$this.org.boxShadow, position:"fixed", left:"0px", top:"0px", "z-index":parseInt($this.org.level)+1 })
		$this.closeEvent();
		
		if(reIE6()) $this.k.css({position:"absolute"});
	};
	//创建锁屏背景
	this.createBg = function(){
		if( $this.id.data("MabenAlertBox_bg") ){ 
			$this.bg = $this.id.data("MabenAlertBox_bg"); return;
		}; 
		$this.bg = $("<div></div>").appendTo("body");
		$this.bg.css({ width:"100%", display:"none", "background-color":$this.org.bgColor, position:"fixed", "z-index":parseInt($this.org.level)-1, opacity:$this.org.bgAlpha, left:"0px", top:"0px" })
		$this.id.data("MabenAlertBox_bg", $this.bg);
		
		if(reIE6()) $this.bg.css({position:"absolute"});
	};
	
	$(window).resize(function(){ $this.reSize(true) });
	if(reIE6()) $(window).scroll(function(){$this.reSize()});
	
	//重定位
	this.reSize = function( flash ){
		if(!$( $this.k ).is(":visible")) return;
		var conW  = $this.k.outerWidth();
		var conH  = $this.k.outerHeight();
		var x = ($(window).width()/2) - (conW/2);
		var y = ($(window).height()*0.382) - (conH/2);
		
		flash ? $this.k.stop().animate({left:x+"px", top: y+"px"}) : $this.k.css({left:x+"px", top: y+"px"})
		$this.bg.height( $(window).height() );
		
		if(reIE6()){
			$this.bg.height( $(document).height() );
			$this.k.css("top", parseInt($this.k.css("top"))+$(document).scrollTop());
		}
	};	
	//显示弹层
	this.show = function(){
		$this.org.initFun($this);
		$this.k.fadeIn($this.org.speedTime);
		$this.bg.fadeIn($this.org.speedTime);
		$this.reSize();
		if( reIE6() ) $("select").show();
	};
	//隐藏弹层
	this.hide = function(){
		if( typeof($this.org.hideFun)=="function" ){ if( $this.org.hideFun($this)==false ) return };
		$this.k.fadeOut($this.org.speedTime);
		$this.bg.fadeOut($this.org.speedTime);
		if( reIE6() ) $("select").hide();
	};
	//绑定自定义事件
	this.bindEvent = function(){
		$this.id.unbind("show", "hide");
		$this.id.bind("show", function(event){ $this.show() });
		$this.id.bind("hide", function(event){ $this.hide() });
	};
	//添加关闭按钮事件
	this.closeEvent = function(){
		$this.id.find(".close").live("click", function(){
			$this.hide();	
		});
	};
	//添加确定按钮事件
	this.okEvent = function(element){
		var tag = $this.id.find(".alertBoxOk");
		tag.die("click").live("click", function(){
			$this.org.okFun($this, element);
		});
	};
	//添加取消按键事件
	this.cancelEvent = function(element){
		var tag = $this.id.find(".alertBoxCancel");
		tag.die("click").live("click", function(){
			$this.org.cancelFun($this, element);
		});
	};
	//添加拖拽事件
	function dragEvent(){
		var $drag = $this.id.find(".alertBoxDrag");
		if($drag.length==0) return;
		$drag.css({cursor:"move", "-moz-user-select":"none", "-webkit-user-select":"none"});
		$drag.bind("selectstart", function(){return false});		
		var x,y;                          //鼠标离控件左上角的相对位置 
		$drag.mousedown(function(e){
			x=e.pageX-parseInt($this.k.css("left")); 
			y=e.pageY-parseInt($this.k.css("top"));
			$(document).mousemove(move).mouseup(up);
		});
		function move(e){
			var wiw = $(window).width();
			var wih = $(window).height();
			var thw = $this.k.outerWidth();
			var thk = $drag.outerHeight();
			var dx=e.pageX-x<0 && "0" || e.pageX-x>wiw-thw && wiw-thw || e.pageX-x;
			var dy=e.pageY-y<0 && "0" || e.pageY-y>wih-thk && wih-thk || e.pageY-y; 
			$this.k.css({left:dx, top:dy});
		};
		function up(e){ $(document).unbind("mousemove",move).unbind("mouseup",up) };
	};
	
	function loadHtml(){
		if( $this.id.attr("data-alertUrl") == undefined ) return;
		var url = $this.id.attr("data-alertUrl");
		
		$.ajax({
			url: url,
			//beforeSend: function(){
			//},
			success: function(html){
				$this.id.append(html);
				$this.reSize();
			}
		});
		$this.id.removeAttr("data-alertUrl");
	};
	
	function reIE6(){ return $.browser.msie&&($.browser.version == "6.0")&&!$.support.style };
}


/* 
	@ 增减
*/
function addInpPrint( minNum, maxNum ){
	var k = $(".inpNum");
	
	k.find(".u").click(function(){
		var input = k.find("input");
		var num = parseInt(input.val())+1;
		if( num > maxNum && maxNum!=undefined ){ return };
		input.val(num);
	})
	
	k.find(".d").click(function(){
		var input = k.find("input");
		var num = parseInt(input.val())-1;
		if( num < minNum && minNum!=undefined ){ return };
		input.val(num);
	})
	
}

/*
	@ 弹框
*/
MabenAlertTsInit = {
	face        : "right",
	left        : 0,
	val         : "请添加内容",
	top         : 0,
	effect      : "",
	level       : 50,
	aniSpeed    : 200,
	width       : "auto",
	aSize       : 5,
	aLoc        : "auto",
	border      : "1px solid #dcdac8",
	bgColor     : "#fdfdf1",
	boxShadow   : "1px 1px 2px #d5d3be",
	color       : "#666666",
	btnClose    : true
}


$.alertTsTimerNew = function(options){
	var thisClass = new MabenAlertTs(options);
	thisClass.init();
	thisClass.resizeForTag(options.id);
	thisClass.tag.show();
	thisClass.$close.remove();
	setTimeout(function(){ thisClass.removeTag() },3000);
};
$.alertTsNew = function(options){
	var thisClass = new MabenAlertTs(options);
	thisClass.init();
	thisClass.resizeForTag(options.id);
	thisClass.tag.show();
	thisClass.$close.remove();
	
	//options.id.bind("hide", function(event){ thisClass.removeTag() });
	//$(this).bind("hide", function(event){ thisClass.removeTag() });
};

$.fn.alertTsNew = function(options){
	if( $(this).data("alertTs_isLife")) return; $(this).data("alertTs_isLife",true);  //防止重复创建
	var thisClass = new MabenAlertTs(options)
	thisClass.init();
	
	var $this = $(this);
	switch(options.act){
		case "click":
			$this.bind("click", function(){
				attrStep( $(this) );
				thisClass.show();
				thisClass.resizeForTag( $(this) );
			});
			thisClass.$close.click(function(){ thisClass.hide() });
			break;
		case "otherClick":
			$this.bind("click", function(){
				attrStep( $(this) );
				thisClass.show();
				thisClass.resizeForTag( $(this) );
				return false;
			});
			thisClass.$close.click(function(){ thisClass.hide() });
			thisClass.tag.click(function(){ return false });
			$(document).bind("click", function(event){ thisClass.hide(); });	
			break;
		case "followMouse":
			thisClass.$close.remove();
			$this.mousemove(function(event){ thisClass.resizeForLoc(event.pageX, event.pageY) });
			$this.hover(function(event){
				thisClass.resizeAngLoc();
				attrStep($(this));
				thisClass.setWidth({addW:1});
				thisClass.show("none");
				thisClass.resizeForLoc(event.pageX, event.pageY)
			},function(){
				thisClass.hide("none");
			});
			break;
		default:
			break;
	};
	function attrStep($this){
		getAttrFace($this);
		getAttrVal ($this);
	};
	function getAttrVal( $this ){
		var str = $this.attr("data-alertTs");
		if( str!=undefined ){
			thisClass.setVal( str );
		};
	};
	function getAttrFace( $this ){
		var str = $this.attr("data-alertTsFace");
		if( str!=undefined ){
			thisClass.org.face=str;
			thisClass.reCreateTag();
			thisClass.$close.remove();
		}else{
			if( options==undefined || !options.hasOwnProperty("face") ){
				if(thisClass.org.face!=MabenAlertTsInit.face){
					thisClass.org.face=MabenAlertTsInit.face;
					thisClass.reCreateTag();
					thisClass.$close.remove();
				}
			}else if( thisClass.org.face!=options.face){
				thisClass.org.face=options.face;
				thisClass.reCreateTag();
				thisClass.$close.remove();
			}
		}
	};
}

function MabenAlertTs(options){
	this.org = jQuery.extend(true, {}, MabenAlertTsInit); $.extend(this.org, options);
	this.tag  = null;
	this.val  = null;
	this.$close= null;
	this.ang  = null;
	var $this = this;
	var thisW = this.org.width;
	var aLocLeft, aLocTop;
	if( this.org.aLoc=="auto" ){
		aLocLeft=15; aLocTop=5;	
	}else{
		aLocLeft=parseInt(this.org.aLoc); aLocTop=parseInt(this.org.aLoc);
	};
	
	$this.setVal = function( str ){
		str ? $this.val.html( str ) : $this.val.html( $this.org.val );
	};
	
	$this.init = function(){
		createTag();
		$this.setVal();
	};
	
	function createTag(){
		var org=$this.org;
		createCss();
		$this.tag = $("<div class='alertTs_k'></div>").appendTo("body");
		$this.tag.css({ "z-index":org.level, border:org.border, background:org.bgColor, color:org.color, "box-shadow":org.boxShadow, "border-radius":2 });
		$this.val = $("<span></span>").appendTo($this.tag);
		createAng();
		createClose();
		
		function createClose(){
			if($this.$close=="none") return;
			$this.$close = $("<span class='alertTs_close'>x</span>").appendTo($this.tag)
		};
		
		function createAng(){
			var org = $this.org;
			if( org.aSize<=2 ) return;
			var str;
			switch(org.face){
				case "top":
					str = '<div class="alertTs_angle alertTs_top1"></div><div class="alertTs_angle alertTs_top2"></div>'
					break;
				case "right":
					str = '<div class="alertTs_angle alertTs_right1"></div><div class="alertTs_angle alertTs_right2"></div>'
					break;
				case "bottom":
					str = '<div class="alertTs_angle alertTs_bottom1"></div><div class="alertTs_angle alertTs_bottom2"></div>'
					break;
				case "left":
					str = '<div class="alertTs_angle alertTs_left1"></div><div class="alertTs_angle alertTs_left2"></div>'
					break;
			}
			$this.ang = $(str).appendTo($this.tag);
			org.face=="top" || org.face=="bottom" ? $this.ang.css({ left:parseInt(org.aLoc)+"px" }) : $this.ang.css({ top:parseInt(org.aLoc)+"px" })
		}; // end createAng
		
		function createCss(){
			if( $("style").is("[title=alertTs_style]") )  return;
			var org = $this.org;
			var css = ".alertTs_k{ padding:2px 12px 2px 8px; font-size:12px; line-height:20px; position:absolute; display:none;}" +
			".alertTs_close{ color:#bbbab4; font-size:10px; font-family:Verdana; position:absolute; top:-5px; right:2px; cursor:pointer }" +
			".alertTs_angle{width:0px; height:0px; line-height:0px; border:"+parseInt(org.aSize)+"px solid #000; position:absolute; }" +
			".alertTs_top1{ bottom:-"+(parseInt(org.aSize)*2)+"px;   left:"+aLocLeft+"px; border-color:#fdfdf1 transparent transparent transparent; _border-style:solid dashed dashed dashed; z-index:10; }" +
			".alertTs_top2{ bottom:-"+(parseInt(org.aSize)*2+1)+"px; left:"+aLocLeft+"px; border-color:#e6e5dc transparent transparent transparent; _border-style:solid dashed dashed dashed; z-index:1; }" +
			".alertTs_bottom1{ top:-"+(parseInt(org.aSize)*2)+"px;   left:"+aLocLeft+"px; border-color:transparent transparent "+org.bgColor+" transparent; _border-style:dashed dashed solid dashed; z-index:10; }" +
			".alertTs_bottom2{ top:-"+(parseInt(org.aSize)*2+1)+"px; left:"+aLocLeft+"px; border-color:transparent transparent "+org.bgColor+" transparent; _border-style:dashed dashed solid dashed; z-index:1; }" +
			".alertTs_right1{ left:-"+(parseInt(org.aSize)*2)+"px;   top:"+aLocTop+"px;   border-color:transparent "+org.bgColor+" transparent transparent; _border-style:dashed dashed dashed solid; z-index:10; }" +
			".alertTs_right2{ left:-"+(parseInt(org.aSize)*2+1)+"px; top:"+aLocTop+"px;   border-color:transparent "+org.bgColor+" transparent transparent; _border-style:dashed dashed dashed solid; z-index:1; }" +
			".alertTs_left1{ right:-"+(parseInt(org.aSize)*2)+"px;   top:"+aLocTop+"px;   border-color:transparent transparent transparent "+org.bgColor+"; _border-style:dashed dashed dashed solid; z-index:10; }" +
			".alertTs_left2{ right:-"+(parseInt(org.aSize)*2+1)+"px; top:"+aLocTop+"px;   border-color:transparent transparent transparent "+org.bgColor+"; _border-style:dashed dashed dashed solid; z-index:1; }";
			$("body").append("<style title='alertTs_style'>"+ css +"</style>");
		};
	};
	
	$this.removeTag = function(){
		$this.tag.remove();
	};
	
	$this.reCreateTag = function(){
		$this.removeTag();
		createTag();
	};

//set
	$this.setWidth = function( options ){
		var o={
			addW:0
		}; $.extend(o,options);
		$this.tag.width("auto");
		var W = $this.tag.innerWidth() - parseInt($this.tag.css("padding-left")) - parseInt($this.tag.css("padding-right")) + o.addW;
		thisW = $this.org.width=="auto" && W || $this.org.width;
		$this.tag.width( parseInt(thisW)+"px" );
	};

//resize
	$this.resizeForTag = function( $id ){
		var org   = $this.org;
		var tag   = $this.tag;
		var pd    = 1;
		var x     = $id.offset().left;
		var y     = $id.offset().top;
		var width = $id.outerWidth();
		var height= $id.outerHeight();
		switch(org.face){
			case "top":
				x = x + parseInt(org.left) - (aLocLeft + parseInt($this.org.aSize)/2);
				y = y - tag.outerHeight() + parseInt(org.top) - org.aSize - pd;
				break;
			case "right":
				x = x + width + org.aSize + parseInt(org.left) + pd;
				y = y + parseInt(org.top) - (aLocLeft + parseInt($this.org.aSize)/2);
				break;
			case "bottom":
				x = x + parseInt(org.left) - (aLocLeft + parseInt($this.org.aSize)/2);
				y = y + height + parseInt(org.top) + org.aSize + pd;
				break;
			case "left":
				x = x - tag.outerWidth() - org.aSize - parseInt(org.left) - pd;
				y = y + parseInt(org.top) - (aLocLeft + parseInt($this.org.aSize)/2);
				break;
		}
		tag.css({ left:x+"px", top:y+"px" });
	};
	
	function reSizeForWindow(x,y){
		var tag  = $this.tag;
		var win  = $(window).width();
		var width= thisW;
		var pad  = parseInt(tag.css("padding-left"))+parseInt(tag.css("padding-right"))+5;
		var tagover = win-(x+width+pad);
		var angover = aLocLeft-tagover;
		if(x+width+pad > win){
			$this.tag.css({ left:(x+tagover)+"px", top:y+"px" });
			if($this.ang!=null) $this.ang.css({ left:angover+"px" });
			return true;
		}else{
			return false;	
		}
	};
	
	$this.resizeAngLoc = function(){ 
		if($this.ang==null) return; 
		$this.org.face=="top" || $this.org.face=="bottom" ? $this.ang.css({ left:aLocLeft+"px" }) : $this.ang.css({ top:aLocTop+"px" });
	};
	
	$this.resizeForLoc = function(x,y){
		var org = $this.org;
		var tag = $this.tag;
		var width = tag.outerWidth();
		var height= tag.outerHeight();
		var pd = 12;
		switch(org.face){
			case "top":
				y = y - height - org.aSize - pd;
				x = x - (aLocLeft + parseInt($this.org.aSize)/2);
				break;
			case "right":
				x = x + org.aSize + pd;
				y = y - (aLocLeft + parseInt($this.org.aSize)/2);
				break;
			case "bottom":
				y = y + org.aSize + pd;
				x = x - (aLocLeft + parseInt($this.org.aSize)/2);
				break;
			case "left":
				x = x - width - org.aSize - pd;
				y = y - (aLocLeft + parseInt($this.org.aSize)/2);
				break;
		}
		if(reSizeForWindow(x, y)) return;
		$this.tag.css({ left:x+"px", top:y+"px" });
	};
//end resize
//show
	$this.show = function( effect ){
		var org = $this.org;
		var tag = $this.tag;
		if(effect==undefined){effect = $this.org.effect};
		switch( effect ){
			case "show":
				tag.show( org.aniSpeed );     break;	
			case "slide" :
				tag.slideDown(org.aniSpeed);  break;
			case "fade":
				tag.fadeIn(org.aniSpeed);     break;
			default:
				tag.show();                   break;
		}
	};
	
	$this.hide = function( effect ){
		var org = $this.org;
		var tag = $this.tag;
		var speed = org.aniSpeed*0.7
		if(effect==undefined){effect = $this.org.effect};
		switch( effect ){
			case "show"	:
				tag.hide(speed);    break;
			case "slide" :
				tag.slideUp(speed); break;
			case "fade" :
				tag.fadeOut(speed); break;
			default:
				tag.hide();         break;
		}
	};
//end show	
};


var ALERTNUM = 0;
var ALERTORG = {
	id          : "",
	act         : "click",
	face        : "left",
	width       : "",
	height      : "",
	left        : "0px",
	top         : "0px",
	jiaoSize    : "14px",
	jiaoLoc     : "70px",
	borderStyle : "solid",
	borderColor : "#bedde0",
	borderWidth : "1px",
	bgColor     : "#fff",
	effect      : "",
	boxShadow   : "1px 1px 4px #bababa",
	isShow      : false,
	level       : "50",
	aniSpeed    : 300,
	delaytime   : 200,
	showFun     : function(){},
	hideFun     : function(){}
}

$.fn.alertTsG = function(options){
	var org = $.extend({}, ALERTORG, options);
	var classEle = $(document.body).find( $(this) );

	classEle.each(function(i){
		if($(this).data("is")){ return }
		var className = (classEle.eq(i).attr("class")).replace(/[ ]/g,"");
		ALERTNUM=ALERTNUM+1;
		var setID = className+"___"+ALERTNUM;
		classEle.eq(i).attr("id", setID).data("is",true);
		$( "#"+ setID ).alertTs(org);
	})
}

$.fn.alertTs = function(options) {
	var org = $.extend({}, ALERTORG, options);
	var _this = $(this)
	var _cssK ;
	var cssK;
	var _cssJ;
	var cssJ;
	var flag  =  true;
	var isOver;
	var isOut;
	var getID = $(org.id);

	var initial = {
		createK:function(){
			if( _this.attr("data-alt")!= undefined ){
				$("body").append("<div class="+_cssK+">"+ _this.attr("data-alt") +"</div>")	
				$(cssK).css({ padding:"8px 8px 6px 8px", "font-size":"12px", "line-height":"20px"});
				if(org.width !=""){ $(cssK).css("width",parseInt(org.width)+"px") }
				if(org.height!=""){ $(cssK).css("height",parseInt(org.height)+"px") }
				org.id = cssK;
			}else{
				$(org.id).wrap("<div class="+_cssK+"></div>");
				$(org.id).show();
			}
		},
		start:function(){
			var str = $(org.id).parent().attr("class");
			if( str!=undefined && str.search("___k")!=-1 ){		
				cssK  = "." + str;
				cssJ  = "."+$(org.id).parent().children("div").eq(0).attr("class");
			}else{
				_cssK = _this.attr("id")+"___k";
				cssK  = "."+_cssK;
				_cssJ = _this.attr("id")+"J";
				cssJ  = "."+_cssJ;
				
				initial.createK();
				$(cssK).css({ position:"absolute", "z-index":org.level, background:org.bgColor, border:org.borderWidth+" "+org.borderStyle+" "+org.borderColor, "box-shadow":org.boxShadow });
				if( parseInt(org.jiaoSize)>0 ){ createAng(); }
			}
			
			$(cssK).hide();
			if( org.isShow ){ show() }
		}
	}
	
	
	var loc = {
		x : _this.offset().left,
		y : _this.offset().top,
		width : this.width(),
		height: this.height(),
		
		ang : function( getH ){
			var n1 = (parseInt(org.jiaoSize)+parseInt(org.borderWidth)*2)/2;
			return n1-getH/2;
		},
		xy  : function(){
			var x,y;
			var pd = 3;
			switch(org.face){
				case "top":
					x = loc.x + parseInt(org.left)-parseInt(org.jiaoLoc);
					y = loc.y - $(cssK).outerHeight() + parseInt(org.top) - $(cssJ).height() - pd;
					break;
				case "right":
					x = loc.x + loc.width + $(cssJ).width() + parseInt(org.left) + pd;
					y = loc.y + parseInt(org.top) - parseInt(org.jiaoLoc);
					break;
				case "bottom":
					x = loc.x + parseInt(org.left)-parseInt(org.jiaoLoc);
					y = loc.y + loc.height + parseInt(org.top) + $(cssJ).height() + pd;
					break;
				case "left":
					x = loc.x - $(cssK).outerWidth() - $(cssJ).width() + parseInt(org.left) - pd;
					y = loc.y - parseInt(org.jiaoLoc) + parseInt(org.top);
					break;
				default:
					break;
			}
			$(cssK).css({ left:x+"px", top:y+"px" });
		},
		resize:function(){
			loc.x = _this.offset().left; 
			loc.y = _this.offset().top;
			loc.xy();
		}
	}
	
	var setTimer = {
		show : function(){ return function(){ show() }	},
		hide : function(){ return function(){ hide() }	},
		exe  : function( str ){
			if( str =="show" ){
				var t = setTimer.show();
				return setTimeout(t, org.delaytime);
			}else{
				var t = setTimer.hide();
				return setTimeout(t, org.delaytime);
			}
		}
	}
	
	initial.start();
	
	switch( org.act ){
		case "tagOver":
			_this.hover(function(){
				isOver = setTimer.exe("show");
			},function(){
				clearTimeout( isOver );
				hide();
			})
			break;
		case "click":
			_this.click(function(){ show();})
			break;
		case "otherClick":
			_this.click(function(event){ 
				event.stopPropagation();    //禁止向上冒泡
				show();
			});
			$(org.id).click(function(event){ event.stopPropagation() });
			$(document).click(function(event){ hide() });			
			break;
		case "otherClickLive":
			_this.live("click", function(event){ 
				event.stopPropagation();    //禁止向上冒泡
				show();
			});
			$(org.id).live("click",function(event){ event.stopPropagation() });
			$(document).click(function(event){ hide() });			
			break;
		case "otherClickTrigger":
			_this.click(function(event){ 
				event.stopPropagation();    //禁止向上冒泡
				//show();
			});
			$(org.id).click(function(event){ event.stopPropagation() });
			$(document).click(function(event){ hide() });
			break;
		case "hover":
			var setTimeOut = setTimer.hide();
			_this.hover(function(){ 
				clearTimeout( isOut );
				isOver = setTimer.exe("show");
			},function(){
				clearTimeout( isOver );
				if( !$(org.id+":visible") )return;
				isOut = setTimeout(setTimeOut, org.delaytime);
			});
			
			$(org.id).hover(function(){
				clearTimeout( isOut );
			},function(){
				isOut = setTimeout(setTimeOut, org.delaytime);
			})
			break;
		default:
			break;
	}
	_this.bind("show", function(event){ show() });
	_this.bind("hide", function(event){ hide() });
	$(cssK+" .close").click(function(){ hide() })
	
	function show(){
		loc.resize();
		if( !flag ) return;
		flag = false; 
		switch( org.effect ){
			case "show":
				$(cssK).show( org.aniSpeed );
				break;	
			case "slide" :
				$(cssK).slideDown(org.aniSpeed);
				break;
			case "fade":
				$(cssK).fadeIn(org.aniSpeed);
			default:
				$(cssK).show();
				break;
		}
		org.showFun(_this, getID);
	}
	
	function hide(){
		var speed = org.aniSpeed*0.7
		switch( org.effect ){
			case "show"	:
				$(cssK).hide(speed, function(){ flag=true });
				break;
			case "slide" :
				$(cssK).slideUp(speed, function(){ flag=true });
				break;
			case "fade" :
				$(cssK).fadeOut(speed, function(){ flag=true });
				break;
			default:
				$(cssK).hide(); flag=true;
				break;
		}
		org.hideFun(_this, getID);
	}
	
	function createAng(){
		var i = 0;
		var j = 0;
		var ph = parseInt(org.borderWidth)*2;
		$(cssK).prepend("<div class="+_cssJ+"></div>");
		switch(org.face){
			case "top"	:
				extH = parseInt(org.jiaoSize);
				while( extH>0 ){
					$(cssJ).append("<ol></ol>");
					$( cssJ+" ol" ).eq(i).css({ top:i+"px", width:extH+"px", left:loc.ang(extH+ph)+"px" });
					extH=extH-2; i++;
				}
			
				extH = parseInt(org.borderWidth)*2;
				for(var k=0; k<parseInt(org.borderWidth); k++){
					$(cssJ).append("<ol class='nav'></ol>");
					$( cssJ+" ol" ).eq(i).css({ top:i+"px", width:extH+"px", left:loc.ang(extH)+"px" });
					extH=extH-2; i++;
				}
				
				$(cssJ).css({ width:i+"px", bottom:-i+"px", left:org.jiaoLoc, width:(parseInt(org.jiaoSize)+ph)+"px", height:i+"px"});
				$( cssJ+" ol").css({ height:"1px" });
				$( cssJ+" ol:not(.nav)"  ).css({ height:"1px", border:org.borderWidth+" "+org.borderStyle+" "+org.borderColor, "border-top":"0", "border-bottom":"0" });
				break;
			case "right":
				var extH = 2;
				for(var k=0; k<parseInt(org.borderWidth); k++){
					$(cssJ).append("<ol class='nav'></ol>");
					$( cssJ+" ol" ).eq(i).css({ left:i+"px", height:extH+"px", top:loc.ang(extH)+"px" });
					extH=extH+2; i++;
				}
			
				extH = 2;
				while( extH < parseInt(org.jiaoSize) ){
					$(cssJ).append("<ol></ol>");
					$( cssJ+" ol" ).eq(i).css({ left:i+"px", height:extH+"px", top:loc.ang(extH+ph)+"px" });
					extH=extH+2; i++;
				}

				
				$(cssJ).css({ width:i+"px", left:-i+"px", top:org.jiaoLoc, height:(parseInt(org.jiaoSize)+ph)+"px"});
				$( cssJ+" ol" ).css({ width:"1px" })
				$( cssJ+" ol:not(.nav)"  ).css({ width:"1px", border:org.borderWidth+" "+org.borderStyle+" "+org.borderColor, "border-left":"0", "border-right":"0" });
				break;
			case "bottom":
				var extH = 2;
				for(var k=0; k<parseInt(org.borderWidth); k++){
					$(cssJ).append("<ol class='nav'></ol>");
					$( cssJ+" ol" ).eq(i).css({ top:i+"px", width:extH+"px", left:loc.ang(extH)+"px" });
					extH=extH+2; i++;
				}
				
				extH = 2;
				while( extH < parseInt(org.jiaoSize)){
					$(cssJ).append("<ol></ol>");
					$( cssJ+" ol" ).eq(i).css({ top:i+"px", width:extH+"px", left:loc.ang(extH+ph)+"px" });
					extH=extH+2; i++;
				}
				
				$(cssJ).css({ width:i+"px", top:-i+"px", left:org.jiaoLoc, width:(parseInt(org.jiaoSize)+ph)+"px", height:i+"px"});
				$( cssJ+" ol").css({ height:"1px" });
				$( cssJ+" ol:not(.nav)"  ).css({ height:"1px", border:org.borderWidth+" "+org.borderStyle+" "+org.borderColor, "border-top":"0", "border-bottom":"0" });
				break;
			case "left":
				var extH = parseInt(org.jiaoSize);
				while( extH>0 ){				
					$(cssJ).append("<ol></ol>");
					$( cssJ+" ol" ).eq(i).css({ left:i+"px", height:extH+"px", top:loc.ang(extH+ph)+"px" });
					extH=extH-2; i++;
				}
				
				extH = parseInt(org.borderWidth)*2;
				for(var k=0; k<parseInt(org.borderWidth); k++){
					$(cssJ).append("<ol class='nav'></ol>");
					$( cssJ+" ol" ).eq(i).css({ left:i+"px", height:extH+"px", top:loc.ang(extH)+"px" });
					extH=extH-2; i++;
				}
				
				$(cssJ).css({ width:i+"px", right:-i+"px", top:org.jiaoLoc, height:(parseInt(org.jiaoSize)+ph)+"px"});
				$( cssJ+" ol" ).css({ width:"1px" })
				$( cssJ+" ol:not(.nav)" ).css({ width:"1px", border:org.borderWidth+" "+org.borderStyle+" "+org.borderColor, "border-left":"0", "border-right":"0" });
				break;
			default:
				break;
		}
		
		$(cssJ).css({ position:"absolute" });
		$( cssJ+" ol:not(.nav)" ).css({ background:org.bgColor, position:"absolute", overflow:"hidden", padding:"0", margin:"0", "z-index":"10" });
		$( cssJ+" .nav" ).css({ background:org.borderColor, position:"absolute", overflow:"hidden", padding:"0", margin:"0", "z-index":"10" });
	} //createAng
	
}

/*
	@ 全选
*/
function jsAllCheckBox( getID ){
	$(getID).click(function() {
		var input = $("input[name="+this.name+"]:checkbox")
		if(this.checked){
			input.attr("checked", true);
			
		}else{ 
			input.attr("checked", false)
		}
	})
}


function selectToInp( clickID, valueID, options ){
	var setValue = function($this, $show){
		//添加对√
		/*
		$show.find("input:checkbox").removeAttr("checked");
		var getVal = $this.val();
		if( getVal!="" ){
			var array = getVal.split(",");	
			for(var i=0; i<array.length; i++){
				$show.find("li").each(function(){
					if( $.trim($(this).text())==$.trim(array[i])){
						$(this).find("input").attr("checked","checked");
						return;
					}
				})
			};
		};
		*/
		
		//多选
		$show.find("input:checkbox").unbind("click").bind("click", function(){
			var li = $(this).closest("li");
			
			if(li.find("ul").size()==0) return
			if( this.checked ){
				li.find("input:checkbox").attr("checked",true);
			}else{
				li.find("input:checkbox").attr("checked",false);
			}
		})
		
		//下拉列表选择
		var ul = $(valueID+" ul");
		ul.unbind("click");
		ul.bind("click", function(){
			var str="";
			$(this).find("li").each(function(i){
				if($(this).find("ul").size()>0) return;
				if(str==""){
					if($(this).find("input").is(":checked")) { str += $.trim($(this).text()) };	
				}else{
					if($(this).find("input").is(":checked")) { str += ","+$.trim($(this).text()) };
				}
			})
			
			$this.find("option").text(str);
			org.ck( $this );
		})
	}
	
	//下拉框效果
	var org={
		id:valueID,
		act:"otherClick", 
		face:"bottom", 
		jiaoSize:"0px", 
		top:"3px", 
		left:"-63px;",
		ck:function(n){},
		showFun:setValue
	}
	$.extend(org, options);
	$(clickID).alertTsG(org);
}



/*
	@ 弹出框
*/

$("#show").click(function(){
	(".newshow").show();
})

