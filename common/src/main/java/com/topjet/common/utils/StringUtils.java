package com.topjet.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String操作类
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its length is 0 or it is made by space
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * is null or its length is 0
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return
     * false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * get length of CharSequence
     *
     * @param str
     * @return if str is null or empty, return 0, else return
     * {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     *
     * @param obj
     * @return
     */
    public static String nullToEmpty(Object obj) {
        return nullToDefault(obj, "");
    }

    /**
     * @param obj
     * @param defaultStr
     * @return
     */
    public static String nullToDefault(Object obj, String defaultStr) {
        return (obj == null ? defaultStr : (obj instanceof String ? (String) obj : obj.toString()));
    }

    /**
     * capitalize first letter
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     *
     * @param str
     * @return
     * @throws java.io.UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     *
     * @param href
     * @return
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source
                : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;",
                "\"");
    }

    /**
     * transform half width char to full width char
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * @param str
     * @return whether a string is json format
     */
    public static boolean isJsonString(String str) {
        try {
            new JSONObject(str);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * @param origStr
     * @param removeTarget
     * @return
     */
    public static String removeFirst(String origStr, char removeTarget) {
        String strRemoveTarget = String.valueOf(removeTarget);
        if (isEmpty(origStr) || isEmpty(strRemoveTarget)) {
            return origStr;
        }
        if (origStr.startsWith(strRemoveTarget)) {
            return origStr.substring(1, origStr.length());
        } else {
            return origStr;
        }
    }

    /**
     * @param arr
     * @return
     */
    public static String byteArrayToString(byte[] arr) {
        if (arr == null) {
            return null;
        }
        return new String(arr, Charset.forName("utf-8"));
    }

    /**
     * @param resId
     * @param args
     * @return
     */
    public static String format(int resId, Object... args) {
        String template = ResourceUtils.getString(resId);
        return String.format(template, args);
    }

    /**
     * @param str
     * @param args
     * @return
     */
    public static String format(String str, Object... args) {
        return String.format(str, args);

    }

    /**
     * 字符串转boolean
     */
    public static boolean strToBoolean(String data) {
        if ("1".equals(data)) {
            return true;
        }
        if ("0".equals(data)) {
            return false;
        }
        try {
            return Boolean.parseBoolean(data);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 文本拼接
     */
    public static String append(String appendStr, String... strs) {
        if (strs == null && strs.length < 1) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            stringBuffer.append(strs[i]);
            if (i != strs.length - 1) {
                stringBuffer.append(appendStr);
            }
        }

        return stringBuffer.toString();
    }

    /**
     * 引号拼接
     */
    public static String appendWithQuotationMarks(String... strs) {
        return append("", strs);
    }

    /**
     * 使用空格拼接文本
     */
    public static String appendWithSpace(String... strs) {
        return append(" ", strs);
    }

    /**
     * 使用空格拼接文本
     */
    public static String appendWithSpace(Class[] clazz, Object[] object) {
        if (clazz == null || object == null) {
            return null;
        }
        if (clazz.length != object.length) {
            return null;
        }

        String[] strs = new String[clazz.length];
        for (int i = 0; i < clazz.length; i++) {
            Field[] fs = clazz[i].getDeclaredFields();
            try {
                for (int j = 0; j < fs.length; j++) {
                    Field f = fs[j];
                    f.setAccessible(true); // 设置些属性是可以访问的
                    Object val = f.get(object[j]); // 得到此属性的值

                    if (f.getName().equalsIgnoreCase(f.getName()) && f.getType().toString().endsWith("String")) {
                        strs[i] = (String) val;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appendWithSpace(strs);
    }


    /**
     * str 转 long
     *
     * @param str
     * @return
     */
    public static long getLongToString(String str) {
        if (isEmpty(str) || str.equals("null")) {
            return 0;
        } else {
            return Long.parseLong(str);
        }
    }

    /**
     * str 转 int
     *
     * @param str
     * @return
     */
    public static int getIntToString(String str) {
        if (isEmpty(str) || str.equals("null")) {
            return 0;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * str 转 double
     *
     * @param str
     * @return
     */
    public static double getDoubleToString(String str) {
        if (isEmpty(str)) {
            return 0;
        } else {

            return Double.parseDouble(str);
        }
    }

    /**
     * str 转 float
     *
     * @param str
     * @return
     */
    public static float getFloatToString(String str) {
        if (isEmpty(str)) {
            return 0;
        } else {

            return Float.parseFloat(str);
        }
    }
}
