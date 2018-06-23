package com.jadlsoft.business.xtgl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.business.xtgl.IGnlbManager;
import com.jadlsoft.model.xtgl.Gnlb;
import com.jadlsoft.utils.SystemConstants;

/**
 * 功能列表的manager实现类
 * @类名: GnlbManagerImpl
 * @描述: 完成功能列表的相关信息的操作
 * @作者: 李春晓
 * @时间: 2017-1-5 上午10:54:15
 */
public class GnlbManager extends BaseManager implements IGnlbManager {

	private static Logger log = Logger.getLogger(GnlbManager.class);
	
	
	@Override
	public List getGnlbList() {
		try {
			// 直接从数据库中查询出来的功能列表的集合
			List gnlbss = daoUtils.find("#xtgl.getGnlbList",new HashMap());
			//经过格式化后的数据
			List formatGnlbs = formatGnlbs(gnlbss);
			return formatGnlbs;
		} catch (Exception e) {
			log.info("获取功能列表出错！",e);
			throw new RuntimeException("获取功能列表出错！");
		}
	}
	
	@Override
	public List getGnlbListByRoleId(String roleid) {
		Map condition = new HashMap();
		condition.put("roleid", roleid);
		List gnlbss = daoUtils.find("#xtgl.getGnlbListByRoleId",condition);
		return formatGnlbs(gnlbss);
		
	}
	
	
	
	/*
	 * 以格式化的形式将所有的权限封装返回
	 * 	为了页面方便取值，在这里将权限的对象 进行封装
	 * 	最终封装的结果为:一个list集合，里面放的是map,包含每一个一级菜单的所有内容
	 * 		map里面有两个字段，分别为
	 * 			self，里面存的是父菜单本身的gnlb对象
	 * 			children,里面存的是父菜单对应的所有子菜单集合list，list里面就是每一个的子菜单对象
	 * 						
	 */
	private List formatGnlbs(List gnlbss){		//gnlbss直接从数据库中查询得到的数据
		// 最终返回的格式化封装后的list集合
		List formatGnlbs = new ArrayList();
		// 直接从数据库中查询出来的功能列表的集合
		List<Map> gnlbs = gnlbss;
		
		// 临时的存储每一个父菜单所有内容的map，为了保证顺序，使用了LinkedHashMap
		Map parentMap = new LinkedHashMap();
		//Map parentMap = new HashMap();
		// 每一个父菜单的具体对象
		Map<String,Object> map = null;
		for (Map gnlb : gnlbs) {
			if (((String) gnlb.get("gncode")).endsWith("0000")) {
				List childGnlbs = new ArrayList();	//每一个父菜单map中要放置的子菜单的集合
				// 说明是一级菜单
				map = new HashMap<String,Object>();
				map.put("self", gnlb);	//将自己封装
				map.put("children", childGnlbs); 	//将子菜单集合封装
				parentMap.put(((String) gnlb.get("gncode")).substring(0, 2), map);	//为了下一次循环可以取到值，将每一个父菜单map对象暂时放进临时的存储的map中,key即为标识父菜单的码，01、02、03...
				continue;
			}else {
				//二级菜单
				String parentCode = ((String) gnlb.get("gncode")).substring(0, 2);	//获取子菜单的前两位得到所属的父菜单
				map = (Map) parentMap.get(parentCode);	//得到父菜单对应的map
				if (map != null) {
					((List) map.get("children")).add(gnlb);	//将子菜单添加到父菜单中
				}
				continue;
			}
		}
		// 为了方便在页面中取值，将最终结果以集合的形式返回
//		return (List) parentMap.values();
		Set keySet = parentMap.keySet();
		for (Object object : keySet) {
			formatGnlbs.add(parentMap.get(object));
		}
		return formatGnlbs;
	}
	
	@Override
	public Gnlb getGnlbByGncode(String gid) {
		Map condition = new HashMap();
		condition.put("gncode", gid);
		condition.put("zt", SystemConstants.ZT_TRUE);
		return (Gnlb) daoUtils.findObjectCompatibleNull(Gnlb.class, condition);
	}
	
	@Override
	public int saveGnlb(Gnlb gnlb) {
		if (gnlb != null && gnlb.getGncode()!= null && !gnlb.getGncode().equals("")) {
			return daoUtils.save(gnlb);
		}
		log.info("功能菜单添加失败，功能菜单对象为空或者功能代码为空");
		throw new RuntimeException("功能菜单添加失败，功能菜单对象为空或者功能代码为空");
	}
	
	@Override
	public int updateGnlb(Gnlb gnlb) {
		if (gnlb != null && gnlb.getGncode()!= null && !gnlb.getGncode().equals("")) {
			return daoUtils.update(gnlb);
		}
		log.info("功能菜单更新失败，功能菜单对象为空或者功能代码为空");
		throw new RuntimeException("功能菜单更新失败，功能菜单对象为空或者功能代码为空");
	}
	
	@Override
	public void deleteGnlb(String gncode) {
		if (gncode!= null && !gncode.equals("")) {
			//因为功能菜单的主键就是gncode，设置zt的方法删除之后在新建时候会有问题，在这里，删除将会真实删除表中的数据
			Map condition = new HashMap();
			condition.put("gncode", gncode);
			int execSql = daoUtils.execSql("#xtgl.deleteGnlbByGncode", condition);
			if (execSql < 1) {
				//说明没有删除，记录日志
				log.warn("删除的数据库中的数据为0，请检查");
			}
			return;
//			Gnlb thisGnlb = getGnlbByGncode(gncode);
//			if (thisGnlb != null) {
//				thisGnlb.setZt(SystemConstants.ZT_FALSE);
//				daoUtils.update(thisGnlb, "zt");
//				return;
//			}else {
//				log.info("功能菜单删除失败，要删除的功能菜单对象不存在");
//				throw new RuntimeException("功能菜单删除失败，要删除的功能菜单对象不存在");
//			}
		}
		log.info("功能菜单删除失败，功能代码为空");
		throw new RuntimeException("功能菜单删除失败，功能代码为空");
	}
	
	@Override
	public Gnlb getGnlbAllByGncode(String gncode) {
		Map condition = new HashMap();
		condition.put("gncode", gncode);
		return (Gnlb) daoUtils.findObject(Gnlb.class, condition);
	}

}
