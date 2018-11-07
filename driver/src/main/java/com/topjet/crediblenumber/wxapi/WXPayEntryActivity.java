package com.topjet.crediblenumber.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.topjet.wallet.config.WalletCMemoryData;
import com.topjet.wallet.config.WalletConstants;
import com.topjet.wallet.utils.Logger;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	private String appId = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay_results);
		if (WalletCMemoryData.isDriver()) {// wx2a5538052969956e
			appId = WalletConstants.AppID_Driver;
		} else {
			appId = WalletConstants.AppID_Shipper;
		}
		api = WXAPIFactory.createWXAPI(this, appId);// appid需换成商户自己开放平台appid
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		Logger.i("TTT", "成功1：");
	}

	@Override
	public void onReq(BaseReq req) {

		Logger.i("TTT", "成功2：" + req.toString());
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			// resp.errCode == -1
			// 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
			// resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
			if (resp.errCode == 0) // 支付成功
			{
				Logger.i("TTT", "成功3：");
			} else {
				Logger.i("TTT", "失败："+resp.errCode+"--AppId:"+appId);
			}
			Logger.i("TTT", "错误码描述：" + resp.errStr);

			finish();
		}
	}
}