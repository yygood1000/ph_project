package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.utils.KeyboardUtil;
import com.topjet.common.utils.NumberFormatUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.widget.wheel.AbstractWheelAdapter;
import com.topjet.common.widget.wheel.OnWheelScrollListener;
import com.topjet.common.widget.wheel.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeWheelPop {

    private static final String hour = "24";// 23以后
    private View contentView = null;
    private Activity activity;
    private PopupWindow mPop;
    private View mParent;
    private WheelView wheel_mon;// 今天 明天 一共30天
    private WheelView wheel_morning; // 上午 下午
    private WheelView wheel_hour;// 0~23时
    ////////
    private LinearLayout ll1;
    private LinearLayout ll2;
    ////////
    private TextView plate_wheel_close;
    private TextView time_wheel_ok;

    private static final String MON = "mon";
    private static final String MORNING = "morning";
    private static final String HOUR = "hour";

    ArrayList<String> mMonthList;//前面添加  今定今装 今定明装  随到随走  第一滑动列默认选中随到随走
    ArrayList<String> morningList = new ArrayList<String>();
    ArrayList<String> hourList;

    private TimeAdapter wheel_monAdapter;
    private TimeAdapter wheel_morningAdapter;
    private TimeAdapter wheel_hourAdapter;

    String selectMon;
    String selectMorning;
    String selectHour;

    private String loadDate;
    private String loadDateType;

    public TimeWheelPop(Activity activity) {
        this.activity = activity;
    }

    public TimeWheelPop(Activity activity, View mParent, OnCompleteListener onCompletel) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.mParent = mParent;
        initView(onCompletel);
    }

    public TimeWheelPop(Activity activity, View mParent, OnCompleteListener onCompletel, String loadDate_, String
            loadDateType_) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.mParent = mParent;
        this.loadDate = loadDate_;
        this.loadDateType = loadDateType_;

        if (StringUtils.isNotBlank(loadDateType)) {
            if (loadDateType.equals("0")) {
                if (StringUtils.isNotBlank(loadDate)) {
                    /////////////
                    try {
                        Date d1 = new Date();// 获取当前时间
                        long long1 = d1.getTime();
                        long long2 = Long.parseLong(loadDate);
                        if (long1 >= long2) {
                            init(onCompletel);
                            return;
                        } else {
                            long days;
                            long diff = long2 - long1;
                            if (diff % (1000 * 60 * 60 * 24) == 0) {
                                days = diff / (1000 * 60 * 60 * 24);
                            } else {
                                days = diff / (1000 * 60 * 60 * 24) + 1;
                            }
                            if (days > 30) {
                                init(onCompletel);
                                return;
                            } else {

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        init(onCompletel);
                        return;
                    }
                    //////////////
                    try {
                        //2016年05月28日
                        String sss = TimeUtils.parseLongFormat(Long.parseLong(loadDate));
                        ////
                        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy年MM月dd日");
                        String string_ = format.format(new Date());
                        if (sss.equals(string_)) {
                            //今天
                            initView(onCompletel);

//						Toaster.showShortToast("今天");
//								wheel_morning.setVisibility(View.VISIBLE);
//								wheel_hour.setVisibility(View.VISIBLE);

                            wheel_morning.setVisibility(View.VISIBLE);
                            wheel_hour.setVisibility(View.VISIBLE);
                            ll1.setVisibility(View.GONE);
                            ll2.setVisibility(View.GONE);

                            wheel_mon.setCurrentItem(3);
                            selectMon = mMonthList.get(3);

                            java.text.SimpleDateFormat formatj = new java.text.SimpleDateFormat("HH:mm:ss");
                            String string_j = formatj.format(new Date(Long.parseLong(loadDate)));
                            int k = 0;
                            for (int i = 0; i < hourList.size(); i++) {
                                if (string_j.substring(0, 2).equals(hourList.get(i).substring(0, 2))) {
                                    k = i;
                                    break;
                                }
                            }
                            wheel_hour.setCurrentItem(k);
                            selectHour = hourList.get(k);

                            int m = 0;
                            if (Integer.parseInt(hourList.get(k).substring(0, 2)) <= 12) {// 0~12点
                                // 肯定 有上午下午
                                m = 0;
                            } else {
                                if (morningList.size() == 1) {
                                    m = 0;
                                } else if (morningList.size() == 2) {
                                    m = 1;
                                }
                            }
                            wheel_morning.setCurrentItem(m);
                            selectMorning = morningList.get(m);

                        } else {
                            //
                            mMonthList = initmon();
                            initMorning();
                            hourList = inithour();
                            istomorrowInit = true;
                            initView(onCompletel);
//						Toaster.showShortToast("今天以后");//明天

//								wheel_morning.setVisibility(View.VISIBLE);
//								wheel_hour.setVisibility(View.VISIBLE);

                            wheel_morning.setVisibility(View.VISIBLE);
                            wheel_hour.setVisibility(View.VISIBLE);
                            ll1.setVisibility(View.GONE);
                            ll2.setVisibility(View.GONE);

                            java.text.SimpleDateFormat formatm = new java.text.SimpleDateFormat("yyyy年MM月dd日");
                            String string_m = formatm.format(new Date(Long.parseLong(loadDate)));
                            int k = 4;//明天
                            for (int i = 3; i < mMonthList.size(); i++) {
                                //去除 前面三个固定的    // 1：今定今装 2：今定明装 3：随到随走
                                if (string_m.substring(5).equals(mMonthList.get(i).substring(5))) {
                                    k = i;
                                    break;
                                }
                            }
                            wheel_mon.setCurrentItem(k);
                            selectMon = mMonthList.get(k);


                            java.text.SimpleDateFormat format2 = new java.text.SimpleDateFormat("HH:mm:ss");
                            String string_2 = format2.format(new Date(Long.parseLong(loadDate)));
                            int k2 = 0;
                            for (int i = 0; i < hourList.size(); i++) {
                                if (string_2.substring(0, 2).equals(hourList.get(i).substring(0, 2))) {
                                    k2 = i;
                                    break;
                                }
                            }
                            wheel_hour.setCurrentItem(k2);
                            selectHour = hourList.get(k2);

                            int m = 0;
                            if (Integer.parseInt(hourList.get(k2).substring(0, 2)) <= 12) {// 0~12点
                                // 肯定 有上午下午
                                m = 0;
                            } else {
                                if (morningList.size() == 1) {
                                    m = 0;
                                } else if (morningList.size() == 2) {
                                    m = 1;
                                }
                            }
                            wheel_morning.setCurrentItem(m);
                            selectMorning = morningList.get(m);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        init(onCompletel);
                    }

                } else {
                    init(onCompletel);
                }
            } else {
                initView(onCompletel);
                int k = 2;// 默认 随到随走
                if (loadDateType.equals("1")) {
                    k = 0;
                } else if (loadDateType.equals("2")) {
                    k = 1;
                } else if (loadDateType.equals("3")) {
                    k = 2;
                }
                wheel_mon.setCurrentItem(k);
                selectMon = mMonthList.get(k);
            }
        } else {
            init(onCompletel);
        }


    }

    private boolean istomorrowInit;

    private void init(OnCompleteListener onCompletel) {
        loadDate = "随到随走";
        loadDateType = "3";
        initView(onCompletel);
    }

    private void initView(final OnCompleteListener onCompletel) {
        if (istomorrowInit) {
            //外部已经初始化了
        } else {
            mMonthList = initmon();
            // initMorning();
            // hourList = inithour();
            hourList = initMorningAndHourCurrent();
        }

        contentView = LayoutInflater.from(activity).inflate(R.layout.wheelpop_time, null);
        wheel_mon = (WheelView) contentView.findViewById(R.id.wheel_mon);
        wheel_morning = (WheelView) contentView.findViewById(R.id.wheel_morning);
        wheel_hour = (WheelView) contentView.findViewById(R.id.wheel_hour);
        ll1 = (LinearLayout) contentView.findViewById(R.id.ll1);
        ll2 = (LinearLayout) contentView.findViewById(R.id.ll2);
        time_wheel_ok = (TextView) contentView.findViewById(R.id.time_wheel_ok);
        plate_wheel_close = (TextView) contentView.findViewById(R.id.plate_wheel_close);
        plate_wheel_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        time_wheel_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onCompletel != null) {
                    if (!NumberFormatUtils.hasDigit(selectMon)) {
                        onCompletel.OnCompleteListener(selectMon, "", "");
                    } else {
                        onCompletel.OnCompleteListener(selectMon, selectMorning, selectHour);
                    }
                }
                mPop.dismiss();
            }
        });
        wheel_monAdapter = new TimeAdapter(mMonthList, MON);
        wheel_mon.setViewAdapter(new TimeAdapter(mMonthList, MON), MON);
        wheel_mon.setCurrentItem(2);
        selectMon = mMonthList.get(2);//最开始 默认 随到随走  第三个
//		wheel_morning.setVisibility(View.INVISIBLE);
//		wheel_hour.setVisibility(View.INVISIBLE);

        wheel_morning.setVisibility(View.GONE);
        wheel_hour.setVisibility(View.GONE);
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);

        wheel_morningAdapter = new TimeAdapter(morningList, MORNING);
        wheel_morning.setViewAdapter(wheel_morningAdapter, MORNING);
        wheel_morning.setCurrentItem(0);
        selectMorning = morningList.get(0);

        wheel_hourAdapter = new TimeAdapter(hourList, HOUR);
        wheel_hour.setViewAdapter(wheel_hourAdapter, HOUR);
        if (hourList.size() == 1 && hourList.get(0).equals(hour)) {
            wheel_hour.setCurrentItem(0);
            selectHour = "00:00";
        } else {
            wheel_hour.setCurrentItem(0);
            selectHour = hourList.get(0);
        }

        wheel_mon.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                if (wheel_mon.getCurrentItem() == 0 || wheel_mon.getCurrentItem() == 1 || wheel_mon.getCurrentItem()
                        == 2) {
//					wheel_morning.setVisibility(View.INVISIBLE);
//					wheel_hour.setVisibility(View.INVISIBLE);

                    wheel_morning.setVisibility(View.GONE);
                    wheel_hour.setVisibility(View.GONE);
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.VISIBLE);

                } else {
//					wheel_morning.setVisibility(View.VISIBLE);
//					wheel_hour.setVisibility(View.VISIBLE);

                    wheel_morning.setVisibility(View.VISIBLE);
                    wheel_hour.setVisibility(View.VISIBLE);
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);

                }
                selectMon = mMonthList.get(wheel_mon.getCurrentItem());
                if (wheel_mon.getCurrentItem() == 3) {
                    hourList = initMorningAndHourCurrent();
                    wheel_morningAdapter = new TimeAdapter(morningList, MORNING);
                    wheel_morning.setViewAdapter(wheel_morningAdapter, MORNING);
                    wheel_morning.setCurrentItem(0);
                    selectMorning = morningList.get(0);

                    wheel_hourAdapter = new TimeAdapter(hourList, HOUR);
                    wheel_hour.setViewAdapter(wheel_hourAdapter, HOUR);
                    if (hourList.size() == 1 && hourList.get(0).equals(hour)) {
                        wheel_hour.setCurrentItem(0);
                        selectHour = "00:00";
                    } else {
                        wheel_hour.setCurrentItem(0);
                        selectHour = hourList.get(0);
                    }

                } else if (wheel_mon.getCurrentItem() > 3) {
                    initMorning();
                    hourList = inithour();
                    wheel_morningAdapter = new TimeAdapter(morningList, MORNING);
                    wheel_morning.setViewAdapter(wheel_morningAdapter, MORNING);
                    wheel_morning.setCurrentItem(0);
                    selectMorning = morningList.get(0);

                    wheel_hourAdapter = new TimeAdapter(hourList, HOUR);
                    wheel_hour.setViewAdapter(wheel_hourAdapter, HOUR);
                    wheel_hour.setCurrentItem(12);// 默认选择12:00
                    selectHour = hourList.get(12);
                }

            }
        });
        wheel_morning.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                selectMorning = morningList.get(wheel_morning.getCurrentItem());

                if (wheel_morning.getCurrentItem() == 0) {

                    int k = 0;
                    for (int i = 0; i < hourList.size(); i++) {
                        if (hourList.get(i).substring(0, 2).equals("12")) {
                            // 默认选择12:00
                            k = i;
                            break;
                        }
                    }
                    wheel_hour.setCurrentItem(k);
                    selectHour = hourList.get(k);


                } else if (wheel_morning.getCurrentItem() == 1) {

                    int k = hourList.size();
                    for (int i = 0; i < hourList.size(); i++) {
                        if (hourList.get(i).substring(0, 2).equals("18")) {
                            // 选择18:00
                            k = i;
                            break;
                        }
                    }
                    wheel_hour.setCurrentItem(k);
                    selectHour = hourList.get(k);

                }
            }
        });

        wheel_hour.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                selectHour = hourList.get(wheel_hour.getCurrentItem());
                if (Integer.parseInt(selectHour.substring(0, 2)) <= 12) {// 0~12点
                    // 肯定 有上午下午
                    wheel_morning.setCurrentItem(0);// 上午
                    selectMorning = morningList.get(0);
                } else {
                    if (morningList.size() == 1) {
                        wheel_morning.setCurrentItem(0);
                        selectMorning = morningList.get(0);
                    } else if (morningList.size() == 2) {
                        wheel_morning.setCurrentItem(1);
                        selectMorning = morningList.get(1);
                    }

                }
            }
        });
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int scr_height = wm.getDefaultDisplay().getHeight();
        int scr_width = wm.getDefaultDisplay().getWidth();
        int height = (int) (scr_height * 0.4);
        int width = (scr_width * 1);
        // mPop = new PopupWindow(LayoutParams.MATCH_PARENT,
        // CommonUtil.dip2px(context, 350));
        mPop = new PopupWindow(width, height);
        // mPop = new PopupWindow(LayoutParams.MATCH_PARENT,
        // activity.getResources().getDimensionPixelSize(R.dimen.pop_height));
        contentView.setFocusable(true);
        mPop.setContentView(contentView);
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });
        // 触摸pop外，pop消失
        mPop.setBackgroundDrawable(new BitmapDrawable());
        mPop.setOutsideTouchable(true);
        // mPop.setAnimationStyle(android.R.style.Animation_InputMethod);
        mPop.setAnimationStyle(R.style.mypopwindow_anim_style);
        mPop.setFocusable(true);
        contentView.requestFocus();

    }

    /**
     * 30天的年月日 字符串 集合
     *
     * @return
     */
    public ArrayList<String> initmon() {
        ArrayList<String> mMonth = new ArrayList<String>();
        //前面添加  今定今装 今定明装  随到随走
        mMonth.add("今定今装");
        mMonth.add("今定明装");
        mMonth.add("随到随走");
        Calendar theCa = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        for (int i = 0; i < 30; i++) {
            theCa.setTime(new Date());
            theCa.add(Calendar.DATE, i);
            Date date = theCa.getTime();
            String str = sdf.format(date);
            mMonth.add(str);
        }
        return mMonth;

    }

    public void initMorning() {
        morningList.clear();
        morningList.add("上午");
        morningList.add("下午");

    }

    public ArrayList<String> inithour() {
        ArrayList<String> hourList = new ArrayList<String>();
        String string = "";
        for (int i = 0; i < 24; i++) {
            if (i == 0) {
                string = "00";
            } else if (i < 10) {
                string = "0" + i;
            } else if (i >= 10) {
                string = "" + i;
            }
            string = string + ":00";
            hourList.add(string);
        }
        return hourList;

    }

    public ArrayList<String> initMorningAndHourCurrent() {

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("HH:mm:ss");
        String string_ = format.format(new Date());
        int k = Integer.parseInt(string_.substring(0, 2)) + 1;// 范围 0~23 1~24

        if (k <= 12) {
            morningList.clear();
            morningList.add("上午");
            morningList.add("下午");
        } else {
            morningList.clear();
            morningList.add("下午");
        }
        ArrayList<String> hourList = new ArrayList<String>();
        String string = "";
        if (k == 24) {// 当前时间 为23：以后
            hourList.add(hour);
            return hourList;
        } else {
            for (int i = k; i < 24; i++) {
                if (i == 0) {
                    string = "00";
                } else if (i < 10) {
                    string = "0" + i;
                } else if (i >= 10) {
                    string = "" + i;
                }
                string = string + ":00";
                hourList.add(string);
            }
            return hourList;
        }

    }

    public void showPop() {
        new KeyboardUtil(activity).hideSoftInputFromWindow(activity);

        mPop.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        if (contentView != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) contentView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, ScreenUtils.getNavigationBarHeight(activity));
        }
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.7f;
        activity.getWindow().setAttributes(params);
    }

    public void dismissPop() {
        mPop.dismiss();
    }

    class TimeAdapter extends AbstractWheelAdapter {

        private ArrayList<String> timeList;
        private String type;

        public TimeAdapter(ArrayList<String> timeList, String type) {
            this.timeList = timeList;
            this.type = type;
        }

        @Override
        public int getItemsCount() {
            return timeList.size();
        }

        public ArrayList<String> getTimeList() {
            return timeList;
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {

            // long start = System.currentTimeMillis();
            if (convertView == null) {
                TextView tv_time = new TextView(activity);
                tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        activity.getResources().getDimension(R.dimen.textsize_middle));
                tv_time.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                tv_time.setGravity(Gravity.CENTER);
                tv_time.setTextColor(0xFF000000);
//				tv_time.setPadding(0, 10, 0, 10);
                float textsize_timewheel = activity.getResources().getDimension(R.dimen.textsize_timewheel);
                tv_time.setPadding(0, (int) textsize_timewheel, 0, (int) textsize_timewheel);
                convertView = tv_time;
            }
            if (MON.equals(type)) {
                ((TextView) convertView).setTextColor(0xFF000000);
                if (index == 0 || index == 1 || index == 2) {
                    ((TextView) convertView).setText(timeList.get(index));
                } else if (index == 3) {
                    ((TextView) convertView).setText("今天");
                } else if (index == 4) {
                    ((TextView) convertView).setText("明天");
                } else {
                    ((TextView) convertView).setText(timeList.get(index).substring(5));
                }
            } else if (HOUR.equals(type)) {// 0~23时
                ((TextView) convertView).setTextColor(0xFF000000);
                ((TextView) convertView).setText(Integer.parseInt(timeList.get(index).substring(0, 2)) + "时");

            } else {
                ((TextView) convertView).setTextColor(0xFF000000);
                ((TextView) convertView).setText(timeList.get(index));// 上午 下午
            }

            // long end = System.currentTimeMillis();
            // Logger.e("wheel", "getItem: consume time === " + (end - start));
            return convertView;
        }

        @Override
        public View getEmptyItem(View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public interface OnCompleteListener {
        void OnCompleteListener(String mon, String morning, String hour);
    }

}
