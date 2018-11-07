package com.topjet.common.widget.RecyclerViewWrapper;

import android.view.View;

import com.topjet.common.R;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CConstants;
import com.topjet.common.utils.DelayUtils;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * creator: zhulunjun
 * time:    2017/9/26
 * describe: 需要上拉更多和下拉刷新页面的封装
 * activity 继承这个页面
 */

public abstract class BaseRecyclerViewActivity<T extends BasePresenter, D> extends MvpActivity<T> implements
        IListView<D>, RecyclerViewWrapper.OnRequestListener {

    public RecyclerViewWrapper recyclerViewWrapper;

    public List<D> mData = new ArrayList<>();
    public int page = 1;// 页数

    public boolean isHavePage = true; // 是否分页 默认分页
    public boolean isShowEmpty = false; // 在没有分页的模式下是否显示空布局 默认不显示

    /**
     * 设置加载结束并且设置数据结束回调
     */
    public void setFinishListener(RecyclerViewWrapper.OnSetDataFinishListener finishListener) {
        recyclerViewWrapper.finishListener = finishListener;
    }

    public void errorClick() {

    }

    public void emptyClick() {

    }

    public boolean isHavePage() {
        return isHavePage;
    }

    public void setHavePage(boolean havePage) {
        isHavePage = havePage;
    }

    public void setShowEmpty(boolean showEmpty) {
        isShowEmpty = showEmpty;
    }

    /**
     * 设置不分页，模式
     */
    public void setNoPageMode() {
        isHavePage = false;
        if (recyclerViewWrapper != null) {
            recyclerViewWrapper.removeFooterView();
            recyclerViewWrapper.setLoadMore(false);
        }
    }

    /**
     * 设置分页模式
     */
    public void setHavePageMode() {
        isHavePage = true;
        if (recyclerViewWrapper != null) {
            recyclerViewWrapper.addFooterView();
            recyclerViewWrapper.setLoadMore(true);
            recyclerViewWrapper.setShowMoreText(true);
        }
    }

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


    @Override
    protected void initView() {
        recyclerViewWrapper = ButterKnife.findById(this, R.id.recyclerViewWrapper);
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
        DelayUtils.delay(300, new DelayUtils.OnListener() {
            @Override
            public void onListener() {
                if (null != recyclerViewWrapper) {
                    checkData(data);
                }
            }
        });

    }

    /**
     * 不检查数据，直接加载成功
     */
    public void noCheckDateToSuccess(){
        DelayUtils.delay(300, new DelayUtils.OnListener() {
            @Override
            public void onListener() {
                if (null != recyclerViewWrapper) {
                    recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                    recyclerViewWrapper.setShowMoreText(false);
                    recyclerViewWrapper.setLoadEnd();
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
     *
     * @param data
     * @param <>
     */
    private void checkData(List<D> data) {
        if (!isHavePage) {
            if (data != null && data.size() > 0) {
                Logger.d("checkData", "有数据");
                replaceData(data);
                recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                recyclerViewWrapper.setLoadEnd();
            } else {
                if (!onEmpty() && isShowEmpty) { // 是否显示空布局
                    Logger.d("checkData", "空布局");
                    recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.EMPTY);
                } else {
                    replaceData(new ArrayList<D>()); // 清空数据
                    recyclerViewWrapper.setLoadingState(LoadingStateDelegate.STATE.SUCCEED);
                    recyclerViewWrapper.setShowMoreText(false);
                    recyclerViewWrapper.setLoadEnd();

                }
            }
            return;
        }
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
     *
     * @param datas
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
     *
     * @param datas
     */
    public void replaceData(List<D> datas) {
        recyclerViewWrapper.getAdapter().setNewData(datas);
        if (recyclerViewWrapper.finishListener != null) {
            recyclerViewWrapper.finishListener.onFinish();
        }
        mData.clear();
        mData.addAll(datas);
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
    public ArrayList<D> getData() {
        return (ArrayList<D>) recyclerViewWrapper.getAdapter().getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerViewWrapper != null) {
            recyclerViewWrapper.removeAllViews();
            recyclerViewWrapper = null;
        }
    }


}
