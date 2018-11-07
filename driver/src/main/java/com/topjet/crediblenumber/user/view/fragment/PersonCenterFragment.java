package com.topjet.crediblenumber.user.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.dialog.AutoDialog;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.common.modle.extra.SettingExtra;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.common.view.activity.MyIntegralActivity;
import com.topjet.common.common.view.activity.SettingActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
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
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.presenter.PersonCenterPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ezy.assist.compat.SettingsCompat;

/**
 * Created by tsj-004 on 2017/10/26.
 * 个人中心 Fragment
 */

public class PersonCenterFragment extends BaseMvpFragment<PersonCenterPresenter> implements PersonCenterView
        , MyScrollView.ScrollViewListener
        , BaseQuickAdapter.OnItemClickListener {
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

    Unbinder unbinder;
    private boolean isToSystemSetPage = false;      // 是否点击了权限设置按钮，跳转到系统设置界面

    private AutoDialog dialog = null;       // 提示开启悬浮窗权限弹窗
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

        if (isToSystemSetPage) {
            isToSystemSetPage = false;

            // 检测悬浮窗是否授权
            boolean canDrawOverlays = SettingsCompat.canDrawOverlays(mContext);
            if (canDrawOverlays) {
                mPresenter.startOrStopFloatViewService(cbListenOrder.isChecked());  // 开启或关闭悬浮窗
            } else {
                Toast.makeText(mContext, "悬浮窗权限已被拒绝", Toast.LENGTH_SHORT).show();
                cbListenOrder.setChecked(false);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示

        } else {// 重新显示到最前端中
            mPresenter.getUserCentreParameter();    // 刷新界面数据
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.getUserCentreParameter();    // 刷新界面数据
    }

    @Override
    protected void initView(View v) {
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
                ivBottomBackground.setBackgroundDrawable(ResourceUtils.getDrawable(R.drawable
                        .bg_person_center_shipper_all));
            }
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

        if (CPersisData.getFloatViewStatus().equals("false")) {
            cbListenOrder.setChecked(false);
        } else if (CPersisData.getFloatViewStatus().equals("true")) {
            cbListenOrder.setChecked(true);
        } else if (StringUtils.isEmpty(CPersisData.getFloatViewStatus())) {
            cbListenOrder.setChecked(true);
            requestOverlayPermission();
        }
        tvTitleName.setAlpha(0);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.cb_listen_order, R.id.ll_integral, R.id.iv_setting, R.id.ll_avatar, R.id.ll_evaluate, R.id
            .ll_balance, R.id.rl_customer_service})
    public void onClickViews(View view) {
        switch (view.getId()) {
            case R.id.cb_listen_order:
                requestOverlayPermission(); // 申请悬浮窗权限
                break;
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

            case R.id.rl_customer_service:  // 客服
                new CallPhoneUtils().callCustomerService(mActivity);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 申请悬浮窗权限
     */
    public void requestOverlayPermission() {
        // 检测悬浮窗是否授权
        boolean canDrawOverlays = SettingsCompat.canDrawOverlays(mContext);
        boolean canWriteSettings = SettingsCompat.canWriteSettings(mContext);

        if (!canDrawOverlays) {
            if (canWriteSettings) {
                // 设置授权状态，仅在未深度定制的 Android 4.3/4.4 上可用
                SettingsCompat.setDrawOverlays(mContext, true);
                SettingsCompat.setWriteSettings(mContext, true);
            }
        }

        if (!canDrawOverlays) {
            dialog = new AutoDialog(mContext);
            dialog.setTitle("为了更好的体验本应用\n需要您开启悬浮窗权限");
            dialog.setRightText("去开启");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setOnBothConfirmListener(new AutoDialog.OnBothConfirmListener() {
                @Override
                public void onLeftClick() {
                    dialog.toggleShow();

                    cbListenOrder.setChecked(false);
                }

                @Override
                public void onRightClick() {
                    dialog.toggleShow();
                    isToSystemSetPage = true;
                    // 跳转到悬浮窗权限设置页
                    SettingsCompat.manageDrawOverlays(mContext);
                }
            });
            dialog.toggleShow();
            return;
        }

        mPresenter.startOrStopFloatViewService(cbListenOrder.isChecked());  // 开启或关闭悬浮窗
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
            icon_image_status, String sign_status, boolean isHideRightArrow, String today_integral, String
                                        degree_of_praise, String integral_amount, String wallet_balance,
                                String use_status) {
        swipeRefreshLayout.setRefreshing(false);

        if (StringUtils.isEmpty(user_name)) {
            user_name = "新用户";
        }
        tvName.setText(user_name);
        tvTitleName.setText(user_name);
        tvEvaluate.setText(degree_of_praise);
        tvIntegral.setText(integral_amount);
        tvBalance.setText(wallet_balance);
        GlideUtils.loaderImageCircle(mContext, icon_image_url, icon_image_key, R.drawable.icon_default_avatar, R
                .drawable.icon_default_avatar, ivAvatar);

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

        personCenterEntranceAdapter.setNewData(mPresenter.getPersonCenterEntranceItems());
    }

    @Override
    public void requestFail() {
        swipeRefreshLayout.setRefreshing(false);
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
}
