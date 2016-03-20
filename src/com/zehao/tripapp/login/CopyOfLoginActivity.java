package com.zehao.tripapp.login;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.data.bean.UserInfo;
import com.zehao.tripapp.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CopyOfLoginActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnLoginListener {

	private JSONObject jsonObject = null;
	private EditText et_account, et_password;
	private TextView tv_register;
	private CheckBox cb_rem_password, cb_auto_login;
	private Button btn_login;
	private ImageView btnQuit;
	private String loginNameValue, passwordValue;
	private ProgressDialog progressDialog;

	public static final String USER_INFO_MODEL = "userInfo";
	public static final String LOGIN_CHECKBOX_REMPWD = "remember_password";
	public static final String LOGIN_CHECKBOX_AUTO = "auto_login";
	public static final String USER_ACCOUNT = "account";
	public static final String USER_PASSWORD = "password";

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
		btnQuit = (ImageView) findViewById(R.id.user_icon);

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
					Toast.makeText(CopyOfLoginActivity.this, "请填写好用户名以及密码！",
							Toast.LENGTH_LONG).show();
				} else {
					progressDialog = ProgressDialog.show(CopyOfLoginActivity.this,
							"登录", "正在联网登录,请稍候......");
					try {
						jsonObject = new JSONObject();
						jsonObject.put("LOGIN_NAME", loginNameValue);
						jsonObject.put("LOGIN_PASSWORD", passwordValue);
						writeXML(USER_INFO_MODEL, USER_ACCOUNT, loginNameValue);
						writeXML(USER_INFO_MODEL, USER_PASSWORD, passwordValue);
						// finish();
						// Intent intent = new Intent(LoginActivity.this,
						// MainActivity.class);
						// LoginActivity.this.startActivity(intent);
						handler.sendEmptyMessage(1);
						
						//移除授权
						//weibo.removeAccount(true);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Toast.makeText(CopyOfLoginActivity.this, "登录失败，请检查网络连接状态！",
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
		
		ShareSDK.initSDK(CopyOfLoginActivity.this);
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//		if(weibo.isValid()){
//			weibo.removeAccount();
//		}
		weibo.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				shortToastHandler("onError");
			}
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				System.out.println("asdffghghj");
//				shortToastHandler("onComplete");
//				String accessToken = weibo.getDb().getToken(); // 获取授权token
//				String openId = weibo.getDb().getUserId(); // 获取用户在此平台的ID
//				String nickname = arg0.getDb().get("nickname"); // 获取用户昵称
				Log.e("login......", arg0.getDb().exportData() + " " + arg2.toString());
			}
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				shortToastHandler("onCancel");
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
		setDataCallback(this);// 设置回调函数
		// 我们还可以把这个Callback传给其它获取数据的类，比如HTTP获取数据
		// 比如 HttClient.get(url,this);
	}

	@Override
	public void onNewData(Object data) {
		// TODO Auto-generated method stub
		// update UI 更新UI数据
		@SuppressWarnings({ "unchecked", "unused" })
		final List<Employee> list = (List<Employee>) ((MData<? extends Domine>) data).dataList;
		handler.post(new Runnable() {
			public void run() {
				// 更新UI
			}
		});
		// 或者
		handler.sendEmptyMessage(0);// 通知Handler
	}

	@Override
	public void onError(String msg, int code) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {
			public void run() {
				// 通知错误消息
			}
		});
		// 或者
		handler.sendEmptyMessage(0);// 通知Handler
	}

	@Override
	protected void handler(Message msg) { // 我们可以处理数据消息了
		switch (msg.what) {
		case 0:
			break;
		case 1:
			progressDialog.dismiss();
			break;
		case -1:
			break;
		default:
			break;
		}
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public boolean onSignin(String platform, HashMap<String, Object> res) {
		// TODO Auto-generated method stub
		// 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
		// 此处全部给回需要注册
		return true;
	}

	@Override
	public boolean onSignUp(UserInfo info) {
		// TODO Auto-generated method stub
		// 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
		return true;
	}

}
