package com.jadlwork.utils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * bean对象操作工具类
 * @类名: BeanUtils
 * @作者: 李春晓
 * @时间: 2017-2-20 下午3:13:20
 */
public class BeanUtils {

	/**
	 * 将map转为对应的bean对象
	 * @param beanClass	要转成的bean对象的class类型
	 * @param map	待转的map数据
	 * @return: Object 转成的bean对象
	 */
	public static Object populate(Class beanClass, Map map){
		Object result = null;
		try {
			result = beanClass.newInstance();
			//得到所有的字段
			Field[] declaredFields = beanClass.getDeclaredFields();	
			for (Field field : declaredFields) {
				String fieldName = field.getName();
				//判断字段是否在map中
				if (map.keySet().contains(fieldName)) {
					//使用field的set方法来执行属性的set方法，使用pd获取属性的get方法
					field.setAccessible(true);
					//获取map中的值并设置					TODO:涉及到类型转换，这里就是时间的问题，暂时设置
					if (field.getType().newInstance() instanceof Date) {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Object object = map.get(fieldName);
						if (object != null) {
							Date parse = df.parse((String) object);
							field.set(result, parse);
						}
					}else {
						field.set(result, map.get(fieldName));
					}
				}
			}
			return result;
		} catch (Exception e) {
			return result;
		}
	}
}
