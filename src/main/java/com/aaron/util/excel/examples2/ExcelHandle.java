package com.aaron.util.excel.examples2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
/**
 * ��excel���в���������
 *@author xiliang.xiao
 *@date 2015��1��8�� ����1:46:36
 *
 **/
@SuppressWarnings("rawtypes")
public class ExcelHandle {
 
    private Map<String,HashMap[]> tempFileMap  = new HashMap<String,HashMap[]>();
    private Map<String,Map<String,Cell>> cellMap = new HashMap<String,Map<String,Cell>>();
    private Map<String,FileInputStream> tempStream = new HashMap<String, FileInputStream>();
    private Map<String,Workbook> tempWorkbook = new HashMap<String, Workbook>();
    private Map<String,Workbook> dataWorkbook = new HashMap<String, Workbook>();
     
    /**
     * ���޸���
     * @author xiliang.xiao
     *
     */
    class Cell{
        private int column;//��
        private int line;//��
        private CellStyle cellStyle;
 
        public int getColumn() {
            return column;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getLine() {
            return line;
        }
        public void setLine(int line) {
            this.line = line;
        }
        public CellStyle getCellStyle() {
            return cellStyle;
        }
        public void setCellStyle(CellStyle cellStyle) {
            this.cellStyle = cellStyle;
        }
    }
     
    /**
     * ��Excel��������ͬtitle�Ķ�������
     * @param tempFilePath excelģ���ļ�·��
     * @param cellList ��Ҫ�������ݣ�ģ��<!%����ַ�����
     * @param dataList ��������
     * @param sheet ����excel sheet,��0��ʼ
     * @throws IOException 
     */
    public void writeListData(String tempFilePath,List<String> cellList,List<Map<String,Object>> dataList,int sheet) throws IOException{
        //��ȡģ������ʽλ�õ�����
        HashMap temp = getTemp(tempFilePath,sheet);
        //��ģ��Ϊд���
        Workbook temWorkbook = getTempWorkbook(tempFilePath);
        //��ȡ������俪ʼ��
        int startCell = Integer.parseInt((String)temp.get("STARTCELL"));
        //��������sheet
        Sheet wsheet = temWorkbook.getSheetAt(sheet);
        //�Ƴ�ģ�忪ʼ�����ݼ�<!%
        wsheet.removeRow(wsheet.getRow(startCell));
        if(dataList!=null&&dataList.size()>0){
            for(Map<String,Object> map:dataList){
                for(String cell:cellList){
                    //��ȡ��Ӧ��Ԫ������
                    Cell c = getCell(cell,temp,temWorkbook,tempFilePath);
                    //д������
                    ExcelUtil.setValue(wsheet, startCell, c.getColumn(), map.get(cell), c.getCellStyle());
                }
                startCell++;
            }
        }
    }
 
    /**
     * ��ģ����Excel����Ӧ�ط��������
     * @param tempFilePath excelģ���ļ�·��
     * @param cellList ��Ҫ�������ݣ�ģ��<%����ַ�����
     * @param dataMap ��������
     * @param sheet ����excel sheet,��0��ʼ
     * @throws IOException 
     */
    public void writeData(String tempFilePath,List<String> cellList,Map<String,Object> dataMap,int sheet) throws IOException{
        //��ȡģ������ʽλ�õ�����
        HashMap tem = getTemp(tempFilePath,sheet);
        //��ģ��Ϊд���
        Workbook wbModule = getTempWorkbook(tempFilePath);
        //��������sheet
        Sheet wsheet = wbModule.getSheetAt(sheet);
        if(dataMap!=null&&dataMap.size()>0){
            for(String cell:cellList){
                //��ȡ��Ӧ��Ԫ������
                Cell c = getCell(cell,tem,wbModule,tempFilePath);
                ExcelUtil.setValue(wsheet, c.getLine(), c.getColumn(), dataMap.get(cell), c.getCellStyle());
            }
        }
    }
     
    /**
     * Excel�ļ���ֵ
     * @param tempFilePath
     * @param cell
     * @param sheet
     * @return
     * @throws IOException 
     */
    public Object getValue(String tempFilePath,String cell,int sheet,File excelFile) throws IOException{
        //��ȡģ������ʽλ�õ�����
        HashMap tem = getTemp(tempFilePath,sheet);
        //ģ�幤����
        Workbook temWorkbook = getTempWorkbook(tempFilePath);
        //���ݹ�����
        Workbook dataWorkbook = getDataWorkbook(tempFilePath, excelFile);
        //��ȡ��Ӧ��Ԫ������
        Cell c = getCell(cell,tem,temWorkbook,tempFilePath);
        //����sheet
        Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
        return ExcelUtil.getCellValue(dataSheet, c.getLine(), c.getColumn());
    }
     
    /**
     * ��ֵ�б�ֵ
     * @param tempFilePath
     * @param cell
     * @param sheet
     * @return
     * @throws IOException 
     */
    public List<Map<String,Object>> getListValue(String tempFilePath,List<String> cellList,int sheet,File excelFile) throws IOException{
        List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
        //��ȡģ������ʽλ�õ�����
        HashMap tem = getTemp(tempFilePath,sheet);
        //��ȡ������俪ʼ��
        int startCell = Integer.parseInt((String)tem.get("STARTCELL"));
        //��Excel�ļ�ת��Ϊ��������
        Workbook dataWorkbook = getDataWorkbook(tempFilePath,excelFile) ;
        //����sheet
        Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
        //�ļ����һ��
        int lastLine = dataSheet.getLastRowNum();
         
        for(int i=startCell;i<=lastLine;i++){
            dataList.add(getListLineValue(i, tempFilePath, cellList, sheet, excelFile));
        }
        return dataList;
    }
     
    /**
     * ��ֵһ���б�ֵ
     * @param tempFilePath
     * @param cell
     * @param sheet
     * @return
     * @throws IOException 
     */
    public Map<String,Object> getListLineValue(int line,String tempFilePath,List<String> cellList,int sheet,File excelFile) throws IOException{
        Map<String,Object> lineMap = new HashMap<String, Object>();
        //��ȡģ������ʽλ�õ�����
        HashMap tem = getTemp(tempFilePath,sheet);
        //��ģ��Ϊд���
        Workbook temWorkbook = getTempWorkbook(tempFilePath);
        //��Excel�ļ�ת��Ϊ��������
        Workbook dataWorkbook = getDataWorkbook(tempFilePath,excelFile) ;
        //����sheet
        Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
        for(String cell:cellList){
            //��ȡ��Ӧ��Ԫ������
            Cell c = getCell(cell,tem,temWorkbook,tempFilePath);
            lineMap.put(cell, ExcelUtil.getCellValue(dataSheet, line, c.getColumn()));
        }
        return lineMap;
    }
     
     
 
    /**
     * ���ģ��������
     * @param tempFilePath 
     * @return
     * @throws FileNotFoundException 
     */
    private FileInputStream getFileInputStream(String tempFilePath) throws FileNotFoundException {
        if(!tempStream.containsKey(tempFilePath)){
            tempStream.put(tempFilePath, new FileInputStream(tempFilePath));
        }
         
        return tempStream.get(tempFilePath);
    }
 
    /**
     * ������빤����
     * @param tempFilePath
     * @return
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    private Workbook getTempWorkbook(String tempFilePath) throws FileNotFoundException, IOException {
        if(!tempWorkbook.containsKey(tempFilePath)){
            if(tempFilePath.endsWith(".xlsx")){
                tempWorkbook.put(tempFilePath, new XSSFWorkbook(getFileInputStream(tempFilePath)));
            }else if(tempFilePath.endsWith(".xls")){
                tempWorkbook.put(tempFilePath, new HSSFWorkbook(getFileInputStream(tempFilePath)));
            }
        }
        return tempWorkbook.get(tempFilePath);
    }
     
    /**
     * ��ȡ��Ӧ��Ԫ����ʽ����������
     * @param cell
     * @param tem
     * @param wbModule 
     * @param tempFilePath
     * @return
     */
    private Cell getCell(String cell, HashMap tem, Workbook wbModule, String tempFilePath) {
        if(!cellMap.get(tempFilePath).containsKey(cell)){
            Cell c = new Cell();
             
            int[] pos = ExcelUtil.getPos(tem, cell);
            if(pos.length>1){
                c.setLine(pos[1]);
            }
            c.setColumn(pos[0]);
            c.setCellStyle((ExcelUtil.getStyle(tem, cell, wbModule)));
            cellMap.get(tempFilePath).put(cell, c);
        }
        return cellMap.get(tempFilePath).get(cell);
    }
 
    /**
     * ��ȡģ������
     * @param tempFilePath ģ���ļ�·��
     * @param sheet 
     * @return
     * @throws IOException
     */
    private HashMap getTemp(String tempFilePath, int sheet) throws IOException {
        if(!tempFileMap.containsKey(tempFilePath)){
            tempFileMap.put(tempFilePath, ExcelUtil.getTemplateFile(tempFilePath));
            cellMap.put(tempFilePath, new HashMap<String,Cell>());
        }
        return tempFileMap.get(tempFilePath)[sheet];
    }
     
    /**
     * ��Դ�ر�
     * @param tempFilePath ģ���ļ�·��
     * @param os �����
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public void writeAndClose(String tempFilePath,OutputStream os) throws FileNotFoundException, IOException{
        if(getTempWorkbook(tempFilePath)!=null){
            getTempWorkbook(tempFilePath).write(os);
            tempWorkbook.remove(tempFilePath);
        }
        if(getFileInputStream(tempFilePath)!=null){
            getFileInputStream(tempFilePath).close();
            tempStream.remove(tempFilePath);
        }
    }
     
    /**
     * ��ö�ȡ���ݹ�����
     * @param tempFilePath
     * @param excelFile
     * @return
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    private Workbook getDataWorkbook(String tempFilePath, File excelFile) throws FileNotFoundException, IOException {
        if(!dataWorkbook.containsKey(tempFilePath)){
            if(tempFilePath.endsWith(".xlsx")){
                dataWorkbook.put(tempFilePath, new XSSFWorkbook(new FileInputStream(excelFile)));
            }else if(tempFilePath.endsWith(".xls")){
                dataWorkbook.put(tempFilePath, new HSSFWorkbook(new FileInputStream(excelFile)));
            }
        }
        return dataWorkbook.get(tempFilePath);
    }
     
    /**
     * ��ȡ���ݺ�ر�
     * @param tempFilePath
     */
    public void readClose(String tempFilePath){
        dataWorkbook.remove(tempFilePath);
    }
     
    public static void main(String args[]) throws IOException{
    	System.out.println(ExcelHandle.class.getResource("").getPath());
        String tempFilePath = "e:/temp/test.xlsx";//ExcelHandle.class.getResource("test.xlsx").getPath();
        List<String> dataListCell = new ArrayList<String>();
        dataListCell.add("names");
        dataListCell.add("ages");
        dataListCell.add("sexs");
        dataListCell.add("deses");
        List<Map<String,Object>> dataList = new  ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("names", "names");
        map.put("ages", 22);
        map.put("sexs", "��");
        map.put("deses", "����");
        dataList.add(map);
        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("names", "names1");
        map1.put("ages", 23);
        map1.put("sexs", "��");
        map1.put("deses", "����1");
        dataList.add(map1);
        Map<String,Object> map2 = new HashMap<String, Object>();
        map2.put("names", "names2");
        map2.put("ages", 24);
        map2.put("sexs", "Ů");
        map2.put("deses", "����2");
        dataList.add(map2);
        Map<String,Object> map3 = new HashMap<String, Object>();
        map3.put("names", "names3");
        map3.put("ages", 25);
        map3.put("sexs", "��");
        map3.put("deses", "����3");
        dataList.add(map3);
         
        ExcelHandle handle = new  ExcelHandle();
        handle.writeListData(tempFilePath, dataListCell, dataList, 0);
         
        List<String> dataCell = new ArrayList<String>();
        dataCell.add("name");
        dataCell.add("age");
        dataCell.add("sex");
        dataCell.add("des");
        Map<String,Object> dataMap = new  HashMap<String, Object>();
        dataMap.put("name", "name");
        dataMap.put("age", 11);
        dataMap.put("sex", "Ů");
        dataMap.put("des", "����");
         
        handle.writeData(tempFilePath, dataCell, dataMap, 0);
         
        File file = new File("e:/temp/data.xlsx");
        OutputStream os = new FileOutputStream(file);
        //д����������ر���Դ
        handle.writeAndClose(tempFilePath, os);
         
        os.flush();
        os.close();
         
        System.out.println("��ȡд�������----------------------------------%%%");
        System.out.println("name:"+handle.getValue(tempFilePath, "name", 0, file));
        System.out.println("age:"+handle.getValue(tempFilePath, "age", 0, file));
        System.out.println("sex:"+handle.getValue(tempFilePath, "sex", 0, file));
        System.out.println("des:"+handle.getValue(tempFilePath, "des", 0, file));
        System.out.println("��ȡд����б�����----------------------------------%%%");
        List<Map<String,Object>> list = handle.getListValue(tempFilePath, dataListCell, 0, file);
        for(Map<String,Object> data:list){
            for(String key:data.keySet()){
                System.out.print(key+":"+data.get(key)+"--");
            }
            System.out.println("");
        }
         
        handle.readClose(tempFilePath);
    }
     
}