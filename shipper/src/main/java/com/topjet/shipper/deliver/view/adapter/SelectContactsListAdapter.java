package com.topjet.shipper.deliver.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.shipper.R;

import java.util.List;

/**
 * 选择常用联系人适配器
 * Created by tsj004 on 2017/8/25.
 */

public class SelectContactsListAdapter extends BaseQuickAdapter<UsualContactListItem, BaseViewHolder> {
    private Context context;

    @Override
    protected void convert(BaseViewHolder viewHolder, final UsualContactListItem item) {
        if (item == null)
            return;

        ImageView iv = viewHolder.getView(R.id.iv_listitem_head);
        GlideUtils.loaderImageCircle(context, item.getIcon_url(), item.getIcon_key(), R.drawable.icon_user_nor, R.drawable.icon_user_nor, iv);
        viewHolder.setText(R.id.tv_listitem_username, item.getContacts_name());
        viewHolder.setText(R.id.tv_listitem_mobile, item.getContacts_mobile());
        viewHolder.setText(R.id.tv_listitem_address, item.getContacts_address());
        TextView tv_select = viewHolder.getView(R.id.tv_select);

        if (item.isCheck()) {
            tv_select.setTextColor(context.getResources().getColor(R.color.white));
            tv_select.setBackgroundResource(R.drawable.shape_btn_bg_smal_green);
        } else {
            tv_select.setTextColor(context.getResources().getColor(R.color.v3_common_green));
            tv_select.setBackgroundResource(R.drawable.shape_bg_btn_border_smal_green);

        }

        viewHolder.getView(R.id.tv_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectContactClick != null) {
                    selectContactClick.selectClick(item);
                }
            }
        });
    }

    public void setSelectContactClick(SelectContactClick selectContactClick) {
        this.selectContactClick = selectContactClick;
    }

    private SelectContactClick selectContactClick;

    public interface SelectContactClick{
        void selectClick(UsualContactListItem item);
    }

    public SelectContactsListAdapter(Context context, List<UsualContactListItem> list) {
        super(R.layout.listitem_select_contacts, list);
        this.context = context;
    }

}
