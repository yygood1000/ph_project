package com.topjet.common.contact.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.contact.ContactExtra;
import com.topjet.common.contact.model.ContactBean;
import com.topjet.common.utils.StringUtils;

import java.util.HashMap;

/**
 * creator: zhulunjun
 * time:    2017/10/16
 * describe:
 */

public class ContactAdapter extends BaseQuickAdapter<ContactBean, BaseViewHolder> {

    private boolean isShowLetter = true;
    private ContactExtra contactExtra;

    public ContactAdapter(Context context) {
        super(R.layout.listitem_contact);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ContactBean item) {

        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(helper.getPosition());
        //根据每个字母下第一个联系人在数据中的位置，来显示headView
        if (getPositionForSection(section) == helper.getPosition() && isShowLetter) {
            helper.getView(R.id.tv_letter).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_letter, item.getSortLetters());
        } else {
            helper.getView(R.id.tv_letter).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_name, item.getName());

        TextView tvBtn = helper.getView(R.id.tv_btn);
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactBtnClick != null) {
                    contactBtnClick.btnClick(item);
                }
            }
        });
        setBtnStyle(tvBtn, contactExtra);

        helper.getView(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactBtnClick!=null){
                    contactBtnClick.contentClick(item);
                }
            }
        });

    }

    /**
     * 设置是否显示字母
     *
     * @param showLetter
     */
    public void setShowLetter(boolean showLetter) {
        isShowLetter = showLetter;
        notifyDataSetChanged();
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return getItem(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = getData().get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    private ContactBtnClick contactBtnClick;

    public void setContactBtnClick(ContactBtnClick contactBtnClick) {
        this.contactBtnClick = contactBtnClick;
    }

    public interface ContactBtnClick {
        void btnClick(ContactBean item);
        void contentClick(ContactBean item);
    }

    public void setContactExtra(ContactExtra contactExtra) {
        this.contactExtra = contactExtra;
    }

    /**
     * 设置按钮样式
     *
     * @param tv
     * @param contactExtra
     */
    public void setBtnStyle(TextView tv, ContactExtra contactExtra) {
        if (CMemoryData.isDriver()) {
            tv.setBackgroundResource(R.drawable.selector_btn_blue_border);
            ColorStateList colorStateList = mContext.getResources().getColorStateList(R.color.selector_white_blue_text_color);
            tv.setTextColor(colorStateList);
        } else {
            tv.setBackgroundResource(R.drawable.selector_btn_green_border);
            ColorStateList colorStateList = mContext.getResources().getColorStateList(R.color.selector_white_green_text_color);
            tv.setTextColor(colorStateList);

        }

        if (contactExtra.getType() == ContactExtra.FAMILIAR_CAR) {
            tv.setText(R.string.invitation);
        } else {
            tv.setText(R.string.share);
        }
    }

}
