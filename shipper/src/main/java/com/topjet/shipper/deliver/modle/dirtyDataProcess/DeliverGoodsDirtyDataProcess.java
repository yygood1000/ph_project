package com.topjet.shipper.deliver.modle.dirtyDataProcess;

import com.amap.api.location.AMapLocation;
import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.resource.dict.TruckDataDict;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.shipper.deliver.modle.extra.OtherInfoExtra;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;

import java.util.Date;
import java.util.List;

/**
 * Created by tsj-004 on 2017/9/22.
 * 发货脏数据处理
 */

public class DeliverGoodsDirtyDataProcess {

    /**
     * OwnerGoodsParams转成deliverExtra
     */
    public static CityAndLocationExtra params2deliverExtra(OwnerGoodsParams params) {
        CityAndLocationExtra extra = new CityAndLocationExtra();
        extra.setName(params.getSender_name());
        extra.setPhone(params.getSender_mobile());
        extra.setAdCode(params.getDepart_city_id());
        extra.setAddress(params.getDepart_detail());
        extra.setLongitude(params.getDepart_lng());
        extra.setLatitude(params.getDepart_lat());

        /**
         * 根据adcode去本地找 最后两级的名字  比如 "上海 徐汇"
         * 不要问我为什么，因为服务器没有返回这个字段，必须自己找
         * 不然进入AddressInformationActivity你咋填充  所在区域
         */
        CityItem cityItem = AreaDataDict.getCityItemByAdcode(params.getDepart_city_id());
        if (cityItem != null) {
            CityItem parentItem = AreaDataDict.getCityItemByAdcode(cityItem.getParentId());
            if (cityItem != null && parentItem != null && cityItem.getCityName() != null && parentItem.getCityName() != null) {
                extra.setBackwards2Name(StringUtils.appendWithSpace(parentItem.getCityName(), cityItem.getCityName()));
            } else if (cityItem != null && cityItem.getCityName() != null) {
                extra.setBackwards2Name(cityItem.getCityName());
            }
        }

        return extra;
    }

    /**
     * OwnerGoodsParams转成receiptExtra
     */
    public static CityAndLocationExtra params2receiptExtra(OwnerGoodsParams params) {
        CityAndLocationExtra extra = new CityAndLocationExtra();
        extra.setName(params.getReceiver_name());
        extra.setPhone(params.getReceiver_mobile());
        extra.setAdCode(params.getDestination_city_id());
        extra.setAddress(params.getDestination_detail());
        extra.setLongitude(params.getDestination_lng());
        extra.setLatitude(params.getDestination_lat());

        /**
         * 根据adcode去本地找 最后两级的名字  比如 "上海 徐汇"
         * 不要问我为什么，因为服务器没有返回这个字段，必须自己找
         * 不然进入AddressInformationActivity你咋填充  所在区域
         */
        CityItem cityItem = AreaDataDict.getCityItemByAdcode(params.getDestination_city_id());
        CityItem parentItem = AreaDataDict.getCityItemByAdcode(cityItem.getParentId());
        if (cityItem != null && parentItem != null && cityItem.getCityName() != null && parentItem.getCityName() != null) {
            extra.setBackwards2Name(StringUtils.appendWithSpace(parentItem.getCityName(), cityItem.getCityName()));
        } else if (cityItem != null && cityItem.getCityName() != null) {
            extra.setBackwards2Name(cityItem.getCityName());
        }

        return extra;
    }

    /**
     * OwnerGoodsParams转成otherInfoExtra
     */
    public static OtherInfoExtra params2otherInfoExtra(OwnerGoodsParams params) {
        OtherInfoExtra extra = new OtherInfoExtra();
        extra.setType(params.getCategory());
        extra.setPack(params.getPack_type());
        extra.setLoad(params.getLoad_type());
        extra.setRemark(params.getText_remark());
        extra.setPhoto(params.getPhoto_remark());
        String isRefreStr = params.getFlush_num();
        if (isRefreStr.equals("1")) {
            extra.setRefre(true);
        } else {
            extra.setRefre(false);
        }
        return extra;
    }

    /**
     * 获取整车/非整车文字
     */
    public static String getIsCarpoolStr(OwnerGoodsParams params) {
        String is_carpool = getIsCarpoolId(params);
        if (is_carpool.equals("0")) {  // 0 : 不可拼车/整车 1: 可拼车
            is_carpool = "整车";
        } else {
            is_carpool = "可拼车";
        }
        return is_carpool;
    }

    /**
     * 获取整车/非整车ID
     */
    public static String getIsCarpoolId(OwnerGoodsParams params) {
        String is_carpoolId = params.getIs_carpool();
        if (StringUtils.isEmpty(is_carpoolId)) {
            is_carpoolId = "0";
        }
        return is_carpoolId;
    }

    /**
     * 获取车型列表文字  空格分割
     */
    public String getTypeStr(OwnerGoodsParams params) {
        List<String> types = params.getTruck_type_ids();
        if (types == null) {
            return "";
        }
        return getTypeStr(types);
    }

    /**
     * 获取车型列表文字  空格分割
     */
    public static String getTypeStr(List<String> types) {
        String tempTypeStr = "";
        if (types != null) {
            for (int i = 0; i < types.size(); i++) {
                tempTypeStr = StringUtils.appendWithSpace(tempTypeStr, TruckDataDict.getTruckTypeById(types.get(i)).getDisplayName());
            }
        }
        return tempTypeStr;
    }

    /**
     * 获取车长列表文字  空格分割
     */
    public static String getLengthStr(OwnerGoodsParams params) {
        List<String> lengths = params.getTruck_length_ids();
        if (lengths == null) {
            return "";
        }
        return getLengthStr(lengths);
    }

    /**
     * 获取车长列表文字  空格分割
     */
    public static String getLengthStr(List<String> lengths) {
        String tempLengthStr = "";
        if (lengths != null) {
            for (int i = 0; i < lengths.size(); i++) {
                tempLengthStr = StringUtils.appendWithSpace(tempLengthStr, TruckDataDict.getTruckLengthById(lengths.get(i)).getDisplayName());
            }
        }
        return tempLengthStr;
    }

    /**
     * 获取车型、车长、整车 文字   空格分割
     */
    public static String getTypeLengthPoolStr(String is_carpool, String tempTypeStr, String tempLengthStr) {
        return StringUtils.appendWithSpace(is_carpool, tempTypeStr, tempLengthStr);
    }

    /**
     * 根据 loadDateType 获取 loadDate 文本
     * 0:具体时间 1:今定今装 2:今定明装 3:随到随走
     */
    public static String getLoadDateText(String loadDateType, String loadDate) {
        if (loadDateType.equals("0")) {
            loadDate = TimeUtils.getFormatDate(new Date(Long.parseLong(loadDate)), TimeUtils.OFTEN_GOODS_LIST_TIME_PATTERN);
        } else if (loadDateType.equals("1")) {
            loadDate = "今定今装";
        } else if (loadDateType.equals("2")) {
            loadDate = "今定明装";
        } else if (loadDateType.equals("3")) {
            loadDate = "随到随走";
        }
        return loadDate;
    }


    /**
     * AMapLocation转换成CityItem
     */
    public static CityItem aMapLocation2CityItem(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getCity() != null) {
            return new CityItem(aMapLocation.getCity(), aMapLocation.getCityCode(), aMapLocation.getAdCode(), aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "");
        }
        return null;
    }

    /**
     * CityAndLocationExtra转换成CityItem
     */
    public static CityItem cityAndLocationExtra2CityItem(CityAndLocationExtra extra) {
        if (extra != null && extra.getAdCode() != null) {
            return new CityItem(extra.getCityName(), extra.getCityCode(), extra.getAdCode(), extra.getLatitude() + "", extra.getLongitude() + "");
        }
        return null;
    }

    /**
     * AMapLocation转换成CityAndLocationExtra
     */
    public static CityAndLocationExtra aMapLocation2CityAndLocationExtra(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getCity() != null) {
            String backwards2Name = StringUtils.appendWithSpace(aMapLocation.getCity(), aMapLocation.getDistrict());
            CityAndLocationExtra cityAndLocationExtra = new CityAndLocationExtra();
            cityAndLocationExtra.setCityName(aMapLocation.getCity());
            cityAndLocationExtra.setCityCode(aMapLocation.getCityCode());
            cityAndLocationExtra.setAdCode(aMapLocation.getAdCode());
            String address = aMapLocation.getAddress();
            if (StringUtils.isNotBlank(address)) {
                address = address.replace(aMapLocation.getCity(), "").replace(aMapLocation.getProvince(), "").replace(aMapLocation.getDistrict(), "");
            }
            cityAndLocationExtra.setAddress(address);
            cityAndLocationExtra.setBackwards2Name(backwards2Name);
            cityAndLocationExtra.setLatitude(aMapLocation.getLatitude() + "");
            cityAndLocationExtra.setLongitude(aMapLocation.getLongitude() + "");
            return cityAndLocationExtra;
        }
        return null;
    }

    /**
     * UsualContactListItem转换成CityAndLocationExtra
     */
    public static CityAndLocationExtra usualContactListItem2CityAndLocationExtra(UsualContactListItem item) {
        if (item != null && item.getContacts_city_code() != null) {
            CityAndLocationExtra cityAndLocationExtra = new CityAndLocationExtra();
            cityAndLocationExtra.setCityName(item.getContacts_city());
            cityAndLocationExtra.setAddress(item.getContacts_address());
            cityAndLocationExtra.setLatitude(item.getLatitude());
            cityAndLocationExtra.setLongitude(item.getLongitude());
            cityAndLocationExtra.setAdCode(item.getContacts_city_code());
            return cityAndLocationExtra;
        }
        return null;
    }
}