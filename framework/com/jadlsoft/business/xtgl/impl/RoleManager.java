package com.jadlsoft.business.xtgl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.business.xtgl.IRoleManager;
import com.jadlsoft.model.xtgl.Role;
import com.jadlsoft.utils.SystemConstants;

public class RoleManager extends BaseManager implements IRoleManager {
	
	private static Logger log = Logger.getLogger(RoleManager.class);

	/*
	 * 获取角色列表
	 */
	@Override
	public List getRolelist() {
		try {
			List rolelist = daoUtils.find("#xtgl.getRolelist",new HashMap());
			return rolelist;
		} catch (Exception e) {
			log.info("获取角色列表出错！",e);
			throw new RuntimeException("获取角色列表出错！");
		}
	}

	/*
	 * 保存角色
	 */
	@Override
	public void save(Role role) {
		role.setRoleid(String.valueOf(this.daoUtils.getNextval("Q_XT_ROLE")));
		role.setZt(SystemConstants.ZT_TRUE);
		try {
			int result = daoUtils.save(role, "roleid,rolename,zt");
			if(result>0){
				updateRolePermisses(role, false);
			}
		} catch (Exception e) {
			log.info("保存角色信息出错！",e);
			throw new RuntimeException("保存角色信息出错！");
		}
	}

	/*
	 * 更新角色
	 */
	@Override
	public void update(Role role) {
		try {
			int result = daoUtils.update(role, "rolename");
			if(result>0){
				updateRolePermisses(role, true);
			}
		} catch (Exception e) {
			log.info("更新角色信息出错！",e);
			throw new RuntimeException("更新角色信息出错！");
		}
	}

	/**
	 * 完成角色权限表的更新和新增操作
	 * @功能: 根据标识符isDelete判断是否要先删除之前的用户跟权限的对应
	 * @param role 操作的角色对象
	 * @param isDelete 是否先删除
	 * @return: void
	 */
	private void updateRolePermisses(Role role, boolean isDelete) {
		if (isDelete) {
			/*
			 * 更新时传入true,将原来的角色对应的功能信息删除
			 */
			Map condition = new HashMap();
			condition.put("roleid", role.getRoleid());
			try {
				daoUtils.execSql("#xtgl.delete_t_xt_rolepermiss_roleid", condition);
			} catch (Exception e) {
				log.info("删除角色对应的功能信息出错！",e);
				throw new RuntimeException("删除角色对应的功能信息出错！");
			}
		}
		/*
		 * 保存角色对应的功能信息
		 */
		try {
			if (role.getPermisses() != null &&role.getPermisses().size()>0) {
				String roleid = String.valueOf(role.getRoleid());
				// 采用批量添加的方式
				String[] rolePermissField = { "roleid", "permissid"}; // 待插入值的字段
				List rolePermissList = new ArrayList();	//待插入的数据，里面放的是map
				Map data = null;
				for (int i = 0; i < role.getPermisses().size(); i++) {
					data = new HashMap();
					data.put("roleid", roleid);
					data.put("permissid", role.getPermisses().get(i).toString());
					rolePermissList.add(data);
				}
				daoUtils.executeBatchUpdate("#xtgl.insert_t_xt_rolepermiss", rolePermissField, rolePermissList);
			}
		} catch (Exception e) {
			log.info("保存角色对应的功能信息出错！",e);
			throw new RuntimeException("保存角色对应的功能信息出错！");
		}
	}

	@Override
	public Role get(String roleid) {
		Map condition = new HashMap();
		condition.put("roleid", roleid);
		try {
			Role role = (Role) daoUtils.findObjectCompatibleNull(Role.class, condition);
			if (role != null) {
				List permiss = daoUtils.find(condition, "t_xt_rolepermiss");
				for (int i = 0; i < permiss.size(); i++) {
					role.getPermisses().add(((Map) permiss.get(i)).get("permissid")); 
				
				}
				return role;
			}
		} catch (Exception e) {
			log.info("根据角色id获取角色对象出错！",e);
			throw new RuntimeException("根据角色id获取角色对象出错！");
		}

		return null;
	}

	/*
	 * 删除角色
	 */
	@Override
	public void delete(String roleid) {
		try {
			// 设置角色状态为不可用
			Map condition = new HashMap();
			condition.put("roleid", roleid);
			Role deleteRole = (Role) daoUtils.findObject(Role.class, condition);
			deleteRole.setZt(SystemConstants.ZT_FALSE);
			int delete = daoUtils.update(deleteRole, "zt");
			if (delete>0) {
				// 删除中间表的数据
				daoUtils.execSql("#xtgl.delete_t_xt_rolepermiss_roleid", condition);
			}
		} catch (Exception e) {
			log.info("根据角色主键roleid删除角色出错！",e);
			throw new RuntimeException("根据角色主键roleid删除角色出错！");
		}
	}

	@Override
	public String getMaxId() {
		try {
			String maxid = (String) daoUtils.queryForObject("select max(TO_NUMBER(roleid)) roleid from t_xt_role",
					String.class);
			if (maxid == null || maxid.equals("")) 
			{
				maxid = "1";
			} 
			else
			{
				maxid = String.valueOf(Integer.valueOf(maxid)+1);
			}	
			return maxid;
		} catch (Exception e) {
			log.info("获取角色当前最大id出错！",e);
			throw new RuntimeException("获取角色当前最大id出错！");
		}
	}

	/*
	 * 通过角色id获取角色，包含角色所拥有的全部权限
	 */
	@Override
	public Role getRoleContainsPermissesByRid(String rid) {
		// 得到role的基本对象
		Map condition = new HashMap();
		condition.put("roleid", rid);
		try {
			Role roleBean = (Role) daoUtils.findObject(Role.class, condition);
			// 得到该角色所拥有的所有的权限对象
			List permissMaps = daoUtils.find("#xtgl.getPermissesByRid", condition);
			// 将权限设置进角色对象中
			roleBean.setPermisses(permissMaps);
			return roleBean;
		} catch (Exception e) {
			log.info("通过角色id获得包含用户的角色对象出错！",e);
			throw new RuntimeException("通过角色id获得包含用户的角色对象出错！");
		}
		
	}

	@Override
	public Role getRoleByUserId(String userid) {
		// 得到role的基本对象
		Map condition = new HashMap();
		condition.put("userid", userid);
		try {
			Object obj = daoUtils.findObject("#xtgl.getRoleByUserId",condition,Role.class);
			return obj==null ? null :(Role) obj;
		} catch (Exception e) {
			log.info("通过用户id获得角色对象出错！",e);
			throw new RuntimeException("通过用户id获得角色对象出错！");
		}
	}

}
