package com.zehao.data.bean;

public interface IDataCallback<T> { // 我们一样传入通用类型
	
	public void onNewData(Object data);

	public void onError(String msg, int code);
	
}