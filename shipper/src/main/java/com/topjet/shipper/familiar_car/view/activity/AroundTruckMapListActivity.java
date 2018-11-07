package com.topjet.shipper.familiar_car.view.activity;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommandAPI;
import com.topjet.shipper.familiar_car.presenter.AroundTruckMapListPresenter;
import com.topjet.shipper.familiar_car.view.adapter.TruckListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 附近货源 地图列表 页面
 */
public class AroundTruckMapListActivity extends BaseRecyclerViewActivity<AroundTruckMapListPresenter, TruckInfo>
        implements AroundTruckMapListView<TruckInfo> {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void initPresenter() {
        mPresenter = new AroundTruckMapListPresenter(this, mContext, new TruckCommand(TruckCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_around_truck_map_list;
    }

    @Override
    public void initView() {
        super.initView();
        getMyTitleBar().setMode(MyTitleBar.Mode.CANCEL_ONLY).setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        refresh();
    }

    @Override
    public void loadData() {
        if (mPresenter.isUseLocalListData) {
            if (page == 1) {// 刷新
                loadSuccess(mPresenter.extra.getTruckListData());
            } else {// 加载更多
                loadSuccess(new ArrayList<TruckInfo>());
            }
        } else {
            mPresenter.getListData(page);
        }
    }


    @OnClick({R.id.iv_close, R.id.rl_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
            case R.id.rl_root:
                finish();
                overridePendingTransition(com.topjet.common.R.anim.anim_no, com.topjet.common.R.anim.slide_out_bottom);
                break;
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        TruckListAdapter mAdapter = new TruckListAdapter(this);
        mAdapter.setShowBtn(false);
        mAdapter.setFamiliarCarClick(new TruckListAdapter.FamiliarCarClick() {
            @Override
            public void contentClick(TruckInfo item) {
                // 车辆详情
                TruckInfoActivity.truckInfo((MvpActivity) mContext, item.getTruck_id());
            }

            @Override
            public void deleteClick(TruckInfo item) {
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
                        .jumpChatPage(AroundTruckMapListActivity.this, item.getUserBean(item));
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
        return mAdapter;
    }

    @Override
    public void showTitle(String str) {
        tvTitle.setText(str);
    }
}
