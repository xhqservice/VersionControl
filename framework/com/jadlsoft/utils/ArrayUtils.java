package com.jadlsoft.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: MakeArray.java</p>
 * <p>Description: 利用按照指定分割符的字符串生成一维数组</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 京安丹灵</p>
 * @author Zong
 * @version 1.0
 */

public class ArrayUtils {

    public ArrayUtils() {
    }

    public static List string2list(String[] str){
    	List list = new ArrayList();
    	for(int i=0;i<str.length;i++){
    		list.add(str[i]);
    	}
    	return list;
    }
    /**
     * 将指定的字符串按照默认的分隔符分割成字符串数组.
     * @param arrayString:要分割的字符串,以[,]分割的一串字符.如果没有[,],则将原来的字符串作为一个元素,返回一个长度为1的字符串数组.
     * @exception
     */
    public static String[] getStringArray(String arrayString) {
        return arrayString.split(",");
    }

    /**
     * 将指定的字符串按照指定的分隔符分割成字符串数组.
     * @param arrayString:要分割的字符串,以某种分隔符分割的一串字符.如果没有指定的分隔符,则将原来的字符串作为一个元素,返回一个长度为1的字符串数组.
     * @exception
     */
    public static String[] getStringArray(String arrayString, String splitString) {
        return arrayString.split(splitString);
    }

    /**
     * 将指定的字符串按照指定的分隔符分割成整型数组.
     * @param arrayString:要分割的字符串,以[,]分割的一串字符.如果没有指定的分隔符,则将原来的字符串作为一个元素,返回一个长度为1的整型数组.
     * @exception
     */
    public static int[] getIntArray(String arrayString) {
        return getIntArray(arrayString, ",");
    }

    /**
     * 将指定的字符串按照指定的分隔符分割成整型数组
     * @param arrayString:要分割的字符串,以某种分隔符分割的一串字符.如果没有指定的分隔符,则将原来的字符串作为一个元素,返回一个长度为1的整型数组.
     * @exception
     */
    public static int[] getIntArray(String arrayString, String splitString) {
        int[] tempArray1;
        String[] tempArray2;
        tempArray2 = arrayString.split(splitString);
        tempArray1 = new int[tempArray2.length];
        for (int i = 0; i < tempArray2.length; i++)
            try {
                tempArray1[i] = Integer.parseInt(tempArray2[i]);
            } catch (Exception ex) {
                tempArray1[i] = 0;
            }
        return tempArray1;
    }
    
    public static boolean containValue(String[] values, String value) {
		if(values==null || value==null)
			return false;
		
		for(int i=0;i<values.length;i++)
			if(values[i].equalsIgnoreCase(value))
				return true;
		
		return false;
	}
    
    public static String join(String[] values, String split) {
    	StringBuffer result = new StringBuffer("");
    	for(int i=0;i<values.length;i++) {
    		result.append(values[i]).append(split);
    	}
    	if(result.length()>0) {
    		result.delete(result.length()-split.length(),result.length());
    	}
    	return result.toString();
    }
    
    public static Map arrayToMap(Object[] data) {
    	if(data==null || data.length % 2>0) {
    		return null;
    	} else {
    		Map map = new HashMap();
    		for(int i=0;i<data.length;i+=2) {
    			map.put(data[i], data[i+1]);
    		}
    		return map;
    	}
    }
    /**
     * 判断code是否在contain容器中
     * @param contain 容器
     * @param code 指定值
     * @param fullMatch 是否完全匹配
     * @return
     */
    public static boolean isContain(String[] contain , String code , boolean fullMatch){
    	if(contain == null){
    		return false;
    	}
    	for(int i=0;i<contain.length;i++){
    		if(fullMatch && contain[i].equals(code)){
    			return true;
    		}
    		if(contain[i].equalsIgnoreCase(code)){
    			return true;
    		}    		
    	}
    	return false;
    }
}
