package com.jadlwork.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;

import com.jadlsoft.utils.StringUtils;

/**
 * war包解压/打包的工具类
 * @类名: WarUtils
 * @作者: 李春晓
 * @时间: 2017-2-4 下午5:02:20
 */
public class WarUtils {
	
	private static Logger logger = Logger.getLogger(WarUtils.class);
	
	/**
	 * 解压war包，单个文件出错就跳过
	 * @param warPath  要解压的war包的路径
	 * @param unzipPath 解压到的路径，如果为空就解压到当前目录
	 * @param isCreateRootDir 是否创建根目录
	 * @return: 返回解压后的根目录绝对路径地址
	 */
	public static String unzip(String warPath, String unzipPath, boolean isCreateRootDir) {
		
		File warFile = new File(warPath);
		if (!warFile.isFile()) {
        	logger.error("目标压缩文件不存在！");
        	return null;
		}
		
		//如果目标目录为空，就设置为当前目录
		if (StringUtils.isEmpty(unzipPath)) {
			unzipPath = warFile.getParentFile().getAbsolutePath();
		}
		
        File unzipDir = new File(unzipPath);
        
        if (isCreateRootDir) {
			unzipDir = new File(unzipDir, FileUtils.getPureFilename(warFile));
		}
        
        if (!unzipDir.isDirectory()) {
        	unzipDir.mkdirs();
        	logger.info("目标文件夹不存在，已创建！");
		}
        
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(warFile));
            ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR,
                    bufferedInputStream);

            JarArchiveEntry entry = null;
            long t1 = System.currentTimeMillis();
            logger.info("【war包解压】开始解压"+warFile.getAbsolutePath()+"文件！");
            while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(unzipDir, entry.getName()).mkdir();
                } else {
                	OutputStream out = null;
                	try {
                		out = new FileOutputStream(new File(unzipDir, entry.getName()));
					} catch (FileNotFoundException e) {
						//程序被占用，跳过
						logger.info("解压"+warFile.getName()+"下的"+entry.getName()+"文件时出错，已经跳过");
						continue;
					}
                    IOUtils.copy(in, out);
                    out.close();
                }
            }
            long t2 = System.currentTimeMillis();
            DecimalFormat df = new DecimalFormat("#.0");
            double s = (t2 - t1) / 1000;
    		double ss = (t2 - t1) % 1000;
    		double sss = ss / 1000;            
            double sum = s + sss;
            String time = df.format(sum);
            
            logger.info("【war包解压】解压"+warFile.getAbsolutePath()+"文件完成！共用时长"+time+"秒！");
            in.close();
            return unzipDir.getAbsolutePath();
        } catch (ArchiveException e) {
        	logger.error("不支持的压缩格式");
        	return null;
        } catch (IOException e) {
        	logger.error("文件写入发生错误");
        	return null;
        }
    }

	/**
	 * 压缩war包
	 * @功能: 还未使用到，没做测试
	 * @param destFile
	 * @param zipDir
	 * @return: void
	 */
    private static void zip(String destFile, String zipDir) {
        File outFile = new File(destFile);
        try {
            outFile.createNewFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outFile));
            ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR,
                    bufferedOutputStream);

            if (zipDir.charAt(zipDir.length() - 1) != '/') {
                zipDir += '/';
            }

            File[] listFiles = new File(zipDir).listFiles();
            for (File file : listFiles) {
            	ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getPath().replace(
                        zipDir.replace("/", "\\"), ""));
                out.putArchiveEntry(zipArchiveEntry);
                IOUtils.copy(new FileInputStream(file), out);
                out.closeArchiveEntry();
			}
            out.finish();
            out.close();
        } catch (IOException e) {
            System.err.println("创建文件失败");
        } catch (ArchiveException e) {
            System.err.println("不支持的压缩格式");
        }
    }
}
