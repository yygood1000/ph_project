package com.topjet.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.topjet.common.config.CConstants.AppkeyConstant;
import com.topjet.common.config.CPersisData;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Base64;

/**
 * AES编码工具类
 * 
 * @author zhangn
 * @version 2.0
 */
public class AESEncodeUtil {

	/**
	 * 密钥算法
	 */
	private static final String KEY_ALGORITHM = "AES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	/*
	 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private static String sKey = CPersisData.getKey();
	private static String ivCode = CPersisData.getKeyIvCode();

	// 加密
	@SuppressLint("TrulyRandom")
	public static String encrypt(String sSrc) {
		try {
			if(StringUtils.isEmpty(sKey)) {
				sKey = CPersisData.getKey();
				ivCode = CPersisData.getKeyIvCode();
			}
			if(TextUtils.isEmpty(sKey) || TextUtils.isEmpty(ivCode)){
				return null;
			}
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			byte[] raw = sKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(ivCode.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
			// System.out.println(ArrayUtils.toString(encrypted));
			return Base64.encodeToString(encrypted, Base64.DEFAULT);// 此处使用BASE64做转码。
		} catch (Exception ex) {
			return null;
		}
	}

	// 解密
	public static String decrypt(String sSrc) {
		try {
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			IvParameterSpec iv = new IvParameterSpec(ivCode.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);// 先用base64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "UTF-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		// String encodeing = "ASCII";
		for (int i = 0; i < 10; i++) {
			// 需要加密的字串
			String cSrc = "我在加班,我在改bug,看你们秀饭吃,我很不开心.我叫沈阳,我为自己代言!";
			System.out.println(cSrc);
			// 加密
			long lStart = System.currentTimeMillis();
			String enString = AESEncodeUtil.encrypt(cSrc);
			System.out.println("加密后的字串是：" + enString);
			// enString = URLEncoder.encode(enString,encodeing);
			// System.out.println("加密后的字串是：URL " + enString);

			long lUseTime = System.currentTimeMillis() - lStart;
			System.out.println("加密耗时：" + lUseTime + "毫秒");
			// 解密
			lStart = System.currentTimeMillis();
			// enString = URLDecoder.decode(enString, encodeing);
			// System.out.println("解密后的字串是： URL " + enString);
			String DeString = AESEncodeUtil.decrypt(enString);
			System.out.println("解密后的字串是：" + DeString);
			lUseTime = System.currentTimeMillis() - lStart;
			System.out.println("解密耗时：" + lUseTime + "毫秒");
		}

	}

}