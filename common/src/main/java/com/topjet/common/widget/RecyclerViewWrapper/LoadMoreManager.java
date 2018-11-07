package com.topjet.common.widget.RecyclerViewWrapper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.topjet.common.utils.Logger;

/**
 * creator: zhulunjun
 * time:    2017/9/29
 * describe: recyclerview 添加加载更多的监听
 */

public class LoadMoreManager {

    private RecyclerView recyclerView;
    private OnLoadMore onLoadMore;
    LinearLayoutManager linearLayoutManager;

    public LoadMoreManager(RecyclerView recyclerView, OnLoadMore onLoadMore) {
        this.recyclerView = recyclerView;
        this.onLoadMore = onLoadMore;
        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

    }

    int lastItemCount;
    public void setScrollListener(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    int itemCount = linearLayoutManager.getItemCount();
                    int lastPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    Logger.d("刷新成功 "+itemCount, lastPosition);
                    if (itemCount != lastItemCount
                            && lastPosition == itemCount - 1
                            && onLoadMore != null
                            && lastPosition != 1) { // 如果没有数据，不调用
                        lastItemCount = itemCount;
                        onLoadMore.loadMore();
                    }
                }
            }
        });
    }

    public interface OnLoadMore{
        void loadMore();
    }
}
