package com.topjet.crediblenumber.goods.presenter;

import com.topjet.common.adv.modle.bean.GoodsListAdvBean;
import com.topjet.common.common.modle.bean.GoodsListData;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;

import java.util.ArrayList;

/**
 * Created by yy on 2017/11/30.
 * 处理货源列表广告
 */

public class ProcessGoodsListAdv {
    public static void insertAdvInfoToList(ArrayList<GoodsListData> goodsList, ArrayList<GoodsListAdvBean>
            advListData) {
        if (!ListUtils.isEmpty(goodsList)) {
            int index;
            for (GoodsListAdvBean adv : advListData) {
                index = Integer.parseInt(adv.getIndex_number());
                // 广告下标小于等于数据长度
                if (index <= goodsList.size()) {
                    // 向对应下标插入广告
                    if (!adv.isInserted()) {
                        Logger.i("oye", "未插入过，插入广告" + adv.isInserted());
                        adv.setInserted(true);
                        Logger.i("oye", "设置插入标签后" + adv.isInserted());
                        goodsList.add(index, new GoodsListData(adv));
                    }
                } else {
                    return;
                }
            }
        } else {
            Logger.i("oye", "货源集合为空，不插入广告");
        }
    }

}
