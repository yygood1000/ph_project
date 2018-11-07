package com.topjet.common.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.topjet.common.resource.bean.OptionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by tsj005 on 2017/9/5.
 * <p>
 * Jaon 字符串与 类/集合的转换
 */

public class DataFormatFactory {

    /**
     * 通过Json 获得单个实体类对象
     */
    public static String toJson(Object obj) {
        String str;
        Gson gson = new Gson();
        str = gson.toJson(obj);
        return str;
    }

    /**
     * 通过Json 获得单个实体类对象
     */
    public static Object getInstanceByJson(Class<?> clazz, String json) {
        Object obj;
        Gson gson = new Gson();
        obj = gson.fromJson(json, clazz);
        return obj;
    }

    /**
     * json to List<class>
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * json to ArrayListList<class>
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    /**
     * 从assets中读取json
     */
    public static String getJsonFromAssets(Context context, String fileName) {
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[2048];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            is.close();
            return baos.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将解析好的集合数据赋值到内存中
     *
     * @param memoryList 内存中的集合
     */
    public static void putDataIntoMemoryList(ArrayList<OptionItem> memoryList, String json) {
        ArrayList<OptionItem> commonDataList = DataFormatFactory.jsonToArrayList(json, OptionItem.class);
        if (commonDataList.size() >= 1) {
            memoryList.clear();
            memoryList.addAll(commonDataList);
        }
    }

    /**
     * 解析json，存入CommonDataDict类中的各个Map中
     * json中存在两项 name 和 categoryId
     */
    private static void parseJsonToMap(Map<String, String> map, String areaJson) {
        try {
            JSONArray josnArr = new JSONArray(areaJson);
            for (int i = 0; i < josnArr.length(); i++) {
                JSONObject LevelJosn = josnArr.optJSONObject(i);
                String name = LevelJosn.getString("name");
                map.put(i + "", name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
