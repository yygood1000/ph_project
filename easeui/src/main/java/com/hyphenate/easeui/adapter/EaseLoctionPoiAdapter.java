package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.hyphenate.easeui.R;

import java.util.List;

/**
 * Created by tsj028 on 2017/7/31 0031.
 */

public class EaseLoctionPoiAdapter extends BaseAdapter {
    private Context context;
    private List<Tip> pois;
    private int selected;

    public EaseLoctionPoiAdapter(Context context, int selected, List<Tip> pois) {
        this.context = context;
        this.selected = selected;
        this.pois = pois;
    }

    public void setSelected(int selected ){
        this.selected = selected;
        notifyDataSetChanged();
    }

    public void reflushList(int selected, List<Tip> pois){
        this.selected = selected;
        this.pois = pois;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pois.size();
    }

    @Override
    public Tip getItem(int i) {
        return pois.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Tip tip = pois.get(position);
        ViewHoder vh;
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.ease_row_location_poi_list,null);
            vh = new ViewHoder();
            vh.iv_select = (ImageView) view.findViewById(R.id.iv_select);
            vh.tv_title = (TextView) view.findViewById(R.id.tv_title);
            vh.tv_message = (TextView) view.findViewById(R.id.tv_message);
            view.setTag(vh);
        }else{
            vh = (ViewHoder) view.getTag();
        }

        if (position == selected){
            vh.iv_select.setVisibility(View.VISIBLE);
        }else{
            vh.iv_select.setVisibility(View.INVISIBLE);
        }
        vh.tv_title.setText(tip.getName());
        vh.tv_message.setText(tip.getAddress());

        return view;
    }

    class ViewHoder {
        TextView tv_title, tv_message;
        ImageView iv_select;
    }
}
