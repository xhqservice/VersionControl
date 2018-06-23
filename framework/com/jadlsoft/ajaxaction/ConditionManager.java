/**
 * <p>Title: ConditionManager</p>
 * <p>Description: 公共条件处理对象</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 
 * @version 1.0
 * 2006-9-20
 */

package com.jadlsoft.ajaxaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
    
/**
 * @author libanggui
 * 
 */
public class ConditionManager {

	/**
	 * Description: 判断是否合法的条件操作
	 * 
	 * @param oper 操作
	 * @return 是否合法 2006-9-6
	 */
	static boolean isvalidOper(String oper) {
		return oper != null && oper.length() > 0 && (oper.equals("equal") || oper.equals("great") || oper.equals("less") || oper.equals("less_date") ||  oper.equals("notequal") || oper.equals("like"));
	}

	/**
	 * Description: 判断数据是否符合条件
	 * 
	 * @param row 数据行
	 * @param cond 分解后的条件
	 * @return 是否选择 2006-9-6
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
			} else {
				selected = rowvalue.matches(condvalue.replaceAll("\\*", ".*"));
			}
			if (!selected) return false;
		}
		return true;
	}

	/**
	 * Description: 检验条件表达式是否合法，合法则分解表达式
	 * 
	 * @param cond 条件表达式
	 * @param data 字段数据
	 * @return 合法则返回分解后的表达式，不合法则返回空 2006-9-6
	 */
	static List checkCondition(String cond, Map data) {
		List condsplist = new ArrayList();
		String[] conds = cond.split("~");
		for (int i = 0; i < conds.length; i++) {
			String[] percond = conds[i].split("#");
			if (percond.length != 3)
				continue;
			if (percond[0].length() > 0 && data.containsKey(percond[0]) && isvalidOper(percond[1]) && percond[2].length() > 0) {
				condsplist.add(percond);
			} else {
				return null;
			}
		}
		return condsplist;
	}

	/**
	 * Description: 检验条件表达式是否合法，合法则分解表达式
	 * 
	 * @param cond 条件表达式
	 * @return 合法则返回分解后的表达式，不合法则返回空 2006-9-6
	 */
	static List checkCondition(String cond) {
		List condsplist = new ArrayList();
		String[] conds = cond.split("~");
		for (int i = 0; i < conds.length; i++) {
			String[] percond = conds[i].split("#");
			if (percond.length != 3)
				continue;
			if (percond[0].length() > 0 && isvalidOper(percond[1]) && percond[2].length() > 0) {
				condsplist.add(percond);
			} else {
				return null;
			}
		}
		return condsplist;
	}
}