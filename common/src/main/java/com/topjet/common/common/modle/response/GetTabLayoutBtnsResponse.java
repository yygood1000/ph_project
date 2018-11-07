package com.topjet.common.common.modle.response;

import com.topjet.common.common.modle.bean.TabLayoutInfo;

/**
 * Created by tsj028 on 2017/12/1 0001.
 */

public class GetTabLayoutBtnsResponse {
    private String version;
    private TabLayoutInfo navigation_one;
    private TabLayoutInfo navigation_two;
    private TabLayoutInfo navigation_three;
    private TabLayoutInfo navigation_four;
    private TabLayoutInfo navigation_five;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public TabLayoutInfo getNavigation_one() {
        return navigation_one;
    }

    public void setNavigation_one(TabLayoutInfo navigation_one) {
        this.navigation_one = navigation_one;
    }

    public TabLayoutInfo getNavigation_two() {
        return navigation_two;
    }

    public void setNavigation_two(TabLayoutInfo navigation_two) {
        this.navigation_two = navigation_two;
    }

    public TabLayoutInfo getNavigation_three() {
        return navigation_three;
    }

    public void setNavigation_three(TabLayoutInfo navigation_three) {
        this.navigation_three = navigation_three;
    }

    public TabLayoutInfo getNavigation_four() {
        return navigation_four;
    }

    public void setNavigation_four(TabLayoutInfo navigation_four) {
        this.navigation_four = navigation_four;
    }

    public TabLayoutInfo getNavigation_five() {
        return navigation_five;
    }

    public void setNavigation_five(TabLayoutInfo navigation_five) {
        this.navigation_five = navigation_five;
    }

    @Override
    public String toString() {
        return "GetTabLayoutBtnsResponse{" +
                "version='" + version + '\'' +
                ", navigation_one=" + navigation_one +
                ", navigation_tow=" + navigation_two +
                ", navigation_three=" + navigation_three +
                ", navigation_four=" + navigation_four +
                ", navigation_five=" + navigation_five +
                '}';
    }
}
