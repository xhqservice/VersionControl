package com.jadlsoft.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @描述：json工具类
 * @作者：吴家旭
 * @时间：May 27, 2013 11:26:49 AM
 */
public class JsonUtil {
	private static Log log = LogFactory.getLog(JsonUtil.class);

	/**
	 * 
	 * @描述：object转json
	 *
	 * @作者：吴家旭
	 *
	 * @时间：May 27, 2013 11:27:36 AM
	 *
	 * @参数：@param obj
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else {
			json.append(bean2json(obj));
		}
		return json.toString();
	}

	/**
	 * 
	 * @描述：bean转json
	 *
	 * @作者：吴家旭
	 *
	 * @时间：May 27, 2013 11:27:12 AM
	 *
	 * @参数：@param bean
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());
					String value = object2json(props[i].getReadMethod().invoke(
							bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * 
	 * @描述：list转json
	 *
	 * @作者：吴家旭
	 *
	 * @时间：May 27, 2013 11:27:48 AM
	 *
	 * @参数：@param list
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 
	 * @描述：数组转json
	 *
	 * @作者：吴家旭
	 *
	 * @时间：May 27, 2013 11:29:13 AM
	 *
	 * @参数：@param array
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 
	 * @描述：map转json
	 *
	 * @作者：吴家旭
	 *
	 * @时间：May 27, 2013 11:29:42 AM
	 *
	 * @参数：@param map
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * 
	 * @描述：set集合转json
	 *
	 * @作者：吴家旭
	 *
	 * @时间：May 27, 2013 11:29:54 AM
	 *
	 * @参数：@param set
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 
	 * @描述：String 转 json
	 *
	 * @作者：吴家旭
	 *
	 * @时间：May 27, 2013 11:30:07 AM
	 *
	 * @参数：@param s
	 * @参数：@return
	 *
	 * @返回值：String
	 */
	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
	public String list2JsonArray(List list){
		JSONArray array = JSONArray.fromObject(list);
		return array.toString();
	}
	public String map2JsonObj(Map map){
		JSONObject obj = JSONObject.fromObject(map);
		return obj.toString();
	}
	

	public  static Map parserToMap(String s){  
	    Map map=new HashMap();  
	    if (s == null || s.equals("")) {
	    	return map;
		}
	    JSONObject json=JSONObject.fromObject(s);  
	    Iterator keys=json.keys();  
	    while(keys.hasNext()){  
	        String key=(String) keys.next();  
	        String value=json.get(key).toString();  
	        if(value.startsWith("{")&&value.endsWith("}")){  
	            map.put(key, parserToMap(value));  
	        }else{  
	            map.put(key, value);  
	        }  
	    }  
	    return map;  
	}  

	public static List parserToList(String s) {
		List list = new ArrayList();
		if (s == null || s.equals("")) {
			return list;
		}
		JSONArray jsonArray = JSONArray.fromObject(s);
		for (Object object : jsonArray) {
			if (object instanceof String) {
				list.add(object);
				continue;
			}
			if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) object;
				String json = jsonObject.toString();
				if (json.startsWith("[") && json.endsWith("]")) {
					//还是数组
					list.add(parserToList(json));
				}else {
					if (json.startsWith("{") && json.endsWith("}")) {
						list.add(parserToMap(json));
					}else {
						list.add(json);
					}
				}
			}
		}
		return list;
	}
}
