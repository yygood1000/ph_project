package com.topjet.crediblenumber.goods.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.CallPhoneUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.modle.bean.EconomicListData;

/**
 * Created by yy on 2017/7/31.
 * <p>
 * 货运经纪人 适配器
 */

public class EconomicListAdapter extends BaseQuickAdapter<EconomicListData, BaseViewHolder> {
    private MvpActivity mActivity;


    public EconomicListAdapter(MvpActivity mvpActivity) {
        super(R.layout.listitem_economic);
        this.mActivity = mvpActivity;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final EconomicListData item) {
        ImageView iv = viewHolder.getView(R.id.iv_avatar);
        GlideUtils.loaderImageRoundWithSize(mContext, item.getIcon_url(), item.getIcon_key(), R.drawable
                .iv_avatar_man, R.drawable.shape_avatar_loading, iv, 4, ScreenUtils.dp2px(mActivity, 35), ScreenUtils
                .dp2px(mActivity, 35));

        viewHolder.setText(R.id.tv_name, item.getName());
        viewHolder.setText(R.id.tv_business_line1, item.getBusinessLine1());
        viewHolder.setText(R.id.tv_business_line2, item.getBusinessLine2());
        viewHolder.setText(R.id.tv_business_line3, item.getBusinessLine3());

        viewHolder.getView(R.id.iv_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转聊天
            }
        });
        viewHolder.getView(R.id.iv_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CallPhoneUtils().showCallDialogWithAdvNotUpload(mActivity, v, item.getName(), item.getMobile(), 0);
            }
        });

    }
}
