package com.topjet.common.common.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.ScrollBtnOptions;
import com.topjet.common.common.presenter.SkipControllerWallet;
import com.topjet.common.common.view.activity.HelpCenterActivity;
import com.topjet.common.common.view.activity.IntegrityInquiryActivity;
import com.topjet.common.common.view.activity.WeatherActivity;
import com.topjet.common.config.ScrollBtnsUrlConfigs;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * RecyclerView 适配器
 * <p>
 * viewHolder.getLayoutPosition() 获取当前item的position
 */

public class ScrollBtnsRecyclerAdapter extends BaseQuickAdapter<ScrollBtnOptions, BaseViewHolder> {
    private OnCustomItemDriverClickListener driverItemClickListener;
    private OnCustomItemShipperClickListener shipperItemClickListener;

    private Context mContext;

    /**
     * 创建一个接口父类，方便设置点击事件是参数传入
     */
    public interface OnScrollBtnsClickListener {
    }

    public interface OnCustomItemDriverClickListener extends OnScrollBtnsClickListener {

        void turnToShipper();

        void turnToIllegalQuery();

    }

    public interface OnCustomItemShipperClickListener extends OnScrollBtnsClickListener {

        void turnToDriver();

        void turnToUsualContact();
    }

    public void setShipperItemClickListener(OnCustomItemShipperClickListener shipperItemClickListener) {
        this.shipperItemClickListener = shipperItemClickListener;
    }

    public void setDriverItemClickListener(OnCustomItemDriverClickListener driverItemClickListener) {
        this.driverItemClickListener = driverItemClickListener;
    }

    public ScrollBtnsRecyclerAdapter(Context mContext) {
        super(R.layout.griditem_homepage);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final ScrollBtnOptions item) {
        ImageView ivIcon = viewHolder.getView(R.id.iv_btn_icon);

        int iconResouceId = loadDefaultIcon(item.getUrl());

        // 图片地址或者key没有的时候直接加载默认图片
        if (StringUtils.isEmpty(item.getIcon_url()) || StringUtils.isBlank(item.getIcon_key())) {
            GlideUtils.loaderImageResource(iconResouceId, ivIcon);
        } else {
            GlideUtils.loaderImageCircle(mContext,
                    item.getIcon_url(),
                    iconResouceId,
                    iconResouceId,
                    ivIcon);
        }

        // 设置Title
        if (StringUtils.isNotBlank(item.getTitle())) {
            setText(viewHolder, R.id.tv_item_title, item.getTitle());
        } else {
            setText(viewHolder, R.id.tv_item_title, "");
        }

        // 设置介绍文本
        if (StringUtils.isNotBlank(item.getContent())) {
            setText(viewHolder, R.id.tv_item_remark, item.getContent());
        } else {
            setText(viewHolder, R.id.tv_item_remark, "");
        }

        // Item 父布局
        LinearLayout llRoot = viewHolder.getView(R.id.ll_root);
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item.getUrl()) {
                    /*
                     * 判断共有页面
                     */
                    case ScrollBtnsUrlConfigs.CommonUrl.URL_WALLET_MAIN_ACTIVITY:
                        // 跳转到钱包
                        CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new
                                CheckUserStatusUtils.OnJudgeResultListener() {
                                    @Override
                                    public void onSucceed() {
                                        SkipControllerWallet.skipToWalletMain((Activity) mContext);
                                    }
                                });
                        break;
                    case ScrollBtnsUrlConfigs.CommonUrl.URL_CREDIT_QUERY_ACTIVITY:
                        // 跳转到诚信查询
                        CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext,
                                new CheckUserStatusUtils.OnJudgeResultListener() {
                                    @Override
                                    public void onSucceed() {
                                        ((MvpActivity) mContext).turnToActivity(IntegrityInquiryActivity.class);
                                    }
                                });
                        break;
                    case ScrollBtnsUrlConfigs.CommonUrl.URL_HELP_CENTER_ACTIVITY:
                        // 跳转到帮助中心
                        ((MvpActivity) mContext).turnToActivity(HelpCenterActivity.class);
                        break;
                    case ScrollBtnsUrlConfigs.CommonUrl.URL_WEATHER_ACTIVITY:
                        // TODO 跳转到天气
                        ((MvpActivity) mContext).turnToActivity(WeatherActivity.class);
                        break;
                    case ScrollBtnsUrlConfigs.CommonUrl.URL_LONG_JU_BANK:
                        // TODO 跳转到龙驹财行
                        turnToOtherApp((Activity) mContext, item.getUrl());
                        break;
                    case ScrollBtnsUrlConfigs.CommonUrl.URL_QU_YIN_GAME:
                        // 跳转趣赢游戏
                        turnToOtherApp((Activity) mContext, item.getUrl());
                        break;

                    /*
                     * 判断司机跳转
                     */
                    // 违章查询
                    case ScrollBtnsUrlConfigs.DriverUrl.URL_ILLEGAL_QUERY_ACTIVITY:
                        if (driverItemClickListener != null) {
                            driverItemClickListener.turnToIllegalQuery();
                        }
                        break;
                    // 切换到货主版
                    case ScrollBtnsUrlConfigs.DriverUrl.URL_SHIPPER_SPLASH_ACTIVITY:
                        if (driverItemClickListener != null) {
                            driverItemClickListener.turnToShipper();
                        }
                        break;
                    /*
                     * 判断货主跳转
                     */
                    // 常用联系人
                    case ScrollBtnsUrlConfigs.ShipperUrl.URL_CONTACTS_LIST_ACTIVITY:
                        if (shipperItemClickListener != null) {
                            shipperItemClickListener.turnToUsualContact();
                        }
                        break;
                    // 切换到司机版
                    case ScrollBtnsUrlConfigs.ShipperUrl.URL_DRIVER_SPLASH_ACTIVITY:
                        if (shipperItemClickListener != null) {
                            shipperItemClickListener.turnToDriver();
                        }
                        break;
                }
            }
        });
    }

    private int loadDefaultIcon(String url) {
        int id = R.drawable.shape_avatar_loading;
        switch (url) {
            case ScrollBtnsUrlConfigs.CommonUrl.URL_WALLET_MAIN_ACTIVITY:
                // 跳转到钱包
                id = R.drawable.iv_icon_default_wallet;
                break;
            case ScrollBtnsUrlConfigs.CommonUrl.URL_CREDIT_QUERY_ACTIVITY:
                // 跳转到诚信查询
                id = R.drawable.iv_icon_default_credit;
                break;
            case ScrollBtnsUrlConfigs.CommonUrl.URL_HELP_CENTER_ACTIVITY:
                // 跳转到帮助中心
                id = R.drawable.iv_icon_default_helpcenter;
                break;
            case ScrollBtnsUrlConfigs.CommonUrl.URL_WEATHER_ACTIVITY:
                //  跳转到天气
                id = R.drawable.iv_icon_default_weather;
                break;
            case ScrollBtnsUrlConfigs.CommonUrl.URL_LONG_JU_BANK:
                //  跳转到龙驹财行
                id = R.drawable.iv_icon_default_longju;
                break;
            case ScrollBtnsUrlConfigs.CommonUrl.URL_QU_YIN_GAME:
                // 跳转趣赢游戏
                id = R.drawable.iv_icon_default_qygame;
                break;
            case ScrollBtnsUrlConfigs.DriverUrl.URL_ILLEGAL_QUERY_ACTIVITY:
                // 违章查询
                id = R.drawable.iv_icon_default_illegal;
                break;
            case ScrollBtnsUrlConfigs.DriverUrl.URL_SHIPPER_SPLASH_ACTIVITY:
                // 切换到货主版
                id = R.drawable.iv_icon_default_shipper;
                break;
            case ScrollBtnsUrlConfigs.ShipperUrl.URL_CONTACTS_LIST_ACTIVITY:
                // 常用联系人
                id = R.drawable.iv_icon_default_contacts;
                break;
            case ScrollBtnsUrlConfigs.ShipperUrl.URL_DRIVER_SPLASH_ACTIVITY:
                // 切换到司机版
                id = R.drawable.iv_icon_default_driver;
                break;
        }
        return id;
    }

    /**
     * 跳转外部APP
     */

    private void turnToOtherApp(Activity activity, String uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        Uri content_url = Uri.parse(uri);
        intent.setData(content_url);
        activity.startActivity(intent);
    }

    /**
     * 设置文本内容，如果没有文本则设置为灰色方框
     */
    private void setText(BaseViewHolder viewHolder, int viewId, String str) {
        if (StringUtils.isNotBlank(str)) {
            viewHolder.setText(viewId, str);
            viewHolder.setBackgroundColor(viewId, ResourceUtils.getColor(R.color.transparent_100));
        } else {
            viewHolder.setText(viewId, "");
            viewHolder.setBackgroundColor(viewId, ResourceUtils.getColor(R.color.color_EEEEEE));
        }

    }
}
