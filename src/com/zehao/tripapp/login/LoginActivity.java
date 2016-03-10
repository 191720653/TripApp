package com.zehao.tripapp.login;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import com.zehao.base.BaseActivity;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.tripapp.MainActivity;
import com.zehao.tripapp.R;
import com.zehao.tripapp.register.SigninActivity;
import com.zehao.util.Tool;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

/**
 * 登录
 * @author zehao
 *
 */
public class LoginActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener {

	private String account,password;
	private EditText accountEditText,passwordEditText;
	private ProgressDialog progressDialog;
//	private Button signInButton,signButton;
//	private CircleImageButton qq,sina,wechat;

	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		baseSetContentView(savedInstanceState, R.layout.activity_login2);

		accountEditText = (EditText) findViewById(R.id.et_account);
		passwordEditText = (EditText) findViewById(R.id.et_password);
		
		// 读取本地用户信息并显示，用于用户注销后跳转此界面
		// 在启动页读取本地信息，若有token则跳过登录到主界面
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login: {
			// 点击登录，跳出进度对话框
			login();
			break;
		}
		case R.id.btn_register: {
			// 跳往注册界面
			goActivity(SigninActivity.class);
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
	
	public void qq_login(){
		ShareSDK.initSDK(this);
		final Platform qq = ShareSDK.getPlatform(QZone.NAME);
		if(qq.isValid()){
			qq.removeAccount();
		}
		qq.setPlatformActionListener(new PlatformActionListener() {
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
				String name = arg0.getDb().getUserName();
				String accessToken = arg0.getDb().getToken(); // 获取授权token
				String openId = arg0.getDb().getUserId(); // 获取用户在此平台的ID
				String nickname = arg0.getDb().get("nickname"); // 获取用户昵称
				Log.e("login......", accessToken + " -- " + openId +" -- " + nickname + " -- " + name +" -- "+ arg0.getDb().getUserIcon());
//				Log.e("login......", arg0.getDb().exportData() + " -------- " + arg2.toString());
				
				// 获取权限之后，判断用户是否已经注册
				// 跳转注册Activity，引导用户注册
				Bundle bundle = new Bundle();
				bundle.putString("platform", arg0.getName());
				goActivity(SigninActivity.class, bundle);
				// 若已经注册直接登录，若没注册则注册后登录
				login();
			}
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				shortToastHandler("onCancel");
			}
		});
		// 关闭SSO授权，即关闭客户端授权，通过网页授权
		// qq.SSOSetting(true);
		qq.showUser(null);
	}
	
	public void sina_login(){
		ShareSDK.initSDK(this);
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		if(weibo.isValid()){
			weibo.removeAccount();
		}
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
				Log.e("login......", arg0.getDb().exportData() + " -------- " + arg2.toString());
				// 获取权限之后，判断用户是否已经注册
				// 跳转注册Activity，引导用户注册
				Bundle bundle = new Bundle();
				bundle.putString("platform", arg0.getName());
				goActivity(SigninActivity.class, bundle);
				// 若已经注册直接登录，若没注册则注册后登录
				// login();
			}
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				shortToastHandler("onCancel");
			}
		});
		// 关闭SSO授权，即关闭客户端授权，通过网页授权
		weibo.SSOSetting(true);
		weibo.showUser(null);
	}
	
	public void wechat_login(){
		ShareSDK.initSDK(this);
		Platform wechat = ShareSDK.getPlatform("Wechat");
//		if(weibo.isValid()){
//			weibo.removeAccount();
//		}
		wechat.setPlatformActionListener(new PlatformActionListener() {
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
				Log.e("login......", arg0.getDb().exportData() + " -------- " + arg2.toString());
			}
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				shortToastHandler("onCancel");
			}
		});
		//关闭SSO授权，即关闭客户端授权，通过网页授权
		wechat.SSOSetting(true);
		wechat.showUser(null);
	}
	
	public void login(){
		account = accountEditText.getText().toString();
		password = passwordEditText.getText().toString();
	
		if (Tool.NVL(account).length() != 11 || Tool.NVL(password).length() < 6) {
			shortToastHandler("请填写好用户名以及密码！");
		} else {
			progressDialog = ProgressDialog.show(this, "登录", "正在联网登录,请稍候......");
			// 联网登录
			progressDialog.dismiss();
			// 登录成功，吧用户信息写到本地
			// writeXML(name, key, value);
			goActivity(MainActivity.class);
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

	}

	@Override
	public void setBaseNoTitle() {
		// TODO Auto-generated method stub
		// 不用系统自带ActionBar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

}
