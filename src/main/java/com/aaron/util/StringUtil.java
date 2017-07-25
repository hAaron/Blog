package com.aaron.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * �ַ���������
 * 
 * @author Aaron
 * @date 2017��7��25��
 * @version 1.0
 * @package_name com.aaron.util
 */
public class StringUtil {

	/**
	 * �ж�Դ�ַ����Ƿ�Ϊ��
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �ж�Դ�ַ����Ƿ�Ϊ��
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if ((str != null) && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��ʽ��ģ����ѯ
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @return
	 */
	public static String formatLike(String str) {
		if (isNotEmpty(str)) {
			return "%" + str + "%";
		} else {
			return null;
		}
	}

	/**
	 * ���˵�������Ŀո�
	 * 
	 * @param list
	 *            Դ�ַ�������
	 * @return
	 */
	public static List<String> filterWhite(List<String> list) {
		List<String> resultList = new ArrayList<String>();
		for (String l : list) {
			if (isNotEmpty(l)) {
				resultList.add(l);
			}
		}
		return resultList;
	}

	/**
	 * �ж�Դ�ַ��Ƿ�Ϊ��
	 * 
	 * @param cs
	 *            Դ�ַ�
	 * @return
	 */
	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * �ж�Դ�ַ��Ƿ�Ϊ��
	 * 
	 * @param cs
	 *            Դ�ַ�
	 * @return
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !StringUtil.isBlank(cs);
	}

}
