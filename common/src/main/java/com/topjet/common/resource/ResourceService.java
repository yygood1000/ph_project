package com.topjet.common.resource;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.config.CPersisData;
import com.topjet.common.resource.bean.CheckResourseParamsBean;
import com.topjet.common.resource.bean.CheckResourseResponseBean;
import com.topjet.common.resource.event.UpdataScrollBtnsEvent;
import com.topjet.common.resource.event.UpdataTabLayoutBtnsEvent;
import com.topjet.common.resource.params.CheckResourceParams;
import com.topjet.common.resource.params.UpdataResourceParams;
import com.topjet.common.resource.response.CheckReourceResponse;
import com.topjet.common.resource.response.UpdataResourceResponse;
import com.topjet.common.resource.response.UpdataResourceUrlResponse;
import com.topjet.common.resource.serverAPI.ResourceCommand;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;

/**
 * Created by tsj-004 on 2017/11/10.
 * <p>
 * 资源文件更新服务
 * 更新后的文件需要在第二次启动APP初始化时生效
 */

public class ResourceService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        // 注册事件总线
        BusManager.getBus().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //检测资源文件更新
        checkResource();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusManager.getBus().unregister(this);
    }

    /**
     * 检测资源文件更新
     */
    private void checkResource() {
        // 构造检查更新请求体集合
        ArrayList<CheckResourseParamsBean> list = new ArrayList<>();
        list.add(new CheckResourseParamsBean(CPersisData.getScrollButtonsReourceVersion(CPersisData.SP_KEY_SCROLL_BUTTONS_VERSION),
                ResourceManager.RESOURCE_TYPE_SCROLL_BUTTONS));
        list.add(new CheckResourseParamsBean(CPersisData.getTablayoutButtonsReourceVersion(CPersisData
                .SP_KEY_TABLAYOUT_BUTTONS_VERSION),
                ResourceManager.RESOURCE_TYPE_TABLAYOUT_BUTTONS));
        list.add(new CheckResourseParamsBean(CPersisData.getCitiesReourceVersion(CPersisData.SP_KEY_CITIES_VERSION),
                ResourceManager.RESOURCE_TYPE_CITIES));
        list.add(new CheckResourseParamsBean(CPersisData.getTruckTypeReourceVersion(CPersisData.SP_KEY_TRUCK_TYPE_VERSION),
                ResourceManager.RESOURCE_TYPE_TRUCK_TYPE));
        list.add(new CheckResourseParamsBean(CPersisData.getTruckLengthReourceVersion(CPersisData.SP_KEY_TRUCK_LENGTH_VERSION),
                ResourceManager.RESOURCE_TYPE_TRUCK_LENGTH));
        list.add(new CheckResourseParamsBean(CPersisData.getPackingTypeReourceVersion(CPersisData.SP_KEY_PACKING_TYPE_VERSION),
                ResourceManager.RESOURCE_TYPE_PACKING_TYPE));
        list.add(new CheckResourseParamsBean(CPersisData.getLoadTypeReourceVersion(CPersisData.SP_KEY_LOAD_TYPE_VERSION),
                ResourceManager.RESOURCE_TYPE_LOAD_TYPE));
        list.add(new CheckResourseParamsBean(CPersisData.getGoodsTypeReourceVersion(CPersisData.SP_KEY_GOODS_TYPE_VERSION),
                ResourceManager.RESOURCE_TYPE_GOODS_TYPE));
        list.add(new CheckResourseParamsBean(CPersisData.getGoodsUnitReourceVersion(CPersisData.SP_KEY_GOODS_UNIT_VERSION),
                ResourceManager.RESOURCE_TYPE_GOODS_UNIT));
        CheckResourceParams params = new CheckResourceParams(list);

        // 校验资源文件更新
        new ResourceCommand().getTheUserParameter(params, new ObserverOnResultListener<CheckReourceResponse>() {
            @Override
            public void onResult(CheckReourceResponse checkReourceResponse) {
                // 处理校验资源结果
                compressCheckResourceResoult(checkReourceResponse.getCheckResourceList());
            }
        });
    }

    /**
     * 处理检查资源文件更新结果
     */
    private void compressCheckResourceResoult(ArrayList<CheckResourseResponseBean> checkResourceList) {
        for (CheckResourseResponseBean bean : checkResourceList) {
            if (bean.getIs_update().equals("1")) {
                // 进行对应资源文件更新
                getNewResource(bean.getResource_type(), bean.getInner_version());
            }
        }
    }

    /**
     * 确定有更新，进行更新资源操作
     */
    private void getNewResource(final String resourceType, String version) {
        Logger.d("TTT","检查到资源有更新：resourceType："+resourceType+"------version:"+version);
        switch (resourceType) {
            case ResourceManager.RESOURCE_TYPE_SCROLL_BUTTONS:// 首页滑动按钮组
                BusManager.getBus().post(new UpdataScrollBtnsEvent());
                break;
            case ResourceManager.RESOURCE_TYPE_TABLAYOUT_BUTTONS:// 首页底部导航
                Logger.d("mTabLayoutBtnsResponse  55 ");
                BusManager.getBus().post(new UpdataTabLayoutBtnsEvent());
                break;
            case ResourceManager.RESOURCE_TYPE_CITIES://城市列表
                //  请求更新资源接口，在更新结束后覆盖本地version，已经SD卡中的数据
                new ResourceCommand().updataCities(new UpdataResourceParams(version), new
                        ObserverOnResultListener<UpdataResourceUrlResponse>() {
                            @Override
                            public void onResult(UpdataResourceUrlResponse updataResourceResponse) {
                                ResourceManager.processUpdataResourceData(resourceType, updataResourceResponse);
                            }
                        });
                break;
            case ResourceManager.RESOURCE_TYPE_TRUCK_TYPE://车型
                new ResourceCommand().updataTruckType(new UpdataResourceParams(version), new
                        ObserverOnResultListener<UpdataResourceUrlResponse>() {
                            @Override
                            public void onResult(UpdataResourceUrlResponse updataResourceResponse) {
                                ResourceManager.processUpdataResourceData(resourceType, updataResourceResponse);
                            }
                        });
                break;
            case ResourceManager.RESOURCE_TYPE_TRUCK_LENGTH:// 车长
                new ResourceCommand().updataTruckLength(new UpdataResourceParams(version), new
                        ObserverOnResultListener<UpdataResourceUrlResponse>() {
                            @Override
                            public void onResult(UpdataResourceUrlResponse updataResourceResponse) {
                                ResourceManager.processUpdataResourceData(resourceType, updataResourceResponse);
                            }
                        });
                break;
            case ResourceManager.RESOURCE_TYPE_PACKING_TYPE:// 包装方式
                new ResourceCommand().updataPackingType(new UpdataResourceParams(version), new
                        ObserverOnResultListener<UpdataResourceUrlResponse>() {
                            @Override
                            public void onResult(UpdataResourceUrlResponse updataResourceResponse) {
                                ResourceManager.processUpdataResourceData(resourceType, updataResourceResponse);
                            }
                        });
                break;
            case ResourceManager.RESOURCE_TYPE_LOAD_TYPE:// 装卸方式
                new ResourceCommand().updataLoadType(new UpdataResourceParams(version), new
                        ObserverOnResultListener<UpdataResourceUrlResponse>() {
                            @Override
                            public void onResult(UpdataResourceUrlResponse updataResourceResponse) {
                                ResourceManager.processUpdataResourceData(resourceType, updataResourceResponse);
                            }
                        });
                break;
            case ResourceManager.RESOURCE_TYPE_GOODS_TYPE:// 货物类型
                new ResourceCommand().updataGoodsType(new UpdataResourceParams(version), new
                        ObserverOnResultListener<UpdataResourceUrlResponse>() {
                            @Override
                            public void onResult(UpdataResourceUrlResponse updataResourceResponse) {
                                ResourceManager.processUpdataResourceData(resourceType, updataResourceResponse);
                            }
                        });
                break;
            case ResourceManager.RESOURCE_TYPE_GOODS_UNIT:// 货物单位
                new ResourceCommand().updataGoodsUnit(new UpdataResourceParams(version), new
                        ObserverOnResultListener<UpdataResourceUrlResponse>() {
                            @Override
                            public void onResult(UpdataResourceUrlResponse updataResourceResponse) {
                                ResourceManager.processUpdataResourceData(resourceType, updataResourceResponse);
                            }
                        });
                break;
        }
    }


}