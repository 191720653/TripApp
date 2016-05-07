package com.zehao.tripapp.all;

import java.util.List;

import org.apache.http.Header;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.achep.header2actionbar.FadingActionBarHelper;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
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
import com.zehao.tripapp.R;
import com.zehao.tripapp.login.LoginActivity;
import com.zehao.tripapp.mine.MineActivity;
import com.zehao.util.Tool;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * < TODO：中山市南区App其它资料的详细介绍 >
 * 
 * @ClassName: AllDetailActivity
 * @author pc-hao
 * @date 2016年2月23日 下午09:23:14
 * @version V 1.0
 */
public class AllDetailActivity extends BaseActivity implements
		IDataCallback<MData<? extends Domine>>, OnClickListener, OnSliderClickListener {
	
	private FadingActionBarHelper mFadingActionBarHelper;
	private ListViewDetailFragment fragment;

	public FadingActionBarHelper getFadingActionBarHelper() {
		return mFadingActionBarHelper;
	}
	
	@Override
	protected void initContentView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSetContentView(savedInstanceState, R.layout.activity_view_detail);
		
		setActionBarLayout(R.layout.action_bar_like);
		mFadingActionBarHelper = new FadingActionBarHelper(getActionBar(),
				getResources().getDrawable(R.drawable.actionbar_bg));

		if (savedInstanceState == null) {
			fragment = new ListViewDetailFragment();
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.action_bar_btn_like: {
			// show("中山市南区点赞");
			String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_LIKE + "other" + fragment.getOtherId());
			if(temp==null){
				like();
			}else{
				Long sign = Long.parseLong(temp);
				if((System.currentTimeMillis()-sign)>24*60*60*1000){
					like();
				}else{
					shortToastHandler("亲，你今天已经点过赞了哦！");
				}
			}
			break;
		}
		case R.id.action_bar_btn_back:{
			show("返回主界面");
			finish();
			break;
			}
		case R.id.action_bar_btn_share:{
			// show("中山市南区分享");
			showShare();
			break;
			}
		case R.id.action_bar_btn_mine:
		case R.id.action_bar_btn_mines:{
			show("中山市南区我的信息");
			mine();
			break;
			}
		default:
			break;
		}
	}
	
	public void mine(){
		String temp = readXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_USERS);
		if(!CONSTANT.NULL_STRING.equals(Tool.NVL(temp))){
			JsonObject json = new JsonParser().parse(temp).getAsJsonObject();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Users user = gson.fromJson(json, Users.class);
			if(CONSTANT.LOGIN_SIGN_OFF.equals(user.getLoginSign())){
				goActivityAndFinish(LoginActivity.class);
			} else {
				goActivityAndFinish(MineActivity.class);
			}
		} else {
			goActivityAndFinish(LoginActivity.class);
		}
	}
	
	public void show(String contents){
		Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
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
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 设置ActionBar的布局
	 * 
	 * @param layoutId
	 */
	public void setActionBarLayout(int layoutId) {
		ActionBar actionBar = getActionBar();
		if (null != actionBar) {
			// 去掉空白
			actionBar.setTitle("");
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setDisplayShowCustomEnabled(true);
			LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflator.inflate(layoutId, null);
			ActionBar.LayoutParams layout = new ActionBar.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			actionBar.setCustomView(v, layout);
		}
	}

	public void like(){
		String url = "/AppLike_addLikeAction.action";
		JsonObject json = new JsonObject();
		json.addProperty(CONSTANT.APP_GET_DATA, CONSTANT.LIKE_ADD);
		json.addProperty(CONSTANT.LIKE_ID, fragment.getOtherId());
		json.addProperty(CONSTANT.LIKE_TYPE, CONSTANT.LIKE_TYPE_OTHER);
		json.addProperty(CONSTANT.LIKE_SIGN, System.currentTimeMillis());
		
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
					String likeNum = json.get(CONSTANT.LIKE_NUM).getAsString();
					Long sign = json.get(CONSTANT.LIKE_SIGN).getAsLong();
					writeXML(CONSTANT.INFO_DATA, CONSTANT.INFO_DATA_LIKE + "other" + fragment.getOtherId(), sign+CONSTANT.NULL_STRING);
					fragment.setLikeNum(likeNum);
					shortToastHandler("点赞成功，当前点赞数为： " + likeNum);
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
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void showShare() {
		String icon = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + "/download/" + "userIcon.jpg";
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
		oks.setText(fragment.getOther().getInfo());
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(icon);// 确保SDcard下面存在此张图片/sdcard/test.jpg //fragment.getUrls().get(0)
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
