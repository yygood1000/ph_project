package com.topjet.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.topjet.common.App;

import java.util.Map;
import java.util.Set;

public class SPUtils {

    private static SharedPreferences sDefaultSP;

    private static final String SP_DEFAULT_NAME = "configs";


    /**
     * 获取 指定name的 SP 对象
     */
    private static SharedPreferences getSP(String name) {
        return App.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 获取 configs SP 对象
     */
    private static SharedPreferences getSP() {
        if (sDefaultSP == null) {
            return getSP(SP_DEFAULT_NAME);
        }
        return sDefaultSP;
    }

    private static Editor getEditor(String name) {
        return getSP(name).edit();
    }

    /**
     * 获取configs SP 的Editor对象
     */
    private static Editor getEditor() {
        return getSP().edit();
    }

    /**
     * 将configs SP中的所有数据取出
     */
    public static Map<String, ?> getAll() {
        return getSP().getAll();
    }

    /**
     * 往configs SP 中存储
     */
    public static void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public static void putFloat(String key, float value) {
        getEditor().putFloat(key, value).commit();
    }

    public static void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    public static void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public static void putStringSet(String key, Set<String> value) {
        getEditor().putStringSet(key, value).commit();
    }

    /**
     * 从 configs SP 中获取key对应的数据
     *
     * @param key      存储时的key值
     * @param defValue 未取到数据时的默认值
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getSP().getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return getSP().getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getSP().getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getSP().getLong(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return getSP().getString(key, defValue);
    }

    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return getSP().getStringSet(key, defValue);
    }

    /**
     * 获取指定name的SP 中所有数据
     *
     * @param name SP 的名称
     */
    public static Map<String, ?> getAll(String name) {
        return getSP(name).getAll();
    }

    /**
     * 往指定name的SP 中存储数据
     *
     * @param name  SP 的名称
     * @param key   要存储值的key
     * @param value 存储的值
     */
    public static void putBoolean(String name, String key, boolean value) {
        getEditor(name).putBoolean(key, value).commit();
    }

    public static void putFloat(String name, String key, float value) {
        getEditor(name).putFloat(key, value).commit();
    }

    public static void putInt(String name, String key, int value) {
        getEditor(name).putInt(key, value).commit();
    }

    public static void putLong(String name, String key, long value) {
        getEditor(name).putLong(key, value).commit();
    }

    public static void putString(String name, String key, String value) {
        getEditor(name).putString(key, value).commit();
    }


    /**
     * 从指定name的SP 中获取key对应的数据
     *
     * @param name     SP 的名称
     * @param key      存储值的key
     * @param defValue 未取到数据时的默认值
     */
    public static boolean getBoolean(String name, String key, boolean defValue) {
        return getSP(name).getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue, String name) {
        return getSP(name).getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue, String name) {
        return getSP(name).getInt(key, defValue);
    }

    public static long getLong(String key, long defValue, String name) {
        return getSP(name).getLong(key, defValue);
    }

    public static String getString(String key, String defValue, String name) {
        return getSP(name).getString(key, defValue);
    }


    public static final String GUIDE = "Guide";
    public static final String NORMAL = "NORMAL";

    public static boolean saveGuide(Context mContext, String guide) {
        SharedPreferences defaultSharedPreferrence = getGuideSharedPreferences(mContext);
        Editor editor = defaultSharedPreferrence.edit();
        editor.putString("Guide", guide);
        return editor.commit();
    }

    public static String getGuide(Context mContext) {
        SharedPreferences defaultSharedPreferrence = getGuideSharedPreferences(mContext);
        return defaultSharedPreferrence.getString("Guide", "");
    }


    public static SharedPreferences getGuideSharedPreferences(Context mContext) {
        return mContext.getSharedPreferences(GUIDE, Context.MODE_PRIVATE);
    }

    /**
     * Method name: setValueInSharedPreferences <BR>
     * Description: 将键值对存入SharedPreferences <BR>
     * Remark: <BR>
     * void<BR>
     */
    public static void setValueInSharedPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NORMAL, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Method name: getValueInSharedPreferences <BR>
     * Description: 取出参数key对应的value <BR>
     * Remark: <BR>
     *
     * @param key
     * @return String<BR>
     */
    public static String getValueInSharedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NORMAL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }


}
