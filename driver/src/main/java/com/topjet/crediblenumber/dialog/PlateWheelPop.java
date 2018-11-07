package com.topjet.crediblenumber.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.crediblenumber.R;

import java.util.ArrayList;

/**
 * 车牌选择
 */
public class PlateWheelPop implements BaseQuickAdapter.OnItemClickListener {
    private Activity activity;
    private PopupWindow mPop;
    private View mParent;
    private AreaAdapter plateAdapter1;
    private ImageView plate_wheel_close;
    private TextView plate_wheel_ok;
    String plate1 = "沪";
    private RecyclerView rv_plate = null;

    private ArrayList<String> sPlateRegion;

    public PlateWheelPop(Activity activity, View mParent, String curText, OnCarPlateCompleteListener onCompletel) {
        this.activity = activity;
        this.mParent = mParent;

        if (!StringUtils.isEmpty(curText)) {
            plate1 = curText;
        }

        initView(onCompletel);
    }

    private void initView(final OnCarPlateCompleteListener onCompletel) {
        View view = LayoutInflater.from(activity).inflate(R.layout.pop_plateselect, null);
        rv_plate = (RecyclerView) view.findViewById(R.id.rv_plate);
        plate_wheel_close = (ImageView) view.findViewById(R.id.plate_wheel_close);

        plate_wheel_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        plate_wheel_ok = (TextView) view.findViewById(R.id.plate_wheel_ok);
        plate_wheel_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onCompletel != null)
                    onCompletel.OnCompleteListener(plate1);
                mPop.dismiss();
            }
        });

        getList();

        plateAdapter1 = new AreaAdapter();
        plateAdapter1.setNewData(sPlateRegion);
        rv_plate.setAdapter(plateAdapter1);
        rv_plate.setLayoutManager(new GridLayoutManager(activity, 6));
        plateAdapter1.setOnItemClickListener(this);
        mPop = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPop.setContentView(view);
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

        mPop.setFocusable(true);
        view.requestFocus();

    }

    public void showPop() {
        mPop.showAtLocation(mParent, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.4f;
        activity.getWindow().setAttributes(params);
    }

    public void dismissPop() {
        mPop.dismiss();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        plate1 = sPlateRegion.get(position);
        plateAdapter1.notifyDataSetChanged();
    }

    class AreaAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public AreaAdapter() {
            super(R.layout.griditem_plate_no);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, final String item) {
            viewHolder.setText(com.topjet.common.R.id.tv_text, item);

            if (item.equals(plate1)) {
                viewHolder.setBackgroundRes(com.topjet.common.R.id.tv_text, com.topjet.common.R.drawable.bg_truck_len_type_item_is_select_blue);
                viewHolder.setTextColor(com.topjet.common.R.id.tv_text, mContext.getResources().getColor(com.topjet.common.R.color.white));
            } else {
                viewHolder.setBackgroundRes(com.topjet.common.R.id.tv_text, com.topjet.common.R.drawable.bg_truck_len_type_item_no_select);
                viewHolder.setTextColor(com.topjet.common.R.id.tv_text, mContext.getResources().getColor(com.topjet.common.R.color.text_color_222222));
            }
        }
    }

    public void getList() {
        /*
         * 车牌地区
		 */
        sPlateRegion = (ArrayList<String>) ListUtils.arrayToList(ResourceUtils.getStringArray(com.topjet.common.R.array.plate_region));
    }

}
