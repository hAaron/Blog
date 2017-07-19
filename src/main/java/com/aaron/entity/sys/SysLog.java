package com.aaron.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Aaron
 * @date 2017��6��27��
 * @version 1.0
 * @package_name com.aaron.entity.sys
 */
public class SysLog implements Serializable {

	private Integer id;

	/**
	 * ��־���ͣ�1��������2���쳣��
	 */
	private String type;

	/**
	 * ����IP��ַ
	 */
	private String remoteAddr;

	/**
	 * �û�����
	 */
	private String userAgent;

	/**
	 * ����URI
	 */
	private String requestUri;

	/**
	 * ������ʽ
	 */
	private String method;

	/**
	 * �����ύ������
	 */
	private String params;

	/**
	 * �쳣��Ϣ
	 */
	private String exception;

	/**
	 * 0:��ɾ�� 1��ɾ��
	 */
	private String isDelete;

	/**
	 * ������
	 */
	private String createBy;

	/**
	 * ����ʱ��
	 */
	private Date createDate;

	/**
	 * �޸���
	 */
	private String updateBy;

	/**
	 * �޸�ʱ��
	 */
	private Date updateDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) {
			return true;
		}
		if (that == null) {
			return false;
		}
		if (getClass() != that.getClass()) {
			return false;
		}
		SysLog other = (SysLog) that;
		return (this.getId() == null ? other.getId() == null : this.getId()
				.equals(other.getId()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "��־��ϢSysLog [type=" + type + ", remoteAddr=" + remoteAddr
				+ ", requestUri=" + requestUri + ", method=" + method
				+ ", params=" + params + ", exception=" + exception + "]";
	}

}