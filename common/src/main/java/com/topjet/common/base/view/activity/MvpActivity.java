package com.topjet.common.base.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.topjet.common.R;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.model.BaseExtra;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.common.view.dialog.NetRequestDialog;
import com.topjet.common.config.CConstants;
import com.topjet.common.config.RespectiveData;
import com.topjet.common.utils.AppManager;
import com.topjet.common.utils.LayoutUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.MemoryUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.SystemUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.common.widget.MyTitleBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;


/**
 * 如果需要启用MVP模式开发，Activity相关的就继承该基类
 * Created by yy on 2017/7/24.
 */

public abstract class MvpActivity<T extends BasePresenter> extends RxAppCompatActivity implements IView,
        View.OnClickListener {
    public T mPresenter;
    public Context mContext;
    public RxAppCompatActivity RxAppCompatActivity;
    private boolean isHasTitleBar = true;
    private MyTitleBar myTitleBar = null;
    public boolean isFragmentActivity;
    public String TAG;
    public NetRequestDialog netRequestDialog = null;

    /**
     * 需要子类来实现，获取子类的IPresenter，一个activity有可能有多个IPresenter
     */
    protected BasePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = (T) new BasePresenter(this);
        }
        return mPresenter;

    }

    /**
     * 初始化presenters,由子类实现
     */
    protected abstract void initPresenter();

    /**
     * 获取layout的id，由子类实现
     */
    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected void initData() {

    }

    protected void initTitle() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // App 被系统回收，App跳转到启动页重新开启
        if (savedInstanceState != null) {
            finish();
            AppManager.getInstance().finishAllActivity();
            turnToActivity(RespectiveData.getSplashActivityClassClass());
            return;
        }

        // 初始化toolbar.和布局
        dependToSetContentView();

        // 使用ButterKnife注入,先加载布局，后绑定ButterKnife。再改顺序自杀
        ButterKnife.bind(this);
        mContext = this;
        RxAppCompatActivity = this;
        TAG = RxAppCompatActivity.getClass().toString();
        if (isAddActivityStack()) {
            AppManager.getInstance().addActivity(this);
        }

        initTitle();
        initPresenter();
        getPresenter().onCreate();// 这里这么写是因为，如果有需要在onCreate生命周期中执行的业务，可写在对应的Presenter中
        initView();
        initData();
        initRxBus();
        setActivityAnimation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        Logger.d("当前页面", this.getClass().getName());
    }

    /**
     * 初始化BusManager
     */
    private void initRxBus() {
        BusManager.getBus().register(this);
    }

    /**
     * 设置布局，若有titleBar则设置默认监听
     */
    protected void dependToSetContentView() {
        int layoutResId = getLayoutResId();
        if (layoutResId > 0) {
            LinearLayout root = new LinearLayout(this);
            LayoutUtils.setViewGroupParams(root, LayoutUtils.MATCH, LayoutUtils.MATCH);
            root.setOrientation(LinearLayout.VERTICAL);
            if (isHasTitleBar) {
                myTitleBar = new MyTitleBar(this);
                root.addView(myTitleBar);

                myTitleBar.getmBack().setOnClickListener(this);
                myTitleBar.getCancel().setOnClickListener(this);
                myTitleBar.getmIvRight().setOnClickListener(this);
                myTitleBar.getmTvRight().setOnClickListener(this);
            }
            View content = View.inflate(this, getLayoutResId(), null);
            LayoutUtils.setLinearParams(content, LayoutUtils.MATCH, LayoutUtils.MATCH);
            root.addView(content);

            setContentView(root);
        }
    }


    /**
     * 设置状态栏的颜色
     */
    public void setStatusBarViewColorResource(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19();
        }

        if (isHasTitleBar) {
//            this.findViewById(R.id.my_title_bar).setBackgroundResource(color);
            this.findViewById(R.id.view_statusbar_fill).setBackgroundResource(color);
        }
    }

    /**
     * 设置状态栏的颜色
     */
    public void setStatusBarViewDrawableResource(int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarUpperAPI21();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setStatusBarUpperAPI19();
        }
        if (isHasTitleBar) {
//            this.findViewById(R.id.my_title_bar).setBackgroundDrawable(ResourceUtils.getDrawable(drawableId));
            this.findViewById(R.id.view_statusbar_fill).setBackgroundDrawable(ResourceUtils.getDrawable(drawableId));
        }
    }

    // 5.0版本以上
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarUpperAPI21() {
        Window window = getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        // API21才支持
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    // 4.4 - 5.0版本
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setStatusBarUpperAPI19() {
        Window window = getWindow();
        // API19 才支持
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View statusBarView = mContentView.getChildAt(0);
        //移除假的 View
        if (statusBarView != null && statusBarView.getLayoutParams() != null &&
                statusBarView.getLayoutParams().height == getStatusBarHeight()) {
            mContentView.removeView(statusBarView);
        }
        //不预留空间
        if (mContentView.getChildAt(0) != null) {
            ViewCompat.setFitsSystemWindows(mContentView.getChildAt(0), false);
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    /**
     * 没有titlebar时调用这个函数
     */
    public void noHasTitle() {
        isHasTitleBar = false;
    }

    /**
     * 获取标题栏
     */
    public MyTitleBar getMyTitleBar() {
        return myTitleBar;
    }

    /**
     * TitleBar 点击事件
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_title_bar_back || id == R.id.iv_title_bar_cancel) {
            onClickBack();
        } else if (id == R.id.tv_title_bar_right) {
            onClickRightText(v);
        } else if (id == R.id.iv_title_bar_right) {
            onClickRightImg(v);
        }
    }

    /**
     * 点击返回按钮
     */
    protected void onClickBack() {
        onBackPressed();
    }

    /**
     * 点击右侧文字
     */

    protected void onClickRightText(View v) {
        onClickRightText();
    }

    protected void onClickRightText() {
    }

    /**
     * 点击右侧图片
     */
    protected void onClickRightImg(View v) {
        onClickRightImg();
    }

    protected void onClickRightImg() {
    }

    /* ==========================================生命周期相关 start========================================== */

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onResume();
        //  友盟统计,如果不是装有Fragment的Activity则直接记录时间。否在该方法由Fragment调用。
        if (!isFragmentActivity) {
            MobclickAgent.onPageStart(getPresenter().mActivity.getClass().toString());
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().onPause();
        //  友盟统计时长，如果不是装有Fragment的Activity则直接记录时间。否在该方法由Fragment调用。
        if (!isFragmentActivity) {
            MobclickAgent.onPageEnd(getPresenter().mActivity.getClass().toString());
        }
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();// 目前BasePresenter.onDestroy() 写解绑方法
        //页面销毁时清除activity栈中的对象，防止内存泄漏
        AppManager.getInstance().finishActivity(this);
        BusManager.getBus().unregister(this);
        // 当内存剩余小于40%时，进行手动GC
        if (MemoryUtils.getMemoryPercent() < CConstants.MIN_FREE_MEMORY_PERSENT) {
            System.gc();
            System.runFinalization();
        }

        recycleLoadingDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().onActivityResult(requestCode, resultCode, data);
    }

    /* ==========================================生命周期相关 end========================================== */

    /**
     * toast默认的展示方法，如果有特殊要求，在子类中重写该方法
     */
    public void showToast(String msg) {
        Toaster.showShortToast(msg);
    }



    /*
     * ============================页面跳转相关方法============================
     */

    /**
     * 跳转至下个页面
     */
    public void turnToActivity(Class className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
    }

    /**
     * 跳转至下个页面并传参
     */
    public void turnToActivity(Class className, Bundle bundle) {
        Intent intent = new Intent(this, className);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转至下个页面并传参
     *
     * @param extra 传递的实体类
     */
    public <E extends BaseExtra> void turnToActivity(Class className, E extra) {
        Bundle bundle = new Bundle();
        if (E.getExtraName() == null) {
            E.setExtraName(SystemUtils.nextExtraName());
        }
        bundle.putSerializable(E.getExtraName(), extra);
        turnToActivity(className, bundle);
    }

    /**
     * 跳转至下个页面，并传参，并销毁
     */
    public void turnToActivityWithFinish(Class className, Bundle bundle) {
        turnToActivity(className, bundle);
        this.finish();
    }

    /**
     * 跳转至下个页面并销毁
     */
    public void turnToActivityWithFinish(Class className) {
        Intent intent = new Intent(this, className);
        startActivity(intent);
        this.finish();
    }

    /**
     * 跳转至下个页面ForResult
     */
    public void turnToActivityForResult(Class className, int requestCode) {
        Intent intent = new Intent(this, className);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转至下个页面并传参ForResult
     */
    public void turnToActivityForResult(Class className, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, className);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转至下个页面并传参ForResult
     *
     * @param extra 需要传递的实体类
     */
    public <E extends BaseExtra> void turnToActivityForResult(Class className, int requestCode, E extra) {
        Bundle bundle = new Bundle();
        if (E.getExtraName() == null) {
            E.setExtraName(SystemUtils.nextExtraName());
        }
        bundle.putSerializable(E.getExtraName(), extra);
        turnToActivityForResult(className, requestCode, bundle);
    }

    /**
     * 设置返回数据 ，并销毁
     */
    public <E extends BaseExtra> void setResultAndFinish(int resultCode, E extra) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (E.getExtraName() == null) {
            E.setExtraName(SystemUtils.nextExtraName());
        }
        bundle.putSerializable(E.getExtraName(), extra);
        intent.putExtras(bundle);
        setResult(resultCode, intent);
        this.finish();
    }

    /**
     * 设置返回数据 ，并销毁
     */
    public void setResultAndFinish(int resultCode) {
        setResult(resultCode);
        this.finish();
    }

    /**
     * 通过extraName获取传参
     *
     * @param extraName getExtraName()方法获取
     */
    public <E extends BaseExtra> E getIntentExtra(String extraName) {
        return getIntentExtra(getIntent(), extraName);
    }

    /**
     * 在OnActivityResult()中获取Extra的方法
     *
     * @param intent    OnActivityResult方法中返回的Intent
     * @param extraName 通过Extra.getExtraName()获取
     */
    public <E extends BaseExtra> E getIntentExtra(Intent intent, String extraName) {
        if (intent != null) {
            return intent.getExtras() != null ? (E) intent.getExtras().getSerializable(extraName) : null;
        }
        return null;
    }

    /**
     * Presenter中调用关闭页面的方法
     */
    @Override
    public void finishPage() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        setActivityAnimation(1);
    }

    public void setNoAnim() {
        isShowAnim = false;
    }

    /**
     * 设置activity转场动画
     */
    public boolean isShowAnim = true; // 是否显示动画

    public void setActivityAnimation(int flag) {
        if (!isShowAnim) {
            if (flag == 0) {  //进入
                overridePendingTransition(R.anim.anim_no, R.anim.anim_no);
            } else {     //退出
                overridePendingTransition(R.anim.anim_no, R.anim.anim_no);
            }
            return;
        }
        if (myTitleBar != null) {
            int va = myTitleBar.getmMode().getIntValue();
            if (va % 2 != 0) {
                if (flag == 0) {  //进入
                    overridePendingTransition(R.anim.slide_in_bottom, com.topjet.common.R.anim.anim_no);
                } else {     //退出
                    overridePendingTransition(com.topjet.common.R.anim.anim_no, R.anim.slide_out_bottom);
                }
                return;
            }
        }
        if (flag == 0) { //进入
            overridePendingTransition(com.topjet.common.R.anim.slide_in_right, com.topjet.common.R.anim.anim_no);
        } else {        //退出
            overridePendingTransition(com.topjet.common.R.anim.anim_no, R.anim.slide_out_right);
        }
    }

    /**
     * 展示/收起加载框Dialog
     */
    @Override
    public void showLoadingDialog() {
        if (netRequestDialog == null) {
            netRequestDialog = new NetRequestDialog(this);
        }
        if(!netRequestDialog.isShowing()) {
            netRequestDialog.show();
        }
    }

    /**
     * 关闭加载框
     */
    @Override
    public void cancelShowLoadingDialog() {
        if (netRequestDialog != null) {
            netRequestDialog.dismiss();
        }
    }

    /**
     * 回收Dialog
     */
    private void recycleLoadingDialog() {
        if (netRequestDialog != null) {
            netRequestDialog.dismiss();
            netRequestDialog = null;
        }
    }


    /**
     * 是否添加到activity栈中
     */
    public boolean isAddActivityStack() {
        return true;
    }

}
