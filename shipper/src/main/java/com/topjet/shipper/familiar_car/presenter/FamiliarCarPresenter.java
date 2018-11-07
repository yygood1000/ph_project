package com.topjet.shipper.familiar_car.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.utils.Toaster;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.shipper.familiar_car.model.respons.TruckListResponse;

/**
 * creator: zhulunjun
 * time:    2017/10/12
 * describe: 我的熟车
 */

public class FamiliarCarPresenter extends BaseApiPresenter<IListView<TruckInfo>, TruckCommand> {


    public FamiliarCarPresenter(IListView<TruckInfo> mView, Context mContext, TruckCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 获取熟车列表
     *
     * @param page
     */
    public void getFamiliarCarList(int page) {
        TruckParams params = new TruckParams();
        params.setPage(page + "");
        mApiCommand.truckList(params, new ObserverOnNextListener<TruckListResponse>() {
            @Override
            public void onNext(TruckListResponse familiarCars) {
                mView.loadSuccess(familiarCars.getOwner_truck_list());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });

    }


    /**
     * 删除，或者，添加
     * @param carId 车辆id
     * @param flag 添加0，删除1
     */
    public void addOrDeleteCar(String carId, final int flag) {
        TruckParams params = new TruckParams();
        params.setTruck_id(carId);
        params.setFlag(flag+"");
        mApiCommand.addOrDeleteTruck(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                if(flag == TruckParams.ADD){
                    Toaster.showShortToast("添加成功");
                }else {
                    Toaster.showShortToast("删除成功");
                    mView.refresh();
                }
            }
        });
    }




}
