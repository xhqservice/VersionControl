package com.jadlwork.utils.httputils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * @ClassName��HttpRequestProxy
 * @Description��
 * @author ��zhangqing
 * @date ��2014-10-13 ����2:46:22
 * @version
 */
public class HttpRequestProxy {
	//���ӳ�ʱ
	private static int connectTimeOut = 30000;   
    //��ȡ��ݳ�ʱ     
   	private static int readTimeOut = 60000;  
	// ��connectionmanager����httpclientconnectionʱ�Ƿ�ر�����
	private static boolean alwaysClose = true;
	// ������ݱ����ʽ
	private final String default_encoding = "utf-8";
	private final Header [] default_header = {new Header("Pragma", "no-cache"),new Header("Cache-Control", "no-cache"), new Header("User-Agent","Mozilla/4.0")};

	private final HttpClient client = new HttpClient(
			new SimpleHttpConnectionManager(alwaysClose));

	public HttpClient getHttpClient() {
		return client;
	}

	/**
	 * �÷��� HttpRequestProxy hrp = new HttpRequestProxy();
	 * hrp.doRequest("http://www.163.com",null,null,"gbk");
	 * 
	 * @param url
	 *            �������Դ�գң�
	 * @param postData
	 *            POST����ʱform�?��װ����� û��ʱnull
	 * @param header
	 *            request����ʱ�����ͷ��Ϣ(header) û��ʱ��Ĭ��ֵ
	 * @param encoding
	 *            response���ص���Ϣ�����ʽ û��ʱ��Ĭ��ֵ
	 * @return response���ص��ı����
	 * @throws Exception
	 */
	public String doRequest(String url, Map postData, Map header,
			String encoding) throws Exception {
		String responseString = null;
		
		// �������ӳ�ʱʱ��(��λ����) 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(
				connectTimeOut);
		  // ���ö���ݳ�ʱʱ��(��λ����) 
		client.getHttpConnectionManager().getParams().setSoTimeout(
				readTimeOut);
		// ͷ��������Ϣ
		Header[] headers = null;
		if (header != null) {
			Set entrySet = header.entrySet();
			int dataLength = entrySet.size();
			headers = new Header[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
				Map.Entry entry = (Map.Entry) itor.next();
				headers[i++] = new Header(entry.getKey().toString(), entry
						.getValue().toString());
			}
		}else{
			headers = default_header;
		}
		// post��ʽ
		if (postData != null) {
			if(encoding == null){
				encoding = default_encoding;
			}
			PostMethod postRequest = new CustomPostMethod(url.trim(),encoding);
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					postRequest.setRequestHeader(headers[i]);
				}
			}
			Set entrySet = postData.entrySet();
			int dataLength = entrySet.size();
			NameValuePair[] params = new NameValuePair[dataLength];
			int i = 0;
			for (Iterator itor = entrySet.iterator(); itor.hasNext();) {
				Map.Entry entry = (Map.Entry) itor.next();
				params[i++] = new NameValuePair(entry.getKey().toString(),
						entry.getValue().toString());
			}
			postRequest.setRequestBody(params);
			try {
				responseString = this.executeMethod(postRequest, encoding);
			} catch (Exception e) {
				throw e;
			} finally {
				postRequest.releaseConnection();
				((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();  
			}
		}
		// get��ʽ
		if (postData == null) {
			GetMethod getRequest = new GetMethod(url.trim());
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					getRequest.setRequestHeader(headers[i]);
				}
			}
			try {
				responseString = this.executeMethod(getRequest, encoding);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				getRequest.releaseConnection();
				((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();  
			}
		}

		return responseString;
	}

	private String executeMethod(HttpMethod request, String encoding)
			throws Exception {
		String responseContent = null;
		InputStream responseStream = null;
		BufferedReader rd = null;
		try {
			this.getHttpClient().executeMethod(request);
			if (encoding != null) {
				responseStream = request.getResponseBodyAsStream();
				rd = new BufferedReader(new InputStreamReader(responseStream,
						encoding));
				String tempLine = rd.readLine();
				StringBuffer tempStr = new StringBuffer();
				String crlf = System.getProperty("line.separator");
				while (tempLine != null) {
					tempStr.append(tempLine);
					tempStr.append(crlf);
					tempLine = rd.readLine();
				}
				responseContent = tempStr.toString();
			} else
				responseContent = request.getResponseBodyAsString();

			Header locationHeader = request.getResponseHeader("location");
			// ���ش���Ϊ302,301ʱ����ʾҳ�漺���ض�������������location��url������
			// һЩ��¼��Ȩȡcookieʱ����Ҫ
			if (locationHeader != null) {
				String redirectUrl = locationHeader.getValue();
				this.doRequest(redirectUrl, null, null, null);
			}
		} catch (HttpException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			throw new Exception(e.getMessage());

		} finally {
			if (rd != null)
				try {
					rd.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());
				}
			if (responseStream != null)
				try {
					responseStream.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());

				}
		}
		return responseContent;
	}

	/**
	 * �����������,�����������������redirect�������ֵݹ���ѭ���ض��� ���Ե���д��һ�����󷽷�
	 * �������������urlΪ��http://localhost:8080/demo/index.jsp ���ش���Ϊ302
	 * ͷ����Ϣ��locationֵΪ:http://localhost:8083/demo/index.jsp
	 * ��ʱhttpclient��Ϊ����ݹ���ѭ���ض����׳�CircularRedirectException�쳣
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doSpecialRequest(String url, int count, String encoding)
			throws Exception {
		String str = null;
		InputStream responseStream = null;
		BufferedReader rd = null;
		GetMethod getRequest = new GetMethod(url);
		// �ر�httpclient�Զ��ض�����
		getRequest.setFollowRedirects(false);
		try {

			this.client.executeMethod(getRequest);
			Header header = getRequest.getResponseHeader("location");
			if (header != null) {
				// �����ض����ģգң̣�countͬʱ��1
				this.doSpecialRequest(header.getValue(), count + 1, encoding);
			}
			// ������count��Ϊ��־λ����countΪ0ʱ�ŷ�������ģգң��ı�,
			// ����Ϳ��Ժ������еĵݹ��ض���ʱ�����ı����������������
			if (count == 0) {
				getRequest = new GetMethod(url);
				getRequest.setFollowRedirects(false);
				this.client.executeMethod(getRequest);
				responseStream = getRequest.getResponseBodyAsStream();
				rd = new BufferedReader(new InputStreamReader(responseStream,
						encoding));
				String tempLine = rd.readLine();
				StringBuffer tempStr = new StringBuffer();
				String crlf = System.getProperty("line.separator");
				while (tempLine != null) {
					tempStr.append(tempLine);
					tempStr.append(crlf);
					tempLine = rd.readLine();
				}
				str = tempStr.toString();
			}

		} catch (HttpException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} finally {
			getRequest.releaseConnection();
			((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();  
			if (rd != null)
				try {
					rd.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());
				}
			if (responseStream != null)
				try {
					responseStream.close();
				} catch (IOException e) {
					throw new Exception(e.getMessage());
				}
		}
		return str;
	}

    public static int getConnectTimeOut()  
	{  
    	return HttpRequestProxy.connectTimeOut;  
	} 
    public  static int getReadTimeOut()  
	{  
    	return HttpRequestProxy.readTimeOut;  
	} 
    public static void setConnectTimeOut(int connectTimeOut)  
	{  
    	HttpRequestProxy.connectTimeOut = connectTimeOut;  
	} 
    public static void setReadTimeOut(int readTimeOut)  
	{  
    	HttpRequestProxy.readTimeOut = readTimeOut;  
	} 
	public static void main(String[] args) throws Exception {
		HttpRequestProxy hrp = new HttpRequestProxy();
		Map header = new HashMap();
		header.put("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)");
		Map postData = new HashMap();
		postData.put("unit","123");
		String str = hrp.doRequest("http://192.168.20.15:8080/mbhsu/login!getUnitInfo.action",postData, null, null);
		System.out.println(str);
		// System.out.println(str);
	}

}
