package com.jadlwork.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.jadlsoft.utils.StringUtils;
import com.jadlwork.utils.zip.ZipEntry;
import com.jadlwork.utils.zip.ZipFile;
import com.jadlwork.utils.zip.ZipOutputStream;

/**
 * @Title: ZipUtils.java
 * @Description:压缩文件类
 * @Copyright: Copyright (c) 2010
 * @Company: 安丹灵
 * @date 2010-4-21
 * @author zhaohuibin 
 * @version 3.0
 */
public class ZipUtils {

	private static Logger log = Logger.getLogger(ZipUtils.class);

	/**
	 * 解压文件到指定的目录下
	 * 
	 * @param zipFileName
	 *            ZIP文件
	 * @param outputDirectory
	 *            解压的目录
	 * @param isCreateRootDir 是否创建根目录        
	 */
	// modify by zhaohuibin 2009-12-16 解决由于本解压方法使用了ant1.6中的ZipFile方法而websphere6.0下不支持ant1.6包的问题
	public static String decompressZip(String zipFileName, String outputDirectory, boolean isCreateRootDir) {

		File warFile = new File(zipFileName);
		if (!warFile.isFile()) {
			log.error("目标文件不存在！");
			throw new RuntimeException("目标文件不存在！");
		}
		
		//如果目标目录为空，就设置为当前目录
		if (StringUtils.isEmpty(outputDirectory)) {
			outputDirectory = warFile.getParentFile().getAbsolutePath();
		}
        File unzipDir = new File(outputDirectory);
        if (isCreateRootDir) {
			unzipDir = new File(unzipDir, FileUtils.getPureFilename(warFile));
			outputDirectory = unzipDir.getAbsolutePath();
		}
        if (!unzipDir.isDirectory()) {
        	unzipDir.mkdirs();
		}

		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFileName, "gb2312");
			java.util.Enumeration e = zipFile.getEntries();
			ZipEntry zipEntry = null;
			while (e.hasMoreElements()) {
				zipEntry = (ZipEntry) e.nextElement();
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f1 = new File(parseFileSeperator(outputDirectory));
					f1.mkdir();
					File f = new File(parseFileSeperator(outputDirectory) + name);
					f.mkdir();
				} else {
					String filepath = parseFileSeperator(outputDirectory) + zipEntry.getName();
					File f = new File(filepath);
					File dir = new File(f.getParent());
					InputStream in = null;
					FileOutputStream out = null;
					if (!dir.exists()) {
						dir.mkdirs();
					}
					try {
						f.createNewFile();
						in = zipFile.getInputStream(zipEntry);
						out = new FileOutputStream(f);
						int c;
						byte[] by = new byte[1024];
						while ((c = in.read(by)) != -1) {
							out.write(by, 0, c);
						}
					} catch (ZipException e1) {
						log.error("解压文件失败。文件名：" + zipFileName + "。错误：" + e1.getMessage());
						throw new RuntimeException(e1);
					} catch (IOException e1) {
						log.error("解压文件失败。文件名：" + zipFileName + "。错误：" + e1.getMessage());
						throw new RuntimeException(e1);
					} finally {
						try {
							if (out != null) {
								out.flush();
								out.close();
							}
							if (in != null) {
								in.close();
							}
						} catch (IOException e1) {
							log.error("解压文件关闭文件流失败。文件名：" + zipFileName + "。错误：" + e1.getMessage());
							throw new RuntimeException(e1);
						}
					}
				}
			}
		} catch (IOException e1) {
			log.error("解压文件失败。文件名：" + zipFileName + "。错误：" + e1.getMessage());
			throw new RuntimeException(e1);
		} finally {
			try {
				if (zipFile != null) {
					zipFile.close();
				}
			} catch (IOException e) {
				log.error("解压文件关闭文件流失败。文件名：" + zipFileName + "。错误：" + e.getMessage());
				throw new RuntimeException(e);
			}
		}
		return outputDirectory;
	}

	/**
	 * 压缩文件
	 * @param fromPath 需压缩文件所在的路径（只是路径）
	 * @param toPath 压缩后压缩文件的存放路径（包含压缩文件的文件名）
	 */
	public static void compressZip(String fromPath, String toPath) {
		ZipOutputStream out = null;
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(toFile);
			out = new ZipOutputStream(new BufferedOutputStream(fos));
			fromPath = fromFile.getAbsolutePath() + "\\";
			makeZip(fromFile, out, fromPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void makeZip(File fromFile, ZipOutputStream out, String dir) {
		try {
			File[] fileList = fromFile.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				File _f = fileList[i];
				if (_f.isDirectory()) {
					makeZip(_f, out, dir);
				} else {
					String path = _f.getAbsolutePath();
					path = getAbsolutePath(path, dir);
					writeEntry(path, _f, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getAbsolutePath(String strPath, String dir)
			throws UnsupportedEncodingException {
		return strPath.substring(dir.length(), strPath.length());
	}

	private static final int BUFFER = 2048;

	private static void writeEntry(String EntryName, File file, ZipOutputStream out) {
		ZipEntry entry = new ZipEntry(EntryName);
		FileInputStream fi = null;
		BufferedInputStream origin = null;
		try {
			out.putNextEntry(entry);
			fi = new FileInputStream(file);
			origin = new BufferedInputStream(fi, BUFFER);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (origin != null)
					origin.close();
				if (fi != null)
					fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 压缩war包	--使用上面的压缩方式发现在Tomcat启动时候会出现问题
	 * @param destFile	目标文件
	 * @param zipDir	待压缩目录
	 */
	public static void war(String destFile, String zipDir) {
		File outFile = new File(destFile);
		try {
			outFile.createNewFile();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					new FileOutputStream(outFile));
			ArchiveOutputStream out = new ArchiveStreamFactory()
					.createArchiveOutputStream(ArchiveStreamFactory.JAR,
							bufferedOutputStream);

			if (zipDir.charAt(zipDir.length() - 1) != '/') {
				zipDir += '/';
			}

			Iterator<File> files = org.apache.commons.io.FileUtils.iterateFiles(new File(zipDir),
					null, true);
			while (files.hasNext()) {
				File file = files.next();
				ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file,
						file.getPath().replace(zipDir.replace("/", "\\"), ""));
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
	

	/**
	 * 检查文件路径是否以“/”结尾，如果没有则增加“/”
	 * 
	 * @param dir
	 * @return
	 */
	private static String parseFileSeperator(String dir) {
		if (dir == null || dir.equals("")) {
			return "";
		}
		return dir.endsWith("/") ? dir : (dir + "/");
	}
}
