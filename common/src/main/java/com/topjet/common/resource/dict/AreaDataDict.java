package com.topjet.common.resource.dict;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.topjet.common.App;
import com.topjet.common.R;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;

public class AreaDataDict {
    /**
     * 全国的ID
     */
    public static final String FIRST_LEVEL_UNLIMITED_ID = "100000";

    /**
     * 默认的城市信息
     */
    public static final String DEFAULT_CITY_NAME = "上海市";
    public static final String DEFAULT_CITY_CODE = "310000";
    public static final double DEFAULT_CITY_LAT = 31.230416;
    public static final double DEFAULT_CITY_LNG = 121.473701;


    public static final String CITIES = "cities.json";//城市文件

    public final static String NONE_FIRST_LEVEL_UNLIMITED_NAME = ResourceUtils.getString(R.string.hava_not_limit);
    private static final ArrayList<String> sSpecial = new ArrayList<>(); // 特殊行政区域  // 直辖市/港澳台
    private static ArrayList<CityItem> citys = new ArrayList<>();

    /**
     * 初始化数据，从assets中读取。与readCityInSDV5()只执行其中一个
     */
    public synchronized static void initAreaData(final Context context) {
        if (context == null) {
            return;
        }
        // 初始化直辖市集合
        initSpecialFirstLevel();

        new Thread() {
            public void run() {
                Logger.d("ResourcesInitialize", "initAreaData ");
                String areaJson = DataFormatFactory.getJsonFromAssets(context, CITIES);
                parseAreaJsonToList(areaJson);
            }
        }.start();
    }


    /**
     * 读取sd卡城市数据（从本地文件里读取）,与initAreaData()只执行其中一个
     */
    public synchronized static void readCityInSDV5() {
        initSpecialFirstLevel();
        new Thread() {
                public void run() {
                try {
                    Logger.d("ResourcesInitialize", "initAreaData readCityInSDV5 ");
                    String filePath = PathHelper.getCitiesPath(App.getInstance());
                    if (FileUtils.isFileExist(filePath)) {
                        //SD卡上存在数据
                        StringBuilder strJson = FileUtils.readFile(filePath, "UTF-8");
                        if (strJson != null) {
                            if (StringUtils.isNotBlank(strJson.toString())) {
                                parseAreaJsonToList(strJson.toString());
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
     * 初始化直辖市集合
     */
    private static void initSpecialFirstLevel() {
        String[] array = ResourceUtils.getStringArray(R.array.special_area);
        sSpecial.clear();
        sSpecial.addAll(ListUtils.arrayToList(array));
    }

    /**
     * 检查 传入的一级行政区域 是否是特殊行政区 : 直辖市/港澳台
     */
    public static boolean isSpecialFirstLevel(String firstLevel) {
        return sSpecial.contains(firstLevel);
    }


    /**
     * 将获取到的文件中的字符串，装载到ArrayList<CityItem>里（新版城市）
     */
    private static void parseAreaJsonToList(String areaJson) {
        ArrayList<CityItem> tempCities ;
        tempCities =  DataFormatFactory.jsonToArrayList(areaJson, CityItem.class);
        if (!ListUtils.isEmpty(tempCities)){
            citys.clear();
            citys.addAll(tempCities);
        }
    }


    /*
     *  ========================================================================
     *   ===============================API方法=================================
     *  ========================================================================
     */

    /**
     * 获取第一级。即省级城市 。(新版城市)
     */
    public static ArrayList<CityItem> getFirstLevel() {
        ArrayList<CityItem> data = new ArrayList<CityItem>();
        try {
            if (citys != null) {
                for (int i = 0; i < citys.size(); i++) {
                    CityItem ci = citys.get(i);
                    String pid = ci.getAdcode();
                    if (StringUtils.isNotBlank(pid)) {
                        // 六位编码中，后四位为0000的即为省级城市
                        if (pid.endsWith("0000")) {
                            data.add(ci);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return data;
    }


    /**
     * 获取第二级。即省级城市下面的市 。(新版城市)
     *
     * @param item：一级传入参数
     * @param isThroughout：是否添加“全境”
     */
    public static ArrayList<CityItem> getSecondLevel(CityItem item, boolean isThroughout) {
        ArrayList<CityItem> data = new ArrayList<CityItem>();
        try {
            if (item == null)
                return null;

            if (StringUtils.isBlank(item.getAdcode()))
                return null;

            String strCityID = item.getAdcode();// 获取城市ID

            if (citys != null) {
                for (int i = 0; i < citys.size(); i++) {
                    CityItem ci = citys.get(i);
                    String pid = ci.getAdcode();
                    if (StringUtils.isNotBlank(pid)) {
                        String strPid = pid.substring(0, 2);// 截取前2位
                        String StrMid = pid.substring(2, 4);// 截取前中间2位
                        String strPidEnd = pid.substring(pid.length() - 2, pid.length());// 截取后2位
                        String strSid = strCityID.substring(0, 2);// 截取前2位

                        // 比较前2位是否一至
                        if (strSid.equals(strPid)) {
                            // 再判断中间两位是否为00
                            if (!StrMid.equals("00")) {
                                // 再判断后两位是否为00
                                if (strPidEnd.equals("00")) {
                                    data.add(ci);
                                }
                            }

                        }
                    }
                }

                // 判断是否需要添加“全境”选项
                if (isThroughout) {
                    if (data.size() > 0) {
                        CityItem cif = new CityItem();
                        cif.setCityName(NONE_FIRST_LEVEL_UNLIMITED_NAME);
                        data.add(0, cif);
                    }
                }
            }
        } catch (Exception e) {
        }
        return data;
    }


    /**
     * 获取第三级。即区或县 。(新版城市)
     *
     * @param item:二级传入参数
     * @param isThroughout:是否添加“全境”
     */
    public static ArrayList<CityItem> getThirdLevel(CityItem item, boolean isThroughout) {
        ArrayList<CityItem> data = new ArrayList<CityItem>();
        try {
            if (item == null)
                return null;

            if (StringUtils.isBlank(item.getAdcode()))
                return null;

            String strCityID = item.getAdcode();// 获取城市ID

            if (citys != null) {
                for (int i = 0; i < citys.size(); i++) {
                    CityItem ci = citys.get(i);
                    String pid = ci.getAdcode();
                    if (StringUtils.isNotBlank(pid)) {
                        String strPid = pid.substring(0, 4);// 截取前4位
                        String strPidEnd = pid.substring(pid.length() - 2, pid.length());// 截取后2位
                        String strSid = strCityID.substring(0, 4);// 截取前4位

                        // 比较前4位是否一至
                        if (strSid.equals(strPid)) {
                            // 再判断后两位是否为00
                            if (!strPidEnd.equals("00")) {
                                data.add(ci);
                            }
                        }
                    }
                }

                // 判断是否需要添加“全境”选项
                if (isThroughout) {
                    if (data.size() >= 0) {
                        CityItem cif = new CityItem();
                        cif.setCityName(NONE_FIRST_LEVEL_UNLIMITED_NAME);
                        data.add(0, cif);
                    }
                }
            }

        } catch (Exception e) {
        }
        return data;
    }

    /**
     * 通过定位信息获得完整的城市名称，该名称是经过直辖市判断的
     * 例1：“上海市”
     * 例2：“上海 徐汇区”
     * 例3：“浙江省 杭州市 余杭区”
     * 如果定位是失败的，返回“上海市”。
     */
    public static String getFullCityNameByLocation(AMapLocation aMapLocation) {
        StringBuilder fullCityName = new StringBuilder();
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {// 定位成功
            // 判断是否是直辖市
            if (AreaDataDict.isSpecialFirstLevel(aMapLocation.getProvince().replace("市", ""))) {
                //  3级区不为空
                if (StringUtils.isNotBlank(aMapLocation.getDistrict())) {
                    // 上海市 徐汇区
                    fullCityName.append(aMapLocation.getCity().replace("市", ""))
                            .append(" ")
                            .append(aMapLocation.getDistrict());
                } else {
                    // 上海市
                    fullCityName.append(aMapLocation.getCity());
                }
            } else {
                // 浙江省 杭州市 余杭区
                fullCityName.append(aMapLocation.getProvince())
                        .append(" ")
                        .append(aMapLocation.getCity().replace("市", ""))
                        .append(" ")
                        .append(aMapLocation.getDistrict());
            }
        } else {
            // 上海市
            fullCityName.append(DEFAULT_CITY_NAME);
        }
        return fullCityName.toString();
    }

    /**
     * 通过定位信息获得最后一级的城市名称，该名称是经过直辖市判断的
     * 例1：“徐汇区/余杭区”
     * 例2：“上海/杭州”
     * 例3：“上海/浙江”
     */
    public static String getLastCityNameByLocation(AMapLocation aMapLocation) {
        StringBuilder fullCityName = new StringBuilder();
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {// 定位成功
            // 判断是否是直辖市
            if (AreaDataDict.isSpecialFirstLevel(aMapLocation.getProvince().replace("市", ""))) {
                //  3级区不为空
                if (StringUtils.isNotBlank(aMapLocation.getDistrict())) {
                    // 徐汇区
                    fullCityName.append(aMapLocation.getDistrict());
                } else {
                    // 上海
                    fullCityName.append(aMapLocation.getProvince().replace("市", ""));
                }
            } else {
                //  3级区不为空
                if (StringUtils.isNotBlank(aMapLocation.getDistrict())) {
                    // 余杭区
                    fullCityName.append(aMapLocation.getDistrict());
                } else if (StringUtils.isNotBlank(aMapLocation.getCity())) {
                    // 杭州
                    fullCityName.append(aMapLocation.getCity().replace("市", ""));
                } else if (StringUtils.isNotBlank(aMapLocation.getProvince())) {
                    // 杭州
                    fullCityName.append(aMapLocation.getProvince().replace("省", ""));
                }
            }
        } else {
            // 空字符串
            fullCityName.append("");
        }
        return fullCityName.toString();
    }

    /**
     * 根据adcode找CityItem
     * adcode是560存的城市id，即之前的cityid
     */
    public static CityItem getCityItemByAdcode(String adCode) {
        if (citys != null) {
            for (int i = 0; i < citys.size(); i++) {
                CityItem ci = citys.get(i);
                String pid = ci.getAdcode();
                if (StringUtils.isNotBlank(pid)) {
                    if (pid.equals(adCode)) {
                        return ci;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 将城市名去除省市
     */
    public static String replaceCityAndProvinceString(String str) {
        String temp;
        temp = str.replace("市", "").replace("省", "");
        return temp;
    }

    /**
     * 通过adCode 获取3级城市对象
     * 如果是二级adCode 返回值中3级城市为空
     * 如果是一级adCode 返回值中2、3级城市为空
     */
    public static AreaInfo getAreaInfoByAdCode(String adCode){
        AreaInfo areaInfo = new AreaInfo();
        CityItem item = getCityItemByAdcode(adCode);
        if (item !=null){
            if (adCode.endsWith("0000")){
                // 如果adCode结尾是0000，表示该adCode对应一级城市
                areaInfo.setFirstLevel(getCityItemByAdcode(item.getParentId()));
                return areaInfo;
            }else if (adCode.endsWith("00")){
                // 如果adCode结尾是00，表示该adCode对应二级城市
                areaInfo.setSecondLevel(item);
                // 设置一级城市对象
                areaInfo.setFirstLevel(getCityItemByAdcode(item.getParentId()));
                return areaInfo;
            }else{
                areaInfo.setThirdLevel(item);
                areaInfo.setSecondLevel(getCityItemByAdcode(item.getParentId()));
                areaInfo.setFirstLevel(getCityItemByAdcode(getCityItemByAdcode(item.getParentId()).getParentId()));
                return areaInfo;
            }
        }else{
            // 该adCode未找到对应城市对象
            return null;
        }
    }

}
