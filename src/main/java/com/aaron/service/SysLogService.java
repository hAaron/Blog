package com.aaron.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.aaron.entity.sys.SysLog;

/**
 * ��־�ӿڵķ���������Ҫע�⣬�����aop�����execution ���ʽ��ͻ�������������ѭ��
 * 
 * @author Aaron
 * @date 2017��6��27��
 * @version 1.0
 * @package_name com.aaron.service
 */
public interface SysLogService {

	int insertLog(SysLog sysLog);
	
	int removeLog(SysLog sysLog);
	
	int changeLog(SysLog sysLog);
	
	SysLog findLogById(int id);
	
	List<SysLog> findAll();

	List<SysLog> list(Map<String, Object> map);

	Long getTotal(Map<String, Object> map);
	
	public void exprot(List<SysLog> sysLogs);

	int insertLogs(List<SysLog> sysLogs);
}
