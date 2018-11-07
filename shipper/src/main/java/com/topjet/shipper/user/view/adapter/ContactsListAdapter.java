package com.topjet.shipper.user.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.controller.ItemSlideHelper;
import com.topjet.common.utils.DelayUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.SwipeMenuLayout;
import com.topjet.shipper.R;

import java.util.List;

/**
 * 常用联系人列表适配适
 * Created by tsj004 on 2017/8/22.
 */

public class ContactsListAdapter extends BaseQuickAdapter<UsualContactListItem, BaseViewHolder> {
    public ContactsListAdapter(Context context) {
        super(R.layout.listitem_contacts);
        mContext = context;
    }
    @Override
    protected void convert(final BaseViewHolder viewHolder, final UsualContactListItem item) {
        if(item == null) {
            return;
        }

        ImageView iv = viewHolder.getView(R.id.iv_listitem_head);
        iv.setImageResource(R.drawable.icon_user_nor);
        if(StringUtils.isNotBlank(item.getIcon_url()) &&
                StringUtils.isNotBlank(item.getIcon_key())) {
            GlideUtils.loaderImageCircle(mContext,
                    item.getIcon_url(),
                    item.getIcon_key(),
                    R.drawable.icon_user_nor,
                    R.drawable.icon_user_nor,
                    iv);
        }
        viewHolder.setText(R.id.tv_listitem_username, item.getContacts_name());
        viewHolder.setText(R.id.tv_listitem_mobile, item.getContacts_mobile());
        viewHolder.setText(R.id.tv_listitem_address, item.getContacts_city());

        viewHolder.getView(R.id.iv_listitem_editor)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactClick.editClick(item);
            }
        });
        final SwipeMenuLayout swipeMenuLayout = viewHolder.getView(R.id.sml_contacts);
        viewHolder.getView(R.id.tv_delete)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeMenuLayout.smoothClose();
                        DelayUtils.delay(300, new DelayUtils.OnListener() {
                            @Override
                            public void onListener() {
                                contactClick.deleteClick(item, viewHolder.getLayoutPosition());
                            }
                        });

                    }
                });

    }

    private ContactClick contactClick;

    public void setContactClick(ContactClick contactClick) {
        this.contactClick = contactClick;
    }

    public interface ContactClick{
        void editClick(UsualContactListItem item);
        void deleteClick(UsualContactListItem item, int position);
    }


}
