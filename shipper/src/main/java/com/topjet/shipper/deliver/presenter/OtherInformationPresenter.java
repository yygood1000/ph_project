package com.topjet.shipper.deliver.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.resource.bean.OptionItem;
import com.topjet.common.resource.dict.CommonDataDict;
import com.topjet.shipper.deliver.modle.bean.TypeAndPackingItem;
import com.topjet.shipper.deliver.view.activity.OtherInformationView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tsj-004 on 2017/8/22.
 * 发货--> 其他信息
 */

public class OtherInformationPresenter extends BasePresenter<OtherInformationView> {
    private List<TypeAndPackingItem> typeList = null;       // 货物类型
    private List<TypeAndPackingItem> packingList = null;    // 包装方式
    private List<TypeAndPackingItem> loadingList = null;    // 装卸方式

    public OtherInformationPresenter(OtherInformationView mView, Context mContext) {
        super(mView, mContext);
    }

    /**
     * 获取选中项
     */
    public String getSelectedType(int type) {
        List<TypeAndPackingItem> tempList = null;
        if (type == 0) {
            tempList = typeList;
        } else if (type == 1) {
            tempList = packingList;
        } else if (type == 2) {
            tempList = loadingList;
        }
        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).isSelected()) {
                return tempList.get(i).getItem().getName();
            }
        }
        return null;
    }

    /**
     * 初始化三个list集合
     */
    public void initLists(String defaultTypeStr, String defaultPackStr, String defaultLoadStr) {
        typeList = new LinkedList();
        ArrayList<OptionItem> tempList = CommonDataDict.getGoodsTypeList();
        boolean noInCDT = true;     // 字典中不存在，即 这是用户输入的数据
        for (int i = 0; i < tempList.size(); i++) {
            TypeAndPackingItem typeAndPackingItem = new TypeAndPackingItem(tempList.get(i), false);
            if (!TextUtils.isEmpty(defaultTypeStr)) {
                if (typeAndPackingItem.getItem().getName().equals(defaultTypeStr)) {
                    typeAndPackingItem.setSelected(true);
                    noInCDT = false;
                }
            } else if (typeAndPackingItem.getItem().getCode().equals("0")) {    // 普货，默认勾选
                typeAndPackingItem.setSelected(true);
            }
            typeList.add(typeAndPackingItem);
        }

        if (noInCDT) {       // 字典中没找到，证明这是用户输入的数据
            if (TextUtils.isEmpty(defaultTypeStr)) {
                typeList.add(new TypeAndPackingItem(new OptionItem(defaultTypeStr), true));
            } else {
                TypeAndPackingItem t = new TypeAndPackingItem(new OptionItem(defaultTypeStr), true);
                t.setSelected(true);
                typeList.add(t);
            }
        } else {// 字典中找到了，增加一项空的
            typeList.add(new TypeAndPackingItem(new OptionItem(""), true));
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        packingList = new LinkedList();
        ArrayList<OptionItem> tempList2 = CommonDataDict.getPackingTypeList();
        boolean noInCDT2 = true;     // 字典中不存在，即 这是用户输入的数据
        for (int i = 0; i < tempList2.size(); i++) {
            TypeAndPackingItem typeAndPackingItem2 = new TypeAndPackingItem(tempList2.get(i), false);
            if (!TextUtils.isEmpty(defaultPackStr) && typeAndPackingItem2.getItem().getName().equals(defaultPackStr)) {
                typeAndPackingItem2.setSelected(true);
                noInCDT2 = false;
            }
            packingList.add(typeAndPackingItem2);
        }

        if (noInCDT2) {       // 字典中没找到，证明这是用户输入的数据
            if (TextUtils.isEmpty(defaultPackStr)) {
                packingList.add(new TypeAndPackingItem(new OptionItem(defaultPackStr), true));
            } else {
                TypeAndPackingItem t = new TypeAndPackingItem(new OptionItem(defaultPackStr), true);
                t.setSelected(true);
                packingList.add(t);
            }
        } else {// 字典中找到了，增加一项空的
            packingList.add(new TypeAndPackingItem(new OptionItem(""), true));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        loadingList = new LinkedList();
        ArrayList<OptionItem> tempList3 = CommonDataDict.getLoadTypeList();
        for (int i = 0; i < tempList3.size(); i++) {
            TypeAndPackingItem typeAndPackingItem3 = new TypeAndPackingItem(tempList3.get(i), false);
            if (!TextUtils.isEmpty(defaultLoadStr) && typeAndPackingItem3.getItem().getName().equals(defaultLoadStr)) {
                typeAndPackingItem3.setSelected(true);
            } else if (TextUtils.isEmpty(defaultLoadStr) && "0".equals(typeAndPackingItem3.getItem().getCode())) {    // 装卸方式，一装一卸，默认勾选
                typeAndPackingItem3.setSelected(true);
            }
            loadingList.add(typeAndPackingItem3);
        }
    }

    public List<TypeAndPackingItem> getTypeList() {
        return typeList;
    }

    public List<TypeAndPackingItem> getPackingList() {
        return packingList;
    }

    public List<TypeAndPackingItem> getLoadingList() {
        return loadingList;
    }
}