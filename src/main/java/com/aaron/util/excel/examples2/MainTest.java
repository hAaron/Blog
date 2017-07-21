package com.aaron.util.excel.examples2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 
 * 
 * @author Aaron
 * @date 2017��7��13��
 * @version 1.0
 * @package_name com.aaron.util
 */
public class MainTest {

	public static void main(String[] args) throws FileNotFoundException, ExcelException {
		List<String> list = new ArrayList<String>();
		list.add("ss");
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("������·", "line");
		fieldMap.put("վ��", "station_name");
		fieldMap.put("����·��", "route");
		fieldMap.put("�����ڵ�", "node");
		fieldMap.put("վַ", "station_address");
		fieldMap.put("�ʱ�", "zcode");
		fieldMap.put("����ʱ��", "time");
		fieldMap.put("���", "licheng");
		fieldMap.put("�ȼ�", "level");
		fieldMap.put("�ͻ������", "condition");
		String[] uniqueFields={};
		String pahtname = "E:"+File.separator+"temp"+File.separator+"test.xsl";
		//InputStream is = new FileInputStream(new File(pahtname));
		//List<beanEntity> excelToList = ExcelUtil.excelToList(is , "�����߳�վ", beanEntity.class, fieldMap, uniqueFields);
		//System.out.println(excelToList+"@@@@");
		OutputStream os = new FileOutputStream(pahtname);
		ExcelUtil.listToExcel(list, fieldMap, "teset", os);;
	}
}
