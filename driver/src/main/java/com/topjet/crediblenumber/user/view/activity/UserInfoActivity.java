package com.topjet.crediblenumber.user.view.activity;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.modle.bean.UserInfoGoods;
import com.topjet.common.common.view.activity.UserInfoBaseActivity;
import com.topjet.common.order_detail.modle.extra.UserIdExtra;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.RecyclerViewWrapper.LoadingStateDelegate;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;
import com.topjet.crediblenumber.order.view.activity.OrderDetailActivity;
import com.topjet.crediblenumber.order.view.activity.ShareGoodsActivity;
import com.topjet.crediblenumber.user.view.adapter.ShipperGoodsListAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 司机查货主 诚信查询结果
 */

public class UserInfoActivity extends UserInfoBaseActivity<UserInfoGoods> {
    private ShipperGoodsListAdapter mGoodsListAdapter;

    @Override
    protected void initView() {
        super.initView();
        // 设置列表点击事件
        mGoodsListAdapter.setGoodsListClick(itemClickListener);
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        mGoodsListAdapter = new ShipperGoodsListAdapter();
        return mGoodsListAdapter;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        getMyTitleBar()
                .setMode(MyTitleBar.Mode.BACK_TITLE_RTEXT)
                .setRightText("分享货源");
    }

    @Override
    protected void onClickRightText() {
        super.onClickRightText();
        if(mUserInfo != null) {
            turnToActivity(ShareGoodsActivity.class, new UserIdExtra(mUserInfo.getUser_id()));
        }
    }

    @Override
    public void loadData() {
        if (mUserInfo != null &&
                StringUtils.isNotBlank(mUserInfo.getUser_id()) &&
                mUserInfo.getUser_type().equals(UserInfo.SHIPPER)) {
            mPresenter.queryShipperInfoGoodsList(mUserInfo.getUser_id(), page + "");
        } else {
            // 当司机查询到司机身份时，不显示车辆信息
            noCheckDateToSuccess();
            // 不显示分享按钮
            getMyTitleBar()
                    .setMode(MyTitleBar.Mode.BACK_TITLE);
        }
    }

    ShipperGoodsListAdapter.GoodsListClick itemClickListener = new ShipperGoodsListAdapter.GoodsListClick() {
        @Override
        public void contentClick(UserInfoGoods item) {
            OrderDetailActivity.toGoodsDetail(UserInfoActivity.this, item.getGoods_id());
        }

        @Override
        public void ordersClick(final View view, final UserInfoGoods item) {
            // 接单
            // 跳弹窗，填报价
            CheckUserStatusUtils.isRealNameAndTruckApproved(UserInfoActivity.this,
                    new CheckUserStatusUtils.OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            new BidOrAlterDialog(UserInfoActivity.this).showBidView(item.getGoods_id(), item
                                            .getPre_goods_version(),
                                    new BidOrAlterDialog.OnPayForDepositResultListener() {
                                        @Override
                                        public void onSucceed() {
                                            refresh(); // 刷新
                                        }
                                    });
                        }
                    });
        }

        @Override
        public void undoQuoteClick(View view, UserInfoGoods item) {
            // 撤销报价
            mPresenter.cancelOffer(item.getPre_goods_id());
        }

        @Override
        public void updateQuoteClick(View view, UserInfoGoods item) {
            // 修改报价
            new BidOrAlterDialog(UserInfoActivity.this).showAlterView(
                    item.getGoods_id(),
                    item.getTransport_fee() + "",
                    item.getDeposit_fee(),
                    item.getPre_goods_id(),
                    item.getPre_goods_version(),
                    new BidOrAlterDialog.OnPayForDepositResultListener() {
                        @Override
                        public void onSucceed() {
                            refresh(); // 刷新
                        }
                    });
        }
    };
}
