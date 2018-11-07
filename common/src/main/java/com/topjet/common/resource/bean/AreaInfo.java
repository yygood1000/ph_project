package com.topjet.common.resource.bean;

import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.utils.StringUtils;

import java.io.Serializable;

public class AreaInfo implements Serializable {
    private CityItem firstLevel = null;
    private CityItem secondLevel = null;
    private CityItem thirdLevel = null;
    private String cityId = "";

    public static final String CITY_SEPARATOR = " ";

    public AreaInfo(CityItem firstLevel, CityItem secondLevel, CityItem thirdLevel) {
        this.firstLevel = firstLevel;
        this.secondLevel = secondLevel;
        this.thirdLevel = thirdLevel;
    }

    public AreaInfo(CityItem firstLevel, CityItem secondLevel) {
        this.firstLevel = firstLevel;
        this.secondLevel = secondLevel;
    }

    public AreaInfo(CityItem firstLevel) {
        this.firstLevel = firstLevel;
    }

    public AreaInfo() {}

    /**
     * 获取最后一个选择城市的名称
     * 例：浙江省/杭州市/余杭区
     */
    public String getShortDisplayText() {
        String text = this.thirdLevel != null ? this.thirdLevel.getCityName() : "";
        if (this.thirdLevel == null) {
            text = this.secondLevel != null ? this.secondLevel.getCityName() : "";
        }
        if (this.secondLevel == null) {
            text = this.firstLevel != null ? this.firstLevel.getCityName() : "";
        }
        return text;
    }

    /**
     * 常跑城市获取3级城市名称
     *
     * @return 1.上海
     * 2.上海 徐汇区
     * 3.浙江
     * 4.浙江 杭州
     * 5.杭州 余杭区
     */
    public String getFullCityName() {
        String first = this.firstLevel == null ? "" : this.firstLevel.getCityName().replace("省", "");
        String second = this.secondLevel == null ? "" : this.secondLevel.getCityName().replace("市", "");
        String third = this.thirdLevel == null ? "" : this.thirdLevel.getCityName();

        // 直辖市,三级选择的是全境.则返回:上海
        if (AreaDataDict.isSpecialFirstLevel(first.replace("市", "")) && StringUtils.isEmpty(third)) {
            return first.replace("市", "");
        }

        // 直辖市,三级选择的不是全境.则返回:上海 闵行区
        if (AreaDataDict.isSpecialFirstLevel(first.replace("市", "")) && StringUtils.isNotBlank(third)) {
            return first.replace("市", "") + CITY_SEPARATOR + third; // 上海 闵行区
        }

        // 只选择了第一级城市，二级就选了全境
        if (StringUtils.isEmpty(second)) {
            return first; // 浙江
        }

        // 第三季城市 选择了全境。
        if (StringUtils.isNotBlank(second) && StringUtils.isEmpty(third)) {
            return first + CITY_SEPARATOR + second; // 浙江 杭州
        }

        // 全部都选了
        if (StringUtils.isNotBlank(second) && StringUtils.isNotBlank(third)) {
            return second + CITY_SEPARATOR + third; // 杭州 余杭区
        }
        return "";
    }


    public String getLastCityId() {
        String strCityId = "";
        if (getLastCityItem() != null) {
            strCityId = getLastCityItem().getAdcode();
        }
        return strCityId;
    }


    /**
     * 获取城市名称 (返回的城市名称是缩写)
     */
    public String getLastCityName() {
        return getLastCityName(true);
    }

    /**
     * 获取城市名称（最后一级的名称）
     *
     * @param isGetFullCityName 是否返回最后一级城市名称的缩写
     */
    public String getLastCityName(boolean isGetFullCityName) {
        String strCityName = "";
        if (getLastCityItem() != null) {
            if (isGetFullCityName) {
                strCityName = getLastCityItem().getCityName();
            } else {
                strCityName = getLastCityItem().getCityFullName();
            }
        }
        return strCityName.replace("市", "");
    }

    public String getCityId() {
        return cityId;
    }


    /**
     * 获取最后一级城市对象
     */
    public CityItem getLastCityItem() {
        if (thirdLevel != null && StringUtils.isNotBlank(this.thirdLevel.getAdcode())) {
            return thirdLevel;
        }

        if (this.thirdLevel == null) {
            if (secondLevel != null && StringUtils.isNotBlank(this.secondLevel.getAdcode())) {
                return secondLevel;
            }
        }

        if (this.secondLevel == null) {
            if (this.firstLevel != null && StringUtils.isNotBlank(this.firstLevel.getAdcode())) {
                return firstLevel;
            }
        }
        return null;
    }

    /**
     * 获取天气城市Id
     * 如果三级选择全境，该城市是直辖市，返回的adCode 是直辖市1级的adCode
     */
    public String getWeatherCityId() {
        if (thirdLevel != null && StringUtils.isNotBlank(this.thirdLevel.getAdcode())) {
            return thirdLevel.getAdcode();
        }

        if (this.thirdLevel == null) {
            if (secondLevel != null && StringUtils.isNotBlank(this.secondLevel.getAdcode())) {
                if (AreaDataDict.isSpecialFirstLevel(AreaDataDict.replaceCityAndProvinceString(secondLevel
                        .getCityName()))) {
                    return firstLevel.getAdcode();
                } else {
                    return secondLevel.getAdcode();
                }
            }
        }
        return "";
    }

    public void setCityId(String strCityId) {
        this.cityId = strCityId;
    }

    public CityItem getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(CityItem firstLevel) {
        this.firstLevel = firstLevel;
    }

    public CityItem getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(CityItem secondLevel) {
        this.secondLevel = secondLevel;
    }

    public CityItem getThirdLevel() {
        return thirdLevel;
    }

    public void setThirdLevel(CityItem thirdLevel) {
        this.thirdLevel = thirdLevel;
    }

    @Override
    public String toString() {
        return "AreaInfo [firstLevel=" + firstLevel + ", secondLevel=" + secondLevel + ", thirdLevel=" + thirdLevel
                + "]";
    }

}
