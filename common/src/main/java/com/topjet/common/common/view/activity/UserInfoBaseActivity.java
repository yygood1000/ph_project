package com.topjet.common.common.view.activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.presenter.UserInfoPresenter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * creator: zhulunjun
 * time:    2017/11/10
 * describe: 用户信息页，通过诚信查询过来的，司机或者货主信息
 */

public abstract class UserInfoBaseActivity<D> extends BaseRecyclerViewActivity<UserInfoPresenter, D> implements
        UserInfoView<D> {

    private LayoutInflater mInflater;
    // 头部控件
    ImageView ivAvatar;
    TextView tvLabelUserType;
    TextView tvLabelReal;
    TextView tvLabelIdentity;
    TextView tvOrderName;
    TextView tvLabelCar;
    RatingBar rbScore;
    TextView tvScore;
    TextView tvIntegrityMark;
    TextView tvIntegrityValue;
    TextView tvOrderNumMark;
    TextView tvOrderNum;
    TextView tvDealNumMark;
    TextView tvDealNum;
    TextView tvOftenCity;
    TextView tvEmpty;
    LinearLayout llUserInfo;
    LinearLayout llEmpty;

    @BindView(R2.id.tv_btn_message)
    TextView tvBtnMessage;
    @BindView(R2.id.tv_btn_call)
    TextView tvBtnCall;

    private View mView, mDriverView;
    private LinearLayout mEmptyView;
    public UserInfo mUserInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        super.initView();
        mInflater = LayoutInflater.from(this);
        getUserInfoView();
        if (CMemoryData.isDriver()) {
            tvBtnMessage.setTextColor(getResources().getColor(R.color.v3_common_blue));
            tvBtnCall.setBackgroundResource(R.drawable.selector_btn_square_blue);
        }
        setNoPageMode(); // 设置不分页
    }

    @Override
    protected void initPresenter() {
        mPresenter = new UserInfoPresenter(this, this);
    }

    /**
     * 用户信息，recycleView头
     */
    private void getUserInfoView() {
        mView = mInflater.inflate(R.layout.layout_user_info_top, null);

        ivAvatar = ButterKnife.findById(mView, R.id.iv_avatar);
        tvOrderName = ButterKnife.findById(mView, R.id.tv_order_name);
        tvLabelUserType = ButterKnife.findById(mView, R.id.tv_label_user_type);
        tvLabelReal = ButterKnife.findById(mView, R.id.tv_label_real);
        tvLabelIdentity = ButterKnife.findById(mView, R.id.tv_label_identity);
        tvLabelCar = ButterKnife.findById(mView, R.id.tv_label_car);
        rbScore = ButterKnife.findById(mView, R.id.rb_score);
        tvScore = ButterKnife.findById(mView, R.id.tv_score);
        tvIntegrityMark = ButterKnife.findById(mView, R.id.tv_integrity_mark);
        tvIntegrityValue = ButterKnife.findById(mView, R.id.tv_integrity_value);
        tvOrderNumMark = ButterKnife.findById(mView, R.id.tv_order_num_mark);
        tvOrderNum = ButterKnife.findById(mView, R.id.tv_order_num);
        tvDealNumMark = ButterKnife.findById(mView, R.id.tv_deal_num_mark);
        tvDealNum = ButterKnife.findById(mView, R.id.tv_deal_num);
        llUserInfo = ButterKnife.findById(mView, R.id.ll_user_info);

        llUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.turnToCommentListOthersActivity(UserInfoBaseActivity.this, mUserInfo.getUser_id());
            }
        });

        if(CMemoryData.isDriver()){
            tvOrderName.setText("发货数");
        } else {
            tvOrderName.setText("接单数");
        }

    }

    /**
     * 司机常跑城市的view
     */
    private void getDriverView() {
        mDriverView = mInflater.inflate(R.layout.layout_user_info_top_driver, null);
        tvOftenCity = ButterKnife.findById(mDriverView, R.id.tv_often_city);

        checkParentView(mDriverView);
        recyclerViewWrapper.getAdapter().addHeaderView(mDriverView, -2);
    }

    /**
     * 空数据ui
     */
    private void getEmptyView(){
        mEmptyView = (LinearLayout) mInflater.inflate(R.layout.layout_empty, null);
        ButterKnife.findById(mEmptyView, R.id.tv_btn_empty).setVisibility(View.GONE);
        tvEmpty = ButterKnife.findById(mEmptyView, R.id.tv_empty);
        llEmpty = ButterKnife.findById(mEmptyView, R.id.ll_empty);
        mEmptyView.addView(getWhiteView());
        if(CMemoryData.isDriver()){
            tvEmpty.setText("暂无货源");
        } else {
            tvEmpty.setText("暂未添加车辆");
        }
        checkParentView(mEmptyView);
        recyclerViewWrapper.getAdapter().addHeaderView(mEmptyView, -1);
    }

    /**
     * 补全下面的灰色区域
     * 获取一个白色视图
     */
    private View getWhiteView(){
        // 补全下面的灰色区域
        View whiteView = new View(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.dp2px(this,200));
        whiteView.setLayoutParams(params);
        whiteView.setBackgroundColor(Color.WHITE);
        return whiteView;
    }


    @Override
    public void loadSuccess(List<D> data) {
        super.loadSuccess(data);
        if ((data == null || data.size() == 0) && page == 1) {
            setNoPageMode();
            // 显示空
            if(llEmpty == null){
                getEmptyView();
            } else {
                llEmpty.setVisibility(View.VISIBLE);
            }
        } else {
            if(llEmpty != null){
                llEmpty.setVisibility(View.GONE);
            }
            setHavePageMode();
        }
        recyclerViewWrapper.notifyDataSetChanged();
    }

    @Override
    public void setUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            mUserInfo = userInfo;

            // 设置头数据
            setData(userInfo);
            if ((mUserInfo.getUser_type().equals(UserInfo.SHIPPER) && CMemoryData.isDriver()) ||
                    (mUserInfo.getUser_type().equals(UserInfo.DRIVER) && !CMemoryData.isDriver())) {
                setHavePageMode();
            }
            checkParentView(mView);
            recyclerViewWrapper.getAdapter().addHeaderView(mView, -3);

            // 加载列表数据
            onRefresh();
        }
    }

    /**
     * 设置头部数据
     */
    private void setData(UserInfo info) {
        if (info != null) {
            // 用户名
            getMyTitleBar().setTitleText(mUserInfo.getUser_name());

            if (StringUtils.isNotBlank(info.getIcon_image_url())) {
                GlideUtils.loaderImageCircle(this,
                        info.getIcon_image_url(),
                        info.getIcon_image_key(),
                        R.drawable.shape_avatar_loading,
                        R.drawable.shape_avatar_loading,
                        ivAvatar);
                // 身份
                if (info.getUser_type().equals(UserInfo.DRIVER)) {
                    tvLabelUserType.setText(R.string.driver_label);
                } else {
                    tvLabelUserType.setText(R.string.shipper_label);
                }
                // 实名，身份 认证
                setLabelStyle(tvLabelReal, info.getUse_status());
                setLabelStyle(tvLabelIdentity, info.getUser_auth_status());
                // 车辆认证
                if (info.getUser_type().equals(UserInfo.DRIVER)) {
                    setLabelStyle(tvLabelCar, info.getTruck_status());
                } else {
                    tvLabelCar.setVisibility(View.GONE);
                }
                // 好评度
                rbScore.setRating(info.getDegree_of_praise());
                tvScore.setText(info.getEvaluation_score() + "分");

                // 诚信值
                tvIntegrityValue.setText(info.getIntegrity_value());
                // 诚信等级
                if (StringUtils.isNotBlank(info.getIntegrity_value_level())) {
                    tvIntegrityMark.setText(info.getIntegrity_value_level());
                } else {
                    tvIntegrityMark.setVisibility(View.INVISIBLE);
                }
                // 接单数
                tvOrderNum.setText(info.getShipments_or_receiving_num());
                // 成交数
                tvDealNum.setText(info.getClinch_a_deal_num());


            }

        }
    }

    /**
     * 设置用户标签的样式
     */
    private void setLabelStyle(TextView text, boolean isPass) {
        if (isPass) {
            text.setTextColor(getResources().getColor(R.color.text_color_FFB000));
            text.setBackgroundResource(R.drawable.icon_bg_yellow_frame);
        } else {
            text.setTextColor(getResources().getColor(R.color.text_color_969696));
            text.setBackgroundResource(R.drawable.icon_bg_gray_frame);
        }
    }

    /**
     * 发私信
     */
    @OnClick(R2.id.tv_btn_message)
    public void messageClick() {
        if(mUserInfo != null) {
            CommonProvider.getInstance().getJumpChatPageProvider()
                    .jumpChatPage(this, mUserInfo.getIMUserInfo(mUserInfo));
        } else {
            finishPage();
        }
    }

    /**
     * 打电话
     */
    @OnClick(R2.id.tv_btn_call)
    public void callClick(final View view) {
        CheckUserStatusUtils.isRealNameAuthentication(this, new CheckUserStatusUtils.OnJudgeResultListener() {
            @Override
            public void onSucceed() {
                int type = CMemoryData.isDriver() ? 1 : 3; // 司机和货主，不同文本显示
                new CallPhoneUtils().showCallDialogWithAdvNotUpload(UserInfoBaseActivity.this, view, mUserInfo.getUser_name(),
                        mUserInfo
                        .getUser_mobile(), type);
            }
        });
    }

    @Override
    public void setBusinessLine(String lines) {
        if(tvOftenCity == null) {
            getDriverView();
        }
        // 常跑城市
        if (StringUtils.isNotBlank(lines)) {
            tvOftenCity.setText(lines);
        }
    }

    /**
     * 检查即将要添加的view是否已有父布局
     * @param view 即将要添加的view
     */
    public void checkParentView(View view){
        if (view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
    }
}
