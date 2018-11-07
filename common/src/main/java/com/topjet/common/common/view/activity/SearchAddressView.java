package com.topjet.common.common.view.activity;


import android.view.View;

import com.topjet.common.base.view.activity.IView;
import com.topjet.common.common.view.adapter.SearchAddressAdapter;

/**
 * 附近货源 搜索地址页面
 * Created by tsj004 on 2017/8/14.
 */

public interface SearchAddressView extends IView {

    void setAdapter(SearchAddressAdapter mAdapter);

    void setIvClearVisibility(boolean notBlank);

    void clearEditText();

    View getFootView();

}
