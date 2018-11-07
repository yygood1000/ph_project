package com.topjet.crediblenumber.car.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.user.view.dialog.AuthenticationPopup;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.extra.GoToCarInfoExtra;
import com.topjet.crediblenumber.car.presenter.EditCarInfoPresenter;
import com.topjet.crediblenumber.dialog.OnCarPlateCompleteListener;
import com.topjet.crediblenumber.dialog.PlateWheelPop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by tsj-004 on 2017/10/12.
 * 修改、删除、添加车辆
 */

public class EditCarInfoActivity extends MvpActivity<EditCarInfoPresenter> implements EditCarInfoView {
    @BindView(R.id.tv_location)
    TextView tvLocation;             // 归属地，沪、京等等
    @BindView(R.id.iv_location)
    ImageView ivLocation;            // 归属地，沪、京等等
    @BindView(R.id.et_car_number)
    EditText etCarNumber;               // 车牌号
    @BindView(R.id.rg_car_number_color)
    RadioGroup rgCarNumberColor;            // 车牌颜色
    @BindView(R.id.rb_yellow)
    RadioButton rbYellow;            // 黄色
    @BindView(R.id.rb_blue)
    RadioButton rbBlue;            // 蓝色
    @BindView(R.id.tv_type_length)
    TextView tvTypeLength;                       // 车型车长
    @BindView(R.id.iv_show_car_front_photo)
    ImageView ivShowCarFrontPhoto;                //车头照
    @BindView(R.id.iv_show_vehicle_license)
    ImageView ivShowVehicleLicense;              // 行驶证
    @BindView(R.id.et_driver)
    EditText etDriver;                          // 开车司机
    @BindView(R.id.et_phone)
    EditText etPhone;                           // 联系电话

    @BindView(R.id.include_carinfo)
    View includeCarinfo;                        // 编辑项

    @BindView(R.id.tv_submit)
    TextView tvSubmit;      // 提交

    private int curPhotoType = 0;       //0 车头照 1 行驶证
    private String frontPath = null;   // 车头照图片地址
    private String vehicleLicensePath = null;   // 行驶证图片地址
    private final int TYPE_CAR_FRONT_PHOTO = 0; // 车头照
    private final int TYPE_VEHICLE_LICENSE_PHOTO = 1;// 行驶证

    GoToCarInfoExtra extra = new GoToCarInfoExtra();

    @Override
    protected void initPresenter() {
        mPresenter = new EditCarInfoPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_car_info;
    }

    @Override
    protected void initView() {
        GoToCarInfoExtra tempExtra = getIntentExtra(GoToCarInfoExtra.getExtraName());
        if (tempExtra != null) {
            extra = tempExtra;
            if (extra.getDriver_truck_id() != null && extra.getInType() == GoToCarInfoExtra.IN_TYPE_EDIT) {
                mPresenter.getTruckInfo(extra.getDriver_truck_id());    // 请求网络，获取详情
            }
            changeShowMode();
        }

        setCarNumberTextChangeListener(etCarNumber);        // 设置车牌号的文字变化监听
        setTypeLengthTextChangeListener(tvTypeLength);
    }

    @Override
    protected void initTitle() {

        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.add_truck_car);
    }

    /**
     * 设置车牌号的文字变化监听
     */
    public void setCarNumberTextChangeListener(final EditText etCarNumber) {
        RxTextView.textChanges(etCarNumber).compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        String str = charSequence.toString().trim();
                        if (!TextUtils.isEmpty(str)) {
                            String tempStr = str.toUpperCase();
                            if (!str.equals(tempStr)) {
                                etCarNumber.setText(tempStr);
                                etCarNumber.setSelection(tempStr.length());
                            }
                        }

                        carNumberAndTypeLengthIsNotEmpty(); // 车型车长和车牌号是否为空，不为空提交按钮可点击
                    }
                });
    }

    /**
     * 设置车型车长的文字变化监听
     */
    public void setTypeLengthTextChangeListener(final TextView etCarNumber) {
        RxTextView.textChanges(etCarNumber).compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        carNumberAndTypeLengthIsNotEmpty(); // 车型车长和车牌号是否为空，不为空提交按钮可点击
                    }
                });
    }

    /**
     * 车型车长和车牌号是否为空，不为空提交按钮可点击
     */
    private void carNumberAndTypeLengthIsNotEmpty() {
        String carNumber = etCarNumber.getText().toString();
        String typeLength = tvTypeLength.getText().toString();
        if (!StringUtils.isEmpty(carNumber) && !StringUtils.isEmpty(typeLength)) {
            tvSubmit.setEnabled(true); // 提交按钮可点击
        } else {
            tvSubmit.setEnabled(false); // 提交按钮可点击
        }
    }

    @OnClick({R.id.tv_submit, R.id.rl_upload_car_front_photo, R.id.rl_upload_vehicle_license, R.id.tv_type_length, R
            .id.tv_location})
    public void onClickViews(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:        // 提交
                if (extra.getInType() == GoToCarInfoExtra.IN_TYPE_EDIT) {
                    mPresenter.editTruckInfo(extra.getDriver_truck_id(), tvLocation.getText().toString(), etCarNumber
                                    .getText().toString()
                            , ((RadioButton) findViewById(rgCarNumberColor.getCheckedRadioButtonId())).getText()
                                    .toString()
                            , frontPath, vehicleLicensePath, etDriver.getText().toString(), etPhone.getText()
                                    .toString());    // 请求网络，修改车辆信息
                } else if (extra.getInType() == GoToCarInfoExtra.IN_TYPE_ADD) {
                    mPresenter.addTruck(extra.getDriver_truck_id(), tvLocation.getText().toString(), etCarNumber
                                    .getText().toString()
                            , ((RadioButton) findViewById(rgCarNumberColor.getCheckedRadioButtonId())).getText()
                                    .toString()
                            , frontPath, vehicleLicensePath, etDriver.getText().toString(), etPhone.getText()
                                    .toString()); // 请求网络，添加车辆
                }
                break;

            case R.id.rl_upload_car_front_photo:      // 车头照
                if (extra.getInType() != GoToCarInfoExtra.IN_TYPE_SHOW) {
                    curPhotoType = TYPE_CAR_FRONT_PHOTO;
                    new AuthenticationPopup(this)
                            .initPop(R.string.car_front_photo, R.drawable.approve_headstock_eg, this)
                            .showPop(ivShowCarFrontPhoto);
                }
                break;

            case R.id.rl_upload_vehicle_license:        // 行驶证
                if (extra.getInType() != GoToCarInfoExtra.IN_TYPE_SHOW) {
                    curPhotoType = TYPE_VEHICLE_LICENSE_PHOTO;
                    new AuthenticationPopup(this)
                            .initPop(R.string.driving_license, R.drawable.approve_car_license_eg, this)
                            .showPop(ivShowVehicleLicense);
                }
                break;

            case R.id.tv_type_length:       // 选择车型车长
                 new TruckLengthAndTypePopupWindow(TAG, true, false)
                        .showPopupWindow(this, tvTypeLength, false, TruckLengthAndTypePopupWindow
                                .ENTIRETY_SELECTED_INDEX_1, true, mPresenter.getTld());
                break;

            case R.id.iv_location:
            case R.id.tv_location:      // 归属地，沪等等
                PlateWheelPop mPop = new PlateWheelPop(this, ivLocation, tvLocation.getText().toString(), new
                        OnCarPlateCompleteListener() {
                            @Override
                            public void OnCompleteListener(String plate1) {
                                tvLocation.setText(plate1);
                            }
                        });
                mPop.showPop();
                break;
        }
    }

    /**
     * popupwindow某一项的点击事件
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.tv_take_photo) {
            if (curPhotoType == TYPE_CAR_FRONT_PHOTO) {
                CameraUtil.jumpToCameraGetPic(this);// 车头照
            } else if (curPhotoType == TYPE_VEHICLE_LICENSE_PHOTO) {
                CameraUtil.jumpToCameraGetPic(this);// 行驶证
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CameraUtil.REQUEST_CODE.SYSTEM_CAMERA:  // 拍照
                    if (curPhotoType == TYPE_CAR_FRONT_PHOTO) {
                        frontPath = CameraUtil.currentPicPath;
                        // 获取得到拍照图片后跳转裁剪页面
                        ImageUtil.toCropForSquare(this, new File(frontPath));
                    } else if (curPhotoType == TYPE_VEHICLE_LICENSE_PHOTO) {
                        vehicleLicensePath = CameraUtil.currentPicPath;
                        // 获取得到拍照图片后跳转裁剪页面
                        ImageUtil.toCropForSquare(this, new File(vehicleLicensePath));
                    }
                    break;
                case CameraUtil.REQUEST_CODE.SQUARE_CROP: // 裁剪后
                    if (curPhotoType == TYPE_CAR_FRONT_PHOTO) {
                        frontPath = data.getStringExtra("filePath");
                        showCarFrontPhoto(frontPath, null); // 车头照
                    } else {
                        vehicleLicensePath = data.getStringExtra("filePath");
                        showVehicleLicense(vehicleLicensePath, null); // 行驶证
                    }
                    break;
            }
        }
    }

    /**
     * 显示部分、隐藏部分
     */
    private void changeShowMode() {
        if (extra.getInType() == GoToCarInfoExtra.IN_TYPE_EDIT) {        // 编辑模式
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.edit_car_information);
            includeCarinfo.setVisibility(View.VISIBLE);
            tvSubmit.setVisibility(View.VISIBLE);
        } else if (extra.getInType() == GoToCarInfoExtra.IN_TYPE_ADD) {        // 添加模式
            getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.add_truck_car);

            includeCarinfo.setVisibility(View.VISIBLE);
            tvSubmit.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 从车型车长选择器中获取数据
     */
    @Subscribe
    public void onEvent(TruckTypeLengthSelectedData tld) {
        if (tld.getTag().equals(TAG)) {
            mPresenter.selectTypeAndLength(tld);  // 从车型车长选择器中获取数据
        }
    }

    @Override
    public void setTypeAndLengthText(String txt) {
        tvTypeLength.setText(txt);
    }

    @Override
    public void showViewByParams(TruckTeamCar car) {
        String color = car.getPlate_color();
        // 1 蓝色 2 黄色
        if (color.equals("1")) {
            rbBlue.setChecked(true);
        } else {
            rbYellow.setChecked(true);
        }
        tvLocation.setText(car.getPlate_no1());
        tvTypeLength.setText(car.getTypeLength());
        etCarNumber.setText(car.getCarNumberNoLocation());

        GlideUtils.loaderImageRoundWithSize(this, car.getTruck_icon_url(), car.getTruck_icon_key(),
                ivShowCarFrontPhoto, 3, ivShowCarFrontPhoto.getWidth(), ivShowCarFrontPhoto.getHeight());
        GlideUtils.loaderImageRoundWithSize(this, car.getTruck_license_url(), car.getTruck_license_key(),
                ivShowVehicleLicense, 3, ivShowVehicleLicense.getWidth(), ivShowVehicleLicense.getHeight());
        etDriver.setText(car.getDriver_name() == null ? "" : car.getDriver_name());
        etPhone.setText(car.getDriver_mobile() == null ? "" : car.getDriver_mobile());
    }

    /**
     * 设置车头照
     */
    @Override
    public void showCarFrontPhoto(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowCarFrontPhoto, 5, ivShowCarFrontPhoto.getWidth()
                    , ivShowCarFrontPhoto.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowCarFrontPhoto, 5, ivShowCarFrontPhoto
                    .getWidth(), ivShowCarFrontPhoto.getHeight());
        }
    }

    /**
     * 设置行驶证
     */
    @Override
    public void showVehicleLicense(String url, String key) {
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowVehicleLicense, 5, ivShowVehicleLicense.getWidth
                    (), ivShowVehicleLicense.getHeight());
        } else {
            GlideUtils.loaderImageRoundWithSize(mContext, url, key, ivShowVehicleLicense, 5, ivShowVehicleLicense
                    .getWidth(), ivShowVehicleLicense.getHeight());
        }
    }

    @Override
    public void showError(int errorMsg) {
        Toaster.showLongToast(errorMsg);
    }
}
