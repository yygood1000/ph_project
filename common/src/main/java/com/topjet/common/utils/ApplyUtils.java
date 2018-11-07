package com.topjet.common.utils;

import com.topjet.common.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yy on 2017/8/9.
 * <p>
 * 字段检测工具类
 */

public class ApplyUtils {
    /**
     * 验证码位数
     */
    private static final int CHECK_CODE_MA_LENTH = 6;

    /**
     * 电话号码正则
     */
    private static final String MOBILE_REG = "^1[3456789]\\d{9}$"; //"^1\\d{10}$";
    private static final String PHONE_REGION_REG = "^(0[1-9][0-9]{1,2})([0-9]{7,8})$";
    private static final String PHONE_400 = "^400([0-9]{7})$";

    /**
     * 检查输入的是否是数字
     */
    public static boolean checkNumber(String txt) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(txt);
        return m.matches();
    }

    /**
     * 注册页面信息检测
     * 对电话号码进行校验
     * 对验证码进行校验
     * 对单个密码进行校验
     */
    public static boolean checkRegisterInfo(String phoneNumber, String password, String verifyCode) {
        // 检测手机号
        if (!PhoneValidator.validateMobile(phoneNumber)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.phone_format_error));
            return false;
        }

        // 验证码格式
        if (StringUtils.isBlank(verifyCode) || verifyCode.length() < CHECK_CODE_MA_LENTH) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.verify_code_error));
            return false;
        }

        // 检测密码格式
        return validatePassword(password);
    }

    /**
     * 检测密码格式
     */
    public static boolean validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_format_error));
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_format_error));
            return false;
        }
        // 不能为重复数字
        if (isSameNumberSequence(password)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_is_same_number));
            return false;
        }
        // 不能为连续数字 正序
        if (isAscendingNumberSequence(password)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_is_sequential));
            return false;
        }
        // 不能为连续数字 倒叙
        if (isDescendingNumberSequence(password)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_is_sequential));
            return false;
        }
        // 不能为 重复字母
        if (isSameLetterSequence(password)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_is_same_letter));
            return false;
        }
        return true;
    }

    /**
     * 重置密码页面 检测密码格式
     *
     * @param password:校验的密码
     */
    public static boolean validatePasswordInReset(String password) {
        if (StringUtils.isEmpty(password)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_format_error));
            return false;
        }
        if (password.length() < 6 || password.length() > 16) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_format_error));
            return false;
        }
        // 不能为重复数字
        if (isSameNumberSequence(password)) {
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_is_too_simple));
            return false;
        }
        if (isSameLetterSequence(password) // 不能为重复字母
                || isSameNumberSequence(password) // 不能为重复数字
                || isAscendingNumberSequence(password) // 不能为连续数字 正序
                || isDescendingNumberSequence(password)) {// 不能为连续数字 倒叙
            Toaster.showShortToast(ResourceUtils.getString(R.string.password_is_too_simple));
            return false;
        }
        return true;
    }


    /**
     * <--------------------------以下为基础检测方法  ----------------------->
     */

    /**
     * 密码不能为重复的数字
     */
    public static boolean isSameNumberSequence(String str) {
        return str.matches("^(\\d)\\1*$");
    }

    /**
     * 字符串不能为重复(字母)
     */
    public static boolean isSameLetterSequence(String str) {
        return str.matches("(\\w)\\1+");
    }

    /**
     * 连续数字
     */
    public static boolean isAscendingNumberSequence(String str) {
        return "0123456789".contains(str);
    }

    /**
     * 连续数字 倒叙
     */
    public static boolean isDescendingNumberSequence(String str) {
        return "9876543210".contains(str);
    }

    /**
     * 检测手机号
     */
    public static boolean validateMobile(String phone) {
        Pattern pattern = Pattern.compile(MOBILE_REG);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 检测400 电话
     */
    public static boolean validPhone400(String phone) {
        Pattern pattern = Pattern.compile(PHONE_400);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 检测 带区号号码
     */
    public static boolean validatePhoneWithRegion(String phone) {
        Pattern pattern = Pattern.compile(PHONE_REGION_REG);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean validatePhoneOrMobile(String phone) {
        return validateMobile(phone) || validatePhoneWithRegion(phone);
    }

}
