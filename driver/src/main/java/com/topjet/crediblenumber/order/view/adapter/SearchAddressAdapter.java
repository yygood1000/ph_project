package com.topjet.crediblenumber.order.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.crediblenumber.R;

/**
 * creator: zhulunjun
 * time:    2017/11/7
 * describe:
 */

public class SearchAddressAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {
    public SearchAddressAdapter() {
        super(R.layout.listitem_seach);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Tip item) {
        ImageView imageView = helper.getView(R.id.iv_icon);
        imageView.setImageResource(R.drawable.iv_icon_locat_address);
        helper.setText(R.id.tv_content, item.getName());
        helper.getView(R.id.rl_content).setClickable(true);
        helper.getView(R.id.rl_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressListener != null){
                    addressListener.addressClick(item);
                }
            }
        });
    }

    public void setAddressListener(AddressListener addressListener) {
        this.addressListener = addressListener;
    }

    private AddressListener addressListener;

    public interface AddressListener {
        void addressClick(Tip item);
    }
}
