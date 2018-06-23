package com.jadlsoft.struts.action.xtgl;

import java.util.List;

import com.jadlsoft.business.xtgl.IPermissManager;
import com.jadlsoft.business.xtgl.IRoleManager;
import com.jadlsoft.model.xtgl.Role;
import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.ResponseUtils;

public class RoleAction extends BaseAction{
	private Role role;
	private IRoleManager roleManager;
	private IPermissManager permissManager;
	
	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}
	public void setPermissManager(IPermissManager permissManager) {
		this.permissManager = permissManager;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	/**
	 * 查看角色
	 * @功能: 查看角色信息
	 * @return: String
	 */
	public String view(){
		String rid = request.getParameter("rid");
		Role roleBean = roleManager.getRoleContainsPermissesByRid(rid);
		request.setAttribute("role", roleBean);
		return "view";
	}
	
	/**
	 * 
	 * @功能 TODO 跳转到角色编辑页面
	 * @return
	 * @作者 吴家旭 Feb 28, 2013 4:01:27 PM
	 */
	public String edit() {
		
		// 通过是否传递的有rid来进行判断是新建还是编辑
		String rid = request.getParameter("rid");
		if (rid != null &&  !"".equals(rid)) {
			// 编辑,将对应的角色的信息保存在作用域中
			Role roleBean = roleManager.getRoleContainsPermissesByRid(rid);
			request.setAttribute("roleBean", roleBean);
		}
		// 得到所有的权限集合
		List permisslist = permissManager.getPermisses();
		request.setAttribute("permisslist", permisslist);
		return "edit";
	}

	/**
	 * 
	 * @功能 保存角色
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:36 PM
	 */
	public String save() {
		roleManager.save(role);
		return "list";
	}
	
	/**
	 * 
	 * @功能 更新角色
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:44 PM
	 */
	public String update() {
		try {
			roleManager.update(role);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return "list";

	}
	
	/**
	 * 
	 * @功能 删除角色
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:55 PM
	 */
	public String delete() {
		String roleid = request.getParameter("rid");
		roleManager.delete(roleid);
		return "list";
	}
	
	/**
	 * 
	 * @功能 ajax检查角色下是否有权限
	 * @return
	 * @throws Exception 
	 * @作者 吴家旭 Mar 2, 2013 2:42:20 PM
	 */
	public String checkRolePermiss() throws Exception {

		String roleid = request.getParameter("roleid");
		List list = permissManager.getPermissesByRoleId(roleid);
		String is_success = "";
		if(list == null || list.size() ==0){
			is_success = "NO";
		}else {
			is_success = "YES";
		}
		ResponseUtils.render(response, is_success);
		return null;
		
	}
}
