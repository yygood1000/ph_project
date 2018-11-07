package com.topjet.crediblenumber.order.presenter;

import android.content.Context;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.base.view.activity.IListView;
import com.topjet.common.config.CConstants;
import com.topjet.common.order_detail.modle.bean.MyOfferList;
import com.topjet.common.order_detail.modle.params.CancelOfferParams;
import com.topjet.common.order_detail.modle.params.GetMyOfferListParams;
import com.topjet.common.order_detail.modle.response.GetMyOfferListResponse;
import com.topjet.common.order_detail.modle.serverAPI.OrderDetailCommand;
import com.topjet.common.utils.LocationUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.goods.view.dialog.BidOrAlterDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * creator: zhulunjun
 * time:    2017/9/12
 * describe: 报价列表
 */

public class MyOfferPresenter extends BaseApiPresenter<IListView<MyOfferList>, OrderDetailCommand> {

    public MyOfferPresenter(IListView<MyOfferList> mView, Context mContext, OrderDetailCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 货主-查看报价列表
     */
    public void showOfferList(int page) {
        mApiCommand.myOfferList(new GetMyOfferListParams(lat + "", lng + "", page + ""),
                new ObserverOnNextListener<GetMyOfferListResponse>() {
                    @Override
                    public void onNext(GetMyOfferListResponse o) {
                        if (o != null) {
                            mView.loadSuccess(o.getList());
                        }
                    }

                    @Override
                    public void onError(String errorCode, String msg) {
                        mView.loadFail(msg);
                    }
                });
    }


    /**
     * 获取当前位置,拿到经纬度，以请求接口
     */
    private String lat = "", lng = "";

    public void getLocationData() {

        LocationUtils.location(mContext, new LocationUtils.OnLocationListener() {
            @Override
            public void onLocated(AMapLocation aMapLocation) {
                mView.cancelShowLoadingDialog();
                if (aMapLocation.getErrorCode() == LocationUtils.LOCATE_SUCCEED) {
                    lat = aMapLocation.getLatitude() + "";
                    lng = aMapLocation.getLongitude() + "";
                } else {
                    Logger.d("onLocated error " + aMapLocation.getErrorCode());
                    AutoDialogHelper.showSettingLocationPermission(mContext).show();
                    lat = "";
                    lng = "";
                }
                // 获取智能找货列表数据
                mView.refresh();
            }

            // 定位权限获取失败
            @Override
            public void onLocationPermissionfaild() {
                mView.cancelShowLoadingDialog();
                lat = "";
                lng = "";
                AutoDialogHelper.showSettingLocationPermission(mContext).show();
            }
        });
    }

    /**
     * CheckBox选择监听
     *
     * @param check
     * @param item
     */
    public List<MyOfferList> cancelOfferList = new ArrayList<>();


    public void checkClick(boolean check, MyOfferList item) {
        if (check) {
            cancelOfferList.add(item);
        } else {
            cancelOfferList.remove(item);
        }
    }

    /**
     * 修改 定金，支付定金 监听
     *
     * @param item
     */

    public void payDepositClick(View v, MyOfferList item) {
        // 跳弹窗，修改报价
        new BidOrAlterDialog(mActivity).showAlterView(
                item.getGoods_id(),
                item.getTransport_fee(),
                item.getDeposit_fee(),
                item.getPre_goods_id(),
                item.getPre_goods_version(),
                new BidOrAlterDialog.OnPayForDepositResultListener() {
                    @Override
                    public void onSucceed() {
                        mView.refresh();
                    }
                });

    }

    /**
     * 全选
     */
    public List<MyOfferList> checkAll(List<MyOfferList> mData) {
        if (mData == null || mData.size() == 0) {
            return new ArrayList<>();
        }
        cancelOfferList.clear();
        for (MyOfferList offerList : mData) {
            offerList.setCheck(true);
            cancelOfferList.add(offerList);
        }

        return mData;

    }

    /**
     * 全部反选
     */
    public List<MyOfferList> checkCancelAll(List<MyOfferList> mData) {
        if (mData == null || mData.size() == 0) {
            return new ArrayList<>();
        }
        cancelOfferList.clear();
        for (MyOfferList offerList : mData) {
            offerList.setCheck(false);
        }

        return mData;
    }

    /**
     * 获取报价id集合
     *
     * @return
     */
    public List<String> getCancelIds() {
        if (cancelOfferList == null || cancelOfferList.size() == 0) {
            return null;
        } else {
            List<String> ids = new ArrayList<>();
            for (MyOfferList offerList : cancelOfferList) {
                ids.add(offerList.getPre_goods_id());
            }
            return ids;
        }
    }

    /**
     * 司机-撤销报价
     */
    public void cancelOffer() {
        List<String> ids = getCancelIds();
        if (ids == null) {
            return;
        }
        mApiCommand.cancelOffer(new CancelOfferParams(ids), new ObserverOnResultListener<Object>() {
            @Override
            public void onResult(Object o) {
                // 刷新
                // 弹窗提示
                Toaster.showShortToast("撤销成功");
                showOfferList(CConstants.PAGE_START);
            }
        });
    }
}
