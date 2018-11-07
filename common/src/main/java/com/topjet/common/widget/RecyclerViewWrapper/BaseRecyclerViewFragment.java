package com.topjet.common.widget.RecyclerViewWrapper;

import android.support.annotation.NonNull;
import android.view.View;

import com.topjet.common.base.presenter.BaseFragmentPresenter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.config.CConstants;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * creator: zhulunjun
 * time:    2017/9/26
 * describe: 需要上拉更多和下拉刷新页面的封装
 * fragment 继承这个页面
 */

public abstract class BaseRecyclerViewFragment<T extends BaseFragmentPresenter, D> extends BaseMvpFragment<T>
        implements IListView<D>, RecyclerViewWrapper.OnRequestListener {

    public RecyclerViewWrapper recyclerViewWrapper;

    public List<D> mData = new ArrayList<>();
    public int page = 1;// 页数

    /**
     * 设置加载结束并且设置数据结束回调
     */
    public void setFinishListener(RecyclerViewWrapper.OnSetDataFinishListener finishListener) {
        recyclerViewWrapper.finishListener = finishListener;
    }

    public void errorClick() {
        refresh();
    }

    public void emptyClick() {
        refresh();
    }

    public abstract RecyclerViewWrapper getRecyclerView();

    /**
     * 空数据显示文本
     *
     * @param emptyText
     */
    public void setEmptyText(String emptyText) {
        if (StringUtils.isNotBlank(emptyText)) {
            recyclerViewWrapper.getTvEmpty().setText(emptyText);
        }
    }

    /**
     * 空数据mini显示文本
     *
     * @param emptyMiniText
     */
    public void setEmptyMiniText(String emptyMiniText) {
        if (StringUtils.isNotBlank(emptyMiniText)) {
            recyclerViewWrapper.getTvEmptyMini().setText(emptyMiniText);
        }
    }

    /**
     * 空数据按钮显示文本
     *
     * @param emptyBtnText
     */
    public void setEmptyBtnText(String emptyBtnText) {
        if (StringUtils.isNotBlank(emptyBtnText)) {
            recyclerViewWrapper.getTvBtnEmpty().setText(emptyBtnText);
        }
    }

    /**
     * 加载失败显示文本
     *
     * @param errorText
     */
    public void setErrorText(String errorText) {
        if (StringUtils.isNotBlank(errorText)) {
            recyclerViewWrapper.getTvError().setText(errorText);
        }
    }

    /**
     * 加载失败按钮显示文本
     *
     * @param errorBtnText
     */
    public void setErrorBtnText(String errorBtnText) {
        if (StringUtils.isNotBlank(errorBtnText)) {
            recyclerViewWrapper.getTvBtnError().setText(errorBtnText);
        }
    }

    /**
     * 设置错误图片
     *
     * @param res
     */
    public void setErrorImage(int res) {
        if (res != -1) {
            recyclerViewWrapper.getIvError().setImageResource(res);
        }
    }

    /**
     * 设置空图片
     *
     * @param res
     */
    public void setEmptyImage(int res) {
        if (res != -1) {
            recyclerViewWrapper.getIvEmpty().setImageResource(res);
        }
    }

    public void setRecyclerViewWrapper(RecyclerViewWrapper recyclerViewWrapper) {
        this.recyclerViewWrapper = recyclerViewWrapper;
    }

    public void initView() {
        recyclerViewWrapper = getRecyclerView();
        recyclerViewWrapper.setAdapter(getAdapter());
        recyclerViewWrapper.setData(mData);
        recyclerViewWrapper.setOnRequestListener(this);
        setEmptyText("没有数据");
        setErrorText("加载失败了");
        setEmptyBtnText("重新加载");
        setErrorBtnText("重新加载");
        recyclerViewWrapper.setErrorViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorClick();
            }
        });
        recyclerViewWrapper.setEmptyViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyClick();
            }
        });

    }

    /**
     * 主动调用刷新
     */
    @Override
    public void refresh() {
        if (recyclerViewWrapper.getState() != LoadingStateDelegate.STATE.SUCCEED) {
            recyclerViewWrapper.clearUI();
        }
        recyclerViewWrapper.showRefreshing();
    }

    @Override
    public void onRefresh() {
        page = 1;
        loadData();
        Logger.d(TAG, "刷新");
    }

    @Override
    public void onLoadingMore() {
        page++;
        loadData();
        Logger.d(TAG, "加载更多");
    }

    @Override
    public void loadFail(String errorMessage) {
        Logger.d(TAG, "加载失败");
        setErrorText(errorMessage);
        recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.ERROR);
    }

    /**
     * 加载成功
     *
     * @param data
     * @param <>
     */
    @Override
    public void loadSuccess(final List<D> data) {
        recyclerViewWrapper.refreshSuccess();
        Logger.d(TAG, "加载成功");
        Observable.timer(300, TimeUnit.MICROSECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (null != recyclerViewWrapper) {
                            checkData(data);
                        }
                    }
                });

    }

    /**
     * 是否自己处理数据为空 的 处理
     *
     * @return true 自己处理 false 默认处理
     */
    public boolean onEmpty() {
        return false;
    }

    /**
     * 检查数据
     * 是否为空
     */
    private void checkData(List<D> data) {
        if (page == CConstants.PAGE_START) { // 下拉刷新
            if (ListUtils.isEmpty(data)) {
                if (!onEmpty()) {
                    recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.EMPTY);
                } else {
                    replaceData(new ArrayList<D>());
                    recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                }
            } else {
                recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                replaceData(data);
                if (data.size() < CConstants.PAGE_SIZE) {
                    recyclerViewWrapper.setLoadEnd();
                }
            }
        } else { //加载更多
            if (ListUtils.isEmpty(data)) {
                // 所有数据接在完成，显示没有更多数据脚布局
                // 修改footView
                recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                recyclerViewWrapper.setLoadEnd();
            } else {
                // 加载结束
                recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                addData(data);
            }

        }
    }

    /**
     * 添加数据
     */
    public void addData(List<D> datas) {
        recyclerViewWrapper.getAdapter().addData(datas);
        if (recyclerViewWrapper.finishListener != null) {
            recyclerViewWrapper.finishListener.onFinish();
        }
        mData.addAll(datas);
    }

    /**
     * 替换数据
     */
    public void replaceData(List<D> datas) {
        recyclerViewWrapper.getAdapter().setNewData(datas);
        if (recyclerViewWrapper.finishListener != null) {
            recyclerViewWrapper.finishListener.onFinish();
        }
        mData.clear();
        mData.addAll(datas);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (recyclerViewWrapper != null) {
            recyclerViewWrapper.removeAllViews();
            recyclerViewWrapper = null;
        }
    }

    /**
     * 刷新列表
     */
    public void notifyDataSetChanged() {
        recyclerViewWrapper.getAdapter().notifyDataSetChanged();
    }

    /**
     * 获取列表展示数据
     */
    public ArrayList getData() {
        return (ArrayList) recyclerViewWrapper.getAdapter().getData();
    }
}
