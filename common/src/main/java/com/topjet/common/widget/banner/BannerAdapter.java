package com.topjet.common.widget.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.topjet.common.R;
import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.base.listener.DebounceClickListener;


/**
 * Created by yy on 2017/8/15.
 * BannerView 适配器
 */

public class BannerAdapter extends BannerBaseAdapter<BannerBean> {
    private OnPageClickListener mListener;

    public BannerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.pageitem_banner;
    }

    @Override
    protected void convert(View convertView, final BannerBean item) {

        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_banner);

        setImage(R.id.iv_banner, item.getPicture_url(), item.getPicture_key(), R.drawable.iv_banner_loading, R
                .drawable.iv_banner_loading);
        iv.setOnClickListener(new DebounceClickListener() {
            @Override
            public void performClick(View v) {
                mListener.onPageClick(item);
            }
        });
    }

    /**
     * 条目页面的点击事件
     */
    public interface OnPageClickListener {
        void onPageClick(BannerBean item);
    }

    public void setOnPageClickListener(OnPageClickListener mListener) {
        this.mListener = mListener;
    }
}
