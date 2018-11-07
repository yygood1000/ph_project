package com.topjet.common.common.view.activity;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherLive;
import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.presenter.WeatherPresenter;
import com.topjet.common.common.view.adapter.WeatherAdapter;
import com.topjet.common.common.view.dialog.CitySelectPopupWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by yy on 2017/11/7.
 * <p>
 * 天气页面
 */

public class WeatherActivity extends MvpActivity<WeatherPresenter> implements WeatherView {
    @BindView(R2.id.sv_all)
    ScrollView svAll;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.ll_weather)
    LinearLayout llWeather;
    @BindView(R2.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R2.id.tv_today_temp)
    TextView tvTodayTemp;
    @BindView(R2.id.tv_today_weather)
    TextView tvTodayWeather;
    @BindView(R2.id.tv_today_wind)
    TextView tvTodayWind;
    @BindView(R2.id.tv_today_humidity)
    TextView tvTodayHumidity;

    private WeatherAdapter mAdapter;

    @Override
    protected void initPresenter() {
        mPresenter = new WeatherPresenter(this, this);
    }

    @Override
    protected int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_weather;
    }

    @Override
    protected void initView() {
        initAdapter();
    }

    @Override
    protected void initData() {
        mPresenter.getLocate();
    }

    private void initAdapter() {
        mAdapter = new WeatherAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 起点/目的地返回事件
     */
    @Subscribe
    public void onEvent(AreaSelectedData data) {
        if (data.getTag().equals(TAG)) {
            AreaInfo areaInfo = data.getAreaInfo();
            tvTitle.setText(areaInfo.getLastCityName());
            mPresenter.adCode = areaInfo.getWeatherCityId();
            mPresenter.getLiveWeatherInfo();
            mPresenter.getForecastWeatherInfo();
        }
    }

    @OnClick({R2.id.iv_back, R2.id.tv_change_city})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finishPage();
        } else if (i == R.id.tv_change_city) {
            new CitySelectPopupWindow(TAG).showCitySelectPopupWindow(this, tvTitle, false, false, true);
        }
    }

    @Override
    public void showLiveWeather(LocalWeatherLive weatherlive) {
        llWeather.setVisibility(View.VISIBLE);
        tvTodayTemp.setText(weatherlive.getTemperature() + "°");
        tvTodayWeather.setText(weatherlive.getWeather());
        tvTodayWind.setText(weatherlive.getWindDirection() + "风：" + weatherlive.getWindPower() + "级");
        tvTodayHumidity.setText("空气湿度：" + weatherlive.getHumidity() + "%");
        svAll.scrollTo(0,0);
    }

    @Override
    public void showForecastWeather(List<LocalDayWeatherForecast> forecastlist) {
        mRecyclerView.setVisibility(View.VISIBLE);
        forecastlist.remove(0);
        mAdapter.setNewData(forecastlist);
        svAll.scrollTo(0,0);
    }

    @Override
    public void setDepart(String city) {
        tvTitle.setText(city);
    }

    @Override
    public void searchFail() {
        llWeather.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
