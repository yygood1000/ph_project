package com.topjet.shipper.familiar_car.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.utils.Toaster;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.shipper.familiar_car.model.respons.TruckInfoResponse;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.view.activity.TruckInfoView;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe:
 */

public class TruckInfoPresenter extends BaseApiPresenter<TruckInfoView, TruckCommand> {
    public TruckInfoPresenter(TruckInfoView mView, Context mContext, TruckCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    private String truckId;
    @Override
    public void onCreate() {
        super.onCreate();
       TruckExtra extra = (TruckExtra) mActivity.getIntentExtra(TruckExtra.getExtraName());
        if(extra!=null){
            truckId = extra.getTruckId();
        }
    }

    /**
     * 车辆详情
     */
    public void truckInfo(){
        mApiCommand.truckInfo(new TruckParams(truckId), new ObserverOnResultListener<TruckInfoResponse>() {
            @Override
            public void onResult(TruckInfoResponse truckInfo) {
                if(truckInfo!=null){
                    mView.bindView(truckInfo);
                }
            }
        });
    }

    /**
     * 删除，或者，添加
     * @param flag 添加0，删除1
     */
    public void addOrDeleteCar(final int flag) {
        TruckParams params = new TruckParams();
        params.setTruck_id(truckId);
        params.setFlag(flag+"");
        mApiCommand.addOrDeleteTruck(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                if(flag == TruckParams.ADD){
                    Toaster.showShortToast("添加成功");
                    mView.addSuccess();
                }else {
                    Toaster.showShortToast("删除成功");
                    mView.deleteSuccess();
                }
            }
        });
    }
}
