package com.topjet.common.config;

import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.common.modle.response.GetTabLayoutBtnsResponse;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.SPUtils;
import com.topjet.common.utils.StringUtils;

import java.util.Set;

/**
 * Persistent data
 */
public class CPersisData {
    /**
     * 是否已登录
     */
    private static final String SP_KEY_IS_LOGIN = "isLogin";

    /**
     * 用户手机号
     */
    private static final String SP_KEY_USER_MOBILE = "userMobile";

    /**
     * 用户姓名
     */
    private static final String SP_KEY_USER_NAME = "SP_KEY_USER_NAME";

    /**
     * 用户id
     */
    private static final String SP_KEY_LOGIN_USER_ID = "userId";

    /**
     * 用户版本号
     */
    private static final String SP_KEY_USER_VERSION = "userVersion";

    /**
     * 当前Application开启前是否Crash过
     */
    private static final String SP_KEY_HAS_JUST_CRASHED = "hasJustCrashed";

    /**
     * 异常日志   描述
     */
    private static final String SP_KEY_EXCEPTION_DESCRIPTION = "description";

    /**
     * 记录560用户Session
     */
    private static final String SP_KEY_560USER_SESSION = "560UserSession";

    /**
     * 加密key
     */
    private static final String SP_AES_KEY = "key_aes";

    /**
     * 加密key
     */
    private static final String SP_AES_IVCODE = "key_aes_ivcode";

    /**
     * 新手引导页
     */
    private static final String SP_KEY_GUIDE = "Guide";

    /**
     * 是否是第一次进入听单界面
     * 是：开启听单，开启听单快捷菜单，终点默认勾选用户的全部常跑城市
     */
    private static final String SP_KEY_IS_FIRST_LISTENORDER = "SP_KEY_IS_FIRST_LISTENORDER";

    /**
     * 悬浮控件是否开启
     */
    private static final String FLOAT_VIEW_STATUS = "FLOAT_VIEW_STATUS";

    /**
     * 是否已经实名认证
     */
    private static final String SP_KEY_REALNAME_AUTHENTICATION = "RealNameAuthentication";

    /**
     * 是否显示过新手引导页Dialog
     */
    private static final String SP_KEY_IS_SHOWED_NOVICE_BOOT = "isShowedNoviceBootDialog";

    /**
     * 附近货源 地址搜索页面 历史记录
     */
    private static final String SP_KEY_SEARCH_HISTROY = "searchHistroy";

    /**
     * 货源列表页面是否使用大号字体布局
     */
    private static final String SP_KEY_IS_LARGE_GOODS_ITEM = "isLargeGoodsItem";

    /**
     * 货源列表页面字体布局是否设置过
     */
    private static final String SP_KEY_IS_CHANGED_GOODS_ITEM = "isChangedGoodsItem";

    /**
     * 听单-》出发地
     */
    private static final String SP_KEY_LISTEN_ORDER_START_CITY_ID = "sp_key_listen_order_start_city_id";

    /**
     * 听单-》目的地
     */
    private static final String SP_KEY_LISTEN_ORDER_DESTINATION_CITY_IDS = "sp_key_listen_order_destination_city_ids";

    /**
     * 首页弹窗当天标记
     */
    private static final String SP_KEY_HOME_DIALOG_KEY = "homeDialog";

    /**
     * 启动页广告信息
     */
    private static final String SP_KEY_SPLASH_ADV_INFO = "splash_adv_info";

    /**
     * 推送token  Registration Id
     */
    private static final String SP_KEY_PUSH_TOKEN = "push_token";

    /**
     * app 是否处于后台运行
     */
    private static final String SP_KEY_IS_BACKGROUND = "is_background";

    /**
     * 首页 首页-下方导航
     */
    /**
     * 切换app
     */
    public static final String SP_SWITCH_KEY = "sp_switch_key";

    /*================================获取货源刷新信息==================================*/
    /**
     * 货主-获取货源刷新信息---------->刷新分钟数
     */
    private static final String SP_KEY_GOODSREFRESHINFO_REFRESH_MINUTE = "SP_KEY_GOODSREFRESHINFO_REFRESH_MINUTE";

    /**
     * 货主-获取货源刷新信息---------->刷新次数
     */
    private static final String SP_KEY_GOODSREFRESHINFO_REFRESH_COUNT = "SP_KEY_GOODSREFRESHINFO_REFRESH_COUNT";


    /*=================================资源文件相关=================================*/
    // 首页 首页-下方导航 版本号
    public static final String SP_KEY_TABLAYOUT_BUTTONS_VERSION = "home_page_tablayout_btns_version";
    // 车型版本号
    public static final String SP_KEY_TRUCK_LENGTH_VERSION = "truck_length_version";
    // 车长版本号
    public static final String SP_KEY_TRUCK_TYPE_VERSION = "truck_type_version";
    // 城市列表版本号
    public static final String SP_KEY_CITIES_VERSION = "cities_version";
    // 包装方式版本号
    public static final String SP_KEY_PACKING_TYPE_VERSION = "packing_type_version";
    // 装卸方式版本号
    public static final String SP_KEY_LOAD_TYPE_VERSION = "load_type_version";
    // 货物类型版本号
    public static final String SP_KEY_GOODS_TYPE_VERSION = "goods_type_version";
    // 货物单位版本号
    public static final String SP_KEY_GOODS_UNIT_VERSION = "goods_unit_version";
    //首页 滑动按钮组 版本号
    public static final String SP_KEY_SCROLL_BUTTONS_VERSION = "home_page_scroll_btns_version";
    // 首页 滑动按钮组List集合
    private static final String SP_KEY_SCROLL_BUTTONS = "home_page_scroll_btns";
    // 首页 首页-下方导航
    private static final String SP_KEY_TABLAYOUT_BUTTONS = "home_page_tablayout_btns";
    /*=================================资源文件相关=================================*/


    /**
     * 关键字统计，聊天，总数
     */
    private static final String SP_IM_SENSITIVE_WORD_AMOUNT = "im_sensitive_word_amount";


    /**
     * 保存服务器返回的时间
     */
    public static final String SP_SERVERS_DATE = "servers_date";

    /**
     * 聊天关键字，提交时间
     */
    public static final String SP_IM_SENSITIVE_WORD_SUBMIT_TIME = "sensitive_word_submit_time";

    /**
     * 用户认证状态 等信息
     */
    public static final String SP_USER_BASE_INFO = "sp_user_base_info";

    /**
     * ==================================API 方法==================================
     */

    /**
     * 请求加密key
     */
    public static void setKey(String key){
        SPUtils.putString(SP_AES_KEY, key);
    }

    public static String getKey(){
        return SPUtils.getString(SP_AES_KEY, "");
    }

    /**
     * 加密偏移量
     */
    public static void setKeyIvCode(String ivCode){
        SPUtils.putString(SP_AES_IVCODE, ivCode);
    }

    public static String getKeyIvCode(){
        return SPUtils.getString(SP_AES_IVCODE, "");
    }

    /*=================================设置用户基础参数=================================*/
    public static void setUserBaseInfo(String userBaseInfo) {
        SPUtils.putString(SP_USER_BASE_INFO, userBaseInfo);
    }

    public static String getUserBaseInfo() {
        return SPUtils.getString(SP_USER_BASE_INFO, "");
    }

    /*=================================设置用户基础参数=================================*/
    /*=================================聊天关键字，提交时间=================================*/

    public static void setSensitiveWordSubmitTime(long value) {
        SPUtils.putLong(SP_IM_SENSITIVE_WORD_SUBMIT_TIME, value);
    }

    public static long getSensitiveWordSubmitTime() {
        return SPUtils.getLong(SP_IM_SENSITIVE_WORD_SUBMIT_TIME, 0);
    }

    /*=================================聊天关键字，提交时间=================================*/
    /*=================================服务器返回的时间=================================*/

    public static void setServersDate(long value) {
        SPUtils.putLong(SP_SERVERS_DATE, value);
    }

    public static long getServersDate() {
        return SPUtils.getLong(SP_SERVERS_DATE, 0);
    }

    /*=================================服务器返回的时间=================================*/
    /*=================================关键字统计，聊天，总数=================================*/

    public static void setSensitiveWordAmount(long value) {
        SPUtils.putLong(SP_IM_SENSITIVE_WORD_AMOUNT, value);
    }

    public static long getSensitiveWordAmount() {
        return SPUtils.getLong(SP_IM_SENSITIVE_WORD_AMOUNT, 0);
    }

    /*=================================关键字统计，聊天，总数=================================*/
    /*=================================app 是否处于后台运行=================================*/

    public static void setIsBackground(boolean value) {
        SPUtils.putBoolean(SP_KEY_IS_BACKGROUND, value);
    }

    public static boolean getIsBackground() {
        return SPUtils.getBoolean(SP_KEY_IS_BACKGROUND, false);
    }

    /*=================================app 是否处于后台运行=================================*/
    /*=================================推送token=================================*/

    public static void setPushToken(String token) {
        SPUtils.putString(SP_KEY_PUSH_TOKEN, token);
    }

    public static String getPushToken(String def) {
        return SPUtils.getString(SP_KEY_PUSH_TOKEN, def);
    }

    /*=================================推送token=================================*/
    /*=================================听单起点城市=================================*/
    public static String getListenOrderStartCityId() {
        return SPUtils.getString(SP_KEY_LISTEN_ORDER_START_CITY_ID, "");
    }

    public static void setListenOrderStartCityId(String id) {
        SPUtils.putString(SP_KEY_LISTEN_ORDER_START_CITY_ID, id);
    }

    /*=================================听单起点城市=================================*/
    /*=================================听单悬浮窗显示状态 true 开false 关=================================*/

    public static String getFloatViewStatus() {
        return SPUtils.getString(FLOAT_VIEW_STATUS, "");
    }

    public static void setFloatViewStatus(String status) {
        SPUtils.putString(FLOAT_VIEW_STATUS, status);
    }

    /*=================================听单悬浮窗显示状态 true 开false 关=================================*/
    /*=================================听单目的地城市=================================*/

    public static Set<String> getListenOrderDestinationCityIds() {
        return SPUtils.getStringSet(SP_KEY_LISTEN_ORDER_DESTINATION_CITY_IDS, null);
    }

    public static void setListenOrderDestinationCityIds(Set<String> ids) {
        SPUtils.putStringSet(SP_KEY_LISTEN_ORDER_DESTINATION_CITY_IDS, ids);
    }

    /*=================================听单目的地城市=================================*/
    /*=================================错误日志=================================*/

    public static String getExceptionDescription() {
        return SPUtils.getString(SP_KEY_EXCEPTION_DESCRIPTION, "");
    }

    public static void setExceptionDescription(String description) {
        SPUtils.putString(SP_KEY_EXCEPTION_DESCRIPTION, description);
    }
    /*=================================错误日志=================================*/
    /*=================================是否已经登录=================================*/

    public static boolean getIsLogin() {
        return SPUtils.getBoolean(SP_KEY_IS_LOGIN, false);
    }

    public static void setIsLogin(boolean isLogin) {
        SPUtils.putBoolean(SP_KEY_IS_LOGIN, isLogin);
    }

    /*=================================是否已经登录=================================*/
    /*=================================用户信息相关=================================*/

    public static String getUserMobile() {
        return SPUtils.getString(SP_KEY_USER_MOBILE, null);
    }

    public static void setUserMobile(String userMobile) {
        SPUtils.putString(SP_KEY_USER_MOBILE, userMobile);
    }

    public static String getUserName() {
        return SPUtils.getString(SP_KEY_USER_NAME, "");
    }

    public static void setUserName(String userName) {
        SPUtils.putString(SP_KEY_USER_NAME, userName);
    }


    public static String getUserID() {
        return SPUtils.getString(SP_KEY_LOGIN_USER_ID, null);
    }

    public static void setUserId(String usrid) {
        SPUtils.putString(SP_KEY_LOGIN_USER_ID, usrid);
    }

    /*=================================用户信息相关=================================*/
    /*=================================崩溃标记=================================*/

    public static boolean hasJustCrashed() {
        return SPUtils.getBoolean(SP_KEY_HAS_JUST_CRASHED, false);
    }

    public static void setHasJustCrashed(boolean hasJustCrashed) {
        SPUtils.putBoolean(SP_KEY_HAS_JUST_CRASHED, hasJustCrashed);
    }

    /*=================================崩溃标记=================================*/
    /*=================================560用户在SP中的Session=================================*/
    public static String getUserSession() {
        return SPUtils.getString(SP_KEY_560USER_SESSION, null);
    }

    public static void setUserSession(String sessionValue) {
        SPUtils.putString(SP_KEY_560USER_SESSION, sessionValue);
    }

    /*=================================560用户在SP中的Session=================================*/
    /*=================================用户版本号=================================*/

    public static String getUserVersion() {
        return SPUtils.getString(SP_KEY_USER_VERSION, null);
    }

    public static void setUserVersion(String userVersion) {
        SPUtils.putString(SP_KEY_USER_VERSION, userVersion);
    }
    /*=================================用户版本号=================================*/
    /*=================================装机引导页标记=================================*/

    public static boolean getIsGuide() {
        return SPUtils.getBoolean(SP_KEY_GUIDE, true);
    }

    public static void setGuideFalse() {
        SPUtils.putBoolean(SP_KEY_GUIDE, false);
    }

    public static boolean getIsFirstListenOrder() {
        return SPUtils.getBoolean(SP_KEY_IS_FIRST_LISTENORDER, true);
    }

    public static void setIsFirstListenOrder(boolean boo) {
        SPUtils.putBoolean(SP_KEY_IS_FIRST_LISTENORDER, boo);
    }

    /*=================================装机引导页标记=================================*/
    /*=================================新手引导页标记=================================*/

    public static boolean getIsShowedNoviceBoot() {
        return SPUtils.getBoolean(SP_KEY_IS_SHOWED_NOVICE_BOOT, false);
    }

    public static void setIsShowedNoviceBoot() {
        SPUtils.putBoolean(SP_KEY_IS_SHOWED_NOVICE_BOOT, true);
    }

    /*=================================新手引导页标记=================================*/
    /*=================================地址搜索页面 历史记录=================================*/

    public static String getSearchHistroy() {
        return SPUtils.getString(SP_KEY_SEARCH_HISTROY, "");
    }

    public static void setSearchHistroy(String listJson) {
        SPUtils.putString(SP_KEY_SEARCH_HISTROY, listJson);
    }

    /*=================================地址搜索页面 历史记录=================================*/
    /*=================================使用大号字体的货源列表布局标记=================================*/

    public static boolean getIsLargeGoodsItem() {
        return SPUtils.getBoolean(SP_KEY_IS_LARGE_GOODS_ITEM, false);
    }

    public static void setIsLargeGoodsItem(boolean isLargeGoodsItem) {
        SPUtils.putBoolean(SP_KEY_IS_LARGE_GOODS_ITEM, isLargeGoodsItem);
    }

    public static boolean getIsChangedGoodsItem() {
        return SPUtils.getBoolean(SP_KEY_IS_CHANGED_GOODS_ITEM, false);
    }

    public static void setIsChangedGoodsItem(boolean isChnagedGoodsItem) {
        SPUtils.putBoolean(SP_KEY_IS_CHANGED_GOODS_ITEM, isChnagedGoodsItem);
    }

    /*=================================使用大号字体的货源列表布局标记=================================*/
    /*=================================首页广告当日弹窗标记=================================*/

    public static int getHomeDialogFlay() {
        return SPUtils.getInt(SP_KEY_HOME_DIALOG_KEY, -1);
    }

    public static void setHomeDialogFlay(int today) {
        SPUtils.putInt(SP_KEY_HOME_DIALOG_KEY, today);
    }

    /*=================================首页广告当日弹窗标记=================================*/
    /*=================================启动页广告信息=================================*/

    public static BannerBean getSplashAdvInfo() {
        return (BannerBean) DataFormatFactory.getInstanceByJson(BannerBean.class, SPUtils.getString
                (SP_KEY_SPLASH_ADV_INFO, ""));
    }

    public static void setSplashAdvInfo(BannerBean info) {
        SPUtils.putString(SP_KEY_SPLASH_ADV_INFO, DataFormatFactory.toJson(info));
    }

    /*=================================启动页广告信息=================================*/


    /*=================================首页底部切换标签 本地存储Json字符串=================================*/
    public static GetTabLayoutBtnsResponse getTabLayoutBtnsInfo() {
        return (GetTabLayoutBtnsResponse) DataFormatFactory.getInstanceByJson(GetTabLayoutBtnsResponse.class, SPUtils
                .getString(SP_KEY_TABLAYOUT_BUTTONS, ""));
    }

    public static void setTabLayoutBtnsInfo(GetTabLayoutBtnsResponse response) {
        SPUtils.putString(SP_KEY_TABLAYOUT_BUTTONS, DataFormatFactory.toJson(response));
    }

    /*=================================首页底部切换标签 本地存储Json字符串=================================*/
    /*=================================滑动按钮组 本地存储Json字符串=================================*/

    public static String getScrollBtnsJsons() {
        return SPUtils.getString(SP_KEY_SCROLL_BUTTONS, "");
    }

    public static void setScrollBtnsJsons(String listJson) {
        SPUtils.putString(SP_KEY_SCROLL_BUTTONS, listJson);
    }

    /*=================================滑动按钮组 本地存储Json字符串=================================*/
    /*=================================获取资源版本号=================================*/

    /**
     * 获取资源文件版本号
     *
     * @param resourceTypeKey 需要获取的资源的key
     *                        1.SP_KEY_TABLAYOUT_BUTTONS_VERSION 首页 首页-下方导航 版本号
     *                        2.SP_KEY_SCROLL_BUTTONS_VERSION 首页 滑动按钮组 版本号
     *                        3.SP_KEY_TRUCK_LENGTH_VERSION 车型版本号
     *                        4.SP_KEY_TRUCK_TYPE_VERSION 车长版本号
     *                        5.SP_KEY_CITIES_VERSION   城市列表版本号
     *                        6.SP_KEY_PACKING_TYPE_VERSION     包装方式版本号
     *                        7.SP_KEY_LOAD_TYPE_VERSION 装卸方式版本号
     *                        8.SP_KEY_GOODS_TYPE_VERSION 货物类型版本号
     *                        9.SP_KEY_GOODS_UNIT_VERSION  货物单位版本号
     */
    public static String getTablayoutButtonsReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_TABLAYOUT_BUTTONS_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setTablayoutButtonsReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getScrollButtonsReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_SCROLL_BUTTONS_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setScrollButtonsReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getTruckLengthReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_TRUCK_LENGTH_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setTruckLengthReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getTruckTypeReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_TRUCK_TYPE_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setTruckTypeReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getCitiesReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_CITIES_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setCitiesReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getPackingTypeReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_PACKING_TYPE_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setPackingTypeReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getLoadTypeReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_LOAD_TYPE_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setLoadTypeReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getGoodsTypeReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_GOODS_TYPE_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setGoodsTypeReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }
    public static String getGoodsUnitReourceVersion(String resourceTypeKey) {
        return StringUtils.isBlank(SPUtils.getString(resourceTypeKey, "")) ?
                CConstants.BASE_GOODS_UNIT_RESOURCE_VERSION : SPUtils.getString(resourceTypeKey, "");
    }

    public static void setGoodsUnitReourceVersion(String resourceTypeKey, String version) {
        SPUtils.putString(resourceTypeKey, version);
    }

    /*=================================获取资源版本号=================================*/
    /*================================动态获取货源刷新信息==================================*/

    public static void setSpKeyGoodsrefreshinfoRefreshCount(String count) {
        SPUtils.putString(SP_KEY_GOODSREFRESHINFO_REFRESH_COUNT, count);
    }

    public static String getSpKeyGoodsrefreshinfoRefreshCount() {
        return SPUtils.getString(SP_KEY_GOODSREFRESHINFO_REFRESH_COUNT, null);
    }

    public static void setSpKeyGoodsrefreshinfoRefreshMinute(String minute) {
        SPUtils.putString(SP_KEY_GOODSREFRESHINFO_REFRESH_MINUTE, minute);
    }

    public static String getSpKeyGoodsrefreshinfoRefreshMinute() {
        return SPUtils.getString(SP_KEY_GOODSREFRESHINFO_REFRESH_MINUTE, null);
    }

    /*================================动态获取货源刷新信息==================================*/
}
