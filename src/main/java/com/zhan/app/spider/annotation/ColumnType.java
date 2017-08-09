package com.zhan.app.spider.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnType {
	Type value() default Type.IGNORE_PERSISTENCE;
}
