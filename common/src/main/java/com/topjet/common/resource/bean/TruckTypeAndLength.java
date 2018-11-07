package com.topjet.common.resource.bean;

public class TruckTypeAndLength {
	private String iconKey;
	private String id;
	private String innerCity;
	private String loadWeight;
	private String name;
	private String truckHeight;
	private String truckLength;
	private String truckTypeName;
	private String truckType;
	private String truckWidth;

	public TruckTypeAndLength() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TruckTypeAndLength(String iconKey, String id, String innerCity, String loadWeight, String name,
                              String truckHeight, String truckLength, String truckType, String truckWidth, String truckTypeName) {
		super();
		this.iconKey = iconKey;
		this.id = id;
		this.innerCity = innerCity;
		this.loadWeight = loadWeight;
		this.name = name;
		this.truckHeight = truckHeight;
		this.truckLength = truckLength;
		this.truckType = truckType;
		this.truckWidth = truckWidth;
		this.truckTypeName = truckTypeName;
	}

	public String getTruckTypeName() {
		return truckTypeName;
	}

	public void setTruckTypeName(String truckTypeName) {
		this.truckTypeName = truckTypeName;
	}

	public String getIconKey() {
		return iconKey;
	}

	public void setIconKey(String iconKey) {
		this.iconKey = iconKey;
	}

	public String getInnerCity() {
		return innerCity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInnerCity(String innerCity) {
		this.innerCity = innerCity;
	}

	public String getLoadWeight() {
		return loadWeight;
	}

	public void setLoadWeight(String loadWeight) {
		this.loadWeight = loadWeight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTruckHeight() {
		return truckHeight;
	}

	public void setTruckHeight(String truckHeight) {
		this.truckHeight = truckHeight;
	}

	public String getTruckLength() {
		return truckLength;
	}

	public void setTruckLength(String truckLength) {
		this.truckLength = truckLength;
	}

	public String getTruckType() {
		return truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}

	public String getTruckWidth() {
		return truckWidth;
	}

	public void setTruckWidth(String truckWidth) {
		this.truckWidth = truckWidth;
	}



	@Override
	public String toString() {
		return "TruckTypeAndLength [iconKey=" + iconKey + ", id=" + id + ", innerCity=" + innerCity + ", loadWeight="
				+ loadWeight + ", name=" + name + ", truckHeight=" + truckHeight + ", truckLength=" + truckLength
				+ ", truckTypeName=" + truckTypeName + ", truckType=" + truckType + ", truckWidth=" + truckWidth + "]";
	}

}
