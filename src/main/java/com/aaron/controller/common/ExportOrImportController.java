package com.aaron.controller.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

import com.aaron.constant.Constants;
import com.aaron.entity.sys.SysLog;
import com.aaron.service.SysLogService;
import com.aaron.util.DateUtil;
import com.aaron.util.FileUtils;
import com.aaron.util.ResponseUtil;
import com.aaron.util.StringUtil;
import com.aaron.util.excel.ExcelUtils;

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
	/**
	 * ��־�ļ����·��
	 */
	public static final String SYS_TEMP_FILE_PATH = "/template/sysLogInf.xlsx";

	@Resource
	SysLogService sysLogService;

	/**
	 * ��־����(Ĭ�ϵ���ȫ����־�ļ�)
	 * 
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/syslog/export")
	public String exportSysLog(HttpServletResponse response) throws Exception {

		// ����1������������ļ�
		List<SysLog> sysLogs = sysLogService.findAll();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// step1:����excel�б�ͷ
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
			// step2:ת��ö�ٳ���
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
				//step3:�������ݼ��� List<Map<String, Object>> Map<key,value> keyΪ��ͷ��valueΪ��Ӧ����ֵ
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
		// step4:������־����
		ExcelUtils.exportXlsx(response, "sysLogInf", headNameMap, list);

		// ����2���Զ��屾�ص���excel�ļ���ŵ�ַ
		// sysLogService.exprot(sysLogs);
		return null;
	}

	/**
	 * ��ȡ��Ŀ·���µ�ģ���ļ�
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getSysLogTemplate")
	public void getSysLogTemplateFile(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("request.getSession().getServletContext().getRealPath()###"
				+ request.getSession().getServletContext().getRealPath(""));
		String formatFilePath = request.getSession().getServletContext()
				.getRealPath(SYS_TEMP_FILE_PATH);
		try {
			FileInputStream fis = new FileInputStream(formatFilePath);
			FileUtils
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

	@RequestMapping("/getExcelPage")
	public String getExcelPage(@RequestParam("parent") String title, Model model) {
		model.addAttribute("pageClass", title);
		return "foreground/excel/excelPage";
	}

	/**
	 * ��־����(����ѡ���ģ�嶨�����ݣ������ļ�����excel�ļ�����ת����ʵ���༯��)
	 * 
	 * @param ids
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/syslog/import")
	public String importSysLog(
			@RequestParam(value = "fileName") String fileName,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<List<String>> list = ExcelUtils.readXlsx(
				FileUtils.uploadToStream(request, fileName), 1);
		List<SysLog> sysLogs = new ArrayList<SysLog>();
		long start = System.currentTimeMillis();
		for (List<String> result : list) {
			SysLog sysLog = new SysLog();
			for (int i = 0; i < result.size();) {
				sysLog.setType("������־".equals(result.get(1)) ? Constants.SYOLOG_TYPE_ACCESS
						: Constants.SYOLOG_TYPE_EXCEPTION);
				sysLog.setRemoteAddr(result.get(2));
				sysLog.setUserAgent(result.get(3));
				sysLog.setRequestUri(result.get(4));
				sysLog.setMethod(result.get(5));
				sysLog.setParams(result.get(6));
				sysLog.setException(result.get(7));
				break;
			}
			sysLogs.add(sysLog);
		}
		sysLogService.insertLogs(sysLogs);
		long end = System.currentTimeMillis();

		logger.info("��־��Ϣ����ɹ���fileName{}" + fileName);
		logger.info("�����ʱ��" + (end - start));
		JSONObject result = new JSONObject();
		result.put("success", "����ɹ�");
		ResponseUtil.write(response, result);
		return null;
	}

}
