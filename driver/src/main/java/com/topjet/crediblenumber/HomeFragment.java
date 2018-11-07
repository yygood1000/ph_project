package com.topjet.crediblenumber;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.topjet.common.adv.ProcessAnnouncementData;
import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.adv.modle.response.GetBannerDataResponse;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.common.modle.response.AppUpgradeResponse;
import com.topjet.common.common.modle.serverAPI.HomeCommand;
import com.topjet.common.common.presenter.SystemPresenter;
import com.topjet.common.common.view.activity.SimpleWebViewActivity;
import com.topjet.common.common.view.adapter.ButtonPageAdapter;
import com.topjet.common.common.view.adapter.ScrollBtnsRecyclerAdapter;
import com.topjet.common.common.view.dialog.UpdateDialog;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.Config;
import com.topjet.common.resource.dict.ScrollButtonsDataDict;
import com.topjet.common.resource.event.UpdataScrollBtnsEvent;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyScrollView;
import com.topjet.common.widget.MyWebView;
import com.topjet.common.widget.RollView;
import com.topjet.common.widget.banner.BannerAdapter;
import com.topjet.common.widget.banner.BannerView;
import com.topjet.crediblenumber.car.view.activity.MyFleetActivity;
import com.topjet.crediblenumber.user.view.activity.UsualCityActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yy on 2017/8/14.
 * 首页 Fragment
 */

public class HomeFragment extends BaseMvpFragment<HomePresenter> implements HomeView, MyScrollView.ScrollViewListener {
    @BindView(R.id.banner_view)
    BannerView mBannerView;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.roll_view)
    RollView mRollView;
    @BindView(R.id.ll_point_group)
    LinearLayout pointGroup;
    @BindView(R.id.web_view)
    MyWebView mWebView;
    @BindView(R.id.ll_float)
    LinearLayout llFloat;
    @BindView(R.id.sv_content)
    MyScrollView svContent;
    @BindView(R.id.rl_headline)
    RelativeLayout rlHeadline;
    @BindView(R.id.ll_headline)
    LinearLayout llHeadline;
    private BannerAdapter mBannerAdapter;
    private int lastPointIndex;// 滑动按钮组 上一页下标
    private ButtonPageAdapter btnsPageAdapter;
    // 首页公告轮播View集合
    private List<View> announcementViewList;

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter(this, mContext, this, new HomeCommand((MvpActivity) getActivity()));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View v) {
        svContent.setMyScrollViewListener(this);
        getAnnouncementTop();
        initBtnPagerAdapter();
        initBanner();
    }

    @Override
    protected void initData() {
        // 获取Banner轮播广告数据
        mPresenter.getBannerData();
        // 获取H5上滑H5地址
        mPresenter.getHomeH5Url();
        // 设置公告栏点击事件
        mPresenter.setAnnouncementClickListener(announcementClikcListener);
        //获取首页公告
        mPresenter.doLocatAndGetHomeAnnouncement();
        // 检测APP是否需要升级
        if (!CMemoryData.isIsShowedUpdataDialog()) {
            new SystemPresenter(this, mContext).checkAppUpgrade();
        }
    }

    /**
     * 初始化Banner广告控件
     */
    private void initBanner() {
        mBannerAdapter = new BannerAdapter(mContext);
        mBannerView.setAdapter(mBannerAdapter);
        // 轮播广告点击事件
        mBannerAdapter.setOnPageClickListener(new BannerAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(BannerBean item) {
                if (StringUtils.isNotBlank(item.getRedirect_url())) {
                    SimpleWebViewActivity.turnToSimpleWebViewActivity(mActivity, item.getRedirect_url(),
                            item.getWeb_title());
                }
            }
        });
    }

    /**
     * 初始化 滑动按钮组
     */
    void initBtnPagerAdapter() {
        btnsPageAdapter = new ButtonPageAdapter();
        mViewPager.setAdapter(btnsPageAdapter);
        // ViewPager切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 设置当前point的状态和前一个point的状态
                pointGroup.getChildAt(position).setEnabled(true);
                pointGroup.getChildAt(lastPointIndex).setEnabled(false);
                // 将当前的arg0赋值给lastPointIndex
                lastPointIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //展示ViewPager内容
        showScrollBtns();
        mViewPager.setCurrentItem(0);
    }

    /**
     * 更新首页滑动按钮组事件
     */
    @Subscribe
    public void onUpdataScrollBtns(UpdataScrollBtnsEvent event) {
        // 获取按钮组信息
        mPresenter.getScrollBtns();
    }

    /**
     * 刷新滑动按钮组
     */
    @Override
    public void showScrollBtns() {
        btnsPageAdapter.setData(ScrollButtonsDataDict.getViewListAndInitPoint(mContext, pointGroup,
                scrollBtnsClickListener));
    }

    /**
     * 设置Banner广告数据
     */
    @Override
    public void setBannerData(GetBannerDataResponse response) {
        List<BannerBean> data = new ArrayList<>();
        if(ListUtils.isEmpty(response.getList()) || response.getList().size() == 1) {
            if(ListUtils.isEmpty(response.getList())) {
                data.add(new BannerBean());
            }else {
                data.add(response.getList().get(0));
            }
            mBannerView.stopAutoScroll();
            mBannerView.setIsScroll(false);
        } else {
            mBannerView.startAutoScroll();
            mBannerView.setIsScroll(true);
            data.addAll(response.getList());
        }
        mBannerAdapter.setData(data);
    }


    /**
     * 加载首页底部H5页面
     */
    @Override
    public void loadHomeWebView(String url) {
        if (StringUtils.isNotBlank(url)) {
            mWebView.setVisibility(View.VISIBLE);
            mWebView.setOnLoadErrorListener(new MyWebView.OnLoadErrorListener() {
                @Override
                public void onError() {
                    mWebView.setVisibility(View.GONE);
                }
            });
            mWebView.loadUrl(url);
        } else {
            mWebView.setVisibility(View.GONE);
        }
    }

    /**
     * 展示公告信息
     */
    @Override
    public void showAnnouncement(List<View> announcementViewList) {
        this.announcementViewList = announcementViewList;
        // 开始滚动公告
        startShowAnnouncement();
    }

    /**
     * 开始滚动公告
     */
    void startShowAnnouncement() {
        if (announcementViewList == null) {
            return;
        }
        if (announcementViewList.size() == 1) {
            mRollView.setAdapter(announcementViewList);
        } else {
            mRollView.setAdapter(announcementViewList).setDelayed(4000).start();
        }
    }

    /*======================↓===========↓====================*/
    /*======================↓各种点击事件↓====================*/
    /*======================↓===========↓====================*/
    /**
     * 公告点击事件
     */
    ProcessAnnouncementData.OnAnnouncementClikcListener announcementClikcListener = new ProcessAnnouncementData
            .OnAnnouncementClikcListener() {
        @Override
        public void onClick(View v) {
            // 公告点击事件,公告id不为空则需要去获取公告跳转url
            if (StringUtils.isNotBlank((String) v.getTag())) {
                mPresenter.getHomeAnnouncementUrl((String) v.getTag());
            }
        }
    };

    /**
     * 我的车队
     */
    @OnClick(R.id.btn_my_truck_team)
    public void myTruckTeam() {
        turnToActivity(MyFleetActivity.class);
    }

    /**
     * 常跑城市
     */
    @OnClick(R.id.btn_usual_city)
    public void clickUsualCity() {
        turnToActivity(UsualCityActivity.class);
    }

    /**
     * 滑动按钮组，司机端特有点击事件
     */
    private ScrollBtnsRecyclerAdapter.OnCustomItemDriverClickListener scrollBtnsClickListener =
            new ScrollBtnsRecyclerAdapter.OnCustomItemDriverClickListener() {
                @Override
                public void turnToShipper() {
                    Logger.d("oye", "切换app turnToShipper");
                    mPresenter.getSwitchKey();
                }

                @Override
                public void turnToIllegalQuery() {
                    // 违章查询
                    SimpleWebViewActivity.turnToSimpleWebViewActivity(mActivity,
                            Config.getIllegalInquiryUrl(),
                            "违章查询",
                            false);
                }
            };

    /**
     * 显示升级弹窗
     */
    @Override
    public void showUpdateDialog(AppUpgradeResponse result) {
        CMemoryData.setIsShowedUpdataDialog();
        new UpdateDialog(mContext, result).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRollView != null) {
            mRollView.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRollView != null) {
            if (!isHidden()) {
                startShowAnnouncement();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRollView != null) {
            mRollView.destroy();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mRollView != null) {
            if (hidden) {
                mRollView.stop();
            } else {
                startShowAnnouncement();
            }
        }
    }

    /**
     * 监听滑动距离，来控制悬浮
     */
    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        Logger.d("floatViewHeight y " + y);
        if (y >= floatViewTop) {
            isFloat = true;
            if (rlHeadline.getParent() != llFloat) {
                llHeadline.removeView(rlHeadline);
                llFloat.addView(rlHeadline);
            }


        } else if (y < floatViewTop && isFloat) {
            isFloat = false;
            if (rlHeadline.getParent() != llHeadline) {
                llFloat.removeView(rlHeadline);
                llHeadline.addView(rlHeadline);
            }
        }
    }

    /**
     * 获取公告的头部坐标，来控制悬浮
     */
    private int floatViewTop;
    private boolean isFloat = false;

    private void getAnnouncementTop() {
        ViewTreeObserver vto = llHeadline.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (llHeadline != null) {
                    floatViewTop = llHeadline.getTop();
                    Logger.d(" floatViewTop " + floatViewTop);
                }
            }
        });
    }
}
