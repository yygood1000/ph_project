package com.topjet.crediblenumber.order.view.adapter;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.order_detail.modle.bean.MyOrderListItem;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.R;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2018/1/15
 * describe: 分享货源列表适配器
 */

public class ShareGoodsListAdapter extends BaseQuickAdapter<MyOrderListItem, BaseViewHolder> {
    private List<MyOrderListItem> mCheckDate = new ArrayList<>(); // 已选择的数据id

    public ShareGoodsListAdapter() {
        super(R.layout.list_item_share_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderListItem item) {
        setShareDisplay(helper, item);
    }
    /**
     * 分享货源数据绑定
     *
     * @param viewHolder 视图
     * @param item       数据
     */
    private void setShareDisplay(final BaseViewHolder viewHolder, final MyOrderListItem item) {
        // 地址信息
        viewHolder.setText(R.id.tv_depart_address, item.getDepart_city());
        viewHolder.setText(R.id.tv_destination_address, item.getDestination_city());
        // 订单更新时间
        viewHolder.setText(R.id.tv_delivery_time, TimeUtils.displayTime(item.getGoods_update_time()));
        // 设置货物信息
        viewHolder.setText(R.id.tv_order_info, item.getGoods_size() + " " + item.getTruck_length_type());
        // 选择框
        final CheckBox checkBox = viewHolder.getView(R.id.cb_order);
        // cb_order
        checkBox.setChecked(item.isCheck());
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Logger.d("选中 " + isChecked + " " + viewHolder.getLayoutPosition() + " ");
//                if(mCheckDate.size() > 9 && isChecked) {
//                    buttonView.setChecked(false);
//                }else {
//                    check(viewHolder.getLayoutPosition(), isChecked);
//                }
//
//
//            }
//        });
        // 设置Item项点击事件
        viewHolder.getView(R.id.rl_order_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("选中 " + item.isCheck() + " " + viewHolder.getLayoutPosition() + " ");
                check(viewHolder.getLayoutPosition(), !item.isCheck());
            }
        });
    }

    /**
     * 选中操作
     *
     * @param position
     */
    public void check(final int position, boolean check) {
        if(mCheckDate.size() > 9 && check){
            // 最多分享10条
            Toaster.showLongToast(mContext.getString(R.string.share_max));
            return;
        }
        if (position >= 0 &&
                position < getItemCount() &&
                getItem(position) != null) {
            getItem(position).setCheck(check);
            if (check) {
                if (!mCheckDate.contains(getItem(position))) {
                    mCheckDate.add(getItem(position));
                }
            } else {
                if (mCheckDate.contains(getItem(position))) {
                    mCheckDate.remove(getItem(position));
                }
            }
            Logger.d("选中2 " + mCheckDate.size());
            // 将个数传递给页面，显示按钮的是否可点击状态
            if (onCheckListener != null) {
                onCheckListener.checkNum(mCheckDate.size());
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position);
                    }
                });
            }
        }

    }


    /**
     * 获取选中的list
     *
     * @return 选中的货源id
     */
    public List<String> getCheckIds() {
        List<String> ids = new ArrayList<>();
        for (MyOrderListItem item : mCheckDate) {
            ids.add(item.getGoods_id());
        }
        return ids;
    }

    public List<MyOrderListItem> getCheckDate() {
        return mCheckDate;
    }

    public void setCheckDate(List<MyOrderListItem> mCheckDate) {
        this.mCheckDate = mCheckDate;
    }


    // 分享选中监听
    public interface OnCheckListener {
        // 选中个数回调
        void checkNum(int num);
    }

    private OnCheckListener onCheckListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

}
