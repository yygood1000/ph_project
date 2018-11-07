package com.topjet.crediblenumber.car.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.extra.GoToCarInfoExtra;
import com.topjet.crediblenumber.car.presenter.MyFleetPresenter;
import com.topjet.crediblenumber.car.view.adapter.MyFleetAdapter;
import com.topjet.crediblenumber.user.view.activity.CarAuthenticationActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tsj-004 on 2017/10/12.
 * 我的车队
 */

public class MyFleetActivity extends BaseRecyclerViewActivity<MyFleetPresenter, TruckTeamCar> implements MyFleetView<TruckTeamCar>,
        BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemClickListener {
    private static final int REQUESTCODE_CARINFO = 100;     // 车辆详情界面
    private static final int REQUESTCODE_AUTHENTICATION = 200;     // 认证车辆界面
    private MyFleetAdapter myFleetAdapter = null;

    @BindView(R.id.ll_all)
    LinearLayout llAll; // 全部求货和休息的外层

    @Override
    protected void initPresenter() {
        mPresenter = new MyFleetPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_fleet;
    }

    @Override
    protected void initView() {
        myFleetAdapter = new MyFleetAdapter();
        myFleetAdapter.setOnItemChildClickListener(this);
        myFleetAdapter.setOnItemClickListener(this);
        super.initView();

        recyclerViewWrapper.getTvEmpty().setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        recyclerViewWrapper.getTvEmpty().setGravity(Gravity.CENTER);
        setEmptyText("您还没有车，请添加您的第一辆车\n待审核通过，就可以接活了");
        recyclerViewWrapper.getTvBtnError().setVisibility(View.GONE);
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        refresh();
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT).setTitleText(R.string.my_truck_team).setRightText(R.string.add_truck_car);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.cb_car_work_status) {
            CheckBox cbCarWorkStatus = (CheckBox) view;
            mPresenter.changeOnlyOneCarWorkStatus(position, cbCarWorkStatus.isChecked());
        }
    }

    @Override
    protected void onClickRightText() {
        List<TruckTeamCar> list = mPresenter.getTruckTeams();
        /**
         * 0 无需认证
         * 1 未认证
         * 2 已认证
         * 3 认证中
         * 4 认证失败
         */
        if (list == null || list.size() == 0 || list.get(0).getAudit_status().equals("1") || list.get(0).getAudit_status().equals("4")) {
            // 车辆认证
            GoToAuthenticationExtra extra = new GoToAuthenticationExtra(GoToAuthenticationExtra.IN_TYPE_SHOW);
            turnToActivityForResult(CarAuthenticationActivity.class, REQUESTCODE_AUTHENTICATION, extra);
        } else if (list != null && list.size() != 0 && list.get(0).getAudit_status().equals("3")) {
            Toaster.showLongToast("您的车辆认证信息还在认证中，无法进行此操作，请耐心等待认证结果！");
        } else {
            // 添加车辆
            turnToActivityForResult(EditCarInfoActivity.class, REQUESTCODE_CARINFO, new GoToCarInfoExtra(GoToCarInfoExtra.IN_TYPE_ADD));
        }
    }

    /**
     * 点击控件
     */
    @OnClick({R.id.tv_all_work, R.id.tv_all_rest})
    public void onClickViews(View view) {
        switch (view.getId()) {
            case R.id.tv_all_work:
                mPresenter.changeAllCarWorkStatus(true);
                break;

            case R.id.tv_all_rest:
                mPresenter.changeAllCarWorkStatus(false);
                break;
        }
    }

    /**
     * 刷新所有checkbox
     */
    public void refreAllCheckBox(boolean checked) {
        myFleetAdapter.setNewData(mPresenter.getTruckTeams());
        if (checked) {
            Toaster.showLongToast("全部求货设置成功");
        } else {
            Toaster.showLongToast("全部休息设置成功");
        }
    }

    /**
     * 列表是空，不显示全部求货和休息。false不显示
     */
    @Override
    public void llAllHideOrShow(boolean hide) {
        if (hide) {
            llAll.setVisibility(View.GONE);
        } else {
            llAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        /**
         * 0 无需认证
         * 1 未认证
         * 2 已认证
         * 3 认证中
         * 4 认证失败
         */
        List<TruckTeamCar> list = mPresenter.getTruckTeams();
        if (list != null && list.size() != 0 && position == 0) {
            // 车辆认证
            GoToAuthenticationExtra extra = new GoToAuthenticationExtra(GoToAuthenticationExtra.IN_TYPE_SHOW);
            turnToActivityForResult(CarAuthenticationActivity.class, REQUESTCODE_AUTHENTICATION, extra);
        } else {
            GoToCarInfoExtra extra = new GoToCarInfoExtra(GoToCarInfoExtra.IN_TYPE_SHOW);
            extra.setDriver_truck_id(mPresenter.getTruckTeams().get(position).getDriver_truck_id());
            extra.setAudit_status(mPresenter.getTruckTeams().get(position).getAudit_status());
            turnToActivityForResult(CarInfoActivity.class, REQUESTCODE_CARINFO, extra);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (REQUESTCODE_CARINFO == requestCode || REQUESTCODE_AUTHENTICATION == requestCode) {
                refresh();  // 刷新列表
            }
        }
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return myFleetAdapter;
    }

    @Override
    public void loadData() {
        mPresenter.getTruckTeamList(page);  // 请求服务器接口  查看车队列表
    }
}
