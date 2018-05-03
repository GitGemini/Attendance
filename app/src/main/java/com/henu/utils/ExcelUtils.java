package com.henu.utils;

		import java.io.File;
		import java.io.IOException;
		import java.lang.reflect.InvocationTargetException;
		import java.lang.reflect.Method;
		import java.util.List;

		import jxl.SheetSettings;
		import jxl.Workbook;
		import jxl.format.Alignment;
		import jxl.format.Border;
		import jxl.format.BorderLineStyle;
		import jxl.format.Colour;
		import jxl.format.PageOrientation;
		import jxl.format.PaperSize;
		import jxl.format.UnderlineStyle;
		import jxl.write.Label;
		import jxl.write.WritableCellFormat;
		import jxl.write.WritableFont;
		import jxl.write.WritableSheet;
		import jxl.write.WritableWorkbook;
		import jxl.write.WriteException;

/**
 * Excel文件工具类
 *
 * @author Administrator
 *
 */
public class ExcelUtils {

	/**
	 * 将实体类的信息写入Excel文件
	 *
	 * @param fileName
	 *            excel文件名称 如：文件1.excel
	 * @param list
	 *            实体类集合
	 * @param titles
	 *            excel标题名称
	 * @param columnLength
	 *            标题名称宽度
	 * @param fileds
	 *            对应标题所填充的实体类信息（属性名）
	 * @throws IOException
	 * @throws WriteException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> void writeExcel(String fileName, List<T> list, String[] titles, int[] columnLength,
									  String[] fileds) throws Exception {
		// 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
		WritableWorkbook wwb = Workbook.createWorkbook(new File(fileName));
		if (wwb != null) {
			// 创建一个可写入的工作表
			// Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
			WritableSheet ws = wwb.createSheet("sheet1", 0);

			/*
			 * 表头单元格样式的设定 WritableFont.createFont("宋体")：设置字体为宋体 12：设置字体大小
			 * WritableFont.BOLD:设置字体加粗（BOLD：加粗 NO_BOLD：不加粗） false：设置非斜体
			 * UnderlineStyle.NO_UNDERLINE：没有下划线 Colour.BLACK 字体颜色 黑色
			 */
			WritableFont titleFont = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat titleCellFormat = new WritableCellFormat(titleFont);
			// 字休居中
			titleCellFormat.setAlignment(Alignment.CENTRE);
			// 设置单元格背景色：表体为白色
			titleCellFormat.setBackground(Colour.WHITE);
			// 整个表格线为细线、黑色
			titleCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

			WritableFont contentFont = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
			WritableCellFormat contentCellFormat = new WritableCellFormat(contentFont);
			// 字休居中
			contentCellFormat.setAlignment(Alignment.CENTRE);
			// 设置单元格背景色：表体为白色
			contentCellFormat.setBackground(Colour.WHITE);
			// 整个表格线为细线、黑色
			contentCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

			for (int i = 0; i < titles.length; i++) {
				ws.setColumnView(i, columnLength[i]); // 设置列的宽度
				Label label = new Label(i, 0, titles[i], titleCellFormat);
				ws.addCell(label);
			}
			// 填充实体类的基本信息
			for (int j = 0; list != null && !list.isEmpty() && j < list.size(); j++) {
				T t = list.get(j);
				Class clazz = t.getClass();
				String[] contents = new String[fileds.length];
				for (int i = 0; fileds != null && i < fileds.length; i++) {
					String filedName = toUpperCaseFirstOne(fileds[i]);
					Method method = clazz.getMethod(filedName);
					method.setAccessible(true);
					Object obj = method.invoke(t);
					String str = String.valueOf(obj);
					if (str == null || str.equals("null"))
						str = "";
					contents[i] = str;

				}

				for (int n = 0; n < contents.length; n++) {
					// 这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
					Label labelC = new Label(n, j + 1, contents[n], contentCellFormat);
					// 将生成的单元格添加到工作表中
					ws.addCell(labelC);
				}
			} // =========在sheet初始化之后： 设置纸张大小、横向打印=============
			WritableSheet sheet = wwb.getSheet(0);
			SheetSettings setting = sheet.getSettings();
			setting.setOrientation(PageOrientation.LANDSCAPE); // 横向
			// ④用纸的大小
			setting.setPaperSize(PaperSize.A3);
			// ⑥打印开始页号
			setting.setPageStart(1);
			// =======================================================
			// 从内存中写入文件中
			wwb.write();
			// 关闭资源，释放内存
			wwb.close();
		}

	}

	/**
	 * 将第一个字母转换为大写字母并和get拼合成方法
	 * @param origin
	 * @return
	 */
	private static String toUpperCaseFirstOne(String origin){
		StringBuffer sb = new StringBuffer(origin);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		sb.insert(0,"get");
		return sb.toString();
	}
}
