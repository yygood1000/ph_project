package com.topjet.common.order_detail.view.activity;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.order_detail.modle.params.ShareGoodsParams;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;
import com.topjet.common.base.view.activity.IView;
import com.topjet.common.order_detail.modle.response.ShareGoodsResponse;
import com.topjet.common.order_detail.view.ShareImageProvider;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/4
 * describe:订单详情基类的，接口
 */

public interface OrderDetailBaseView extends IView {

    /**
     * 货源详情
     * @param data
     */
    void showGoodsInfo(GoodsInfoResponse data);

    /**
     * 订单详情
     * @param data
     */
    void showOrderInfo(OrderInfoResponse data);

    /**
     * 分享货源
     * @param path
     */
    void shareImage(String path);

    /**
     * 显示掉钱动画
     */
    void showGif();

}
