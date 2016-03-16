package com.zehao.tripapp.welcome;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Users;
import com.zehao.tripapp.MainActivity;
import com.zehao.tripapp.R;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.util.Tool;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;

/**
 * 功能：使用ViewPager实现初次进入应用时的引导页
 * 
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法 (2)是，则进入引导activity；否，则进入MainActivity
 * (3)5s后执行(2)操作
 * 
 * @author zehao
 * 
 */
public class SplashActivity extends BaseActivity {

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_welcome_splash);

		boolean mFirst = isFirstEnter(SplashActivity.this, SplashActivity.this
				.getClass().getName());
		if (mFirst)
			handler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 1000);
		else
			handler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 1000);
	}

	// ****************************************************************
	// 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
	// ****************************************************************
	private static final String SHAREDPREFERENCES_NAME = "my_pref";
	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

	private boolean isFirstEnter(Context context, String className) {
		if (context == null || className == null
				|| "".equalsIgnoreCase(className))
			return false;
		String mResultStr = readXML(SHAREDPREFERENCES_NAME, KEY_GUIDE_ACTIVITY);// 取得所有类名如
																				// com.my.MainActivity
		if (mResultStr != null && mResultStr.equalsIgnoreCase("false"))
			return false;
		else
			return true;
	}

	// *************************************************
	// Handler:跳转至不同页面
	// *************************************************
	private final static int SWITCH_MAINACTIVITY = 1000;
	private final static int SWITCH_GUIDACTIVITY = 1001;

	@Override
	protected void handler(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case SWITCH_MAINACTIVITY:
			// 在启动页读取本地信息，若有token则跳过登录到主界面，否则先登录（游客用不了，必须注册）
			String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
			if(!CONSTANT.NULL_STRING.equals(Tool.NVL(temp))){
				JsonObject json = new JsonParser().parse(temp).getAsJsonObject();
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
				Users user = gson.fromJson(json, Users.class);
				if(CONSTANT.LOGIN_SIGN_OFF.equals(user.getLoginSign())){
					goActivityAndFinish(LoginActivity.class);
				} else {
					goActivityAndFinish(MainActivity.class);
				}
			} else {
				goActivityAndFinish(MainActivity.class);
			}
			break;
		case SWITCH_GUIDACTIVITY:
			goActivityAndFinish(GuideActivity.class);
			break;
		}
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}