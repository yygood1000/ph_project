package com.topjet.shipper.user.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.common.modle.extra.SettingExtra;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.common.view.activity.MyIntegralActivity;
import com.topjet.common.common.view.activity.SettingActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.controller.ListViewItemDecoration;
import com.topjet.common.user.modle.bean.PersonCenterEntranceItem;
import com.topjet.common.user.view.activity.EditAvatarActivity;
import com.topjet.common.user.view.adapter.PersonCenterEntranceAdapter;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyScrollView;
import com.topjet.common.widget.SwipeRefreshLayout.CarRunRefreshView;
import com.topjet.common.widget.SwipeRefreshLayout.RefreshView;
import com.topjet.common.widget.SwipeRefreshLayout.SwipeRefreshLayout;
import com.topjet.shipper.R;
import com.topjet.shipper.user.presenter.PersonCenterPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by tsj-004 on 2017/10/30.
 * 个人中心 Fragment
 */

public class PersonCenterFragment extends BaseMvpFragment<PersonCenterPresenter> implements PersonCenterView,
        MyScrollView.ScrollViewListener,
        BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.iv_titlebar)
    ImageView ivTitlebar;
    @BindView(R.id.iv_all_background)
    ImageView ivBottomBackground;
    @BindView(R.id.ll_0001)
    LinearLayout ll_0001;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.iv_shen)
    ImageView ivShen;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_authentication_status)
    ImageView ivAuthenticationStatus;
    @BindView(R.id.tv_evaluate)
    TextView tvEvaluate;
    @BindView(R.id.tv_integral)
    TextView tvIntegral;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.cb_listen_order)
    CheckBox cbListenOrder;
    @BindView(R.id.rl_customer_service)
    RelativeLayout rlCustomerService;
    @BindView(R.id.scrollView)
    MyScrollView scrollView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rl_listen_order)
    RelativeLayout rlListenOrder;


    PersonCenterEntranceAdapter personCenterEntranceAdapter = new PersonCenterEntranceAdapter(); // 个人中心功能入口

    @Override
    protected void initPresenter() {
        mPresenter = new PersonCenterPresenter(this, mContext, this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_person_center;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getUserCentreParameter();    // 刷新界面数据
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示

        } else {// 重新显示到最前端中
            mPresenter.getUserCentreParameter();    // 刷新界面数据
        }
    }

    @Override
    protected void initView(View v) {
        // 隐藏听单设置
        rlListenOrder.setVisibility(View.GONE);
        if (CMemoryData.isDriver()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ivTitlebar.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_person_center_driver_top21));
            } else {
                ivTitlebar.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_person_center_driver_top));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_0001.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin, 80, layoutParams.rightMargin, 0);
                ll_0001.setLayoutParams(layoutParams);
            }
            ivBottomBackground.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_person_center_driver_all));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ivTitlebar.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_person_center_shipper_top21));
            } else {
                ivTitlebar.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable.bg_person_center_shipper_top));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_0001.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin, 80, layoutParams.rightMargin, 0);
                ll_0001.setLayoutParams(layoutParams);
            }
            ivBottomBackground.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable
                    .bg_person_center_shipper_all));
        }

        personCenterEntranceAdapter.setNewData(mPresenter.getPersonCenterEntranceItems());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setAdapter(personCenterEntranceAdapter);
        recyclerView.addItemDecoration(new ListViewItemDecoration(mContext));
        personCenterEntranceAdapter.setOnItemClickListener(this);

        scrollView.setMyScrollViewListener(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        swipeRefreshLayout.setRefreshView(new CarRunRefreshView(mContext, RefreshView.REFRESH), layoutParams);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getUserCentreParameter();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.ll_integral, R.id.iv_setting, R.id.ll_avatar, R.id.ll_evaluate, R.id.ll_balance, R.id
            .rl_customer_service})
    public void onClickViews(View view) {
        switch (view.getId()) {
            case R.id.ll_integral:  // 积分
                turnToActivity(MyIntegralActivity.class);
                break;
            case R.id.iv_setting:  // 设置
                SettingExtra extra = new SettingExtra();
                if (mPresenter.userInfo == null) {
                    extra.setReferrerName("");
                } else {
                    extra.setReferrerName(mPresenter.userInfo.getRecommend_name());
                }
                turnToActivity(SettingActivity.class, extra);
                break;
            case R.id.ll_avatar:    // 头像
                turnToActivityForResult(EditAvatarActivity.class, 3000);
                break;
            case R.id.ll_evaluate:    // 评价列表
                CommentActivity.turnToCommentListSelfActivity(mActivity);
                break;

            case R.id.ll_balance:
                // 跳转到钱包
                CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new
                        CheckUserStatusUtils.OnJudgeResultListener() {
                            @Override
                            public void onSucceed() {
                                SkipControllerWallet.skipToWalletMain((Activity) mContext);
                            }
                        });
                break;

            case R.id.rl_customer_service:
                // 客服
                new CallPhoneUtils().callCustomerService(mActivity);
                break;
        }
    }

    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
//        float titleBarHeight = ivTitlebar.getHeight();
//        int alpha = (int) (Math.abs((y - titleBarHeight) / titleBarHeight) * 255);
//        alpha = alpha + (int) (255 * 0.5f);
//        if (alpha < 125) {
//            alpha = 125;
//        } else if (alpha > 255) {
//            alpha = 255;
//        }
//        ivTitlebar.getBackground().setAlpha(alpha);
//
//        float a = y / titleBarHeight;
//        tvTitleName.setAlpha(a);
    }

    @Override
    public void setViewByParams(String user_name, String icon_image_url, String icon_image_key, String
            icon_image_status, String sign_status, boolean isHideRightArrow, String today_integral
            , String degree_of_praise, String integral_amount, String wallet_balance, String use_status) {
        swipeRefreshLayout.setRefreshing(false);

        if (StringUtils.isEmpty(user_name)) {
            user_name = "新用户";
        }

        tvName.setText(user_name);
        tvTitleName.setText(user_name);
        tvEvaluate.setText(degree_of_praise);
        tvIntegral.setText(integral_amount);
        tvBalance.setText(wallet_balance);

        /**
         * 0、未认证 1、认证中 2、认证失败 3、认证通过
         */
        if (icon_image_status.equals("1")) {
            ivShen.setVisibility(View.VISIBLE);
        } else {
            ivShen.setVisibility(View.GONE);
        }

        /**
         * 0、未认证 1、待审核 2、审核通过 3、资料修改待审核 4、认证失败
         */
        if (use_status.equals("2")) {
            ivAuthenticationStatus.setImageResource(R.drawable.icon_authenticationed);
        } else {
            ivAuthenticationStatus.setImageResource(R.drawable.icon_unauthentication);
        }

        GlideUtils.loaderImageCircle(mContext, icon_image_url, icon_image_key, R.drawable.icon_default_avatar, R
                .drawable.icon_default_avatar, ivAvatar);

        personCenterEntranceAdapter.setNewData(mPresenter.getPersonCenterEntranceItems());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.getUserCentreParameter();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<PersonCenterEntranceItem> list = mPresenter.getPersonCenterEntranceItems();
        if (list != null) {
            PersonCenterEntranceItem item = list.get(position);
            if (item != null) {
                if (item.getExtra() != null) {
                    turnToActivityForResult(item.getActivityClass(), (position + 1) * 100, item.getExtra());
                } else {
                    turnToActivityForResult(item.getActivityClass(), (position + 1) * 100);
                }
            }
        }
    }

    @Override
    public void requestFail() {
        swipeRefreshLayout.setRefreshing(false);
    }

}
