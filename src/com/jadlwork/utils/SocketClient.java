package com.jadlwork.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

public class SocketClient {
	private static Logger log = Logger.getLogger(SocketClient.class);
	private static SocketClient instance = new SocketClient();
	private Socket socket_communication;
	private Socket socket_upload;
	private Socket socket_download;
	final int serverPort_Communication = 10003;//主机通信端口
	final int serverPort_upload = 10002;//主机上传文件端口
	final int serverPort_download = 10004;	//主机下载文件端口
	final int deilyTime = 10000;//超时时间
	/**
	 * 检测主机socket通信是否正常
	 * @参数：@param ip info{do:do,warname:warname}
	 * @参数：@return
	 * @返回值：boolean
	 */
	public synchronized String socketTest(String ip,String info) {
		String re = "";
		InputStream is = null;
		BufferedReader br = null;
		OutputStream os = null;
		PrintWriter pw = null;
		try {
			//1.建立客户端socket连接，指定主机位置及端口  连接超过2秒超时处理
			socket_communication =new Socket();  
	        InetAddress addr = InetAddress.getByName(ip); 
	        socket_communication.connect( new InetSocketAddress( addr, serverPort_Communication ), 3000 );  
	      
	        //2.得到socket读写流  
	        os=socket_communication.getOutputStream();  
	        pw=new PrintWriter(os);  
	        //输入流  
	        is=socket_communication.getInputStream();  
	        br=new BufferedReader(new InputStreamReader(is,"UTF-8"));  
	        //3.利用流按照一定的操作，对socket进行读写操作  
	        pw.write(info);  
	        pw.flush();  
	        socket_communication.shutdownOutput();  
	        //接收主机的相应  
	        re=br.readLine();  

	        //4.关闭资源  
	        br.close();  
	        is.close();  
	        pw.close();  
	        os.close();  
	        socket_communication.close(); 
		} catch (Exception e) {
			log.info("与主机"+ip+"连接异常!");
			return "";
		}finally{
			try {
				if (pw != null) {
					pw.close();
				}
				if (os != null) {
					os.close();
				}
				if (br != null) {
					br.close();
				}
				if (is != null) {
					is.close();
				}
				if (socket_communication != null) {
					socket_communication.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return re;

	}

	/**
	 * socket上传war包到部署主机
	 * @参数：@param ip
	 * @参数：@return
	 * @返回值：boolean
	 */
	public synchronized boolean uploadWar(String ip, String warsrc) {
		log.info("往主机【"+ip+"】上传war包【"+warsrc+"】开始!");
		boolean bool = false;
		DataOutputStream dos = null;  
		FileInputStream fis = null;  
		try {
			// 1.建立客户端socket连接，指定主机位置及端口 连接超过2秒超时处理
			socket_upload = new Socket();
			InetAddress addr = InetAddress.getByName(ip);
			socket_upload.connect(new InetSocketAddress(addr, serverPort_upload), 2000);
			socket_upload.setKeepAlive(true);
			socket_upload.setSoTimeout(deilyTime);
			File file = new File(warsrc); // 要传输的文件路径
			if(!file.exists()){
				log.error("文件【"+warsrc+"】不存在!");
				return false;
			}
			
			int length = 0;  
			double sumL = 0 ;  
			byte[] sendBytes = null;  
			long l = file.length();
			dos = new DataOutputStream(socket_upload.getOutputStream());
			dos.writeUTF(file.getName());
			dos.flush();
			fis = new FileInputStream(file);
			sendBytes = new byte[1024];
			while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
				sumL += length;
				//System.out.println("已传输：" + ((sumL / l) * 100) + "%");
				dos.write(sendBytes, 0, length);
				dos.flush();
			}

			// 虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较
			if (sumL == l) {
				bool = true;
			}
			
		} catch (Exception e) {
			log.info("war包【"+warsrc+"】传输异常");
			bool = false;
		} finally {
			try {
				if (dos != null)
					dos.close();
				if (fis != null)
					fis.close();
				if (socket_upload != null)
					socket_upload.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("往主机【"+ip+"】上传war包【"+warsrc+"】完成!");
		return bool;
	}
	
	/**
	 * 下载流，使用socket完成文件的下载功能
	 * @param ip
	 * @param info
	 * @param destPath 目标目录
	 * @param filename 设置文件名，如果为空就设置为原始文件名
	 * @return: boolean 
	 */
	public synchronized boolean downloadStream(String ip,String info,String destPath, String filename) {
		boolean re = true;
		
		InputStream is = null;
		OutputStream os = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte[] bytes = null;
		
		try {
			//1.建立客户端socket连接，指定主机位置及端口  连接超过3秒超时处理
			socket_download =new Socket();  
	        InetAddress addr = InetAddress.getByName(ip); 
	        socket_download.connect( new InetSocketAddress( addr, serverPort_download ), 3000 );  
	      
	        //2.得到socket读写流  
	        os = socket_download.getOutputStream();
	        is = socket_download.getInputStream();
	        bis = new BufferedInputStream(is);
	        
	        /*
	         * 3、定下规则：
	         *		下载文件的时候	(统一使用utf-8的格式)
	         *			i.客户端先向主机发送一个请求，表名要下载的文件的类型（具体要下载什么东西），如果需要参数，将参数通过&传递过去 
	         *			ii.主机收到客户端的请求信息后，将文件名和文件的大小返回
	         *			iii.客户端根据返回的文件信息确定发送下载文件的请求
	         *			iV.主机收到客户端的下载文件的请求后发送文件的字节流信息
	         */
	        //3.1 向主机发送请求，让主机知道下载什么文件
	        os.write(info.getBytes("UTF-8"));
	        os.flush();
	        bytes = new byte[1024];
	        String fileInfo = new String(bytes, 0, is.read(bytes), "UTF-8");	//获取文件信息  格式为：filename&filesize eg: nginx.conf&6565
	        //异常统一在下面进行了抓取，这里直接使用
	        //0开头的为正常，1开头说明文件不存在
	        if (fileInfo != null && fileInfo.startsWith("1")) {
				//文件不存在
	        	log.info("要下载的文件不存在");
	        	return false;
			}
	        String[] fileInfoArr = fileInfo.split("&");	//目前定为最多三个字段    0&filename&size  或者 1
	        //3.2判断文件信息，确定文件名（提供文件的大小，如果要限制文件大小可以在这里进行判断）
	        if (filename == null || filename.equals("")) {	//如果没有指定文件名 ，就以原始文件的文件名存储
				filename = fileInfoArr[1];
			}
	        //3.3发送确定下载的请求
	        os.write("0".getBytes("UTF-8"));
	        os.flush();
	        //接收主机发送的字节流信息
	        //设置文件输入流
	        File destDir = new File(destPath);
	        if (!destDir.isDirectory()) {
				destDir.mkdirs();
			}
	        bos = new BufferedOutputStream(new FileOutputStream(new File(destPath, filename)));
	        bytes = new byte[1024];
	        int len;
	        while((len=bis.read(bytes))!=-1){
	        	bos.write(bytes, 0, len);
	        	bos.flush();
	        }
	        socket_download.shutdownOutput();  

		} catch (Exception e) {
			re = false;
			log.info("与主机执行"+info+"操作出现异常!");
		}finally{
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				if (socket_download != null) {
					socket_download.close();
				}
			} catch (Exception e) {
				re = false;
				e.printStackTrace();
			}
		}
		return re;

	}
	
	public SocketClient() {
		instance = this;
	}

	public static SocketClient getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		SocketClient.getInstance().downloadStream("192.168.20.124", "currentNginxConfig&D:\\soft\\install\\nginx-1.8.0", "D:\\soft", "nginxDownload.conf");
	}
}