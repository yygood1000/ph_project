package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.common.view.adapter.EntiretyAdapter;
import com.topjet.common.common.view.adapter.TruckLengthAdapter;
import com.topjet.common.common.view.adapter.TruckTypeAdapter;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.resource.bean.EntiretyInfo;
import com.topjet.common.resource.bean.TruckLengthInfo;
import com.topjet.common.resource.bean.TruckTypeInfo;
import com.topjet.common.resource.bean.TypeAndLengthDad;
import com.topjet.common.resource.dict.TruckDataDict;
import com.topjet.common.utils.KeyboardUtil;
import com.topjet.common.utils.PopupAlphaAnimatorUtil;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tsj-004 on 2017/9/3.
 * 车型车长
 */

public class TruckLengthAndTypePopupWindow extends PopupWindow implements View.OnClickListener {
    /**
     * selectedEntiretyIndex 取值范围
     */
    public static final int ENTIRETY_NO_SELECTED = -1;          // 不勾选任何一项
    public static final int ENTIRETY_SELECTED_INDEX_0 = 0;      // 勾选第一项
    public static final int ENTIRETY_SELECTED_INDEX_1 = 1;      // 勾选第二项

    private List<EntiretyInfo> entiretyList = null;     // 是否整车
    private List<TruckLengthInfo> lengthList = null;    // 车长
    private List<TruckTypeInfo> typeList = null;        // 车型
    private String tag = "";
    private EntiretyAdapter entiretyAdapter = null;
    private TruckLengthAdapter truckLengthAdapter = null;
    private TruckTypeAdapter truckTypeAdapter = null;
    private int typeMax = 2;            // 车型最大勾选项，默认2个，传-1则不限制
    private int LengthMax = 5;          // 车长最大勾选项，默认5个，传-1则不限制
    private boolean deSelectOther = false;  // 是否反选其他项

    private TextView tvLengthTitle = null;      // 车长标题
    private TextView tvTypeTitle = null;      // 车型标题
    private ScrollView scrollView = null;
    private RelativeLayout ll_transparent = null;   // 最外层布局，如果是从底部弹出，有200dp的padding

    private String lengthTitle = null;

    private OnCustomDismissListener onDismissListener;

    public interface OnCustomDismissListener {
        void onDismiss();
    }

    public TruckLengthAndTypePopupWindow setOnDismissListener(OnCustomDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    /**
     * typeMax      车型最大勾选项，默认2个，传 -1 则不限制
     * LengthMax    车长最大勾选项，默认5个，传 -1 则不限制
     */
    public TruckLengthAndTypePopupWindow(String t, int typeMax, int LengthMax) {
        this(t, typeMax, LengthMax, true);
    }

    /**
     * deSelectOther          是否反选其他项
     * isShowUnlimited   是否   显示 不限
     */
    public TruckLengthAndTypePopupWindow(String t, boolean deSelectOther, boolean isShowUnlimited) {
        this(t, 1, 1, isShowUnlimited);
        this.deSelectOther = deSelectOther;
    }

    /**
     * typeMax      车型最大勾选项，默认2个，传 -1 则不限制
     * LengthMax    车长最大勾选项，默认5个，传 -1 则不限制
     *
     * lengthTitleLong 是否使用长文字，内容是选择车长(大于该车长的车辆均可承接）
     */
    public TruckLengthAndTypePopupWindow(String t, int typeMax, int LengthMax, boolean isShowUnlimited, boolean lengthTitleLong) {
        this(t, typeMax, LengthMax, isShowUnlimited);
        lengthTitle = "选择车长(大于该车长的车辆均可承接）";
    }

    /**
     * typeMax      车型最大勾选项，默认2个，传 -1 则不限制
     * LengthMax    车长最大勾选项，默认5个，传 -1 则不限制
     */
    public TruckLengthAndTypePopupWindow(String t, int typeMax, int LengthMax, boolean isShowUnlimited) {
        this.tag = t;
        this.typeMax = typeMax;
        this.LengthMax = LengthMax;

        lengthList = new LinkedList<>();
        ArrayList<TruckLengthInfo> length = TruckDataDict.getTruckLengthList(isShowUnlimited);
        for (int i = 0; i < length.size(); i++) {
            lengthList.add((TruckLengthInfo) length.get(i).clone());
        }

        typeList = new LinkedList<>();
        ArrayList<TruckTypeInfo> type = TruckDataDict.getTruckTypeList(isShowUnlimited);
        for (int i = 0; i < type.size(); i++) {
            typeList.add((TruckTypeInfo) type.get(i).clone());
        }

        entiretyList = new LinkedList<>();
        EntiretyInfo e0 = new EntiretyInfo(R.string.notpool, false, 1);     // 可拼车
        EntiretyInfo e1 = new EntiretyInfo(R.string.pool, false, 0);        // 整车
        entiretyList.add(e0);
        entiretyList.add(e1);
    }

    /**
     * 车型车长
     * isShowEntirety 是否显示整车
     * selectedEntiretyIndex  勾选整车的哪一项，与selectedData中的数据冲突时，selectedData优先（取值范围：  -1 不勾选， 0 勾选第一项， 1 勾选第二项）
     * selectedData   已选中的车型车长
     */
    public void showPopupWindow(final Activity activity, View view, boolean isShowEntirety, int
            selectedEntiretyIndex, final boolean isScreenBottom, TruckTypeLengthSelectedData selectedData) {
        new KeyboardUtil(activity).hideSoftInputFromWindow(activity);       // 隐藏输入法

        initSelectedData(selectedData, selectedEntiretyIndex);

        View parent = View.inflate(activity, R.layout.pop_truck_length_type, null);
        parent.setFocusable(true);

        truckLengthAdapter = new TruckLengthAdapter();
        truckTypeAdapter = new TruckTypeAdapter();

        // 获取控件
        ll_transparent = (RelativeLayout) parent.findViewById(R.id.ll_transparent);
        TextView tvEntiretyTitle = (TextView) parent.findViewById(R.id.tv_entirety_title);
        RecyclerView gvEntirety = (RecyclerView) parent.findViewById(R.id.gv_entirety);
        RecyclerView gvLength = (RecyclerView) parent.findViewById(R.id.gv_length);
        RecyclerView gvType = (RecyclerView) parent.findViewById(R.id.gv_type);
        TextView tvConfirm = (TextView) parent.findViewById(R.id.tv_confirm);

        tvTypeTitle = (TextView) parent.findViewById(R.id.tv_type_title);
        tvLengthTitle = (TextView) parent.findViewById(R.id.tv_length_title);
        if(StringUtils.isNotBlank(lengthTitle)) {
            tvLengthTitle.setText(lengthTitle);
        }
        scrollView = (ScrollView) parent.findViewById(R.id.scrollView);

        if (CMemoryData.isDriver()) {
            tvConfirm.setBackgroundResource(R.drawable.selector_btn_corner_blue);
        } else {
            tvConfirm.setBackgroundResource(R.drawable.selector_btn_corner_green);
        }

        if (isShowEntirety) {
            tvEntiretyTitle.setVisibility(View.VISIBLE);
            gvEntirety.setVisibility(View.VISIBLE);
            entiretyAdapter = new EntiretyAdapter();
            gvEntirety.setAdapter(entiretyAdapter);
            gvEntirety.setLayoutManager(new GridLayoutManager(activity, 4));
            entiretyAdapter.setNewData(entiretyList);
            entiretyAdapter.setOnItemClickListener(mSelectEntiretyOnItemClickListener);
        } else {
            tvEntiretyTitle.setVisibility(View.GONE);
            gvEntirety.setVisibility(View.GONE);
            ll_transparent.post(new Runnable() {
                @Override
                public void run() {
                    scroll2EmptyView(ll_transparent.getPaddingTop(), tvLengthTitle);      // 没有选择车长车型时需要滚动到相应的位置
                }
            });

        }

        gvLength.setAdapter(truckLengthAdapter);
        gvType.setAdapter(truckTypeAdapter);

        truckLengthAdapter.setNewData(lengthList);
        truckTypeAdapter.setNewData(typeList);

        gvLength.setLayoutManager(new GridLayoutManager(activity, 4));
        gvType.setLayoutManager(new GridLayoutManager(activity, 4));

        truckLengthAdapter.setOnItemClickListener(mSelectLengthOnItemClickListener);
        truckTypeAdapter.setOnItemClickListener(mSelectTypeOnItemClickListener);

        ll_transparent.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        // 设置窗口属性
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        this.setContentView(parent);

        this.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isScreenBottom) {
                    PopupAlphaAnimatorUtil.setOutAlphaAnim(activity);
                }

                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });

        if (isScreenBottom) {
            this.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            this.setClippingEnabled(false);     // 可以超出屏幕，状态栏终于可以变半透明啦
            // 进入动画,透明度改变
            PopupAlphaAnimatorUtil.setInAlphaAnim(activity);
        } else {
            // 去掉顶部高度
            ll_transparent.setPadding(0, 0, 0, 0);
            this.showAsDropDown(view);
        }

//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) parent.getLayoutParams();
//        layoutParams.setMargins(0, 0, 0, ScreenUtils.getNavigationBarHeight((Activity) mContext));
    }

    /**
     * 设置已经勾选的项目
     */
    private void initSelectedData(TruckTypeLengthSelectedData selectedData, int selectedEntiretyIndex) {
        if (selectedData != null) {
            if (selectedData.getIs_carpool() != null) {
                for (int i = 0; i < entiretyList.size(); i++) {
                    if (entiretyList.get(i).getId() == selectedData.getIs_carpool().getId()) {
                        // 为什么要用id来对比？因为服务器要求传的id和界面下标index不对应
                        entiretyList.get(i).setSelected(true);
                    } else {
                        entiretyList.get(i).setSelected(false);
                    }
                }
            } else {
                if (selectedEntiretyIndex >= 0) {       // selectedData查不到数据时，根据selectedEntiretyIndex勾选整车和非整车
                    entiretyList.get(selectedEntiretyIndex).setSelected(true);
                }
            }

            List<TruckLengthInfo> selectedLengthLists = selectedData.getLengthList();
            if (selectedLengthLists != null) {
                for (int i = 0; i < lengthList.size(); i++) {
                    for (int j = 0; j < selectedLengthLists.size(); j++) {
                        if (lengthList.get(i).getId().equals(selectedLengthLists.get(j).getId())) {
                            lengthList.get(i).setSelected(true);
                        }
                    }
                }
            }

            List<TruckTypeInfo> selectedTypes = selectedData.getTypeList();
            if (selectedTypes != null) {
                for (int i = 0; i < typeList.size(); i++) {
                    for (int j = 0; j < selectedTypes.size(); j++) {
                        if (typeList.get(i).getId().equals(selectedTypes.get(j).getId())) {
                            typeList.get(i).setSelected(true);
                        }
                    }
                }
            }
        } else {
            if (selectedEntiretyIndex >= 0) {  // selectedData查不到数据时，根据selectedEntiretyIndex勾选整车和非整车
                entiretyList.get(selectedEntiretyIndex).setSelected(true);
            }
        }
    }

    // 整车和非整车  的点击事件
    // 选项间互斥
    private BaseQuickAdapter.OnItemClickListener mSelectEntiretyOnItemClickListener = new BaseQuickAdapter
            .OnItemClickListener() {
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            for (int i = 0; i < entiretyList.size(); i++) {
                entiretyList.get(i).setSelected(false);
            }
            entiretyList.get(position).setSelected(true);
            entiretyAdapter.setNewData(entiretyList);
        }
    };

    // 车长的点击事件
    // 除了"不限"外都是多选
    // "不限"与其他互斥
    private BaseQuickAdapter.OnItemClickListener mSelectLengthOnItemClickListener = new BaseQuickAdapter
            .OnItemClickListener() {
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            enemy(lengthList, position);
            check_max_out_of_bounds(lengthList, position, LengthMax);
            truckLengthAdapter.setNewData(lengthList);
        }
    };

    // 车型的点击事件
    // 多选
    private BaseQuickAdapter.OnItemClickListener mSelectTypeOnItemClickListener = new BaseQuickAdapter
            .OnItemClickListener() {
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            enemy(typeList, position);
            check_max_out_of_bounds(typeList, position, typeMax);
            truckTypeAdapter.setNewData(typeList);
        }
    };

    /**
     * 处理各种互斥
     */
    private void enemy(List<? extends TypeAndLengthDad> list, int position) {
        if (list != null) {
            if (list.get(position).getId().equals("1"))    // 不限
            {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                list.get(position).setSelected(true);
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals("1")) {
                        list.get(i).setSelected(false); // 不限
                    }
                }
                list.get(position).setSelected(!list.get(position).isSelected());
            }
        }
    }

    /**
     * 检查是否超出最大勾选限制
     */
    private void check_max_out_of_bounds(List<? extends TypeAndLengthDad> list, int position, int max) {
        if (list != null) {
            if (max == -1) {
                return;
            }

            if (deSelectOther) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                list.get(position).setSelected(true);
            } else {
                int selectedCount = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelected()) {
                        selectedCount++;
                    }
                }

                if (selectedCount > max) {
                    list.get(position).setSelected(!list.get(position).isSelected());
                    if (list.size() > 0) {
                        if (list.get(0) instanceof TruckLengthInfo) {
                            Toaster.showLongToast(String.format(ResourceUtils.getString(R.string
                                    .max_out_of_bounds_length), max));
                        } else if (list.get(0) instanceof TruckTypeInfo) {
                            Toaster.showLongToast(String.format(ResourceUtils.getString(R.string
                                    .max_out_of_bounds_type), max));
                        }
                    }
                }
            }
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_confirm) {
            TruckTypeLengthSelectedData tldata = new TruckTypeLengthSelectedData(tag);

            /**
             * 从lengthList中获取选中项，添加到tempLengthList
             * 如果tempLengthList长度为0，提示选择，并滚动到指定位置
             */
            List<TruckLengthInfo> tempLengthList = new ArrayList<TruckLengthInfo>();
            for (int i = 0; i < lengthList.size(); i++) {
                if (lengthList.get(i).isSelected()) {
                    tempLengthList.add(lengthList.get(i));
                }
            }
            if (tempLengthList.size() == 0) {
                Toaster.showShortToast(R.string.please_select_length);
                scroll2EmptyView(ll_transparent.getPaddingTop(), tvLengthTitle);      // 没有选择车长车型时需要滚动到相应的位置
                return;
            }
            tldata.setLengthList(tempLengthList);

            /**
             * 从typeList中获取选中项，添加到tempTypeList
             * 如果tempTypeList长度为0，提示选择，并滚动到指定位置
             */
            List<TruckTypeInfo> tempTypeList = new ArrayList<TruckTypeInfo>();
            for (int i = 0; i < typeList.size(); i++) {
                if (typeList.get(i).isSelected()) {
                    tempTypeList.add(typeList.get(i));
                }
            }
            if (tempTypeList.size() == 0) {
                Toaster.showShortToast(R.string.please_select_type);
                scroll2EmptyView(ll_transparent.getPaddingTop(), tvTypeTitle);  // 没有选择车长车型时需要滚动到相应的位置
                return;
            }
            tldata.setTypeList(tempTypeList);

            for (int i = 0; i < entiretyList.size(); i++) {
                if (entiretyList.get(i).isSelected()) {
                    tldata.setIs_carpool(entiretyList.get(i));     //0 整车 1: 可拼车
                }
            }

            BusManager.getBus().post(tldata);
            this.dismiss();
        } else if (id == R.id.ll_transparent)        // 空白的地方
        {
            this.dismiss();
        }
    }

    /**
     * 没有选择车长车型时需要滚动到相应的位置
     * 需要去除掉scrollView的marginTop值，否则乱跳
     */
    private void scroll2EmptyView(int topMargin, View v) {
        int[] l = new int[2];
        v.getLocationInWindow(l);
        scrollView.scrollTo(0, l[1] - topMargin);
    }
}
