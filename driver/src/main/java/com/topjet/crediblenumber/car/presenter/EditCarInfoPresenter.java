package com.topjet.crediblenumber.car.presenter;

import android.app.Activity;
import android.content.Context;

import com.topjet.common.base.listener.ObserverOnNextListener;
import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BasePresenter;
import com.topjet.common.resource.bean.TruckLengthInfo;
import com.topjet.common.resource.bean.TruckTypeInfo;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.extra.GoToCarInfoExtra;
import com.topjet.crediblenumber.car.modle.params.CarIDParams;
import com.topjet.crediblenumber.car.modle.serverAPI.CarCommand;
import com.topjet.crediblenumber.car.modle.serverAPI.CarCommandAPI;
import com.topjet.crediblenumber.car.view.activity.EditCarInfoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsj-004 on 2017/10/12.
 * 车辆详情
 */

public class EditCarInfoPresenter extends BasePresenter<EditCarInfoView> {
    private TruckTeamCar truckTeamCar = null;

    private TruckTypeLengthSelectedData tld = null;            // 车型车长勾选数据
    private String truckLengthId = "";       // 车长id
    private String truckTypeId = "";         // 车型id

    public EditCarInfoPresenter(EditCarInfoView mView, Context mContext) {
        super(mView, mContext);
    }

    /**
     * 请求服务器接口
     * 查看车辆详情
     */
    public void getTruckInfo(String id) {
        CarIDParams carID = new CarIDParams(id);
        new CarCommand(CarCommandAPI.class, mActivity).getTruckInfo(carID, new ObserverOnNextListener<TruckTeamCar>() {
            @Override
            public void onNext(TruckTeamCar car) {
                if (car != null) {
                    mView.showViewByParams(car);
                    truckTypeId = car.getTruck_type_id();
                    truckLengthId = car.getTruck_length_id();

                    truckTeamCar = car;

                    /**
                     * 设置车型勾选数据
                     */
                    tld = new TruckTypeLengthSelectedData();
                    List<TruckTypeInfo> typeInfoList = new ArrayList<TruckTypeInfo>();
                    TruckTypeInfo typeInfo = new TruckTypeInfo();
                    typeInfo.setId(truckTypeId);
                    typeInfoList.add(typeInfo);
                    tld.setTypeList(typeInfoList);

                    /**
                     * 设置车长勾选数据
                     */
                    List<TruckLengthInfo> lengthInfoList = new ArrayList<TruckLengthInfo>();
                    TruckLengthInfo lengthInfo = new TruckLengthInfo();
                    lengthInfo.setId(truckLengthId);
                    lengthInfoList.add(lengthInfo);
                    tld.setLengthList(lengthInfoList);
                }
            }

            @Override
            public void onError(String errorCode, String msg) {
                Toaster.showLongToast(msg);
            }
        });
    }

    /**
     * 请求服务器接口
     * 添加车辆
     */
    public void addTruck(String id, String plate_no1, String carNumber, String plate_color, String truck_icon_img, String truck_license_img, String driver_name, String driver_mobile) {
        boolean isSuccess = paramsSuccess(plate_no1, carNumber, plate_color, truck_icon_img, truck_license_img);
        if (isSuccess) {
            truckTeamCar = new TruckTeamCar();
            processingParameters(truckTeamCar, id, plate_no1, carNumber, plate_color, truck_icon_img, truck_license_img, driver_name, driver_mobile); // 处理参数
            new CarCommand(CarCommandAPI.class, mActivity).addTruck(truckTeamCar, new ObserverOnNextListener<CarIDParams>() {
                @Override
                public void onNext(CarIDParams car) {
                    if (car != null) {
                        mActivity.setResultAndFinish(Activity.RESULT_OK);
                    }
                }

                @Override
                public void onError(String errorCode, String msg) {
                    Toaster.showLongToast(msg);
                }
            });
        }
    }

    /**
     * 参数是否合法
     */
    private boolean paramsSuccess(String plate_no1, String carNumber, String plate_color, String truck_icon_img, String truck_license_img) {
        if (StringUtils.isEmpty(plate_no1)) {
            mView.showError(R.string.plate_no1_error);
            return false;
        }
        if (StringUtils.isEmpty(carNumber)) {
            mView.showError(R.string.carNumber_error);
            return false;
        }
        if (StringUtils.isEmpty(plate_color)) {
            mView.showError(R.string.plate_color_error);
            return false;
        }
//        if (StringUtils.isEmpty(truck_icon_img)) {
//            mView.showError(R.string.truck_icon_img_error);
//            return false;
//        }
//        if (StringUtils.isEmpty(truck_license_img)) {
//            mView.showError(R.string.truck_license_img_error);
//            return false;
//        }
        return true;
    }

    /**
     * 请求服务器接口
     * 修改车辆信息
     */
    public void editTruckInfo(String id, String plate_no1, String carNumber, String plate_color, String truck_icon_img, String truck_license_img, String driver_name, String driver_mobile) {
        boolean isSuccess = true;//paramsSuccess(plate_no1, carNumber, plate_color, truck_icon_img, truck_license_img);
        if (isSuccess) {
            processingParameters(truckTeamCar, id, plate_no1, carNumber, plate_color, truck_icon_img, truck_license_img, driver_name, driver_mobile); // 处理参数
            new CarCommand(CarCommandAPI.class, mActivity).updateTruck(truckTeamCar, new ObserverOnResultListener<Object>() {
                @Override
                public void onResult(Object object) {
                    mActivity.setResultAndFinish(Activity.RESULT_OK);
                }
            });
        }
    }

    /**
     * 处理参数
     */
    private void processingParameters(TruckTeamCar truck, String id, String plate_no1, String carNumber, String plate_color, String truck_icon_img, String truck_license_img, String driver_name, String driver_mobile) {
        if (truck == null) {
            truck = new TruckTeamCar();
        }
        truck.setDriver_truck_id(id);
        truck.setPlate_no1(plate_no1);
        truck.setPlate_no2(carNumber.substring(0, 1));
        truck.setPlate_no3(carNumber.substring(1));
        plate_color = new GoToCarInfoExtra().getCarNumberColorId(plate_color);
        truck.setPlate_color(plate_color);
        if (!StringUtils.isEmpty(truckTypeId)) {
            truck.setTruck_type_id(truckTypeId);
        }
        if (!StringUtils.isEmpty(truckLengthId)) {
            truck.setTruck_length_id(truckLengthId);
        }
        if (!StringUtils.isEmpty(truck_icon_img) && new File(truck_icon_img).exists()) {
            truck_icon_img = ImageUtil.getBase64With480x800(truck_icon_img);
            truck.setTruck_icon_img(truck_icon_img);
        }

        if (!StringUtils.isEmpty(truck_license_img) && new File(truck_license_img).exists()) {
            truck_license_img = ImageUtil.getBase64With480x800(truck_license_img);
            truck.setTruck_license_img(truck_license_img);
        }

        truck.setDriver_name(driver_name);
        truck.setDriver_mobile(driver_mobile);
    }

    /**
     * 从车型车长选择器中获取数据
     */
    public void selectTypeAndLength(TruckTypeLengthSelectedData tld) {
        this.tld = tld;
        StringBuffer stringBuffer = new StringBuffer();

        List<TruckTypeInfo> typeList = tld.getTypeList();
        if (typeList != null) {
            for (int i = 0; i < typeList.size(); i++) {
                stringBuffer.append(typeList.get(i).getDisplayName() + " ");
                truckTypeId = typeList.get(i).getId();
            }
        }

        List<TruckLengthInfo> lengthList = tld.getLengthList();
        if (lengthList != null) {
            for (int i = 0; i < lengthList.size(); i++) {
                stringBuffer.append(lengthList.get(i).getDisplayName());
                truckLengthId = lengthList.get(i).getId();
                if (i == lengthList.size()) {
                    break;
                }
                stringBuffer.append(" ");
            }
        }

        mView.setTypeAndLengthText(stringBuffer.toString());
    }

    public TruckTypeLengthSelectedData getTld() {
        return tld;
    }

    public void setTld(TruckTypeLengthSelectedData tld) {
        this.tld = tld;
    }
}
