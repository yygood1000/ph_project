package com.topjet.shipper.user.view.activity;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.modle.bean.UserInfoGoods;
import com.topjet.common.common.view.activity.UserInfoBaseActivity;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.LoadingStateDelegate;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.shipper.familiar_car.view.activity.TruckPositionActivity;
import com.topjet.shipper.user.view.adapter.DriverTruckListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 货主查司机 诚信查询结果
 */

public class UserInfoActivity extends UserInfoBaseActivity<TruckInfo> {

    private DriverTruckListAdapter mTruckListAdapter;


    @Override
    protected void initView() {
        super.initView();
        mTruckListAdapter.setDriverTruckListClick(new DriverTruckListAdapter.DriverTruckListClick() {
            @Override
            public void orderClick(TruckInfo item) {
                // 下单
                List<String> listType = new ArrayList<>();
                listType.add(item.getTruck_type_id());
                List<String> listLength = new ArrayList<>();
                listLength.add(item.getTruck_length_id());
                DeliverGoodsActivity.turnToDeliverGoodsForAddAssigned(UserInfoActivity.this, mUserInfo.getUser_id(),
                        item.getTruck_id(), listType,listLength);
            }

            @Override
            public void locationClick(TruckInfo item) {
                // 车辆位置
                TruckExtra extra = new TruckExtra();
                extra.setTruckId(item.getTruck_id());
                extra.setLatitude(item.getGps_latitude());
                extra.setLongitude(item.getGps_longitude());
                extra.setMobile(mUserInfo.getUser_mobile());
                extra.setName(mUserInfo.getUser_name());
                extra.setAddress(item.getGps_address());
                extra.setTime(item.getGps_update_time());
                extra.setTruck_plate(item.getPlateNo());
                TruckPositionActivity.truckPosition(UserInfoActivity.this, extra, true);
            }
        });
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        mTruckListAdapter = new DriverTruckListAdapter();
        return mTruckListAdapter;
    }

    @Override
    public void loadData() {
        if(mUserInfo != null &&
                StringUtils.isNotBlank(mUserInfo.getUser_id()) &&
                mUserInfo.getUser_type().equals(UserInfo.DRIVER)){
            mPresenter.queryDriverInfoTruckList(mUserInfo.getUser_id(), page+"");
        } else {
            // 当货主查询到货主身份时，不显示货源信息
            noCheckDateToSuccess();
        }
    }


}
