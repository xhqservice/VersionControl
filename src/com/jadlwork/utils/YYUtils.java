package com.jadlwork.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;

import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SystemConstants;

public class YYUtils {
	
	private static Logger logger = Logger.getLogger(YYUtils.class);

	/**
	 * 获取应用版本（读取META-INF/MANIFEST.MF文件中Manifest-Version的值）
	 * @param yyRootPtah	应用根目录
	 * @return: String 返回版本，如果文件路径为空或者目录不存在就返回null，如果没有MANIFEST.MS文件或者没有对应字段或者字段为空，就返回默认原始版本1.0
	 */
	public static String getYyVersion(String yyRootPtah) {
		if (StringUtils.isEmpty(yyRootPtah)) {
			return null;
		}
		File rootDir = new File(yyRootPtah);
		if (!rootDir.isDirectory()) {
			return null;
		}
		
		File MANIFESTFile = new File(rootDir.getAbsoluteFile()+File.separator+"META-INF", "MANIFEST.MF");
		if (!MANIFESTFile.isFile()) {
			return SystemConstants.YYVERSION_DEFAULT;			
		}
		
		//读取（这里采用兼容的方式，也就是版本号不一定放在第一行）
		String version = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(MANIFESTFile));
			String readContent = "";
			while((readContent=br.readLine())!=null){
				if (readContent.contains(":")) {
					String[] split = readContent.split(":");
					if (split[0].equals("Manifest-Version")) {
						String verS = split[1];
						if (!StringUtils.isEmpty(verS)) {
							//找到了
							version = split[1].trim();
							break;
						}
					}
				}
			}
			br.close();
			if (StringUtils.isEmpty(version)) {
				return SystemConstants.YYVERSION_DEFAULT;
			}
			return version;
		} catch (Exception e) {
			logger.error("获取应用版本出错！"+e.toString());
			return SystemConstants.YYVERSION_DEFAULT;
		}
	}
}
