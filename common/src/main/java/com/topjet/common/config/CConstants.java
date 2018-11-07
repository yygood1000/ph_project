package com.topjet.common.config;

public class CConstants {

    /**
     * 列表页 页数第一页
     */
    public static final int PAGE_START = 1;

    public static final int PAGE_SIZE = 15;  // 分页的条数


    /**
     * 两个应用程序的包名
     */
    public static final String SHIPPER_PACKAGE_NAME = "com.topjet.shipper";
    public static final String DRIVER_PACKAGE_NAME = "com.topjet.crediblenumber";

    /**
     * 定金最小金额限制
     */
    public static final double depositMin = 50;

    /**
     * 预加载 下标
     */
    public static final int PRE_LOAD_NUMBER = 4;
    /**
     * 获取验证码倒计时
     */
    public static int SECOND = 60;

    /**
     * 最低 允许内存剩余量百分比 （分配剩余内存/分配总内存）
     */
    public static double MIN_FREE_MEMORY_PERSENT = 0.4;

    /**
     * 司机版
     */
    public static final String REQUEST_SOURCE_DRIVER = "1";
    /**
     * 货主版
     */
    public static final String REQUEST_SOURCE_SHIPPER = "2";
    /**
     * 司机版，该版本号仅仅为升级使用，与当前真正的系统版本无关
     */
    public static final String OUT_VERSION_DRIVER = "3051001";// "1713263";//2008596/2109461
    /**
     * 货主版，该版本号仅仅为升级使用，与当前真正的系统版本无关
     */
    public static final String OUT_VERSION_SHIPPER = "3051001";// "1713263";//2008596/2109461

    /**
     * 聊天服务器是否登录成功
     */
    public static final String IM_IS_LOGIN_SUCCESS = "im_is_login_success";
    /**
     * 聊天服务器是否登录中
     */
    public static final String IM_IS_LOGIN_ING = "im_is_login_ing";

    /**
     * 聊天用户为空，不存在
     */
    public static final String IM_USER_EMPTY = "im_user_empty";

    /**
     * IM账户统一的密码
     */
    public static final String IM_USER_PASSWORD = "123456";

    /**
     * IM黑名单最大数
     */
    public static final int BLACK_LIST_MAX_SIZE = 15;

    /**
     * 区分司机聊天id前缀
     */
    public static final String IM_HEAR_DRIVER = "10";
    /**
     * 区分货主聊天id前缀
     */
    public static final String IM_HEAR_SHIPPER = "20";

    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 首页 滑动按钮组 版本号
     */
    public static final String BASE_SCROLL_BUTTONS_RESOURCE_VERSION = "3001001";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 首页 首页-下方导航 版本号
     */
    public static final String BASE_TABLAYOUT_BUTTONS_RESOURCE_VERSION = "3001001";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 车长版本号
     */
    public static final String BASE_TRUCK_LENGTH_RESOURCE_VERSION = "3001001";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 车型版本号
     */
    public static final String BASE_TRUCK_TYPE_RESOURCE_VERSION = "3001002";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 城市列表版本号
     */
    public static final String BASE_CITIES_RESOURCE_VERSION = "3001008";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 包装方式版本号
     */
    public static final String BASE_PACKING_TYPE_RESOURCE_VERSION = "3001001";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 装卸方式版本号
     */
    public static final String BASE_LOAD_TYPE_RESOURCE_VERSION = "3001001";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 货物类型版本号
     */
    public static final String BASE_GOODS_TYPE_RESOURCE_VERSION = "3001001";
    /**
     * 资源文件版本号 初始值,该值每次发包的时候修改 货物单位版本号
     */
    public static final String BASE_GOODS_UNIT_RESOURCE_VERSION = "3001001";

    /**
     * 客服号码
     */
    public static final String SERVICE_PHONE_NUMBER = "4000566560";
    /**
     * 客服号码
     */
    public static final String SERVICE_PHONE_NUMBER2 = "400-056-6560";

    /**
     * 本地数据库储存的 搜索记（车源搜素，网上货源搜索）录最大条数
     */
    public static final int MAX_SEARCH_RECORD = 5;
    /**
     * 上传当前位置的最小距离偏移（单位米）
     */
    public static double MIN_UNLOAD_LOCATION_DISTANCE = 150;
    /**
     * Kill Process前的延迟
     */
    public static int KILL_PROCESS_DELAYED = 1000;

    /**
     * 附近货源/车源 -> 地址录入搜索页面 返回码
     */
    public static int RESULT_CODE_AROUND_MAP_SEARCH = 1001;

    /**
     * AES 加密key 和 偏移值
     */
    public static class AppkeyConstant {
        /**
         * APP AES加密key AES_偏移值
         */
        public static String APP_AES_KEY = "2@!fsfjk$*^daksj";
        public static String APP_AES_IVCODE = "@8#9^das2~$09fsc";

    }

    /**
     * 上传图片相关设置
     */
    public static class UploadBitmapSet {
        public static int reqWidth = 400;//图片宽度
        public static int reqHeight = 800;//图片高度
        public static int reqSquareWidth = 300;//正方形图片宽度
        public static int reqSquareHeight = 300;//正方形图片宽度
        public static int bitmapMaxSize = 300;//设置上传最终大小峰值。单位为KB
    }

    /**
     * 龙驹 url
     */
    public static final String LJCH_url = "http://m.longjubank.com/AccountRegister/AccountRegister/560";
    /**
     * 趣赢 url
     */
    public static final String QY_url = "http://account.177lele.com/down/560/560.html";
    /**
     * 好运购 url
     */
    public static final String HYG_url = "http://m.97hyg.com/novice";


}
