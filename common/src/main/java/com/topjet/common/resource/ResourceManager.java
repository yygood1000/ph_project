package com.topjet.common.resource;

import android.content.Context;

import com.topjet.common.App;
import com.topjet.common.config.CPersisData;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.bean.OptionItem;
import com.topjet.common.resource.bean.TruckLengthInfo;
import com.topjet.common.resource.bean.TruckTypeInfo;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.resource.dict.CommonDataDict;
import com.topjet.common.resource.dict.ScrollButtonsDataDict;
import com.topjet.common.resource.dict.TruckDataDict;
import com.topjet.common.resource.response.UpdataResourceResponse;
import com.topjet.common.resource.response.UpdataResourceUrlResponse;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.DownloadUtil;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.StringUtils;

/**
 * 初始化资源操作类,在APP开启的时候初始化资源。
 * 与资源文件更新无关
 */
public class ResourceManager {
    private static String TAG = "ResourceInit";
    /**
     * 资源类型编号。此编号与服务端需要的对应
     */
    static final String RESOURCE_TYPE_SCROLL_BUTTONS = "2";
    static final String RESOURCE_TYPE_TABLAYOUT_BUTTONS = "1";
    static final String RESOURCE_TYPE_CITIES = "3";
    static final String RESOURCE_TYPE_TRUCK_TYPE = "4";
    static final String RESOURCE_TYPE_TRUCK_LENGTH = "5";
    static final String RESOURCE_TYPE_PACKING_TYPE = "6";
    static final String RESOURCE_TYPE_LOAD_TYPE = "7";
    static final String RESOURCE_TYPE_GOODS_TYPE = "8";
    static final String RESOURCE_TYPE_GOODS_UNIT = "9";


    /**
     * 初始化资源文件,在Application中调用
     * <p>
     * 初始化逻辑：
     * 1.判断资源在SD卡相应位置资源文件是否存在。
     * 2.若存在，则读取SD卡中资源文件到内存中。
     * 3.若不存在，则读取Assets文件夹中的资源文件到内存中
     */
    public static void initResourcesData(final Context mContext) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // 城市列表
                if (FileUtils.isFileExist(PathHelper.getCitiesPath(mContext))) {
                    AreaDataDict.readCityInSDV5();
                } else {
                    AreaDataDict.initAreaData(mContext);
                }

                // 车型
                if (FileUtils.isFileExist(PathHelper.getTruckTypePath(mContext))) {
                    TruckDataDict.readTruckTypeFromSdCard();
                } else {
                    TruckDataDict.initTruckTypeFromAssets(mContext);
                }

                // 车长
                if (FileUtils.isFileExist(PathHelper.getTruckLengthPath(mContext))) {
                    TruckDataDict.readTruckLengthFromSdCard();
                } else {
                    TruckDataDict.initTruckLengthFromAssets(mContext);
                }

                // 货物类型（或者叫货物名称）
                initCommonDataDict(mContext, PathHelper.getGoodsNamePath(mContext), RESOURCE_TYPE_GOODS_TYPE,
                        CommonDataDict.GOODS_TYPE_FILE_NAME);

                // 包装方式
                initCommonDataDict(mContext, PathHelper.getPackgeTypePath(mContext), RESOURCE_TYPE_PACKING_TYPE,
                        CommonDataDict.PACKAGE_TYPE_FILE_NAME);

                //  装卸方式
                initCommonDataDict(mContext, PathHelper.getLoadTypePath(mContext), RESOURCE_TYPE_LOAD_TYPE,
                        CommonDataDict.LOAD_TYPE_FILE_NAME);

                // 货物单位
                initCommonDataDict(mContext, PathHelper.getUnitPath(mContext), RESOURCE_TYPE_GOODS_UNIT, CommonDataDict
                        .GOODS_UNIT_FILE_NAME);

                // 滑动按钮组
                ScrollButtonsDataDict.initScrollBtnsData(mContext);
            }
        }.start();
    }


    private static void initCommonDataDict(Context mContext, String resourcePath, String resourceType, String
            resourceFileName) {
        if (FileUtils.isFileExist(resourcePath)) {
            readDataFromSDCard(resourcePath, resourceType);
        } else {
            readDataFromAssets(mContext, resourceFileName, resourceType);
        }
    }

    /**
     * 读取sd卡 中的资源文件:
     */
    private static void readDataFromSDCard(final String filePath, final String resourceType) {
        new Thread() {
            @Override
            public void run() {
                // SD卡上存在数据
                StringBuilder json = FileUtils.readFile(filePath, "UTF-8");
                if (json != null && StringUtils.isNotBlank(json.toString())) {
                    // 将SD中读取的到Json解析成List集合
                    switch (resourceType) {
                        case ResourceManager.RESOURCE_TYPE_GOODS_TYPE://货物类型
                            DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getGoodsTypeList(), json.toString());
                            break;
                        case ResourceManager.RESOURCE_TYPE_PACKING_TYPE://包装方式
                            DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getPackingTypeList(), json
                                    .toString());
                            break;
                        case ResourceManager.RESOURCE_TYPE_LOAD_TYPE://装卸方式
                            DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getLoadTypeList(), json.toString());
                            break;
                        case ResourceManager.RESOURCE_TYPE_GOODS_UNIT:// 货物单位
                            DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getGoodsUnitList(), json.toString());
                            break;
                        default:
                            break;
                    }
                }
            }
        }.start();
    }

    /**
     * 读取Assets 中的资源文件:
     */
    private static void readDataFromAssets(final Context mContext, final String resourceFileName, final String
            resourceType) {
        new Thread() {
            public void run() {
                String json = DataFormatFactory.getJsonFromAssets(mContext, resourceFileName);
                switch (resourceType) {
                    case ResourceManager.RESOURCE_TYPE_GOODS_TYPE://货物类型
                        DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getGoodsTypeList(), json);
                        break;
                    case ResourceManager.RESOURCE_TYPE_PACKING_TYPE://包装方式
                        DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getPackingTypeList(), json);
                        break;
                    case ResourceManager.RESOURCE_TYPE_LOAD_TYPE://装卸方式
                        DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getLoadTypeList(), json);
                        break;
                    case ResourceManager.RESOURCE_TYPE_GOODS_UNIT:// 货物单位
                        DataFormatFactory.putDataIntoMemoryList(CommonDataDict.getGoodsUnitList(), json);
                        break;
                    default:
                        break;
                }
            }
        }.start();
    }

    /**
     * 处理资源更新到的新资源数据
     * <p>
     * 1.更新SP中的资源版本号
     * 2.更新本地文件的资源
     */
    static void processUpdataResourceData(final String resourceType, final
    UpdataResourceUrlResponse newResource) {
        if (StringUtils.isBlank(newResource.getInner_version())) {
            return;
        }
        Logger.d("TTT","processUpdataResourceData---"+resourceType+"------------"+newResource.getInner_version());
        switch (resourceType) {
            case RESOURCE_TYPE_CITIES://城市列表
                if (StringUtils.isNotBlank(newResource.getCity_url())) {
                    DownLoadFile2SDCard(newResource.getCity_url(), PathHelper.getCitiesPath(App.getInstance()),
                            CPersisData.SP_KEY_CITIES_VERSION, newResource.getInner_version());
                }
                break;
            case RESOURCE_TYPE_TRUCK_TYPE://车型
                if (StringUtils.isNotBlank(newResource.getType_url())) {
                    DownLoadFile2SDCard(newResource.getType_url(), PathHelper.getTruckTypePath(App.getInstance()),
                            CPersisData.SP_KEY_TRUCK_TYPE_VERSION, newResource.getInner_version());
                }
                break;
            case RESOURCE_TYPE_TRUCK_LENGTH:// 车长
                if (StringUtils.isNotBlank(newResource.getLength_url())) {
                    DownLoadFile2SDCard(newResource.getLength_url(), PathHelper.getTruckLengthPath(App
                            .getInstance()), CPersisData.SP_KEY_TRUCK_LENGTH_VERSION, newResource
                            .getInner_version());
                }
                break;
            case RESOURCE_TYPE_PACKING_TYPE:// 包装方式
                if (StringUtils.isNotBlank(newResource.getUrl())) {
                    DownLoadFile2SDCard(newResource.getUrl(), PathHelper.getPackgeTypePath(App.getInstance()),
                            CPersisData.SP_KEY_PACKING_TYPE_VERSION, newResource.getInner_version());
                }
                break;
            case RESOURCE_TYPE_LOAD_TYPE:// 装卸方式
                if (StringUtils.isNotBlank(newResource.getUrl())) {
                    DownLoadFile2SDCard(newResource.getUrl(), PathHelper.getLoadTypePath(App.getInstance()),
                            CPersisData.SP_KEY_LOAD_TYPE_VERSION, newResource.getInner_version());
                }
                break;
            case RESOURCE_TYPE_GOODS_TYPE:// 货物类型
                if (StringUtils.isNotBlank(newResource.getUrl())) {
                    DownLoadFile2SDCard(newResource.getUrl(), PathHelper.getGoodsNamePath(App.getInstance()),
                            CPersisData.SP_KEY_GOODS_TYPE_VERSION, newResource.getInner_version());
                }
                break;
            case RESOURCE_TYPE_GOODS_UNIT:// 货物单位
                if (StringUtils.isNotBlank(newResource.getUrl())) {
                    DownLoadFile2SDCard(newResource.getUrl(), PathHelper.getUnitPath(App.getInstance()),
                            CPersisData.SP_KEY_GOODS_UNIT_VERSION, newResource.getInner_version());
                }
                break;
        }

    }

    /**
     * 写文件到SD卡   city文件  车型车长文件 货物名称 包装方式 装卸方式 货物单位
     * 该操作是服务端的资源文件有更新，移动端将更新后的配置写在本地，下次开启使用的是本地的文件
     * 写入文件成功后还需要更改SP 中的资源版本号
     */

    private synchronized static void WriteFile2SDCard(final String json, final String filename, final String
            resourceTypeKey, final String version) {
        // 新的资源文件为空
        if (StringUtils.isBlank(json)) {
            return;
        }
        // 新的资源文件解析后为空
        if (checkNewResourceIsEmpty(json)) {
            return;
        }
        new Thread() {
            public void run() {
                try {
                    // 如果文件存在，则覆盖，否则直接写入，成功后更新version
                    if (FileUtils.writeFile(filename, json, false)) {
                        if (resourceTypeKey.equals(CPersisData.SP_KEY_CITIES_VERSION)) {
                            CPersisData.setCitiesReourceVersion(resourceTypeKey, version);
                        }else if (resourceTypeKey.equals(CPersisData.SP_KEY_TRUCK_TYPE_VERSION)){
                            CPersisData.setTruckTypeReourceVersion(resourceTypeKey, version);
                        }else if (resourceTypeKey.equals(CPersisData.SP_KEY_TRUCK_LENGTH_VERSION)){
                            CPersisData.setTruckLengthReourceVersion(resourceTypeKey, version);
                        }else if (resourceTypeKey.equals(CPersisData.SP_KEY_PACKING_TYPE_VERSION)){
                            CPersisData.setPackingTypeReourceVersion(resourceTypeKey, version);
                        }else if (resourceTypeKey.equals(CPersisData.SP_KEY_LOAD_TYPE_VERSION)){
                            CPersisData.setLoadTypeReourceVersion(resourceTypeKey, version);
                        }else if (resourceTypeKey.equals(CPersisData.SP_KEY_GOODS_TYPE_VERSION)){
                            CPersisData.setGoodsTypeReourceVersion(resourceTypeKey, version);
                        }else if (resourceTypeKey.equals(CPersisData.SP_KEY_GOODS_UNIT_VERSION)){
                            CPersisData.setGoodsUnitReourceVersion(resourceTypeKey, version);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private synchronized static void DownLoadFile2SDCard(final String url, final String filename, final String
            resourceTypeKey, final String version) {
        if (StringUtils.isBlank(url)){
            return;
        }
        Logger.d("TTT","DownLoadFile2SDCard：resourceTypeKey："+resourceTypeKey+"-----"+version);
        DownloadUtil.getInstance().download(resourceTypeKey, version, url, filename, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path, String type, String version) {
                Logger.d("TTT","资源更新成功：resourceTypeKey："+type+"----version:"+version+"--------"+path);

                if (type.equals(CPersisData.SP_KEY_CITIES_VERSION)) {
                    CPersisData.setCitiesReourceVersion(type, version);
                    Logger.d("TTT","CPersisData.setCitiesReourceVersion---"+CPersisData.getCitiesReourceVersion(type));
                    // 城市列表
                    if (FileUtils.isFileExist(PathHelper.getCitiesPath(App.getInstance()))) {
                        AreaDataDict.readCityInSDV5();
                    } else {
                        AreaDataDict.initAreaData(App.getInstance());
                    }
                }else if (type.equals(CPersisData.SP_KEY_TRUCK_TYPE_VERSION)){
                    CPersisData.setTruckTypeReourceVersion(type, version);
                    Logger.d("TTT","CPersisData.setTruckTypeReourceVersion---"+CPersisData.getTruckTypeReourceVersion(type));
                    // 车型
                    if (FileUtils.isFileExist(PathHelper.getTruckTypePath(App.getInstance()))) {
                        TruckDataDict.readTruckTypeFromSdCard();
                    } else {
                        TruckDataDict.initTruckTypeFromAssets(App.getInstance());
                    }
                }else if (type.equals(CPersisData.SP_KEY_TRUCK_LENGTH_VERSION)){
                    CPersisData.setTruckLengthReourceVersion(type, version);
                    Logger.d("TTT","CPersisData.setTruckLengthReourceVersion---"+CPersisData.getTruckLengthReourceVersion(type));

                    // 车长
                    if (FileUtils.isFileExist(PathHelper.getTruckLengthPath(App.getInstance()))) {
                        TruckDataDict.readTruckLengthFromSdCard();
                    } else {
                        TruckDataDict.initTruckLengthFromAssets(App.getInstance());
                    }
                }else if (type.equals(CPersisData.SP_KEY_PACKING_TYPE_VERSION)){
                    CPersisData.setPackingTypeReourceVersion(type, version);
                    Logger.d("TTT","CPersisData.setPackingTypeReourceVersion---"+CPersisData.getPackingTypeReourceVersion(type));

                    // 包装方式
                    initCommonDataDict(App.getInstance(), PathHelper.getPackgeTypePath(App.getInstance()), RESOURCE_TYPE_PACKING_TYPE,
                            CommonDataDict.PACKAGE_TYPE_FILE_NAME);
                }else if (type.equals(CPersisData.SP_KEY_LOAD_TYPE_VERSION)){
                    CPersisData.setLoadTypeReourceVersion(type, version);
                    Logger.d("TTT","CPersisData.setLoadTypeReourceVersion---"+CPersisData.getLoadTypeReourceVersion(type));

                    //  装卸方式
                    initCommonDataDict(App.getInstance(), PathHelper.getLoadTypePath(App.getInstance()), RESOURCE_TYPE_LOAD_TYPE,
                            CommonDataDict.LOAD_TYPE_FILE_NAME);
                }else if (type.equals(CPersisData.SP_KEY_GOODS_TYPE_VERSION)){
                    CPersisData.setGoodsTypeReourceVersion(type, version);
                    Logger.d("TTT","CPersisData.setGoodsTypeReourceVersion---"+CPersisData.getGoodsTypeReourceVersion(type));

                    // 货物类型（或者叫货物名称）
                    initCommonDataDict(App.getInstance(), PathHelper.getGoodsNamePath(App.getInstance()), RESOURCE_TYPE_GOODS_TYPE,
                            CommonDataDict.GOODS_TYPE_FILE_NAME);
                }else if (type.equals(CPersisData.SP_KEY_GOODS_UNIT_VERSION)){
                    CPersisData.setGoodsUnitReourceVersion(type, version);
                    Logger.d("TTT","CPersisData.setGoodsUnitReourceVersion---"+CPersisData.getGoodsUnitReourceVersion(type));

                    // 货物单位
                    initCommonDataDict(App.getInstance(), PathHelper.getUnitPath(App.getInstance()), RESOURCE_TYPE_GOODS_UNIT, CommonDataDict
                            .GOODS_UNIT_FILE_NAME);
                }

            }

            @Override
            public void onDownloading(int progress) {
                Logger.d("TTT","资源更新中：resourceTypeKey："+resourceTypeKey+"-----"+progress);
            }

            @Override
            public void onDownloadFailed() {
                Logger.d("TTT","资源更新失败：resourceTypeKey："+resourceTypeKey+"-----");
            }
        });

    }


    /**
     * 判断新资源解析后是否为空
     * 因为不知道是解析的什么文件，所以只要符合一个解析后不为空，就直接往下走
     */
    private static boolean checkNewResourceIsEmpty(String json) {
        // 假设解析的是城市数据
        return !(!ListUtils.isEmpty(DataFormatFactory.jsonToArrayList(json, CityItem.class))
                | !ListUtils.isEmpty(DataFormatFactory.jsonToArrayList(json, TruckLengthInfo.class))
                | !ListUtils.isEmpty(DataFormatFactory.jsonToArrayList(json, TruckTypeInfo.class))
                | !ListUtils.isEmpty(DataFormatFactory.jsonToArrayList(json, OptionItem.class)));
    }
}
