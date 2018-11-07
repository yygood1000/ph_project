package com.topjet.common.base.controller;

import com.topjet.common.base.busManger.BusManager;
import com.topjet.common.base.model.BaseEvent;

public class BaseController {

	public void postEvent(BaseEvent baseEvent) {
		BusManager.getBus().post(baseEvent);
	}
}