package com.topjet.shipper.familiar_car.view.activity;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.shipper.familiar_car.model.respons.TruckInfoResponse;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 车辆详情
 */

public interface TruckInfoView extends IView{

    void bindView(TruckInfoResponse info);

    // 添加删除熟车是否成功
    void addSuccess();
    void deleteSuccess();
}
