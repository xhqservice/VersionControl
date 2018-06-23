package com.jadlsoft.business.xtgl;

import java.util.Map;

import com.jadlsoft.model.xtgl.UserBean;

/**
 * 系统管理的用户的manager接口
 * @类名: IUserManager
 * @描述: 完成用户的增删改查等相关的业务操作
 * @作者: 李春晓
 * @时间: 2016-12-30 上午11:12:30
 */
public interface IUserManager {

	/**
	 * @功能: 保存用户，并且保存用户的角色信息
	 * @return: void
	 */
	void saveUser(UserBean userBean);
	
	/**
	 * 通过user的id获取userBean对象
	 * @param uid user的id
	 * @return: UserBean
	 */
	UserBean getUserByUid(String uid);
	
	/**
	 * 通过user的id获取use对象，包含用户的角色信息
	 * @param uid 用户id
	 * @return: Map 表中的所有列和对应值得map集合形式
	 */
	Map getUserContainRoleByUid(String uid);
	
	/**
	 * 根据userid和密码获取用户信息
	 * @param userid
	 * @param password
	 * @return: UserBean
	 */
	UserBean get(String userid, String password);

	/**
	 * @功能: 更新用户
	 * @param customUserBean
	 * @return: void
	 */
	void updateUser(UserBean userBean);

	/**
	 * @功能: 删除用户
	 * @param uid 用户的id
	 * @return: void
	 */
	void deleteUser(String uid);
}
