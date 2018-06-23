package com.jadlsoft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * @Title: FtpClient.java
 * @Description:FTP客户端通用处理类
 * @Copyright: Copyright (c) 2010
 * @Company: 安丹灵
 * @date 2010-3-5
 * @author zhaohuibin
 * @version 3.0
 */
public class FtpClient {

	private Logger logger = Logger.getLogger(this.getClass());

	private StringBuffer wjm = new StringBuffer();

	private StringBuffer wjczsj = new StringBuffer();

	private static final String DELETE_FLAG = "1";

	private static final String REMAIN_FLAG = "2";
	/**
	 * @函数名：downloadFtpRootFilesRemainSource
	 * @功能：将FTP的文件转移到本地的目录，在FTP上保留源文件
	 * @param localDir
	 *            文件转移到本地的目录
	 * @param fileExt
	 *            处理的文件类型（文件扩展名），逗号隔开的扩展名字符串（如.dat,.zip ......）
	 * @param min
	 *            对多少分钟前生成的文件进行处理
	 * @param ftpInfo
	 *            ftp相关信息，包括ftp服务器IP地址、端口、用户名、密码（依次排列）
	 * @param singleFileName
	 * 			  下载文件的文件名或文件夹名（为空时，下载ftp用户根目录下所有文件）
	 * void
	 */
	public void downloadFtpRootFilesRemainSource(String localDir,
			String[] fileExt, int min, String[] ftpInfo, String singleFileName) {
		downloadFtpRootFiles(localDir, fileExt, min, ftpInfo, REMAIN_FLAG,
				singleFileName);
	}
	/**
	 * @函数名：downloadFtpRootFiles
	 * @功能：将FTP的文件转移到本地的目录后，删除FTP上的源文件
	 * @param localDir
	 *            文件转移到本地的目录
	 * @param fileExt
	 *            处理的文件类型（文件扩展名），逗号隔开的扩展名字符串（如.dat,.zip ......）
	 * @param min
	 *            对多少分钟前生成的文件进行处理
	 * @param ftpInfo
	 *            ftp相关信息，包括ftp服务器IP地址、端口、用户名、密码（依次排列）
	 * @param singleFileName
	 * 			  下载文件的文件名或文件夹名（为空时，下载ftp用户根目录下所有文件）
	 * void
	 */
	public void downloadFtpRootFilesDeleteSource(String localDir,
			String[] fileExt, int min, String[] ftpInfo, String singleFileName) {
		downloadFtpRootFiles(localDir, fileExt, min, ftpInfo, DELETE_FLAG,
				singleFileName);
	}

	/**
	 * @函数名：downloadFtpRootFiles
	 * @功能：将FTP的文件转移到本地的目录
	 * @param localDir
	 *            文件转移到本地的目录
	 * @param fileExt
	 *            处理的文件类型（文件扩展名），逗号隔开的扩展名字符串（如.dat,.zip ......）
	 * @param min
	 *            对多少分钟前生成的文件进行处理
	 * @param ftpInfo
	 *            ftp相关信息，包括ftp服务器IP地址、端口、用户名、密码（依次排列）
	 * @param delOrNot
	 * 			  下载完成后是否删除（1：删除 其他：保留）
	 * @param singleFileName
	 * 			  下载文件的文件名或文件夹名（为空时，下载ftp用户根目录下所有文件）
	 * void
	 */

	private void downloadFtpRootFiles(String localDir, String[] fileExt,
			int min, String[] ftpInfo, String delOrNot, String singleFileName) {
		FTPClient ftp = loginFtpServer(ftpInfo[0],
				Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
		if (ftp == null) {
			return;
		}
		OutputStream output = null;
		try {
			ftp.setControlEncoding("utf-8");
			FTPFile[] ftpFiles = null;
			if (!"".equals(singleFileName.trim())) {
				File file = new File(singleFileName);
				if (file.isFile()) {
					ftpFiles = ftp.listFiles(singleFileName);
				} else {
					ftp.changeWorkingDirectory(singleFileName);
					ftpFiles = ftp.listFiles();
				}
			} else {
				ftpFiles = ftp.listFiles();
			}

			long startDownloadTime = System.currentTimeMillis();

			for (int i = 0; i < ftpFiles.length; i++) {
				if (!checkFtpFile(ftpFiles[i], fileExt, min)) {
					continue;
				}
				String fileName = ftpFiles[i].getName();
				/*
				 * 下载文件。如果下载文件失败，保留文件。
				 */
				output = new FileOutputStream(parseFileSeperator(localDir)
						+ fileName);
				if (ftp.retrieveFile(new String(fileName.getBytes("utf-8"),// 如果下载的文件为非压缩文件，此处的fileName需用ISO-8859-1编码，否则下载文件内容为空
						"ISO-8859-1"), output)) {
					closeStream(output);
					logger.info("文件" + fileName + "下载成功");
					if (IConstants.INVALID.equals(delOrNot)) {//常量INVALID使用系统常量类中已有的，此处表示删除
						/*
						 * 删除FTP文件
						 */
						if (!ftp.deleteFile(fileName)) {
							logger.error("FTP客户端：删除FTP文件" + fileName + "失败");
						}
					}
				} else {
					closeStream(output);
					logger.error("FTP客户端：复制FTP文件" + fileName + "失败");
					continue;
				}

				/*
				 * 为避免下载过程中出现网络故障，故每个文件下载完毕后均记录日志
				 */
				long endDownloadTime = System.currentTimeMillis();

				logger.info("下载起始时间：" + startDownloadTime + ",下载结束时间："
						+ endDownloadTime + ",下载文件信息：" + fileName);
			}
		} catch (Exception e) {
			logger.error("FTPCLIENT：下载文件失败。", e);
		} finally {
			closeStream(output);
			disconnect(ftp);
		}
	}

	/**
	 * @函数名：uploadFileOrDir
	 * @功能：上传文件或文件夹下的所有文件
	 * @param localDir
	 *            上传文件夹的名称（包含路径）
	 * @param fileName
	 *            上传文件的名称或文件夹名称(1、为文件名时，上传单个文件；2、为文件夹时，上传该文件夹下所有的文件；3、为空时，将上传localDir下所有的文件)
	 * @param ftpInfo
	 *            ftp相关信息，包括ftp服务器IP地址、端口、用户名、密码（依次排列） void
	 */
	public void uploadFileOrDir(String localDir, String fileName,
			String[] ftpInfo) {
		FTPClient ftp = loginFtpServer(ftpInfo[0],
				Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
		if (ftp == null) {
			return;
		}
		ftp.enterLocalPassiveMode();
		ftp.setControlEncoding("utf-8");
		String startUploadTime = "" + System.currentTimeMillis();
		try {
			uploadDir(ftp, "", localDir, fileName);
		} catch (IOException e) {
			logger.error("上传文件异常！", e);
			throw new RuntimeException("上传文件：IO错误！");
		} finally {
			disconnect(ftp);
		}

		String endUploadTime = "" + System.currentTimeMillis();
		if (!"".equals(wjm.toString())) {
			logger.info("上传起始时间：" + startUploadTime + ",上传结束时间："
					+ endUploadTime + ",上传文件信息：" + this.wjm.toString()
					+ ",文件传输时间：" + this.wjczsj.toString());
		}
	}

	/**
	 * @函数名：parseFileSeperator
	 * @功能：文件夹路径是否以“/”或“\\”结尾，如果不是则加入“/”
	 * @param dir
	 *            文件夹路径
	 * @return String
	 */
	private String parseFileSeperator(String dir) {
		if (dir == null || dir.equals("")) {
			return "";
		}
		return dir.endsWith("/") || dir.endsWith("\\") ? dir : (dir + "/");
	}

	/**
	 * @函数名：checkFtpFile
	 * @功能：过滤文件列表
	 * @param ftpFile
	 *            ftp待处理的文件
	 * @param fileExt
	 *            处理的文件类型（文件扩展名）
	 * @param min
	 *            对多少分钟前生成的文件进行处理
	 * @return boolean
	 */
	private boolean checkFtpFile(FTPFile ftpFile, String[] fileExt, int min) {
		String fileName = ftpFile.getName();
		for (int i = 0; i < fileExt.length; i++) {
			String temp = fileExt[i];
			long lastModifiedTime = ftpFile.getTimestamp().getTimeInMillis();

			if (fileName.toLowerCase().endsWith(temp.toLowerCase())
					&& (System.currentTimeMillis() - lastModifiedTime) >= 1000 * 60 * min) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @函数名：uploadFile
	 * @功能：上传单个文件
	 * @param ftp
	 *            FTP客户端
	 * @param ftpdir
	 *            FTP的上传目录
	 * @param dir
	 *            待上传文件的本地目录
	 * @param fileName
	 *            上传文件名称 void
	 */
	private void uploadFile(FTPClient ftp, String ftpdir, String dir,
			String fileName) {
		InputStream in = null;
		int i = 0;
		try {
			if (!ftpdir.equals("")) {
				String[] ftpdirs = ftpdir.split("/");
				for (; i < ftpdirs.length; i++) {
					if (!ftp.changeWorkingDirectory(ftpdirs[i])) {
						ftp.makeDirectory(ftpdirs[i]);
						ftp.changeWorkingDirectory(ftpdirs[i]);
					}
				}
			}
			in = new FileInputStream(new File(parseFileSeperator(dir)
					+ fileName));
			if (ftp.storeFile(fileName, in)) {
				closeStream(in);
				logger.info("文件" + fileName + "上传成功");

				File localDirFile = new File(parseFileSeperator(dir) + fileName);
				//				if (!localDirFile.delete()) {
				//					logger.error("删除本地文件" + fileName + "失败");
				//				}
				localDirFile.delete();
			} else {
				closeStream(in);
				logger.info("文件" + fileName + "上传失败");
			}
			/*
			 * 记录日志
			 */
			if (this.wjm.toString().equals("")) {
				this.wjm.append(parseFileSeperator(ftpdir) + fileName);
			} else {
				this.wjm.append("@@");
				this.wjm.append(parseFileSeperator(ftpdir) + fileName);
			}
			if (this.wjczsj.toString().equals("")) {
				this.wjczsj.append("" + System.currentTimeMillis());
			} else {
				this.wjczsj.append("@@");
				this.wjczsj.append("" + System.currentTimeMillis());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("上传文件：未找到指定文件！");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("上传文件：IO错误！");
		} finally {
			closeStream(in);
			try {
				if (!"".equals(ftpdir)) {
					for (; i >= 0; i--) {
						ftp.changeToParentDirectory();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("上传文件：FTP转换目录错误！");
			}
		}
	}

	/**
	 * 上传目录
	 * 
	 * @param ftp
	 *            FTP客户端
	 * @param ftpdir
	 *            FTP的上传目录
	 * @param dir
	 *            本地目录所在绝对路径
	 * @param fileName
	 *            上传文件名称
	 * @throws IOException
	 */
	private void uploadDir(FTPClient ftp, String ftpdir, String dir,
			String fileName) throws IOException {
		File file = new File(parseFileSeperator(dir) + fileName);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			uploadFile(ftp, ftpdir, parseFileSeperator(dir), fileName);
		} else if (file.isDirectory()) {
			String[] files = file.list();
			for (int i = 0; i < files.length; i++) {
				uploadDir(ftp, parseFileSeperator(ftpdir) + fileName,
						parseFileSeperator(dir) + fileName, files[i]);
			}
		}
	}

	/**
	 * @函数名：closeStream
	 * @功能：关闭文件操作流
	 * @param object
	 *            文件操作流 void
	 */
	private void closeStream(Object object) {
		if (object != null && object instanceof OutputStream) {
			try {
				((OutputStream) object).close();
			} catch (IOException e) {
				logger.error("FTPCLIENT：关闭文件输出流失败。", e);
			}
		} else if (object != null && object instanceof InputStream) {
			try {
				((InputStream) object).close();
			} catch (IOException e) {
				logger.error("FTPCLIENT：关闭文件输入流失败。", e);
			}
		}
	}

	/**
	 * @函数名：loginFtpServer
	 * @功能：连接ftp
	 * @param url
	 *            ftp服务器IP地址
	 * @param port
	 *            端口
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @return FTPClient
	 */
	private FTPClient loginFtpServer(String url, int port, String userName,
			String password) {
		FTPClient ftp = new FTPClient();
		for (int i = 0; i < 3; i++) {// 获取ftp链接，连接3次
			try {
				/*
				 * 连接服务器
				 */
				ftp.connect(url, port);
				/*
				 * 连接反馈
				 */
				int reply = ftp.getReplyCode();
				if (!FTPReply.isPositiveCompletion(reply)) {
					disconnect(ftp);
					logger.error("连接服务器：FTP拒绝连接。");
					ftp = null;
				} else {// 连接成功后，登录
					ftp.login(userName, password);
					ftp.enterLocalPassiveMode();//采用被动访问模式，如果有防火墙，下载必须采用此种方式，否则登录成功后，不下载
					ftp.setFileType(FTP.BINARY_FILE_TYPE);
				}
			} catch (SocketException e) {
				logger.error("连接服务器：socket连接错误!", e);
				ftp = null;
			} catch (IOException e) {
				logger.error("连接服务器：IO连接错误!", e);
				ftp = null;
			}

			if (ftp != null) {
				break;
			} else {
				try {
					Thread.sleep(1000 * 60);// 等待一分钟后重连
					continue;
				} catch (InterruptedException e) {
					logger.error("线程等待异常！", e);
				}
			}
		}

		return ftp;
	}

	/**
	 * 关闭FTP连接
	 * 
	 * @param ftp
	 */
	private void disconnect(FTPClient ftp) {
		if (ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				logger.error("FTPCLIENT：关闭ftp连接异常！", e);
			}
		}
	}

}
