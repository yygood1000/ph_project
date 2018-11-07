package com.topjet.crediblenumber.user.modle.params;

/**
 * Created by tsj-004 on 2017/8/9.
 * 司机车辆认证请求参数
 */

public class UploadUsualCityParams {
    private String business_line_city_code1;//	第1条长跑路线cityid	否	string
    private String business_line_city_code2;//	第2条长跑路线cityid	否	string
    private String business_line_city_code3;//	第3条长跑路线cityid	否	string
    private String business_line_city_code4;//第4条长跑路线cityid	否	string
    private String business_line_city_code5;//第5条长跑路线cityid	否	string
    private String business_line_city_code6;//第6条长跑路线cityid	否	string
    private String business_line_city_code7;//	第7条长跑路线cityid	否	string
    private String business_line_city_code8;//第8条长跑路线cityid	否	string

    public void setBusinessLineCityCode1(String business_line_city_code1) {
        this.business_line_city_code1 = business_line_city_code1;
    }

    public void setBusinessLineCityCode2(String business_line_city_code2) {
        this.business_line_city_code2 = business_line_city_code2;
    }

    public void setBusinessLineCityCode3(String business_line_city_code3) {
        this.business_line_city_code3 = business_line_city_code3;
    }

    public void setBusinessLineCityCode4(String business_line_city_code4) {
        this.business_line_city_code4 = business_line_city_code4;
    }

    public void setBusinessLineCityCode5(String business_line_city_code5) {
        this.business_line_city_code5 = business_line_city_code5;
    }

    public void setBusinessLineCityCode6(String business_line_city_code6) {
        this.business_line_city_code6 = business_line_city_code6;
    }

    public void setBusinessLineCityCode7(String business_line_city_code7) {
        this.business_line_city_code7 = business_line_city_code7;
    }

    public void setBusinessLineCityCode8(String business_line_city_code8) {
        this.business_line_city_code8 = business_line_city_code8;
    }

    @Override
    public String toString() {
        return "UploadUsualCityParams{" +
                "business_line_city_code1='" + business_line_city_code1 + '\'' +
                ", business_line_city_code2='" + business_line_city_code2 + '\'' +
                ", business_line_city_code3='" + business_line_city_code3 + '\'' +
                ", business_line_city_code4='" + business_line_city_code4 + '\'' +
                ", business_line_city_code5='" + business_line_city_code5 + '\'' +
                ", business_line_city_code6='" + business_line_city_code6 + '\'' +
                ", business_line_city_code7='" + business_line_city_code7 + '\'' +
                ", business_line_city_code8='" + business_line_city_code8 + '\'' +
                '}';
    }
}