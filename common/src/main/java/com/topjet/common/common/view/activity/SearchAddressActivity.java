package com.topjet.common.common.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.common.view.adapter.SearchAddressAdapter;
import com.topjet.common.common.presenter.SearchAddressPresenter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.config.CMemoryData;

/**
 * Created by yy on 2017/8/30.
 * <p>
 * 输入详细地址并搜索页面
 */

public class SearchAddressActivity extends MvpActivity<SearchAddressPresenter> implements SearchAddressView {
    private ImageView ivClear;
    private EditText etSearch;
    public RecyclerView mRecyclerView;

    @Override
    protected void initPresenter() {
        mPresenter = new SearchAddressPresenter(this, mContext);
    }

    @Override
    protected int getLayoutResId() {
        noHasTitle();
        return R.layout.activity_input_address;
    }

    @Override
    protected void initView() {
        View statusBar =  findViewById(R.id.view_statusbar);
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        ivClear = (ImageView) findViewById(R.id.iv_clear);
        TextView tvSearch = (TextView) findViewById(R.id.tv_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        ivBack.setOnClickListener(clickListener);
        ivClear.setOnClickListener(clickListener);
        tvSearch.setOnClickListener(clickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mPresenter.initAdapter();

        // 初始化搜索输入框
        mPresenter.initEditText(etSearch);

        if (!CMemoryData.isDriver()) {
            statusBar.setBackgroundResource(R.drawable.shape_bg_gradient_shipper);
        }
    }

    @Override
    protected void initData() {
        //  获取SP 中的历史记录
        mPresenter.getHistroyListData();
    }

    private DebounceClickListener clickListener = new DebounceClickListener() {
        @Override
        public void performClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_back) {
                finish();
            } else if (id == R.id.iv_clear) {
                etSearch.setText("");
                setIvClearVisibility(false);
            } else if (id == R.id.tv_search) {
                mPresenter.geocodeSearch(etSearch.getText().toString());
            }
        }
    };

    /**
     * 设置适配器
     */
    @Override
    public void setAdapter(SearchAddressAdapter mAdapter) {
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 设置清空按钮是否可见
     */
    @Override
    public void setIvClearVisibility(boolean isVisible) {
        if (isVisible) {
            ivClear.setVisibility(View.VISIBLE);
        } else {
            ivClear.setVisibility(View.GONE);
        }
    }

    /**
     * 清空输入框
     */
    @Override
    public void clearEditText() {
        etSearch.setText("");
    }

    /**
     * 获取脚布局
     */
    @Override
    public View getFootView() {
        View view = getLayoutInflater().inflate(R.layout.layout_search_address_footer, (ViewGroup)
                mRecyclerView.getParent(), false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.clearHistroy();
            }
        });
        return view;
    }
}
