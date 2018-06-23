package com.jadlwork.utils;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.aspectj.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.jadlsoft.utils.StringUtils;

/**
 * 文件相关的工具类
 * @类名: FileUtils
 * @描述: 完成文件相关的操作，如通过Tomcat的日志名获取日志文件
 * @作者: 李春晓
 * @时间: 2016-12-29 下午2:29:17
 */
public class FileUtils {
	  private static Logger logger = Logger.getLogger(FileUtils.class);
	
	
	
	 /**
     * 删除文件
     * @参数：@param f
     * @返回值：void
     */
    public static void delete(File f) {
        if (f != null && f.exists()) {
            if (f.isDirectory()) {
                File files[] = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    delete(files[i]);
                }
            }
            if (!f.delete()) {
                logger.debug("Cannot delete " + f.getAbsolutePath());
            }
        } else {
            logger.debug(f + " does not exist");
        }
    }

    
    /**
	 * @函数名：writeFile
	 * @功能：写文件
	 * @param filePath
	 *            包含文件名的文件路径
	 * @param content
	 *            文件内容
	 * @param encode
	 *            编码格式 void
	 */

	public static void writeFile(String filePath, String content, String encode) {
		try {
			FileOutputStream o = new FileOutputStream(filePath);
			o.write(content.getBytes(encode));
			o.close();
		} catch (FileNotFoundException fne) {
			logger.error("未找到相关文件！FileUtils.writeFile", fne);
		} catch (IOException ioe) {
			logger.error("写文件内容异常！FileUtils.writeFile", ioe);
		}
	}
	/**
     *  新建目录
     *  @param  folderPath  String  如  c:/test
     *  @return  boolean
     */
    public static void newFolder(String folderPath) {
        try {
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.mkdir();      
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	/**
	 * @函数名：addFileContent
	 * @功能：给文件追加数据
	 * @param context
	 *            追加内容
	 * @param fileName
	 *            要追加内容的文件 void
	 */
	public static void addFileContent(String context, String fileName) {
		RandomAccessFile rfile = null;
		try {
			File file = new File(fileName);
			// 追加文件内容
			rfile = new RandomAccessFile(file, "rw");
			rfile.seek(rfile.length());
			rfile.write(context.getBytes());
			rfile.write(lineSeparator.getBytes());
		} catch (Exception e) {
			logger.error("给文件追加数据异常！FileUtils.addFileContent", e);
		} finally {
			if(rfile != null){
				try {
					rfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static String lineSeparator = (String) java.security.AccessController.doPrivileged(  
			new sun.security.action.GetPropertyAction("line.separator"));
	
	public static void addFileContent(byte[] context, String fileName) {
		RandomAccessFile rfile = null;
		try {
			File file = new File(fileName);
			// 追加文件内容
			rfile = new RandomAccessFile(file, "rw");
			rfile.seek(rfile.length());
			rfile.write(context);
			rfile.write(lineSeparator.getBytes());
		} catch (Exception e) {
			logger.error("给文件追加数据异常！FileUtils.addFileContent", e);
		} finally {
			if(rfile != null){
				try {
					rfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @函数名：writeFile
	 * @功能：将二进制数据写入指定文件
	 * @param filePath
	 *            包含文件名的文件路径
	 * @param bytes
	 *            二进制数据 void
	 */
	public static void writeFile(String filePath, byte[] bytes) {
		File file = new File(filePath);
		OutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			bufferedOutputStream.write(bytes);
			bufferedOutputStream.flush();
		} catch (FileNotFoundException e) {
			logger.error("未找到相关二进制文件！FileUtils.writeFile", e);
		} catch (IOException e) {
			logger.error("写二进制文件异常！FileUtils.writeFile", e);
		} finally {
			try {
				if (bufferedOutputStream != null) {
					bufferedOutputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @函数名：moveFile
	 * @功能：移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/test.txt
	 * @param newPath
	 *            String 如：d:/test.txt
	 * @param type
	 *            String 移动类型，create新建(改变文件创建时间)、move移动(不改变文件创建时间)
	 */
	public static void moveFile(String oldPath, String newPath, String type) {
		if (type.equalsIgnoreCase("create")) {
			copyFile(oldPath, newPath);
			delFile(oldPath);
		} else if (type.equalsIgnoreCase("move")) {
			File tmp = new File(oldPath);
			int num = 0;
			File newFile = new File(newPath);
			File newFileBak = new File(newPath);
			while (newFileBak.exists() && newFileBak.isFile()) {
				newFileBak = new File(newPath + ++num);
			}
			newFile.renameTo(newFileBak);
			tmp.renameTo(newFile);
		}
	}

	/**
	 * @函数名：copyFile
	 * @功能：复制单个文件
	 * @param oldPath
	 *            String 原文件路径 如：c:/test.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/test.txt void
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			logger.error("复制单个文件操作出错！FileUtils.copyFile", e);
			return false;
		}
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/test.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();

		} catch (Exception e) {
			logger.error("删除文件操作出错！FileUtils.copyFile", e);
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/test
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]); // 先删除文件夹里面的文件
				delFolder(path + File.separator + tempList[i]); // 再删除空文件夹
			}
		}
	}
	
	 /**
     *  判断指定目录的文件是否存在
     *  @param  filePath  String  如：c:/test.txt
     */
    public static boolean isFileExists(String filePath) {
    	try {
        	File file = new File(filePath);
            if (file.exists())
            	return true;
            else
            	return false;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/test
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			logger.error("删除文件夹操作出错！FileUtils.copyFile", e);
		}
	}

	/**
	 * @函数名：readXMLFile
	 * @功能：读取XML文件
	 * @param filePath
	 *            包含文件名的文件路径
	 * @param columns
	 *            需解析的字段名数组
	 * @return List
	 */
	public static List readXMLFile(String filePath, String[] columns) {
		SAXReader saxReader = new SAXReader();
		List list = new ArrayList();
		try {
			Document doc = saxReader.read(filePath);
			Element root = doc.getRootElement();
			List listTemp = root.elements();
			for (int i = 0; i < listTemp.size(); i++) {
				Element item = (Element) listTemp.get(i);
				Map map = new HashMap();
				for (int j = 0; j < columns.length; j++) {
					String column = columns[j];
					// System.out.println(item.attribute(column).getText());
					map.put(column, item.attribute(column).getData());
				}

				list.add(map);
			}

			return list;
		} catch (Exception e) {
			logger.error("解析XML文件异常！", e);
		}

		return list;
	}

	/**
	 * @函数名：writeXML
	 * @功能：写XML文件
	 * @param docRes
	 *            XML文件对象
	 * @param fileName
	 *            包含文件名的文件路径 void
	 */
	public static void writeXML(Document docRes, String fileName) {
		FileOutputStream out = null;
		XMLWriter writer = null;
		try {
			if (docRes != null) {
				out = new FileOutputStream(fileName);

				OutputFormat of = new OutputFormat();
				of.setEncoding("GB2312");
				of.setIndent(true);
				of.setNewlines(true);

				writer = new XMLWriter(out, of);
				writer.write(docRes);

				writer.flush();
				out.flush();
			}
		} catch (Exception e) {
			logger.error("FileUtils:writeXML 写XML文件错误！", e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	

	/**
	 * @功能：将Document对象转换为xml文件
	 * @参数：
	 * @param document
	 * @param lspath
	 * @param path
	 * @param fileName
	 * @return
	 * @返回值：void
	 * create by zhaohuibin 2010-9-27 下午03:55:28
	 */
	public static void transDocToXMLFile(org.w3c.dom.Document document,
			String lspath, String path, String fileName) {
		try {
			String xmlString = toXMLString(document);
			saveXMLFile(xmlString, lspath, path, fileName);
		} catch (TransformerException e) {
			logger.error("转换XML文件错误！", e);
		} catch (IOException e) {
			logger.error("关闭文件流错误！", e);
		}
	}

	/**
	 * 将XML转换成字符串形式
	 * 
	 * @param document
	 *            Document
	 * @return
	 * @throws TransformerException
	 * @throws IOException
	 */
	private static String toXMLString(org.w3c.dom.Document document)
			throws TransformerException, IOException {
		/*
		 * 创建一个DOM转换器
		 */
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		/*
		 * 设置输出属性 encoding = "GB2312" 代表 输出的编码格式为 GB2312 indent = "yes" 代表缩进输出
		 */
		transformer.setOutputProperty("encoding", "GB2312");
		transformer.setOutputProperty("indent", "yes");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// transformer.transform()方法 将 XML Source转换为 Result
		String result = "";
		transformer.transform(new DOMSource(document), new StreamResult(
				outputStream));
		result = outputStream.toString();
		outputStream.close();// 关闭文件输出流
		return result;
	}

	private static void saveXMLFile(String fileContent, String lspath,
			String path, String fileName) {
		File file = new File(lspath);
		if (!file.isDirectory() || !file.exists()) {
			if (!mkdir(lspath)) {
				logger.error("生成临时文件夹失败！");
			}
		}

		file = new File(path);
		if (!file.isDirectory() || !file.exists()) {
			if (!mkdir(path)) {
				logger.error("生成文件夹失败！");
			}
		}

		String lsFileName = lspath + File.separator + fileName;
		String copyFileName = path + File.separator + fileName;
		logger.info("生成文件开始！");
		byte[] content = fileContent.getBytes();
		FileOutputStream fw = null;
		try {
			fw = new FileOutputStream(lsFileName);
			fw.write(content);
			fw.flush();
		} catch (IOException e) {
			logger.error("文件输出异常！", e);
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件输出流异常！", e);
			}
		}
		new File(lsFileName).renameTo(new File(copyFileName));
		logger.info("生成文件结束并转移成功！");
	}

	/**
	 * 创建不存在的指定文件夹
	 * 
	 * @param src_url
	 *            文件夹路径
	 */
	public static boolean mkdir(String src_url) {
		String[] urls = src_url.split("/");
		String pre = "";
		String preUrl = "";
		for (int i = 0; i < urls.length; i++) {
			preUrl += pre + urls[i];
			File file = new File(preUrl);
			if (!file.isDirectory()) {
				if (!file.mkdir()) {
					return false;
				}
			}
			if (i == 0) {
				pre = "/";
			}
		}
		return true;
	}

	/**
	 * @函数名：createYearMonthDir
	 * @功能：创建年月目录
	 * @param dir
	 *            原始目录
	 * @return String
	 */
	public static String createYearMonthDir(String dir) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		String year = "" + cal.get(Calendar.YEAR);
		String month = "" + (1 + cal.get(Calendar.MONTH));
		File yearDir = new File(dir + File.separator + year);
		if (!yearDir.exists()) {
			yearDir.mkdir();
		}
		File monthDir = new File(dir + File.separator + year + File.separator
				+ month);
		if (!monthDir.exists()) {
			monthDir.mkdir();
		}
		return (dir + File.separator + year + File.separator + month);
	}
	
	/**
	 * @author 王庆 2013-8-22 下午05:05:59
	 * @功能：在普通java类直接获取当前部署项目名称
	 * */
	public static String getProjectName(){
		String url = FileUtils.class.getResource("/").toString();
		String[] str = url.split("/");
		return str[str.length-3];
		
	}
	
	/**
	 * @author WangQing 2014-5-26 上午10:08:23
	 * @function:获取文件的最后一次修改时间
	 * @return long (注：返回结果为0，则指定文件不存在)
	 */
	public static long getFileLastModified(String filePath){
		File file = new File(filePath);
		return file.lastModified();
	}
	
	/**
	 * @author WangQing 2014-5-26 上午10:51:49
	 * @function:一次性读取文件加载到内存（注：文件太大会造成内存溢出，设置加载文件大于小于等于5M）
	 * @return
	 */
	public static byte[] readFileAllByte(String filePath){
		File file = new File(filePath);
		if(!file.exists()){
			return null;
		}
		int fileSize = (int)file.length();
		if(fileSize > 5*1024*1024){
			throw new RuntimeException("加载文件大于5M！");
		}
		byte[] b = new byte[fileSize];
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(b);
		} catch (IOException e) {
			logger.error("一次性读取文件加载到内存异常",e);
		}finally{
			try {
				if(fileIn != null) fileIn.close();
			} catch (IOException e) {
				logger.error("一次性读取文件加载到内存关闭输入流异常",e);
			}
		}
		return b;
	}
	
	/**
	 * @author WangQing 2014-5-30 下午03:23:57
	 * @function:读取.properties格式文件，返回Properties对象（注：文件格式必须为.properties）
	 * @param filePath
	 * @return
	 */
	public static Properties getProperties(String filePath){
		Properties p = null;
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			p = new Properties();
			p.load(in);
		} catch (FileNotFoundException e) {
			logger.error("给定文件路径路径不存在",e);
		} catch (IOException e) {
			logger.error("文件读取异常",e);
		}finally{
			try {
				if(in !=null)in.close();
			} catch (IOException e) {
				logger.error("文件流关闭异常",e);
			}
		}
		return p;
	}
	
	/**
	 * 获取文件编码格式
	 * @功能: 调用第三方工具cpDetector完成
	 * @param path	待检测文件的路径
	 * @return: String
	 */
	public static String getFileEncode(String path) {
		/*
		 * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
		 * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
		 * JChardetFacade、ASCIIDetector、UnicodeDetector。
		 * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
		 * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
		 * cpDetector是基于统计学原理的，不保证完全正确。
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/*
		 * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
		 * 指示是否显示探测过程的详细信息，为false不显示。
		 */
		detector.add(new ParsingDetector(false));
		/*
		 * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
		 * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
		 * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
		 */
		detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
		// ASCIIDetector用于ASCII编码测定
		detector.add(ASCIIDetector.getInstance());
		// UnicodeDetector用于Unicode家族编码的测定
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		File f = new File(path);
		try {
			charset = detector.detectCodepage(f.toURI().toURL());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null)
			return charset.name();
		else
			return null;
	}
	
	/**
	 * action中下载文件工具
	 * @param request
	 * @param response
	 * @param file	要下载的文件路径
	 * @param displayName 下载要展示的文件名，如果为空，就显示为文件名
	 * @return: void
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, 
			File file, String displayName) {
		if (file == null || !file.isFile()) {
			throw new RuntimeException("要下载的文件不存在");
		}
		if (displayName == null || displayName.equals("")) {
			displayName = file.getName();
		}
		String userAgent = request.getHeader("User-Agent");  
        boolean isIE = (userAgent != null) && (userAgent.toLowerCase().indexOf("msie") != -1);  
  
        response.reset();  
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "must-revalidate, no-transform");  
        response.setDateHeader("Expires", 0L);  
        response.setContentType("application/x-msdownload;charset=UTF-8");  
        response.setContentLength((int) file.length());  
        try {
        	if (isIE) {  
                displayName = URLEncoder.encode(displayName, "UTF-8");  
                response.setHeader("Content-Disposition", "attachment;filename=\"" + displayName + "\"");  
            } else {  
                displayName = new String(displayName.getBytes("UTF-8"), "ISO8859-1");  
                response.setHeader("Content-Disposition", "attachment;filename=" + displayName);  
            }  
            BufferedInputStream is = null;  
            OutputStream os = null;  
            try {  
                os = new BufferedOutputStream(response.getOutputStream());  
                is = new BufferedInputStream(new FileInputStream(file));
                FileUtil.copyStream(is, os);
            }catch (Exception e) {
    		}finally {
    			if (os != null) {
    				os.close();
    			}
    			if (is != null) {
    				is.close();
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取文件名，不带后缀
	 * @param file
	 * @return: String
	 */
	public static String getPureFilename(File file) {
		if (file == null || !file.isFile()) {
			return null;
		}
		String name = file.getName();
		if (!name.contains(".")) {
			return name;
		}
		name = name.substring(0, name.lastIndexOf("."));
		return name;
	}
	
	/**
	 * 在普通类中获取当前部署的项目的跟路径 
	 * 		部署之后的就是tomcat目录中webapp下面的该项目目录	
	 * 			D:\soft\install\tomcat6gdmb\webapps\mbwpzhxxglxt
	 * 		开发时候直接获取的是源码中的webroot同级别的目录
	 * 			D:\coding\workspace\myworkOne\mbwpzhxxglxt\WebRoot\
	 * @return: String
	 */
	public static String getProjectRootPath(){
		
		ClassLoader classLoader = Thread.currentThread()  
				.getContextClassLoader();  
		if (classLoader == null) {  
			classLoader = ClassLoader.getSystemClassLoader();  
		}
		
		URL url = classLoader.getResource("");  
		String ROOT_CLASS_PATH = url.getPath() + File.separator;  
		File rootFile = new File(ROOT_CLASS_PATH);  
		String WEB_INFO_DIRECTORY_PATH = rootFile.getParent() + File.separator;  
		File webInfoDir = new File(WEB_INFO_DIRECTORY_PATH);  
		String servletContextPath = webInfoDir.getParent() + File.separator;
		return servletContextPath;
	}
	
	/**
	 * 获取文件大小
	 * @param filePath	文件路径
	 * @param sizeFormat	获取文件大小格式， B、K、M、G、AUTO
	 * @param pointLength	保留小数点位数
	 * @return
	 */
	public static String getFileSize(String filePath, int sizeFormat, int pointLength) {
		
		if (StringUtils.isEmpty(filePath)) {
			return null;
		}
		
		File file = new File(filePath);
		if (!file.isFile()) {
			return null;
		}
		
		long fileSize = file.length();
		
		if (SIZEFORMAT_AUTO == sizeFormat) {
			if (fileSize < 1024) {
				sizeFormat = SIZEFORMAT_B;
			} else if (fileSize < 1048576) {
				sizeFormat = SIZEFORMAT_K;
			} else if (fileSize < 1073741824) {
				sizeFormat = SIZEFORMAT_M;
			} else {
				sizeFormat = SIZEFORMAT_G;
			}
		}
		
		String decimalFormatStr = "#";
		if (pointLength>0) {
			decimalFormatStr += ".";
			for (int i = 0; i < pointLength; i++) {
				decimalFormatStr += "0";
			}
		}
		
		DecimalFormat df = new DecimalFormat(decimalFormatStr); 
		String res = "";
		if (SIZEFORMAT_B == sizeFormat) {
			res =  df.format((double)fileSize) + "B";
		}else if (SIZEFORMAT_K == sizeFormat) {
			res = df.format((double)fileSize / 1024) + "K";
		}else if (SIZEFORMAT_M == sizeFormat) {
			res = df.format((double)fileSize / 1048576) + "M";
		}else{
			res = df.format((double)fileSize / 1073741824) + "G";
		}
		
		if (res.startsWith(".")) {
			res = "0" + res;
		}
		return res;
	}
	
	
	public static final int SIZEFORMAT_AUTO = 0;
	public static final int SIZEFORMAT_B = 1;
	public static final int SIZEFORMAT_K = 2;
	public static final int SIZEFORMAT_M = 3;
	public static final int SIZEFORMAT_G = 4;



	/**
	 * 从根目录中找到第一个文件名中包含关键字的文件
	 * @param rootpath 根目录
	 * @param key 关键字
	 * @return
	 */
	public static File findFile(String rootpath, String key) {
		File file = null;
		File root = new File(rootpath);
		File[] listFiles = root.listFiles();
		if (listFiles == null || listFiles.length==0) {
			return null;
		}
		for (File _file : listFiles) {
			if (_file.isFile() && _file.getName().contains(key)) {
				file = _file;
				break;
			}
			if (_file.isDirectory()) {
				File f = findFile(_file.getAbsolutePath(), key);
				if (file == null && f != null) {
					file = f;
				}
			}
		}
		return file;
	}
	
}
