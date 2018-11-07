package com.topjet.shipper.order.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ScreenUtils;

/**
 * creator: zhulunjun
 * time:    2017/9/14
 * describe:
 */

public class MyBehavior extends AppBarLayout.ScrollingViewBehavior {

    private AppBarLayout appBarLayout;

    private Context mContext;

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MyBehavior() {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        Logger.d("MyBehavior");
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        if (appBarLayout == null) {
            appBarLayout = (AppBarLayout) dependency;
        }

        Logger.d("MyBehavior", "onDependentViewChanged" + " child.getBottom()  " + child.getBottom() + " parent.getBottom() " + parent.getBottom() + " " +
                "child.getMeasuredHeight() " + child.getMeasuredHeight() + "  parent.getMeasuredHeight() " + parent.getMeasuredHeight() + " " +
                " appBarLayout.getMeasuredHeight " + appBarLayout.getMeasuredHeight() + " appBarLayout.getBottom() " + appBarLayout.getBottom() + "" +
                " getScreenHeight " + ScreenUtils.getScreenHeight(mContext));

        if (child instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) child;
            if (viewPager.getChildCount() > 0) {
                LinearLayout linearLayout = (LinearLayout) viewPager.getChildAt(0);
                Logger.d("linearLayout " + linearLayout.getChildCount());
                if (linearLayout.getChildCount() > 0) {
                    Logger.d(" linearLayout " + (linearLayout.getChildAt(0) instanceof RecyclerView));
                    RecyclerView recyclerView = (RecyclerView) linearLayout.getChildAt(0);
                    Logger.d(" recyclerView.getMeasuredHeight() " + recyclerView.getMeasuredHeight() + " recyclerView.getBottom()  " + recyclerView.getBottom());


                    CoordinatorLayout.LayoutParams layout = (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
                    CoordinatorLayout.LayoutParams layoutapp = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//                    layout.height = recyclerView.getMeasuredHeight();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                    layoutParams.height = viewPager.getHeight();
                    recyclerView.setLayoutParams(layoutParams);

                    if (child.getBottom() <= parent.getMeasuredHeight()) {
                    } else {

                    }
//                    viewPager.setLayoutParams(layout);
//                    appBarLayout.setLayoutParams(layoutapp);

                }
            }
            /**
             * 如果child.getBottom() == parent.getMeasuredHeight() 则appBarLayout不让滑动了
             */
//

        }


        return super.onDependentViewChanged(parent, child, dependency);
    }

    private boolean isScroll = true;


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        Logger.d("MyBehavior", "onStartNestedScroll " + nestedScrollAxes);
        if (child.getBottom() <= coordinatorLayout.getMeasuredHeight()) {
            Logger.d("onStartNestedScroll " + nestedScrollAxes);
            return false;
        } else {
            Logger.d("1111111111111 22222222 " + nestedScrollAxes);
            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Logger.d("MyBehavior", "onNestedScroll ");
        if (child.getBottom() <= coordinatorLayout.getMeasuredHeight()) {

        } else {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }

    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        Logger.d("MyBehavior", "onNestedPreFling ");
        if (child.getBottom() <= coordinatorLayout.getMeasuredHeight()) {
            return false;
        } else {
            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        Logger.d("MyBehavior", "onInterceptTouchEvent ");
//        if (child.getBottom() <= parent.getMeasuredHeight()) {
//            return true;
//        } else {
        return super.onInterceptTouchEvent(parent, child, ev);
//        }


    }


    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        Logger.d("MyBehavior", "onTouchEvent ");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (child.getBottom() <= parent.getMeasuredHeight()) {
                    return true;
                }
                break;
        }
        return super.onTouchEvent(parent, child, ev);
    }
}
