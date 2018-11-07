package com.topjet.common.resource.bean;

import java.io.Serializable;

/**
 * 关于城市选择时的选择项类。
 *
 */
public class CityItem implements Serializable {
	private String cityName;
	private String citycode;		// 之前的百度的城市id
	private String adcode;			// 560存的城市id，之前的cityid
	private String latitude;
	private String cityFullName;
	private String parentId;
	private String longitude;

	public CityItem() {

	}

	public CityItem(String cityName, String citycode, String adcode, String latitude, String longitude) {
		this.cityName = cityName;
		this.citycode = citycode;
		this.adcode = adcode;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public CityItem(String cityName, String citycode, String adcode, String latitude, String cityFullName, String parentId, String longitude) {
		this.cityName = cityName;
		this.citycode = citycode;
		this.adcode = adcode;
		this.latitude = latitude;
		this.cityFullName = cityFullName;
		this.parentId = parentId;
		this.longitude = longitude;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getAdcode() {
		return adcode;
	}

	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getCityFullName() {
		return cityFullName;
	}

	public void setCityFullName(String cityFullName) {
		this.cityFullName = cityFullName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "CityItem{" +
				"cityName='" + cityName + '\'' +
				", citycode='" + citycode + '\'' +
				", adcode='" + adcode + '\'' +
				", latitude='" + latitude + '\'' +
				", cityFullName='" + cityFullName + '\'' +
				", parentId='" + parentId + '\'' +
				", longitude='" + longitude + '\'' +
				'}';
	}
}
