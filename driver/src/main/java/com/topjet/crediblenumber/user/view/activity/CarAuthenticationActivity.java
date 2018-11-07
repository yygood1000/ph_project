package com.topjet.crediblenumber.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.view.dialog.TruckLengthAndTypePopupWindow;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.user.view.dialog.AuthenticationPopup;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.dialog.OnCarPlateCompleteListener;
import com.topjet.crediblenumber.dialog.PlateWheelPop;
import com.topjet.crediblenumber.user.modle.serverAPI.AuthenticationCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.AuthenticationCommandAPI;
import com.topjet.crediblenumber.user.presenter.CarAuthenticationPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 车辆认证
 * 司机版
 */
public class CarAuthenticationActivity extends MvpActivity<CarAuthenticationPresenter> implements
        CarAuthenticationView {
    @BindView(R.id.et_car_number)   //车牌号
            EditText etCarNumber;
    @BindView(R.id.iv_location)     //车牌开头
            ImageView ivLocation;
    @BindView(R.id.tv_location)     //车牌开头
            TextView tvLocation;
    @BindView(R.id.rg_car_number_color) //车牌颜色
            RadioGroup rgCarNumberColor;
    @BindView(R.id.rb_yellow) //黄色
            RadioButton rbYellow;
    @BindView(R.id.rb_blue) //蓝色
            RadioButton rbBlue;
    @BindView(R.id.iv_type_length)  //车型车长
            ImageView ivTypeLength;
    @BindView(R.id.tv_type_length)  //车型车长
            TextView tvTypeLength;
    @BindView(R.id.include_carinfo)
    View includeCarinfo;                        // 编辑项
    @BindView(R.id.include_carinfo_result)
    View includeCarinfoResult;                 // 结果

    @BindView(R.id.tv_car_number_result)
    TextView tvCarNumberResult;                     // 车牌号
    @BindView(R.id.tv_car_number_color_result)
    TextView tvCarNumberColorResult;         // 车牌颜色
    @BindView(R.id.tv_type_length_result)
    TextView tvTypeLengthResult;             // 车型车长
    @BindView(R.id.iv_show_car_front_photo) //车头照
            ImageView ivShowCarFrontPhoto;
    @BindView(R.id.rl_upload_car_front_photo)
    RelativeLayout rlUploadCarFrontPhoto;
    @BindView(R.id.iv_show_vehicle_license) //行驶证
            ImageView ivShowVehicleLicense;
    @BindView(R.id.rl_upload_vehicle_license)
    RelativeLayout rlUploadVehicleLicense;
    @BindView(R.id.tv_submit)   //提交按钮
            TextView tvSubmit;

    @BindView(R.id.tv_car_front_hint) //车头照提示
            TextView tvCarFrontHint;
    @BindView(R.id.iv_car_front_add) //车头照加号
            ImageView ivCarFrontAdd;

    @BindView(R.id.tv_vehicle_license_hint) //行驶证提示
            TextView tvVehicleLicenseHint;
    @BindView(R.id.iv_vehicle_license_add) //行驶证加号
            ImageView ivVehicleLicenseAdd;

    @BindView(R.id.tv_status)
    TextView tvStatus;      // 认证进度
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;      // 认证进度
    @BindView(R.id.vvvvvv)
    View vvvvvv;      // 认证进度下方间隔
    @BindView(R.id.ll_car_front_authenticaioning)
    LinearLayout llCarFrontAuthenticaioning;      // 审核中，车头照
    @BindView(R.id.tv_car_front_authenticaioning)
    TextView tvCarFrontAuthenticaioning;      // 审核中，车头照
    @BindView(R.id.rl_car_front_fail)
    RelativeLayout rlCarFrontFail;      // 认证失败，重新上传，车头照

    @BindView(R.id.ll_vehicle_license_authenticaioning)
    LinearLayout llVehicleLicenseAuthenticaioning;      // 审核中，行驶证
    @BindView(R.id.tv_vehicle_license_authenticaioning)
    TextView tvVehicleLicenseAuthenticaioning;      // 审核中，行驶证
    @BindView(R.id.rl_vehicle_license_fail)
    RelativeLayout rlVehicleLicenseFail;      // 认证失败，重新上传，行驶证

    private int curPhotoType = 0;       //0 车头照 1 行驶证
    private String frontPath = null;   // 车头照图片地址
    private String vehicleLicensePath = null;   //行驶证图片地址

    private GoToAuthenticationExtra goToAuthenticationExtra = new GoToAuthenticationExtra();    // 界面间传值
    private boolean isShowMode = false; // 是否是显示模式，显示模式有返回按钮，没有“跳过”

    @Override
    public void onBackPressed() {
        if (isShowMode) {
            super.onBackPressed();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_authentication_car;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.TITLE_RTEXT).setTitleText(R.string.car_authentication).setRightText(R
                .string.skip);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CarAuthenticationPresenter(this, mContext, new AuthenticationCommand
                (AuthenticationCommandAPI.class, this));
    }

    @Override
    public void initView() {
        GoToAuthenticationExtra extra = getIntentExtra(GoToAuthenticationExtra.getExtraName());
        if (extra != null) {
            goToAuthenticationExtra = extra;
        }
        ivShowCarFrontPhoto.post(new Runnable() {
            @Override
            public void run() {
                if (goToAuthenticationExtra != null && goToAuthenticationExtra.getInType() == GoToAuthenticationExtra
                        .IN_TYPE_SHOW) {
                    getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.car_authentication);
                    isShowMode = true;

                    mPresenter.queryCarAuthenStatus(); // 请求网络，获取审核状态和图片地址
                    llProgress.setVisibility(View.GONE);
                    vvvvvv.setVisibility(View.GONE);
                }
            }
        });

        etCarNumber.addTextChangedListener(new EditChangedListener(etCarNumber));
    }

    /**
     * 小写转大写监听
     */
    public class EditChangedListener implements TextWatcher {
        private EditText et;

        EditChangedListener(EditText et) {
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 当输入为小写字母时，自动转换为大写字母
            et.removeTextChangedListener(this);// 解除文字改变事件
            et.setText(s.toString().toUpperCase());// 转换
            et.setSelection(s.toString().length());// 重新设置光标位置
            et.addTextChangedListener(this);// 重新绑
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkParams();
        }
    }

    @Override
    protected void onClickRightText() { // 跳过
        turnToActivityWithFinish(RespectiveData.getMainActivityClass());
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.rl_upload_car_front_photo, R.id.rl_upload_vehicle_license, R.id.tv_submit, R.id.iv_location, R.id
            .tv_location, R.id.rl_type_length, R.id.rl_car_front_fail, R.id.rl_vehicle_license_fail})
    public void clickViews(View v) {
        switch (v.getId()) {
            case R.id.rl_upload_car_front_photo:    // 车头照
            case R.id.rl_car_front_fail:
                curPhotoType = mPresenter.TYPE_CAR_FRONT_PHOTO;
                new AuthenticationPopup(this)
                        .initPop(R.string.car_front_photo, R.drawable.approve_headstock_eg, this)
                        .showPop(rlUploadCarFrontPhoto);
                break;
            case R.id.rl_upload_vehicle_license:    // 行驶证
            case R.id.rl_vehicle_license_fail:
                curPhotoType = mPresenter.TYPE_VEHICLE_LICENSE_PHOTO;
                new AuthenticationPopup(this)
                        .initPop(R.string.driving_license, R.drawable.approve_car_license_eg, this)
                        .showPop(rlUploadVehicleLicense);
                break;
            case R.id.tv_submit: // 提交
                mPresenter.savetruck(tvLocation.getText().toString(), etCarNumber.getText().toString(), (
                                (RadioButton) findViewById(rgCarNumberColor.getCheckedRadioButtonId())).getText()
                                .toString(),
                        frontPath, vehicleLicensePath);
                break;
            case R.id.iv_location:
            case R.id.tv_location:
                PlateWheelPop mPop = new PlateWheelPop(this, ivLocation, tvLocation.getText().toString(), new
                        OnCarPlateCompleteListener() {
                            @Override
                            public void OnCompleteListener(String plate1) {
                                tvLocation.setText(plate1);
                            }
                        });
                mPop.showPop();
                break;

            case R.id.rl_type_length:   // 车型车长
                new TruckLengthAndTypePopupWindow(TAG, 1, 1, false)
                        .showPopupWindow(this, tvTypeLength, false, TruckLengthAndTypePopupWindow
                                .ENTIRETY_SELECTED_INDEX_1, true, mPresenter.getTld());
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
            if (curPhotoType == 0) {
                CameraUtil.jumpToCameraGetPic(this);// 车头照
            } else if (curPhotoType == 1) {
                CameraUtil.jumpToCameraGetPic(this);// 行驶证
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CameraUtil.REQUEST_CODE.SYSTEM_CAMERA:  // 系统拍照
                    if (curPhotoType == mPresenter.TYPE_CAR_FRONT_PHOTO) {
                        frontPath = CameraUtil.currentPicPath;
                        // 获取得到拍照图片后跳转裁剪页面
                        ImageUtil.toCropForSquare(this, new File(frontPath));
                    } else if (curPhotoType == mPresenter.TYPE_VEHICLE_LICENSE_PHOTO) {
                        vehicleLicensePath = CameraUtil.currentPicPath;
                        // 获取得到拍照图片后跳转裁剪页面
                        ImageUtil.toCropForSquare(this, new File(vehicleLicensePath));
                    }
                    break;
                case CameraUtil.REQUEST_CODE.SQUARE_CROP: // 裁剪后
                    if (curPhotoType == mPresenter.TYPE_CAR_FRONT_PHOTO) {
                        frontPath = data.getStringExtra("filePath");
                        showCarFrontPhoto(frontPath, null); // 显示车头照
                    } else {
                        vehicleLicensePath = data.getStringExtra("filePath");
                        showVehicleLicense(vehicleLicensePath, null); // 显示行驶证
                    }

                    checkParams();  // 校验字段，一切ok，tvSubmit亮起
                    break;
            }
        }
    }


    /**
     * 提交成功，跳转首页
     */
    @Override
    public void submitSuccess() {
        if (isShowMode) {
            Toaster.showLongToast(R.string.update_authentication_success);
            setResultAndFinish(Activity.RESULT_OK);
        } else {
            turnToActivityWithFinish(RespectiveData.getMainActivityClass());
        }
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

    /**
     * 校验字段
     */
    private void checkParams() {
        boolean isSuccess = mPresenter.paramsSuccess(false, tvLocation.getText().toString(), etCarNumber.getText()
                        .toString(), ((RadioButton) findViewById(rgCarNumberColor.getCheckedRadioButtonId())).getText().toString(),
                frontPath, vehicleLicensePath);
        tvSubmit.setEnabled(isSuccess);
    }

    @Override
    public void setTypeAndLengthText(String txt) {
        tvTypeLength.setText(txt);
        tvTypeLengthResult.setText(txt);

        checkParams();  // 校验字段，一切ok，tvSubmit亮起
    }

    @Override
    public void setUseStatus(String use_status_remark, String use_status) {
        if (StringUtils.isEmpty(use_status)) {
            return;
        }

        /**
         * 1 未认证 2 已认证 3 认证中 4 认证失败
         */
        boolean canClickPhoto = false;

        switch (use_status) {
            case "1":
                canClickPhoto = true;
                break;

            case "2":
                hideAddAndHint();

                llCarFrontAuthenticaioning.setVisibility(View.VISIBLE);
                llVehicleLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                tvCarFrontAuthenticaioning.setText(R.string.authentication_success);
                tvVehicleLicenseAuthenticaioning.setText(R.string.authentication_success);
                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;

            case "3":
                hideAddAndHint();

                llCarFrontAuthenticaioning.setVisibility(View.VISIBLE);
                llVehicleLicenseAuthenticaioning.setVisibility(View.VISIBLE);
                canClickPhoto = false;
                tvSubmit.setVisibility(View.GONE);
                break;

            case "4":
                hideAddAndHint();

                canClickPhoto = true;
                rlCarFrontFail.setVisibility(View.VISIBLE);
                rlVehicleLicenseFail.setVisibility(View.VISIBLE);
                break;
        }

        tvStatus.setText(use_status_remark);
        rlCarFrontFail.setEnabled(canClickPhoto);
        rlUploadCarFrontPhoto.setEnabled(canClickPhoto);
        rlUploadVehicleLicense.setEnabled(canClickPhoto);
        rlVehicleLicenseFail.setEnabled(canClickPhoto);
    }

    /**
     * 隐藏加号和提示
     */
    private void hideAddAndHint() {
        tvCarFrontHint.setVisibility(View.INVISIBLE);
        ivCarFrontAdd.setVisibility(View.INVISIBLE);
        tvVehicleLicenseHint.setVisibility(View.INVISIBLE);
        ivVehicleLicenseAdd.setVisibility(View.INVISIBLE);
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
    public void showViewByParams(String plate_color, String plate_no1, String carNumberNoLocation, String carNumber) {
        // 1 蓝色 2 黄色
        String carNumberColor = "";
        if (plate_color != null && plate_color.equals("1")) {
            rbBlue.setChecked(true);
            carNumberColor = "蓝牌";
        } else {
            rbYellow.setChecked(true);
            carNumberColor = "黄牌";
        }
        tvLocation.setText(plate_no1);
        etCarNumber.setText(carNumberNoLocation);
        tvCarNumberResult.setText(carNumber);
        tvCarNumberColorResult.setText(carNumberColor);
    }

    /**
     * 显示部分、隐藏部分
     */
    public void changeShowMode(boolean showResult) {
        if (showResult) {
            includeCarinfo.setVisibility(View.GONE);
            includeCarinfoResult.setVisibility(View.VISIBLE);
        } else {
            includeCarinfo.setVisibility(View.VISIBLE);
            includeCarinfoResult.setVisibility(View.GONE);
        }
    }

    public static void turnToRealNameActivity(MvpActivity mActivtiy, int type) {
        mActivtiy.turnToActivity(CarAuthenticationActivity.class, new GoToAuthenticationExtra(type));
    }
}
