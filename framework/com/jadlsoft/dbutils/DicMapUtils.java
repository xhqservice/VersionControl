package com.jadlsoft.dbutils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import sun.misc.BASE64Encoder;

import com.jadlsoft.utils.DateUtils;

public class DicMapUtils implements RowMapper {
	private static Logger log = Logger.getLogger(DicMapUtils.class);
	private static Map dicMapCache = new HashMap();
	private static JdbcOperations jdbcTemplate = null;
	private static String[] tableList;		//需要缓存的数据表信息列表
	private static String keyColumn;		//字典表中的主键列
	
	private static String dicConfigFile;	//字典配置文件
	private static Map dicMapTranslate = new LinkedHashMap();	//数据字典定义
	private static Map dicMapCacheForTranslate = new HashMap();
	private static DicMapUtils instance = new DicMapUtils();
	
	/*
	 * zhangsj add 20170628 字典表名（在数据库中可以不存在）和sql语句对应关系的缓存
	 */
	private static Map DicMapTableNameSql = new HashMap();
	
	public DicMapUtils() {
		instance = this;
	}
	
	public static DicMapUtils getInstance() {
		return instance;
	}
    
	public static Map getDicMapTableNameSql(){
		return DicMapTableNameSql;
	}
	
	public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setTableList(String[] tableList) {
		this.tableList = tableList;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}
	/**
	 * dicConfigFile 字典配置文件, 默认为classpath下的dicconfig.xml
	 */
	public void setDicConfigFile(String dicConfigFile) {
		if (dicConfigFile != null && dicConfigFile.trim().length() > 0) {
			this.dicConfigFile = dicConfigFile.startsWith("/") ? dicConfigFile
					: "/" + dicConfigFile;
		}
	}

	public void setCache() {
		RowMapper rowmapper = new LowcaseColumnRowMapper();
		for(int i=0;i<tableList.length;i++) {
			if(!tableList[i].trim().equals("")){
				cacheOneTable(rowmapper, tableList[i]);	
			};
		}
	
		//dicMapCache.put("permiss", jdbcTemplate.query("SELECT MENUCODE,LINK FROM T_SYS_MENU  ORDER BY SQNUM",
		//		new LowcaseColumnRowMapper()));
		loadDicConfig(dicConfigFile);
	}

	public void cacheOneTable( RowMapper rowmapper, String table) {
		if (table == null || table.trim().length() == 0) {
			return;
		}
		String[] diccfgs = table.split("//");
		String tableName = null;
		String sql = null;
		String keycol = null;
		if (diccfgs.length >= 2) {
			tableName = diccfgs[0];
			sql = "(" + diccfgs[1] + ")";
			if(diccfgs.length == 2){
				keycol  = keyColumn;
			}else{
				keycol = diccfgs[2];
			}
		} else {
			tableName = diccfgs[0];
			sql = tableName;
			keycol = keyColumn;
		}
		//zhangsj add 20170628 字典表名（在数据库中可以不存在）和sql语句对应关系的缓存
		DicMapTableNameSql.put(tableName, sql+"@"+keycol);
		
		List datalist = jdbcTemplate.query("SELECT * FROM " + sql, rowmapper);
		dicMapCache.put(tableName, datalist);
		
		Map tabledata = new LinkedHashMap();
		for(int j=0;j<datalist.size();j++) {
			Map data = (Map) datalist.get(j);
			tabledata.put(data.get(keycol), data);
		}
		dicMapCacheForTranslate.put(tableName, tabledata);
	}

	public static Map getDicMapCache() {
		return dicMapCache;
	}	
	public static Map getDicMapCacheForTranslate() {
		return dicMapCacheForTranslate;
	}
	
	
	public static Map getDicMapTranslate() {
		return dicMapTranslate;
	}

	private void loadDicConfig(String configFile){
		URL url = DicMapUtils.class.getResource(configFile);

		String dicconfigFile = url.getFile();
		
		try {
			dicconfigFile = URLDecoder.decode(dicconfigFile, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("获取SQL映射文件错误！");
			e.printStackTrace();
		}

		File file = new File(dicconfigFile.replaceAll("%20", " "));
		if(file.exists() && file.isFile() && file.canRead()) {
			SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(file);
				Element root = doc.getRootElement();
				Iterator item = root.elementIterator();
				while(item.hasNext()) {
					Element column = (Element) item.next();
					String columnName = column.attributeValue("name");
					String columnTable = column.attributeValue("table");
					String columnKey = column.attributeValue("key");
					if(columnKey==null || columnKey.length()==0) {
						columnKey = columnName;
					}
					String columnText = column.attributeValue("text");
					if(dicMapCacheForTranslate.containsKey(columnTable)) {
						Map datamap = (Map) dicMapCacheForTranslate.get(columnTable);
						//有数据才添加缓存定义
						if(!datamap.isEmpty()) {
							Map dicone = new HashMap();
							dicone.put("table", columnTable);
							dicone.put("key", columnKey);
							dicone.put("text", columnText);							
							dicMapTranslate.put(columnName, dicone);
						}
					}
				}
			} catch (DocumentException e) {
				log.error("读取数据字典映射关系错误！", e);
			}
		}
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Map mapOfColValues = new LinkedHashMap(columnCount);
		List diclist = new LinkedList();
		for (int i = 1; i <= columnCount; i++) {
			//根据数据库中每一列的类型做相应处理
			String columnType = rsmd.getColumnTypeName(i);
			String key = rsmd.getColumnName(i).toLowerCase();
			if(columnType.equalsIgnoreCase("DATE")){
				// 如果是DATE类型，格式化并精确到秒
				String value = DateUtils.DateToString(rs.getTimestamp(i), "yyyy-MM-dd HH:mm:ss");
				mapOfColValues.put(key, value);
			} else if(columnType.equalsIgnoreCase("BLOB")){
				// 如果是BLOB字段就将他的内容编码为Base64的字符串
				if (rs.getBlob(key) != null) {
					OracleLobHandler lobHandler = new OracleLobHandler();
					mapOfColValues.put(key, base64BlankFilter(new BASE64Encoder().encode(lobHandler.getBlobAsBytes(rs, key))));
				} else {
					mapOfColValues.put(key, "");
				}
			} else if(columnType.equalsIgnoreCase("NUMBER")){
				if (rs.getBigDecimal(key) != null) {
					mapOfColValues.put(key, String.valueOf(rs.getBigDecimal(key)));
				} else {
					mapOfColValues.put(key, "");
				}
			} else {
				Object obj = rs.getObject(i);
				mapOfColValues.put(key, obj);
			}
			if(dicMapTranslate.containsKey(key)) {
				diclist.add(key);
			}
		}
		if(!diclist.isEmpty()) {
			for(int i=0;i<diclist.size();i++) {
				String key = (String) diclist.get(i);
				Map coldicMap = (Map) dicMapTranslate.get(key);
				String keycols = (String) coldicMap.get("key");
				String keyValue = "";
				if(keycols.indexOf(",")>0) {
					String[] columns= keycols.split(",");
					boolean haserr = false;
					for(int j=0;j<columns.length;j++) {
						Object tempvalue = mapOfColValues.get(columns[j]);
						if(tempvalue==null) {
							haserr = true;
							continue;
						}
						keyValue += tempvalue + ",";
					}
					if(haserr) {
						continue;
					}
					keyValue = keyValue.substring(0,keyValue.length()-1);
				} else {
					/*
					 * 李洪磊 2008-07-09 修改tempvalue值
					 * 原程序为：Object tempvalue = mapOfColValues.get(keycols);
					 */
					Object tempvalue = mapOfColValues.get(key);
					if(tempvalue==null) {
						/*
						 * 张方俊 2008-06-24 修改：增加翻译不成功的处理
						 */
						mapOfColValues.put(key + "_dicvalue", mapOfColValues.get(key));
						continue;
					}
					keyValue = tempvalue.toString();
				}
				Map data = (Map) ((Map) dicMapCacheForTranslate.get(coldicMap.get("table"))).get(keyValue);
				if(data!=null) {
					String textcol = (String) coldicMap.get("text");
					String[] cols = textcol.split(",");
					for(int colsi = 0;colsi<cols.length;colsi++){
						if(colsi==0){
							mapOfColValues.put(key + "_dicvalue", data.get(cols[0]));
						}else{
							mapOfColValues.put(key + "_dicvalue_" + cols[colsi], data.get(cols[colsi]));
						}
					}
				}
				/*
				 * 张方俊 2008-06-24 修改：增加翻译不成功的处理
				 */
				else{
					mapOfColValues.put(key + "_dicvalue", mapOfColValues.get(key));
				}
			}
		}
		return mapOfColValues;
	}
	
	public static String base64BlankFilter(String _value) {
		if (_value == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < _value.length(); i++) {
			int _c = _value.charAt(i);
			if (_c != 10 && _c != 13) {
				sb.append(_value.substring(i, i + 1));
			}
		}
		return sb.toString();
	}
	
	protected String getColumnKey(String columnName) {
		return columnName.toLowerCase();
	}
	
	private static Calendar calendar = Calendar.getInstance(); 
	
	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
		Object value = rs.getObject(index);
		
		if(value instanceof String) {
			return ((String)value).trim();
		} else {
			if(value instanceof java.util.Date || value instanceof java.sql.Date) {
				Timestamp result = rs.getTimestamp(index);
				calendar.setTime(result);
				if(calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0){
					return value;
				}else{
					return result;
				}
			} else {
				return value;
			}
		}
	}
	

	/**
	 * 张方俊 2008-09-01 增加，为business层提供重新缓存的功能。
	 * 重新缓存指定的表。表名同application.xml中设定的表名，不区分大小写。
	 * 
	 * @param tableName 重新缓存的表名
	 */
	public void reCacheOneTable(String tableName) {
		if (tableName == null) {
			return;
		}
		for (int i = 0; i < tableList.length; i++) {
			String tableCacheString = tableList[i];	
			String _table_name = tableCacheString.indexOf("//") != -1 ? tableCacheString.split("//")[0] : tableCacheString;
			if (tableName.equalsIgnoreCase(_table_name)) {
				RowMapper rowmapper = new LowcaseColumnRowMapper();
				cacheOneTable(rowmapper, tableList[i]);
				return;
			}
		}
	}
	
}