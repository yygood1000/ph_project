package com.topjet.common.common.view.adapter;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.utils.TimeUtils;

/**
 * Created by yy on 2017/8/15.
 * <p>
 * 首页底部滑动按钮组 ViewPager 适配器
 */

public class WeatherAdapter extends BaseQuickAdapter<LocalDayWeatherForecast, BaseViewHolder> {
    public WeatherAdapter() {
        super(R.layout.listitem_weather);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalDayWeatherForecast item) {
        long millis = TimeUtils.getMillis(item.getDate(), "yyyy-MM-dd", false);
        String week = null;
        switch (Integer.valueOf(item.getWeek())) {
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;
            case 7:
                week = "周日";
                break;
            default:
                break;
        }
        String time = TimeUtils.getFormatDate(millis, "MM/dd") + week;
        helper.setText(R.id.tv_time, time);
        helper.setText(R.id.tv_temp, String.format("%-3s-%3s", item.getNightTemp(), item.getDayTemp() + "°"));
        helper.setText(R.id.tv_wind, item.getDayWindDirection() + "风：" + item.getDayWindPower() + "级");
    }
}
