package com.topjet.common.config;

import com.topjet.common.BuildConfig;
import com.topjet.common.base.net.RetrofitUtil;
import com.topjet.common.utils.Logger;
import com.topjet.wallet.config.WalletConfig;

public class Config {
    public static void init() {
        String environment = BuildConfig.ENVIRONMENT;
        Logger.d("环境变量 " + environment);

        //钱包初始化配置
        //WalletConfig.init(0);//0为开发和测试环境，1为测试环境，2为生产环境，3为堡垒环境 4内测环境
        switch (environment) {
            default:
            case "dev":
                WalletConfig.init(0);
                break;
            case "test":
                WalletConfig.init(1);
                break;
            case "baolei":
                WalletConfig.init(4);
//                onProductEnvironment();
                break;
            case "product":
                WalletConfig.init(2);
//                onProductEnvironment();
                break;
            case "closed_beta":
                WalletConfig.init(4);
                break;
        }
    }

    private static void onProductEnvironment() {
        Logger.setDebug(false);
        RetrofitUtil.setDebug(false);
    }

    public static String getAppHost() {
        return BuildConfig.BASE_URL;
    }

    public static String getCallPhoneAdvUrl() {
        return BuildConfig.CALL_PHONE_ADV;
    }

    public static String getProtocolUrl() {
        return BuildConfig.PROTOCOL_URL;
    }

    public static String getRefundUrl() {
        return BuildConfig.REFUND_URL;
    }

    public static String getCommentUrl() {
        return BuildConfig.COMMENT_URL;
    }

    public static String getComplaintUrl() {
        return BuildConfig.COMPLAINT_URL;
    }

    public static String getCheckCommentUrl() {
        return BuildConfig.CHECK_COMMENT_URL;
    }

    public static String getCommentListUrl() {
        return BuildConfig.COMMENT_LIST_URL;
    }

    public static String getHelpCenterUrl() {
        return BuildConfig.HELP_CENTER_URL;
    }

    public static String getIllegalInquiryUrl() {
        return BuildConfig.ILLEGAL_INQUIRY_URL;
    }
}