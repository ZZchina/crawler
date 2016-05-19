package com.crawler.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpClientTool {

	public static String getHtml(String url, String params) {
		String response = "";
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(60000);// 请求超时
		client.getHttpConnectionManager().getParams().setSoTimeout(60000);
		HttpMethod method = new GetMethod(url);
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");// 模拟浏览器信息
		method.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
		try {
			if (StringUtils.isNotBlank(params)) {
				method.setQueryString(params);
			}
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				// BufferedReader br = new BufferedReader(new
				// InputStreamReader(is,"gb2312"));//字符流编码，防止返回数据乱码
				response = IOUtils.toString(is);
				// response = method.getResponseBodyAsString();
			}
		} catch (URIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}

		return response;

	}

	/**
	 * 带cookie的HttpClient
	 * @param url 地址
	 * @param params 查询参数
	 * @param cookies  cookie信息
	 * @return
	 * @throws Exception
	 */
	public static String getHtml(String url, String params, String cookies)throws Exception {
		String response = "";
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);// 请求超时
		client.getHttpConnectionManager().getParams().setSoTimeout(60000);
		HttpMethod method = new GetMethod(url);
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
		method.setRequestHeader("Cookie", cookies);
		try {
			if (StringUtils.isNotBlank(params)) {
				method.setQueryString(params);
			}
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				response = IOUtils.toString(is);
				// response = method.getResponseBodyAsString();
			}
		} catch (URIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}

		return response;

	}

	/**
	 * 带cookie的代理ip HttpClient
	 * @param url 地址
	 * @param params 查询参数
	 * @param cookies cookie信息
	 * @return 页面源码字符
	 * @throws Exception
	 */
	public static String getHtmlProxy(String url, String params, String cookies)throws Exception {
		String response = "";
		Map<String, Object> map = Config.getIp();
		HttpClient client = new HttpClient();
		// 代理的主机
		ProxyHost proxy = new ProxyHost((String) map.get("ip"),Integer.valueOf((String) map.get("port")));
		// 使用代理
		client.getHostConfiguration().setProxyHost(proxy);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);// 请求超时
		HttpMethod method = new GetMethod(url);
		setHeaders(method);
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
		method.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
		method.setRequestHeader("Cookie", cookies);
		try {
			if (StringUtils.isNotBlank(params)) {
				method.setQueryString(params);
			}
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				// BufferedReader br = new BufferedReader(new
				// InputStreamReader(is,"UTF-8"));//字符流编码，防止返回数据乱码
				response = IOUtils.toString(is);
				// response = method.getResponseBodyAsString();
			}
		} catch (URIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}

		return response;

	}

	/**
	 * 通过代理IP连接，代理ip网站：http://pachong.org/
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getHtmlProxy(String url,String params){ 
        String response = ""; 
        HttpClient client = new HttpClient();
        Map<String,Object> map=Config.getIp();
        String ip=(String) map.get("ip");
        if(StringUtils.isNotBlank(ip)){//代理数据不为空，使用代理
        	// 代理的主机
        	ProxyHost proxy = new ProxyHost(ip , Integer.valueOf((String) map.get("port")));
        	// 使用代理
        	client.getHostConfiguration().setProxyHost(proxy);
        	//抢先认证模式也提供对于特定目标或代理的缺省认证。如果没有提供缺省的认证信息，则该模式会失效。
//        client.getParams().setAuthenticationPreemptive(true);

        }
        client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);//请求超时
        HttpMethod method = new GetMethod(url);
        setHeaders(method);
        method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");//模拟浏览器信息
        method.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
        try {
	        	if (StringUtils.isNotBlank(params)){
	            	method.setQueryString(params);
	            }
                client.executeMethod(method);
//                System.out.println("代理访问返回状态："+method.getStatusCode());
                if (method.getStatusCode() == HttpStatus.SC_OK) { 
                	InputStream is = method.getResponseBodyAsStream();
//                	BufferedReader br = new BufferedReader(new InputStreamReader(is,"gb2312"));//字符流编码，防止返回数据乱码
                	response =IOUtils.toString(is);
//                    response = method.getResponseBodyAsString(); 
                } 
        } catch (URIException e) { 
                e.printStackTrace();
        } catch (IOException e) { 
                e.printStackTrace();
        } finally { 
                method.releaseConnection(); 
        }
        
        return response; 

	}

	public static String invokeDoPost(String url, NameValuePair[] data) {
		String response = "";
		PostMethod method = null;
		try {
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(60000);// 请求超时
			method = new PostMethod(url);
			method.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setRequestBody(data);
			client.executeMethod(method);
			// if (method.getStatusCode() == HttpStatus.SC_OK)
			// {
			response = method.getResponseBodyAsString();
			// }
		} catch (URIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return response;

	}

	/**
	 * 获取返回的js数据，并将gzip进行转换
	 * 
	 * @param url
	 * @return
	 */
	public static String getJs(String url) {
		String response = "";
		HttpClient client = new HttpClient();
		Map<String, Object> map = Config.getIp();
		String ip = (String) map.get("ip");
		if (StringUtils.isNotBlank(ip)) {// 代理数据不为空，使用代理
			// 代理的主机
			ProxyHost proxy = new ProxyHost(ip, Integer.valueOf((String) map.get("port")));
			// 使用代理
			client.getHostConfiguration().setProxyHost(proxy);
		}
		client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);// 请求超时
		HttpMethod method = new GetMethod(url);
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");		
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				// response的流是gzip，所以需要转换一下
				InputStream is = new GZIPInputStream(method.getResponseBodyAsStream());
				// System.out.println(IOUtils.toString(is));
				// System.out.println(method.getResponseBodyAsString());
				response = IOUtils.toString(is,"UTF-8");
			}
		} catch (URIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return response;

	}

	/**
	 * 设置http请求头文件
	 * @param method
     */
	private static void setHeaders(HttpMethod method) {
		method.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
		method.setRequestHeader("Accept-Language", "zh-cn");
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0");
		method.setRequestHeader("Accept-Charset", "utf-8");
		method.setRequestHeader("Keep-Alive", "300");
		method.setRequestHeader("Connection", "Keep-Alive");
		method.setRequestHeader("Cache-Control", "no-cache");
	}




	public static String gethtmlcss(String cssLink) {
		String response = "";
		HttpClient client = new HttpClient();
		Map<String,Object> map=Config.getIp();
		String ip=(String) map.get("ip");
		if(StringUtils.isNotBlank(ip)){//代理数据不为空，使用代理
			// 代理的主机
			ProxyHost proxy = new ProxyHost(ip , Integer.valueOf((String) map.get("port")));
			// 使用代理
			client.getHostConfiguration().setProxyHost(proxy);
			//抢先认证模式也提供对于特定目标或代理的缺省认证。如果没有提供缺省的认证信息，则该模式会失效。
//        client.getParams().setAuthenticationPreemptive(true);

		}
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(60000);// 请求超时
		HttpMethod method = new GetMethod(cssLink);
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0");// 模拟浏览器信息
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		method.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.BROWSER_COMPATIBILITY);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
		method.getParams().setParameter("Accept-Encoding","gzip, deflate, sdch");
		method.getParams().setParameter("Accept-Language","zh-CN,zh;q=0.8");
		method.getParams().setParameter("Host","p0.meituan.net");
		method.setRequestHeader("Connection", "Keep-Alive");
		method.setRequestHeader("Upgrade-Insecure-Requests","1");
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				// BufferedReader br = new BufferedReader(new
				// InputStreamReader(is,"gb2312"));//字符流编码，防止返回数据乱码
				response = IOUtils.toString(is);
				// response = method.getResponseBodyAsString();
			}
		} catch (URIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return response;

	}



	public static String gethtml(String url) throws Exception {
		URL u = new URL(url);
		URLConnection conn = u.openConnection();
		conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		conn.connect();
//		conn.setDoOutput(true);
//		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		//write parameters
//		writer.write(data);
//		writer.flush();
		InputStream in = conn.getInputStream();
		GZIPInputStream gzin = new GZIPInputStream(in);
		BufferedReader bin = new BufferedReader(new InputStreamReader(gzin, "utf-8"));

		// Get the response
		StringBuffer answer = new StringBuffer();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
		String line;
		while ((line = bin.readLine()) != null) {
			answer.append(line+"\n");
		}
//		writer.close();
		bin.close();

		//Output the response
//		System.out.println(answer.toString());
		return answer.toString();
	}


}
