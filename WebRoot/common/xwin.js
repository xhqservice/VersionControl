var x0=0,y0=0,x1=0,y1=0;
//var offx=6,offy=6;
var moveable=false;
var hover='orange',normal='slategray';//color;
var xwinindex=10000;//z-index;
var imgpath = "../images";


//开始拖动;
function startDrag(obj)
{	
	//锁定标题栏;
    if(obj.setCapture){
		obj.setCapture();
 	}else if(window.captureEvents){
    	window.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
 	}
                
    //定义对象;
    var win = obj.parentNode;
             
    //记录鼠标和层位置;
    var e = e||window.event;
    x0 = e.clientX ;
    y0 = e.clientY ;

    x1 = parseInt(win.style.left);
    y1 = parseInt(win.style.top);
 
    moveable = true;
}
//拖动;
function drag(obj,width)
{		 
		if(moveable) {   
		
		 	var win = obj.parentNode;
               var e = event || window.event;   
                  
               if(x1 + e.clientX - x0 + +  (width-2*14-4)>0) {
                win.style.left = x1 + e.clientX - x0 +"px";
               }
               if(y1 + e.clientY - y0 + 5>0) {
                win.style.top  = y1 + e.clientY - y0 +"px";
            }
               
           }   
           
}
//停止拖动;
function stopDrag(obj)
{	 
        
          var win = obj.parentNode;
          var msg = obj.nextSibling;
          if (!window.releaseEvents) {    
           obj.releaseCapture();       
	       } else {       
	           window.releaseEvents(Event.MOUSEMOVE|Event.MOUSEUP);       
	       }   
          moveable = false;
        
}
//获得焦点;
function getFocus(obj)
{
        if(obj.style.zIndex!=xwinindex)
        {
                xwinindex = xwinindex + 2;
                var idx = xwinindex;
                obj.style.zIndex=idx;
                obj.nextSibling.style.zIndex=idx-1;
        }
}
//最小化;
function min(obj)
{
        var win = obj.parentNode.parentNode;
        var sha = win.nextSibling;
        var tit = obj.parentNode;
        var msg = tit.nextSibling;
        var flg = msg.style.display=="none";
        if(flg)
        {
                win.style.height  = parseInt(msg.style.height) + parseInt(tit.style.height) + 2*2;
                sha.style.height  = win.style.height;
                msg.style.display = "block";
                obj.innerHTML = "0";
        }
        else
        {
                win.style.height  = parseInt(tit.style.height) + 2*2;
                sha.style.height  = win.style.height;
                obj.innerHTML = "2";
                msg.style.display = "none";
        }
}

function xwinshow()
{
        var win = document.getElementById("xMsg" +this.id);
        win.style.visibility = "visible";
}

function xwincls() {
	if (this.onclose && typeof(this.onclose)=="function") {
		this.onclose();
	}
	var win = document.getElementById("xMsg" +this.id);
	//var sha = win.nextSibling;
	win.style.visibility = "hidden";
	//sha.style.visibility = "hidden";
}

function xwinresize(width, height)
{
		var win = document.getElementById("xMsg" +this.id);
		win.style.width = width;
		win.style.height = height;
		var frm = document.getElementById("xMsgBackFrame" +this.id);
		frm.width = width;
		frm.height = height;
		var ttitle = document.getElementById("xMsgTitle" +this.id);
		ttitle.style.width = width;
		var tbar = document.getElementById("xMsgTitleBar" +this.id);
		tbar.style.width = width;
		var tbarcon = document.getElementById("xMsgTitleBarCon" +this.id);
		tbarcon.style.width = width;
		var msgcon = document.getElementById("xMsgCon" +this.id);
		msgcon.style.width = width;
		msgcon.style.height = height - 25;
		var infrm = document.getElementById("xMsgFrame" + this.id);
		if(infrm){
			infrm.width = width - 10;
			infrm.height = height - 29;
		}	
}

function xwinreposition(left, top)
{
		var win = document.getElementById("xMsg" +this.id);
		win.style.left = left;
		win.style.top = top;
		this.left = left;
		this.top = top;		
}

//创建一个对象;
function xWin(id,w,h,l,t,tit,msg)
{
        xwinindex = xwinindex+2;
        this.id      = id;
        this.width   = w;
        this.height  = h;
        this.left    = l;
        this.top     = t;
        this.zIndex  = xwinindex;
        this.title   = tit;
        this.message = msg;
        this.obj     = null;
        this.bulid   = bulid;
        this.close	 = xwincls;
        this.show	 = xwinshow;
        this.resize  = xwinresize;
		this.reposition = xwinreposition;
		this.onclose = null;
        this.bulid(this);
}
//初始化;
function bulid(obj)
{	
	var titleheight = "25"; // 提示窗口标题高度
	var bordercolor = "#2f72b2"; // 提示窗口的边框颜色
	var titlecolor = "#FFFFFF"; // 提示窗口的标题颜色
	var titlebgcolor = "#2f72b2"; 
	var bgcolor = "#FFFFFF"; 
	//1、创建 CONTAINER DIV
	var containerDiv = document.createElement("div"); 
	containerDiv.id = "xMsg"+this.id; 
	containerDiv.style.cssText = "position:absolute;"
									+"font:12px '宋体';"
									+"top:"+this.top+"px;"
									+"left:"+this.left+"px;"
									+"width:"+this.width+"px;"
									+"height:"+this.height+"px;"
									+"border:2px solid "+bordercolor+";"
									+"background-color:"+normal+";"
									+ "color:" + normal + ";"
									+"z-index:1000;";
	document.body.appendChild(containerDiv);
	//2、创建iframe
	var iframe = document.createElement("iframe");
	iframe.id = "xMsgBackFrame"+this.id; 
	iframe.setAttribute("width", this.width+"px");
	iframe.setAttribute("height", this.height+"px");
	iframe.setAttribute("frameborder","no");

	containerDiv.appendChild(iframe); 
	//3.1、创建头部
	var dragDiv = document.createElement("div");
	dragDiv.id = "xMsgTitle"+ this.id ;
    dragDiv.style.cssText = "z-index:1;width:" + this.width + "px;"
                       		+ "height:25px;"
	                        + "position:absolute;"
	                        + "left:0px;top:0px;"
	                        + "background: url("+imgpath+"/hd-sprite.gif) no-repeat 0 -41px;"
	                        + "margin:0px;overflow:hidden;"
	                
	containerDiv.appendChild(dragDiv);  
	dragDiv.onmousedown = function (){
		startDrag(this);
	}
	dragDiv.onmouseup = function (){
		stopDrag(this);
	}
	var objWidth = this.width;
    dragDiv.onmousemove = function (){
		drag(this,objWidth);
	}
              
	
	
	 
	var dragHtmlDiv = document.createElement("div");
	dragHtmlDiv.id = "xMsgTitleBar"+ this.id ;
    dragHtmlDiv.style.cssText = "width:" + (this.width) + "px;"
                        	+ "height:25px;"
                        	+ "background:url("+imgpath+"/hd-sprite.gif) no-repeat right 0;"
                        	
    dragDiv.appendChild(dragHtmlDiv);  
    
    var dragHtmlDiv_ = document.createElement("div");
	dragHtmlDiv_.id = "xMsgTitleBarCon"+ this.id ;
    dragHtmlDiv_.style.cssText = "background:url("+imgpath+"/hd-sprite.gif) repeat-x 0 -82px;"
                        		+ "background-color:navy;"
                        		+ "width:" + (this.width) + "px;"
                        		+ "height:25px;"
                        		+ "color:white;"
                        		+ "overflow:out;"
                        		+ "padding-top:3px;padding-bottom:5px;padding-left:5px;"
                        	
                        		+ "white-space: nowrap;"
                        		+ "font-weight: bold;"
    dragHtmlDiv_.setAttribute("unselectable", "on");
    dragHtmlDiv_.innerHTML = this.title;
    dragHtmlDiv.appendChild(dragHtmlDiv_);  
   
    //3.2、创建关闭按钮
   	var closeDiv = document.createElement("div");
	
    closeDiv.style.cssText = "z-index:1;width:15px;height:15px;right:0px;top:3px;position:absolute;cursor:default;background:transparent;";
    closeDiv.innerHTML =  "<img id='xMsgCloseImg"+this.id+"' src='"+imgpath+"/close.gif' title='关闭'>";	
	                
	containerDiv.appendChild(closeDiv);      
	
	//4、创建内容
	var msgDiv = document.createElement("div");
	msgDiv.id = "xMsgCon"+ this.id ;
    msgDiv.style.cssText = "width:100%;"
                                + "height:" + (this.height-25) + "px;"
								+ "background-color:white;"
                                + "position:absolute;"
                                + "color:black;"
                                + "top:25px;"
                                + "left:0px;"
                                + "overflow:no;";
    msgDiv.innerHTML =  this.message;	                
	containerDiv.appendChild(msgDiv);      
    
    
	document.getElementById("xMsgCloseImg"+this.id).onclick=function(){
		obj.close();
	};
}

//以下为显示信息函数
function showmsg(title,content,width,height)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	
	var clientHeight = getClientHeight();
	var clientWidth =  getClientWidth();
	var scrollTop = getScrollTop();
	var scrollLeft = getScrollLeft();
	
	if(width==null || width==0) width=200;
	if(height==null || height==0) height=160;
	var left = (clientWidth - width) / 2;
	if(left<0) left=0;
	if(scrollLeft && scrollLeft > 0){
		left = left + scrollLeft;
	}
	var top = (clientHeight - height) / 2;
	if(top<0) top=0;
	if(scrollTop && scrollTop > 0){
		top = top + scrollTop;
	}	
	
	var w=new xWin(id ,width,height,left,top,title,content);
	return w;
}
//以下是显示弹出窗口函数
function showwin(title,url,width,height)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	
	var clientHeight = getClientHeight();
	var clientWidth =  getClientWidth();
	var scrollTop = getScrollTop();
	var scrollLeft = getScrollLeft();
	
	if(width==null || width==0) width=200;
	if(height==null || height==0) height=160;
	var left = (clientWidth - width) / 2;
	if(left<0) left=0;
	if(scrollLeft && scrollLeft > 0){
		left = left + scrollLeft;
	}
	var top = (clientHeight - height) / 2;
	if(top<0) top=0;
	if(scrollTop && scrollTop > 0){
		top = top + scrollTop;
	}
	var w=new xWin(id ,width,height,left,top,title,"<iframe id='xMsgFrame" + id 
		+ "' frameborder=0 scrolling=no src='"
		+url+"' width="+(width-10)+" height="+(height-29)+" ></iframe>");
	return w;
}

//获取浏览器可视化高度
function getClientHeight(){
	return window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;   //height
}
//获取浏览器可视化宽度
function getClientWidth(){
	return window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
}
//获取滚动条距离顶部距离
function getScrollTop(){
	return document.body.scrollTop || (document.documentElement && document.documentElement.scrollTop);
}
//获取滚动条距离底部距离
function getScrollLeft(){
	return document.body.scrollLeft || (document.documentElement && document.documentElement.scrollLeft);
}

function setWinUrl(w, fr, url) {
	if(fr==null || w==null) return;
	fr.src = "";
	fr.src = url;
	var clientHeight = getClientHeight();
	var clientWidth =  getClientWidth();
	var scrollTop = getScrollTop();
	var scrollLeft = getScrollLeft();
	//modify by zhaohuibin 2013-02-27 窗口对象已经存在时，重新显示时重置窗口显示位置
	try {
		var width=600;
		var height=320;
		var left = (clientWidth - width) / 2;
		if(left<0) left=0;
		if(scrollLeft && scrollLeft > 0){
			left = left + scrollLeft;
		}
		var top = (clientHeight - height) / 2;
		if(top<0) top=0;
		if(scrollTop && scrollTop > 0){
			top = top + scrollTop;
		}
		w.reposition(left ,top);
		w.show();
	}catch(e) {
		
	}
}
//刷新窗口
function refreshwin(w)
{
	if(w==null) return ;
	fr = document.getElementById("xMsgFrame" + w.id);
	if(fr==null) return;
	try {
		setWinUrl(w, fr, fr.src);
	}catch(e) {
		
	}
}
//更改窗口URL
function navwin(w, url, newtitle)
{
	if(w==null) return ;
	var fr = document.getElementById("xMsgFrame" + w.id);
	if(fr==null) return;
	try {
		setWinUrl(w, fr, url);
		//w.show();
		if(newtitle) {
			var vt = document.getElementById("xMsgTitleBarCon" + w.id);
			if(vt) vt.innerText = newtitle;
		}
	}catch(e) {
		
	}
}
//更改窗口内容
function updatewin(w, content, newtitle, width, height)
{	
	if(w==null) return ;
	fr = document.getElementById("xMsgCon" + w.id);
	var vt = document.getElementById("xMsgTitleBarCon" + w.id);
	if(fr==null || vt==null) return;
	try {
		var clientHeight = getClientHeight();
		var clientWidth =  getClientWidth();
		var scrollTop = getScrollTop();
		var scrollLeft = getScrollLeft();
		fr.innerHTML=content;
		if(newtitle) vt.innerText=newtitle;
		if(width==null || width==0) width=200;
		if(height==null || height==0) height=160;
		var left = (clientWidth - width) / 2;
		if(left<0) left=0;
		if(scrollLeft && scrollLeft > 0){
			left = left + scrollLeft;
		}
		var top = (clientHeight - height) / 2;
		if(top<0) top=0;
		if(scrollTop && scrollTop > 0){
			top = top + scrollTop;
		}
		w.reposition(left ,top);
		w.show();
	}catch(e) {
		
	}
}
//取消按钮
function cancelxwininput(w) {
	if(w) {
		w.close();
	}
}
//确定按钮
function okxwininput(w, func) {
	if(w) {
		var inp = document.getElementById("xWinInputbox" + w.id);
		w.close();
		func(inp.value);
	}
}
//msgbox框
function msgbox(title, msg)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	var width=300;
	var height=150;
	var clientHeight = getClientHeight();
	var clientWidth =  getClientWidth();
		
	var left = (clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (clientHeight - height) / 2;
	if(top<0) top=0;
	
	var content = "<p id='xWinMsgboxPrompt" + id +  " align='left' style='padding:0;'>" + msg 
		+ "</p><p align='center'  style='padding-bottom:0;'><input type='button' id='xWinInputboxOk"+id+"' class='Button_Silver' value='确定'><p>";
	
	var w=new xWin(id ,width,height,left,top,title,content);
	var okinp = document.getElementById("xWinMsgboxOk"+id);
	okinp.attachEvent("onclick", function(){cancelxwininput(w);});
	return w;
}
//录入框
function inputbox(title,promptstr,defaultstr, handler)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	var width=300;
	var height=150;
	var clientHeight = getClientHeight();
	var clientWidth =  getClientWidth();
	var left = (clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (clientHeight - height) / 2;
	if(top<0) top=0;
	
	var content = "<p id='xWinInputboxPrompt" + id +  " align='left' style='padding:0;'>" + promptstr 
		+ "</p><p align='center' style='padding:0;'><input type='text' id='xWinInputbox" + id 
		+ "' value='" + defaultstr + "'></p>" 
		+ "<p align='center'  style='padding-bottom:0;'><input type='button' id='xWinInputboxOk"+id+"' class='Button_Silver' value='确定'>"
		+ "&nbsp;&nbsp;<input type='button' id='xWinInputboxCancel"+id+"' class='Button_Silver' value='取消'><p>";
	
	var w=new xWin(id ,width,height,left,top,title,content);
	var inp = document.getElementById("xWinInputbox" + w.id);
	inp.style.posWidth = width - 30;
	var okinp = document.getElementById("xWinInputboxOk"+id);
	okinp.attachEvent("onclick", function(){okxwininput(w, handler);});
	var cancelinp = document.getElementById("xWinInputboxCancel"+id);
	cancelinp.attachEvent("onclick", function(){cancelxwininput(w);});
	return w;
}

function inputboxmulti(title,promptstr,defaultstr, handler)
{
	var dt= new Date();
	var id = dt.getMilliseconds();
	var width=300;
	var height=150;
	var clientHeight = getClientHeight();
	var clientWidth =  getClientWidth();
	var left = (clientWidth - width) / 2;
	if(left<0) left=0;
	var top = (clientHeight - height) / 2;
	if(top<0) top=0;
	
	var content = "<div id='xWinInputboxPrompt" + id +  " align='left' style='display:block;padding:5;'>" + promptstr 
		+ "</div><div align='center' style='display:block;padding:5;'><textarea type='text' id='xWinInputbox" + id 
		+ "' style='width:"+(width-40)+";height:"+(height-95)+";overflow-x:hidden;overflow-y:auto;line-height:120%;'>" + defaultstr + "</textarea></div>" 
		+ "<div align='center'  style='display:block;padding-bottom:5;'><input type='button' id='xWinInputboxOk"+id+"' class='Button_Silver' value='确定'>"
		+ "&nbsp;&nbsp;<input type='button' id='xWinInputboxCancel"+id+"' class='Button_Silver' value='取消'><div>";
	
	var w=new xWin(id ,width,height,left,top,title,content);
	var inp = document.getElementById("xWinInputbox" + w.id);
	inp.style.posWidth = width - 30;
	var okinp = document.getElementById("xWinInputboxOk"+id);
	okinp.attachEvent("onclick", function(){okxwininput(w, handler);});
	var cancelinp = document.getElementById("xWinInputboxCancel"+id);
	cancelinp.attachEvent("onclick", function(){cancelxwininput(w);});
	return w;
}