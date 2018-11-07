package com.topjet.common.user.presenter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.topjet.common.R;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.view.activity.GuideView;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.utils.Logger;

/**
 * 装机引导页Presenter
 * Created by tsj004 on 2017/8/7.
 */

public class GuidePresenter extends BasePresenter<GuideView> {

    public GuidePresenter(GuideView mView, Context mContext) {
        super(mView, mContext);
    }

    /**
     * 显示界面
     */
    public void showPage() {
        if (CMemoryData.isDriver()) {
            mView.setrRlGuide1BackgroundResource(R.drawable.driver_guide1);
            mView.setrRlGuide2BackgroundResource(R.drawable.driver_guide2);
            mView.setrRlGuide3BackgroundResource(R.drawable.driver_guide3);
            mView.setrRlGuide4BackgroundResource(R.drawable.driver_guide4);
            mView.setBtnGuideBackgroundResource(R.drawable.shape_bg_btn_border_blue);
        } else {
            mView.setrRlGuide1BackgroundResource(R.drawable.shipper_guide1);
            mView.setrRlGuide2BackgroundResource(R.drawable.shipper_guide2);
            mView.setrRlGuide3BackgroundResource(R.drawable.shipper_guide3);
            mView.setrRlGuide4BackgroundResource(R.drawable.shipper_guide4);
            mView.setBtnGuideBackgroundResource(R.drawable.shape_bg_btn_border_green);
            mView.setBtnGuideTextColor(mActivity.getResources().getColor(R.color.v3_common_green));
        }

        showViewPager();
    }

    public void showViewPager() {
        mView.setArrayList();
        mView.setViewPager(new GuidePageAdapter());
    }

    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mView.getArrayListSize();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View view, int position, Object object) {
            ((ViewPager) view).removeView(mView.getArrayList().get(position));
        }

        @Override
        public Object instantiateItem(View view, int position) {
            ((ViewPager) view).addView(mView.getArrayList().get(position));
            return mView.getArrayList().get(position);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View view) {
        }

        @Override
        public void finishUpdate(View view) {

        }
    }

    /**
     * 立即体验按扭点击事件
     */
    public void setOnClick(boolean isToMain) {
        CPersisData.setGuideFalse();// 引导页缓存
        if (isToMain) {
            // 跳转APP过来的
            CPersisData.setIsLogin(true);
            mView.turnToActivityWithFinish(RespectiveData.getMainActivityClass());// 跳转主页
        } else {
            mView.goToLoginActivity();
            Logger.i("MyLog", "转到登录页");
        }
    }


}
