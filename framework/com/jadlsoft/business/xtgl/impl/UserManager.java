package com.jadlsoft.business.xtgl.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.business.xtgl.IUserManager;
import com.jadlsoft.model.xtgl.UserBean;
import com.jadlsoft.utils.DateUtils;
import com.jadlsoft.utils.SystemConstants;

/**
 * 用户manager的实现类
 * @类名: UserManager
 * @描述: 完成用户相关的业务操作
 * @作者: 李春晓
 * @时间: 2016-12-30 上午11:19:30
 */
public class UserManager extends BaseManager implements IUserManager {

	private static Logger log = Logger.getLogger(UserManager.class);
	
	/*
	 * 保存用户
	 */
	@Override
	public void saveUser(UserBean userBean) {
		// 通过序列获得id
		String uid = String.valueOf(this.daoUtils.getNextval("Q_XT_USER"));
		// 设置默认信息
		userBean.setId(uid);	// 设置id
		userBean.setPassword("123456");	//设置默认密码
		userBean.setLrsj(DateUtils.createCurrentDate());  //设置录入时间
		userBean.setZhxgsj(DateUtils.createCurrentDate());	//设置最后修改时间默认就是当前录入时间
		userBean.setZt(SystemConstants.ZT_TRUE);
		try {
			// 保存基本的用户信息
			int save = daoUtils.save(userBean,"id,userid,password,username,qydm,xzqh,lrsj,zhxgsj,lxr,lxrlxdh,zt");
			if (save > 0) {
				// 保存用户的角色信息
				if (userBean.getRole() != null && userBean.getId() != null) {
					updateUserRole(userBean, false);
				}
			}
		} catch (Exception e) {
			log.info("保存用户出错！",e);
			throw new RuntimeException("保存用户出错！");
		}
		
	}
	
	/*
	 * 根据用户名和密码查询user对象
	 */
	@Override
	public UserBean get(String userid, String password){
		if (userid == null || password == null) {
			return null;
		}
		Map condition = new HashMap();
		condition.put("userid", userid);
		condition.put("password", password);
		condition.put("zt", SystemConstants.ZT_TRUE);
		try {
			//Object obj = daoUtils.findObject("#xtgl.getUserBean", condition,UserBean.class);
			Object obj = daoUtils.findObjectCompatibleNull(UserBean.class, condition);
			return obj==null ? null : (UserBean) obj;
		} catch (Exception e) {
			log.info("根据userid和密码获取用户信息出错！",e);
			throw new RuntimeException("根据userid和密码获取用户信息出错！");
		}
	}

	/*
	 * 通过用户id获取用户对象，不包含角色信息
	 */
	@Override
	public UserBean getUserByUid(String uid) {
		Map condition = new HashMap();
		condition.put("id", uid);
		try {
			return (UserBean) daoUtils.findObject(UserBean.class, condition);
		} catch (Exception e) {
			log.info("通过用户id获取用户对象出错！",e);
			throw new RuntimeException("通过用户id获取用户对象出错！");
		}
	}
	
	/*
	 * 通过用户id获取用户对象，包含角色信息
	 */
	@Override
	public Map getUserContainRoleByUid(String uid) {
		try {
			Map condition = new HashMap();
			condition.put("uid", uid);
			List list = daoUtils.find("#xtgl.getUserContainRoleByUid", condition);
			if (list != null && list.size()>0) {
				return (Map) list.get(0);
			}
			return null;
		} catch (Exception e) {
			log.info("通过用户id获取包含角色对象的用户对象出错！",e);
			throw new RuntimeException("通过用户id获取包含角色对象的用户对象出错！");
		}
	}

	/*
	 * 更新用户
	 */
	@Override
	public void updateUser(UserBean userBean) {
		//设置最后修改时间
		userBean.setZhxgsj(DateUtils.createCurrentDate());
		try {
			// 更新用户的基本信息
			int update = daoUtils.update(userBean, "userid,username,qydm,xzqh,zhxgsj,lxr,lxrlxdh");
			if (update > 0) {
				updateUserRole(userBean, true);
			}
		} catch (Exception e) {
			log.info("更新用户出错！",e);
			throw new RuntimeException("更新用户出错！");
		}
	}
	
	/*
	 * 删除用户
	 */
	@Override
	public void deleteUser(String uid) {
		// 更新用户表中的状态
		UserBean userBean = getUserByUid(uid);
		userBean.setZt(SystemConstants.ZT_FALSE);
		userBean.setZxsj(DateUtils.createCurrentDate());
		int update = daoUtils.update(userBean,"zt,zxsj");
		if (update > 0) {
			// 删除用户角色表中对应的数据
			Map condition = new HashMap();
			condition.put("userid", uid);
			daoUtils.execSql("#delete_t_xt_userrole", condition);
		}
	}
	
	/**
	 * 执行对用户角色表的更新操作
	 * @功能: 通过判断传递进来的标识符isUpdate，决定是更新数据还是新增数据
	 * @param userBean 要操作的对象
	 * @param isUpdate 是否为更新
	 * @return: void
	 */
	private void updateUserRole(UserBean userBean, boolean isUpdate){
		if (isUpdate) {
			// 为更新操作，先将之前的数据删除，再插入
			Map condition = new HashMap();
			condition.put("userid", userBean.getId());
			daoUtils.execSql("#xtgl.delete_t_xt_userrole", condition);
		}
		// 不管是更新还是插入，都要执行插入的操作
		Map condition = new HashMap();
		condition.put("userid", userBean.getId());
		condition.put("roleid", userBean.getRole().getRoleid());
		try {
			daoUtils.execSql("#xtgl.insert_t_xt_userrole", condition);
		} catch (Exception e) {
			log.info("向用户角色中间表中插入数据出错！",e);
			throw new RuntimeException("向用户角色中间表中插入数据出错！");
		}
	}
}
