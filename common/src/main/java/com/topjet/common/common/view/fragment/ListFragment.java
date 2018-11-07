package com.topjet.common.common.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.base.view.fragment.BaseMvpFragment;
import com.topjet.common.common.presenter.ListFragmentPresenter;
import com.topjet.common.config.CConstants;
import com.topjet.common.order_detail.modle.bean.DriverInfo;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.view.adapter.DriverInfoAdapter;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.widget.RecyclerViewWrapper.LoadMoreManager;
import com.topjet.common.widget.SwipeRefreshLayout.RefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * creator: zhulunjun
 * time:    2017/9/8
 * describe: 查看过的列表，拨打过电话的列表
 */

public class ListFragment extends BaseMvpFragment<ListFragmentPresenter> implements ListFragmentView {

    @BindView(R2.id.rv_data)
    RecyclerView rvData;
    @BindView(R2.id.view_margin)
    View viewMargin;


    private DriverInfoAdapter adapter;
    private String emptyStr = "";
    private GoodsInfoResponse mGoodsInfo;

    public ListFragment() {
        this.adapter = new DriverInfoAdapter();
    }

    public void setGoodsInfo(GoodsInfoResponse mGoodsInfo) {
        this.mGoodsInfo = mGoodsInfo;
    }

    public void setEmptyStr(String emptyStr) {
        this.emptyStr = emptyStr;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ListFragmentPresenter(this, getActivity(), this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(final View v) {
        rvData.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvData.setAdapter(adapter);
        initFooterView();
        adapter.addFooterView(rvLoading);

        adapter.setDriverInfoClick(new DriverInfoAdapter.DriverInfoClick() {
            @Override
            public void callClick(View view, DriverInfo item) {
                new CallPhoneUtils().showCallDialogWithAdvNotUpload((MvpActivity) getActivity(), v,
                        item.getDriver_name(), item.getDriver_mobile(), 2);
            }

            @Override
            public void messageClick(View view, DriverInfo item) {
                if (mGoodsInfo != null) {
                    CommonProvider.getInstance().getJumpChatPageProvider()
                            .jumpChatPage(getActivity(), item.getIMUserInfo(item), mGoodsInfo);
                }

            }
        });

    }

    /**
     * 初始化底部控件
     */
    RefreshView rvLoading;

    private void initFooterView() {
        rvLoading = new RefreshView(getContext(), RefreshView.LOAD_MORE);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ScreenUtils.getWindowsWidth(),
                ScreenUtils.dp2px(mContext, 50));
        layoutParams.gravity = Gravity.CENTER;
        rvLoading.setLayoutParams(layoutParams);

        new LoadMoreManager(rvData, new LoadMoreManager.OnLoadMore() {
            @Override
            public void loadMore() {
                rvLoading.startLoading();
                if (loadMore != null) {
                    loadMore.loadMore();
                }
            }
        }).setScrollListener();
    }

    private LoadMoreManager.OnLoadMore loadMore;

    public void setLoadMore(LoadMoreManager.OnLoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void loadMoreEnd() {
        rvLoading.stopLoading();
        rvLoading.showLoadEnd();
    }

    public RecyclerView getRvData() {
        return rvData;
    }

    /**
     * 清空数据
     */
    public void clearDate() {
        adapter.replaceData(new ArrayList<DriverInfo>());
    }

    @Override
    protected void initData() {

    }

    public void addData(List<DriverInfo> datas, int page) {

        if (ListUtils.isEmpty(datas)) {

            if (ListUtils.isEmpty(adapter.getData())) { // 空
                viewMargin.setVisibility(View.VISIBLE);
                rvLoading.showEmpty(emptyStr);
            } else { // 加载结束
                viewMargin.setVisibility(View.GONE);
                loadMoreEnd();
            }

        } else { // 加载成功
            viewMargin.setVisibility(View.GONE);
            if (datas.size() < CConstants.PAGE_SIZE) {
                loadMoreEnd();
            } else {
                page++;
            }
            adapter.setNewData(datas);
        }
    }


}
