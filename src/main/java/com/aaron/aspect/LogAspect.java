package com.aaron.aspect;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import com.aaron.entity.sys.SysLog;
import com.aaron.service.SysLogService;

/**
 * ��־���� spring aop @Aspect ע�ⷽʽ
 * 
 * @author Aaron
 * @date 2017��6��27��
 * @version 1.0
 * @package_name com.aaron.aspect
 */
@Aspect
public class LogAspect {

	@Resource
	private SysLogService sysLogService;

	/**
	 * ���ҵ���߼����������
	 */
	@Pointcut("execution(* com.aaron.service.*.*.add*(..))")
	public void insertCell() {
	}

	/**
	 * �޸�ҵ���߼����������
	 */
	@Pointcut("execution(* com.aaron.service.*.*.update*(..))")
	public void updateCell() {
	}

	/**
	 * ɾ��ҵ���߼����������
	 */
	@Pointcut("execution(* com.aaron.service.*.*.delete*(..))")
	public void deleteCell() {
	}

	/**
	 * ��Ӳ�����־(����֪ͨ)
	 * 
	 * @param joinPoint
	 * @param rtv
	 */
	@AfterReturning(value = "insertCell()", argNames = "rtv", returning = "rtv")
	public void insertLog(JoinPoint joinPoint, Object rtv) throws Throwable {
		// TODO
		initSysLog(joinPoint);
	}

	/**
	 * ����Ա�޸Ĳ�����־(����֪ͨ)
	 * 
	 * @param joinPoint
	 * @param rtv
	 * @throws Throwable
	 */
	@AfterReturning(value = "updateCell()", argNames = "rtv", returning = "rtv")
	public void updateLog(JoinPoint joinPoint, Object rtv) throws Throwable {
		// TODO
		initSysLog(joinPoint);
	}

	/**
	 * ɾ������
	 * 
	 * @param joinPoint
	 * @param rtv
	 */
	@AfterReturning(value = "deleteCell()", argNames = "rtv", returning = "rtv")
	public void deleteLog(JoinPoint joinPoint, Object rtv) throws Throwable {
		// TODO
		initSysLog(joinPoint);
	}

	/**
	 * ʹ��Java��������ȡ�����ط���(insert��update)�Ĳ���ֵ�� ������ֵƴ��Ϊ��������
	 * 
	 * @param args
	 * @param mName
	 * @return
	 */
	public String optionContent(Object[] args, String mName) {
		if (args == null) {
			return null;
		}
		StringBuffer rs = new StringBuffer();
		rs.append(mName);
		String className = null;
		int index = 1;
		// ������������
		for (Object info : args) {
			// ��ȡ��������
			className = info.getClass().getName();
			className = className.substring(className.lastIndexOf(".") + 1);
			rs.append("[����" + index + "������:" + className + "��ֵ:");
			// ��ȡ��������з���
			Method[] methods = info.getClass().getDeclaredMethods();
			// �����������ж�get����
			for (Method method : methods) {
				String methodName = method.getName();
				// �ж��ǲ���get����
				if (methodName.indexOf("get") == -1) {// ����get����
					continue;// ������
				}
				Object rsValue = null;
				try {
					// ����get��������ȡ����ֵ
					rsValue = method.invoke(info);
				} catch (Exception e) {
					continue;
				}
				// ��ֵ����������
				rs.append("(" + methodName + ":" + rsValue + ")");
			}
			rs.append("]");
			index++;
		}
		return rs.toString();
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param joinPoint
	 */
	private void initSysLog(JoinPoint joinPoint) {
		// ��ȡ������
		String methodName = joinPoint.getSignature().getName();
		// ��ȡ��������
		String opContent = optionContent(joinPoint.getArgs(), methodName);
		SysLog sysLog = new SysLog();
		sysLog.setMethod(methodName);
		sysLog.setParams(opContent);
		sysLogService.insertLog(sysLog);
	}
}
