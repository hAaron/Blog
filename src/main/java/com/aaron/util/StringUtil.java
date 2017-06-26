package com.aaron.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * �ַ���������
 * 
 * @author
 * 
 */
public class StringUtil {

	/**
	 * �ж��Ƿ��ǿ�
	 * 
	 * @param str
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
	 * �ж��Ƿ��ǿ�
	 * 
	 * @param str
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
	 * 
	 * @param cs
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
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !StringUtil.isBlank(cs);
	}

}
