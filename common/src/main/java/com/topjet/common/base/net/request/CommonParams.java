package com.topjet.common.base.net.request;

import com.topjet.common.App;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.utils.SystemUtils;

/**
 * 公共请求参数基类
 * Created by tsj004 on 2017/7/31.
 */

public class CommonParams<T> {
    protected String destination;//接口地址
    protected String mobile = CMemoryData.getUserMobile();
    protected String imei = SystemUtils.getDeviceID(App.getInstance());
    protected String req_source = RespectiveData.getRequestSource();
    protected String phone_model = android.os.Build.MODEL;//手机型号
    protected String time_stamp = String.valueOf(System.currentTimeMillis());
    protected String session_id = CMemoryData.getSessionId();
    protected String system_version = "Android"+android.os.Build.VERSION.RELEASE;//android系统版本
    protected String version = SystemUtils.getVersionName(App.getInstance());
    protected String ip_address;
    protected String inno_version = CMemoryData.isDriver() ? CConstants.OUT_VERSION_DRIVER : CConstants.OUT_VERSION_SHIPPER;//内部版本号
    protected T parameter;

    public CommonParams() {}

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public CommonParams(String des) {
        this.destination = des;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getReqSource() {
        return req_source;
    }

    public void setReqSource(String req_source) {
        this.req_source = req_source;
    }

    public String getPhoneModel() {
        return phone_model;
    }

    public void setPhoneModel(String phone_model) {
        this.phone_model = phone_model;
    }

    public String getTimeStamp() {
        return time_stamp;
    }

    public void setTimeStamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getSessionId() {
        return session_id;
    }

    public void setSessionId(String session_id) {
        this.session_id = session_id;
    }

    public String getSystemVersion() {
        return system_version;
    }

    public void setSystemVersion(String system_version) {
        this.system_version = system_version;
    }

    public String getIpAddress() {
        return ip_address;
    }

    public void setIpAddress(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getInnoVersion() {
        return inno_version;
    }

    public void setInnoVersion(String inno_version) {
        this.inno_version = inno_version;
    }

    @Override
    public String toString() {
        return "CommonParams{" +
                "destination='" + destination + '\'' +
                ", mobile='" + mobile + '\'' +
                ", imei='" + imei + '\'' +
                ", req_source='" + req_source + '\'' +
                ", phone_model='" + phone_model + '\'' +
                ", time_stamp='" + time_stamp + '\'' +
                ", session_id='" + session_id + '\'' +
                ", system_version='" + system_version + '\'' +
                ", version='" + version + '\'' +
                ", ip_address='" + ip_address + '\'' +
                ", inno_version='" + inno_version + '\'' +
                ", parameter=" + parameter +
                '}';
    }
}
