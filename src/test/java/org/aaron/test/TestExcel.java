package org.aaron.test;

import java.util.List;

import com.aaron.util.excel.ExcelUtils;

/**
 * ����excel ���뵼�� ������
 * 
 * @author Aaron
 * @date 2017��7��24��
 * @version 1.0
 * @package_name org.aaron.test
 */
public class TestExcel {
	public static void main(String[] args) {
		List<List<String>> list = ExcelUtils.readXlsx(
				"D://dowload//sysLogInf.xlsx", 1);
		System.out.println(list.size());
		for (List<String> result : list) {
			for (String result2 : result) {
				System.out.print(result2 + " / ");
			}
			System.out.println();
			System.out.println("------------------------");
		}
	}
}
