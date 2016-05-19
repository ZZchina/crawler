/**
 * 
 */
package com.crawler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件操作类
 * @author
 * @date	2014年9月3日
 */
public class Config {
	
	 
	private static Map<String,Object> map=new HashMap<String, Object>();
	
	
	/**
	 * 文件名称
	 */
	public static String FILENAME = "common.properties";
	
	public static  Map<String,Object> getIp(){
		if(map.isEmpty()){
			getConfig();
		}
		return map;
	}
	
	
	/**
	 * 根据配置文件属性名字读取配置文件内容
	 * @return	返回配置文件中的内容String
	 */
	private static Map<String,Object> getConfig(){
		Properties properties = new Properties();
		try {
			InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(FILENAME);//加载文件
			properties.load(inputStream);
			inputStream.close(); // 关闭流
			String ip = properties.getProperty("ip");//获取配置的Ip字符串
			String port = properties.getProperty("port");//获取配置的Ip字符串
			map.put("ip", ip);
			map.put("port", port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 通用获取配置文件的方法（禁止私有化）
	 * @param fileName	配置文件名名称
	 * @param key	配置文件中的属性名
	 * @return	返回配置文件中的内容String
	 */
	public static String getConfig(String fileName,String key){
		String config ="";//定义返回数据变量
		Properties properties = new Properties();
		try {
			InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(fileName);//加载文件
			properties.load(inputStream);
			inputStream.close(); // 关闭流
			config = properties.getProperty(key);//获取配置的key字符串值
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}
}
