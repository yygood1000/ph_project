package com.topjet.shipper.familiar_car.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.topjet.common.base.dialog.AutoDialogHelper;
import com.topjet.common.base.view.activity.MvpActivity;
import com.topjet.common.common.modle.bean.TruckInfo;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.GlideUtils;
import com.topjet.common.utils.ResourceUtils;
import com.topjet.common.utils.StringUtils;
import com.topjet.shipper.R;

/**
 * creator: zhulunjun
 * time:    2017/10/12
 * describe: 车辆信息适配器
 */

public class TruckListAdapter extends BaseQuickAdapter<TruckInfo, BaseViewHolder> {
    private boolean isShowBtn = true;// 是否显示按钮
    private FamiliarCarClick familiarCarClick;
    private Context mContext;

    public void setFamiliarCarClick(FamiliarCarClick familiarCarClick) {
        this.familiarCarClick = familiarCarClick;
    }

    public interface FamiliarCarClick {
        /**
         * item点击事件
         */
        void contentClick(TruckInfo item);

        /**
         * 删除熟车
         */
        void deleteClick(TruckInfo item);

        /**
         * 添加熟车
         */
        void addClick(TruckInfo item);

        /**
         * 给他下单
         */
        void placeOrderClick(TruckInfo item);

        /**
         * 拨打电话
         */
        void callClick(TruckInfo item);

        /**
         * 发生消息
         */
        void messageClick(TruckInfo item);

        /**
         * 司机位置
         */
        void clickDriverLocation(TruckInfo item);
    }

    public TruckListAdapter(Context mContext) {
        super(R.layout.listitem_familiar_car);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, final TruckInfo item) {

        // 车辆信息
        helper.setText(R.id.tv_truck_info, item.getTruck_type_name() + " " + item.getTruck_length_name());
        String orderNum = StringUtils.format(R.string.order_num_str, item.getDriver_order_count());
        // 司机信息
        helper.setText(R.id.tv_driver_info, item.getDriver_name() + " " + orderNum);
        // 车头照
        ImageView ivAvatar = helper.getView(R.id.iv_car);
        ivAvatar.setImageResource(R.drawable.icon_car_default);
        if(StringUtils.isNotBlank(item.getTruck_icon_url()) &&
                StringUtils.isNotBlank(item.getTruck_icon_key())) {
            GlideUtils.loaderImageRound(mContext,
                    item.getTruck_icon_url(),
                    item.getTruck_icon_key(),
                    R.drawable.icon_car_default,
                    R.drawable.icon_car_loading,
                    ivAvatar,
                    4);
        }

        TextView tvPlateNumber = helper.getView(R.id.tv_plate_number);
        // 车牌号 1 蓝色 2 黄色
        if (item.getPlate_color().equals("1")) {
            tvPlateNumber.setBackgroundResource(R.drawable.shape_blue_r2);
        } else {
            tvPlateNumber.setBackgroundResource(R.drawable.shape_yellow_r2);
        }
        tvPlateNumber.setText(item.getPlateNo());

        // 地址
        if (StringUtils.isEmpty(item.getGps_address())) {
            helper.getView(R.id.ll_address).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.ll_address).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_address, item.getGps_address());
        }

        // 按钮
        if (isShowBtn) {
            helper.getView(R.id.ll_btn).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.ll_btn).setVisibility(View.GONE);
        }

        TextView tvBtnLeft = helper.getView(R.id.tv_btn_left);
        if (item.getIsFamiliarTruck()) {
            helper.setText(R.id.tv_btn_left, ResourceUtils.getString(R.string.delete_familiar_truck));
        } else {
            helper.setText(R.id.tv_btn_left, ResourceUtils.getString(R.string.add_familiar_truck));
        }


        // Item的点击事件
        helper.getView(R.id.ll_familiar_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familiarCarClick != null) {
                    CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new CheckUserStatusUtils
                            .OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            familiarCarClick.contentClick(item);
                        }
                    });
                }
            }
        });

        // 给他下单
        helper.getView(R.id.tv_btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familiarCarClick != null) {
                    CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new CheckUserStatusUtils
                            .OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            familiarCarClick.placeOrderClick(item);
                        }
                    });
                }
            }
        });

        // 添加. 删除熟车
        tvBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familiarCarClick != null) {
                    CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new CheckUserStatusUtils
                            .OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            // 判断是否是熟车，是才删除，不是添加
                            if (item.getIsFamiliarTruck()) {
                                showDeleteDialog(item);
                            } else {
                                familiarCarClick.addClick(item);
                            }
                        }
                    });
                }
            }
        });

        // 车辆地址点击事件
        helper.getView(R.id.ll_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familiarCarClick != null) {
                    familiarCarClick.clickDriverLocation(item);
                }
            }
        });

        // 电话点击事件
        helper.getView(R.id.iv_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familiarCarClick != null) {
                    CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new CheckUserStatusUtils
                            .OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            familiarCarClick.callClick(item);
                        }
                    });
                }
            }
        });

        // 聊天点击事件
        helper.getView(R.id.iv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familiarCarClick != null) {
                    CheckUserStatusUtils.isRealNameAuthentication((MvpActivity) mContext, new CheckUserStatusUtils
                            .OnJudgeResultListener() {
                        @Override
                        public void onSucceed() {
                            familiarCarClick.messageClick(item);
                        }
                    });
                }
            }
        });
    }

    public void setShowBtn(boolean showBtn) {
        isShowBtn = showBtn;
    }

    /**
     * 展示删除熟车弹窗
     */
    private void showDeleteDialog(final TruckInfo item) {
        AutoDialogHelper.showContent(mContext, "删除后将在熟车列表中消失，\n确认删除？", new AutoDialogHelper.OnConfirmListener() {
            @Override
            public void onClick() {
                familiarCarClick.deleteClick(item);
            }
        }).show();
    }
}
