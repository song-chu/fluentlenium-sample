package com.legendapl.fluent.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * クラスコメント
 *
 * @author la_song
 *
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassComment {
	/** リポートファイル名 */
	String reportFileName();

	/** コメント */
	String[] value();
}
