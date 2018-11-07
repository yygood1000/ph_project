package com.topjet.common.widget.banner;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/8/14.
 * <p>
 * BannerView 自定义适配器基类
 */

public abstract class BannerBaseAdapter<T> extends PagerAdapter {
    private Context mContext;
    private List<T> mDatas = new ArrayList<>();
    private View mConvertView;
    private BannerView mBannerView;

    BannerBaseAdapter(Context context) {
        this.mContext = context;
    }

    // 绑定视图和数据
    protected abstract int getLayoutResID();

    protected abstract void convert(View convertView, T data);

    /**
     * 必须实现
     */
    @Override
    public int getCount() {
        return mDatas == null || mDatas.size() == 0 ? 0 : Integer.MAX_VALUE;
    }

    /**
     * 必须实现
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 必须实现，销毁
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 必须实现，实例化
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
        mConvertView = LayoutInflater.from(mContext).inflate(getLayoutResID(), null);
        mConvertView.setClickable(true);

        if (!ListUtils.isEmpty(mDatas)) {
            position = position % mDatas.size();// 取余，取得当前显示数据的下标
        }

        if (mDatas != null) {
            // 处理视图和数据
            convert(mConvertView, getItem(position));
        }
//        // 处理条目的触摸事件
//        mConvertView.setOnTouchListener(onTouchListener);
//        // 设置点击事件
//        mConvertView.setOnClickListener(clickListener);

        container.addView(mConvertView);
        return mConvertView;
    }


//    /**
//     * 触摸事件
//     */
//    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                case MotionEvent.ACTION_MOVE:
//                case MotionEvent.ACTION_UP:
//                        break;
//            }
//            return false;
//        }
//    };

    /**
     * ==========================API============================
     * 将BannerView 赋值,用于通知BannerView 开始轮播
     */
    void setBannerView(BannerView bannerView) {
        this.mBannerView = bannerView;
    }

    /**
     * 通过该方法设置数据并显示
     */
    public void setData(List<T> datas) {
        if (datas == null) {
            return;
        }
        if (ListUtils.isEmpty(mDatas)) {
            this.mDatas = new ArrayList<>(datas);
        } else {
            this.mDatas.clear();
            this.mDatas.addAll(datas);
        }

        notifyDataSetChanged();

        if (mBannerView != null) {
            // 设置BannerView 的显示
            mBannerView.resetCurrentPosition(datas.size());
        }
    }

    /**
     * 设置BannerView 中的TextView
     */
    protected BannerBaseAdapter setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置BannerView 中的图片
     */
    void setImage(int viewId, String url, String key, int errorImg, int loadingImg) {
        ImageView iv = getView(viewId);
        if (StringUtils.isEmpty(key)) {
            GlideUtils.loaderImageResource(R.drawable.iv_banner_loading, iv);
        } else {
            GlideUtils.loaderImageRound(mContext, url, key, errorImg, loadingImg, iv, 5);
        }
    }

    /**
     * 从父布局中获取子View
     */
    protected <K extends View> K getView(@IdRes int id) {
        return (K) mConvertView.findViewById(id);
    }

    /**
     * 获取当前显示的数据类
     */
    public T getItem(int position) {
        return position >= mDatas.size() ? mDatas.get(0) : mDatas.get(position);
    }

    /**
     * 获取当前显示的整体布局
     */
    protected View getItemView() {
        return mConvertView;
    }

    /**
     * 获取显示的数据集合的长度
     */
    public int getDataSize() {
        return mDatas == null ? 0 : mDatas.size();
    }

}
