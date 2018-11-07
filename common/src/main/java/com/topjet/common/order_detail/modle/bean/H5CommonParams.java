package com.topjet.common.order_detail.modle.bean;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.Config;

/**
 * Created by yy on 2017/10/16.
 * <p>
 * H5 页面需要的外层参数类。
 */

public class H5CommonParams extends CommonParams {
    private String service_host = Config.getAppHost();// Host地址
    private String aes_key = CPersisData.getKey();	// 加密key	string
    private String aes_ivcode = CPersisData.getKeyIvCode(); //	加密偏移量	string
}
