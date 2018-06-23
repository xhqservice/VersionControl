package com.jadlsoft.business.xtgl;

import java.util.List;

import com.jadlsoft.model.xtgl.Role;

/**
 * 角色管理的Manager的接口
 * @类名: IRoleManager
 * @描述: 实现对角色的管理
 * @作者: 李春晓
 * @时间: 2017-1-3 上午9:56:03
 */
public interface IRoleManager {

	/**
	 * @功能:获得所有的可用角色列表 
	 * @return: List 角色的列表集合
	 */
	List getRolelist();
	
	/**
	 * @功能: 保存角色信息
	 * @param role 要保存的角色对象
	 * @return: void
	 */
	void save(Role role);
	
	/**
	 * @功能: 更新角色信息
	 * @param role 要更新的角色对象
	 * @return: void
	 */
	void update(Role role);
	
	/**
	 * @功能: 根据角色id获取角色对象
	 * @param roleid 角色id
	 * @return: Role
	 */
	Role get(String roleid);
	
	/**
	 * @功能: 根据角色主键roleid删除角色
	 * @param roleid 角色id
	 * @param compelType 
	 * @return: void
	 */
	void delete(String roleid);
	
	/**
	 * @功能: 获取角色当前最大id
	 * @return: String
	 */
	String getMaxId();
	
	/**
	 * 通过角色id获取角色，包含角色所拥有的全部权限
	 * @param rid 角色id 
	 * @return: Map 将返回的列和值作为key/value的形式保存在map中
	 */
	Role getRoleContainsPermissesByRid(String rid);

	/**
	 * 通过用户的id获取角色对象
	 * @return: Role
	 */
	Role getRoleByUserId(String userid);
}
