package com.zehao.data.bean;

import java.io.Serializable;

public class MData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public String id;
    public String type;
    public T dataList;//多种类型数据，一般是List集合，比如获取所有员工列表
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public T getDataList() {
		return dataList;
	}
	public void setDataList(T dataList) {
		this.dataList = dataList;
	}

}