package com.jadlwork.utils;

import java.lang.reflect.Field;
import java.util.TimerTask;

import org.apache.log4j.Logger;

/**
 * 
 * 定时器工具类
 * @author wujiaxu
 * @Time 2017-6-22 上午10:14:47
 *
 */
public class TimerUtils {

	private static Logger log = Logger.getLogger(TimerUtils.class);
	
	/**
	 * 重置定时任务运行时间
	 * @param obj
	 * @param re_time
	 * @return
	 * @author wujiaxu
	 * @Time 2017-6-22 上午10:15:52
	 */
	public static boolean re_schedule(Object task, int re_time) {  
        return setDeclaredField(TimerTask.class, task, "period", re_time);  
    }  
    
	/**
	 * 重写定时器方法
	 * @参数：@return
	 * @返回值：boolean
	 */
    static boolean setDeclaredField(Class<?> clazz, Object obj,  
            String name, Object value) {  
        try {  
            Field field = clazz.getDeclaredField(name);  
            field.setAccessible(true);  
            field.set(obj, value);  
            return true;  
        } catch (Exception e) {  
        	log.error("重写定时器失败！", e);
            return false;  
        }  
    }  
}
