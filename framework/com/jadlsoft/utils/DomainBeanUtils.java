/**
 * <p>Title: DomainBeanUtils </p>
 * <p>Description: 域对象的BeanUtils，用于获取和绑定属性 </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @author 李帮贵
 * @version 1.0
 * 2006-9-7
*/

package com.jadlsoft.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author libanggui
 *
 */
public class DomainBeanUtils {
	
	private static Logger log = Logger.getLogger(DomainBeanUtils.class);
	
	/**
	 * Description: 转换首字母大写，用于获取getter/setter
	 * @param str :需要转换的字符串
	 * @return 转换后的字符串
	 * 2006-9-7
	 */
	private static String upFirstChar(String str) {
	    String first = (str.substring(0, 1)).toUpperCase();
	    String other = str.substring(1);
	    return first + other;
	}
	
	/**
	 * Description: 判断域是否可访问 
	 * @param field 域
	 * @return	T/F
	 * 2006-9-13
	 */
	private static boolean isFieldAccessible(Field field) {
		if(Modifier.isPublic(field.getModifiers())) {
			Class clazz = field.getDeclaringClass();
			return Modifier.isPublic(clazz.getModifiers());
		}
		return false;
	}
	
	/**
	 * Description: 判断方法是否可以访问
	 * @param method 方法
	 * @return T/F
	 * 2006-9-13
	 */
	private static boolean isMethodAccessible(Method method) {
		if(Modifier.isPublic(method.getModifiers())) {
			Class clazz = method.getDeclaringClass();
			return Modifier.isPublic(clazz.getModifiers());
		}
		return false;
	}
	
	/**
	 * Description: 获取类可访问的属性列表
	 * @param clazz 类
	 * @return 属性字符串数组
	 * 2006-9-14
	 */
	public static Object getPropertyValue(Object bean, String property) {
		try {
			Field field = bean.getClass().getDeclaredField(property);
			if(isFieldAccessible(field)) {
				return field.get(bean);
			} else {
				Method method = bean.getClass().getDeclaredMethod("get"+upFirstChar(field.getName()),new Class[]{});
				if (isMethodAccessible(method)) {
					return method.invoke(bean, (Object[])null);
				}
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.debug("对象【" + bean.getClass().getName() + "】" + bean.toString() 
						+ "属性【" + property + "】获取错误", e);
			}
		}
		return null;
	}
	
	public static Object getStaticPropertyValue(Class clazz, String property) {
		try {
			Field field = clazz.getDeclaredField(property);
			if(Modifier.isStatic(field.getModifiers()) && isFieldAccessible(field)) {
				return field.get(null);
			} else {
				Method method = clazz.getDeclaredMethod("get"+upFirstChar(field.getName()),new Class[]{});
				if (Modifier.isStatic(method.getModifiers()) && isMethodAccessible(method)) {
					return method.invoke(null, (Object[])null);
				}
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.debug("类【" + clazz.getName() + "】属性【" + property + "】获取错误", e);
			}
		}
		return null;
	}
	
	/**
	 * Description: 获取类可访问的属性列表
	 * @param clazz 类
	 * @return 属性字符串数组
	 * 2006-9-14
	 */
	public static String[] getProperties(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if(fields.length<=0)
			return null;
		
		String[] properties = new String[fields.length];
		for(int i=0;i<fields.length;i++) {
			try {
				if(isFieldAccessible(fields[i])) {
					properties[i] = fields[i].getName();
				} else {
					Method method = clazz.getDeclaredMethod("get"+upFirstChar(fields[i].getName()),null);
					if (isMethodAccessible(method))
						properties[i] = fields[i].getName();
					else
						properties[i] = "";
				}
			} catch (Exception e) {
				properties[i] = "";
			}
		}
		
		if(log.isDebugEnabled()) log.debug("获取对象属性名称列表：" + clazz.getName());
		return properties;
	}
	
	/**
	 * Description: 获取对象可访问的属性列表
	 * @param bean 对象
	 * @return 属性字符串数组
	 * 2006-9-7
	 */
	public static String[] getProperties(Object bean) {
		Class clazz = bean.getClass();
		
		return getProperties(clazz);
	}
	
	/**
	 * Description: 获取类属性Map（Name/Class）
	 * @param clazz 类
	 * @return 属性Map
	 * 2006-9-14
	 */
	public static Map getPropertiesClassMap(Class clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if(fields.length<=0)
			return null;
		
		Map properties = new HashMap();
		for(int i=0;i<fields.length;i++) {
			try {
				if(isFieldAccessible(fields[i])) {
					properties.put(fields[i].getName(),fields[i].getType());
				} else {
					Method method = clazz.getDeclaredMethod("get"+upFirstChar(fields[i].getName()),new Class[]{});
					if (isMethodAccessible(method)) {
						properties.put(fields[i].getName(),method.getReturnType());
					}
				}
			} catch (Exception e) {
				log.error("获取对象属性【" + fields[i].getName() + "】失败！", e);
			}
		}
		
		if(log.isDebugEnabled()) log.debug("获取对象属性/类列表：" + clazz.getName());
		return properties;
	}
	
	/**
	 * Description: 获取对象属性Map（Name/Class）
	 * @param bean 对象
	 * @return 属性Map
	 * 2006-9-14
	 */
	public static Map getPropertiesClassMap(Object bean) {
		Class clazz = bean.getClass();
		return getPropertiesClassMap(clazz);
	}
	
	/**
	 * Description: 获取对象属性Map（Name/Value）
	 * @param bean 对象
	 * @return 属性Map
	 * 2006-9-13
	 */
	public static Map getPropertiesMap(Object bean) {
		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if(fields.length<=0)
			return null;
		
		Map properties = new HashMap();
		for(int i=0;i<fields.length;i++) {
			try {
				if(isFieldAccessible(fields[i])) {
					properties.put(fields[i].getName(),fields[i].get(bean));
				} else {
					Method method = clazz.getDeclaredMethod("get"+upFirstChar(fields[i].getName()),new Class[]{});
					if (isMethodAccessible(method)) {
						properties.put(fields[i].getName(),method.invoke(bean, null));
					}
				}
			} catch (Exception e) {
				log.error("获取对象属性【" + fields[i].getName() + "】失败！", e);
			}
		}
		
		if(log.isDebugEnabled()) log.debug("获取对象属性/值列表：" + bean.getClass().getName());
		return properties;
	}
	
	/**
	 * Description: 获取对象属性Map（Name/Value）
	 * @param bean 对象
	 * @return 属性Map
	 * 2006-9-13
	 */
	public static Map getPropertiesMap(Object bean, String valuefields) {
		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if(fields.length<=0)
			return null;
		
		valuefields = "," + valuefields + ",";
		
		Map properties = new HashMap();
		for(int i=0;i<fields.length;i++) {
			if(valuefields.indexOf("," + fields[i].getName() + ",")>=0) {
				try {
					if(isFieldAccessible(fields[i])) {
						properties.put(fields[i].getName(),fields[i].get(bean));
					} else {
						Method method = clazz.getDeclaredMethod("get"+upFirstChar(fields[i].getName()),new Class[]{});
						if (isMethodAccessible(method)) {
							properties.put(fields[i].getName(),method.invoke(bean, null));
						}
					}
				} catch (Exception e) {
					log.error("获取对象属性【" + fields[i].getName() + "】失败！", e);
				}
			}
		}
		if(log.isDebugEnabled()) log.debug("获取对象属性/值列表：" + bean.getClass().getName());
		return properties;
	}
	
	/**
	 * Description: 设置对象单个属性 
	 * @param bean 对象
	 * @param property 属性名称
	 * @param value 属性值
	 * 2006-9-13
	 */
	public static Object setProperty(Object bean, String property, Object value) {
		Class clazz = bean.getClass();
		try {
			Field field = clazz.getDeclaredField(property);
			if (isFieldAccessible(field)) {
				field.set(bean, value);
			} else {
				Class valueClazz = field.getType(); //value.getClass();
				Object realValue;
				if (valueClazz==Integer.class) {
					valueClazz=int.class;
				}
				if(valueClazz == int.class) {
					realValue = new Integer(((Number)value).intValue());
				} else {
					realValue = value;
				}
				Method method = null;
				try {
					method = clazz.getDeclaredMethod("set"+upFirstChar(field.getName()),new Class[]{valueClazz});
				} catch(NoSuchMethodException ex) {
					if(valueClazz==Boolean.class || valueClazz==boolean.class) {
						method = clazz.getDeclaredMethod("is"+upFirstChar(field.getName()),new Class[]{valueClazz});
					} else {
						throw ex;
					}
				}
				if (isMethodAccessible(method)) {
					method.invoke(bean, new Object[]{realValue});
				}
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.debug("设置对象属性【" + property + "】为【" 
					+ (value==null?"null":value.toString()) + "】失败！", e);
			}
		}
		return bean;
	}
	
	/**
	 * Description: 绑定对象属性，不覆盖，不区分大小写 
	 * @param bean 对象
	 * @param properties 要绑定的属性Map
	 * 2006-9-13
	 */
	public static Object populate(Object bean, Map properties) {
		if(bean==null || properties==null)
			return bean;
		
		String[] fields = getProperties(bean);
		Set keys = properties.keySet();
		Iterator item = keys.iterator();
		while(item.hasNext()) {
			Object key = item.next();
			if (key instanceof String){
				for(int i=0;i<fields.length;i++) {
					if(fields[i].equalsIgnoreCase((String)key)) {
						setProperty(bean, fields[i], properties.get(key));
						break;
					}
				}
			}
		}
		if(log.isDebugEnabled()) {
			log.debug("设置对象属性列表：" + bean.getClass().getName());
		}
		return bean;
	}

	/**
	 * Description: 用fromBean中与toBean同名字段设置toBean中的字段值
	 * @param toBean 被填充的bean
	 * @param fromBean 填充的bean
	 * 2006-9-19
	 */
	public static Object copyBeanProperties(Object toBean, Object fromBean) {
		Class clazz = fromBean.getClass();
		Map properties = new HashMap();
		Field [] fields = clazz.getDeclaredFields();
		try {
			for (int i=0;i<fields.length;i++){
				properties.put(fields[i].getName(), clazz.getDeclaredMethod("get" + upFirstChar(fields[i].getName()), new Class[]{}).invoke(fromBean, null));
			}
		} catch (Exception e) {
			log.debug("设置对象错误：从" + fromBean.getClass().getName() + "到" + toBean.getClass().getName());
		}
		return DomainBeanUtils.populate(toBean, properties);
	}
}
