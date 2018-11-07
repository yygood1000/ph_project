package com.topjet.crediblenumber;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.topjet.common.adv.modle.bean.BannerBean;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 启动页
 */
public class SplashActivity extends MvpActivity<SplashPresenter> implements SplashView {
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.iv_adv)
    ImageView ivAdv;
    @BindView(R.id.iv_splash_logo)
    ImageView ivLogo;


    @Override
    protected int getLayoutResId() {
        noHasTitle(); // 不要titlebar、在返回布局前加这一句
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new SplashPresenter(this, mContext);
    }

    @Override
    public void initView() {
    }

    @Override
    protected void initData() {
        mPresenter.startTranslateAnim(ivLogo);
    }

    @OnClick({R.id.tv_skip, R.id.iv_adv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
                mPresenter.skip();
                mPresenter.mTimeDsposable.dispose();
                break;
            case R.id.iv_adv:
                if (null != mPresenter.getSplashAdvInfo() && StringUtils.isNotBlank(mPresenter.getSplashAdvInfo()
                        .getRedirect_url())) {
                    Uri uri = Uri.parse(mPresenter.getSplashAdvInfo().getRedirect_url());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
//                    SimpleWebViewActivity.turnToSimpleWebViewActivity(this, mPresenter.getSplashAdvInfo()
//                                    .getRedirect_url(),
//                            mPresenter.getSplashAdvInfo().getWeb_title());
                }
                break;
        }
    }


    /**
     * 加载gif图片
     *
     * @param splashAdvInfo 图片信息
     */
    @Override
    public void loadSplashGif(BannerBean splashAdvInfo) {
        GlideUtils.loadSplashGif(splashAdvInfo.getPicture_url(), splashAdvInfo.getPicture_key(), new
                GlideDrawableImageViewTarget(ivAdv) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable>
                            glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        onLoadImageFinish();
                        // 图片加载完成
                        ivAdv.setImageDrawable(resource);
                    }
                });
    }

    /**
     * 加载图片
     *
     * @param splashAdvInfo 图片信息
     */
    @Override
    public void loadSplashImg(BannerBean splashAdvInfo) {
        Logger.d("loadSplashImg ");
        GlideUtils.loadSplashImage(splashAdvInfo.getPicture_url(),
                splashAdvInfo.getPicture_key(), new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable bitmap, GlideAnimation glideAnimation) {
                        onLoadImageFinish();
                        Logger.d("loadSplashImg 11");
                        // 设置广告图片
                        ivAdv.setImageDrawable(bitmap);
                    }
                });
    }

    @Override
    public void setSkipView(int visible) {
        tvSkip.setVisibility(visible);
    }

    /**
     * 图片加载结束公共的处理
     */
    private void onLoadImageFinish() {
        // 广告图片渐显动画
        ivAdv.startAnimation(mPresenter.getAlphaAnim());
        setSkipView(View.VISIBLE);
        // 开始计时5秒
        mPresenter.startTheTime();
        //进行权限获取，但是不在获取成功后进行跳转
        mPresenter.initPermissionAndGoMain(false);
    }


}
