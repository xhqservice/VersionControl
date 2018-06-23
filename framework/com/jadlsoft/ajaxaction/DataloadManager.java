/**
 * <p>Title: DataloadManager </p>
 * <p>Description: 数据刷新管理，用于从数据库中获取请求的数据 </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-5
*/

package com.jadlsoft.ajaxaction;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jadlsoft.dbutils.BlobUtils;
import com.jadlsoft.dbutils.DicMapUtils;

public class DataloadManager {
	
	private static Logger log = Logger.getLogger(DataloadManager.class);
	
	/**
	 * Description: 获取条件SQL语句
	 * @param condsplist 条件列表
	 * @return SQL条件
	 * 2006-10-9
	 */
	private static String getConditionSQL(List condsplist) {
		if(condsplist==null || condsplist.size()==0)
			return "";
		
		StringBuffer sql = new StringBuffer(" WHERE ");
		for(int i=0;i<condsplist.size();i++) {
			String[] percond = (String[])condsplist.get(i);
			String oper = percond[1];
			String condvalue = percond[2];
			if(oper.equals("equal")) {
				sql.append(percond[0] + "='" + condvalue +"' AND ");
			} else if(oper.equals("great")) {
				sql.append(percond[0] + ">'" + condvalue +"' AND ");
			} else if(oper.equals("less")) {
				sql.append(percond[0] + "<'" + condvalue +"' AND ");
			} else if(oper.equals("notequal")) {
				sql.append(percond[0] + "<>'" + condvalue +"' AND ");
			} else {
				sql.append(percond[0] + " like '" 
						+ condvalue.replaceAll("\\*", "%") +"' AND ");
			}
		}
		if(sql.length()>7) {
			sql.delete(sql.length()-4, sql.length());
		}
		return sql.toString();
	}
	
	/**
	 * Description:  检查数据是否存在，返回文本，0不存在，3存在，1系统错误，2参数错误
	 * @param dataname	数据名称
	 * @param cond		条件
	 * @return XML文本
	 * 2006-9-5
	 */
	public static String checkExistContent(DataSource ds, String dataname, String cond) {
		
		//检验是否存在对应的字段
		boolean checked = true;
		boolean hascond = false;
		List condsplist = null; //条件列表，每项分解成三段
		hascond = (cond!=null && cond.length()>0);  //是否有条件表达式
		if(hascond) {
			condsplist = ConditionManager.checkCondition(cond);
			checked = (condsplist != null);
		}
		
		
		if(!checked) {
			log.error("检查数据是否存在时传入的参数不正确！参数为：" + cond);
			return("2检查数据是否存在时发现参数不对");
		}
		
		try {
			JdbcTemplate jt = new JdbcTemplate(ds);
			int count = jt.queryForInt("SELECT COUNT(*) FROM t_" + dataname + 
					getConditionSQL(condsplist));
					
			if(count==0) {
				return("0");
			} else {
				return("3");
			}
		} catch(Exception e) {
			log.error("检查数据是否存在时执行错误！", e);
			return("1检查数据是否存在时出现错误");
		}
	}
	
	/**
	 * Description:  检查数据是否存在，返回XML，0不存在，3存在，1系统错误，2参数错误
	 * @param dataname	数据名称
	 * @param cond		条件
	 * @return XML文本
	 * @deprecated 目前暂时不使用
	 * 2006-9-5
	 */
	public static String getExistContent(DataSource ds, String dataname, String cond) {
		
		Document doc=null;
		doc = DocumentFactory.getInstance().createDocument();
		doc.setXMLEncoding("GB2312");
		
		Element eroot=doc.addElement("result");
		Element etemp = null;
		
		
		//检验是否存在对应的字段
		boolean checked = true;
		boolean hascond = false;
		List condsplist = null; //条件列表，每项分解成三段
		hascond = (cond!=null && cond.length()>0);  //是否有条件表达式
		if(hascond) {
			condsplist = ConditionManager.checkCondition(cond);
			checked = (condsplist != null);
		}
		
		
		if(!checked) {
			etemp = eroot.addElement("status");
			etemp.setText("2");
			etemp = eroot.addElement("content");
			etemp.setText("检查数据是否存在时发现参数不对");
			log.error("检查数据是否存在时传入的参数不正确！参数为：" + cond);
			return doc.asXML();
		}
		
		etemp = eroot.addElement("status");
		try {
			JdbcTemplate jt = new JdbcTemplate(ds);
			int count = jt.queryForInt("SELECT COUNT(*) FROM t_" + dataname + 
					getConditionSQL(condsplist));
					
			if(count==0) {
				etemp.setText("0");
			} else {
				etemp.setText("3");
			}
			etemp = eroot.addElement("content");
		} catch(Exception e) {
			etemp.setText("1");
			etemp = eroot.addElement("content");
			etemp.setText("检查数据是否存在时出现错误");
			log.error("检查数据是否存在时执行错误！", e);
		}
		return doc.asXML();
	}
	
	/**
	 * Description:  获取数据Map列表，
	 * @param dataname	数据名称
	 * @param cols 列名
	 * @param cond		条件
	 * @param isBlob 是否Blob字段
	 * @return XML文本
	 * 2006-9-5
	 */
	public static List getDataContentMap(DataSource ds, BlobUtils blobUtils, String dataname, String cols, String cond, boolean isBlob) {
		//	检验是否存在对应的字段
		boolean checked = true;
		boolean hascond = false;
		List condsplist = null; //条件列表，每项分解成三段
		hascond = (cond!=null && cond.length()>0);  //是否有条件表达式
		if(hascond) {
			condsplist = ConditionManager.checkCondition(cond);
			checked = (condsplist != null);
		}
		
		
		if(!checked) {
			return null;
		}
		
		String realcols;
		if(cols==null || cols.length()==0) {
			realcols = "*";
		} else {
			realcols = cols;
		}
		
		String sql = null;
		try {
			JdbcTemplate jt = new JdbcTemplate(ds);
			List list = null;
			if(!isBlob) {
				sql = "SELECT " + realcols + " FROM " + dataname 
						+ getConditionSQL(condsplist);
				list = jt.query(sql, new DicMapUtils());
			} else {
				sql = "SELECT " + realcols + " FROM " + dataname 
						+ getConditionSQL(condsplist);
				list = blobUtils.doGetBlobList(sql, cols.split(","), (String[]) null);
			}
					
			return list;
		} catch(Exception e) {
			log.error("获取数据时执行错误！" + (sql==null? "": sql), e);
			return null;
		}
	}
	
	
	/**
	 * Description:  获取数据
	 * @param dataname	数据名称
	 * @param cols 		列名
	 * @param cond		条件
	 * @return XML文本
	 * 2006-9-5
	 */
	public static String getDataContent(DataSource ds, BlobUtils blobUtils, String dataname, String cols, String cond, boolean isBlob) {
		
		Document doc=null;
		doc = DocumentFactory.getInstance().createDocument();
		doc.setXMLEncoding("UTF-8");
		
		Element eroot=doc.addElement("result");
		Element etemp = null;
		
		List resultlist = getDataContentMap(ds, blobUtils, dataname, cols, cond, isBlob);
		
		if(resultlist==null) {
			etemp = eroot.addElement("status");
			etemp.setText("2");
			etemp = eroot.addElement("content");
			etemp.setText("数据获取时出现错误");
			log.error("获取数据时出现错误！");
			return doc.asXML();
		}
		
		etemp = eroot.addElement("status");
		etemp.setText("0");
		etemp = eroot.addElement("content");
		for(int i=0;i<resultlist.size();i++) {
			Map row = (Map)resultlist.get(i);
			
			Element et = etemp.addElement("row");
			Set keys = row.keySet();
			Iterator item = keys.iterator();
			
			while(item.hasNext()) {
				Object col = item.next();
				et.addAttribute((String)col, row.get(col)==null?"":row.get(col).toString());
			}
			
		}
		return doc.asXML();
	}

}
