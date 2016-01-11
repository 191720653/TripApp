package com.zehao.data.bean;

import java.io.Serializable;

public class Domine implements Serializable {

	private static final long serialVersionUID = 1L;

	public Domine() {
	}

	public int id;
	public String desc;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}