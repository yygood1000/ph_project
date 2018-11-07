package com.topjet.common.common.modle.serverAPI;

import com.topjet.common.base.net.serverAPI.BaseCommand;
import com.topjet.common.base.view.activity.MvpActivity;

/**
 * creator: zhulunjun
 * time:    2017/11/9
 * describe: 诚信查询
 */

public class IntegrityInquiryCommand extends BaseCommand<IntegrityInquiryCommandAPI> {
    public IntegrityInquiryCommand(Class<IntegrityInquiryCommandAPI> c, MvpActivity mActivity) {
        super(c, mActivity);
    }


}
