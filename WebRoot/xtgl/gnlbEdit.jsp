<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="jadlhtml.tld" prefix="jadlhtml"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>功能菜单编辑</title>
		<%@ include file="../include/include.jsp"%>
		<script type="text/javascript">
		
		  	//校验域填写是否完整
		  	function save(){
		  		$("#insert").attr("disabled", true);
				var err = checkForm(document.forms[0]);
				if(!err){
					$("#insert").attr("disabled", false);
					return false;
				}
				var gnid = $("#gnid").val();
				var gncode = $("#gncode").val();
				if(gnid == null || gnid == ""){
					//新建
					//校验功能代码是否已经存在
					$.ajax({
						method:"post",
						url:"gnlbisGncodeAvailable.action",
						data:"gncode="+gncode,
						async:false,
						success:function(data){
							if(data=="notAvailable"){
								alert("功能代码已存在，请先看功能菜单列表后再添加");
								return false;
							}
							document.getElementById("gnlbFormId").action="gnlbsave.action";
							document.getElementById("gnlbFormId").submit();
						}
					});
				}else{
					//修改
					document.getElementById("gnlbFormId").action="gnlbupdate.action";
					document.getElementById("gnlbFormId").submit();
				}
			}
  		</script>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">菜单编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="gnlbFormId"
              		method="post" id="gnlbFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input type="hidden" id="gnid" value="${gnlbBean.gncode}" />
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>功能代码：</label>
			            <div class="labelRight">
			            	<c:if test="${empty gnlbBean.gncode}">
								<input type="text" value="${gnlbBean.gncode}" name="gnlb.gncode" id="gncode" 
									class="inputText"
									size="30" title="功能代码"
									alt="notnull;length<=6;" maxlength="6"/>
							</c:if>
							<c:if test="${not empty gnlbBean.gncode}">
								<input type="text" value="${gnlbBean.gncode}" name="gnlb.gncode" id="gncode" 
									class="inputText readonly"
									size="30" title="功能代码"
									alt="notnull;length<=6;" maxlength="6"
									readonly="readonly"/>
							</c:if>
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>功能名称：</label>
			            <div class="labelRight">
							<input type="text" name="gnlb.gnname" id="gnname"
								value="${gnlbBean.gnname}" class="inputText"
								size="30" title="功能名称"
								alt="notnull;length<=30;" maxlength="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>链接：</label>
			            <div class="labelRight">
							<input type="text" name="gnlb.link" id="link"
								value="${gnlbBean.link}" class="inputText"
								size="30" title="链接" alt="notnull;length<=100"
								maxlength="100"/>
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">顺序号：</label>
			            <div class="labelRight">
							<input type="text" name="gnlb.sort" id="sort"
								value="${gnlbBean.sort}" class="inputText"
								size="30" title="顺序号" 
								maxlength="6"/>
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        <!-- 操作按钮 -->
			        <li class="clearfix">
			            <label class="labelLeft">&nbsp;</label>
			            <div class="labelRight">
			                <input type="button" class="defaultBtn" onclick="save()" value="保存" />
			                <input type="button" class="defaultBtn backBtn" onclick="history.back(-1)" value="返回" />
			            </div>
			        </li>
			    </ul>
			</form>
		</div>    
	</section>
</body>
</html>

