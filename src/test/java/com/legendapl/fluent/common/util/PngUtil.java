package com.legendapl.fluent.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.imageio.plugins.png.PNGMetadata;

/**
 *
 * @author c.k.song
 *
 */
public class PngUtil {

	private PngUtil() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	/**
	 * イメージファイルにメタ情報を設定する
	 * @param imageFile	イメージファイル
	 * @param key		キー
	 * @param value		値
	 * @throws Exception
	 */
	public static void writeCustomData(File imageFile, File outputFile, String key, String value) throws Exception {

		BufferedImage buffImg = ImageIO.read(imageFile);

		ImageWriter writer = ImageIO.getImageWritersByFormatName(getFileExtension(imageFile)).next();

		ImageWriteParam writeParam = writer.getDefaultWriteParam();
		ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);

		//adding metadata
		IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);

		IIOMetadataNode textEntry = new IIOMetadataNode("tEXtEntry");
		textEntry.setAttribute("keyword", key);
		textEntry.setAttribute("value", value);

		IIOMetadataNode text = new IIOMetadataNode("tEXt");
		text.appendChild(textEntry);

		IIOMetadataNode root = new IIOMetadataNode("javax_imageio_png_1.0");
		root.appendChild(text);

		metadata.mergeTree("javax_imageio_png_1.0", root);

		//writing the data
		FileOutputStream fos = null;
		ImageOutputStream stream = null;

		try {
			fos = new FileOutputStream(outputFile);
			stream = ImageIO.createImageOutputStream(fos);
			writer.setOutput(stream);
			writer.write(metadata, new IIOImage(buffImg, null, metadata), writeParam);

			FileUtils.deleteQuietly(imageFile);

		} finally {
			if (fos != null) {
				fos.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}

	/**
	 * イメージファイルからメタ情報を取得する
	 *
	 * @param imageFile	イメージファイル
	 * @param key		メタ情報キー
	 * @return	メタ情報
	 * @throws IOException
	 */
	public static String readCustomData(File imageFile, String key) throws IOException {
		ImageReader imageReader = ImageIO.getImageReadersByFormatName(getFileExtension(imageFile)).next();

		imageReader.setInput(ImageIO.createImageInputStream(imageFile), true);

		// read metadata of first image
		IIOMetadata metadata = imageReader.getImageMetadata(0);

		//this cast helps getting the contents
		PNGMetadata pngmeta = (PNGMetadata)metadata;
		IIOMetadataNode tEXtNode = pngmeta.getStandardTextNode();
		NodeList childNodes = tEXtNode.getChildNodes();

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			String keyword = node.getAttributes().getNamedItem("keyword").getNodeValue();
			String value = node.getAttributes().getNamedItem("value").getNodeValue();
			if (key.equals(keyword)) {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			}
		}
		return null;
	}

	/**
	 * イメージファイルからコメントを取得する
	 *
	 * @param imageFile	イメージファイル
	 * @return	コメント
	 * @throws IOException
	 */
	public static String getComment(File imageFile) throws IOException {
		return readCustomData(imageFile, "comment");
	}

	/**
	 * ファイル拡張子を取得する
	 * @param file	ファイル
	 * @return	拡張子
	 */
	private static String getFileExtension(File file) {
		String fileName = file.getName();
		int lastDot = fileName.lastIndexOf('.');
		return fileName.substring(lastDot + 1);
	}
}
