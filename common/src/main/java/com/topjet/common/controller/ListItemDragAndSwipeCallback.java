package com.topjet.common.controller;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.topjet.common.utils.Logger;

/**
 * 列表项侧滑处理
 * Created by tsj004 on 2017/8/23.
 */

public class ListItemDragAndSwipeCallback extends ItemDragAndSwipeCallback {

    private BaseItemDraggableAdapter mAdapter;

    private int viewWidth = 200;

    private float startX = 0;

    /**
     * 初始化函数
     * @param adapter
     * @param viewWidth:菜单项view的宽度
     */
    public ListItemDragAndSwipeCallback(BaseItemDraggableAdapter adapter, int viewWidth) {
        super(adapter);
        this.mAdapter = adapter;
        this.viewWidth = viewWidth;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //重置改变，防止由于复用而导致的显示问题
        viewHolder.itemView.setScrollX(0);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //仅对侧滑状态下的效果做出改变
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来

                if (Math.abs(dX) < viewWidth) {
                    viewHolder.itemView.scrollTo(-(int) dX, 0);
                    Logger.i("=====dX(" + viewWidth + ")====", "" + dX);
                }
                //如果dX大于菜单项宽度
                else if (Math.abs(dX) >= viewWidth) {
                    viewHolder.itemView.scrollTo(viewWidth, 0);
                    Logger.i("=====dX到了(" + viewWidth + ")====", "" + dX);

                }

        }else {
            //拖拽状态下不做改变，需要调用父类的方法
            Logger.i("=====recyclerView====","到这了");
            super.onChildDraw(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive);
        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Logger.i("=====onSwiped====","到这了");
    }
}
