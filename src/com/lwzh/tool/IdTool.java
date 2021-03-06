package com.lwzh.tool;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

public class IdTool {

	public static String uuid() {
		return uuid16();
	}

	public static String uuid64() {
		UUID uuid = UUID.randomUUID();
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		return Base64.encodeBase64URLSafeString(bb.array());
	}

	public static String uuid16() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "").toLowerCase();
	}

	public static String randomNumeric(int length) {
		return RandomStringUtils.randomNumeric(length);
	}

	public static String randomAlphabetic(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}

	public static String randomAlphanumeric(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	private static final SimpleDateFormat timeSdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	synchronized public static String timeId() {
		StringBuilder sb = new StringBuilder();
		sb.append(timeSdf.format(System.currentTimeMillis()));
		sb.append(randomNumeric(5));
		return sb.toString();
	}

}
