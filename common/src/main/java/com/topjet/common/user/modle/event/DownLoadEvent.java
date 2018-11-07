package com.topjet.common.user.modle.event;

import com.topjet.common.base.model.BaseEvent;

public class DownLoadEvent extends BaseEvent {
//	private static final String TAG_V3_DownLoadEvent_V3_OrderEntryInCityGoodsActivity="V3_OrderEntryInCityGoodsActivity";

	public DownLoadEvent(boolean success, String tag) {
		setTag(tag);
		setSuccess(success);
	}

}
