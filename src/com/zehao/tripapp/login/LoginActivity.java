package com.zehao.tripapp.login;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.data.bean.Users;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.LastActivity;
import com.zehao.tripapp.MainActivity;
import com.zehao.tripapp.R;
import com.zehao.tripapp.register.SigninActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

/**
 * 登录： 1、用户点击我的按钮跳转过来，读取本地信息，若有则显示账号密码，用户点击登陆，调用登陆方法
 * 2、用户点击注册按钮，注册回来后，直接在forresult里调用登录方法
 * 3、用户点击第三方登录按钮，授权后发送Id检查用户是否注册，若是则写入本地后直接跳转到主界面
 * ，否则跳转到注册界面，注册回来后，直接在forresult里调用登录方法
 * 
 * @author zehao
 * 
 */
public class LoginActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener {

	private String account, password, platform;
	private EditText accountEditText, passwordEditText;
	private ProgressDialog progressDialog;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		baseSetContentView(savedInstanceState, R.layout.activity_login2);

		accountEditText = (EditText) findViewById(R.id.et_account);
		passwordEditText = (EditText) findViewById(R.id.et_password);

		// 读取本地用户信息并显示，用于用户注销后跳转此界面
		account = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_ACCOUNT);
		password = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_PASSWORD);
		System.out.println("----->本地数据：account=" + account + " password="
				+ password);

		accountEditText.setText(account);
		passwordEditText.setText(password);

		// 在启动页读取本地信息，若有token则跳过登录到主界面，否则先登录（游客用不了，必须注册）
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login: {
			// goActivityAndFinish(MainActivity.class);
			// 点击登录，跳出进度对话框
			account = accountEditText.getText().toString();
			password = passwordEditText.getText().toString();
			login();
			break;
		}
		case R.id.btn_register: {
			// 跳往注册界面
			goActivityForResult(SigninActivity.class, null, 0);
			break;
		}
		case R.id.qq_button: {
			// QQ第三方登录
			qq_login();
			break;
		}
		case R.id.sina_button: {
			// Sina第三方登录
			sina_login();
			break;
		}
		case R.id.wechat_button: {
			// Wechat第三方登录
			wechat_login();
			break;
		}
		default:
			break;
		}
	}

	public void qq_login() {
		ShareSDK.initSDK(this);
		final Platform qq = ShareSDK.getPlatform(QZone.NAME);
		/*
		 * if(qq.isValid()){ qq.removeAccount(); }
		 */
		qq.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				shortToastHandler("onError");
				qq.removeAccount();
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				platform = arg0.getName();
				checkId(arg0.getDb().getUserId());
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				shortToastHandler("onCancel");
				qq.removeAccount();
			}
		});
		// 关闭SSO授权，即关闭客户端授权，通过网页授权
		// qq.SSOSetting(true);
		qq.showUser(null);
	}

	public void sina_login() {
		ShareSDK.initSDK(this);
		final Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		/*
		 * if(weibo.isValid()){ weibo.removeAccount(); }
		 */
		weibo.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				shortToastHandler("onError");
				weibo.removeAccount();
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				platform = arg0.getName();
				checkId(arg0.getDb().getUserId());
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				shortToastHandler("onCancel");
				weibo.removeAccount();
			}
		});
		// 关闭SSO授权，即关闭客户端授权，通过网页授权
		weibo.SSOSetting(true);
		weibo.showUser(null);
	}

	public void wechat_login() {
		ShareSDK.initSDK(this);
		final Platform wechat = ShareSDK.getPlatform("Wechat");
		// if(weibo.isValid()){
		// weibo.removeAccount();
		// }
		wechat.setPlatformActionListener(new PlatformActionListener() {
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				shortToastHandler("onError");
				wechat.removeAccount();
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				platform = arg0.getName();
				checkId(arg0.getDb().getUserId());
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				shortToastHandler("onCancel");
				wechat.removeAccount();
			}
		});
		// 关闭SSO授权，即关闭客户端授权，通过网页授权
		// wechat.SSOSetting(true);
		wechat.showUser(null);
	}

	public void login() {
		if (!Pattern.matches("^[0-9a-zA-Z]{8,12}", account)
				|| !Pattern.matches("^[0-9a-zA-Z]{8,12}", password)) {
			shortToastHandler("请填写8到12位的纯数字、字母或数字字母组合的账号、密码！");
		} else {
			// 联网登录
			JsonObject json = new JsonObject();
			json.addProperty(CONSTANT.ACCOUNT, account);
			json.addProperty(CONSTANT.PASSWORD, password);
			RequestParams params = new RequestParams();
			params.put(CONSTANT.DATA, json.toString());
			String url = "/AppLogin_loginAction.action";
			System.out.println("App发送数据：" + json.toString());
			handler.sendEmptyMessage(CONSTANT.ACTION_SHOW_DIALOG);
			HttpCLient.post(url, params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub
					JsonObject json = (JsonObject) new JsonParser()
							.parse(new String(arg2));
					System.out.println("服务器返回数据：" + json);
					String errorCode = json.get(CONSTANT.ERRCODE).getAsString();
					if (CONSTANT.CODE_168.equals(errorCode)) {
						JsonObject info = json.get(CONSTANT.USER_INFO)
								.getAsJsonObject();
						Gson gson = new GsonBuilder().create();
						Users user = gson.fromJson(info, Users.class);
						System.out.println("返回结果转Uers：" + user.toString());
						writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS,
								info.toString());
						writeXML(CONSTANT.INFO_DATA,
								CONSTANT.INFO_DATA_ACCOUNT, user.getAccount());
						writeXML(CONSTANT.INFO_DATA,
								CONSTANT.INFO_DATA_PASSWORD, user.getPassword());
						writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_TOKEN,
								user.getToken());
						// 登录成功，跳到主界面
						goActivityAndFinish(MainActivity.class);
						// finish();
					} else if (CONSTANT.CODE_173.equals(errorCode)) {
						shortToastHandler(CONSTANT.CODE_173_TEXT);
					} else if (CONSTANT.CODE_177.equals(errorCode)) {
						shortToastHandler(CONSTANT.CODE_177_TEXT);
					} else {
						shortToastHandler(CONSTANT.OTHER_ERROR);
					}
					handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					shortToastHandler(CONSTANT.OTHER_ERROR);
					handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
				}
			});
		}
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
		case CONSTANT.ACTION_SHOW_DIALOG:
			progressDialog = ProgressDialog.show(this, "登录", "正在联网,请稍候......");
			break;
		case CONSTANT.ACTION_DISMISS_DIALOG:
			progressDialog.dismiss();
			break;
		case 0:
			break;
		case -1:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {// 注册回来直接登录
			account = data.getStringExtra(CONSTANT.INFO_DATA_ACCOUNT);
			password = data.getStringExtra(CONSTANT.INFO_DATA_PASSWORD);
			login();
		}
	}

	/**
	 * 第三方登录Id检查
	 * 
	 * @param typeId
	 */
	public void checkId(String typeId) {
		String url = "/AppSignIn_checkIdAction.action";
		JsonObject json = new JsonObject();
		json.addProperty(CONSTANT.USER_TYPE_ID, typeId);
		RequestParams params = new RequestParams();
		params.put(CONSTANT.DATA, json.toString());
		handler.sendEmptyMessage(CONSTANT.ACTION_SHOW_DIALOG);
		System.out.println("App发送数据：" + json.toString());
		HttpCLient.post(url, params,
				new AsyncHttpResponseHandler(this.getMainLooper()) {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
						JsonObject json = (JsonObject) new JsonParser()
								.parse(new String(arg2));
						System.out.println("服务器返回数据：" + json);
						String errorCode = json.get(CONSTANT.ERRCODE)
								.getAsString();
						if (CONSTANT.CODE_168.equals(errorCode)) {
							String commonCode = json.get(CONSTANT.COMMON_SIGN)
									.getAsString();
							// 第三方用户是否注册过
							if (CONSTANT.COMMON_SIGN_HAS.equals(commonCode)) {
								JsonObject info = json.get(CONSTANT.USER_INFO)
										.getAsJsonObject();
								info.addProperty("createDate",
										info.get("createDates").getAsString());
								Gson gson = new GsonBuilder().setDateFormat(
										"yyyy-MM-dd HH:mm:ss").create();
								Users user = gson.fromJson(info, Users.class);
								System.out.println("返回结果转Uers："
										+ user.toString());
								writeXML(CONSTANT.INFO_DATA,
										CONSTANT.INFO_DATA_USERS,
										info.toString());
								writeXML(CONSTANT.INFO_DATA,
										CONSTANT.INFO_DATA_ACCOUNT,
										user.getAccount());
								writeXML(CONSTANT.INFO_DATA,
										CONSTANT.INFO_DATA_PASSWORD,
										user.getPassword());
								writeXML(CONSTANT.INFO_DATA,
										CONSTANT.INFO_DATA_TOKEN,
										user.getToken());
								// 登录成功，跳到主界面
								shortToastHandler("登录成功！");
								goActivityAndFinish(MainActivity.class);
								// finish();
							} else {
								// 跳往注册界面
								shortToastHandler("您还没注册，请先注册！");
								Bundle bundle = new Bundle();
								bundle.putString("platform", platform);
								goActivityForResult(SigninActivity.class,
										bundle, 0);
							}
						} else {
							shortToastHandler(CONSTANT.OTHER_ERROR);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						shortToastHandler(CONSTANT.OTHER_ERROR);
						handler.sendEmptyMessage(CONSTANT.ACTION_DISMISS_DIALOG);
					}
				});
	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(LoginActivity.this, LastActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);// 清理此Activity之前的同个栈的（一个？）Activity
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 开始一个新的栈，下一个Activity执行finish()栈底无Activity即可退出
			this.startActivity(intent);
			finish();
		}
		return true;
	}

}
