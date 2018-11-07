package com.topjet.common.utils;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
public class DisplayUtils {

    public static String getCNVersionName() {
        if (CMemoryData.isDriver()) {
            return ResourceUtils.getString(R.string.driver_version_title);
        } else {
            return ResourceUtils.getString(R.string.shipper_version_title);
        }
    }
}
