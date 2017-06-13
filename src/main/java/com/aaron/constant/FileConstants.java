package com.aaron.constant;

import com.aaron.util.PropertiesLoader;

/**
 * �ļ���س���
 * 
 * @author Aaron
 * @date 2017��6��13��
 * @version 1.0
 * @package_name com.aaron.constant
 */
public class FileConstants {
	public static PropertiesLoader loader = null;
	
	public static final String CONFIG_FILE="/config.properties";
	
	static {
		loader = new PropertiesLoader(CONFIG_FILE);
	}

	public static String fileuploadPath = loader.getProperty("fileuploadPath");
	public static String httpPath = loader.getProperty("httpPath");
	
}
