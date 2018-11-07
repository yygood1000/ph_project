package com.topjet.crediblenumber.goods.view.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommand;
import com.topjet.crediblenumber.goods.modle.serverAPI.SubscribeRouteCommandAPI;
import com.topjet.crediblenumber.goods.presenter.AddSubscribeRoutePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加订阅路线
 */
public class AddSubscribeRouteActivity extends MvpActivity<AddSubscribeRoutePresenter> implements
        AddSubscribeRouteView {
    @BindView(R.id.tv_depart)
    TextView tvDepart;
    @BindView(R.id.tv_destination)
    TextView tvDestination;
    @BindView(R.id.rl_truck_type)
    RelativeLayout rlTruckType;
    @BindView(R.id.tv_truck_type)
    TextView tvTruckType;


    private int mCitySelectTag = -1;// 0 出发地，1 目的地
    private String departId;
    private String destinationId;
    private String truckTypeId;
    private String truckLengthId;
    private TruckTypeLengthSelectedData tld;

    @Override
    protected void initTitle() {
        getMyTitleBar().setTitleText(R.string.add_subscribe_route);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new AddSubscribeRoutePresenter(this, this, new SubscribeRouteCommand(SubscribeRouteCommandAPI
                .class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_subscribe_route;
    }

    @Override
    protected void initView() {

        mPresenter.getLocation();// 定位
    }

    @OnClick({R.id.rl_depart_root, R.id.rl_destination, R.id.rl_truck_type, R.id.btn_confirm_to_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_depart_root:// 出发地
                mCitySelectTag = 0;
                new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(this, tvDepart, false, true, true);
                break;
            case R.id.rl_destination:// 目的地
                mCitySelectTag = 1;
                new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(this, tvDestination, false, true, true);
                break;
            case R.id.rl_truck_type:// 车型车长
                new TruckLengthAndTypePopupWindow(TAG, 1, 1).showPopupWindow(this, rlTruckType, false,
                        TruckLengthAndTypePopupWindow.ENTIRETY_NO_SELECTED, false, tld);
                break;
            case R.id.btn_confirm_to_add:// 确认添加
                mPresenter.addSubscribeRoute(departId, destinationId, truckTypeId, truckLengthId);
                break;
        }
    }

    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (mCitySelectTag == 0) {// 出发地
            departId = data.getAreaInfo().getLastCityId();
            tvDepart.setText(data.getAreaInfo().getFullCityName());
        } else {// 目的地
            destinationId = data.getAreaInfo().getLastCityId();
            tvDestination.setText(data.getAreaInfo().getFullCityName());
        }
    }

    @Subscribe
    public void onEvent(TruckTypeLengthSelectedData tld) {
        if (tld.getTag().equals(TAG)) {
            // 设置ID
            truckTypeId = StringUtils.isNotBlank(tld.getTypeList().get(0).getId()) ?
                    tld.getTypeList().get(0).getId() : "";
            truckLengthId = StringUtils.isNotBlank(tld.getLengthList().get(0).getId()) ?
                    tld.getLengthList().get(0).getId() : "";

            // 设置文本
            String truckLength = StringUtils.isNotBlank(tld.getLengthList().get(0).getDisplayName()) ? tld
                    .getLengthList().get(0).getDisplayName() : "";
            String truckType = StringUtils.isNotBlank(tld.getTypeList().get(0).getDisplayName()) ? tld
                    .getTypeList().get(0).getDisplayName() : "";
            tvTruckType.setText(truckLength + truckType);
        }
    }

    @Override
    public void setDepartTextView(String departCityName, String departCityId) {
        departId = departCityId;
        tvDepart.setText(departCityName);
    }
}
