package com.topjet.common.widget.bottomlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.base.listener.OnTabClickListener;
import com.topjet.common.common.modle.bean.TabLayoutInfo;
import com.topjet.common.common.modle.response.GetTabLayoutBtnsResponse;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.widget.bottomlayout.download.DownLoadImageManager;
import com.topjet.common.widget.bottomlayout.download.DownLoadImageService;
import com.topjet.common.widget.bottomlayout.download.ImageDownLoadCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/8/10.
 * <p>
 * 底部导航
 */

public class TabLayout extends RelativeLayout {
    private RelativeLayout rlTabOne, rlTabTwo, rlTabThree, rlTabFour;
    private TextView tvTabOne, tvTabTwo, tvTabThree, tvTabFour, tvTabThreeCorner;
    private ImageView ivTabOne, ivTabTwo, ivTabThree, ivTabFour, ivCenter;
    private int currentIndex = -1;
    private int lastIndex = -1;
    private boolean isFirstSet = true;

    private int SEARCH_GOODS = 1;// 找货标签
    private int LISTENER_ORDER = 2;// 听单标签
    private int centerBtnFlag = 1;// 中间图标的标记，默认处于1


    private OnTabClickListener mOnTabClickListener;

    private GetTabLayoutBtnsResponse mTabLayoutBtnsResponse;

    /**
     * 文本控件集合
     */
    private List<TextView> itemTexts = new ArrayList<>();
    /**
     * 图片控件集合
     */
    private List<ImageView> itemImages = new ArrayList<>();
    /**
     * 数据集合
     */
    private List<TabLayoutBean> datas = new ArrayList<>();

    /**
     * 是否使用网络或者缓存数据
     */
    private boolean useNetData = false;

    public TabLayout(Context context) {
        super(context);
        this.initView(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        Logger.i("oye", "initView tablayout");
        View view = View.inflate(context, R.layout.layout_tabbar, this);

        rlTabOne = (RelativeLayout) view.findViewById(R.id.rl_tab_one);
        rlTabTwo = (RelativeLayout) view.findViewById(R.id.rl_tab_two);
        rlTabThree = (RelativeLayout) view.findViewById(R.id.rl_tab_three);
        rlTabFour = (RelativeLayout) view.findViewById(R.id.rl_tab_four);
        tvTabOne = (TextView) findViewById(R.id.tv_tab_one);
        tvTabTwo = (TextView) findViewById(R.id.tv_tab_two);
        tvTabThree = (TextView) findViewById(R.id.tv_tab_three);
        tvTabFour = (TextView) findViewById(R.id.tv_tab_four);
        tvTabThreeCorner = (TextView) findViewById(R.id.tv_tab_three_corner);
        ivTabOne = (ImageView) findViewById(R.id.iv_tab_one);
        ivTabTwo = (ImageView) findViewById(R.id.iv_tab_two);
        ivTabThree = (ImageView) findViewById(R.id.iv_tab_three);
        ivTabFour = (ImageView) findViewById(R.id.iv_tab_four);
        ivCenter = (ImageView) findViewById(R.id.iv_center);

        rlTabOne.setOnClickListener(clickListener);
        rlTabTwo.setOnClickListener(clickListener);
        rlTabThree.setOnClickListener(clickListener);
        rlTabFour.setOnClickListener(clickListener);
        ivCenter.setOnClickListener(clickCenterImageListener);

        itemTexts.add(tvTabOne);
        itemTexts.add(tvTabTwo);
        itemTexts.add(tvTabThree);
        itemTexts.add(tvTabFour);

        itemImages.add(ivTabOne);
        itemImages.add(ivTabTwo);
        itemImages.add(ivCenter);
        itemImages.add(ivTabThree);
        itemImages.add(ivTabFour);

        initData();

    }

    /**
     * 初始化数据
     * 下载图片
     */
    private void initData(){
        mTabLayoutBtnsResponse = CPersisData.getTabLayoutBtnsInfo();
        if (null != mTabLayoutBtnsResponse && isFullData() && isActionTime()) {
            Logger.i("oye", "initView mTabLayoutBtnsResponse === " + mTabLayoutBtnsResponse.toString());
            downLoadImage(mTabLayoutBtnsResponse.getNavigation_one().getBean());
            downLoadImage(mTabLayoutBtnsResponse.getNavigation_two().getBean());
            downLoadImage(mTabLayoutBtnsResponse.getNavigation_three().getBean());
            downLoadImage(mTabLayoutBtnsResponse.getNavigation_four().getBean());
            downLoadImage(mTabLayoutBtnsResponse.getNavigation_five().getBean());
        } else {
            defaultUI();
        }
    }

    /**
     * 不为空
     * @return 是否所有数据都不为空
     */
    private boolean isFullData(){
        return mTabLayoutBtnsResponse.getNavigation_one() != null &&
                mTabLayoutBtnsResponse.getNavigation_two() != null &&
                mTabLayoutBtnsResponse.getNavigation_three() != null &&
                mTabLayoutBtnsResponse.getNavigation_four() != null &&
                mTabLayoutBtnsResponse.getNavigation_five() != null;
    }

    /**
     * 是否在活动时间内
     * 在活动时间内才显示
     * @return 是否
     */
    private boolean isActionTime(){
        return TimeUtils.isActionTime(mTabLayoutBtnsResponse.getNavigation_one().getBegin_date(),
                mTabLayoutBtnsResponse.getNavigation_one().getEnd_date());
    }

    /**
     * 下载图片
     * @param bean 数据
     */
    private void downLoadImage(final TabLayoutBean bean){
        DownLoadImageService service = new DownLoadImageService(getContext(),
                bean.getIcon_normal_url(),
                bean.getIcon_down_url(),
                new ImageDownLoadCallBack() {
                    @Override
                    public void onDownLoadSuccess(Drawable icon, Drawable selectIcon) {
                        bean.setIcon_down(selectIcon);
                        bean.setIcon_normal(icon);

                        datas.add(bean);
                        // 全部设置完成，则绑定数据
                        if(datas.size() == 5){
                            useNetData = true;
                            initUI();
                        }
                    }

                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                        Logger.d("图片保存失败");
                    }
                });

        // 启动下载线程
        DownLoadImageManager.runOnQueue(service);
    }

    /**
     * 根据版本初始化文本框selector
     */
    private void initUI() {
        if (useNetData) {
            // 使用缓存图标和字体颜色
            Logger.d("oye", "initUI==============缓存");
            // 设置文本数据
            for (int i = 0; i < itemTexts.size(); i++){
                TabLayoutBean info = datas.get(i > 1 ? i + 1 : i);
                TextView text = itemTexts.get(i);

                text.setText(info.getTitle());
                setSelectorColor(text ,
                        Color.parseColor(info.getTitle_color_normal()),
                        Color.parseColor(info.getTitle_color_down()));
            }

            // 设置图片数据
            for(int i = 0; i < itemImages.size(); i++){
                TabLayoutBean info = datas.get(i);
                ImageView imageView = itemImages.get(i);
                // 中间按钮
                if(i == 2) {
                    imageView.setBackgroundDrawable(info.getIcon_normal());
                } else {
                    imageView.setBackgroundDrawable(getSelector(info.getIcon_normal(), info.getIcon_down()));
                }
            }

        } else {
            Logger.d("oye", "initUI==============默认");
            defaultUI();
        }
    }

    /**
     * 默认图标
     */
    private void defaultUI(){
        if (!CMemoryData.isDriver()) {
            for (int i=0;i<itemTexts.size();i++){
                TextView textView = itemTexts.get(i);
                textView.setTextColor(getContext().getResources().getColorStateList(R.color
                        .selector_text_color_tabbar_shipper));
            }
            ivTabOne.setBackgroundResource(R.drawable.selector_tl_home_shipper);
            ivTabTwo.setBackgroundResource(R.drawable.selector_tl_order_shipper);
            ivTabThree.setBackgroundResource(R.drawable.selector_tl_message_shipper);
            ivTabFour.setBackgroundResource(R.drawable.selector_tl_my_shipper);

            ivCenter.setBackgroundResource(R.drawable.tabbar_ship);
        }
    }

    /**
     * 四个标签点击事件
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (lastIndex != currentIndex) {
                lastIndex = currentIndex;
            }
            if (id == R.id.rl_tab_one) {
                Logger.d("oye", "rl_tab_one");
                currentIndex = 0;
            } else if (id == R.id.rl_tab_two) {
                Logger.d("oye", "rl_tab_two");
                currentIndex = 1;
            } else if (id == R.id.rl_tab_three) {
                Logger.d("oye", "rl_tab_three");
                currentIndex = 2;
            } else if (id == R.id.rl_tab_four) {
                Logger.d("oye", "rl_tab_four");
                currentIndex = 3;
            }
            if (CMemoryData.isDriver()) {
                changeCenterImage(SEARCH_GOODS);
            }

            setIndex(currentIndex);

            if (mOnTabClickListener != null) {
                mOnTabClickListener.onClick(v, currentIndex);
            }
        }
    };


    /**
     * 中间标签点击事件
     * 该事件中做的事情：
     * 1。判断当前中间图标处于什么状态，根据状态进行不同的操作
     * 2.点击事件触发中间图标的变更
     * 3.在变更图标的同时，记录图片的状态。
     */
    View.OnClickListener clickCenterImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.iv_center) {
                Logger.d("oye", "iv_center");
                // 司机版特有逻辑
                if (CMemoryData.isDriver()) {
                    //  进行判断，如果标签处于听单状态，则是切换到找货，否者是切换到听单
                    if (centerBtnFlag == SEARCH_GOODS) {// 正处于找货页面
                        Logger.i("oye", "切换听单图片");
                        changeCenterImage(LISTENER_ORDER);
                        if (mOnTabClickListener != null) {
                            // 第二个参数是指:当前改图标是否处于找货状态。true：表示处于找货状态，主页面需要切换到听单界面
                            mOnTabClickListener.onCenterClick(v, true);
                        }
                    } else if (centerBtnFlag == LISTENER_ORDER) {// 正处于听单页面
                        changeCenterImage(SEARCH_GOODS);
                        if (mOnTabClickListener != null) {
                            mOnTabClickListener.onCenterClick(v, false);
                        }
                    }
                    // 重置其他所有标签的状态
                    resetUI();
                } else {
                    // 货主版 走 这里
                    if (mOnTabClickListener != null) {
                        mOnTabClickListener.onCenterClick(v, false);
                    }
                }
            }

        }
    };

    /**
     * 修改 中心图标,并记录标签
     */
    private void changeCenterImage(int flag) {
        centerBtnFlag = flag;// 记录中间的标签状态
        if (CMemoryData.isDriver()) {
            if (flag == SEARCH_GOODS) {
                if (useNetData) {
                    ivCenter.setBackgroundDrawable(datas.get(2).getIcon_normal());
                } else {
                    ivCenter.setBackgroundResource(R.drawable.tabbar_findgoods);
                }
            } else if (flag == LISTENER_ORDER) {
                if (useNetData) {
                    ivCenter.setBackgroundDrawable(datas.get(2).getIcon_down());
                } else {
                    ivCenter.setBackgroundResource(R.drawable.tabbar_listen_order);
                }
            }
        }
    }

    /**
     * 对外提供的修改选中项方法
     */
    public void setIndex(int index) {
        // 手动设置Item时，如果是第一次进行设置，不去记录currentIndex,不然在changeLastItemUI显示会错误
        if (isFirstSet) {
            isFirstSet = false;
        } else {
            lastIndex = currentIndex;
        }

        if (currentIndex != index) {
            currentIndex = index;
        }

        for (int i=0; i < 4; i++){
            boolean isCheck = currentIndex == i;
            ImageView imageView = itemImages.get(i > 1 ? i + 1 : i);
            TextView textView = itemTexts.get(i);
            imageView.setEnabled(isCheck);
            textView.setEnabled(isCheck);
        }
    }

    /**
     * 重置UI
     */
    private void resetUI() {
        lastIndex = -1;// 重置已选中标签
        currentIndex = -1;// 重置当先选中标签
        for (int i=0; i < 4; i++){
            ImageView imageView = itemImages.get(i > 1 ? i + 1 : i);
            TextView textView = itemTexts.get(i);

            imageView.setEnabled(false);
            textView.setEnabled(false);
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * 设置字体颜色
     *
     * @param tv
     * @param normal  正常状态颜色
     * @param checked 选中状态颜色
     */
    public void setSelectorColor(TextView tv, int normal, int checked) {
        int[] colors = new int[]{normal, checked, normal};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled};
        states[2] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        tv.setTextColor(colorStateList);

    }

    /**
     * 控件选择器 选中模式
     * @param normal 默认状态
     * @param check 选中状态
     * @return 选择器
     */
    public StateListDrawable getSelector(Drawable normal, Drawable check){
        StateListDrawable bg=new StateListDrawable();
        bg.addState(new int[] { android.R.attr.state_enabled }, check);
        bg.addState(new int[] { - android.R.attr.state_enabled }, normal);
        bg.addState(new int[] {}, normal);
        return bg;
    }

    /**
     * 刷新view
     */
    public void reflushView() {
        initData();
    }

    /**
     * 设置角标状态（已读、未读）
     */
    public void setCornerVisable(boolean isVisable) {
        if (isVisable) {
            // 设置可见（有未读消息）
            tvTabThreeCorner.setVisibility(View.VISIBLE);
        } else {
            tvTabThreeCorner.setVisibility(View.GONE);
        }
    }

    /**
     * 对外提供的设置点击事件方法
     */
    public void setmOnTabClickListener(OnTabClickListener mOnTabClickListener) {
        this.mOnTabClickListener = mOnTabClickListener;
    }
}
