package com.aaron.util.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aaron.util.DateUtil;

/**
 * POI方式导出Excel工具类 导出/导入
 * 
 * @author Aaron
 * @date 2017年7月21日
 * @version 1.0
 * @package_name com.aaron.util.excel
 */
public class ExcelUtils {

	/**
	 * 
	 * @param response
	 * @param fileName
	 *            文件名称
	 * @param headNameMap
	 *            表头
	 * @param list
	 *            DTO数据
	 */
	public static <T> void exportXlsxByBean(HttpServletResponse response,
			String fileName, Map<String, String> headNameMap, List<T> list) {

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		try {
			for (T t : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				Field[] fields = t.getClass().getDeclaredFields();
				if (fields != null) {
					for (Field field : fields) {
						field.setAccessible(true);
						map.put(field.getName(), field.get(t));
					}
				}
				dataList.add(map);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		exportXlsx(response, fileName, headNameMap, dataList);

	}

	/**
	 * 导出excel 浏览器中下载
	 * 
	 * @param response
	 *            页面响应
	 * @param fileName
	 *            文件名
	 * @param headNameMap
	 *            excel列表头
	 * @param dataList
	 *            元数据
	 */
	public static void exportXlsx(HttpServletResponse response,
			String fileName, Map<String, String> headNameMap,
			List<Map<String, Object>> dataList) {

		Workbook workbook = exportXlsx(fileName, headNameMap, dataList);

		response.setContentType("application/binary;charset=ISO8859_1");

		OutputStream outputStream = null;

		try {
			outputStream = response.getOutputStream();
			String fn = new String(fileName.getBytes(), "ISO8859_1");
			response.setHeader("Content-disposition", "attachment; filename="
					+ fn + ".xlsx");
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 导出数据
	 * 
	 * @param headNameMap
	 * @param dataList
	 */
	public static Workbook exportXlsx(String sheetName,
			Map<String, String> headNameMap, List<Map<String, Object>> dataList) {

		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet(sheetName);

		Set<String> keys = headNameMap.keySet();

		int i = 0, j = 0;
		Row row = sheet.createRow(i++);
		// 表头
		for (String key : keys) {
			Cell cell = row.createCell(j++);
			cell.setCellValue(headNameMap.get(key));
		}

		// 内容
		if (dataList != null && !dataList.isEmpty()) {
			for (Map<String, Object> map : dataList) {
				row = sheet.createRow(i++);
				j = 0;
				for (String key : keys) {
					Cell cell = row.createCell(j++);
					setCellValue(cell, map.get(key));
				}
			}
		}

		return workbook;
	}

	private static void setCellValue(Cell cell, Object obj) {
		if (obj == null) {
		} else if (obj instanceof String) {
			cell.setCellValue((String) obj);
		} else if (obj instanceof Date) {
			Date date = (Date) obj;
			if (date != null) {
				cell.setCellValue(DateUtil.dfDateTime.format(date));
			}
		} else if (obj instanceof Calendar) {
			Calendar calendar = (Calendar) obj;
			if (calendar != null) {
				cell.setCellValue(DateUtil.dfDateTime.format(calendar.getTime()));
			}
		} else if (obj instanceof Timestamp) {
			Timestamp timestamp = (Timestamp) obj;
			if (timestamp != null) {
				cell.setCellValue(DateUtil.dfDateTime.format(new Date(timestamp
						.getTime())));
			}
		} else if (obj instanceof Double) {
			cell.setCellValue((Double) obj);
		} else {
			cell.setCellValue(obj.toString());
		}
	}

	/**
	 * 从excel文件中读取数据，转换成list集合
	 * 
	 * @param path
	 *            文件路径
	 * @param startIdx
	 *            从第{startIdx}行开始读取
	 * @return
	 */
	public static List<List<String>> readXlsx(String path, int startIdx) {

		try {
			InputStream is = new FileInputStream(path);

			return readXlsx(is, startIdx);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 从excel文件中读取数据，转换成list集合
	 * 
	 * @param is
	 *            文件流
	 * @param startIdx
	 *            从第{startIdx}行开始读取
	 * @return
	 */
	public static List<List<String>> readXlsx(InputStream is, int startIdx) {

		List<List<String>> list = new ArrayList<List<String>>();
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			if (xssfSheet == null) {
				return list;
			}
			int endIdx = xssfSheet.getLastRowNum() + 1;
			if (endIdx > startIdx) {
				for (; startIdx < endIdx; startIdx++) {
					XSSFRow xssfRow = xssfSheet.getRow(startIdx);
					if (xssfRow != null) {
						List<String> rowList = new ArrayList<String>();
						int colNum = xssfRow.getLastCellNum();
						boolean isAdd = false;
						for (int i = 0; i < colNum; i++) {
							XSSFCell cell = xssfRow.getCell(i);
							String str = getValue(cell);
							rowList.add(str);
							if (StringUtils.isNotBlank(str)) {
								isAdd = true;
							}
						}
						if (isAdd) {
							list.add(rowList);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	private static String getValue(XSSFCell xssFCell) {
		String str = null;
		if (xssFCell == null) {
			return str;
		}
		if (xssFCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			str = String.valueOf(xssFCell.getBooleanCellValue());
		} else if (xssFCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			str = String.valueOf(new DecimalFormat("#").format(xssFCell
					.getNumericCellValue()));
		} else {
			str = String.valueOf(xssFCell.getStringCellValue());
		}
		return StringUtils.trim(str);
	}

}
