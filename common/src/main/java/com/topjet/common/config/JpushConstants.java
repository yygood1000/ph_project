package com.topjet.common.config;
/**
 * 2.0.0 极光推送类型
*@author yaoq
*/
public class JpushConstants {
	
	
	
	
	
	/**
	 * 大类型消息的类型
	 */
	public static class MsgType {
		/**
		 * 不计入消息中心的
		 * 比如听单关闭后的推送
		 */
		public static final String TYPE_DEFAUTL = "0";
		/**
		 * 用户类型
		 */
		public static final String TYPE_USER = "1";
		/**
		 * 车辆类型
		 */
		public static final String TYPE_TRUCK = "2";
		/**
		 * 订单类型
		 */
		public static final String TYPE_ORDER = "3";
		/**
		 * 钱包类型
		 */
		public static final String TYPE_WALLET = "4";
		/**
		 * 其他类型
		 */
		public static final String TYPE_QITA = "5";

		/**
		 * 公告类型
		 */
		public static final String TYPE_Announcement = "6";
	}

	/**
	 * /**
	 * 不计入消息中心的
	 * 比如听单关闭后的推送
	 *类型细分
	 */
	public static class DefaultMsgType {
		/** 听单提醒推送
		 * 为保证您能及时收到新货源，建议您打开听单。
		 * 司机版
		 * */
		public static final String ORDER_NOTIFICATION_OFF="1";

	}
	
	/**
	 * 用户相关消息类型细分
	 */
	public static class UserMsgType {
		/**
		 * 1:用户审核通过  司机 和货主
		 * 实名认证
		 */
		public static final String TYPE_USER_SUC = "1";
		/**
		 * 2:用户审核失败 司机 和货主
		 * 实名认证
		 */
		public static final String TYPE_USER_FAIL = "2";

		/**
		 * 3:积分兑换商品审核成功 司机 和货主
		 */
		public static final String TYPE_USER_JIFEN_SUC = "3";

		/**
		 * 4:积分兑换商品审核失败 司机 和货主
		 */
		public static final String TYPE_USER__JIFEN_FAIL= "4";


		/**
		 * 5:身份认证通过  司机 和货主
		 */
		public static final String TYPE_ID_SUC = "5";
		/**
		 * 6:身份认证失败 司机 和货主
		 */
		public static final String TYPE_ID_FAIL = "6";




		//只弹出通知  点击不做任何操作
		/**
		 * 1, "7", "您的头像认证已通过！"
		 */
		public static final String TYPE_ICON_SUC = "7";
		/**
		 * 1, "8", " 您提交的的头像认证信息未通过认证，请重新提交！"
		 */
		public static final String TYPE_ICON_FAIL = "8";
	}
	/**
	 * 车辆相关消息类型细分
	 */
	public static class TruckMsgType {
		/**
		 * 1:车辆认证通过  司机 
		 */
		public static final String TYPE_TRUCK_SUC = "1";
		/**
		 * 2:车辆认证失败 司机 
		 */
		public static final String TYPE_TRUCK_FAIL = "2";
		/**
		 * 3:货主邀请司机     司机 
		 */
		public static final String TYPE_SHIPPER = "3";
	}
	
	
	
	
	/**
	 * 订单相关消息类型细分
	 */
	public static class OrderMsgType {
		/**
		 * 1:城内订单超时   货主
		 */
		public static final String TYPE_ORDER_INCITY_OVERTIME = "1";
		/**
		 * 2:城际订单超时  货主
		 */
		public static final String TYPE_ORDER_OUTCITY_OVERTIME = "2";
		/**
		 * 3:发布货源后推送     司机 
		 */
		public static final String TYPE_AFTER_ENTRY = "3";
		/**  
		 * 4:指派司机成交   司机
		 */
		public static final String TYPE_ASSIGN_DRIVER_TO_DEAL = "4";
		/**
		 * 5:取消司机指派 司机
		 */
		public static final String TYPE_CANCEL_DRIVER_ASSIGN = "5";
		/**
		 * 6:退款通知  司机/货主
		 * 修改 tsj-004 2017年6月26日15:11:22 发起退款
		 */
		public static final String TYPE_REFUNDS = "6";
		/**
		 * 7:司机确认承接 货主
		 */
		public static final String TYPE_TO_UNDERTAKE = "7";
		/**
		 * 8:司机提货  货主
		 */
		public static final String TYPE_DELIVERGOODS = "8";
		/**
		 * 9:订单签收  货主 司机
		 */
		public static final String TYPE_ORDER_SIGN = "9";
		/**
		 * 10:订单评价   货主 司机
		 */
		public static final String TYPE_ORDER_COMPARE = "10";
		/**
		 * 11:司机超时未承接   货主
		 */
		public static final String TYPE_NO_UNDERTAKE = "11";
		/**
		 * 12:司机放弃订单   货主
		 */
		public static final String TYPE_GIVEUP_ORDER = "12";
		
		
		
		/**
		 * 13:司机确认承接(运费托管)   货主
		 */
		public static final String TYPE_CHENGJIE_ORDER = "13";
		
		
		/**
		 * 14:对方拒绝退款
		 */
		public static final String TYPE_TUIKUAN_ORDER = "14";


		/**
		 * 15:客服已经审核并撤销了您的退款
		 */
		public static final String TYPE_CHEXIAO_TUIKUAN_ORDER = "15";

		/**
		 * 司机竞价推送（您的货源（上海徐汇到天津武清）有司机报价XXX元，请前去查看！）
		 * 货主
		 *
		 */
		public static final String ORDER_BIDDINGORDER = "16";

		/**
		 * 18:自动退款前三天提醒
		 */
		public static final String AUTOMATIC_REFUND_BEFORE_3DAYS = "18";

		/**
		 * 19:同意退款
		 */
		public static final String AGREES_REFUND = "19";

		/**
		 * 20:关闭退款
		 */
		public static final String CLOSE_REFUND = "20";

		/**
		 * 21:自动退款退款通知退款方
		 * LAUNCH 发起者
		 */
		public static final String AUTOMATIC_REFUND_2LAUNCH = "21";

		/**
		 * 22:自动退款退款通知被退款方
		 * ACCEPT 接受者
		 */
		public static final String AUTOMATIC_REFUND_2ACCEPT = "22";

		/**
		 * 23:自动退款退款通知被退款方
		 * ACCEPT 接受者
		 */
		public static final String AUTOMATIC_REFUND_2ACCEPT_2 = "23";
	}
	//语音播报的内容：您的560货源有司机报价了，请及时查看！ 2分钟外播报下一个
	public  static final long ORDER_BIDDINGORDER_time=120000;
	
	/**
	 * 钱包相关消息类型细分
	 */
	public static class WalletMsgType {
		/**
		 * 1:余额变动    货主 司机
		 */
		public static final String TYPE_WALLET_CHANGE = "1";
	}
	
	/**
	 * 其他相关消息类型细分
	 */
	public static class QitaMsgType {
		/**
		 * 1:系统升级  app
		 */
		public static final String TYPE_Qita_APP_update = "1";
		
	}


	/**
	 * 公告相关消息类型细分
	 */
	public static class AnnouncementMsgType {
		/**
		 * 1:公告  发布最新公告  货主 司机
		 */
		public static final String TYPE_Announcement_APP = "1";

	}



}
