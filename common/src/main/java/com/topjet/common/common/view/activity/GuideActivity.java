package com.topjet.common.common.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.topjet.common.R;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.user.presenter.GuidePresenter;

import java.util.ArrayList;

/**
 * 装机引导页
 * Created by tsj004 on 2017/8/7.
 */

public class GuideActivity extends MvpActivity<GuidePresenter> implements GuideView {
    private ArrayList<View> pageViews;
    private Button btn_guide;
    private View item1, item2, item3, item4;
    private RelativeLayout rl_guide1, rl_guide2, rl_guide3, rl_guide4;
    private ViewPager viewPager;


    private boolean isToMain = false;   // 是否直接跳转主页


    @Override
    public void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                isToMain = bundle.getBoolean("isToMain");
            }
        }

        LayoutInflater inflater = getLayoutInflater();
        item1 = inflater.inflate(R.layout.guide1, null);
        rl_guide1 = (RelativeLayout) item1.findViewById(R.id.rl_guide1);
        item2 = inflater.inflate(R.layout.guide2, null);
        rl_guide2 = (RelativeLayout) item2.findViewById(R.id.rl_guide2);
        item3 = inflater.inflate(R.layout.guide3, null);
        rl_guide3 = (RelativeLayout) item3.findViewById(R.id.rl_guide3);
        item4 = inflater.inflate(R.layout.guide4, null);
        rl_guide4 = (RelativeLayout) item4.findViewById(R.id.rl_guide4);
        btn_guide = (Button) item4.findViewById(R.id.btn_guide);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mPresenter.showPage();
        btn_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setOnClick(isToMain);
            }
        });
    }

    @Override
    public void setrRlGuide1BackgroundResource(int resid) {
        rl_guide1.setBackgroundResource(resid);
    }

    @Override
    public void setrRlGuide2BackgroundResource(int resid) {
        rl_guide2.setBackgroundResource(resid);
    }

    @Override
    public void setrRlGuide3BackgroundResource(int resid) {
        rl_guide3.setBackgroundResource(resid);
    }

    @Override
    public void setrRlGuide4BackgroundResource(int resid) {
        rl_guide4.setBackgroundResource(resid);
    }

    @Override
    public void setBtnGuideBackgroundResource(int resid) {
        btn_guide.setBackgroundResource(resid);
    }

    @Override
    public void setBtnGuideTextColor(int colorid) {
        btn_guide.setTextColor(colorid);
    }

    @Override
    public ArrayList<View> setArrayList() {
        pageViews = new ArrayList<>();
        pageViews.add(item1);
        pageViews.add(item2);
        pageViews.add(item3);
        pageViews.add(item4);
        return pageViews;
    }

    @Override
    public ArrayList<View> getArrayList() {
        return pageViews;
    }

    @Override
    public int getArrayListSize() {
        if (pageViews != null) {
            return pageViews.size();
        } else {
            return 0;
        }
    }

    @Override
    public void setViewPager(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    @Override
    public void goToLoginActivity() {
        turnToActivityWithFinish(RespectiveData.getLoginActivityClass());
    }

    @Override
    protected void initPresenter() {
        mPresenter = new GuidePresenter(this, mContext);
    }

    @Override
    protected int getLayoutResId() {
        // 不要titlebar、在返回布局前加这一句
        noHasTitle();
        return R.layout.activity_guide;
    }
}
