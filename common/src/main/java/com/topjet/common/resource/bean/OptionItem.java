package com.topjet.common.resource.bean;

public class OptionItem {
	private String code;
	private String name;

	public OptionItem(String code, String textDeception) {
		this.code = code;
		this.name = textDeception;
	}

	public OptionItem(String textDeception) {
		this.name = textDeception;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "OptionItem{" +
				"code='" + code + '\'' +
				", name='" + name + '\'' +
				'}';
	}

}
