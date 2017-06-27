package com.aaron.intercepter;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.aaron.entity.sys.SysLog;
import com.aaron.util.DateUtil;
/**
 * ��־����  spring������
 * 
 * @author Aaron
 * @date 2017��6��26��
 * @version 1.0
 * @package_name com.aaron
 */
public class LogIntercepter implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(LogIntercepter.class);

	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>(
			"ThreadLocal StartTime");

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		long beginTime = System.currentTimeMillis();// 1����ʼʱ��
		startTimeThreadLocal.set(beginTime);
		logger.info("��ʼ��ʱ: {}  URI: {}",
				new SimpleDateFormat("hh:mm:ss.SSS").format(beginTime),
				arg0.getRequestURI());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		if (arg3 != null) {
			logger.info("ViewName: " + arg3.getViewName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String requestURL = request.getRequestURL().toString();
		SysLog log = new SysLog();
		log.setType(ex == null ? SysLog.TYPE_ACCESS : SysLog.TYPE_EXCEPTION);
		log.setRemoteAddr(request.getRemoteAddr());
		log.setUserAgent(request.getHeader("user-agent"));
		log.setRequestUri(request.getRequestURI());
		//log.setParams(request.getParameterMap());
		log.setMethod(request.getMethod());
		
		
		// ������־
		//SysLog.saveLog(log);

		// ��ӡJVM��Ϣ��
		if (logger.isDebugEnabled()) {
			long beginTime = startTimeThreadLocal.get();// �õ��̰߳󶨵ľֲ���������ʼʱ�䣩
			long endTime = System.currentTimeMillis(); // 2������ʱ��
			logger.debug(
					"��ʱ������{}  ��ʱ��{}  URI: {}  ����ڴ�: {}m  �ѷ����ڴ�: {}m  �ѷ����ڴ��е�ʣ��ռ�: {}m  �������ڴ�: {}m",
					new SimpleDateFormat("hh:mm:ss.SSS").format(endTime),
					DateUtil.formatDateTime(endTime - beginTime), request
							.getRequestURI(),
					Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime
							.getRuntime().totalMemory() / 1024 / 1024, Runtime
							.getRuntime().freeMemory() / 1024 / 1024, (Runtime
							.getRuntime().maxMemory()
							- Runtime.getRuntime().totalMemory() + Runtime
							.getRuntime().freeMemory()) / 1024 / 1024);
			// ɾ���̱߳����е����ݣ���ֹ�ڴ�й©
			startTimeThreadLocal.remove();
		}

	}

}
