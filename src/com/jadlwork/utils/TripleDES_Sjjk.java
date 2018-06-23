package com.jadlwork.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3DES加密
 * 
 * @author 张方俊
 * 
 */
public class TripleDES_Sjjk {
	
	private static Cipher TripleDES_cp;
	
	private static Cipher TripleDES_cp_decode;
	
	static {
		/*
		 * 2008年4月24日。发现之前的一个重大BUG：之前TripleDES_cp的INIT方法是在ENCODE（）方法体中进行的，
		 * 当多个线程同时执行时，导致TripleDES_cp被多次初始化，从而抛出异常。
		 */
		String ori_key = generateKey();
		byte[] tmp = new byte[24];		
		tmp = ori_key.getBytes();		
		SecretKey k = new SecretKeySpec(tmp, "DESede");
		
		try {
			TripleDES_cp = Cipher.getInstance("DESede");
			TripleDES_cp.init(Cipher.ENCRYPT_MODE, k);
			TripleDES_cp_decode = Cipher.getInstance("DESede");
			TripleDES_cp_decode.init(Cipher.DECRYPT_MODE, k);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e)
		{
			e.printStackTrace();
		}
	}
	
		
	private TripleDES_Sjjk() {}
	
	/**
	 * 密钥生成方法。如需要用其他的密钥生成方法，子类覆盖此方法即可。
	 * 注：密钥为24位字符串
	 * @return 密钥
	 */
	protected static String generateKey(){
		/*
		String year = ""+Calendar.getInstance().get(Calendar.YEAR);
		String month = ""+(Calendar.getInstance().get(Calendar.MONTH)+1);
		if(month.length() == 1){
			month = "0" + month;
		}
		String day = ""+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if(day.length() == 1){
			day = "0" + day;
		}
		//由当前的年月日生成密钥，规则为：MMYYDDYY YYDDMMYY MMYYDDYY,分开的YY按顺序代表年的前、后两位
		return (month + year.substring(0,2) + day + year.substring(2,4) + year.substring(0,2) + day + month + year.substring(2,4) + month + year.substring(0,2) + day + year.substring(2,4));
		*/
		return "MBSCJYJKXTHZCMISMBSCJYJK";
	}
	
	/**
	 * 对指定的字符串加密
	 * @param waitforencode : 需要加密的字符串
	 * @return byte数组
	 */
	public static final byte[] encode(String waitforencode) {
		
		if (waitforencode == null || waitforencode.equals("")) {
			return null;
		}
			
		try {				
						
			byte[] waitforencode_bytes = waitforencode.getBytes("iso8859_1");
			//byte[] waitforencode_bytes = waitforencode.getBytes();			
			
			byte[] encode_bytes = TripleDES_cp.doFinal(waitforencode_bytes);
			
			return encode_bytes;
			
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} /**/catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		} 
	}
public static final byte[] encode(String pre, String crc, String use) {		
		
		try {						
			byte[] b1 = pre.getBytes();
			byte[] b2 = crc.getBytes("iso8859_1");
			byte[] b3 = use.getBytes();
			byte[] b  = new byte[b1.length+b2.length+b3.length];
			int i = 0;
			for(;i<b1.length;i++){
				b[i] = b1[i];
			}
			for(;i<(b1.length+b2.length);i++){
				b[i] = b2[i-b1.length];
			}
			for(;i<(b1.length+b2.length+b3.length);i++){
				b[i] = b3[i-b1.length-b2.length];
			}	
			
			byte[] encode_bytes = TripleDES_cp.doFinal(b);
			
			return encode_bytes;
			
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} /**/catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		} 
	}
	

	/**
	 * 对byte数组解密
	 * @param waitfordecode ：待解密的数组
	 * @return 解密过后的字符串
	 */
	public static final String decode(byte[] waitfordecode) {
		
		if (waitfordecode == null || waitfordecode.length == 0) {
			return null;
		}	
		
		try {	
			byte[] decode_bytes = TripleDES_cp_decode.doFinal(waitfordecode);
			return new String(decode_bytes);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		} 
	}
	/**/
	public static void main(String[] args){
		System.out.println("de_Base64 = "+decode(Base64.decode("+KXXhI0wOcIpInoT+2Ytag==")));
		String str = "world cup final game : france vs italy";		
		
		String d_str = decode(encode(str));
		System.out.println("str = "+str);
		System.out.println("str.len = "+str.length());

		System.out.println("d_str = "+d_str);
		String base = Base64.encode(encode("123456"));
		System.out.println("Base64 = "+base + "       len = "+base.length());
		System.out.println("de_Base64 = "+decode(Base64.decode("MEhJIyhruIetyNsEeatu/pPuWqabgncp")));
		System.out.println("de_Base64.len = "+decode(Base64.decode(base)).length());
	}
	
}
