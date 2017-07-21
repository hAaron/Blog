package com.aaron.controller.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aaron.constant.Constants;
import com.aaron.entity.sys.SysLog;
import com.aaron.service.SysLogService;
import com.aaron.util.DateUtil;
import com.aaron.util.FileOperationTool;
import com.aaron.util.ResponseUtil;
import com.aaron.util.StringUtil;
import com.aaron.util.excel.ExcelUtils;
import com.aaron.util.excel.examples2.ExcelUtil;

/**
 * �ļ����뵼��
 * 
 * @author Aaron
 * @date 2017��7��18��
 * @version 1.0
 * @package_name com.aaron.controller.common
 */
@Controller
@RequestMapping("/ei")
public class ExportOrImportController {
	public static Logger logger = LoggerFactory
			.getLogger(ExportOrImportController.class);
	@Resource
	SysLogService sysLogService;

	@RequestMapping("/getExcelPage")
	public String getExcelPage(@RequestParam("parent") String title, Model model) {
		model.addAttribute("pageClass", title);
		return "foreground/excel/excelPage";
	}

	/**
	 * ��־����
	 * 
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/syslog/import")
	public String importSysLog(
			@RequestParam(value = "filename") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = request.getParameter("fileName");
		if (file == null)
			return null;
		long size = file.getSize();
		if (fileName == null || ("").equals(fileName) && size == 0)
			return null;
		logger.info("TODO..........");
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}

	/**
	 * ��־����(Ĭ�ϵ���ȫ����־�ļ�)
	 * 
	 * @param ids
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/syslog/export")
	public String exportSysLog(HttpServletResponse response) throws Exception {

		List<SysLog> sysLogs = sysLogService.findAll();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// ����excel�б�ͷ
		Map<String, String> headNameMap = new LinkedHashMap<String, String>();
		headNameMap.put("id", "���");
		headNameMap.put("type", "��־����");
		headNameMap.put("remoteAddr", "����IP��ַ");
		headNameMap.put("userAgent", "�û�����");
		headNameMap.put("requestUri", "����URI");
		headNameMap.put("method", "������ʽ");
		headNameMap.put("params", "�����ύ������");
		headNameMap.put("exception", "�쳣��Ϣ");
		headNameMap.put("isDelete", "ɾ����ʶ");
		headNameMap.put("createBy", "������");
		headNameMap.put("createDate", "����ʱ��");
		headNameMap.put("updateBy", "�޸���");
		headNameMap.put("updateDate", "�޸�ʱ��");

		if (CollectionUtils.isNotEmpty(sysLogs)) {
			// ת��ö�ٳ���
			String type = "������־";// ��־���ͣ�1��������־��2��������־��
			String isDelete = "δɾ��";// ɾ����ʶ('0:δɾ�� 1����ɾ��')
			String createDate = "";
			String updateDate = "";
			for (SysLog sysLog : sysLogs) {
				if (StringUtil.isNotEmpty(sysLog.getType())
						&& Constants.SYOLOG_TYPE_EXCEPTION.equals(sysLog
								.getType())) {
					type = "������־";
				}
				if (StringUtil.isNotEmpty(sysLog.getIsDelete())
						&& Constants.DELETE_YES.equals(sysLog.getIsDelete())) {
					isDelete = "��ɾ��";
				}
				if (sysLog.getCreateDate() != null) {
					createDate = DateUtil.dfDateTime.format(sysLog
							.getCreateDate());
				}
				if (sysLog.getUpdateDate() != null) {
					updateDate = DateUtil.dfDateTime.format(sysLog
							.getUpdateDate());
				}
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("id", sysLog.getId());
				map.put("type", type);
				map.put("remoteAddr", sysLog.getRemoteAddr());
				map.put("userAgent", sysLog.getUserAgent());
				map.put("requestUri", sysLog.getRequestUri());
				map.put("method", sysLog.getMethod());
				map.put("params", sysLog.getParams());
				map.put("exception", sysLog.getException());
				map.put("isDelete", isDelete);
				map.put("createBy", sysLog.getCreateBy());
				map.put("createDate", createDate);
				map.put("updateBy", sysLog.getUpdateBy());
				map.put("updateDate", updateDate);
				list.add(map);
			}
		}
		ExcelUtils.exportXlsx(response, "��־����", headNameMap, list);
		return null;
	}

	@RequestMapping("/getSysLogTemplate")
	public void getSysLogTemplateFile(HttpServletRequest request,
			HttpServletResponse response) {
		String formatFilePath = request.getSession().getServletContext()
				.getRealPath("/template/sysLogInf.xlsx");
		try {
			FileInputStream fis = new FileInputStream(formatFilePath);
			FileOperationTool
					.download(
							request,
							response,
							"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
							"sysLogInf.xlsx", fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
