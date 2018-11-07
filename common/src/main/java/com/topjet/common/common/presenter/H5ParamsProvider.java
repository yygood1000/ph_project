package com.topjet.common.common.presenter;

import android.content.Context;
import android.util.Base64;

import com.topjet.common.App;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.config.Config;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.SystemUtils;
//import com.topjetpaylib.encrypt.MD5Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * creator: zhulunjun
 * time:    2017/10/31
 * describe: h5页面参数拼接
 */

public class H5ParamsProvider {

    private static H5ParamsProvider instance;
    public static H5ParamsProvider getInstance() {
        if(instance == null){
            instance = new H5ParamsProvider();
        }
        return instance;
    }


    /**
     * 获取积分商城的url
     * @return url
     */
    public String getMallUrl(Context context){
//        String strUrl = "http://192.168.8.116:100/join";
        String strUrl = "http://192.168.20.122:8087/task?flag=1";
        long time = System.currentTimeMillis();
        String key = "%^)@(&amp;hyg";
            String sign = "";
            strUrl = strUrl +
                    "?uid=" + CPersisData.getUserID() +
                    "&mobile=" + CMemoryData.getUserMobile() +
                    "&time=" + time +
                    "&nickname=" + CMemoryData.getUserMobile() +
                    "&apptype=" + (CMemoryData.isDriver() ? "2" : "1") +
                    "&usertype=" + "type" +
                    "&sign=" + sign +
                    "&version=" + SystemUtils.getVersionName(context) +
                    "&paramStr=" + getParamStr();
        Logger.i("getUrl", "" + strUrl);
        return strUrl;
    }

    /**
     * 获取组织参数传给html5
     *
     * @return
     */
    private String getParamStr() {
        String str = "";
        JSONObject json = new JSONObject();
        try {
            json.put("mobile", CMemoryData.getUserMobile());
            json.put("domain", Config.getAppHost());
            json.put("aesKey", CConstants.AppkeyConstant.APP_AES_KEY);
            json.put("imei", SystemUtils.getDeviceID(App.getInstance()));
            json.put("aeslv", CConstants.AppkeyConstant.APP_AES_IVCODE);
            json.put("reqSource", RespectiveData.getRequestSource());
            json.put("sessionId", CMemoryData.getSessionId());
            json.put("version", SystemUtils.getVersionName(App.getInstance()));
            str = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.i("TTT", "json   ---  str:" + str);
        String s = null;
        try {
            s = Base64.encodeToString(str.getBytes("UTF-8"), Base64.URL_SAFE | Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
