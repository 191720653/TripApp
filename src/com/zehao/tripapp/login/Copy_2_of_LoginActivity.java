package com.zehao.tripapp.login;

import java.util.HashMap;

import org.apache.http.Header;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.data.bean.UserInfo;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.MainActivity;
import com.zehao.tripapp.R;
import com.zehao.tripapp.register.SignupActivity;
import com.zehao.view.CircleImageButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Copy_2_of_LoginActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>> {

	private JsonObject jsonObject = null;
	private EditText et_account, et_password;
	private TextView tv_register;
	private CheckBox cb_rem_password, cb_auto_login;
	private Button btn_login;
	private CircleImageButton btnQuit;
	private String loginNameValue, passwordValue;
	private ProgressDialog progressDialog;

	public static final String USER_INFO_MODEL = "userInfo";
	public static final String USER_IS_REGISTER = "is_register";
	public static final String LOGIN_CHECKBOX_REMPWD = "remember_password";
	public static final String LOGIN_CHECKBOX_AUTO = "auto_login";
	public static final String USER_ACCOUNT = "account";
	public static final String USER_PASSWORD = "password";
	
	private static final int MSG_PROGRESS_DIALOG_SHOW = 0;
	private static final int MSG_PROGRESS_DIALOG_DISMISS = 1;
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR= 3;
	private static final int MSG_AUTH_COMPLETE = 4;
	
	private String platform = null;
	
	public boolean onSignin(String userId) {
		// 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
		// 读取sharepreference中的注册标志，是否注册，否返回false
		readXML(USER_INFO_MODEL, USER_IS_REGISTER);
		// 此处全部给回需要注册
		return true;
	}
	public boolean onSignUp(UserInfo info) {
		// 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
		return true;
	}

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_login);

		// ConnectivityManager
		// cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		// Log.d("check_internet",
		// cwjManager.getActiveNetworkInfo().isAvailable()+"");

		et_account = (EditText) findViewById(R.id.et_account);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_register = (TextView) findViewById(R.id.tv_register);
		cb_rem_password = (CheckBox) findViewById(R.id.cb_rem_password);
		cb_auto_login = (CheckBox) findViewById(R.id.cb_auto_login);
		btn_login = (Button) findViewById(R.id.btn_login);
		btnQuit = (CircleImageButton) findViewById(R.id.user_icon);

		btnQuit.setImageDrawable(getResources()
				.getDrawable(R.drawable.ic_empty));

		if (readXMLBoolean(USER_INFO_MODEL, LOGIN_CHECKBOX_REMPWD)) {
			cb_rem_password.setChecked(true);
			String account = readXML(USER_INFO_MODEL, USER_ACCOUNT);
			String password = readXML(USER_INFO_MODEL, USER_PASSWORD);
			et_account.setText(account);
			et_password.setText(password);

			if (readXMLBoolean(USER_INFO_MODEL, LOGIN_CHECKBOX_AUTO)) {
				cb_auto_login.setChecked(true);
				shortToastHandler("记住密码，自动登录，跳到主界面！");
				// Intent intent = new Intent(LoginActivity.this,
				// MainActivity.class);
				// LoginActivity.this.startActivity(intent);
			}
		}

		tv_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// finish();
				// Intent intent = new Intent(LoginActivity.this,
				// RegisterActivity.class);
				// LoginActivity.this.startActivity(intent);
				shortToastHandler("跳到注册Activity！");
			}
		});

		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				loginNameValue = et_account.getText().toString();
				passwordValue = et_password.getText().toString();

				if (loginNameValue.length() != 11 || passwordValue.length() < 6) {
					Toast.makeText(Copy_2_of_LoginActivity.this, "请填写好用户名以及密码！",
							Toast.LENGTH_LONG).show();
				} else {
					handler.sendEmptyMessage(MSG_PROGRESS_DIALOG_SHOW);
					try {
						jsonObject = new JsonObject();
						jsonObject.addProperty("LOGIN_NAME", loginNameValue);
						jsonObject.addProperty("LOGIN_PASSWORD", passwordValue);
						writeXML(USER_INFO_MODEL, USER_ACCOUNT, loginNameValue);
						writeXML(USER_INFO_MODEL, USER_PASSWORD, passwordValue);
						// finish();
						// Intent intent = new Intent(LoginActivity.this,
						// MainActivity.class);
						// LoginActivity.this.startActivity(intent);
						handler.sendEmptyMessage(MSG_PROGRESS_DIALOG_DISMISS);
						
						//移除授权
						//weibo.removeAccount(true);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Toast.makeText(Copy_2_of_LoginActivity.this, "登录失败，请检查网络连接状态！",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		cb_rem_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (cb_rem_password.isChecked()) {
							System.out.println("记住密码");
							writeXMLBoolean(USER_INFO_MODEL,
									LOGIN_CHECKBOX_REMPWD, true);
						} else {
							System.out.println("不记住密码");
							writeXMLBoolean(USER_INFO_MODEL,
									LOGIN_CHECKBOX_REMPWD, false);
						}
					}
				});
		cb_auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (cb_auto_login.isChecked()) {
					System.out.println("自动登录");
					writeXMLBoolean(USER_INFO_MODEL, LOGIN_CHECKBOX_AUTO, true);
				} else {
					System.out.println("不自动登录");
					writeXMLBoolean(USER_INFO_MODEL, LOGIN_CHECKBOX_AUTO, false);
				}
			}
		});
		btnQuit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ShareSDK.initSDK(Copy_2_of_LoginActivity.this);
		platform = SinaWeibo.NAME;
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//		if(weibo.isValid()){
//			weibo.removeAccount();
//		}
		weibo.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(MSG_AUTH_ERROR);
			}
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = MSG_AUTH_COMPLETE;
				UserInfo userInfo = new UserInfo();
				userInfo.setUserId(arg0.getDb().getUserId());
				userInfo.setUserGender(arg0.getDb().getUserGender());
				userInfo.setUserIcon(arg0.getDb().getUserIcon());
				userInfo.setUserName(arg0.getDb().getUserName());
				msg.obj = new Gson().toJson(userInfo, UserInfo.class);
				handler.sendMessage(msg);
				System.out.println(arg0.getDb().exportData());
			}
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(MSG_AUTH_CANCEL);
			}
		});
		//关闭SSO授权，即关闭客户端授权，通过网页授权
		weibo.SSOSetting(true);
		weibo.showUser(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void loadData() {
		// TODO Auto-generated method stub
		setDataCallback(this);
	}

	@Override
	public void onNewData(Object data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(String msg, int code) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void handler(Message msg) { // 我们可以处理数据消息了
		switch(msg.what) {
		case MSG_PROGRESS_DIALOG_SHOW: {
			progressDialog = ProgressDialog.show(Copy_2_of_LoginActivity.this,
					"登录", "正在联网登录,请稍候......");
		}break;
		case MSG_PROGRESS_DIALOG_DISMISS: {
			progressDialog.dismiss();
		}break;
		case MSG_AUTH_CANCEL: {
			//取消授权
			shortToastHandler("取消授权");
		} break;
		case MSG_AUTH_ERROR: {
			//授权失败
			shortToastHandler("授权失败");
		} break;
		case MSG_AUTH_COMPLETE: {
			//授权成功
			shortToastHandler("授权成功");
			String temp = (String) msg.obj;
			JsonObject info = new JsonParser().parse(temp).getAsJsonObject();
			//校验用户Id，是否已经注册
			RequestParams params = new RequestParams();
			params.put("userId", info.get("userId"));
			handler.sendEmptyMessage(MSG_PROGRESS_DIALOG_SHOW);
			HttpCLient.postJson(this, "/User_checkAction.action", params, new JsonHttpResponseHandler(){
				@Override
				public void onFailure(int statusCode, Header[] headers,
						String responseString, Throwable throwable) {
					// TODO Auto-generated method stub
					longToastHandler("登录失败，请检查网络连接状态！");
				}
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						org.json.JSONObject response) {
					// TODO Auto-generated method stub
					JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
					if(json.get("code").equals("success")){
						// 登录成功
						goActivity(MainActivity.class);
					}
					else{
						// 跳转注册Activity，引导用户注册
						Bundle bundle = new Bundle();
						bundle.putString("platform", platform);
						goActivity(SignupActivity.class, bundle);
					}
				}
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(MSG_PROGRESS_DIALOG_DISMISS);
				}});
		} break;
		default : break;
		}
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

}
