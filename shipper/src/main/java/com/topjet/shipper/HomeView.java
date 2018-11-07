package com.topjet.shipper;

import android.view.View;

import com.topjet.common.adv.modle.response.GetBannerDataResponse;
import com.topjet.common.common.view.activity.SystemView;

import java.util.List;

/**
 * Created by yy on 2017/8/14.
 * 首页View
 */

public interface HomeView extends SystemView {
    void setBannerData(GetBannerDataResponse response);

    void loadHomeWebView(String url);

    void showAnnouncement(List<View> viewList);

    void showScrollBtns();
}
