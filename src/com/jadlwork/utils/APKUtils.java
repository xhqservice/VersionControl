package com.jadlwork.utils;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.AXmlResourceParser;
import android.util.TypedValue;

import com.jadlsoft.utils.StringUtils;

/**
 * Copyright © 2017京安丹灵. All rights reserved.
 * @类名: APKUtils
 * @作者: lcx
 * @时间: 2017-12-25 下午3:30:17
 */
public class APKUtils {
	
	public static void main(String[] args) {
		String apkUrl = "C:\\Users\\Administrator\\Desktop\\APP控制中心\\apks\\Via.apk";
		Map<String, Object> map = getAPKInfo(apkUrl);
//		System.out.println(map);
	}

	/**
	 * 获取指定apk的versionCode   获取失败的话返回0
	 * @param apkUrl
	 * @return
	 */
	public static int getApkVersionCode(String apkUrl) {
		Map<String, Object> apkInfo = getAPKInfo(apkUrl);
		if (apkInfo == null) {
			return 0;
		}
		Object object = apkInfo.get("versionCode");
		if (object == null) {
			return 0;
		}else {
			return Integer.parseInt(object+"");
		}
	}
	
	/**
	 * 获取指定apk版本号   获取失败的话返回空字符串
	 * @param apkUrl
	 * @return
	 */
	public static String getApkVersionName(String apkUrl) {
		Map<String, Object> apkInfo = getAPKInfo(apkUrl);
		if (apkInfo == null) {
			return "";
		}
		Object object = apkInfo.get("versionName");
		if (object == null) {
			return "";
		}else {
			return object.toString();
		}
	}
	
	/**
	 * 获取apk相关数据，包含：
	 * 		platformBuildVersionCode	--25
	 * 		platformBuildVersionName	--7.1.1
	 * 		package		--com.jadlsoft.jadl_ysgl
	 * 		versionCode		--4
	 * 		versionName		--1.0.3
	 * @param apkUrl
	 * @return
	 */
	public static Map<String, Object> getAPKInfo(String apkUrl) {
		
		if (StringUtils.isEmpty(apkUrl)) {
			return null;
		}
		File apkFile = new File(apkUrl);
		if (!apkFile.isFile()) {
			return null;
		}
		
		ZipFile zipFile;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			zipFile = new ZipFile(apkUrl);
			Enumeration<?> enumeration = zipFile.entries();
			ZipEntry zipEntry = null;
			while (enumeration.hasMoreElements()) {
				zipEntry = (ZipEntry) enumeration.nextElement();
				if (zipEntry.isDirectory()) {

				} else {
					if ("androidmanifest.xml".equals(zipEntry.getName()
							.toLowerCase())) {
						AXmlResourceParser parser = new AXmlResourceParser();
						parser.open(zipFile.getInputStream(zipEntry));
						while (true) {
							int type = parser.next();
							if (type == XmlPullParser.END_DOCUMENT) {
								break;
							}
							String name = parser.getName();
							if (null != name
									&& name.toLowerCase().equals("manifest")) {
								for (int i = 0; i != parser.getAttributeCount(); i++) {
									String value = getAttributeValue(
											parser, i);
									if (value == null) {
										value = "";
									}
									
									map.put(parser.getAttributeName(i), value);
								}
								break;
							}
						}
					}

				}
			}
			zipFile.close();
		} catch (Exception e) {
			map.put("code", "fail");
			map.put("error", "读取apk失败");
		}
		return map;
	}

	private static String getAttributeValue(AXmlResourceParser parser, int index) {
		int type = parser.getAttributeValueType(index);
		int data = parser.getAttributeValueData(index);
		if (type == TypedValue.TYPE_STRING) {
			return parser.getAttributeValue(index);
		}
		if (type == TypedValue.TYPE_ATTRIBUTE) {
			return String.format("?%s%08X", getPackage(data), data);
		}
		if (type == TypedValue.TYPE_REFERENCE) {
			return String.format("@%s%08X", getPackage(data), data);
		}
		if (type == TypedValue.TYPE_FLOAT) {
			return String.valueOf(Float.intBitsToFloat(data));
		}
		if (type == TypedValue.TYPE_INT_HEX) {
			return String.format("0x%08X", data);
		}
		if (type == TypedValue.TYPE_INT_BOOLEAN) {
			return data != 0 ? "true" : "false";
		}
		if (type == TypedValue.TYPE_DIMENSION) {
			return Float.toString(complexToFloat(data))
					+ DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type == TypedValue.TYPE_FRACTION) {
			return Float.toString(complexToFloat(data))
					+ FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
		}
		if (type >= TypedValue.TYPE_FIRST_COLOR_INT
				&& type <= TypedValue.TYPE_LAST_COLOR_INT) {
			return String.format("#%08X", data);
		}
		if (type >= TypedValue.TYPE_FIRST_INT
				&& type <= TypedValue.TYPE_LAST_INT) {
			return String.valueOf(data);
		}
		return String.format("<0x%X, type 0x%02X>", data, type);
	}

	private static String getPackage(int id) {
		if (id >>> 24 == 1) {
			return "android:";
		}
		return "";
	}

	private static float complexToFloat(int complex) {
		return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
	}

	private static final float RADIX_MULTS[] = { 0.00390625F, 3.051758E-005F,
			1.192093E-007F, 4.656613E-010F };
	private static final String DIMENSION_UNITS[] = { "px", "dip", "sp", "pt",
			"in", "mm", "", "" };
	private static final String FRACTION_UNITS[] = { "%", "%p", "", "", "", "",
			"", "" };
}
