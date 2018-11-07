package com.topjet.common.order_detail.view.activity;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.topjet.common.R;
import com.topjet.common.R2;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.base.view.activity.BaseWebViewActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.order_detail.modle.extra.ClickAgreeBtnEvent;
import com.topjet.common.order_detail.modle.extra.ProtocolExtra;
import com.topjet.common.order_detail.modle.js_interface.ProtocolJSInterface;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.order_detail.presenter.ProtocolPresenter;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import butterknife.BindView;

/**
 * Created by yy on 2017/9/22.
 * <p>
 * 交易协议页面
 * <p>
 * 该页面有个逻辑，先接在H5页面，加载完成后再请求服务端获取服务端数据，然后才调用H5的方法
 */

public class ProtocolActivity extends BaseWebViewActivity<ProtocolPresenter> implements ProtocolView {
    @Nullable
    @BindView(R2.id.btn_agree)
    Button btnAgree;
    @Nullable
    @BindView(R2.id.rl_root)
    RelativeLayout rlRoot;
    private String truckId;// 司机确认承接时选择的车辆。可能不选。不选服务端自动选择认证车辆

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_protocol;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ProtocolPresenter(this, mContext, new OrderDetailCommand(OrderDetailCommandApi.class, this));
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置H5 交互接口
        mWebView.addJavascriptInterface(new ProtocolJSInterface(this, mWebView), "App");
        btnAgree.setOnClickListener(clickListener);
        // 设置加载结束监听
        setLoadFinishListener(loadFinishListener);
        // 设置按钮背景色
        setBtnBackGround();
    }

    @Override
    public String getUrl() {
        return mPresenter.getUrl();
    }

    /**
     * 进入页面先请求协议页面需要的内容
     */
    @Override
    protected void initData() {
        super.initData();
        mPresenter.getProtocolInfo();
    }

    /**
     * 数据获取完成，开始加载协议页面
     */
    @Override
    public void loadWeb() {
        rlRoot.setVisibility(View.VISIBLE);
        Logger.d("oye ---------- "+mPresenter.getUrl());
        mWebView.loadUrl(mPresenter.getUrl());
    }

    /**
     * 初始化同意按钮点击监听
     */
    @Override
    public void initBtn() {
        int type = mPresenter.extra.getPageType();
        switch (type) {
            case ProtocolExtra.TYPE_CHECK_PROTOCOL:
                btnAgree.setVisibility(View.GONE);
                break;
            case ProtocolExtra.TYPE_CONFIRM_HAS_NOT_DIALOG:
            case ProtocolExtra.TYPE_CONFIRM_HAS_DIALOG:
                // 确认成交
                break;
            case ProtocolExtra.TYPE_ALTER_PAY_TYPE:
                // 修改支付方式
                break;
            case ProtocolExtra.TYPE_ACCEPT_ORDER:
                // 我要承接
                break;
            case ProtocolExtra.TYPE_DELIVER_GOODS:
                // 发货界面进来的
                btnAgree.setText("我知道了");
                break;
        }
    }

    /**
     * H5页面加载完成
     */
    private BaseWebViewActivity.OnLoadFinishListener loadFinishListener = new BaseWebViewActivity
            .OnLoadFinishListener() {
        @Override
        public void onLoadFinish() {
            mWebView.loadUrl("javascript:getContract('" + mPresenter.contentJson + "')");
        }
    };

    /**
     * 设置按钮背景色
     */
    private void setBtnBackGround() {
        if (CMemoryData.isDriver()) {
            btnAgree.setBackgroundResource(R.drawable.shape_bg_gradient_driver);
        }
        if (mPresenter.extra.getPageType() == ProtocolExtra.TYPE_CHECK_PROTOCOL) {
            btnAgree.setVisibility(View.GONE);
        }
    }

    /**
     * 点击事件
     */
    private DebounceClickListener clickListener = new DebounceClickListener() {
        @Override
        public void performClick(View v) {
            if (StringUtils.isNotBlank(truckId)) {
                BusManager.getBus().post(new ClickAgreeBtnEvent(mPresenter.extra.getTag(), truckId));
            } else {
                BusManager.getBus().post(new ClickAgreeBtnEvent(mPresenter.extra.getTag()));
            }
            finish();
        }
    };

    /**
     * 设置承接车辆
     */
    public void setTruckId(String truckId) {
        Logger.i("oye", "truckId = " + truckId);
        this.truckId = truckId;
    }
}
