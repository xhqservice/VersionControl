/**
 * <p>Title:UserAction.java </p>
 * <p>Description: 用户管理Action</p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: 京安丹灵</p>
 * @date 2012-05-12
 * @author ZongShuai
 * @version 1.0
*/

package com.jadlsoft.struts.action.xtgl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.jadlsoft.business.xtgl.IRoleManager;
import com.jadlsoft.business.xtgl.IUserManager;
import com.jadlsoft.model.xtgl.Role;
import com.jadlsoft.model.xtgl.UserBean;
import com.jadlsoft.struts.action.BaseAction;

public class UserAction extends BaseAction{
	private UserBean userBean;
	private IRoleManager roleManager;
	private IUserManager userManager;
	
	
	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}
	
	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}
	
	/**
	 * 
	 * @功能  跳转到用户编辑页面
	 * @return
	 * @作者 吴家旭 Feb 28, 2013 4:01:27 PM
	 */
	public String edit() {
		
		String uid = request.getParameter("uid");
		if (uid != null && !"".equals(uid)) {
			// 编辑操作
//			UserBean user = userManager.getUserByUid(uid);
			Map user = userManager.getUserContainRoleByUid(uid);
			request.setAttribute("userBean", user);
		}
		
		List<Role> rolelist= roleManager.getRolelist();
		request.setAttribute("rolelist", rolelist);
		return "edit";
	}
	
	/**
	 * 
	 * @功能 保存用户
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:36 PM
	 */
	public String save() {
		userManager.saveUser(userBean);
		return "list";
	}
	
	/**
	 * 
	 * @功能 更新用户
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:44 PM
	 */
	public String update() {
		userManager.updateUser(userBean);
		return "list";
	}
	
	/**
	 * 
	 * @功能 删除用户
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @作者 吴家旭 Mar 1, 2013 2:37:55 PM
	 */
	public String delete() throws UnsupportedEncodingException {
		String uid = request.getParameter("uid");
		userManager.deleteUser(uid);
		//String userid = java.net.URLDecoder.decode(request.getParameter("userid") , "UTF-8");
//		userManager.delete(userid);

		return "list";
	}
	
	/**
	 * 
	 * @功能 跳转到用户编辑页面
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @作者 吴家旭 Mar 1, 2013 2:43:40 PM
	 */
	public String get() throws UnsupportedEncodingException {
		List<Role> rolelist= roleManager.getRolelist();
		request.setAttribute("rolelist", rolelist);
		String userid = java.net.URLDecoder.decode(request.getParameter("userid") , "UTF-8");
		return "get";
	}
	
	
	/**
	 * 
	 * @功能 查看用户具体信息
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @作者 吴家旭 Mar 1, 2013 2:43:40 PM
	 */
	public String view() {
		String uid = request.getParameter("uid");
		Map user = userManager.getUserContainRoleByUid(uid);
		// 封装进用户角色
		request.setAttribute("userBean", user);
		return "view";
	}
	
	/**
	 * 
	 * @功能 进入用户密码修改画面
	 * @return
	 * @作者 吴家旭 Apr 5, 2013 3:38:40 PM
	 */
	public String psw() {
		
//		userBean = userManager.get(super.getUserSessionBean().getUserid());
		
		return "pswedit";
	}
	/**
	 * 
	 * @功能 更新密码
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:44 PM
	 */
	public String updatepsw() {
		
//		userManager.update(userBean, "password");
		
		return "list";
	}
	
	
}