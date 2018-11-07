package com.topjet.common.widget.RecyclerViewWrapper;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.widget.SwipeRefreshLayout.DragDistanceConverterEg;
import com.topjet.common.widget.SwipeRefreshLayout.RefreshView;
import com.topjet.common.widget.SwipeRefreshLayout.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by sunfusheng on 2017/1/15.
 * <p>
 * 1. 下拉刷新
 * 2. 上拉更多
 * 3. 加载空数据，显示空布局
 * 4. 加载失败，显示错误布局
 */
public class RecyclerViewWrapper extends FrameLayout implements NestedScrollingChild {

    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.error_stub)
    ViewStub errorStub;
    @BindView(R2.id.empty_stub)
    ViewStub emptyStub;
    RefreshView rvLoading;

    Context mContext;

    // 错误
    LinearLayout llError;
    ImageView ivError;
    TextView tvError;
    TextView tvBtnError;

    // 空
    LinearLayout llEmpty;
    ImageView ivEmpty;
    TextView tvEmpty;
    TextView tvEmptyMini;
    TextView tvBtnEmpty;


    // 正在加载
    public boolean isLoading = false;

    // 是否允许加载更多
    boolean isLoadMore = true;

    // 是否显示“没有更多数据”的文本
    boolean isShowMoreText = true;

    private OnClickListener errorViewClickListener;
    private OnClickListener emptyViewClickListener;
    private OnRequestListener onRequestListener;
    private OnScrollListener onScrollListener;
    public OnSetDataFinishListener finishListener;

    private LoadingStateDelegate loadingStateDelegate;

    private MyLinearLayoutManager linearLayoutManager;
    private BaseQuickAdapter mAdapter;
    private int lastItemCount;
    private int firstVisibleItemPosition = RecyclerView.NO_POSITION;
    private int lastVisibleItemPosition = RecyclerView.NO_POSITION;

    public RecyclerViewWrapper(@NonNull Context context) {
        this(context, null);
    }

    public RecyclerViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewWrapper(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
        initListener();

    }

    private View mView;

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.layout_recyclerview, this);
        ButterKnife.bind(this, mView);

        initErrorEmpty();
        loadingStateDelegate = new LoadingStateDelegate(swipeRefreshLayout, null, llError, llEmpty);

        swipeRefreshLayout.setDragDistanceConverter(new DragDistanceConverterEg());
        linearLayoutManager = new MyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        initFooterView();
        initBtnBg();
    }

    /**
     * 初始化底部控件
     */
    private void initFooterView() {
        rvLoading = new RefreshView(getContext(), RefreshView.LOAD_MORE);
        LayoutParams layoutParams = new LayoutParams(ScreenUtils.getWindowsWidth(), ScreenUtils.dp2px(mContext, 50));
        layoutParams.gravity = Gravity.CENTER;
        rvLoading.setLayoutParams(layoutParams);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseQuickAdapter adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
            mAdapter = adapter;
//            if(mAdapter.getFooterLayout() == null) {
//                mAdapter.addFooterView(rvLoading);
//            }
        }
    }

    /**
     * 刷新成功
     */
    public void refreshSuccess() {
        swipeRefreshLayout.refreshViewSuccess();
    }


    private void initErrorEmpty() {
        errorStub.inflate();
        emptyStub.inflate();
        llEmpty = ButterKnife.findById(mView, R.id.ll_empty);
        ivError = ButterKnife.findById(mView, R.id.iv_error);
        llError = ButterKnife.findById(mView, R.id.ll_error);
        tvError = ButterKnife.findById(mView, R.id.tv_error);
        tvBtnError = ButterKnife.findById(mView, R.id.tv_btn_error);
        ivEmpty = ButterKnife.findById(mView, R.id.iv_empty);
        tvEmpty = ButterKnife.findById(mView, R.id.tv_empty);
        tvEmptyMini = ButterKnife.findById(mView, R.id.tv_empty_mini);
        tvBtnEmpty = ButterKnife.findById(mView, R.id.tv_btn_empty);
    }

    /**
     * 初始化按钮背景
     */
    private void initBtnBg() {
        if (tvBtnEmpty != null) {
            tvBtnEmpty.setBackgroundResource(CMemoryData.isDriver() ? R.drawable.shape_bg_btn_border_blue : R.drawable
                    .shape_bg_btn_border_green);
            tvBtnEmpty.setTextColor(CMemoryData.isDriver() ? getResources().getColor(R.color.v3_common_blue) :
                    getResources().getColor(R.color.v3_common_green));
        }
        if (tvBtnError != null) {
            tvBtnError.setBackgroundResource(CMemoryData.isDriver() ? R.drawable.shape_bg_btn_border_blue : R.drawable
                    .shape_bg_btn_border_green);
            tvBtnError.setTextColor(CMemoryData.isDriver() ? getResources().getColor(R.color.v3_common_blue) :
                    getResources().getColor(R.color.v3_common_green));
        }

    }

    public TextView getTvError() {
        return tvError;
    }

    public TextView getTvBtnError() {
        return tvBtnError;
    }

    public TextView getTvEmpty() {
        return tvEmpty;
    }

    public TextView getTvEmptyMini() {
        return tvEmptyMini;
    }

    public TextView getTvBtnEmpty() {
        return tvBtnEmpty;
    }

    public ImageView getIvError() {
        return ivError;
    }

    public ImageView getIvEmpty() {
        return ivEmpty;
    }


    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRequestListener != null) {
                    lastItemCount = 0;
                    onRequestListener.onRefresh();
                    isLoading = true;
                    isLoadMore = true;
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    int itemCount = linearLayoutManager.getItemCount();
                    int lastPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    Logger.d("刷新成功 " + itemCount, lastPosition + " " + isLoadMore);
                    if (itemCount != lastItemCount
                            && lastPosition == itemCount - 1
                            && onRequestListener != null
                            && lastPosition != 1
                            && isLoadMore
                            && !isLoading) { // 有数据，并且没有加载结束，没有正在加载
                        isLoading = true;
                        lastItemCount = itemCount;
                        onRequestListener.onLoadingMore();
                        addFooterView();
                        rvLoading.startLoading();

                    }
                }

                if (onScrollListener != null) {
                    onScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    /**
     * 情况布局，数据，加载更多布局，空显示，错误显示等
     */
    public void clearUI() {
        mAdapter.replaceData(new ArrayList());
        removeFooterView();
        loadingStateDelegate.setViewState(LoadingStateDelegate.STATE.SUCCEED);
    }

    /**
     * 删除加载视图
     */
    public void removeFooterView() {
        if (mAdapter.getFooterLayout() != null && mAdapter.getFooterLayout().getChildCount() > 0) {
            mAdapter.removeFooterView(rvLoading);
        }
    }

    public void addFooterView() {
        if (mAdapter.getFooterLayout() == null || mAdapter.getFooterLayout().getChildCount() == 0) {
            mAdapter.addFooterView(rvLoading);
        }
    }

    /**
     * 设置加载完成的显示
     */
    public void setLoadEnd() {
        isLoadMore = false;// 不加载更多
        if (rvLoading != null) {
            if (isShowMoreText) {
                rvLoading.showLoadEnd();
            } else {
                rvLoading.setVisibility(GONE);
            }
        }
    }

    /**
     * 不显示“没有更多数据”的文本显示
     */
    public void setShowMoreText(boolean showMoreText) {
        isShowMoreText = showMoreText;
    }

    public void setLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    private @LoadingStateDelegate.STATE
    int state;

    public void setLoadingState(@LoadingStateDelegate.STATE int state) {
        this.state = state;
        isLoading = false;
        addFooterView();
        if (rvLoading.getVisibility() == VISIBLE) {
            rvLoading.stopLoading();
        }
        swipeRefreshLayout.setRefreshing(false);
        if (state == LoadingStateDelegate.STATE.ERROR) {
            if (recyclerView.getAdapter() != null && recyclerView.getAdapter().getItemCount() > 1) {
                loadingStateDelegate.setViewState(LoadingStateDelegate.STATE.SUCCEED);
            } else {
                loadingStateDelegate.setViewState(LoadingStateDelegate.STATE.ERROR);
                setErrorViewClickListener(errorViewClickListener);
            }
        } else if (state == LoadingStateDelegate.STATE.EMPTY) {
            loadingStateDelegate.setViewState(LoadingStateDelegate.STATE.EMPTY);
            setEmptyViewClickListener(emptyViewClickListener);
        } else {
            loadingStateDelegate.setViewState(state);
        }

    }

    public int getState() {
        return state;
    }

    public void setErrorViewClickListener(OnClickListener errorLayoutClickListener) {
        this.errorViewClickListener = errorLayoutClickListener;
        if (tvBtnError != null) {
            tvBtnError.setOnClickListener(errorLayoutClickListener);
        }
    }

    public void setEmptyViewClickListener(OnClickListener emptyViewClickListener) {
        this.emptyViewClickListener = emptyViewClickListener;
        if (tvBtnEmpty != null) {
            tvBtnEmpty.setOnClickListener(emptyViewClickListener);
        }
    }

    public void setEmptyView(int layoutId) {
        if (emptyStub != null && layoutId != -1) {
            emptyStub.setLayoutResource(layoutId);
        }
    }

    public void setErrorView(int layoutId) {
        if (errorStub != null && layoutId != -1) {
            errorStub.setLayoutResource(layoutId);
        }
    }

    public void setData(@Nullable List<?> items) {
        mAdapter.setNewData(items);

    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }


    public List<?> getData() {
        return mAdapter.getData();
    }

    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public BaseQuickAdapter getAdapter() {
        return mAdapter;
    }

    public int getFirstVisibleItemPosition() {
        return firstVisibleItemPosition;
    }

    public int getLastVisibleItemPosition() {
        return lastVisibleItemPosition;
    }

    public Object getFirstVisibleItem() {
        if (mAdapter.getItemCount() <= 0) return null;
        if (firstVisibleItemPosition == RecyclerView.NO_POSITION) return null;
        if (firstVisibleItemPosition >= mAdapter.getItemCount()) return null;
        return mAdapter.getData().get(firstVisibleItemPosition);
    }

    public Object getLastVisibleItem() {
        if (mAdapter.getItemCount() <= 0) return null;
        if (lastVisibleItemPosition == RecyclerView.NO_POSITION) return null;
        if (lastVisibleItemPosition >= mAdapter.getItemCount()) return null;
        return mAdapter.getData().get(lastVisibleItemPosition);
    }

    public void setOnRequestListener(OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 手动调用下拉刷新
     */
    public void showRefreshing() {
        swipeRefreshLayout.showRefreshing();
    }

    @Override
    public int computeHorizontalScrollRange() {
        return recyclerView.computeHorizontalScrollRange();
    }

    @Override
    public int computeHorizontalScrollOffset() {
        return recyclerView.computeHorizontalScrollOffset();
    }

    @Override
    public int computeHorizontalScrollExtent() {
        return recyclerView.computeHorizontalScrollExtent();
    }

    @Override
    public int computeVerticalScrollRange() {
        return recyclerView.computeVerticalScrollRange();
    }

    @Override
    public int computeVerticalScrollOffset() {
        return recyclerView.computeVerticalScrollOffset();
    }

    @Override
    public int computeVerticalScrollExtent() {
        return recyclerView.computeVerticalScrollExtent();
    }

    public interface OnRequestListener {
        void onRefresh();

        void onLoadingMore();
    }

    public interface OnScrollListener {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    /**
     * 单次数据加载完成并设置结束回调
     */
    public interface OnSetDataFinishListener {
        void onFinish();
    }
}
