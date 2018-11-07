package com.topjet.common.common.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.UserInfo;
import com.topjet.common.common.modle.params.PhoneParams;
import com.topjet.common.common.modle.params.UserInfoParams;
import com.topjet.common.common.modle.response.UserInfoGoodsResponse;
import com.topjet.common.common.modle.response.UserInfoResponse;
import com.topjet.common.common.modle.response.UserInfoTruckResponse;
import com.topjet.common.common.modle.serverAPI.UserInfoCommand;
import com.topjet.common.common.modle.serverAPI.UserInfoCommandAPI;
import com.topjet.common.common.view.activity.UserInfoView;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.db.DBManager;
import com.topjet.common.db.bean.PhoneNumberBean;
import com.topjet.common.order_detail.modle.params.CancelOfferParams;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommandApi;
import com.topjet.common.user.modle.extra.PhoneExtra;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/11/13
 * describe: 诚信查询，用户信息
 */

public class UserInfoPresenter extends BaseApiPresenter<UserInfoView, UserInfoCommand> {
    public UserInfoPresenter(UserInfoView mView, Context mContext) {
        super(mView, mContext, new UserInfoCommand(UserInfoCommandAPI.class, (MvpActivity) mContext));
    }
    private UserInfo mUserInfo; // 用户信息
    private PhoneExtra mExtra; // 手机号
    @Override
    public void onCreate() {
        super.onCreate();

    }

    boolean isFirst = true;
    @Override
    public void onStart() {
        super.onStart();
        BaseExtra extra = mActivity.getIntentExtra(PhoneExtra.getExtraName());
        if(isFirst){
            isFirst = false;
        } else {
            return;
        }
        if(extra == null){
            mActivity.finishPage();
            return;
        }
        if(extra instanceof PhoneExtra){ // 带手机号进入
            mExtra = (PhoneExtra) extra;
            if(StringUtils.isNotBlank(mExtra.getPhone())){
                // 根据手机号查询
                if(CMemoryData.isDriver()){
                    queryShipperInfo();
                } else {
                    queryDriverInfo();
                }
            }
        } else if(extra instanceof UserInfo){ // 带用户信息进入
            // 根据用户信息显示
            if(mUserInfo == null){
                mUserInfo = (UserInfo) extra;
                mView.setUserInfo(mUserInfo);
            }
        }

    }


    /**
     * 查询司机信息
     */
    public void queryDriverInfo() {
        mApiCommand.queryDriverInfo(new PhoneParams(mExtra.getPhone()), new ObserverOnNextListener<UserInfoResponse>() {
            @Override
            public void onNext(UserInfoResponse userInfoResponse) {
                mView.setUserInfo(userInfoResponse.getQuery_integrity_user_info());
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showLongToast(msg);
            }

        });
    }

    /**
     * 查询货主信息
     */
    public void queryShipperInfo() {
        mApiCommand.queryShipperInfo(new PhoneParams(mExtra.getPhone()), new ObserverOnNextListener<UserInfoResponse>() {
            @Override
            public void onNext(UserInfoResponse userInfoResponse) {
                mView.setUserInfo(userInfoResponse.getQuery_integrity_user_info());
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showLongToast(msg);
            }

        });
    }

    /**
     * 查询货主货源信息
     */
    public void queryShipperInfoGoodsList(String userId, String page) {
        mApiCommand.queryShipperInfoGoodsList(new UserInfoParams(userId, page), new ObserverOnNextListener<UserInfoGoodsResponse>() {
            @Override
            public void onNext(UserInfoGoodsResponse userInfoGoodsResponse) {
                if (userInfoGoodsResponse != null) {
                    mView.loadSuccess(userInfoGoodsResponse.getList());
                } else {
                    mView.loadSuccess(new ArrayList());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 查询司机车辆信息
     */
    public void queryDriverInfoTruckList(String userId, String page) {
        mApiCommand.queryDriverInfoTruckList(new UserInfoParams(userId, page), new ObserverOnNextListener<UserInfoTruckResponse>() {
            @Override
            public void onNext(UserInfoTruckResponse userInfoTruckResponse) {
                if (userInfoTruckResponse != null) {
                    mView.setBusinessLine(userInfoTruckResponse.getBusiness_line_info());
                    mView.loadSuccess(userInfoTruckResponse.getList());
                } else {
                    mView.setBusinessLine("");
                    mView.loadSuccess(new ArrayList());
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }


    /**
     * 撤销报价
     */
   public void cancelOffer(String id){
       List<String> ids = new ArrayList<>();
       ids.add(id);
       new OrderDetailCommand(OrderDetailCommandApi.class, mActivity)
               .cancelOffer(new CancelOfferParams(ids), new ObserverOnResultListener<Object>() {
           @Override
           public void onResult(Object o) {
               // 刷新货源
               // 弹窗提示, 关闭页面
               Toaster.showShortToast("撤销成功");
               mView.refresh();
           }
       });
   }



}
