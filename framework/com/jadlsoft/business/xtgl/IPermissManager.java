package com.jadlsoft.business.xtgl;

import java.util.List;

import com.jadlsoft.model.xtgl.Permiss;

/**
 * 权限管理的接口
 * @类名: IPermissManager
 * @描述: 完成权限相关操作的功能
 * @作者: 李春晓
 * @时间: 2017-1-4 上午10:09:08
 */
public interface IPermissManager {

	/**
	 * 获取所有的权限列表
	 * @功能: 列出所有的权限
	 * @return: List
	 */
	List getPermisses();
	
	/**
	 * 通过角色id获得该角色下的所有的权限
	 * @param roleid
	 * @return: List
	 */
	List getPermissesByRoleId(String roleid);
	
	/**
	 * 通过permissid获得包含功能列表的权限对象
	 * @param permissid 
	 * @return: Permiss
	 */
	Permiss getPermissContainsGnlbByPid(String permissid);

	/**
	 * 保存权限操作
	 * @功能: 保存权限
	 * @param permiss 要保存的权限
	 * @return: void
	 */
	void save(Permiss permiss);

	/**
	 * 更新权限操作
	 * @功能: 更新权限
	 * @param permiss 要更新的对象
	 * @return: void
	 */
	void update(Permiss permiss);

	/**
	 * 删除权限操作
	 * @功能: 删除权限
	 * @param pid 要删除的权限的id
	 * @return: void
	 */
	void delete(String pid);
}
