package com.topjet.shipper;

import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/7/29.
 * <p>
 * Main界面的View接口
 */

public interface MainView extends IView {

    void showRegularActivityIcon(String url, String key);

    void hideRegularActivityIcon();

    void showHomeAdvDialog(BannerBean bannerBean);

    void showTabLayoutBtns();

    void showTabLayoutMessageCorner(String unread_sum);
}
