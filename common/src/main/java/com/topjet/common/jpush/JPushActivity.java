package com.topjet.common.jpush;


import com.topjet.common.R;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.DelayUtils;

/**
 * creator: zhulunjun
 * time:    2017/11/23
 * describe: 一个透明的activity
 * 为了避免小米手机获取不到悬浮窗权限，
 * 而无法显示弹窗，
 * 在广播接收器中
 */

public class JPushActivity extends MvpActivity {

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_jpush;
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        noHasTitle();
        setNoAnim();
    }

    @Override
    public boolean isAddActivityStack() {
        return false;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        super.initData();
        final JPushBean bean = (JPushBean) getIntentExtra(JPushBean.getExtraName());
        if(bean != null) {
            JPushHelper.getInstance().handleData(JPushActivity.this, bean);
        }
    }
}
