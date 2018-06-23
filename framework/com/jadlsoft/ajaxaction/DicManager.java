/**
 * <p>Title: DicManager </p>
 * <p>Description: 数据字典管理，用于从内存中获取请求的数据 </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-5
 */

package com.jadlsoft.ajaxaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

public class DicManager {

	private static Logger log = Logger.getLogger(DicManager.class);

	/**
	 * Description: 获取数据Map列表
	 * 
	 * @param dic 数据字典列表
	 * @param dmcol 代码字段
	 * @param valuecol 数值字段
	 * @param condcol 条件字段
	 * @param oper 条件操作符
	 * @param condvalue 条件比较值
	 * @return 选中的数据列表，错误则返回null 2006-9-5
	 */
	public static List getDicContentMap(List dic, String dmcol, String valuecol, String cond) {
		// 检验是否存在对应的字段
		Map data = (Map) dic.get(0);
		boolean checked = data.containsKey(dmcol) && data.containsKey(valuecol);
		boolean hascond = false;
		List condsplist = null; // 条件列表，每项分解成三段
		if (checked) {
			hascond = (cond != null && cond.length() > 0); // 是否有条件表达式
			if (hascond) {
				condsplist = ConditionManager.checkCondition(cond, data);
				checked = (condsplist != null);
			}
		}

		if (!checked) return null;

		// 获取数据列表
		List resultlist = new ArrayList();
		for (int i = 0; i < dic.size(); i++) {
			Map row = (Map) dic.get(i);
			if (!hascond || ConditionManager.selectData(row, condsplist)) {
				resultlist.add(row);
			}
		}
		return resultlist;
	}

	/**
	 * Description:
	 * 
	 * @param dic 数据字典列表
	 * @param dmcol 代码字段
	 * @param valuecol 数值字段
	 * @param condcol 条件字段
	 * @param oper 条件操作符
	 * @param condvalue 条件比较值
	 * @return XML文本 2006-9-5
	 */
	public static String getDicContent(List dic, String dmcol, String valuecol, String cond) {

		Document doc = null;
		doc = DocumentFactory.getInstance().createDocument();
		doc.setXMLEncoding("GB2312");

		Element eroot = doc.addElement("result");
		Element etemp = null;

		List resultlist = getDicContentMap(dic, dmcol, valuecol, cond);

		if (resultlist == null) {
			etemp = eroot.addElement("status");
			etemp.setText("2");
			etemp = eroot.addElement("content");
			etemp.setText("数据字典获取时发现参数不对");
			log.error("获取数据字典时传入的参数不正确！");
			return doc.asXML();
		}

		etemp = eroot.addElement("status");
		etemp.setText("0");
		etemp = eroot.addElement("content");
		for (int i = 0; i < resultlist.size(); i++) {
			Map row = (Map) resultlist.get(i);
			Element et = etemp.addElement("row");
			et.addAttribute("dm", (String) row.get(dmcol));
			et.addAttribute("name", (String) row.get(valuecol));
		}
		return doc.asXML();
	}

	public static void main(String[] args) {
		List dic = new ArrayList();
		for (int i = 0; i < 10; i++) {
			Map row = new HashMap();
			row.put("dm", String.valueOf(i) + "001");
			row.put("name", "name" + String.valueOf(i));
			dic.add(row);
		}
		System.out.println(DicManager.getDicContent(dic, "dm", "name", "dm#like#5*01"));
	}
}