package com.topjet.common.config;

/**
 * Created by yyy on 2017/9/17.
 * <p>
 * 滑动按钮组  URL 配置类
 * <p>
 * 司机/货主公用类
 * <p>
 * 该类中配置的URL 为需要跳转页面的类名。需要与服务端返回的滑动按钮组集合中的url字段相同才进行跳转
 * <p>
 * 字段在ScrollBtnsRecyclerAdapter 适配器中使用
 */

public class ScrollBtnsUrlConfigs {

    public static class CommonUrl {
        /**
         * 共有跳转
         */
        // 钱包首页
        public static final String URL_WALLET_MAIN_ACTIVITY = "WalletMainActivity";
        // 诚信查询
        public static final String URL_CREDIT_QUERY_ACTIVITY = "IntegrityInquiryActivity";
        // 帮助中心
        public static final String URL_HELP_CENTER_ACTIVITY = "HelpCenterActivity";
        // 天气
        public static final String URL_WEATHER_ACTIVITY = "WeatherActivity";
        // 龙驹财行
        public static final String URL_LONG_JU_BANK = "http://m.longjubank.com/AccountRegister/AccountRegister/560";
        // 趣赢游戏
        public static final String URL_QU_YIN_GAME = "http://account.177lele.com/down/560/560.html";
    }


    /**
     * 司机独有跳转
     */
    public static class DriverUrl {
        // 货主版首页（司机切换到货主版）
        public static final String URL_SHIPPER_SPLASH_ACTIVITY = "ShipperSplashActivity";
        // 违章查询
        public static final String URL_ILLEGAL_QUERY_ACTIVITY = "IllegalQueryActivity";
    }

    /**
     * 货主独有跳转
     */
    public static class ShipperUrl {
        // 司机版首页（货主跳转司机版）
        public static final String URL_DRIVER_SPLASH_ACTIVITY = "DriverSplashActivity";
        // 常用联系人
        public static final String URL_CONTACTS_LIST_ACTIVITY = "ContactsListActivity";
    }
}
