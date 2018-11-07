package com.topjet.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 集合数据/数据数据转换
 * 对象比较
 */
public class ObjectUtils {

    private ObjectUtils() {
        throw new AssertionError();
    }

    /**
     * compare two object
     *
     * @param actual
     * @param expected
     * @return if both are null, return true
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * compare two object
     *
     * @param actual
     * @param expected
     * @return if both are null, return true
     */
    public static boolean isEqualsClass(Object actual, Class expected) {
        if (actual == null)
            return false;
        return actual.getClass().equals(expected);
    }

    /**
     * null Object to empty string
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
    }

    /**
     * convert long array to Long array
     *
     * @param source
     * @return
     */
    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Long array to long array
     *
     * @param source
     * @return
     */
    public static long[] transformLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert int array to Integer array
     *
     * @param source
     * @return
     */
    public static Integer[] transformIntArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Integer array to int array
     *
     * @param source
     * @return
     */
    public static int[] transformIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * compare two object
     *
     * @param v1
     * @param v2
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable) v1).compareTo(v2));
    }

    /**
     * @param objects
     * @return
     */
    public static boolean isEmpty(Object... objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * @param objects
     * @return
     */
    public static int nonemptyCount(Object... objects) {
        int nonemptyCount = 0;
        if (!ArrayUtils.isEmpty(objects)) {
            for (Object obj : objects) {
                if (obj != null) {
                    nonemptyCount++;
                }
            }
        }
        return nonemptyCount;
    }

    /**
     * 将N个对象转换为数组返回
     */
    @SafeVarargs
    public static <U> List<U> nonemptyItems(U... objects) {
        List<U> nonemptyItems = null;
        if (!ArrayUtils.isEmpty(objects)) {
            nonemptyItems = new ArrayList<U>();
            for (U obj : objects) {
                if (obj != null) {
                    nonemptyItems.add(obj);
                }
            }
        }
        return nonemptyItems;
    }
}
