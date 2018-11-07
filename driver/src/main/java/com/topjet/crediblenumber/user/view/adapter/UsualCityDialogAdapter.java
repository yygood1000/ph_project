package com.topjet.crediblenumber.user.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.crediblenumber.R;
import com.topjet.common.common.modle.bean.UsualCityBean;

/**
 * Created by  yy on 2017/8/16.
 * <p>
 * 司机版 未设置常跑城市，弹出添加常跑城市Dialog中的列表适配器
 */

public class UsualCityDialogAdapter extends BaseQuickAdapter<UsualCityBean, BaseViewHolder> {

    public UsualCityDialogAdapter() {
        super(R.layout.listitem_usual_city_dialog);
    }

    @Override
    protected void convert(BaseViewHolder helper, UsualCityBean item) {
        helper.getLayoutPosition();// 获取当前position

        // 设置城市名称
        helper.setText(R.id.tv_usual_city, item.getBusinessLineCityName());
        // 设置icon
        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.iv_usual_city, true);
            helper.setVisible(R.id.iv_icon_delete, false);
        } else {
            helper.setVisible(R.id.iv_usual_city, false);
            helper.setVisible(R.id.iv_icon_delete, true);
        }

        // 隐藏最后一项的分分割线
        if (helper.getLayoutPosition() == getData().size() - 1) {
            helper.setVisible(R.id.parding_line, false);
        } else {
            helper.setVisible(R.id.parding_line, true);
        }

        // 添加删除按钮点击事件
        helper.addOnClickListener(R.id.iv_icon_delete);
    }
}