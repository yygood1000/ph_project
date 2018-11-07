package com.topjet.crediblenumber.user.presenter;

import android.content.Context;
import android.content.Intent;

import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseFragmentApiPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.view.activity.ShareDownloadActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.user.modle.bean.PersonCenterEntranceItem;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.user.modle.response.UserCenterParameterResponse;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.goods.service.FloatViewService;
import com.topjet.crediblenumber.user.modle.serverAPI.DriverCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.DriverCommandAPI;
import com.topjet.crediblenumber.user.view.activity.CallLogActivity;
import com.topjet.crediblenumber.user.view.activity.CarAuthenticationActivity;
import com.topjet.crediblenumber.user.view.activity.IdentityAuthenticationActivity;
import com.topjet.crediblenumber.user.view.activity.RealNameAuthenticationActivity;
import com.topjet.crediblenumber.user.view.fragment.PersonCenterView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yy on 2017/8/14.
 * 首页Presenter
 */

public class PersonCenterPresenter extends BaseFragmentApiPresenter<PersonCenterView, DriverCommand> {
    private List<PersonCenterEntranceItem> personCenterEntranceItems = new LinkedList<>();
    public UserCenterParameterResponse userInfo;// 用户信息

    public PersonCenterPresenter(PersonCenterView mView, Context mContext, RxFragment mFragment) {
        super(mView, mContext, mFragment);
        mApiCommand = new DriverCommand(DriverCommandAPI.class, mActivity);

        initPCEIList();
    }

    /**
     * 初始化list数据
     */
    private void initPCEIList() {
        personCenterEntranceItems.clear();

        String useStatusStr = "";
        if (userInfo != null) {
            String use_status = userInfo.getUse_status();       // 用户实名认证状态
            switch (use_status) {
                case "0":
                    useStatusStr = "未认证";
                    break;
                case "1":
//                    useStatusStr = "待审核";
                    useStatusStr = "认证中";
                    break;
                case "2":
                    //useStatusStr = "审核通过";
                    useStatusStr = ""; // 状态为双引号，在adapter中过滤不显示
                    break;
                case "3":
//                    useStatusStr = "资料修改待审核";
                    useStatusStr = "认证中";
                    break;
                case "4":
                    useStatusStr = "认证失败";
                    break;
            }
        }
        PersonCenterEntranceItem item = new PersonCenterEntranceItem();
        item.setContentStr("实名认证");
        item.setStatusStr(useStatusStr);
        item.setDrawableResourceId(R.drawable.icon_real_name_authentication);
        item.setExtra(new GoToAuthenticationExtra(GoToAuthenticationExtra.IN_TYPE_SHOW));
        item.setActivityClass(RealNameAuthenticationActivity.class);
        personCenterEntranceItems.add(item);

        String userAuthStatusStr = "";
        if (userInfo != null) {
            String user_auth_status = userInfo.getUser_auth_status();     // 身份认证
            switch (user_auth_status) {
                case "0":
                    userAuthStatusStr = "未认证";
                    break;

                case "1":
                    userAuthStatusStr = "认证中";
                    break;

                case "2":
                    userAuthStatusStr = "认证失败";
                    break;

                case "3":
                    //userAuthStatusStr = "认证通过";
                    userAuthStatusStr = "";// 状态为双引号，在adapter中过滤不显示
                    break;
            }
        }
        item = new PersonCenterEntranceItem();
        item.setContentStr("身份认证");
        item.setStatusStr(userAuthStatusStr);
        item.setDrawableResourceId(R.drawable.icon_identity_authentication);
        item.setExtra(new GoToAuthenticationExtra(GoToAuthenticationExtra.IN_TYPE_SHOW));
        item.setActivityClass(IdentityAuthenticationActivity.class);
        personCenterEntranceItems.add(item);

        String auditStatusString = "";
        if (userInfo != null) {
            String audit_status = userInfo.getAudit_status();       // 车辆认证状态
            switch (audit_status) {
                case "1":
                    auditStatusString = "未认证";
                    break;

                case "2":
                    //auditStatusString = "已认证";
                    auditStatusString = "";// 状态为双引号，在adapter中过滤不显示
                    break;

                case "3":
                    auditStatusString = "认证中";
                    break;

                case "4":
                    auditStatusString = "认证失败";
                    break;
            }
        }
        item = new PersonCenterEntranceItem();
        item.setContentStr("车辆认证");
        item.setStatusStr(auditStatusString);
        item.setDrawableResourceId(R.drawable.icon_car_authentication);
        item.setExtra(new GoToAuthenticationExtra(GoToAuthenticationExtra.IN_TYPE_SHOW));
        item.setActivityClass(CarAuthenticationActivity.class);
        personCenterEntranceItems.add(item);

        item = new PersonCenterEntranceItem();
        item.setContentStr("通话记录");
        item.setStatusStr("");
        item.setDrawableResourceId(R.drawable.icon_call_record);
        item.setActivityClass(CallLogActivity.class);
        personCenterEntranceItems.add(item);

        item = new PersonCenterEntranceItem();
        item.setContentStr("分享下载");
        item.setStatusStr("");
        item.setDrawableResourceId(R.drawable.icon_share);
        item.setActivityClass(ShareDownloadActivity.class);
        personCenterEntranceItems.add(item);
    }

    /**
     * 获取个人中心信息
     */
    public void getUserCentreParameter() {
        mApiCommand.getUserCentreParameter(new ObserverOnNextListener<UserCenterParameterResponse>() {
            @Override
            public void onError(String errorCode, String msg) {
                mView.requestFail();
            }

            @Override
            public void onNext(UserCenterParameterResponse response) {
                userInfo = response;
                saveUserInfo(userInfo);
                initPCEIList();

                String signStatusStr = "";
                String todayIntegralStr = "";
                boolean isHideRightArrow = false;   // 是否隐藏右箭头
                mView.setViewByParams(response.getUser_name(), response.getIcon_image_url(), response
                                .getIcon_image_key(), response.getIcon_image_status(), signStatusStr,
                        isHideRightArrow, todayIntegralStr,
                        response.getDegree_of_praise(), response.getIntegral_amount(), response.getWallet_balance(),
                        response.getUse_status());
            }
        });
    }

    /**
     * 保存自己的信息到本地
     */
    private void saveUserInfo(UserCenterParameterResponse userInfo) {
        CMemoryData.setUserState(userInfo.getUse_status());
        CommonProvider.getInstance().getUserInfoProvider().saveUserInfo(userInfo.getImUserInfo(userInfo));
    }


    /**
     * 开启floatview服务
     */
    public void startOrStopFloatViewService(boolean checked) {
        CPersisData.setFloatViewStatus(checked + "");
        if (checked) {
            mContext.startService(new Intent(mContext, FloatViewService.class));    // 开启悬浮窗服务（可控制语音是否播报）
        } else {
            mContext.stopService(new Intent(mContext, FloatViewService.class));    // 开启悬浮窗服务（可控制语音是否播报）
        }
    }

    public List<PersonCenterEntranceItem> getPersonCenterEntranceItems() {
        return personCenterEntranceItems;
    }

    public void setPersonCenterEntranceItems(List<PersonCenterEntranceItem> personCenterEntranceItems) {
        this.personCenterEntranceItems = personCenterEntranceItems;
    }
}
