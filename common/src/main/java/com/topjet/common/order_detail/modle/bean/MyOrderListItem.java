package com.topjet.common.order_detail.modle.bean;

import android.text.TextUtils;

import com.topjet.common.R;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * Created by yy on 2017/8/24.
 * <p>
 * 我的订单 列表信息实体类
 */

public class MyOrderListItem {

    // 共有
    private String goods_id;//货源ID
    private String order_id;//订单ID
    private String freight_fee;//运费总金额
    private String is_freight_fee_managed;//	是否托管	0否1是
    private String order_status;//订单状态	1.货主取消交易 2.货主确认成交 3.待支付定金 4.待支付运费 5.提货中 6.承运中 7.已承运
    private String agency_fee;//定金金额
    private String order_version;//订单version

    // 司机列表返回参数
    private String owner_icon_key;//货主头像Key
    private String owner_icon_url;//货主头像Url
    private String owner_name;//货主姓名
    private String owner_id;    // 货主id	是	String
    private String owner_mobile;//货主手机号
    private String owner_comment_level;//货主发货诚信
    private String order_update_time;//订单修改时间，发布订单时间
    private String goods_update_time; // 分享货源中带的货源更新时间
    private String freight_feeStatus;//是否托管
    private String pickup_start_time;//提货时间
    private String pickup_code;// 提货码
    private OrderUserInfo sender_info;//发货人信息
    private OrderUserInfo receiver_info;//收货人信息
    private String delivery_fee;//到付费金额
    private String delivery_fee_status;//	到付费状态

    // 货主列表返回参数
    private String goods_status;//	货源状态
    private String goods_version;//	货源数据版本	是
    private String load_date_name;//提货时间	是
    private String pay_style;//支付方式
    private String pre_order_count;//报价数	是
    private String driver_id;//司机Id
    private String driver_name;//司机姓名
    private String driver_mobile;//司机电话
    private String driver_longitude;//经度
    private String driver_latitude;//	纬度
    private String driver_gps_detailed_address;//详细地址
    private String driver_gps_update_time;//更新时间
    private String driver_truck_id;// 车辆id
    private String license_plate_number;//车牌号
    private String depart_city;    // 出发城市	是	String
    private String depart_city_id;    // 出发城市id	是	String
    private String destination_city;    // 目的地城市	是	String
    private String destination_city_id;    // 目的地城市id	是	String
    private FreightInfo freight;// 费用信息
    private String is_assigned;//是否是定向订单	0否1是
    private ArrayList<String> truck_type_ids;  // 	订单所需的车型id集合
    private ArrayList<String> truck_length_ids;// 订单所需的车长id集合

    private String goods_size;    // 货物数量	String
    private String truck_length_type;    // 车长 车型	String

    public String getGoods_update_time() {
        return goods_update_time;
    }

    public String getGoods_size() {
        return goods_size;
    }

    public String getTruck_length_type() {
        return truck_length_type;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public FreightInfo getFreight() {
        return freight;
    }

    public String getDepart_city() {
        return depart_city;
    }

    public String getDepart_city_id() {
        return depart_city_id;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public String getDestination_city_id() {
        return destination_city_id;
    }

    private boolean check = false; // 是否选中

    public String getDriver_longitude() {
        return driver_longitude;
    }

    public String getDriver_latitude() {
        return driver_latitude;
    }

    public String getDriver_gps_detailed_address() {
        return driver_gps_detailed_address;
    }

    public String getDriver_gps_update_time() {
        return driver_gps_update_time;
    }

    public String getLicense_plate_number() {
        return license_plate_number;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getPickup_code() {
        return pickup_code;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public OrderUserInfo getSender_info() {
        return sender_info;
    }

    public OrderUserInfo getReceiver_info() {
        return receiver_info;
    }

    public String getPickup_start_time() {
        if (TextUtils.isEmpty(pickup_start_time)) {
            return "0";
        }
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(pickup_start_time);
        // 正则判断是否为全是数字
        if (m.matches()) {
            String year = (TimeUtils.getYear(Long.parseLong(pickup_start_time)) + "");
            String month = TimeUtils.getMonth(Long.parseLong(pickup_start_time)) + "";
            String hour = TimeUtils.getHour(Long.parseLong(pickup_start_time)) + "";
            return year + "-" + month + " " + hour + "点  " + ResourceUtils.getString(R.string.pick_up_good);
        } else {
            return pickup_start_time;
        }
    }

    public String getOwner_icon_key() {
        return owner_icon_key;
    }

    public String getOwner_icon_url() {
        return owner_icon_url;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public float getOwner_comment_level() {
        return StringUtils.isEmpty(owner_comment_level) ? 0f : Float.parseFloat(owner_comment_level);
    }

    public String getOrder_update_time() {
        return order_update_time;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public String getOrder_version() {
        return order_version;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getGoods_status() {
        return goods_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getPay_style() {
        return pay_style;
    }

    public double getFreight_fee() {
        return StringUtils.getDoubleToString(freight_fee);
    }

    public String getFriendlyAgencyFeeDriver() {
        if (StringUtils.isNotBlank(agency_fee + "") && StringUtils.getDoubleToString(agency_fee) > 0) {
            return "丨定金￥" + agency_fee;
        } else {
            return "";
        }
    }

    public String getFriendlyAgencyFeeShipper() {
        if (StringUtils.isNotBlank(freight.getAgency_fee() + "") && freight.getAgency_fee() > 0) {
            if (NumberFormatUtils.strToInt(order_status) > 5) {
                return "丨已收定金￥" + freight.getAgency_fee();
            } else {
                return "丨待收定金￥" + freight.getAgency_fee();
            }
        } else {
            return "";
        }
    }

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public String getGoods_version() {
        return goods_version;
    }

    public String getLoad_date_name() {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(load_date_name);
        // 正则判断是否为全是数字
        if (m.matches() && StringUtils.isNotBlank(load_date_name)) {
            TimeUtils.getFormatDate(Long.parseLong(load_date_name), TimeUtils.ORDER_LIST_TIME_PATTERN);
            return TimeUtils.getFormatDate(Long.parseLong(load_date_name), TimeUtils.ORDER_LIST_TIME_PATTERN) + "  "
                    + ResourceUtils.getString(R.string.pick_up_good);
        } else {
            return load_date_name;
        }
    }

    public String getIsFreightManaged() {
        if (is_freight_fee_managed.equals("0")) {
            return "";
        } else {
            return "托管";
        }
    }

    public String getIsFreightManagedWithLine() {
        if (is_freight_fee_managed.equals("0")) {
            return "";
        } else {
            return "丨托管";
        }
    }

    public String getPre_order_count() {
        return pre_order_count;
    }

    public boolean getIs_assigned() {
        return !TextUtils.isEmpty(is_assigned) && is_assigned.equals("1");
    }

    public int getDelivery_fee() {
        return StringUtils.getIntToString(NumberFormatUtils.removeDecimalZero(delivery_fee));
    }

    public int getDelivery_fee_status() {
        return StringUtils.getIntToString(delivery_fee_status);
    }

    public String getDriver_truck_id() {
        return driver_truck_id;
    }
}
