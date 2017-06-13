package com.aaron.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

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
}

