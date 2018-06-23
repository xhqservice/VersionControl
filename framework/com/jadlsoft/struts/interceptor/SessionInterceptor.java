/**
 * @Description Session拦截器,对非正常登录进入系统的用户实时拦截并跳转到登录页面
 * @Company 京安丹灵
 * @author zongshuai
 * @date 2012-03-06
 * @version 1.0
 */
package com.jadlsoft.struts.interceptor;

import com.jadlsoft.struts.action.UserUtils;
import com.jadlsoft.struts.action.xtgl.LoginAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor extends AbstractInterceptor {
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		Action action = (Action) actionInvocation.getAction();
		if (action instanceof LoginAction) {
			return actionInvocation.invoke();
		}

		ActionContext ctx = ActionContext.getContext();
		Object userBean = ctx.getSession().get(UserUtils.USER_SESSION);
		if (userBean == null) {
			 
			return Action.LOGIN;
		} else {
			return actionInvocation.invoke();
		}
	}
}