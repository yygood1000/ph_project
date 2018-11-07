package com.topjet.common.resource.dict;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.topjet.common.R;
import com.topjet.common.common.modle.bean.ScrollBtnOptions;
import com.topjet.common.common.view.adapter.ScrollBtnsRecyclerAdapter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.controller.DividerGridItemDecoration;
import com.topjet.common.utils.DataFormatFactory;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;

import java.util.ArrayList;

public class ScrollButtonsDataDict {
    private static String TAG = "ScrollButtonsInit";
    private static ArrayList<ScrollBtnOptions> btns = new ArrayList<>();

    public static final String SCROLL_BUTTONS = "scrollButtons.json";//车型

    public static String defulatJson; // 默认布局的json

    /**
     * 初始化滑动按钮组
     */
    public synchronized static void initScrollBtnsData(final Context context) {
        if (context == null) {
            return;
        }
        // 初始化本地数据
        new Thread() {
            public void run() {
                Logger.d(TAG, "初始化滑动按钮组 from assets ");
                defulatJson = DataFormatFactory.getJsonFromAssets(context, SCROLL_BUTTONS);
            }
        }.start();

        // 先从SP 中获取滑动按钮组 Json字符串，解析准备用于首页显示。
        // 若SP 中没有，则从assets中读取默认滑动按钮组字符串。
        // SP 中的Json 字符串会在 读取完 assets数据后在SP 中存储一份；或者在服务端告知有更新时，请求成功后更新存储一份。
        String areaJson = CPersisData.getScrollBtnsJsons();
        if (StringUtils.isNotBlank(areaJson)) {
            Logger.d(TAG, "初始化滑动按钮组 from SharePrefrence ");
            parseScrollBtnsJsonToList(areaJson);

            // 如果活动时间已过，则显示默认的数据
            if (btns != null && btns.size() > 0) {
                checkActionTime(context);
            }
        } else {
            defaultData();
        }


    }

    /**
     * 检查是否在活动时间内
     *
     * @param context 环境
     */
    private static void checkActionTime(Context context) {
        if (btns == null || btns.size() == 0) {
            defaultData();
        } else {
            boolean isActionTime = TimeUtils.isActionTime(btns.get(0).getBegin_date(),
                    btns.get(0).getEnd_date());
            Logger.d(TAG, "GetScrollBtnsResponse " + isActionTime);
            if (!isActionTime) {
                Logger.d(TAG, "GetScrollBtnsResponse default");
                btns.clear();
                defaultData();
            }
        }
    }

    /**
     * 默认显示本地数据
     */
    private static void defaultData() {
        if (StringUtils.isNotBlank(defulatJson)) {
            parseScrollBtnsJsonToList(defulatJson);// 解析Json
            CPersisData.setScrollBtnsJsons(defulatJson);// 在SP中存储一份
        }
    }


    /**
     * 解析Json成ArrayList
     */
    private static void parseScrollBtnsJsonToList(String areaJson) {
        try {
            btns.clear();
            btns = DataFormatFactory.jsonToArrayList(areaJson, ScrollBtnOptions.class);
        } catch (Exception e) {
            Logger.d("json解析错误");
            e.printStackTrace();
        }
    }


    /*
     *  ========================================================================================
     *   =======================================API方法=========================================
     *  ========================================================================================
     */

    /**
     * 获取滑动按钮组集合
     */
    public static ArrayList<ScrollBtnOptions> getScrollBtnsListData() {
        if (btns != null && btns.size() > 0) {
            return btns;
        } else {
            String areaJson = CPersisData.getScrollBtnsJsons();
            if (StringUtils.isNotBlank(areaJson)) {
                parseScrollBtnsJsonToList(areaJson);
            }
        }
        return btns;
    }

    /**
     * 更新滑动按钮组 内存数据
     */
    public static void updataScrollBtnsListData(ArrayList<ScrollBtnOptions> newBtnsData) {
        btns.clear();
        btns.addAll(newBtnsData);
    }

    /**
     * 获取滑动按钮组ViewPager显示的的viewList,并且初始化底部圆点
     */
    public static ArrayList<View> getViewListAndInitPoint(Context mContext, LinearLayout pointGroup,
                                                          ScrollBtnsRecyclerAdapter.OnScrollBtnsClickListener
                                                                  mListener) {


        // 刷新之前先判断是否在活动时间内
        checkActionTime(mContext);

        // ViewPager 用来显示的View集合
        ArrayList<View> viewList = new ArrayList<>();
        // 进行本次操作的按钮组数据集合，这样写是为了不影响内存中的数据
        ArrayList<ScrollBtnOptions> tempBtnsList = new ArrayList<>();
        tempBtnsList.addAll(btns);
        // 总页数
        int pageCount = getTotalPages(tempBtnsList);
        // 重新设置小圆点前需要清空布局内已有的小圆点
        pointGroup.removeAllViews();

        // 每次取出6个，取出后删除
        for (int page = 0; page < pageCount; page++) {
            // 构造View实体类，用于ViewPager显示
            View view = LayoutInflater.from(mContext).inflate(R.layout.pageitem_scroll_btns, null);

            // 每一页显示的按钮数据集合,用于RecyclerView中显示。每一个RecyclerView 需要单独的一个List做展示。
            ArrayList<ScrollBtnOptions> pageBtnsList = new ArrayList<>();
            pageBtnsList = getBtnsListOfOnePage(tempBtnsList);

            // 设置每页View中RecyclerView显示的内容,并设置点击事件
            convertRecyclerView(mContext, view, pageBtnsList, mListener);

            // View展示设置完成，添加到View集合中
            viewList.add(view);

            // 根据总页数和当前页数，初始化底部切换圆点控件
            initPointGroup(mContext, pointGroup, page, pageCount);
        }
        return viewList;
    }

    /**
     * 计算滑动按钮组页数
     */
    private static int getTotalPages(ArrayList<ScrollBtnOptions> textBtn) {
        int count = textBtn.size() / 6;
        if (textBtn.size() % 6 > 0) {
            count++;
        }
        return count;
    }

    /**
     * 获取每一页的滑动按钮组数据
     */
    private static ArrayList<ScrollBtnOptions> getBtnsListOfOnePage(ArrayList<ScrollBtnOptions> scrollBtns) {
        ArrayList<ScrollBtnOptions> dataList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (!ListUtils.isEmpty(scrollBtns)) {
                dataList.add(scrollBtns.get(0));
                scrollBtns.remove(0);
            }
        }
        return dataList;
    }


    /**
     * 初始化ViewPager每一页中的RecyclerView
     */
    private static void convertRecyclerView(Context mContext, View convertView, ArrayList<ScrollBtnOptions> dataList,
                                            ScrollBtnsRecyclerAdapter.OnScrollBtnsClickListener mListener) {
        final RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerview);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();

        layoutParams.height = ScreenUtils.dp2px(mContext, 225);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext));
        ScrollBtnsRecyclerAdapter mRecyclerAdapter = new ScrollBtnsRecyclerAdapter(mContext);

        // 设置RecyclierView的点击事件
        if (mListener instanceof ScrollBtnsRecyclerAdapter.OnCustomItemDriverClickListener) {
            mRecyclerAdapter.setDriverItemClickListener((ScrollBtnsRecyclerAdapter.OnCustomItemDriverClickListener) mListener);
        } else {
            mRecyclerAdapter.setShipperItemClickListener((ScrollBtnsRecyclerAdapter.OnCustomItemShipperClickListener)
                    mListener);
        }

        recyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setNewData(dataList);
    }


    /**
     * 初始化滑动按钮组底部切换圆点控件
     *
     * @param pointGroup 装圆点的ViewGroup
     * @param page       当前页数
     * @param pageCount  总页数
     */
    private static void initPointGroup(Context mContext, LinearLayout pointGroup, int page, int pageCount) {
        Logger.e("oye", "initPointGroup");
        // 设置下面指示点的指示个数
        ImageView point = new ImageView(mContext);
        if (CMemoryData.isDriver()) {
            point.setBackgroundResource(R.drawable.selector_point_driver);
        } else {
            point.setBackgroundResource(R.drawable.selector_point_shipper);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 当不是最后一个时，需要设置间距
        if (page != pageCount - 1) {
            params.setMargins(0, 0, ScreenUtils.dp2px(mContext, 6), 0);
            point.setLayoutParams(params);
        }
        // 初始化指示器指向第一个point
        if (page == 0) {
            point.setEnabled(true);
        } else {
            point.setEnabled(false);
        }
        pointGroup.addView(point);
    }
}