package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.hyphenate.easeui.R;

import java.util.List;

/**
 * Created by tsj028 on 2017/8/1 0001.
 */

public class EaseSearchListViewAdapter extends BaseAdapter {
    private Context context;
    private List<PoiItem> poiInfos;

    public EaseSearchListViewAdapter(Context context, List<PoiItem> poiInfos) {
        this.context = context;
        this.poiInfos = poiInfos;
    }

    @Override
    public int getCount() {
        return poiInfos.size();
    }

    @Override
    public PoiItem getItem(int i) {
        return poiInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.ease_row_search_list, null);
            vh = new ViewHolder();
            vh.tv = (TextView) view.findViewById(R.id.tv);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.tv.setText(poiInfos.get(i).getTitle());
        return view;
    }

    class ViewHolder {
        TextView tv;
    }
}
