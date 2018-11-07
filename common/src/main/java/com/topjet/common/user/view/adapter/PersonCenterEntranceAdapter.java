package com.topjet.common.user.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.R;
import com.topjet.common.user.modle.bean.PersonCenterEntranceItem;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;

/**
 * Created by  tsj-004 on 2017/10/23.
 * <p>
 * 个人中心功能入口
 */

public class PersonCenterEntranceAdapter extends BaseQuickAdapter<PersonCenterEntranceItem, BaseViewHolder> {

    public PersonCenterEntranceAdapter() {
        super(R.layout.griditem_person_center_entrance);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonCenterEntranceItem item) {
        if (StringUtils.isEmpty(item.getStatusStr())) {
            helper.getView(R.id.tv_status).setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
        }
        TextView mTextView = helper.getView(R.id.tv_status);
        helper.setText(R.id.tv_status, item.getStatusStr());
        if (mTextView.getText().length() >= 4){
            mTextView.setTextSize(6);
        }else{
            mTextView.setTextSize(8);
        }
        helper.setImageResource(R.id.iv_icon, item.getDrawableResourceId());
        helper.setText(R.id.tv_content, item.getContentStr());
    }
}