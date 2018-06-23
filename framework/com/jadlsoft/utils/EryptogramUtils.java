package com.jadlsoft.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加、解密处理类
 * @author wrc
 * @since  2009-1-7 下午05:10:06
 */
public class EryptogramUtils {
	
	/**
	 * 加密、解密算法
	 */
	private static final String DEFAULT_ALGORITHM = "Blowfish";
	
	/**
	 * 密钥：Blowfish算法密钥的长度为4-56个bytes
	 */
	private static final String DEFAULT_KEY = "maxLen-8";
	
	/**
	 * 密码信息
	 * 算法：Blowfish
	 * 模式：ECB
	 * 填充方式：PKCS5Padding
	 */
	private static final String DEFAULT_CIPHER = "Blowfish/ECB/PKCS5Padding";
	
	/**
	 * 
	 */
	private static Cipher cipher = null;
	
	/**
     * 生成一个对象，加、解密时只需初始化
     */
    static {
		try {
			cipher = Cipher.getInstance(EryptogramUtils.DEFAULT_CIPHER);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * 生成密钥
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    private static SecretKeySpec getSecretKey() {
    	SecretKeySpec sks = null;
    	try {
    		sks = new SecretKeySpec(EryptogramUtils.DEFAULT_KEY.getBytes(DocumentUtils.DEFAULT_ENCODING), EryptogramUtils.DEFAULT_ALGORITHM);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sks;
    }
    
    /**
     * 数据根据密钥进行加密
     * @param  加密前source
     * @return 加密后source
     */
    public static byte[] encrypt(byte[] source) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			return cipher.doFinal(source);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 数据根据密钥进行解密
     * @param  解密前source
     * @return 解密后source
     */
    public static byte[] decrypt(byte[] source) {		
		try {
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		try {
			return cipher.doFinal(source);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
    }
}
