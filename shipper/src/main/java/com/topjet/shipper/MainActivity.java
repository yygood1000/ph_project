package com.topjet.shipper;

import android.app.Dialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.adv.view.HomeAdvDialog;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.listener.OnTabClickListener;
import com.topjet.common.base.view.activity.BaseFragmentActivity;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.common.modle.event.MessageUnReadCountEvent;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.common.modle.serverAPI.HomeCommand;
import com.topjet.common.common.presenter.TransferPresenter;
import com.topjet.common.common.view.fragment.MessageCenterFragment;
import com.topjet.common.im.presenter.IMPresenter;
import com.topjet.common.resource.ResourceService;
import com.topjet.common.resource.event.UpdataTabLayoutBtnsEvent;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.RegularImageView;
import com.topjet.common.widget.bottomlayout.TabLayout;
import com.topjet.shipper.deliver.view.activity.DeliverGoodsActivity;
import com.topjet.shipper.order.view.fragment.MyOrderFragment;
import com.topjet.shipper.user.view.fragment.PersonCenterFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 主Activity
 */
public class MainActivity extends BaseFragmentActivity<MainPresenter> implements MainView {
    private long exitTime;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_mainactivity_statusbar)
    View viewMainactivityStatusbar;
    @BindView(R.id.iv_regular_activity)
    RegularImageView ivRegularActivity;

    private TransferPresenter mTransferPresenter; // 外表跳转的处理
    private Dialog mAdvDialog;

    @Override
    protected int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter(this, this, new HomeCommand(this));
        mTransferPresenter = new TransferPresenter(this);
    }

    private MyOrderFragment myOrderFragment;

    @Override
    protected ArrayList<BaseMvpFragment> getFragments() {
        myOrderFragment = new MyOrderFragment();
        ArrayList<BaseMvpFragment> fragments = new ArrayList<BaseMvpFragment>();
        fragments.add(new HomeFragment());// 首页
        fragments.add(myOrderFragment);//  我的订单
        fragments.add(new MessageCenterFragment());// 服务站
        fragments.add(new PersonCenterFragment());// 个人中心
        return fragments;
    }

    @Override
    public void initView() {
        setStatusBarViewDrawableResource(R.drawable.shape_bg_gradient_shipper);
        // 设置初始标签
        tabLayout.setIndex(TabIndex.HOME_PAGE);
        // 切换到首页
        changerPage(TabIndex.HOME_PAGE);
        // 设置导航栏切换监听
        tabLayout.setmOnTabClickListener(mOnTabClickListener);
    }

    @Override
    protected void initData() {
        // 所有需要调接口的都放到下面，因为，切换app过来调接口缺少参数，需要先登录再进入
        if (!handleTransfer(getIntent())) {
            // 开启资源更新服务
            startService(new Intent(this, ResourceService.class));
            // 获取用户基础参数
            mPresenter.getTheUserParameter();
            // 展示所有需要展示的弹窗
            mPresenter.showAllDialog();
            //获取定时福袋信息
            mPresenter.getRegularActivityData();
            // 获取下次启动页展示时的广告链接
            mPresenter.getSplashActitvityAdvUrl();
            // 开启后台定位服务
            mPresenter.startDoFixedCycleLocation();
            // 货主-获取货源刷新信息
            mPresenter.getGoodsRefreshInfo();

            // 登陆环信
            new IMPresenter(this).loginIM(); // 登录聊天系统
            // 上传聊天敏感字统计 获取敏感字列表
            new IMPresenter(this).updateSensitiveWord();
        }
    }

    /**
     * 导航条点击事件
     */
    private OnTabClickListener mOnTabClickListener = new OnTabClickListener() {
        @Override
        public void onClick(View v, int currentIndex) {
            // 切换Faragment
            changerPage(currentIndex);
        }

        @Override
        public void onCenterClick(View v, boolean isInSmartSearch) {
            // 跳转发货页面
            CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new CheckUserStatusUtils
                    .OnJudgeResultListener() {
                @Override
                public void onSucceed() {
                    DeliverGoodsActivity.turnToDeliverGoodsForAdd(MainActivity.this);
                }
            });
        }
    };

    /**
     * 切换页面
     */
    public void changerPage(int currentIndex) {
        setCurrFragment(mFragments.get(currentIndex), "" + currentIndex);

        if (mFragments.get(currentIndex) instanceof PersonCenterFragment) {
            viewMainactivityStatusbar.setVisibility(View.GONE);
        } else {
            viewMainactivityStatusbar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示定时活动图标
     */
    @Override
    public void showRegularActivityIcon(String url, String key) {
        ivRegularActivity.setVisibility(View.VISIBLE);
        // 定时活动广告点击事件
        ivRegularActivity.setOnCustomClickListener(new RegularImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getRegularActivityUrl();
            }
        });
        GlideUtils.loaderImage(url, key, ivRegularActivity);
    }

    /**
     * 隐藏定时活动图标
     */
    @Override
    public void hideRegularActivityIcon() {
        ivRegularActivity.setVisibility(View.GONE);
    }

    /**
     * 展示首页弹窗广告
     */
    @Override
    public void showHomeAdvDialog(BannerBean bannerBean) {
        mAdvDialog = new HomeAdvDialog(mContext, bannerBean);
        mAdvDialog.show();
    }

    /**
     * 展示首页-下方导航
     */
    @Override
    public void showTabLayoutBtns() {
        tabLayout.reflushView();
    }

    /**
     * 展示首页-下方导航未读消息状态
     */
    @Override
    public void showTabLayoutMessageCorner(String unread_sum) {
        if (StringUtils.isNotBlank(unread_sum) && Double.valueOf(unread_sum) > 0) {
            tabLayout.setCornerVisable(true);
        } else {
            tabLayout.setCornerVisable(false);
        }
    }

    /**
     * 设置小红点的显示状态
     */
    @Subscribe
    public void unReadEvent(MessageUnReadCountEvent event) {
        showTabLayoutMessageCorner(event.getUnReadCount() + "");
    }

    /**
     * 更新首页底部导航栏事件
     */
    @Subscribe
    public void onUpdataTabLayoutBtns(UpdataTabLayoutBtnsEvent event) {
        mPresenter.getTabLayoutBtns();
    }

    /**
     * 再次开启MainActivity走此回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleTransfer(intent);
        intentChangePage(intent);
    }

    /**
     * 外部跳转页面，切换底部
     */
    private void intentChangePage(Intent intent) {
        if (intent != null) {
            TabIndex index = getIntentExtra(intent, TabIndex.getExtraName());
            if (index != null && index.getIndex() != -1) {
                changerPage(index.getIndex());
                Logger.d("跳转index " + tabLayout.getCurrentIndex() + " " + index.getIndex());
                if (tabLayout.getCurrentIndex() != index.getIndex()) {
                    tabLayout.setIndex(index.getIndex());
                }
            }
            // 二级
            if (index != null && index.getOrderIndex() != -1) {
                myOrderFragment.changePage(index.getOrderIndex());
            }
            // 三级
            if (index != null && index.getOrderIndex() != -1 && index.getOrderStateIndex() != -1) {
                myOrderFragment.changeOrderListTabLayout(index.getOrderStateIndex());
            }

        }
    }

    /**
     * 处理外部跳转的intent
     */
    private boolean handleTransfer(Intent intent) {
        // 如果app还没有启动过，就去启动页
//        if (CPersisData.getIsGuide()) {
//            turnToActivityWithFinish(SplashActivity.class);
//            return true;
//        }
        int tabIndex = mTransferPresenter.distributePage(intent);
        if (tabIndex == -2) {
            // 切换app时，不调用主页接口
            return true;
        } else if (tabIndex != -1) {
            changerPage(tabIndex);
            tabLayout.setIndex(tabIndex);
        }
        return false;
    }

    /**
     * 返回键监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                Toaster.showShortToast(getResources().getString(R.string.again_out));
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//            }
            // app 退到后台
            moveTaskToBack(true);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mAdvDialog != null && mAdvDialog.isShowing()) {
            mAdvDialog.dismiss();
        }
        //关闭资源更新服务
        stopService(new Intent(this, ResourceService.class));
        super.onDestroy();
    }
}
