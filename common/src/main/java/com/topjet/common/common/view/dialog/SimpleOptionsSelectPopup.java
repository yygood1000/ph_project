package com.topjet.common.common.view.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.R;
import com.topjet.common.resource.bean.OptionItem;
import com.topjet.common.common.view.adapter.SimpleOptionsAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 2017/9/3.
 * 简单的筛选弹窗
 * 使用到的地方：1.我的推荐记录筛选窗
 */

public class SimpleOptionsSelectPopup extends PopupWindow {
    public interface SimpleOptionsPopListener {
        void onItemClick(OptionItem item);

        void onPopdismissListener();
    }

    /**
     * 展示简单筛选弹窗
     *
     * @param listData 需要展示的并进行选择的集合数据
     */
    public void showPopupWindow(final Activity activity, View view,
                                final ArrayList<OptionItem> listData,
                                final SimpleOptionsPopListener mListener) {

        View parent = View.inflate(activity, R.layout.ppw_simple_options, null);
        parent.setFocusable(true);

        SimpleOptionsAdapter mAdapter = new SimpleOptionsAdapter();
        // 获取控件,并设置列表数据
        RecyclerView recyclerView = (RecyclerView) parent.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(listData);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mListener.onItemClick(listData.get(position));
                dismiss();
            }
        });
        // 设置窗口属性
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setContentView(parent);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // 通知外部弹窗消失
                mListener.onPopdismissListener();
            }
        });
        this.showAsDropDown(view);
    }
}
