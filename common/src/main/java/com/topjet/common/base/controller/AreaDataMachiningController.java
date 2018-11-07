package com.topjet.common.base.controller;


import com.topjet.common.order_detail.modle.extra.CityAndLocationExtra;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.StringUtils;

/**
 * Created by tsj-004 on 2017/9/5.
 * 城市选择器返回的数据处理
 */

public class AreaDataMachiningController {

    /**
     * 城市选择器的数据转成可在页面间传递的CityAndLocationExtra
     */
    public static CityAndLocationExtra machining(AreaSelectedData areaSelectedData) {
        CityAndLocationExtra extra = null;
        if (areaSelectedData != null) {
            AreaInfo areaInfo = areaSelectedData.getAreaInfo();
            if (areaInfo != null) {
                extra = new CityAndLocationExtra();
                CityItem third = areaInfo.getThirdLevel();
                CityItem second = areaInfo.getSecondLevel();
                CityItem first = areaInfo.getFirstLevel();
                if (third != null) {
                    setValues(extra, third);
                    if (second != null) {
                        extra.setCityName(second.getCityName());
                        extra.setBackwards2Name(AreaDataDict.replaceCityAndProvinceString(StringUtils.appendWithSpace(second.getCityFullName(), third.getCityFullName())));
//                        extra.setAddress(StringUtils.appendWithSpace(second.getCityName(), third.getCityName()));
                    }
                } else if (second != null) {
                    setValues(extra, second);
                    extra.setCityName(second.getCityName());
                    if (first != null) {
                        extra.setBackwards2Name(AreaDataDict.replaceCityAndProvinceString(StringUtils.appendWithSpace(first.getCityFullName(), second.getCityFullName())));
//                        extra.setAddress(StringUtils.appendWithSpace(first.getCityName(), second.getCityName()));
                    }
                } else if (first != null) {
                    setValues(extra, first);
                }
            }
        }
        return extra;
    }

    /**
     * 设置参数
     */
    private static void setValues(CityAndLocationExtra extra, CityItem cityItem) {
        extra.setLatitude(cityItem.getLatitude());
        extra.setLongitude(cityItem.getLongitude());
        extra.setAdCode(cityItem.getAdcode());
        extra.setCityCode(cityItem.getCitycode());
//        extra.setCityName(cityItem.getCityName());
//        extra.setAddress(cityItem.getCityName());
        extra.setLastName(cityItem.getCityName());
    }
}
