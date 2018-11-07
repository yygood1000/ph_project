package com.topjet.common.adv.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.topjet.common.R;
import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.base.dialog.BaseDialog;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.view.activity.SimpleWebViewActivity;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.LayoutUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;

import butterknife.ButterKnife;

/**
 * creator: yy
 * time:    2017/11/2
 * describe: 首页弹窗广告
 */

public class HomeAdvDialog extends BaseDialog implements View.OnClickListener {
    private BannerBean bannerBean;

    public HomeAdvDialog(Context context, BannerBean bannerBean) {
        super(context, R.layout.dialog_home_adv);
        this.bannerBean = bannerBean;
        setView();
    }

    private void setView() {
        setWindowAnimations(R.style.right_top_out_dialog_anim);
        ImageView ivClose = ButterKnife.findById(view, R.id.iv_close);
        ImageView iv_adv = ButterKnife.findById(view, R.id.iv_adv);
        LayoutUtils.setRelativeParams(iv_adv, (int) (ScreenUtils.getScreenWidth(mContext) * 0.875), (int) (ScreenUtils
                .getScreenHeight(mContext) * 0.675));
        ivClose.setOnClickListener(this);
        iv_adv.setOnClickListener(this);


//        GlideUtils.loaderImageRound(mContext, bannerBean.getPicture_url(), bannerBean.getPicture_key(), R.drawable
//                .shape_avatar_loading, R.drawable.shape_avatar_loading, iv_adv, 4);
        GlideUtils.loaderImage(bannerBean.getPicture_url(), bannerBean.getPicture_key(), R.drawable
                .shape_avatar_loading, R.drawable.shape_avatar_loading, iv_adv);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_close) {
            // 关闭
            dismiss();
        } else if (i == R.id.iv_adv) {
            // 链接为空不跳转
            if(StringUtils.isNotBlank(bannerBean.getRedirect_url())) {
                SimpleWebViewActivity.turnToSimpleWebViewActivity((MvpActivity) mContext, bannerBean.getRedirect_url(),
                        bannerBean.getWeb_title());
            }
        }
    }


}
