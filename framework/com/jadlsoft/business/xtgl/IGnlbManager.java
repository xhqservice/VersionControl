package com.jadlsoft.business.xtgl;

import java.util.List;

import com.jadlsoft.model.xtgl.Gnlb;

/**
 * 功能列表的接口
 * @类名: IGnlbManager
 * @描述: 完成功能列表的相关操作
 * @作者: 李春晓
 * @时间: 2017-1-5 上午10:54:56
 */
public interface IGnlbManager {

	/**
	 * 以格式化的形式将所有的权限封装返回
	 * 	为了页面方便取值，在这里将权限的对象 进行封装
	 * 	最终封装的结果为:一个list集合，里面放的是map,包含每一个一级菜单的所有内容
	 * 		map里面有两个字段，分别为
	 * 			self，里面存的是父菜单本身的gnlb对象
	 * 			children,里面存的是父菜单对应的所有子菜单集合list，list里面就是每一个的子菜单对象
	 * @return: Map
	 */
	List getGnlbList();
	
	/**
	 * 根据角色id获取对应的所有的功能列表的code集合
	 * @return: List 所有的功能列表的code的集合
	 */
	List getGnlbListByRoleId(String roleid);

	/**
	 * 根据功能代码获取功能菜单对象
 	 * @param gid 功能代码
	 * @return: Gnlb
	 */
	Gnlb getGnlbByGncode(String gid);

	/**
	 * 保存功能菜单
	 * @return: void
	 */
	int saveGnlb(Gnlb gnlb);

	/**
	 * 修改功能菜单
	 * @param gnlb
	 * @return: int
	 */
	int updateGnlb(Gnlb gnlb);

	/**
	 * 删除功能菜单
	 * @param gncode 要删除的功能菜单的代码
	 */
	void deleteGnlb(String gncode);

	/**
	 * 通过功能代码获取功能对象，包括zt为1的
	 * @return: Gnlb
	 */
	Gnlb getGnlbAllByGncode(String gncode);
}
 