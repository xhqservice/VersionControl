package com.jadlsoft.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BeanTest {

	public static String tablename = "test";
	
	public static void main(String[] args){
		try {
			Field field = BeanTest.class.getDeclaredField("tablename");
			System.out.println(field);
			System.out.println(Modifier.toString(field.getModifiers()));
			System.out.println(field.get(null));
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
