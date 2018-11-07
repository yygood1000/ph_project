package com.topjet.shipper.familiar_car.model.serverAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.common.contact.model.InviteTruckResponse;
import com.topjet.shipper.familiar_car.model.params.AroundTruckMapParams;
import com.topjet.shipper.familiar_car.model.params.FindTruckParams;
import com.topjet.common.common.modle.params.TruckParams;
import com.topjet.shipper.familiar_car.model.respons.AroundTruckMapResponse;
import com.topjet.shipper.familiar_car.model.respons.FindTruckListResponse;
import com.topjet.shipper.familiar_car.model.respons.TruckInfoResponse;
import com.topjet.shipper.familiar_car.model.respons.TruckListResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * creator: zhulunjun
 * time:    2017/10/12
 * describe: 车辆
 */

public interface TruckCommandAPI {

    //我的熟车--熟车列表
    String TRUCK_LIST = "ownertruck.trucklist";
    @POST("truck-service/ownertruck/trucklist")
    Observable<BaseResponse<TruckListResponse>> truckList(@Body CommonParams<TruckParams> params);

    //我的熟车--添加/删除熟车
    String ADD_OR_DELETE_TRUCK = "ownertruck.addordeletetruck";
    @POST("truck-service/ownertruck/addordeletetruck")
    Observable<BaseResponse<Object>> addOrDeleteTruck(@Body CommonParams<TruckParams> params);


    //我的熟车--邀请熟车
    String INVITE_TRUCK = "ownertruck.invitetruck";
    @POST("truck-service/ownertruck/invitetruck")
    Observable<BaseResponse<InviteTruckResponse>> inviteTruck(@Body CommonParams<TruckParams> params);

    //货主-车辆详情
    String TRUCK_INFO = "ownertruck.ownertruckinfo";
    @POST("truck-service/ownertruck/ownertruckinfo")
    Observable<BaseResponse<TruckInfoResponse>> truckInfo(@Body CommonParams<TruckParams> params);

    //我要用车-车源列表
    String FIND_TRUCK = "findtruck.trucklist";
    @POST("truck-service/findtruck/trucklist")
    Observable<BaseResponse<FindTruckListResponse>> getFindTruckList(@Body CommonParams<FindTruckParams> params);

    //附近车源- 地图
    String AROUND_TRUCK_MAP = "findtruck.getneartrucktomap";
    @POST("truck-service/findtruck/getneartrucktomap")
    Observable<BaseResponse<AroundTruckMapResponse>> getAroundTruckMap(@Body CommonParams<AroundTruckMapParams> params);

    //附近车源- 地图列表
    String AROUND_TRUCK_MAP_LSIT = "findtruck.getneartruckmaplist";
    @POST("truck-service/findtruck/getneartruckmaplist")
    Observable<BaseResponse<AroundTruckMapResponse>> getAroundTruckMapList(@Body CommonParams<AroundTruckMapParams> params);

}
