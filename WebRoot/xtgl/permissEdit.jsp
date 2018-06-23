<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglib/c.tld"%>
<!DOCTYPE html>
<html>
	<head>
		<title>权限编辑</title>
		<%@ include file="../include/include.jsp"%>
		<style type="text/css">
		</style>
		<script type="text/javascript">
		
			var beforeData;
		
			$(function(){
				// 设置总的全选的复选框的点击事件
				$("#checkboxAll").click(function(){
					$(this).nextAll("ul").find("input:checkbox").attr("checked",$(this).is(':checked'));
				});
				
				setParentsInitCheck(); 	//进入页面时候设置一级菜单的选中状态
				setAllInitChecked();	//进入页面时候设置总的CheckBox的选中状态
				
				//设置每个父复选框的点击事件
				$(".cbparent").change(function(){
					$(this).nextAll("ul").find("input:checkbox").attr("checked",$(this).is(':checked'));
					//设置总的复选框的被动事件
					var flag = true;
					var cbparents = $(".cbparent");
					cbparents.each(function(){
						if (!this.checked) {
							flag = false;
						}
						//flag &= this.checked;
					});
					$("#checkboxAll").attr("checked",flag);
				});
				
			
				//设置每个子复选框的点击事件
				$(".cbitem").click(function(){
					var flag = true;
					//得到除了自己的其他的子复选框
					var cbitems = $(this).parent().siblings().find("input:checkbox");
					//将自己添加进去
					cbitems.push(this);
					//循环遍历每个子复选框的状态
					cbitems.each(function(){
						if (!this.checked) {
							flag = false;
						}
						//flag &= this.checked;
					});
					//设置到其对应的父复选框的选中状态
					$(this).parent().parent().prevAll("input:checkbox").attr("checked",flag);
					
					//更改总的的点击事件
					var f = true;
					$(".cbparent").each(function(){
						if (!this.checked) {
							f = false;
						}
						//f &= this.checked;
					});
					$("#checkboxAll").attr("checked",f);
				});
				
				//一级菜单点击事件
				$(".spparent").click(function(){
					$(this).next().toggle();
				});
				//初始化时候获取表单的数据
				beforeData = $("#permissFormId").serialize();
			});
			
			//总的CheckBox的选中状态
			function setAllInitChecked(){
				var f = true;
				$(".cbparent").each(function(){
					if (!this.checked) {
						f = false;
					}
				});
				$("#checkboxAll").attr("checked",f);
			}
			
			//每个父菜单的选中状态设置
			function setParentsInitCheck(){
				$(".cbparent").each(function(){
					var f = true;
					var s = $(this).nextAll("ul");
					s = s.find("input:checkbox");
					s.each(function(){
						if (!this.checked) {
							f = false;
						}
					});
					$(this).attr("checked",f);
				});
			}
			
		  	//保存操作
		  	function save(){
		  	
		  		//设置按钮不可用属性
		  		$("#insert").attr("disabled", true);
		  		//表单校验
				var err = checkForm(document.forms[0]);
				if(!err){
					$("#insert").attr("disabled", false);
					return false;
				}
				
		  		//校验是否没有选择一个
		  		if (checkIsCheckedNone()) {
					alert("请至少选择一项");
					return false;
				}
				
				//检验数据是否更改
				var submitData = $("#permissFormId").serialize();
				if (submitData == beforeData) {
					//没有更改
					alert("没有更改任何内容");
					return false;
				}else {
					if (confirm("确认要更新这些改变?")) {
					
						//设置父级菜单的选中状态,如果有子菜单选中，设置父菜单也为选中状态
						$(".hiparent").each(function(){
							var flag = false;
							var cbitems = $(this).nextAll("ul").find(".cbitem");
							cbitems.each(function(){
								if (this.checked) {
									flag = true;	//只要有选中的就为true
								}
							});
							if (flag) {
								$(this).removeAttr("disabled");
							}
						});
						
						var pid = $("#pid").val();
						if(pid == null || pid == ""){
							//新建
							document.getElementById("permissFormId").action="permisssave.action";
						}else{
							//修改
							document.getElementById("permissFormId").action="permissupdate.action";
						}
						document.getElementById("permissFormId").submit();
					}
				}
			}
			
			//返回按钮
			function goback(){
				var submitData = $("#permissFormId").serialize();
				if (submitData != beforeData) {
					if(confirm("不保存这些更改吗?")){
						history.back();
					}
				}else {
					history.back();
				}
			}
			
			//判断是否未选择任何内容
			function checkIsCheckedNone(){
				var f = false;
				$(".cbitem").each(function(){
					if (this.checked) {
						f = true;	//有一个选中就为true
					}
				});
				return !f;
			}
			
  		</script>
	</head>
<body>
	<section id="contentMain">
		<h2 class="infoTitle">权限编辑</h2>
        <div class="contentDiv mainContent">
        	<form action="" name="permissFormId"
              		method="post" id="permissFormId" 
              		class="verifyPersonForm vertifyPersonForm vertifyForm">
              	<!-- 隐藏域信息 -->
              	<input type="hidden" value="${permissBean.permissid}" name="permiss.permissid" id="pid" />
              	
				<ul class="formUl" style="padding-top: 0;">
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft"><span class="required">*</span>权限名：</label>
			            <div class="labelRight">
			            	<input type="text" name="permiss.permissname" id="permissname"
								value="${permissBean.permissname}" class="inputText"
								size="30" title="权限名"
								alt="notnull;length<=50;" maxlength="50" size="30" />
			                <div class="errorMsg"></div>
			            </div>
			        </li>
			        
			        <li class="clearfix" style="height: auto;">
			            <label class="labelLeft">菜单功能：</label>
			            <div class="labelRight">
			            	<c:if test="${not empty gnlbList}">
							<input id="checkboxAll" type="checkbox" value="" onclick="checkboxAll()"/>全选
							<ul class="option1">
								<c:forEach var="parentMap" items="${gnlbList}">
									<!-- 每一个的父菜单，里面包含其对应的子菜单集合 -->
									<li  style="height: 0;height: auto;">
				  						<input class="cbparent" type="checkbox" value=""/>
				  						<!-- 设置隐藏域，用来保存父级菜单的值,如果有自己菜单选中了，父级菜单的值也要上传 -->
				  						<input class="hiparent" type="hidden" name="permiss.gnlbs" value="${parentMap.self.gncode}" disabled="disabled" />
				  						<span class="spparent" style="clear: both;">${parentMap.self.gnname}</span>
				  						<ul class="option2">
						  					<c:forEach var="gnlbItem" items="${parentMap.children}">
						  						<li style="height: 0;line-height: 0;">
						  							<c:set var="flag" value="false"></c:set>
													<c:forEach items="${permissBean.gnlbs}" var="gnlb">
														<c:if test="${gnlb.gncode == gnlbItem.gncode}">
													    	<c:set var="flag" value="true"></c:set>   
													   </c:if>
													</c:forEach>
													<c:if test="${flag==true}">
														<input type="checkbox" checked="checked" name="permiss.gnlbs" class="cbitem" value="${gnlbItem.gncode}"/>
													</c:if>
													<c:if test="${flag==false}">
														<input type="checkbox" name="permiss.gnlbs" class="cbitem" value="${gnlbItem.gncode}"/>
													</c:if>	
						  							<span class="spitem" style="clear: both;">${gnlbItem.gnname}</span>
						  							<c:if test="${flag==true}">
														<font size="2" color="red">@</font>
													</c:if>
						  						</li>
						  					</c:forEach>
					  					</ul>
					  				</li>
								</c:forEach>
							</ul>
						</c:if>
						<c:if test="${empty gnlbList}">
							没有菜单可选
						</c:if>
			            </div>
			        </li>
			        <!-- 操作按钮 -->
			        <li class="clearfix" style="clear: both;">
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

