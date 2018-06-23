package com.jadlwork.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * BASE64的标准编码
 * 
 * @author 张方俊
 */
public class Base64 {
	
	private static final BASE64Encoder bASE64Encoder = new BASE64Encoder();
		
	private Base64(){} 
	 	
	/**
	 * 编码
	 * 
	 * @param waitforencode ： byte数组
	 * @return 编号过后的字符串
	 */
	public static final String encode(byte[] waitforencode) {
		if (waitforencode == null)
			return null;
		return bASE64Encoder.encode(waitforencode);
	} 

	/**
	 * 解码
	 * 
	 * @param waitfordecode 待解码的字符串
	 * @return byte数组
	 */		
	public static final byte[] decode(String waitfordecode) {
		if (waitfordecode == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			return decoder.decodeBuffer(waitfordecode);
		} catch (Exception e) {
			return null;
		}
	}
		
}
