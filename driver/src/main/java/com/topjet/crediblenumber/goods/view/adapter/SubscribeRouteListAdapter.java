package com.topjet.crediblenumber.goods.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.bean.SubscribeRouteListItem;

import java.util.ArrayList;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 订阅路线 适配器
 */

public class SubscribeRouteListAdapter extends BaseQuickAdapter<SubscribeRouteListItem, BaseViewHolder> {
    private boolean isEditing = false;
    private OnJumpToGoodsListListener onJumpToGoodsListListener;

    // 被选中要删除的订阅路线ID
    private ArrayList<String> deleteRouteIds = new ArrayList<>();
    private ArrayList<SubscribeRouteListItem> deleteRouteBeanList = new ArrayList<SubscribeRouteListItem>();

    public interface OnJumpToGoodsListListener {
        void jump(boolean isAllRoute, String departCityName, String destinationCityName, String subscribeLineId);
    }

    public SubscribeRouteListAdapter(Context context, OnJumpToGoodsListListener onJumpToGoodsListListener) {
        super(R.layout.listitem_subscribe_route_child);
        this.mContext = context;
        this.onJumpToGoodsListListener = onJumpToGoodsListListener;
    }


    @Override
    protected void convert(final BaseViewHolder viewHolder, final SubscribeRouteListItem item) {
        if (item.getIsAllRoute()) {// 全部路线,不会出现展示在编辑状态。
            Logger.i("oye", "getIsAllRoute");
            viewHolder.setVisible(R.id.rl_all_route, true);
            viewHolder.setVisible(R.id.rl_child_route, false);
            // 数量文本框相关设置
            setCountTextView(item.getAll_count(), viewHolder);

        } else {// 子订阅路线
            viewHolder.setVisible(R.id.rl_all_route, false);
            viewHolder.setVisible(R.id.rl_child_route, true);


            // 设置地址
            viewHolder.setText(R.id.tv_depart_address, item.getDepart_city());
            viewHolder.setText(R.id.tv_destation_address, item.getDestination_city());
            // 车型车长
            if (StringUtils.isNotBlank(item.getTruck_type_length())) {
                viewHolder.setVisible(R.id.tv_truck_type_and_length, true);
                viewHolder.setText(R.id.tv_truck_type_and_length, item.getTruck_type_length());
            } else {
                viewHolder.setVisible(R.id.tv_truck_type_and_length, false);
            }

            if (isEditing) {//处于编辑状态
                // 隐藏数量文本框
                viewHolder.setVisible(R.id.tv_goods_count, false);
                // 显示CheckBox
                viewHolder.setVisible(R.id.cb_select, true);
                // 设置选中状态
                viewHolder.setChecked(R.id.cb_select, item.isSeleced());
                CheckBox checkBox = viewHolder.getView(R.id.cb_select);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        check(viewHolder.getLayoutPosition(), isChecked);
                    }
                });
            } else {//不处于编辑状态
                // 展示数量文本框
                setCountTextView(item.getSupply_of_goods_count(), viewHolder);
                // 隐藏CheckBox
                viewHolder.setVisible(R.id.cb_select, false);
            }
        }

        // Item项点击事件
        RelativeLayout rlRoot = viewHolder.getView(R.id.rl_root);
        rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    check(viewHolder.getLayoutPosition(), !item.isSeleced());
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(viewHolder.getLayoutPosition());
                        }
                    });
                } else {
                    if (onJumpToGoodsListListener != null) {
                        if (viewHolder.getLayoutPosition() == 0) {
                            onJumpToGoodsListListener.jump(true, item.getDepart_city(), item.getDestination_city(),
                                    "");
                        } else {
                            onJumpToGoodsListListener.jump(false, item.getDepart_city(), item.getDestination_city(),
                                    item.getSubscribe_line_id());
                        }
                    }
                }
            }
        });
    }

    /**
     * 选中操作
     */
    private void check(int position, boolean isCheck) {
        SubscribeRouteListItem item = getItem(position);
        if (position >= 0 && position < getItemCount() && item != null) {
            item.setSeleced(isCheck);
            if (isCheck) {
                if (!deleteRouteBeanList.contains(item)) {
                    deleteRouteBeanList.add(item);
                }
            } else {
                if (deleteRouteBeanList.contains(item)) {
                    deleteRouteBeanList.remove(item);
                }
            }
        }
    }


    /**
     * 设置货源数量文本框
     */
    private void setCountTextView(String count, BaseViewHolder viewHolder) {
        if (StringUtils.isNotBlank(count)) {
            viewHolder.setVisible(R.id.tv_goods_count, true);
            viewHolder.setText(R.id.tv_goods_count, count + "");
        } else {
            viewHolder.setVisible(R.id.tv_goods_count, false);
        }
    }

    /**
     * 设置是否进入编辑状态
     */
    public void setEditing(boolean editing) {
        isEditing = editing;
        notifyDataSetChanged();
    }

    public ArrayList<String> getDeleteRouteIds() {
        deleteRouteIds.clear();
        Logger.i("oye", " deleteRouteBeanList = " + deleteRouteBeanList.toString());
        for (SubscribeRouteListItem item : deleteRouteBeanList) {
            deleteRouteIds.add(item.getSubscribe_line_id());
        }
        Logger.i("oye", " deleteRouteIds = " + deleteRouteIds.toString());
        return deleteRouteIds;
    }

    /**
     * 删除订阅路线成功，清空删除集合
     */
    public void clearDeleteRouteIdsList() {
        deleteRouteIds.clear();
    }
}
