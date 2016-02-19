package com.lwzh.web.annotation;

public class PParser {

	public static boolean parseEmptyValueAsNull(P classP, P methodP, P parameterP) {
		String config = "true";// 默认值
		if (classP != null && !classP.emptyValueAsNull().isEmpty()) {
			config = classP.emptyValueAsNull();
		}
		if (methodP != null && !methodP.emptyValueAsNull().isEmpty()) {
			config = methodP.emptyValueAsNull();
		}
		if (parameterP != null && !parameterP.emptyValueAsNull().isEmpty()) {
			config = parameterP.emptyValueAsNull();
		}
		boolean ret = Boolean.parseBoolean(config);
		return ret;
	}

	public static String parseDatePattern(P classP, P methodP, P parameterP) {
		String config = "yyyy-MM-dd HH:mm";// 默认值
		if (classP != null && !classP.datePattern().isEmpty()) {
			config = classP.datePattern();
		}
		if (methodP != null && !methodP.datePattern().isEmpty()) {
			config = methodP.datePattern();
		}
		if (parameterP != null && !parameterP.datePattern().isEmpty()) {
			config = parameterP.datePattern();
		}
		String ret = config;
		return ret;
	}

}
