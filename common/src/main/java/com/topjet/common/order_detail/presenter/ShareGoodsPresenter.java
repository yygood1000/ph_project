package com.topjet.common.order_detail.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.extra.UserIdExtra;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.common.order_detail.modle.response.ShareGoodsListResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.view.ShareImageProvider;
import com.topjet.common.order_detail.view.activity.ShareGoodsView;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;

import com.topjet.common.order_detail.modle.response.ShareGoodsResponse;


import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/10/24
 * describe: 分享货源列表
 */

public class ShareGoodsPresenter extends BaseApiPresenter<ShareGoodsView<MyOrderListItem>, OrderDetailCommand> {

    public ShareGoodsPresenter(ShareGoodsView<MyOrderListItem> mView, Context mContext, OrderDetailCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    private String userId;

    @Override
    public void onCreate() {
        super.onCreate();
        if(CMemoryData.isDriver()) {
            UserIdExtra extra = (UserIdExtra) mActivity.getIntentExtra(UserIdExtra.getExtraName());
            if (extra != null) {
                userId = extra.getId();
            }
        }else {
            userId = CMemoryData.getUserId();
        }
    }

    /**
     * 分享货源D list
     *
     * @param page
     */
    public void goodsListByShare(int page) {
        if (StringUtils.isEmpty(userId)) {
            Toaster.showLongToast("用户id为空");
            return;
        }
        ShareGoodsParams params = new ShareGoodsParams();
        params.setOwner_id(userId);
        params.setPage(page + "");
        mApiCommand.goodsListByShare(params, new ObserverOnNextListener<ShareGoodsListResponse>() {
            @Override
            public void onNext(ShareGoodsListResponse shareGoodsListResponse) {
                if (shareGoodsListResponse != null) {
                    mView.loadSuccess(shareGoodsListResponse.getShare_goods_list_by_owner_ids());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }


    /**
     * 获取分享内容
     *
     * @param ids 分享的货源id
     */
    public void shareGoodsList(List<String> ids) {
        ShareGoodsParams params = new ShareGoodsParams();
        params.setOwner_id(userId);
        params.setGoods_ids(ids);

        mApiCommand.shareGoodsList(params, new ObserverOnResultListener<ShareGoodsResponse>() {
            @Override
            public void onResult(ShareGoodsResponse shareGoodsResponse) {
                if (shareGoodsResponse != null) {
                    // 根据获取的信息 设置分享图片信息
                    String path = new ShareImageProvider(mActivity).getShareImage(shareGoodsResponse);
                    if (StringUtils.isEmpty(path)) {
                        Toaster.showLongToast("分享失败");
                        return;
                    }
                    mView.shareImage(path);
                }
            }
        });
    }


    /**
     * 记录分享状态
     *
     * @param ids
     * @param flag
     */
    public void recordShareGoods(List<String> ids, String flag) {
        ShareGoodsParams params = new ShareGoodsParams();
        params.setShare_status(flag);
        params.setGoods_ids(ids);
        mApiCommand.recordShareGoods(params, new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {

            }
        });
    }
}
