package com.aaron.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * �ļ�����
 * 
 * @author Aaron
 * @date 2017��6��13��
 * @version 1.0
 * @package_name com.aaron.util
 */
public class FileUtils {
	
	private static final String FILE_SEP = File.separator;
	
    /**
     * �ļ�copy����
     * @param src
     * @param dest
     */
    public static void copy(InputStream src, OutputStream dest) {
        try {
            byte[] tmp = new byte[1024];
            int len = -1;
            while ((len = src.read(tmp)) != -1)
                dest.write(tmp, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * ���ļ������� ��ֹ����
     * @param fileName
     * @return ʱ���+ԭʼ�ļ��ĺ�׺
     */
    public static String reName(String fileName){
        return new StringBuffer().append(new Date().getTime()).append(fileName.substring(fileName.indexOf("."))).toString();
    }
    
    /**
     * �ļ�����
     * @param fileName reName֮����ļ�����
     * @param content 
     * @param filePath �ļ�����·��
     * @return 
     * @throws IOException
     */
    public static String saveFile(String fileName,InputStream content,String filePath) throws IOException {
        FileOutputStream fos = null;
        StringBuffer contentPath =  new StringBuffer(""); // �����ĵ�ַ
        try {
            contentPath.append(FILE_SEP);     
            contentPath.append(fileName); 
            
            File pictureFile = new File(filePath + contentPath.toString());
            File pf = pictureFile.getParentFile();
            if(!pf.exists()){
                pf.mkdirs();
            }
            pictureFile.createNewFile();    // �����ļ�
            fos = new FileOutputStream(pictureFile);
            FileUtils.copy(content, fos);
        } catch (Exception e) {
            throw new IOException("�ļ�����ʧ��!");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    throw new IOException("�ļ�����ʧ��!");
                }
            }
        }
        return contentPath.toString();
    }
    
    public static InputStream uploadToStream(HttpServletRequest request,String fileName) throws Exception{
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = mRequest.getFileMap();
		Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, MultipartFile> entry = it.next();
			MultipartFile mFile = entry.getValue();
			String name = mFile.getOriginalFilename();
			if(0!=mFile.getSize() && fileName.equals(name)){
				return mFile.getInputStream();
			}
		}
		return null;
		
	}
	
	public static File transToFile(MultipartFile file,HttpServletRequest request){
		if(!file.isEmpty()){
			String url = request.getSession().getServletContext().getRealPath("/") + "/tmp_upload/" + file.getOriginalFilename();
			File f = new File(url);
			if(f.exists())
				f.delete();
			try {
				file.transferTo(f);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return f;
		}
		else
			return null;
	}
	
	public static void download(HttpServletRequest request,HttpServletResponse response,String contentType,
		String realName,InputStream from) throws IOException{
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-disposition", "attachment; filename="  
                + new String(realName.getBytes("utf-8"), "ISO8859-1"));
		BufferedInputStream bis = new BufferedInputStream(from);
		BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
		FileUtils.write(bis, bos);
		response.flushBuffer();
	}
	
	public static void write(InputStream in, OutputStream out) throws IOException{
        try{
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            in.close();
            out.close();
        }
        catch (IOException ex) {
        	ex.printStackTrace();
        }
        
    } 
}

