package com.lwzh.tool;

import com.alibaba.fastjson.JSON;

public class JsonTool {

	public static String toJson(Object obj) {
		return JSON.toJSONString(obj);
	}

	public static Object parse(String json) {
		return JSON.parse(json);
	}

}
