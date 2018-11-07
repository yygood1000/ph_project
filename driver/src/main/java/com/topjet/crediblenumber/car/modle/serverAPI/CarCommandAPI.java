package com.topjet.crediblenumber.car.modle.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.crediblenumber.car.modle.bean.TruckTeamCar;
import com.topjet.crediblenumber.car.modle.params.CarIDParams;
import com.topjet.crediblenumber.car.modle.response.MyFleetResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by tsj-004 on 2017/10/16.
 * 我的车队
 */

public interface CarCommandAPI {
    // 我的车队--查看车队列表
    String GET_TRUCK_TEAM_LIST = "truckteam.list";
    @POST("truck-service/truckteam/list")
    Observable<BaseResponse<MyFleetResponse>> getTruckTeamList(@Body CommonParams params);

    // 我的车队--求货/休息状态切换-----单个
    String CHANGE_CAR_WORK_STATUS = "truckteam.switchstatus";
    @POST("truck-service/truckteam/switchstatus")
    Observable<BaseResponse<Object>> changeCarWorkStatus(@Body CommonParams<CarIDParams> params);

    // 我的车队--求货/休息状态切换-----全部
    String CHANGE_ALL_CAR_WORK_STATUS = "truckteam.switchstatusall";
    @POST("truck-service/truckteam/switchstatusall")
    Observable<BaseResponse<Object>> changeAllCarWorkStatus(@Body CommonParams<CarIDParams> params);

    // 我的车队--查看车辆详情
    String GET_TRUCK_INFO = "truckteam.truckinfo";
    @POST("truck-service/truckteam/truckinfo")
    Observable<BaseResponse<TruckTeamCar>> getTruckInfo(@Body CommonParams<CarIDParams> params);

    // 我的车队--添加车辆
    String ADD_TRUCK = "truckteam.addtruck";
    @POST("truck-service/truckteam/addtruck")
    Observable<BaseResponse<CarIDParams>> addTruck(@Body CommonParams<TruckTeamCar> params);

    // 我的车队--修改车辆信息
    String UPDATE_TRUCK = "truckteam.updatetruck";
    @POST("truck-service/truckteam/updatetruck")
    Observable<BaseResponse<Object>> updateTruck(@Body CommonParams<TruckTeamCar> params);

    // 我的车队--删除车辆
    String DELETE_TRUCK = "truckteam.deletetruck";
    @POST("truck-service/truckteam/deletetruck")
    Observable<BaseResponse<Object>> deleteTruck(@Body CommonParams<CarIDParams> params);

}