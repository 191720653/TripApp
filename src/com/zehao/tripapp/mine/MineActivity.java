package com.zehao.tripapp.mine;

import java.util.List;

import org.apache.http.Header;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zehao.base.BaseActivity;
import com.zehao.constant.CONSTANT;
import com.zehao.data.bean.Domine;
import com.zehao.data.bean.Employee;
import com.zehao.data.bean.IDataCallback;
import com.zehao.data.bean.MData;
import com.zehao.data.bean.Users;
import com.zehao.http.HttpCLient;
import com.zehao.tripapp.R;
import com.zehao.tripapp.info.InfoActivity;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.picture.WaterfallActivity;
import com.zehao.util.Tool;
import com.zehao.view.RoundImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

/**
 * < TODO：中山市南区App 我的信息以及设置 >
 * 
 * @ClassName: MineActivity
 * @author pc-hao
 * @date 2016年2月24日 下午14:23:14
 * @version V 1.0
 */
public class MineActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener {

	private TextView textViewName, textViewSign;
	private RoundImageView imageView;
	
	private Users user;
	
	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_mine);
		
		textViewSign = (TextView) findViewById(R.id.mine_info_sign);
		textViewName = (TextView) findViewById(R.id.mine_info_name);
		
		imageView = (RoundImageView) findViewById(R.id.mine_info_icon);
        
		String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
		JsonObject json = new JsonParser().parse(temp).getAsJsonObject();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		user = gson.fromJson(json, Users.class);
		
		textViewName.setText(user.getNickName());
		textViewSign.setText(user.getInfo());
		
		ImageLoader.getInstance().displayImage(CONSTANT.BASE_ROOT_URL + user.getIcon().replaceFirst(".", ""), imageView);
        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mine_info: {
			shortToastHandler("进入修改我的信息");
			goActivity(InfoActivity.class);
			break;
		}
		case R.id.mine_info_picture: {
			shortToastHandler("进入查看我的相册");
			goActivity(WaterfallActivity.class);
			break;
		}
		case R.id.action_bar_btn_back:{
			shortToastHandler("返回主界面");
			finish();
			break;
			}
		case R.id.action_bar_btn_share:{
			showShare();
			break;
			}
		case R.id.mine_advice:{
			shortToastHandler("进入提交意见反馈");
			break;
			}
		case R.id.mine_update:{
			shortToastHandler("点击开始检查更新");
			break;
			}
		case R.id.mine_logout_button:{
			shortToastHandler("点击退出当前登录");
			logout();
			break;
			}
		default:
			break;
		}
	}
	
	public void logout(){
		String url = "/AppLogin_logoutAction.action";
		JsonObject json = new JsonObject();
		json.addProperty(CONSTANT.TOKEN, user.getToken());
		
		RequestParams params = new RequestParams();
		params.put(CONSTANT.DATA, json.toString());
		
		System.out.println("App发送数据：" + json.toString());
		HttpCLient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				JsonObject json = (JsonObject) new JsonParser()
				.parse(new String(arg2));
				System.out.println("服务器返回数据：" + json);
				String errorCode = json.get(CONSTANT.ERRCODE).getAsString();
				if (CONSTANT.CODE_168.equals(errorCode)) {
					String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
					if(!CONSTANT.NULL_STRING.equals(Tool.NVL(temp))){
						JsonObject userJson = new JsonParser().parse(temp).getAsJsonObject();
						userJson.addProperty("loginSign", CONSTANT.LOGIN_SIGN_OFF);
						writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_TOKEN, CONSTANT.NULL_STRING);
						writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS, userJson.toString());
						writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_PHOTO_URL, "");
						shortToastHandler("退出登录成功！");
						goActivityAndFinish(LoginActivity.class);
					} else {
						goActivityAndFinish(LoginActivity.class);
					}
				} else {
					shortToastHandler(CONSTANT.OTHER_ERROR);
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				shortToastHandler(CONSTANT.OTHER_ERROR);
			}
		});
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

	private void showShare() {
		// String icon = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + "/download/" + "userIcon.jpg";
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		// oks.setTitle(fragment.getViews().getViewName());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我最近在使用中山市南区导览App哦，超赞！想要去中山市南区观光的朋友赶紧下载吧！");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath(icon);// 确保SDcard下面存在此张图片/sdcard/test.jpg //fragment.getUrls().get(0)
		// url仅在微信（包括好友和朋友圈）中使用
		// oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}
}
