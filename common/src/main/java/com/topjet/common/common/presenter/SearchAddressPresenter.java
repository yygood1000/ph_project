package com.topjet.common.common.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.modle.bean.SearchAddressListItem;
import com.topjet.common.common.modle.bean.SearchAddressResultExtra;
import com.topjet.common.common.view.activity.SearchAddressView;
import com.topjet.common.common.view.adapter.SearchAddressAdapter;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 附近货源/车源 搜索地址页面
 */

public class SearchAddressPresenter extends BasePresenter<SearchAddressView> {
    private SearchAddressAdapter mAdapter;// 适配器
    private ArrayList<SearchAddressListItem> histroyListData = new ArrayList<>();
    private ArrayList<SearchAddressListItem> listData = new ArrayList<>();
    private boolean isShowHistroy = true;

    public SearchAddressPresenter(SearchAddressView mView, Context mContext) {
        super(mView, mContext);
    }

    /**
     * 初始化适配器
     */
    public void initAdapter() {
        mAdapter = new SearchAddressAdapter();
        // 点击了联想列表，获取数据，传回附近货源地图页面
        mAdapter.setOnItemClickListener(onItemClickListener);
        mAdapter.setEnableLoadMore(false);
        mView.setAdapter(mAdapter);
    }

    /**
     * 下拉列表Item点击事件
     */
    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (isShowHistroy) {
                if (histroyListData.get(position).getLatLonPoint() == null) {
                    geocodeSearch(histroyListData.get(position).getAddress());
                } else {
                    setResultAndFinish(histroyListData.get(position).getLatLonPoint().getLatitude(),
                            histroyListData.get(position).getLatLonPoint().getLongitude(),
                            histroyListData.get(position).getAdCode(), histroyListData.get(position).getAddress());
                }
            } else {
                // 添加到历史记录中
                addHistroy(position);

                if (listData.get(position).getLatLonPoint() == null) {
                    geocodeSearch(listData.get(position).getAddress());
                } else {
                    setResultAndFinish(listData.get(position).getLatLonPoint().getLatitude(),
                            listData.get(position).getLatLonPoint().getLongitude(),
                            listData.get(position).getAdCode(), listData.get(position).getAddress());
                }
            }
        }
    };

    /**
     * 添加历史记录
     */
    private void addHistroy(int position) {
        // 历史记录列表插入一条搜索记录
        if (histroyListData.size() > 10) {
            histroyListData.remove(histroyListData.size() - 1);
        }

        if (!histroyListData.contains(listData.get(position))) {
            histroyListData.add(0, listData.get(position).setSearch(false));
        }

        // SP中存储历史集合数据
        CPersisData.setSearchHistroy(new Gson().toJson(histroyListData));

    }


    /**
     * 初始化输入框
     */
    public void initEditText(EditText etSearch) {
        RxTextView.textChanges(etSearch)
                .debounce(200, TimeUnit.MILLISECONDS)
                .compose(mActivity.<CharSequence>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        // 设置清空按钮显示与否
                        mView.setIvClearVisibility(StringUtils.isNotBlank(charSequence.toString()));
                        // 调用高德地图 获取联想列表
                        if (StringUtils.isNotBlank(charSequence.toString())) {
                            getInputTipsList(charSequence.toString());
                        }
                    }
                });
    }

    /**
     * 获取联想列表
     */
    private void getInputTipsList(String newText) {
        LocationUtils.getInputTipsList(mContext, newText, "", new LocationUtils.onGetInputTipsListener() {
            @Override
            public void onGetInputTips(List<Tip> result) {
                // 构造下拉列表集合
                Logger.d("oye", "输入联想列表" + result.toString());
                listData.clear();
                for (int i = 0; i < result.size(); i++) {
                    if (listData.size() >= 10) {
                        return;
                    } else {
                        listData.add(new SearchAddressListItem(result.get(i).getPoint(),
                                result.get(i).getName(),
                                result.get(i).getDistrict(),
                                result.get(i).getAdcode(),
                                true));
                    }
                }
                mAdapter.removeAllFooterView();
                mAdapter.setNewData(listData);
                isShowHistroy = false;
            }

            @Override
            public void onGetInputTipsFail() {
                listData.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取历史记录
     */
    public void getHistroyListData() {
        String histroyListJson = CPersisData.getSearchHistroy();
        if (StringUtils.isNotBlank(histroyListJson)) {
            histroyListData = DataFormatFactory.jsonToArrayList(histroyListJson, SearchAddressListItem.class);
            mAdapter.setNewData(histroyListData);
            mAdapter.addFooterView(mView.getFootView());
        }
    }

    /**
     * 地理信息编码
     */
    public void geocodeSearch(String address) {
        LocationUtils.geocodeSearch(mContext, address, "", new LocationUtils
                .onGeocodeSearchedListener() {
            @Override
            public void onGeocodeSearched(GeocodeAddress result) {
                setResultAndFinish(result.getLatLonPoint().getLatitude(), result.getLatLonPoint().getLongitude(),
                        result.getAdcode(), result.getFormatAddress());
            }

            @Override
            public void onGeocodeSearchedFail() {
                Toaster.showShortToast("您所输入的地址有误，请重新输入！");
                mView.clearEditText();
            }
        });
    }

    /**
     * 设置返回数据 并关闭界面
     */
    private void setResultAndFinish(double lat, double lng, String adCode, String address) {
        SearchAddressResultExtra extra = new SearchAddressResultExtra();
        extra.setAdCode(adCode);
        extra.setLat(lat);
        extra.setLng(lng);
        extra.setFullAddress(address);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchAddressResultExtra.getExtraName(), extra);
        intent.putExtras(bundle);
        mActivity.setResult(CConstants.RESULT_CODE_AROUND_MAP_SEARCH, intent);
        mActivity.finish();
    }

    /**
     * 清空历史记录
     */
    public void clearHistroy() {
        histroyListData.clear();
        CPersisData.setSearchHistroy("");
        mAdapter.removeAllFooterView();
        mAdapter.notifyDataSetChanged();
    }
}
