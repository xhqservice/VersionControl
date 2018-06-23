package com.jadlwork.utils.httputils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class HttpUtils {

	
	public static String fileUpload(String remoteUrl, List<String> files){  
//        List<String> list  = new ArrayList<String>();  //要上传的文件名,如：d:\haha.doc.你要实现自己的业务。我这里就是一个空list.  
        try {  
            String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线  
            URL url = new URL(remoteUrl);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");  
            conn.setRequestProperty("Charsert", "UTF-8");   
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  
              
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线  
            int leng = files.size();  
            for(int i=0;i<leng;i++){  
                String fname = files.get(i);  
                File file = new File(fname);  
                StringBuilder sb = new StringBuilder();    
                sb.append("--");    
                sb.append(BOUNDARY);    
                sb.append("\r\n");    
                sb.append("Content-Disposition: form-data;name=\"file"+i+"\";filename=\""+ file.getName() + "\"\r\n");    
                sb.append("Content-Type:application/octet-stream\r\n\r\n");    
                  
                byte[] data = sb.toString().getBytes();  
                out.write(data);  
                DataInputStream in = new DataInputStream(new FileInputStream(file));  
                int bytes = 0;  
                byte[] bufferOut = new byte[1024];  
                while ((bytes = in.read(bufferOut)) != -1) {  
                    out.write(bufferOut, 0, bytes);  
                }  
                out.write("\r\n".getBytes()); //多个文件时，二个文件之间加入这个  
                in.close();  
            }  
            out.write(end_data);  
            out.flush();    
            out.close();   
              
            // 定义BufferedReader输入流来读取URL的响应  
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
            StringBuffer sb = new StringBuffer();
            String line = null;  
            while ((line = reader.readLine()) != null) {
            	sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {  
            System.out.println("发送POST请求出现异常！" + e);  
            e.printStackTrace();  
        }
        return "";
    }  
	
}
