package com.topjet.crediblenumber.goods.modle.bean;

import com.topjet.common.base.model.BaseEvent;
import com.topjet.common.resource.bean.CityItem;

/**
 * Created by yy on 2017/9/1.
 * 目的地选择后post出来的通知时间
 */

public class DestinationsSelectEvent extends BaseEvent {
    private CityItem departCityItem;

    public DestinationsSelectEvent(String tag) {
        setTag(tag);
    }

    public DestinationsSelectEvent(String tag, CityItem departCityItem) {
        setTag(tag);
        this.departCityItem = departCityItem;
    }

    public CityItem getDepartCityItem() {
        return departCityItem;
    }
}
