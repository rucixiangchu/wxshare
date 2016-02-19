package com.lwzh.tool;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.lwzh.Constant;

public class FileTool {

	public static void close(InputStream is) throws IOException {
		if (is != null) {
			is.close();
		}
	}

	public static byte[] readFileBytes(InputStream is) throws IOException {
		if (is == null) {
			return null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(is, baos);
			return baos.toByteArray();
		} finally {
			close(is);
		}
	}

	public static String readFile(InputStream is, String charsetName) throws IOException {
		byte[] bytes = readFileBytes(is);
		if (bytes == null) {
			return null;
		}
		return new String(bytes, charsetName);
	}

	public static Map<String, String> readKvFile(InputStream is, String charsetName) throws IOException {
		if (is == null) {
			return null;
		}
		try {
			Map<String, String> kvMap = new HashMap<String, String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, charsetName));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				int pos = line.indexOf("=");
				if (pos == -1) {
					continue;
				}
				String key = line.substring(0, pos).trim();
				String value = line.substring(pos + 1).trim();
				kvMap.put(key, value);
			}
			return kvMap;
		} finally {
			close(is);
		}
	}

	public static Map<String, String> readKvFile(InputStream is) throws IOException {
		return readKvFile(is, Constant.CHARSET_UTF8);
	}

}
