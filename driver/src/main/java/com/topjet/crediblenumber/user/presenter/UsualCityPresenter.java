package com.topjet.crediblenumber.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.modle.params.UploadUsualCityInCenterParams;
import com.topjet.crediblenumber.user.modle.response.GetUsualCityListResponse;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommand;
import com.topjet.crediblenumber.user.modle.serverAPI.UsualCityCommandAPI;
import com.topjet.crediblenumber.user.view.activity.UsualCityView;

import java.util.ArrayList;

/**
 * Created by yy on 2017/10/16.
 * 常跑城市
 */

public class UsualCityPresenter extends BaseApiPresenter<UsualCityView<UsualCityBean>, UsualCityCommand> {
    private ArrayList<String> mAddUsualCityList;// 添加常跑城市的集合
    private ArrayList<UsualCityBean> mUpdataUsualCityList;// 修改常跑城市的集合
    private ArrayList<String> mDeleteUsualCityList;// 删除常跑城市的集合
    private ArrayList<UsualCityBean> listItem;  // 服务器返回数据

    public UsualCityPresenter(UsualCityView<UsualCityBean> mView, Context mContext, UsualCityCommand mApiCommand) {
        super(mView, mContext, mApiCommand);
        mAddUsualCityList = new ArrayList<>();
        mUpdataUsualCityList = new ArrayList<>();
        mDeleteUsualCityList = new ArrayList<>();
    }

    /**
     * 更新 修改常跑城市 集合
     */
    public void setUpdataList(String cityId, String lineId) {
        // 修改同一项的循环判断
        if (mUpdataUsualCityList.size() == 0) {
            addItemToUpdataList(cityId, lineId);
        } else {
            for (int i = 0; i < mUpdataUsualCityList.size(); i++) {
                if (mUpdataUsualCityList.get(i).getBussinessLineId().equals(lineId)) {
                    mUpdataUsualCityList.get(i).setBusiness_line_city_id(cityId);
                    return;
                }
                Logger.i("oye", "i ==  " + i + "  size =" + mUpdataUsualCityList.size());
                if (i == mUpdataUsualCityList.size() - 1) {
                    Logger.i("oye", "新增了一条修改信息 ");
                    addItemToUpdataList(cityId, lineId);
                }
            }
        }
    }

    /**
     * 向修改集合中添加一条新的数据
     */
    private void addItemToUpdataList(String cityId, String lineId) {
        UsualCityBean bean = new UsualCityBean();
        bean.setBusiness_line_city_id(cityId);
        bean.setBusiness_line_id(lineId);
        mUpdataUsualCityList.add(bean);
    }

    /**
     * 更新 新增常跑城市 集合
     */
    public void updataAddList(String oldCityId, String newCityId) {
        if (mAddUsualCityList.contains(oldCityId)) {
            mAddUsualCityList.remove(oldCityId);
        }
        mAddUsualCityList.add(newCityId);
    }

    /**
     * 删除新增常跑城市集合中的数据
     */
    public void removeAddListItem(String oldCityId) {
        if (mAddUsualCityList.contains(oldCityId)) {
            mAddUsualCityList.remove(oldCityId);
        }
    }

    /**
     * 更新 删除常跑城市 集合
     */
    public void updataDeleteList(String cityId) {
        mDeleteUsualCityList.add(cityId);
    }

    /**
     * 获取常跑城市列表
     */
    public void getUsualCityList() {
        CommonParams commonParams = new CommonParams(UsualCityCommandAPI.GET_USUAL_CITY_LIST_IN_CENTER);
        mApiCommand.getUsualCityListInCenter(commonParams, new ObserverOnNextListener<GetUsualCityListResponse>() {
            @Override
            public void onNext(GetUsualCityListResponse response) {
                listItem = response.getList();
                mView.loadSuccess(response.getList());
            }

            @Override
            public void onError(String errorCode, String msg) {
                mView.loadFail(msg);
            }
        });
    }

    /**
     * 提交常跑城市
     */
    public void updataUsualCity() {
        if (ListUtils.isEmpty(mAddUsualCityList) && ListUtils.isEmpty(mUpdataUsualCityList) && ListUtils.isEmpty
                (mDeleteUsualCityList)) {
            mView.showToast("您未修改信息，无需提交。");
        } else {
            UploadUsualCityInCenterParams params = new UploadUsualCityInCenterParams();
            params.setAddCityCodes(mAddUsualCityList);
            params.setAlterBusinessLines(mUpdataUsualCityList);
            params.setDeleteIds(mDeleteUsualCityList);
            mApiCommand.updataUsualCityListInCenter(params, new ObserverOnResultListener<Object>() {
                @Override
                public void onResult(Object o) {
                    mView.showToast(ResourceUtils.getString(R.string.upload_succeed));
                    mView.finishPage();
                }
            });
        }

    }

}
