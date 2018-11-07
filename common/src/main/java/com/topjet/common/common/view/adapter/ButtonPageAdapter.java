package com.topjet.common.common.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.topjet.common.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/8/15.
 * <p>
 * 首页底部滑动按钮组 ViewPager 适配器
 */

public class ButtonPageAdapter extends PagerAdapter {
    private ArrayList<View> viewList;

    public ButtonPageAdapter() {
    }

    @Override
    public int getCount() {
        return ListUtils.isEmpty(viewList) ? 0 : viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 通过该方法设置数据并显示
     */
    public void setData(List<View> datas) {
        if (datas == null) {
            return;
        }
        if (ListUtils.isEmpty(viewList)) {
            this.viewList = new ArrayList<>(datas);
        } else {
            this.viewList.clear();
            this.viewList.addAll(datas);
        }
        notifyDataSetChanged();
    }
}
