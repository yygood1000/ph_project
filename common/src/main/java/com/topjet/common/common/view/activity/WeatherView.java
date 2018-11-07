package com.topjet.common.common.view.activity;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherLive;
import com.topjet.common.base.view.activity.IView;

import java.util.List;

/**
 * 设置页面
 * Created by yy on 2017/8/7.
 */

public interface WeatherView extends IView {
    void showLiveWeather(LocalWeatherLive weatherlive);

    void showForecastWeather(List<LocalDayWeatherForecast> forecastlist);

    void setDepart(String dapart);

    void searchFail();
}
