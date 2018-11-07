package com.topjet.common.common.presenter;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.common.view.activity.WeatherView;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Toaster;

/**
 * Created by yy on 2017/10/12.
 * 本地天气
 */

public class WeatherPresenter extends BasePresenter<WeatherView> implements WeatherSearch.OnWeatherSearchListener {
    public String adCode;

    public WeatherPresenter(WeatherView mView, Context mContext) {
        super(mView, mContext);
    }


    /**
     * 定位当前位置，这里需要添加
     */
    public void getLocate() {
        mActivity.showLoadingDialog();
        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    adCode = aMapLocation.getAdCode();
                    mView.setDepart(AreaDataDict.getLastCityNameByLocation(aMapLocation));
                } else {
                    Toaster.showShortToast("定位失败");
                    adCode = AreaDataDict.DEFAULT_CITY_CODE;
                    mView.setDepart(AreaDataDict.DEFAULT_CITY_NAME);
                }
                getLiveWeatherInfo();
                getForecastWeatherInfo();
                mActivity.cancelShowLoadingDialog();
            }

            // 定位权限获取失败
            @Override
            public void onLocationPermissionfaild() {
                AutoDialogHelper.showSettingLocationPermission(mActivity).show();
                mActivity.cancelShowLoadingDialog();
            }
        });
    }

    /**
     * 获取当前城市天气信息
     */
    public void getLiveWeatherInfo() {
        WeatherSearchQuery mquery = new WeatherSearchQuery(adCode, WeatherSearchQuery.WEATHER_TYPE_FORECAST);
        //检索参数为城市和天气类型，实时天气为1、天气预报为2
        WeatherSearch mweathersearch = new WeatherSearch(mContext);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    /**
     * 获取未来城市天气信息
     */
    public void getForecastWeatherInfo() {
        WeatherSearchQuery mquery = new WeatherSearchQuery(adCode, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        //检索参数为城市和天气类型，实时天气为1、天气预报为2
        WeatherSearch mweathersearch = new WeatherSearch(mContext);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == 1000) {// 成功
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                mView.showLiveWeather(weatherlive);
            } else {
                searchFail();
            }
        } else {
            searchFail();
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == 1000) {// 成功
            if (weatherForecastResult != null && weatherForecastResult.getForecastResult() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
                mView.showForecastWeather(weatherForecastResult.getForecastResult().getWeatherForecast());
            } else {
                searchFail();
            }
        } else {
            searchFail();
        }
    }

    private void searchFail() {
        mView.searchFail();
        mView.showToast("天气预报查询失败");
    }
}
