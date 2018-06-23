/**
 * <p>Title:UserManager.java </p>
 * <p>Description: 系统用户数据库存储处理类</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 京安丹灵</p>
 * @date 2009-07-30
 * @author zongshuai
 * @version 3.0
*/

package com.jadlsoft.business.xtgl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.model.xtgl.Gnlb;
import com.jadlsoft.model.xtgl.UserBean;

public class UserManager extends BaseManager {
	private static Logger log = Logger.getLogger(UserManager.class);
	/**
	 * 保存用户信息
	 * @param userBean:待保存用户
	 * @return
	 */ 
	public void save(UserBean userBean) {
		userBean.setZt("1");
		userBean.setId(this.getMaxId());
		try {
			daoUtils.save(userBean, "id,userid,password,username,levels,qydm,qymc,roleid,zt,xzqh,dwrzt,lxrxm,lxrlxdh");
		} catch (Exception e) {
			log.info("保存登录用户出错！",e);
			throw new RuntimeException("保存登录用户出错！");
		}
		
	}
	
	/**
	 * 更新用户信息
	 * @param userBean:待更新用户信息
	 * @param updateFields:待更新的字段
	 * @return
	 */
	public void update(UserBean userBean, String updateFields) {
		try {
			daoUtils.update(userBean, updateFields);
		} catch (Exception e) {
			log.info("更新登录用户出错！",e);
			throw new RuntimeException("更新登录用户出错！");
		}
		
	}

	/**
	 * 根据用户主键userid删除用户
	 * @return
	 */
	public void delete(String userid) {
		Map condition = new HashMap();
		condition.put("userid", userid);
		try {
			daoUtils.execSql("#xtgl.delete_t_xt_user_userid", condition);
		} catch (Exception e) {
			log.info("根据用户主键userid删除用户出错！",e);
			throw new RuntimeException("根据用户主键userid删除用户出错！");
		}
		
	}
	
	/**
	 * 根据userid获取用户信息
	 * @param userid
	 * @return User对象
	 */
	public UserBean get(String userid){
		Map condition = new HashMap();
		condition.put("userid", userid);
		try {
			Object object = daoUtils.findObject("#xtgl.select_t_xt_user_userid", condition, UserBean.class);
			return object==null?null:(UserBean)object;
		} catch (Exception e) {
			log.info("根据userid获取用户信息出错！",e);
			throw new RuntimeException("根据userid获取用户信息出错！");
		}
		
	}

	/**
	 * 根据userid和密码获取用户信息
	 * @param userid 用户ID
	 * @param password 密码
	 * @return User对象
	 */
	public UserBean get(String userid, String password){
		Map condition = new HashMap();
		condition.put("userid", userid);
		condition.put("password", password);
		
		try {
			Object object = daoUtils.findObject("#xtgl.getUserBean", condition, UserBean.class);
			return object==null?null:(UserBean)object;
		} catch (Exception e) {
			log.info("根据userid和密码获取用户信息出错！",e);
			throw new RuntimeException("根据userid和密码获取用户信息出错！");
		}
		
	}

	/**
	 * 获取用户信息及用户角色信息列表
	 * @param sfz:用户身份证
	 * @return List 用户信息及用户角色信息列表
	 */
	public List getUserList(String sfz){
		Map condition = new HashMap();
		condition.put("sfz", sfz);
		try {
			return daoUtils.find("#xtgl.select_user_role", condition);
		} catch (Exception e) {
			log.info("获取用户信息及用户角色信息列表出错！",e);
			throw new RuntimeException("获取用户信息及用户角色信息列表出错！");
		}
		
	}

	
	/**
	 * getTreeModuleInfo() 功能：获取树形目录
	 * 
	 * @param roleID
	 *            角色ID
	 * @return
	 * @throws Exception
	 *             List
	 */
	public List getTreeModuleInfo(String roleID)  {
		final List treeList = new ArrayList();
		StringBuffer strSelectSql = new StringBuffer();
		RowCallbackHandler rowCallbackHandler = null;
		try {

			strSelectSql
					.append("select M.* from T_XT_USER U,T_XT_MODULE M,T_XT_ROLE R,T_XT_ROLEPERMISS RP, T_XT_PERMISS P, T_XT_OPERATION O");
			strSelectSql
					.append(" where M.ZT='1'  AND R.ROLEID=RP.ROLEID AND RP.PERMISSID=p.PERMISSID AND U.ROLEID=R.ROLEID AND p.PERMISSID=O.PERMISSID AND O.CODE=M.CODE AND U.USERID = '");
			strSelectSql.append(roleID);
			strSelectSql.append("'");
			strSelectSql.append(" order by M.CODE");

			rowCallbackHandler = new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					Gnlb module = new Gnlb();
					module.setGncode(rs.getString("CODE"));
					module.setGnname(rs.getString("GN"));
					module.setLink(rs.getString("LINK"));
					module.setSort(rs.getString("SXH"));
					treeList.add(module);
				}
			};
			daoUtils.getJdbcTemplate().query(strSelectSql.toString(),
					rowCallbackHandler);
		} catch (Exception e) {
			log.info("获取树形功能目录出错！",e);
			throw new RuntimeException("获取树形功能目录出错！");
		}
		return treeList;
	}
	
	/**
	 * 
	 * @功能 TODO 根据userid获取用户
	 * @param userid
	 * @return
	 * @作者 吴家旭 Feb 28, 2013 8:40:04 PM
	 */
	public  List getUserByuserid(String userid) {
		String sql = "select * from t_xt_user where userid = '" + userid + "' and zt = '1'";
		try {
			return this.daoUtils.find(sql, new HashMap<String, String>());
		} catch (Exception e) {
			log.info("根据userid获取用户出错！",e);
			throw new RuntimeException("根据userid获取用户出错！");
		}
		
	}
	/**
	 * 
	 * @功能 获取用户当前最大id
	 * @return
	 * @作者 吴家旭 Feb 28, 2013 8:39:43 PM
	 */
	public String getMaxId() {
		try {
			String maxid = (String) daoUtils.queryForObject("select max(to_number(id)) id from t_xt_user",String.class);
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
			log.info("获取用户当前最大id出错！",e);
			throw new RuntimeException("获取用户当前最大id出错！");
		}
		
	}

	
	
	
}