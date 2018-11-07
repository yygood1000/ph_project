package com.topjet.shipper.familiar_car.view.activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.common.contact.ContactExtra;
import com.topjet.common.contact.view.ContactActivity;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommandAPI;
import com.topjet.shipper.familiar_car.presenter.FamiliarCarPresenter;
import com.topjet.shipper.familiar_car.view.adapter.TruckListAdapter;

import java.util.ArrayList;

/**
 * creator: zhulunjun
 * time:    2017/10/12
 * describe: 我的熟车界面
 */

public class FamiliarCarActivity extends BaseRecyclerViewActivity<FamiliarCarPresenter, TruckInfo> implements
        IListView<TruckInfo> {
    private TruckListAdapter mAdapter;

    @Override
    public BaseQuickAdapter getAdapter() {
        mAdapter = new TruckListAdapter(this);
        return mAdapter;
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerViewWrapper.getIvEmpty().setImageResource(R.drawable.icon_empty_familiar_car);
        recyclerViewWrapper.getTvEmpty().setText("您还没有添加熟车，点击按钮，去添加吧");
        recyclerViewWrapper.getTvEmpty().setTextSize(13);
        recyclerViewWrapper.getTvBtnEmpty().setText(ResourceUtils.getString(R.string.add_familiar_truck));

        mAdapter.setFamiliarCarClick(new TruckListAdapter.FamiliarCarClick() {
            @Override
            public void contentClick(TruckInfo item) {
                // 车辆详情
                TruckInfoActivity.truckInfo((MvpActivity) mContext, item.getTruck_id());
            }

            @Override
            public void deleteClick(TruckInfo item) {
                mPresenter.addOrDeleteCar(item.getTruck_id(), TruckParams.DELETE);
            }

            @Override
            public void addClick(TruckInfo item) {
            }

            @Override
            public void placeOrderClick(TruckInfo item) {
                ArrayList<String> truckTypeId = new ArrayList<>();
                ArrayList<String> truckLehgthId = new ArrayList<>();
                truckTypeId.add(item.getTruck_type_id());
                truckLehgthId.add(item.getTruck_length_id());
                DeliverGoodsActivity.turnToDeliverGoodsForAddAssigned((MvpActivity) mContext, item.getDriver_id(),
                        item.getTruck_id(), truckTypeId, truckLehgthId);
            }

            @Override
            public void callClick(TruckInfo item) {
                new CallPhoneUtils().showCallDialogWithAdvNotUpload((MvpActivity) mContext, recyclerViewWrapper,
                        item.getDriver_name(),
                        item.getDriver_mobile(),
                        3);
            }

            @Override
            public void messageClick(TruckInfo item) {
                CommonProvider.getInstance().getJumpChatPageProvider()
                        .jumpChatPage(FamiliarCarActivity.this, item.getUserBean(item));
            }

            @Override
            public void clickDriverLocation(TruckInfo item) {
                // 跳转司机位置地图页面
                TruckExtra extra = new TruckExtra();
                extra.setTruckId(item.getTruck_id());
                extra.setLatitude(item.getLatitude());
                extra.setLongitude(item.getLongitude());
                extra.setTruck_plate(item.getPlateNo());
                extra.setAddress(item.getGps_address());
                extra.setName(item.getDriver_name());
                extra.setMobile(item.getDriver_mobile());
                extra.setTime(item.getGps_update_time());
                TruckPositionActivity.truckPosition((MvpActivity) mContext, extra, true);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT)
                .setTitleText(ResourceUtils.getString(R.string.my_familiar_truck))
                .setRightText(ResourceUtils.getString(R.string.add_familiar_truck));
    }


    @Override
    protected void onClickRightText() {
        super.onClickRightText();
        addFamiliarCar();
    }

    public void addFamiliarCar() {
        turnToActivity(ContactActivity.class, new ContactExtra(ContactExtra.FAMILIAR_CAR));
    }

    @Override
    public void emptyClick() {
        super.emptyClick();
        addFamiliarCar();
    }

    @Override
    public void loadData() {
        mPresenter.getFamiliarCarList(page);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new FamiliarCarPresenter(this, this, new TruckCommand(TruckCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_familiar_car;
    }
}
