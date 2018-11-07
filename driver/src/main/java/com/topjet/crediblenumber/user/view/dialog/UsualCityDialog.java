package com.topjet.crediblenumber.user.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.topjet.common.base.listener.DebounceClickListener;
import com.topjet.common.common.modle.bean.UsualCityBean;
import com.topjet.common.utils.ScreenUtils;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.view.adapter.UsualCityDialogAdapter;

import java.util.ArrayList;

/**
 * 常跑城市弹窗
 * created by yy
 */
public class UsualCityDialog extends Dialog {
    private UsualCityDialogAdapter mAdapter;
    private OnUsualCityDialogListener mListener;
    private int index;
    public View rootView;

    public interface OnUsualCityDialogListener {
        /**
         * 修改准备添加的常跑城市
         */
        void onItemClick(View view);

        /**
         * 删除准备添加的常跑城市
         */
        void onIconDeleteClick(int position);

        /**
         * 确定按钮
         */
        void onClickConfirm(ArrayList<UsualCityBean> list);

        /**
         * 添加城市 按钮
         */
        void onClickAddCity(View rootView);
    }

    public UsualCityDialog(Context context) {
        super(context, R.style.AutoDialogTheme);
        initView(context);
    }

    private void initView(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_usual_city, null);
        this.setContentView(rootView);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        TextView btnConfirm = (TextView) findViewById(R.id.btn_confirm);
        Button btnAddCity = (Button) findViewById(R.id.btn_add_city);

        btnAddCity.setOnClickListener(clickListener);
        btnConfirm.setOnClickListener(clickListener);

        mAdapter = new UsualCityDialogAdapter();
        mAdapter.setOnItemClickListener(onItemClickListener);
        mAdapter.setOnItemChildClickListener(onItemChildClickListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));//这里用线性显示 类似于listview
        recyclerView.setAdapter(mAdapter);

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        initDialogWidth(context);// 设置Dialog宽度
    }

    /**
     * 初始化Dialog宽度
     */
    private void initDialogWidth(final Context context) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                FrameLayout.LayoutParams rootParams = (FrameLayout.LayoutParams) rootView.getLayoutParams();
                rootParams.width = ScreenUtils.getScreenWidth(context) * 9 / 10;
                rootView.setLayoutParams(rootParams);
            }
        });
    }

    /**
     * 按钮点击事件
     */
    private DebounceClickListener clickListener = new DebounceClickListener() {
        @Override
        public void performClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_confirm) {
                if (mListener != null) {
                    mListener.onClickConfirm((ArrayList<UsualCityBean>) mAdapter.getData());
                }
            } else if (id == R.id.btn_add_city) {
                if (mListener != null) {
                    mListener.onClickAddCity(rootView);
                }
            }
        }
    };

    /**
     * 每项Item的点击事件
     */
    private BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (mListener != null) {
                index = position;
                mListener.onItemClick(view);
            }
        }
    };

    /**
     * 删除图标点击事件
     */
    private BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter
            .OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            if (mListener != null) {
                mListener.onIconDeleteClick(position);
            }
        }
    };

    /**
     * 显示常跑城市，设置默认的显示
     */
    public void setListDatas(ArrayList<UsualCityBean> data) {
        mAdapter.setNewData(data);
    }

    /**
     * 添加了新的常跑城市
     */
    public void appendNewUsualCityDatas(UsualCityBean data) {
        ArrayList<UsualCityBean> dataList = (ArrayList<UsualCityBean>) mAdapter.getData();
        dataList.add(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 修改了新的常跑城市
     */
    public void updataNewUsualCityDatas(UsualCityBean data) {
        ArrayList<UsualCityBean> dataList = (ArrayList<UsualCityBean>) mAdapter.getData();
        dataList.remove(index);
        dataList.add(index, data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 删除选中的常跑城市
     */
    public void deleteUsualCityDatas(int position) {
        ArrayList<UsualCityBean> dataList = (ArrayList<UsualCityBean>) mAdapter.getData();
        dataList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取常跑城市集合数据
     */
    public ArrayList<UsualCityBean> getUsualCityList() {
        return (ArrayList<UsualCityBean>) mAdapter.getData();
    }

    /**
     * 展示/隐藏Dialog
     */
    public void toggleShow() {
        if (isShowing()) {
            dismiss();
        } else {
            show();
        }
    }

    public void setOnUsualCityDialogListener(OnUsualCityDialogListener mListener) {
        this.mListener = mListener;
    }
}
