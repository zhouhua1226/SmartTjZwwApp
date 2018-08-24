package com.game.smartremoteapp.protocol;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

/**
 * 功能：MD5协议类 将信息进行加密处理 
 * 
 * @author Administrator 
 */
public class MD5 {

	public final static String MD5_key = "Q56GtyNkop97H3kJfsd80WRQhbFwN98a";

	/**
	 * 将字符串进行加密处理
	 * 
	 * @param text
	 *            要加密的信息
	 * @return 加密后的信息
	 */
	public static String md5(String text) {

		return DigestUtils.md5Hex(getContentBytes(text, "utf-8"));

	}

	/**
	 * 将字符串转成 byte 数组
	 * 
	 * @param content
	 *            内容
	 * @param charset
	 *            编码格式
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
					+ charset);
		}
	}

}
