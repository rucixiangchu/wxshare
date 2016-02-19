package com.lwzh.tool;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.lwzh.Constant;
import com.lwzh.tool.md5Crypt.digest.Md5CryptTool;

public class CryptTool {

	private static final String MD5_PREFIX = "$1$";
	private static final String DEFAULT_SALT = "com.lwzh";
	private static final int CRYPT_TIMES = 3;

	public static String randomSalt() {
		return IdTool.randomAlphanumeric(8);
	}

	public static String crypt(String s, String salt, int times) {
		if (salt == null || salt.length() != 8) {
			throw new RuntimeException("Illegal salt : " + salt);
		}
		try {
			for (int i = 0; i < times; i++) {
				s = Md5CryptTool.md5Crypt(s.getBytes(Constant.CHARSET_UTF8), MD5_PREFIX + salt, MD5_PREFIX);
			}
			return s;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String crypt(String s, String salt) {
		return crypt(s, salt, CRYPT_TIMES);
	}

	public static String crypt(String s) {
		return crypt(s, DEFAULT_SALT);
	}

	private static SecretKeySpec genKey(String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes(Constant.CHARSET_UTF8));
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] keyBytes = secretKey.getEncoded();
			return new SecretKeySpec(keyBytes, "AES");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] encrypt(byte[] content, String password) {
		try {
			SecretKeySpec key = genKey(password);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] decrypt(byte[] content, String password) {
		try {
			SecretKeySpec key = genKey(password);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final String DEFAULT_ENCRYPT_PASSWORD = "com.lwzh.encrypt.password";

	public static String encrypt(String content) {
		try {
			byte[] bytes = encrypt(content.getBytes(Constant.CHARSET_UTF8), DEFAULT_ENCRYPT_PASSWORD);
			return Hex.encodeHexString(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String hex) {
		try {
			byte[] bytes = decrypt(Hex.decodeHex(hex.toCharArray()), DEFAULT_ENCRYPT_PASSWORD);
			return new String(bytes, Constant.CHARSET_UTF8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
