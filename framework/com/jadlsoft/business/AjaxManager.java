/**
 * <p>Title:AjaxManager.java </p>
 * <p>Description: 页面脚本与后台服务器交互</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 京安丹灵</p>
 * @date 2011-12-29
 * @author ZongShuai
 * @version 1.0
*/
package com.jadlsoft.business;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class AjaxManager extends BaseManager {
	private static Logger log = Logger.getLogger(AjaxManager.class);
	
	/**
	 * 从数据库中获取数据
	 * @param dataname	数据名称
	 * @param cols 		列名
	 * @param cond		条件
	 * @return List
	 */
	public List getDataContent(String dataname, String cols, String cond) {
		boolean checked = true;
		boolean hascond = false;
		List condsplist = null; // 条件列表,每项分解成三段
		hascond = (cond != null && cond.length() > 0); // 是否有条件表达式
		if (hascond) {
			condsplist = checkCondition(cond);
			checked = (condsplist != null);
		}

		if (!checked) {
			return null;
		}

		String realcols;
		if (cols == null || cols.length() == 0) {
			realcols = "*";
		} else {
			realcols = cols;
		}

		String sql = null;
		try {
			sql = "SELECT " + realcols + " FROM " + dataname
					+ getConditionSQL(condsplist);
			System.out.println("-----------------ajaxManager:"+sql); 
			List list = daoUtils.find(sql, new HashMap());
			return list;
		} catch (Exception e) {
			log.error("获取数据时执行错误！" + (sql == null ? "" : sql), e);
			return null;
		}
	}

	/**
	 * 从代码表缓存中获取数据
	 * @param dic	数据字典列表
	 * @param codecol	代码字段
	 * @param textcol	显示名称字段
	 * @param cond	查询条件
	 * @return 
	 */
	public List getDicContent(List dic, String codecol, 
			String textcol, String cond) {
		return getDicContentMap(dic, codecol, textcol, cond);
	}

	/**
	 * 从代码表缓存中获取数据,其他Action的view方法可以直接调用
	 * @param dic	数据字典列表
	 * @param codecol	代码字段
	 * @param textcol	显示名称字段
	 * @param cond	查询条件
	 * @return 选中的数据列表,错误则返回null
	 */
	public static List getDicContentMap(List dic, String codecol, 
			String textcol, String cond) {
		Map data = (Map)dic.get(0);
		boolean checked = data.containsKey(codecol) && data.containsKey(textcol);
		boolean hascond = false;
		List condsplist = null; // 条件列表,每项分解成三段
		if (checked) {
			hascond = (cond != null && cond.length() > 0); // 是否有条件表达式
			if (hascond) {
				condsplist = checkCondition(cond, data);
				checked = (condsplist != null);
			}
		}

		if (!checked) {
			return null;
		}

		// 获取数据列表
		List resultlist = new ArrayList();
		for (int i = 0; i < dic.size(); i++) {
			Map row = (Map) dic.get(i);
			if (!hascond || selectData(row, condsplist)) {
				resultlist.add(row);
			}
		}
		return resultlist;
	}
	
	/**
	 * 查询数据是否存在
	 * @param dataname	完整的表名称
	 * @param cond	查询条件
	 * @return 存在返回3,不存在返回0
	 */
	public String checkExistContent(String dataname, String cond) {
		//检验是否存在对应的字段
		boolean checked = true;
		boolean hascond = false;
		List condsplist = null; // 条件列表，每项分解成三段
		hascond = (cond != null && cond.length() > 0); // 是否有条件表达式
		if (hascond) {
			condsplist = checkCondition(cond);
			checked = (condsplist != null);
		}
		
		if (!checked) {
			log.error("检查数据是否存在时传入的参数不正确！参数为：" + cond);
			return ("2检查数据是否存在时发现参数不对");
		}

		try {
			
			int count = daoUtils.queryForInt("SELECT COUNT(*) FROM "
					+ dataname + getConditionSQL(condsplist));
			if (count == 0) {
				int a = 0;
				return (String.valueOf(a));
			} else {
				int a = 3;
				return (String.valueOf(a));
			}
		} catch (Exception e) {
			log.error("检查数据是否存在时执行错误！", e);
			return ("1检查数据是否存在时出现错误");
		}
	}

	/**
	 * 获取条件SQL语句
	 * @param condsplist 条件列表
	 * @return SQL条件
	 */
	private String getConditionSQL(List condsplist) {
		if (condsplist == null || condsplist.size() == 0)
			return "";

		StringBuffer sql = new StringBuffer(" WHERE ");
		for (int i = 0; i < condsplist.size(); i++) {
			String[] percond = (String[]) condsplist.get(i);
			String oper = percond[1];
			String condvalue = percond[2];
			if (oper.equals("equal")) {
				sql.append(percond[0] + "='" + condvalue + "' AND ");
			} else if (oper.equals("great")) {
				sql.append(percond[0] + ">'" + condvalue + "' AND ");
			} else if (oper.equals("less")) {
				sql.append(percond[0] + "<'" + condvalue + "' AND ");
			} else if (oper.equals("notequal")) {
				sql.append(percond[0] + "<>'" + condvalue + "' AND ");
			} else {
				sql.append(percond[0] + " like '"
						+ condvalue.replaceAll("\\*", "%") + "' AND ");
			}
		}
		if (sql.length() > 7) {
			sql.delete(sql.length() - 4, sql.length());
		}
		return sql.toString();
	}

	/**
	 * 判断是否合法的条件操作
	 * @param oper 操作
	 * @return 是否合法
	 */
	static boolean isvalidOper(String oper) {
		return oper != null
				&& oper.length() > 0
				&& (oper.equals("equal") || oper.equals("great")
						|| oper.equals("less") || oper.equals("notequal") || oper
						.equals("like") || oper.equals("greatequal")
				|| oper.equals("lessequal"));
	}

	/**
	 * 判断数据是否符合条件 
	 * @param row 数据行
	 * @param cond 分解后的条件
	 * @return 是否选择
	 */
	static boolean selectData(Map row, List cond) {
		boolean selected = true;
		for (int i = 0; i < cond.size(); i++) {
			String[] percond = (String[]) cond.get(i);
			String rowvalue = (String) row.get(percond[0]);
			String oper = percond[1];
			String condvalue = percond[2];
			if (oper.equals("equal")) {
				selected = rowvalue.equals(condvalue);
			} else if (oper.equals("great")) {
				selected = rowvalue.compareTo(condvalue) > 0;
			} else if (oper.equals("less")) {
				selected = rowvalue.compareTo(condvalue) < 0;
			} else if (oper.equals("notequal")) {
				selected = !rowvalue.equals(condvalue);
			} else if (oper.equals("greatequal")) {
				selected = (rowvalue.compareTo(condvalue) > 0 || rowvalue
						.equals(condvalue));
			} else if (oper.equals("lessequal")) {
				selected = (rowvalue.compareTo(condvalue) < 0 || rowvalue
						.equals(condvalue));
			} else {
				selected = rowvalue.matches(condvalue.replaceAll("\\*", ".*"));
			}
			if (!selected)
				return false;
		}
		return true;
	}

	/**
	 * 检验条件表达式是否合法,合法则分解表达式
	 * @param cond 条件表达式
	 * @param data 字段数据
	 * @return 合法则返回分解后的表达式，不合法则返回空
	 */
	static List checkCondition(String cond, Map data) {
		List condsplist = new ArrayList();
		String[] conds = cond.split("~");
		for (int i = 0; i < conds.length; i++) {
			String[] percond = conds[i].split("#");
			if (percond.length != 3)
				continue;
			if (percond[0].length() > 0 && data.containsKey(percond[0])
					&& isvalidOper(percond[1]) && percond[2].length() > 0) {
				condsplist.add(percond);
			} else {
				return null;
			}
		}
		return condsplist;
	}
	
	/**
	 * 检验条件表达式是否合法,合法则分解表达式
	 * @param cond 条件表达式
	 * @return 合法则返回分解后的表达式，不合法则返回空
	 */
	List checkCondition(String cond) {
		List condsplist = new ArrayList();
		String[] conds = cond.split("~");
		for (int i = 0; i < conds.length; i++) {
			String[] percond = conds[i].split("#");
			if (percond.length != 3)
				continue;
			if (percond[0].length() > 0 && isvalidOper(percond[1])
					&& percond[2].length() > 0) {
				condsplist.add(percond);
			} else {
				return null;
			}
		}
		return condsplist;
	}
}
