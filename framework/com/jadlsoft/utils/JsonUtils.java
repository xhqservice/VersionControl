package com.jadlsoft.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * @deprecated:json工具类
 * @author:��邱嘉 2013年3月27日10:30:57
 *
 */
public class JsonUtils {
	/**
	 * @author shihongbo 2012-05-18
	 * @Description 把JsonArray转换成List<Map>
	 */
	public static List getList4Json(String JsonArray) {
		List list = new ArrayList();
		Map map = new HashMap();
		if (JsonArray != null && !JsonArray.equals("")) {
			Object obj = JSONValue.parse(JsonArray);
			JSONArray jsonArray = (JSONArray) obj;
			for (int i = 0; i < jsonArray.size(); i++) {
				map = getMap4Json(jsonArray.get(i).toString());
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * @author shihongbo 2012-05-15
	 * @Description ��jsonObjec把jsonObject转换成map
	 */
	public static Map getMap4Json(String jsonString) {
		Object obj = JSONValue.parse(jsonString);
		JSONObject jsonObject = (JSONObject) obj;
		Iterator keyIter = jsonObject.keySet().iterator();
		String key;
		Object value;
		Map valueMap = new HashMap();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}
	/**
	 * @Description 把map格式的数据转成jsonObject
	 * @param map
	 *            <String,String>
	 * @return
	 */
	public static JSONObject getJson4Map(Map map) {
		JSONObject jsonObject = new JSONObject();
		if (map != null && !map.isEmpty()) {
			Iterator keyIter = map.keySet().iterator();
			String key;
			Object value;
			while (keyIter.hasNext()) {
				key = (String) keyIter.next();
				if (map.get(key) instanceof java.util.Date) {
					String format = "yyyy-MM-dd HH:mm:ss";
					Date date = (Date) map.get(key);
					value = getDatatoString(date, format);
				} else {
					value = map.get(key);
				}
				jsonObject.put(key, value);
			}
		}
		return jsonObject;
	}
	/**
	 * @author shihongbo 2012-05-17
	 * @Description 这边的DateUtils包里没有这个转fulldate的方法。所以把它放这里了
	 * @param date
	 * @param format
	 * @return
	 */
	private static String getDatatoString(Date date, String format) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat(format).format(date);
	}
}
