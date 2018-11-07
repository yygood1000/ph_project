package com.topjet.common.common.view.activity;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.topjet.common.base.view.activity.IView;

import java.util.ArrayList;

/**
 * 引导页View接口
 * Created by tsj004 on 2017/8/7.
 */

public interface GuideView extends IView {
    void setrRlGuide1BackgroundResource(int resid);

    void setrRlGuide2BackgroundResource(int resid);

    void setrRlGuide3BackgroundResource(int resid);

    void setrRlGuide4BackgroundResource(int resid);

    void setBtnGuideBackgroundResource(int resid);

    void setBtnGuideTextColor(int colorid);

    ArrayList<View> getArrayList();

    ArrayList<View> setArrayList();

    int getArrayListSize();

    void setViewPager(PagerAdapter adapter);

    void goToLoginActivity();
}
