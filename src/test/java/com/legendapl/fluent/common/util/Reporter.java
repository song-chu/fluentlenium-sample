package com.legendapl.fluent.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.legendapl.fluent.common.annotation.ClassComment;
import com.legendapl.fluent.common.annotation.MethodComment;
import com.legendapl.fluent.common.env.Env;

/**
 * Seleniumテストにて作成したイメージファイルをExcelに纏めるクラス
 *
 * @author c.k.song
 *
 */
public class Reporter {

	/** ログ */
	private static Log log = LogFactory.getLog(Reporter.class);

	/**
	 * リポートファイルからworkbookを取得する。<br>
	 * 既に存在する場合は、既存workbookをリターンする。
	 * @param report	リポートファイル
	 * @return	workbook
	 * @throws Exception
	 */
	private static Workbook getWorkBook(File report) throws Exception {

		if (!report.exists()) {
			return new HSSFWorkbook();

		} else {
			POIFSFileSystem in = new POIFSFileSystem(new FileInputStream(report));
			return new HSSFWorkbook(in);
		}
	}

	/**
	 * リポートファイルを取得する。<br>
	 * "@ClassComment"から取得する。
	 * @param testClass		テストクラス
	 * @return	リポートファイル
	 */
	private static File getReportFile(Class<?> testClass) {
		String reportDirPath = Env.value("dir.report");

		File reportDir = new File(reportDirPath);

		if (!reportDir.exists()) {
			reportDir.mkdirs();
		}

		ClassComment classComment = testClass.getAnnotation(ClassComment.class);
		String reportFileName = classComment.reportFileName();

		File report = new File(reportDir, reportFileName);

		log.info("report file path=" + report.getAbsolutePath());

		return report;
	}

	/**
	 * シートを新規生成する。<br>
	 * 既存シートがあると、削除する。
	 * @param wb			workbook
	 * @param sheetName		シート名
	 * @return	シート
	 * @throws Exception
	 */
	private static Sheet getWorksheet(Workbook wb, String sheetName) throws Exception {

		Sheet sh = wb.getSheet(sheetName);

		if (sh != null) {
			wb.removeSheetAt(wb.getSheetIndex(sheetName));
			log.info("delete sheet:" + sheetName);
		}

		return wb.createSheet(sheetName);
	}

	/**
	 * テストメソッド単位でシート分けをし、イメージファイルを貼り付ける
	 * @param testClass			テストクラス
	 * @param testMethodName	テストメソッド名
	 * @throws Exception
	 */
	public static void makeReport(Class<?> testClass, String testMethodName) throws Exception {

		File report = getReportFile(testClass);

		// excelシートクラス作成
		Workbook wb = getWorkBook(report);

		// テストメソッド名でシートを作成
		Sheet sh = getWorksheet(wb, testMethodName);

		// 列幅設定
		setColumnsWidth(sh);

		// 概要設定
		setSummary(testClass, testMethodName, wb, sh);

		// タイトル行設定
		setTitleRow(wb, sh, "No", "Screen Shot", "Comment");

		// イメージファイルリスト
		String testClassName = testClass.getSimpleName();
		File imageDir = new File(EnvUtils.getScreenShotDirPath(testClassName, testMethodName));
		File[] imageFiles = getSortedFileArray(imageDir);

		int rowIndex = 2;	// ０：概要、１：タイトル

		// スタイル
		CellStyle noStyle = getNoCellType(wb);
		CellStyle commentStyle = getCommentCellType(wb);

		for (File imageFile : imageFiles) {

			if (!imageFile.isFile()) {
				continue;
			}

			//行作成
			Row row = sh.createRow(rowIndex);
			row.setHeightInPoints(430);	// 画像サイズに合わせて指定

			// 番号
			setNoCell(row, rowIndex, 0, noStyle);

			// イメージを貼り付ける
			pasteImage(sh, rowIndex, 1, imageFile);

			// コメント
			String comment = getUserDefinedAttributeFromFile(imageFile, "comment");	// 該当属性はSeleniumテスト時、設定
			setCommentCell(row, 2, comment, commentStyle);

			rowIndex++;
		}

		FileOutputStream out = null;

		try {
			out = new FileOutputStream(report);
			wb.write(out);

		} finally {
			wb.close();

			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 時間順でソートしたファイル配列を取得する。
	 * @param dir	ディレクトリ
	 * @return	ファイルリスト
	 */
	private static File[] getSortedFileArray(File dir) {
		File[] fileList = dir.listFiles();
		Arrays.sort(fileList, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
		return fileList;
	}

	/**
	 * 列幅を設定する
	 * @param sh	シート
	 */
	private static void setColumnsWidth(Sheet sh) {
		sh.setColumnWidth(0, 5 * 256);		// 1列幅
		sh.setColumnWidth(1, 120 * 256);	// 2列幅
		sh.setColumnWidth(2, 20 * 256);		// 3列幅
	}

	/**
	 * 指定ファイルからユーザ定義属性値を取得する
	 * @param file	ファイル
	 * @param key	属性キー
	 *
	 * @return	属性値
	 * @throws IOException
	 */
	private static String getUserDefinedAttributeFromFile(File file, String key) throws IOException {
		String value = FileAttributeUtil.getAttributeValue(file, key);	// ユーザ定義属性を取得する。
		return (value == null ? "" : value);
	}

	/**
	 * 指定位置にイメージファイルを貼り付ける
	 * @param sh		シート
	 * @param rowIndex	行インデックス
	 * @param colIndex	列インデックス
	 * @param imageFile	イメージファイル
	 * @throws Exception
	 */
	private static void pasteImage(Sheet sh, int rowIndex, int colIndex, File imageFile) throws Exception {

		Workbook wb = sh.getWorkbook();

		//画像描画
		Drawing drawing = sh.createDrawingPatriarch();

		/* @param dx1  the x coordinate in EMU within the first cell.
		* @param dy1  the y coordinate in EMU within the first cell.
		* @param dx2  the x coordinate in EMU within the second cell.
		* @param dy2  the y coordinate in EMU within the second cell.
		* @param col1 the column (0 based) of the first cell.
		* @param row1 the row (0 based) of the first cell.
		* @param col2 the column (0 based) of the second cell.
		* @param row2 the row (0 based) of the second cell.
		* */
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, colIndex, rowIndex, colIndex + 1, rowIndex);

		/*
		 * 0 = Move and size with Cells,
		 * 2 = Move but don't size with cells,
		 * 3 = Don't move or size with cells.
		 */
		anchor.setAnchorType(0);

		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

		// イメージファイル作成
		BufferedImage img = ImageIO.read(imageFile);
		ImageIO.write(img, "png", byteArrayOut);

		int picIndex = wb.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG);
		Picture pic = drawing.createPicture(anchor, picIndex);

		//元の画像のサイズに合わせてリサイズ
		pic.resize(1);
		//pic.resize(Math.floor(220 / (double)img.getHeight() * 100) / 100);
		//			pic.resize(1, 1);
	}

	/**
	 * 指定位置にコメントを設定する。
	 * @param row		行
	 * @param colIndex	列インデックス
	 * @param comment	コメント
	 * @param style		シェルスタイル
	 */
	private static void setCommentCell(Row row, int colIndex, String comment, CellStyle style) {
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(style); //Apply style to cell
		cell.setCellValue(new HSSFRichTextString(comment));
	}

	/**
	 * 指定位置に番号を設定する
	 * @param row		行
	 * @param rowIndex	行インデックス
	 * @param colIndex	列インデックス
	 * @param style		シェルスタイル
	 */
	private static void setNoCell(Row row, int rowIndex, int colIndex, CellStyle style) {
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(style); //Apply style to cell
		cell.setCellValue(new HSSFRichTextString(Integer.toString(rowIndex - 1)));
	}

	/**
	 * タイトル行を出力する
	 * @param clazz				クラス
	 * @param testMethodName	テストメソッド名
	 * @param wb				ワークブック
	 * @param sh				シート
	 * @throws Exception
	 */
	private static void setSummary(Class<?> clazz, String testMethodName, Workbook wb, Sheet sh) throws Exception {

		Method method = getMethod(clazz, testMethodName);

		if (method == null) {
			throw new Exception("テストメソッドがありません。");
		}

		MethodComment methodComment = method.getAnnotation(MethodComment.class);
		if (methodComment == null) {
			throw new Exception("指定された型の注釈がありません。");
		}

		String[] statements = methodComment.value();

		// Row, Cell作成
		Row topRow = sh.createRow(0);
		topRow.setHeightInPoints(18 * statements.length);

		Cell cell1 = topRow.createCell(0);
		topRow.createCell(1);
		topRow.createCell(2);

		// セル結合
		sh.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

		//スタイルを生成
		CellStyle style = getSummaryCellType(wb);

		//セルにスタイルをセット
		cell1.setCellStyle(style);

		StringBuilder sb = new StringBuilder();
		for (String statement : statements) {

			statement = statement.replace("[SYSDATE]", getSysDate());
			statement = statement.replace("[CLASSNAME]", clazz.getSimpleName());
			statement = statement.replace("[METHODNAME]", testMethodName);

			sb.append(statement).append("\n");
		}
		cell1.setCellValue(new HSSFRichTextString(sb.toString()));
	}

	/**
	 * システム日時を取得する
	 * @return	システム日時
	 */
	private static String getSysDate() {
		return DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm");
	}

	/**
	 * テストメソッドを取得する
	 * @param clazz			クラス
	 * @param methodName	メソッド名
	 * @return	テストメソッド
	 */
	private static Method getMethod(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getDeclaredMethods();

		for (Method method : methods) {

			if (methodName.equals(method.getName())) {
				return method;
			}
		}

		return null;
	}

	/**
	 * タイトル行を出力する
	 * @param wb		ワークブック
	 * @param sh		シート
	 * @param titles	タイトル項目
	 */
	private static void setTitleRow(Workbook wb, Sheet sh, String... titles) {
		// タイトル行
		Row titleRow = sh.createRow(1);
		titleRow.setHeightInPoints(25);

		for (int i = 0; i < titles.length; i++) {
			Cell cell = titleRow.createCell(i);
			CellStyle style = getTitleCellType(wb);

			cell.setCellStyle(style);
			cell.setCellValue(new HSSFRichTextString(titles[i]));
		}
	}

	/**
	 * コメントシェルの書式を設定する
	 * @param wb	ワークブック
	 * @return	書式スタイル
	 */
	private static CellStyle getNoCellType(Workbook wb) {

		CellStyle style = wb.createCellStyle();

		style.setWrapText(true); //Set wordwrap
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		return style;
	}

	/**
	 * コメントシェルの書式を設定する
	 * @param wb	ワークブック
	 * @return	書式スタイル
	 */
	private static CellStyle getCommentCellType(Workbook wb) {

		CellStyle style = wb.createCellStyle();

		style.setWrapText(true); //Set wordwrap
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		return style;
	}

	/**
	 * コメントシェルの書式を設定する
	 * @param wb	ワークブック
	 * @return	書式スタイル
	 */
	private static CellStyle getTitleCellType(Workbook wb) {

		CellStyle style = wb.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND); // 塗りつぶし

		Font font = wb.createFont();
		font.setColor(IndexedColors.BLUE.getIndex()); // 文字色
		font.setUnderline(Font.U_SINGLE); // 下線
		font.setBold(true);
		style.setFont(font);

		return style;
	}

	/**
	 * 概要行の書式を設定する
	 * @param wb	ワークブック
	 * @return	書式スタイル
	 */
	private static CellStyle getSummaryCellType(Workbook wb) {

		CellStyle style = wb.createCellStyle();

		style.setWrapText(true);  //セル内改行をtrueにする
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND); // 塗りつぶし

		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);

		return style;
	}

}