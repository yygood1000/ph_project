package com.topjet.common.base;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.modle.extra.RouteExtra;
import com.topjet.common.common.modle.extra.SwitchExtra;
import com.topjet.common.common.modle.extra.TabIndex;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.contact.model.InviteTruckResponse;
import com.topjet.common.db.bean.IMUserBean;
import com.topjet.common.im.model.response.HistoryMessageResponse;
import com.topjet.common.order_detail.modle.response.GoodsInfoResponse;
import com.topjet.common.order_detail.modle.response.OrderInfoResponse;

/**
 * creator: zhulunjun
 * time:    2017/10/18
 * describe: 库需要调用上层api时使用这个
 * 例如，common 需要调用 shipper 中的一个类里面的方法
 * common需要跳转到一个 shipper 的页面
 */

public class CommonProvider {

    private static CommonProvider instance = new CommonProvider();

    public static CommonProvider getInstance() {
        return instance;
    }

    private FamiliarCarProvider familiarCarProvider; // 熟车api调用
    private JumpDriverProvider jumpDriverProvider; // 跳转司机版页面调用
    private JumpShipperProvider jumpShipperProvider; // 跳转货主版页面调用
    private JumpChatPageProvider jumpChatPageProvider; // 跳转聊天界面
    private ListenerOrderProvider listenerOrderProvider;// 司机版听单设置。在退出登录时调用
    private HistoryMessageProvider historyMessageProvider; // 保存历史聊天消息
    private UserInfoProvider userInfoProvider; // 保存用户消息


    public UserInfoProvider getUserInfoProvider() {
        return userInfoProvider;
    }

    public void setUserInfoProvider(UserInfoProvider userInfoProvider) {
        this.userInfoProvider = userInfoProvider;
    }

    public HistoryMessageProvider getHistoryMessageProvider() {
        return historyMessageProvider;
    }

    public void setHistoryMessageProvider(HistoryMessageProvider historyMessageProvider) {
        this.historyMessageProvider = historyMessageProvider;
    }

    public void setFamiliarCarProvider(FamiliarCarProvider familiarCarProvider) {
        this.familiarCarProvider = familiarCarProvider;
    }

    public void setJumpDriverProvider(JumpDriverProvider jumpDriverProvider) {
        this.jumpDriverProvider = jumpDriverProvider;
    }

    public void setJumpShipperProvider(JumpShipperProvider jumpShipperProvider) {
        this.jumpShipperProvider = jumpShipperProvider;
    }

    public void setListenerOrderProvider(ListenerOrderProvider listenerOrderProvider) {
        this.listenerOrderProvider = listenerOrderProvider;
    }

    public ListenerOrderProvider getListenerOrderProvider() {
        return listenerOrderProvider;
    }

    public FamiliarCarProvider getFamiliarCarProvider() {
        return familiarCarProvider;
    }

    public JumpDriverProvider getJumpDriverProvider() {
        return jumpDriverProvider;
    }

    public JumpShipperProvider getJumpShipperProvider() {
        return jumpShipperProvider;
    }

    public JumpChatPageProvider getJumpChatPageProvider() {
        return jumpChatPageProvider;
    }

    public void setJumpChatPageProvider(JumpChatPageProvider jumpChatPageProvider) {
        this.jumpChatPageProvider = jumpChatPageProvider;
    }

    public interface UserInfoProvider {
        /**
         * 保存用户信息
         *
         * @param userBean
         */
        void saveUserInfo(IMUserBean userBean);
    }

    public interface HistoryMessageProvider {
        /**
         * 保存历史消息和用户信息
         */
        void saveHistoryMessage(HistoryMessageResponse response);
    }

    public interface FamiliarCarProvider {
        /**
         * 邀请熟车
         */
        void inviteTruck(MvpActivity activity, String mobile, ObserverOnResultListener<InviteTruckResponse> listener);

        /**
         * 添加删除熟车
         */
        void addOrDeleteCar(MvpActivity activity, String carId, int flag, ObserverOnResultListener<Object> listener);
    }

    public interface JumpDriverProvider {

        /**
         * 跳转首页
         *
         * @param tab      底部模块
         * @param orderTab 订单状态 tab
         */
        void jumpMain(MvpActivity activity, int tab, int orderTab);

        /**
         * 跳转首页
         *
         * @param tab 底部模块
         */
        void jumpMain(MvpActivity activity, int tab);

        /**
         * 跳转车辆认证
         */
        void jumpCarAuthentication(MvpActivity activity, int type);

        /**
         * 跳转身份认证
         */
        void jumpIdentityAuthentication(MvpActivity activity);

        /**
         * 跳转实名认证
         */
        void jumpRealNameAuthentication(MvpActivity activity, int type);

        /**
         * 跳转用户信息，诚信查询结果
         */
        void jumpUserInfo(MvpActivity activity, UserInfo userInfo);

        /**
         * 跳转用户信息，根据id查询
         */
        void jumpUserInfo(MvpActivity activity, String mobile);

        /**
         * 跳转订单详情
         */
        void jumpOrderDetail(MvpActivity activity, String orderId);

        /**
         * 跳转订单详情,显示提货码弹窗
         */
        void jumpOrderDetail(MvpActivity activity, String orderId, String showPickUp);

        /**
         * 跳转货源详情
         */
        void jumpGoodsDetail(MvpActivity activity, String goodsId);

        /**
         * 跳转评价详情页面
         */
        void jumpComment(MvpActivity activity, String orderId, String orderVersion);

        /**
         * 跳转引导页
         */
        void jumpGuide(MvpActivity activity, SwitchExtra extra);

        /**
         * 跳转线路规划页面
         */
        void jumpRoute(MvpActivity activity, RouteExtra extra);

        /**
         * 跳转设置字体大小页
         */
        void jumpChangeSize(MvpActivity activity);

    }

    public interface ListenerOrderProvider {
        /**
         * 关闭听单悬浮窗
         */
        void stopFloatViewService(Context context);

        /**
         * 关闭听单服务
         */
        void stopListenOrderService(Context context);

        /**
         * 关闭定位服务
         */
        void stopLocationService(Context context);
    }

    public interface JumpShipperProvider {
        /**
         * 跳转首页
         *
         * @param tab      底部模块
         * @param orderTab 订单状态 tab
         */
        void jumpMain(MvpActivity activity, int tab, int orderTab);

        /**
         * @param tab        底部模块
         * @param orderTab   我都订单 历史订单
         * @param orderState 订单状态 tab
         */
        void jumpMain(MvpActivity activity, int tab, int orderTab, int orderState);

        /**
         * 跳转首页
         *
         * @param tab 底部模块
         */
        void jumpMain(MvpActivity activity, int tab);

        /**
         * 我要发货
         */
        void jumpDeliverGoods(MvpActivity activity);

        /**
         * 修改定向订单
         */
        void jumpToDeliverGoodsForEditAssigend(MvpActivity activity);

        /**
         * 修改非定向订单
         */
        void jumpToDeliverGoodsForEdit(MvpActivity activity, String goodsId);

        /**
         * 跳转身份认证
         */
        void jumpIdentityAuthentication(MvpActivity activity);

        /**
         * 跳转实名认证
         */
        void jumpRealNameAuthentication(MvpActivity activity, int type);

        /**
         * 跳转用户信息，诚信查询结果
         */
        void jumpUserInfo(MvpActivity activity, UserInfo userInfo);

        /**
         * 跳转用户信息，根据id查询
         */
        void jumpUserInfo(MvpActivity activity, String mobile);

        /**
         * 跳转订单详情
         */
        void jumpOrderDetail(MvpActivity activity, String orderId);

        /**
         * 跳转货源详情
         */
        void jumpGoodsDetail(MvpActivity activity, String goodsId);

        /**
         * 跳转评价详情页面
         */
        void jumpComment(MvpActivity activity, String orderId, String orderVersion);

        /**
         * 查看报价
         */
        void jumpShowOffer(MvpActivity activity, String goodsId, String goodsVersion, String payStyle);

        /**
         * 查看报价
         */
        void jumpRefindTruck(MvpActivity activity, String departCityId, String destinationCityId, String goodsId);

        /**
         * 跳转引导页
         */
        void jumpGuide(MvpActivity activity, SwitchExtra extra);
    }

    public interface JumpChatPageProvider {
        /**
         * 跳转聊天界面, 带订单消息
         */
        void jumpChatPage(Context context, IMUserBean user, OrderInfoResponse orderInfo);

        /**
         * 跳转聊天界面, 带货源消息
         */
        void jumpChatPage(Context context, IMUserBean user, GoodsInfoResponse orderInfo);

        /**
         * 跳转聊天界面, 带用户信息
         */
        void jumpChatPage(Context context, IMUserBean user);

        /**
         * 跳转聊天界面, 带用户id
         */
        void jumpChatPage(Context context, String username);

        /**
         * 跳转黑名单
         */
        void jumpBlackList(MvpActivity activity);
    }

    /**
     * 去主页
     */
    public void goMain(MvpActivity activity) {
        if (CMemoryData.isDriver()) {
            getJumpDriverProvider().jumpMain(activity, TabIndex.HOME_PAGE);
        } else {
            getJumpShipperProvider().jumpMain(activity, TabIndex.HOME_PAGE);
        }
    }

    /**
     * 订单详情
     */
    public void goOrderDetail(MvpActivity activity, String id) {
        if (CMemoryData.isDriver()) {
            getJumpDriverProvider().jumpOrderDetail(activity, id);
        } else {
            getJumpShipperProvider().jumpOrderDetail(activity, id);
        }
    }

    /**
     * 货源详情
     */
    public void goGoodsDetail(MvpActivity activity, String id) {
        if (CMemoryData.isDriver()) {
            getJumpDriverProvider().jumpGoodsDetail(activity, id);
        } else {
            getJumpShipperProvider().jumpGoodsDetail(activity, id);
        }
    }
}
