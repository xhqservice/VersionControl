package com.jadlsoft.business.xtgl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.business.xtgl.IPermissManager;
import com.jadlsoft.model.xtgl.Permiss;
import com.jadlsoft.model.xtgl.Role;
import com.jadlsoft.utils.SystemConstants;

/**
 * 权限相关操作的实现类
 * @类名: PermissManager
 * @描述: 完成具体的权限相关的操作
 * @作者: 李春晓
 * @时间: 2017-1-4 上午10:10:28
 */
public class PermissManager extends BaseManager implements IPermissManager {

	private static Logger log = Logger.getLogger(PermissManager.class);
	
	@Override
	public List getPermisses() {
		List permisses = daoUtils.find("#xtgl.getPermisses", new HashMap());
		return permisses;
	}

	@Override
	public List getPermissesByRoleId(String roleid) {
		Map condition = new HashMap();
		condition.put("roleid", roleid);
		return daoUtils.find(Role.class, condition);
	}

	@Override
	public Permiss getPermissContainsGnlbByPid(String permissid) {
		Map condition = new HashMap();
		condition.put("permissid", permissid);
		try {
			// 先得到权限自身对象
			Permiss permissBean = (Permiss) daoUtils.findObject(Permiss.class, condition);
			// 得到该权限对应的所有的gnlb的gncode集合
			List gnlbs = daoUtils.find("#xtgl.getGnlbsByPermissid",condition);
			permissBean.setGnlbs(gnlbs);
			return permissBean;
		} catch (Exception e) {
			log.info("获取包含功能列表的权限对象失败！",e);
			throw new RuntimeException("获取包含功能列表的权限对象失败！");
		}
	}

	@Override
	public void save(Permiss permiss) {
		//先保存基本的信息
		permiss.setPermissid(String.valueOf(this.daoUtils.getNextval("Q_XT_PERMISS")));	//设置permissid
		permiss.setZt(SystemConstants.ZT_TRUE);	//设置状态
		try {
			int save = daoUtils.save(permiss, "permissid,permissname,zt");
			if (save>0) {
				//更新中间表
				updatePermissGnlb(permiss, false);
			}
		} catch (Exception e) {
			log.info("保存权限对象失败！",e);
			throw new RuntimeException("保存权限对象失败！");
		}
	}
	
	@Override
	public void update(Permiss permiss) {
		try {
			int update = daoUtils.update(permiss, "permissname");
			if (update>0) {
				updatePermissGnlb(permiss, true);
			}
		} catch (Exception e) {
			log.info("更新权限对象失败！",e);
			throw new RuntimeException("更新权限对象失败！");
		}
	}
	
	@Override
	public void delete(String pid) {
		Map condition = new HashMap();
		condition.put("permissid", pid);
		try {
			Permiss deletePermiss = (Permiss) daoUtils.findObject(Permiss.class, condition);
			deletePermiss.setZt(SystemConstants.ZT_FALSE);
			int delete = daoUtils.update(deletePermiss, "zt");
			if (delete>0) {
				//删除中间表内容
				daoUtils.execSql("#xtgl.delete_t_xt_permissgnlb_permissid", condition);
			}
		} catch (Exception e) {
			log.info("删除权限对象失败！",e);
			throw new RuntimeException("删除权限对象失败！");
		}
	}
	
	/**
	 * 根据标识符isUpdate判断是否是更新操作
	 * @功能: 如果true，说明是更新操作，在插入之前先将之前的信息删除
	 * @param permiss 要操作的对象
	 * @param isUpdate 是否为更新的操作
	 * @return: void
	 */
	private void updatePermissGnlb(Permiss permiss, boolean isUpdate){
		if (isUpdate) {
			//先将之前的信息清空
			Map condition =new HashMap();
			condition.put("permissid", permiss.getPermissid());
			daoUtils.execSql("#xtgl.delete_t_xt_permissgnlb_permissid", condition);
		}
		//将信息批量插入中间表中
		String permissid = String.valueOf(permiss.getPermissid());
		// 采用批量添加的方式
		String[] permissGnlbField = { "permissid", "gncode"}; // 待插入值的字段
		List permissGnlbList = new ArrayList();	//待插入的数据，里面放的是map
		Map data = null;
		for (int i = 0; i < permiss.getGnlbs().size(); i++) {
			data = new HashMap();
			data.put("permissid", permissid);
			data.put("gncode", permiss.getGnlbs().get(i).toString());
			permissGnlbList.add(data);
		}
		daoUtils.executeBatchUpdate("#xtgl.insert_t_xt_permissgnlb", permissGnlbField, permissGnlbList);
	}

}