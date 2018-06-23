package com.jadlsoft.struts.action.xtgl;

import java.util.List;

import com.jadlsoft.business.xtgl.IGnlbManager;
import com.jadlsoft.business.xtgl.IPermissManager;
import com.jadlsoft.model.xtgl.Permiss;
import com.jadlsoft.struts.action.BaseAction;

public class PermissAction extends BaseAction{
	private Permiss permiss;
	private IPermissManager permissManager;
	private IGnlbManager gnlbManager;
	
	
	public Permiss getPermiss() {
		return permiss;
	}
	public void setPermiss(Permiss permiss) {
		this.permiss = permiss;
	}

	public void setPermissManager(IPermissManager permissManager) {
		this.permissManager = permissManager;
	}
	public void setGnlbManager(IGnlbManager gnlbManager) {
		this.gnlbManager = gnlbManager;
	}
	/**
	 * 
	 * @功能 TODO 跳转到权限编辑页面
	 * @return
	 * @作者 吴家旭 Feb 28, 2013 4:01:27 PM
	 */
	public String edit() {
		
		String pid = request.getParameter("pid");
		if (pid != null && !"".equals(pid)) {
			//为编辑界面,通过pid得到包含gncode的权限对象
			Permiss permiss = permissManager.getPermissContainsGnlbByPid(pid);
			request.setAttribute("permissBean", permiss);
		}
		List list = gnlbManager.getGnlbList();
		request.setAttribute("gnlbList", list);
		return "edit";
	}

	public String view(){
		String pid = request.getParameter("pid");
		Permiss permissBean = permissManager.getPermissContainsGnlbByPid(pid);
		request.setAttribute("permiss", permissBean);
		return "view";
	}
	
	/**
	 * 
	 * @功能 保存权限
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:36 PM
	 */
	public String save() {
		permissManager.save(permiss);
		return "list";
	}
	
	/**
	 * 
	 * @功能 跳转到权限编辑页面
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:43:40 PM
	 */
	public String get() {
		String permissid = request.getParameter("permissid");
		//permiss = permissManager.get(permissid);
		//List list = permissManager.getGnList();
		
		//request.setAttribute("operationlist", list);
		return "get";
	}
	
	/**
	 * 
	 * @功能 更新权限
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:44 PM
	 */
	public String update() {
		permissManager.update(permiss);
		return "list";

	}
	
	/**
	 * 
	 * @功能 删除权限
	 * @return
	 * @作者 吴家旭 Mar 1, 2013 2:37:55 PM
	 */
	public String delete() {
		String pid = request.getParameter("pid");
		permissManager.delete(pid);
		
		return "list";
	}
	
	/**
	 * 
	 * @功能 ajax检查权限下是否有功能
	 * @return
	 * @throws Exception 
	 * @作者 吴家旭 Mar 2, 2013 2:42:20 PM
	 */
	public String checkPermissoperation() throws Exception {

		/*String permissid = request.getParameter("permissid");
		List list = permissManager.getPermissOperaByPermissid(permissid);
		String is_success = "";
		if(list == null || list.size() ==0){
			is_success = "NO";
		}else {
			is_success = "YES";
		}
		ResponseUtils.render(response, is_success);*/
		return null;
		
	}
}
