package com.topjet.crediblenumber.user.presenter;

import android.content.Context;

import com.topjet.common.base.listener.ObserverOnResultListener;
import com.topjet.common.base.presenter.BaseApiPresenter;
import com.topjet.common.resource.bean.TruckLengthInfo;
import com.topjet.common.resource.bean.TruckTypeInfo;
import com.topjet.common.common.modle.event.TruckTypeLengthSelectedData;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.CheckUserStatusUtils;
import com.topjet.common.utils.ImageUtil;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.Toaster;
import com.topjet.crediblenumber.R;
import com.topjet.crediblenumber.user.modle.params.SaveTruckParams;
import com.topjet.crediblenumber.user.modle.serverAPI.AuthenticationCommand;
import com.topjet.crediblenumber.user.view.activity.CarAuthenticationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 2017/7/29.
 * 车辆认证
 */

public class CarAuthenticationPresenter extends BaseApiPresenter<CarAuthenticationView, AuthenticationCommand> {
    public int TYPE_CAR_FRONT_PHOTO = 0;// 车头照
    public int TYPE_VEHICLE_LICENSE_PHOTO = 1;// 行驶证
    private TruckTypeLengthSelectedData tld = null;            // 车型车长勾选数据
    private SaveTruckParams saveTruckParams = null;
    private String truckLengthId = "";       // 车长id
    private String truckTypeId = "";         // 车型id

    public CarAuthenticationPresenter(CarAuthenticationView mView, Context mContext, AuthenticationCommand
            mApiCommand) {
        super(mView, mContext, mApiCommand);
    }

    /**
     * 车辆认证，请求服务器接口
     */
    public void savetruck(String plate_no1, String carNumber, String plate_color, String frontPath, String
            vehicleLicensePath) {
        boolean isSuccess = paramsSuccess(true, plate_no1, carNumber, plate_color, frontPath, vehicleLicensePath);
        if (isSuccess) {
            processingParameters(plate_no1, carNumber, plate_color, frontPath, vehicleLicensePath); // 处理参数
            mApiCommand.savetruck(saveTruckParams, new ObserverOnResultListener() {
                @Override
                public void onResult(Object o) {
                    CMemoryData.setCarState(CheckUserStatusUtils.TRUCK_STATUS_DATA_WAIT_REVIEWED);
                    mView.submitSuccess();
                }
            });
        }
    }

    /**
     * 参数是否合法
     */
    public boolean paramsSuccess(boolean showError, String plate_no1, String carNumber, String plate_color, String
            truck_icon_img, String truck_license_img) {
        int errorId = 0;
        if (StringUtils.isEmpty(plate_no1)) {
            errorId = R.string.plate_no1_error;
            return false;
        }
        if (StringUtils.isEmpty(carNumber)) {
            errorId = R.string.carNumber_error;
            return false;
        }
        if (carNumber.length() < 2) {
            errorId = R.string.carNumberLength_error;
            return false;
        }
        if (StringUtils.isEmpty(plate_color)) {
            errorId = R.string.plate_color_error;
            return false;
        }
        if (StringUtils.isEmpty(truckTypeId) || StringUtils.isEmpty(truckLengthId)) {
            errorId = R.string.please_select_length;
            return false;
        }
        if (StringUtils.isEmpty(truck_icon_img)) {
            errorId = R.string.truck_icon_img_error;
            return false;
        }
        if (StringUtils.isEmpty(truck_license_img)) {
            errorId = R.string.truck_license_img_error;
            return false;
        }
        if (showError && errorId != 0) {
            Toaster.showLongToast(errorId);
        }
        return true;
    }

    /**
     * 处理参数
     */
    private void processingParameters(String plate_no1, String carNumber, String plate_color, String frontPath,
                                      String vehicleLicensePath) {
        if (saveTruckParams == null) {
            saveTruckParams = new SaveTruckParams();
        }
        saveTruckParams.setPlate_no1(plate_no1);
        saveTruckParams.setPlate_no2(carNumber.substring(0, 1));
        saveTruckParams.setPlate_no3(carNumber.substring(1));
        plate_color = getCarNumberColorId(plate_color);
        saveTruckParams.setPlate_color(plate_color);
        if (!StringUtils.isEmpty(truckTypeId)) {
            saveTruckParams.setTruck_typeId(truckTypeId);
        }
        if (!StringUtils.isEmpty(truckLengthId)) {
            saveTruckParams.setTruck_lengthId(truckLengthId);
        }
        if (!StringUtils.isEmpty(frontPath) && new File(frontPath).exists()) {
            String truck_icon_img = ImageUtil.getBase64With480x800(frontPath);
            saveTruckParams.setTruck_head_img(truck_icon_img);
            saveTruckParams.setTruck_head_img_key(null);
        }

        if (!StringUtils.isEmpty(vehicleLicensePath) && new File(vehicleLicensePath).exists()) {
            String truck_license_img = ImageUtil.getBase64With480x800(vehicleLicensePath);
            saveTruckParams.setDriver_license_img(truck_license_img);
            saveTruckParams.setDriver_license_img_key(null);
        }
    }

    /**
     * 获取车牌颜色Id
     */
    public String getCarNumberColorId(String plate_color) {
        // 1 蓝色 2 黄色
        if (plate_color.equals("蓝牌")) {
            return "1";
        } else {
            return "2";
        }
    }

    /**
     * 车辆认证状态，请求服务器接口
     */
    public void queryCarAuthenStatus() {
        mApiCommand.queryCarAuthenStatus(new ObserverOnResultListener<SaveTruckParams>() {
            @Override
            public void onResult(SaveTruckParams params) {
                if (params != null) {
                    /**
                     * 1 未认证 2 已认证 3 认证中 4 认证失败
                     */
                    boolean showResult = false;
                    if (params.getAudit_status() != null) {
                        CMemoryData.setCarState(params.getAudit_status());
                        switch (params.getAudit_status()) {
                            case "2":
                            case "3":
                                showResult = true;
                        }
                    }
                    mView.changeShowMode(showResult);

                    mView.showCarFrontPhoto(params.getTruck_head_img_url(), params.getTruck_head_img_key());
                    mView.showVehicleLicense(params.getDriver_license_img_url(), params.getDriver_license_img_key());
                    mView.setUseStatus(params.getAudit_status_remark(), params.getAudit_status());

                    if (!StringUtils.isEmpty(params.getTruck_type()) && !StringUtils.isEmpty(params.getTruck_length())) {
                        truckTypeId = params.getTruck_typeId();
                        truckLengthId = params.getTruck_lengthId();
                        mView.setTypeAndLengthText(StringUtils.appendWithSpace(params.getTruck_type(), params
                                .getTruck_length()));
                    }

                    if (!StringUtils.isEmpty(params.getPlate_color())) {
                        if (!StringUtils.isEmpty(params.getPlate_no1())) {
                            if (!StringUtils.isEmpty(params.getPlate_no2())) {
                                if (!StringUtils.isEmpty(params.getPlate_no3())) {
                                    mView.showViewByParams(params.getPlate_color(), params.getPlate_no1(),
                                            StringUtils.appendWithQuotationMarks(params.getPlate_no2(), params.getPlate_no3()),
                                            StringUtils.appendWithQuotationMarks(params.getPlate_no1(), params.getPlate_no2(),
                                                    params.getPlate_no3()));
                                }
                            }
                        }
                    }

                    CPersisData.setUserVersion(params.getVersion());
                    saveTruckParams = params;

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
        });
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
