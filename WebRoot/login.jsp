<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>丹灵云-登录</title>
<link rel="stylesheet" type="text/css" href="css/reset.min.css" />
<link rel="stylesheet" type="text/css" href="css/theme_common.css" />
<link rel="stylesheet" type="text/css" href="css/ucenter.css" />
<link rel="stylesheet" type="text/css" href="css/index/login.css" />

<script src="js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
<script src="common/commonUtil.js" type="text/javascript" charset="utf-8"></script>
<script src="common/menucookie.js" type="text/javascript" charset="utf-8"></script>
<script src="common/validate.js" type="text/javascript" charset="utf-8"></script>
<script src="js/ucenterPublic.js" type="text/javascript" charset="utf-8"></script>
<script src="common/layer/layer.js" type="text/javascript" charset="utf-8"></script>

<!--[if lt IE 9]>
<script src="js/html5shiv.min.js" type="text/javascript" charset="utf-8"></script>
<script src="js/respond.min.js" type="text/javascript" charset="utf-8"></script>
<![endif]-->
<style type="text/css" ></style><script>//console.log('a')
</script><style type="text/css">object,embed{                -webkit-animation-duration:.001s;-webkit-animation-name:playerInserted;                -ms-animation-duration:.001s;-ms-animation-name:playerInserted;                -o-animation-duration:.001s;-o-animation-name:playerInserted;                animation-duration:.001s;animation-name:playerInserted;}                @-webkit-keyframes playerInserted{from{opacity:0.99;}to{opacity:1;}}                @-ms-keyframes playerInserted{from{opacity:0.99;}to{opacity:1;}}                @-o-keyframes playerInserted{from{opacity:0.99;}to{opacity:1;}}                @keyframes playerInserted{from{opacity:0.99;}to{opacity:1;}}</style></head>
</head>
<script type="text/javascript">

	//网页内按下回车触发
	document.onkeydown = hotkey;
	function hotkey(){
		var esc = window.event.keyCode;
		if(esc==13){
			document.getElementById("loginBtn").click();                
            return false;
		}
	}

	function login() {
		$("#loginBtn").attr("disabled", true);
		$("#usernameErr").hide();		
		$("#passwordErr").hide();
		$("#errDiv").html("");
	
		var username = $("#username").val();
		var password = $("#password").val();
		
		if (isBlank(username)) {
			$("#usernameErr").html("用户名不能为空！");
			$("#usernameErr").show();
			$("#username").focus();
			$("#loginBtn").attr("disabled", false);
			return false;
		}
		
		if (isBlank(password)) {
			$("#passwordErr").html("密码不能为空！");
			$("#passwordErr").show();
			$("#password").focus();
			$("#loginBtn").attr("disabled", false);
			return false;
		}
		delCookie();	//清除cookie中的菜单id
		document.LoginForm.submit();
	}
	
	function init(){
		document.getElementById("username").value = "admin";
		document.getElementById("password").value = "admin";
		
		
	}
</script>
<body style="background: #FFFFFE;min-width: 200px;" onload="init()">
	<div class="loginWrapper clearfix">
		<div class="loginMain">
			<div class="loginWidth">
				<div class="loginLogoDiv">
					<a class="loginLogo" href="http://www.danlingyun.com"></a>
					<div style="color: #656A70;margin: 0 auto;
							display: inline-block;
							margin-left: -123px;
							font-size: 16px;font-weight: bolder;
							font-style: italic;">
						-------------版本控制中心
					</div>
				</div>
				<form action="dologin.action" class="formLogin" method="post"
					id="LoginForm" name="LoginForm" >
					<div class="loginList loginListUser">
						<label></label> 
						<input type="text" class="loginText" title="用户名"
							name="userBean.userid" id="username" alt="notnull" value="" placeholder="用户名" />
						<div id="usernameErr" class="errorTips"></div>
					</div>
					<div class="loginList loginListPwd">
						<label></label> 
						<input type="password" class="loginText" title="密码"
							name="userBean.password" id="password" alt="notnull" value="" placeholder="密码" />
						<div id="passwordErr" class="errorTips"></div>
					</div>
					<c:if test="${not empty errMsg}">
						<div id="errDiv"><font color="red">${errMsg}</font></div>
		  			</c:if>
					<input class="loginBtn" type="button" id="loginBtn" onclick="login()" value="登录" />
				</form>
			</div>
		</div>
	</div>
</body>
</html>
