package com.topjet.common.config;

/**
 * Created by tsj-004 on 2017/8/18.
 * 各种错误码
 */

public class ErrorConstant {
    //APP自定义错误码
    public static final String APP_ERROR_TIMEOUT = "APP_ERROR_TIMEOUT";//超时
    public static final String APP_ERROR_HTTPEXCEPTION = "APP_ERROR_HTTPEXCEPTION";//服务端异常
    public static final String APP_ERROR_FAIL = "APP_ERROR_FAIL";//请求服务端失败

    //服务端返回
    public static final String E_USER_1 = "E_USER_1"; //您输入的手机号尚未注册！请先去注册!
    public static final String E_USER_2 = "E_USER_2"; //该手机号已被注册!
    public static final String E_USER_3 = "E_USER_3"; //您输入的密码错误！请重新输入
    public static final String E_USER_4 = "E_USER_4"; //验证码错误！请重新输入！
    public static final String E_USER_5 = "E_USER_5"; //系统检测到您正在使用新设备登陆账号为保证您的账户安全请使用验证码登陆
    public static final String E_USER_6 = "E_USER_6"; //用户注册信息保存失败
    public static final String E_USER_7 = "E_USER_7"; //图片上传失败!
    public static final String E_USER_8 = "E_USER_8"; //新密码不能与旧密码一样
    public static final String E_USER_9 = "E_USER_9"; //操作失败
    public static final String E_USER_10 = "E_USER_10"; //数据信息过期
    public static final String E_USER_11 = "E_USER_11"; //验证码已过期，请重新获取
    public static final String E_USER_12 = "E_USER_12"; //驾驶证与运营证二者必须上传一张
    public static final String E_USER_13 = "E_USER_13"; //用户已进行过此认证，无需再次进行
    public static final String E_USER_14 = "E_USER_14"; //验证码类型有误
    public static final String E_USER_19 = "E_USER_19"; //未读取到此用户数据，请联系管理员

    public static final String E_TRUCK_1 = "E_TRUCK_1"; //	此手机号尚未注册!
    public static final String E_TRUCK_2 = "E_TRUCK_2"; //	此车牌号已存在，请勿重复提交!
    public static final String E_TRUCK_3 = "E_TRUCK_3"; //	车辆信息提交失败，图片上传出现错误

    public static final String E_ORDER_15 = "E_ORDER_15"; // "E_ORDER_15","business_msg":"该订单已撤销，不能查看!"

    public static final String E_100 = "100";   // 会话不存在!请联系系统管理员
    public static final String E_101 = "101";   // 请求头缺少字段![mobile]
    public static final String E_102 = "102";   // 请求头缺少字段![imei]
    public static final String E_103 = "103";   // 会话错误,请联系系统管理员
    public static final String E_104 = "104";   // 会话已过期
    public static final String E_105 = "105";   // 请求头缺少字段![destination]
    public static final String E_106 = "106";   // 请求不存在！
    public static final String E_107 = "107";   // 用户未登录,请重新登录!
    public static final String E_109 = "109";   // ["congeal_remark":"冻结原因","congeal_time":"冻结时间"]	用户被冻结
    public static final String E_111 = "111";   // {"valid":"1","title":"系统维护","text":"维护时间2017.10.20——2017-10.21"}	系统维护
    public static final String E_200 = "200";    // 请求成功!
    public static final String E_300 = "300";    // 业务异常!
    public static final String E_112 = "112";    // 请求解密错误!

    /** AES解密错误 */
    // ZE_SESSION_112("112", "请求解密错误!"),
}
