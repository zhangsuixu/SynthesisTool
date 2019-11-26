package com.common.tools;

import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5加密
 * 
 * @author 513416
 *
 */
public class MD5 {
	

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	public MD5() {
	}

	public static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return (new StringBuilder())
				.append(hexDigits[d1])
				.append(hexDigits[d2])
				.toString();
	}

	/*** md5加密 要加密的字符串*/
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (NoSuchAlgorithmException e) {
		}
		return resultString;
	}
	
	public static String md5Hex(String message, String encoding) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return hex(md.digest(message.getBytes(encoding)));
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e("Failed to generate md5 string", e);
			throw new RuntimeException("Failed to generate md5 string", e);
		} catch (UnsupportedEncodingException e) {
			LogUtil.e("Failed to generate md5 string", e);
			throw new RuntimeException("Failed to generate md5 string", e);
		}
	}
	
	public static String hex(byte[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(
					1, 3));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
	}












}