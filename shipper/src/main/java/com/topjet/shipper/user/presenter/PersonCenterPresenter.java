package com.topjet.shipper.user.presenter;

import android.content.Context;

import com.topjet.common.base.CommonProvider;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.presenter.BaseFragmentApiPresenter;
import com.topjet.common.base.view.fragment.RxFragment;
import com.topjet.common.common.view.activity.ShareDownloadActivity;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.user.modle.bean.PersonCenterEntranceItem;
import com.topjet.common.user.modle.extra.GoToAuthenticationExtra;
import com.topjet.common.user.modle.response.UserCenterParameterResponse;
import com.topjet.shipper.R;
import com.topjet.shipper.user.model.serviceAPI.ShipperCommand;
import com.topjet.shipper.user.model.serviceAPI.ShipperCommandAPI;
import com.topjet.shipper.user.view.activity.AnonymousSetActivity;
import com.topjet.shipper.user.view.activity.IdentityAuthenticationActivity;
import com.topjet.shipper.user.view.activity.RealNameAuthenticationActivity;
import com.topjet.shipper.user.view.fragment.PersonCenterView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yy on 2017/8/14.
 * 首页Presenter
 */

public class PersonCenterPresenter extends BaseFragmentApiPresenter<PersonCenterView, ShipperCommand> {
    private List<PersonCenterEntranceItem> personCenterEntranceItems = new LinkedList<>();
    public UserCenterParameterResponse userInfo;// 用户信息

    public PersonCenterPresenter(PersonCenterView mView, Context mContext, RxFragment mFragment) {
        super(mView, mContext, mFragment);
        mApiCommand = new ShipperCommand(ShipperCommandAPI.class, mActivity);

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
                    useStatusStr = "认证中";
                    break;
                case "2":
                    useStatusStr = "";
                    break;
                case "3":
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
                    // userAuthStatusStr = "认证通过";
                    userAuthStatusStr = "";
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

        String anonymityStatusStr = "";
        if (userInfo != null) {
            String anonymity_status = userInfo.getAnonymity_status();       // 匿名设置
            switch (anonymity_status) {
                case "0":
                    anonymityStatusStr = "已关闭";
                    break;

                case "1":
                    anonymityStatusStr = "已开启";
                    break;
            }
        }
        item = new PersonCenterEntranceItem();
        item.setContentStr("匿名设置");
        item.setStatusStr(anonymityStatusStr);
        item.setDrawableResourceId(R.drawable.icon_invisible);
        item.setActivityClass(AnonymousSetActivity.class);
        personCenterEntranceItems.add(item);

        item = new PersonCenterEntranceItem();
        item.setContentStr("分享下载");
        item.setStatusStr("");
        item.setDrawableResourceId(R.drawable.icon_share);
        item.setActivityClass(ShareDownloadActivity.class);
        personCenterEntranceItems.add(item);
    }

    /**
     * 获取各种状态参数
     */
    public void getUserCentreParameter() {
        mApiCommand.getUserCentreParameter(new ObserverOnNextListener<UserCenterParameterResponse>() {
            @Override
            public void onNext(UserCenterParameterResponse response) {
                userInfo = response;
                saveUserInfo(userInfo);
                initPCEIList();

                String signStatus = response.getSign_status();  // 0：未签到 1：已签到
                String signStatusStr = "";
                String todayIntegralStr = "";
                boolean isHideRightArrow = false;   // 是否隐藏右箭头
                mView.setViewByParams(response.getUser_name(), response.getIcon_image_url(), response
                                .getIcon_image_key(), response.getIcon_image_status(), signStatusStr,
                        isHideRightArrow, todayIntegralStr,
                        response.getDegree_of_praise(), response.getIntegral_amount(), response.getWallet_balance(),
                        response.getUse_status());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.requestFail();
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

    public List<PersonCenterEntranceItem> getPersonCenterEntranceItems() {
        return personCenterEntranceItems;
    }

    public void setPersonCenterEntranceItems(List<PersonCenterEntranceItem> personCenterEntranceItems) {
        this.personCenterEntranceItems = personCenterEntranceItems;
    }
}
