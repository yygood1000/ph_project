package com.topjet.common.resource.dict;

import com.topjet.common.R;
import com.topjet.common.resource.bean.OptionItem;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CommonDataDict {
    private static final String UNRESTRICTED = "";


    /**
     * 对应资源的文件名
     * 这块不能改。要改需要把assets中的文件名一起改
     */
    public static final String GOODS_TYPE_FILE_NAME = "goodName.json";//货物名称
    public static final String PACKAGE_TYPE_FILE_NAME = "packagetype.json";//包装方式
    public static final String LOAD_TYPE_FILE_NAME = "loadtype.json";//装卸方式
    public static final String GOODS_UNIT_FILE_NAME = "goodsunit.json";// 货物单位

    private static LinkedHashMap<String, String> sTruckType = new LinkedHashMap<String, String>();
    private static LinkedHashMap<String, String> sTruckLen = new LinkedHashMap<String, String>();
    private static ArrayList<String> sPlateRegion = new ArrayList<String>();
    private static ArrayList<String> sPlateLetter = new ArrayList<String>();
    //2.3版本  货物类型  包装方式  装卸方式三个中      只有装卸方式 与服务端是以id通信
    private static LinkedHashMap<String, String> sCategory = new LinkedHashMap<String, String>();// 货物类型
    private static LinkedHashMap<String, String> sLoadWay = new LinkedHashMap<String, String>();// 装卸方式
    private static LinkedHashMap<String, String> sPackingStyle = new LinkedHashMap<String, String>();//包装方式
    private static LinkedHashMap<String, String> sOrderUnit = new LinkedHashMap<String, String>();// 货物单位


    // 货物类型集合
    private static ArrayList<OptionItem> goodsTypeList = new ArrayList<>();
    // 装卸方式集合
    private static ArrayList<OptionItem> loadTypeList = new ArrayList<>();
    // 包装方式集合
    private static ArrayList<OptionItem> packingTypeList = new ArrayList<>();
    // 货物单位集合
    private static ArrayList<OptionItem> goodsUnitList = new ArrayList<>();
    // 支付方式Map
    private static LinkedHashMap<String, String> sPayType = new LinkedHashMap<String, String>();


    public enum CommonDataType {
        CATEGORY, TRUCK_TYPE, TRUCK_LENGTH, LOAD_WAY, PAY_WAY, PAKING_STYLE, ORDER_UNIT
    }

    public static String getData(CommonDataType type, String key) {
        if (key == null) {
            key = UNRESTRICTED;
        }
        return getMapByType(type).get(key);
    }


    static {
        initData();
    }

    /**
     * 该方法 初始化了：
     * 1.车牌地区
     * 2.车牌字母
     * 3.付款方式
     */
    private static void initData() {
        /*
         * 车牌地区
		 */
        sPlateRegion = (ArrayList<String>) ListUtils.arrayToList(ResourceUtils.getStringArray(R.array.plate_region));

		/*
         * 车牌字母
		 */
        sPlateLetter = (ArrayList<String>) ListUtils.arrayToList(ResourceUtils.getStringArray(R.array.plate_letter));

		/*
         * 支付方式
		 */
        sPayType.put("1", "货到付款");
        sPayType.put("2", "提付全款");
        sPayType.put("3", "提付部分运费");
        sPayType.put("4", "回单付运费");

    }

    public static LinkedHashMap<String, String> getMapByType(CommonDataType type) {
        switch (type) {
            default:
            case CATEGORY:
                return getCategoryData();
            case TRUCK_TYPE:
                return getTruckTypeData();
            case TRUCK_LENGTH:
                return getTruckLenData();
            case LOAD_WAY:
                return getLoadWayData();
            case PAY_WAY:
                return getPayWayData();
            case PAKING_STYLE:
                return getPackingStyleData();
            case ORDER_UNIT:
                return getsOrderUnit();
        }
    }

    public static LinkedHashMap<String, String> getTruckTypeData() {
        return sTruckType;
    }

    public static LinkedHashMap<String, String> getTruckLenData() {
        return sTruckLen;
    }

    public static ArrayList<String> getPlateRegionData() {
        return sPlateRegion;
    }

    public static ArrayList<String> getPlateLetterData() {
        return sPlateLetter;
    }


    /**
     * 获取货物类型集合
     */
    public static ArrayList<OptionItem> getGoodsTypeList() {
        return goodsTypeList;
    }

    /**
     * 获取装卸方式集合
     */
    public static ArrayList<OptionItem> getLoadTypeList() {
        return loadTypeList;
    }

    /**
     * 获取包装方式集合
     */
    public static ArrayList<OptionItem> getPackingTypeList() {
        return packingTypeList;
    }

    /**
     * 获取货物单位集合
     */
    public static ArrayList<OptionItem> getGoodsUnitList() {
        return goodsUnitList;
    }

    public static LinkedHashMap<String, String> getCategoryData() {
        return sCategory;
    }

    public static LinkedHashMap<String, String> getLoadWayData() {
        return sLoadWay;
    }

    public static LinkedHashMap<String, String> getPayWayData() {
        return sPayType;
    }

    public static LinkedHashMap<String, String> getPackingStyleData() {
        return sPackingStyle;
    }

    public static LinkedHashMap<String, String> getsOrderUnit() {
        return sOrderUnit;
    }

    public static void setsOrderUnit(LinkedHashMap<String, String> sOrderUnit) {
        CommonDataDict.sOrderUnit = sOrderUnit;
    }


    public static ArrayList<OptionItem> getPayTypeOptionItems() {
        return getOptionItems(sPayType);
    }


    /**
     * 将LinkedHashMap 中的数据变为ArrayList 数据返回
     */
    private static ArrayList<OptionItem> getOptionItems(Map<String, String> dataMap) {
        ArrayList<OptionItem> optionItems = new ArrayList<OptionItem>();
        Set<Map.Entry<String, String>> entries = dataMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            optionItems.add(new OptionItem(entry.getKey(), entry.getValue()));
        }
        return optionItems;
    }

}
