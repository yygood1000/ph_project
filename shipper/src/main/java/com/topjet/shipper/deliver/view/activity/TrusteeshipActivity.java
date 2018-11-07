package com.topjet.shipper.deliver.view.activity;

import android.view.View;

import com.topjet.common.base.busManger.Subscribe;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.DoubleClickCheckUtil;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.shipper.MainActivity;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.controller.OwnerGoodsController;
import com.topjet.shipper.deliver.modle.params.OwnerGoodsParams;
import com.topjet.shipper.deliver.presenter.TrusteeshipPresenter;
import com.topjet.shipper.order.modle.response.AddGoodsReponse;

import butterknife.OnClick;

/**
 * 是否托管运费
 * 货主版
 */
public class TrusteeshipActivity extends MvpActivity<TrusteeshipPresenter> implements TrusteeshipView {
    private OwnerGoodsParams params = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_trusteeship_free;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.is_trusteeship_free);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new TrusteeshipPresenter(this, mContext);
    }

    @Override
    public void initView() {


        params = getIntentExtra(OwnerGoodsParams.getExtraName());
    }

    @OnClick({R.id.tv_yes, R.id.tv_no})
    public void clickViews(View v) {
        int id = v.getId();

        /**
         * 防止重复点击
         */
        if (DoubleClickCheckUtil.check(this.getClass().getName(), id)) {
            return;
        }

        if (id == R.id.tv_yes || id == R.id.tv_no) {
            if (params != null) {
                if (id == R.id.tv_yes) {
                    params.setFreight_fee_managed("1");
                } else {
                    params.setFreight_fee_managed("0");
                }

                switch (params.getInType()) {
                    case OwnerGoodsParams.IN_TYPE_EDIT:
                        // 修改货源订单
                        new OwnerGoodsController(this).updateGoods(params, TAG);
                        break;
                    default:
                        if (params.getPhoto_remark_key() == null) {
                            String url = params.getPhoto_remark();
                            if (!StringUtils.isEmpty(url)) {
                                params.setPhoto_remark(ImageUtil.getBase64With480x800(url));
                            }
                        }
                        // 发布新货源或复制货源
                        new OwnerGoodsController(this).addGoods(params, TAG);
                        break;
                }
            }
        }
    }

    /**
     * 发货成功
     */
    @Subscribe
    public void onEvent(AddGoodsReponse addGoodsReponse) {
        if (addGoodsReponse.getTag().equalsIgnoreCase(TAG)) {
            Toaster.showLongToast(R.string.add_goods_success);
            turnToActivity(MainActivity.class);
            finish();
        }
    }
}
