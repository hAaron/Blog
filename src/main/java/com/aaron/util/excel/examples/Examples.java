package com.aaron.util.excel.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class Examples {

    public static void main(String[] agrs ) throws IOException, Exception{
    	
    	Excel2EntityConfig config = new Excel2EntityConfig();
		String[] columns = {"name", "password", "birthday"};
		config.setColumns(columns);
//		//�������ڵĸ�ʽ����Excel������ڸ�ʽһ��
		config.setFormater(new SimpleDateFormat(
						"yyyy.MM.dd"));
		//���ôӵ��п�ʼ��������ǰ1��
		config.setCurrPosittion(2);
		//���ôӵڶ��п�ʼ��ȡ�����Ե�һ�е��������
		config.setColStartPosittion(0);
		ExcelReader<TestEntity> excel = new ExcelReader<TestEntity>();
		excel.setExcel2EntityConfig(config);
		
		File file = new File("e:\\temp\\testEntity.xls"); //��testEntity.xls�ļ����Ƶ�d:
    	InputStream input = new FileInputStream(file);  
    	//�������EXCEl������������Ķ�ȡ���⣬�뽫InputStream���� ByteArrayInputStream �ɽ������
    	//ByteArrayInputStream input = new ByteArrayInputStream(byte[])	
    	excel.InitExcelReader(input); 
		try {
			TestEntity entity = new TestEntity();
			excel.setEntity(entity);
			entity = excel.readLine();
			long start = System.currentTimeMillis();
			while (entity != null) {				
				System.out.print(entity.getName()+" ");
				System.out.print(entity.getPassword()+" ");
				System.out.println(new SimpleDateFormat("yyyy.MM.dd").format(entity.getBirthday()));
				///����ʵ�����
				//entity = new TestEntity();
				//excel.setEntity(entity);
				entity = excel.readLine();
			}
			long end = System.currentTimeMillis();
			System.out.println("��ʱ��"+(end-start));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			input.close();
		}   	
    	
    }
}
