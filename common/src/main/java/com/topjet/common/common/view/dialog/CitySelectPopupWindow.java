package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.resource.bean.AreaInfo;
import com.topjet.common.resource.bean.CityItem;
import com.topjet.common.resource.dict.AreaDataDict;
import com.topjet.common.common.modle.event.AreaSelectedData;
import com.topjet.common.common.view.adapter.SelectLocalAreaAdapter;
import com.topjet.common.common.view.adapter.SelectLocalCityAdapter;
import com.topjet.common.common.view.adapter.SelectLocalProvinceAdapter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.controller.DividerGridItemDecoration;
import com.topjet.common.utils.KeyboardUtil;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Toaster;

import java.util.ArrayList;

import static com.topjet.common.resource.dict.AreaDataDict.FIRST_LEVEL_UNLIMITED_ID;


/**
 * Created by tsj-004 on 2017/8/23.
 * 城市选择器
 */

public class CitySelectPopupWindow extends PopupWindow {
    private SelectLocalProvinceAdapter selectLocalProvinceAdapter;// 省 的选择适配器
    private SelectLocalCityAdapter selectLocalCityAdapter;// 市 的选择适配器
    private SelectLocalAreaAdapter selectLocalAreaAdapter;// 区 的选择适配器
    private boolean isFirstLevelUnlimitedAllowed; //是否显示全国
    private boolean isSecondLevelUnlimitedAllowed; //是否显示二级全境
    private boolean isThirdLevelUnlimitedAllowed; //是否显示三级全境

    private RecyclerView gv_province, gv_city, gv_area;
    private RadioButton rb_province, rb_city, rb_area;
    private boolean isThirdLevelAllowed = true;

    private String tag = "";

    private OnCustomDismissListener onDismissListener;

    public interface OnCustomDismissListener {
        void onDismiss();
    }

    public CitySelectPopupWindow setOnDismissListener(OnCustomDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public CitySelectPopupWindow(String tag) {
        this.tag = tag;
    }

    /**
     * 显示城市选择列表
     *
     * @param isFirstLevelUnlimitedAllowed  是否显示全国
     * @param isSecondLevelUnlimitedAllowed 是否显示二级全境
     * @param isThirdLevelUnlimitedAllowed  是否显示三级全境
     */
    public void showCitySelectPopupWindow(Activity activity, View view, boolean isFirstLevelUnlimitedAllowed,
                                          boolean isSecondLevelUnlimitedAllowed, boolean isThirdLevelUnlimitedAllowed) {
        new KeyboardUtil(activity).hideSoftInputFromWindow(activity);       // 隐藏输入法

        this.isFirstLevelUnlimitedAllowed = isFirstLevelUnlimitedAllowed;
        this.isSecondLevelUnlimitedAllowed = isSecondLevelUnlimitedAllowed;
        this.isThirdLevelUnlimitedAllowed = isThirdLevelUnlimitedAllowed;
        View parent = View.inflate(activity, R.layout.pop_city_selectlocal, null);
        parent.setFocusable(true);

        selectLocalProvinceAdapter = new SelectLocalProvinceAdapter(R.layout.griditem_city_select);
        selectLocalCityAdapter = new SelectLocalCityAdapter(R.layout.griditem_city_select);
        selectLocalAreaAdapter = new SelectLocalAreaAdapter(R.layout.griditem_city_select);

        // 获取控件
        LinearLayout llSelectLocal = (LinearLayout) parent.findViewById(R.id.ll_select_local);

        rb_province = (RadioButton) parent.findViewById(R.id.rb_province);
        rb_city = (RadioButton) parent.findViewById(R.id.rb_city);
        rb_area = (RadioButton) parent.findViewById(R.id.rb_area);

        if (CMemoryData.isDriver()) {
            rb_province.setBackgroundResource(R.drawable.btn_city_select_local_left_blue);
            rb_city.setBackgroundResource(R.drawable.btn_city_select_local_center_blue);
            rb_area.setBackgroundResource(R.drawable.btn_city_select_local_right_blue);

            Resources resource = activity.getResources();
            ColorStateList csl = resource.getColorStateList(R.color.city_selecter_rb_text_color_blue);
            if (csl != null) {
                rb_province.setTextColor(csl);// 设置按钮文字颜色
                rb_city.setTextColor(csl);
                rb_area.setTextColor(csl);
            }
        }

        gv_province = (RecyclerView) parent.findViewById(R.id.gv_province);
        gv_city = (RecyclerView) parent.findViewById(R.id.gv_city);
        gv_area = (RecyclerView) parent.findViewById(R.id.gv_area);

        gv_province.setLayoutManager(new GridLayoutManager(activity, 4));
        gv_province.addItemDecoration(new DividerGridItemDecoration(activity));

        gv_city.setLayoutManager(new GridLayoutManager(activity, 4));
        gv_city.addItemDecoration(new DividerGridItemDecoration(activity));

        gv_area.setLayoutManager(new GridLayoutManager(activity, 4));
        gv_area.addItemDecoration(new DividerGridItemDecoration(activity));

        // 设置列表点击监听
        gv_province.setAdapter(selectLocalProvinceAdapter);
        gv_city.setAdapter(selectLocalCityAdapter);
        gv_area.setAdapter(selectLocalAreaAdapter);
        selectLocalProvinceAdapter.setOnItemClickListener(mSelectProvinceOnItemClickListener);
        selectLocalCityAdapter.setOnItemClickListener(mSelectCityOnItemClickListener);
        selectLocalAreaAdapter.setOnItemClickListener(mSelectAreaOnItemClickListener);
        rb_province.setOnClickListener(new RadioButtonOnClickListener());
        rb_city.setOnClickListener(new RadioButtonOnClickListener());
        rb_area.setOnClickListener(new RadioButtonOnClickListener());
        llSelectLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CitySelectPopupWindow.this.dismiss();
                // 将窗口颜色调回完全透明
                // WindowManager.LayoutParams params =
                // mActivity.getWindow().getAttributes();
                // params.alpha = 1;
                // mActivity.getWindow().setAttributes(params);
            }
        });

        // 加载第一季城市列表并显示
        getFirstLevelCities();

        // 设置窗口属性
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setContentView(parent);
        this.setClippingEnabled(false);     // 可以超出屏幕，状态栏终于可以变半透明啦
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });

        this.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    // 加载第一层数据
    private void getFirstLevelCities() {
        ArrayList<CityItem> firstLevelCities = AreaDataDict.getFirstLevel();// 数据源
        if (!ListUtils.isEmpty(firstLevelCities)) {
            if (isFirstLevelUnlimitedAllowed) {
                firstLevelCities.add(0, getFirstLevelUnlimitedCityItem());// 添加“全国”的一条数据
            }
            selectLocalProvinceAdapter.setNewData(firstLevelCities);// 添加第一层“城市”
        }
    }

    // 第一级 “省” 条目的点击事件
    private BaseQuickAdapter.OnItemClickListener mSelectProvinceOnItemClickListener = new BaseQuickAdapter
            .OnItemClickListener() {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CityItem firstLevel = selectLocalProvinceAdapter.getItem(position);
            selectLocalProvinceAdapter.setFirstLevel(firstLevel);// 放入一级省存储的数据

            // 选择了全国，结束城市选择
            if (FIRST_LEVEL_UNLIMITED_ID.equals(firstLevel.getAdcode())) {
                CitySelectPopupWindow.this.dismiss();
                onFinishCities(firstLevel, null, null);
                return;
            }

            // 选择了直辖市，直接跳到3级城市选择
            if (AreaDataDict.isSpecialFirstLevel(firstLevel.getCityName())) {

                // 2级城市的数据,用于结束选择时post出去的数据
                ArrayList<CityItem> secondLevelCities = AreaDataDict.getSecondLevel(firstLevel,
                        isSecondLevelUnlimitedAllowed);
                CityItem secondLevel = new CityItem();
                // 获得直辖市的2级城市,如果是允许显示全境，直辖市应该拿取城市集合中的第二个,否则拿第一个
                if (isSecondLevelUnlimitedAllowed) {
                    secondLevel = secondLevelCities.get(1);
                } else {
                    secondLevel = secondLevelCities.get(0);
                }

                if (!isThirdLevelAllowed) {
                    CitySelectPopupWindow.this.dismiss();
                    onFinishCities(firstLevel, null, null);
                } else {
                    selectLocalCityAdapter.setSecondLevel(secondLevel);// 放入二级市存储的数据

                    // 3级列表的数据
                    ArrayList<CityItem> thirdLevelCities = AreaDataDict.getThirdLevel(secondLevel,
                            isThirdLevelUnlimitedAllowed);
                    // 进行3级城市数据的展示及CheckBox的设置,1、2级数据的保存
                    if (!ListUtils.isEmpty(thirdLevelCities)) {
                        rb_province.setText(firstLevel.getCityName());
                        selectLocalAreaAdapter.setNewData(thirdLevelCities);// 设置3级城市显示数据

                        gv_province.setVisibility(View.GONE);
                        gv_city.setVisibility(View.GONE);
                        gv_area.setVisibility(View.VISIBLE);

                        rb_province.setChecked(false);
                        rb_city.setChecked(false);
                        rb_city.setEnabled(false);// 2级城市选择框不能选择
                        rb_area.setChecked(true);
                    }
                }

            } else {
                ArrayList<CityItem> secondLevelCities = AreaDataDict.getSecondLevel(firstLevel,
                        isSecondLevelUnlimitedAllowed);
                if (!ListUtils.isEmpty(secondLevelCities)) {
                    rb_province.setText(firstLevel.getCityName());
                    // selectLocalProvinceAdapter.setFirstLevel(firstLevel);//
                    // 放一级省存储的数据
                    selectLocalCityAdapter.setNewData(secondLevelCities);
                    gv_province.setVisibility(View.GONE);
                    gv_city.setVisibility(View.VISIBLE);
                    rb_province.setChecked(false);
                    rb_city.setChecked(true);
                    rb_area.setChecked(false);
                }
            }

        }

    };

    // 第二级 “市” 条目的点击事件
    private BaseQuickAdapter.OnItemClickListener mSelectCityOnItemClickListener = new BaseQuickAdapter
            .OnItemClickListener() {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CityItem firstLevel = selectLocalProvinceAdapter.getFirstLevel();// 获取一级省存储的数据
            CityItem secondLevel = selectLocalCityAdapter.getItem(position);
            if (AreaDataDict.NONE_FIRST_LEVEL_UNLIMITED_NAME.equals(secondLevel.getCityName())) {
                // 二级选了“全境”，结束城市选择
                CitySelectPopupWindow.this.dismiss();
                onFinishCities(firstLevel, null, null);
                return;
            }
            if (isThirdLevelAllowed) {
                ArrayList<CityItem> thirdLevelCities = AreaDataDict.getThirdLevel(secondLevel,
                        isThirdLevelUnlimitedAllowed);
                if (!ListUtils.isEmpty(thirdLevelCities)) {
                    rb_city.setText(secondLevel.getCityName());
                    selectLocalCityAdapter.setSecondLevel(secondLevel);// 放入二级市存储的数据
                    selectLocalAreaAdapter.setNewData(thirdLevelCities);
                    gv_province.setVisibility(View.GONE);
                    gv_city.setVisibility(View.GONE);
                    gv_area.setVisibility(View.VISIBLE);
                    rb_province.setChecked(false);
                    rb_city.setChecked(false);
                    rb_area.setChecked(true);
                } else {
                    //没有三级，返回二级
                    CitySelectPopupWindow.this.dismiss();
                    onFinishCities(firstLevel, secondLevel, null);
                }
            } else {
                // 没选全境，只有二级，点击后结束城市选择
                CitySelectPopupWindow.this.dismiss();
                onFinishCities(firstLevel, secondLevel, null);
            }
        }
    };

    // 第三级 “区” 条目的点击事件
    private BaseQuickAdapter.OnItemClickListener mSelectAreaOnItemClickListener = new BaseQuickAdapter
            .OnItemClickListener() {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CityItem firstLevel = selectLocalProvinceAdapter.getFirstLevel();// 获取一级省存储的数据
            CityItem secondLevel = selectLocalCityAdapter.getSecondLevel();// 获取二级市存储的数据
            CityItem thirdLevel = selectLocalAreaAdapter.getItem(position);// 获取三级区存储的数据
            if (AreaDataDict.NONE_FIRST_LEVEL_UNLIMITED_NAME.equals(thirdLevel.getCityName())) {// 点击了3级列表的全境
                CitySelectPopupWindow.this.dismiss();
                onFinishCities(firstLevel, secondLevel, null);
            } else {
                CitySelectPopupWindow.this.dismiss();
                onFinishCities(firstLevel, secondLevel, thirdLevel);
            }

        }
    };

    private CityItem getFirstLevelUnlimitedCityItem() {
        CityItem cityItem = new CityItem();
        cityItem.setCityName("全国");
        cityItem.setAdcode(FIRST_LEVEL_UNLIMITED_ID);
        return cityItem;
    }

    /**
     * 结束城市选择后的处理
     */
    private void onFinishCities(CityItem firstLevel, CityItem secondLevel, CityItem thirdLevel) {
        if (firstLevel != null) {
            AreaInfo areaInfo = new AreaInfo(firstLevel);
            if (secondLevel != null) {
                areaInfo.setSecondLevel(secondLevel);
            }
            if (thirdLevel != null) {
                areaInfo.setThirdLevel(thirdLevel);
            }
            AreaSelectedData areaSelectedData = new AreaSelectedData(tag, areaInfo);
            BusManager.getBus().post(areaSelectedData);
        }
//		curtainView.toggleStatus();
    }

    private class RadioButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.rb_province) {
                rb_province.setText("省");
                rb_city.setText("市");
                gv_province.setVisibility(View.VISIBLE);
                gv_city.setVisibility(View.GONE);
                gv_area.setVisibility(View.GONE);
                selectLocalCityAdapter.setNewData(null);
                selectLocalAreaAdapter.setNewData(null);// 清除第二三级数据
                selectLocalProvinceAdapter.setFirstLevel(null);
                selectLocalCityAdapter.setSecondLevel(null);// 把一二级数adapter里的据设置为空
            } else if (id == R.id.rb_city) {
                if (selectLocalProvinceAdapter.getFirstLevel() == null) {// 第一级为空
                    Toaster.showLongToast("请选择省");
                    rb_province.setChecked(true);
                    rb_city.setChecked(false);
                    rb_area.setChecked(false);
                } else { // 从 区 返回选择
                    rb_city.setText("市");
                    gv_province.setVisibility(View.GONE);
                    gv_city.setVisibility(View.VISIBLE);
                    gv_area.setVisibility(View.GONE);
                    selectLocalAreaAdapter.setNewData(null);// 清除第三级数据
                    selectLocalCityAdapter.setSecondLevel(null);// 设置第二级Adapter中数据为空
                }
            } else if (id == R.id.rb_area) {
                if (selectLocalProvinceAdapter.getFirstLevel() == null) {
                    Toaster.showLongToast("请选择省");
                    rb_province.setChecked(true);
                    rb_city.setChecked(false);
                    rb_area.setChecked(false);
                } else if (selectLocalProvinceAdapter.getFirstLevel() != null
                        && selectLocalCityAdapter.getSecondLevel() == null) {
                    Toaster.showLongToast("请选择市");
                    rb_province.setChecked(false);
                    rb_city.setChecked(true);
                    rb_area.setChecked(false);
                }
            }
        }
    }

    public void setIsThirdLevelAllowed(boolean isThirdLevelAllowed) {
        this.isThirdLevelAllowed = isThirdLevelAllowed;
    }
}
