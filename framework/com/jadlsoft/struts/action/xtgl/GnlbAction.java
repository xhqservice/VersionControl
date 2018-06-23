package com.jadlsoft.struts.action.xtgl;

import com.jadlsoft.business.xtgl.IGnlbManager;
import com.jadlsoft.model.xtgl.Gnlb;
import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.ResponseUtils;

public class GnlbAction extends BaseAction {
	private Gnlb gnlb;
	private IGnlbManager gnlbManager;
	public Gnlb getGnlb() {
		return gnlb;
	}
	public void setGnlb(Gnlb gnlb) {
		this.gnlb = gnlb;
	}
	public void setGnlbManager(IGnlbManager gnlbManager) {
		this.gnlbManager = gnlbManager;
	}
	
	/**
	 * 进入编辑界面
	 * @return: String
	 */
	public String edit(){
		String gid = request.getParameter("gid");
		if (gid != null && !"".equals(gid)) {
			//gid存在，说明是编辑界面
			Gnlb editGnlb = gnlbManager.getGnlbByGncode(gid);
			request.setAttribute("gnlbBean", editGnlb);
		}
		return "edit";
	}
	
	/**
	 * ajax请求，判断输入的功能代码是否可用
	 * @return: void
	 */
	public void isGncodeAvailable(){
		String gncode = request.getParameter("gncode");
		Gnlb thisGnlb = gnlbManager.getGnlbByGncode(gncode);
		try {
			if (thisGnlb != null) {
				ResponseUtils.render(response, "notAvailable");
			}else {
				ResponseUtils.render(response, "available");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 查看功能菜单
	 * @return: String
	 */
	public String view(){
		String gncode = request.getParameter("gid");
		Gnlb viewGnlb = gnlbManager.getGnlbByGncode(gncode);
		request.setAttribute("gnlbBean",viewGnlb);
		return "view";
	}
	
	/**
	 * 保存功能菜单
	 * @return: String
	 */
	public String save(){
		gnlbManager.saveGnlb(gnlb);
		return "list";
	}
	
	/**
	 * 更新功能菜单
	 * @return: String
	 */
	public String update(){
		gnlbManager.updateGnlb(gnlb);
		return "list";
	}
	
	/**
	 * 删除功能菜单
	 * @return: String
	 */
	public String delete(){
		String gncode = request.getParameter("gid");
		gnlbManager.deleteGnlb(gncode);
		return "list";
	}
}
