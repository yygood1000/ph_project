package com.topjet.shipper.user.model.serviceAPI;

import com.topjet.common.base.net.request.CommonParams;
import com.topjet.common.base.net.response.BaseResponse;
import com.topjet.shipper.user.model.bean.ContactsInputParams;
import com.topjet.common.user.modle.response.DeleteLinkmanResponse;
import com.topjet.shipper.user.model.bean.DeteleContactParams;
import com.topjet.common.user.modle.response.IdResponse;
import com.topjet.shipper.user.model.bean.GetUsualContactInfoParams;
import com.topjet.common.user.modle.params.GetUsualContactListParams;
import com.topjet.common.user.modle.params.UsualContactListItem;
import com.topjet.shipper.user.model.bean.GetUserInfoByMobileParams;
import com.topjet.common.user.modle.params.PageNumParams;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 联系人相关
 * Created by tsj004 on 2017/8/24.
 */

public interface ContactCommandAPI {

    //常用联系人列表
    String SELECT_LINKMAN_LIST = "linkman.selectlinkmanlist";
    @POST("order-service/linkman/selectlinkmanlist")
    Observable<BaseResponse<GetUsualContactListParams>> selectLinkmanList(@Body CommonParams<PageNumParams> params);

    //删除常用联系人
    String DELETE_LINKMAN_BY_ID = "linkman.deletelinkmanbyid";
    @POST("order-service/linkman/deletelinkmanbyid")
    Observable<BaseResponse<DeleteLinkmanResponse>> deleteLinkmanByid(@Body CommonParams<DeteleContactParams> params);

    //添加常用联系人
    String INSERT_LINKMAN = "linkman.insertlinkman";
    @POST("order-service/linkman/insertlinkman")
    Observable<BaseResponse<IdResponse>> insertLinkman(@Body CommonParams<ContactsInputParams> params);

    //编辑常用联系人
    String UPDATE_LINKMAN_BY_ID = "linkman.updatelinkmanbyid";
    @POST("order-service/linkman/updatelinkmanbyid")
    Observable<BaseResponse<IdResponse>> updateLinkmanByid(@Body CommonParams<ContactsInputParams> params);

    //获取常用联系人信息
    String SELECT_LINKMAN_INFO_BY_ID = "linkman.selectlinkmaninfobyid";
    @POST("order-service/linkman/selectlinkmaninfobyid")
    Observable<BaseResponse<UsualContactListItem>> selectLinkmanInfoById(@Body CommonParams<GetUsualContactInfoParams> params);

    //通过手机号获取常用联系人信息
    String SELECT_LINKMAN_INFO_BY_MOBILE = "linkman.selectlinkmaninfobymobile";
    @POST("user-service/linkman/selectlinkmaninfobymobile")
    Observable<BaseResponse<UsualContactListItem>> selectLinkmanInfoByMobile(@Body CommonParams<GetUserInfoByMobileParams> params);
}
