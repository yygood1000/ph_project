package com.topjet.common.common.modle.event;

import com.topjet.common.base.model.BaseEvent;
import com.topjet.common.resource.bean.AreaInfo;

/**
 * 城市选择器传回数据
 */
public class AreaSelectedData extends BaseEvent {
	private AreaInfo areaInfo;

	public AreaSelectedData(String tag, AreaInfo areaInfo) {
		setTag(tag);
		this.areaInfo = areaInfo;
	}
	
	
	public AreaInfo getAreaInfo() {
		return areaInfo;
	}
	public void setAreaInfo(AreaInfo areaInfo) {
		this.areaInfo = areaInfo;
	}

}
