package com.topjet.shipper.deliver.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.foamtrace.photopicker.ImageModel;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.view.dialog.AlbumAndCameraPopup;
import com.topjet.common.common.view.dialog.EditPopupWindow;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.CameraUtil;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.widget.AnFQNumEditText;
import com.topjet.common.widget.MyTitleBar;
import com.topjet.common.widget.viewpagetitle.ViewPagerTitle;
import com.topjet.shipper.R;
import com.topjet.shipper.deliver.modle.bean.TypeAndPackingItem;
import com.topjet.shipper.deliver.modle.extra.OtherInfoExtra;
import com.topjet.shipper.deliver.presenter.OtherInformationPresenter;
import com.topjet.shipper.deliver.view.adapter.TypeAndPackingAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发货--其他信息
 * 货主版
 */
public class OtherInformationActivity extends MvpActivity<OtherInformationPresenter> implements OtherInformationView,
        ViewPagerTitle.OnTextViewClick,    // TabLayout选中监听
        BaseQuickAdapter.OnItemClickListener,   // RecyclerView的item点击事件
        BaseQuickAdapter.OnItemChildClickListener, // RecyclerView的item的子控件的点击事件
        EditPopupWindow.OnEditPopupDismissListener // EditPopup的消失事件
{
    @BindView(R.id.tab_layout)
    ViewPagerTitle viewPagerTitle;
    @BindView(R.id.gv_type)
    RecyclerView gvType;
    @BindView(R.id.gv_packing)
    RecyclerView gvPacking;
    @BindView(R.id.gv_unloading)
    RecyclerView gvUnloading;
    @BindView(R.id.iv_show_photo)
    ImageView ivShowPhoto;
    @BindView(R.id.iv_delete_photo)
    ImageView ivDeletePhoto;
    @BindView(R.id.cb_is_refre)
    CheckBox cbIsRefre;
    @BindView(R.id.tv_refresh_title)
    TextView tvRefreshTitle;
    @BindView(R.id.anFQNumEditText)
    AnFQNumEditText anFQNumEditText;
    @BindView(R.id.rl_refresh)
    RelativeLayout rlRefresh;
    private OtherInfoExtra otherInfoExtra = null;           // 其他信息，不会有m层来更改，就放这里吧

    private TypeAndPackingAdapter typeAdapter = null;       // 货物类型
    private TypeAndPackingAdapter packingAdapter = null;    // 包装方式
    private TypeAndPackingAdapter unloadingAdapter = null;  // 装卸方式
    private String defaultTypeStr = null;
    private String defaultPackStr = null;
    private String defaultLoadStr = null;

    private ArrayList<ImageModel> selectedImgList = new ArrayList<>();      // 选中的图片集合
    private String selectedImgPath = null;      // 选中的图片路径
    private String photoKey = null;         // 图片的key。网络图片会有key，修改后key会清空
    private int curTab = 0;// tablayout选中项

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_other_information;
    }

    @Override
    protected void initTitle() {
        getMyTitleBar().setMode(MyTitleBar.Mode.BACK_TITLE).setTitleText(R.string.other_information);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new OtherInformationPresenter(this, mContext);
    }

    @Override
    public void initView() {


        otherInfoExtra = getIntentExtra(OtherInfoExtra.getExtraName());
        initViews();  // 根据传入参数初始化界面
        initTabLayout();
        viewPagerTitle.setOnTextViewClickListener(this);
        initGridView(gvType, gvPacking, gvUnloading);
        onTabSelected(0); // 切换到默认tablayout Index

        String count = CPersisData.getSpKeyGoodsrefreshinfoRefreshCount();
        String minute = CPersisData.getSpKeyGoodsrefreshinfoRefreshMinute();
        if (StringUtils.isEmpty(count)) {
            count = "4";
        }
        if (StringUtils.isEmpty(minute)) {
            minute = "30";
        }
        tvRefreshTitle.setText("自动刷新" + count + "次，每次间隔" + minute + "分钟");
    }

    /**
     * 初始化界面
     */
    public void initViews() {
        if (otherInfoExtra != null) {
            defaultTypeStr = otherInfoExtra.getType();
            defaultPackStr = otherInfoExtra.getPack();
            defaultLoadStr = otherInfoExtra.getLoad();

            String defaultRemarkStr = otherInfoExtra.getRemark();
            String defaultPhotoStr = otherInfoExtra.getPhoto();
            boolean defalutRefre = otherInfoExtra.getRefre();

            if(otherInfoExtra.isAssigned()){
                rlRefresh.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(defaultRemarkStr)) {
                anFQNumEditText.setText(defaultRemarkStr);
            }
            if (!TextUtils.isEmpty(defaultPhotoStr)) {
                GlideUtils.loaderImageRoundWithSize(mContext, defaultPhotoStr, ivShowPhoto, 10, ScreenUtils.dp2px
                        (mContext, 80), ScreenUtils.dp2px(mContext, 80));
                ivDeletePhoto.setVisibility(View.VISIBLE);
            }
            cbIsRefre.setChecked(defalutRefre);
        }
    }

    public void onTabSelected(int index) {
        gvType.setVisibility(View.GONE);
        gvPacking.setVisibility(View.GONE);
        gvUnloading.setVisibility(View.GONE);
        if (index == 0) {
            gvType.setVisibility(View.VISIBLE);
        } else if (index == 1) {
            gvPacking.setVisibility(View.VISIBLE);
        } else if (index == 2) {
            gvUnloading.setVisibility(View.VISIBLE);
        }
        curTab = index;
    }

    /**
     * 初始化RecyclerView
     */
    public void initGridView(RecyclerView gvType, RecyclerView gvPacking, RecyclerView gvUnloading) {
        mPresenter.initLists(defaultTypeStr, defaultPackStr, defaultLoadStr);

        this.gvType = gvType;
        this.gvPacking = gvPacking;
        this.gvUnloading = gvUnloading;
        typeAdapter = new TypeAndPackingAdapter(R.layout.griditem_truck_type);
        packingAdapter = new TypeAndPackingAdapter(R.layout.griditem_truck_type);
        unloadingAdapter = new TypeAndPackingAdapter(R.layout.griditem_truck_type);
        // 设置列表点击监听
        gvType.setAdapter(typeAdapter);
        gvPacking.setAdapter(packingAdapter);
        gvUnloading.setAdapter(unloadingAdapter);

        gvType.setLayoutManager(new GridLayoutManager(mContext, 4));
        gvPacking.setLayoutManager(new GridLayoutManager(mContext, 4));
        gvUnloading.setLayoutManager(new GridLayoutManager(mContext, 4));

        typeAdapter.setNewData(mPresenter.getTypeList());
        packingAdapter.setNewData(mPresenter.getPackingList());
        unloadingAdapter.setNewData(mPresenter.getLoadingList());

        typeAdapter.setOnItemClickListener(this);
        typeAdapter.setOnItemChildClickListener(this);
        packingAdapter.setOnItemClickListener(this);
        packingAdapter.setOnItemChildClickListener(this);
        unloadingAdapter.setOnItemClickListener(this);
        unloadingAdapter.setOnItemChildClickListener(this);
    }

    /**
     * 初始化tablayout(viewPagerTitle)
     */
    public void initTabLayout() {
        viewPagerTitle.initData(new int[]{R.string.goods_type, R.string.pack_type, R.string.load_type}, 0);
    }

    /**
     * 点击加号，弹出编辑窗
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        List<TypeAndPackingItem> tempList = getTempList();
        String defaultStr = tempList.get(tempList.size() - 1).getItem().getName();
        new EditPopupWindow().showPopupWindow(this, view, defaultStr, this);
    }

    /**
     * 选中的时候变绿
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<TypeAndPackingItem> tempList = getTempList();
        TypeAndPackingAdapter tempAdapter = getTempAdapter();
        tempList.get(position).setSelected(true);
        tempAdapter.setNewData(tempList);


        /**
         * 当用户自定义项为空时，弹出编辑框，否则切换tab
         */
        if (tempList != null) {
            if (tempList.get(position) != null) {
                if (StringUtils.isEmpty(tempList.get(position).getItem().getName())) {
                    String defaultStr = tempList.get(tempList.size() - 1).getItem().getName();
                    new EditPopupWindow().showPopupWindow(this, view, defaultStr, this);
                    return;
                }
            }
        }
        autoSwitchTab();       // 自动切换到下一个tab
    }

    /**
     * editPopup消失的时候设置新值
     */
    @Override
    public void editPopDismiss(String result) {
        List<TypeAndPackingItem> tempList = getTempList();
        TypeAndPackingAdapter tempAdapter = getTempAdapter();
        tempList.get(tempList.size() - 1).getItem().setName(result);
        if (StringUtils.isEmpty(result)) {
            if (tempAdapter == typeAdapter) {
                tempList.get(0).setSelected(true);
            }
        } else {
            tempList.get(tempList.size() - 1).setSelected(true);
        }
        tempAdapter.setNewData(tempList);

        autoSwitchTab();    // 自动切换到下一个tab
    }

    /**
     * 自动切换到下一个tab
     */
    private void autoSwitchTab() {
        curTab++;
        if (curTab > 2) {
            curTab = 0;
        }
        if (curTab != 0) {
            viewPagerTitle.getTextView().get(curTab).performClick();
        }
    }

    @OnClick({R.id.iv_delete_photo, R.id.iv_show_photo, R.id.tv_save})
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.iv_show_photo) {
            // 拍照、相册
            new AlbumAndCameraPopup(this).initPop(1, selectedImgList).showPop(view);
        } else if (id == R.id.iv_delete_photo) {        // 删除当前图片
            ivShowPhoto.setImageResource(R.drawable.icon_add_photo);
            selectedImgList.clear();
            selectedImgPath = null;
            photoKey = null;
            ivDeletePhoto.setVisibility(View.GONE);
        } else if (id == R.id.tv_save) {        // 保存信息，结束页面，回传数据
            otherInfoExtra.setType(mPresenter.getSelectedType(0));
            otherInfoExtra.setPack(mPresenter.getSelectedType(1));
            otherInfoExtra.setLoad(mPresenter.getSelectedType(2));
            otherInfoExtra.setRemark(anFQNumEditText.getText().toString());
            otherInfoExtra.setPhoto(selectedImgPath);
            otherInfoExtra.setPhotoKey(photoKey);
            otherInfoExtra.setRefre(cbIsRefre.isChecked());
            this.setResultAndFinish(Activity.RESULT_OK, otherInfoExtra);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CameraUtil.REQUEST_CODE.SYSTEM_CAMERA) {
                selectedImgPath = CameraUtil.currentPicPath;

                showSelectedPhoto(selectedImgPath);
                ivDeletePhoto.setVisibility(View.VISIBLE);
                photoKey = null;
            } else if (requestCode == CameraUtil.REQUEST_CODE.PHOTO_PICKER) {
                selectedImgList = (ArrayList<ImageModel>) data.getSerializableExtra(PhotoPickerActivity.EXTRA_RESULT);
                selectedImgPath = selectedImgList.get(0).getPath();

                showSelectedPhoto(selectedImgPath);
                ivDeletePhoto.setVisibility(View.VISIBLE);
                photoKey = null;
            }
        }
    }

    /**
     * tablayout的选择
     */
    @Override
    public void textViewClick(TextView textView, int index) {
        onTabSelected(index);
    }

    /**
     * 获取tempList和tempAdapter
     */
    private List<TypeAndPackingItem> getTempList() {
        List<TypeAndPackingItem> tempList = null;
        if (curTab == 0) {
            tempList = mPresenter.getTypeList();
        } else if (curTab == 1) {
            tempList = mPresenter.getPackingList();
        } else if (curTab == 2) {
            tempList = mPresenter.getLoadingList();
        }
        for (int i = 0; i < tempList.size(); i++) {
            tempList.get(i).setSelected(false);
        }

        for (int i = 0; i < tempList.size(); i++) {
            tempList.get(i).setSelected(false);
        }
        return tempList;
    }

    /**
     * 获取tempList和tempAdapter
     */
    private TypeAndPackingAdapter getTempAdapter() {
        TypeAndPackingAdapter tempAdapter = null;
        if (curTab == 0) {
            tempAdapter = typeAdapter;
        } else if (curTab == 1) {
            tempAdapter = packingAdapter;
        } else if (curTab == 2) {
            tempAdapter = unloadingAdapter;
        }
        return tempAdapter;
    }

    /**
     * 显示 选择的图片
     */
    public void showSelectedPhoto(String url) {
        GlideUtils.loaderImageRoundWithSize(mContext, url, ivShowPhoto, 5, ivShowPhoto.getWidth(),
                ivShowPhoto.getHeight());
    }


}
