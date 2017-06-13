package com.aaron.controller.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.aaron.constant.FileConstants;
import com.aaron.util.FileUtils;
import com.aaron.util.PropertiesLoader;

/**
 * ͼƬ�ϴ���nginx
 * 
 * @author Aaron
 * @date 2017��6��13��
 * @version 1.0
 * @package_name com.aaron.controller
 */
@Controller
@RequestMapping("/ueditor/fileupload")
public class FileUpLoadController {

	public static Logger logger = LoggerFactory.getLogger(FileUpLoadController.class); 
	
	// �ļ��ϴ�·��
//	@Resource(name = "fileuploadPath")
//	private String fileuploadPath;

	// �ļ���ȡ·��
//	@Resource(name = "httpPath")
//	private String httpPath;

	/**
	 * �ļ��ϴ�Action
	 * 
	 * @param req
	 * @return UEDITOR ��Ҫ��json��ʽ����
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map<String, Object> upload(HttpServletRequest req) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		MultipartHttpServletRequest mReq = null;
		MultipartFile file = null;
		InputStream is = null;
		String fileName = "";
		// ԭʼ�ļ��� UEDITOR����ҳ��Ԫ��ʱ��alt��title����
		String originalFileName = "";
		String filePath = "";

		try {
			mReq = (MultipartHttpServletRequest) req;
			// ��config.json��ȡ���ϴ��ļ���ID
			file = mReq.getFile("upfile");
			// ȡ���ļ���ԭʼ�ļ�����
			fileName = file.getOriginalFilename();

			originalFileName = fileName;

			if (!StringUtils.isEmpty(fileName)) {
				is = file.getInputStream();
				fileName = FileUtils.reName(fileName);
				filePath = FileUtils.saveFile(fileName, is, FileConstants.fileuploadPath);
			} else {
				throw new IOException("�ļ���Ϊ��!");
			}
			logger.info("�ļ��ϴ��ĵ�ַurl{}"+(FileConstants.httpPath + filePath));
			result.put("state", "SUCCESS");// UEDITOR�Ĺ���:��ΪSUCCESS����ʾstate������
			result.put("url", FileConstants.httpPath + filePath);
			result.put("title", originalFileName);
			result.put("original", originalFileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("####�ļ��ϴ�ʧ��fileName{}"+fileName+"#####�쳣��Ϣ####"+e);
			result.put("state", "�ļ��ϴ�ʧ��!");
			result.put("url", "");
			result.put("title", "");
			result.put("original", "");
		}

		return result;
	}
}
