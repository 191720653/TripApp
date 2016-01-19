package com.zehao.tripapp.login;

import java.util.HashMap;

import com.zehao.data.bean.UserInfo;
/** 
 * 设置授权回调，用于判断是否进入注册
 */
public interface OnLoginListener {
	/** 授权完成调用此接口，返回授权数据，如果需要注册，则返回true */
	public boolean onSignin(String platform, HashMap<String, Object> res);
	
	/** 填写完注册信息后调用此接口，返回true表示数据合法，注册页面可以关闭 */
	public boolean onSignUp(UserInfo info) ;
	
}
