package com.topjet.common.order_detail.view.activity;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.order_detail.presenter.ShareGoodsPresenter;
import com.topjet.common.order_detail.view.dialog.ShareDialog;
import com.topjet.common.utils.ShareHelper;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.BaseRecyclerViewActivity;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * creator: zhulunjun
 * time:    2017/10/24
 * describe: 分享多个货源的列表
 * 从个人中心进入
 */

public abstract class ShareGoodsBaseActivity extends BaseRecyclerViewActivity<ShareGoodsPresenter, MyOrderListItem> implements ShareGoodsView<MyOrderListItem> {
    @BindView(R2.id.tv_btn_share)
    public TextView tvBtnShare;

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.CANCEL_TITLE)
                .setTitleText("你可以分享以下货源");
    }



    @Override
    protected void initView() {
        super.initView();
        setEmptyText("当前无可分享货源");
        recyclerViewWrapper.getTvBtnEmpty().setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        super.initData();
        refresh();
    }

    public abstract List<String> getCheckIds();
    @Override
    public void loadSuccess(List<MyOrderListItem> data) {
        if(!tvBtnShare.isShown()) {
            tvBtnShare.setVisibility(View.VISIBLE);
        }
        super.loadSuccess(data);
    }

    @Override
    public void loadData() {
        mPresenter.goodsListByShare(page);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ShareGoodsPresenter(this, this, new OrderDetailCommand(OrderDetailCommandApi.class, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_share_goods;
    }

    @OnClick(R2.id.tv_btn_share)
    public void share() {
        mPresenter.shareGoodsList(getCheckIds());
    }

    @Override
    public boolean onEmpty() {
        tvBtnShare.setVisibility(View.GONE);
        return super.onEmpty();
    }

    @Override
    public void emptyClick() {
        super.emptyClick();
        refresh();
    }

    /**
     * 分享图片
     * @param path
     */
    @Override
    public void shareImage(final String path) {
        new ShareDialog(this)
                .setShareClickListener(new ShareDialog.ShareClickListener() {
            @Override
            public void weChatClick() {
                ShareHelper.shareImage(ShareGoodsBaseActivity.this, Wechat.NAME, path, shareComplete);
            }

            @Override
            public void circleClick() {
                ShareHelper.shareImage(ShareGoodsBaseActivity.this, WechatMoments.NAME, path, shareComplete);
            }
        }).show();
    }

    // 分享的回调
    ShareHelper.OnShareComplete shareComplete =new ShareHelper.OnShareComplete() {
        @Override
        public void shareComple(String s) {
            mPresenter.recordShareGoods(getCheckIds(), ShareGoodsParams.SUCCESS);
        }

        @Override
        public void shareError() {
            mPresenter.recordShareGoods(getCheckIds(), ShareGoodsParams.FAIL);
        }
    };
}
