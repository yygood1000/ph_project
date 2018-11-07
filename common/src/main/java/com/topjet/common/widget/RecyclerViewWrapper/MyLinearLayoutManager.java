package com.topjet.common.widget.RecyclerViewWrapper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.topjet.common.utils.Logger;

/**
 * creator: zhulunjun
 * time:    2017/11/3
 * describe: 解决recycleview 下拉刷新时上滑闪退
 * java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid item position 3(offset:3).state:16
 */

public class MyLinearLayoutManager extends LinearLayoutManager {
    public MyLinearLayoutManager(Context context) {
        super( context );
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super( context, orientation, reverseLayout );
    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren( recycler, state );

        } catch (IndexOutOfBoundsException e) {
            Logger.e("onLayoutChildren "+e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

}