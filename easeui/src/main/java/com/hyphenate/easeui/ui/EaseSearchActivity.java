package com.hyphenate.easeui.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseSearchListViewAdapter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsj028 on 2017/8/1 0001.
 */

public class EaseSearchActivity extends MvpActivity {
    public static final int RESULT_CODE_LOCAL = 1;
    public static final int RESULT_CODE_CANCEL = 2;
    private EaseSearchListViewAdapter searchListViewAdapter;
    private EditText et_search;
    private ListView lv_search;
    private String city;
    private TextView tv_cancel;
    private PoiSearch poiSearch;
    private PoiSearch.Query poiCitySearchOption;
    private List<PoiItem> poiInfos = new ArrayList<>();
    private boolean isCanPoiSearch;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.ease_search_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
        // 文本输入框改变监听，必须在定位完成之后
        city = getIntent().getStringExtra("city");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        et_search = (EditText) findViewById(R.id.et_search);
        lv_search = (ListView) findViewById(R.id.lv_search);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKey();
                Intent mIntent = new Intent();
                setResult(RESULT_CODE_CANCEL, mIntent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != searchListViewAdapter && searchListViewAdapter.getCount() != 0) {
                    Intent mIntent = new Intent();
                    mIntent.putExtra("latitude", searchListViewAdapter.getItem(i).getLatLonPoint().getLatitude());
                    mIntent.putExtra("longitude", searchListViewAdapter.getItem(i).getLatLonPoint().getLongitude());
                    // 设置结果，并进行传送
                    setResult(RESULT_CODE_LOCAL, mIntent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {

                }
            }
        });
        et_search.addTextChangedListener(mTextWatcher);
    }

    /**
     * 如果键盘打开，则关闭
     */
    private void closeKey() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private TextWatcher mTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {

            // 先去掉监听器，否则会出现栈溢出
            et_search.removeTextChangedListener(mTextWatcher);
            if (s.length() == 0 || "".equals(s.toString())) {
                isCanPoiSearch = false;
                lv_search.setVisibility(View.GONE);
            } else {
                isCanPoiSearch = true;

                // 城市内检索
                poiCitySearchOption = new PoiSearch.Query(s.toString(), city);
                // 创建PoiSearch实例
                poiSearch = new PoiSearch(EaseSearchActivity.this, poiCitySearchOption);
                //设置poi检索监听者
                poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult poiResult, int i) {
                        if (isCanPoiSearch) {
                            poiInfos.clear();
                            if (null != poiResult.getPois()) {
                                poiInfos = poiResult.getPois();
                            } else {
                                Logger.d("TTT", "null == poiInfos");
                            }
                            searchListViewAdapter = new EaseSearchListViewAdapter(EaseSearchActivity.this, poiInfos);
                            lv_search.setVisibility(View.VISIBLE);
                            lv_search.setAdapter(searchListViewAdapter);
                        }
                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {
                    }
                });

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            poiSearch.searchPOI();
                        } catch (AMapException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            // 恢复监听器
            et_search.addTextChangedListener(mTextWatcher);
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent mIntent = new Intent();
            setResult(RESULT_CODE_CANCEL, mIntent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != poiSearch) {
            poiSearch = null;
        }
    }
}
