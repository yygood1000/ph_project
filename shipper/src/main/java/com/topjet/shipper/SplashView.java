package com.topjet.shipper;

import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.base.view.activity.IView;

/**
 * Created by yy on 2017/8/7.
 */

public interface SplashView extends IView {
    /**
     * 加载广告图 gif
     * @param splashAdvInfo 图片信息
     */
    void loadSplashGif(BannerBean splashAdvInfo);

    /**
     * 加载广告图
     * @param splashAdvInfo 图片信息
     */
    void loadSplashImg(BannerBean splashAdvInfo);

    /**
     * 设置跳过文本的显示
     * @param visible 是否显示
     */
    void setSkipView(int visible);
}
