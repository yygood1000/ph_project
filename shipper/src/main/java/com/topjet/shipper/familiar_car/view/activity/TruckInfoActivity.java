package com.topjet.shipper.familiar_car.view.activity;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.common.order_detail.modle.bean.DriverInfo;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.familiar_car.model.extra.TruckExtra;
import com.topjet.shipper.familiar_car.model.respons.TruckInfoResponse;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommand;
import com.topjet.shipper.familiar_car.model.serverAPI.TruckCommandAPI;
import com.topjet.shipper.familiar_car.presenter.TruckInfoPresenter;
import com.topjet.shipper.user.view.activity.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * creator: zhulunjun
 * time:    2017/10/19
 * describe: 车辆详情
 */

public class TruckInfoActivity extends MvpActivity<TruckInfoPresenter> implements TruckInfoView {
    @BindView(R.id.iv_truck)
    ImageView ivTruck;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.rb_score)
    RatingBar rbScore;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_truck_info)
    TextView tvTruckInfo;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.sv_content)
    NestedScrollView svContent;
    @BindView(R.id.tv_truck_text)
    TextView tvTruckText;
    @BindView(R.id.tv_btn_one)
    TextView tvBtnOne;
    @BindView(R.id.tv_btn_two)
    TextView tvBtnTwo;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.ll_driver_info)
    LinearLayout llDriverInfo;

    private TruckInfoResponse mInfo;
    private boolean isFamiliarCar = false; //是否是熟车

    /**
     * 外部跳转的方法
     *
     * @param activity
     * @param truckId
     */
    public static void truckInfo(MvpActivity activity, String truckId) {
        activity.turnToActivity(TruckInfoActivity.class, new TruckExtra(truckId));
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TruckInfoPresenter(this, this, new TruckCommand(TruckCommandAPI.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_truck_info;
    }

    @Override
    protected void initView() {
        getMyTitleBar().setTitleText(getString(R.string.truck_info));
        tvBtnOne.setText(R.string.add_familiar_truck);
        tvBtnTwo.setText(R.string.place_order_to_he);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.truckInfo();
    }

    @Override
    public void bindView(TruckInfoResponse info) {
        if (info == null) {
            return;
        }
        mInfo = info;
        llContent.setVisibility(View.VISIBLE);
        TruckInfo truckInfo = info.getTruck_info();
        if (StringUtils.isNotBlank(truckInfo.getTruck_hread_image_url())) {
            GlideUtils.loaderImage(
                    truckInfo.getTruck_hread_image_url(),
                    truckInfo.getTruck_hread_image_key(),
                    R.drawable.icon_car_head_default,
                    R.drawable.icon_car_head_loading,
                    ivTruck);
        }
        String truckText = truckInfo.getTruck_plate() + "  " + truckInfo.getTruck_type() + "  " + truckInfo
                .getTruck_length();
        tvTruckText.setText(truckText);

        tvTime.setText(TimeUtils.showUpdateTimeDay(info.getGps_update()));
        tvAddress.setText(info.getDetail());
        tvCity.setText(info.getBusinesslinecentre_list());

        if (info.getDriver_info() != null) {
            // 用户信息
            tvUserName.setText(info.getDriver_info().getDriver_name());
            String orderNumStr = getString(com.topjet.common.R.string.order_num_str);
            // 成交笔数
            String clinch_a_deal_sum = info.getDriver_info().getClinch_a_deal_sum().trim();
            if (clinch_a_deal_sum.equals("0")) {
                tvTruckInfo.setVisibility(View.GONE);
            } else {
                tvTruckInfo.setText(String.format(orderNumStr, clinch_a_deal_sum));
            }
            // 手机
            tvOrderNum.setText(info.getDriver_info().getDriver_mobile());
            // 用户头像
            GlideUtils.loaderImageRound(this,
                    info.getDriver_info().getDriver_icon_url(),
                    info.getDriver_info().getDriver_icon_key(),
                    com.topjet.common.R.drawable.shape_avatar_loading,
                    com.topjet.common.R.drawable.shape_avatar_loading,
                    ivUserAvatar,
                    4);
            // 星星
            rbScore.setRating(info.getDriver_info().getDriver_comment_level());
        } else {
            // 隱藏，沒有數據
            llDriverInfo.setVisibility(View.GONE);
        }

        // 没有定位，就隐藏车辆位置的入口
        if (StringUtils.isEmpty(info.getDetail())) {
            llAddress.setVisibility(View.GONE);
            llTime.setVisibility(View.GONE);
        } else {
            llAddress.setVisibility(View.VISIBLE);
            llTime.setVisibility(View.VISIBLE);
        }
        isFamiliarCar = info.getIs_familiar_truck();
        setFamiliarState();

    }

    /**
     * 设置熟车状态
     */
    private void setFamiliarState() {
        if (isFamiliarCar) {
            tvBtnOne.setText(R.string.delete_familiar_car);
        } else {
            tvBtnOne.setText(R.string.add_familiar_truck);
        }
    }

    @Override
    public void addSuccess() {
        isFamiliarCar = true;
        setFamiliarState();
    }


    @Override
    public void deleteSuccess() {
        isFamiliarCar = false;
        setFamiliarState();
    }

    /**
     * 跳转定位页面
     */
    @OnClick(R.id.ll_address)
    public void truckPosition() {
        TruckExtra extra = new TruckExtra();
        if (mInfo != null && mInfo.getTruck_info() != null) {
            extra.setLatitude(mInfo.getLatitude());
            extra.setLongitude(mInfo.getLongitude());
            extra.setTruck_plate(mInfo.getTruck_info().getTruck_plate());
            extra.setTime(mInfo.getGps_update());
            extra.setAddress(mInfo.getDetail());
            if (mInfo.getDriver_info() != null) {
                extra.setMobile(mInfo.getDriver_info().getDriver_mobile());
                extra.setName(mInfo.getDriver_info().getDriver_name());
            }
        }
        TruckPositionActivity.truckPosition(this, extra);
    }

    @OnClick(R.id.iv_call)
    public void call(View view) {
        /**
         * 拨打电话
         */
        if (mInfo != null && mInfo.getDriver_info() != null) {
            new CallPhoneUtils().showCallDialogWithAdvNotUpload(this, view,
                    mInfo.getDriver_info().getDriver_name(),
                    mInfo.getDriver_info().getDriver_mobile(),
                    3);
        }
    }

    @OnClick(R.id.iv_message)
    public void message() {
        if (mInfo != null && mInfo.getDriver_info() != null) {
            DriverInfo info = mInfo.getDriver_info();
            CommonProvider.getInstance().getJumpChatPageProvider()
                    .jumpChatPage(this, info.getIMUserInfo(info));
        }
    }

    /**
     * 添加熟车
     */
    @OnClick(R.id.tv_btn_one)
    public void addFamiliar() {
        if (isFamiliarCar) {
            mPresenter.addOrDeleteCar(TruckParams.DELETE);
        } else {
            mPresenter.addOrDeleteCar(TruckParams.ADD);
        }
    }

    /**
     * 给他下单
     */
    @OnClick(R.id.ll_btn_two)
    public void order() {
        CheckUserStatusUtils.isRealNameAuthentication(this, new CheckUserStatusUtils.OnJudgeResultListener() {
            @Override
            public void onSucceed() {
                if (mInfo != null && mInfo.getDriver_info() != null && mInfo.getTruck_info() != null) {
                    List<String> listLength = new ArrayList<>();
                    listLength.add(mInfo.getTruck_info().getTruck_length_id());
                    List<String> listType = new ArrayList<>();
                    listType.add(mInfo.getTruck_info().getTruck_type_id());
                    Logger.i("oye", "truckTypeId == " + mInfo.getTruck_info().getTruck_type_id());
                    Logger.i("oye", "truckLengthId == " + mInfo.getTruck_info().getTruck_length_id());
                    DeliverGoodsActivity.turnToDeliverGoodsForAddAssigned(TruckInfoActivity.this,
                            mInfo.getDriver_info().getDriver_id(),
                            mInfo.getTruck_info().getTruck_id(),
                            listType,
                            listLength);
                }
            }
        });

    }

    /**
     * 跳转司机用户信息页，诚信查询结果页
     */
    @OnClick(R.id.ll_driver_info)
    public void userInfo() {
        if (mInfo.getDriver_info() != null && StringUtils.isNotBlank(mInfo.getDriver_info().getDriver_mobile())) {
            turnToActivity(UserInfoActivity.class, new PhoneExtra(mInfo.getDriver_info().getDriver_mobile()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
