package com.topjet.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.NetStatusUtil;
import com.topjet.common.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



/**
 * WebView 自定义错误界面。
 * JS 交互总结
 * http://www.jianshu.com/p/a25907862523
 * 错误页面加载
 * http://blog.csdn.net/u010899138/article/details/52180280
 */

public class MyWebView extends WebView {
    private Context mContext;

    /* ==============================默认属性配置相关===================================*/
    /* 进度条进度默认的颜色--深天蓝*/
    private static final int DEF_BAR_COLOR_REACH = 0xFF00BFFF;
    /* 进度条默认的高度*/
    private static final int DEF_BAR_HEIGHT = 2;// dp
    /* 错误显示文字字体大小*/
    private static final int DEF_ERROR_TEXT_SIZE = 12;// sp
    /* 错误显示文字内容*/
    private static final String DEF_ERROR_TEXT = "加载失败，请点击刷新";
    /* 错误按钮显示文字内容*/
    private static final String DEF_ERROR_BTN_TEXT = "点击刷新";
    /* 错误显示文字颜色 */
    private static final int DEF_ERROR_TEXT_COLOR = 0xFF222222;// 黑色
    /* 错误按钮显示文字颜色 */
    private static final int DEF_ERROR_BTN_TEXT_COLOR = 0xFF6E90FF;// 蓝色主题色
    /* 错误按钮显示文字字体大小 */
    private static final int DEF_ERROR_BTN_TEXT_SIZE = 16;// sp
    /* ==============================默认属性配置相关 end===================================*/

    /* ==============================进度条相关变量start===================================*/
    /* 网页加载进度条 */
    private ProgressBar mProgressBar;
    /* 进度条进度的颜色 */
    private int mProgressColor;
    /* 进度条高度 */
    private int mProgressHeight;

    /* ==============================进度条相关变量end===================================*/

    /* ==============================WebView相关变量===================================*/
    /* 自定义的Url历史记录--不存储错误页面mUrlErrorStart的地址 */
    private List<String> mUrlList;

    private String mMimeType = "text/html";
    /* 编码 */
    private String mEncoding = "UTF-8";
    /* 网页加载错误时加载空白页 */
    private String mUrlErrorStart = "about:blank";
    /* ==============================WebView相关变量===================================*/

    /* ==============================WebView加载错误相关变量===================================*/
    /* WebView显示错误页面时候，需要设置一个空字符串，否则不同的手机有可能会出现不同的文字 */
    private String mHintFail = "";

    /* 错误提醒文字大小 */
    private int mErrorTextSize;

    /* 错误页面提醒文字 */
    private String mErrorText;

    /* 错误按钮提醒文字大小 */
    private int mErrorBtnTextSize;
    /* 错误按钮文字 */
    private String mErrorBtnText;
    /* 错误页面 整体布局*/
    private View loadFailLayout;

    /* ==============================WebView加载错误相关变量===================================*/


    private OnLoadFinishListener loadFinishListener;
    private OnLoadErrorListener loadErrorListener;

    /**
     * 设置加载完成回调
     */
    public void setOnLoadFinishListener(OnLoadFinishListener loadFinishListener) {
        this.loadFinishListener = loadFinishListener;
    }

    /**
     * 设置加载完成回调
     */
    public void setOnLoadErrorListener(OnLoadErrorListener loadErrorListener) {
        this.loadErrorListener = loadErrorListener;
    }

    /**
     * 页面加载完成回调
     */
    public interface OnLoadFinishListener {
        void onLoadFinish();
    }

    /**
     * 页面加载失败回调
     */
    public interface OnLoadErrorListener {
        void onError();
    }

    public MyWebView(Context context) {
        this(context, null);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mUrlList = new LinkedList<>();

        //声明WebSettings子类
        WebSettings webSettings = this.getSettings();

        this.setWebViewClient(mWebViewClient);
        this.setWebChromeClient(mWebChromeClient);

        // 设置编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        // 阻止图片网络数据
        webSettings.setBlockNetworkImage(false);

         /*
            设置缓存模式
            http://www.jianshu.com/p/3c94ae673e2a
            LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
            LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
            LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
            LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);//设置最大缓存10M

        //设置  Application Caches 缓存目录
        webSettings.setAppCachePath(mContext.getDir("cache", Context.MODE_PRIVATE).getAbsolutePath());
        webSettings.setDatabasePath(mContext.getDir("database", Context.MODE_PRIVATE).getAbsolutePath());

        if (NetStatusUtil.isNetworkAvailable(mContext)) {
            //根据cache-control决定是否从网络上取数据。
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        /*
            设置硬件加速相关
            http://blog.csdn.net/hqdoremi/article/details/8307496
            LAYER_TYPE_SOFTWARE  无论硬件加速是否打开，都会有一张Bitmap（software layer），并在上面对WebView进行软渲染。
            LAYER_TYPE_HARDWARE  硬件加速关闭时，作用同software
            LAYER_TYPE_NONE      这个就比较简单了，不为这个View树建立单独的layer
         */
        this.setLayerType(View.LAYER_TYPE_NONE, null);
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 设置背景颜色 透明
        this.setBackgroundColor(Color.argb(0, 0, 0, 0));

        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
        this.setLongClickable(true);

        // 加载自定义属性
        obtainStyledAttributes(attrs, defStyle);

        // 初始化加载失败布局
        initLoadFailView(mContext);

        // 初始化ProgressBar
        initProgressBar();
    }

//    /**
//     * 加载自定义属性
//     */
    private void obtainStyledAttributes(AttributeSet attrs, int defStyle) {
//        TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.myWebView, defStyle, 0);
//        // ProgressBar 颜色
//        mProgressColor = ta.getColor(R.styleable.myWebView_progress_color, DEF_BAR_COLOR_REACH);
//        // ProgressBar 高度
//        mProgressHeight = ta.getDimensionPixelSize(R.styleable.myWebView_progress_height, ScreenUtils.dp2px(mContext,DEF_BAR_HEIGHT));
//
//        // 错误页面 提示信息 字体大小
//        mErrorTextSize = ta.getDimensionPixelSize(R.styleable.myWebView_error_text_size,
//                ScreenUtils.sp2px(mContext, DEF_ERROR_TEXT_SIZE));
//        // 错误页面 提示信息内容
//        mErrorText = ta.getString(R.styleable.myWebView_error_text);
//        if (TextUtils.isEmpty(mErrorText)) {
//            mErrorText = DEF_ERROR_TEXT;
//        }
//
//        // 错误页面 按钮 字体大小
//        mErrorBtnTextSize = ta.getDimensionPixelSize(R.styleable.myWebView_error_btn_text_size,
//                ScreenUtils.sp2px(mContext, DEF_ERROR_BTN_TEXT_SIZE));
//        // 错误页面 按钮内容
//        mErrorBtnText = ta.getString(R.styleable.myWebView_error_btn_text);
//        if (TextUtils.isEmpty(mErrorText)) {
//            mErrorBtnText = DEF_ERROR_BTN_TEXT;
//        }
    }

    /**
     * 初始化加载失败布局
     */
    private void initLoadFailView(Context mContext) {
        loadFailLayout = View.inflate(mContext, R.layout.layout_webview_load_fail, null);
        TextView mTvError = (TextView) loadFailLayout.findViewById(R.id.tv_error);
        Button mBtnError = (Button) loadFailLayout.findViewById(R.id.btn_error);

//        // 设置错误信息文本框属性
//        mTvError.setText(mErrorText);
//        mTvError.setTextSize(mErrorTextSize);
//
//        // 设置错误信息按钮属性
//        mBtnError.setText(mErrorBtnText);
//        mBtnError.setTextSize(mErrorBtnTextSize);
        if (CMemoryData.isDriver()) {
            mBtnError.setTextColor(ResourceUtils.getColorStateList(R.color.selector_textcolor_empty_to_blue));
            mBtnError.setBackground(ResourceUtils.getDrawable(R.drawable.selector_btn_empty_to_blue));
        } else {
            mBtnError.setTextColor(ResourceUtils.getColorStateList(R.color.selector_textcolor_empty_to_green));
            mBtnError.setBackground(ResourceUtils.getDrawable(R.drawable.selector_btn_empty_to_green));
        }

        // 失败按钮点击事件。这里直接写成重新加载页面
        mBtnError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getLastUrl();// 获取最后加载url，并进行重新加载
                MyWebView.this.loadUrl(url);
            }
        });
        loadFailLayout.setVisibility(View.GONE);
        this.addView(loadFailLayout);
    }

    /**
     * 初始化进度条
     */
    private void initProgressBar() {
        mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(android.widget.RelativeLayout
                .LayoutParams.MATCH_PARENT, mProgressHeight));

        ClipDrawable clipDrawable = new ClipDrawable(new ColorDrawable(mProgressColor), Gravity.LEFT, ClipDrawable
                .HORIZONTAL);
        mProgressBar.setProgressDrawable(clipDrawable);

        this.addView(mProgressBar);
    }

    /**
     * 处理各种通知 & 请求事件
     */
    WebViewClient mWebViewClient = new WebViewClient() {
        // 打开网页时不调用系统浏览器， 而是在本WebView中显示；在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.d("TTT", "WebView Load shouldOverrideUrlLoading");
            view.loadUrl(url);
            return true;
        }

        // 开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.d("TTT", "WebView Load onPageStarted");
        }

        // 在页面加载结束时调用。
        @Override
        public void onPageFinished(WebView view, String url) {
            Logger.d("TTT", "WebView Load onPageFinished  url = " + url);
            // 在onReceivedError 中知道了错误，手动加载空白页，从而错误的布局
            if (url.startsWith(mUrlErrorStart)) {
                loadFailLayout.setVisibility(View.VISIBLE);
            } else {
//                // 对外提供的加载完成接口
                if (loadFinishListener != null) {
                    loadFinishListener.onLoadFinish();
                }
                // 对外提供的加载完成接口
                mProgressBar.setVisibility(GONE);
                // 检查返回URL，如果与第一个重复则删除集合第一个
                checkBack(url);
                // 在url列表中添加url
                addUrlList(url);
            }
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
        }

//        // 加载页面的服务器出现错误时（如404）调用
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String url) {
//            Logger.d("TTT", "WebView Load onReceiveError errorCode = " + errorCode);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                return;
//            }
//            // 加载错误时，进行错误页面的加载，该方式是为了在错误时统一跳转空白页，从而显示错误页面
//            loadDataWithBaseURL(null, mHintFail, mMimeType, mEncoding, null);
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//            if (request.isForMainFrame()) {// 在这里加上个判断
//                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), request.getUrl()
//                        .toString());
//                // 显示错误界面
//                loadFailLayout.setVisibility(View.VISIBLE);
//            }
//        }
    };

    /**
     * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
     */
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            mProgressBar.setProgress(progress);
            if (progress == 100) {
                if (mProgressBar.getVisibility() == VISIBLE) {
                    // 延迟隐藏进度条
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mProgressBar.setVisibility(GONE);
                        }
                    }, 100);
                }
            } else {
                mProgressBar.setVisibility(VISIBLE);
            }
            super.onProgressChanged(view, progress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            Logger.d("网页错误 " + title);
            super.onReceivedTitle(view, title);
            // 通过title获取
            if (title.contains("404") || title.contains("500") || title.contains("505") || title.contains("Error")|| title.contains("网页无法打开")) {
                loadFailLayout.setVisibility(View.VISIBLE);
                if (loadErrorListener != null) {
                    loadErrorListener.onError();
                }
            } else {
                loadFailLayout.setVisibility(View.GONE);
            }
        }
    };

    private Handler handler = new Handler();

    /**
     * 检查是否可以回退
     */
    private void checkBack(String url) {
        String backUrl = getBackUrl();
        if (TextUtils.equals(url, backUrl))
            removeUrlTop();
    }

    /**
     * url页面回退(重写webView的goBack方法)，并删除上一级url
     */
//    @Override
//    public void goBack() {
//        String url = getBackUrl();
//        this.loadUrl(url);
//        removeUrlTop();
//    }


    /**
     * 移除url列表顶部的url
     *
     * @return url列表
     */
    private List<String> removeUrlTop() {
        if (mUrlList != null && mUrlList.size() > 1) {
            int size = mUrlList.size();
            mUrlList.remove(size - 1);
        }
        return mUrlList;
    }

    /**
     * 在url列表中添加url
     *
     * @param url 当前url
     * @return url列表
     * <p>
     * 添加规则：
     * 1.错误页面(about:blank)不添加
     * 2.顶部页面相同不添加(不重复添加url--这个添加url列表的操作是在onPageFinished()中进行，有些手机在页面访问不成功会执行两次这个回调函数)
     */
    private List<String> addUrlList(String url) {
        if (TextUtils.isEmpty(url))
            return mUrlList;
        if (url.startsWith(mUrlErrorStart))// 如果是错误页面显示的地址，则不添加到url列表中
            return mUrlList;
        if (null == mUrlList)
            mUrlList = new ArrayList<String>();
        int size = mUrlList.size();
        if (size == 0)
            mUrlList.add(url);
        else if (!TextUtils.equals(getLastUrl(), url))
            mUrlList.add(url);
        return mUrlList;
    }

    /**
     * 获取url列表顶部的url(最后访问的地址)
     *
     * @return 最顶部的url
     */
    private String getLastUrl() {
        if (null == mUrlList || mUrlList.size() == 0)
            return null;
        int size = mUrlList.size();
        return mUrlList.get(size - 1);
    }

    /**
     * 获取上一页的url
     *
     * @return 上一页的url，如果只有一个页面，则返回当前页面的url
     */
    private String getBackUrl() {
        if (mUrlList == null)
            return null;
        int size = mUrlList.size();
        if (size == 0)
            return null;
        else if (size == 1)
            return mUrlList.get(0);
        else
            return mUrlList.get(size - 2);
    }

//    /**
//     * 判断是否可以回退
//     * 1.如果自定义url列表只有一个对象的时候，则表示不能回退了
//     * 2.其他情况，调用WebView的super方法进行判定
//     */
//    @Override
//    public boolean canGoBack() {
//        if (mUrlList != null && mUrlList.size() == 1) {
//            return false;
//        }
//        return super.canGoBack();
//    }


    /* =====================================对外提供的API方法===================================== */

    /**
     * 设置进度条进度的颜色
     *
     * @param color 色值
     */
    public void setBarReachColor(int color) {
        mProgressColor = color;
        ClipDrawable clipDrawable = new ClipDrawable(new ColorDrawable(mProgressColor), Gravity.LEFT, ClipDrawable
                .HORIZONTAL);
        mProgressBar.setProgressDrawable(clipDrawable);
    }

    /**
     * 隐藏加载进度条
     */
    public void hideProgress() {
        mProgressBar.setVisibility(GONE);
    }
}