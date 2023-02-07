package cn.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author zhanggt
 * 
 */
public class HttpClient {

	private static final String CHARSET = "UTF-8";
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE_TEXT_JSON = "application/x-www-form-urlencoded;charset=UTF-8";

	public static Map<String, Object> jsonPost(String url, Map<String, Object> params) {
		com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject(params);
		String reqStr = json.toJSONString();
		String respStr = post(url, reqStr);
		com.alibaba.fastjson.JSONObject resp = com.alibaba.fastjson.JSON.parseObject(respStr);
		return resp;
	}

	/**
	 * http post请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String post(String url, Map<String, Object> params) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				if(params.get(key)!=null){
					parameters.add(new BasicNameValuePair(key, params.get(key).toString()));
				}else{
					parameters.add(new BasicNameValuePair(key, null));
				}
			}
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(parameters, CHARSET);
			uefEntity.setContentType(CONTENT_TYPE_TEXT_JSON);
			httpPost.setEntity(uefEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, CHARSET);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public static String uploadFile(String httpUrl, byte[] b, int length, String fileName) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Upload-File-Name", fileName);
			connection.setRequestProperty("Content-Length", "" + length);
			connection.setDoOutput(true);
			connection.getOutputStream().write(b, 0, length);
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static String postJSON(String URL, String JSONBody) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(URL);
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity sEntity = new StringEntity(JSONBody, CHARSET);
			httpPost.setEntity(sEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, CHARSET);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	public static String post(String httpUrl, byte[] b, int length) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "multipart/form-data");
			connection.setDoOutput(true);
			connection.getOutputStream().write(b, 0, length);
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * http post请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return
	 */
	public static String post(String url, String params) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity sEntity = new StringEntity(params, CHARSET);
			sEntity.setContentType(CONTENT_TYPE_TEXT_JSON);
			sEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			httpPost.setEntity(sEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, CHARSET);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * 使用apache下的HttpClient  进行post请求
	 * 
	 * @param url：路径
	 * @param params：json格式的参数,可以使用json对象tostring
	 * @return
	 */
	public static String sendApacheHttpClientPostMethod(String url,String params) {  
		org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient(new HttpClientParams(),new SimpleHttpConnectionManager(true));  
		httpClient.getParams().setContentCharset("UTF-8");
		PostMethod method = new PostMethod(url);        
		try {        
			if(params != null && !params.trim().equals("")) {        
				RequestEntity requestEntity = new StringRequestEntity(params,APPLICATION_JSON,"UTF-8");        
				method.setRequestEntity(requestEntity);        
			}        
			int status = httpClient.executeMethod(method);    
			//logger.info("http响应状态  = " +status);
			if(status ==HttpStatus.SC_OK){
				//logger.info("http发起请求  url = " + url);
				//logger.info("http发起请求  params = " + params );
				String responses= method.getResponseBodyAsString(); 
				//logger.info("http发起请求  responses = " + responses);
				return responses ;        
			}else{
				return "{errCode:-1,errMsg:'error'}";
			}
		} catch (HttpException e) {        
			e.printStackTrace();        
			//logger.error("http发起请求异常1  HttpException ", e);
		} catch (IOException e) {        
			e.printStackTrace();  
			//logger.error("http发起请求异常2 IOException ", e);
		}finally{
			method.releaseConnection();   
		}
		
		return "{errCode:-1,errMsg:'error'}";   
	}
	
	
	public static String get(String fullUrl) {
		return httpGet(fullUrl,CHARSET);
	}

	private static String httpGet(String fullUrl,String reCharSet) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(fullUrl);
			httpGet.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, reCharSet);
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	
	public static String get(String fullUrl,String reCharSet){
		return httpGet(fullUrl,reCharSet);
	}
	

	public static String getUrl(String url, String uri) {
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		if (url.endsWith("/") && uri.startsWith("/")) {
			uri = uri.substring(1);
		} else if (!url.endsWith("/") && !uri.startsWith("/")) {
			sb.append("/");
		}
		sb.append(uri);
		return sb.toString();
	}

}
