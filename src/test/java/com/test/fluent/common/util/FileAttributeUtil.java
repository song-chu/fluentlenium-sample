package com.test.fluent.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;

/**
 * ファイルのユーザ定義属性管理
 *
 * @author c.k.song
 *
 */
public class FileAttributeUtil {

	/**
	 * インスタンス生成抑制
	 */
	private FileAttributeUtil() {
	}

	/**
	 * ファイルにメタ情報を設定する
	 * @param file	イメージファイル
	 * @param key	キー
	 * @param value	値
	 * @throws Exception
	 */
	public static void setAttribute(File file, String key, String value) throws Exception {

		Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());

		UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);

		Charset charset = Charset.defaultCharset();
		view.write(key, charset.encode(value));
	}

	/**
	 * ファイルからメタ情報を取得する
	 *
	 * @param file	ファイル
	 * @param key	メタ情報キー
	 * @return	メタ情報
	 * @throws IOException
	 */
	public static String getAttributeValue(File file, String key) throws IOException {

		Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());

		UserDefinedFileAttributeView view = Files.getFileAttributeView(path, UserDefinedFileAttributeView.class);

		ByteBuffer buf = ByteBuffer.allocate(view.size(key));
		view.read(key, buf);
		buf.flip();
		String value = Charset.defaultCharset().decode(buf).toString();

		return value;
	}
}
