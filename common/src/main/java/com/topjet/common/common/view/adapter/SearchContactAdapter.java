package com.topjet.common.common.view.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.contact.model.ContactBean;
import com.topjet.common.utils.StringUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/10
 * describe: 诚信查询页查询联系人
 */

public class SearchContactAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder> {
    public SearchContactAdapter() {
        super(R.layout.listitem_seach);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ContactBean item) {
        String contact;
        if(StringUtils.isNotBlank(item.getName())){
            contact = item.getName()+" "+item.getMobile();
        }else {
            contact = item.getMobile();
        }
        helper.setText(R.id.tv_content, contact);
        helper.getView(R.id.rl_content).setClickable(true);
        helper.getView(R.id.rl_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactListener != null){
                    contactListener.addressClick(item);
                }
            }
        });
    }

    public void setContactListener(ContactListener contactListener) {
        this.contactListener = contactListener;
    }

    private ContactListener contactListener;

    public interface ContactListener {
        void addressClick(ContactBean item);
    }
}
