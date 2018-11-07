/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui;

public class EaseConstant {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    
    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
    
    public static final String MESSAGE_ATTR_AT_MSG = "em_at_list";
    public static final String MESSAGE_ATTR_VALUE_AT_MSG_ALL = "ALL";

    public static final String MESSAGE_ATTR_IS_ORDER_INFO = "em_is_order_info";//标识识订单详情类的消息
    public static final String MESSAGE_ATTR_DEPART = "em_depart";//出发地
    public static final String MESSAGE_ATTR_DESTINATION = "em_destination";//目的地
    public static final String MESSAGE_ATTR_TRUCK_INFO = "em_truck_info";//车辆信息
//    public static final String MESSAGE_ATTR_ORDER_ID = "em_order_id";//订单id
    public static final String MESSAGE_ATTR_GOODS_ID = "em_goods_id";//货源id
    public static final String MESSAGE_ATTR_GOODS_VERSION = "em_goods_version";//货源版本
//    public static final String MESSAGE_ATTR_IS_GOODS = "em_is_goods";//是否货源
//    public static final String MESSAGE_ATTR_LOCATION_NAME = "em_location_name";//位置名称

    public static final String MESSAGE_ATTR_TALK_ID = "talkId";//用户聊天账号id 10,20
    public static final String MESSAGE_ATTR_USER_ID = "userId";//用户id
    public static final String MESSAGE_ATTR_TALK_NAME = "talkName";//聊天对象用户名
    public static final String MESSAGE_ATTR_TALK_PHONE = "talkPhone";//聊天对象手机号
    public static final String MESSAGE_ATTR_IS_ANONYMOUS = "isAnonymous";//是否需要显示匿名
    public static final String MESSAGE_ATTR_ICON_URL = "iconUrl";//聊天对象头像
    public static final String MESSAGE_ATTR_ICON_KEY = "iconKey";//头像key
    public static final String MESSAGE_ATTR_SEX = "sex";//性别

    public static final String MESSAGE_ATTR_EMPTY = "empty";//空消息，为了保存没有消息的回话

    public static final String MESSAGE_ATTR_APP_TYPE = "app_type";// 发货版 shipper 接货版 driver   app类型，发货版发的消息，不在接货版中显示
    public static final String APP_TYPE_SHIPPER = "shipper";//货主版
    public static final String APP_TYPE_DRIVER = "driver";//司机版


    public static final String MESSAGE_ATTR_BY_ADD_BLACK_LIST = "em_by_add_black_list";//被加入黑名单了
    public static final String MESSAGE_ATTR_IS_FROM_HISTORY = "em_is_fromHistory";//消息是否来至于历史消息接口

    
	public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;
    
    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";

    public static final String REQUEST_RED_BAG = "request_red_bag";// 收到透传消息，请求红包状态
}
