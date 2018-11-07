package com.topjet.common.resource.dict;

import android.content.Context;

import com.topjet.common.App;
import com.topjet.common.resource.bean.TruckLengthInfo;
import com.topjet.common.resource.bean.TruckTypeAndLength;
import com.topjet.common.resource.bean.TruckTypeInfo;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;


/**
 * 车型车长  预加载
 */

public class TruckDataDict {
    private static String TAG = "TruckDataInit";
    private static final ArrayList<TruckTypeAndLength> trucks = new ArrayList<>();

    private static ArrayList<TruckTypeInfo> truckType = new ArrayList<>();//车型列表
    private static ArrayList<TruckLengthInfo> truckLength = new ArrayList<>();//车长列表

    public static final String TRUCK_TYPE = "truckType.json";//车型
    public static final String TRUCK_LEN = "truckLength.json";//车长

    /**
     * 初始化车长数据
     */
    public synchronized static void initTruckLengthFromAssets(final Context context) {
        if (context == null) {
            return;
        }
        truckLength.clear();
        new Thread() {
            public void run() {
                Logger.d(TAG, "初始化车长数据 from assets");
                String strJson = DataFormatFactory.getJsonFromAssets(context, TRUCK_LEN);
                parseTruckLengthJsonToList(strJson);
            }
        }.start();
    }

    /**
     * 初始化车长数据
     */
    public synchronized static void initTruckTypeFromAssets(final Context context) {
        if (context == null) {
            return;
        }
        truckType.clear();
        new Thread() {
            public void run() {
                Logger.d(TAG, "初始化车长数据 from assets");
                String strJson = DataFormatFactory.getJsonFromAssets(context, TRUCK_TYPE);
                parseTruckTypeJsonToList(strJson);
            }
        }.start();
    }

    /**
     * 读取sd卡车长数据（从文件里读取）
     */
    public synchronized static void readTruckLengthFromSdCard() {

        new Thread() {
            public void run() {
                try {
                    Logger.d(TAG, "初始化车长数据 from SdCard");
                    String filePath = PathHelper.getTruckLengthPath(App.getInstance());
                    if (FileUtils.isFileExist(filePath)) {
                        //SD卡上存在数据
                        StringBuilder strJson = FileUtils.readFile(filePath, "UTF-8");
                        if (strJson != null) {
                            if (StringUtils.isNotBlank(strJson.toString())) {
                                parseTruckLengthJsonToList(strJson.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 读取sd卡车型数据（从文件里读取）
     */
    public synchronized static void readTruckTypeFromSdCard() {
        new Thread() {
            public void run() {
                try {
                    Logger.d(TAG, "初始化车型数据 from SdCard");
                    String filePath = PathHelper.getTruckTypePath(App.getInstance());
                    if (FileUtils.isFileExist(filePath)) {
                        //SD卡上存在数据
                        StringBuilder strJson = FileUtils.readFile(filePath, "UTF-8");
                        if (strJson != null) {
                            if (StringUtils.isNotBlank(strJson.toString())) {
                                parseTruckTypeJsonToList(strJson.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 将车长json数据反序列化为ArrayList
     */
    private static void parseTruckLengthJsonToList(String strJson) {
        ArrayList<TruckLengthInfo> tempTruckLength = new ArrayList<>();
        if (StringUtils.isNotBlank(strJson)) {
            tempTruckLength = DataFormatFactory.jsonToArrayList(strJson, TruckLengthInfo.class);
        }

        if (!ListUtils.isEmpty(tempTruckLength)) {
            truckLength.addAll(tempTruckLength);
            Logger.i(TAG, "赋值成功");
        }
    }


    /**
     * 将车型json数据反序列化为ArrayList
     */
    private static void parseTruckTypeJsonToList(String strJson) {
        ArrayList<TruckTypeInfo> tempTruckType = new ArrayList<>();
        if (StringUtils.isNotBlank(strJson)) {
            tempTruckType = DataFormatFactory.jsonToArrayList(strJson, TruckTypeInfo.class);
        }

        if (!ListUtils.isEmpty(tempTruckType)) {
            truckType.addAll(tempTruckType);
            Logger.i(TAG, "赋值成功");
        }
    }

    /**
     * 重置车长数据（从服务端请求过来的）
     */
    public synchronized static void retSetTruckLengthData(final Context context, ArrayList<TruckLengthInfo>
            tempTruckLength) {
        if (context == null || ListUtils.isEmpty(tempTruckLength)) {
            return;
        }

        truckLength = tempTruckLength;
        Logger.i(TAG, "重置成功");
    }

    /**
     * 重置车型数据。（从服务端请求过来的）
     */
    public synchronized static void retSetTruckTypeData(final Context context, ArrayList<TruckTypeInfo> tempTruckType) {
        if (context == null || ListUtils.isEmpty(tempTruckType)) {
            return;
        }

        truckType = tempTruckType;
        Logger.i(TAG, "重置成功");
    }

    /**
     *  ========================================================================================
     *   =======================================API方法=========================================
     *  ========================================================================================
     */

    /**
     * 用车型的中文，获取该车型的车长集合
     */
    public static ArrayList<TruckTypeAndLength> getLength(String tru) {
        ArrayList<TruckTypeAndLength> data = new ArrayList<TruckTypeAndLength>();
        try {
            for (int i = 0; i < trucks.size(); i++) {
                TruckTypeAndLength tr = trucks.get(i);
                String name = tr.getTruckTypeName();
                if (StringUtils.isNotBlank(name)) {
                    if (name.equals(tru)) {
                        data.add(tr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 根据ID 获取车辆对象
     */
    public static TruckTypeAndLength getTruck(String TruckId) {
        TruckTypeAndLength data = null;
        if (StringUtils.isEmpty(TruckId)) {
            return null;
        }
        if (TruckId.equals("0")) {
            return null;
        }

        try {
            for (int i = 0; i < trucks.size(); i++) {
                TruckTypeAndLength tr = trucks.get(i);
                String id = tr.getId();
                if (StringUtils.isNotBlank(id)) {
                    if (TruckId.equals(id)) {
                        data = tr;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取车型列表数据
     */
    public static ArrayList<TruckTypeInfo> getTruckTypeList() {
        return truckType;
    }

    /**
     * 获取车型列表数据
     *
     * @param isShowUnlimited:是否显示不限
     */
    public static ArrayList<TruckTypeInfo> getTruckTypeList(boolean isShowUnlimited) {
        ArrayList<TruckTypeInfo> tempTruckType = new ArrayList<>();
        TruckTypeInfo tempTruckTypeInfo;
        String tempId = "";

        if (isShowUnlimited) {
            //显示不限
            tempTruckType = truckType;
        } else {
            //不显示
            if (!ListUtils.isEmpty(truckType)) {
                for (int i = 0; i < truckType.size(); i++) {
                    tempTruckTypeInfo = truckType.get(i);
                    if (tempTruckTypeInfo != null) {
                        tempId = tempTruckTypeInfo.getId();
                        if (StringUtils.isNotBlank(tempId)) {
                            if (!tempId.equals("1")) {
                                tempTruckType.add(tempTruckTypeInfo);
                            }
                        }
                    }
                }
            }
        }

        return tempTruckType;
    }

    /**
     * 获取车长列表数据
     */
    public static ArrayList<TruckLengthInfo> getTruckLengthList() {
        return truckLength;
    }

    /**
     * 获取车长列表数据
     *
     * @param isShowUnlimited:是否显示不限
     */
    public static ArrayList<TruckLengthInfo> getTruckLengthList(boolean isShowUnlimited) {
        ArrayList<TruckLengthInfo> tempTruckLength = new ArrayList<>();
        TruckLengthInfo tempTruckLengthInfo;
        String tempId = "";

        if (isShowUnlimited) {
            //显示不限
            tempTruckLength = truckLength;
        } else {
            //不显示
            if (!ListUtils.isEmpty(truckLength)) {
                for (int i = 0; i < truckLength.size(); i++) {
                    tempTruckLengthInfo = truckLength.get(i);
                    if (tempTruckLengthInfo != null) {
                        tempId = tempTruckLengthInfo.getId();
                        if (StringUtils.isNotBlank(tempId)) {
                            if (!tempId.equals("1")) {
                                tempTruckLength.add(tempTruckLengthInfo);
                            }
                        }
                    }
                }
            }
        }

        return tempTruckLength;
    }

    /**
     * 根据车长ID查找车长相关信息
     *
     * @param id：车长ID
     */
    public static TruckLengthInfo getTruckLengthById(String id) {
        TruckLengthInfo truckLengthInfo = new TruckLengthInfo();
        TruckLengthInfo tempTruckLengthInfo;
        String tempId = "";

        if (!ListUtils.isEmpty(truckLength)) {
            for (int i = 0; i < truckLength.size(); i++) {
                tempTruckLengthInfo = truckLength.get(i);
                if (tempTruckLengthInfo != null) {
                    tempId = tempTruckLengthInfo.getId();
                    if (StringUtils.isNotBlank(tempId)) {
                        if (tempId.equals(id)) {
                            truckLengthInfo = tempTruckLengthInfo;
                            break;
                        }
                    }
                }
            }
        }

        return truckLengthInfo;
    }

    /**
     * 根据车型ID查找车型相关信息
     *
     * @param id：车型ID
     */
    public static TruckTypeInfo getTruckTypeById(String id) {
        TruckTypeInfo truckTypeInfo = new TruckTypeInfo();
        TruckTypeInfo tempTruckTypeInfo;
        String tempId;

        if (!ListUtils.isEmpty(truckType)) {
            for (int i = 0; i < truckType.size(); i++) {
                tempTruckTypeInfo = truckType.get(i);
                if (tempTruckTypeInfo != null) {
                    tempId = tempTruckTypeInfo.getId();
                    if (StringUtils.isNotBlank(tempId)) {
                        if (tempId.equals(id)) {
                            truckTypeInfo = tempTruckTypeInfo;
                            break;
                        }
                    }
                }
            }
        }

        return truckTypeInfo;
    }

}