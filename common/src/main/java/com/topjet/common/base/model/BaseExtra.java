package com.topjet.common.base.model;

import android.util.Log;

import java.io.Serializable;

public abstract class BaseExtra extends BaseEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public static String sExtraName;

    public static String getExtraName() {
        return sExtraName;
    }

    public static void setExtraName(String extraName) {
        sExtraName = extraName;
    }

}
