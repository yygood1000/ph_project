package com.topjet.crediblenumber.goods.view.adapter;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.common.modle.bean.DestinationListItem;
import com.topjet.crediblenumber.R;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 目的地弹窗 适配器
 */

public class DestinationAdapter extends BaseQuickAdapter<DestinationListItem, BaseViewHolder> {

    public DestinationAdapter() {
        super(R.layout.listitem_destination);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final DestinationListItem item) {
        viewHolder.setText(R.id.tv_destination, item.getDestinationName());
        CheckBox cbDestination = viewHolder.getView(R.id.cb_destination);


        // 最后一项 手动选择项 CheckBox逻辑编写
        if (item.getIsSelectedBySelf()) {
            viewHolder.setVisible(R.id.iv_arrow_right, true);
            cbDestination.setClickable(true);
            cbDestination.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setSelected(isChecked);
                }
            });
        } else {
            cbDestination.setClickable(false);
            cbDestination.setOnCheckedChangeListener(null);
            viewHolder.setVisible(R.id.iv_arrow_right, false);
        }
        if (item.isSelected()) {
            cbDestination.setChecked(true);
        } else {
            cbDestination.setChecked(false);
        }

    }

}
