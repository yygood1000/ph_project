package com.topjet.common.common.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.modle.extra.SwitchExtra;
import com.topjet.common.common.view.activity.CommentActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/17
 * describe: 中转页面的跳转逻辑
 * 需要跳转的页面：
 * 1. 首页    -- 不需要处理
 * 2. 订单详情 -- param1   订单id     param2=1 是否弹出提货码弹窗
 * 3. 货源详情 -- param3   货源id
 * 4. 我的订单 -- param1=1 首页tab下标
 * 5. 评价详情 -- param1   订单id     param2 订单版本
 * 6. 听单设置 -- param1=5 首页tab下标
 * 7. 实名认证 -- param1=1 显示认证信息
 * 8. 车辆认证 -- param1=1 显示认证信息
 * 9. 身份认证 -- param1=1 显示认证信息
 * 10.头像设置 -- 不需要处理
 * 11.系统消息 -- 不需要处理
 * 12.跳转发货版/接货版 -- param2 switch_key  param3 手机号
 * 13.重新找车 -- param1=1 出发地城市id param2=1 目的地城市id param3=1 货源id
 * 14.查看报价 -- param1=1 货源id      param2=1 货源版本     param3=1 支付方式
 * <p>
 * 默认跳转首页
 */

public class TransferPresenter extends BasePresenter {
    public TransferPresenter(Context mContext) {
        super(mContext);
    }

    public static final String ACTION = "actionAndroid"; // 需要跳转的页面
    private static final String PARAM1 = "param1"; // 第一个参数
    private static final String PARAM2 = "param2"; // 第二个参数
    private static final String PARAM3 = "param3"; // 第二个参数

    private String action, param1, param2, param3; // 获取的数据

    /**
     * 主界面
     */
    private static final String MAIN = "MainActivity";
    /**
     * 订单详情，货源详情
     */
    private static final String ORDER_DETAIL = "OrderDetailActivity";
    /**
     * 评论列表，评论详情
     */
    private static final String COMMENT = "CommentActivity";
    /**
     * 身份认证，车辆，实名
     */
    private static final String AUTHENTICATION = "Authentication";
    /**
     * 查看报价
     */
    private static final String SHOW_OFFER = "ShowOfferActivity";
    /**
     * 找车
     */
    private static final String FIND_TRUCK = "FindTruckActivity";
    @Override
    public void onCreate() {
        super.onCreate();
        distributePage(mActivity.getIntent());
    }

    /**
     * 分发页面
     */
    public int distributePage(Intent intent) {
        Logger.d("外部跳转");
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            Uri uri = intent.getData();
            action = uri.getQueryParameter(ACTION);
            param1 = uri.getQueryParameter(PARAM1);
            param2 = uri.getQueryParameter(PARAM2);
            param3 = uri.getQueryParameter(PARAM3);
            Logger.d("外部跳转 " + action);
            if (StringUtils.isNotBlank(action)) {
                // 先处理跳转主页的uri, 当前在主页。如果有下标，则返回
                if (action.contains(MAIN)) {
                    if (StringUtils.isNotBlank(param1)) {
                        return StringUtils.getIntToString(param1);
                    } else if (StringUtils.isNotBlank(param2) && StringUtils.isNotBlank(param3)) {
                        // 跳转app
                        // 跳转引导页
                        jumpGuide(new SwitchExtra(param2, param3));
                        return -2; // 切换app这种情况不掉接口
                    }

                } else if (action.contains(ORDER_DETAIL)) {
                    // 订单详情
                    if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2)) {
                        // 主动弹窗提货码弹窗
                        CommonProvider.getInstance().getJumpDriverProvider().jumpOrderDetail(mActivity, param1, param2);
                    } else if (StringUtils.isNotBlank(param1)) {
                        // 不弹窗
                        // 订单详情
                        CommonProvider.getInstance().goOrderDetail(mActivity, param1);
                    }
                    if (StringUtils.isNotBlank(param3)) {
                        // 货源详情
                        CommonProvider.getInstance().goGoodsDetail(mActivity, param3);
                    }
                } else if (action.contains(COMMENT)) {
                    if (StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2)) {
                        // 评价详情
                        jumpComment(param1, param2);
                    } else {
                        // 自己的评价列表 
                        CommentActivity.turnToCommentListSelfActivity(mActivity);
                    }
                } else if (action.contains(AUTHENTICATION)) {
                    // 认证页面，包括 实名认证，身份认证，车辆认证
                    if (StringUtils.isNotBlank(param1)) {
                        jumpAction(action, new GoToAuthenticationExtra(GoToAuthenticationExtra.IN_TYPE_SHOW),
                                GoToAuthenticationExtra.getExtraName());
                    }
                } else if (action.contains(SHOW_OFFER)) {
                    // 查看报价
                    if(StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) && StringUtils.isNotBlank(param3)){
                        CommonProvider.getInstance().getJumpShipperProvider().jumpShowOffer(mActivity, param1, param2, param3);
                    }
                } else if (action.contains(FIND_TRUCK)) {
                    // 重新找车
                    if(StringUtils.isNotBlank(param1) && StringUtils.isNotBlank(param2) && StringUtils.isNotBlank(param3)){
                        CommonProvider.getInstance().getJumpShipperProvider().jumpRefindTruck(mActivity, param1, param2, param3);
                    }
                } else {
                    jumpAction(action);
                }

            }

        }

        return -1;
    }

    /**
     * 通过action 跳转页面
     *
     * @param action    包名路径
     * @param extra     参数
     * @param extraName 参数key
     */
    private void jumpAction(String action, BaseExtra extra, String extraName) {
        Class<?> clazz = null;
        try {
            Intent intent = new Intent();
            clazz = Class.forName(action);
            intent.setClass(mContext, clazz);
            if (extra != null) {
                intent.putExtra(extraName, extra);
            }
            mContext.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void jumpAction(String action) {
        jumpAction(action, null, null);
    }

    /**
     * 跳转评价详情
     *
     * @param id      订单id
     * @param version
     */
    private void jumpComment(String id, String version) {
        if (CMemoryData.isDriver()) {
            CommonProvider.getInstance().getJumpDriverProvider().jumpComment(mActivity, id, version);
        } else {
            CommonProvider.getInstance().getJumpShipperProvider().jumpComment(mActivity, id, version);
        }
    }


    /**
     * 跳转引导页
     */
    private void jumpGuide(SwitchExtra extra) {
        if (CMemoryData.isDriver()) {
            CommonProvider.getInstance().getJumpDriverProvider().jumpGuide(mActivity, extra);
        } else {
            CommonProvider.getInstance().getJumpShipperProvider().jumpGuide(mActivity, extra);
        }
    }


}
