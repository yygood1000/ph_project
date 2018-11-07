package com.topjet.crediblenumber.order.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.topjet.common.utils.Logger;
import com.topjet.common.widget.viewpagetitle.ViewPagerTitle;

/**
 * creator: zhulunjun
 * time:    2017/9/14
 * describe: 让 viewpagertitle 跟随 viewpage 动，当到CoordinatorLayout达顶部是悬停
 */

public class ViewPageTitleBehavior extends CoordinatorLayout.Behavior<ViewPagerTitle> {

    public ViewPageTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewPagerTitle child, View dependency) {
        return dependency instanceof ViewPager;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewPagerTitle child, View dependency) {

        if(dependency instanceof ViewPager) {

            ViewPager viewpager = (ViewPager) dependency;

            int parentTop = parent.getTop();// ViewPagerTitle 悬浮的地方
            LinearLayout linearLayout = (LinearLayout) viewpager.getChildAt(0);
            RecyclerView recyclerView = (RecyclerView) linearLayout.getChildAt(0);
            int scrolly = recyclerView.getScrollY();

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

            int top = viewpager.getTop(); // viewpager滑动的距离


            Logger.d("ViewPageTitleBehavior", top+"   "+parentTop+"  "+scrolly);

//            if(top != parentTop){
//                params.topMargin = top;
//            }
//
//            child.setLayoutParams(params);



        }



        return true;
    }
}
