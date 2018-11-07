package com.topjet.crediblenumber.car.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.extra.GoToCarInfoExtra;
import com.topjet.crediblenumber.car.presenter.CarInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tsj-004 on 2017/10/12.
 * 车辆详情
 */

public class CarInfoActivity extends MvpActivity<CarInfoPresenter> implements CarInfoView {
    @BindView(R.id.tv_car_number_result)
    TextView tvCarNumberResult;                     // 车牌号
    @BindView(R.id.tv_car_number_color_result)
    TextView tvCarNumberColorResult;         // 车牌颜色
    @BindView(R.id.tv_type_length_result)
    TextView tvTypeLengthResult;             // 车型车长
    @BindView(R.id.iv_show_car_front_photo)
    ImageView ivShowCarFrontPhoto;                //车头照
    @BindView(R.id.iv_show_vehicle_license)
    ImageView ivShowVehicleLicense;              // 行驶证
    @BindView(R.id.et_driver)
    EditText etDriver;                          // 开车司机
    @BindView(R.id.et_phone)
    EditText etPhone;                           // 联系电话

    @BindView(R.id.include_carinfo_result)
    View includeCarinfoResult;                 // 结果
    @BindView(R.id.include_car_photo)
    View includeCarPhoto;                      // 图片显示或修改

    @BindView(R.id.ll_all)
    LinearLayout llAll;     // 修改和删除外层
    @BindView(R.id.tv_driver_title)
    TextView tvDriverTitle;
    @BindView(R.id.v_0000)
    View v0000;
    @BindView(R.id.tv_phone_title)
    TextView tvPhoneTitle;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;

    private final static int REQUEST_CODE_TO_EDITCARINFO = 200; // requestCode，跳转编辑页
    GoToCarInfoExtra extra = new GoToCarInfoExtra();

    @Override
    protected void initPresenter() {
        mPresenter = new CarInfoPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_car_info;
    }

    @Override
    protected void initView() {
        GoToCarInfoExtra tempExtra = getIntentExtra(GoToCarInfoExtra.getExtraName());
        if (tempExtra != null) {
            extra = tempExtra;
            if (extra.getDriver_truck_id() != null && extra.getInType() == GoToCarInfoExtra.IN_TYPE_SHOW) {
                mPresenter.getTruckInfo(extra.getDriver_truck_id());    // 请求网络，获取详情
            }
            changeShowMode();
        }
    }

    @Override
    protected void initTitle() {

        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.car_details);
    }

    @OnClick({R.id.tv_edit, R.id.tv_delete})
    public void onClickViews(View view) {
        switch (view.getId()) {
            case R.id.tv_edit:      // 编辑
                extra.setInType(GoToCarInfoExtra.IN_TYPE_EDIT);
                turnToActivityForResult(EditCarInfoActivity.class, REQUEST_CODE_TO_EDITCARINFO, extra);
                break;

            case R.id.tv_delete:        // 删除
                showDeleteDialog();
                break;
        }
    }

    /**
     * 显示删除警告
     */
    private void showDeleteDialog() {
        final AutoDialog dialog = new AutoDialog(mContext);
        dialog.setTitle("删除车辆后，无法找回\n您确定要删除当前的这辆车吗？");
        dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
            @Override
            public void onLeftClick() {
                dialog.toggleShow();
            }

            @Override
            public void onRightClick() {
                dialog.toggleShow();
                mPresenter.deleteTruckInfo();    // 请求网络，删除车辆信息
            }
        });
        dialog.toggleShow();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TO_EDITCARINFO:
                    this.setResultAndFinish(RESULT_OK);
                    break;
            }
        }
    }

    /**
     * 显示部分、隐藏部分
     */
    private void changeShowMode() {
        if (extra.getInType() == GoToCarInfoExtra.IN_TYPE_SHOW) {     // 显示模式
            includeCarinfoResult.setVisibility(View.VISIBLE);

            /**
             * 0 无需认证
             1 未认证
             2 已认证
             3 认证中
             4 认证失败
             */
            if (!StringUtils.isEmpty(extra.getAudit_status()) && extra.getAudit_status().equals("2")) {
                llAll.setVisibility(View.GONE);
            } else {
                llAll.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showViewByParams(TruckTeamCar car) {
        tvCarNumberResult.setText(car.getCarNumber());
        tvCarNumberColorResult.setText(car.getCarNumberColor());
        tvTypeLengthResult.setText(car.getTypeLength());

        if (StringUtils.isEmpty(car.getTruck_icon_url()) && StringUtils.isEmpty(car.getTruck_license_url())) {
            includeCarPhoto.setVisibility(View.GONE);
        }
        GlideUtils.loaderImageRoundWithSize(this, car.getTruck_icon_url(), car.getTruck_icon_key(), ivShowCarFrontPhoto, 3, ivShowCarFrontPhoto.getWidth(), ivShowCarFrontPhoto.getHeight());
        GlideUtils.loaderImageRoundWithSize(this, car.getTruck_license_url(), car.getTruck_license_key(), ivShowVehicleLicense, 3, ivShowVehicleLicense.getWidth(), ivShowVehicleLicense.getHeight());

        boolean isNeedHideRemarksView = true;  // 是否需要隐藏备注
        if (StringUtils.isEmpty(car.getDriver_name())) {
            tvDriverTitle.setVisibility(View.GONE);
            etDriver.setVisibility(View.GONE);
            v0000.setVisibility(View.GONE);
        } else {
            isNeedHideRemarksView = false;
        }
        if (StringUtils.isEmpty(car.getDriver_mobile())) {
            tvPhoneTitle.setVisibility(View.GONE);
            etPhone.setVisibility(View.GONE);
        } else {
            isNeedHideRemarksView = false;
        }

        etDriver.setText(car.getDriver_name() == null ? "" : car.getDriver_name());
        etPhone.setText(car.getDriver_mobile() == null ? "" : car.getDriver_mobile());

        if (isNeedHideRemarksView) {
            tvRemarks.setVisibility(View.GONE);
        }
    }
}
