package com.topjet.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * Copyright:	Copyright (c)2009
 * Company:		杭州龙驹信息科技
 * Author:		HuLingwei
 * Create at:	2013-6-17 下午4:03:47
 *
 * 修改历史:
 * 日期    作者    版本  修改描述
 * ------------------------------------------------------------------
 *
 * </pre>
 */

public class PhoneValidator {
	private static final String MOBILE_REG = "^1\\d{10}$";
	private static final String PHONE_REGION_REG = "^(0[1-9][0-9]{1,2})([0-9]{7,8})$";
	private static final String PHONE_400 = "^400([0-9]{7})$";

	public static boolean validateMobile(String phone) {
		Pattern pattern = Pattern.compile(MOBILE_REG);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	public static boolean validPhone400(String phone) {
		Pattern pattern = Pattern.compile(PHONE_400);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	public static boolean validatePhoneWithRegion(String phone) {
		Pattern pattern = Pattern.compile(PHONE_REGION_REG);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}

	public static boolean validatePhoneOrMobile(String phone) {
		return validateMobile(phone) || validatePhoneWithRegion(phone);
	}
}
