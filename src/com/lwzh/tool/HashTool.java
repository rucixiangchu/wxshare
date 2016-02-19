package com.lwzh.tool;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class HashTool {

	private static final String UTF_8 = "UTF-8";

	public static String md5(String source) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(source.getBytes(UTF_8));
			byte[] bytes = md5.digest();
			return Hex.encodeHexString(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}